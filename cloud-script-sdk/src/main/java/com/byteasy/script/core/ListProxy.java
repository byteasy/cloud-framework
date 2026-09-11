package com.byteasy.script.core;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import java.util.List;

public class ListProxy implements ProxyArray {
    private final List<?> targetList;

    public ListProxy(List<?> targetList) {
        this.targetList = targetList;
    }

    // JS 访问 arr[index] 时触发
    @Override
    public Object get(long index) {
        if (index < 0 || index >= targetList.size()) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Object item = targetList.get((int) index);

        // 【关键】按需懒包装：如果拿出来的元素还是 Map 或 List，继续用 Proxy 包装给 JS
        return ProxyWrapper.wrap(item);
    }

    // JS 给 arr[index] = value 赋值时触发
    @Override
    public void set(long index, Value value) {
        // 如果业务脚本只需要读数据，不需要改数据，这里可以直接抛出 UnsupportedOperationException
        throw new UnsupportedOperationException("JS脚本不允许修改源数组");
    }

    // JS 访问 arr.length 时触发
    @Override
    public long getSize() {
        return targetList.size();
    }

    @Override
    public boolean remove(long index) {
        if (index >= 0 && index < targetList.size()) {
            targetList.remove((int) index);
            return true;
        }
        return false;
    }
}
