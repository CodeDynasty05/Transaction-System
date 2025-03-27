package com.matrix.Transaction.mapper;

import com.matrix.Transaction.model.dto.CustomerAddRequestDTO;
import com.matrix.Transaction.model.dto.CustomerDTO;
import com.matrix.Transaction.model.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerAddRequestDTOToCustomer(CustomerAddRequestDTO customerAddRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer updateCustomer(CustomerAddRequestDTO customerAddRequestDTO,@MappingTarget Customer customer);
}
