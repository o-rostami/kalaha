package com.bol.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * A<i>ExceptionResponseDto</i>.This model contains the details about thrown exception<p>
 *
 * @author Omid Rostami
 */


@Data
@Builder
public class ExceptionResponseDto {

    private String type;
    private String[] title;
    private HttpStatus status;
    private String detail;
    private String instance;
}
