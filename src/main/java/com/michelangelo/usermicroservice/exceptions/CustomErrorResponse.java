package com.michelangelo.usermicroservice.exceptions;

import org.springframework.http.HttpStatusCode;


/* Representerar ett felmeddelande till användaren. */
public class CustomErrorResponse {
    private HttpStatusCode status;
    private String message;

    public CustomErrorResponse(HttpStatusCode status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
    public void setStatus(HttpStatusCode status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
