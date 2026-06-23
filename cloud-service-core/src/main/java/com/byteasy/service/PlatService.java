package com.byteasy.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.Optional;

public class PlatService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {

        IPage<T> page = page(new Page<T>(1, 1), queryWrapper);
        if (!page.getRecords().isEmpty()) {
            return page.getRecords().getFirst();
        } else {
            if (throwEx) {
                throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: 0");
            }
            return null;
        }
    }

    @Override
    public Optional<T> getOneOpt(Wrapper<T> queryWrapper, boolean throwEx) {
        IPage<T> page = page(new Page<T>(1, 1), queryWrapper);
        if (!page.getRecords().isEmpty()) {
            return Optional.of(page.getRecords().getFirst());
        } else {
            if (throwEx) {
                throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: 0");
            }
        }
        return Optional.empty();
    }
}
