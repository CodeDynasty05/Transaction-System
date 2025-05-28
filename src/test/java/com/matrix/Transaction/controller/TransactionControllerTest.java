package com.matrix.Transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.Transaction.model.dto.TransactionAddRequestDto;
import com.matrix.Transaction.model.dto.TransactionDto;
import com.matrix.Transaction.model.entity.TransactionStatus;
import com.matrix.Transaction.model.entity.TransactionType;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    private TransactionDto transactionDto;
    private TransactionAddRequestDto transactionAddRequestDto;

    @BeforeEach
    void setUp() {
        Account account = new Account();
        account.setAccountNumber("ACC001");

        transactionDto = new TransactionDto();
        transactionDto.setId(1L);
        transactionDto.setAmount(200.0);
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionDto.setAccount(account);

        transactionAddRequestDto = new TransactionAddRequestDto();
        transactionAddRequestDto.setAmount(200.0);
        transactionAddRequestDto.setTransactionType(TransactionType.DEPOSIT);
        transactionAddRequestDto.setAccountNumber("ACC001");
    }

    @Test
    void getAllTransactions() throws Exception {
        when(transactionService.getTransactions()).thenReturn(List.of(transactionDto));

        mvc.perform(get("/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(200.0))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[0].transactionStatus").value("SUCCESS"));

        verify(transactionService, times(1)).getTransactions();
    }

    @Test
    void getAccountTransactions() throws Exception {
        when(transactionService.getAccountTransactions("ACC001")).thenReturn(List.of(transactionDto));

        mvc.perform(get("/transactions/ACC001")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(200.0))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"));

        verify(transactionService, times(1)).getAccountTransactions("ACC001");
    }

    @Test
    void createTransaction() throws Exception {
        when(transactionService.addTransaction(any(TransactionAddRequestDto.class))).thenReturn(transactionDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionAddRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionStatus").value("SUCCESS"));

        verify(transactionService, times(1)).addTransaction(any(TransactionAddRequestDto.class));
    }

    @Test
    void changeTransactionStatus() throws Exception {
        transactionDto.setTransactionStatus(TransactionStatus.PENDING);
        when(transactionService.changePaymentStatus(1L, TransactionStatus.SUCCESS)).thenReturn(transactionDto);

        mvc.perform(put("/transactions/1")
                        .param("transactionStatus", "SUCCESS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionStatus").value("SUCCESS"));

        verify(transactionService, times(1)).changePaymentStatus(1L, TransactionStatus.SUCCESS);
    }
}