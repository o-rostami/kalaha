package com.bol.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ExceptionResponseDto {

    private String type;
    private String[] title;
    private HttpStatus status;
    private String detail;
    private String instance;
}
