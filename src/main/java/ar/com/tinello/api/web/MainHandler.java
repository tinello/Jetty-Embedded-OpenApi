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

import ar.com.tinello.api.core.Provider;

public class MainHandler extends Handler.Abstract {

    private final RequestValidator requestValidator;
    private final Provider provider;
    private final Map<String, Operation> operations;

    public MainHandler(RequestValidator requestValidator, Provider provider, Map<String, Operation> operations) {
        this.requestValidator = requestValidator;
        this.provider = provider;
        this.operations = operations;
    }

    @Override
    public boolean handle(Request request, Response response, Callback callback) throws Exception {

        response.getHeaders().put(HttpHeader.CONTENT_TYPE, "application/json; charset=utf-8");
        
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
            body = op.execute(request, provider);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            body = "{\"message\":\"" + e.getMessage().replaceAll("\"", "").replaceAll("\r", "").replaceAll("\n", "") + "\"}";
        }

        return true;
    }
    
}
