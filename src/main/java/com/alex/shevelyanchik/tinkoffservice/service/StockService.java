package com.alex.shevelyanchik.tinkoffservice.service;

import com.alex.shevelyanchik.tinkoffservice.dto.*;
import com.alex.shevelyanchik.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDto getStocksByTickers(TickersDto tickers);

    StockPricesDto getStockPrices(FigiesDto figiesDto);
}
