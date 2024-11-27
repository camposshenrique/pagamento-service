package br.com.instivo.pagamento.exceptions;

import br.com.instivo.pagamento.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GenericExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleExceptions(MethodArgumentNotValidException exception){
        String error = exception.getBindingResult().getAllErrors().stream()
                .map(ex -> ex.getDefaultMessage()).collect(Collectors.joining(", "));

        return new ResponseEntity<>(ErrorResponse.builder().message(error).build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleExceptions(){
        return new ResponseEntity<>(ErrorResponse.builder().message("Erro desconhecido, favor entrar em contato com o suporte").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValorPixException.class)
    public ResponseEntity<Object> handleExceptions(ValorPixException exception){
        String error = exception.getMessage();

        return new ResponseEntity<>(ErrorResponse.builder().message(error).build(), HttpStatus.BAD_REQUEST);
    }
}
