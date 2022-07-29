package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.DepotEntryRepository;
import com.exxeta.investmentservice.service.profits.ProfitHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DividendHandler {

    private final DepotEntryRepository depotEntryRepository;
    private final ProfitHandler profitHandler;

    public DividendHandler(DepotEntryRepository depotEntryRepository,
        ProfitHandler profitHandler) {
        this.depotEntryRepository = depotEntryRepository;
        this.profitHandler = profitHandler;
    }

//    public void processDividend(Transaction transaction) {
//        List<DepotEntry> depotEntryList = depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndSecurityName(
//            transaction.getUserId(), transaction.getDepotName(), transaction.getSecurityName());
//
////        List<Profit> profitList = profitRepository.findProfitsByUserIdAndDepotNameAndSecurityName(transaction.getUserId(),
////            transaction.getDepotName(), transaction.getSecurityName());
//
//        if (depotEntryList.size() == 1) {
//            if (!depotEntryList.get(0).getNumber().equals(transaction.getNumber())) {
//                logger.error("The number of depotEntry (" + depotEntryList.get(0).getNumber() + ") is not the same as the transaction (" + transaction.getNumber() + ")");
//            }
//            DepotEntry depotEntry = null; //new DepotEntry(transaction.getUserId(), transaction.getDepotName(),
////                transaction.getSecurityName(), transaction.getNumber(), depotEntryList.get(0).getSinglePrice(), BigDecimal.ZERO);
//            BigDecimal dividendPerSecurity = transaction.getTotalPrice().divide(depotEntryList.get(0).getNumber(), 4, RoundingMode.HALF_UP)
//                .add(depotEntry.getSinglePrice());
////            transaction.setPrice(dividendPerSecurity);
//
//            //TODO: don't use method 'handleProfit', but the methods createProfit and saveProfit
////            profitHandler.handleProfit(transaction, depotEntry, profitList);
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of depot entries in the database for "
//                + "the user ID " + transaction.getUserId() + ", the depot with the name " + transaction.getDepotName()
//                + " and the security name " + transaction.getSecurityName() + " should be one, but was " + depotEntryList.size());
//        }
//    }

    public void processDividend(Transaction transaction) {
        DepotEntry depotEntry = getDepotEntryFromDatabase(transaction);
        if (depotEntry.getNumber().doubleValue()!=transaction.getNumber().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The number of depotEntry ("
                + depotEntry.getNumber() + ") is not the same as the transaction (" + transaction.getNumber() + ")");
        }
        DepotEntry depotEntryFromDividend = new DepotEntry(transaction.getUserId(), transaction.getDepotName(),
                transaction.getIsin(), transaction.getSecurity().getSecurityName(), transaction.getNumber(), depotEntry.getSinglePrice(), depotEntry.getCosts());
        BigDecimal transactionPrice = depotEntry.getSinglePrice().add(transaction.getTotalPrice().
            divide(transaction.getNumber(), 6, RoundingMode.HALF_UP));
        Transaction transactionFromDividend = new Transaction(transaction.getDepotName(), transaction.getDate(),
            transaction.getType(), transaction.getIsin(), transaction.getSecurity().getSecurityName(), transactionPrice, transaction.getNumber(),
            transaction.getExpenses(), transaction.getTotalPrice());
        transactionFromDividend.setUserId(transaction.getUserId());
        profitHandler.createProfit(transactionFromDividend, depotEntryFromDividend);
        profitHandler.saveProfit();
    }

    private DepotEntry getDepotEntryFromDatabase(Transaction transaction) {
        List<DepotEntry> depotEntryList = depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndIsin(
            transaction.getUserId(), transaction.getDepotName(), transaction.getIsin());

        if (depotEntryList.size() == 1) {
            return depotEntryList.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of depot entries in the database for "
                + "the user ID " + transaction.getUserId() + ", the depot with the name " + transaction.getDepotName()
                + " and the ISIN " + transaction.getIsin() + " must be one, but was " + depotEntryList.size());
        }
    }
}
