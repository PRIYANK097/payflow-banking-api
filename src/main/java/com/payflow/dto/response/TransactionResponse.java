package com.payflow.dto.response;

import com.payflow.entity.Transaction.TransactionStatus;
import com.payflow.entity.Transaction.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
    private AccountResponse senderAccount;
    private AccountResponse receiverAccount;
}
