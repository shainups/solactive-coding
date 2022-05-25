package com.solactive.realtimestatistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.realtimestatistics.model.Statistics;
import com.solactive.realtimestatistics.service.StatisticsService;
import com.solactive.realtimestatistics.model.Tick;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class RealTimePriceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private RealTimePriceController realTimePriceController;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(realTimePriceController).build();
    }

    @Test
    public void testReceiveTick() throws Exception {
        Mockito.doNothing().when(statisticsService).processTick(Mockito.any(), Mockito.anyLong());

        Tick tick = createTick("abc",1.0, System.currentTimeMillis());

        mockMvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
    }

    @Test
    public void testReceiveTickNoTick() throws Exception {
        Tick tick = createTick("abc", 1.0, 0);

        mockMvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_NO_CONTENT));
    }

    @Test
    public void testReceiveTickOldTimestamp() throws Exception {
        Tick tick = createTick("abc", 1.0,
                Instant.ofEpochMilli(System.currentTimeMillis() - 61000).atOffset(ZoneOffset.UTC).toEpochSecond());

        mockMvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_NO_CONTENT));
    }

    @Test
    public void testGetStatistics() throws Exception {
        Statistics statistics = Statistics.newBuilder().withCount(5).withAvg(5.6).withMin(3.4)
                .withMax(6.7).build();
        Mockito.when(statisticsService.getStatistics(Instant.now(Clock.systemUTC()).getEpochSecond())).thenReturn(statistics);

        mockMvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.count").value(statistics.getCount()))
                .andExpect(jsonPath("$.avg").value(statistics.getAvg()))
                .andExpect(jsonPath("$.min").value(statistics.getMin()))
                .andExpect(jsonPath("$.max").value(statistics.getMax()));
    }

    @Test
    public void testGetInstrumentStatistics() throws Exception {
        Statistics statistics = Statistics.newBuilder().withCount(5).withAvg(5.6).withMin(3.4)
                .withMax(6.7).build();
        Mockito.when(statisticsService.getStatistics("abc", Instant.now(Clock.systemUTC()).getEpochSecond())).thenReturn(statistics);

        mockMvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.count").value(statistics.getCount()))
                .andExpect(jsonPath("$.avg").value(statistics.getAvg()))
                .andExpect(jsonPath("$.min").value(statistics.getMin()))
                .andExpect(jsonPath("$.max").value(statistics.getMax()));
    }

    private Tick createTick(String instrument, double amount, long timestamp) {
        Tick tick = new Tick();
        tick.setInstrument(instrument);
        tick.setPrice(amount);
        tick.setTimestamp(timestamp);
        return tick;
    }
}
