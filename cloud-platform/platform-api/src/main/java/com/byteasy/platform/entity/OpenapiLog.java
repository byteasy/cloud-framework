package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.common.token.UserInfo;
import com.byteasy.entity.ImmutablyEntity;
import com.byteasy.enums.IBaseEnum;
import com.byteasy.enums.YesNo;
import com.byteasy.typehandler.PgJsonbTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * <p>
 * 接口执行日志记录表
 * </p>
 *
 * @author devops
 * @since 2026-09-17
 */
@Getter
@Setter
@TableName("platform_openapi_log")
@FieldMeta(name = "OpenapiLog", description = "接口执行日志记录表")
public class OpenapiLog extends ImmutablyEntity<Long> {

    @FieldMeta(name = "微服务编码", showOrder = 100)
    @TableField("service_code")
    private String serviceCode;

    @FieldMeta(name = "请求路径(URI)", showOrder = 101)
    @TableField("uri")
    private String uri;

    @FieldMeta(name = "HTTP请求方式(GET/POST等)", showOrder = 102)
    @TableField("http_method")
    private Openapi.HttpMethod httpMethod;

    @FieldMeta(name = "URL查询参数", showOrder = 103)
    @TableField("query_string")
    private String queryString;

    @FieldMeta(name = "请求头", showOrder = 104)
    @TableField("http_header")
    private Map<String, String[]> httpHeader;

    @FieldMeta(name = "请求类型", showOrder = 105)
    @TableField("content_type")
    private String contentType;

    @FieldMeta(name = "请求体", showOrder = 106)
    @TableField("http_body")
    private String httpBody;

    @FieldMeta(name = "接口响应", showOrder = 107)
    @TableField("http_response")
    private String httpResponse;

    @FieldMeta(name = "请求发起人身份上下文快照", showOrder = 108)
    @TableField(value = "user_info", typeHandler = PgJsonbTypeHandler.class)
    private UserInfo userInfo;

    @FieldMeta(name = "OpenAPI模型定义ID", showOrder = 109)
    @TableField("openapi_id")
    private Long openapiId;

    @FieldMeta(name = "OpenAPI接口名称", showOrder = 110)
    @TableField("openapi_name")
    private String openapiName;

    @FieldMeta(name = "耗时(毫秒)", showOrder = 111)
    @TableField("cost_time")
    private Long costTime;

    @FieldMeta(name = "执行状态", showOrder = 112)
    @TableField("status")
    private Status status;

    @FieldMeta(name = "异常错误详细信息", showOrder = 113)
    @TableField("error_message")
    private String errorMessage;

    @FieldMeta(name = "是否重放", showOrder = 114)
    @TableField("is_replay")
    private YesNo isReplay;

    @FieldMeta(name = "重放来源ID", showOrder = 115)
    @TableField("replay_source_id")
    private Long replaySourceId;

    public enum Status implements IBaseEnum<Integer> {
        SUCCESS(0, "成功"),
        FAILED(1, "失败");

        public final int value;
        public final String desc;

        Status(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("无效的状态值: " + value);
        }

        @Override
        public String getName() {
            return desc;
        }

        @Override
        public String getCss() {
            if (value == 1) {
                return DANGER;
            } else {
                return IBaseEnum.super.getCss();
            }
        }
    }
}
