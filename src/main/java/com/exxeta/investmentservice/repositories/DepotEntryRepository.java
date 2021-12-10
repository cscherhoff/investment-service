package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.DepotEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepotEntryRepository  extends JpaRepository<DepotEntry, Long> {
    List<DepotEntry> findDepotEntriesByUserId(long userId);
    List<DepotEntry> findDepotEntriesByUserIdAndDepotNameAndSecurityName(long userId, String depotName, String securityName);
    List<DepotEntry> findDepotEntriesByUserIdAndDepotName(long userId, String depotName);
}
