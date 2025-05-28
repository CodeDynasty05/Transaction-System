package com.matrix.Transaction;

import com.matrix.Transaction.jwt.JwtService;
import com.matrix.Transaction.model.dto.CustomerDto;
import com.matrix.Transaction.model.entity.Authority;
import com.matrix.Transaction.model.entity.Customer;
import com.matrix.Transaction.repository.CustomerRepository;
import com.matrix.Transaction.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class TransactionApplication implements CommandLineRunner {

	private final CustomerRepository customerRepository;
	private final JwtService jwtService;

	private final BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(TransactionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Customer user=customerRepository.findById(8L).get();
//		user.setAccountNonExpired(true);
//		user.setAccountNonLocked(true);
//		user.setCredentialsNonExpired(true);
//		user.setEnabled(true);
//
//		Authority authority = new Authority();
//		authority.setAuthority("USER");
//		authority.setCustomer(user);
//
//		user.setAuthorities(Set.of(authority));
//		customerRepository.save(user);

		Customer user=customerRepository.findByUsername("nazi").get();
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		customerRepository.save(user);
		String token = jwtService.issueToken(user);
		log.info(token);
	}
}
