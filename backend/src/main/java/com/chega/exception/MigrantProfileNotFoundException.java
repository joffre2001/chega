package com.chega.exception;

public class MigrantProfileNotFoundException
        extends RuntimeException {

    public MigrantProfileNotFoundException() {
        super("Perfil migratório não encontrado.");
    }
}