package com.example.FXDealsTask.controller;

import com.example.FXDealsTask.Exceptions.CurrencyNotFoundException;
import com.example.FXDealsTask.Exceptions.DuplicateDealException;
import com.example.FXDealsTask.Exceptions.SameCurrencyException;
import com.example.FXDealsTask.Validation.ValidationService;
import com.example.FXDealsTask.model.FxDeals;
import com.example.FXDealsTask.service.DealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    private final DealService dealService;
    private final ValidationService validationService;

    @Autowired
    public DealController(DealService dealService, ValidationService validationService) {
        this.dealService = dealService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<String> createDeal(@RequestBody FxDeals deal) {
        logger.info("Received deal: {}", deal);

        Errors validationResult = validationService.validateFxDeal(deal);

        // If there are validation errors, return 402 Payment Required status code
        if (validationResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (FieldError error : validationResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            logger.warn("Validation failed: {}", errorMessage.toString());
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(errorMessage.toString());
        }

        // Save the deal if validation passes
        try {
            dealService.save(deal);
            logger.info("Deal processed successfully: {}", deal);
            return ResponseEntity.ok("Deal processed successfully");
        } catch (CurrencyNotFoundException e) {
            logger.error("Currency not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SameCurrencyException e) {
            logger.error("Same currency exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DuplicateDealException e) {
            logger.error("Duplicate deal exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
