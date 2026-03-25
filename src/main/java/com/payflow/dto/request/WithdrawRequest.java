package com.payflow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    @NotNull(message = "Account Id is required")
    private Long accountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0" , message = "Minimum withdrawal amount is 1")
    private BigDecimal amount;

    private String description;

}
