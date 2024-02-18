package com.nvgrig.appstatus.handler;

import com.nvgrig.appstatus.response.ApplicationStatusResponse;

public interface Handler {
    ApplicationStatusResponse performOperation(String id);
}
