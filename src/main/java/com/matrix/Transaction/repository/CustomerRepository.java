package com.matrix.Transaction.repository;

import com.matrix.Transaction.model.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<Customer> findByUsername(String username);
}
