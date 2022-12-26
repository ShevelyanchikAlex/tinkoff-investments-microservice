package com.alex.shevelyanchik.tinkoffservice.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorDto {
    String errorMessage;
}
