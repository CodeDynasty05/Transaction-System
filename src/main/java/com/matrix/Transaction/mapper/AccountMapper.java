package com.matrix.Transaction.mapper;

import com.matrix.Transaction.model.dto.AccountDTO;
import com.matrix.Transaction.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO accountToAccountDTO(Account account);

    @Named("mapAccount")
    default Account mapAccount(String accountNumber) {
        return AccountMapperHelper.mapAccount(accountNumber);
    }
}
