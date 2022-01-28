package com.apmm.datareader.reactive.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler
{
    public GlobalErrorWebExceptionHandler(com.apmm.datareader.reactive.exception.GlobalErrorAttributes g, ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(g, new WebProperties.Resources(), applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }
    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        final Map<String, Object> map = getErrorAttributes(request, ErrorAttributeOptions.defaults());
System.out.println("errorPropertiesMap::"+map.toString());

        Map<String,Object> finalMap=new HashMap<>();
        finalMap.put("status",String.valueOf(map.get("status")));
        finalMap.put("resources",String.valueOf(map.get("path")));

        String statusFromMap= String.valueOf(map.get("status"));
        HttpStatus status=HttpStatus.BAD_REQUEST;
        switch (statusFromMap) {
            case "500":

                finalMap.put("message", "Something went wrong.Please try again later");
                status=HttpStatus.INTERNAL_SERVER_ERROR;
                break;

            case "404":
                finalMap.put("message", "Data not found for the search record");
                status=HttpStatus.NOT_FOUND;
                break;

            case "405":
                finalMap.put("message", "Only get method is allowed");
                status=HttpStatus.METHOD_NOT_ALLOWED;
                break;

            case "400":
                finalMap.put("message", "Bad request..");
                status=HttpStatus.BAD_REQUEST;
                break;

        }
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(finalMap));
    }
}
