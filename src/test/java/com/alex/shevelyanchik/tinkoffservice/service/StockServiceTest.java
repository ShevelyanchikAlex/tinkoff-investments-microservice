package com.alex.shevelyanchik.tinkoffservice.service;

import com.alex.shevelyanchik.tinkoffservice.model.Currency;
import com.alex.shevelyanchik.tinkoffservice.model.Stock;
import com.alex.shevelyanchik.tinkoffservice.service.impl.TinkoffStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TinkoffStockServiceTest {
    @Mock
    private CompletableFuture<MarketInstrumentList> completableFuture;
    @Mock
    private OpenApi api;
    @Mock
    MarketContext marketContext;
    private TinkoffStockService tinkoffStockService;

    private static final String STOCK_NAME = "Test name";
    private static final String STOCK_TICKER = "Test ticker";
    private static final String STOCK_TYPE = "Etf";
    private static final Currency STOCK_CURR = Currency.RUB;


    @BeforeEach
    void beforeEach() {
        MarketInstrumentList list = new MarketInstrumentList();
        MarketInstrument marketInstrument = new MarketInstrument();
        marketInstrument.setName(STOCK_NAME);
        marketInstrument.setTicker(STOCK_TICKER);
        marketInstrument.setType(InstrumentType.ETF);
        marketInstrument.setCurrency(ru.tinkoff.invest.openapi.model.rest.Currency.RUB);
        list.addInstrumentsItem(marketInstrument);

        when(api.getMarketContext())
                .thenReturn(marketContext);
        when(marketContext.searchMarketInstrumentsByTicker(anyString()))
                .thenReturn(completableFuture);
        when(completableFuture.join())
                .thenReturn(list);
        tinkoffStockService = new TinkoffStockService(api);
    }

    @Test
    void getStockByTicker() {
        Stock actualStock = tinkoffStockService.getStockByTicker("TEST");
        assertEquals(STOCK_NAME, actualStock.getName());
        assertEquals(STOCK_TICKER, actualStock.getTicker());
        assertEquals(STOCK_TYPE, actualStock.getType());
        assertEquals(STOCK_CURR, actualStock.getCurrency());
    }
}