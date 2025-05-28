package com.matrix.Transaction.mapper;

import com.matrix.Transaction.model.dto.TransactionAddRequestDto;
import com.matrix.Transaction.model.dto.TransactionDto;
import com.matrix.Transaction.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AccountMapper.class })
public interface TransactionMapper {
    TransactionDto transactionToTransactionDTO(Transaction transaction);

    @Mapping(target = "account", source = "accountNumber", qualifiedByName = "mapAccount")
    Transaction transactionAddRequestDTOtoTransaction(TransactionAddRequestDto transactionAddRequestDTO);
}
