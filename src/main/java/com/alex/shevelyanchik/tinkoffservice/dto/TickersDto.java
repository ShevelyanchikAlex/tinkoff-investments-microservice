package com.alex.shevelyanchik.tinkoffservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TickersDto {
    private List<String> tickers;
}
