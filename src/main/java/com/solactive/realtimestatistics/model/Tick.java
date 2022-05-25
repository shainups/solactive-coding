package com.solactive.realtimestatistics.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Tick {

    @NotBlank
    private String instrument;

    @NotNull
    private Double price;

    @NotNull
    private Long timestamp;
}
