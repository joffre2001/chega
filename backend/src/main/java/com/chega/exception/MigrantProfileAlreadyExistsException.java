package com.chega.exception;

public class MigrantProfileAlreadyExistsException
        extends RuntimeException {

    public MigrantProfileAlreadyExistsException() {
        super("Este usuário já possui um perfil migratório.");
    }
}