package com.bol.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    private MessageSource messageSource;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public GlobalExceptionHandler() {
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("/////////////////////// BAD_REQUEST Message Not Readable:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(HttpMessageNotReadableException.class.getSimpleName())
                        .status(status)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .title(new String[]{ex.getMessage()})
                        .detail(ex.getLocalizedMessage())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders
            headers, HttpStatus status, WebRequest request) {
        log.error("/////////////////////// BAD_REQUEST Method Argument Not Valid:  ///////////////////////", ex);
        List<ExceptionResponseDto> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.add(ExceptionResponseDto.builder()
                        .title(new String[]{((FieldError) error).getField()})
                        .detail(
                                error.getDefaultMessage()
                        )
                        .build()
                )
        );

        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(MethodArgumentNotValidException.class.getSimpleName())
                        .status(status)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus
            status, WebRequest request) {
        log.error("/////////////////////// BAD_REQUEST Type Mismatch:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(TypeMismatchException.class.getSimpleName())
                        .status(status)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .detail(((MethodArgumentTypeMismatchException) ex).getName())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ExceptionResponseDto> handleNotFoundException(NotFoundException ex, WebRequest
            request, Locale locale) {
        log.error("/////////////////////// NOT_FOUND Not Found Exception:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(NotFoundException.class.getSimpleName())
                        .status(HttpStatus.NOT_FOUND)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .detail(getMessage(ex, locale))
                        .build()
                , HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionResponseDto> handleGlobalException(BusinessException ex, WebRequest request, Locale
            locale) {
        log.error("/////////////////////// INTERNAL_SERVER_ERROR Global Exception:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(BusinessException.class.getSimpleName())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .title(ex.getArguments())
                        .detail(getMessage(ex, locale))
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NotNullException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(NotNullException ex, WebRequest request, Locale
            locale) {
        log.error("/////////////////////// NotNullException Handle Exception: ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(NotNullException.class.getSimpleName())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .title(ex.getArguments())
                        .detail(getMessage(ex, locale))
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request, Locale
            locale) {
        log.error("/////////////////////// BAD_REQUEST Constraint Violation Exception:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(ConstraintViolationException.class.getSimpleName())
                        .status(HttpStatus.BAD_REQUEST)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .title((String[]) ex.getConstraintViolations().toArray())
                        .detail(getMessage(ex, locale))
                        .build()

                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request, Locale
            locale) {
        log.error("/////////////////////// BAD_REQUEST Data Integrity Violation Exception:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(DataIntegrityViolationException.class.getSimpleName())
                        .status(HttpStatus.BAD_REQUEST)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .title(new String[]{ex.getCause().getMessage()})
                        .detail(getMessage(ex, locale))
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request, Locale
            locale) {
        log.error("/////////////////////// INTERNAL_SERVER_ERROR Handle Exception:  ///////////////////////", ex);
        return new ResponseEntity<>(
                ExceptionResponseDto.builder()
                        .type(Exception.class.getSimpleName())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .instance(((ServletWebRequest) request).getRequest().getRequestURI())
                        .title(new String[]{ex.getMessage()})
                        .detail(getMessage(ex, locale))
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private String getMessage(Exception ex, Locale locale) {
        if (Objects.nonNull(ex.getMessage())) {
            try {
                return messageSource.getMessage(ex.getMessage(), null, locale);
            } catch (Exception e) {
                return ex.getMessage();
            }
        } else
            try {
                return messageSource.getMessage(ex.getClass().getName(), null, locale);
            } catch (Exception e) {
                return ex.getClass().getName();
            }
    }

}
