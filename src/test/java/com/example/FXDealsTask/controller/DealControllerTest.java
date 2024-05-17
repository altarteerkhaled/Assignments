package com.example.FXDealsTask.controller;

import com.example.FXDealsTask.Exceptions.CurrencyNotFoundException;
import com.example.FXDealsTask.Exceptions.DuplicateDealException;
import com.example.FXDealsTask.Exceptions.SameCurrencyException;
import com.example.FXDealsTask.Validation.ValidationService;
import com.example.FXDealsTask.model.FxDeals;
import com.example.FXDealsTask.service.DealService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DealControllerTest {

    @Mock
    private ValidationService validationService;

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    private FxDeals validDeal;

    @Before
    public void setUp() {
        validDeal = new FxDeals("123456", "USD", "EUR", Timestamp.valueOf("2024-05-17 10:30:00"), BigDecimal.valueOf(1000.50));
    }

    @Test
    public void createDeal_ValidDeal_ShouldReturnOk() {
        BindingResult bindingResult = new BeanPropertyBindingResult(validDeal, "fxDeals");

        when(validationService.validateFxDeal(validDeal)).thenReturn(bindingResult);

        ResponseEntity<String> response = dealController.createDeal(validDeal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deal processed successfully", response.getBody());

        verify(dealService, times(1)).save(validDeal);
    }

    @Test
    public void createDeal_InvalidDeal_ShouldReturn402() {
        BindingResult bindingResult = new BeanPropertyBindingResult(validDeal, "fxDeals");
        bindingResult.rejectValue("dealUniqueId", "NotNull", "Deal Unique Id is missing.");

        when(validationService.validateFxDeal(validDeal)).thenReturn(bindingResult);

        ResponseEntity<String> response = dealController.createDeal(validDeal);

        assertEquals(HttpStatus.PAYMENT_REQUIRED, response.getStatusCode());
        assertEquals("Validation failed: Deal Unique Id is missing.; ", response.getBody());

        verify(dealService, times(0)).save(validDeal);
    }

    @Test
    public void createDeal_CurrencyNotFoundException_ShouldReturn404() {
        BindingResult bindingResult = new BeanPropertyBindingResult(validDeal, "fxDeals");

        when(validationService.validateFxDeal(validDeal)).thenReturn(bindingResult);
        doThrow(new CurrencyNotFoundException("Currency not found")).when(dealService).save(validDeal);

        ResponseEntity<String> response = dealController.createDeal(validDeal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Currency not found", response.getBody());
    }

    @Test
    public void createDeal_SameCurrencyException_ShouldReturn409() {
        validDeal.setToCurrency("USD");
        BindingResult bindingResult = new BeanPropertyBindingResult(validDeal, "fxDeals");

        when(validationService.validateFxDeal(validDeal)).thenReturn(bindingResult);
        doThrow(new SameCurrencyException("Deals can't be the same currency")).when(dealService).save(validDeal);

        ResponseEntity<String> response = dealController.createDeal(validDeal);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Deals can't be the same currency", response.getBody());
    }

    @Test
    public void createDeal_DuplicateDealException_ShouldReturn409() {
        BindingResult bindingResult = new BeanPropertyBindingResult(validDeal, "fxDeals");

        when(validationService.validateFxDeal(validDeal)).thenReturn(bindingResult);
        doThrow(new DuplicateDealException("Deal with ID 123456 already exists")).when(dealService).save(validDeal);

        ResponseEntity<String> response = dealController.createDeal(validDeal);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Deal with ID 123456 already exists", response.getBody());
    }
}
