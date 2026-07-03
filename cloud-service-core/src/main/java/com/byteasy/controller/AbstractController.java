package com.byteasy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Constant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;


@Log4j2
public abstract class AbstractController<SERVICE extends IService<ENTITY>, ENTITY, ID extends Serializable> implements Constant {

    @Autowired
    protected SERVICE baseService;

    public SERVICE getBaseService() {
        return baseService;
    }

    public Class<?> getEntityClass() {
        return baseService.getEntityClass();
    }
}
