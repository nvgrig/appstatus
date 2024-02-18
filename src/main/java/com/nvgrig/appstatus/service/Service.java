package com.nvgrig.appstatus.service;

import com.nvgrig.appstatus.model.DataModel;
import com.nvgrig.appstatus.model.ProcessedData;

public interface Service {

    DataModel getData();

    ProcessedData processData(DataModel dataModel);
}
