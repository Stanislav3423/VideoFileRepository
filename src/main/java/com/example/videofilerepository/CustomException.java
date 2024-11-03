package com.example.videofilerepository;

/*
 * Власі класи-винятки для опису та обробки виняткових ситуацій
 */
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}

class VideoNotFoundException extends CustomException {
    public VideoNotFoundException(String message) {
        super(message);
    }
}

class EmptyFieldException extends CustomException {
    public EmptyFieldException(String message) {
        super(message);
    }
}

class EmptyListException extends CustomException {

    public EmptyListException(String message) {
        super(message);
    }
}