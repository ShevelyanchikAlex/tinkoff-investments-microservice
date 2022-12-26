package com.alex.shevelyanchik.tinkoffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class StockPriceDto {
    String figi;
    BigDecimal price;
}
