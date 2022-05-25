package com.solactive.realtimestatistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.realtimestatistics.model.Tick;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealTimePriceControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testAddTick() throws Exception {

        long delayToBeginningOfNextSecond = 1100 - System.currentTimeMillis() % 1000;
        TimeUnit.MILLISECONDS.sleep(delayToBeginningOfNextSecond);

        long timestamp = System.currentTimeMillis() - 59000;

        Tick tick = createTick("abc", 1.2, timestamp);
        // add statistics
        mvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
        // validate new statistics
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.avg").value(1.2))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.2));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.avg").value(1.2))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.2));


        tick = createTick("abc",1.3, timestamp + 1000);
        // add statistics
        mvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
        // validate new statistics
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.avg").value(1.25))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.3));

        // validate new statistics
        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.avg").value(1.25))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.3));



        tick = createTick("abc",1.4, timestamp + 2000);
        // add statistics
        mvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
        // validate new statistics - count,avg normal effect - max changed
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.avg").value(1.3))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.4));

        // validate new statistics - count,avg normal effect - max changed
        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.avg").value(1.3))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.4));


        // 2 ticks in a second
        tick = createTick("abc",1.5, timestamp + 3000);
        // add statistics
        mvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
        // validate new statistics- count,avg normal effect - max changed
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.avg").value(1.35))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.5));

        // validate new statistics- count,avg normal effect - max changed
        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.avg").value(1.35))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.5));


        tick = createTick("abc",1.6, timestamp + 3000);
        // add statistics
        mvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
        // validate new statistics- count,avg normal effect - max changed
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.avg").value(1.4))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.6));

        // validate new statistics- count,avg normal effect - max changed
        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.avg").value(1.4))
                .andExpect(jsonPath("$.min").value(1.2))
                .andExpect(jsonPath("$.max").value(1.6));


        tick = createTick("abc",1.1, timestamp + 4000);
        // add statistics
        mvc.perform(post("/ticks").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tick)))
                .andExpect(status().is(HttpServletResponse.SC_CREATED));
        // validate new statistics- min value changed
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(6))
                .andExpect(jsonPath("$.avg").value(1.35))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        // validate new statistics- min value changed
        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(6))
                .andExpect(jsonPath("$.avg").value(1.35))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));


        // ##############Validate values for the subsequent seconds###############.

        TimeUnit.SECONDS.sleep(1);
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(6))
                .andExpect(jsonPath("$.avg").value(1.35))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(6))
                .andExpect(jsonPath("$.avg").value(1.35))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        TimeUnit.SECONDS.sleep(1);
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.avg").value(1.38))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.avg").value(1.38))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        TimeUnit.SECONDS.sleep(1);
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.avg").value(1.4))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.avg").value(1.4))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));


        TimeUnit.SECONDS.sleep(1);
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.avg").value(1.40))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.avg").value(1.40))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.6));

        TimeUnit.SECONDS.sleep(1);
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.avg").value(1.1))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.1));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.avg").value(1.1))
                .andExpect(jsonPath("$.min").value(1.1))
                .andExpect(jsonPath("$.max").value(1.1));

        TimeUnit.SECONDS.sleep(1);
        mvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.avg").value(0))
                .andExpect(jsonPath("$.min").value(0))
                .andExpect(jsonPath("$.max").value(0));

        mvc.perform(get("/statistics/abc").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.avg").value(0))
                .andExpect(jsonPath("$.min").value(0))
                .andExpect(jsonPath("$.max").value(0));
    }

    private Tick createTick(String instrument, double amount, long timestamp) {
        Tick tick = new Tick();
        tick.setInstrument(instrument);
        tick.setPrice(amount);
        tick.setTimestamp(timestamp);
        return tick;
    }

}
