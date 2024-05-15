package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser_id(Long username);

    @Modifying
    @Transactional
    @Query(value="update Customer customer  set  customer.state_id=:stateID  where customer.id =:customerId", nativeQuery = true)
    Integer  updateStateId(Long customerId, Long stateID);
}
