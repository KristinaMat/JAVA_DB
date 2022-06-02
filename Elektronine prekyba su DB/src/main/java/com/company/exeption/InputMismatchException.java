package com.company.exeption;

import java.util.NoSuchElementException;

public class InputMismatchException extends NoSuchElementException {
    public InputMismatchException (String messages2){
        super(messages2);
    }
}
