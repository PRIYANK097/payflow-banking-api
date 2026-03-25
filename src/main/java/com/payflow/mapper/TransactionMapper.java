package com.payflow.mapper;

import com.payflow.dto.response.TransactionResponse;
import com.payflow.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses = {AccountMapper.class})
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);
}
