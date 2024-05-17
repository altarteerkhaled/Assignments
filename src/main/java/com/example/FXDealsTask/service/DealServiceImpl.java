package com.example.FXDealsTask.service;

import com.example.FXDealsTask.Exceptions.CurrencyNotFoundException;
import com.example.FXDealsTask.Exceptions.SameCurrencyException;
import com.example.FXDealsTask.model.FxDeals;
import com.example.FXDealsTask.repository.DealJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DealServiceImpl implements DealService {

    private final DealJpaRepository repository;

    @Autowired
    public DealServiceImpl(DealJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FxDeals> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<FxDeals> findById(String theId) {
        if (theId == null || theId.isEmpty()) {
            throw new CurrencyNotFoundException("Invalid deal id: " + theId);
        }

        Optional<FxDeals> deal = repository.findById(theId);
        if (deal.isEmpty()) {
            throw new CurrencyNotFoundException("deal not found: " + theId);
        }
        return deal;
    }

    @Override
    public void save(FxDeals theDeal) {
        if (theDeal == null) {
            throw new IllegalArgumentException("Deal cannot be null");
        }
        if (theDeal.getToCurrency().equals(theDeal.getFromCurrency())) {
            throw new SameCurrencyException("Deals can't be the same currency");
        }
        if (theDeal.getDealAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deal amount must be positive");
        }

        repository.save(theDeal); // Save the deal directly
    }
}
