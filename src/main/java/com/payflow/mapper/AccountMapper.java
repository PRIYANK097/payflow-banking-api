package com.payflow.mapper;

import com.payflow.dto.response.AccountResponse;
import com.payflow.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses = {UserMapper.class})
public interface AccountMapper {
    AccountResponse toResponse(Account account);
}
