package fiap.restaurant.app.configuration;

import fiap.restaurant.app.adapter.web.json.user.ErrorResponseDto;
import fiap.restaurant.app.core.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
        String requestPath = httpRequest.getRequestURI();
        String httpMethod = httpRequest.getMethod();

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                ex.getMessage(),
                httpMethod,
                requestPath,
                LocalDateTime.now().toString(),
                status.value()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler({DuplicatedDataException.class})
    public ResponseEntity<ErrorResponseDto> handleAlreadyInUseException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            RestaurantNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidCredentialException.class,
            UnauthorizedOperationException.class
    })
    public ResponseEntity<ErrorResponseDto> handleUnauthorized(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            EmailFormatException.class,
            IllegalArgumentException.class,
            BusinessException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildErrorResponse(new Exception(errorMessage), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}