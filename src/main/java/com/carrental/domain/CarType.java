package com.carrental.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of car")
public enum CarType {
    SEDAN,
    SUV,
    VAN
}