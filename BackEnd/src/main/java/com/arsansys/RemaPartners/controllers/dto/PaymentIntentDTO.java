package com.arsansys.RemaPartners.controllers.dto;

import lombok.Data;

@Data
public class PaymentIntentDTO {
    private enum Currency {
        EUR, USD
    }

    private String description;
    private Integer amount;
    private Currency currency;
}
