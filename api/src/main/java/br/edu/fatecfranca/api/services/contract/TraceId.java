package br.edu.fatecfranca.api.services.contract;

import org.slf4j.MDC;

public final class TraceId {

    private static final String TRACE_ID = "traceId";

    private TraceId() {
    }

    public static String current() {
        return MDC.get(TRACE_ID);
    }
}