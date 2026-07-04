package com.akshay.electronic.store.ElectronicStore.exception;

import lombok.Builder;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Super;
@Builder
public class BadApiRequest extends RuntimeException {

    public BadApiRequest() {
        super("Bad Request !!!");
    }

    public BadApiRequest(String message) {
        super(message);
    }
}
