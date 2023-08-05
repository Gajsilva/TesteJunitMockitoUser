package com.br.tutorial.demo.handler;

import com.br.tutorial.demo.handler.entidadeHandler.BadRequestException;
import com.br.tutorial.demo.handler.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestController
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorMessage> handleBadException(BadRequestException e, WebRequest webRequest){
        return new ResponseEntity<>((new ErrorMessage(new Date(),e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException e, WebRequest webRequest){
        return new ResponseEntity<>((new ErrorMessage(new Date(),e.getMessage())), HttpStatus.BAD_REQUEST);
    }

}
