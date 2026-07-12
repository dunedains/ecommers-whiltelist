package com.ecommers.wishlist.exception;

public record ErrorResponse(int status, String message, String timestamp) {}
