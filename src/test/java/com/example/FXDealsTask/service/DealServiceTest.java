package com.example.FXDealsTask.service;

import com.example.FXDealsTask.Exceptions.CurrencyNotFoundException;
import com.example.FXDealsTask.Exceptions.SameCurrencyException;
import com.example.FXDealsTask.model.FxDeals;
import com.example.FXDealsTask.repository.DealJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class DealServiceTest {

    @Mock
    private DealJpaRepository repository;

    @InjectMocks
    private DealServiceImpl dealService;

    private FxDeals validDeal;

    @Before
    public void setUp() {
        validDeal = new FxDeals("1", "USD", "EUR", Timestamp.valueOf("2024-05-17 10:30:00"), BigDecimal.valueOf(1000.50));
    }

    @Test
    public void save_ValidDeal_ShouldSaveSuccessfully() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        dealService.save(validDeal);

        verify(repository, times(1)).save(validDeal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_NullDeal_ShouldThrowException() {
        dealService.save(null);
    }

    @Test(expected = SameCurrencyException.class)
    public void save_SameCurrency_ShouldThrowException() {
        validDeal.setToCurrency("USD");

        dealService.save(validDeal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_InvalidDealAmount_ShouldThrowException() {
        validDeal.setDealAmount(BigDecimal.valueOf(-1000.50));

        dealService.save(validDeal);
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void findById_DealNotFound_ShouldThrowException() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        dealService.findById("1");
    }
}
