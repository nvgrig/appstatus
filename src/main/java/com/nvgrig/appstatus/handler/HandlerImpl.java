package com.nvgrig.appstatus.handler;

import com.nvgrig.appstatus.response.ApplicationStatusResponse;
import com.nvgrig.appstatus.response.Response;
import com.nvgrig.appstatus.service.Service;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class HandlerImpl implements Handler {

    private static long TIME_OUT_SECONDS = 15;
    private AtomicInteger retriesCount = new AtomicInteger(0);
    private String currentId = null;
    private Service service;

    public HandlerImpl(Service service) {
        this.service = service;
    }

    @Override
    @Synchronized
    public ApplicationStatusResponse performOperation(String id) {
        LocalDateTime start = LocalDateTime.now();

        if (currentId == null) {
            currentId = id;
            retriesCount.addAndGet(1);
        } else {
            if (currentId.equals(id)) {
                retriesCount.addAndGet(1);
            } else {
                currentId = id;
                retriesCount = new AtomicInteger(0);
            }
        }

        Response response;
        LocalDateTime end = LocalDateTime.now();
        do {
            response = service.getStatus(id);
            if (response instanceof Response.RetryAfter) {
                end = LocalDateTime.now();
                Duration duration = Duration.between(start, end);
                if (duration.getSeconds() > TIME_OUT_SECONDS) {
                    break;
                }
            }
        } while (response instanceof Response.RetryAfter);

        if (response instanceof Response.Failure) {
            log.error("service error", ((Response.Failure) response).ex());
            return new ApplicationStatusResponse.Failure(Duration.between(start, end), retriesCount.get());
        }
        if (response instanceof Response.RetryAfter) {
            return new ApplicationStatusResponse.Failure(Duration.between(start, end), retriesCount.get());
        }

        Response.Success success = (Response.Success) response;
        return new ApplicationStatusResponse.Success(success.applicationId(), success.applicationStatus());
    }
}
