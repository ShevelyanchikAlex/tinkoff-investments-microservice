package com.alex.shevelyanchik.tinkoffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class StockPricesDto {
    List<StockPriceDto> prices;
}
