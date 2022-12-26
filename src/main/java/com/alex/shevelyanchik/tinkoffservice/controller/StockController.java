package com.alex.shevelyanchik.tinkoffservice.controller;

import com.alex.shevelyanchik.tinkoffservice.dto.FigiesDto;
import com.alex.shevelyanchik.tinkoffservice.dto.StockPricesDto;
import com.alex.shevelyanchik.tinkoffservice.dto.StocksDto;
import com.alex.shevelyanchik.tinkoffservice.dto.TickersDto;
import com.alex.shevelyanchik.tinkoffservice.model.Stock;
import com.alex.shevelyanchik.tinkoffservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto) {
        return stockService.getStocksByTickers(tickersDto);
    }

    @PostMapping("/prices")
    public StockPricesDto getPrices(@RequestBody FigiesDto figiesDto) {
        return stockService.getStockPrices(figiesDto);
    }
}
