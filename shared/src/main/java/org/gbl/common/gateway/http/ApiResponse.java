package org.gbl.common.gateway.http;

public record ApiResponse<T>(String status, String message, T data) {
}
