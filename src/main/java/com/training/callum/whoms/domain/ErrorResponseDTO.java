package com.training.callum.whoms.domain;

import java.util.UUID;

/**
 * Generic error response returned to clients when an exception is handled globally.
 *
 * Fields:
 * - message: user-friendly message
 * - errorCode: numeric HTTP-like error code (e.g. 500)
 * - messageId: UUID to correlate logs with client response
 */
public record ErrorResponseDTO(String message, int errorCode, UUID messageId) {
}