package com.matrix.Transaction.mapper;

import com.matrix.Transaction.model.dto.CustomerAddRequestDto;
import com.matrix.Transaction.model.dto.CustomerDto;
import com.matrix.Transaction.model.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto customerToCustomerDTO(Customer customer);

    Customer customerAddRequestDTOToCustomer(CustomerAddRequestDto customerAddRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer updateCustomer(CustomerAddRequestDto customerAddRequestDTO, @MappingTarget Customer customer);
}
