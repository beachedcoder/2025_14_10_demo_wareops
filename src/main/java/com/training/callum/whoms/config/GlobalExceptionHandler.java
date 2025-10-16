package com.training.callum.whoms.config;

import com.training.callum.whoms.domain.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.UUID;

/**
 * Global exception handler for all controllers.
 *
 * Returns a generic, safe error message to clients and logs the original exception
 * along with a UUID (messageId) included in the response for correlation.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String GENERIC_MESSAGE = "Our apologies for fumbling your request";

    /**
     * Handle bad request exceptions (e.g., malformed JSON).
     *
     * @param ex the exception thrown
     * @return ResponseEntity with ErrorResponseDTO and HTTP 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(final HttpMessageNotReadableException ex) {
        final UUID messageId = UUID.randomUUID();
        LOGGER.error("Bad request exception captured, messageId={}. Returning error to client.", messageId, ex);

        final ErrorResponseDTO body = new ErrorResponseDTO(GENERIC_MESSAGE, HttpStatus.BAD_REQUEST.value(), messageId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handle unsupported media type exceptions (e.g., wrong Content-Type).
     *
     * @param ex the exception thrown
     * @return ResponseEntity with ErrorResponseDTO and HTTP 415
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnsupportedMediaType(final HttpMediaTypeNotSupportedException ex) {
        final UUID messageId = UUID.randomUUID();
        LOGGER.error("Unsupported media type exception captured, messageId={}. Returning error to client.", messageId, ex);

        final ErrorResponseDTO body = new ErrorResponseDTO(GENERIC_MESSAGE, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), messageId);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(body);
    }

    /**
     * Handle no handler found exceptions (e.g., 404 for unmatched routes).
     *
     * @param ex the exception thrown
     * @return ResponseEntity with ErrorResponseDTO and HTTP 404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFound(final NoHandlerFoundException ex) {
        final UUID messageId = UUID.randomUUID();
        LOGGER.error("No handler found exception captured, messageId={}. Returning error to client.", messageId, ex);

        final ErrorResponseDTO body = new ErrorResponseDTO(GENERIC_MESSAGE, HttpStatus.NOT_FOUND.value(), messageId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handle any uncaught exception from controllers.
     *
     * @param ex the exception thrown
     * @return ResponseEntity with ErrorResponseDTO and HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(final Exception ex) {
        final UUID messageId = UUID.randomUUID();
        // Log the exception with the UUID that is returned to the client for correlation.
        LOGGER.error("Unhandled exception captured, messageId={}. Returning generic error to client.", messageId, ex);

        final ErrorResponseDTO body = new ErrorResponseDTO(GENERIC_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.value(), messageId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}