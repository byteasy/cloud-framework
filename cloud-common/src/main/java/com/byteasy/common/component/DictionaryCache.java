package com.byteasy.common.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.enums.Enable;
import com.byteasy.infinispan.util.CacheEvictHelper;
import com.byteasy.platform.Constant;
import com.byteasy.platform.api.DictionaryClient;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DictionaryCache implements ICacheLoader<String, DictionaryItem> {

    @Autowired
    private DictionaryClient dictionaryClient;
    @Autowired
    private CacheEvictHelper cacheEvictHelper;

    @Override
    public DictionaryItem load(String key) {
        String[] keys = key.split("#");
        DictionaryItem pwd = dictionaryClient.getItemByCode(keys[0], keys[1]).getIfPresent();
        if (pwd != null) {
            return pwd;
        }
        return pwd;
    }

    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'com.byteasy.platform.entity.DictionaryItem:' + #key")
    public String getOrCreate(String key, String label, String defaultValue) {
        DictionaryItem value = load(key);
        if (value != null) {
            return value.getValue();
        } else {
            // 自动创建默认配置项
            value = new DictionaryItem();
            String[] keys = key.split("#");
            Dictionary dictionary = dictionaryClient.getByCode(keys[0]).getIfPresent();
            if (dictionary != null) {
                value.setDictionaryId(dictionary.getId());
                value.setCode(keys[1]);
                value.setValue(defaultValue);
                value.setLabel(label);
                value.setStatus(Enable.ENABLE);
                dictionaryClient.saveOrUpdate(value);
            }
            return value.getValue();
        }
    }

    @Override
    public void invalid(Object entity) {
        if (entity instanceof DictionaryItem) {
            DictionaryItem item = (DictionaryItem) entity;
            if (Constant.DIC_SETTING_AES_PWD.equals(item.getCode())) {
                cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, DictionaryItem.class.getName() + ":" + Constant.DIC_SETTING_AES_PWD);
            }
        }
    }

    @Override
    public String cacheType() {
        return DictionaryItem.class.getName();
    }

    @Override
    public String[] invalidType() {
        return new String[]{DictionaryItem.class.getName()};
    }
}
