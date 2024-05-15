package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    State findByCode(String code);
}
