package com.michelangelo.usermicroservice.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MediaServiceResponseErrorHandlerTest {
    private MediaServiceResponseErrorHandler mediaServiceResponseErrorHandler;
    private ClientHttpResponse response;

    @BeforeEach
    void setUp() {
        mediaServiceResponseErrorHandler = new MediaServiceResponseErrorHandler();
        response = mock(ClientHttpResponse.class);
    }

    // hasError() tester
    @Test
    void shouldReturnFalseWith200StatusCode() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        assertFalse(mediaServiceResponseErrorHandler.hasError(response));
    }

    @Test
    void shouldReturnTrueWith400StatusCode() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        assertTrue(mediaServiceResponseErrorHandler.hasError(response));
    }

    @Test
    void shouldReturnTrueWith500StatusCode() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);
        assertTrue(mediaServiceResponseErrorHandler.hasError(response));
    }


    // handleError() tester
    @Test
    void shouldThrowExceptionWithStatusCodeNotFound() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaServiceResponseErrorHandler.handleError(response));

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWithStatusCodeBadRequest() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaServiceResponseErrorHandler.handleError(response));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWithStatusCodeForbidden() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.FORBIDDEN);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaServiceResponseErrorHandler.handleError(response));

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWithStatusCodeServiceUnavailable() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaServiceResponseErrorHandler.handleError(response));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWithStatusCodeInternalServerError() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaServiceResponseErrorHandler.handleError(response));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWithStatusCodeBadGateway() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_GATEWAY);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaServiceResponseErrorHandler.handleError(response));

        assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
    }

}