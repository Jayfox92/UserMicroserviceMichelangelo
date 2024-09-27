package com.michelangelo.usermicroservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/* För att hantera http responser mellan 4xx - 5xx i kommunikationen med UserMicroservice. */
public class MediaServiceResponseErrorHandler implements ResponseErrorHandler {

    // Kontroll om statuskoden innehåller error i form av 4xx eller 5xx kod
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    // Om error 4xx  eller 5xx i responsen hanteras det här
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();

        if (statusCode.is4xxClientError()) { // Client Error - 4xx
            if(statusCode == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested resource in Media microservice not found");
            }else if(statusCode == HttpStatus.BAD_REQUEST){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request/Bad request for Media microservice");
            }else {
                throw new ResponseStatusException(statusCode, "Unexpected error during Media microservice communication: " + response.getStatusText());
            }

        }else if(statusCode.is5xxServerError()) { // Server Error - 5xx
            if(statusCode == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Media microservice is not available");
            }else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error during Media microservice communication");
            }else {
                throw new ResponseStatusException(statusCode, "Unexpected error during Media microservice communication: " + response.getStatusText());
            }
        }
    }

}
