package org.gbl.out.http;

public record ApiResponse<T>(int status, String message, T data) {
}
