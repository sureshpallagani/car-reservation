package com.carrental.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of car")
public enum CarType {
    @Schema(description = "Standard sedan", example = "SEDAN")
    SEDAN,
    @Schema(description = "Sport utility vehicle", example = "SUV")
    SUV,
    @Schema(description = "Passenger van", example = "VAN")
    VAN
}