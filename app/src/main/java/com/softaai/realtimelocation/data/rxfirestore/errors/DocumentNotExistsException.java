package com.softaai.realtimelocation.data.rxfirestore.errors;

public class DocumentNotExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Firebase document not exists";
    }
}
