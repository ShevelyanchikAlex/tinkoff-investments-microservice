package com.alex.shevelyanchik.tinkoffservice.service.impl;

import com.alex.shevelyanchik.tinkoffservice.dto.*;
import com.alex.shevelyanchik.tinkoffservice.exception.StockNotFoundException;
import com.alex.shevelyanchik.tinkoffservice.model.Currency;
import com.alex.shevelyanchik.tinkoffservice.model.Stock;
import com.alex.shevelyanchik.tinkoffservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentByTicker(String ticker) {
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        var cf = getMarketInstrumentByTicker(ticker);
        var marketInstrumentList = cf.join().getInstruments();
        if (marketInstrumentList.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found.", ticker));
        }
        var item = marketInstrumentList.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINKOFF"
        );
    }

    @Override
    public StocksDto getStocksByTickers(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstrument = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstrument.add(getMarketInstrumentByTicker(ticker)));
        List<Stock> stocks = marketInstrument.stream()
                .map(CompletableFuture::join)
                .map(mi -> {
                    if (!mi.getInstruments().isEmpty()) {
                        return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                ))
                .collect(Collectors.toList());

        return new StocksDto(stocks);
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        return openApi.getMarketContext().getMarketOrderbook(figi, 0);
    }

    @Override
    public StockPricesDto getStockPrices(FigiesDto figiesDto) {
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        var pricesList = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(orderBook -> orderBook.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(orderBook -> new StockPriceDto(
                        orderBook.getFigi(),
                        orderBook.getLastPrice()
                ))
                .collect(Collectors.toList());
        return new StockPricesDto(pricesList);
    }
}
