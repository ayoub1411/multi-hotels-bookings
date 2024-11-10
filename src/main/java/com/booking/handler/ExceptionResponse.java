package com.booking.handler;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private Integer businessErrorCode;
    private String businessErrorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;

    // Private constructor for the builder
    private ExceptionResponse(Builder builder) {
        this.businessErrorCode = builder.businessErrorCode;
        this.businessErrorDescription = builder.businessErrorDescription;
        this.error = builder.error;
        this.validationErrors = builder.validationErrors;
        this.errors = builder.errors;
    }

    public Integer getBusinessErrorCode() {
        return businessErrorCode;
    }

    public String getBusinessErrorDescription() {
        return businessErrorDescription;
    }

    public String getError() {
        return error;
    }

    public Set<String> getValidationErrors() {
        return validationErrors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer businessErrorCode;
        private String businessErrorDescription;
        private String error;
        private Set<String> validationErrors;
        private Map<String, String> errors;

        public Builder businessErrorCode(Integer businessErrorCode) {
            this.businessErrorCode = businessErrorCode;
            return this;
        }

        public Builder businessErrorDescription(String businessErrorDescription) {
            this.businessErrorDescription = businessErrorDescription;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder validationErrors(Set<String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public Builder errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public ExceptionResponse build() {
            return new ExceptionResponse(this);
        }
    }
}
