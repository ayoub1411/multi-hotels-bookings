package com.booking.exception;

public class UnavailableRoomException  extends RuntimeException{
    public UnavailableRoomException(String message){
        super(message);
    }
}
