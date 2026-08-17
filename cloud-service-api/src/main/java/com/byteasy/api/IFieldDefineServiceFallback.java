package com.byteasy.api;

import com.byteasy.dto.Column;

import java.util.List;

public class IFieldDefineServiceFallback implements IFieldDefineService {

    @Override
    public List<Column> getSchemaMetaInfo(String className) throws Exception {
        return List.of();
    }
}
