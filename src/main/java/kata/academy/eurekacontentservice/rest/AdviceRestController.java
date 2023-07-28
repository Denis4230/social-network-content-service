package kata.academy.eurekacontentservice.rest;

import kata.academy.eurekacontentservice.api.Response;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
public class AdviceRestController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<Void> onGlobalException(Exception exception) {
        return Response.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PropertyReferenceException.class)
    public Response<Void> onPropertyReferenceException(PropertyReferenceException exception) {
        return Response.error(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<Void> onConstraintViolationException(ConstraintViolationException exception) {
        return Response.error(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return Response.error("В теле запроса допущены ошибки в следующих полях: " + exception.getBindingResult().getFieldErrors().stream().map(FieldError::getField).toList());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Response<Void> onMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return Response.error("В запросе не указан параметр: " + exception.getParameterName());
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<Void> onHttpMessageNotReadableException() {
        return Response.error("В запросе не указано тело");
    }

    @ResponseBody
    @ExceptionHandler(NoFallbackAvailableException.class)
    public Response<Void> onNoFallbackAvailableException(NoFallbackAvailableException exception) {
        return Response.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}
