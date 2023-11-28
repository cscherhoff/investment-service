package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.files.InvestmentExporter;
import com.exxeta.investmentservice.repositories.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestService {

    private final DepotEntryRepository depotEntryRepository = mock(DepotEntryRepository.class);
    private final ProfitRepository profitRepository = mock(ProfitRepository.class);
    private final SecurityRepository securityRepository = mock(SecurityRepository.class);
    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final AccountMovementRepository accountMovementRepository = mock(AccountMovementRepository.class);
    private final InvestmentRepository investmentRepository = mock(InvestmentRepository.class);
//    private final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
    private final InvestmentExporter investmentExporter = mock(InvestmentExporter.class);

    private final InvestmentService investmentService = new InvestmentService(depotEntryRepository, profitRepository, securityRepository, transactionRepository, accountMovementRepository, investmentRepository, investmentExporter);

    private final String userId = "6";
    private final String depotName = "ING Depot";
    private final String securityName = "BMW";

    @Test
    public void getAllDepotEntriesFromDatabase() {
        DepotEntry depotEntry = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(20.5),
            BigDecimal.valueOf(15.87), BigDecimal.valueOf(4.5));

        when(depotEntryRepository.findDepotEntriesByUserId(userId)).thenReturn(List.of(depotEntry, depotEntry));

        List<DepotEntry> actual = investmentService.getAllDepotEntries(userId);
        List<DepotEntry> expected = List.of(depotEntry, depotEntry);

        assertEquals(expected, actual,"The two lists are not the same!");
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserId(userId);
        verifyNoMoreInteractions(depotEntryRepository);
        verifyNoInteractions(profitRepository);
    }

    @Test
    public void getAllDepotEntriesFromDatabaseByUsingDepotName() {
        DepotEntry depotEntry = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(20.5),
            BigDecimal.valueOf(15.87), BigDecimal.valueOf(4.5));

        when(depotEntryRepository.findDepotEntriesByUserIdAndDepotName(userId, depotName)).thenReturn(List.of(depotEntry, depotEntry));

        List<DepotEntry> actual = investmentService.getAllDepotEntries(userId, depotName);
        List<DepotEntry> expected = List.of(depotEntry, depotEntry);

        assertEquals(expected, actual,"The two lists are not the same!");
        verify(depotEntryRepository, times(1)).findDepotEntriesByUserIdAndDepotName(userId, depotName);
        verifyNoMoreInteractions(depotEntryRepository);
        verifyNoInteractions(profitRepository);
    }

    @Test
    public void getAllProfitsFromDatabase() {
        Profit profitFromDatabase = new Profit(userId, depotName, securityName,
            BigDecimal.valueOf(1193.5000).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(1188.50000000).setScale(8, RoundingMode.HALF_UP),
            BigDecimal.valueOf(2.3870).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(2.3770).setScale(4, RoundingMode.HALF_UP));

        when(profitRepository.findProfitsByUserId(userId)).thenReturn(List.of(profitFromDatabase, profitFromDatabase));

        List<Profit> actual = investmentService.getAllProfits(userId);
        List<Profit> expected = List.of(profitFromDatabase, profitFromDatabase);

        assertEquals(expected, actual, "The two lists are not the same!");
        verify(profitRepository, times(1)).findProfitsByUserId(userId);
        verifyNoMoreInteractions(profitRepository);
        verifyNoInteractions(depotEntryRepository);
    }

}
