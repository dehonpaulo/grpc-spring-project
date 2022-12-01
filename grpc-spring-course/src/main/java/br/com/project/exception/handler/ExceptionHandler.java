package br.com.project.exception.handler;

import br.com.project.exception.BaseBusinessException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {

    @GrpcExceptionHandler(BaseBusinessException.class)
    public StatusRuntimeException handleBaseBusinessException(BaseBusinessException e) {
        return e
                .getStatusCode()
                .withCause(e.getCause())
                .withDescription(e.getErrorMessage())
                .asRuntimeException();
    }
}