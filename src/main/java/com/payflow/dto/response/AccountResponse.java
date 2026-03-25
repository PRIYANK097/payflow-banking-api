package com.payflow.dto.response;

import com.payflow.entity.Account.AccountStatus;
import com.payflow.entity.Account.AccountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private UserResponse user ;

}
