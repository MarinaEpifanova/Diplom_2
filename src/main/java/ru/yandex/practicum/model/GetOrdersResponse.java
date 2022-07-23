package ru.yandex.practicum.model;

public class GetOrdersResponse {
    public int total;
    public boolean success;

    public GetOrdersResponse(boolean success, int total) {
        this.success = success;
        this.total = total;
    }
}