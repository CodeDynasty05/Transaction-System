package com.matrix.Transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.Transaction.model.dto.CustomerAddRequestDto;
import com.matrix.Transaction.model.dto.CustomerDto;
import com.matrix.Transaction.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    private CustomerDto customerDto;

    private CustomerAddRequestDto customerAddRequestDto;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setId(15L);
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setPhone("123456789");
        customerDto.setEmail("test@test.com");

        customerAddRequestDto = new CustomerAddRequestDto();
        customerAddRequestDto.setFirstName("John");
        customerAddRequestDto.setLastName("Doe");
        customerAddRequestDto.setPhone("123456789");
        customerAddRequestDto.setEmail("test@test.com");
    }

    @AfterEach
    void tearDown() {
        customerDto = null;
    }

    @Test
    void getAccount() throws Exception {
        when(customerService.getCustomer(anyLong())).thenReturn(customerDto);

        mvc.perform(get("/customer/{id}", customerDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andDo(print());

        verify(customerService, times(1)).getCustomer(anyLong());
    }

    @Test
    void addAccount() throws Exception {
        when(customerService.createCustomer(any(CustomerAddRequestDto.class))).thenReturn(customerDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerAddRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andDo(print());

        verify(customerService, times(1)).createCustomer(any(CustomerAddRequestDto.class));
    }

    @Test
    void updateCustomer() throws Exception {
        when(customerService.updateCustomer(eq(15L), any(CustomerAddRequestDto.class)))
                .thenReturn(customerDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/customers/15")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerAddRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.phone").value("123456789"));

        verify(customerService, times(1)).updateCustomer(eq(15L), any(CustomerAddRequestDto.class));
    }

    @Test
    void deleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(15L);

        mvc.perform(post("/users/delete/15")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomer(15L);
    }
}
