package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    boolean existsByUserName(String username);

}
