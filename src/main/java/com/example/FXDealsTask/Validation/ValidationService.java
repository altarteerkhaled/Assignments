package com.example.FXDealsTask.Validation;

import com.example.FXDealsTask.model.FxDeals;
import org.springframework.validation.Errors;

public interface ValidationService {
    Errors validateFxDeal(FxDeals fxDeal);
}
