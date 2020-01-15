/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smartadvertizer.core;

/**
 *
 */
public class BookingStatus {

    private BookingType type;
    private String client;
    public static final BookingStatus FREE = new BookingStatus(BookingType.Free);

    private BookingStatus(BookingType type) {
        this.type = type;
    }

    public BookingStatus(BookingType type, String client) {
        this(type);
        this.client = client;

    }

    public BookingType getType() {
        return type;
    }

    public String getClient() {
        return client;
    }

    public static BookingStatus free() {
        return FREE;
    }

    public static BookingStatus reserved(String client) {
        return new BookingStatus(BookingType.Reserved, client);
    }

    public static BookingStatus booked(String client) {
        return new BookingStatus(BookingType.Booked, client);
    }

    public boolean isFree() {
        return type == BookingType.Free;
    }

    public boolean isReserved() {
        return type == BookingType.Reserved;
    }

    public boolean isBooked() {
        return type == BookingType.Booked;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
