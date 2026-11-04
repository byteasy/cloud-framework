package com.byteasy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodInvokeDTO implements Serializable {
    private String methodName;        // 调用的方法名，如 saveOrUpdate
    private String className;         // 实体类名
    private List<Object> arguments;   // 传入的实参列表
}
