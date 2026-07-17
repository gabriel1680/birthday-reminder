package org.gbl.controller.common;

import org.eclipse.jetty.http.HttpStatus.Code;
import org.gbl.contacts.application.service.query.InvalidSearchInputException;
import org.gbl.contacts.application.usecase.add.ContactAlreadyExistsException;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import spark.Response;

import java.util.Map;

import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.INTERNAL_SERVER_ERROR;
import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;

public class ExceptionMapper {

    public static final Map<Class<? extends Exception>, Code> STATUS_CODES =
            Map.of(
                    ContactAlreadyExistsException.class, UNPROCESSABLE_ENTITY,
                    InvalidPayloadException.class, BAD_REQUEST,
                    InvalidSearchInputException.class, BAD_REQUEST,
                    IllegalArgumentException.class, BAD_REQUEST,
                    ContactNotFoundException.class, NOT_FOUND
            );

    public static HttpAPIResponse mapException(Response response, Throwable throwable) {
        final var status = STATUS_CODES.getOrDefault(throwable.getClass(), INTERNAL_SERVER_ERROR);
        response.status(status.getCode());
        final var message = isInternalError(status)
                ? "Internal server error"
                : throwable.getMessage();
        return HttpAPIResponse.ofError(message);
    }

    private static boolean isInternalError(Code status) {
        return status == INTERNAL_SERVER_ERROR;
    }
}
