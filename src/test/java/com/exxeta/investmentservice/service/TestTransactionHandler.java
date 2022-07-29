package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.SecurityRepository;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class TestTransactionHandler {

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final BuyHandler buyHandler = mock(BuyHandler.class);
    private final SalesHandler salesHandler = mock(SalesHandler.class);
    private final DividendHandler dividendHandler = mock(DividendHandler.class);
    private final SecurityRepository securityRepository = mock(SecurityRepository.class);

    private final TransactionHandler transactionHandler = new TransactionHandler(transactionRepository, buyHandler,
        salesHandler, dividendHandler, securityRepository);

    @Test
    public void testHandleTransactionBuy() {
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Buy", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));

        doNothing().when(buyHandler).processBuy(transaction);
        when(transactionRepository.save(transaction)).thenReturn(null);

        transactionHandler.handleTransaction(transaction);

        verify(transactionRepository, times(1)).save(transaction);
        verify(buyHandler, times(1)).processBuy(transaction);

        verifyNoMoreInteractions(buyHandler, transactionRepository);
        verifyNoInteractions(salesHandler, dividendHandler);
    }

    @Test
    public void testHandleTransactionSell() {
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));

        doNothing().when(salesHandler).processSale(transaction);
        when(transactionRepository.save(transaction)).thenReturn(null);

        transactionHandler.handleTransaction(transaction);

        verify(transactionRepository, times(1)).save(transaction);
        verify(salesHandler, times(1)).processSale(transaction);

        verifyNoMoreInteractions(salesHandler, transactionRepository);
        verifyNoInteractions(buyHandler, dividendHandler);
    }

    @Test
    public void testHandleTransactionDividend() {
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Dividend", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));

        doNothing().when(dividendHandler).processDividend(transaction);
        when(transactionRepository.save(transaction)).thenReturn(null);

        transactionHandler.handleTransaction(transaction);

        verify(transactionRepository, times(1)).save(transaction);
        verify(dividendHandler, times(1)).processDividend(transaction);

        verifyNoMoreInteractions(dividendHandler, transactionRepository);
        verifyNoInteractions(salesHandler, buyHandler);
    }

    @ParameterizedTest
    @CsvSource({
        "invalid Type",
        "another invalid type",
        "costs",
        "nothing",
    })
    public void testHandleTransactionInvalidType(String type) {
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            type, "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));

        when(transactionRepository.save(transaction)).thenReturn(null);

        transactionHandler.handleTransaction(transaction);

        verify(transactionRepository, times(1)).save(transaction);
        verifyNoMoreInteractions(transactionRepository);
        verifyNoInteractions(salesHandler, buyHandler, dividendHandler);
    }

    @Test
    @Disabled
    public void testErrorHandling() {
        Transaction transaction = new Transaction("ING Depot", LocalDate.of(2021, 7, 26),
            "Sell", "BMW", BigDecimal.valueOf(33.87), BigDecimal.valueOf(4.5), BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(153.92));

        Mockito.doThrow(ArithmeticException.class).when(salesHandler).processSale(transaction);

        transactionHandler.handleTransaction(transaction);

//        verify(transactionRepository, times(1)).save(transaction);
        verify(salesHandler, times(1)).processSale(transaction);

        verifyNoMoreInteractions(salesHandler, transactionRepository);
        verifyNoInteractions(buyHandler, dividendHandler);

        assertThatExceptionOfType(ArithmeticException.class).
            isThrownBy(() -> salesHandler.processSale(transaction));
    }
}
