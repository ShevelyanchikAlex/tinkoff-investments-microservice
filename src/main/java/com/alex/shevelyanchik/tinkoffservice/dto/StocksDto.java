package com.alex.shevelyanchik.tinkoffservice.dto;

import com.alex.shevelyanchik.tinkoffservice.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StocksDto {
    private List<Stock> stocks;
}
