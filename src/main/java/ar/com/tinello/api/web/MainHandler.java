package ar.com.tinello.api.web;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.operation.validator.validation.OperationValidator;
import org.openapi4j.operation.validator.validation.RequestValidator;


public final class MainHandler extends Handler.Abstract {

    private final RequestValidator requestValidator;
    private final Map<String, Operation> operations;

    public MainHandler(final RequestValidator requestValidator, final Map<String, Operation> operations) {
        this.requestValidator = requestValidator;
        this.operations = operations;
    }

    @Override
    public final boolean handle(final Request request, final Response response, final Callback callback) throws Exception {

        response.getHeaders().put(HttpHeader.CONTENT_TYPE, "application/json; charset=utf-8");
        
        // Convert from jetty request to openapi request
        final var req = ServletRequest.of(request);

        OperationValidator operationValidator;

        try {
            requestValidator.validate(req);
            operationValidator = requestValidator.getValidator(req);
        } catch (ValidationException e) {
            response.write(true, BufferUtil.toBuffer("{\"message\":\"" + e.getMessage().replaceAll("\"", "").replaceAll("\r", "").replaceAll("\n", "") + "\"}", StandardCharsets.UTF_8), callback);
            return true;
        }

        final var operation = operationValidator.getOperation();
        final var op = operations.get(operation.getOperationId());
        
        String body;
        try {
            body = op.execute(request);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            body = "{\"message\":\"" + e.getMessage().replaceAll("\"", "").replaceAll("\r", "").replaceAll("\n", "") + "\"}";
        }

        response.write(true, BufferUtil.toBuffer(body, StandardCharsets.UTF_8), callback);
        return true;
    }
    
}
