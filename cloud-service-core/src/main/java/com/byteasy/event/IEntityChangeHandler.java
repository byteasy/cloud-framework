package com.byteasy.event;

/**
 * 数据变更统一处理service，如果需要统一处理，实现此方法
 */
public interface IEntityChangeHandler extends IPlatformEventHandler<EntityChangeEvent> {

}
