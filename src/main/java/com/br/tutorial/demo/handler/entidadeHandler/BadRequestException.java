package com.br.tutorial.demo.handler.entidadeHandler;


public class BadRequestException extends RuntimeException {
    public BadRequestException(String getMessage) {
        super(getMessage);
    }


}
