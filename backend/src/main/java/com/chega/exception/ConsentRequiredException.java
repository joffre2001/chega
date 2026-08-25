package com.chega.exception;

public class ConsentRequiredException
        extends RuntimeException {

    public ConsentRequiredException() {
        super("É necessário consentir com o tratamento dos dados.");
    }
}