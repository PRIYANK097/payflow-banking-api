package com.payflow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "Sender account Id is required")
    private Long senderAccountId;

    @NotNull(message = "Receiver account Id is required")
    private Long receiverAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0" , message = "Minimum trasfer amount is 1")
    private BigDecimal amount;

    private String description;
}
