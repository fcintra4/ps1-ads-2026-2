package br.edu.fatecfranca.api.contract;

import java.time.Instant;

/**
 * Contrato padrão de resposta da API.
 *
 * @param <T> tipo do payload retornado em data
 */
public final class Contract<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;
    private final Meta meta;
    private final Instant timestamp;
    private final String traceId;

    private Contract(
            boolean success,
            T data,
            ApiError error,
            Meta meta,
            Instant timestamp,
            String traceId
    ) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.meta = meta;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    /**
     * Resposta de sucesso sem metadados.
     */
    public static <T> Contract<T> ok(T data) {
        return new Contract<>(
                true,
                data,
                null,
                null,
                Instant.now(),
                TraceId.current()
        );
    }

    /**
     * Resposta de sucesso com metadados.
     */
    public static <T> Contract<T> ok(
            T data,
            Meta meta
    ) {
        return new Contract<>(
                true,
                data,
                null,
                meta,
                Instant.now(),
                TraceId.current()
        );
    }

    /**
     * Resposta paginada.
     */
    public static <T> Contract<T> okPage(
            T data,
            PageMeta page
    ) {
        return new Contract<>(
                true,
                data,
                null,
                Meta.page(page),
                Instant.now(),
                TraceId.current()
        );
    }

    /**
     * Resposta de erro.
     */
    public static <T> Contract<T> error(
            ApiError error
    ) {
        return new Contract<>(
                false,
                null,
                error,
                null,
                Instant.now(),
                TraceId.current()
        );
    }

    /**
     * Resposta de erro simplificada.
     */
    public static <T> Contract<T> error(
            String code,
            String message
    ) {
        return error(
                ApiError.of(code, message)
        );
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    public Meta getMeta() {
        return meta;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getTraceId() {
        return traceId;
    }
}