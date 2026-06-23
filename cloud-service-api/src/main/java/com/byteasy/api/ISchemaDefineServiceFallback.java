package com.byteasy.api;

import com.byteasy.dto.Column;

import java.util.List;

public class ISchemaDefineServiceFallback implements ISchemaDefineService {

    @Override
    public List<Column> getSchemaMetaInfo(String className) throws Exception {
        return List.of();
    }
}
