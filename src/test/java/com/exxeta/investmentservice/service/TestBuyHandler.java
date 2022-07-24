package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.DepotEntryRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class TestBuyHandler {

    private final DepotEntryRepository depotEntryRepository = mock(DepotEntryRepository.class);
    private final  BuyHandler buyHandler = new BuyHandler(depotEntryRepository);

    @Test
    public void testProcessBuyWithNewDepotEntriesInTheDatabase() {
        long userId = 7;
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Buy", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);
        DepotEntry createdDepotEntry = new DepotEntry(userId, depotName, securityName, transaction.getNumber(),
            transaction.getPrice(), transaction.getExpenses());

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(Collections.emptyList());

        buyHandler.processBuy(transaction);

        verify(depotEntryRepository, times(1))
            .findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName);
        verify(depotEntryRepository, times(1)).save(createdDepotEntry);
        verifyNoMoreInteractions(depotEntryRepository);
    }

    @Test
    public void testProcessBuyWithOneDepotEntryInTheDatabase() {
        long userId = 7;
        String depotName = "ING Depot";
        String securityName = "BMW";

        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Buy", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);

        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(120.5),
            BigDecimal.valueOf(10.84), BigDecimal.valueOf(1.5));
        DepotEntry updatedDepotEntry = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(125.0),
            BigDecimal.valueOf(11.669080000).setScale(9, RoundingMode.HALF_UP), BigDecimal.valueOf(3.0));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(depotEntryFromDatabase));

        buyHandler.processBuy(transaction);

        verify(depotEntryRepository, times(1))
            .findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName);
        verify(depotEntryRepository, times(1)).save(updatedDepotEntry);
        verifyNoMoreInteractions(depotEntryRepository);
    }

    @Test
    public void testProcessBuyWithTwoDepotEntriesInTheDatabase() {
        long userId = 7;
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Buy", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);
        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(120.5),
            BigDecimal.valueOf(10.84), BigDecimal.valueOf(1.5));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(depotEntryFromDatabase, depotEntryFromDatabase));

//        try {
//            buyHandler.processBuy(transaction);
//        } catch (IllegalStateException ignore) {
//
//        }

        assertThatExceptionOfType(IllegalStateException.class).
            isThrownBy(() -> buyHandler.processBuy(transaction));
        verify(depotEntryRepository, times(1))
            .findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName);
        verifyNoMoreInteractions(depotEntryRepository);
    }
}
