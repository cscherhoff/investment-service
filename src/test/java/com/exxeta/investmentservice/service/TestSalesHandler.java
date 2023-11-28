package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.DepotEntryRepository;
import com.exxeta.investmentservice.service.profits.ProfitHandler;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class TestSalesHandler {

    private final DepotEntryRepository depotEntryRepository = mock(DepotEntryRepository.class);
    private final ProfitHandler profitHandler = mock(ProfitHandler.class);
    private final SalesHandler salesHandler = new SalesHandler(depotEntryRepository, profitHandler);

    @Test
    public void testProcessSale() {
        String userId = "6";
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);

        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        DepotEntry updatedDepotEntry = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(20.5),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(0.8200000).setScale(7, RoundingMode.HALF_UP));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(depotEntryFromDatabase));
        doNothing().when(profitHandler).createProfit(transaction, depotEntryFromDatabase);
        doNothing().when(profitHandler).saveProfit();
        when(depotEntryRepository.save(updatedDepotEntry)).thenReturn(null);

        salesHandler.processSale(transaction);
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserIdAndDepotNameAndIsin(
            userId, depotName, securityName);
        verify(depotEntryRepository, times(1)).save(updatedDepotEntry);
        verify(profitHandler, times(1)).createProfit(transaction, depotEntryFromDatabase);
        verify(profitHandler, times(1)).saveProfit();
        verifyNoMoreInteractions(depotEntryRepository, profitHandler);
    }

    @Test
    public void testSellEverythingOfOneSecurity() {
        String userId = "6";
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.0), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);
        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));
//        DepotEntry updatedDepotEntry = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(0.0),
//            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(depotEntryFromDatabase));
        doNothing().when(profitHandler).createProfit(transaction, depotEntryFromDatabase);
        doNothing().when(profitHandler).saveProfit();
        doNothing().when(depotEntryRepository).delete(depotEntryFromDatabase);

        salesHandler.processSale(transaction);
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserIdAndDepotNameAndIsin(
            userId, depotName, securityName);
        verify(depotEntryRepository, times(1)).delete(depotEntryFromDatabase);
        verify(profitHandler, times(1)).createProfit(transaction, depotEntryFromDatabase);
        verify(profitHandler, times(1)).saveProfit();
        verifyNoMoreInteractions(depotEntryRepository, profitHandler);
    }

    @Test
    public void testSellMoreThaThereIsInTheDatabase() {
        String userId = "6";
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.3), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);
        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(depotEntryFromDatabase));

        assertThatExceptionOfType(ResponseStatusException.class).
            isThrownBy(() -> salesHandler.processSale(transaction)).withMessage("400 BAD_REQUEST \"It is not possible "
            + "to sell more securities (25.3) than there are in the database (25.0)!\"");
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserIdAndDepotNameAndIsin(
            userId, depotName, securityName);
        verify(profitHandler, times(1)).createProfit(transaction, depotEntryFromDatabase);
        verifyNoMoreInteractions(depotEntryRepository, profitHandler);
    }

    @Test void testSellSomethingThatIsNotInTheDatabase() {
        String userId = "6";
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.0), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(Collections.emptyList());

        assertThatExceptionOfType(ResponseStatusException.class).
            isThrownBy(() -> salesHandler.processSale(transaction)).withMessage("400 BAD_REQUEST \"The number of depot entries in the database for "
            + "the user ID 6, the depot with the name ING Depot and the ISIN BMW must be one, but was 0\"");
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserIdAndDepotNameAndIsin(
            userId, depotName, securityName);
        verifyNoMoreInteractions(depotEntryRepository);
        verifyNoInteractions(profitHandler);
    }

    @Test void testSGetMoreThanOneDepotEntriesFormTheDatabase() {
        String userId = "6";
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.0), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);
        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(depotEntryFromDatabase, depotEntryFromDatabase));

        assertThatExceptionOfType(ResponseStatusException.class).
            isThrownBy(() -> salesHandler.processSale(transaction)).withMessage("400 BAD_REQUEST \"The number of depot entries in the database for "
            + "the user ID 6, the depot with the name ING Depot and the ISIN BMW must be one, but was 2\"");
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserIdAndDepotNameAndIsin(
            userId, depotName, securityName);
        verifyNoMoreInteractions(depotEntryRepository);
        verifyNoInteractions(profitHandler);
    }
}
