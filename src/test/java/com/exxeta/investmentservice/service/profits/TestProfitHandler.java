package com.exxeta.investmentservice.service.profits;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.ProfitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class TestProfitHandler {

    private final ProfitRepository profitRepository = mock(ProfitRepository.class);
    private final ProfitCalculator profitCalculator = new ProfitCalculator();
    private final ProfitHandler profitHandler = new ProfitHandler(profitRepository, profitCalculator);

    private final long userId = 6;

    @Test
    public void testCreateNewProfit() {
//        long userId = 6;
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW",
            BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.0), BigDecimal.valueOf(1.5), BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);

        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        Profit profitToSaveToDatabase = new Profit(userId, depotName, securityName,
            BigDecimal.valueOf(596.7500).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(594.2500).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(2.3870).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(2.3770).setScale(4, RoundingMode.HALF_UP));

        when(profitRepository.findProfitsByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(Collections.emptyList());
        when(profitRepository.save(profitToSaveToDatabase)).thenReturn(null);

        profitHandler.createProfit(transaction, depotEntryFromDatabase);
        profitHandler.saveProfit();

        verify(profitRepository, times(1)).findProfitsByUserIdAndDepotNameAndIsin(userId,
            depotName, securityName);
        verify(profitRepository, times(1)).save(profitToSaveToDatabase);
        verifyNoMoreInteractions(profitRepository);
    }

    @Test
    public void testUpdateExistingProfit() {
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW",
            BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.0), BigDecimal.valueOf(1.5), BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);

        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        Profit profitFromDatabase = new Profit(userId, depotName, securityName,
            BigDecimal.valueOf(596.7500), BigDecimal.valueOf(594.2500), BigDecimal.valueOf(2.3870), BigDecimal.valueOf(2.3770));

        Profit profitToSaveToDatabase = new Profit(userId, depotName, securityName,
            BigDecimal.valueOf(1193.5000).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(1188.50000000).setScale(8, RoundingMode.HALF_UP),
            BigDecimal.valueOf(2.3870).setScale(4, RoundingMode.HALF_UP),
            BigDecimal.valueOf(2.3770).setScale(4, RoundingMode.HALF_UP));

        when(profitRepository.findProfitsByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(profitFromDatabase));

        when(profitRepository.save(profitToSaveToDatabase)).thenReturn(null);

        profitHandler.createProfit(transaction, depotEntryFromDatabase);
        profitHandler.saveProfit();

        verify(profitRepository, times(1)).findProfitsByUserIdAndDepotNameAndIsin(userId,
            depotName, securityName);
        verify(profitRepository, times(1)).save(profitToSaveToDatabase);
        verifyNoMoreInteractions(profitRepository);
    }

    @Test
    public void testFindMoreThanOneProfitInDatabaseError() {
        long userId = 6;
        String depotName = "ING Depot";
        String securityName = "BMW";
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW",
            BigDecimal.valueOf(33.87), BigDecimal.valueOf(25.0), BigDecimal.valueOf(1.5), BigDecimal.valueOf(153.92));
        transaction.setUserId(userId);

        DepotEntry depotEntryFromDatabase = new DepotEntry(userId, depotName, securityName, BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(10.0), BigDecimal.valueOf(1.0));

        Profit profitFromDatabase = new Profit(userId, depotName, securityName,
            BigDecimal.valueOf(596.7500), BigDecimal.valueOf(594.2500), BigDecimal.valueOf(2.3870), BigDecimal.valueOf(2.3770));

        when(profitRepository.findProfitsByUserIdAndDepotNameAndIsin(userId, depotName, securityName))
            .thenReturn(List.of(profitFromDatabase, profitFromDatabase));

        assertThatExceptionOfType(ResponseStatusException.class).
            isThrownBy(() -> profitHandler.createProfit(transaction, depotEntryFromDatabase))
            .withMessage("400 BAD_REQUEST \"The number of profits in the database for the user ID " + userId + ", "
                + "the depot with the name " + depotName + " and the ISIN " + securityName
                + " should be zero or one, but was 2\"");
    }
}
