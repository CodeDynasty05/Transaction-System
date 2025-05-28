package com.matrix.Transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.Transaction.model.dto.AccountDto;
import com.matrix.Transaction.model.entity.AccountStatus;
import com.matrix.Transaction.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto();
        accountDto.setAccountNumber("ACC123456");
        accountDto.setBalance(1000.0);
        accountDto.setAccountStatus(AccountStatus.ACTIVE);
    }

    @Test
    void getAccounts() throws Exception {
        when(accountService.getAccounts(anyLong(),false)).thenReturn(List.of(accountDto));

        mvc.perform(get("/account/10")
                        .param("onlyActive", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[0].accountStatus").value("ACTIVE"));

        verify(accountService, times(1)).getAccounts(eq(10L), eq(true));
    }

    @Test
    void createAccount() throws Exception {
        when(accountService.createAccount(10L)).thenReturn(accountDto);

        mvc.perform(post("/account/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));

        verify(accountService, times(1)).createAccount(10L);
    }

    @Test
    void changeStatus() throws Exception {
        accountDto.setAccountStatus(AccountStatus.BLOCKED);
        when(accountService.changeStatus("ACC123456")).thenReturn(accountDto);

        mvc.perform(put("/account/status/ACC123456")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.accountStatus").value("BLOCKED"));

        verify(accountService, times(1)).changeStatus("ACC123456");
    }

    @Test
    void deleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount("ACC123456");

        mvc.perform(delete("/account/ACC123456")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(accountService, times(1)).deleteAccount("ACC123456");
    }
}