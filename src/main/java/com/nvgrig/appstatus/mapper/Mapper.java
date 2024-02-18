package com.nvgrig.appstatus.mapper;

import com.nvgrig.appstatus.dto.DataModelDto;
import com.nvgrig.appstatus.model.DataModel;

public interface Mapper {

    DataModelDto from(DataModel dataModel);
}
