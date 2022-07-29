package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<Security, String> {
    List<Security> findAllByUserId(long userId);
    Optional<Security> findByIsin(String isin);


}
