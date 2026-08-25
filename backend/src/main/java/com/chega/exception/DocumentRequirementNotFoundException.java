package com.chega.exception;

public class DocumentRequirementNotFoundException
        extends RuntimeException {

    public DocumentRequirementNotFoundException() {
        super("Requisito documental não encontrado.");
    }
}