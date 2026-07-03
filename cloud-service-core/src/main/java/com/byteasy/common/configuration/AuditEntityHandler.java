package com.byteasy.common.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.entity.AuditEntity;
import com.byteasy.entity.Relation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class AuditEntityHandler implements MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        Object object = metaObject.getOriginalObject();
        if (object instanceof AuditEntity || object instanceof Relation) {
            UserInfo userInfo = TokenUtil.getUserinfo();

            // Job更新数据，拿不到userInfo
            if (userInfo == null) {
                return;
            }

            this.setFieldValByName("creatorId", userInfo.getUserId(), metaObject);
            this.setFieldValByName("createdBy", StringUtils.deleteWhitespace(userInfo.getName()), metaObject);
        }
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
        Object object = metaObject.getOriginalObject();

        if (object instanceof AuditEntity) {
            UserInfo userInfo = TokenUtil.getUserinfo();

            // Job更新数据，拿不到userInfo
            if (userInfo == null) {
                return;
            }

            this.setFieldValByName("updaterId", userInfo.getUserId(), metaObject);
            this.setFieldValByName("updatedBy", StringUtils.deleteWhitespace(userInfo.getName()), metaObject);
        }
    }
}
