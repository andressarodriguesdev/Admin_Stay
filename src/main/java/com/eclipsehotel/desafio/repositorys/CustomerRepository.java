package com.eclipsehotel.desafio.repositorys;

import com.eclipsehotel.desafio.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
