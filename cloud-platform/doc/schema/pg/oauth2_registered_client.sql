-- 创建主表：oauth2_registered_client
CREATE TABLE oauth2_registered_client (
    -- 主键（分布式ID）
                                          ID VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),

    -- 客户端唯一标识符
                                          CLIENT_ID VARCHAR(100) NOT NULL UNIQUE,

    -- 客户端ID颁发时间
                                          CLIENT_ID_ISSUED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,,

    -- 客户端密钥（加密存储）
                                          CLIENT_SECRET VARCHAR(500),

    -- 客户端密钥过期时间
                                          CLIENT_SECRET_EXPIRES_AT TIMESTAMP,

    -- 客户端名称（用于展示）
                                          CLIENT_NAME VARCHAR(200),

    -- 客户端认证方式列表（如：client_secret_basic, client_secret_post 等）
                                          CLIENT_AUTHENTICATION_METHODS VARCHAR(50)[] DEFAULT '{}',

    -- 授权许可类型列表（如：authorization_code, refresh_token, client_credentials 等）
                                          AUTHORIZATION_GRANT_TYPES VARCHAR(50)[] DEFAULT '{}',

    -- 重定向URI列表（授权回调地址）
                                          REDIRECT_URIS TEXT[] DEFAULT '{}',

    -- 登出后重定向URI列表
                                          POST_LOGOUT_REDIRECT_URIS TEXT[] DEFAULT '{}',

    -- 授权作用域列表（如：read, write, openid 等）
                                          SCOPES VARCHAR(100)[] DEFAULT '{}',

    -- 客户端设置（JSONB格式，如 require-proof-key, require-authorization-consent 等）
                                          CLIENT_SETTINGS JSONB DEFAULT '{}'::jsonb,

    -- 令牌设置（JSONB格式，如 access-token-time-to-live, refresh-token-time-to-live 等）
                                          TOKEN_SETTINGS JSONB DEFAULT '{}'::jsonb,

    -- 创建人ID（用户唯一标识）
                                          CREATOR_ID VARCHAR(36),

    -- 创建人姓名
                                          CREATED_BY VARCHAR(100),

    -- 创建时间
                                          CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- 更新人ID（用户唯一标识）
                                          UPDATER_ID VARCHAR(36),

    -- 更新人姓名
                                          UPDATED_BY VARCHAR(100),

    -- 更新时间
                                          UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- 乐观锁版本号（用于并发控制）
                                          VERSION INTEGER DEFAULT 1,

    -- 是否启用（逻辑删除替代方案）
                                          IS_ACTIVE BOOLEAN DEFAULT TRUE,

    -- 客户端描述信息
                                          DESCRIPTION TEXT,

    -- 约束：客户端ID长度至少3位
                                          CONSTRAINT chk_client_id CHECK (char_length(CLIENT_ID) >= 3),

    -- 约束：客户端名称不能为空
                                          CONSTRAINT chk_client_name CHECK (char_length(CLIENT_NAME) >= 1),

    -- 约束：数组字段不允许为NULL（但可为空数组）
                                          CONSTRAINT chk_arrays_not_null CHECK (
                                              CLIENT_AUTHENTICATION_METHODS IS NOT NULL AND
                                              AUTHORIZATION_GRANT_TYPES IS NOT NULL AND
                                              REDIRECT_URIS IS NOT NULL AND
                                              POST_LOGOUT_REDIRECT_URIS IS NOT NULL AND
                                              SCOPES IS NOT NULL
                                              )
);

-- 创建索引：加速通过 CLIENT_ID 查询
CREATE INDEX idx_oauth2_client_id ON oauth2_registered_client (CLIENT_ID);

-- 添加表注释
COMMENT ON TABLE oauth2_registered_client IS 'OAuth2 已注册客户端信息表';

-- 添加字段注释
COMMENT ON COLUMN oauth2_registered_client.ID IS '主键（分布式ID）';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_ID IS '客户端唯一标识符';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_ID_ISSUED_AT IS '客户端ID颁发时间';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_SECRET IS '客户端密钥（加密存储）';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_SECRET_EXPIRES_AT IS '客户端密钥过期时间';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_NAME IS '客户端名称（用于展示）';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_AUTHENTICATION_METHODS IS '客户端认证方式列表（如：client_secret_basic, client_secret_post 等）';
COMMENT ON COLUMN oauth2_registered_client.AUTHORIZATION_GRANT_TYPES IS '授权许可类型列表（如：authorization_code, refresh_token, client_credentials 等）';
COMMENT ON COLUMN oauth2_registered_client.REDIRECT_URIS IS '重定向URI列表（授权回调地址）';
COMMENT ON COLUMN oauth2_registered_client.POST_LOGOUT_REDIRECT_URIS IS '登出后重定向URI列表';
COMMENT ON COLUMN oauth2_registered_client.SCOPES IS '授权作用域列表（如：read, write, openid 等）';
COMMENT ON COLUMN oauth2_registered_client.CLIENT_SETTINGS IS '客户端设置（JSONB格式，如 require-proof-key, require-authorization-consent 等）';
COMMENT ON COLUMN oauth2_registered_client.TOKEN_SETTINGS IS '令牌设置（JSONB格式，如 access-token-time-to-live, refresh-token-time-to-live 等）';
COMMENT ON COLUMN oauth2_registered_client.CREATOR_ID IS '创建人ID（用户唯一标识）';
COMMENT ON COLUMN oauth2_registered_client.CREATED_BY IS '创建人姓名';
COMMENT ON COLUMN oauth2_registered_client.CREATED_TIME IS '创建时间';
COMMENT ON COLUMN oauth2_registered_client.UPDATER_ID IS '更新人ID（用户唯一标识）';
COMMENT ON COLUMN oauth2_registered_client.UPDATED_BY IS '更新人姓名';
COMMENT ON COLUMN oauth2_registered_client.UPDATED_TIME IS '更新时间';
COMMENT ON COLUMN oauth2_registered_client.VERSION IS '乐观锁版本号（用于并发控制）';
COMMENT ON COLUMN oauth2_registered_client.IS_ACTIVE IS '是否启用（逻辑删除替代方案）';
COMMENT ON COLUMN oauth2_registered_client.DESCRIPTION IS '客户端描述信息';