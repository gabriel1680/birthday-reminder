package org.gbl.out.http;

public record ApiResponse<T>(String status, String message, T data) {
}
