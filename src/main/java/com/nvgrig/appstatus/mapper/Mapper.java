package com.nvgrig.appstatus.mapper;

import com.nvgrig.appstatus.dto.DataDto;
import com.nvgrig.appstatus.model.ProcessedData;

public interface Mapper {

    DataDto from(ProcessedData processedData);
}
