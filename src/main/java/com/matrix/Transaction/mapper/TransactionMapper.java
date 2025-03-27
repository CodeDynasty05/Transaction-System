package com.matrix.Transaction.mapper;

import com.matrix.Transaction.model.dto.TransactionAddRequestDTO;
import com.matrix.Transaction.model.dto.TransactionDTO;
import com.matrix.Transaction.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AccountMapper.class })
public interface TransactionMapper {
    TransactionDTO transactionToTransactionDTO(Transaction transaction);

    @Mapping(target = "account", source = "accountNumber", qualifiedByName = "mapAccount")
    Transaction transactionAddRequestDTOtoTransaction(TransactionAddRequestDTO transactionAddRequestDTO);
}
