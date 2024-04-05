package nst.springboot.nstapplication.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
     void testHandleEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        MyErrorDetails errorDetails = globalExceptionHandler.handleEntityNotFoundException(exception);
        assertEquals("Entity not found", errorDetails.getErrorMessage());
    }

    @Test
     void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal argument");
        MyErrorDetails errorDetails = globalExceptionHandler.handleIllegalArgumentException(exception);
        assertEquals("Illegal argument", errorDetails.getErrorMessage());
    }

    @Test
     void testHandleEmptyResponseException() {
        EmptyResponseException exception = new EmptyResponseException("Empty response");
        MyErrorDetails errorDetails = globalExceptionHandler.handleEmptyResponseException(exception);
        assertEquals("Empty response", errorDetails.getErrorMessage());
    }

    @Test
     void testHandleEntityAlreadyExistsException() {
        EntityAlreadyExistsException exception = new EntityAlreadyExistsException("Entity already exists");
        MyErrorDetails errorDetails = globalExceptionHandler.handleEntityNotFoundException(exception);
        assertEquals("Entity already exists", errorDetails.getErrorMessage());
    }


    @Test
     void testHandleMethodArgumentNotValid() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        WebRequest request = mock(WebRequest.class);

        List<ObjectError> errors = new ArrayList<>();
        errors.add(new FieldError("objectName", "fieldName", "Invalid value"));

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(exception, null, null, request);
        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();

        assertEquals("Invalid value", responseBody.get("fieldName"));
    }
}
