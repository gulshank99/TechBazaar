package com.gsa.tech.bazaar.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFoundEception extends RuntimeException{

    public ResourceNotFoundEception() {
        super("Resource Not found !!");
    }

    public ResourceNotFoundEception(String message) {
        super(message);
    }
}
