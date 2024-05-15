package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByCustomer_id(Long customerID);
    Integer deleteByCustomer_id(Long customerId);
}
