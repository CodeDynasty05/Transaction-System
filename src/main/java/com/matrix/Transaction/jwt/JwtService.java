package com.matrix.Transaction.jwt;

import com.matrix.Transaction.model.entity.Customer;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String issueToken(Customer customer);

    Claims validateToken(String token);
}