package com.example.FXDealsTask.Validation;

import com.example.FXDealsTask.Exceptions.DuplicateDealException;
import com.example.FXDealsTask.model.FxDeals;
import com.example.FXDealsTask.repository.DealJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final FxDealsValidator fxDealsValidator;
    private final DealJpaRepository dealJpaRepository;

    @Autowired
    public ValidationServiceImpl(FxDealsValidator fxDealsValidator, DealJpaRepository dealJpaRepository) {
        this.fxDealsValidator = fxDealsValidator;
        this.dealJpaRepository = dealJpaRepository;
    }

    @Override
    public Errors validateFxDeal(FxDeals deal) {
        Errors errors = new BeanPropertyBindingResult(deal, "fxDeals");
        fxDealsValidator.validate(deal, errors);

        if (dealJpaRepository.findById(deal.getDealUniqueId()).isPresent()) {
            throw new DuplicateDealException("Deal with ID " + deal.getDealUniqueId() + " already exists");
        }

        return errors;
    }
}
