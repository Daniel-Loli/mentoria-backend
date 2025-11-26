package com.qt.qtBackend.exception;

import com.qt.qtBackend.dto.base.ExceptionResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import static com.qt.qtBackend.Enum.Message.*;

@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    //global
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ListResponse<ExceptionResponse>> handleDefaultException(Exception ex, WebRequest request) {
        ExceptionResponse cer = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(new ListResponse<>(
                500,
                ERROR_SERVIDOR.toString(),
                List.of(cer),
                List.of(cer).size()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //especifico
    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ListResponse<ExceptionResponse>> handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        ExceptionResponse cer = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(new ListResponse<>(
                404,
                RECURSOS_NO_ENCONTRADO.toString(),
                List.of(cer),
                List.of(cer).size()
        ), HttpStatus.NOT_FOUND);
    }
    /*
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<CustomErrorResponse> handleArithmeticException(ArithmeticException ex, WebRequest request) {
        CustomErrorResponse cer = new CustomErrorResponse(
                LocalDateTime.now(),
                "Error de cálculo: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(cer, HttpStatus.NOT_ACCEPTABLE);
    }

     */
    //otra forma de hacer una excepcion de validacion usando un metodo heredado ResponseEntityExceptionHandler
    /*@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        CustomErrorResponse cer = new CustomErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(cer, HttpStatus.BAD_REQUEST);
    }*/

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ExceptionResponse> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ExceptionResponse(
                        LocalDateTime.now(),
                        error.getDefaultMessage(),
                        request.getDescription(false)
                ))
                .toList();

        return new ResponseEntity<>(
                new ListResponse<>(400, ERROR_VALIDACION.toString(), errores, errores.size()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ListResponse<ExceptionResponse>> handleArithmeticException(
            ArithmeticException ex, WebRequest request) {

        ExceptionResponse cer = new ExceptionResponse(
                LocalDateTime.now(),
                "Error de cálculo: " + ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(
                new ListResponse<>(
                        406,
                        "ERROR_ARITMETICO",
                        List.of(cer),
                        1
                ),
                HttpStatus.NOT_ACCEPTABLE
        );
    }



    // Manejo de excepciones de validación
    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        CustomErrorResponse cer = new CustomErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(cer, HttpStatus.BAD_REQUEST);
    }*/


    /*
    @ExceptionHandler(ModelNotFoundException.class)
    public ProblemDetail handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        pd.setTitle("Recurso no encontrado");
        pd.setType(URI.create(request.getDescription(false)));
        pd.setProperty("code", 404);
        pd.setProperty("message", "NO ENCONTRADO");
        return pd;
    }
     */

}
