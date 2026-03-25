package com.payflow.dto.request;

import com.payflow.entity.Account.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

}
