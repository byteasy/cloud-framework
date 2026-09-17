CREATE TABLE "platform_openapi_log"
(
    id            BIGSERIAL PRIMARY KEY,
    service_code  VARCHAR(128) NOT NULL,
    uri           VARCHAR(512) NOT NULL,
    http_method   VARCHAR(16)  NOT NULL,
    query_string  TEXT,
    http_header   JSONB                 DEFAULT '{}'::jsonb,
    content_type  VARCHAR(128),
    http_body     TEXT,
    http_response TEXT,
    user_info     JSONB                 DEFAULT '{}'::jsonb,
    openapi_id    BIGINT       NOT NULL,
    openapi_name  VARCHAR(512) NOT NULL,
    cost_time     BIGINT       NOT NULL DEFAULT 0, -- 耗时(ms)
    status        SMALLINT     NOT NULL DEFAULT 1, -- 1: 成功, 0: 异常/失败
    error_message TEXT,
    creator_id    VARCHAR(64),
    created_by    VARCHAR(64),
    created_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. 补全完整规范的表及列注释
COMMENT ON TABLE "platform_openapi_log" IS '接口执行日志记录表';
COMMENT ON COLUMN "platform_openapi_log"."id" IS '主键编号';
COMMENT ON COLUMN "platform_openapi_log"."service_code" IS '微服务编码';
COMMENT ON COLUMN "platform_openapi_log"."uri" IS '请求路径(URI)';
COMMENT ON COLUMN "platform_openapi_log"."http_method" IS 'HTTP请求方式(GET/POST等)';
COMMENT ON COLUMN "platform_openapi_log"."query_string" IS 'URL查询参数(?后面的参数)';
COMMENT ON COLUMN "platform_openapi_log"."http_header" IS '请求头信息(JSON格式)';
COMMENT ON COLUMN "platform_openapi_log"."content_type" IS '请求载荷类型';
COMMENT ON COLUMN "platform_openapi_log"."http_body" IS '请求体载荷(Body)';
COMMENT ON COLUMN "platform_openapi_log"."http_response" IS '接口响应报文';
COMMENT ON COLUMN "platform_openapi_log"."user_info" IS '请求发起人身份上下文快照';
COMMENT ON COLUMN "platform_openapi_log"."openapi_id" IS 'OpenAPI模型定义ID';
COMMENT ON COLUMN "platform_openapi_log"."openapi_name" IS 'OpenAPI接口名称';
COMMENT ON COLUMN "platform_openapi_log"."cost_time" IS '接口执行总耗时(毫秒)';
COMMENT ON COLUMN "platform_openapi_log"."status" IS '执行状态(1:成功, 0:失败/异常)';
COMMENT ON COLUMN "platform_openapi_log"."error_message" IS '异常错误详细信息';
COMMENT ON COLUMN "platform_openapi_log"."creator_id" IS '创建人ID';
COMMENT ON COLUMN "platform_openapi_log"."created_by" IS '创建人姓名/账号';
COMMENT ON COLUMN "platform_openapi_log"."created_time" IS '创建时间';

-- 3. 生产级必备检索索引（日志表排查与检索必备）
CREATE INDEX "idx_openapi_log_created_time" ON "platform_openapi_log" ("created_time" DESC);
CREATE INDEX "idx_openapi_log_openapi_id" ON "platform_openapi_log" ("openapi_id");
CREATE INDEX "idx_openapi_log_service_uri" ON "platform_openapi_log" ("service_code", "uri");
CREATE INDEX "idx_openapi_log_status" ON "platform_openapi_log" ("status");