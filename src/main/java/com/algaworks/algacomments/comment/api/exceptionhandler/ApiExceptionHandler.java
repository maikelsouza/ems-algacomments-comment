package com.algaworks.algacomments.comment.api.exceptionhandler;

import com.algaworks.algacomments.comment.api.client.exception.CommentClientBadGatewayException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("One or more fields are not valid");
        problemDetail.setType(URI.create("https://algacomments.com/erros/invalid-fields"));

        Map<String, String> fields = ex.getBindingResult().getAllErrors()
                .stream()
                .collect(Collectors.toMap(objectError -> ((FieldError) objectError).getField(),
                        objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));

        fields.forEach((field, message) ->
                log.warn("Field not valid: '{}' - reason: {}", field, message)
        );
        problemDetail.setProperty("fields", fields);

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException e){

        log.error("Data Integrity Violation: {}", e.getMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("resource in use");
        problemDetail.setType(URI.create("https://algacomments.com/erros/resource-in-use"));
        return problemDetail;
    }


    @ExceptionHandler(CommentClientBadGatewayException.class)
    public ProblemDetail handle(CommentClientBadGatewayException e){
        log.error("Comment Client Bad Gateway: {}", e.getMessage(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setTitle(HttpStatus.BAD_GATEWAY.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("erros/bad-gateway"));
        return problemDetail;
    }

    @ExceptionHandler({
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class
    })
    public ProblemDetail handle(IOException e){
        log.error("Timeout Error: {}", e.getMessage(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        problemDetail.setTitle(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("erros/gateway-timeout"));
        return problemDetail;
    }
}
