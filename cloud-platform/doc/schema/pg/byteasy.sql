--
-- PostgreSQL database dump
--

\restrict NJzIlBIr0lAbbhlVPuFcNagLINb7UZWDz3XlJ7mQdOq0K36OZef3wfXbkXSjcnm

-- Dumped from database version 16.14 (Ubuntu 16.14-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.14 (Ubuntu 16.14-0ubuntu0.24.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: byteasy_cloud_platform; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA byteasy_cloud_platform;


ALTER SCHEMA byteasy_cloud_platform OWNER TO postgres;

--
-- Name: knowledge_base; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA knowledge_base;


ALTER SCHEMA knowledge_base OWNER TO postgres;

--
-- Name: micro_product_assistant; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA micro_product_assistant;


ALTER SCHEMA micro_product_assistant OWNER TO postgres;

--
-- Name: tuna_cloud_platform; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA tuna_cloud_platform;


ALTER SCHEMA tuna_cloud_platform OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: oauth2_authorization; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.oauth2_authorization (
    id character varying(100) NOT NULL,
    registered_client_id character varying(100) NOT NULL,
    principal_name character varying(200) NOT NULL,
    authorization_grant_type character varying(100) NOT NULL,
    authorized_scopes character varying(1000) DEFAULT NULL::character varying,
    attributes text,
    state character varying(500) DEFAULT NULL::character varying,
    authorization_code_value text,
    authorization_code_issued_at timestamp(6) without time zone,
    authorization_code_expires_at timestamp(6) without time zone,
    authorization_code_metadata text,
    access_token_value text,
    access_token_issued_at timestamp(6) without time zone,
    access_token_expires_at timestamp(6) without time zone,
    access_token_metadata text,
    access_token_type character varying(100) DEFAULT NULL::character varying,
    access_token_scopes character varying(1000) DEFAULT NULL::character varying,
    oidc_id_token_value text,
    oidc_id_token_issued_at timestamp(6) without time zone,
    oidc_id_token_expires_at timestamp(6) without time zone,
    oidc_id_token_metadata text,
    refresh_token_value text,
    refresh_token_issued_at timestamp(6) without time zone,
    refresh_token_expires_at timestamp(6) without time zone,
    refresh_token_metadata text,
    user_code_value text,
    user_code_issued_at timestamp(6) without time zone,
    user_code_expires_at timestamp(6) without time zone,
    user_code_metadata text,
    device_code_value text,
    device_code_issued_at timestamp(6) without time zone,
    device_code_expires_at timestamp(6) without time zone,
    device_code_metadata text
);


ALTER TABLE byteasy_cloud_platform.oauth2_authorization OWNER TO postgres;

--
-- Name: oauth2_authorization_consent; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.oauth2_authorization_consent (
    registered_client_id character varying(100) NOT NULL,
    principal_name character varying(200) NOT NULL,
    authorities character varying(1000) NOT NULL
);


ALTER TABLE byteasy_cloud_platform.oauth2_authorization_consent OWNER TO postgres;

--
-- Name: oauth2_registered_client; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.oauth2_registered_client (
    id character varying(36) DEFAULT gen_random_uuid() NOT NULL,
    client_id character varying(100) NOT NULL,
    client_id_issued_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    client_secret character varying(500),
    client_secret_expires_at timestamp(6) without time zone,
    client_name character varying(200),
    client_authentication_methods character varying(50)[] DEFAULT '{}'::character varying[],
    authorization_grant_types character varying(50)[] DEFAULT '{}'::character varying[],
    redirect_uris text[] DEFAULT '{}'::text[],
    post_logout_redirect_uris text[] DEFAULT '{}'::text[],
    scopes character varying(100)[] DEFAULT '{}'::character varying[],
    client_settings jsonb DEFAULT '{}'::jsonb,
    token_settings jsonb DEFAULT '{}'::jsonb,
    creator_id character varying(36),
    created_by character varying(100),
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updater_id character varying(36),
    updated_by character varying(100),
    updated_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    version integer DEFAULT 1,
    is_active boolean DEFAULT true,
    description text,
    CONSTRAINT chk_arrays_not_null CHECK (((client_authentication_methods IS NOT NULL) AND (authorization_grant_types IS NOT NULL) AND (redirect_uris IS NOT NULL) AND (post_logout_redirect_uris IS NOT NULL) AND (scopes IS NOT NULL))),
    CONSTRAINT chk_client_id CHECK ((char_length((client_id)::text) >= 3)),
    CONSTRAINT chk_client_name CHECK ((char_length((client_name)::text) >= 1))
);


ALTER TABLE byteasy_cloud_platform.oauth2_registered_client OWNER TO postgres;

--
-- Name: TABLE oauth2_registered_client; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.oauth2_registered_client IS 'OAuth2 已注册客户端信息表';


--
-- Name: COLUMN oauth2_registered_client.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.id IS '主键（分布式ID）';


--
-- Name: COLUMN oauth2_registered_client.client_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_id IS '客户端唯一标识符';


--
-- Name: COLUMN oauth2_registered_client.client_id_issued_at; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_id_issued_at IS '客户端ID颁发时间';


--
-- Name: COLUMN oauth2_registered_client.client_secret; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_secret IS '客户端密钥（加密存储）';


--
-- Name: COLUMN oauth2_registered_client.client_secret_expires_at; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_secret_expires_at IS '客户端密钥过期时间';


--
-- Name: COLUMN oauth2_registered_client.client_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_name IS '客户端名称（用于展示）';


--
-- Name: COLUMN oauth2_registered_client.client_authentication_methods; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_authentication_methods IS '客户端认证方式列表（如：client_secret_basic, client_secret_post 等）';


--
-- Name: COLUMN oauth2_registered_client.authorization_grant_types; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.authorization_grant_types IS '授权许可类型列表（如：authorization_code, refresh_token, client_credentials 等）';


--
-- Name: COLUMN oauth2_registered_client.redirect_uris; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.redirect_uris IS '重定向URI列表（授权回调地址）';


--
-- Name: COLUMN oauth2_registered_client.post_logout_redirect_uris; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.post_logout_redirect_uris IS '登出后重定向URI列表';


--
-- Name: COLUMN oauth2_registered_client.scopes; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.scopes IS '授权作用域列表（如：read, write, openid 等）';


--
-- Name: COLUMN oauth2_registered_client.client_settings; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.client_settings IS '客户端设置（JSONB格式，如 require-proof-key, require-authorization-consent 等）';


--
-- Name: COLUMN oauth2_registered_client.token_settings; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.token_settings IS '令牌设置（JSONB格式，如 access-token-time-to-live, refresh-token-time-to-live 等）';


--
-- Name: COLUMN oauth2_registered_client.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.creator_id IS '创建人ID（用户唯一标识）';


--
-- Name: COLUMN oauth2_registered_client.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.created_by IS '创建人姓名';


--
-- Name: COLUMN oauth2_registered_client.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.created_time IS '创建时间';


--
-- Name: COLUMN oauth2_registered_client.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.updater_id IS '更新人ID（用户唯一标识）';


--
-- Name: COLUMN oauth2_registered_client.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.updated_by IS '更新人姓名';


--
-- Name: COLUMN oauth2_registered_client.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.updated_time IS '更新时间';


--
-- Name: COLUMN oauth2_registered_client.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.version IS '乐观锁版本号（用于并发控制）';


--
-- Name: COLUMN oauth2_registered_client.is_active; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.is_active IS '是否启用（逻辑删除替代方案）';


--
-- Name: COLUMN oauth2_registered_client.description; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client.description IS '客户端描述信息';


--
-- Name: oauth2_registered_client_openapi; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.oauth2_registered_client_openapi (
    oauth2_registered_client_id character varying(32) NOT NULL,
    openapi_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.oauth2_registered_client_openapi OWNER TO postgres;

--
-- Name: TABLE oauth2_registered_client_openapi; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.oauth2_registered_client_openapi IS 'OAuth2客户端与平台资源的授权关联表';


--
-- Name: COLUMN oauth2_registered_client_openapi.oauth2_registered_client_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client_openapi.oauth2_registered_client_id IS 'OAuth2客户端ID，引用oauth2_registered_client表';


--
-- Name: COLUMN oauth2_registered_client_openapi.openapi_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client_openapi.openapi_id IS '平台资源ID，引用openapi_id';


--
-- Name: COLUMN oauth2_registered_client_openapi.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client_openapi.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN oauth2_registered_client_openapi.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client_openapi.created_by IS '创建人名称';


--
-- Name: COLUMN oauth2_registered_client_openapi.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.oauth2_registered_client_openapi.created_time IS '创建时间';


--
-- Name: platform_dictionary; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_dictionary (
    id bigint NOT NULL,
    group_id bigint,
    code character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_dictionary OWNER TO postgres;

--
-- Name: TABLE platform_dictionary; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_dictionary IS '字典表';


--
-- Name: COLUMN platform_dictionary.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.id IS '主键';


--
-- Name: COLUMN platform_dictionary.group_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.group_id IS '分组编号';


--
-- Name: COLUMN platform_dictionary.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.code IS '编码';


--
-- Name: COLUMN platform_dictionary.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.name IS '名称';


--
-- Name: COLUMN platform_dictionary.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.status IS '状态';


--
-- Name: COLUMN platform_dictionary.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.remark IS '备注';


--
-- Name: COLUMN platform_dictionary.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.creator_id IS '创建人账户编号';


--
-- Name: COLUMN platform_dictionary.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_dictionary.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.created_time IS '创建时间';


--
-- Name: COLUMN platform_dictionary.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.updater_id IS '更新人账户编号';


--
-- Name: COLUMN platform_dictionary.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_dictionary.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.updated_time IS '更新时间';


--
-- Name: COLUMN platform_dictionary.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary.version IS '乐观锁版本号';


--
-- Name: platform_dictionary1_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_dictionary1_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_dictionary1_id_seq OWNER TO postgres;

--
-- Name: platform_dictionary1_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_dictionary1_id_seq OWNED BY byteasy_cloud_platform.platform_dictionary.id;


--
-- Name: platform_dictionary_item; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_dictionary_item (
    id bigint NOT NULL,
    dictionary_id bigint NOT NULL,
    code character varying(64) NOT NULL,
    label character varying(128) NOT NULL,
    value character varying(128) NOT NULL,
    is_default smallint DEFAULT 0 NOT NULL,
    css character varying(128),
    show_order smallint DEFAULT 1 NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_dictionary_item OWNER TO postgres;

--
-- Name: TABLE platform_dictionary_item; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_dictionary_item IS '字典项目定义表';


--
-- Name: COLUMN platform_dictionary_item.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.id IS '主键';


--
-- Name: COLUMN platform_dictionary_item.dictionary_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.dictionary_id IS '字典';


--
-- Name: COLUMN platform_dictionary_item.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.code IS '编码';


--
-- Name: COLUMN platform_dictionary_item.label; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.label IS '标签';


--
-- Name: COLUMN platform_dictionary_item.value; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.value IS '值';


--
-- Name: COLUMN platform_dictionary_item.is_default; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.is_default IS '是否默认';


--
-- Name: COLUMN platform_dictionary_item.css; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.css IS '显示样式';


--
-- Name: COLUMN platform_dictionary_item.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.show_order IS '显示顺序';


--
-- Name: COLUMN platform_dictionary_item.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.status IS '状态';


--
-- Name: COLUMN platform_dictionary_item.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.remark IS '备注';


--
-- Name: COLUMN platform_dictionary_item.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_dictionary_item.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_dictionary_item.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.created_time IS '创建时间';


--
-- Name: COLUMN platform_dictionary_item.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_dictionary_item.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_dictionary_item.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.updated_time IS '更新时间';


--
-- Name: COLUMN platform_dictionary_item.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_dictionary_item.version IS '乐观锁版本号';


--
-- Name: platform_dictionary_item_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_dictionary_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_dictionary_item_id_seq OWNER TO postgres;

--
-- Name: platform_dictionary_item_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_dictionary_item_id_seq OWNED BY byteasy_cloud_platform.platform_dictionary_item.id;


--
-- Name: platform_entity_define; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_entity_define (
    id bigint NOT NULL,
    code character varying(512) NOT NULL,
    service_code character varying(128) NOT NULL,
    name character varying(512) NOT NULL,
    url character varying(512),
    remark character varying(2048) DEFAULT NULL::character varying,
    title_column_name character varying(128) DEFAULT 'name'::character varying,
    operation_column_width integer DEFAULT 160,
    query_params character varying(512) DEFAULT NULL::character varying,
    form_component character varying(220) DEFAULT NULL::character varying,
    form_width character varying(20) DEFAULT '50%'::character varying,
    dialog_model smallint,
    pagination_visible smallint DEFAULT 1,
    excel_enable smallint DEFAULT 0,
    query_form_visible smallint DEFAULT 1,
    excel_import_component character varying(220) DEFAULT NULL::character varying,
    row_key character varying(120) DEFAULT NULL::character varying,
    default_expand_all smallint DEFAULT 0,
    tree_props character varying(512) DEFAULT NULL::character varying,
    query_mode smallint DEFAULT 0,
    show_lang character varying(60) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1,
    show_summary smallint DEFAULT 0,
    query_bar_buttons character varying(512) DEFAULT NULL::character varying,
    operation_buttons character varying(512) DEFAULT NULL::character varying,
    table_name character varying(128) DEFAULT NULL::character varying
);


ALTER TABLE byteasy_cloud_platform.platform_entity_define OWNER TO postgres;

--
-- Name: TABLE platform_entity_define; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_entity_define IS '平台模型定义';


--
-- Name: COLUMN platform_entity_define.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.id IS '编号';


--
-- Name: COLUMN platform_entity_define.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.code IS '编码';


--
-- Name: COLUMN platform_entity_define.service_code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.service_code IS '服务编码';


--
-- Name: COLUMN platform_entity_define.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.name IS '名称';


--
-- Name: COLUMN platform_entity_define.url; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.url IS '请求接口地址';


--
-- Name: COLUMN platform_entity_define.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.remark IS '说明';


--
-- Name: COLUMN platform_entity_define.title_column_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.title_column_name IS '卡片模式标题字段名称';


--
-- Name: COLUMN platform_entity_define.operation_column_width; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.operation_column_width IS '操作列宽度';


--
-- Name: COLUMN platform_entity_define.query_params; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.query_params IS '默认查询条件';


--
-- Name: COLUMN platform_entity_define.form_component; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.form_component IS '自定义表单组件';


--
-- Name: COLUMN platform_entity_define.form_width; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.form_width IS '弹窗宽度';


--
-- Name: COLUMN platform_entity_define.dialog_model; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.dialog_model IS '弹窗方式';


--
-- Name: COLUMN platform_entity_define.pagination_visible; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.pagination_visible IS '启用分页插件';


--
-- Name: COLUMN platform_entity_define.excel_enable; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.excel_enable IS '是否启用excel导入导出';


--
-- Name: COLUMN platform_entity_define.query_form_visible; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.query_form_visible IS '开启查询条件栏';


--
-- Name: COLUMN platform_entity_define.excel_import_component; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.excel_import_component IS '自定义导入excel组件';


--
-- Name: COLUMN platform_entity_define.row_key; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.row_key IS '树形表格rowkey';


--
-- Name: COLUMN platform_entity_define.default_expand_all; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.default_expand_all IS '默认展开树';


--
-- Name: COLUMN platform_entity_define.tree_props; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.tree_props IS '树属性名称';


--
-- Name: COLUMN platform_entity_define.query_mode; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.query_mode IS '查询模式';


--
-- Name: COLUMN platform_entity_define.show_lang; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.show_lang IS '显示语言';


--
-- Name: COLUMN platform_entity_define.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.creator_id IS '创建人编号';


--
-- Name: COLUMN platform_entity_define.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.created_by IS '创建人';


--
-- Name: COLUMN platform_entity_define.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.created_time IS '创建时间';


--
-- Name: COLUMN platform_entity_define.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.updater_id IS '更新人编号';


--
-- Name: COLUMN platform_entity_define.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.updated_by IS '更新人';


--
-- Name: COLUMN platform_entity_define.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.updated_time IS '更新时间';


--
-- Name: COLUMN platform_entity_define.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.version IS '乐观锁版本';


--
-- Name: COLUMN platform_entity_define.show_summary; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.show_summary IS '显示表尾合计';


--
-- Name: COLUMN platform_entity_define.query_bar_buttons; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.query_bar_buttons IS '查询栏拓展按钮';


--
-- Name: COLUMN platform_entity_define.operation_buttons; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.operation_buttons IS '操作列拓展按钮';


--
-- Name: COLUMN platform_entity_define.table_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_entity_define.table_name IS '数据库表名';


--
-- Name: platform_entity_define_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_entity_define_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_entity_define_id_seq OWNER TO postgres;

--
-- Name: platform_entity_define_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_entity_define_id_seq OWNED BY byteasy_cloud_platform.platform_entity_define.id;


--
-- Name: platform_group_i18n; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_group_i18n (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(1024) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_group_i18n OWNER TO postgres;

--
-- Name: TABLE platform_group_i18n; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_group_i18n IS '平台国际化翻译语言包';


--
-- Name: COLUMN platform_group_i18n.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.id IS '主键';


--
-- Name: COLUMN platform_group_i18n.group_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.group_id IS '应用编号';


--
-- Name: COLUMN platform_group_i18n.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.code IS '编码';


--
-- Name: COLUMN platform_group_i18n.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.name IS '文案';


--
-- Name: COLUMN platform_group_i18n.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_group_i18n.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_group_i18n.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.created_time IS '创建时间';


--
-- Name: COLUMN platform_group_i18n.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_group_i18n.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_group_i18n.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.updated_time IS '更新时间';


--
-- Name: COLUMN platform_group_i18n.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_group_i18n.version IS '乐观锁版本号';


--
-- Name: platform_group_i18n_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_group_i18n_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_group_i18n_id_seq OWNER TO postgres;

--
-- Name: platform_group_i18n_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_group_i18n_id_seq OWNED BY byteasy_cloud_platform.platform_group_i18n.id;


--
-- Name: platform_job; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_job (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    org_id bigint NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    show_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_job OWNER TO postgres;

--
-- Name: TABLE platform_job; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_job IS '平台岗位定义表';


--
-- Name: COLUMN platform_job.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_job.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.code IS '编码（业务唯一标识）';


--
-- Name: COLUMN platform_job.org_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.org_id IS '组织编码';


--
-- Name: COLUMN platform_job.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.status IS '状态：0-禁用，1-正常';


--
-- Name: COLUMN platform_job.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.name IS '名称';


--
-- Name: COLUMN platform_job.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.remark IS '备注';


--
-- Name: COLUMN platform_job.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.show_order IS '显示顺序，值越小，越靠前';


--
-- Name: COLUMN platform_job.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_job.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_job.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.created_time IS '创建时间';


--
-- Name: COLUMN platform_job.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_job.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_job.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.updated_time IS '更新时间';


--
-- Name: COLUMN platform_job.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job.version IS '乐观锁版本号';


--
-- Name: platform_job_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_job_id_seq OWNER TO postgres;

--
-- Name: platform_job_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_job_id_seq OWNED BY byteasy_cloud_platform.platform_job.id;


--
-- Name: platform_job_resource; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_job_resource (
    job_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_job_resource OWNER TO postgres;

--
-- Name: TABLE platform_job_resource; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_job_resource IS '平台用户分组授权表';


--
-- Name: COLUMN platform_job_resource.job_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_resource.job_id IS '平台用户分组ID';


--
-- Name: COLUMN platform_job_resource.resource_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_job_resource.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_job_resource.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_job_resource.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_resource.created_time IS '创建时间';


--
-- Name: platform_job_user; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_job_user (
    user_id bigint NOT NULL,
    job_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_job_user OWNER TO postgres;

--
-- Name: TABLE platform_job_user; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_job_user IS '平台岗位用户关联表';


--
-- Name: COLUMN platform_job_user.user_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_user.user_id IS '平台资源关联的角色ID';


--
-- Name: COLUMN platform_job_user.job_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_user.job_id IS '平台资源ID';


--
-- Name: COLUMN platform_job_user.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_user.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_job_user.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_user.created_by IS '创建人名称';


--
-- Name: COLUMN platform_job_user.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_job_user.created_time IS '创建时间';


--
-- Name: platform_login_log; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_login_log (
    id bigint NOT NULL,
    type smallint DEFAULT 0 NOT NULL,
    username character varying(64) NOT NULL,
    real_name character varying(64) DEFAULT ''::character varying,
    ip_address character varying(64) DEFAULT ''::character varying,
    session_id character varying(64) DEFAULT ''::character varying,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_login_log OWNER TO postgres;

--
-- Name: TABLE platform_login_log; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_login_log IS '登录日志';


--
-- Name: COLUMN platform_login_log.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.id IS '主键';


--
-- Name: COLUMN platform_login_log.type; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.type IS '类型';


--
-- Name: COLUMN platform_login_log.username; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.username IS '账号';


--
-- Name: COLUMN platform_login_log.real_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.real_name IS '姓名';


--
-- Name: COLUMN platform_login_log.ip_address; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.ip_address IS 'IP';


--
-- Name: COLUMN platform_login_log.session_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.session_id IS 'session';


--
-- Name: COLUMN platform_login_log.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.remark IS '备注';


--
-- Name: COLUMN platform_login_log.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.creator_id IS '创建人账户编号';


--
-- Name: COLUMN platform_login_log.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_login_log.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_login_log.created_time IS '创建时间';


--
-- Name: platform_login_log_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_login_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_login_log_id_seq OWNER TO postgres;

--
-- Name: platform_login_log_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_login_log_id_seq OWNED BY byteasy_cloud_platform.platform_login_log.id;


--
-- Name: platform_openapi; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_openapi (
    id bigint NOT NULL,
    service_id bigint NOT NULL,
    name character varying(512) NOT NULL,
    http_method character varying(32) NOT NULL,
    class_method character varying(512) NOT NULL,
    uri character varying(256) DEFAULT NULL::character varying,
    remark character varying(2048) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_openapi OWNER TO postgres;

--
-- Name: TABLE platform_openapi; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_openapi IS '平台接口定义';


--
-- Name: COLUMN platform_openapi.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.id IS '编号';


--
-- Name: COLUMN platform_openapi.service_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.service_id IS '服务编号';


--
-- Name: COLUMN platform_openapi.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.name IS '接口名称';


--
-- Name: COLUMN platform_openapi.http_method; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.http_method IS 'Http方法';


--
-- Name: COLUMN platform_openapi.class_method; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.class_method IS '类方法';


--
-- Name: COLUMN platform_openapi.uri; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.uri IS 'URI';


--
-- Name: COLUMN platform_openapi.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.remark IS '说明';


--
-- Name: COLUMN platform_openapi.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.creator_id IS '创建人编号';


--
-- Name: COLUMN platform_openapi.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.created_by IS '创建人';


--
-- Name: COLUMN platform_openapi.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.created_time IS '创建时间';


--
-- Name: COLUMN platform_openapi.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.updater_id IS '更新人编号';


--
-- Name: COLUMN platform_openapi.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.updated_by IS '更新人';


--
-- Name: COLUMN platform_openapi.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.updated_time IS '更新时间';


--
-- Name: COLUMN platform_openapi.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_openapi.version IS '版本';


--
-- Name: platform_openapi_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_openapi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_openapi_id_seq OWNER TO postgres;

--
-- Name: platform_openapi_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_openapi_id_seq OWNED BY byteasy_cloud_platform.platform_openapi.id;


--
-- Name: platform_org; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_org (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    parent_id bigint NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    show_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_org OWNER TO postgres;

--
-- Name: TABLE platform_org; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_org IS '平台组织定义表';


--
-- Name: COLUMN platform_org.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_org.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.code IS '编码（业务唯一标识）';


--
-- Name: COLUMN platform_org.parent_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.parent_id IS '父级编码';


--
-- Name: COLUMN platform_org.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.status IS '状态：0-禁用，1-正常';


--
-- Name: COLUMN platform_org.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.name IS '名称';


--
-- Name: COLUMN platform_org.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.remark IS '备注';


--
-- Name: COLUMN platform_org.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.show_order IS '显示顺序，值越小，越靠前';


--
-- Name: COLUMN platform_org.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_org.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_org.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.created_time IS '创建时间';


--
-- Name: COLUMN platform_org.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_org.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_org.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.updated_time IS '更新时间';


--
-- Name: COLUMN platform_org.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org.version IS '乐观锁版本号';


--
-- Name: platform_org_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_org_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_org_id_seq OWNER TO postgres;

--
-- Name: platform_org_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_org_id_seq OWNED BY byteasy_cloud_platform.platform_org.id;


--
-- Name: platform_org_resource; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_org_resource (
    org_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_org_resource OWNER TO postgres;

--
-- Name: TABLE platform_org_resource; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_org_resource IS '平台组织授权表';


--
-- Name: COLUMN platform_org_resource.org_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org_resource.org_id IS '平台用户分组ID';


--
-- Name: COLUMN platform_org_resource.resource_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_org_resource.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_org_resource.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_org_resource.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_org_resource.created_time IS '创建时间';


--
-- Name: platform_realm; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_realm (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_realm OWNER TO postgres;

--
-- Name: TABLE platform_realm; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_realm IS '平台用户领域定义表';


--
-- Name: COLUMN platform_realm.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_realm.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.code IS '分组编码（业务唯一标识，用于关联资源）';


--
-- Name: COLUMN platform_realm.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.name IS '分组名称';


--
-- Name: COLUMN platform_realm.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.remark IS '备注';


--
-- Name: COLUMN platform_realm.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_realm.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_realm.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.created_time IS '创建时间';


--
-- Name: COLUMN platform_realm.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_realm.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_realm.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.updated_time IS '更新时间';


--
-- Name: COLUMN platform_realm.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_realm.version IS '乐观锁版本号';


--
-- Name: platform_realm_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_realm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_realm_id_seq OWNER TO postgres;

--
-- Name: platform_realm_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_realm_id_seq OWNED BY byteasy_cloud_platform.platform_realm.id;


--
-- Name: platform_resource; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_resource (
    id bigint NOT NULL,
    code character varying(150) NOT NULL,
    parent_id bigint,
    group_id bigint,
    name character varying(2048) NOT NULL,
    icon character varying(2048) DEFAULT NULL::character varying,
    visible smallint DEFAULT 1 NOT NULL,
    type smallint DEFAULT 1 NOT NULL,
    open_tab smallint DEFAULT 0 NOT NULL,
    affix smallint DEFAULT 0 NOT NULL,
    keep_alive smallint DEFAULT 0 NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    layout character varying(128) DEFAULT NULL::character varying,
    component character varying(256) DEFAULT NULL::character varying,
    router_url character varying(512),
    redirect_url character varying(512) DEFAULT NULL::character varying,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_resource OWNER TO postgres;

--
-- Name: TABLE platform_resource; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_resource IS '平台资源定义表（菜单/目录/按钮权限）';


--
-- Name: COLUMN platform_resource.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_resource.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.code IS '资源编码（业务唯一标识）';


--
-- Name: COLUMN platform_resource.parent_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.parent_id IS '上一级资源编码（构建树形结构）';


--
-- Name: COLUMN platform_resource.group_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.group_id IS '分组编码';


--
-- Name: COLUMN platform_resource.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.name IS '资源名称';


--
-- Name: COLUMN platform_resource.icon; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.icon IS '资源图标';


--
-- Name: COLUMN platform_resource.visible; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.visible IS '是否显示：0-否，1-显示';


--
-- Name: COLUMN platform_resource.type; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.type IS '资源类型：0-目录，1-菜单，2-按钮';


--
-- Name: COLUMN platform_resource.open_tab; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.open_tab IS '是否打开新的tab：0-否 1-打开';


--
-- Name: COLUMN platform_resource.affix; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.affix IS '在标签模式下是否固定：0-不固定，1-固定';


--
-- Name: COLUMN platform_resource.keep_alive; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.keep_alive IS '是否开启前端缓存：0-不开启，1-开启';


--
-- Name: COLUMN platform_resource.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.show_order IS '显示顺序，值越小越靠前';


--
-- Name: COLUMN platform_resource.layout; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.layout IS '前端布局';


--
-- Name: COLUMN platform_resource.component; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.component IS '前端组件路径';


--
-- Name: COLUMN platform_resource.router_url; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.router_url IS '前端路由地址';


--
-- Name: COLUMN platform_resource.redirect_url; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.redirect_url IS '重定向地址';


--
-- Name: COLUMN platform_resource.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.remark IS '备注';


--
-- Name: COLUMN platform_resource.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_resource.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_resource.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.created_time IS '创建时间';


--
-- Name: COLUMN platform_resource.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_resource.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_resource.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.updated_time IS '更新时间';


--
-- Name: COLUMN platform_resource.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource.version IS '乐观锁版本号';


--
-- Name: platform_resource_category; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_resource_category (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(2000) NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1,
    parent_id bigint
);


ALTER TABLE byteasy_cloud_platform.platform_resource_category OWNER TO postgres;

--
-- Name: TABLE platform_resource_category; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_resource_category IS '平台资源分类表';


--
-- Name: COLUMN platform_resource_category.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_resource_category.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.code IS '编码';


--
-- Name: COLUMN platform_resource_category.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.name IS '名称';


--
-- Name: COLUMN platform_resource_category.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.show_order IS '显示顺序';


--
-- Name: COLUMN platform_resource_category.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.remark IS '备注';


--
-- Name: COLUMN platform_resource_category.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_resource_category.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_resource_category.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.created_time IS '创建时间';


--
-- Name: COLUMN platform_resource_category.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_resource_category.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_resource_category.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.updated_time IS '更新时间';


--
-- Name: COLUMN platform_resource_category.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.version IS '乐观锁版本号';


--
-- Name: COLUMN platform_resource_category.parent_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_category.parent_id IS '父级编号';


--
-- Name: platform_resource_category_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_resource_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_resource_category_id_seq OWNER TO postgres;

--
-- Name: platform_resource_category_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_resource_category_id_seq OWNED BY byteasy_cloud_platform.platform_resource_category.id;


--
-- Name: platform_resource_group; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_resource_group (
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(2048) NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    router character varying(256),
    navbar_menu smallint DEFAULT 0 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1,
    entry character varying(512),
    props character varying(512),
    is_microapp smallint DEFAULT 0
);


ALTER TABLE byteasy_cloud_platform.platform_resource_group OWNER TO postgres;

--
-- Name: TABLE platform_resource_group; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_resource_group IS '平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等）';


--
-- Name: COLUMN platform_resource_group.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_resource_group.category_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.category_id IS '资源分类';


--
-- Name: COLUMN platform_resource_group.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.code IS '编码';


--
-- Name: COLUMN platform_resource_group.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.name IS '名称';


--
-- Name: COLUMN platform_resource_group.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.show_order IS '显示顺序';


--
-- Name: COLUMN platform_resource_group.router; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.router IS '路由';


--
-- Name: COLUMN platform_resource_group.navbar_menu; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.navbar_menu IS '快捷菜单';


--
-- Name: COLUMN platform_resource_group.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.remark IS '备注';


--
-- Name: COLUMN platform_resource_group.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_resource_group.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_resource_group.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.created_time IS '创建时间';


--
-- Name: COLUMN platform_resource_group.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_resource_group.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_resource_group.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.updated_time IS '更新时间';


--
-- Name: COLUMN platform_resource_group.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.version IS '乐观锁版本号';


--
-- Name: COLUMN platform_resource_group.entry; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.entry IS '入口地址';


--
-- Name: COLUMN platform_resource_group.props; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.props IS '拓展属性';


--
-- Name: COLUMN platform_resource_group.is_microapp; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_group.is_microapp IS '是否微应用0:否 1：是';


--
-- Name: platform_resource_group_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_resource_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_resource_group_id_seq OWNER TO postgres;

--
-- Name: platform_resource_group_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_resource_group_id_seq OWNED BY byteasy_cloud_platform.platform_resource_group.id;


--
-- Name: platform_resource_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_resource_id_seq OWNER TO postgres;

--
-- Name: platform_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_resource_id_seq OWNED BY byteasy_cloud_platform.platform_resource.id;


--
-- Name: platform_resource_openapi; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_resource_openapi (
    resource_id bigint NOT NULL,
    openapi_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_resource_openapi OWNER TO postgres;

--
-- Name: TABLE platform_resource_openapi; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_resource_openapi IS 'OAuth2客户端与平台资源的授权关联表';


--
-- Name: COLUMN platform_resource_openapi.resource_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_openapi.resource_id IS 'OAuth2客户端ID，引用oauth2_registered_client表';


--
-- Name: COLUMN platform_resource_openapi.openapi_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_openapi.openapi_id IS '平台资源ID，引用openapi_id';


--
-- Name: COLUMN platform_resource_openapi.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_openapi.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_resource_openapi.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_openapi.created_by IS '创建人名称';


--
-- Name: COLUMN platform_resource_openapi.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_resource_openapi.created_time IS '创建时间';


--
-- Name: platform_role; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_role (
    id bigint NOT NULL,
    code character varying(128) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(1024) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_role OWNER TO postgres;

--
-- Name: TABLE platform_role; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_role IS '平台角色定义表';


--
-- Name: COLUMN platform_role.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.id IS '主键';


--
-- Name: COLUMN platform_role.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.code IS '编码';


--
-- Name: COLUMN platform_role.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.status IS '状态';


--
-- Name: COLUMN platform_role.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.name IS '名称';


--
-- Name: COLUMN platform_role.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.remark IS '备注';


--
-- Name: COLUMN platform_role.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_role.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_role.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.created_time IS '创建时间';


--
-- Name: COLUMN platform_role.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_role.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_role.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.updated_time IS '更新时间';


--
-- Name: COLUMN platform_role.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role.version IS '乐观锁版本号';


--
-- Name: platform_role_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_role_id_seq OWNER TO postgres;

--
-- Name: platform_role_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_role_id_seq OWNED BY byteasy_cloud_platform.platform_role.id;


--
-- Name: platform_role_resource; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_role_resource (
    role_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_role_resource OWNER TO postgres;

--
-- Name: TABLE platform_role_resource; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_role_resource IS '平台资源角色授权表';


--
-- Name: COLUMN platform_role_resource.role_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role_resource.role_id IS '平台资源关联的角色ID';


--
-- Name: COLUMN platform_role_resource.resource_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_role_resource.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_role_resource.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_role_resource.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_role_resource.created_time IS '创建时间';


--
-- Name: platform_schema_define; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_schema_define (
    id bigint NOT NULL,
    class_name character varying(255),
    name character varying(255) NOT NULL,
    cn_name character varying(255),
    i18n_key character varying(100),
    display_name character varying(1024),
    type smallint DEFAULT 0 NOT NULL,
    type_class_name character varying(255),
    is_show smallint DEFAULT 1 NOT NULL,
    is_sort smallint DEFAULT 0 NOT NULL,
    is_overflow smallint DEFAULT 0 NOT NULL,
    is_search smallint DEFAULT 1 NOT NULL,
    is_fixed smallint DEFAULT 0 NOT NULL,
    is_require smallint DEFAULT 0 NOT NULL,
    is_form smallint DEFAULT 1 NOT NULL,
    align smallint DEFAULT 0 NOT NULL,
    width integer DEFAULT 120,
    show_order integer DEFAULT 1,
    max_length integer DEFAULT 50,
    min_length integer DEFAULT 1,
    max integer DEFAULT 2147483647,
    min integer DEFAULT 0,
    "precision" integer DEFAULT 0,
    multiple smallint DEFAULT 0 NOT NULL,
    pattern smallint DEFAULT '-1'::integer NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1,
    service_name character varying(255),
    is_i18n smallint DEFAULT 0,
    relation_type smallint,
    external_class character varying(255) DEFAULT NULL::character varying,
    external_class_primary_field character varying(80) DEFAULT NULL::character varying,
    external_class_display_field character varying(80) DEFAULT NULL::character varying,
    ui_component smallint,
    field_name character varying(255) DEFAULT NULL::character varying,
    description character varying(1024) DEFAULT NULL::character varying,
    encrypt smallint DEFAULT 1 NOT NULL,
    desensitize smallint DEFAULT 1 NOT NULL,
    field_component character varying(512) DEFAULT NULL::character varying,
    inner_primary_field character varying(80) DEFAULT NULL::character varying,
    column_component character varying(512) DEFAULT NULL::character varying
);


ALTER TABLE byteasy_cloud_platform.platform_schema_define OWNER TO postgres;

--
-- Name: TABLE platform_schema_define; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_schema_define IS '列配置表（存储业务实体字段的展示/校验/表单等配置）';


--
-- Name: COLUMN platform_schema_define.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_schema_define.class_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.class_name IS '所属业务实体';


--
-- Name: COLUMN platform_schema_define.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.name IS '字段名';


--
-- Name: COLUMN platform_schema_define.cn_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.cn_name IS '中文名';


--
-- Name: COLUMN platform_schema_define.i18n_key; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.i18n_key IS '国际化Key';


--
-- Name: COLUMN platform_schema_define.display_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.display_name IS '国际化：名字';


--
-- Name: COLUMN platform_schema_define.type; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.type IS '属性类型';


--
-- Name: COLUMN platform_schema_define.type_class_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.type_class_name IS '属性类型类名';


--
-- Name: COLUMN platform_schema_define.is_show; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_show IS '是否显示：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_sort; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_sort IS '是否排序：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_overflow; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_overflow IS '是否省略：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_search; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_search IS '是否搜索：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_fixed; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_fixed IS '是否固定列：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_require; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_require IS '是否必填：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_form; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.is_form IS '是否表单字段：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.align; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.align IS '对齐方式：LEFT-左对齐，RIGHT-右对齐，CENTER-居中';


--
-- Name: COLUMN platform_schema_define.width; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.width IS '显示宽度';


--
-- Name: COLUMN platform_schema_define.show_order; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.show_order IS '显示顺序';


--
-- Name: COLUMN platform_schema_define.max_length; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.max_length IS '最大长度';


--
-- Name: COLUMN platform_schema_define.min_length; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.min_length IS '最小长度';


--
-- Name: COLUMN platform_schema_define.max; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.max IS '最大值';


--
-- Name: COLUMN platform_schema_define.min; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.min IS '最小值';


--
-- Name: COLUMN platform_schema_define."precision"; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define."precision" IS '精度';


--
-- Name: COLUMN platform_schema_define.multiple; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.multiple IS '是否支持多选：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.pattern; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.pattern IS '字段校验规则';


--
-- Name: COLUMN platform_schema_define.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_schema_define.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.created_by IS '创建人（用户ID）';


--
-- Name: COLUMN platform_schema_define.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.created_time IS '创建时间';


--
-- Name: COLUMN platform_schema_define.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_schema_define.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_schema_define.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.updated_time IS '更新时间';


--
-- Name: COLUMN platform_schema_define.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.version IS '乐观锁版本号';


--
-- Name: COLUMN platform_schema_define.service_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.service_name IS '服务名';


--
-- Name: COLUMN platform_schema_define.relation_type; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.relation_type IS '关联类型';


--
-- Name: COLUMN platform_schema_define.external_class; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.external_class IS '关联对象类名';


--
-- Name: COLUMN platform_schema_define.external_class_primary_field; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.external_class_primary_field IS '关联对象字段';


--
-- Name: COLUMN platform_schema_define.external_class_display_field; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.external_class_display_field IS '关联对象展示字段';


--
-- Name: COLUMN platform_schema_define.ui_component; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.ui_component IS '关联对象前端组件';


--
-- Name: COLUMN platform_schema_define.field_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.field_name IS '字段名';


--
-- Name: COLUMN platform_schema_define.description; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.description IS '说明';


--
-- Name: COLUMN platform_schema_define.encrypt; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.encrypt IS '是否加密';


--
-- Name: COLUMN platform_schema_define.desensitize; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.desensitize IS '是否脱敏';


--
-- Name: COLUMN platform_schema_define.field_component; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.field_component IS '表单自定义组件';


--
-- Name: COLUMN platform_schema_define.inner_primary_field; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.inner_primary_field IS '明细表关联字段';


--
-- Name: COLUMN platform_schema_define.column_component; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_schema_define.column_component IS '表格列自定义组件';


--
-- Name: platform_schema_define_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_schema_define_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_schema_define_id_seq OWNER TO postgres;

--
-- Name: platform_schema_define_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_schema_define_id_seq OWNED BY byteasy_cloud_platform.platform_schema_define.id;


--
-- Name: platform_service_instance; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_service_instance (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    context_path character varying(512) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_service_instance OWNER TO postgres;

--
-- Name: TABLE platform_service_instance; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_service_instance IS '平台服务定义表';


--
-- Name: COLUMN platform_service_instance.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.id IS '编号';


--
-- Name: COLUMN platform_service_instance.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.code IS '编码';


--
-- Name: COLUMN platform_service_instance.context_path; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.context_path IS '路径';


--
-- Name: COLUMN platform_service_instance.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.status IS '状态';


--
-- Name: COLUMN platform_service_instance.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.name IS '名称';


--
-- Name: COLUMN platform_service_instance.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.remark IS '备注';


--
-- Name: COLUMN platform_service_instance.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.creator_id IS '创建人编号';


--
-- Name: COLUMN platform_service_instance.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.created_by IS '创建人';


--
-- Name: COLUMN platform_service_instance.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.created_time IS '创建时间';


--
-- Name: COLUMN platform_service_instance.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.updater_id IS '更新人编号';


--
-- Name: COLUMN platform_service_instance.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.updated_by IS '更新人';


--
-- Name: COLUMN platform_service_instance.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.updated_time IS '更新时间';


--
-- Name: COLUMN platform_service_instance.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_service_instance.version IS '版本号';


--
-- Name: platform_service_instance_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_service_instance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_service_instance_id_seq OWNER TO postgres;

--
-- Name: platform_service_instance_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_service_instance_id_seq OWNED BY byteasy_cloud_platform.platform_service_instance.id;


--
-- Name: platform_user; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_user (
    id bigint NOT NULL,
    user_id uuid DEFAULT gen_random_uuid() NOT NULL,
    username character varying(64) NOT NULL,
    password character varying(256) NOT NULL,
    salt character varying(64) DEFAULT ''::character varying,
    account_expired_at timestamp(6) without time zone,
    credentials_expired_at timestamp(6) without time zone,
    real_name character varying(64) DEFAULT ''::character varying,
    nickname character varying(64) DEFAULT ''::character varying,
    gender smallint DEFAULT 0,
    birthday date,
    avatar character varying(512) DEFAULT ''::character varying,
    mobile character varying(20) DEFAULT ''::character varying,
    mobile_verified smallint,
    email character varying(128) DEFAULT ''::character varying,
    email_verified smallint,
    status smallint DEFAULT 1 NOT NULL,
    user_type smallint DEFAULT 0 NOT NULL,
    login_fail_count smallint DEFAULT 0 NOT NULL,
    lock_time timestamp(6) without time zone,
    last_login_time timestamp(6) without time zone,
    last_login_ip character varying(64) DEFAULT ''::character varying,
    tenant_id character varying(64) DEFAULT 'default'::character varying,
    realm_id bigint,
    org_id bigint,
    dept_id bigint,
    ext_info jsonb DEFAULT '{}'::jsonb,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64),
    created_by character varying(64) DEFAULT NULL::character varying,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    is_deleted smallint DEFAULT 0 NOT NULL,
    deleter_id character varying(64) DEFAULT ''::character varying,
    deleted_by character varying(64) DEFAULT ''::character varying,
    deleted_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_user OWNER TO postgres;

--
-- Name: COLUMN platform_user.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_user.user_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.user_id IS '业务唯一标识（UUID，对外暴露/集成用，避免自增ID泄露）';


--
-- Name: COLUMN platform_user.username; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.username IS '登录账号（支持手机号/邮箱/自定义账号，租户内唯一）';


--
-- Name: COLUMN platform_user.password; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.password IS '密码（加密存储）';


--
-- Name: COLUMN platform_user.salt; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.salt IS '密码盐值';


--
-- Name: COLUMN platform_user.real_name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.real_name IS '真实姓名';


--
-- Name: COLUMN platform_user.nickname; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.nickname IS '用户昵称';


--
-- Name: COLUMN platform_user.gender; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.gender IS '性别：0-未知，1-男，2-女';


--
-- Name: COLUMN platform_user.birthday; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.birthday IS '出生日期';


--
-- Name: COLUMN platform_user.avatar; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.avatar IS '头像URL';


--
-- Name: COLUMN platform_user.mobile; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.mobile IS '手机号';


--
-- Name: COLUMN platform_user.mobile_verified; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.mobile_verified IS '手机号是否验证';


--
-- Name: COLUMN platform_user.email; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.email IS '邮箱';


--
-- Name: COLUMN platform_user.email_verified; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.email_verified IS '邮箱是否验证';


--
-- Name: COLUMN platform_user.status; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.status IS '用户状态：0-禁用，1-正常，2-锁定，3-注销';


--
-- Name: COLUMN platform_user.user_type; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.user_type IS '用户类型：0-普通用户，1-管理员，2-系统用户，9-第三方用户';


--
-- Name: COLUMN platform_user.login_fail_count; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.login_fail_count IS '登录失败次数（防暴力破解）';


--
-- Name: COLUMN platform_user.lock_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.lock_time IS '账号锁定时间';


--
-- Name: COLUMN platform_user.last_login_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.last_login_time IS '最后登录时间';


--
-- Name: COLUMN platform_user.last_login_ip; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.last_login_ip IS '最后登录IP';


--
-- Name: COLUMN platform_user.tenant_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user.tenant_id IS '租户ID（多租户隔离）';


--
-- Name: platform_user_group; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_user_group (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp(6) without time zone,
    version integer DEFAULT 1
);


ALTER TABLE byteasy_cloud_platform.platform_user_group OWNER TO postgres;

--
-- Name: TABLE platform_user_group; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_user_group IS '平台用户分组定义表';


--
-- Name: COLUMN platform_user_group.id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_user_group.code; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.code IS '分组编码（业务唯一标识，用于关联资源）';


--
-- Name: COLUMN platform_user_group.name; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.name IS '分组名称';


--
-- Name: COLUMN platform_user_group.remark; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.remark IS '备注';


--
-- Name: COLUMN platform_user_group.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_user_group.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_user_group.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.created_time IS '创建时间';


--
-- Name: COLUMN platform_user_group.updater_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_user_group.updated_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_user_group.updated_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.updated_time IS '更新时间';


--
-- Name: COLUMN platform_user_group.version; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group.version IS '乐观锁版本号';


--
-- Name: platform_user_group_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_user_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_user_group_id_seq OWNER TO postgres;

--
-- Name: platform_user_group_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_user_group_id_seq OWNED BY byteasy_cloud_platform.platform_user_group.id;


--
-- Name: platform_user_group_resource; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_user_group_resource (
    group_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_user_group_resource OWNER TO postgres;

--
-- Name: TABLE platform_user_group_resource; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_user_group_resource IS '平台用户分组授权表';


--
-- Name: COLUMN platform_user_group_resource.group_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group_resource.group_id IS '平台用户分组ID';


--
-- Name: COLUMN platform_user_group_resource.resource_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_user_group_resource.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_user_group_resource.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_user_group_resource.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_group_resource.created_time IS '创建时间';


--
-- Name: platform_user_group_user; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_user_group_user (
    user_id bigint NOT NULL,
    group_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_user_group_user OWNER TO postgres;

--
-- Name: platform_user_id_seq; Type: SEQUENCE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE SEQUENCE byteasy_cloud_platform.platform_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE byteasy_cloud_platform.platform_user_id_seq OWNER TO postgres;

--
-- Name: platform_user_id_seq; Type: SEQUENCE OWNED BY; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER SEQUENCE byteasy_cloud_platform.platform_user_id_seq OWNED BY byteasy_cloud_platform.platform_user.id;


--
-- Name: platform_user_resource; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_user_resource (
    user_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_user_resource OWNER TO postgres;

--
-- Name: TABLE platform_user_resource; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_user_resource IS '平台用户授权表';


--
-- Name: COLUMN platform_user_resource.user_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_resource.user_id IS '平台资源关联的用户ID';


--
-- Name: COLUMN platform_user_resource.resource_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_resource.resource_id IS '平台资源关联的资源ID';


--
-- Name: COLUMN platform_user_resource.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_user_resource.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_user_resource.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_resource.created_time IS '创建时间';


--
-- Name: platform_user_role; Type: TABLE; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE TABLE byteasy_cloud_platform.platform_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE byteasy_cloud_platform.platform_user_role OWNER TO postgres;

--
-- Name: TABLE platform_user_role; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON TABLE byteasy_cloud_platform.platform_user_role IS '平台角色授权表';


--
-- Name: COLUMN platform_user_role.user_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_role.user_id IS '平台资源关联的用户ID';


--
-- Name: COLUMN platform_user_role.role_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_role.role_id IS '平台资源关联的角色ID';


--
-- Name: COLUMN platform_user_role.creator_id; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_role.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_user_role.created_by; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_role.created_by IS '创建人名称';


--
-- Name: COLUMN platform_user_role.created_time; Type: COMMENT; Schema: byteasy_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN byteasy_cloud_platform.platform_user_role.created_time IS '创建时间';


--
-- Name: knowledge_category; Type: TABLE; Schema: knowledge_base; Owner: postgres
--

CREATE TABLE knowledge_base.knowledge_category (
    id bigint NOT NULL,
    parent_id bigint,
    name character varying(2000) NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE knowledge_base.knowledge_category OWNER TO postgres;

--
-- Name: TABLE knowledge_category; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON TABLE knowledge_base.knowledge_category IS '知识分类目录';


--
-- Name: COLUMN knowledge_category.id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.id IS '主键';


--
-- Name: COLUMN knowledge_category.parent_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.parent_id IS '上级分类';


--
-- Name: COLUMN knowledge_category.name; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.name IS '分类名称';


--
-- Name: COLUMN knowledge_category.show_order; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.show_order IS '显示顺序';


--
-- Name: COLUMN knowledge_category.creator_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.creator_id IS '创建人账户ID';


--
-- Name: COLUMN knowledge_category.created_by; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.created_by IS '创建人姓名';


--
-- Name: COLUMN knowledge_category.created_time; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.created_time IS '创建时间';


--
-- Name: COLUMN knowledge_category.updater_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.updater_id IS '更新人账户ID';


--
-- Name: COLUMN knowledge_category.updated_by; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.updated_by IS '更新人姓名';


--
-- Name: COLUMN knowledge_category.updated_time; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.updated_time IS '更新时间';


--
-- Name: COLUMN knowledge_category.version; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_category.version IS '版本';


--
-- Name: knowledge_category_id_seq; Type: SEQUENCE; Schema: knowledge_base; Owner: postgres
--

CREATE SEQUENCE knowledge_base.knowledge_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE knowledge_base.knowledge_category_id_seq OWNER TO postgres;

--
-- Name: knowledge_category_id_seq; Type: SEQUENCE OWNED BY; Schema: knowledge_base; Owner: postgres
--

ALTER SEQUENCE knowledge_base.knowledge_category_id_seq OWNED BY knowledge_base.knowledge_category.id;


--
-- Name: knowledge_file; Type: TABLE; Schema: knowledge_base; Owner: postgres
--

CREATE TABLE knowledge_base.knowledge_file (
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    file_name character varying(2000) NOT NULL,
    file_path character varying(3000),
    file_type smallint DEFAULT 1 NOT NULL,
    file_version character varying(512),
    show_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE knowledge_base.knowledge_file OWNER TO postgres;

--
-- Name: TABLE knowledge_file; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON TABLE knowledge_base.knowledge_file IS '知识文件';


--
-- Name: COLUMN knowledge_file.id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.id IS '主键';


--
-- Name: COLUMN knowledge_file.category_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.category_id IS '分类';


--
-- Name: COLUMN knowledge_file.file_name; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.file_name IS '文件名';


--
-- Name: COLUMN knowledge_file.file_path; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.file_path IS '路径';


--
-- Name: COLUMN knowledge_file.file_type; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.file_type IS '种类';


--
-- Name: COLUMN knowledge_file.file_version; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.file_version IS '版本';


--
-- Name: COLUMN knowledge_file.show_order; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.show_order IS '顺序';


--
-- Name: COLUMN knowledge_file.creator_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.creator_id IS '创建人账户ID';


--
-- Name: COLUMN knowledge_file.created_by; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.created_by IS '创建人姓名';


--
-- Name: COLUMN knowledge_file.created_time; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.created_time IS '创建时间';


--
-- Name: COLUMN knowledge_file.updater_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.updater_id IS '更新人账户ID';


--
-- Name: COLUMN knowledge_file.updated_by; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.updated_by IS '更新人姓名';


--
-- Name: COLUMN knowledge_file.updated_time; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.updated_time IS '更新时间';


--
-- Name: COLUMN knowledge_file.version; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file.version IS '版本';


--
-- Name: knowledge_file_history; Type: TABLE; Schema: knowledge_base; Owner: postgres
--

CREATE TABLE knowledge_base.knowledge_file_history (
    id bigint NOT NULL,
    file_id bigint NOT NULL,
    category_id bigint NOT NULL,
    lang character varying(80) NOT NULL,
    file_name character varying(120) NOT NULL,
    file_path character varying(120) DEFAULT NULL::character varying,
    file_version character varying(120) DEFAULT NULL::character varying,
    file_type smallint DEFAULT 1 NOT NULL,
    version integer,
    shor_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE knowledge_base.knowledge_file_history OWNER TO postgres;

--
-- Name: TABLE knowledge_file_history; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON TABLE knowledge_base.knowledge_file_history IS '文件变更历史';


--
-- Name: COLUMN knowledge_file_history.id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.id IS '主键';


--
-- Name: COLUMN knowledge_file_history.file_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.file_id IS '文件ID';


--
-- Name: COLUMN knowledge_file_history.category_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.category_id IS '分类';


--
-- Name: COLUMN knowledge_file_history.lang; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.lang IS '语言';


--
-- Name: COLUMN knowledge_file_history.file_name; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.file_name IS '文件名';


--
-- Name: COLUMN knowledge_file_history.file_path; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.file_path IS '路径';


--
-- Name: COLUMN knowledge_file_history.file_version; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.file_version IS '版本';


--
-- Name: COLUMN knowledge_file_history.file_type; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.file_type IS '种类';


--
-- Name: COLUMN knowledge_file_history.version; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.version IS '版本';


--
-- Name: COLUMN knowledge_file_history.shor_order; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.shor_order IS '顺序';


--
-- Name: COLUMN knowledge_file_history.creator_id; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.creator_id IS '创建人账户ID';


--
-- Name: COLUMN knowledge_file_history.created_by; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.created_by IS '创建人姓名';


--
-- Name: COLUMN knowledge_file_history.created_time; Type: COMMENT; Schema: knowledge_base; Owner: postgres
--

COMMENT ON COLUMN knowledge_base.knowledge_file_history.created_time IS '创建时间';


--
-- Name: knowledge_file_history_id_seq; Type: SEQUENCE; Schema: knowledge_base; Owner: postgres
--

CREATE SEQUENCE knowledge_base.knowledge_file_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE knowledge_base.knowledge_file_history_id_seq OWNER TO postgres;

--
-- Name: knowledge_file_history_id_seq; Type: SEQUENCE OWNED BY; Schema: knowledge_base; Owner: postgres
--

ALTER SEQUENCE knowledge_base.knowledge_file_history_id_seq OWNED BY knowledge_base.knowledge_file_history.id;


--
-- Name: knowledge_file_id_seq; Type: SEQUENCE; Schema: knowledge_base; Owner: postgres
--

CREATE SEQUENCE knowledge_base.knowledge_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE knowledge_base.knowledge_file_id_seq OWNER TO postgres;

--
-- Name: knowledge_file_id_seq; Type: SEQUENCE OWNED BY; Schema: knowledge_base; Owner: postgres
--

ALTER SEQUENCE knowledge_base.knowledge_file_id_seq OWNED BY knowledge_base.knowledge_file.id;


--
-- Name: model_code_serial_no; Type: TABLE; Schema: micro_product_assistant; Owner: postgres
--

CREATE TABLE micro_product_assistant.model_code_serial_no (
    id bigint NOT NULL,
    model_code character varying(64) DEFAULT NULL::character varying,
    serial_number integer DEFAULT 1 NOT NULL,
    serial_number_encrypted character varying(64) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE micro_product_assistant.model_code_serial_no OWNER TO postgres;

--
-- Name: TABLE model_code_serial_no; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON TABLE micro_product_assistant.model_code_serial_no IS '车型流水号生成记录表';


--
-- Name: COLUMN model_code_serial_no.id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.id IS '主键';


--
-- Name: COLUMN model_code_serial_no.model_code; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.model_code IS '车型';


--
-- Name: COLUMN model_code_serial_no.serial_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.serial_number IS '流水号';


--
-- Name: COLUMN model_code_serial_no.creator_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.creator_id IS '创建人账户ID';


--
-- Name: COLUMN model_code_serial_no.created_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.created_by IS '创建人姓名';


--
-- Name: COLUMN model_code_serial_no.created_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.created_time IS '创建时间';


--
-- Name: COLUMN model_code_serial_no.updater_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.updater_id IS '更新人账户ID';


--
-- Name: COLUMN model_code_serial_no.updated_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.updated_by IS '更新人姓名';


--
-- Name: COLUMN model_code_serial_no.updated_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.updated_time IS '更新时间';


--
-- Name: COLUMN model_code_serial_no.version; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_code_serial_no.version IS '乐观锁版本号';


--
-- Name: model_code_serial_no_id_seq; Type: SEQUENCE; Schema: micro_product_assistant; Owner: postgres
--

CREATE SEQUENCE micro_product_assistant.model_code_serial_no_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE micro_product_assistant.model_code_serial_no_id_seq OWNER TO postgres;

--
-- Name: model_code_serial_no_id_seq; Type: SEQUENCE OWNED BY; Schema: micro_product_assistant; Owner: postgres
--

ALTER SEQUENCE micro_product_assistant.model_code_serial_no_id_seq OWNED BY micro_product_assistant.model_code_serial_no.id;


--
-- Name: model_env_pub_no; Type: TABLE; Schema: micro_product_assistant; Owner: postgres
--

CREATE TABLE micro_product_assistant.model_env_pub_no (
    id bigint NOT NULL,
    code character varying(80) NOT NULL,
    model_code character varying(80) NOT NULL,
    machine_family character varying(80) NOT NULL,
    engine_family character varying(80) NOT NULL,
    engine_model character varying(80) NOT NULL,
    engine_public_number character varying(80) NOT NULL,
    environmental_public_number character varying(80) NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE micro_product_assistant.model_env_pub_no OWNER TO postgres;

--
-- Name: TABLE model_env_pub_no; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON TABLE micro_product_assistant.model_env_pub_no IS '车型环保公开号对照表';


--
-- Name: COLUMN model_env_pub_no.id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.id IS '主键';


--
-- Name: COLUMN model_env_pub_no.code; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.code IS '内部编号';


--
-- Name: COLUMN model_env_pub_no.model_code; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.model_code IS '型号';


--
-- Name: COLUMN model_env_pub_no.machine_family; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.machine_family IS '整机机械系族';


--
-- Name: COLUMN model_env_pub_no.engine_family; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.engine_family IS '发动机系族';


--
-- Name: COLUMN model_env_pub_no.engine_model; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.engine_model IS '发动机型号';


--
-- Name: COLUMN model_env_pub_no.engine_public_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.engine_public_number IS '发动机环保公开号';


--
-- Name: COLUMN model_env_pub_no.environmental_public_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.environmental_public_number IS '整机环保公开号';


--
-- Name: COLUMN model_env_pub_no.creator_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.creator_id IS '创建人账户ID';


--
-- Name: COLUMN model_env_pub_no.created_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.created_by IS '创建人姓名';


--
-- Name: COLUMN model_env_pub_no.created_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.created_time IS '创建时间';


--
-- Name: COLUMN model_env_pub_no.updater_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.updater_id IS '更新人账户ID';


--
-- Name: COLUMN model_env_pub_no.updated_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.updated_by IS '更新人姓名';


--
-- Name: COLUMN model_env_pub_no.updated_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.updated_time IS '更新时间';


--
-- Name: COLUMN model_env_pub_no.version; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.model_env_pub_no.version IS '乐观锁版本号';


--
-- Name: model_env_pub_no_id_seq; Type: SEQUENCE; Schema: micro_product_assistant; Owner: postgres
--

CREATE SEQUENCE micro_product_assistant.model_env_pub_no_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE micro_product_assistant.model_env_pub_no_id_seq OWNER TO postgres;

--
-- Name: model_env_pub_no_id_seq; Type: SEQUENCE OWNED BY; Schema: micro_product_assistant; Owner: postgres
--

ALTER SEQUENCE micro_product_assistant.model_env_pub_no_id_seq OWNED BY micro_product_assistant.model_env_pub_no.id;


--
-- Name: nameplate_record; Type: TABLE; Schema: micro_product_assistant; Owner: postgres
--

CREATE TABLE micro_product_assistant.nameplate_record (
    id bigint NOT NULL,
    model_code character varying(64) NOT NULL,
    vin character varying(64) NOT NULL,
    factory_date timestamp without time zone,
    environmental_public_number character varying(120) DEFAULT NULL::character varying,
    engine_model character varying(512) DEFAULT NULL::character varying,
    engine_manufacturer character varying(512) DEFAULT NULL::character varying,
    rated_power character varying(512) DEFAULT NULL::character varying,
    fuel_injection_system_type character varying(512) DEFAULT NULL::character varying,
    environmental_key_parts character varying(512) DEFAULT NULL::character varying,
    execution_standard character varying(512) DEFAULT NULL::character varying,
    engine_serial_number character varying(512) DEFAULT NULL::character varying,
    qr_code character varying(512) DEFAULT NULL::character varying,
    qr_url character varying(512) DEFAULT NULL::character varying,
    sim_card_status smallint DEFAULT 1 NOT NULL,
    pay_status smallint DEFAULT 1 NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1,
    engine_public_number character varying(120),
    qr_raw_url character varying(255)
);


ALTER TABLE micro_product_assistant.nameplate_record OWNER TO postgres;

--
-- Name: TABLE nameplate_record; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON TABLE micro_product_assistant.nameplate_record IS '生产铭牌打印记录表';


--
-- Name: COLUMN nameplate_record.id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN nameplate_record.model_code; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.model_code IS '型号';


--
-- Name: COLUMN nameplate_record.vin; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.vin IS '出厂编号';


--
-- Name: COLUMN nameplate_record.factory_date; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.factory_date IS '生产日期';


--
-- Name: COLUMN nameplate_record.environmental_public_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.environmental_public_number IS '整机环保公开号';


--
-- Name: COLUMN nameplate_record.engine_model; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.engine_model IS '发动机型号';


--
-- Name: COLUMN nameplate_record.engine_manufacturer; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.engine_manufacturer IS '发动机生产厂';


--
-- Name: COLUMN nameplate_record.rated_power; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.rated_power IS '标定功率';


--
-- Name: COLUMN nameplate_record.fuel_injection_system_type; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.fuel_injection_system_type IS '燃油喷射系统形式';


--
-- Name: COLUMN nameplate_record.environmental_key_parts; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.environmental_key_parts IS '环保关键件';


--
-- Name: COLUMN nameplate_record.execution_standard; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.execution_standard IS '执行标准';


--
-- Name: COLUMN nameplate_record.engine_serial_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.engine_serial_number IS '发动机编号';


--
-- Name: COLUMN nameplate_record.qr_code; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.qr_code IS '二维码编号';


--
-- Name: COLUMN nameplate_record.qr_url; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.qr_url IS '二维码图片地址';


--
-- Name: COLUMN nameplate_record.sim_card_status; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.sim_card_status IS '绑定物联网卡';


--
-- Name: COLUMN nameplate_record.pay_status; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.pay_status IS '补贴办理';


--
-- Name: COLUMN nameplate_record.status; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.status IS '状态';


--
-- Name: COLUMN nameplate_record.creator_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.creator_id IS '创建人账户ID';


--
-- Name: COLUMN nameplate_record.created_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.created_by IS '创建人姓名';


--
-- Name: COLUMN nameplate_record.created_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.created_time IS '创建时间';


--
-- Name: COLUMN nameplate_record.updater_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.updater_id IS '更新人账户ID';


--
-- Name: COLUMN nameplate_record.updated_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.updated_by IS '更新人姓名';


--
-- Name: COLUMN nameplate_record.updated_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.updated_time IS '更新时间';


--
-- Name: COLUMN nameplate_record.version; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.version IS '乐观锁版本号';


--
-- Name: COLUMN nameplate_record.engine_public_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.engine_public_number IS '发动机环保公开号';


--
-- Name: COLUMN nameplate_record.qr_raw_url; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.nameplate_record.qr_raw_url IS '二维码识别后地址';


--
-- Name: nameplate_record_id_seq; Type: SEQUENCE; Schema: micro_product_assistant; Owner: postgres
--

CREATE SEQUENCE micro_product_assistant.nameplate_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE micro_product_assistant.nameplate_record_id_seq OWNER TO postgres;

--
-- Name: nameplate_record_id_seq; Type: SEQUENCE OWNED BY; Schema: micro_product_assistant; Owner: postgres
--

ALTER SEQUENCE micro_product_assistant.nameplate_record_id_seq OWNED BY micro_product_assistant.nameplate_record.id;


--
-- Name: print_record; Type: TABLE; Schema: micro_product_assistant; Owner: postgres
--

CREATE TABLE micro_product_assistant.print_record (
    id bigint NOT NULL,
    product_time timestamp without time zone,
    vins character varying(2048) DEFAULT NULL::character varying,
    include_day smallint DEFAULT 0 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE micro_product_assistant.print_record OWNER TO postgres;

--
-- Name: TABLE print_record; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON TABLE micro_product_assistant.print_record IS '打印模板生成记录';


--
-- Name: COLUMN print_record.id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.id IS '主键';


--
-- Name: COLUMN print_record.product_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.product_time IS '生产日期';


--
-- Name: COLUMN print_record.vins; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.vins IS '车辆编号';


--
-- Name: COLUMN print_record.include_day; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.include_day IS '是否打印天';


--
-- Name: COLUMN print_record.creator_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.creator_id IS '创建人账户ID';


--
-- Name: COLUMN print_record.created_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.created_by IS '创建人姓名';


--
-- Name: COLUMN print_record.created_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.created_time IS '创建时间';


--
-- Name: COLUMN print_record.updater_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.updater_id IS '更新人账户ID';


--
-- Name: COLUMN print_record.updated_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.updated_by IS '更新人姓名';


--
-- Name: COLUMN print_record.updated_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.updated_time IS '更新时间';


--
-- Name: COLUMN print_record.version; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.print_record.version IS '乐观锁版本号';


--
-- Name: print_record_id_seq; Type: SEQUENCE; Schema: micro_product_assistant; Owner: postgres
--

CREATE SEQUENCE micro_product_assistant.print_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE micro_product_assistant.print_record_id_seq OWNER TO postgres;

--
-- Name: print_record_id_seq; Type: SEQUENCE OWNED BY; Schema: micro_product_assistant; Owner: postgres
--

ALTER SEQUENCE micro_product_assistant.print_record_id_seq OWNED BY micro_product_assistant.print_record.id;


--
-- Name: qr_sync_job; Type: TABLE; Schema: micro_product_assistant; Owner: postgres
--

CREATE TABLE micro_product_assistant.qr_sync_job (
    id bigint NOT NULL,
    product_time timestamp without time zone,
    vins character varying(2048) DEFAULT NULL::character varying,
    excels character varying(2048) DEFAULT NULL::character varying,
    push_status smallint DEFAULT 0 NOT NULL,
    pull_status smallint DEFAULT 0 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE micro_product_assistant.qr_sync_job OWNER TO postgres;

--
-- Name: TABLE qr_sync_job; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON TABLE micro_product_assistant.qr_sync_job IS '大田农机二维码同步任务';


--
-- Name: COLUMN qr_sync_job.id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN qr_sync_job.product_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.product_time IS '生产日期';


--
-- Name: COLUMN qr_sync_job.vins; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.vins IS '任务同步的车辆编号';


--
-- Name: COLUMN qr_sync_job.excels; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.excels IS '导入Excel模板文件';


--
-- Name: COLUMN qr_sync_job.push_status; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.push_status IS '推送是否成功';


--
-- Name: COLUMN qr_sync_job.pull_status; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.pull_status IS '获取二维码是否成功';


--
-- Name: COLUMN qr_sync_job.creator_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.creator_id IS '创建人账户ID';


--
-- Name: COLUMN qr_sync_job.created_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.created_by IS '创建人姓名';


--
-- Name: COLUMN qr_sync_job.created_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.created_time IS '创建时间';


--
-- Name: COLUMN qr_sync_job.updater_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.updater_id IS '更新人账户ID';


--
-- Name: COLUMN qr_sync_job.updated_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.updated_by IS '更新人姓名';


--
-- Name: COLUMN qr_sync_job.updated_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.updated_time IS '更新时间';


--
-- Name: COLUMN qr_sync_job.version; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.qr_sync_job.version IS '乐观锁版本号';


--
-- Name: qr_sync_job_id_seq; Type: SEQUENCE; Schema: micro_product_assistant; Owner: postgres
--

CREATE SEQUENCE micro_product_assistant.qr_sync_job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE micro_product_assistant.qr_sync_job_id_seq OWNER TO postgres;

--
-- Name: qr_sync_job_id_seq; Type: SEQUENCE OWNED BY; Schema: micro_product_assistant; Owner: postgres
--

ALTER SEQUENCE micro_product_assistant.qr_sync_job_id_seq OWNED BY micro_product_assistant.qr_sync_job.id;


--
-- Name: vin_generate_request; Type: TABLE; Schema: micro_product_assistant; Owner: postgres
--

CREATE TABLE micro_product_assistant.vin_generate_request (
    id bigint NOT NULL,
    model_code character varying(64) NOT NULL,
    engine_model character varying(512) DEFAULT NULL::character varying,
    environmental_public_number character varying(512) DEFAULT NULL::character varying,
    engine_serial_number character varying(512) DEFAULT NULL::character varying,
    vin character varying(64) DEFAULT NULL::character varying,
    engine_public_number character varying(512) DEFAULT NULL::character varying,
    engine_img character varying(512) DEFAULT NULL::character varying,
    engine_manufacturer character varying(512) DEFAULT NULL::character varying,
    rated_power character varying(512) DEFAULT NULL::character varying,
    environmental_key_parts character varying(512) DEFAULT NULL::character varying,
    fuel_injection_system_type character varying(128) DEFAULT NULL::character varying,
    execution_standard character varying(128) DEFAULT NULL::character varying,
    nameplate_record_id bigint,
    status smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE micro_product_assistant.vin_generate_request OWNER TO postgres;

--
-- Name: TABLE vin_generate_request; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON TABLE micro_product_assistant.vin_generate_request IS '车辆编号生成申请表';


--
-- Name: COLUMN vin_generate_request.id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN vin_generate_request.model_code; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.model_code IS '型号';


--
-- Name: COLUMN vin_generate_request.engine_model; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.engine_model IS '发动机型号';


--
-- Name: COLUMN vin_generate_request.environmental_public_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.environmental_public_number IS '机械环保公开号';


--
-- Name: COLUMN vin_generate_request.engine_serial_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.engine_serial_number IS '发动机编号';


--
-- Name: COLUMN vin_generate_request.vin; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.vin IS '出厂编号';


--
-- Name: COLUMN vin_generate_request.engine_public_number; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.engine_public_number IS '发动机环保公开号';


--
-- Name: COLUMN vin_generate_request.engine_img; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.engine_img IS '发动机铭牌照片';


--
-- Name: COLUMN vin_generate_request.engine_manufacturer; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.engine_manufacturer IS '发动机生产厂';


--
-- Name: COLUMN vin_generate_request.rated_power; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.rated_power IS '标定功率';


--
-- Name: COLUMN vin_generate_request.environmental_key_parts; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.environmental_key_parts IS '环保关键件';


--
-- Name: COLUMN vin_generate_request.nameplate_record_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.nameplate_record_id IS '铭牌记录外键';


--
-- Name: COLUMN vin_generate_request.status; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.status IS '状态';


--
-- Name: COLUMN vin_generate_request.creator_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.creator_id IS '创建人账户ID';


--
-- Name: COLUMN vin_generate_request.created_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.created_by IS '创建人姓名';


--
-- Name: COLUMN vin_generate_request.created_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.created_time IS '创建时间';


--
-- Name: COLUMN vin_generate_request.updater_id; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.updater_id IS '更新人账户ID';


--
-- Name: COLUMN vin_generate_request.updated_by; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.updated_by IS '更新人姓名';


--
-- Name: COLUMN vin_generate_request.updated_time; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.updated_time IS '更新时间';


--
-- Name: COLUMN vin_generate_request.version; Type: COMMENT; Schema: micro_product_assistant; Owner: postgres
--

COMMENT ON COLUMN micro_product_assistant.vin_generate_request.version IS '乐观锁版本号';


--
-- Name: vin_generate_request_id_seq; Type: SEQUENCE; Schema: micro_product_assistant; Owner: postgres
--

CREATE SEQUENCE micro_product_assistant.vin_generate_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE micro_product_assistant.vin_generate_request_id_seq OWNER TO postgres;

--
-- Name: vin_generate_request_id_seq; Type: SEQUENCE OWNED BY; Schema: micro_product_assistant; Owner: postgres
--

ALTER SEQUENCE micro_product_assistant.vin_generate_request_id_seq OWNED BY micro_product_assistant.vin_generate_request.id;


--
-- Name: oauth2_authorization; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.oauth2_authorization (
    id character varying(100) NOT NULL,
    registered_client_id character varying(100) NOT NULL,
    principal_name character varying(200) NOT NULL,
    authorization_grant_type character varying(100) NOT NULL,
    authorized_scopes character varying(1000) DEFAULT NULL::character varying,
    attributes text,
    state character varying(500) DEFAULT NULL::character varying,
    authorization_code_value text,
    authorization_code_issued_at timestamp without time zone,
    authorization_code_expires_at timestamp without time zone,
    authorization_code_metadata text,
    access_token_value text,
    access_token_issued_at timestamp without time zone,
    access_token_expires_at timestamp without time zone,
    access_token_metadata text,
    access_token_type character varying(100) DEFAULT NULL::character varying,
    access_token_scopes character varying(1000) DEFAULT NULL::character varying,
    oidc_id_token_value text,
    oidc_id_token_issued_at timestamp without time zone,
    oidc_id_token_expires_at timestamp without time zone,
    oidc_id_token_metadata text,
    refresh_token_value text,
    refresh_token_issued_at timestamp without time zone,
    refresh_token_expires_at timestamp without time zone,
    refresh_token_metadata text,
    user_code_value text,
    user_code_issued_at timestamp without time zone,
    user_code_expires_at timestamp without time zone,
    user_code_metadata text,
    device_code_value text,
    device_code_issued_at timestamp without time zone,
    device_code_expires_at timestamp without time zone,
    device_code_metadata text
);


ALTER TABLE tuna_cloud_platform.oauth2_authorization OWNER TO postgres;

--
-- Name: oauth2_authorization_consent; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.oauth2_authorization_consent (
    registered_client_id character varying(100) NOT NULL,
    principal_name character varying(200) NOT NULL,
    authorities character varying(1000) NOT NULL
);


ALTER TABLE tuna_cloud_platform.oauth2_authorization_consent OWNER TO postgres;

--
-- Name: oauth2_registered_client; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.oauth2_registered_client (
    id character varying(36) DEFAULT gen_random_uuid() NOT NULL,
    client_id character varying(100) NOT NULL,
    client_id_issued_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    client_secret character varying(500),
    client_secret_expires_at timestamp(6) without time zone,
    client_name character varying(200),
    client_authentication_methods character varying(50)[] DEFAULT '{}'::character varying[],
    authorization_grant_types character varying(50)[] DEFAULT '{}'::character varying[],
    redirect_uris text[] DEFAULT '{}'::text[],
    post_logout_redirect_uris text[] DEFAULT '{}'::text[],
    scopes character varying(100)[] DEFAULT '{}'::character varying[],
    client_settings jsonb DEFAULT '{}'::jsonb,
    token_settings jsonb DEFAULT '{}'::jsonb,
    creator_id character varying(36),
    created_by character varying(100),
    created_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updater_id character varying(36),
    updated_by character varying(100),
    updated_time timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    version integer DEFAULT 1,
    is_active boolean DEFAULT true,
    description text,
    CONSTRAINT chk_arrays_not_null CHECK (((client_authentication_methods IS NOT NULL) AND (authorization_grant_types IS NOT NULL) AND (redirect_uris IS NOT NULL) AND (post_logout_redirect_uris IS NOT NULL) AND (scopes IS NOT NULL))),
    CONSTRAINT chk_client_id CHECK ((char_length((client_id)::text) >= 3)),
    CONSTRAINT chk_client_name CHECK ((char_length((client_name)::text) >= 1))
);


ALTER TABLE tuna_cloud_platform.oauth2_registered_client OWNER TO postgres;

--
-- Name: TABLE oauth2_registered_client; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.oauth2_registered_client IS 'OAuth2 已注册客户端信息表';


--
-- Name: COLUMN oauth2_registered_client.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.id IS '主键（分布式ID）';


--
-- Name: COLUMN oauth2_registered_client.client_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_id IS '客户端唯一标识符';


--
-- Name: COLUMN oauth2_registered_client.client_id_issued_at; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_id_issued_at IS '客户端ID颁发时间';


--
-- Name: COLUMN oauth2_registered_client.client_secret; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_secret IS '客户端密钥（加密存储）';


--
-- Name: COLUMN oauth2_registered_client.client_secret_expires_at; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_secret_expires_at IS '客户端密钥过期时间';


--
-- Name: COLUMN oauth2_registered_client.client_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_name IS '客户端名称（用于展示）';


--
-- Name: COLUMN oauth2_registered_client.client_authentication_methods; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_authentication_methods IS '客户端认证方式列表（如：client_secret_basic, client_secret_post 等）';


--
-- Name: COLUMN oauth2_registered_client.authorization_grant_types; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.authorization_grant_types IS '授权许可类型列表（如：authorization_code, refresh_token, client_credentials 等）';


--
-- Name: COLUMN oauth2_registered_client.redirect_uris; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.redirect_uris IS '重定向URI列表（授权回调地址）';


--
-- Name: COLUMN oauth2_registered_client.post_logout_redirect_uris; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.post_logout_redirect_uris IS '登出后重定向URI列表';


--
-- Name: COLUMN oauth2_registered_client.scopes; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.scopes IS '授权作用域列表（如：read, write, openid 等）';


--
-- Name: COLUMN oauth2_registered_client.client_settings; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.client_settings IS '客户端设置（JSONB格式，如 require-proof-key, require-authorization-consent 等）';


--
-- Name: COLUMN oauth2_registered_client.token_settings; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.token_settings IS '令牌设置（JSONB格式，如 access-token-time-to-live, refresh-token-time-to-live 等）';


--
-- Name: COLUMN oauth2_registered_client.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.creator_id IS '创建人ID（用户唯一标识）';


--
-- Name: COLUMN oauth2_registered_client.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.created_by IS '创建人姓名';


--
-- Name: COLUMN oauth2_registered_client.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.created_time IS '创建时间';


--
-- Name: COLUMN oauth2_registered_client.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.updater_id IS '更新人ID（用户唯一标识）';


--
-- Name: COLUMN oauth2_registered_client.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.updated_by IS '更新人姓名';


--
-- Name: COLUMN oauth2_registered_client.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.updated_time IS '更新时间';


--
-- Name: COLUMN oauth2_registered_client.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.version IS '乐观锁版本号（用于并发控制）';


--
-- Name: COLUMN oauth2_registered_client.is_active; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.is_active IS '是否启用（逻辑删除替代方案）';


--
-- Name: COLUMN oauth2_registered_client.description; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client.description IS '客户端描述信息';


--
-- Name: oauth2_registered_client_openapi; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.oauth2_registered_client_openapi (
    oauth2_registered_client_id character varying(32) NOT NULL,
    openapi_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.oauth2_registered_client_openapi OWNER TO postgres;

--
-- Name: TABLE oauth2_registered_client_openapi; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.oauth2_registered_client_openapi IS 'OAuth2客户端与平台资源的授权关联表';


--
-- Name: COLUMN oauth2_registered_client_openapi.oauth2_registered_client_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client_openapi.oauth2_registered_client_id IS 'OAuth2客户端ID，引用oauth2_registered_client表';


--
-- Name: COLUMN oauth2_registered_client_openapi.openapi_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client_openapi.openapi_id IS '平台资源ID，引用openapi_id';


--
-- Name: COLUMN oauth2_registered_client_openapi.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client_openapi.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN oauth2_registered_client_openapi.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client_openapi.created_by IS '创建人名称';


--
-- Name: COLUMN oauth2_registered_client_openapi.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.oauth2_registered_client_openapi.created_time IS '创建时间';


--
-- Name: platform_dictionary; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_dictionary (
    id bigint NOT NULL,
    group_id bigint,
    code character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_dictionary OWNER TO postgres;

--
-- Name: TABLE platform_dictionary; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_dictionary IS '字典表';


--
-- Name: COLUMN platform_dictionary.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.id IS '主键';


--
-- Name: COLUMN platform_dictionary.group_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.group_id IS '分组编号';


--
-- Name: COLUMN platform_dictionary.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.code IS '编码';


--
-- Name: COLUMN platform_dictionary.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.name IS '名称';


--
-- Name: COLUMN platform_dictionary.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.status IS '状态';


--
-- Name: COLUMN platform_dictionary.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.remark IS '备注';


--
-- Name: COLUMN platform_dictionary.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.creator_id IS '创建人账户编号';


--
-- Name: COLUMN platform_dictionary.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_dictionary.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.created_time IS '创建时间';


--
-- Name: COLUMN platform_dictionary.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.updater_id IS '更新人账户编号';


--
-- Name: COLUMN platform_dictionary.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_dictionary.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.updated_time IS '更新时间';


--
-- Name: COLUMN platform_dictionary.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary.version IS '乐观锁版本号';


--
-- Name: platform_dictionary1_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_dictionary1_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_dictionary1_id_seq OWNER TO postgres;

--
-- Name: platform_dictionary1_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_dictionary1_id_seq OWNED BY tuna_cloud_platform.platform_dictionary.id;


--
-- Name: platform_dictionary_item; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_dictionary_item (
    id bigint NOT NULL,
    dictionary_id bigint NOT NULL,
    code character varying(64) NOT NULL,
    label character varying(128) NOT NULL,
    value character varying(128) NOT NULL,
    is_default smallint DEFAULT 0 NOT NULL,
    css character varying(128),
    show_order smallint DEFAULT 1 NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_dictionary_item OWNER TO postgres;

--
-- Name: TABLE platform_dictionary_item; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_dictionary_item IS '字典项目定义表';


--
-- Name: COLUMN platform_dictionary_item.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.id IS '主键';


--
-- Name: COLUMN platform_dictionary_item.dictionary_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.dictionary_id IS '字典';


--
-- Name: COLUMN platform_dictionary_item.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.code IS '编码';


--
-- Name: COLUMN platform_dictionary_item.label; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.label IS '标签';


--
-- Name: COLUMN platform_dictionary_item.value; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.value IS '值';


--
-- Name: COLUMN platform_dictionary_item.is_default; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.is_default IS '是否默认';


--
-- Name: COLUMN platform_dictionary_item.css; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.css IS '显示样式';


--
-- Name: COLUMN platform_dictionary_item.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.show_order IS '显示顺序';


--
-- Name: COLUMN platform_dictionary_item.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.status IS '状态';


--
-- Name: COLUMN platform_dictionary_item.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.remark IS '备注';


--
-- Name: COLUMN platform_dictionary_item.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_dictionary_item.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_dictionary_item.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.created_time IS '创建时间';


--
-- Name: COLUMN platform_dictionary_item.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_dictionary_item.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_dictionary_item.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.updated_time IS '更新时间';


--
-- Name: COLUMN platform_dictionary_item.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_dictionary_item.version IS '乐观锁版本号';


--
-- Name: platform_dictionary_item_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_dictionary_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_dictionary_item_id_seq OWNER TO postgres;

--
-- Name: platform_dictionary_item_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_dictionary_item_id_seq OWNED BY tuna_cloud_platform.platform_dictionary_item.id;


--
-- Name: platform_entity_define; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_entity_define (
    id bigint NOT NULL,
    code character varying(512) NOT NULL,
    service_code character varying(128) NOT NULL,
    name character varying(512) NOT NULL,
    url character varying(512),
    remark character varying(2048) DEFAULT NULL::character varying,
    title_column_name character varying(128) DEFAULT 'name'::character varying,
    operation_column_width integer DEFAULT 160,
    query_params character varying(512) DEFAULT NULL::character varying,
    form_component character varying(220) DEFAULT NULL::character varying,
    form_width character varying(20) DEFAULT '50%'::character varying,
    dialog_model smallint,
    pagination_visible smallint DEFAULT 1,
    excel_enable smallint DEFAULT 0,
    query_form_visible smallint DEFAULT 1,
    excel_import_component character varying(220) DEFAULT NULL::character varying,
    row_key character varying(120) DEFAULT NULL::character varying,
    default_expand_all smallint DEFAULT 0,
    tree_props character varying(512) DEFAULT NULL::character varying,
    query_mode smallint DEFAULT 0,
    show_lang character varying(60) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1,
    show_summary smallint DEFAULT 0,
    query_bar_buttons character varying(512) DEFAULT NULL::character varying,
    operation_buttons character varying(512) DEFAULT NULL::character varying,
    table_name character varying(128) DEFAULT NULL::character varying
);


ALTER TABLE tuna_cloud_platform.platform_entity_define OWNER TO postgres;

--
-- Name: TABLE platform_entity_define; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_entity_define IS '平台模型定义';


--
-- Name: COLUMN platform_entity_define.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.id IS '编号';


--
-- Name: COLUMN platform_entity_define.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.code IS '编码';


--
-- Name: COLUMN platform_entity_define.service_code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.service_code IS '服务编码';


--
-- Name: COLUMN platform_entity_define.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.name IS '名称';


--
-- Name: COLUMN platform_entity_define.url; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.url IS '请求接口地址';


--
-- Name: COLUMN platform_entity_define.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.remark IS '说明';


--
-- Name: COLUMN platform_entity_define.title_column_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.title_column_name IS '卡片模式标题字段名称';


--
-- Name: COLUMN platform_entity_define.operation_column_width; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.operation_column_width IS '操作列宽度';


--
-- Name: COLUMN platform_entity_define.query_params; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.query_params IS '默认查询条件';


--
-- Name: COLUMN platform_entity_define.form_component; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.form_component IS '自定义表单组件';


--
-- Name: COLUMN platform_entity_define.form_width; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.form_width IS '弹窗宽度';


--
-- Name: COLUMN platform_entity_define.dialog_model; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.dialog_model IS '弹窗方式';


--
-- Name: COLUMN platform_entity_define.pagination_visible; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.pagination_visible IS '启用分页插件';


--
-- Name: COLUMN platform_entity_define.excel_enable; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.excel_enable IS '是否启用excel导入导出';


--
-- Name: COLUMN platform_entity_define.query_form_visible; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.query_form_visible IS '开启查询条件栏';


--
-- Name: COLUMN platform_entity_define.excel_import_component; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.excel_import_component IS '自定义导入excel组件';


--
-- Name: COLUMN platform_entity_define.row_key; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.row_key IS '树形表格rowkey';


--
-- Name: COLUMN platform_entity_define.default_expand_all; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.default_expand_all IS '默认展开树';


--
-- Name: COLUMN platform_entity_define.tree_props; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.tree_props IS '树属性名称';


--
-- Name: COLUMN platform_entity_define.query_mode; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.query_mode IS '查询模式';


--
-- Name: COLUMN platform_entity_define.show_lang; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.show_lang IS '显示语言';


--
-- Name: COLUMN platform_entity_define.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.creator_id IS '创建人编号';


--
-- Name: COLUMN platform_entity_define.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.created_by IS '创建人';


--
-- Name: COLUMN platform_entity_define.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.created_time IS '创建时间';


--
-- Name: COLUMN platform_entity_define.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.updater_id IS '更新人编号';


--
-- Name: COLUMN platform_entity_define.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.updated_by IS '更新人';


--
-- Name: COLUMN platform_entity_define.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.updated_time IS '更新时间';


--
-- Name: COLUMN platform_entity_define.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.version IS '乐观锁版本';


--
-- Name: COLUMN platform_entity_define.show_summary; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.show_summary IS '显示表尾合计';


--
-- Name: COLUMN platform_entity_define.query_bar_buttons; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.query_bar_buttons IS '查询栏拓展按钮';


--
-- Name: COLUMN platform_entity_define.operation_buttons; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.operation_buttons IS '操作列拓展按钮';


--
-- Name: COLUMN platform_entity_define.table_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_entity_define.table_name IS '数据库表名';


--
-- Name: platform_entity_define_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_entity_define_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_entity_define_id_seq OWNER TO postgres;

--
-- Name: platform_entity_define_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_entity_define_id_seq OWNED BY tuna_cloud_platform.platform_entity_define.id;


--
-- Name: platform_group_i18n; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_group_i18n (
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(1024) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_group_i18n OWNER TO postgres;

--
-- Name: TABLE platform_group_i18n; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_group_i18n IS '平台国际化翻译语言包';


--
-- Name: COLUMN platform_group_i18n.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.id IS '主键';


--
-- Name: COLUMN platform_group_i18n.group_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.group_id IS '应用编号';


--
-- Name: COLUMN platform_group_i18n.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.code IS '编码';


--
-- Name: COLUMN platform_group_i18n.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.name IS '文案';


--
-- Name: COLUMN platform_group_i18n.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_group_i18n.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_group_i18n.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.created_time IS '创建时间';


--
-- Name: COLUMN platform_group_i18n.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_group_i18n.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_group_i18n.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.updated_time IS '更新时间';


--
-- Name: COLUMN platform_group_i18n.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_group_i18n.version IS '乐观锁版本号';


--
-- Name: platform_group_i18n_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_group_i18n_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_group_i18n_id_seq OWNER TO postgres;

--
-- Name: platform_group_i18n_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_group_i18n_id_seq OWNED BY tuna_cloud_platform.platform_group_i18n.id;


--
-- Name: platform_job; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_job (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    org_id bigint NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    show_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_job OWNER TO postgres;

--
-- Name: TABLE platform_job; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_job IS '平台岗位定义表';


--
-- Name: COLUMN platform_job.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_job.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.code IS '编码（业务唯一标识）';


--
-- Name: COLUMN platform_job.org_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.org_id IS '组织编码';


--
-- Name: COLUMN platform_job.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.status IS '状态：0-禁用，1-正常';


--
-- Name: COLUMN platform_job.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.name IS '名称';


--
-- Name: COLUMN platform_job.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.remark IS '备注';


--
-- Name: COLUMN platform_job.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.show_order IS '显示顺序，值越小，越靠前';


--
-- Name: COLUMN platform_job.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_job.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_job.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.created_time IS '创建时间';


--
-- Name: COLUMN platform_job.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_job.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_job.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.updated_time IS '更新时间';


--
-- Name: COLUMN platform_job.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job.version IS '乐观锁版本号';


--
-- Name: platform_job_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_job_id_seq OWNER TO postgres;

--
-- Name: platform_job_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_job_id_seq OWNED BY tuna_cloud_platform.platform_job.id;


--
-- Name: platform_job_resource; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_job_resource (
    job_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_job_resource OWNER TO postgres;

--
-- Name: TABLE platform_job_resource; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_job_resource IS '平台用户分组授权表';


--
-- Name: COLUMN platform_job_resource.job_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_resource.job_id IS '平台用户分组ID';


--
-- Name: COLUMN platform_job_resource.resource_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_job_resource.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_job_resource.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_job_resource.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_resource.created_time IS '创建时间';


--
-- Name: platform_job_user; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_job_user (
    user_id bigint NOT NULL,
    job_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_job_user OWNER TO postgres;

--
-- Name: TABLE platform_job_user; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_job_user IS '平台岗位用户关联表';


--
-- Name: COLUMN platform_job_user.user_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_user.user_id IS '平台资源关联的角色ID';


--
-- Name: COLUMN platform_job_user.job_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_user.job_id IS '平台资源ID';


--
-- Name: COLUMN platform_job_user.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_user.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_job_user.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_user.created_by IS '创建人名称';


--
-- Name: COLUMN platform_job_user.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_job_user.created_time IS '创建时间';


--
-- Name: platform_login_log; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_login_log (
    id bigint NOT NULL,
    type smallint DEFAULT 0 NOT NULL,
    username character varying(64) NOT NULL,
    real_name character varying(64) DEFAULT ''::character varying,
    ip_address character varying(64) DEFAULT ''::character varying,
    session_id character varying(64) DEFAULT ''::character varying,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_login_log OWNER TO postgres;

--
-- Name: TABLE platform_login_log; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_login_log IS '登录日志';


--
-- Name: COLUMN platform_login_log.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.id IS '主键';


--
-- Name: COLUMN platform_login_log.type; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.type IS '类型';


--
-- Name: COLUMN platform_login_log.username; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.username IS '账号';


--
-- Name: COLUMN platform_login_log.real_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.real_name IS '姓名';


--
-- Name: COLUMN platform_login_log.ip_address; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.ip_address IS 'IP';


--
-- Name: COLUMN platform_login_log.session_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.session_id IS 'session';


--
-- Name: COLUMN platform_login_log.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.remark IS '备注';


--
-- Name: COLUMN platform_login_log.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.creator_id IS '创建人账户编号';


--
-- Name: COLUMN platform_login_log.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_login_log.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_login_log.created_time IS '创建时间';


--
-- Name: platform_login_log_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_login_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_login_log_id_seq OWNER TO postgres;

--
-- Name: platform_login_log_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_login_log_id_seq OWNED BY tuna_cloud_platform.platform_login_log.id;


--
-- Name: platform_openapi; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_openapi (
    id bigint NOT NULL,
    service_id bigint NOT NULL,
    name character varying(512) NOT NULL,
    http_method character varying(32) NOT NULL,
    class_method character varying(512) NOT NULL,
    uri character varying(256) DEFAULT NULL::character varying,
    remark character varying(2048) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_openapi OWNER TO postgres;

--
-- Name: TABLE platform_openapi; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_openapi IS '平台接口定义';


--
-- Name: COLUMN platform_openapi.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.id IS '编号';


--
-- Name: COLUMN platform_openapi.service_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.service_id IS '服务编号';


--
-- Name: COLUMN platform_openapi.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.name IS '接口名称';


--
-- Name: COLUMN platform_openapi.http_method; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.http_method IS 'Http方法';


--
-- Name: COLUMN platform_openapi.class_method; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.class_method IS '类方法';


--
-- Name: COLUMN platform_openapi.uri; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.uri IS 'URI';


--
-- Name: COLUMN platform_openapi.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.remark IS '说明';


--
-- Name: COLUMN platform_openapi.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.creator_id IS '创建人编号';


--
-- Name: COLUMN platform_openapi.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.created_by IS '创建人';


--
-- Name: COLUMN platform_openapi.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.created_time IS '创建时间';


--
-- Name: COLUMN platform_openapi.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.updater_id IS '更新人编号';


--
-- Name: COLUMN platform_openapi.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.updated_by IS '更新人';


--
-- Name: COLUMN platform_openapi.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.updated_time IS '更新时间';


--
-- Name: COLUMN platform_openapi.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_openapi.version IS '版本';


--
-- Name: platform_openapi_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_openapi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_openapi_id_seq OWNER TO postgres;

--
-- Name: platform_openapi_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_openapi_id_seq OWNED BY tuna_cloud_platform.platform_openapi.id;


--
-- Name: platform_org; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_org (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    parent_id bigint NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    show_order smallint DEFAULT 1 NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_org OWNER TO postgres;

--
-- Name: TABLE platform_org; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_org IS '平台组织定义表';


--
-- Name: COLUMN platform_org.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_org.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.code IS '编码（业务唯一标识）';


--
-- Name: COLUMN platform_org.parent_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.parent_id IS '父级编码';


--
-- Name: COLUMN platform_org.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.status IS '状态：0-禁用，1-正常';


--
-- Name: COLUMN platform_org.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.name IS '名称';


--
-- Name: COLUMN platform_org.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.remark IS '备注';


--
-- Name: COLUMN platform_org.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.show_order IS '显示顺序，值越小，越靠前';


--
-- Name: COLUMN platform_org.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_org.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_org.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.created_time IS '创建时间';


--
-- Name: COLUMN platform_org.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_org.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_org.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.updated_time IS '更新时间';


--
-- Name: COLUMN platform_org.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org.version IS '乐观锁版本号';


--
-- Name: platform_org_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_org_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_org_id_seq OWNER TO postgres;

--
-- Name: platform_org_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_org_id_seq OWNED BY tuna_cloud_platform.platform_org.id;


--
-- Name: platform_org_resource; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_org_resource (
    org_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_org_resource OWNER TO postgres;

--
-- Name: TABLE platform_org_resource; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_org_resource IS '平台组织授权表';


--
-- Name: COLUMN platform_org_resource.org_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org_resource.org_id IS '平台用户分组ID';


--
-- Name: COLUMN platform_org_resource.resource_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_org_resource.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_org_resource.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_org_resource.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_org_resource.created_time IS '创建时间';


--
-- Name: platform_realm; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_realm (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_realm OWNER TO postgres;

--
-- Name: TABLE platform_realm; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_realm IS '平台用户领域定义表';


--
-- Name: COLUMN platform_realm.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_realm.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.code IS '分组编码（业务唯一标识，用于关联资源）';


--
-- Name: COLUMN platform_realm.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.name IS '分组名称';


--
-- Name: COLUMN platform_realm.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.remark IS '备注';


--
-- Name: COLUMN platform_realm.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_realm.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_realm.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.created_time IS '创建时间';


--
-- Name: COLUMN platform_realm.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_realm.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_realm.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.updated_time IS '更新时间';


--
-- Name: COLUMN platform_realm.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_realm.version IS '乐观锁版本号';


--
-- Name: platform_realm_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_realm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_realm_id_seq OWNER TO postgres;

--
-- Name: platform_realm_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_realm_id_seq OWNED BY tuna_cloud_platform.platform_realm.id;


--
-- Name: platform_resource; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_resource (
    id bigint NOT NULL,
    code character varying(150) NOT NULL,
    parent_id bigint,
    group_id bigint,
    name character varying(2048) NOT NULL,
    icon character varying(2048) DEFAULT NULL::character varying,
    visible smallint DEFAULT 1 NOT NULL,
    type smallint DEFAULT 1 NOT NULL,
    open_tab smallint DEFAULT 0 NOT NULL,
    affix smallint DEFAULT 0 NOT NULL,
    keep_alive smallint DEFAULT 0 NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    layout character varying(128) DEFAULT NULL::character varying,
    component character varying(256) DEFAULT NULL::character varying,
    router_url character varying(512),
    redirect_url character varying(512) DEFAULT NULL::character varying,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_resource OWNER TO postgres;

--
-- Name: TABLE platform_resource; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_resource IS '平台资源定义表（菜单/目录/按钮权限）';


--
-- Name: COLUMN platform_resource.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_resource.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.code IS '资源编码（业务唯一标识）';


--
-- Name: COLUMN platform_resource.parent_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.parent_id IS '上一级资源编码（构建树形结构）';


--
-- Name: COLUMN platform_resource.group_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.group_id IS '分组编码';


--
-- Name: COLUMN platform_resource.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.name IS '资源名称';


--
-- Name: COLUMN platform_resource.icon; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.icon IS '资源图标';


--
-- Name: COLUMN platform_resource.visible; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.visible IS '是否显示：0-否，1-显示';


--
-- Name: COLUMN platform_resource.type; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.type IS '资源类型：0-目录，1-菜单，2-按钮';


--
-- Name: COLUMN platform_resource.open_tab; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.open_tab IS '是否打开新的tab：0-否 1-打开';


--
-- Name: COLUMN platform_resource.affix; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.affix IS '在标签模式下是否固定：0-不固定，1-固定';


--
-- Name: COLUMN platform_resource.keep_alive; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.keep_alive IS '是否开启前端缓存：0-不开启，1-开启';


--
-- Name: COLUMN platform_resource.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.show_order IS '显示顺序，值越小越靠前';


--
-- Name: COLUMN platform_resource.layout; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.layout IS '前端布局';


--
-- Name: COLUMN platform_resource.component; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.component IS '前端组件路径';


--
-- Name: COLUMN platform_resource.router_url; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.router_url IS '前端路由地址';


--
-- Name: COLUMN platform_resource.redirect_url; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.redirect_url IS '重定向地址';


--
-- Name: COLUMN platform_resource.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.remark IS '备注';


--
-- Name: COLUMN platform_resource.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_resource.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_resource.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.created_time IS '创建时间';


--
-- Name: COLUMN platform_resource.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_resource.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_resource.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.updated_time IS '更新时间';


--
-- Name: COLUMN platform_resource.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource.version IS '乐观锁版本号';


--
-- Name: platform_resource_category; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_resource_category (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(2000) NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1,
    parent_id bigint
);


ALTER TABLE tuna_cloud_platform.platform_resource_category OWNER TO postgres;

--
-- Name: TABLE platform_resource_category; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_resource_category IS '平台资源分类表';


--
-- Name: COLUMN platform_resource_category.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_resource_category.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.code IS '编码';


--
-- Name: COLUMN platform_resource_category.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.name IS '名称';


--
-- Name: COLUMN platform_resource_category.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.show_order IS '显示顺序';


--
-- Name: COLUMN platform_resource_category.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.remark IS '备注';


--
-- Name: COLUMN platform_resource_category.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_resource_category.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_resource_category.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.created_time IS '创建时间';


--
-- Name: COLUMN platform_resource_category.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_resource_category.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_resource_category.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.updated_time IS '更新时间';


--
-- Name: COLUMN platform_resource_category.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.version IS '乐观锁版本号';


--
-- Name: COLUMN platform_resource_category.parent_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_category.parent_id IS '父级编号';


--
-- Name: platform_resource_category_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_resource_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_resource_category_id_seq OWNER TO postgres;

--
-- Name: platform_resource_category_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_resource_category_id_seq OWNED BY tuna_cloud_platform.platform_resource_category.id;


--
-- Name: platform_resource_group; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_resource_group (
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(2048) NOT NULL,
    show_order smallint DEFAULT 1 NOT NULL,
    router character varying(256),
    navbar_menu smallint DEFAULT 0 NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1,
    entry character varying(512),
    props character varying(512),
    is_microapp smallint DEFAULT 0
);


ALTER TABLE tuna_cloud_platform.platform_resource_group OWNER TO postgres;

--
-- Name: TABLE platform_resource_group; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_resource_group IS '平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等）';


--
-- Name: COLUMN platform_resource_group.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_resource_group.category_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.category_id IS '资源分类';


--
-- Name: COLUMN platform_resource_group.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.code IS '编码';


--
-- Name: COLUMN platform_resource_group.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.name IS '名称';


--
-- Name: COLUMN platform_resource_group.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.show_order IS '显示顺序';


--
-- Name: COLUMN platform_resource_group.router; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.router IS '路由';


--
-- Name: COLUMN platform_resource_group.navbar_menu; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.navbar_menu IS '快捷菜单';


--
-- Name: COLUMN platform_resource_group.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.remark IS '备注';


--
-- Name: COLUMN platform_resource_group.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_resource_group.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_resource_group.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.created_time IS '创建时间';


--
-- Name: COLUMN platform_resource_group.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_resource_group.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_resource_group.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.updated_time IS '更新时间';


--
-- Name: COLUMN platform_resource_group.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.version IS '乐观锁版本号';


--
-- Name: COLUMN platform_resource_group.entry; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.entry IS '入口地址';


--
-- Name: COLUMN platform_resource_group.props; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.props IS '拓展属性';


--
-- Name: COLUMN platform_resource_group.is_microapp; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_group.is_microapp IS '是否微应用0:否 1：是';


--
-- Name: platform_resource_group_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_resource_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_resource_group_id_seq OWNER TO postgres;

--
-- Name: platform_resource_group_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_resource_group_id_seq OWNED BY tuna_cloud_platform.platform_resource_group.id;


--
-- Name: platform_resource_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_resource_id_seq OWNER TO postgres;

--
-- Name: platform_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_resource_id_seq OWNED BY tuna_cloud_platform.platform_resource.id;


--
-- Name: platform_resource_openapi; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_resource_openapi (
    resource_id bigint NOT NULL,
    openapi_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_resource_openapi OWNER TO postgres;

--
-- Name: TABLE platform_resource_openapi; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_resource_openapi IS 'OAuth2客户端与平台资源的授权关联表';


--
-- Name: COLUMN platform_resource_openapi.resource_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_openapi.resource_id IS 'OAuth2客户端ID，引用oauth2_registered_client表';


--
-- Name: COLUMN platform_resource_openapi.openapi_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_openapi.openapi_id IS '平台资源ID，引用openapi_id';


--
-- Name: COLUMN platform_resource_openapi.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_openapi.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_resource_openapi.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_openapi.created_by IS '创建人名称';


--
-- Name: COLUMN platform_resource_openapi.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_resource_openapi.created_time IS '创建时间';


--
-- Name: platform_role; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_role (
    id bigint NOT NULL,
    code character varying(128) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(1024) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_role OWNER TO postgres;

--
-- Name: TABLE platform_role; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_role IS '平台角色定义表';


--
-- Name: COLUMN platform_role.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.id IS '主键';


--
-- Name: COLUMN platform_role.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.code IS '编码';


--
-- Name: COLUMN platform_role.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.status IS '状态';


--
-- Name: COLUMN platform_role.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.name IS '名称';


--
-- Name: COLUMN platform_role.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.remark IS '备注';


--
-- Name: COLUMN platform_role.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_role.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_role.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.created_time IS '创建时间';


--
-- Name: COLUMN platform_role.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_role.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_role.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.updated_time IS '更新时间';


--
-- Name: COLUMN platform_role.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role.version IS '乐观锁版本号';


--
-- Name: platform_role_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_role_id_seq OWNER TO postgres;

--
-- Name: platform_role_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_role_id_seq OWNED BY tuna_cloud_platform.platform_role.id;


--
-- Name: platform_role_resource; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_role_resource (
    role_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_role_resource OWNER TO postgres;

--
-- Name: TABLE platform_role_resource; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_role_resource IS '平台资源角色授权表';


--
-- Name: COLUMN platform_role_resource.role_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role_resource.role_id IS '平台资源关联的角色ID';


--
-- Name: COLUMN platform_role_resource.resource_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_role_resource.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_role_resource.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_role_resource.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_role_resource.created_time IS '创建时间';


--
-- Name: platform_schema_define; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_schema_define (
    id bigint NOT NULL,
    class_name character varying(255),
    name character varying(255) NOT NULL,
    cn_name character varying(255),
    i18n_key character varying(100),
    display_name character varying(1024),
    type smallint DEFAULT 0 NOT NULL,
    type_class_name character varying(255),
    is_show smallint DEFAULT 1 NOT NULL,
    is_sort smallint DEFAULT 0 NOT NULL,
    is_overflow smallint DEFAULT 0 NOT NULL,
    is_search smallint DEFAULT 1 NOT NULL,
    is_fixed smallint DEFAULT 0 NOT NULL,
    is_require smallint DEFAULT 0 NOT NULL,
    is_form smallint DEFAULT 1 NOT NULL,
    align smallint DEFAULT 0 NOT NULL,
    width integer DEFAULT 120,
    show_order integer DEFAULT 1,
    max_length integer DEFAULT 50,
    min_length integer DEFAULT 1,
    max integer DEFAULT 2147483647,
    min integer DEFAULT 0,
    "precision" integer DEFAULT 0,
    multiple smallint DEFAULT 0 NOT NULL,
    pattern smallint DEFAULT '-1'::integer NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1,
    service_name character varying(255),
    is_i18n smallint DEFAULT 0,
    relation_type smallint,
    external_class character varying(255) DEFAULT NULL::character varying,
    external_class_primary_field character varying(80) DEFAULT NULL::character varying,
    external_class_display_field character varying(80) DEFAULT NULL::character varying,
    ui_component smallint,
    field_name character varying(255) DEFAULT NULL::character varying,
    description character varying(1024) DEFAULT NULL::character varying,
    encrypt smallint DEFAULT 1 NOT NULL,
    desensitize smallint DEFAULT 1 NOT NULL,
    field_component character varying(512) DEFAULT NULL::character varying,
    inner_primary_field character varying(80) DEFAULT NULL::character varying,
    column_component character varying(512) DEFAULT NULL::character varying
);


ALTER TABLE tuna_cloud_platform.platform_schema_define OWNER TO postgres;

--
-- Name: TABLE platform_schema_define; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_schema_define IS '列配置表（存储业务实体字段的展示/校验/表单等配置）';


--
-- Name: COLUMN platform_schema_define.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_schema_define.class_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.class_name IS '所属业务实体';


--
-- Name: COLUMN platform_schema_define.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.name IS '字段名';


--
-- Name: COLUMN platform_schema_define.cn_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.cn_name IS '中文名';


--
-- Name: COLUMN platform_schema_define.i18n_key; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.i18n_key IS '国际化Key';


--
-- Name: COLUMN platform_schema_define.display_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.display_name IS '国际化：名字';


--
-- Name: COLUMN platform_schema_define.type; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.type IS '属性类型';


--
-- Name: COLUMN platform_schema_define.type_class_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.type_class_name IS '属性类型类名';


--
-- Name: COLUMN platform_schema_define.is_show; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_show IS '是否显示：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_sort; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_sort IS '是否排序：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_overflow; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_overflow IS '是否省略：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_search; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_search IS '是否搜索：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_fixed; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_fixed IS '是否固定列：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_require; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_require IS '是否必填：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.is_form; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.is_form IS '是否表单字段：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.align; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.align IS '对齐方式：LEFT-左对齐，RIGHT-右对齐，CENTER-居中';


--
-- Name: COLUMN platform_schema_define.width; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.width IS '显示宽度';


--
-- Name: COLUMN platform_schema_define.show_order; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.show_order IS '显示顺序';


--
-- Name: COLUMN platform_schema_define.max_length; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.max_length IS '最大长度';


--
-- Name: COLUMN platform_schema_define.min_length; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.min_length IS '最小长度';


--
-- Name: COLUMN platform_schema_define.max; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.max IS '最大值';


--
-- Name: COLUMN platform_schema_define.min; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.min IS '最小值';


--
-- Name: COLUMN platform_schema_define."precision"; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define."precision" IS '精度';


--
-- Name: COLUMN platform_schema_define.multiple; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.multiple IS '是否支持多选：YES-是，NO-否';


--
-- Name: COLUMN platform_schema_define.pattern; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.pattern IS '字段校验规则';


--
-- Name: COLUMN platform_schema_define.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_schema_define.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.created_by IS '创建人（用户ID）';


--
-- Name: COLUMN platform_schema_define.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.created_time IS '创建时间';


--
-- Name: COLUMN platform_schema_define.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_schema_define.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_schema_define.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.updated_time IS '更新时间';


--
-- Name: COLUMN platform_schema_define.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.version IS '乐观锁版本号';


--
-- Name: COLUMN platform_schema_define.service_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.service_name IS '服务名';


--
-- Name: COLUMN platform_schema_define.relation_type; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.relation_type IS '关联类型';


--
-- Name: COLUMN platform_schema_define.external_class; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.external_class IS '关联对象类名';


--
-- Name: COLUMN platform_schema_define.external_class_primary_field; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.external_class_primary_field IS '关联对象字段';


--
-- Name: COLUMN platform_schema_define.external_class_display_field; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.external_class_display_field IS '关联对象展示字段';


--
-- Name: COLUMN platform_schema_define.ui_component; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.ui_component IS '关联对象前端组件';


--
-- Name: COLUMN platform_schema_define.field_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.field_name IS '字段名';


--
-- Name: COLUMN platform_schema_define.description; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.description IS '说明';


--
-- Name: COLUMN platform_schema_define.encrypt; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.encrypt IS '是否加密';


--
-- Name: COLUMN platform_schema_define.desensitize; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.desensitize IS '是否脱敏';


--
-- Name: COLUMN platform_schema_define.field_component; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.field_component IS '表单自定义组件';


--
-- Name: COLUMN platform_schema_define.inner_primary_field; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.inner_primary_field IS '明细表关联字段';


--
-- Name: COLUMN platform_schema_define.column_component; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_schema_define.column_component IS '表格列自定义组件';


--
-- Name: platform_schema_define_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_schema_define_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_schema_define_id_seq OWNER TO postgres;

--
-- Name: platform_schema_define_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_schema_define_id_seq OWNED BY tuna_cloud_platform.platform_schema_define.id;


--
-- Name: platform_service_instance; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_service_instance (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    context_path character varying(512) NOT NULL,
    status smallint DEFAULT 1 NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_service_instance OWNER TO postgres;

--
-- Name: TABLE platform_service_instance; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_service_instance IS '平台服务定义表';


--
-- Name: COLUMN platform_service_instance.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.id IS '编号';


--
-- Name: COLUMN platform_service_instance.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.code IS '编码';


--
-- Name: COLUMN platform_service_instance.context_path; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.context_path IS '路径';


--
-- Name: COLUMN platform_service_instance.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.status IS '状态';


--
-- Name: COLUMN platform_service_instance.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.name IS '名称';


--
-- Name: COLUMN platform_service_instance.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.remark IS '备注';


--
-- Name: COLUMN platform_service_instance.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.creator_id IS '创建人编号';


--
-- Name: COLUMN platform_service_instance.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.created_by IS '创建人';


--
-- Name: COLUMN platform_service_instance.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.created_time IS '创建时间';


--
-- Name: COLUMN platform_service_instance.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.updater_id IS '更新人编号';


--
-- Name: COLUMN platform_service_instance.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.updated_by IS '更新人';


--
-- Name: COLUMN platform_service_instance.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.updated_time IS '更新时间';


--
-- Name: COLUMN platform_service_instance.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_service_instance.version IS '版本号';


--
-- Name: platform_service_instance_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_service_instance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_service_instance_id_seq OWNER TO postgres;

--
-- Name: platform_service_instance_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_service_instance_id_seq OWNED BY tuna_cloud_platform.platform_service_instance.id;


--
-- Name: platform_user; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_user (
    id bigint NOT NULL,
    user_id uuid DEFAULT gen_random_uuid() NOT NULL,
    username character varying(64) NOT NULL,
    password character varying(256) NOT NULL,
    salt character varying(64) DEFAULT ''::character varying,
    account_expired_at timestamp without time zone,
    credentials_expired_at timestamp without time zone,
    real_name character varying(64) DEFAULT ''::character varying,
    nickname character varying(64) DEFAULT ''::character varying,
    gender smallint DEFAULT 0,
    birthday date,
    avatar character varying(512) DEFAULT ''::character varying,
    mobile character varying(20) DEFAULT ''::character varying,
    mobile_verified smallint,
    email character varying(128) DEFAULT ''::character varying,
    email_verified smallint,
    status smallint DEFAULT 1 NOT NULL,
    user_type smallint DEFAULT 0 NOT NULL,
    login_fail_count smallint DEFAULT 0 NOT NULL,
    lock_time timestamp without time zone,
    last_login_time timestamp without time zone,
    last_login_ip character varying(64) DEFAULT ''::character varying,
    tenant_id character varying(64) DEFAULT 'default'::character varying,
    realm_id bigint,
    org_id bigint,
    dept_id bigint,
    ext_info jsonb DEFAULT '{}'::jsonb,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64),
    created_by character varying(64) DEFAULT NULL::character varying,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    is_deleted smallint DEFAULT 0 NOT NULL,
    deleter_id character varying(64) DEFAULT ''::character varying,
    deleted_by character varying(64) DEFAULT ''::character varying,
    deleted_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_user OWNER TO postgres;

--
-- Name: COLUMN platform_user.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_user.user_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.user_id IS '业务唯一标识（UUID，对外暴露/集成用，避免自增ID泄露）';


--
-- Name: COLUMN platform_user.username; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.username IS '登录账号（支持手机号/邮箱/自定义账号，租户内唯一）';


--
-- Name: COLUMN platform_user.password; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.password IS '密码（加密存储）';


--
-- Name: COLUMN platform_user.salt; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.salt IS '密码盐值';


--
-- Name: COLUMN platform_user.real_name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.real_name IS '真实姓名';


--
-- Name: COLUMN platform_user.nickname; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.nickname IS '用户昵称';


--
-- Name: COLUMN platform_user.gender; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.gender IS '性别：0-未知，1-男，2-女';


--
-- Name: COLUMN platform_user.birthday; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.birthday IS '出生日期';


--
-- Name: COLUMN platform_user.avatar; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.avatar IS '头像URL';


--
-- Name: COLUMN platform_user.mobile; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.mobile IS '手机号';


--
-- Name: COLUMN platform_user.mobile_verified; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.mobile_verified IS '手机号是否验证';


--
-- Name: COLUMN platform_user.email; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.email IS '邮箱';


--
-- Name: COLUMN platform_user.email_verified; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.email_verified IS '邮箱是否验证';


--
-- Name: COLUMN platform_user.status; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.status IS '用户状态：0-禁用，1-正常，2-锁定，3-注销';


--
-- Name: COLUMN platform_user.user_type; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.user_type IS '用户类型：0-普通用户，1-管理员，2-系统用户，9-第三方用户';


--
-- Name: COLUMN platform_user.login_fail_count; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.login_fail_count IS '登录失败次数（防暴力破解）';


--
-- Name: COLUMN platform_user.lock_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.lock_time IS '账号锁定时间';


--
-- Name: COLUMN platform_user.last_login_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.last_login_time IS '最后登录时间';


--
-- Name: COLUMN platform_user.last_login_ip; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.last_login_ip IS '最后登录IP';


--
-- Name: COLUMN platform_user.tenant_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user.tenant_id IS '租户ID（多租户隔离）';


--
-- Name: platform_user_group; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_user_group (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    remark character varying(512) DEFAULT NULL::character varying,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id character varying(64) DEFAULT NULL::character varying,
    updated_by character varying(64) DEFAULT NULL::character varying,
    updated_time timestamp without time zone,
    version integer DEFAULT 1
);


ALTER TABLE tuna_cloud_platform.platform_user_group OWNER TO postgres;

--
-- Name: TABLE platform_user_group; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_user_group IS '平台用户分组定义表';


--
-- Name: COLUMN platform_user_group.id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.id IS '主键（自增ID，内部使用）';


--
-- Name: COLUMN platform_user_group.code; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.code IS '分组编码（业务唯一标识，用于关联资源）';


--
-- Name: COLUMN platform_user_group.name; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.name IS '分组名称';


--
-- Name: COLUMN platform_user_group.remark; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.remark IS '备注';


--
-- Name: COLUMN platform_user_group.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.creator_id IS '创建人账户ID';


--
-- Name: COLUMN platform_user_group.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.created_by IS '创建人姓名';


--
-- Name: COLUMN platform_user_group.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.created_time IS '创建时间';


--
-- Name: COLUMN platform_user_group.updater_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.updater_id IS '更新人账户ID';


--
-- Name: COLUMN platform_user_group.updated_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.updated_by IS '更新人姓名';


--
-- Name: COLUMN platform_user_group.updated_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.updated_time IS '更新时间';


--
-- Name: COLUMN platform_user_group.version; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group.version IS '乐观锁版本号';


--
-- Name: platform_user_group_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_user_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_user_group_id_seq OWNER TO postgres;

--
-- Name: platform_user_group_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_user_group_id_seq OWNED BY tuna_cloud_platform.platform_user_group.id;


--
-- Name: platform_user_group_resource; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_user_group_resource (
    group_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_user_group_resource OWNER TO postgres;

--
-- Name: TABLE platform_user_group_resource; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_user_group_resource IS '平台用户分组授权表';


--
-- Name: COLUMN platform_user_group_resource.group_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group_resource.group_id IS '平台用户分组ID';


--
-- Name: COLUMN platform_user_group_resource.resource_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group_resource.resource_id IS '平台资源ID';


--
-- Name: COLUMN platform_user_group_resource.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_user_group_resource.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_user_group_resource.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_group_resource.created_time IS '创建时间';


--
-- Name: platform_user_group_user; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_user_group_user (
    user_id bigint NOT NULL,
    group_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_user_group_user OWNER TO postgres;

--
-- Name: platform_user_id_seq; Type: SEQUENCE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE SEQUENCE tuna_cloud_platform.platform_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE tuna_cloud_platform.platform_user_id_seq OWNER TO postgres;

--
-- Name: platform_user_id_seq; Type: SEQUENCE OWNED BY; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER SEQUENCE tuna_cloud_platform.platform_user_id_seq OWNED BY tuna_cloud_platform.platform_user.id;


--
-- Name: platform_user_resource; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_user_resource (
    user_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_user_resource OWNER TO postgres;

--
-- Name: TABLE platform_user_resource; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_user_resource IS '平台用户授权表';


--
-- Name: COLUMN platform_user_resource.user_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_resource.user_id IS '平台资源关联的用户ID';


--
-- Name: COLUMN platform_user_resource.resource_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_resource.resource_id IS '平台资源关联的资源ID';


--
-- Name: COLUMN platform_user_resource.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_resource.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_user_resource.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_resource.created_by IS '创建人名称';


--
-- Name: COLUMN platform_user_resource.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_resource.created_time IS '创建时间';


--
-- Name: platform_user_role; Type: TABLE; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE TABLE tuna_cloud_platform.platform_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    creator_id character varying(64) NOT NULL,
    created_by character varying(64) NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE tuna_cloud_platform.platform_user_role OWNER TO postgres;

--
-- Name: TABLE platform_user_role; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON TABLE tuna_cloud_platform.platform_user_role IS '平台角色授权表';


--
-- Name: COLUMN platform_user_role.user_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_role.user_id IS '平台资源关联的用户ID';


--
-- Name: COLUMN platform_user_role.role_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_role.role_id IS '平台资源关联的角色ID';


--
-- Name: COLUMN platform_user_role.creator_id; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_role.creator_id IS '创建人账户ID（如：工号/账号）';


--
-- Name: COLUMN platform_user_role.created_by; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_role.created_by IS '创建人名称';


--
-- Name: COLUMN platform_user_role.created_time; Type: COMMENT; Schema: tuna_cloud_platform; Owner: postgres
--

COMMENT ON COLUMN tuna_cloud_platform.platform_user_role.created_time IS '创建时间';


--
-- Name: platform_dictionary id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_dictionary ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_dictionary1_id_seq'::regclass);


--
-- Name: platform_dictionary_item id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_dictionary_item ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_dictionary_item_id_seq'::regclass);


--
-- Name: platform_entity_define id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_entity_define ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_entity_define_id_seq'::regclass);


--
-- Name: platform_group_i18n id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_group_i18n ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_group_i18n_id_seq'::regclass);


--
-- Name: platform_job id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_job_id_seq'::regclass);


--
-- Name: platform_login_log id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_login_log ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_login_log_id_seq'::regclass);


--
-- Name: platform_openapi id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_openapi ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_openapi_id_seq'::regclass);


--
-- Name: platform_org id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_org ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_org_id_seq'::regclass);


--
-- Name: platform_realm id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_realm ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_realm_id_seq'::regclass);


--
-- Name: platform_resource id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_resource_id_seq'::regclass);


--
-- Name: platform_resource_category id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_category ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_resource_category_id_seq'::regclass);


--
-- Name: platform_resource_group id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_group ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_resource_group_id_seq'::regclass);


--
-- Name: platform_role id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_role ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_role_id_seq'::regclass);


--
-- Name: platform_schema_define id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_schema_define ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_schema_define_id_seq'::regclass);


--
-- Name: platform_service_instance id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_service_instance ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_service_instance_id_seq'::regclass);


--
-- Name: platform_user id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_user_id_seq'::regclass);


--
-- Name: platform_user_group id; Type: DEFAULT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group ALTER COLUMN id SET DEFAULT nextval('byteasy_cloud_platform.platform_user_group_id_seq'::regclass);


--
-- Name: knowledge_category id; Type: DEFAULT; Schema: knowledge_base; Owner: postgres
--

ALTER TABLE ONLY knowledge_base.knowledge_category ALTER COLUMN id SET DEFAULT nextval('knowledge_base.knowledge_category_id_seq'::regclass);


--
-- Name: knowledge_file id; Type: DEFAULT; Schema: knowledge_base; Owner: postgres
--

ALTER TABLE ONLY knowledge_base.knowledge_file ALTER COLUMN id SET DEFAULT nextval('knowledge_base.knowledge_file_id_seq'::regclass);


--
-- Name: knowledge_file_history id; Type: DEFAULT; Schema: knowledge_base; Owner: postgres
--

ALTER TABLE ONLY knowledge_base.knowledge_file_history ALTER COLUMN id SET DEFAULT nextval('knowledge_base.knowledge_file_history_id_seq'::regclass);


--
-- Name: model_code_serial_no id; Type: DEFAULT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.model_code_serial_no ALTER COLUMN id SET DEFAULT nextval('micro_product_assistant.model_code_serial_no_id_seq'::regclass);


--
-- Name: model_env_pub_no id; Type: DEFAULT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.model_env_pub_no ALTER COLUMN id SET DEFAULT nextval('micro_product_assistant.model_env_pub_no_id_seq'::regclass);


--
-- Name: nameplate_record id; Type: DEFAULT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.nameplate_record ALTER COLUMN id SET DEFAULT nextval('micro_product_assistant.nameplate_record_id_seq'::regclass);


--
-- Name: print_record id; Type: DEFAULT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.print_record ALTER COLUMN id SET DEFAULT nextval('micro_product_assistant.print_record_id_seq'::regclass);


--
-- Name: qr_sync_job id; Type: DEFAULT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.qr_sync_job ALTER COLUMN id SET DEFAULT nextval('micro_product_assistant.qr_sync_job_id_seq'::regclass);


--
-- Name: vin_generate_request id; Type: DEFAULT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.vin_generate_request ALTER COLUMN id SET DEFAULT nextval('micro_product_assistant.vin_generate_request_id_seq'::regclass);


--
-- Name: platform_dictionary id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_dictionary ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_dictionary1_id_seq'::regclass);


--
-- Name: platform_dictionary_item id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_dictionary_item ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_dictionary_item_id_seq'::regclass);


--
-- Name: platform_entity_define id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_entity_define ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_entity_define_id_seq'::regclass);


--
-- Name: platform_group_i18n id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_group_i18n ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_group_i18n_id_seq'::regclass);


--
-- Name: platform_job id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_job_id_seq'::regclass);


--
-- Name: platform_login_log id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_login_log ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_login_log_id_seq'::regclass);


--
-- Name: platform_openapi id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_openapi ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_openapi_id_seq'::regclass);


--
-- Name: platform_org id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_org ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_org_id_seq'::regclass);


--
-- Name: platform_realm id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_realm ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_realm_id_seq'::regclass);


--
-- Name: platform_resource id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_resource_id_seq'::regclass);


--
-- Name: platform_resource_category id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_category ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_resource_category_id_seq'::regclass);


--
-- Name: platform_resource_group id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_group ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_resource_group_id_seq'::regclass);


--
-- Name: platform_role id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_role ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_role_id_seq'::regclass);


--
-- Name: platform_schema_define id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_schema_define ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_schema_define_id_seq'::regclass);


--
-- Name: platform_service_instance id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_service_instance ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_service_instance_id_seq'::regclass);


--
-- Name: platform_user id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_user_id_seq'::regclass);


--
-- Name: platform_user_group id; Type: DEFAULT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group ALTER COLUMN id SET DEFAULT nextval('tuna_cloud_platform.platform_user_group_id_seq'::regclass);


--
-- Data for Name: oauth2_authorization; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.oauth2_authorization (id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at, authorization_code_metadata, access_token_value, access_token_issued_at, access_token_expires_at, access_token_metadata, access_token_type, access_token_scopes, oidc_id_token_value, oidc_id_token_issued_at, oidc_id_token_expires_at, oidc_id_token_metadata, refresh_token_value, refresh_token_issued_at, refresh_token_expires_at, refresh_token_metadata, user_code_value, user_code_issued_at, user_code_expires_at, user_code_metadata, device_code_value, device_code_issued_at, device_code_expires_at, device_code_metadata) FROM stdin;
45fabd5e-4eaf-45c1-bfcb-ca2d7cb47ec7	55d05d31-dec8-475b-8852-8c6e5d491134	administrator	authorization_code	openid,profile	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://tuna.net/platformSvr/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"demo-client","redirectUri":"http://tuna.net/platformSvr/sapi/redirect","scopes":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"state":"751c64d132130f43f68113fc39bf36d0","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap","continue":""},"authorizationRequestUri":"http://tuna.net/platformSvr/oauth2/authorize?response_type=code&client_id=demo-client&scope=openid%20profile&state=751c64d132130f43f68113fc39bf36d0&redirect_uri=http://tuna.net/platformSvr/sapi/redirect&continue=","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"192.168.56.102","sessionId":"5E024E95D3FA5A30666A0B6436BDC381"},"authenticated":true,"principal":{"@class":"com.byteasy.platform.service.impl.PlatformUserDetails","id":4,"creatorId":"ca029a30-41ad-4c15-81d0-e8c7116f1ad8","createdBy":"管理员","createdTime":"2026-04-09 15:38:43","updaterId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","updatedBy":"超级管理员","updatedTime":"2026-04-29 16:51:43","version":5,"isDeleted":"NORMAL","deleterId":"","deletedBy":"","deletedTime":null,"avatar":"","userId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","username":"administrator","password":"{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6","salt":"","accountExpiredAt":null,"credentialsExpiredAt":null,"realName":"超级管理员","nickname":"","gender":"MAN","birthday":"2026-04-21","mobile":"15315086265","mobileVerified":null,"email":"xushuyang@lgag.com","emailVerified":null,"status":"NORMAL","userType":"ADMIN","loginFailCount":0,"lockTime":null,"lastLoginTime":null,"lastLoginIp":"","tenantId":"default","realmId":null,"orgId":null,"deptId":null,"extInfo":null,"remark":null},"credentials":null}}	\N	GtFKieCfp6NObUlvmexY0kBe75PpqFPMyvG63AesAXjrFfdzLnyDqr-feqxHTdSgFWWniVI1_MBaISAanigsYp4mh1b4IoySyCPgnRvs-X9EwPuvvcblqeo_pjh-7Oqe	2026-07-01 16:55:24.833911	2026-07-01 17:00:24.833911	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":true}	eyJraWQiOiIwZTNiMjU5Ni1lNDQzLTRkODItYmE2Yy0wY2IxYmQ2Y2JhNmQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiYXVkIjoiZGVtby1jbGllbnQiLCJuYmYiOjE3ODI4OTYxMjUsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL3R1bmEubmV0L3BsYXRmb3JtU3ZyIiwiZXhwIjoxNzgzMjU2MTI1LCJpYXQiOjE3ODI4OTYxMjUsImp0aSI6IjRhN2JhN2ZjLWY4ZmMtNDVkOC05NGI1LWFhYjlkZjM3NTYxZiJ9.wSngmkG8lvHGdlRcFouCNwfEvpUtGx-t85Qb0aVxOD-nav3rwKAWbxdav7ZDSMxAjeR3kYKB3e17zMXqsaGk7ZLhUBxyQI-Bm709s3s1OhNqrq_6Fa36a46IMmshehqBS7zW_6prhlfWWxrhDAiIdS5Vn0E3xRjAeymt6ALD_Bn1K3asiCplvT_rmLyH9FUnk5aquyjolr7ZjL9cu2maVCDehvIjtsdiWZBrQKk0D7P5pC9dlbbUCtpOXlJB7N1ytJqd93gznvrRo7rVvbXPwKrQWwhPHfw-mwdL3CS8INi4FzTJarXRsYhF-ei8uMgn2Qk5rdkf1zTzIJDlnM8UTg	2026-07-01 16:55:25.101617	2026-07-05 20:55:25.101617	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat":"self-contained","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"administrator","aud":["java.util.Collections$SingletonList",["demo-client"]],"nbf":["java.time.Instant",1782896125.101616700],"scope":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"iss":["java.net.URL","http://tuna.net/platformSvr"],"exp":["java.time.Instant",1783256125.101616700],"iat":["java.time.Instant",1782896125.101616700],"jti":"4a7ba7fc-f8fc-45d8-94b5-aab9df37561f"},"metadata.token.invalidated":false}	Bearer	openid,profile	eyJraWQiOiIwZTNiMjU5Ni1lNDQzLTRkODItYmE2Yy0wY2IxYmQ2Y2JhNmQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMmE2ZDRiYy03ZjA0LTQzYTItOWZjOS0wY2I5OTc2NmE5OTMiLCJpc3MiOiJodHRwOi8vdHVuYS5uZXQvcGxhdGZvcm1TdnIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiZ2l2ZW5fbmFtZSI6Iue6p-euoeeQhuWRmCIsInNpZCI6IldCWGUwWmJtNTZZM19JMGlvVDhjREpucGtTT055MFJlM0pZRG9MMXBKMDQiLCJhdWQiOiJkZW1vLWNsaWVudCIsImF6cCI6ImRlbW8tY2xpZW50IiwiYXV0aF90aW1lIjoxNzgyODk2MTI0LCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwibmlja25hbWUiOiIiLCJwaG9uZV9udW1iZXIiOiIxNTMxNTA4NjI2NSIsImV4cCI6MTc4Mjg5NzkyNSwiaWF0IjoxNzgyODk2MTI1LCJmYW1pbHlfbmFtZSI6Iui2hSIsImp0aSI6IjY1ODI5YmZiLTQ0NDMtNDNlMy1iOGUwLWJkNjc5NzgwYWQ1NyIsImVtYWlsIjoieHVzaHV5YW5nQGxnYWcuY29tIn0.mOB1Ei8dl2TSfsRJ7MohEmwOG9IRSbB__T9UgJnhpPUMz_f3owWnPUFQp2H7OgnCK1FBPsuWDD5nbu1WyRd-zwZfk_tfE-ZmQuFC6PFaJtyl9qqcS7Vg7P7bufAUXYIfdjijzpDiN6x4riiptG5sIQ4c6UszIKwXcKVmPfMILJs75NA26T8_WQs7BbzNiLf1cuChmhjE4JZgm5WH6Jet-HHln_9bZ3tuamHrA0_QE8Y2TEj21qYNy2GXWo1E_lnAtMxshNiPYRlW-hAYLa9Iwr6hFSLoEZSMI_VP_IrD_URKVZssL8V_owBkfhDSsDBUHDNI3mj1hzFyFyKHc2Hz7Q	2026-07-01 16:55:25.232102	2026-07-01 17:25:25.232102	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","iss":["java.net.URL","http://tuna.net/platformSvr"],"preferred_username":"administrator","given_name":"级管理员","sid":"WBXe0Zbm56Y3_I0ioT8cDJnpkSONy0Re3JYDoL1pJ04","aud":["java.util.Collections$SingletonList",["demo-client"]],"azp":"demo-client","auth_time":["java.util.Date",1782896124901],"name":"超级管理员","nickname":"","phone_number":"15315086265","exp":["java.time.Instant",1782897925.232101600],"iat":["java.time.Instant",1782896125.232101600],"family_name":"超","jti":"65829bfb-4443-43e3-b8e0-bd679780ad57","email":"xushuyang@lgag.com"},"metadata.token.invalidated":false}	LxzSpoE6ZjHIgNXJiDdswsTomZg_BxDlogydwPY5FmaCWylI6mMDlhhhgd8HW56wtUGO42w1XIbruUbdpeiGwUs4T-8bUblfqqM3URBC3boy7OX-xSplyB8j6l5sJ4qP	2026-07-01 16:55:25.232102	2026-07-05 20:55:25.232102	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}	\N	\N	\N	\N	\N	\N	\N	\N
8b50d89b-d482-403c-a9e6-c70d98c91cfa	55d05d31-dec8-475b-8852-8c6e5d491134	administrator	authorization_code	openid,profile	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://tuna.net/platformSvr/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"demo-client","redirectUri":"http://tuna.net/platformSvr/sapi/redirect","scopes":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"state":"df23f8ccd94358ea173687df2b1f4ac9","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap"},"authorizationRequestUri":"http://tuna.net/platformSvr/oauth2/authorize?response_type=code&client_id=demo-client&scope=openid%20profile&state=df23f8ccd94358ea173687df2b1f4ac9&redirect_uri=http://tuna.net/platformSvr/sapi/redirect","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"192.168.56.102","sessionId":"36CDF1CD8F48AF6603CCD3719A53EF79"},"authenticated":true,"principal":{"@class":"com.byteasy.platform.service.impl.PlatformUserDetails","id":4,"creatorId":"ca029a30-41ad-4c15-81d0-e8c7116f1ad8","createdBy":"管理员","createdTime":"2026-04-09 15:38:43","updaterId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","updatedBy":"超级管理员","updatedTime":"2026-04-29 16:51:43","version":5,"isDeleted":"NORMAL","deleterId":"","deletedBy":"","deletedTime":null,"avatar":"","userId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","username":"administrator","password":"{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6","salt":"","accountExpiredAt":null,"credentialsExpiredAt":null,"realName":"超级管理员","nickname":"","gender":"MAN","birthday":"2026-04-21","mobile":"15315086265","mobileVerified":null,"email":"xushuyang@lgag.com","emailVerified":null,"status":"NORMAL","userType":"ADMIN","loginFailCount":0,"lockTime":null,"lastLoginTime":null,"lastLoginIp":"","tenantId":"default","realmId":null,"orgId":null,"deptId":null,"extInfo":null,"remark":null},"credentials":null}}	\N	PsQQiq0jeyrXWqfmfQnBbvC2tqmrOekC4_4PCSehTgFRGRGMUS3Mz7WWFwFjX1ij5PrsGLcfb0yp-ShzwYQUQUEx5PGyqjDzBgftXShiZl-77PHpfzMPDs366myRISel	2026-07-02 09:20:00.668943	2026-07-02 09:25:00.668943	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":true}	eyJraWQiOiJiMTFhYjg0ZC01ZWQwLTQ1ZTAtODNhNi05YzE2NmUwYWY4YjgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiYXVkIjoiZGVtby1jbGllbnQiLCJuYmYiOjE3ODI5NTUyMDAsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL3R1bmEubmV0L3BsYXRmb3JtU3ZyIiwiZXhwIjoxNzgzMzE1MjAwLCJpYXQiOjE3ODI5NTUyMDAsImp0aSI6IjcyMDJmZDk2LWQ2ODktNGEwNC1iNGZkLTJhZmFiMGVmZTc4MSJ9.vtCKH75A5DPRcD4Qw4dF6ehCt1S-wMe_fbFcfWK9ldTvh8uLfk4dj2kvregepQme_XRi_7_bySD-vQBJ1FIoJbLM-3FuqTf-wZrPArI3yusoi6hvfdzIPyQjXMKDu_W7ssxa2nzwpR0I7qXMxV16aqO8QEwOhvAq4069TlEkEmPiZAmNxODjlQckDWJ1IwLSCCqy14mNqWFiNYzRLmCeOPUpZ155SQNslDbjLA2zXL7tERnXEWkqQP3PfhxOAQec_qFxAMSYkcrlOm-R8muMM0UvaJjErMA40lTYp6DQE80pO4j23R05XMqQJ5NhIJia5ylas36U3BIbp5qsgzJWcg	2026-07-02 09:20:00.960401	2026-07-06 13:20:00.960401	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat":"self-contained","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"administrator","aud":["java.util.Collections$SingletonList",["demo-client"]],"nbf":["java.time.Instant",1782955200.960400800],"scope":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"iss":["java.net.URL","http://tuna.net/platformSvr"],"exp":["java.time.Instant",1783315200.960400800],"iat":["java.time.Instant",1782955200.960400800],"jti":"7202fd96-d689-4a04-b4fd-2afab0efe781"},"metadata.token.invalidated":false}	Bearer	openid,profile	eyJraWQiOiJiMTFhYjg0ZC01ZWQwLTQ1ZTAtODNhNi05YzE2NmUwYWY4YjgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMmE2ZDRiYy03ZjA0LTQzYTItOWZjOS0wY2I5OTc2NmE5OTMiLCJpc3MiOiJodHRwOi8vdHVuYS5uZXQvcGxhdGZvcm1TdnIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiZ2l2ZW5fbmFtZSI6Iue6p-euoeeQhuWRmCIsInNpZCI6Ing1QjRiR1RoUDVONHdhaWRDZGpicjBjSGhYUndNSXUtb0lqcGhOdlRPQWMiLCJhdWQiOiJkZW1vLWNsaWVudCIsImF6cCI6ImRlbW8tY2xpZW50IiwiYXV0aF90aW1lIjoxNzgyOTU1MjAwLCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwibmlja25hbWUiOiIiLCJwaG9uZV9udW1iZXIiOiIxNTMxNTA4NjI2NSIsImV4cCI6MTc4Mjk1NzAwMSwiaWF0IjoxNzgyOTU1MjAxLCJmYW1pbHlfbmFtZSI6Iui2hSIsImp0aSI6IjQ1ODg2ZjRkLTE0OGUtNDZiOS1hZmUzLWExZDY2ZGZjZjljMCIsImVtYWlsIjoieHVzaHV5YW5nQGxnYWcuY29tIn0.kUAY6bEIdr7s5TtdrQPjRBzOzzsjdxUi6yUCEIWTLITYp1KxHiu88LuM-Xv03v3J520kE3TZ1E8AnmE8DvyrC_4b4-uEu-DSlgjIpv3Yn5DQBOVv5x4EK60WhcyJdInuYddVYogG8NZ_YRNXZn9q6wnySmsMrKUyGzwUoBGVwDXjiK02wcnPI-ct6ZEwkmeg0yjtjhG9bQCXDN2OydhskhH6hjpTvw-SLSrTOV_TTDHWrQ0OmzPdHNErlq2F9a-YAyh6TAmtoO7Xecm2lmy9Yup17Vo_Lt6xGcuW_ed42DVbV0oZm3Vj_9WKm04bbi3o9SFbjiEigEzwiI0igPXUOQ	2026-07-02 09:20:01.089103	2026-07-02 09:50:01.089103	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","iss":["java.net.URL","http://tuna.net/platformSvr"],"preferred_username":"administrator","given_name":"级管理员","sid":"x5B4bGThP5N4waidCdjbr0cHhXRwMIu-oIjphNvTOAc","aud":["java.util.Collections$SingletonList",["demo-client"]],"azp":"demo-client","auth_time":["java.util.Date",1782955200735],"name":"超级管理员","nickname":"","phone_number":"15315086265","exp":["java.time.Instant",1782957001.089102800],"iat":["java.time.Instant",1782955201.089102800],"family_name":"超","jti":"45886f4d-148e-46b9-afe3-a1d66dfcf9c0","email":"xushuyang@lgag.com"},"metadata.token.invalidated":false}	QjQmL618N41vRkGeZY27mvM00sbSf8P6Fk2HipVHAdmTBQnrga8Q4-pxMy3ahlCwMNl8vff4pgVMII0opHLFc6gLAl_No2X1RbK_aBRpjJsEiY2Skwd7Ld7dYkg4Qgy_	2026-07-02 09:20:01.089103	2026-07-06 13:20:01.089103	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}	\N	\N	\N	\N	\N	\N	\N	\N
3d5d6f4d-f4bf-4e27-8704-2a989a204016	55d05d31-dec8-475b-8852-8c6e5d491134	administrator	authorization_code	openid,profile	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://tuna.net/platformSvr/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"demo-client","redirectUri":"http://tuna.net/platformSvr/sapi/redirect","scopes":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"state":"1429ee62ec7cde49a840514acdfdc309","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap","continue":""},"authorizationRequestUri":"http://tuna.net/platformSvr/oauth2/authorize?response_type=code&client_id=demo-client&scope=openid%20profile&state=1429ee62ec7cde49a840514acdfdc309&redirect_uri=http://tuna.net/platformSvr/sapi/redirect&continue=","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"192.168.56.102","sessionId":"FE9134E99761528788613F7C5C2384D5"},"authenticated":true,"principal":{"@class":"com.byteasy.platform.service.impl.PlatformUserDetails","id":4,"creatorId":"ca029a30-41ad-4c15-81d0-e8c7116f1ad8","createdBy":"管理员","createdTime":"2026-04-09 15:38:43","updaterId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","updatedBy":"超级管理员","updatedTime":"2026-04-29 16:51:43","version":5,"isDeleted":"NORMAL","deleterId":"","deletedBy":"","deletedTime":null,"avatar":"","userId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","username":"administrator","password":"{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6","salt":"","accountExpiredAt":null,"credentialsExpiredAt":null,"realName":"超级管理员","nickname":"","gender":"MAN","birthday":"2026-04-21","mobile":"15315086265","mobileVerified":null,"email":"xushuyang@lgag.com","emailVerified":null,"status":"NORMAL","userType":"ADMIN","loginFailCount":0,"lockTime":null,"lastLoginTime":null,"lastLoginIp":"","tenantId":"default","realmId":null,"orgId":null,"deptId":null,"extInfo":null,"remark":null},"credentials":null}}	\N	5AnrFQxV-NkI25qXtBc6WAwo_u7S5hs8yWpe7QJtBKHfcjHtsYhw4eLVsrZbMTd-MsWvDMNsW9jxd9Lc1RhbR4DJl1Wydvx0tRmXv_qjkbiJ5hQzeay7vtV-ikzm1bq3	2026-07-02 10:52:16.102178	2026-07-02 10:57:16.102178	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":true}	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiYXVkIjoiZGVtby1jbGllbnQiLCJuYmYiOjE3ODI5NjA3MzYsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL3R1bmEubmV0L3BsYXRmb3JtU3ZyIiwiZXhwIjoxNzgzMzIwNzM2LCJpYXQiOjE3ODI5NjA3MzYsImp0aSI6IjRiM2UzM2RkLTc2ZWYtNDhjNi1hMTg2LWJkN2M2YTgxNzZhMyJ9.7boeK2sRKgo3kzas4jX0EtnGdT55q0M9tV8Iy1wiKA3_zkkFfZeFyAlTQ5r1t6tkZntwcl15b9rXKwGgKCM7e6xVuN-CKtqA42k-345dVibUO6Uj_Kz7YylUjP_Fa7jCtddQzkciXwpg7qUINa2jCAJQodZaGr_DNIatytpwKvhuUhk3uaF1sluU6Qks1Gre0WO7PVjhF1bkMQRpzpRoo2O9kbffjRz7SE1Gmubl69XcaVEbNxpZuXyZ7vdcx96XJj4Q1yRdPMInxuO8LOG3koKTJC80PJpnI94I7Cn2CtMnudFNNO2VKxqd-1dvpAzahb15MsIou-3E5tHhMXcggw	2026-07-02 10:52:16.361188	2026-07-06 14:52:16.361188	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat":"self-contained","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"administrator","aud":["java.util.Collections$SingletonList",["demo-client"]],"nbf":["java.time.Instant",1782960736.361187600],"scope":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"iss":["java.net.URL","http://tuna.net/platformSvr"],"exp":["java.time.Instant",1783320736.361187600],"iat":["java.time.Instant",1782960736.361187600],"jti":"4b3e33dd-76ef-48c6-a186-bd7c6a8176a3"},"metadata.token.invalidated":false}	Bearer	openid,profile	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMmE2ZDRiYy03ZjA0LTQzYTItOWZjOS0wY2I5OTc2NmE5OTMiLCJpc3MiOiJodHRwOi8vdHVuYS5uZXQvcGxhdGZvcm1TdnIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiZ2l2ZW5fbmFtZSI6Iue6p-euoeeQhuWRmCIsInNpZCI6InVYcElSamJHNGdUcXpvS000dDFMa01NTEc0aXNBRUxuLVNPUENZWEswOHMiLCJhdWQiOiJkZW1vLWNsaWVudCIsImF6cCI6ImRlbW8tY2xpZW50IiwiYXV0aF90aW1lIjoxNzgyOTYwNzM2LCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwibmlja25hbWUiOiIiLCJwaG9uZV9udW1iZXIiOiIxNTMxNTA4NjI2NSIsImV4cCI6MTc4Mjk2MjUzNiwiaWF0IjoxNzgyOTYwNzM2LCJmYW1pbHlfbmFtZSI6Iui2hSIsImp0aSI6ImQwZjM5MDMwLTAxYTEtNDJkZi05OTA5LTBlNjZkOTlmMzJhNyIsImVtYWlsIjoieHVzaHV5YW5nQGxnYWcuY29tIn0.Jfyxf8nwlufcpyKPhIYPA7B_hK8tbZaVNE_fp2Zh89rC0gdHacAJN3W9GHi5ralydR8Zi1xhzcElK6vK-oVNqA90ZronTYDiDSEfWRs0FMP_VElGEUom77NgDap1_s2J8xO1Z_2xxK-7XUOYT2aP6nZ54rtvypDQpazm2NRVCz_Xjn1BrXN5YDYp9fvmwT4vey1kMkNn0ekzveu7v-A4ejzpI0BmR2-3M_DUG36_uiuJDc0_Z2zLCWsd9tHpn9NZX-h8SRDGHgRG3op0GK-nj_vS29u6ZNqGM3VHQCEx9ytZ2sUBm_5vrAGdTrSQoJmued0e2ogQdIeQcclcikREfw	2026-07-02 10:52:16.507913	2026-07-02 11:22:16.507913	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","iss":["java.net.URL","http://tuna.net/platformSvr"],"preferred_username":"administrator","given_name":"级管理员","sid":"uXpIRjbG4gTqzoKM4t1LkMMLG4isAELn-SOPCYXK08s","aud":["java.util.Collections$SingletonList",["demo-client"]],"azp":"demo-client","auth_time":["java.util.Date",1782960736165],"name":"超级管理员","nickname":"","phone_number":"15315086265","exp":["java.time.Instant",1782962536.507912700],"iat":["java.time.Instant",1782960736.507912700],"family_name":"超","jti":"d0f39030-01a1-42df-9909-0e66d99f32a7","email":"xushuyang@lgag.com"},"metadata.token.invalidated":false}	fwLybr6Be7dva9w1ZHFrQbSzO6fWXKLbCvN8-7UdpghsfDk6Opl7Z7-ynqoOLMLly3zSFjfKpay3d3c6aPfMItqP5i5IN9WPxgqiiJoIxcYgbGoB0IGTi_LjP93Lx3YU	2026-07-02 10:52:16.507913	2026-07-06 14:52:16.507913	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}	\N	\N	\N	\N	\N	\N	\N	\N
b44648f3-1b3d-42f3-a59e-64495b38a7a2	55d05d31-dec8-475b-8852-8c6e5d491134	administrator	authorization_code	openid,profile	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://tuna.net/platformSvr/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"demo-client","redirectUri":"http://tuna.net/platformSvr/sapi/redirect","scopes":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"state":"689a526d0b0a82aa52fa43b53b0f4caf","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap"},"authorizationRequestUri":"http://tuna.net/platformSvr/oauth2/authorize?response_type=code&client_id=demo-client&scope=openid%20profile&state=689a526d0b0a82aa52fa43b53b0f4caf&redirect_uri=http://tuna.net/platformSvr/sapi/redirect","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"192.168.56.102","sessionId":"FE9134E99761528788613F7C5C2384D5"},"authenticated":true,"principal":{"@class":"com.byteasy.platform.service.impl.PlatformUserDetails","id":4,"creatorId":"ca029a30-41ad-4c15-81d0-e8c7116f1ad8","createdBy":"管理员","createdTime":"2026-04-09 15:38:43","updaterId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","updatedBy":"超级管理员","updatedTime":"2026-04-29 16:51:43","version":5,"isDeleted":"NORMAL","deleterId":"","deletedBy":"","deletedTime":null,"avatar":"","userId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","username":"administrator","password":"{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6","salt":"","accountExpiredAt":null,"credentialsExpiredAt":null,"realName":"超级管理员","nickname":"","gender":"MAN","birthday":"2026-04-21","mobile":"15315086265","mobileVerified":null,"email":"xushuyang@lgag.com","emailVerified":null,"status":"NORMAL","userType":"ADMIN","loginFailCount":0,"lockTime":null,"lastLoginTime":null,"lastLoginIp":"","tenantId":"default","realmId":null,"orgId":null,"deptId":null,"extInfo":null,"remark":null},"credentials":null}}	\N	SOw32EX4p_8i2LqvMSR13NAr3zNv5Bi8TR8ItcwH7tcMgKRpxy59sJbCE5TrytSZWf8_crApSpJe535EKaYui1vedRCJDymlJexTrmJcIt0TDGxCLQFMi1zoBptKJzdF	2026-07-02 10:53:48.5319	2026-07-02 10:58:48.5319	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":true}	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiYXVkIjoiZGVtby1jbGllbnQiLCJuYmYiOjE3ODI5NjA4MjgsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL3R1bmEubmV0L3BsYXRmb3JtU3ZyIiwiZXhwIjoxNzgzMzIwODI4LCJpYXQiOjE3ODI5NjA4MjgsImp0aSI6ImE0YjhiNWFhLWRhYzQtNDAyMy1hMTk0LTMzNWY2NjI5ZGZkYiJ9.W4LHnz_q0_ADIR9DOuYu9mt3i2pQq2wE7dxxE9gqHulIJGhc0xI32jyMR09mp12nR_vqPxEF0uV8YIke3378PUdinkRRM10Yd13biFr1t8bD7ljYvc2dqaCNtxUz1zyWOfaefLkcRnFowEYxu5DRJeTwex53AmJVV52PygINaXVmkagQSKFxnAts4O5ng5Iw3D5IDy_uu3BTsJwa9LYnav9nBDkSdhFocgCfsj7IMHAQWoNxC3-wVUf3Su3jTZFNreNtEFoik5LtZLLiztBZ3eNBnL4ToGdTAvgTLvS2ablYVgl2aMaWombN2fd3LABIxYbTTR07DgyJhliQLY-cGQ	2026-07-02 10:53:48.669638	2026-07-06 14:53:48.669638	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat":"self-contained","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"administrator","aud":["java.util.Collections$SingletonList",["demo-client"]],"nbf":["java.time.Instant",1782960828.669637500],"scope":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"iss":["java.net.URL","http://tuna.net/platformSvr"],"exp":["java.time.Instant",1783320828.669637500],"iat":["java.time.Instant",1782960828.669637500],"jti":"a4b8b5aa-dac4-4023-a194-335f6629dfdb"},"metadata.token.invalidated":false}	Bearer	openid,profile	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMmE2ZDRiYy03ZjA0LTQzYTItOWZjOS0wY2I5OTc2NmE5OTMiLCJpc3MiOiJodHRwOi8vdHVuYS5uZXQvcGxhdGZvcm1TdnIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiZ2l2ZW5fbmFtZSI6Iue6p-euoeeQhuWRmCIsInNpZCI6InVYcElSamJHNGdUcXpvS000dDFMa01NTEc0aXNBRUxuLVNPUENZWEswOHMiLCJhdWQiOiJkZW1vLWNsaWVudCIsImF6cCI6ImRlbW8tY2xpZW50IiwiYXV0aF90aW1lIjoxNzgyOTYwNzM2LCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwibmlja25hbWUiOiIiLCJwaG9uZV9udW1iZXIiOiIxNTMxNTA4NjI2NSIsImV4cCI6MTc4Mjk2MjYyOCwiaWF0IjoxNzgyOTYwODI4LCJmYW1pbHlfbmFtZSI6Iui2hSIsImp0aSI6ImRlOTIwZTk4LTQ1MDYtNDcwZi1hYjRhLWRlM2M1Nzk2NTNhOCIsImVtYWlsIjoieHVzaHV5YW5nQGxnYWcuY29tIn0.27rA_eTILNfTPukR4Tzh809ickakFPGnUzgQJ9pyTuYIlAfJOrB37_Yi0d_EtTnAuilUWtK3jccfbAJhULqMo0Erses37xqAYngWAzD5Nc1AibjhWMbFF1ccUeSsBaGTPPupMnlStTqB0gqxhjbyxjKceDHy6pgtM8OkN6xCo5YlsJo3WUUdWdMtFKP_3Gap5dQ_2Z5d5grqKQ0vWC5e7hRBqWVmoDGvbj8YyJLwrmfGy377MCsdSfS2VK81FkYFOb27V5Ewq69LYD_oPh_pGZGUQd86a2wogWcmGdSck6QKWgfRx_CQwVoPXZj6ryXYfQ4ZgqIFc6Dizjr07RL98Q	2026-07-02 10:53:48.684377	2026-07-02 11:23:48.684377	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","iss":["java.net.URL","http://tuna.net/platformSvr"],"preferred_username":"administrator","given_name":"级管理员","sid":"uXpIRjbG4gTqzoKM4t1LkMMLG4isAELn-SOPCYXK08s","aud":["java.util.Collections$SingletonList",["demo-client"]],"azp":"demo-client","auth_time":["java.util.Date",1782960736165],"name":"超级管理员","nickname":"","phone_number":"15315086265","exp":["java.time.Instant",1782962628.684376600],"iat":["java.time.Instant",1782960828.684376600],"family_name":"超","jti":"de920e98-4506-470f-ab4a-de3c579653a8","email":"xushuyang@lgag.com"},"metadata.token.invalidated":false}	KRqSDJ9Ih2eNVyRRGdMR-fX1x7qGww-FxiSn2mADPqZMU_PqcPN_cz_HldY8gA3oPXAGJKLAlgmAJws8r8aaRIuIEngGvvnLtJFsjbFEpRfdH4Um-lKCd95gXL4LmsIN	2026-07-02 10:53:48.684377	2026-07-06 14:53:48.684377	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}	\N	\N	\N	\N	\N	\N	\N	\N
90bcb38b-bb23-4f59-84fe-46968a0a7ec2	55d05d31-dec8-475b-8852-8c6e5d491134	administrator	authorization_code	openid,profile	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://tuna.net/platformSvr/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"demo-client","redirectUri":"http://tuna.net/platformSvr/sapi/redirect","scopes":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"state":"78e90b06cc1bb598c82aca86de6a8c34","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap","continue":""},"authorizationRequestUri":"http://tuna.net/platformSvr/oauth2/authorize?response_type=code&client_id=demo-client&scope=openid%20profile&state=78e90b06cc1bb598c82aca86de6a8c34&redirect_uri=http://tuna.net/platformSvr/sapi/redirect&continue=","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"192.168.56.102","sessionId":"707327A484D694A1D7A63A894005F4C9"},"authenticated":true,"principal":{"@class":"com.byteasy.platform.service.impl.PlatformUserDetails","id":4,"creatorId":"ca029a30-41ad-4c15-81d0-e8c7116f1ad8","createdBy":"管理员","createdTime":"2026-04-09 15:38:43","updaterId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","updatedBy":"超级管理员","updatedTime":"2026-04-29 16:51:43","version":5,"isDeleted":"NORMAL","deleterId":"","deletedBy":"","deletedTime":null,"avatar":"","userId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","username":"administrator","password":"{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6","salt":"","accountExpiredAt":null,"credentialsExpiredAt":null,"realName":"超级管理员","nickname":"","gender":"MAN","birthday":"2026-04-21","mobile":"15315086265","mobileVerified":null,"email":"xushuyang@lgag.com","emailVerified":null,"status":"NORMAL","userType":"ADMIN","loginFailCount":0,"lockTime":null,"lastLoginTime":null,"lastLoginIp":"","tenantId":"default","realmId":null,"orgId":null,"deptId":null,"extInfo":null,"remark":null},"credentials":null}}	\N	XkgatH9aBQcKLFmN1Zbtgh0DoUJfvznUBJbNZDMgsej7Z2a5HNkocYRSLON9aNnb9jnvVGj_F3zJqiRO-8UOJOGFnUol5iJH_4STgHJKz7ptSnQ18cg_6AivkvmFmpSQ	2026-07-02 13:33:49.119527	2026-07-02 13:38:49.119527	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":true}	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiYXVkIjoiZGVtby1jbGllbnQiLCJuYmYiOjE3ODI5NzA0MjksInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL3R1bmEubmV0L3BsYXRmb3JtU3ZyIiwiZXhwIjoxNzgzMzMwNDI5LCJpYXQiOjE3ODI5NzA0MjksImp0aSI6IjAxMGQxYjY2LTgxY2EtNDQ3My04NGYwLTVlNTAyZDk5YjViNSJ9.J7TL-2M8I2wIDAzjhr3ojVKAsTaIDQa9FN7JYUgSCg1QuOZC-NUZv2YXiFRoNSoaTHGxV4gZqi3ih1oiwci0f0QaYOtYxfZmgDPK1NykcWzJGpCjhWen1qnLh9zL1qrzJSBQ2dNPihVwLiDcO7Kvdbrm55rAeDDETHg6fVIGrFl7edwuj4yXwRXDSB7HFUOr8FxADsIuX2WVj6NPDviulTh5v_rMYGZ2xOyS7w_C2wFlyeQZy9F7nD5al34yGxWzCtXvIEByEIx7XbwLslwUKVuet5qV9H3abuqHxKtF0pfdARhCCQaqY8-0WCcGnhnhQm4d9MrS18WQaoEtFjWtFQ	2026-07-02 13:33:49.246708	2026-07-06 17:33:49.246708	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat":"self-contained","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"administrator","aud":["java.util.Collections$SingletonList",["demo-client"]],"nbf":["java.time.Instant",1782970429.246707800],"scope":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"iss":["java.net.URL","http://tuna.net/platformSvr"],"exp":["java.time.Instant",1783330429.246707800],"iat":["java.time.Instant",1782970429.246707800],"jti":"010d1b66-81ca-4473-84f0-5e502d99b5b5"},"metadata.token.invalidated":false}	Bearer	openid,profile	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMmE2ZDRiYy03ZjA0LTQzYTItOWZjOS0wY2I5OTc2NmE5OTMiLCJpc3MiOiJodHRwOi8vdHVuYS5uZXQvcGxhdGZvcm1TdnIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiZ2l2ZW5fbmFtZSI6Iue6p-euoeeQhuWRmCIsInNpZCI6IlV6NlRGZjFjNXRkYmRBeko1MWVPVHJtQlNZM0hQSU1KQkNEWl9oTHpaRjAiLCJhdWQiOiJkZW1vLWNsaWVudCIsImF6cCI6ImRlbW8tY2xpZW50IiwiYXV0aF90aW1lIjoxNzgyOTcwNDI5LCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwibmlja25hbWUiOiIiLCJwaG9uZV9udW1iZXIiOiIxNTMxNTA4NjI2NSIsImV4cCI6MTc4Mjk3MjIyOSwiaWF0IjoxNzgyOTcwNDI5LCJmYW1pbHlfbmFtZSI6Iui2hSIsImp0aSI6Ijc0M2E1OWQ0LTVkYjYtNGQ1ZC04N2FhLWU1N2MyZDhjMWMxYyIsImVtYWlsIjoieHVzaHV5YW5nQGxnYWcuY29tIn0.izbHZF0863XA5N6q5Mz3RUd6OLs3eRGM6lxsIEu-RcHPRcHXCQ05xIA3qQJrp-06mG6e-HCTCwTtPWudcICleA9GuhVzme7IclSj-6Da7YSfQPGaxmav0Ltx-E7ZWiNfazlC7Rbdguj7CYLmCWlkkBn6kzFSjjBtHk9qtoQyd0PNCfUXp0SlYFa5ohtAZ3ANDQJ9I5ojCMftG9pL0RoYt2c_0KXvnEkkMjZgEQ6rDvxbaWF3YxUXO21bjvR1mZpALENUWE8yuPF7ImsxNp6-Bhd0VlLvYqRV3DL0206Zy5xKeJVczRvIk5il2wlR1BJjcfk2ywZFScV7aUAI1nGpfA	2026-07-02 13:33:49.268384	2026-07-02 14:03:49.268384	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","iss":["java.net.URL","http://tuna.net/platformSvr"],"preferred_username":"administrator","given_name":"级管理员","sid":"Uz6TFf1c5tdbdAzJ51eOTrmBSY3HPIMJBCDZ_hLzZF0","aud":["java.util.Collections$SingletonList",["demo-client"]],"azp":"demo-client","auth_time":["java.util.Date",1782970429126],"name":"超级管理员","nickname":"","phone_number":"15315086265","exp":["java.time.Instant",1782972229.268383800],"iat":["java.time.Instant",1782970429.268383800],"family_name":"超","jti":"743a59d4-5db6-4d5d-87aa-e57c2d8c1c1c","email":"xushuyang@lgag.com"},"metadata.token.invalidated":false}	hhG4LoJ6eZSr3dQ1-ErpmktkoI2IlBYDzv_GiJiNazxeRe51mTgCK9Z5SVe05Qzw56mbzcX2cTNQV_7NXJTwmkvHUyiCIebxXoxbeJhH8jnsQt9BNa58leOxrjBLD0pq	2026-07-02 13:33:49.267407	2026-07-06 17:33:49.267407	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}	\N	\N	\N	\N	\N	\N	\N	\N
b81f641e-1ff2-402e-a92f-48461e9319a7	55d05d31-dec8-475b-8852-8c6e5d491134	administrator	authorization_code	openid,profile	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://tuna.net/platformSvr/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"demo-client","redirectUri":"http://tuna.net/platformSvr/sapi/redirect","scopes":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"state":"d7359d4183b19874beb927a767b4f9ec","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap"},"authorizationRequestUri":"http://tuna.net/platformSvr/oauth2/authorize?response_type=code&client_id=demo-client&scope=openid%20profile&state=d7359d4183b19874beb927a767b4f9ec&redirect_uri=http://tuna.net/platformSvr/sapi/redirect","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"192.168.56.102","sessionId":"FE9134E99761528788613F7C5C2384D5"},"authenticated":true,"principal":{"@class":"com.byteasy.platform.service.impl.PlatformUserDetails","id":4,"creatorId":"ca029a30-41ad-4c15-81d0-e8c7116f1ad8","createdBy":"管理员","createdTime":"2026-04-09 15:38:43","updaterId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","updatedBy":"超级管理员","updatedTime":"2026-04-29 16:51:43","version":5,"isDeleted":"NORMAL","deleterId":"","deletedBy":"","deletedTime":null,"avatar":"","userId":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","username":"administrator","password":"{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6","salt":"","accountExpiredAt":null,"credentialsExpiredAt":null,"realName":"超级管理员","nickname":"","gender":"MAN","birthday":"2026-04-21","mobile":"15315086265","mobileVerified":null,"email":"xushuyang@lgag.com","emailVerified":null,"status":"NORMAL","userType":"ADMIN","loginFailCount":0,"lockTime":null,"lastLoginTime":null,"lastLoginIp":"","tenantId":"default","realmId":null,"orgId":null,"deptId":null,"extInfo":null,"remark":null},"credentials":null}}	\N	jR0ONYQqveZkx4vOznuMHefuerZxwDNrP7OKlvLrdjOA9bTpmNkjcTiek1GPDvjqiCjUNA1UZzBz0t6MxqMZpzOWT0EWyadt-kAiwyb_0aqvphR6LArZStrEok8emQFU	2026-07-02 10:54:35.982826	2026-07-02 10:59:35.982826	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":true}	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiYXVkIjoiZGVtby1jbGllbnQiLCJuYmYiOjE3ODI5NjA4NzYsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL3R1bmEubmV0L3BsYXRmb3JtU3ZyIiwiZXhwIjoxNzgzMzIwODc2LCJpYXQiOjE3ODI5NjA4NzYsImp0aSI6IjE0MDlmN2IwLWQxZTUtNDI1ZC05MDRhLWRhYmU1ZDgxMDdhYSJ9.0_lqVY5WxzuXyIGPVjQncbMbqH0P9Gxnp_4iZMWHFDgOiKjM6U5ieq3HE7T4gJiv9Z4-1f5YYkDNxUqIT3FfzcRfa5v8-LjaF0pgTYevzvGShIcEnhBQNBsWMiek-RQ4MeC3br7lV2FtsKt1M8UrYLvM9WnBiLXwsjncv56JmTQd1K3tCubMegSVOWoXnJ2wfz21uYJBwU1DCzrrCuLmy1jL3vKuO2bs2-OX2WfGMTSZn0Fqd6w-HEC1CVGNbt7GzknvKhEDQPnHK-R7oMItKd4rk7ER_UFOrc49OGhken24aQl5ak56Rwk5KG0apKnCKy0l69P0BvguUqSZBnRBEQ	2026-07-02 10:54:36.121736	2026-07-06 14:54:36.121736	{"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat":"self-contained","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"administrator","aud":["java.util.Collections$SingletonList",["demo-client"]],"nbf":["java.time.Instant",1782960876.121735500],"scope":["java.util.Collections$UnmodifiableSet",["openid","profile"]],"iss":["java.net.URL","http://tuna.net/platformSvr"],"exp":["java.time.Instant",1783320876.121735500],"iat":["java.time.Instant",1782960876.121735500],"jti":"1409f7b0-d1e5-425d-904a-dabe5d8107aa"},"metadata.token.invalidated":false}	Bearer	openid,profile	eyJraWQiOiIzNWViMjA3YS03ODVhLTQ3MjMtYTg2Ny01NmVjZDgxMWU5YWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMmE2ZDRiYy03ZjA0LTQzYTItOWZjOS0wY2I5OTc2NmE5OTMiLCJpc3MiOiJodHRwOi8vdHVuYS5uZXQvcGxhdGZvcm1TdnIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiZ2l2ZW5fbmFtZSI6Iue6p-euoeeQhuWRmCIsInNpZCI6InVYcElSamJHNGdUcXpvS000dDFMa01NTEc0aXNBRUxuLVNPUENZWEswOHMiLCJhdWQiOiJkZW1vLWNsaWVudCIsImF6cCI6ImRlbW8tY2xpZW50IiwiYXV0aF90aW1lIjoxNzgyOTYwNzM2LCJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwibmlja25hbWUiOiIiLCJwaG9uZV9udW1iZXIiOiIxNTMxNTA4NjI2NSIsImV4cCI6MTc4Mjk2MjY3NiwiaWF0IjoxNzgyOTYwODc2LCJmYW1pbHlfbmFtZSI6Iui2hSIsImp0aSI6IjIxM2ZlZmNmLWM2ZjktNDU1Ni1hNmQyLTI0NzBhMzZjMTMwNSIsImVtYWlsIjoieHVzaHV5YW5nQGxnYWcuY29tIn0.I9KBsxvwoUT9DdbV6kcId-59q2sedUf-LHvCorxTO7XcK265pV12pVX1JWVAzyMdGMNnxMyhSrmS5ZPXTjTWqjpOvEJiP-OESZmwIZT62uUUVN2GGH49z2ViU_0pYUdDeSBtICG5g9QjIlfZG--qLSJzKws9YW4JA8pEmyooBT1VT-tTwRaPGIVjQpe4ACzO0T9NA3MEDYiIMdSA0ZpB0dBcZr19EeNNcUdfWdAPARZKt1YLaq1RqPG3wx__TA5Z6ztxskvvjiS7EykQRSoq5W0t8Bw-8brn9ciwPJ_obfMkjpY32VJghFEVGaWMWoD5j76D677h4pcfoZVMOvXE7A	2026-07-02 10:54:36.138679	2026-07-02 11:24:36.138679	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"32a6d4bc-7f04-43a2-9fc9-0cb99766a993","iss":["java.net.URL","http://tuna.net/platformSvr"],"preferred_username":"administrator","given_name":"级管理员","sid":"uXpIRjbG4gTqzoKM4t1LkMMLG4isAELn-SOPCYXK08s","aud":["java.util.Collections$SingletonList",["demo-client"]],"azp":"demo-client","auth_time":["java.util.Date",1782960736165],"name":"超级管理员","nickname":"","phone_number":"15315086265","exp":["java.time.Instant",1782962676.138679100],"iat":["java.time.Instant",1782960876.138679100],"family_name":"超","jti":"213fefcf-c6f9-4556-a6d2-2470a36c1305","email":"xushuyang@lgag.com"},"metadata.token.invalidated":false}	rk5UGVcTtGhW7dDdPPHrhw7ay58PzpOlTdjBohs7D3EsxjnhC0Owfvz6U3hcvgMymDVvq6QNRBh4GkOT9vMqJOHo6KI-OQukhwfgiuHKzWfAz05yymprF7iXIPZUoyF5	2026-07-02 10:54:36.138679	2026-07-06 14:54:36.138679	{"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}	\N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Data for Name: oauth2_authorization_consent; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.oauth2_authorization_consent (registered_client_id, principal_name, authorities) FROM stdin;
\.


--
-- Data for Name: oauth2_registered_client; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, is_active, description) FROM stdin;
8fd92236-98f1-447d-bc84-39fdfe71be08	test	2026-05-08 17:30:43	{bcrypt}$2a$10$TAN1ent0GZ3.q66ZHgFU5Opwc3XwCNjLv4vdtGUK4ATNgnerclAJO	\N	test	{client_secret_post,client_secret_basic}	{refresh_token,authorization_code}	{http://tuna.net/platformSvr/sapi/1redirect}	{http://tuna.net/platformSvr/index.html}	{openid,profile}	{"@class": "com.byteasy.platform.oauth2.entity.OAuth2ClientSettings", "jwkSetUrl": "", "requireProofKey": true, "x509CertificateSubjectDN": "", "requireAuthorizationConsent": true, "tokenEndpointAuthenticationSigningAlgorithm": "HS256"}	{"@class": "com.byteasy.platform.oauth2.entity.OAuth2TokenSettings", "accessTokenFormat": "self-contained", "reuseRefreshTokens": true, "deviceCodeTimeToLive": 300, "accessTokenTimeToLive": 360000, "refreshTokenTimeToLive": 360000, "idTokenSignatureAlgorithm": "RS256", "authorizationCodeTimeToLive": 600, "x509CertificateBoundAccessTokens": false}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 17:30:39	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-07 08:48:35.16713	63	t	\N
55d05d31-dec8-475b-8852-8c6e5d491134	demo-client	2026-02-26 13:40:52	{bcrypt}$2a$10$lINSmYjy6VVmM2X.x84Np.ObUnbva2wKsCHDTO0AH7xi4GR4mXODO	\N	55d05d31-dec8-475b-8852-8c6e5d491134	{client_secret_post,client_secret_basic}	{refresh_token,authorization_code}	{http://tuna.net/platformSvr/sapi/redirect}	{http://tuna.net/platformSvr/index.html}	{openid,profile}	{"@class": "com.byteasy.platform.oauth2.entity.OAuth2ClientSettings", "requireProofKey": false, "requireAuthorizationConsent": false}	{"@class": "com.byteasy.platform.oauth2.entity.OAuth2TokenSettings", "accessTokenFormat": "self-contained", "reuseRefreshTokens": true, "deviceCodeTimeToLive": 300, "accessTokenTimeToLive": 360000, "refreshTokenTimeToLive": 360000, "idTokenSignatureAlgorithm": "RS256", "authorizationCodeTimeToLive": 300, "x509CertificateBoundAccessTokens": false}	\N	\N	2026-02-26 13:40:53	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 11:17:36.845614	3	t	\N
c8eaef7a-794e-4932-8779-a5559781fc03	rmiClient	2026-06-09 17:33:05.807602	{bcrypt}$2a$10$jF0HIQOnlM/GFqwxvzvIE.XuUMawXg0mbSebzEO0lELbdc3L.g0uy	\N	rmiClient	{client_secret_post,client_secret_basic}	{refresh_token,authorization_code}	{https://rmi.mylgag.com/platformSvr/sapi/redirect}	{https://rmi.mylgag.com/platformSvr/index.html}	{openid,profile}	{"@class": "com.byteasy.platform.oauth2.entity.OAuth2ClientSettings", "requireProofKey": false, "requireAuthorizationConsent": false, "tokenEndpointAuthenticationSigningAlgorithm": "HS256"}	{"@class": "com.byteasy.platform.oauth2.entity.OAuth2TokenSettings", "accessTokenFormat": "self-contained", "reuseRefreshTokens": true, "deviceCodeTimeToLive": 300, "accessTokenTimeToLive": 360000, "refreshTokenTimeToLive": 3600, "idTokenSignatureAlgorithm": "RS256", "authorizationCodeTimeToLive": 300, "x509CertificateBoundAccessTokens": false}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-09 17:33:04.102863	\N	\N	2026-06-09 17:33:05.807602	1	t	\N
\.


--
-- Data for Name: oauth2_registered_client_openapi; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.oauth2_registered_client_openapi (oauth2_registered_client_id, openapi_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_dictionary; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_dictionary (id, group_id, code, name, status, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
8	5	platform_settings	平台默认参数	1	\N	admin	admin	2026-04-09 07:08:18.651066	\N	\N	\N	1
\.


--
-- Data for Name: platform_dictionary_item; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_dictionary_item (id, dictionary_id, code, label, value, is_default, css, show_order, status, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
65	8	default_password	账户默认密码	Aa@112233	0	\N	1	1	\N	admin	admin	2026-04-09 07:12:05.004995	\N	\N	\N	1
\.


--
-- Data for Name: platform_entity_define; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_entity_define (id, code, service_code, name, url, remark, title_column_name, operation_column_width, query_params, form_component, form_width, dialog_model, pagination_visible, excel_enable, query_form_visible, excel_import_component, row_key, default_expand_all, tree_props, query_mode, show_lang, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, show_summary, query_bar_buttons, operation_buttons, table_name) FROM stdin;
32	com.byteasy.platform.oauth2.entity.Oauth2AuthorizationConsent	platform-service	{"zh_CN":"Oauth2AuthorizationConsent"}	\N	Oauth2AuthorizationConsent	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.754131	\N	\N	\N	1	0	\N	\N	\N
33	com.byteasy.platform.entity.Openapi	platform-service	{"zh_CN":"函数定义"}	/sapi/openapis	平台接口定义	name	160	descColumns=["updatedTime", "createdTime"]	\N	80%	0	1	1	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 14:10:54.577974	19	0	\N	\N	\N
34	com.byteasy.platform.entity.ResourceGroup	platform-service	{"zh_CN":"菜单分组"}	/sapi/resourceGroups	菜单分组	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 14:06:24.545333	2	0	\N	\N	\N
35	com.byteasy.platform.entity.ServiceInstance	platform-service	{"zh_CN":"服务实例"}	/sapi/common/com.byteasy.platform.entity.ServiceInstance	平台服务定义表	name	160	\N	\N	50%	0	1	1	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-04 10:41:47.405479	8	0	\N	\N	platform_service_instance
36	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClientOpenapi	platform-service	{"zh_CN":"Oauth2RegisteredClientOpenapi"}	\N	Oauth2RegisteredClientOpenapi	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.75706	\N	\N	\N	1	0	\N	\N	\N
48	com.byteasy.platform.entity.I18n	platform-service	{"zh_CN":"I18n"}	/sapi/i18ns	平台国际化翻译表	name	160	\N	/views/template/TunaFormilyForm.vue	100%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-26 08:31:36.408173	17	0	\N	\N	\N
37	com.byteasy.platform.entity.Dictionary	platform-service	{"zh_CN":"Dictionary"}	/sapi/dictionarys	字典定义表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.758037	\N	\N	\N	1	0	\N	\N	\N
38	com.byteasy.platform.entity.Role	platform-service	{"zh_CN":"Role"}	/sapi/roles	{"zh_CN":"系统角色","en":"System Role"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.759013	\N	\N	\N	1	0	\N	\N	\N
39	com.byteasy.platform.entity.EntityDefine	platform-service	{"zh_CN":"模型定义"}	/sapi/common/com.byteasy.platform.entity.EntityDefine	平台模型定义	name	160	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 10:41:52.267055	31	\N	\N	\N	platform_entity_define
40	com.byteasy.platform.entity.Resource	platform-service	{"zh_CN":"Resource"}	/sapi/resources	{"zh_CN":"系统菜单","en":"System Menu"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.760016	\N	\N	\N	1	0	\N	\N	\N
41	com.byteasy.platform.entity.LoginLog	platform-service	{"zh_CN":"LoginLog"}	/sapi/loginLogs	{"zh_CN":"登录日志","en":"Login log"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.760016	\N	\N	\N	1	0	\N	\N	\N
42	com.byteasy.platform.entity.UserRole	platform-service	{"zh_CN":"UserRole"}	\N	平台角色授权表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.760016	\N	\N	\N	1	0	\N	\N	\N
43	com.byteasy.platform.entity.User	platform-service	{"zh_CN":"User"}	/sapi/users		name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 15:46:57.869951	3	0	\N	\N	\N
44	com.byteasy.platform.entity.FieldDefine	platform-service	{"zh_CN":"字段定义"}	/sapi/fieldDefines	列配置表（存储业务实体字段的展示/校验/表单等配置）	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.761008	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 11:42:47.086029	2	0	\N	\N	\N
45	com.byteasy.platform.oauth2.entity.Oauth2Authorization	platform-service	{"zh_CN":"Oauth2Authorization"}	/sapi/oauth2Authorizations	Oauth2Authorization	name	0	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:15:27.002922	3	0	\N	\N	\N
46	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient	platform-service	{"zh_CN":"Oauth2RegisteredClient"}	/sapi/oauth2RegisteredClients	{"zh_CN":"应用认证","en":"OAuth2 application"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:52:06.928357	2	0	\N	\N	\N
47	com.byteasy.platform.entity.ResourceOpenapi	platform-service	{"zh_CN":"ResourceOpenapi"}	/sapi/resourceOpenapis	OAuth2客户端与平台资源的授权关联表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 09:59:41.618411	5	0	\N	\N	platform_resource_openapi
49	com.byteasy.platform.entity.RoleResource	platform-service	{"zh_CN":"RoleResource"}	\N	平台资源角色授权表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:49:35.707392	2	0	\N	\N	\N
50	com.byteasy.platform.entity.ResourceCategory	platform-service	{"zh_CN":"ResourceCategory"}	/sapi/resourceCategorys	{"zh_CN":"菜单分类","en":"Menu Category"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:49:12.09688	2	0	\N	\N	\N
51	com.byteasy.platform.entity.DictionaryItem	platform-service	{"zh_CN":"DictionaryItem"}	/sapi/dictionaryItems	字典项目定义表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:49:04.219655	2	0	\N	\N	\N
52	com.byteasy.platform.vo.PersonalSettingsVO	platform-service	{"zh_CN":"用户信息"}	/sapi/users	\N	realName	160	\N	\N	50%	0	1	1	1	\N	\N	0	\N	0	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:39:26	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:48:52.896868	2	0	\N	\N	\N
54	com.byteasy.knowledgeBase.entity.KnowledgeCategory	knowledge-base-service	{"zh_CN":"KnowledgeCategory"}	/sapi/knowledgeCategorys	知识分类目录	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:25:55.23515	3	0	\N		knowledge_category
55	com.byteasy.knowledgeBase.entity.KnowledgeFile	knowledge-base-service	{"zh_CN":"KnowledgeFile"}	/sapi/knowledgeFiles	知识文件	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-01 15:12:02.079694	2	0	\N	\N	knowledge_file
56	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	knowledge-base-service	{"zh_CN":"KnowledgeFileHistory"}	/sapi/knowledgeFileHistorys	文件变更历史	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:25:41.537804	3	0	\N		knowledge_file_history
63	com.byteasy.productAssistant.entity.NameplateRecord	product-assistant-service	{"zh_CN":"NameplateRecord"}	/sapi/nameplateRecords	生产铭牌打印记录表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.216618	\N	\N	\N	1	0	\N	\N	nameplate_record
64	com.byteasy.productAssistant.entity.ModelCodeSerialNo	product-assistant-service	{"zh_CN":"ModelCodeSerialNo"}	/sapi/modelCodeSerialNos	车型流水号生成记录表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	model_code_serial_no
65	com.byteasy.productAssistant.entity.PrintRecord	product-assistant-service	{"zh_CN":"PrintRecord"}	/sapi/printRecords	打印模板生成记录	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	print_record
66	com.byteasy.productAssistant.entity.VinGenerateRequest	product-assistant-service	{"zh_CN":"VinGenerateRequest"}	/sapi/vinGenerateRequests	车辆编号生成申请表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	vin_generate_request
67	com.byteasy.productAssistant.entity.ModelEnvPubNo	product-assistant-service	{"zh_CN":"ModelEnvPubNo"}	/sapi/modelEnvPubNos	车型环保公开号对照表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	model_env_pub_no
68	com.byteasy.productAssistant.entity.QrSyncJob	product-assistant-service	{"zh_CN":"QrSyncJob"}	/sapi/qrSyncJobs	大田农机二维码同步任务	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.220633	\N	\N	\N	1	0	\N	\N	qr_sync_job
\.


--
-- Data for Name: platform_group_i18n; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_group_i18n (id, group_id, code, name, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	5	123	{"zh_CN":"1123"}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:59:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 10:12:03.584242	2
8	8	3	{"zh_CN":"1"}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 10:21:40.869545	\N	\N	\N	1
9	1	ss	{"zh_CN":"ss"}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 13:48:03.050195	\N	\N	\N	1
\.


--
-- Data for Name: platform_job; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_job (id, code, org_id, status, name, remark, show_order, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
\.


--
-- Data for Name: platform_job_resource; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_job_resource (job_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_job_user; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_job_user (user_id, job_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_login_log; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_login_log (id, type, username, real_name, ip_address, session_id, remark, creator_id, created_by, created_time) FROM stdin;
169	0	administrator	超级管理员	192.168.56.1	5E024E95D3FA5A30666A0B6436BDC381	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	administrator	2026-07-01 16:55:24.781756
170	0	administrator	超级管理员	192.168.56.1	36CDF1CD8F48AF6603CCD3719A53EF79	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	administrator	2026-07-02 09:20:00.536587
171	0	administrator	超级管理员	192.168.56.1	FE9134E99761528788613F7C5C2384D5	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	administrator	2026-07-02 10:52:16.051843
172	0	administrator	超级管理员	192.168.56.1	707327A484D694A1D7A63A894005F4C9	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	administrator	2026-07-02 13:33:49.102647
\.


--
-- Data for Name: platform_openapi; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_openapi (id, service_id, name, http_method, class_method, uri, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
2	3	1	1	1	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:29:48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-26 08:46:01.221833	2
\.


--
-- Data for Name: platform_org; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_org (id, code, parent_id, status, name, remark, show_order, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
\.


--
-- Data for Name: platform_org_resource; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_org_resource (org_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_realm; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_realm (id, code, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	lgag	临工农机	\N	1	admin	2026-03-02 09:14:48.618331	\N	\N	\N	1
\.


--
-- Data for Name: platform_resource; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_resource (id, code, parent_id, group_id, name, icon, visible, type, open_tab, affix, keep_alive, show_order, layout, component, router_url, redirect_url, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
4	DevelopConfig	\N	5	{"zh_CN":"开发配置","en":"Developer config"}	<svg t="1775096517850" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7693" width="200" height="200"><path d="M885.9 63.9H139.6C98.4 63.9 65 97.3 65 138.5v559.8c0 41.2 33.4 74.6 74.6 74.6h261.2v112h-74.6c-20.6 0-37.3 16.7-37.3 37.3 0 20.6 16.7 37.3 37.3 37.3h373.2c20.6 0 37.3-16.7 37.3-37.3 0-20.6-16.7-37.3-37.3-37.3h-74.6V773H886c41.2 0 74.6-33.4 74.6-74.6V138.6c0-41.2-33.5-74.7-74.7-74.7z m-335.8 821h-74.6V773h74.6v111.9z m335.8-186.6H139.6V138.6h746.3v559.7zM689.1 350.6l86.3 72.1-87.7 84.9c-10.7 10.4-11 27.4-0.6 38.1 5.3 5.4 12.3 8.2 19.4 8.2 6.8 0 13.5-2.5 18.8-7.6l109.2-105.7c5.5-5.3 8.5-12.7 8.2-20.3-0.3-7.6-3.8-14.8-9.6-19.6l-109.2-91.3c-11.4-9.5-28.4-8-38 3.3-9.7 11.4-8.2 28.4 3.2 37.9zM300.3 546.3c5.2 5.1 12 7.6 18.8 7.6 7 0 14.1-2.7 19.4-8.2 10.4-10.7 10.1-27.7-0.6-38.1l-87.8-84.9 86.3-72.1c11.4-9.5 12.9-26.5 3.4-37.9-9.6-11.4-26.6-12.9-38-3.3l-109.2 91.3c-5.9 4.9-9.4 12-9.6 19.6-0.3 7.6 2.7 15 8.2 20.3l109.1 105.7z m143.7 4.1c4 2.3 8.6 3.5 13.2 3.5 9.4 0 18.6-4.9 23.6-13.7l111-197c7.3-13 2.7-29.4-10.3-36.6-13-7.3-29.4-2.7-36.7 10.3l-111 197c-7.4 12.8-2.7 29.2 10.2 36.5z" p-id="7694"></path></svg>	1	0	0	0	0	10	AsideMain	\N	/PlatfomDevOps/SystemManage/DevelopConfig	/PlatfomDevOps/SystemManage/DevelopConfig/resourceGroups	\N	test	管理员	2026-03-19 09:49:21	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:26:38.353981	12
5	ResourceGroup	4	5	{"zh_CN":"领域组","en":"Domain group"}	\N	1	1	0	0	0	1	\N	/views/microapp/developConfig/resource/ResourceGroupList.vue	ResourceGroup	\N	\N	test	管理员	2026-03-19 09:52:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 15:00:10.378816	8
7	PlatformResource	4	5	{"zh_CN":"平台菜单","en":"System menu"}	\N	1	1	0	0	0	2	\N	/views/microapp/developConfig/resource/ResourceList.vue	PlatformResource	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-20 15:32:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:33:36.652192	8
8	SchemaDefine	4	5	{"zh_CN":"字段定义","en":"Column Define"}	\N	1	1	0	0	0	5	\N	/views/microapp/developConfig/fieldDefine/SchemaDefineList.vue	SchemaDefine	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-20 15:40:24	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:59:39.589958	10
9	SchemaDefineList	4	5	{"zh_CN":"国际化配置","en":"Multi language config"}	\N	1	1	0	0	0	10	\N	/views/template/TunaObjectList.vue	I18nDefine?className=com.byteasy.platform.entity.I18n&queryParams=%7B%22descColumns%22%3A%5B%22updatedTime%22%2C+%22createdTime%22%5D%7D	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-20 15:43:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 16:55:45.669687	19
14	AccountManage	\N	5	{"zh_CN":"账号管理","en":"Account manage"}	<svg t="1775096100110" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6429" width="200" height="200"><path d="M727.4 506a208 208 0 1 0-238.8 0A288.4 288.4 0 0 0 320 768v64a64.1 64.1 0 0 0 64 64h448a64.1 64.1 0 0 0 64-64v-64a288.4 288.4 0 0 0-168.6-262z m-263.4-170A144 144 0 1 1 608 480a144.2 144.2 0 0 1-144-144z m368 496H384v-64a224 224 0 0 1 448 0zM256 832a64.1 64.1 0 0 1-64-64V320a64.1 64.1 0 0 1 64-64h96a32 32 0 0 0 0-64h-96A128.1 128.1 0 0 0 128 320v448a128.1 128.1 0 0 0 128 128 32 32 0 1 0 0-64z" fill="currentColor" p-id="6430"></path><path d="M256 448h32a32 32 0 0 0 0-64h-32a32 32 0 0 0 0 64zM256 576H320a32 32 0 1 0 0-64h-64a32 32 0 1 0 0 64z" fill="currentColor" p-id="6431"></path></svg>	1	0	0	0	0	1	\N	\N	/PlatfomDevOps/SystemManage/AccountManage	\N	\N	test	管理员	2026-04-02 08:30:10	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:24:43.672762	9
15	RoleList	14	5	{"zh_CN":"系统角色","en":"System role"}	\N	1	1	0	0	0	2	\N	/views/microapp/systemManage/role/RoleList.vue	RoleList	\N	\N	test	管理员	2026-04-02 08:31:48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-18 14:09:10.083289	7
16	UserList	14	5	{"zh_CN":"用户账号","en":"User account"}	\N	1	1	0	0	0	1	\N	/views/microapp/systemManage/user/UserList.vue	UserList	\N	\N	test	管理员	2026-04-02 15:33:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-18 14:20:36.114245	13
76	com.byteasy.platform.vo.PersonalSettingsVO.Enable	16	5	{"zh_CN":"禁用/启用","en":"Enable/Disable"}	\N	1	2	0	0	0	7	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 16:54:04.10273	\N	\N	\N	1
27	KnowledgeBase	\N	8	{"zh_CN":"知识库","en":"Knowledge Base","fr":"Base de connaissances","de":"Wissensdatenbank","it":"Base di conoscenza","ja":"知識ベース","ko":"지식 기반","ru":"База знаний","pt_br":"Base de conhecimento","pt":"Base de conhecimento","es":"Base de conocimiento"}	Memo	1	0	0	0	0	1	\N	\N	/Sales/Knowledge/Base	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 09:41:25	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:29:34.31232	6
28	KnowledgeFiles	27	8	{"zh_CN":"知识文档","en":"Knowledge Document","fr":"Document de connaissances","de":"Wissensdokument","it":"Documento di conoscenza","ja":"知識文書","ko":"지식 문서","ru":"Документ знаний","pt_br":"Documento de conhecimento","pt":"Documento de conhecimento","es":"Documento de conocimiento"}	\N	1	1	0	0	0	100	\N	/views/microapp/knowledgeBase/knowledgeFile/KnoledgeFileList.vue	KnowledgeFiles	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 09:52:26	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:29:54.19544	3
47	com.byteasy.platform.vo.PersonalSettingsVO.Delete	16	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:53:34.10365	\N	\N	\N	1
48	com.byteasy.platform.vo.PersonalSettingsVO.Import	16	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:53:34.106711	\N	\N	\N	1
52	com.byteasy.platform.entity.Role.Delete	15	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.475703	\N	\N	\N	1
53	com.byteasy.platform.entity.Role.Import	15	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.475703	\N	\N	\N	1
54	com.byteasy.platform.entity.Role.Export	15	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:44:54.489812	2
55	com.byteasy.platform.entity.Resource.Add	7	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.70819	\N	\N	\N	1
56	com.byteasy.platform.entity.Resource.Edit	7	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.709176	\N	\N	\N	1
57	com.byteasy.platform.entity.Resource.Delete	7	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.710161	\N	\N	\N	1
58	com.byteasy.platform.entity.Resource.Import	7	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.710161	\N	\N	\N	1
59	com.byteasy.platform.entity.Resource.Export	7	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:06.853355	2
60	com.byteasy.platform.entity.FieldDefine.Add	8	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.828206	\N	\N	\N	1
61	com.byteasy.platform.entity.FieldDefine.Edit	8	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.829193	\N	\N	\N	1
62	com.byteasy.platform.entity.FieldDefine.Delete	8	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.829193	\N	\N	\N	1
63	com.byteasy.platform.entity.FieldDefine.Import	8	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.830701	\N	\N	\N	1
64	com.byteasy.platform.entity.FieldDefine.Export	8	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:11.952761	2
65	com.byteasy.platform.entity.I18n.Add	9	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.479291	\N	\N	\N	1
66	com.byteasy.platform.entity.I18n.Edit	9	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.480263	\N	\N	\N	1
67	com.byteasy.platform.entity.I18n.Delete	9	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.481246	\N	\N	\N	1
68	com.byteasy.platform.entity.I18n.Import	9	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.482217	\N	\N	\N	1
69	com.byteasy.platform.entity.I18n.Export	9	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:16.68134	2
70	com.byteasy.platform.entity.ResourceGroup.Add	5	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.527858	\N	\N	\N	1
71	com.byteasy.platform.entity.ResourceGroup.Edit	5	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.527858	\N	\N	\N	1
72	com.byteasy.platform.entity.ResourceGroup.Delete	5	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.528832	\N	\N	\N	1
73	com.byteasy.platform.entity.ResourceGroup.Import	5	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.528832	\N	\N	\N	1
74	com.byteasy.platform.entity.ResourceGroup.Export	5	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:01.426741	2
75	com.byteasy.platform.vo.PersonalSettingsVO.RoleAssign	16	5	{"zh_CN":"角色分配","en":"Role assign"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 16:51:55.379145	\N	\N	\N	1
77	com.byteasy.platform.entity.Role.Permission	15	5	{"zh_CN":"权限分配","en":"Permission assign"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 17:01:27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 17:02:12.50014	2
78	com.byteasy.knowledgeBase.entity.KnowledgeFile.Add	28	8	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:45:17.572515	\N	\N	\N	1
79	com.byteasy.knowledgeBase.entity.KnowledgeFile.Edit	28	8	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:45:17.583486	\N	\N	\N	1
80	com.byteasy.knowledgeBase.entity.KnowledgeFile.Delete	28	8	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:45:17.58513	\N	\N	\N	1
83	com.byteasy.knowledgeBase.entity.KnowledgeFile.Viewer	28	8	{"zh_CN":"查看"}	\N	1	2	0	0	0	0	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:33.982044	\N	\N	\N	1
87	LoginLogList	14	5	{"zh_CN":"登录日志","en":"Login log"}	\N	1	1	0	0	0	3	\N	/views/microapp/systemManage/loginLog/LoginLogList.vue	LoginLogList	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:53:37	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 15:51:40.892335	4
93	com.byteasy.platform.vo.PersonalSettingsVO.Add	16	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:53.075245	2
94	com.byteasy.platform.vo.PersonalSettingsVO.Edit	16	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:59.104507	2
98	com.byteasy.platform.entity.LoginLog.View	87	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 15:54:31.922887	\N	\N	\N	1
104	PlatformConfig	\N	5	{"zh_CN":"平台配置","en":"Platform Config"}	<svg t="1778057163023" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2813" width="200" height="200"><path d="M869.934545 62.138182H153.832727a104.727273 104.727273 0 0 0-104.727272 104.727273v512a104.96 104.96 0 0 0 104.96 104.96H477.090909v108.218181H234.123636a34.909091 34.909091 0 0 0 0 69.818182H791.272727a34.909091 34.909091 0 0 0 0-69.818182H546.909091v-107.985454h323.025454a104.96 104.96 0 0 0 104.96-104.96v-512a104.96 104.96 0 0 0-104.96-104.96z m35.141819 616.96a35.141818 35.141818 0 0 1-35.141819 35.141818H154.065455a35.141818 35.141818 0 0 1-35.141819-35.141818v-512a34.909091 34.909091 0 0 1 34.909091-34.909091h716.101818a34.909091 34.909091 0 0 1 35.141819 34.909091z" p-id="2814"></path><path d="M546.909091 434.734545v-166.632727a34.909091 34.909091 0 1 0-69.818182 0v166.4a62.836364 62.836364 0 0 0 0 104.727273v48.64a34.909091 34.909091 0 1 0 69.818182 0v-49.105455a62.138182 62.138182 0 0 0 27.694545-51.898181 62.836364 62.836364 0 0 0-27.694545-52.13091zM327.912727 316.741818v-48.64a34.909091 34.909091 0 0 0-69.818182 0v49.105455a62.603636 62.603636 0 0 0 0 104.029091v166.632727a34.909091 34.909091 0 0 0 69.818182 0v-166.167273a63.069091 63.069091 0 0 0 0-104.96zM772.189091 316.741818v-48.64a34.909091 34.909091 0 0 0-69.818182 0v49.105455a62.603636 62.603636 0 0 0 0 104.029091v166.632727a34.909091 34.909091 0 0 0 69.818182 0v-166.167273a63.069091 63.069091 0 0 0 0-104.96z" p-id="2815"></path></svg>	1	0	0	0	0	2	\N	\N	/PlatfomDevOps/SystemManage/PlatformConfig	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:47:18	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:47:47.447658	2
105	DictionaryList	104	5	{"zh_CN":"数据字典","en":"Data dictionary"}	\N	1	1	0	0	0	1	\N	/views/microapp/platformConfig/dictionary/DictionaryList.vue	DictionaryList	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:56:02.543317	\N	\N	\N	1
106	com.byteasy.platform.entity.Dictionary.View	105	5	{"zh_CN":"查看字典","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:03.203199	2
107	com.byteasy.platform.entity.Dictionary.Add	105	5	{"zh_CN":"添加字典","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:22.083635	2
108	com.byteasy.platform.entity.Dictionary.Edit	105	5	{"zh_CN":"编辑字典","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:39.522035	2
109	com.byteasy.platform.entity.Dictionary.Delete	105	5	{"zh_CN":"删除字典","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:53.424521	2
112	com.byteasy.platform.entity.DictionaryItem.View	105	5	{"zh_CN":"查看字典项","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:13.15931	2
113	com.byteasy.platform.entity.DictionaryItem.Add	105	5	{"zh_CN":"添加字典项","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:30.267839	2
114	com.byteasy.platform.entity.DictionaryItem.Edit	105	5	{"zh_CN":"编辑字典项","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:47.712537	2
115	com.byteasy.platform.entity.DictionaryItem.Delete	105	5	{"zh_CN":"删除字典项","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:32:00.771017	2
118	Oauth2RegisteredClient	104	5	{"zh_CN":"应用认证","en":"OAuth2 Application"}	\N	1	1	0	0	0	2	\N	/views/microapp/platformConfig/oauth2Client/Oauth2ClientList.vue	Oauth2RegisteredClient	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:55:19.491091	\N	\N	\N	1
119	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient.View	118	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.437819	\N	\N	\N	1
120	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient.Add	118	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.445245	\N	\N	\N	1
121	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient.Edit	118	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.448174	\N	\N	\N	1
122	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient.Delete	118	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.45262	\N	\N	\N	1
123	com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient.Import	118	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.455541	\N	\N	\N	1
125	ServiceInstance	4	5	{"zh_CN":"服务实例","en":"Micro Service"}	\N	1	1	0	0	0	3	\N	/views/template/TunaObjectList.vue	ServiceInstance?className=com.byteasy.platform.entity.ServiceInstance&queryParams=%7B%22descColumns%22%3A%5B%22updatedTime%22%2C%22createdTime%22%5D%7D	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 14:15:05	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:59:26.398179	21
126	com.byteasy.platform.entity.ServiceInstance.View	125	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.513856	\N	\N	\N	1
128	com.byteasy.platform.entity.ServiceInstance.Edit	125	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.537504	\N	\N	\N	1
151	com.byteasy.platform.entity.EntityDefine.Delete	147	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.252901	\N	\N	\N	1
49	com.byteasy.platform.vo.PersonalSettingsVO.Export	16	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:53:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 15:46:54.578711	2
50	com.byteasy.platform.entity.Role.Add	15	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.474716	\N	\N	\N	1
51	com.byteasy.platform.entity.Role.Edit	15	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.475703	\N	\N	\N	1
154	com.byteasy.platform.entity.Openapi.Import	140	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-22 11:42:05.822101	\N	\N	\N	1
155	com.byteasy.platform.entity.Openapi.Export	140	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-22 11:42:05.841234	\N	\N	\N	1
156	com.byteasy.platform.entity.ServiceInstance.Import	125	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:05:59.871905	\N	\N	\N	1
157	com.byteasy.platform.entity.ServiceInstance.Export	125	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:05:59.890643	\N	\N	\N	1
127	com.byteasy.platform.entity.ServiceInstance.Add	125	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.52868	\N	\N	\N	1
129	com.byteasy.platform.entity.ServiceInstance.Delete	125	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.544905	\N	\N	\N	1
132	Oauth2Authorization	14	5	{"zh_CN":"Token日志","en":"Token Log"}	\N	1	1	0	0	0	4	\N	/views/template/TunaObjectList.vue	Oauth2Authorization?className=com.byteasy.platform.oauth2.entity.Oauth2Authorization&titleColumnName=principalName&queryParams=%7B%22descColumns%22%3A%5B%22authorizationCodeIssuedAt%22%2C%22accessTokenIssuedAt%22%2C+%22refreshTokenIssuedAt%22%5D%7D	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 10:10:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:31:19.54183	10
133	com.byteasy.platform.oauth2.entity.Oauth2Authorization.View	132	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 10:11:15.2256	\N	\N	\N	1
140	Openapi	4	5	{"zh_CN":"函数定义","en":"Openapi"}	\N	1	1	0	0	0	6	\N	/views/template/TunaObjectList.vue	Openapi?className=com.byteasy.platform.entity.Openapi	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 13:37:00.153573	17
141	com.byteasy.platform.entity.Openapi.View	140	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.21909	\N	\N	\N	1
142	com.byteasy.platform.entity.Openapi.Add	140	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.232374	\N	\N	\N	1
143	com.byteasy.platform.entity.Openapi.Edit	140	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.244717	\N	\N	\N	1
144	com.byteasy.platform.entity.Openapi.Delete	140	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.25211	\N	\N	\N	1
147	EntityDefine	4	5	{"zh_CN":"模型定义"}	\N	1	1	0	0	0	4	\N	/views/template/TunaObjectList.vue	EntityDefine?baseUrl=%2FplatformSvr%2Fsapi%2FentityDefines&context=platformSvr&className=com.byteasy.platform.entity.EntityDefine&queryParams=%7B%22descColumns%22%3A%5B%22updatedTime%22%2C+%22createdTime%22%5D%7D	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:22:54	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:59:32.603052	4
148	com.byteasy.platform.entity.EntityDefine.View	147	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.224266	\N	\N	\N	1
149	com.byteasy.platform.entity.EntityDefine.Add	147	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.234131	\N	\N	\N	1
150	com.byteasy.platform.entity.EntityDefine.Edit	147	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.244942	\N	\N	\N	1
\.


--
-- Data for Name: platform_resource_category; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_resource_category (id, code, name, show_order, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, parent_id) FROM stdin;
65531	Sale	{"en":"Marketing","zh_CN":"营销","fr":"Marketing","de":"Marketing","it":"Marketing","ja":"マーケティング","ko":"마케팅","ru":"Маркетинг","pt_br":"Marketing","pt":"Marketing","es":"Marketing"}	51	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-16 10:22:53	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:42:14.376792	3	\N
65532	platformDevOps	{"en":"Platform operation","zh_CN":"平台运营"}	1000	\N	test	管理员	2026-04-01 14:01:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:42:01.564516	3	\N
65534	MarketingBase	{"zh_CN":"常用","en":"MarketingBase","fr":"Courant","de":"Gebräuchlich","it":"Comune","ja":"よく使う","ko":"자주 사용함","ru":"Обычный","pt_br":"Comum","pt":"Comum","es":"Común"}	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 14:42:57	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:41:10.010715	3	65531
65535	SystemManage	{"zh_CN":"平台运维","en":"Platform operations"}	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 14:57:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:52:13.517802	4	65532
\.


--
-- Data for Name: platform_resource_group; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_resource_group (id, category_id, code, name, show_order, router, navbar_menu, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, entry, props, is_microapp) FROM stdin;
8	65534	KnowledgeBase	{"zh_CN":"知识库","en":"Knowledge Base","fr":"Base de connaissances","de":"Wissensdatenbank","it":"Base di conoscenze","ja":"知識ベース","ko":"지식 베이스","ru":"База знаний","pt_br":"Base de conhecimento","pt":"Base de conhecimento","es":"Base de conocimientos"}	3	\N	1	//{host}/knowledge-base/index.html?v=1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-16 17:13:36	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-10 16:24:44.84789	18	//{host}/knowledge-base/index.html?v=1	\N	1
5	65535	SystemManage	{"zh_CN":"平台管理","en":"Platform Management","fr":"Gestion du système","de":"Systemverwaltung","it":"Gestione del sistema","ja":"システム管理","ko":"시스템 관리","ru":"Управление системой","pt_br":"Gerenciamento do sistema","pt":"Gestão do sistema","es":"Gestión del sistema"}	100	/PlatfomDevOps/SystemManage/AccountManage	1	//{host}/platformSvr/common/index.html	test	管理员	2026-04-01 14:49:19	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 09:22:25.728847	58	http://localhost:7998/platformSvr/common	\N	1
\.


--
-- Data for Name: platform_resource_openapi; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_resource_openapi (resource_id, openapi_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_role; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_role (id, code, status, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	admin	1	{"zh_CN":"超级管理员","en":"Admin"}	\N	test	管理员	2026-04-02 10:45:11	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-28 19:08:25.93904	6
5	knowledgeBaseAdmin	1	{"zh_CN":"知识库管理员","en":"Knowledge manager"}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 09:52:57	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:25:57.73105	3
6	knowledgeViewer	1	{"zh_CN":"知识库使用者","en":"Knowledge Base Viewer"}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:41:29.013763	\N	\N	\N	1
\.


--
-- Data for Name: platform_role_resource; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_role_resource (role_id, resource_id, creator_id, created_by, created_time) FROM stdin;
1	4	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	5	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	7	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	8	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	9	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	14	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	15	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.001554
1	16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.001554
1	47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	49	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	50	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	51	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	52	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	53	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.005458
1	54	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.005458
1	55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.005458
1	56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	57	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	58	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	60	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	61	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	62	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	63	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.007945
1	64	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.008935
1	65	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.008935
1	66	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.008935
1	67	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	68	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	69	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	70	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	71	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	72	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	73	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	74	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	75	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	76	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	77	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	87	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	93	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	94	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	98	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	104	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	105	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	106	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	107	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	108	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	109	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	112	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	113	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	114	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.014344
1	115	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.014344
1	118	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.014344
1	119	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	120	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	121	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	122	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	123	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	125	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	126	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.015852
1	127	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.015852
1	128	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.99467
1	129	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.998624
1	132	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	133	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	140	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	141	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	142	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	143	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.001554
1	144	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	147	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	148	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	149	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	150	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	151	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	154	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	155	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	156	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	157	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
5	27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.967558
5	28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.968968
5	78	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.969249
5	79	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.96946
5	80	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.966659
6	27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:51.252756
6	28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:51.253653
6	83	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:51.25078
\.


--
-- Data for Name: platform_schema_define; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_schema_define (id, class_name, name, cn_name, i18n_key, display_name, type, type_class_name, is_show, is_sort, is_overflow, is_search, is_fixed, is_require, is_form, align, width, show_order, max_length, min_length, max, min, "precision", multiple, pattern, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, service_name, is_i18n, relation_type, external_class, external_class_primary_field, external_class_display_field, ui_component, field_name, description, encrypt, desensitize, field_component, inner_primary_field, column_component) FROM stdin;
232	com.byteasy.platform.entity.ResourceCategory	remark	备注	com.byteasy.platform.entity.ResourceCategory.remark	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	0	0	0	0	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:49.12287	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
233	com.byteasy.platform.entity.ResourceCategory	creatorId	创建人ID	com.byteasy.entity.ImmutablyEntity.creatorId	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.930885	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
234	com.byteasy.platform.entity.ResourceCategory	createdBy	创建人	com.byteasy.entity.ImmutablyEntity.createdBy	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.940263	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
235	com.byteasy.platform.entity.ResourceCategory	createdTime	创建时间	com.byteasy.entity.ImmutablyEntity.createdTime	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.94826	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
236	com.byteasy.platform.entity.ResourceCategory	updaterId	更新人ID	com.byteasy.entity.AuditEntity.updaterId	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.956105	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
237	com.byteasy.platform.entity.ResourceCategory	updatedBy	更新人	com.byteasy.entity.AuditEntity.updatedBy	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.963965	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
238	com.byteasy.platform.entity.ResourceCategory	updatedTime	更新时间	com.byteasy.entity.AuditEntity.updatedTime	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.968411	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
239	com.byteasy.platform.entity.ResourceCategory	version	乐观锁	com.byteasy.entity.AuditEntity.version	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.972321	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
240	com.byteasy.platform.entity.Resource	id	编号	com.byteasy.entity.Entity.id	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:32:43.200484	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
241	com.byteasy.platform.entity.Resource	code	编码	com.byteasy.platform.entity.Resource.code	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	200	101	150	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:35:35.430512	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
242	com.byteasy.platform.entity.Resource	parentId	上级资源	com.byteasy.platform.entity.Resource.parentId	{"zh_CN":"父文件夹","en":"Parent id"}	3	java.lang.Long	0	0	0	0	0	0	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:34:11.927328	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
243	com.byteasy.platform.entity.Resource	groupId	资源分组	com.byteasy.platform.entity.Resource.groupId	{"zh_CN":"分组","en":"Group id"}	3	java.lang.Long	0	0	0	0	0	0	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:33:26.413813	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
314	com.byteasy.platform.vo.PersonalSettingsVO	gender	\N	\N	{"zh_CN":"性别","en":"Gender"}	9	com.byteasy.enums.Gender	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:54.180959	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
244	com.byteasy.platform.entity.Resource	name	名称	com.byteasy.platform.entity.Resource.name	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	200	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:03:58.619466	4	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
245	com.byteasy.platform.entity.Resource	icon	图标	com.byteasy.platform.entity.Resource.icon	{"zh_CN":"图标","en":"Icon"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	100	2000	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:26:22.985922	8	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
246	com.byteasy.platform.entity.Resource	visible	是否显示	com.byteasy.platform.entity.Resource.visible	{"zh_CN":"显示","en":"Visible"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:34:41.509317	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
439	com.byteasy.platform.entity.Dictionary	status	\N	\N	{"zh_CN":"状态"}	9	com.byteasy.enums.Enable	1	0	0	1	0	1	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.020097	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
247	com.byteasy.platform.entity.Resource	type	资源类型	com.byteasy.platform.entity.Resource.type	{"zh_CN":"类型","en":"type"}	9	com.byteasy.platform.entity.Resource$Type	1	0	0	0	0	1	1	0	0	99	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:11:03.973501	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
248	com.byteasy.platform.entity.Resource	openTab	是否打开tab	com.byteasy.platform.entity.Resource.openTab	{"zh_CN":"打开tab","en":"Open tab"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:35:53.152707	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
249	com.byteasy.platform.entity.Resource	affix	是否固定	com.byteasy.platform.entity.Resource.affix	{"zh_CN":"固定","en":"Fixed"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:08.781373	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
250	com.byteasy.platform.entity.Resource	keepAlive	是否缓存	com.byteasy.platform.entity.Resource.keepAlive	{"zh_CN":"缓存","en":"Cache"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:25.845082	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
251	com.byteasy.platform.entity.Resource	showOrder	显示顺序	com.byteasy.platform.entity.Resource.showOrder	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:46.163904	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
252	com.byteasy.platform.entity.Resource	layout	布局	com.byteasy.platform.entity.Resource.layout	{"zh_CN":"布局","en":"Layout"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:58.735228	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
253	com.byteasy.platform.entity.Resource	component	前端组件	com.byteasy.platform.entity.Resource.component	{"zh_CN":"组件","en":"Component"}	6	java.lang.String	1	0	1	0	0	0	1	0	200	113	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:37:11.770672	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
254	com.byteasy.platform.entity.Resource	routerUrl	路由	com.byteasy.platform.entity.Resource.routerUrl	{"zh_CN":"路由","en":"Router"}	6	java.lang.String	1	0	1	0	0	0	1	0	200	114	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 09:41:39.210696	6	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
255	com.byteasy.platform.entity.Resource	redirectUrl	重定向地址	com.byteasy.platform.entity.Resource.redirectUrl	{"zh_CN":"重定向地址","en":"Redirect router"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	115	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:37:46.20956	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
256	com.byteasy.platform.entity.Resource	remark	备注	com.byteasy.platform.entity.Resource.remark	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	116	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:38:02.081051	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
257	com.byteasy.platform.entity.Resource	creatorId	创建人ID	com.byteasy.entity.ImmutablyEntity.creatorId	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.516256	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
258	com.byteasy.platform.entity.Resource	createdBy	创建人	com.byteasy.entity.ImmutablyEntity.createdBy	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.52716	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
259	com.byteasy.platform.entity.Resource	createdTime	创建时间	com.byteasy.entity.ImmutablyEntity.createdTime	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.537628	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
293	com.byteasy.platform.entity.Role	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.422519	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
315	com.byteasy.platform.vo.PersonalSettingsVO	birthday	\N	\N	{"zh_CN":"出生日期","en":"Birthday"}	7	java.time.LocalDate	1	0	0	0	0	0	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:02.740636	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
260	com.byteasy.platform.entity.Resource	updaterId	更新人ID	com.byteasy.entity.AuditEntity.updaterId	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.549932	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
261	com.byteasy.platform.entity.Resource	updatedBy	更新人	com.byteasy.entity.AuditEntity.updatedBy	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.557805	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
262	com.byteasy.platform.entity.Resource	updatedTime	更新时间	com.byteasy.entity.AuditEntity.updatedTime	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.568236	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
263	com.byteasy.platform.entity.Resource	version	乐观锁	com.byteasy.entity.AuditEntity.version	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.575677	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
264	com.byteasy.platform.entity.ResourceGroup	id	编号	com.byteasy.entity.Entity.id	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.720876	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
265	com.byteasy.platform.entity.ResourceGroup	categoryId	资源分类	com.byteasy.platform.entity.ResourceGroup.categoryId	{"zh_CN":"分类","en":"Category"}	3	java.lang.Long	0	0	0	0	0	1	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:06:45.691421	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
266	com.byteasy.platform.entity.ResourceGroup	code	编码	com.byteasy.platform.entity.ResourceGroup.code	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	102	50	2	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.73708	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
267	com.byteasy.platform.entity.ResourceGroup	name	名称	com.byteasy.platform.entity.ResourceGroup.name	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:07:02.578519	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
268	com.byteasy.platform.entity.ResourceGroup	showOrder	显示顺序	com.byteasy.platform.entity.ResourceGroup.showOrder	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	1	0	0	0	0	1	1	0	200	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:07:19.958639	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
269	com.byteasy.platform.entity.ResourceGroup	router	路由	com.byteasy.platform.entity.ResourceGroup.router	{"zh_CN":"路由","en":"Router"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:08:23.278029	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
270	com.byteasy.platform.entity.ResourceGroup	navbarMenu	快捷菜单	com.byteasy.platform.entity.ResourceGroup.navbarMenu	{"zh_CN":"快捷菜单","en":"Quick entry menu"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	1	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:08:41.226213	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
271	com.byteasy.platform.entity.ResourceGroup	isMicroapp	是否微应用	com.byteasy.platform.entity.ResourceGroup.isMicroapp	{"zh_CN":"微应用","en":"Micro app"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	1	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:01.640388	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
272	com.byteasy.platform.entity.ResourceGroup	entry	入口地址	com.byteasy.platform.entity.ResourceGroup.entry	{"zh_CN":"地址","en":"Entry url"}	6	java.lang.String	1	0	0	0	0	1	1	0	0	108	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:22.157232	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
273	com.byteasy.platform.entity.ResourceGroup	props	拓展属性	com.byteasy.platform.entity.ResourceGroup.props	{"zh_CN":"属性","en":"Properties"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	109	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:41.438752	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
274	com.byteasy.platform.entity.ResourceGroup	remark	备注	com.byteasy.platform.entity.ResourceGroup.remark	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	110	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:51.621784	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
275	com.byteasy.platform.entity.ResourceGroup	creatorId	创建人ID	com.byteasy.entity.ImmutablyEntity.creatorId	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.812111	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
316	com.byteasy.platform.vo.PersonalSettingsVO	mobile	\N	\N	{"zh_CN":"手机","en":"Mobile"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:09.445779	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
276	com.byteasy.platform.entity.ResourceGroup	createdBy	创建人	com.byteasy.entity.ImmutablyEntity.createdBy	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.818972	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
277	com.byteasy.platform.entity.ResourceGroup	createdTime	创建时间	com.byteasy.entity.ImmutablyEntity.createdTime	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.827825	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
434	com.byteasy.platform.entity.LoginLog	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.255754	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
278	com.byteasy.platform.entity.ResourceGroup	updaterId	更新人ID	com.byteasy.entity.AuditEntity.updaterId	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.838162	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
279	com.byteasy.platform.entity.ResourceGroup	updatedBy	更新人	com.byteasy.entity.AuditEntity.updatedBy	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.848932	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
280	com.byteasy.platform.entity.ResourceGroup	updatedTime	更新时间	com.byteasy.entity.AuditEntity.updatedTime	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.872154	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
281	com.byteasy.platform.entity.ResourceGroup	version	乐观锁	com.byteasy.entity.AuditEntity.version	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.894762	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
282	com.byteasy.platform.entity.Role	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35.852676	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
283	com.byteasy.platform.entity.Role	code	\N	\N	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	2	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:36.50328	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
284	com.byteasy.platform.entity.Role	name	\N	\N	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	3	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:35.612731	4	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
285	com.byteasy.platform.entity.Role	status	\N	\N	{"zh_CN":"状态","en":"Status"}	9	com.byteasy.enums.Enable	1	0	0	1	0	1	1	0	0	4	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:34.743962	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
286	com.byteasy.platform.entity.Role	remark	\N	\N	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	5	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:33.346797	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
287	com.byteasy.platform.entity.Role	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.371675	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
288	com.byteasy.platform.entity.Role	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.381987	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
289	com.byteasy.platform.entity.Role	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.390459	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
290	com.byteasy.platform.entity.Role	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.398821	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
291	com.byteasy.platform.entity.Role	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.407175	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
435	com.byteasy.platform.entity.Dictionary	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:11.990132	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
543	components.form.BeLayoutCard.vue	title	\N	\N	{"zh_CN":"卡片标题"}	6	java.lang.String	1	0	0	0	0	1	1	0	120	1	100	1	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-29 15:36:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-29 15:39:24.523431	2	platform-service	1	\N	\N	\N	\N	\N	title	卡片标题	0	0	\N	\N	\N
413	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.747211	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
292	com.byteasy.platform.entity.Role	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.415001	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
294	com.byteasy.platform.vo.PersonalSettingsVO	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.559222	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
295	com.byteasy.platform.vo.PersonalSettingsVO	accountExpiredAt	\N	\N	{"zh_CN":"账号过期时间","en":"Account expire time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:52:37.515168	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
296	com.byteasy.platform.vo.PersonalSettingsVO	credentialsExpiredAt	\N	\N	{"zh_CN":"密码过期时间","en":"Password expire time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:52:28.851264	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
297	com.byteasy.platform.vo.PersonalSettingsVO	mobileVerified	\N	\N	{"zh_CN":"手机是否验证","en":"Mobile verified"}	9	com.byteasy.enums.YesNo	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:52:51.237493	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
298	com.byteasy.platform.vo.PersonalSettingsVO	orgId	\N	\N	{"zh_CN":"组织编号","en":"Org id"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:01.709383	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
299	com.byteasy.platform.vo.PersonalSettingsVO	extInfo	\N	\N	{"zh_CN":"扩展属性","en":"Extant properties"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:12.786869	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
300	com.byteasy.platform.vo.PersonalSettingsVO	password	\N	\N	{"zh_CN":"密码","en":"Password"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	1	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:48:05.021138	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
301	com.byteasy.platform.vo.PersonalSettingsVO	lockTime	\N	\N	{"zh_CN":"账号锁定时间","en":"Lock time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:34.74206	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
302	com.byteasy.platform.vo.PersonalSettingsVO	orgName	\N	\N	{"zh_CN":"所属部门","en":"Org name"}	6	java.lang.String	0	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:34:00.540356	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
303	com.byteasy.platform.vo.PersonalSettingsVO	realmId	\N	\N	{"zh_CN":"领域编号","en":"Realm id"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:57.046801	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
304	com.byteasy.platform.vo.PersonalSettingsVO	salt	\N	\N	{"zh_CN":"密码盐","en":"Salt"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:04.189884	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
305	com.byteasy.platform.vo.PersonalSettingsVO	deptId	\N	\N	{"zh_CN":"部门编号","en":"Dept id"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:13.568177	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
306	com.byteasy.platform.vo.PersonalSettingsVO	emailVerified	\N	\N	{"zh_CN":"邮箱是否验证","en":"Email verified"}	9	com.byteasy.enums.YesNo	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:25.762616	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
307	com.byteasy.platform.vo.PersonalSettingsVO	loginFailCount	\N	\N	{"zh_CN":"登录失败次数","en":"Login failed times"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:54.173873	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
308	com.byteasy.platform.vo.PersonalSettingsVO	tenantId	\N	\N	{"zh_CN":"租户编号","en":"Tenant id"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:04.080767	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
309	com.byteasy.platform.vo.PersonalSettingsVO	username	\N	\N	{"zh_CN":"用户名","en":"User name"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	1	20	4	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-09 15:55:30.929736	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
310	com.byteasy.platform.vo.PersonalSettingsVO	avatar	\N	\N	{"zh_CN":"头像","en":"Avatar"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:13.264617	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
311	com.byteasy.platform.vo.PersonalSettingsVO	userId	\N	\N	{"zh_CN":"用户编号","en":"User id"}	6	java.lang.String	1	0	1	0	0	0	0	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:20.070724	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
312	com.byteasy.platform.vo.PersonalSettingsVO	realName	\N	\N	{"zh_CN":"姓名","en":"Name"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:33.132684	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
313	com.byteasy.platform.vo.PersonalSettingsVO	nickname	\N	\N	{"zh_CN":"昵称","en":"Nick name"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:41.039583	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
317	com.byteasy.platform.vo.PersonalSettingsVO	email	\N	\N	{"zh_CN":"邮箱","en":"Email"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:17.762359	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
318	com.byteasy.platform.vo.PersonalSettingsVO	status	\N	\N	{"zh_CN":"状态","en":"Status"}	9	com.byteasy.platform.entity.UserStatus	1	0	0	1	0	1	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.783023	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
319	com.byteasy.platform.vo.PersonalSettingsVO	userType	\N	\N	{"zh_CN":"类型","en":"User type"}	9	com.byteasy.platform.entity.UserType	1	0	0	1	0	1	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:32.730781	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
320	com.byteasy.platform.vo.PersonalSettingsVO	lastLoginTime	\N	\N	{"zh_CN":"最后登录时间","en":"Last login time"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:41.788642	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
321	com.byteasy.platform.vo.PersonalSettingsVO	lastLoginIp	\N	\N	{"zh_CN":"最后登录IP","en":"Last login ip"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:50.275064	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
322	com.byteasy.platform.vo.PersonalSettingsVO	remark	\N	\N	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	0	0	0	0	0	0	1	0	0	113	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.817328	\N	\N	\N	1	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
323	com.byteasy.platform.vo.PersonalSettingsVO	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.82613	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
324	com.byteasy.platform.vo.PersonalSettingsVO	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.840887	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
325	com.byteasy.platform.vo.PersonalSettingsVO	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.850268	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
326	com.byteasy.platform.vo.PersonalSettingsVO	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.861564	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
327	com.byteasy.platform.vo.PersonalSettingsVO	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.869924	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
328	com.byteasy.platform.vo.PersonalSettingsVO	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.879363	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
329	com.byteasy.platform.vo.PersonalSettingsVO	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.890665	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
330	com.byteasy.platform.vo.PersonalSettingsVO	isDeleted	\N	\N	{"zh_CN":"是否删除","en":"Deleted"}	9	com.byteasy.enums.Delete	0	0	0	0	0	0	0	0	0	10007	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.90097	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
331	com.byteasy.platform.vo.PersonalSettingsVO	deleterId	\N	\N	{"zh_CN":"删除人编号","en":"Deleter id"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10008	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.917286	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
332	com.byteasy.platform.vo.PersonalSettingsVO	deletedBy	\N	\N	{"zh_CN":"删除人","en":"Deleter name"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10009	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.923687	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
333	com.byteasy.platform.vo.PersonalSettingsVO	deletedTime	\N	\N	{"zh_CN":"删除时间","en":"Deleted time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	180	10010	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.934008	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
334	com.byteasy.enums.YesNo	NO	\N	\N	{"zh_CN":"否","en":"No"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:18:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:05.375446	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
335	com.byteasy.enums.YesNo	YES	\N	\N	{"zh_CN":"是","en":"Yes"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:18:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:14.685408	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
336	com.byteasy.enums.Align	LEFT	\N	\N	{"zh_CN":"左对齐","en":"Left"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:59.149411	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
337	com.byteasy.enums.Align	MIDDLE	\N	\N	{"zh_CN":"居中对齐","en":"Middle"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:22.951958	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
338	com.byteasy.enums.Align	RIGHT	\N	\N	{"zh_CN":"右对齐","en":"Right"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:04.960634	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
339	com.byteasy.enums.ColumnType	ENUM	\N	\N	{"zh_CN":"枚举","en":"Enum"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:18.086498	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
340	com.byteasy.enums.ColumnType	FLOAT	\N	\N	{"zh_CN":"单精度浮点数","en":"Float"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:26.426778	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
341	com.byteasy.enums.ColumnType	ARRAY	\N	\N	{"zh_CN":"数组","en":"Array"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:32.839677	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
342	com.byteasy.enums.ColumnType	JSON_ARRAY	\N	\N	{"zh_CN":"Json对象数组","en":"JsonObject Array"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:47.325735	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
343	com.byteasy.enums.ColumnType	JSON	\N	\N	{"zh_CN":"Json对象","en":"Json"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:58.072138	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
344	com.byteasy.enums.ColumnType	LOCAL_DATE_TIME	\N	\N	{"zh_CN":"时间","en":"LocalDateTime"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:09.572565	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
345	com.byteasy.enums.ColumnType	INT	\N	\N	{"zh_CN":"整型","en":"Integer"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:16.34303	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
346	com.byteasy.enums.ColumnType	SHORT	\N	\N	{"zh_CN":"短整型","en":"Short"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:22.502803	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
347	com.byteasy.enums.ColumnType	BYTE	\N	\N	{"zh_CN":"字节","en":"Byte"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:10.40772	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
348	com.byteasy.enums.ColumnType	SPLITTER	\N	\N	{"zh_CN":"分割对象","en":"Splitter"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:03.707992	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
349	com.byteasy.enums.ColumnType	LOCAL_DATE	\N	\N	{"zh_CN":"日期","en":"LocalDate"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:48.925698	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
350	com.byteasy.enums.ColumnType	STRING	\N	\N	{"zh_CN":"字符串","en":"String"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:41.827913	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
351	com.byteasy.enums.ColumnType	DOUBLE	\N	\N	{"zh_CN":"双精度浮点数","en":"Double"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:35.794551	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
352	com.byteasy.enums.ColumnType	LONG	\N	\N	{"zh_CN":"长整型","en":"Long"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:29.237009	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
353	com.byteasy.enums.Delete	NORMAL	\N	\N	{"zh_CN":"正常","en":"Normal"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:30	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:39.284414	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
354	com.byteasy.enums.Delete	DELETE	\N	\N	{"zh_CN":"已删除","en":"Deleted"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:30	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:57.345081	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
355	com.byteasy.enums.Enable	DISABLE	\N	\N	{"zh_CN":"禁用","en":"Disable"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:05	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:14.825726	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
356	com.byteasy.enums.Enable	ENABLE	\N	\N	{"zh_CN":"正常","en":"Enable"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:05	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:25.439756	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
357	com.byteasy.enums.Gender	WOMAN	\N	\N	{"zh_CN":"女性","en":"Woman"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:39	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:46.91152	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
358	com.byteasy.enums.Gender	MAN	\N	\N	{"zh_CN":"男性","en":"Man"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:39	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:52.576776	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
359	com.byteasy.enums.ValidatorPattern	ACCOUNT	\N	\N	{"zh_CN":"登录账号","en":"Account"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:15.341789	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
360	com.byteasy.enums.ValidatorPattern	PASSWORD	\N	\N	{"zh_CN":"密码","en":"Password"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:22.764699	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
361	com.byteasy.enums.ValidatorPattern	NONE	\N	\N	{"zh_CN":"无","en":"None"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:29.543039	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
362	com.byteasy.enums.ValidatorPattern	EMAIL	\N	\N	{"zh_CN":"邮箱","en":"Email"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:35.1447	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
363	com.byteasy.enums.ValidatorPattern	URL	\N	\N	{"zh_CN":"URL","en":"Url"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:40.649767	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
364	com.byteasy.enums.ValidatorPattern	ID_CARD	\N	\N	{"zh_CN":"身份证","en":"ID Card"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:49.344257	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
365	com.byteasy.enums.ValidatorPattern	MOBILE	\N	\N	{"zh_CN":"手机号","en":"Mobile"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:55.489575	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
366	com.byteasy.platform.entity.Resource$Type	DIRECTORY	\N	\N	{"zh_CN":"目录","en":"Directory"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:53:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:54:06.247415	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
367	com.byteasy.platform.entity.Resource$Type	MENU	\N	\N	{"zh_CN":"菜单","en":"Menu"}	9	success	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:53:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 13:47:16.584788	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
368	com.byteasy.platform.entity.Resource$Type	BUTTON	\N	\N	{"zh_CN":"按钮","en":"Button"}	9	warning	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:53:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 13:47:39.612004	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
369	com.byteasy.platform.entity.UserStatus	DISABLE	\N	\N	{"zh_CN":"禁用","en":"Disable"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:23.228939	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
370	com.byteasy.platform.entity.UserStatus	LOGOUT	\N	\N	{"zh_CN":"注销","en":"Logout"}	9	warning	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:31.075912	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
371	com.byteasy.platform.entity.UserStatus	LOCKED	\N	\N	{"zh_CN":"锁定","en":"Locked"}	9	warning	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:37.821068	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
372	com.byteasy.platform.entity.UserStatus	NORMAL	\N	\N	{"zh_CN":"正常","en":"Normal"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:43.991098	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
373	com.byteasy.platform.entity.UserType	SYSTEM	\N	\N	{"zh_CN":"系统","en":"System"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:06.194157	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
374	com.byteasy.platform.entity.UserType	ADMIN	\N	\N	{"zh_CN":"管理员","en":"Admin"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:12.767555	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
375	com.byteasy.platform.entity.UserType	EXTERNAL	\N	\N	{"zh_CN":"外部系统用户","en":"External"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:20.134086	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
376	com.byteasy.platform.entity.UserType	NORMAL	\N	\N	{"zh_CN":"普通账户","en":"Normal"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:26.358324	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
377	com.byteasy.platform.entity.I18n	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:47:30.492093	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
378	com.byteasy.platform.entity.I18n	groupId	\N	\N	{"zh_CN":"应用","en":"Group id"}	3	java.lang.Long	1	0	0	1	0	1	1	0	160	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 14:28:53.819847	8	platform-service	0	0	com.tuna.platform.entity.ResourceGroup	id	name	2	\N	\N	1	1	/views/microapp/developConfig/resource/ResourceCategoryGroupTreeSelect.vue	\N	\N
379	com.byteasy.platform.entity.I18n	code	\N	\N	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.5723	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
380	com.byteasy.platform.entity.I18n	name	\N	\N	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.585604	\N	\N	\N	1	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
381	com.byteasy.platform.entity.I18n	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.614247	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
382	com.byteasy.platform.entity.I18n	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.62371	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
383	com.byteasy.platform.entity.I18n	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.634542	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
399	com.byteasy.knowledgeBase.entity.KnowledgeFile	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.215467	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
384	com.byteasy.platform.entity.I18n	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.643875	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
385	com.byteasy.platform.entity.I18n	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.654823	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
386	com.byteasy.platform.entity.I18n	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.664636	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
387	com.byteasy.platform.entity.I18n	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.674014	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
388	com.byteasy.knowledgeBase.entity.KnowledgeFile	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-01 15:12:30.86852	3	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
389	com.byteasy.knowledgeBase.entity.KnowledgeFile	fileType	\N	\N	{"zh_CN":"种类","en":"Type","fr":"Tipo","de":"Art","it":"Tipo","ja":"種類","ko":"종류","ru":"Вид","pt_br":"Tipo","pt":"Tipo","es":"Tipo"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:07:23.539507	4	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
390	com.byteasy.knowledgeBase.entity.KnowledgeFile	categoryId	\N	\N	{"zh_CN":"分类","en":"Category","fr":"Catégorie","de":"Kategorie","it":"Categoria","ja":"分類","ko":"분류","ru":"Категория","pt_br":"Categoria","pt":"Categoria","es":"Categoría"}	3	java.lang.Long	0	0	0	0	0	1	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.10448	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
391	com.byteasy.knowledgeBase.entity.KnowledgeFile	filePath	\N	\N	{"zh_CN":"路径","en":"Path","fr":"Chemin","de":"Pfad","it":"Percorso","ja":"パス","ko":"경로","ru":"Путь","pt_br":"Caminho","pt":"Caminho","es":"Ruta"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	101	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.114357	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
392	com.byteasy.knowledgeBase.entity.KnowledgeFile	fileName	\N	\N	{"zh_CN":"文件名","en":"File Name","fr":"Nom de fichier","de":"Dateiname","it":"Nome file","ja":"ファイル名","ko":"파일명","ru":"Имя файла","pt_br":"Nome do arquivo","pt":"Nome do ficheiro","es":"Nombre del archivo"}	6	java.lang.String	1	0	0	1	0	1	1	0	260	102	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:36:00.236286	3	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
393	com.byteasy.knowledgeBase.entity.KnowledgeFile	fileVersion	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.137428	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
394	com.byteasy.knowledgeBase.entity.KnowledgeFile	showOrder	\N	\N	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	0	0	0	0	0	1	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:06:14.544628	2	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
395	com.byteasy.knowledgeBase.entity.KnowledgeFile	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.162819	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
396	com.byteasy.knowledgeBase.entity.KnowledgeFile	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.175429	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
397	com.byteasy.knowledgeBase.entity.KnowledgeFile	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.190685	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
398	com.byteasy.knowledgeBase.entity.KnowledgeFile	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.204722	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
400	com.byteasy.knowledgeBase.entity.KnowledgeFile	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.224653	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
401	com.byteasy.knowledgeBase.entity.KnowledgeFile	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.240912	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
402	com.byteasy.knowledgeBase.entity.KnowledgeCategory	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.448966	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
403	com.byteasy.knowledgeBase.entity.KnowledgeCategory	name	\N	\N	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.460434	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
404	com.byteasy.knowledgeBase.entity.KnowledgeCategory	showOrder	\N	\N	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.472174	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
405	com.byteasy.knowledgeBase.entity.KnowledgeCategory	parentId	\N	\N	{"zh_CN":"上级分类","en":"Parent Category","fr":"Catégorie parente","de":"Übergeordnete Kategorie","it":"Categoria padre","ja":"上位分類","ko":"상위 분류","ru":"Родительская категория","pt_br":"Categoria superior","pt":"Categoria superior","es":"Categoría superior"}	3	java.lang.Long	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.490812	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
406	com.byteasy.knowledgeBase.entity.KnowledgeCategory	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.510261	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
407	com.byteasy.knowledgeBase.entity.KnowledgeCategory	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.51946	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
408	com.byteasy.knowledgeBase.entity.KnowledgeCategory	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.525927	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
409	com.byteasy.knowledgeBase.entity.KnowledgeCategory	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.53545	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
410	com.byteasy.knowledgeBase.entity.KnowledgeCategory	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.544717	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
411	com.byteasy.knowledgeBase.entity.KnowledgeCategory	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.554644	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
412	com.byteasy.knowledgeBase.entity.KnowledgeCategory	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.56147	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
433	com.byteasy.platform.entity.LoginLog	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:15:28.334706	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
414	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	shorOrder	\N	\N	{"zh_CN":"顺序","en":"Sequence","fr":"Séquence","de":"Reihenfolge","it":"Sequenza","ja":"順序","ko":"순서","ru":"Последовательность","pt_br":"Sequência","pt":"Sequência","es":"Secuencia"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.754755	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
415	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	fileType	\N	\N	{"zh_CN":"种类","en":"Kind","fr":"Genre","de":"Art","it":"Tipo","ja":"種類","ko":"종류","ru":"Вид","pt_br":"Tipo","pt":"Tipo","es":"Tipo"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.764154	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
416	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	fileId	\N	\N	{"zh_CN":"文件编号","en":"File No","fr":"N° de fichier","de":"Dateinummer","it":"N° file","ja":"ファイル番号","ko":"파일 번호","ru":"Номер файла","pt_br":"Nº do arquivo","pt":"Nº do ficheiro","es":"Nº de archivo"}	3	java.lang.Long	1	0	0	0	0	0	0	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:01:37.624118	2	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
417	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	categoryId	\N	\N	{"zh_CN":"分类","en":"Classification","fr":"Classification","de":"Klassifizierung","it":"Classificazione","ja":"分類","ko":"분류","ru":"Классификация","pt_br":"Classificação","pt":"Classificação","es":"Clasificación"}	3	java.lang.Long	0	0	0	0	0	1	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.780295	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
418	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	lang	\N	\N	{"zh_CN":"语言","en":"Language","fr":"Langue","de":"Sprache","it":"Lingua","ja":"言語","ko":"언어","ru":"Язык","pt_br":"Idioma","pt":"Idioma","es":"Idioma"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	101	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.793124	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
419	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	filePath	\N	\N	{"zh_CN":"路径","en":"Path","fr":"Chemin","de":"Pfad","it":"Percorso","ja":"パス","ko":"경로","ru":"Путь","pt_br":"Caminho","pt":"Caminho","es":"Ruta"}	6	java.lang.String	0	0	0	0	0	1	1	0	0	102	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.799935	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
420	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	fileName	\N	\N	{"zh_CN":"文档名","en":"Document Name","fr":"Nom du document","de":"Dokumentenname","it":"Nome documento","ja":"文書名","ko":"문서명","ru":"Название документа","pt_br":"Nome do documento","pt":"Nome do documento","es":"Nombre del documento"}	6	java.lang.String	1	0	0	0	0	1	1	0	260	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.807277	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
421	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	fileVersion	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.814012	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
422	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.820804	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
423	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.829323	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
424	com.byteasy.knowledgeBase.entity.KnowledgeFileHistory	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.836505	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
425	com.byteasy.platform.entity.LoginLog	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.134678	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
426	com.byteasy.platform.entity.LoginLog	type	\N	\N	{"zh_CN":"类型"}	9	com.byteasy.platform.entity.LoginLog$Type	1	0	0	1	0	0	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.16269	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
427	com.byteasy.platform.entity.LoginLog	username	\N	\N	{"zh_CN":"账号"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:13:04.035844	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
428	com.byteasy.platform.entity.LoginLog	realName	\N	\N	{"zh_CN":"姓名"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.188313	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
429	com.byteasy.platform.entity.LoginLog	ipAddress	\N	\N	{"zh_CN":"IP地址"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.200329	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
430	com.byteasy.platform.entity.LoginLog	sessionId	\N	\N	{"zh_CN":"Session"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.209126	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
431	com.byteasy.platform.entity.LoginLog	remark	\N	\N	{"zh_CN":"备注"}	6	java.lang.String	0	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:24:24.539572	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
432	com.byteasy.platform.entity.LoginLog	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:06:44.740611	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
436	com.byteasy.platform.entity.Dictionary	groupId	\N	\N	{"zh_CN":"资源分组"}	3	java.lang.Long	0	0	0	0	0	0	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:11.998497	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
437	com.byteasy.platform.entity.Dictionary	code	\N	\N	{"zh_CN":"编码"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.00239	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
438	com.byteasy.platform.entity.Dictionary	name	\N	\N	{"zh_CN":"名称"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.011733	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
440	com.byteasy.platform.entity.Dictionary	remark	\N	\N	{"zh_CN":"备注"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 16:43:16.488847	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
441	com.byteasy.platform.entity.Dictionary	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.033334	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
442	com.byteasy.platform.entity.Dictionary	createdBy	\N	\N	{"zh_CN":"创建人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.039736	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
443	com.byteasy.platform.entity.Dictionary	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:59:00.024095	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
444	com.byteasy.platform.entity.Dictionary	updaterId	\N	\N	{"zh_CN":"更新人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.053952	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
445	com.byteasy.platform.entity.Dictionary	updatedBy	\N	\N	{"zh_CN":"更新人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.06036	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
446	com.byteasy.platform.entity.Dictionary	updatedTime	\N	\N	{"zh_CN":"更新时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:38.990057	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
447	com.byteasy.platform.entity.Dictionary	version	\N	\N	{"zh_CN":"版本"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.073589	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
458	com.byteasy.platform.vo.PersonalSettingsVO	extendMap	\N	\N	{"zh_CN":"extendMap"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:21:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 08:22:43.897571	2	platform-service	0	\N	\N	id	name	1			0	0	\N	\N	\N
460	com.byteasy.platform.entity.EntityDefine	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.647705	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	id		0	0	\N	\N	\N
461	com.byteasy.platform.entity.EntityDefine	extendMap	\N	\N	{"zh_CN":"extendMap"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 08:22:50.169082	2	platform-service	0	\N	\N	id	name	1			0	0	\N	\N	\N
463	com.byteasy.platform.entity.EntityDefine	serviceCode	\N	\N	{"zh_CN":"服务"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-04 16:46:12.21525	3	platform-service	0	0	com.tuna.platform.entity.ServiceInstance	code	name	2	service_code		0	0	\N	\N	\N
464	com.byteasy.platform.entity.EntityDefine	name	\N	\N	{"zh_CN":"名称"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	102	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.698477	\N	\N	\N	1	platform-service	1	\N	\N	id	name	1	name		0	0	\N	\N	\N
465	com.byteasy.platform.entity.EntityDefine	url	\N	\N	{"zh_CN":"接口地址"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	103	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.710769	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	url		0	0	\N	\N	\N
466	com.byteasy.platform.entity.EntityDefine	remark	\N	\N	{"zh_CN":"说明"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	104	1024	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 15:46:39.953391	3	platform-service	0	\N	\N	id	name	1	remark		0	0	\N	\N	
467	com.byteasy.platform.entity.EntityDefine	titleColumnName	\N	\N	{"zh_CN":"标题属性列"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.741738	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	title_column_name		0	0	\N	\N	\N
468	com.byteasy.platform.entity.EntityDefine	operationColumnWidth	\N	\N	{"zh_CN":"操作列宽度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.752092	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	operation_column_width		0	0	\N	\N	\N
469	com.byteasy.platform.entity.EntityDefine	queryParams	\N	\N	{"zh_CN":"默认查询条件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.762454	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	query_params		0	0	\N	\N	\N
470	com.byteasy.platform.entity.EntityDefine	formComponent	\N	\N	{"zh_CN":"表单组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.772344	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	form_component		0	0	\N	\N	\N
471	com.byteasy.platform.entity.EntityDefine	formWidth	\N	\N	{"zh_CN":"表单组件宽度"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.788621	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	form_width		0	0	\N	\N	\N
499	com.byteasy.platform.entity.FieldDefine	name	\N	\N	{"zh_CN":"属性名"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.958895	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	name		0	0	\N	id	\N
472	com.byteasy.platform.entity.EntityDefine	dialogModel	\N	\N	{"zh_CN":"启用对话框"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 13:50:31.189804	3	platform-service	0	\N	\N	id	name	1	dialog_model		0	0	\N	\N	
473	com.byteasy.platform.entity.EntityDefine	paginationVisible	\N	\N	{"zh_CN":"启用分页插件"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.80635	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	pagination_visible		0	0	\N	\N	\N
474	com.byteasy.platform.entity.EntityDefine	excelEnable	\N	\N	{"zh_CN":"启用Excel导入导出"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.815143	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	excel_enable		0	0	\N	\N	\N
475	com.byteasy.platform.entity.EntityDefine	queryFormVisible	\N	\N	{"zh_CN":"启用查询条件"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	113	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.858058	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	query_form_visible		0	0	\N	\N	\N
476	com.byteasy.platform.entity.EntityDefine	excelImportComponent	\N	\N	{"zh_CN":"Excel导入组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	114	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 08:48:22.547802	2	platform-service	0	\N	\N	id	name	1	excel_import_component		0	0	/components/TunaComponentSelect.vue	\N	\N
477	com.byteasy.platform.entity.EntityDefine	rowKey	\N	\N	{"zh_CN":"RowKey"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	115	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.920018	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	row_key		0	0	\N	\N	\N
478	com.byteasy.platform.entity.EntityDefine	defaultExpandAll	\N	\N	{"zh_CN":"默认展开"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	116	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.933317	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	default_expand_all		0	0	\N	\N	\N
479	com.byteasy.platform.entity.EntityDefine	treeProps	\N	\N	{"zh_CN":"树属性"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	117	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.942794	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	tree_props		0	0	\N	\N	\N
480	com.byteasy.platform.entity.EntityDefine	queryMode	\N	\N	{"zh_CN":"启用查询模式"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	118	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.953107	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	query_mode		0	0	\N	\N	\N
482	com.byteasy.platform.entity.EntityDefine	showLang	\N	\N	{"zh_CN":"显示语言"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	120	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.972873	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	show_lang		0	0	\N	\N	\N
483	com.byteasy.platform.entity.EntityDefine	showSummary	\N	\N	{"zh_CN":"启用合计"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	121	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.980741	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	show_summary		0	0	\N	\N	\N
484	com.byteasy.platform.entity.EntityDefine	queryBarButtons	\N	\N	{"zh_CN":"查询栏拓展按钮"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	122	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:41:39.337943	5	platform-service	0	\N	\N	id	name	1	query_bar_buttons		0	0	/components/TunaStringArraySplitInput.vue	\N	\N
485	com.byteasy.platform.entity.EntityDefine	operationButtons	\N	\N	{"zh_CN":"操作列拓展按钮"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	123	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:41:46.450823	5	platform-service	0	\N	\N	id	name	1	operation_buttons		0	0	/components/TunaStringArraySplitInput.vue	\N	\N
486	com.byteasy.platform.entity.EntityDefine	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.014252	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATOR_ID		0	0	\N	\N	\N
487	com.byteasy.platform.entity.EntityDefine	createdBy	\N	\N	{"zh_CN":"创建人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.018689	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_BY		0	0	\N	\N	\N
488	com.byteasy.platform.entity.EntityDefine	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.028514	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_TIME		0	0	\N	\N	\N
489	com.byteasy.platform.entity.EntityDefine	updaterId	\N	\N	{"zh_CN":"更新人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.034928	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATER_ID		0	0	\N	\N	\N
490	com.byteasy.platform.entity.EntityDefine	updatedBy	\N	\N	{"zh_CN":"更新人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.040167	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_BY		0	0	\N	\N	\N
491	com.byteasy.platform.entity.EntityDefine	updatedTime	\N	\N	{"zh_CN":"更新时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.04912	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_TIME		0	0	\N	\N	\N
492	com.byteasy.platform.entity.EntityDefine	version	\N	\N	{"zh_CN":"版本"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.054558	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	VERSION		0	0	\N	\N	\N
494	com.byteasy.platform.entity.EntityDefine	tableName	\N	\N	{"zh_CN":"数据库表名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	104	1024	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-01 15:32:34.137969	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	table_name		0	0	\N	id	\N
495	com.byteasy.platform.entity.FieldDefine	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.887132	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	id		0	0	\N	id	\N
498	com.byteasy.platform.entity.FieldDefine	className	\N	\N	{"zh_CN":"类"}	6	java.lang.String	1	0	0	1	0	0	1	0	240	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.945998	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	class_name		0	0	\N	id	\N
500	com.byteasy.platform.entity.FieldDefine	fieldName	\N	\N	{"zh_CN":"字段名"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.971274	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	field_name		0	0	\N	id	\N
501	com.byteasy.platform.entity.FieldDefine	description	\N	\N	{"zh_CN":"说明"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 11:10:26.933141	2	platform-service	1	\N	\N	id	name	1	description		0	0	\N	id	\N
502	com.byteasy.platform.entity.FieldDefine	displayName	\N	\N	{"zh_CN":"显示名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	106	80	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.99542	\N	\N	\N	1	platform-service	1	\N	\N	id	name	1	display_name		0	0	\N	id	\N
503	com.byteasy.platform.entity.FieldDefine	type	\N	\N	{"zh_CN":"字段类型"}	9	com.byteasy.enums.ColumnType	1	0	0	0	0	0	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.010761	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	type		0	0	\N	id	\N
504	com.byteasy.platform.entity.FieldDefine	typeClassName	\N	\N	{"zh_CN":"字段类名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.024084	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	type_class_name		0	0	\N	id	\N
505	com.byteasy.platform.entity.FieldDefine	isShow	\N	\N	{"zh_CN":"是否显示"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.036461	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_show		0	0	\N	id	\N
506	com.byteasy.platform.entity.FieldDefine	isOverflow	\N	\N	{"zh_CN":"是否省略"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.047748	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_overflow		0	0	\N	id	\N
507	com.byteasy.platform.entity.FieldDefine	isI18n	\N	\N	{"zh_CN":"是否多语言"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.056733	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_i18n		0	0	\N	id	\N
508	com.byteasy.platform.entity.FieldDefine	showOrder	\N	\N	{"zh_CN":"显示顺序"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.06856	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	show_order		0	0	\N	id	\N
509	com.byteasy.platform.entity.FieldDefine	width	\N	\N	{"zh_CN":"显示宽度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	113	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.079905	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	width		0	0	\N	id	\N
510	com.byteasy.platform.entity.FieldDefine	isRequire	\N	\N	{"zh_CN":"是否必填"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	114	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.099625	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_require		0	0	\N	id	\N
511	com.byteasy.platform.entity.FieldDefine	isForm	\N	\N	{"zh_CN":"是否表单"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	115	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.119366	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_form		0	0	\N	id	\N
512	com.byteasy.platform.entity.FieldDefine	isSort	\N	\N	{"zh_CN":"是否排序"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	116	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.138615	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_sort		0	0	\N	id	\N
513	com.byteasy.platform.entity.FieldDefine	isSearch	\N	\N	{"zh_CN":"是否搜索"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	117	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.158028	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_search		0	0	\N	id	\N
514	com.byteasy.platform.entity.FieldDefine	isFixed	\N	\N	{"zh_CN":"是否固定"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	118	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.169311	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_fixed	在Table内是否固定	0	0	\N	id	\N
515	com.byteasy.platform.entity.FieldDefine	encrypt	\N	\N	{"zh_CN":"是否加密"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	119	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.187069	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	encrypt		0	0	\N	id	\N
516	com.byteasy.platform.entity.FieldDefine	desensitize	\N	\N	{"zh_CN":"是否脱敏"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	120	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.199356	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	desensitize		0	0	\N	id	\N
517	com.byteasy.platform.entity.FieldDefine	align	\N	\N	{"zh_CN":"对齐方式"}	9	com.byteasy.enums.Align	1	0	0	0	0	0	1	0	0	121	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.210818	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	align	在Table内的对齐方式	0	0	\N	id	\N
518	com.byteasy.platform.entity.FieldDefine	maxLength	\N	\N	{"zh_CN":"最大长度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	122	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.221727	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	max_length		0	0	\N	id	\N
519	com.byteasy.platform.entity.FieldDefine	minLength	\N	\N	{"zh_CN":"最小长度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	123	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.232035	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	min_length		0	0	\N	id	\N
520	com.byteasy.platform.entity.FieldDefine	max	\N	\N	{"zh_CN":"最大值"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	124	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.24429	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	max		0	0	\N	id	\N
521	com.byteasy.platform.entity.FieldDefine	min	\N	\N	{"zh_CN":"最小值"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	125	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.255338	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	min		0	0	\N	id	\N
522	com.byteasy.platform.entity.FieldDefine	precision	\N	\N	{"zh_CN":"精度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	126	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.264177	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	precision		0	0	\N	id	\N
523	com.byteasy.platform.entity.FieldDefine	multiple	\N	\N	{"zh_CN":"是否多选"}	9	com.byteasy.enums.YesNo	1	0	0	0	0	0	1	0	0	127	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.27547	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	multiple	当表格组件被用于对象筛选时起作用，控制是单选还是多选	0	0	\N	id	\N
524	com.byteasy.platform.entity.FieldDefine	pattern	\N	\N	{"zh_CN":"校验规则"}	9	com.byteasy.enums.ValidatorPattern	1	0	0	0	0	0	1	0	0	128	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.296233	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	pattern	自定义校验规则	0	0	\N	id	\N
227	com.byteasy.platform.entity.ResourceCategory	id	编号	com.byteasy.entity.Entity.id	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.888567	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
228	com.byteasy.platform.entity.ResourceCategory	parentId	资源分类	com.byteasy.platform.entity.ResourceCategory.parentId	{"zh_CN":"上级分类","en":"Parent category"}	3	java.lang.Long	1	0	0	0	0	0	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:04:55.018165	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
525	com.byteasy.platform.entity.FieldDefine	relationType	\N	\N	{"zh_CN":"关联类型"}	9	com.byteasy.enums.RelationType	1	0	0	0	0	0	1	0	0	129	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.312996	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	relation_type	目前支持外键关联和明细表关联，1对1或者1对多	0	0	\N	id	\N
526	com.byteasy.platform.entity.FieldDefine	externalClass	\N	\N	{"zh_CN":"关联对象类名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	130	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.335601	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	external_class		0	0	\N	id	\N
527	com.byteasy.platform.entity.FieldDefine	externalClassPrimaryField	\N	\N	{"zh_CN":"关联对象字段"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	131	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.346938	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	external_class_primary_field		0	0	\N	id	\N
535	com.byteasy.platform.entity.FieldDefine	updaterId	\N	\N	{"zh_CN":"更新人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.439271	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATER_ID		0	0	\N	id	\N
536	com.byteasy.platform.entity.FieldDefine	updatedBy	\N	\N	{"zh_CN":"更新人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.449584	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_BY		0	0	\N	id	\N
537	com.byteasy.platform.entity.FieldDefine	updatedTime	\N	\N	{"zh_CN":"更新时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.461555	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_TIME		0	0	\N	id	\N
538	com.byteasy.platform.entity.FieldDefine	version	\N	\N	{"zh_CN":"版本"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.471455	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	VERSION		0	0	\N	id	\N
539	com.byteasy.platform.entity.FieldDefine	columnComponent	\N	\N	{"zh_CN":"表格列自定义组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	136	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:48:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 11:49:28.683891	10	platform-service	0	\N	\N	id	name	1	column_component	Table Column自定义组件	0	0	/components/TunaComponentSelect.vue	id
540	com.byteasy.platform.entity.Dictionary	extendMap	\N	\N	{"zh_CN":"extendMap"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:54:40.900051	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1			0	0	\N	id	\N
541	components.TunaBeanInput.vue	code	\N	\N	{"zh_CN":"字典编码"}	6	java.lang.String	1	0	0	0	0	1	1	0	120	1	200	1	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-07 08:37:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-07 08:49:32.305444	3	platform-service	0	\N	\N	\N	\N	\N	\N	字典组件元数据	0	0	\N	\N	\N
229	com.byteasy.platform.entity.ResourceCategory	code	编码	com.byteasy.platform.entity.ResourceCategory.code	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	0	0	0	0	0	1	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:07.616567	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
230	com.byteasy.platform.entity.ResourceCategory	name	名称	com.byteasy.platform.entity.ResourceCategory.name	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	0	0	1	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:22.759959	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
231	com.byteasy.platform.entity.ResourceCategory	showOrder	显示顺序	com.byteasy.platform.entity.ResourceCategory.showOrder	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	0	0	0	0	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:38.314847	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
528	com.byteasy.platform.entity.FieldDefine	innerPrimaryField	\N	\N	{"zh_CN":"明细表关联字段"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	132	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.356403	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	inner_primary_field	关联类型为明细表时数据库无真实字段，需要指明本对象的关联字段名	0	0	\N	id	\N
529	com.byteasy.platform.entity.FieldDefine	externalClassDisplayField	\N	\N	{"zh_CN":"关联对象展示字段"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	133	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.370679	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	external_class_display_field	用于展示关联对象的字段名称	0	0	\N	id	\N
530	com.byteasy.platform.entity.FieldDefine	uiComponent	\N	\N	{"zh_CN":"关联对象前端组件"}	9	com.byteasy.enums.UiComponent	1	0	0	0	0	0	1	0	0	134	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.382948	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	ui_component	当字段是外键类型用于查询条件关联选择或者表单录入的时候关联选择	0	0	\N	id	\N
531	com.byteasy.platform.entity.FieldDefine	fieldComponent	\N	\N	{"zh_CN":"表单自定义组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	135	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 11:49:50.349018	2	platform-service	0	\N	\N	id	name	1	field_component	绑定到字段上的自定义前端组件,查询表单和编辑表单共用	0	0	/components/TunaComponentSelect.vue	id	\N
532	com.byteasy.platform.entity.FieldDefine	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.406221	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATOR_ID		0	0	\N	id	\N
533	com.byteasy.platform.entity.FieldDefine	createdBy	\N	\N	{"zh_CN":"创建人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.418485	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_BY		0	0	\N	id	\N
534	com.byteasy.platform.entity.FieldDefine	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.430365	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_TIME		0	0	\N	id	\N
497	com.byteasy.platform.entity.FieldDefine	serviceName	\N	\N	{"zh_CN":"服务"}	6	java.lang.String	1	0	0	1	0	0	1	0	240	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 09:42:06.624026	2	platform-service	0	0	com.byteasy.platform.entity.ServiceInstance	code	name	1	service_name		0	0	\N	id	\N
462	com.byteasy.platform.entity.EntityDefine	code	\N	\N	{"zh_CN":"编码"}	6	java.lang.String	1	0	1	1	0	0	1	0	0	100	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 10:00:11.306109	2	platform-service	0	\N	\N	id	name	1	code		0	0	\N	\N	\N
546	components.base.style	display	\N	\N	{"zh_CN":"展示"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	3	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 11:45:32.620617	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	display	{"zh_CN":"展示"}	0	0	/components/form/BeFormPropsDisplayEditor.vue?	\N	\N
547	components.base.style	backgroundColor	\N	\N	{"zh_CN":"背景色"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	4	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 16:37:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 16:45:05.883938	3	platform-service	0	\N	\N	\N	\N	\N	backgroundColor	{"zh_CN":"背景色"}	1	1	/components/BeColorPicker.vue?	\N	\N
550	components.base.style	marginStyle	\N	\N	{"zh_CN":"外边距样式编辑"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	7	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 15:18:19.53667	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	marginStyle	{"zh_CN":"外边距样式编辑"}	0	0	/components/form/BeFormPopsMarginStylePicker.vue?	\N	\N
545	components.base.style	height	\N	\N	{"zh_CN":"高度"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	2	10	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 11:20:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:03:18.680316	2	platform-service	0	\N	\N	\N	\N	\N	height	{"zh_CN":"如：200px 200vh 50%"}	0	0	/components/BeUnitInput.vue?	\N	\N
542	components.BeEnumSelect.vue	code	\N	\N	{"zh_CN":"枚举类名"}	6	java.lang.String	1	0	0	0	0	1	1	0	120	100	100	1	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 09:54:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 17:20:51.238798	4	platform-service	0	\N	\N	\N	\N	\N	code	{"zh_CN":"枚举类名"}	0	0	\N	\N	\N
548	components.base.style	boxShadow	\N	\N	{"zh_CN":"阴影"}	6	java.lang.String	1	0	0	1	0	0	1	0	120	5	80	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 17:06:02	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 17:10:21.167904	2	platform-service	0	\N	\N	\N	\N	\N	boxShadow	{"zh_CN":"阴影"}	1	1	/components/form/BeFormPropsBoxShadow.vue?	\N	\N
549	components.base.style	fontStyle	\N	\N	{"zh_CN":"字体样式"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	6	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 13:50:40	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 13:55:49.529788	3	platform-service	0	\N	\N	\N	\N	\N	fontStyle	{"zh_CN":"字体样式"}	0	0	/components/BeFontStylePicker.vue?	\N	\N
544	components.base.style	width	\N	\N	{"zh_CN":"宽度"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	1	10	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-30 09:58:51	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:03:08.940795	3	platform-service	0	\N	\N	\N	\N	\N	width	{"zh_CN":"如：200px 200vw 200rem 100%等"}	0	0	/components/BeUnitInput.vue?	\N	\N
551	components.base.style	paddingStyle	\N	\N	{"zh_CN":"内边距编辑"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	8	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:28:32.482056	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	paddingStyle	{"zh_CN":"内边距编辑"}	0	0	/components/form/BeFormPropsPaddingStylePicker.vue?	\N	\N
552	components.base.style	radiusStyle	\N	\N	{"zh_CN":"圆角编辑器"}	6	java.lang.String	1	0	0	1	0	0	1	0	120	9	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:37:47.073181	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	radiusStyle	{"zh_CN":"圆角编辑器"}	1	1	/components/form/BeFormPopsBorderRadiusPicker.vue?	\N	\N
553	components.base.style	borderStyle	\N	\N	{"zh_CN":"边框"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	10	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:44:05.983195	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	borderStyle	{"zh_CN":"边框"}	0	0	/components/form/BeFormPopsBorderPicker.vue?	\N	\N
554	components.base.style	opacityStyle	\N	\N	{"zh_CN":"透明度"}	6	java.lang.String	1	0	0	0	0	0	1	0	120	11	100	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:56:54	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-07-02 16:58:01.640104	2	platform-service	0	\N	\N	\N	\N	\N	opacityStyle	{"zh_CN":"透明度"}	0	0	/components/BeSlider.vue?	\N	\N
\.


--
-- Data for Name: platform_service_instance; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_service_instance (id, code, context_path, status, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
3	platform-service	/platformSvr	1	{"zh_CN":"平台基础服务"}	\N	platform-service	platform-service	2026-05-21 15:49:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 09:55:36.768582	8
5	knowledge-base-service	/knowledge-base	1	{"zh_CN":"knowledge-base-service"}	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:38.715792	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:03:02.55111	2
30	product-assistant-service	/product-assistant	1	{"zh_CN":"product-assistant-service"}	\N	product-assistant-service	product-assistant-service	2026-06-12 09:24:58.348269	\N	\N	\N	1
\.


--
-- Data for Name: platform_user; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_user (id, user_id, username, password, salt, account_expired_at, credentials_expired_at, real_name, nickname, gender, birthday, avatar, mobile, mobile_verified, email, email_verified, status, user_type, login_fail_count, lock_time, last_login_time, last_login_ip, tenant_id, realm_id, org_id, dept_id, ext_info, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, is_deleted, deleter_id, deleted_by, deleted_time, version) FROM stdin;
2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	test	{bcrypt}$2a$10$P1QgrD14fkOwgSsNFGF0HOBnHlStv.hFBHl5qodDZk2Pu931ZaptC		\N	\N	管理员	风清扬	0	2026-02-02	/platformSvr/upload/files/public/2026/4/08164216.png	15315086265	0	xushuyang@mylgag.com	0	0	0	0	\N	\N		default	1	\N	\N	{}	测试	\N	\N	2026-03-03 01:11:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:51:52.740903	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:51:52.723221	18
4	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	administrator	{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6		\N	\N	超级管理员		0	2026-04-21		15315086265	\N	xushuyang@lgag.com	\N	1	1	0	\N	\N		default	\N	\N	\N	{}	\N	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-09 15:38:43.214093	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:51:43.138431	0			\N	5
5	bae99f2a-8f2f-4f12-b2e7-6320f389b5a7	test	{bcrypt}$2a$10$r0/lRye8hET06q45FVADdOng.rbiNSZWzywD.HlalFgxwGA19K9CO		\N	\N	test		0	2026-05-11			\N		\N	1	0	25	\N	\N		default	\N	\N	\N	{}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:26:15.315869	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 16:59:26.036048	0			\N	29
\.


--
-- Data for Name: platform_user_group; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_user_group (id, code, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
\.


--
-- Data for Name: platform_user_group_resource; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_user_group_resource (group_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_user_group_user; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_user_group_user (user_id, group_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_user_resource; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_user_resource (user_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_user_role; Type: TABLE DATA; Schema: byteasy_cloud_platform; Owner: postgres
--

COPY byteasy_cloud_platform.platform_user_role (user_id, role_id, creator_id, created_by, created_time) FROM stdin;
4	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 15:01:12.251246
4	5	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 15:01:12.253939
\.


--
-- Data for Name: knowledge_category; Type: TABLE DATA; Schema: knowledge_base; Owner: postgres
--

COPY knowledge_base.knowledge_category (id, parent_id, name, show_order, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	\N	{"zh_CN":"全部知识","en":"All Knowledge","fr":"Toutes les connaissances","de":"Alle Kenntnisse","it":"Tutte le conoscenze","ja":"全ての知識","ko":"모든 지식","ru":"Все знания","pt_br":"Todo o conhecimento","pt":"Todo o conhecimento","es":"Todo el conocimiento"}	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 17:30:11	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:59:15.364592	2
6	1	{"zh_CN":"营销","en":"Marketing","fr":"Marketing","de":"Marketing","it":"Marketing","ja":"マーケティング","ko":"마케팅","ru":"Маркетинг","pt_br":"Marketing","pt":"Marketing","es":"Marketing"}	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-20 09:48:45.484896	\N	\N	\N	1
7	1	{"zh_CN":"服务","en":"Service","fr":"Service","de":"Dienst","it":"Servizio","ja":"サービス","ko":"서비스","ru":"Сервис","pt_br":"Serviço","pt":"Serviço","es":"Servicio"}	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-20 09:49:00.350559	\N	\N	\N	1
10	7	{"zh_CN":"EM2001","en":"EM2001","fr":"EM2001","de":"EM2001","it":"EM2001","ja":"EM2001","ko":"EM2001","ru":"EM2001","pt_br":"EM2001","pt":"EM2001","es":"EM2001"}	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-20 10:46:28.545226	\N	\N	\N	1
24	7	{"zh_CN":"DB1002","en":"DB1002","fr":"DB1002","de":"DB1002","it":"DB1002","ja":"DB1002","ko":"DB1002","ru":"DB1002","pt_br":"DB1002","pt":"DB1002","es":"DB1002"}	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-20 16:20:10.31399	\N	\N	\N	1
\.


--
-- Data for Name: knowledge_file; Type: TABLE DATA; Schema: knowledge_base; Owner: postgres
--

COPY knowledge_base.knowledge_file (id, category_id, file_name, file_path, file_type, file_version, show_order, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
8	10	{"zh_CN":"抵押合同.pdf","en":"english.pdf","fr":"fa.pdf","de":"de.pdf","ru":"ru.pdf"}	{"zh_CN":"upload/files/public/2026/4/27105258.pdf","en":"upload/files/public/2026/4/27105320.pdf","fr":"upload/files/public/2026/4/27105339.pdf","de":"upload/files/public/2026/4/27105208.pdf","ru":"upload/files/public/2026/4/27105220.pdf"}	1	{"zh_CN":"2","en":"2","fr":"2","de":"1","ru":"1"}	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 10:52:22	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:01:12.371982	5
\.


--
-- Data for Name: knowledge_file_history; Type: TABLE DATA; Schema: knowledge_base; Owner: postgres
--

COPY knowledge_base.knowledge_file_history (id, file_id, category_id, lang, file_name, file_path, file_version, file_type, version, shor_order, creator_id, created_by, created_time) FROM stdin;
7	8	10	zh_CN	抵押合同.pdf	upload/files/public/2026/4/27105258.pdf	1	1	\N	1	administrator	超级管理员	2026-04-27 10:52:59.886064
8	8	10	en	english.pdf	upload/files/public/2026/4/27105320.pdf	1	1	\N	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 10:53:21.714511
9	8	10	fr	fa.pdf	upload/files/public/2026/4/27105339.pdf	1	1	\N	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 10:53:40.360717
\.


--
-- Data for Name: model_code_serial_no; Type: TABLE DATA; Schema: micro_product_assistant; Owner: postgres
--

COPY micro_product_assistant.model_code_serial_no (id, model_code, serial_number, serial_number_encrypted, status, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
3	D1204-4P	1	0J	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-07 16:28:05.049915	\N	\N	\N	1
4	F1404-5P	1	0J	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:32:57.900924	\N	\N	\N	1
5	D1204-4P	1	0J	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:34:19.029268	\N	\N	\N	1
6	D1204-4P	2	0S	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:35:10.598575	\N	\N	\N	1
7	D1204-4P	3	0L	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:36:41.520498	\N	\N	\N	1
8	D1204-4P	4	0U	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:37:17.709671	\N	\N	\N	1
9	D1204-4P	5	0N	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 11:48:28.938599	\N	\N	\N	1
10	D1204-4P	6	0W	2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 11:49:43.822579	\N	\N	\N	1
13	D1204-4MA	1	0J	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:19:19.372344	\N	\N	\N	1
25	4P-120	1	0J	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 09:58:58.445481	\N	\N	\N	1
49	D1004-4P	1	0J	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:02:25.335175	\N	\N	\N	1
54	4P-120	2	0S	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:22:24.105943	\N	\N	\N	1
\.


--
-- Data for Name: model_env_pub_no; Type: TABLE DATA; Schema: micro_product_assistant; Owner: postgres
--

COPY micro_product_assistant.model_env_pub_no (id, code, model_code, machine_family, engine_family, engine_model, engine_public_number, environmental_public_number, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
3	LGAG0001	D1204-4P	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000216 000001	CN FJ G4 00 03A1000001 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
4	LGAG0002	D1004-4P	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000001	CN FJ G4 00 03A1000002 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
5	LGAG0002	D1004-4P	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000002	CN FJ G4 00 03A1000002 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
6	LGAG0003	D1054-4P	G403A1FJ2QCC01	G40602L0362C01	V38-120C43/安徽全柴动力股份有限公司 89/2300	CN FC G4 00 0602000219 000001	CN FJ G4 00 03A1000003 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
7	LGAG0004	D1204-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000216 000001	CN FJ G4 00 03A1000004 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
8	LGAG0005	D1204-4H	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000216 000001	CN FJ G4 00 03A1000005 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
9	LGAG0006	G2104-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H230.1G4B/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000388 000001	CN FJ G4 00 03A1000006 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
10	LGAG0006	G2104-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H230.1G4B/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000388 000002	CN FJ G4 00 03A1000006 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
11	LGAG0006	G2104-6M	G403A1FJ2SCH01	G4SDECL0644H01	SC7H230G4/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000034 000001	CN FJ G4 00 03A1000006 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
12	LGAG0006	G2104-6M	G403A1FJ2SCH01	G4SDECL0644H01	SC7H230G4/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000034 000002	CN FJ G4 00 03A1000006 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
13	LGAG0007	D1004-4H	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000001	CN FJ G4 00 03A1000007 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
14	LGAG0007	D1004-4H	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000002	CN FJ G4 00 03A1000007 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
15	LGAG0009	T3004-7E	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司 250/2200	CN FC G4 00 0293000184 000001	CN FJ G4 00 03A1000023 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
16	LGAG0009	T3004-7E	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司 250/2200	CN FC G4 00 0293000184 000002	CN FJ G4 00 03A1000023 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
17	LGAG0010	T2804-7E	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF320G4/上海新动力汽车科技股份有限公司 235.5/2200	CN FC G4 00 0293000007 000001	CN FJ G4 00 03A1000009 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
18	LGAG0010	T2804-7E	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF320G4/上海新动力汽车科技股份有限公司 235.5/2200	CN FC G4 00 0293000007 000002	CN FJ G4 00 03A1000009 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
19	LGAG0011	D1004-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000001	CN FJ G4 00 03A1000010 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
20	LGAG0011	D1004-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000002	CN FJ G4 00 03A1000010 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
21	LGAG0012	G1804-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H200.1G4B/上海新动力汽车科技股份有限公司 147/2200	CN FC G4 00 0293000396 000001	CN FJ G4 00 03A1000011 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
22	LGAG0012	G1804-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H200.1G4B/上海新动力汽车科技股份有限公司 147/2200	CN FC G4 00 0293000396 000002	CN FJ G4 00 03A1000011 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
23	LGAG0013	G2604-6PA	G403A1FJ2SCH05	G4SDECL0715H05	SC7H290G4/上海新动力汽车科技股份有限公司 213.5/2200	CN FC G4 00 0293000151 000001	CN FJ G4 00 03A1000012 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
24	LGAG0008	G2404-6PA	G403A1FJ2SCH05	G4SDECL0715H05	SC7H260.5G4/上海新动力汽车科技股份有限公司 191.5/2200	CN FC G4 00 0293000154 000001	CN FJ G4 00 03A1000013 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
25	LGAG0014	G2604-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF280G4/上海新动力汽车科技股份有限公司 206/2200	CN FC G4 00 0293000020 000001	CN FJ G4 00 03A1000014 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
26	LGAG0014	G2604-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF280G4/上海新动力汽车科技股份有限公司 206/2200	CN FC G4 00 0293000020 000002	CN FJ G4 00 03A1000014 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
27	LGAG0015	G2804-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF300G4/上海新动力汽车科技股份有限公司 221/2200	CN FC G4 00 0293000008 000001	CN FJ G4 00 03A1000015 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
28	LGAG0015	G2804-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF300G4/上海新动力汽车科技股份有限公司 221/2200	CN FC G4 00 0293000008 000002	CN FJ G4 00 03A1000015 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
29	LGAG0016	G3004-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF320G4/上海新动力汽车科技股份有限公司 235.5/2200	CN FC G4 00 0293000007 000001	CN FJ G4 00 03A1000016 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
30	LGAG0016	G3004-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF320G4/上海新动力汽车科技股份有限公司 235.5/2200	CN FC G4 00 0293000007 000002	CN FJ G4 00 03A1000016 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
31	LGAG0003	D1054-4P	G403A1FJ2QCC01	G40602L0362C01	V38-120C41/安徽全柴动力股份有限公司 89/2400	CN FC G4 00 0602000217 000001	CN FJ G4 00 03A1000003 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
32	LGAG0003	D1054-4P	G403A1FJ2QCC01	G40602L0362C01	V38-120C41/安徽全柴动力股份有限公司 89/2400	CN FC G4 00 0602000217 000002	CN FJ G4 00 03A1000003 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
33	LGAG0016	G3004-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司 250/2200	CN FC G4 00 0293000184 000001	CN FJ G4 00 03A1000016 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
34	LGAG0003	D1054-4P	G403A1FJ2QCC01	G40602L0362C01	V38-120C41/安徽全柴动力股份有限公司 89/2400	CN FC G4 00 0293000184 000002	CN FJ G4 00 03A1000016 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
35	LGAG0016	G3004-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司 250/2200	CN FC G4 00 0293000007 000001	CN FJ G4 00 03A1000015 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
36	LGAG0016	G3004-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司 250/2200	CN FC G4 00 0293000007 000002	CN FJ G4 00 03A1000015 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
37	LGAG0014	G2604-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF300G4/上海新动力汽车科技股份有限公司 221/2200	CN FC G4 00 0293000008 000001	CN FJ G4 00 03A1000014 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
38	LGAG0014	G2604-6P	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF300G4/上海新动力汽车科技股份有限公司 221/2200	CN FC G4 00 0293000008 000002	CN FJ G4 00 03A1000014 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
39	LGAG0017	LG2104-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H230.1G4B/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000388 000001	CN FJ G4 00 03A1000017 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
40	LGAG0017	LG2104-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H230.1G4B/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000388 000002	CN FJ G4 00 03A1000017 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
41	LGAG0017	LG2104-6M	G403A1FJ2SCH01	G4SDECL0644H01	SC7H230G4/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000034 000001	CN FJ G4 00 03A1000017 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
42	LGAG0017	LG2104-6M	G403A1FJ2SCH01	G4SDECL0644H01	SC7H230G4/上海新动力汽车科技股份有限公司 169/2200	CN FC G4 00 0293000034 000002	CN FJ G4 00 03A1000017 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
43	LGAG0018	LG1804-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H200.1G4B/上海新动力汽车科技股份有限公司 147/2200	CN FC G4 00 0293000396 000001	CN FJ G4 00 03A1000018 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
44	LGAG0018	LG1804-6M	G403A1FJ2SCH12	G4SDECL0644H12	SC7H200.1G4B/上海新动力汽车科技股份有限公司 147/2200	CN FC G4 00 0293000396 000002	CN FJ G4 00 03A1000018 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
45	LGAG0019	LG1204-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000216 000001	CN FJ G4 00 03A1000019 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
46	LGAG0019	LG1204-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000266 000001	CN FJ G4 00 03A1000019 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
47	LGAG0020	LG1004-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000001	CN FJ G4 00 03A1000020 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
48	LGAG0020	LG1004-4MA	G403A1FJ2QCC01	G40602L0362C01	V38-110C43/安徽全柴动力股份有限公司 81/2300	CN FC G4 00 0602000222 000002	CN FJ G4 00 03A1000020 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
49	LGAG0001	D1204-4P	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000266 000001	CN FJ G4 00 03A1000001 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
50	LGAG0005	D1204-4H	G403A1FJ2QCC01	G40602L0362C01	V38-130C43/安徽全柴动力股份有限公司 96/2300	CN FC G4 00 0602000266 000001	CN FJ G4 00 03A1000005 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
51	LGAG0013	G2604-6PA	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF300G4/上海新动力汽车科技股份有限公司 221/2200	CN FC G4 00 0293000008 000001	CN FJ G4 00 03A1000012 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
52	LGAG0013	G2604-6PA	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF300G4/上海新动力汽车科技股份有限公司 221/2200	CN FC G4 00 0293000008 000002	CN FJ G4 00 03A1000012 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
53	LGAG0008	G2404-6PA	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF280G4/上海新动力汽车科技股份有限公司 206/2200	CN FC G4 00 0293000020 000001	CN FJ G4 00 03A1000013 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
54	LGAG0008	G2404-6PA	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF280G4/上海新动力汽车科技股份有限公司 206/2200	CN FC G4 00 0293000020 000002	CN FJ G4 00 03A1000013 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
55	LGAG0001	D1204-4P	G403A1FJ2SCH03	G4SDECL0429H03	SC4H135.3G4/上海新动力汽车科技股份有限公司 99.3/2300	CN FC G4 00 0293000103 000001	CN FJ G4 00 03A1000001 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
56	LGAG0001	D1204-4P	G403A1FJ2SCH03	G4SDECL0429H03	SC4H135.3G4/上海新动力汽车科技股份有限公司 99.3/2300	CN FC G4 00 0293000103 000002	CN FJ G4 00 03A1000001 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
57	LGAG0019	LG1204-4MA	G403A1FJ2SCH03	G4SDECL0429H03	SC4H135.3G4/上海新动力汽车科技股份有限公司 99.3/2300	CN FC G4 00 0293000103 000001	CN FJ G4 00 03A1000019 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
58	LGAG0019	LG1204-4MA	G403A1FJ2SCH03	G4SDECL0429H03	SC4H135.3G4/上海新动力汽车科技股份有限公司 99.3/2300	CN FC G4 00 0293000103 000002	CN FJ G4 00 03A1000019 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
59	LGAG0005	D1204-4H	G403A1FJ2SCH03	G4SDECL0429H03	SC4H135.3G4/上海新动力汽车科技股份有限公司 99.3/2300	CN FC G4 00 0293000103 000001	CN FJ G4 00 03A1000005 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
60	LGAG0005	D1204-4H	G403A1FJ2SCH03	G4SDECL0429H03	SC4H135.3G4/上海新动力汽车科技股份有限公司 99.3/2300	CN FC G4 00 0293000103 000002	CN FJ G4 00 03A1000005 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
61	LGAG0002	D1004-4P	G403A1FJ2SCH03	G4SDECL0429H03	SC4H115.3G4/上海新动力汽车科技股份有限公司 85/2300	CN FC G4 00 0293000113 000001	CN FJ G4 00 03A1000002 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
62	LGAG0002	D1004-4P	G403A1FJ2SCH03	G4SDECL0429H03	SC4H115.3G4/上海新动力汽车科技股份有限公司 85/2300	CN FC G4 00 0293000113 000002	CN FJ G4 00 03A1000002 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
63	LGAG0007	D1004-4H	G403A1FJ2SCH03	G4SDECL0429H03	SC4H115.3G4/上海新动力汽车科技股份有限公司 85/2300	CN FC G4 00 0293000113 000001	CN FJ G4 00 03A1000007 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
64	LGAG0007	D1004-4H	G403A1FJ2SCH03	G4SDECL0429H03	SC4H115.3G4/上海新动力汽车科技股份有限公司 85/2300	CN FC G4 00 0293000113 000002	CN FJ G4 00 03A1000007 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
65	LGAG0020	LG1004-4MA	G403A1FJ2SCH03	G4SDECL0429H03	SC4H115.3G4/上海新动力汽车科技股份有限公司 85/2300	CN FC G4 00 0293000113 000001	CN FJ G4 00 03A1000020 000003	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
66	LGAG0020	LG1004-4MA	G403A1FJ2SCH03	G4SDECL0429H03	SC4H115.3G4/上海新动力汽车科技股份有限公司 85/2300	CN FC G4 00 0293000113 000002	CN FJ G4 00 03A1000020 000004	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
67	LGAG0021	T3204-7E	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司250/2200	CN FC G4 00 0293000184 000002	CN FJ G4 00 03A1000021 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
68	LGAG0021	T3204-7E	G403A1FJ2SCD01	G4SDECL0882D01	SC9DF340G4/上海新动力汽车科技股份有限公司250/2200	CN FC G4 00 0293000184 000001	CN FJ G4 00 03A1000021 000002	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
69	LGAG0022	4YL-12	G403A1FJ2YCF70	G4GYMCL1098F70	YCK11480-T400/广西玉柴机器股份有限公司353/2200	CN FC G4 00 0158000217 000001	CN FJ G4 00 03A1000022 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
70	LGAG0023	F1604-5PA	G403A1FJ2SCH04	G4SDECL0454H04	SC4H175.5G4/上海新动力汽车科技股份有限公司\n    129/2200	CN FC G4 00 0293000131 000001	CN FJ G4 00 03A1000024 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
71	LGAG0024	F1604-5P	G403A1FJ2SCH10	G4SDECL0644H10	SC7H180G4B/上海新动力汽129/2200	CN FC G4 00 0293000334 000001	CN FJ G4 00 03A1000030 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
72	LGAG0025	F1800-5PB	G403A1FJ2SCH12	G4SDECL0644H12	SC7H200.1G4B/上海新动力汽车科技股份有限公司\n    147/2200	CN FC G4 00 0293000396 000001	CN FJ G4 00 03A1000025 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
73	LGAG0026	F1600-5PB	G403A1FJ2SCH10	G4SDECL0644H10	SC7H180G4B/上海新动力汽129/2200	CN FC G4 00 0293000334 000001	CN FJ G4 00 03A1000026 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
74	LGAG0027	F1400-5PB	G403A1FJ2SCH10	G4SDECL0644H10	SC7H160G4B/上海新动力汽车科技股份有限公司\n    118/2200	CN FC G4 00 0293000342 000001	CN FJ G4 00 03A1000027 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
75	LGAG0028	F1404-5PA	G403A1FJ2SCH04	G4SDECL0454H04	SC4H160.5G4/上海新动力汽车科技股份有限公司\n    118/2200	CN FC G4 00 0293000133 000001	CN FJ G4 00 03A1000028 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
76	LGAG0029	F1404-5P	G403A1FJ2SCH10	G4SDECL0644H10	SC7H160G4B/上海新动力汽车科技股份有限公司\n    118/2200	CN FC G4 00 0293000342 000001	CN FJ G4 00 03A1000029 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
77	LGAG0030	G2604-6MA	G403A1FJ2SCY01	G4SDECL0793Y01	SC8Y290.1G4/上海新动力汽车科技股份有限公司\n    118/2200	CN FC G4 00 0293000447 000001	CN FJ G4 00 03A1000031 000001	admin	admin	2026-03-27 17:21:38.747135	\N	\N	\N	1
\.


--
-- Data for Name: nameplate_record; Type: TABLE DATA; Schema: micro_product_assistant; Owner: postgres
--

COPY micro_product_assistant.nameplate_record (id, model_code, vin, factory_date, environmental_public_number, engine_model, engine_manufacturer, rated_power, fuel_injection_system_type, environmental_key_parts, execution_standard, engine_serial_number, qr_code, qr_url, sim_card_status, pay_status, status, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, engine_public_number, qr_raw_url) FROM stdin;
5428	D1204-4P	3A12100J1T40U0H0W	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000001	V38-130C43	安徽全柴动力股份有限公司	300	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	63622222222	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 11:49:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:39:30.318745	2	\N	\N
5397	D1204-4P	3A12100JXS4J0JN0N	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H9259032850	JJ2C98fKrPQG	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29095758634662.png	1	1	2	102508012	陈艺文	2026-03-29 09:56:25.254268	102508012	陈艺文	2026-04-01 16:26:54.076725	4	\N	http://nj2wm.cn/JJ2C98fKrPQG/oJCo
5398	LG2104-6M	3A1210JPXT40L0WUZ	2026-03-28 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008285	JJ2CM1G0LcNC	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29134604321803.png	1	1	2	102508012	陈艺文	2026-03-29 10:35:23	102508012	陈艺文	2026-03-29 13:47:31.249843	4	\N	http://nj2wm.cn/JJ2CM1G0LcNC/cHAL
5402	LG2104-6M	3A1210JP4T40L0USJ	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007695	JJ2CCaPJtVj1	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29134604189881.png	1	1	2	102508012	陈艺文	2026-03-29 10:59:54.531709	102508012	陈艺文	2026-03-29 13:47:31.251785	3	\N	http://nj2wm.cn/JJ2CCaPJtVj1/0ujQ
5403	LG2104-6M	3A1210JP2T40SJJSJ	2026-04-01 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007293	JJ2CM9zLWCe9	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29134546438336.png	1	1	2	102508012	陈艺文	2026-03-29 11:00:38.350054	102508012	陈艺文	2026-03-29 13:47:31.252171	3	\N	http://nj2wm.cn/JJ2CM9zLWCe9/ginx
5404	LG2104-6M	3A1210JP4T40SSSNU	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008280	JJ2CvSshWUo5	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111943355480.png	1	1	2	102508012	陈艺文	2026-03-30 10:49:54.451434	102508012	陈艺文	2026-03-30 14:30:07.167501	3	\N	http://nj2wm.cn/JJ2CvSshWUo5/Uaib
5405	LG2104-6M	3A1210JP6T40L0WN0	2026-04-01 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008077	JJ2CpwIDIRe0	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111940439574.png	1	1	2	102508012	陈艺文	2026-03-30 10:50:49.582716	102508012	陈艺文	2026-03-30 14:30:07.171746	3	\N	http://nj2wm.cn/JJ2CpwIDIRe0/4FCw
5399	LG2104-6M	3A1210JP6T40LJLUN	2026-04-25 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263012608	JJ2CG71RBqJc	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29134604796235.png	1	1	2	102508012	陈艺文	2026-03-29 10:36:17	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 10:38:33.532691	5	\N	http://nj2wm.cn/JJ2CG71RBqJc/RkD7
5400	LG2104-6M	3A1210JP8T40LJLUW	2026-04-25 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263012451	JJ2CkrHJAAWz	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29134604945963.png	1	1	2	102508012	陈艺文	2026-03-29 10:58:32.094118	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 10:38:33.538147	4	\N	http://nj2wm.cn/JJ2CkrHJAAWz/BFMg
5401	LG2104-6M	3A1210JP6T40L0USS	2026-04-25 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008295	JJ2C0gvifjiO	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/29134604703882.png	1	1	2	102508012	陈艺文	2026-03-29 10:59:17.302387	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 10:38:33.541081	4	\N	http://nj2wm.cn/JJ2C0gvifjiO/634G
5422	F1404-5P	3A1210SZ9T40U0H0J	2026-03-29 00:00:00	CN FJ G4 00 03A1000029 000001	SC7H160G4B	安徽全柴动力股份有限公司	33	高压共轨	DOC、DPF、EGR	GB/T15370.1-2012	HG000000001	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:32:57.951499	\N	\N	\N	1	\N	\N
5423	D1204-4P	3A12100J2T40U0H0J	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000001	V38-130C43	安徽全柴动力股份有限公司	88	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	V3801111111	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:34:19.035141	\N	\N	\N	1	\N	\N
5424	D1204-4P	3A12100J4T40U0H0S	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000001	V38-130C43	安徽全柴动力股份有限公司	300	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	V3333333333	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:35:10.603457	\N	\N	\N	1	\N	\N
5426	D1204-4P	3A12100J8T40U0H0U	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	600	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	65566666666	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:37:17	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:42:11.193001	5	\N	\N
5425	D1204-4P	3A12101J6T40U0H0L	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000001	V38-130C43	安徽全柴动力股份有限公司	666	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	55555555555	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:36:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 14:06:37.447699	2	\N	\N
5421	D1204-4P	3A12100J9T40U0P0J	\N	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251194527V	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-07 16:28:05	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-07 16:34:30.675134	2	CN FC G4 00 0602000266 000001	\N
5427	D1204-4P	3A12100JXT40U0H0N	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000001	V38-130C43	安徽全柴动力股份有限公司	300	高压共轨	DOC、DPF、EGR	GB/T15370.1-2012	36222222222	\N	\N	1	1	0	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 11:48:29	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:39:19.09331	2	\N	\N
5406	LG2104-6M	3A1210JP3T40SSNHU	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262009455	JJ2CibF6tuVE	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111932540384.png	1	1	2	102508012	陈艺文	2026-03-30 10:52:08.823005	102508012	陈艺文	2026-03-30 14:30:07.172854	3	\N	http://nj2wm.cn/JJ2CibF6tuVE/ton2
5407	LG2104-6M	3A1210JP1T40S0NNP	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006573	JJ2CLEty8Vcs	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111927248038.png	1	1	2	102508012	陈艺文	2026-03-30 10:53:30.156672	102508012	陈艺文	2026-03-30 14:30:07.174856	3	\N	http://nj2wm.cn/JJ2CLEty8Vcs/T64Y
5408	D1204-4P	3A12100J2S4JS0HLZ	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251196165V	JJ2CgQ2LULIr	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111950436938.png	1	1	2	102508012	陈艺文	2026-03-30 10:54:42.110726	102508012	陈艺文	2026-03-30 14:30:07.175822	3	\N	http://nj2wm.cn/JJ2CgQ2LULIr/Zw9V
5409	LG2104-6M	3A1210JP8T40SSSNW	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008288	JJ2C4ODlqy4q	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111913824621.png	1	1	2	102508012	陈艺文	2026-03-30 10:55:44.770717	102508012	陈艺文	2026-03-30 14:30:07.176683	3	\N	http://nj2wm.cn/JJ2C4ODlqy4q/gN4e
5410	D1204-4P	3A12100J8S40USHSW	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q241190617V	JJ2C2Vd2fFyT	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30112008222321.png	1	1	2	102508012	陈艺文	2026-03-30 11:01:55.450407	102508012	陈艺文	2026-03-30 14:30:07.177569	3	\N	http://nj2wm.cn/JJ2C2Vd2fFyT/zgJc
5411	D1204-4P	3A12100JXS4J0LJ0H	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H925A033365	JJ2CCfDVih5i	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30112001511579.png	1	1	2	102508012	陈艺文	2026-03-30 11:02:56.342674	102508012	陈艺文	2026-03-30 14:30:07.178351	3	\N	http://nj2wm.cn/JJ2CCfDVih5i/62qG
5412	D1204-4P	3A12100J1S4J0LJ0Z	2026-03-29 00:00:00	CN FJ G4 00 03A1000001 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H925A033619	JJ2CX9c9ryLL	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111956652786.png	1	1	2	102508012	陈艺文	2026-03-30 11:03:49.543422	102508012	陈艺文	2026-03-30 14:30:07.179797	3	\N	http://nj2wm.cn/JJ2CX9c9ryLL/U6I7
5413	LG2104-6M	3A1210JPXT40SSSW0	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005612	JJ2CVamPOD53	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/30111946918893.png	1	1	2	102508012	陈艺文	2026-03-30 11:04:52.044639	102508012	陈艺文	2026-03-30 14:30:07.179079	3	\N	http://nj2wm.cn/JJ2CVamPOD53/rd67
5414	LG2104-6M	3A1210JP8T40L0WNJ	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008278	JJ2CqkJejWZD	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722653790.png	1	1	2	102508012	陈艺文	2026-03-30 16:06:48.124288	102508012	陈艺文	2026-03-31 16:01:15.555708	4	\N	http://nj2wm.cn/JJ2CqkJejWZD/C7mW
5415	LG2104-6M	3A1210JP7T40L0PNU	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008081	JJ2CVI948uAb	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722528509.png	1	1	2	102508012	陈艺文	2026-03-30 16:07:41.107966	102508012	陈艺文	2026-03-31 16:01:15.556259	4	\N	http://nj2wm.cn/JJ2CVI948uAb/PdP5
5416	LG2104-6M	3A1210JP9T40L0PNN	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006046	JJ2C89DIvP4o	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722780593.png	1	1	2	102508012	陈艺文	2026-03-30 16:08:49.208093	102508012	陈艺文	2026-03-31 16:01:15.55651	4	\N	http://nj2wm.cn/JJ2C89DIvP4o/xVl2
5417	LG2104-6M	3A1210JP5T40L0PNL	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006033	JJ2CB7WcJWK5	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722559468.png	1	1	2	102508012	陈艺文	2026-03-30 16:09:55.672366	102508012	陈艺文	2026-03-31 16:01:15.556711	4	\N	http://nj2wm.cn/JJ2CB7WcJWK5/H0EG
5418	LG2104-6M	3A1210JP0T40L0PNW	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005640	JJ2CQcUt8ysJ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722409644.png	1	1	2	102508012	陈艺文	2026-03-30 16:10:39.101384	102508012	陈艺文	2026-03-31 16:01:15.556913	4	\N	http://nj2wm.cn/JJ2CQcUt8ysJ/qvcs
5419	LG2104-6M	3A1210JPXT40LJLUP	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263012613	JJ2CDIjDTgdX	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722852364.png	1	1	2	102508012	陈艺文	2026-03-30 16:11:18.368196	102508012	陈艺文	2026-04-09 11:22:08.020453	5	\N	http://nj2wm.cn/JJ2CDIjDTgdX/AgK6
5420	LG2104-6M	3A1210JP6T40L0Z0P	2026-04-01 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007302	JJ2CqNXYz1AC	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154722605257.png	1	1	2	102508012	陈艺文	2026-03-30 16:12:08.189869	102508012	陈艺文	2026-03-31 16:01:15.557336	4	\N	http://nj2wm.cn/JJ2CqNXYz1AC/zdsS
5429	LG2104-6M	3A1210JPXT40SSJUN	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008287	JJ2CwNrQad0O	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725240485.png	1	1	2	102508012	陈艺文	2026-03-31 14:55:18.491247	102508012	陈艺文	2026-03-31 16:01:15.558736	4	\N	http://nj2wm.cn/JJ2CwNrQad0O/sL6e
5430	LG2104-6M	3A1210JP3T40SSJUP	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007700	JJ2CfMRaz6NH	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725533288.png	1	1	2	102508012	陈艺文	2026-03-31 14:55:58.620572	102508012	陈艺文	2026-03-31 16:01:15.558979	4	\N	http://nj2wm.cn/JJ2CfMRaz6NH/FxYC
5431	LG2104-6M	3A1210JP5T40LJWWJ	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263012611	JJ2CMuq05FrY	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725176004.png	1	1	2	102508012	陈艺文	2026-03-31 15:00:11.917202	102508012	陈艺文	2026-03-31 16:01:15.559199	4	\N	http://nj2wm.cn/JJ2CMuq05FrY/7Gmo
5433	LG2104-6M	3A1210JP1T40SSSNH	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005635	JJ2CZYS2hb5P	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725491046.png	1	1	2	102508012	陈艺文	2026-03-31 15:03:03.012459	102508012	陈艺文	2026-03-31 16:01:15.55939	4	\N	http://nj2wm.cn/JJ2CZYS2hb5P/OsLS
5434	LG2104-6M	3A1210JP0T40SSL0S	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008275	JJ2CnbDNqX3t	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725263570.png	1	1	2	102508012	陈艺文	2026-03-31 15:04:03.87422	102508012	陈艺文	2026-03-31 16:01:15.559572	4	\N	http://nj2wm.cn/JJ2CnbDNqX3t/euVl
5435	LG2104-6M	3A1210JP9T40SSL0J	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007121	JJ2CSMHqjnSU	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725349248.png	1	1	2	102508012	陈艺文	2026-03-31 15:04:51.303329	102508012	陈艺文	2026-03-31 16:01:15.559752	4	\N	http://nj2wm.cn/JJ2CSMHqjnSU/kQVj
5436	LG2104-6M	3A1210JP6T40SSSNN	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008286	JJ2CgGp8MHll	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725472722.png	1	1	2	102508012	陈艺文	2026-03-31 15:06:04.790555	102508012	陈艺文	2026-03-31 16:01:15.559983	4	\N	http://nj2wm.cn/JJ2CgGp8MHll/Z4ZM
5437	LG2104-6M	3A1210JPXT40SSSNP	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008296	JJ2CEXnRBVTP	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154725274508.png	1	1	2	102508012	陈艺文	2026-03-31 15:07:36.973168	102508012	陈艺文	2026-03-31 16:01:15.560198	4	\N	http://nj2wm.cn/JJ2CEXnRBVTP/eCeO
5438	LG2104-6M	3A1210JP1T40SSSWJ	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007113	JJ2CeoAZNoJT	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154728417369.png	1	1	2	102508012	陈艺文	2026-03-31 15:08:12.513088	102508012	陈艺文	2026-03-31 16:01:15.560389	4	\N	http://nj2wm.cn/JJ2CeoAZNoJT/kGiB
5439	LG2104-6M	3A1210JP0T40L0LJH	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006580	JJ2Cpg65ir6B	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154728242138.png	1	1	2	102508012	陈艺文	2026-03-31 15:11:52.963355	102508012	陈艺文	2026-03-31 16:01:15.560583	4	\N	http://nj2wm.cn/JJ2Cpg65ir6B/BtZE
5440	LG2104-6M	3A1210JP2T40L0LJZ	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262006913	JJ2CzYWew06N	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154728790268.png	1	1	2	102508012	陈艺文	2026-03-31 15:12:32.625496	102508012	陈艺文	2026-03-31 16:01:15.560791	4	\N	http://nj2wm.cn/JJ2CzYWew06N/CV6W
5441	LG2104-6M	3A1210JP2T40L0US0	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007684	JJ2CcJvrxnoT	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154728934642.png	1	1	2	102508012	陈艺文	2026-03-31 15:13:11.056868	102508012	陈艺文	2026-03-31 16:01:15.560994	4	\N	http://nj2wm.cn/JJ2CcJvrxnoT/UcL7
5442	LG2104-6M	3A1210JP4T40SJJSS	2026-04-01 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007124	JJ2CaIkYrbD6	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154728792460.png	1	1	2	102508012	陈艺文	2026-03-31 15:13:53.309835	102508012	陈艺文	2026-03-31 16:01:15.561192	4	\N	http://nj2wm.cn/JJ2CaIkYrbD6/hyTK
5443	LG2104-6M	3A1210JP5T40LJJSH	2026-03-30 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263011721	JJ2ClVIXZKjB	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/26145705952298.png	1	1	2	102508012	陈艺文	2026-03-31 15:14:44.040294	102508012	陈艺文	2026-03-31 16:01:15.561368	4	\N	http://nj2wm.cn/JJ2ClVIXZKjB/jdSl
5445	D1204-4P	3A12100J7S4JJS0NW	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250792528V	JJ2C4Bu83gq6	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/01/30112503650565.png	1	1	2	102508012	陈艺文	2026-03-31 15:30:08.693279	102508012	陈艺文	2026-03-31 16:01:15.561546	4	\N	http://nj2wm.cn/JJ2C4Bu83gq6/QUcL
5446	LG2104-6M	3A1210JP8T40L0LJJ	2026-03-31 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261001325	JJ2CKxft504x	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31154728148561.png	1	1	2	102508012	陈艺文	2026-03-31 15:31:52.999933	102508012	陈艺文	2026-03-31 16:01:15.561722	4	\N	http://nj2wm.cn/JJ2CKxft504x/cuni
5447	D1204-4P	3A12100JXS40H0J0W	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490596V	JJ2Cwm1myAt6	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155856525876.png	1	1	2	102508012	陈艺文	2026-03-31 15:32:41.617613	102508012	陈艺文	2026-03-31 16:01:15.561954	4	\N	http://nj2wm.cn/JJ2Cwm1myAt6/4PZS
5448	D1204-4P	3A12100J6S40PLJ0N	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490593V	JJ2Cve8jofWe	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155856645746.png	1	1	2	102508012	陈艺文	2026-03-31 15:35:20.671326	102508012	陈艺文	2026-03-31 16:01:15.562162	4	\N	http://nj2wm.cn/JJ2Cve8jofWe/Gz46
5449	D1204-4P	3A12100J2S40HJUJL	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490554V	JJ2CxoFlb83J	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155856619919.png	1	1	2	102508012	陈艺文	2026-03-31 15:36:07.201718	102508012	陈艺文	2026-03-31 16:01:15.562361	4	\N	http://nj2wm.cn/JJ2CxoFlb83J/mebF
5450	D1204-4P	3A12100J9S4JS0JJS	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490539V	JJ2CPPUiFUn4	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155856914648.png	1	1	2	102508012	陈艺文	2026-03-31 15:36:37.353582	102508012	陈艺文	2026-03-31 16:01:15.562575	4	\N	http://nj2wm.cn/JJ2CPPUiFUn4/Rf7K
5451	D1204-4P	3A12100J0S4JS0JJL	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490560V	JJ2CZJRjUNEi	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155856543094.png	1	1	2	102508012	陈艺文	2026-03-31 15:37:14.165268	102508012	陈艺文	2026-03-31 16:01:15.562774	4	\N	http://nj2wm.cn/JJ2CZJRjUNEi/96ph
5453	D1204-4P	3A12100JXS4JS0JSJ	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490550V	JJ2CbXU9Lfnc	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155835337729.png	1	1	2	102508012	陈艺文	2026-03-31 15:38:27.222515	102508012	陈艺文	2026-03-31 16:01:15.563188	4	\N	http://nj2wm.cn/JJ2CbXU9Lfnc/KZoP
5454	F1800-5PB	3A1210SN8T40J0L0J	2025-06-11 00:00:00	CN FJ G4 00 03A1000025 000001	SC7H200.1G4B	上海新动力汽车科技股份有限公司	132.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2012	H925C003962	JJ2C9ewBtcsh	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04175209100688.png	1	1	2	102508012	陈艺文	2026-04-02 09:57:04.979323	102508012	陈艺文	2026-04-04 17:53:18.452988	3	\N	http://nj2wm.cn/JJ2C9ewBtcsh/UcIs
5455	LG2104-6M	3A1210JP8T40L0PWL	2026-04-04 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006028	JJ2CTp4giN0H	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04150304945625.png	1	1	2	102508012	陈艺文	2026-04-04 14:36:12.575357	102508012	陈艺文	2026-04-04 15:07:04.077396	3	\N	http://nj2wm.cn/JJ2CTp4giN0H/WCkL
5456	LG2104-6M	3A1210JPXT40L0PWU	2026-04-04 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262009082	JJ2CakhTXswB	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04150246457731.png	1	1	2	102508012	陈艺文	2026-04-04 14:37:03.956417	102508012	陈艺文	2026-04-04 15:07:04.077175	3	\N	http://nj2wm.cn/JJ2CakhTXswB/jwjp
5457	LG1204-4MA	3A1210JZ4T40J0U0J	2026-04-04 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251094800V	JJ2Cy4lA8Qgh	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04150203931857.png	1	1	2	102508012	陈艺文	2026-04-04 14:41:59.630333	102508012	陈艺文	2026-04-04 15:07:04.076849	3	\N	http://nj2wm.cn/JJ2Cy4lA8Qgh/1EVo
5458	LG1204-4MA	3A1210JZ4T40J0W0H	2026-04-04 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251093108V	JJ2Ct9Kaihyy	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04150203573513.png	1	1	2	102508012	陈艺文	2026-04-04 14:42:42.942568	102508012	陈艺文	2026-04-04 15:07:04.076271	3	\N	http://nj2wm.cn/JJ2Ct9Kaihyy/zGln
5459	LG2104-6M	3A1210JP6T40L0PWS	2026-04-04 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006038	JJ2CLzKTDtB0	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04175204583176.png	1	1	2	102508012	陈艺文	2026-04-04 17:44:05.36205	102508012	陈艺文	2026-04-04 17:53:18.452114	3	\N	http://nj2wm.cn/JJ2CLzKTDtB0/t1y1
5460	D1204-4P	3A12100J8S4JJSZWZ	2026-04-04 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251092280V	JJ2CSE5myWDQ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04175203584301.png	1	1	2	102508012	陈艺文	2026-04-04 17:45:45.764469	102508012	陈艺文	2026-04-04 17:53:18.452416	3	\N	http://nj2wm.cn/JJ2CSE5myWDQ/5Is9
5461	D1204-4P	3A12100J4S4JJSZWP	2026-04-04 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251196155V	JJ2CsJC465RA	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04175204637659.png	1	1	2	102508012	陈艺文	2026-04-04 17:46:41.427306	102508012	陈艺文	2026-04-04 17:53:18.452723	3	\N	http://nj2wm.cn/JJ2CsJC465RA/MO7n
5462	D1204-4P	3A12100J1S4JJSHWS	2026-04-04 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251092275V	JJ2C5hUwUjRS	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04175204783955.png	1	1	2	102508012	陈艺文	2026-04-04 17:47:44.721657	102508012	陈艺文	2026-04-04 17:53:18.451257	3	\N	http://nj2wm.cn/JJ2C5hUwUjRS/D7pB
5463	LG2104-6M	3A1210JP4T40LJUNN	2026-04-04 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263012607	JJ2CBTxo4eMG	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/04175204324336.png	1	1	2	102508012	陈艺文	2026-04-04 17:49:16.172548	102508012	陈艺文	2026-04-04 17:53:18.451766	3	\N	http://nj2wm.cn/JJ2CBTxo4eMG/P5Vh
5464	LG2104-6M	3A1210JP1T40SSNHL	2026-03-29 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262009452	JJ2CyORRpat4	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/05121204439899.png	1	1	2	102508012	陈艺文	2026-04-05 12:04:23.749371	102508012	陈艺文	2026-04-05 12:21:25.256523	3	\N	http://nj2wm.cn/JJ2CyORRpat4/CTFI
5465	T3004-7E	3A1210SLXT40SJ0SU	2026-04-05 00:00:00	CN FJ G4 00 03A1000023 000002	SC9DF340G4	上海新动力汽车科技股份有限公司	220.5	高压共轨	DOC、DPF、SCR	GB/T15370.3-2025	D9261000470	JJ2ClYC66CR7	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/05121209122915.png	1	1	2	102508012	陈艺文	2026-04-05 12:09:12.634654	102508012	陈艺文	2026-04-05 12:21:25.257179	3	\N	http://nj2wm.cn/JJ2ClYC66CR7/h8Uz
5466	T3004-7E	3A1210SL0T40S0HJZ	2026-04-05 00:00:00	CN FJ G4 00 03A1000023 000002	SC9DF340G4	上海新动力汽车科技股份有限公司	220.5	高压共轨	DOC、DPF、SCR	GB/T15370.3-2025	D9261000312	JJ2CTUp8x2Jh	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/05121209663959.png	1	1	2	102508012	陈艺文	2026-04-05 12:10:08.265737	102508012	陈艺文	2026-04-07 15:14:13.573821	3	\N	http://nj2wm.cn/JJ2CTUp8x2Jh/vO9p
5467	LG2104-6M	3A1210JP5T40S0SSL	2026-04-07 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005623	JJ2CO5qlD5a2	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/07151020957863.png	1	1	2	102508012	陈艺文	2026-04-07 15:00:17.003151	102508012	陈艺文	2026-04-07 15:14:13.574325	3	\N	http://nj2wm.cn/JJ2CO5qlD5a2/9AzB
5468	LG2104-6M	3A1210JPXT40S0SS0	2026-04-07 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261002632	JJ2CuDl1KfQd	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/07151020304813.png	1	1	2	102508012	陈艺文	2026-04-07 15:01:07.455584	102508012	陈艺文	2026-04-07 15:14:13.574601	3	\N	http://nj2wm.cn/JJ2CuDl1KfQd/HDOv
5469	D1204-4P	3A12100J3T40JSJ0L	2026-04-07 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q260190475V	JJ2C2aWJmPjS	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/07151004171992.png	1	1	2	102508012	陈艺文	2026-04-07 15:01:56.450459	102508012	陈艺文	2026-04-07 15:14:13.574817	3	\N	http://nj2wm.cn/JJ2C2aWJmPjS/RJlv
5470	D1204-4P	3A12100JXT40JSJ0J	2026-04-07 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q260190484V	JJ2CC3KnQLRO	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/07151004355539.png	1	1	2	102508012	陈艺文	2026-04-07 15:02:57.077791	102508012	陈艺文	2026-04-07 15:14:13.575125	3	\N	http://nj2wm.cn/JJ2CC3KnQLRO/8rxo
5473	D1204-4P	3A12100J1S4J0SL0S	2026-04-08 00:00:00	CN FJ G4 00 03A1000001 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H9259032843	JJ2CrGr0HNjQ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/08102904310402.png	1	1	2	102508012	陈艺文	2026-04-08 10:24:01.856193	102508012	陈艺文	2026-04-08 10:31:04.692601	3	\N	http://nj2wm.cn/JJ2CrGr0HNjQ/6nfj
5474	D1204-4P	3A12100J3S4J0SP0W	2026-04-08 00:00:00	CN FJ G4 00 03A1000001 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H9259032833	JJ2CChf8wMRQ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/08102904586877.png	1	1	2	102508012	陈艺文	2026-04-08 10:24:41.125266	102508012	陈艺文	2026-04-08 10:31:04.69329	3	\N	http://nj2wm.cn/JJ2CChf8wMRQ/TT8a
5475	LG2104-6M	3A1210JP4T40L0PWJ	2026-04-08 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005858	JJ2CUtwOFTfW	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/08102917449719.png	1	1	2	102508012	陈艺文	2026-04-08 10:26:18.537788	102508012	陈艺文	2026-04-08 10:31:04.693064	3	\N	http://nj2wm.cn/JJ2CUtwOFTfW/1VbZ
5476	LG1204-4MA	3A1210JZ9T40J0W0N	2026-04-08 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	Q251094791V	JJ2CayNT547D	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/08102904855919.png	1	1	2	102508012	陈艺文	2026-04-08 10:27:09.474769	102508012	陈艺文	2026-04-08 10:31:04.693467	3	\N	http://nj2wm.cn/JJ2CayNT547D/eweN
5477	LG2104-6M	3A1210JP1T40L0PWN	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008097	JJ2CMAAx74kO	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904426476.png	1	1	2	102508012	陈艺文	2026-04-09 11:07:41.944487	102508012	陈艺文	2026-04-09 11:22:08.016202	3	\N	http://nj2wm.cn/JJ2CMAAx74kO/92T7
5478	LG2104-6M	3A1210JP2T40L0HPS	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006027	JJ2CAJIrukNv	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904727924.png	1	1	2	102508012	陈艺文	2026-04-09 11:08:38.223916	102508012	陈艺文	2026-04-09 11:22:08.01985	3	\N	http://nj2wm.cn/JJ2CAJIrukNv/EQFS
5479	LG2104-6M	3A1210JP9T40S0Z0N	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007310	JJ2Cr3ugmYO7	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904167447.png	1	1	2	102508012	陈艺文	2026-04-09 11:09:22.486377	102508012	陈艺文	2026-04-09 11:22:08.02265	3	\N	http://nj2wm.cn/JJ2Cr3ugmYO7/ZBWv
5480	LG2104-6M	3A1210JP4T40L0HPL	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005835	JJ2Ckw4XdWt8	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904503720.png	1	1	2	102508012	陈艺文	2026-04-09 11:13:32.460711	102508012	陈艺文	2026-04-09 11:22:08.023183	3	\N	http://nj2wm.cn/JJ2Ckw4XdWt8/1zvy
5481	LG2104-6M	3A1210JP0T40L0HPJ	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008083	JJ2Cdz1OqtZ2	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904594833.png	1	1	2	102508012	陈艺文	2026-04-09 11:14:34.291144	102508012	陈艺文	2026-04-09 11:22:08.020974	3	\N	http://nj2wm.cn/JJ2Cdz1OqtZ2/cChU
5482	LG2104-6M	3A1210JP1T40L0HPP	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005843	JJ2C9LQoJYiz	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904214987.png	1	1	2	102508012	陈艺文	2026-04-09 11:15:13.73442	102508012	陈艺文	2026-04-09 11:22:08.021544	3	\N	http://nj2wm.cn/JJ2C9LQoJYiz/B1xX
5483	LG2104-6M	3A1210JP8T40L0HPN	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006026	JJ2CnKUQMgev	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904491767.png	1	1	2	102508012	陈艺文	2026-04-09 11:15:51.918157	102508012	陈艺文	2026-04-09 11:22:08.022135	3	\N	http://nj2wm.cn/JJ2CnKUQMgev/HAYy
5484	LG2104-6M	3A1210JPXT40L0HPW	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005614	JJ2CzF5oRVE2	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904361976.png	1	1	2	102508012	陈艺文	2026-04-09 11:16:29.276581	102508012	陈艺文	2026-04-09 11:22:08.023628	3	\N	http://nj2wm.cn/JJ2CzF5oRVE2/P48a
5485	LG2104-6M	3A1210JP3T40S0WPL	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006595	JJ2CAK6wagAN	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111904588932.png	1	1	2	102508012	陈艺文	2026-04-09 11:17:04.070242	102508012	陈艺文	2026-04-09 11:22:08.02409	3	\N	http://nj2wm.cn/JJ2CAK6wagAN/5Cj8
5486	LG2104-6M	3A1210JP1T40S0WPS	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006577	JJ2CmgpGbtkx	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09111846807426.png	1	1	2	102508012	陈艺文	2026-04-09 11:17:40.000416	102508012	陈艺文	2026-04-09 11:22:08.024519	3	\N	http://nj2wm.cn/JJ2CmgpGbtkx/G6qx
5487	LG1204-4MA	3A1210JZ8T40LSL0N	2026-04-09 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250995689V	JJ2C3zX1mBCI	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09155224804441.png	1	1	0	102508012	陈艺文	2026-04-09 15:41:46.227135	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-27 09:40:58.483294	4	\N	http://nj2wm.cn/JJ2C3zX1mBCI/OtIP
5472	D1204-4P	3A12100J1T40JJZ0N	2026-04-07 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q260190487V	\N	\N	1	1	0	102508012	陈艺文	2026-04-07 15:06:56.749898	102508012	陈艺文	2026-04-07 15:14:13.575552	3	\N	\N
5488	LG1204-4MA	3A1210JZ4T40LSL0L	2026-04-09 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250995688V	JJ2C1GwEKc5X	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09155224618024.png	1	1	2	102508012	陈艺文	2026-04-09 15:42:20.044539	102508012	陈艺文	2026-04-09 15:55:57.663797	3	\N	http://nj2wm.cn/JJ2C1GwEKc5X/6DgK
5489	LG2104-6M	3A1210JP3T40S0HZP	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007131	JJ2CgEZmcJLY	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09155304476131.png	1	1	2	102508012	陈艺文	2026-04-09 15:44:44.436982	102508012	陈艺文	2026-04-09 15:55:57.664367	3	\N	http://nj2wm.cn/JJ2CgEZmcJLY/3K11
5490	LG2104-6M	3A1210JP1T40S0HZW	2026-04-09 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007118	JJ2CVCNG6ZyQ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09155239210887.png	1	1	2	102508012	陈艺文	2026-04-09 15:45:29.792274	102508012	陈艺文	2026-04-09 15:55:57.664813	3	\N	http://nj2wm.cn/JJ2CVCNG6ZyQ/5uxs
5491	D1204-4P	3A12100J6T40JJ00U	2026-04-09 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q260190494V	JJ2CZyLNwASg	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09155204177479.png	1	1	2	102508012	陈艺文	2026-04-09 15:47:03.113422	102508012	陈艺文	2026-04-09 15:55:57.66526	3	\N	http://nj2wm.cn/JJ2CZyLNwASg/EmmO
5492	LG1204-4MA	3A1210JZ8T40J0ZJH	2026-04-09 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251094492V	JJ2CWNoivvOE	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/09155224878873.png	1	1	2	102508012	陈艺文	2026-04-09 15:47:53.760266	102508012	陈艺文	2026-04-09 15:55:57.665794	3	\N	http://nj2wm.cn/JJ2CWNoivvOE/idyu
5493	LG2104-6M	3A1210JP5T40S0WPU	2026-04-11 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261005626	JJ2CyXRbLJ92	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/11142409409921.png	1	1	2	102508012	陈艺文	2026-04-11 13:52:35.510543	102508012	陈艺文	2026-04-11 14:26:25.788972	3	\N	http://nj2wm.cn/JJ2CyXRbLJ92/zDHR
5494	LG2104-6M	3A1210JP7T40S0WPN	2026-04-11 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262006924	JJ2CZ3y8pis8	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/11142409567502.png	1	1	2	102508012	陈艺文	2026-04-11 13:53:05.784136	102508012	陈艺文	2026-04-11 14:26:25.789937	3	\N	http://nj2wm.cn/JJ2CZ3y8pis8/fdao
5495	LG1204-4MA	3A1210JZXT40LLJ0S	2026-04-12 00:00:00	CN FJ G4 00 03A1000019 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H9263032727	JJ2CKQcUaqBk	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/12195749654163.png	1	1	2	102508012	陈艺文	2026-04-12 19:55:26.142543	102508012	陈艺文	2026-04-12 19:58:13.868539	3	\N	http://nj2wm.cn/JJ2CKQcUaqBk/lz9q
5496	LG1204-4MA	3A1210JZ3T40LLJ0U	2026-04-12 00:00:00	CN FJ G4 00 03A1000019 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H9269032730	JJ2CkrPwnV4s	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/12195746457269.png	1	1	2	102508012	陈艺文	2026-04-12 19:56:04.844512	102508012	陈艺文	2026-04-12 19:58:13.867691	3	\N	http://nj2wm.cn/JJ2CkrPwnV4s/SS6B
5497	LG1204-4MA	3A1210JZ5T40LLJ0N	2026-04-12 00:00:00	CN FJ G4 00 03A1000019 000004	SC4H135.3G4	上海新动力汽车科技股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	H9263032725	JJ2CHaChUSYl	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/12195735647981.png	1	1	2	102508012	陈艺文	2026-04-12 19:56:57.512974	102508012	陈艺文	2026-04-12 19:58:13.869082	3	\N	http://nj2wm.cn/JJ2CHaChUSYl/ssnP
5498	D1204-4P	3A12100J9S4JS0HU0	2026-04-13 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251196168V	JJ2CjSE2Lkm0	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/13141704486601.png	1	1	2	102508012	陈艺文	2026-04-13 14:15:27.380028	102508012	陈艺文	2026-04-13 14:35:07.062336	4	\N	http://nj2wm.cn/JJ2CjSE2Lkm0/HEuR
5499	D1204-4P	3A12100J1S4JS0WSL	2026-04-13 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251196167V	JJ2CdMVA26Eu	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/13141704399877.png	1	1	2	102508012	陈艺文	2026-04-13 14:16:11.966644	102508012	陈艺文	2026-04-13 14:35:07.063359	4	\N	http://nj2wm.cn/JJ2CdMVA26Eu/BZwH
5500	D1204-4P	3A12100J2S4JS0HUS	2026-04-13 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251196166V	JJ2CYHNWu7FV	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14161913513589.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:32:45.145239	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.044538	3	\N	http://nj2wm.cn/JJ2CYHNWu7FV/IVqi
5501	LG2104-6M	3A1210JP5T40L0HPZ	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006030	JJ2CvcVpjs1p	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14162005317727.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:34:49.130125	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.044915	3	\N	http://nj2wm.cn/JJ2CvcVpjs1p/hLgp
5502	LG2104-6M	3A1210JP6T40L0ZHW	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008092	JJ2ChJR2swRD	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14162005615066.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:35:45.304992	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.045325	3	\N	http://nj2wm.cn/JJ2ChJR2swRD/kSdY
5503	LG2104-6M	3A1210JP3T40L0HPH	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008082	JJ2CVQumrwCh	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14162001788463.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:37:34.607066	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.045663	3	\N	http://nj2wm.cn/JJ2CVQumrwCh/AjgO
5504	LG2104-6M	3A1210JP1T40JSNJ0	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261001814	JJ2CLZ49eiFo	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14161958730901.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:39:00.23391	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.046075	3	\N	http://nj2wm.cn/JJ2CLZ49eiFo/jSwj
5505	LG2104-6M	3A1210JP1T40S0SSJ	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261002784	JJ2C0DinSE4Z	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14162005420839.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:40:00	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.040572	4	\N	http://nj2wm.cn/JJ2C0DinSE4Z/9pBn
5506	LG2104-6M	3A1210JPXT40L0ZZJ	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008074	JJ2CyIuWcZvr	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14162005153028.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:44:35.125681	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.04649	3	\N	http://nj2wm.cn/JJ2CyIuWcZvr/bYai
5507	LG2104-6M	3A1210JP8T40L0ZZ0	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006035	JJ2Cd4xT9458	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14161945419219.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:45:52	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.044082	4	\N	http://nj2wm.cn/JJ2Cd4xT9458/Lyuo
5508	LG2104-6M	3A1210JPXT40L0ZHH	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262008084	JJ2CSHtv7UEZ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14162005108690.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:46:45.370024	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.046845	3	\N	http://nj2wm.cn/JJ2CSHtv7UEZ/EIvC
5509	LG2104-6M	3A1210JP1T40L0ZHZ	2026-04-14 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007298	JJ2CoMvhelse	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/14161955575370.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 13:47:53	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-14 16:21:29.043507	4	\N	http://nj2wm.cn/JJ2CoMvhelse/2MJf
5518	T3004-7E	3A1210SL5T40SJ0SP	2026-04-16 00:00:00	CN FJ G4 00 03A1000023 000002	SC9DF340G4	上海新动力汽车科技股份有限公司	220.5	高压共轨	DOC、DPF、SCR	GB/T15370.3-2025	D9261000368	JJ2CvHIaHYbJ	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/16135104368319.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:40:09.902937	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:54:47.376336	3	\N	http://nj2wm.cn/JJ2CvHIaHYbJ/JkVy
5519	LG2104-6M	3A1210JP5T40S0HZH	2026-04-16 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007130	JJ2CWjUiOcKP	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/16135204878999.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:43:05.498597	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:54:47.375877	3	\N	http://nj2wm.cn/JJ2CWjUiOcKP/u6Z7
5520	LG1204-4MA	3A1210JZ8T40J0U0L	2026-04-16 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251094792V	JJ2C5YeyqOx5	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/16135104123961.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:43:52.552617	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:54:47.375242	3	\N	http://nj2wm.cn/JJ2C5YeyqOx5/bWqw
5521	LG2104-6M	3A1210JP7S40ZJU0W	2025-09-21 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9267021226	JJ2CzR3LLmpK	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2025/10/08101915596072.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:46:05.465491	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 13:54:47.369943	3	\N	http://nj2wm.cn/JJ2CzR3LLmpK/pGpo
5522	LG1204-4MA	3A1210JZ1T40LSL0P	2026-04-18 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251194525V	JJ2Cv0oWt17U	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/18145745165554.png	1	1	2	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-18 14:55:58.386208	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-18 15:07:19.759289	3	\N	http://nj2wm.cn/JJ2Cv0oWt17U/t4ev
5432	D1204-4MA	3A12100U4T40N0N0J	2026-05-05 00:00:00	CN FJ G4 00 03A1000004 000001	V38-130C43	安徽全柴动力股份有限公司	111	高压共轨	DOC、DPF、EGR	GB/T15370.1-2012	12333333333	\N	\N	1	1	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:19:19	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:21:06.933614	2	\N	\N
5515	LG2104-6M	3A1210JP3T40UJW0N	2026-04-16 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263012432	\N	\N	1	1	0	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 10:39:23	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:21:27.985746	2	\N	\N
5514	LG2104-6M	3A1210JP1T40UJW0U	2026-04-16 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262010995	\N	\N	1	1	0	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 10:38:27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:36:09.022937	2	\N	\N
5513	LG2104-6M	3A1210JPXT40UJW0L	2026-04-16 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9261006044	\N	\N	1	1	0	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 10:37:31	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:36:17.87684	2	\N	\N
5512	LG2104-6M	3A1210JP8T40UJW0S	2026-04-16 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9263011727	\N	\N	1	1	0	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 10:36:18	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:39:01.27491	2	\N	\N
5511	LG2104-6M	3A1210JP6T40UJW0J	2026-04-16 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262007309	\N	\N	1	1	0	3fbdcdd6-a524-4bf3-a07c-cd1670d9aded	陈艺文	2026-04-16 10:35:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:39:05.993719	2	\N	\N
5510	LG2104-6M	3A1210JP2T40UJN0J	2026-04-15 00:00:00	CN FJ G4 00 03A1000017 000001	SC7H230.1G4B	上海新动力汽车科技股份有限公司	154.5	高压共轨	DOC、DPF、EGR	GB/T15370.3-2025	H9262010993	\N	\N	1	1	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-15 16:10:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:39:14.331401	2	CN FC G4 00 0293000388 000001	\N
5471	LG1204-4MA	3A1210JZXT40J0U0U	2026-04-07 00:00:00	CN FJ G4 00 03A1000019 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q251094797V	JJ2CkdgItQSy	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/04/07151004291690.png	1	1	0	102508012	陈艺文	2026-04-07 15:06:24.129716	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-27 09:36:31.699838	4	\N	http://nj2wm.cn/JJ2CkdgItQSy/LVxO
5452	D1204-4P	3A12100J6S40HJUJN	2026-03-31 00:00:00	CN FJ G4 00 03A1000001 000002	V38-130C43	安徽全柴动力股份有限公司	88.2	高压共轨	DOC、DPF、EGR	GB/T15370.2-2025	Q250490532V	JJ2Ch8IDVJjP	https://nj2wm.obs.cn-north-4.myhuaweicloud.com/qrcode/2026/03/31155842891722.png	1	1	0	102508012	陈艺文	2026-03-31 15:37:48.450592	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-27 09:41:30.778673	5	\N	http://nj2wm.cn/JJ2Ch8IDVJjP/wN5U
5444	4P-120	3A121Z0L9T40WJS0J	2026-06-12 00:00:00	\N	SC4H160.5G4	安徽全柴动力股份有限公司	11	高压共轨	DOC、DPF、EGR	GB/T15370.1-2012	11111111111	\N	\N	1	1	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 09:58:58	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:06:37.504799	2	\N	\N
5524	D1004-4P	3A12100S5T40WJS0J	2026-06-12 00:00:00	CN FJ G4 00 03A1000002 000001	V38-110C43	安徽全柴动力股份有限公司	333	高压共轨	DOC、DPF、EGR	GB/T15370.1-2012	12222222222	\N	\N	1	1	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:02:25	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:07:07.002426	3	\N	\N
5529	4P-120	3A121Z0L0T40WJS0S	2026-06-12 00:00:00	\N	SC4H160.5G4	安徽全柴动力股份有限公司	33	高压共轨	DOC、DPF、EGR	GB/T15370.1-2012	12111111333	\N	\N	1	1	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:22:24	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:22:35.021608	2	\N	\N
\.


--
-- Data for Name: print_record; Type: TABLE DATA; Schema: micro_product_assistant; Owner: postgres
--

COPY micro_product_assistant.print_record (id, product_time, vins, include_day, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
4	\N	3A1210JZ5T40J0HJL	0	test	管理员	2026-03-28 11:38:30.493335	\N	\N	\N	1
5	\N	3A12100JXT40JSSJL,3A12100J7S40HJNJZ	0	test	管理员	2026-03-28 14:09:41.759653	\N	\N	\N	1
6	2026-04-25 00:00:00	3A1210JP6T40LJLUN,3A1210JP8T40LJLUW,3A1210JP6T40L0USS	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 10:38:33.367154	\N	\N	\N	1
\.


--
-- Data for Name: qr_sync_job; Type: TABLE DATA; Schema: micro_product_assistant; Owner: postgres
--

COPY micro_product_assistant.qr_sync_job (id, product_time, vins, excels, push_status, pull_status, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
23	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_1_1776156642282.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:50:42.270818	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:50:42.312464	1
24	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_1_1776156808266.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:53:28.256357	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:53:28.291457	1
12	\N	3A1210JZ5T40J0HJL	{"LG1204-4MA":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\LG1204-4MA_1_1774669003807.xlsx"}	0	1	test	管理员	2026-03-28 11:36:43.796113	test	管理员	2026-03-28 11:37:51.275013	2
25	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_1_1776157206190.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 17:00:06.182671	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 17:00:06.208886	1
26	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant/D1204-4P_1_1776157259330.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 17:00:59.267971	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 17:01:01.247065	1
13	\N	3A12100JXT40JSSJL,3A12100J7S40HJNJZ	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_2_1774678063108.xlsx"}	0	1	test	管理员	2026-03-28 14:07:43.097459	test	管理员	2026-03-28 14:08:46.889045	2
14	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant/D1204-4P_1_1776061506735.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-13 14:25:06.696548	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-13 14:25:08.670628	1
15	\N	3A12100J9T40U0P0J,3A1210SZ9T40U0H0J,3A12100J2T40U0H0J,3A12100J4T40U0H0S,3A12100J6T40U0H0L,3A12100J8T40U0H0U,3A12100JXT40U0H0N,3A12100J1T40U0H0W	\N	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:43:46.916872	\N	\N	\N	1
16	\N	3A12100J9T40U0P0J,3A1210SZ9T40U0H0J,3A12100J2T40U0H0J,3A12100J4T40U0H0S,3A12100J6T40U0H0L,3A12100J8T40U0H0U,3A12100JXT40U0H0N,3A12100J1T40U0H0W	\N	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:45:07.500678	\N	\N	\N	1
27	\N	3A12100J1T40JJZ0N,3A12100J9T40U0P0J,3A12100U4T40N0N0J,3A1210JP3T40UJW0N,3A1210JP1T40UJW0U	\N	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-22 14:55:49.426786	\N	\N	\N	1
17	2026-04-14 00:00:00	3A12100J9T40U0P0J,3A1210SZ9T40U0H0J,3A12100J2T40U0H0J,3A12100J4T40U0H0S,3A12100J6T40U0H0L,3A12100J8T40U0H0U,3A12100JXT40U0H0N,3A12100J1T40U0H0W	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_7_1776156370892.xlsx","F1404-5P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\F1404-5P_1_1776156371143.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:46:10.86793	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:46:11.186289	1
18	\N	3A12100J9T40U0P0J,3A1210SZ9T40U0H0J,3A12100J2T40U0H0J,3A12100J4T40U0H0S,3A12100J6T40U0H0L,3A12100J8T40U0H0U,3A12100JXT40U0H0N,3A12100J1T40U0H0W	\N	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:48:59.330153	\N	\N	\N	1
19	2026-04-14 00:00:00	3A12100J9T40U0P0J,3A1210SZ9T40U0H0J,3A12100J2T40U0H0J,3A12100J4T40U0H0S,3A12100J6T40U0H0L,3A12100J8T40U0H0U,3A12100JXT40U0H0N,3A12100J1T40U0H0W	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_7_1776156543887.xlsx","F1404-5P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\F1404-5P_1_1776156543930.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:49:03.871353	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:49:03.963595	1
20	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_1_1776156570064.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:49:30.055316	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:49:30.105959	1
21	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_1_1776156591113.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:49:51.100486	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:49:51.141829	1
22	\N	3A12100J9T40U0P0J	{"D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant\\\\D1204-4P_1_1776156605785.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:50:05.776784	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-14 16:50:05.79789	1
28	2026-06-22 00:00:00	3A12100J1T40JJZ0N,3A12100J9T40U0P0J,3A12100U4T40N0N0J,3A1210JP3T40UJW0N,3A1210JP1T40UJW0U	{"LG2104-6M":"D:\\\\\\\\tunaData\\\\\\\\product-assistant/LG2104-6M_2_1782111385613.xlsx","D1204-4P":"D:\\\\\\\\tunaData\\\\\\\\product-assistant/D1204-4P_2_1782111385730.xlsx","D1204-4MA":"D:\\\\\\\\tunaData\\\\\\\\product-assistant/D1204-4MA_1_1782111385848.xlsx"}	0	0	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-22 14:56:25.590425	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-22 14:56:26.012338	1
\.


--
-- Data for Name: vin_generate_request; Type: TABLE DATA; Schema: micro_product_assistant; Owner: postgres
--

COPY micro_product_assistant.vin_generate_request (id, model_code, engine_model, environmental_public_number, engine_serial_number, vin, engine_public_number, engine_img, engine_manufacturer, rated_power, environmental_key_parts, fuel_injection_system_type, execution_standard, nameplate_record_id, status, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
20	D1204-4MA	V38-130C43	CN FJ G4 00 03A1000004 000001	12333333333	3A12100U4T40N0N0J	\N	/platformSvr/upload/files/public/2026/4/28100920.png	安徽全柴动力股份有限公司	111	DOC、DPF、EGR	高压共轨	GB/T15370.1-2012	5432	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-28 10:09:23	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 14:19:27.177	3
8	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000002	Q251194527V	3A12100J9T40U0P0J	CN FC G4 00 0602000266 000001	/platformSvr/upload/files/public/2026/4/07162750.jpg	安徽全柴动力股份有限公司	88.2	DOC、DPF、EGR	高压共轨	GB/T15370.2-2025	5421	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-07 16:27:51	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 16:35:46.508675	11
10	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000001	55555555555	3A12100J6T40U0H0L	\N	/platformSvr/upload/files/public/2026/4/07164446.jpg,/platformSvr/upload/files/public/2026/4/07164454.jpg	安徽全柴动力股份有限公司	666	DOC、DPF、EGR	高压共轨	GB/T15370.2-2025	5425	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-07 16:44:54	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:37:27.965893	3
11	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000001	V3333333333	3A12100J4T40U0H0S	\N	/platformSvr/upload/files/public/2026/4/07164641.jpg	安徽全柴动力股份有限公司	300	DOC、DPF、EGR	高压共轨	GB/T15370.2-2025	5424	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-07 16:46:42	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:37:30.040418	3
12	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000001	V3801111111	3A12100J2T40U0H0J	\N	/platformSvr/upload/files/public/2026/4/07165135.jpg	安徽全柴动力股份有限公司	88	DOC、DPF、EGR	高压共轨	GB/T15370.3-2025	5423	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-07 16:51:36	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:37:31.779417	3
13	F1404-5P	SC7H160G4B	CN FJ G4 00 03A1000029 000001	HG000000001	3A1210SZ9T40U0H0J	\N	/platformSvr/upload/files/public/2026/4/08100947.jpg	安徽全柴动力股份有限公司	33	DOC、DPF、EGR	高压共轨	GB/T15370.1-2012	5422	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-08 10:09:48	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 10:37:33.683013	3
14	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000001	36222222222	3A12100JXT40U0H0N	\N	/platformSvr/upload/files/public/2026/4/08114756.jpg	安徽全柴动力股份有限公司	300	DOC、DPF、EGR	高压共轨	GB/T15370.1-2012	5427	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-08 11:47:57	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 11:48:37.526277	3
15	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000001	63622222222	3A12100J1T40U0H0W	\N	/platformSvr/upload/files/public/2026/4/08114851.jpg	安徽全柴动力股份有限公司	300	DOC、DPF、EGR	高压共轨	GB/T15370.2-2025	5428	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-08 11:48:52	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 11:49:48.490339	3
16	F1600-5PB	\N	\N	\N	\N	\N	/platformSvr/upload/files/public/2026/4/09115634.png	\N	\N	\N	\N	\N	\N	0	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-09 11:56:36.020343	\N	\N	\N	1
17	4P-120	SC4H160.5G4	\N	12111111333	3A121Z0L0T40WJS0S	\N	/platformSvr/upload/files/public/2026/4/09115640.png	安徽全柴动力股份有限公司	112	DOC、DPF、EGR	高压共轨	GB/T15370.1-2012	5529	1	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-09 11:56:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:22:24.137342	2
9	D1204-4P	V38-130C43	CN FJ G4 00 03A1000001 000001	65566666666	3A12100J8T40U0H0U	\N	/platformSvr/upload/files/public/2026/4/07163651.jpg	安徽全柴动力股份有限公司	600	DOC、DPF、EGR	高压共轨	GB/T15370.3-2025	5426	2	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-07 16:36:52	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-05 16:36:21.241143	7
19	4P-120	SC4H160.5G4	\N	11111111111	3A121Z0L9T40WJS0J	\N	/platformSvr/upload/files/public/2026/4/13173404.jpg	安徽全柴动力股份有限公司	11	DOC、DPF、EGR	高压共轨	GB/T15370.1-2012	5444	1	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-13 17:34:06	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 09:58:58.540066	2
18	D1004-4P	V38-110C43	CN FJ G4 00 03A1000002 000001	12222222222	3A12100S5T40WJS0J	\N	/platformSvr/upload/files/public/2026/4/09115712.png	安徽全柴动力股份有限公司	333	DOC、DPF、EGR	高压共轨	GB/T15370.1-2012	5524	1	fa1a2cec-9686-4b05-978f-54e374:public:mobile	手机端	2026-04-09 11:57:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:02:25.342577	2
\.


--
-- Data for Name: oauth2_authorization; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.oauth2_authorization (id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at, authorization_code_metadata, access_token_value, access_token_issued_at, access_token_expires_at, access_token_metadata, access_token_type, access_token_scopes, oidc_id_token_value, oidc_id_token_issued_at, oidc_id_token_expires_at, oidc_id_token_metadata, refresh_token_value, refresh_token_issued_at, refresh_token_expires_at, refresh_token_metadata, user_code_value, user_code_issued_at, user_code_expires_at, user_code_metadata, device_code_value, device_code_issued_at, device_code_expires_at, device_code_metadata) FROM stdin;
\.


--
-- Data for Name: oauth2_authorization_consent; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.oauth2_authorization_consent (registered_client_id, principal_name, authorities) FROM stdin;
\.


--
-- Data for Name: oauth2_registered_client; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, is_active, description) FROM stdin;
55d05d31-dec8-475b-8852-8c6e5d491134	demo-client	2026-02-26 13:40:52	{bcrypt}$2a$10$lINSmYjy6VVmM2X.x84Np.ObUnbva2wKsCHDTO0AH7xi4GR4mXODO	\N	55d05d31-dec8-475b-8852-8c6e5d491134	{client_secret_post,client_secret_basic}	{refresh_token,authorization_code}	{http://tuna.net/platformSvr/sapi/redirect}	{http://tuna.net/platformSvr/index.html}	{openid,profile}	{"@class": "com.tuna.platform.oauth2.entity.TunaClientSettings", "requireProofKey": false, "requireAuthorizationConsent": false}	{"@class": "com.tuna.platform.oauth2.entity.TunaTokenSettings", "accessTokenFormat": "self-contained", "reuseRefreshTokens": true, "deviceCodeTimeToLive": 300, "accessTokenTimeToLive": 360000, "refreshTokenTimeToLive": 360000, "idTokenSignatureAlgorithm": "RS256", "authorizationCodeTimeToLive": 300, "x509CertificateBoundAccessTokens": false}	\N	\N	2026-02-26 13:40:53	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 11:17:36.845614	3	t	\N
8fd92236-98f1-447d-bc84-39fdfe71be08	test	2026-05-08 17:30:43	{bcrypt}$2a$10$TAN1ent0GZ3.q66ZHgFU5Opwc3XwCNjLv4vdtGUK4ATNgnerclAJO	\N	test	{client_secret_post,client_secret_basic}	{refresh_token,authorization_code}	{http://tuna.net/platformSvr/sapi/1redirect}	{http://tuna.net/platformSvr/index.html}	{openid,profile}	{"@class": "com.tuna.platform.oauth2.entity.TunaClientSettings", "jwkSetUrl": "", "requireProofKey": true, "x509CertificateSubjectDN": "", "requireAuthorizationConsent": true, "tokenEndpointAuthenticationSigningAlgorithm": "HS256"}	{"@class": "com.tuna.platform.oauth2.entity.TunaTokenSettings", "accessTokenFormat": "self-contained", "reuseRefreshTokens": true, "deviceCodeTimeToLive": 300, "accessTokenTimeToLive": 360000, "refreshTokenTimeToLive": 360000, "idTokenSignatureAlgorithm": "RS256", "authorizationCodeTimeToLive": 600, "x509CertificateBoundAccessTokens": false}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 17:30:39	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-07 08:48:35.16713	63	t	\N
c8eaef7a-794e-4932-8779-a5559781fc03	rmiClient	2026-06-09 17:33:05.807602	{bcrypt}$2a$10$jF0HIQOnlM/GFqwxvzvIE.XuUMawXg0mbSebzEO0lELbdc3L.g0uy	\N	rmiClient	{client_secret_post,client_secret_basic}	{refresh_token,authorization_code}	{https://rmi.mylgag.com/platformSvr/sapi/redirect}	{https://rmi.mylgag.com/platformSvr/index.html}	{openid,profile}	{"@class": "com.tuna.platform.oauth2.entity.TunaClientSettings", "requireProofKey": false, "requireAuthorizationConsent": false, "tokenEndpointAuthenticationSigningAlgorithm": "HS256"}	{"@class": "com.tuna.platform.oauth2.entity.TunaTokenSettings", "accessTokenFormat": "self-contained", "reuseRefreshTokens": true, "deviceCodeTimeToLive": 300, "accessTokenTimeToLive": 360000, "refreshTokenTimeToLive": 3600, "idTokenSignatureAlgorithm": "RS256", "authorizationCodeTimeToLive": 300, "x509CertificateBoundAccessTokens": false}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-09 17:33:04.102863	\N	\N	2026-06-09 17:33:05.807602	1	t	\N
\.


--
-- Data for Name: oauth2_registered_client_openapi; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.oauth2_registered_client_openapi (oauth2_registered_client_id, openapi_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_dictionary; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_dictionary (id, group_id, code, name, status, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	1	product_model	产品型号	1	\N	admin	admin	2026-03-24 13:41:48	\N	\N	\N	1
2	1	execution_standard	执行标准	1	\N	admin	admin	2026-03-25 00:43:14.215011	\N	\N	\N	1
3	1	environmental_key_parts	环保关键件	1	\N	admin	admin	2026-03-25 00:51:51.888026	\N	\N	\N	1
5	1	engine_manufacturer	发动机生产厂	1	\N	admin	admin	2026-03-25 00:56:58.32123	\N	\N	\N	1
6	1	engine_model	发动机型号	1	\N	admin	admin	2026-03-25 01:32:20.359563	\N	\N	\N	1
9	1	external_product_model	海外产品编码对照表	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:02:27.537088	\N	\N	\N	1
8	5	platform_settings	平台默认参数	1	\N	admin	admin	2026-04-09 07:08:18.651066	\N	\N	\N	1
\.


--
-- Data for Name: platform_dictionary_item; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_dictionary_item (id, dictionary_id, code, label, value, is_default, css, show_order, status, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
65	8	default_password	账户默认密码	Aa@112233	0	\N	1	1	\N	admin	admin	2026-04-09 07:12:05.004995	\N	\N	\N	1
75	1	4P-100	4P-100	4P-100	0	\N	902	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:49:08	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:53:53.765137	2
89	1	4P-120	4P-120	4P-120	0	\N	903	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:49:40	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:54:05.019513	2
90	1	4P-130	4P-130	4P-130	0	\N	904	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:50:11	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:54:16.499784	2
91	1	4P-140	4P-140	4P-140	0	\N	905	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:50:27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:54:23.671627	2
92	1	5P-130	5P-130	5P-130	0	\N	906	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:50:37	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:54:45.114672	2
93	1	5P-150	5P-150	5P-150	0	\N	907	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:50:48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:54:50.029297	2
94	1	7E-240	7E-240	7E-240	0	\N	908	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:51:04	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:55:44.540422	2
95	1	7E-260	7E-260	7E-260	0	\N	909	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:51:26	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:55:39.356446	2
96	1	7E-280	7E-280	7E-280	0	\N	910	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:51:38	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:55:28.535916	2
97	1	7E-300	7E-300	7E-300	0	\N	911	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:51:50	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:55:19.679585	2
98	1	7E-320	7E-320	7E-320	0	\N	912	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:52:01	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:55:12.62228	2
99	1	7EA-280	7EA-280	7EA-280	0	\N	913	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:52:10	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:56:24.651373	2
100	1	7EA-300	7EA-300	7EA-300	0	\N	914	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:52:20	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:56:20.297342	2
101	1	7EA-320	7EA-320	7EA-320	0	\N	915	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:52:32	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:56:14.989309	2
102	1	7EA-340	7EA-340	7EA-340	0	\N	916	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:52:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:56:10.502079	2
103	1	7EA-360	7EA-360	7EA-360	0	\N	917	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:52:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:56:05.165069	2
104	9	LG904-4MA	LG904-4MA	901	0	\N	1	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:03:01.098277	\N	\N	\N	1
105	9	4P-100	4P-100	902	0	\N	2	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:03:29.334508	\N	\N	\N	1
106	9	4P-120	4P-120	903	0	\N	3	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:03:43.937915	\N	\N	\N	1
107	9	4P-130	4P-130	904	0	\N	4	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:04:00.52846	\N	\N	\N	1
70	1	LG904-4MA	LG904-4MA	LG904-4MA	0	\N	901	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:48:49	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:53:35.784279	2
4	1	F1404-5P	F1404-5P	F1404-5P	0	\N	2	1	\N	admin	admin	2026-03-24 05:45:37.486647	\N	\N	\N	1
5	1	F1600-5PB	F1600-5PB	F1600-5PB	0	\N	4	1	\N	admin	admin	2026-03-24 05:52:45.404675	\N	\N	\N	1
6	1	LG1804-6M	LG1804-6M	LG1804-6M	0	\N	5	1	\N	admin	admin	2026-03-24 05:52:45.41045	\N	\N	\N	1
7	1	F1400-5PB	F1400-5PB	F1400-5PB	0	\N	6	1	\N	admin	admin	2026-03-24 05:52:45.413661	\N	\N	\N	1
8	1	T3004-7E	T3004-7E	T3004-7E	0	\N	7	1	\N	admin	admin	2026-03-24 05:52:45.417743	\N	\N	\N	1
9	1	D1004-4P	D1004-4P	D1004-4P	0	\N	8	1	\N	admin	admin	2026-03-24 05:52:45.421626	\N	\N	\N	1
10	1	G3004-6P	G3004-6P	G3004-6P	0	\N	9	1	\N	admin	admin	2026-03-24 05:52:45.425421	\N	\N	\N	1
11	1	DG2604-6PA	G2604-6PA	G2604-6PA	0	\N	10	1	\N	admin	admin	2026-03-24 05:52:45.432255	\N	\N	\N	1
12	1	F1800-5PB	F1800-5PB	F1800-5PB	0	\N	11	1	\N	admin	admin	2026-03-24 05:52:45.446869	\N	\N	\N	1
13	1	D1204-4P	D1204-4P	D1204-4P	0	\N	12	1	\N	admin	admin	2026-03-24 05:52:45.452187	\N	\N	\N	1
14	1	G2104-6M	G2104-6M	G2104-6M	0	\N	13	1	\N	admin	admin	2026-03-24 05:52:45.458004	\N	\N	\N	1
15	1	F1604-5PA	F1604-5PA	F1604-5PA	0	\N	14	1	\N	admin	admin	2026-03-24 05:52:45.461941	\N	\N	\N	1
16	1	D1004-4H	D1004-4H	D1004-4H	0	\N	15	1	\N	admin	admin	2026-03-24 05:52:45.465349	\N	\N	\N	1
17	1	LG2104-6M	LG2104-6M	LG2104-6M	0	\N	16	1	\N	admin	admin	2026-03-24 05:52:45.474139	\N	\N	\N	1
18	1	LG1004-4MA	LG1004-4MA	LG1004-4MA	0	\N	17	1	\N	admin	admin	2026-03-24 05:52:45.477531	\N	\N	\N	1
19	1	F1604-5P	F1604-5P	F1604-5P	0	\N	18	1	\N	admin	admin	2026-03-24 05:52:45.482158	\N	\N	\N	1
20	1	LG1204-4MA	LG1204-4MA	LG1204-4MA	0	\N	19	1	\N	admin	admin	2026-03-24 05:52:45.486886	\N	\N	\N	1
21	1	D1204-4H	D1204-4H	D1204-4H	0	\N	20	1	\N	admin	admin	2026-03-24 05:52:45.491079	\N	\N	\N	1
22	1	F1404-5PA	F1404-5PA	F1404-5PA	0	\N	21	1	\N	admin	admin	2026-03-24 05:52:45.493985	\N	\N	\N	1
23	1	G2804-6P	G2804-6P	G2804-6P	0	\N	22	1	\N	admin	admin	2026-03-24 05:52:45.495555	\N	\N	\N	1
24	1	G2604-6P	G2604-6P	G2604-6P	0	\N	23	1	\N	admin	admin	2026-03-24 05:52:45.497057	\N	\N	\N	1
25	2	GB/T15370.1-2012	GB/T15370.1-2012	GB/T15370.1-2012	0	\N	1	1	\N	admin	admin	2026-03-25 00:44:37.675181	\N	\N	\N	1
27	2	GB/T15370.2-2025	GB/T15370.2-2025	GB/T15370.2-2025	0	\N	2	1	\N	admin	admin	2026-03-25 00:45:47.387311	\N	\N	\N	1
28	2	GB/T15370.3-2025	GB/T15370.3-2025	GB/T15370.3-2025	0	\N	3	1	\N	admin	admin	2026-03-25 00:46:15.228072	\N	\N	\N	1
29	2	GB/T15370.2-2009	GB/T15370.2-2009	GB/T15370.2-2009	0	\N	4	1	\N	admin	admin	2026-03-25 00:46:39.651382	\N	\N	\N	1
30	2	GB/T15370.3-2012	GB/T15370.3-2012	GB/T15370.3-2012	0	\N	5	1	\N	admin	admin	2026-03-25 00:47:00.228114	\N	\N	\N	1
31	3	DOC、DPF、EGR	DOC、DPF、EGR	DOC、DPF、EGR	0	\N	1	1	\N	admin	admin	2026-03-25 00:52:27.166912	\N	\N	\N	1
32	3	DOC、DPF、SCR	DOC、DPF、SCR	DOC、DPF、SCR	0	\N	2	1	\N	admin	admin	2026-03-25 00:52:51.939513	\N	\N	\N	1
33	5	安徽全柴动力股份有限公司	安徽全柴动力股份有限公司	安徽全柴动力股份有限公司	0	\N	1	1	\N	admin	admin	2026-03-25 00:58:01.809216	\N	\N	\N	1
34	5	上海新动力汽车科技股份有限公司	上海新动力汽车科技股份有限公司	上海新动力汽车科技股份有限公司	0	\N	2	1	\N	admin	admin	2026-03-25 00:58:20.037876	\N	\N	\N	1
35	6	SC4H160.5G4	SC4H160.5G4	SC4H160.5G4	0	\N	1	1	\N	admin	admin	2026-03-25 01:33:03.124294	\N	\N	\N	1
36	6	SC9DF290G4	SC9DF290G4	SC9DF290G4	0	\N	2	1	\N	admin	admin	2026-03-25 01:33:25.677044	\N	\N	\N	1
37	6	SC7H230.1GB	SC7H230.1GB	SC7H230.1GB	0	\N	3	1	\N	admin	admin	2026-03-25 01:33:46.96892	\N	\N	\N	1
39	6	SC7H260.5G4	SC7H260.5G4	SC7H260.5G4	0	\N	4	1	\N	admin	admin	2026-03-25 01:34:14.57332	\N	\N	\N	1
40	6	V38-130C43	V38-130C43	V38-130C43	0	\N	5	1	\N	admin	admin	2026-03-25 01:34:31.413875	\N	\N	\N	1
41	6	SC4H175.5G4	SC4H175.5G4	SC4H175.5G4	0	\N	6	1	\N	admin	admin	2026-03-25 01:34:50.248574	\N	\N	\N	1
42	6	SC9D340G4	SC9D340G4	SC9D340G4	0	\N	7	1	\N	admin	admin	2026-03-25 01:37:12.504506	\N	\N	\N	1
43	6	SC9DF320G4	SC9DF320G4	SC9DF320G4	0	\N	8	1	\N	admin	admin	2026-03-25 01:37:48.654072	\N	\N	\N	1
44	6	SC7H230G4	SC7H230G4	SC7H230G4	0	\N	9	1	\N	admin	admin	2026-03-25 01:38:07.967607	\N	\N	\N	1
45	6	SC7H230.1G4B	SC7H230.1G4B	SC7H230.1G4B	0	\N	10	1	\N	admin	admin	2026-03-25 01:38:27.668907	\N	\N	\N	1
46	6	SC7H200.1G4B	SC7H200.1G4B	SC7H200.1G4B	0	\N	11	1	\N	admin	admin	2026-03-25 01:40:08.175326	\N	\N	\N	1
47	6	SC9DF340G4	SC9DF340G4	SC9DF340G4	0	\N	12	1	\N	admin	admin	2026-03-25 01:40:29.749553	\N	\N	\N	1
48	6	SC9DF280G4	SC9DF280G4	SC9DF280G4	0	\N	13	1	\N	admin	admin	2026-03-25 01:40:49.461578	\N	\N	\N	1
49	6	SC7H180G4B	SC7H180G4B	SC7H180G4B	0	\N	14	1	\N	admin	admin	2026-03-25 01:41:17.756872	\N	\N	\N	1
50	6	V38-110C43	V38-110C43	V38-110C43	0	\N	15	1	\N	admin	admin	2026-03-25 01:41:39.60964	\N	\N	\N	1
51	6	SC9DF300G4	SC9DF300G4	SC9DF300G4	0	\N	16	1	\N	admin	admin	2026-03-25 01:41:57.657214	\N	\N	\N	1
52	6	SC7H290G4	SC7H290G4	SC7H290G4	0	\N	17	1	\N	admin	admin	2026-03-25 01:42:15.616066	\N	\N	\N	1
53	6	V30-130C43	V30-130C43	V30-130C43	0	\N	18	1	\N	admin	admin	2026-03-25 01:42:37.682695	\N	\N	\N	1
54	6	SC4H135.3G4	SC4H135.3G4	SC4H135.3G4	0	\N	19	1	\N	admin	admin	2026-03-25 01:42:57.209566	\N	\N	\N	1
55	6	SC7H160G4B	SC7H160G4B	SC7H160G4B	0	\N	20	1	\N	admin	admin	2026-03-25 01:43:15.713747	\N	\N	\N	1
56	1	D1004-4MA	D1004-4MA	D1004-4MA	0	\N	30	1	\N	admin	admin	2026-03-29 10:41:58.656021	\N	\N	\N	1
57	1	T3204-7E	T3204-7E	T3204-7E	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.906719	\N	\N	\N	1
58	1	G2604-6PA	G2604-6PA	G2604-6PA	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.92168	\N	\N	\N	1
59	1	D1204-4MA	D1204-4MA	D1204-4MA	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.924713	\N	\N	\N	1
60	1	T2804-7E	T2804-7E	T2804-7E	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.926821	\N	\N	\N	1
61	1	D1054-4P	D1054-4P	D1054-4P	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.929034	\N	\N	\N	1
62	1	G1804-6M	G1804-6M	G1804-6M	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.931383	\N	\N	\N	1
63	1	G2604-6MA	G2604-6MA	G2604-6MA	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.933214	\N	\N	\N	1
64	1	4YL-12	4YL-12	4YL-12	0	\N	30	1	\N	admin	admin	2026-03-29 10:42:03.935584	\N	\N	\N	1
66	1	G2204-6MA	G2204-6MA	G2204-6MA	0	\N	1	1	\N	admin	admin	2026-04-13 15:34:35.943301	\N	\N	\N	1
67	1	G2404-6MA	G2404-6MA	G2404-6MA	0	\N	1	1	\N	admin	admin	2026-04-13 15:36:12.255002	\N	\N	\N	1
68	1	G3004-6P1	G3004-6P1	G3004-6P1	0	\N	1	1	\N	admin	admin	2026-04-13 15:38:10.505295	\N	\N	\N	1
69	1	G2804-6P1	G2804-6P1	G2804-6P1	0	\N	1	1	\N	admin	admin	2026-04-13 15:38:38.123314	\N	\N	\N	1
3	1	G2404-6PA	G2404-6PA	G2404-6PA	0	\N	1	1	\N	admin	admin	2026-03-24 05:45:11.031603	\N	\N	\N	1
71	1	G2604-6P1	G2604-6P1	G2604-6P1	0	\N	1	1	\N	admin	admin	2026-04-13 15:39:13.375517	\N	\N	\N	1
72	1	D1404-4PA	D1404-4PA	D1404-4PA	0	\N	1	1	\N	admin	admin	2026-04-13 15:41:09.752371	\N	\N	\N	1
73	1	D1204-4PA	D1204-4PA	D1204-4PA	0	\N	1	1	\N	admin	admin	2026-04-13 15:41:30.863465	\N	\N	\N	1
74	1	T3604-7EA	T3604-7EA	T3604-7EA	0	\N	1	1	\N	admin	admin	2026-04-15 16:32:20.357963	\N	\N	\N	1
76	6	SC8Y300.2G4	SC8Y300.2G4	SC8Y300.2G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:15:06.174326	\N	\N	\N	1
77	6	V38-120C43	V38-120C43	V38-120C43	0	\N	1	1	\N	admin	admin	2026-04-25 14:20:04.478548	\N	\N	\N	1
78	6	V38-120C41	V38-120C41	V38-120C41	0	\N	1	1	\N	admin	admin	2026-04-25 14:23:09.425416	\N	\N	\N	1
79	6	SC4H160.8G4	SC4H160.8G4	SC4H160.8G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:34:35.74774	\N	\N	\N	1
80	6	SC8Y270.1G4	SC8Y270.1G4	SC8Y270.1G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.339321	\N	\N	\N	1
81	6	SC8Y290.1G4	SC8Y290.1G4	SC8Y290.1G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.344956	\N	\N	\N	1
82	6	SC8Y250.1G4	SC8Y250.1G4	SC8Y250.1G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.34845	\N	\N	\N	1
83	6	SC8Y320.2G4	SC8Y320.2G4	SC8Y320.2G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.351576	\N	\N	\N	1
84	6	YCK11480-T400	YCK11480-T400	YCK11480-T400	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.35464	\N	\N	\N	1
85	6	SC4H115.3G4	SC4H115.3G4	SC4H115.3G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.358268	\N	\N	\N	1
86	6	SC4H115.3G4	SC4H115.3G4	SC4H115.3G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.363014	\N	\N	\N	1
87	6	SC4H115.3G4	SC4H115.3G4	SC4H115.3G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.366677	\N	\N	\N	1
88	6	SC8Y340.2G4	SC8Y340.2G4	SC8Y340.2G4	0	\N	1	1	\N	admin	admin	2026-04-25 14:36:12.369573	\N	\N	\N	1
108	9	4P-140	4P-140	905	0	\N	5	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:04:14.466877	\N	\N	\N	1
109	9	5P-130	5P-130	906	0	\N	6	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:04:28.925962	\N	\N	\N	1
110	9	5P-150	5P-150	907	0	\N	7	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:04:43.783596	\N	\N	\N	1
111	9	7E-240	7E-240	908	0	\N	8	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:04:58.911486	\N	\N	\N	1
112	9	7E-260	7E-260	909	0	\N	9	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:05:15.261255	\N	\N	\N	1
113	9	7E-280	7E-280	910	0	\N	10	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:05:36.361276	\N	\N	\N	1
114	9	7E-300	7E-300	911	0	\N	11	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:05:53.290981	\N	\N	\N	1
115	9	7E-320	7E-320	912	0	\N	12	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:06:04.243783	\N	\N	\N	1
116	9	7EA-280	7EA-280	913	0	\N	13	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:06:19.544318	\N	\N	\N	1
117	9	7EA-300	7EA-300	914	0	\N	14	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:06:32.976359	\N	\N	\N	1
118	9	7EA-320	7EA-320	915	0	\N	15	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:06:46.044532	\N	\N	\N	1
119	9	7EA-340	7EA-340	916	0	\N	16	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:06:57.295944	\N	\N	\N	1
120	9	7EA-360	7EA-360	917	0	\N	17	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:07:14.322603	\N	\N	\N	1
121	9	LG904-4H	LG904-4H	918	0	\N	918	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-11 16:22:36.618443	\N	\N	\N	1
122	1	LG904-4H	LG904-4H	LG904-4H	0	\N	902	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 08:36:48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 08:37:24.405226	2
\.


--
-- Data for Name: platform_entity_define; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_entity_define (id, code, service_code, name, url, remark, title_column_name, operation_column_width, query_params, form_component, form_width, dialog_model, pagination_visible, excel_enable, query_form_visible, excel_import_component, row_key, default_expand_all, tree_props, query_mode, show_lang, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, show_summary, query_bar_buttons, operation_buttons, table_name) FROM stdin;
32	com.tuna.platform.oauth2.entity.Oauth2AuthorizationConsent	platform-service	{"zh_CN":"Oauth2AuthorizationConsent"}	\N	Oauth2AuthorizationConsent	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.754131	\N	\N	\N	1	0	\N	\N	\N
54	com.tuna.knowledgeBase.entity.KnowledgeCategory	knowledge-base-service	{"zh_CN":"KnowledgeCategory"}	/sapi/knowledgeCategorys	知识分类目录	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:25:55.23515	3	0	\N		knowledge_category
33	com.tuna.platform.entity.Openapi	platform-service	{"zh_CN":"函数定义"}	/sapi/openapis	平台接口定义	name	160	descColumns=["updatedTime", "createdTime"]	\N	80%	0	1	1	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 14:10:54.577974	19	0	\N	\N	\N
52	com.tuna.platform.vo.PersonalSettingsVO	platform-service	{"zh_CN":"用户信息"}	/sapi/users	\N	realName	160	\N	\N	50%	0	1	1	1	\N	\N	0	\N	0	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:39:26	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:48:52.896868	2	0	\N	\N	\N
63	com.tuna.productAssistant.entity.NameplateRecord	product-assistant-service	{"zh_CN":"NameplateRecord"}	/sapi/nameplateRecords	生产铭牌打印记录表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.216618	\N	\N	\N	1	0	\N	\N	nameplate_record
64	com.tuna.productAssistant.entity.ModelCodeSerialNo	product-assistant-service	{"zh_CN":"ModelCodeSerialNo"}	/sapi/modelCodeSerialNos	车型流水号生成记录表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	model_code_serial_no
65	com.tuna.productAssistant.entity.PrintRecord	product-assistant-service	{"zh_CN":"PrintRecord"}	/sapi/printRecords	打印模板生成记录	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	print_record
34	com.tuna.platform.entity.ResourceGroup	platform-service	{"zh_CN":"菜单分组"}	/sapi/resourceGroups	菜单分组	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 14:06:24.545333	2	0	\N	\N	\N
55	com.tuna.knowledgeBase.entity.KnowledgeFile	knowledge-base-service	{"zh_CN":"KnowledgeFile"}	/sapi/knowledgeFiles	知识文件	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-01 15:12:02.079694	2	0	\N	\N	knowledge_file
66	com.tuna.productAssistant.entity.VinGenerateRequest	product-assistant-service	{"zh_CN":"VinGenerateRequest"}	/sapi/vinGenerateRequests	车辆编号生成申请表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	vin_generate_request
56	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	knowledge-base-service	{"zh_CN":"KnowledgeFileHistory"}	/sapi/knowledgeFileHistorys	文件变更历史	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:25:41.537804	3	0	\N		knowledge_file_history
67	com.tuna.productAssistant.entity.ModelEnvPubNo	product-assistant-service	{"zh_CN":"ModelEnvPubNo"}	/sapi/modelEnvPubNos	车型环保公开号对照表	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.219629	\N	\N	\N	1	0	\N	\N	model_env_pub_no
68	com.tuna.productAssistant.entity.QrSyncJob	product-assistant-service	{"zh_CN":"QrSyncJob"}	/sapi/qrSyncJobs	大田农机二维码同步任务	name	160	\N	\N	50%	\N	1	0	1	\N	\N	0	\N	0	\N	product-assistant-service	product-assistant-service	2026-06-12 09:25:03.220633	\N	\N	\N	1	0	\N	\N	qr_sync_job
36	com.tuna.platform.oauth2.entity.Oauth2RegisteredClientOpenapi	platform-service	{"zh_CN":"Oauth2RegisteredClientOpenapi"}	\N	Oauth2RegisteredClientOpenapi	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.75706	\N	\N	\N	1	0	\N	\N	\N
37	com.tuna.platform.entity.Dictionary	platform-service	{"zh_CN":"Dictionary"}	/sapi/dictionarys	字典定义表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.758037	\N	\N	\N	1	0	\N	\N	\N
38	com.tuna.platform.entity.Role	platform-service	{"zh_CN":"Role"}	/sapi/roles	{"zh_CN":"系统角色","en":"System Role"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.759013	\N	\N	\N	1	0	\N	\N	\N
40	com.tuna.platform.entity.Resource	platform-service	{"zh_CN":"Resource"}	/sapi/resources	{"zh_CN":"系统菜单","en":"System Menu"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.760016	\N	\N	\N	1	0	\N	\N	\N
41	com.tuna.platform.entity.LoginLog	platform-service	{"zh_CN":"LoginLog"}	/sapi/loginLogs	{"zh_CN":"登录日志","en":"Login log"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.760016	\N	\N	\N	1	0	\N	\N	\N
42	com.tuna.platform.entity.UserRole	platform-service	{"zh_CN":"UserRole"}	\N	平台角色授权表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.760016	\N	\N	\N	1	0	\N	\N	\N
48	com.tuna.platform.entity.I18n	platform-service	{"zh_CN":"I18n"}	/sapi/i18ns	平台国际化翻译表	name	160	\N	/views/template/TunaFormilyForm.vue	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-11 14:45:25.377408	14	0	\N	\N	\N
45	com.tuna.platform.oauth2.entity.Oauth2Authorization	platform-service	{"zh_CN":"Oauth2Authorization"}	/sapi/oauth2Authorizations	Oauth2Authorization	name	0	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:15:27.002922	3	0	\N	\N	\N
43	com.tuna.platform.entity.User	platform-service	{"zh_CN":"User"}	/sapi/users		name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 15:46:57.869951	3	0	\N	\N	\N
44	com.tuna.platform.entity.SchemaDefine	platform-service	{"zh_CN":"字段定义"}	/sapi/fieldDefines	列配置表（存储业务实体字段的展示/校验/表单等配置）	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47.761008	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 11:42:47.086029	2	0	\N	\N	\N
46	com.tuna.platform.oauth2.entity.Oauth2RegisteredClient	platform-service	{"zh_CN":"Oauth2RegisteredClient"}	/sapi/oauth2RegisteredClients	{"zh_CN":"应用认证","en":"OAuth2 application"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:52:06.928357	2	0	\N	\N	\N
39	com.tuna.platform.entity.EntityDefine	platform-service	{"zh_CN":"模型定义"}	/sapi/common/com.tuna.platform.entity.EntityDefine	平台模型定义	name	160	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 10:41:52.267055	31	\N	\N	\N	platform_entity_define
51	com.tuna.platform.entity.DictionaryItem	platform-service	{"zh_CN":"DictionaryItem"}	/sapi/dictionaryItems	字典项目定义表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:49:04.219655	2	0	\N	\N	\N
50	com.tuna.platform.entity.ResourceCategory	platform-service	{"zh_CN":"ResourceCategory"}	/sapi/resourceCategorys	{"zh_CN":"菜单分类","en":"Menu Category"}	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:49:12.09688	2	0	\N	\N	\N
49	com.tuna.platform.entity.RoleResource	platform-service	{"zh_CN":"RoleResource"}	\N	平台资源角色授权表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:49:35.707392	2	0	\N	\N	\N
47	com.tuna.platform.entity.ResourceOpenapi	platform-service	{"zh_CN":"ResourceOpenapi"}	/sapi/resourceOpenapis	OAuth2客户端与平台资源的授权关联表	name	160	\N	\N	50%	0	1	0	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 09:59:41.618411	5	0	\N	\N	platform_resource_openapi
35	com.tuna.platform.entity.ServiceInstance	platform-service	{"zh_CN":"服务实例"}	/sapi/common/com.tuna.platform.entity.ServiceInstance	平台服务定义表	name	160	\N	\N	50%	0	1	1	1	\N	\N	0	\N	0	\N	platform-service	platform-service	2026-05-27 16:19:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-04 10:41:47.405479	8	0	\N	\N	platform_service_instance
\.


--
-- Data for Name: platform_group_i18n; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_group_i18n (id, group_id, code, name, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	5	123	{"zh_CN":"1123"}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:59:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 10:12:03.584242	2
8	8	3	{"zh_CN":"1"}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 10:21:40.869545	\N	\N	\N	1
9	1	ss	{"zh_CN":"ss"}	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 13:48:03.050195	\N	\N	\N	1
\.


--
-- Data for Name: platform_job; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_job (id, code, org_id, status, name, remark, show_order, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
\.


--
-- Data for Name: platform_job_resource; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_job_resource (job_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_job_user; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_job_user (user_id, job_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_login_log; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_login_log (id, type, username, real_name, ip_address, session_id, remark, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_openapi; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_openapi (id, service_id, name, http_method, class_method, uri, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
2	3	1	1	1	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:29:48.88457	\N	\N	\N	1
\.


--
-- Data for Name: platform_org; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_org (id, code, parent_id, status, name, remark, show_order, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
\.


--
-- Data for Name: platform_org_resource; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_org_resource (org_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_realm; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_realm (id, code, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
1	lgag	临工农机	\N	1	admin	2026-03-02 09:14:48.618331	\N	\N	\N	1
\.


--
-- Data for Name: platform_resource; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_resource (id, code, parent_id, group_id, name, icon, visible, type, open_tab, affix, keep_alive, show_order, layout, component, router_url, redirect_url, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
7	PlatformResource	4	5	{"zh_CN":"平台菜单","en":"System menu"}	\N	1	1	0	0	0	2	\N	/views/microapp/developConfig/resource/ResourceList.vue	PlatformResource	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-20 15:32:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 16:33:36.652192	8
9	SchemaDefineList	4	5	{"zh_CN":"国际化配置","en":"Multi language config"}	\N	1	1	0	0	0	10	\N	/views/template/TunaObjectList.vue	I18nDefine?className=com.tuna.platform.entity.I18n&queryParams=%7B%22descColumns%22%3A%5B%22updatedTime%22%2C+%22createdTime%22%5D%7D	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-20 15:43:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 16:55:45.669687	19
11	qrSyncJob	2	1	{"zh_CN":"同步记录","en":"QRCode sync job"}	\N	1	1	0	0	0	2	\N	/views/microapp/productAssistant/daTianSyncJob/QrSyncJobList.vue	qrSyncJob	\N	\N	test	管理员	2026-03-25 16:09:27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:36:55.779107	6
12	printRecord	2	1	{"zh_CN":"打印记录","en":"Nameplate print record"}	\N	1	1	0	0	0	3	\N	/views/microapp/productAssistant/printRecord/PrintRecordList.vue	printRecord	\N	\N	test	管理员	2026-03-26 10:44:21	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:37:13.29836	3
13	ModelEnvPubNo	2	1	{"zh_CN":"环保公开信息","en":"Model env no"}	\N	1	1	0	0	0	4	\N	/views/microapp/productAssistant/modelEnvPubNo/ModelEnvPubNoList.vue	modelEnvPubNo	\N	\N	test	管理员	2026-03-29 10:49:52	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:37:35.111835	3
5	ResourceGroup	4	5	{"zh_CN":"领域组","en":"Domain group"}	\N	1	1	0	0	0	1	\N	/views/microapp/developConfig/resource/ResourceGroupList.vue	ResourceGroup	\N	\N	test	管理员	2026-03-19 09:52:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 15:00:10.378816	8
17	VinGenerateRequest	2	1	{"zh_CN":"出厂编号审核","en":"Vin generate request"}	\N	1	1	0	1	1	5	\N	/views/microapp/productAssistant/vinGenerateRequest/VinGenerateRequestList.vue	VinGenerateRequest	\N	\N	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-06 14:17:38	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:37:48.455708	2
75	com.tuna.platform.vo.PersonalSettingsVO.RoleAssign	16	5	{"zh_CN":"角色分配","en":"Role assign"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 16:51:55.379145	\N	\N	\N	1
8	SchemaDefine	4	5	{"zh_CN":"字段定义","en":"Column Define"}	\N	1	1	0	0	0	5	\N	/views/microapp/developConfig/fieldDefine/SchemaDefineList.vue	SchemaDefine	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-20 15:40:24	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:59:39.589958	10
18	VinPrinter	2	1	{"zh_CN":"出厂编号打印","en":"Vin printer"}	\N	1	1	0	0	0	6	\N	/views/microapp/productAssistant/vinGenerateRequest/VinGeneratePrintList.vue	VinPrinter	\N	\N	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-08 08:41:08	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:38:07.186623	3
76	com.tuna.platform.vo.PersonalSettingsVO.Enable	16	5	{"zh_CN":"禁用/启用","en":"Enable/Disable"}	\N	1	2	0	0	0	7	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 16:54:04.10273	\N	\N	\N	1
70	com.tuna.platform.entity.ResourceGroup.Add	5	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.527858	\N	\N	\N	1
71	com.tuna.platform.entity.ResourceGroup.Edit	5	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.527858	\N	\N	\N	1
2	namePlatPrint	\N	1	{"zh_CN":"铭牌打印","en":"Nameplate print"}	Postcard	1	0	0	1	1	1	AsideMain	\N	/supplyChain/productAssistant/namePlatePrint	/supplyChain/productAssistant/namePlatRecord	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-17 16:59:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:36:20.272787	11
3	namePlatRecord	2	1	{"zh_CN":"铭牌记录","en":"Nameplate print records"}	\N	1	1	0	0	0	1	AsideMain	/views/microapp/productAssistant/namePlatePrint/NamePlateRecordList.vue	namePlatRecord	\N	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-17 17:12:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:36:35.724222	6
77	com.tuna.platform.entity.Role.Permission	15	5	{"zh_CN":"权限分配","en":"Permission assign"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 17:01:27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 17:02:12.50014	2
54	com.tuna.platform.entity.Role.Export	15	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:44:54.489812	2
72	com.tuna.platform.entity.ResourceGroup.Delete	5	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.528832	\N	\N	\N	1
16	UserList	14	5	{"zh_CN":"用户账号","en":"User account"}	\N	1	1	0	0	0	1	\N	/views/microapp/systemManage/user/UserList.vue	UserList	\N	\N	test	管理员	2026-04-02 15:33:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-18 14:20:36.114245	13
15	RoleList	14	5	{"zh_CN":"系统角色","en":"System role"}	\N	1	1	0	0	0	2	\N	/views/microapp/systemManage/role/RoleList.vue	RoleList	\N	\N	test	管理员	2026-04-02 08:31:48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-18 14:09:10.083289	7
73	com.tuna.platform.entity.ResourceGroup.Import	5	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09.528832	\N	\N	\N	1
74	com.tuna.platform.entity.ResourceGroup.Export	5	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:59:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:01.426741	2
59	com.tuna.platform.entity.Resource.Export	7	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:06.853355	2
79	com.tuna.knowledgeBase.entity.KnowledgeFile.Edit	28	8	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:45:17.583486	\N	\N	\N	1
80	com.tuna.knowledgeBase.entity.KnowledgeFile.Delete	28	8	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:45:17.58513	\N	\N	\N	1
4	DevelopConfig	\N	5	{"zh_CN":"开发配置","en":"Developer config"}	<svg t="1775096517850" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7693" width="200" height="200"><path d="M885.9 63.9H139.6C98.4 63.9 65 97.3 65 138.5v559.8c0 41.2 33.4 74.6 74.6 74.6h261.2v112h-74.6c-20.6 0-37.3 16.7-37.3 37.3 0 20.6 16.7 37.3 37.3 37.3h373.2c20.6 0 37.3-16.7 37.3-37.3 0-20.6-16.7-37.3-37.3-37.3h-74.6V773H886c41.2 0 74.6-33.4 74.6-74.6V138.6c0-41.2-33.5-74.7-74.7-74.7z m-335.8 821h-74.6V773h74.6v111.9z m335.8-186.6H139.6V138.6h746.3v559.7zM689.1 350.6l86.3 72.1-87.7 84.9c-10.7 10.4-11 27.4-0.6 38.1 5.3 5.4 12.3 8.2 19.4 8.2 6.8 0 13.5-2.5 18.8-7.6l109.2-105.7c5.5-5.3 8.5-12.7 8.2-20.3-0.3-7.6-3.8-14.8-9.6-19.6l-109.2-91.3c-11.4-9.5-28.4-8-38 3.3-9.7 11.4-8.2 28.4 3.2 37.9zM300.3 546.3c5.2 5.1 12 7.6 18.8 7.6 7 0 14.1-2.7 19.4-8.2 10.4-10.7 10.1-27.7-0.6-38.1l-87.8-84.9 86.3-72.1c11.4-9.5 12.9-26.5 3.4-37.9-9.6-11.4-26.6-12.9-38-3.3l-109.2 91.3c-5.9 4.9-9.4 12-9.6 19.6-0.3 7.6 2.7 15 8.2 20.3l109.1 105.7z m143.7 4.1c4 2.3 8.6 3.5 13.2 3.5 9.4 0 18.6-4.9 23.6-13.7l111-197c7.3-13 2.7-29.4-10.3-36.6-13-7.3-29.4-2.7-36.7 10.3l-111 197c-7.4 12.8-2.7 29.2 10.2 36.5z" p-id="7694"></path></svg>	1	0	0	0	0	10	AsideMain	\N	/PlatfomDevOps/SystemManage/DevelopConfig	/PlatfomDevOps/SystemManage/DevelopConfig/resourceGroups	\N	test	管理员	2026-03-19 09:49:21	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:26:38.353981	12
50	com.tuna.platform.entity.Role.Add	15	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.474716	\N	\N	\N	1
154	com.tuna.platform.entity.Openapi.Import	140	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-22 11:42:05.822101	\N	\N	\N	1
155	com.tuna.platform.entity.Openapi.Export	140	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-22 11:42:05.841234	\N	\N	\N	1
51	com.tuna.platform.entity.Role.Edit	15	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.475703	\N	\N	\N	1
52	com.tuna.platform.entity.Role.Delete	15	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.475703	\N	\N	\N	1
53	com.tuna.platform.entity.Role.Import	15	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:54:37.475703	\N	\N	\N	1
14	AccountManage	\N	5	{"zh_CN":"账号管理","en":"Account manage"}	<svg t="1775096100110" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6429" width="200" height="200"><path d="M727.4 506a208 208 0 1 0-238.8 0A288.4 288.4 0 0 0 320 768v64a64.1 64.1 0 0 0 64 64h448a64.1 64.1 0 0 0 64-64v-64a288.4 288.4 0 0 0-168.6-262z m-263.4-170A144 144 0 1 1 608 480a144.2 144.2 0 0 1-144-144z m368 496H384v-64a224 224 0 0 1 448 0zM256 832a64.1 64.1 0 0 1-64-64V320a64.1 64.1 0 0 1 64-64h96a32 32 0 0 0 0-64h-96A128.1 128.1 0 0 0 128 320v448a128.1 128.1 0 0 0 128 128 32 32 0 1 0 0-64z" fill="currentColor" p-id="6430"></path><path d="M256 448h32a32 32 0 0 0 0-64h-32a32 32 0 0 0 0 64zM256 576H320a32 32 0 1 0 0-64h-64a32 32 0 1 0 0 64z" fill="currentColor" p-id="6431"></path></svg>	1	0	0	0	0	1	\N	\N	/PlatfomDevOps/SystemManage/AccountManage	\N	\N	test	管理员	2026-04-02 08:30:10	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:24:43.672762	9
47	com.tuna.platform.vo.PersonalSettingsVO.Delete	16	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:53:34.10365	\N	\N	\N	1
48	com.tuna.platform.vo.PersonalSettingsVO.Import	16	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:53:34.106711	\N	\N	\N	1
55	com.tuna.platform.entity.Resource.Add	7	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.70819	\N	\N	\N	1
56	com.tuna.platform.entity.Resource.Edit	7	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.709176	\N	\N	\N	1
57	com.tuna.platform.entity.Resource.Delete	7	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.710161	\N	\N	\N	1
58	com.tuna.platform.entity.Resource.Import	7	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:56:28.710161	\N	\N	\N	1
60	com.tuna.platform.entity.SchemaDefine.Add	8	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.828206	\N	\N	\N	1
61	com.tuna.platform.entity.SchemaDefine.Edit	8	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.829193	\N	\N	\N	1
62	com.tuna.platform.entity.SchemaDefine.Delete	8	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.829193	\N	\N	\N	1
63	com.tuna.platform.entity.SchemaDefine.Import	8	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33.830701	\N	\N	\N	1
65	com.tuna.platform.entity.I18n.Add	9	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.479291	\N	\N	\N	1
66	com.tuna.platform.entity.I18n.Edit	9	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.480263	\N	\N	\N	1
67	com.tuna.platform.entity.I18n.Delete	9	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.481246	\N	\N	\N	1
68	com.tuna.platform.entity.I18n.Import	9	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25.482217	\N	\N	\N	1
64	com.tuna.platform.entity.SchemaDefine.Export	8	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:57:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:11.952761	2
69	com.tuna.platform.entity.I18n.Export	9	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:58:25	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 08:45:16.68134	2
78	com.tuna.knowledgeBase.entity.KnowledgeFile.Add	28	8	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:45:17.572515	\N	\N	\N	1
49	com.tuna.platform.vo.PersonalSettingsVO.Export	16	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:53:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 15:46:54.578711	2
140	Openapi	4	5	{"zh_CN":"函数定义","en":"Openapi"}	\N	1	1	0	0	0	6	\N	/views/template/TunaObjectList.vue	Openapi?className=com.tuna.platform.entity.Openapi	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 13:37:00.153573	17
83	com.tuna.knowledgeBase.entity.KnowledgeFile.Viewer	28	8	{"zh_CN":"查看"}	\N	1	2	0	0	0	0	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:33.982044	\N	\N	\N	1
85	com.tuna.productAssistant.entity.NameplateRecord.viewer	3	1	{"zh_CN":"查看"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:37:29.539113	\N	\N	\N	1
84	com.tuna.productAssistant.entity.NameplateRecord.edit	3	1	{"zh_CN":"编辑"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:36:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:37:39.764119	2
86	com.tuna.productAssistant.entity.NameplateRecord.Import	3	1	{"zh_CN":"导入"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 16:29:16.585662	\N	\N	\N	1
27	KnowledgeBase	\N	8	{"zh_CN":"知识库","en":"Knowledge Base","fr":"Base de connaissances","de":"Wissensdatenbank","it":"Base di conoscenza","ja":"知識ベース","ko":"지식 기반","ru":"База знаний","pt_br":"Base de conhecimento","pt":"Base de conhecimento","es":"Base de conocimiento"}	Memo	1	0	0	0	0	1	\N	\N	/Sales/Knowledge/Base	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 09:41:25	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:29:34.31232	6
28	KnowledgeFiles	27	8	{"zh_CN":"知识文档","en":"Knowledge Document","fr":"Document de connaissances","de":"Wissensdokument","it":"Documento di conoscenza","ja":"知識文書","ko":"지식 문서","ru":"Документ знаний","pt_br":"Documento de conhecimento","pt":"Documento de conhecimento","es":"Documento de conocimiento"}	\N	1	1	0	0	0	100	\N	/views/microapp/knowledgeBase/knowledgeFile/KnoledgeFileList.vue	KnowledgeFiles	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 09:52:26	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:29:54.19544	3
157	com.tuna.platform.entity.ServiceInstance.Export	125	5	{"zh_CN":"导出","en":"Export"}	\N	1	2	0	0	0	6	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:05:59.890643	\N	\N	\N	1
132	Oauth2Authorization	14	5	{"zh_CN":"Token日志","en":"Token Log"}	\N	1	1	0	0	0	4	\N	/views/template/TunaObjectList.vue	Oauth2Authorization?className=com.tuna.platform.oauth2.entity.Oauth2Authorization&titleColumnName=principalName&queryParams=%7B%22descColumns%22%3A%5B%22authorizationCodeIssuedAt%22%2C%22accessTokenIssuedAt%22%2C+%22refreshTokenIssuedAt%22%5D%7D	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 10:10:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 17:31:19.54183	10
147	EntityDefine	4	5	{"zh_CN":"模型定义"}	\N	1	1	0	0	0	4	\N	/views/template/TunaObjectList.vue	EntityDefine?baseUrl=%2FplatformSvr%2Fsapi%2FentityDefines&context=platformSvr&className=com.tuna.platform.entity.EntityDefine&queryParams=%7B%22descColumns%22%3A%5B%22updatedTime%22%2C+%22createdTime%22%5D%7D	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:22:54	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:59:32.603052	4
156	com.tuna.platform.entity.ServiceInstance.Import	125	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:05:59.871905	\N	\N	\N	1
93	com.tuna.platform.vo.PersonalSettingsVO.Add	16	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:53.075245	2
94	com.tuna.platform.vo.PersonalSettingsVO.Edit	16	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:56:59.104507	2
98	com.tuna.platform.entity.LoginLog.View	87	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 15:54:31.922887	\N	\N	\N	1
133	com.tuna.platform.oauth2.entity.Oauth2Authorization.View	132	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 10:11:15.2256	\N	\N	\N	1
87	LoginLogList	14	5	{"zh_CN":"登录日志","en":"Login log"}	\N	1	1	0	0	0	3	\N	/views/microapp/systemManage/loginLog/LoginLogList.vue	LoginLogList	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:53:37	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 15:51:40.892335	4
148	com.tuna.platform.entity.EntityDefine.View	147	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.224266	\N	\N	\N	1
149	com.tuna.platform.entity.EntityDefine.Add	147	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.234131	\N	\N	\N	1
142	com.tuna.platform.entity.Openapi.Add	140	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.232374	\N	\N	\N	1
143	com.tuna.platform.entity.Openapi.Edit	140	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.244717	\N	\N	\N	1
144	com.tuna.platform.entity.Openapi.Delete	140	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.25211	\N	\N	\N	1
150	com.tuna.platform.entity.EntityDefine.Edit	147	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.244942	\N	\N	\N	1
151	com.tuna.platform.entity.EntityDefine.Delete	147	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-21 16:24:28.252901	\N	\N	\N	1
104	PlatformConfig	\N	5	{"zh_CN":"平台配置","en":"Platform Config"}	<svg t="1778057163023" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2813" width="200" height="200"><path d="M869.934545 62.138182H153.832727a104.727273 104.727273 0 0 0-104.727272 104.727273v512a104.96 104.96 0 0 0 104.96 104.96H477.090909v108.218181H234.123636a34.909091 34.909091 0 0 0 0 69.818182H791.272727a34.909091 34.909091 0 0 0 0-69.818182H546.909091v-107.985454h323.025454a104.96 104.96 0 0 0 104.96-104.96v-512a104.96 104.96 0 0 0-104.96-104.96z m35.141819 616.96a35.141818 35.141818 0 0 1-35.141819 35.141818H154.065455a35.141818 35.141818 0 0 1-35.141819-35.141818v-512a34.909091 34.909091 0 0 1 34.909091-34.909091h716.101818a34.909091 34.909091 0 0 1 35.141819 34.909091z" p-id="2814"></path><path d="M546.909091 434.734545v-166.632727a34.909091 34.909091 0 1 0-69.818182 0v166.4a62.836364 62.836364 0 0 0 0 104.727273v48.64a34.909091 34.909091 0 1 0 69.818182 0v-49.105455a62.138182 62.138182 0 0 0 27.694545-51.898181 62.836364 62.836364 0 0 0-27.694545-52.13091zM327.912727 316.741818v-48.64a34.909091 34.909091 0 0 0-69.818182 0v49.105455a62.603636 62.603636 0 0 0 0 104.029091v166.632727a34.909091 34.909091 0 0 0 69.818182 0v-166.167273a63.069091 63.069091 0 0 0 0-104.96zM772.189091 316.741818v-48.64a34.909091 34.909091 0 0 0-69.818182 0v49.105455a62.603636 62.603636 0 0 0 0 104.029091v166.632727a34.909091 34.909091 0 0 0 69.818182 0v-166.167273a63.069091 63.069091 0 0 0 0-104.96z" p-id="2815"></path></svg>	1	0	0	0	0	2	\N	\N	/PlatfomDevOps/SystemManage/PlatformConfig	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:47:18	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:47:47.447658	2
105	DictionaryList	104	5	{"zh_CN":"数据字典","en":"Data dictionary"}	\N	1	1	0	0	0	1	\N	/views/microapp/platformConfig/dictionary/DictionaryList.vue	DictionaryList	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:56:02.543317	\N	\N	\N	1
125	ServiceInstance	4	5	{"zh_CN":"服务实例","en":"Micro Service"}	\N	1	1	0	0	0	3	\N	/views/template/TunaObjectList.vue	ServiceInstance?className=com.tuna.platform.entity.ServiceInstance&queryParams=%7B%22descColumns%22%3A%5B%22updatedTime%22%2C%22createdTime%22%5D%7D	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 14:15:05	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:59:26.398179	21
106	com.tuna.platform.entity.Dictionary.View	105	5	{"zh_CN":"查看字典","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:03.203199	2
112	com.tuna.platform.entity.DictionaryItem.View	105	5	{"zh_CN":"查看字典项","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:43	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:13.15931	2
107	com.tuna.platform.entity.Dictionary.Add	105	5	{"zh_CN":"添加字典","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:22.083635	2
113	com.tuna.platform.entity.DictionaryItem.Add	105	5	{"zh_CN":"添加字典项","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:30.267839	2
108	com.tuna.platform.entity.Dictionary.Edit	105	5	{"zh_CN":"编辑字典","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:39.522035	2
114	com.tuna.platform.entity.DictionaryItem.Edit	105	5	{"zh_CN":"编辑字典项","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:47.712537	2
109	com.tuna.platform.entity.Dictionary.Delete	105	5	{"zh_CN":"删除字典","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 17:17:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:31:53.424521	2
115	com.tuna.platform.entity.DictionaryItem.Delete	105	5	{"zh_CN":"删除字典项","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:30:44	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 15:32:00.771017	2
118	Oauth2RegisteredClient	104	5	{"zh_CN":"应用认证","en":"OAuth2 Application"}	\N	1	1	0	0	0	2	\N	/views/microapp/platformConfig/oauth2Client/Oauth2ClientList.vue	Oauth2RegisteredClient	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:55:19.491091	\N	\N	\N	1
119	com.tuna.platform.oauth2.entity.Oauth2RegisteredClient.View	118	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.437819	\N	\N	\N	1
120	com.tuna.platform.oauth2.entity.Oauth2RegisteredClient.Add	118	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.445245	\N	\N	\N	1
121	com.tuna.platform.oauth2.entity.Oauth2RegisteredClient.Edit	118	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.448174	\N	\N	\N	1
122	com.tuna.platform.oauth2.entity.Oauth2RegisteredClient.Delete	118	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.45262	\N	\N	\N	1
123	com.tuna.platform.oauth2.entity.Oauth2RegisteredClient.Import	118	5	{"zh_CN":"导入","en":"Import"}	\N	1	2	0	0	0	5	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-08 09:58:16.455541	\N	\N	\N	1
127	com.tuna.platform.entity.ServiceInstance.Add	125	5	{"zh_CN":"添加","en":"Add"}	\N	1	2	0	0	0	2	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.52868	\N	\N	\N	1
126	com.tuna.platform.entity.ServiceInstance.View	125	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.513856	\N	\N	\N	1
128	com.tuna.platform.entity.ServiceInstance.Edit	125	5	{"zh_CN":"编辑","en":"Edit"}	\N	1	2	0	0	0	3	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.537504	\N	\N	\N	1
129	com.tuna.platform.entity.ServiceInstance.Delete	125	5	{"zh_CN":"删除","en":"Delete"}	\N	1	2	0	0	0	4	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-13 15:02:48.544905	\N	\N	\N	1
141	com.tuna.platform.entity.Openapi.View	140	5	{"zh_CN":"查看","en":"View"}	\N	1	2	0	0	0	1	\N	\N	\N	\N	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-20 11:53:52.21909	\N	\N	\N	1
\.


--
-- Data for Name: platform_resource_category; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_resource_category (id, code, name, show_order, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, parent_id) FROM stdin;
65530	SupplyChain	{"zh_CN":"供应链","en":"Supply chain"}	50	\N	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-17 14:55:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:41:24.826712	4	\N
65532	platformDevOps	{"en":"Platform operation","zh_CN":"平台运营"}	1000	\N	test	管理员	2026-04-01 14:01:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:42:01.564516	3	\N
65534	MarketingBase	{"zh_CN":"常用","en":"MarketingBase","fr":"Courant","de":"Gebräuchlich","it":"Comune","ja":"よく使う","ko":"자주 사용함","ru":"Обычный","pt_br":"Comum","pt":"Comum","es":"Común"}	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 14:42:57	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:41:10.010715	3	65531
65531	Sale	{"en":"Marketing","zh_CN":"营销","fr":"Marketing","de":"Marketing","it":"Marketing","ja":"マーケティング","ko":"마케팅","ru":"Маркетинг","pt_br":"Marketing","pt":"Marketing","es":"Marketing"}	51	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-16 10:22:53	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:42:14.376792	3	\N
65535	SystemManage	{"zh_CN":"平台运维","en":"Platform operations"}	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 14:57:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:52:13.517802	4	65532
65533	Production	{"en":"Production","zh_CN":"生产"}	1	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 14:27:08	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 16:54:37.51001	4	65530
\.


--
-- Data for Name: platform_resource_group; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_resource_group (id, category_id, code, name, show_order, router, navbar_menu, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, entry, props, is_microapp) FROM stdin;
8	65534	KnowledgeBase	{"zh_CN":"知识库","en":"Knowledge Base","fr":"Base de connaissances","de":"Wissensdatenbank","it":"Base di conoscenze","ja":"知識ベース","ko":"지식 베이스","ru":"База знаний","pt_br":"Base de conhecimento","pt":"Base de conhecimento","es":"Base de conocimientos"}	3	\N	1	//{host}/knowledge-base/index.html?v=1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-16 17:13:36	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-10 16:24:44.84789	18	//{host}/knowledge-base/index.html?v=1	\N	1
1	65533	ProductAssistant	{"zh_CN":"生产辅助","en":"Knowledge Base","fr":"Assistance à la production","de":"Produktionsunterstützung","it":"Supporto alla produzione","ja":"生産補助","ko":"생산 보조","ru":"Вспомогательное производство","pt_br":"Auxiliar de produção","pt":"Auxiliar de produção","es":"Auxiliar de producción"}	1	/supplyChain/productAssistant1	1	//{host}/product-assistant/index.html	fa1a2cec-9686-4b05-978f-54e3746660c6	测试	2026-03-17 16:49:30	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 10:17:13.734859	89	//{host}/product-assistant/index.html	\N	1
5	65535	SystemManage	{"zh_CN":"平台管理","en":"Platform Management","fr":"Gestion du système","de":"Systemverwaltung","it":"Gestione del sistema","ja":"システム管理","ko":"시스템 관리","ru":"Управление системой","pt_br":"Gerenciamento do sistema","pt":"Gestão do sistema","es":"Gestión del sistema"}	100	/PlatfomDevOps/SystemManage/AccountManage	1	//tuna.net/platformSvr/common/index.html	test	管理员	2026-04-01 14:49:19	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-12 11:46:25.363723	57	//localhost:7998/platformSvr/common	\N	1
\.


--
-- Data for Name: platform_resource_openapi; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_resource_openapi (resource_id, openapi_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_role; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_role (id, code, status, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
5	knowledgeBaseAdmin	1	{"zh_CN":"知识库管理员","en":"Knowledge manager"}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-17 09:52:57	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:25:57.73105	3
2	vinAdmin	1	{"en":"Vin admin","zh_CN":"车架号管理员"}	\N	test	管理员	2026-04-02 10:45:47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:26:47.157202	3
6	knowledgeViewer	1	{"zh_CN":"知识库使用者","en":"Knowledge Base Viewer"}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:41:29.013763	\N	\N	\N	1
3	vinPrinter	1	{"zh_CN":"出厂编号打印人员","en":"Vin printer"}	\N	test	管理员	2026-04-02 10:46:18	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:38:09.94523	5
7	vinViewer	1	{"zh_CN":"环保数据导出","en":"EnviromentExporter"}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:38:49	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-28 13:44:25.883528	2
1	admin	1	{"zh_CN":"超级管理员","en":"Admin"}	\N	test	管理员	2026-04-02 10:45:11	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-28 19:08:25.93904	6
\.


--
-- Data for Name: platform_role_resource; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_role_resource (role_id, resource_id, creator_id, created_by, created_time) FROM stdin;
2	17	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.136526
2	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.148326
2	18	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.151187
2	3	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.152194
2	84	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.152745
2	85	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.155164
2	86	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.155964
2	11	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.156382
2	12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.158216
2	13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 15:02:04.159361
1	128	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.99467
1	129	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.998624
1	132	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	4	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	133	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	5	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	7	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	8	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:16.9996
1	9	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	140	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	141	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	14	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	142	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.000575
1	15	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.001554
1	143	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.001554
1	16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.001554
3	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 08:27:17.711065
3	18	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 08:27:17.712041
1	144	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	147	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	148	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	149	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.002529
1	150	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	151	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	154	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	155	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	156	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.003505
1	157	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	47	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	48	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	49	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	50	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	51	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	52	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.004482
1	53	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.005458
1	54	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.005458
1	55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.005458
1	56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	57	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	58	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
5	80	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.966659
5	27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.967558
5	28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.968968
5	78	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.969249
5	79	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:49:25.96946
7	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:39:04.019426
7	3	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:39:04.030448
7	85	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:39:04.031513
1	60	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	61	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	62	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.006434
1	63	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.007945
1	64	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.008935
1	65	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.008935
1	66	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.008935
1	67	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	68	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	69	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	70	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	71	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.009911
1	72	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	73	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	74	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	75	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.010887
1	76	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	77	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	87	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	93	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	94	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	98	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.011864
1	104	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	105	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	106	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	107	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	108	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	109	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	112	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	113	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.012841
1	114	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.014344
1	115	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.014344
1	118	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.014344
1	119	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
6	83	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:51.25078
6	27	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:51.252756
6	28	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 13:52:51.253653
1	120	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	121	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	122	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	123	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	125	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.01486
1	126	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.015852
1	127	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:06:17.015852
\.


--
-- Data for Name: platform_schema_define; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_schema_define (id, class_name, name, cn_name, i18n_key, display_name, type, type_class_name, is_show, is_sort, is_overflow, is_search, is_fixed, is_require, is_form, align, width, show_order, max_length, min_length, max, min, "precision", multiple, pattern, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version, service_name, is_i18n, relation_type, external_class, external_class_primary_field, external_class_display_field, ui_component, field_name, description, encrypt, desensitize, field_component, inner_primary_field, column_component) FROM stdin;
353	com.tuna.enums.Delete	NORMAL	\N	\N	{"zh_CN":"正常","en":"Normal"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:30	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:39.284414	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
329	com.tuna.platform.vo.PersonalSettingsVO	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.890665	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
354	com.tuna.enums.Delete	DELETE	\N	\N	{"zh_CN":"已删除","en":"Deleted"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:30	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:57.345081	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
357	com.tuna.enums.Gender	WOMAN	\N	\N	{"zh_CN":"女性","en":"Woman"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:39	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:46.91152	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
358	com.tuna.enums.Gender	MAN	\N	\N	{"zh_CN":"男性","en":"Man"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:39	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:52.576776	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
360	com.tuna.enums.ValidatorPattern	PASSWORD	\N	\N	{"zh_CN":"密码","en":"Password"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:22.764699	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
236	com.tuna.platform.entity.ResourceCategory	updaterId	更新人ID	com.tuna.entity.AuditEntity.updaterId	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.956105	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
292	com.tuna.platform.entity.Role	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.415001	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
237	com.tuna.platform.entity.ResourceCategory	updatedBy	更新人	com.tuna.entity.AuditEntity.updatedBy	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.963965	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
231	com.tuna.platform.entity.ResourceCategory	showOrder	显示顺序	com.tuna.platform.entity.ResourceCategory.showOrder	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	0	0	0	0	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:38.314847	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
233	com.tuna.platform.entity.ResourceCategory	creatorId	创建人ID	com.tuna.entity.ImmutablyEntity.creatorId	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.930885	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
234	com.tuna.platform.entity.ResourceCategory	createdBy	创建人	com.tuna.entity.ImmutablyEntity.createdBy	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.940263	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
458	com.tuna.platform.vo.PersonalSettingsVO	extendMap	\N	\N	{"zh_CN":"extendMap"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 09:21:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 08:22:43.897571	2	platform-service	0	\N	\N	id	name	1			0	0	\N	\N	\N
285	com.tuna.platform.entity.Role	status	\N	\N	{"zh_CN":"状态","en":"Status"}	9	com.tuna.enums.Enable	1	0	0	1	0	1	1	0	0	4	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:34.743962	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
235	com.tuna.platform.entity.ResourceCategory	createdTime	创建时间	com.tuna.entity.ImmutablyEntity.createdTime	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.94826	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
229	com.tuna.platform.entity.ResourceCategory	code	编码	com.tuna.platform.entity.ResourceCategory.code	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	0	0	0	0	0	1	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:07.616567	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
232	com.tuna.platform.entity.ResourceCategory	remark	备注	com.tuna.platform.entity.ResourceCategory.remark	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	0	0	0	0	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:49.12287	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
228	com.tuna.platform.entity.ResourceCategory	parentId	资源分类	com.tuna.platform.entity.ResourceCategory.parentId	{"zh_CN":"上级分类","en":"Parent category"}	3	java.lang.Long	1	0	0	0	0	0	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:04:55.018165	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
298	com.tuna.platform.vo.PersonalSettingsVO	orgId	\N	\N	{"zh_CN":"组织编号","en":"Org id"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:01.709383	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
227	com.tuna.platform.entity.ResourceCategory	id	编号	com.tuna.entity.Entity.id	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.888567	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
305	com.tuna.platform.vo.PersonalSettingsVO	deptId	\N	\N	{"zh_CN":"部门编号","en":"Dept id"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:13.568177	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
286	com.tuna.platform.entity.Role	remark	\N	\N	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	5	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:33.346797	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
239	com.tuna.platform.entity.ResourceCategory	version	乐观锁	com.tuna.entity.AuditEntity.version	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.972321	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
241	com.tuna.platform.entity.Resource	code	编码	com.tuna.platform.entity.Resource.code	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	200	101	150	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:35:35.430512	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
254	com.tuna.platform.entity.Resource	routerUrl	路由	com.tuna.platform.entity.Resource.routerUrl	{"zh_CN":"路由","en":"Router"}	6	java.lang.String	1	0	1	0	0	0	1	0	200	114	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-15 09:41:39.210696	6	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
351	com.tuna.enums.ColumnType	DOUBLE	\N	\N	{"zh_CN":"双精度浮点数","en":"Double"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:35.794551	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
247	com.tuna.platform.entity.Resource	type	资源类型	com.tuna.platform.entity.Resource.type	{"zh_CN":"类型","en":"type"}	9	com.tuna.platform.entity.Resource$Type	1	0	0	0	0	1	1	0	0	99	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:11:03.973501	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
240	com.tuna.platform.entity.Resource	id	编号	com.tuna.entity.Entity.id	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:32:43.200484	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
310	com.tuna.platform.vo.PersonalSettingsVO	avatar	\N	\N	{"zh_CN":"头像","en":"Avatar"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:13.264617	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
246	com.tuna.platform.entity.Resource	visible	是否显示	com.tuna.platform.entity.Resource.visible	{"zh_CN":"显示","en":"Visible"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:34:41.509317	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
255	com.tuna.platform.entity.Resource	redirectUrl	重定向地址	com.tuna.platform.entity.Resource.redirectUrl	{"zh_CN":"重定向地址","en":"Redirect router"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	115	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:37:46.20956	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
314	com.tuna.platform.vo.PersonalSettingsVO	gender	\N	\N	{"zh_CN":"性别","en":"Gender"}	9	com.tuna.enums.Gender	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:54.180959	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
245	com.tuna.platform.entity.Resource	icon	图标	com.tuna.platform.entity.Resource.icon	{"zh_CN":"图标","en":"Icon"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	100	2000	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 10:26:22.985922	8	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
249	com.tuna.platform.entity.Resource	affix	是否固定	com.tuna.platform.entity.Resource.affix	{"zh_CN":"固定","en":"Fixed"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:08.781373	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
250	com.tuna.platform.entity.Resource	keepAlive	是否缓存	com.tuna.platform.entity.Resource.keepAlive	{"zh_CN":"缓存","en":"Cache"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:25.845082	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
252	com.tuna.platform.entity.Resource	layout	布局	com.tuna.platform.entity.Resource.layout	{"zh_CN":"布局","en":"Layout"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:58.735228	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
259	com.tuna.platform.entity.Resource	createdTime	创建时间	com.tuna.entity.ImmutablyEntity.createdTime	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.537628	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
262	com.tuna.platform.entity.Resource	updatedTime	更新时间	com.tuna.entity.AuditEntity.updatedTime	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.568236	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
261	com.tuna.platform.entity.Resource	updatedBy	更新人	com.tuna.entity.AuditEntity.updatedBy	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.557805	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
268	com.tuna.platform.entity.ResourceGroup	showOrder	显示顺序	com.tuna.platform.entity.ResourceGroup.showOrder	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	1	0	0	0	0	1	1	0	200	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:07:19.958639	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
267	com.tuna.platform.entity.ResourceGroup	name	名称	com.tuna.platform.entity.ResourceGroup.name	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:07:02.578519	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
350	com.tuna.enums.ColumnType	STRING	\N	\N	{"zh_CN":"字符串","en":"String"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:41.827913	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
270	com.tuna.platform.entity.ResourceGroup	navbarMenu	快捷菜单	com.tuna.platform.entity.ResourceGroup.navbarMenu	{"zh_CN":"快捷菜单","en":"Quick entry menu"}	9	com.tuna.enums.YesNo	1	0	0	0	0	1	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:08:41.226213	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
274	com.tuna.platform.entity.ResourceGroup	remark	备注	com.tuna.platform.entity.ResourceGroup.remark	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	110	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:51.621784	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
266	com.tuna.platform.entity.ResourceGroup	code	编码	com.tuna.platform.entity.ResourceGroup.code	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	102	50	2	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.73708	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
319	com.tuna.platform.vo.PersonalSettingsVO	userType	\N	\N	{"zh_CN":"类型","en":"User type"}	9	com.tuna.platform.entity.UserType	1	0	0	1	0	1	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:32.730781	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
302	com.tuna.platform.vo.PersonalSettingsVO	orgName	\N	\N	{"zh_CN":"所属部门","en":"Org name"}	6	java.lang.String	0	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 13:34:00.540356	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
362	com.tuna.enums.ValidatorPattern	EMAIL	\N	\N	{"zh_CN":"邮箱","en":"Email"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:35.1447	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
363	com.tuna.enums.ValidatorPattern	URL	\N	\N	{"zh_CN":"URL","en":"Url"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:40.649767	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
269	com.tuna.platform.entity.ResourceGroup	router	路由	com.tuna.platform.entity.ResourceGroup.router	{"zh_CN":"路由","en":"Router"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:08:23.278029	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
364	com.tuna.enums.ValidatorPattern	ID_CARD	\N	\N	{"zh_CN":"身份证","en":"ID Card"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:49.344257	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
272	com.tuna.platform.entity.ResourceGroup	entry	入口地址	com.tuna.platform.entity.ResourceGroup.entry	{"zh_CN":"地址","en":"Entry url"}	6	java.lang.String	1	0	0	0	0	1	1	0	0	108	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:22.157232	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
273	com.tuna.platform.entity.ResourceGroup	props	拓展属性	com.tuna.platform.entity.ResourceGroup.props	{"zh_CN":"属性","en":"Properties"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	109	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:41.438752	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
264	com.tuna.platform.entity.ResourceGroup	id	编号	com.tuna.entity.Entity.id	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.720876	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
359	com.tuna.enums.ValidatorPattern	ACCOUNT	\N	\N	{"zh_CN":"登录账号","en":"Account"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:15.341789	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
382	com.tuna.platform.entity.I18n	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.62371	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
383	com.tuna.platform.entity.I18n	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.634542	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
384	com.tuna.platform.entity.I18n	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.643875	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
385	com.tuna.platform.entity.I18n	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.654823	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
399	com.tuna.knowledgeBase.entity.KnowledgeFile	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.215467	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
387	com.tuna.platform.entity.I18n	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.674014	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
394	com.tuna.knowledgeBase.entity.KnowledgeFile	showOrder	\N	\N	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	0	0	0	0	0	1	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:06:14.544628	2	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
334	com.tuna.enums.YesNo	NO	\N	\N	{"zh_CN":"否","en":"No"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:18:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:05.375446	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
335	com.tuna.enums.YesNo	YES	\N	\N	{"zh_CN":"是","en":"Yes"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:18:34	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:14.685408	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
379	com.tuna.platform.entity.I18n	code	\N	\N	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.5723	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
391	com.tuna.knowledgeBase.entity.KnowledgeFile	filePath	\N	\N	{"zh_CN":"路径","en":"Path","fr":"Chemin","de":"Pfad","it":"Percorso","ja":"パス","ko":"경로","ru":"Путь","pt_br":"Caminho","pt":"Caminho","es":"Ruta"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	101	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.114357	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
401	com.tuna.knowledgeBase.entity.KnowledgeFile	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.240912	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
389	com.tuna.knowledgeBase.entity.KnowledgeFile	fileType	\N	\N	{"zh_CN":"种类","en":"Type","fr":"Tipo","de":"Art","it":"Tipo","ja":"種類","ko":"종류","ru":"Вид","pt_br":"Tipo","pt":"Tipo","es":"Tipo"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:07:23.539507	4	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
393	com.tuna.knowledgeBase.entity.KnowledgeFile	fileVersion	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.137428	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
380	com.tuna.platform.entity.I18n	name	\N	\N	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.585604	\N	\N	\N	1	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
396	com.tuna.knowledgeBase.entity.KnowledgeFile	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.175429	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
279	com.tuna.platform.entity.ResourceGroup	updatedBy	更新人	com.tuna.entity.AuditEntity.updatedBy	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.848932	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
242	com.tuna.platform.entity.Resource	parentId	上级资源	com.tuna.platform.entity.Resource.parentId	{"zh_CN":"父文件夹","en":"Parent id"}	3	java.lang.Long	0	0	0	0	0	0	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:34:11.927328	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
339	com.tuna.enums.ColumnType	ENUM	\N	\N	{"zh_CN":"枚举","en":"Enum"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:18.086498	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
340	com.tuna.enums.ColumnType	FLOAT	\N	\N	{"zh_CN":"单精度浮点数","en":"Float"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:26.426778	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
341	com.tuna.enums.ColumnType	ARRAY	\N	\N	{"zh_CN":"数组","en":"Array"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:32.839677	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
342	com.tuna.enums.ColumnType	JSON_ARRAY	\N	\N	{"zh_CN":"Json对象数组","en":"JsonObject Array"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:47.325735	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
343	com.tuna.enums.ColumnType	JSON	\N	\N	{"zh_CN":"Json对象","en":"Json"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:48:58.072138	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
344	com.tuna.enums.ColumnType	LOCAL_DATE_TIME	\N	\N	{"zh_CN":"时间","en":"LocalDateTime"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:09.572565	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
281	com.tuna.platform.entity.ResourceGroup	version	乐观锁	com.tuna.entity.AuditEntity.version	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.894762	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
337	com.tuna.enums.Align	MIDDLE	\N	\N	{"zh_CN":"居中对齐","en":"Middle"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:22.951958	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
378	com.tuna.platform.entity.I18n	groupId	\N	\N	{"zh_CN":"应用","en":"Group id"}	3	java.lang.Long	1	0	0	1	0	1	1	0	160	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 14:28:53.819847	8	platform-service	0	0	com.tuna.platform.entity.ResourceGroup	id	name	2	\N	\N	1	1	/views/microapp/developConfig/resource/ResourceCategoryGroupTreeSelect.vue	\N	\N
345	com.tuna.enums.ColumnType	INT	\N	\N	{"zh_CN":"整型","en":"Integer"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:16.34303	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
352	com.tuna.enums.ColumnType	LONG	\N	\N	{"zh_CN":"长整型","en":"Long"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:29.237009	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
348	com.tuna.enums.ColumnType	SPLITTER	\N	\N	{"zh_CN":"分割对象","en":"Splitter"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:03.707992	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
265	com.tuna.platform.entity.ResourceGroup	categoryId	资源分类	com.tuna.platform.entity.ResourceGroup.categoryId	{"zh_CN":"分类","en":"Category"}	3	java.lang.Long	0	0	0	0	0	1	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:06:45.691421	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
347	com.tuna.enums.ColumnType	BYTE	\N	\N	{"zh_CN":"字节","en":"Byte"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:50:10.40772	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
361	com.tuna.enums.ValidatorPattern	NONE	\N	\N	{"zh_CN":"无","en":"None"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:29.543039	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
243	com.tuna.platform.entity.Resource	groupId	资源分组	com.tuna.platform.entity.Resource.groupId	{"zh_CN":"分组","en":"Group id"}	3	java.lang.Long	0	0	0	0	0	0	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:33:26.413813	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
248	com.tuna.platform.entity.Resource	openTab	是否打开tab	com.tuna.platform.entity.Resource.openTab	{"zh_CN":"打开tab","en":"Open tab"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:35:53.152707	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
253	com.tuna.platform.entity.Resource	component	前端组件	com.tuna.platform.entity.Resource.component	{"zh_CN":"组件","en":"Component"}	6	java.lang.String	1	0	1	0	0	0	1	0	200	113	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:37:11.770672	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
297	com.tuna.platform.vo.PersonalSettingsVO	mobileVerified	\N	\N	{"zh_CN":"手机是否验证","en":"Mobile verified"}	9	com.tuna.enums.YesNo	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:52:51.237493	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
300	com.tuna.platform.vo.PersonalSettingsVO	password	\N	\N	{"zh_CN":"密码","en":"Password"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	1	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:48:05.021138	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
278	com.tuna.platform.entity.ResourceGroup	updaterId	更新人ID	com.tuna.entity.AuditEntity.updaterId	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.838162	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
256	com.tuna.platform.entity.Resource	remark	备注	com.tuna.platform.entity.Resource.remark	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	116	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:38:02.081051	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
294	com.tuna.platform.vo.PersonalSettingsVO	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.559222	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
271	com.tuna.platform.entity.ResourceGroup	isMicroapp	是否微应用	com.tuna.platform.entity.ResourceGroup.isMicroapp	{"zh_CN":"微应用","en":"Micro app"}	9	com.tuna.enums.YesNo	1	0	0	0	0	1	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:09:01.640388	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
296	com.tuna.platform.vo.PersonalSettingsVO	credentialsExpiredAt	\N	\N	{"zh_CN":"密码过期时间","en":"Password expire time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:52:28.851264	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
295	com.tuna.platform.vo.PersonalSettingsVO	accountExpiredAt	\N	\N	{"zh_CN":"账号过期时间","en":"Account expire time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:52:37.515168	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
299	com.tuna.platform.vo.PersonalSettingsVO	extInfo	\N	\N	{"zh_CN":"扩展属性","en":"Extant properties"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:12.786869	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
355	com.tuna.enums.Enable	DISABLE	\N	\N	{"zh_CN":"禁用","en":"Disable"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:05	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:14.825726	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
301	com.tuna.platform.vo.PersonalSettingsVO	lockTime	\N	\N	{"zh_CN":"账号锁定时间","en":"Lock time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:34.74206	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
303	com.tuna.platform.vo.PersonalSettingsVO	realmId	\N	\N	{"zh_CN":"领域编号","en":"Realm id"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:53:57.046801	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
277	com.tuna.platform.entity.ResourceGroup	createdTime	创建时间	com.tuna.entity.ImmutablyEntity.createdTime	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.827825	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
409	com.tuna.knowledgeBase.entity.KnowledgeCategory	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.53545	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
406	com.tuna.knowledgeBase.entity.KnowledgeCategory	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.510261	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
336	com.tuna.enums.Align	LEFT	\N	\N	{"zh_CN":"左对齐","en":"Left"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:59.149411	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
338	com.tuna.enums.Align	RIGHT	\N	\N	{"zh_CN":"右对齐","en":"Right"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:46:41	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:04.960634	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
366	com.tuna.platform.entity.Resource$Type	DIRECTORY	\N	\N	{"zh_CN":"目录","en":"Directory"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:53:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:54:06.247415	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
460	com.tuna.platform.entity.EntityDefine	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.647705	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	id		0	0	\N	\N	\N
373	com.tuna.platform.entity.UserType	SYSTEM	\N	\N	{"zh_CN":"系统","en":"System"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:06.194157	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
374	com.tuna.platform.entity.UserType	ADMIN	\N	\N	{"zh_CN":"管理员","en":"Admin"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:12.767555	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
375	com.tuna.platform.entity.UserType	EXTERNAL	\N	\N	{"zh_CN":"外部系统用户","en":"External"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:20.134086	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
376	com.tuna.platform.entity.UserType	NORMAL	\N	\N	{"zh_CN":"普通账户","en":"Normal"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:59	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:56:26.358324	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
425	com.tuna.platform.entity.LoginLog	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.134678	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
367	com.tuna.platform.entity.Resource$Type	MENU	\N	\N	{"zh_CN":"菜单","en":"Menu"}	9	success	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:53:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 13:47:16.584788	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
368	com.tuna.platform.entity.Resource$Type	BUTTON	\N	\N	{"zh_CN":"按钮","en":"Button"}	9	warning	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:53:46	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 13:47:39.612004	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
411	com.tuna.knowledgeBase.entity.KnowledgeCategory	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.554644	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
410	com.tuna.knowledgeBase.entity.KnowledgeCategory	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.544717	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
404	com.tuna.knowledgeBase.entity.KnowledgeCategory	showOrder	\N	\N	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.472174	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
439	com.tuna.platform.entity.Dictionary	status	\N	\N	{"zh_CN":"状态"}	9	com.tuna.enums.Enable	1	0	0	1	0	1	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.020097	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
403	com.tuna.knowledgeBase.entity.KnowledgeCategory	name	\N	\N	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.460434	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
426	com.tuna.platform.entity.LoginLog	type	\N	\N	{"zh_CN":"类型"}	9	com.tuna.platform.entity.LoginLog$Type	1	0	0	1	0	0	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.16269	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
327	com.tuna.platform.vo.PersonalSettingsVO	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.869924	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
330	com.tuna.platform.vo.PersonalSettingsVO	isDeleted	\N	\N	{"zh_CN":"是否删除","en":"Deleted"}	9	com.tuna.enums.Delete	0	0	0	0	0	0	0	0	0	10007	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.90097	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
331	com.tuna.platform.vo.PersonalSettingsVO	deleterId	\N	\N	{"zh_CN":"删除人编号","en":"Deleter id"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10008	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.917286	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
332	com.tuna.platform.vo.PersonalSettingsVO	deletedBy	\N	\N	{"zh_CN":"删除人","en":"Deleter name"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10009	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.923687	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
322	com.tuna.platform.vo.PersonalSettingsVO	remark	\N	\N	{"zh_CN":"备注","en":"Remark"}	6	java.lang.String	0	0	0	0	0	0	1	0	0	113	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.817328	\N	\N	\N	1	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
318	com.tuna.platform.vo.PersonalSettingsVO	status	\N	\N	{"zh_CN":"状态","en":"Status"}	9	com.tuna.platform.entity.UserStatus	1	0	0	1	0	1	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.783023	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
325	com.tuna.platform.vo.PersonalSettingsVO	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.850268	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
304	com.tuna.platform.vo.PersonalSettingsVO	salt	\N	\N	{"zh_CN":"密码盐","en":"Salt"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:04.189884	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
306	com.tuna.platform.vo.PersonalSettingsVO	emailVerified	\N	\N	{"zh_CN":"邮箱是否验证","en":"Email verified"}	9	com.tuna.enums.YesNo	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:25.762616	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
307	com.tuna.platform.vo.PersonalSettingsVO	loginFailCount	\N	\N	{"zh_CN":"登录失败次数","en":"Login failed times"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:54:54.173873	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
308	com.tuna.platform.vo.PersonalSettingsVO	tenantId	\N	\N	{"zh_CN":"租户编号","en":"Tenant id"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:04.080767	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
328	com.tuna.platform.vo.PersonalSettingsVO	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.879363	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
311	com.tuna.platform.vo.PersonalSettingsVO	userId	\N	\N	{"zh_CN":"用户编号","en":"User id"}	6	java.lang.String	1	0	1	0	0	0	0	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:20.070724	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
312	com.tuna.platform.vo.PersonalSettingsVO	realName	\N	\N	{"zh_CN":"姓名","en":"Name"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:33.132684	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
313	com.tuna.platform.vo.PersonalSettingsVO	nickname	\N	\N	{"zh_CN":"昵称","en":"Nick name"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:55:41.039583	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
315	com.tuna.platform.vo.PersonalSettingsVO	birthday	\N	\N	{"zh_CN":"出生日期","en":"Birthday"}	7	java.time.LocalDate	1	0	0	0	0	0	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:02.740636	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
316	com.tuna.platform.vo.PersonalSettingsVO	mobile	\N	\N	{"zh_CN":"手机","en":"Mobile"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:09.445779	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
317	com.tuna.platform.vo.PersonalSettingsVO	email	\N	\N	{"zh_CN":"邮箱","en":"Email"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:17.762359	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
320	com.tuna.platform.vo.PersonalSettingsVO	lastLoginTime	\N	\N	{"zh_CN":"最后登录时间","en":"Last login time"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:41.788642	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
321	com.tuna.platform.vo.PersonalSettingsVO	lastLoginIp	\N	\N	{"zh_CN":"最后登录IP","en":"Last login ip"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:56:50.275064	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
356	com.tuna.enums.Enable	ENABLE	\N	\N	{"zh_CN":"正常","en":"Enable"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:05	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:51:25.439756	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
365	com.tuna.enums.ValidatorPattern	MOBILE	\N	\N	{"zh_CN":"手机号","en":"Mobile"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:07	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:52:55.489575	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
369	com.tuna.platform.entity.UserStatus	DISABLE	\N	\N	{"zh_CN":"禁用","en":"Disable"}	9	danger	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:23.228939	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
370	com.tuna.platform.entity.UserStatus	LOGOUT	\N	\N	{"zh_CN":"注销","en":"Logout"}	9	warning	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:31.075912	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
371	com.tuna.platform.entity.UserStatus	LOCKED	\N	\N	{"zh_CN":"锁定","en":"Locked"}	9	warning	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:37.821068	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
372	com.tuna.platform.entity.UserStatus	NORMAL	\N	\N	{"zh_CN":"正常","en":"Normal"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:16	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:55:43.991098	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
333	com.tuna.platform.vo.PersonalSettingsVO	deletedTime	\N	\N	{"zh_CN":"删除时间","en":"Deleted time"}	8	java.time.LocalDateTime	0	0	0	0	0	0	0	0	180	10010	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.934008	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
280	com.tuna.platform.entity.ResourceGroup	updatedTime	更新时间	com.tuna.entity.AuditEntity.updatedTime	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.872154	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
414	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	shorOrder	\N	\N	{"zh_CN":"顺序","en":"Sequence","fr":"Séquence","de":"Reihenfolge","it":"Sequenza","ja":"順序","ko":"순서","ru":"Последовательность","pt_br":"Sequência","pt":"Sequência","es":"Secuencia"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.754755	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
462	com.tuna.platform.entity.EntityDefine	code	\N	\N	{"zh_CN":"编码"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	100	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.674871	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	code		0	0	\N	\N	\N
346	com.tuna.enums.ColumnType	SHORT	\N	\N	{"zh_CN":"短整型","en":"Short"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:22.502803	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
349	com.tuna.enums.ColumnType	LOCAL_DATE	\N	\N	{"zh_CN":"日期","en":"LocalDate"}	9	primary	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:47:45	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 15:49:48.925698	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
421	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	fileVersion	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.814012	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
415	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	fileType	\N	\N	{"zh_CN":"种类","en":"Kind","fr":"Genre","de":"Art","it":"Tipo","ja":"種類","ko":"종류","ru":"Вид","pt_br":"Tipo","pt":"Tipo","es":"Tipo"}	2	java.lang.Integer	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.764154	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
417	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	categoryId	\N	\N	{"zh_CN":"分类","en":"Classification","fr":"Classification","de":"Klassifizierung","it":"Classificazione","ja":"分類","ko":"분류","ru":"Классификация","pt_br":"Classificação","pt":"Classificação","es":"Clasificación"}	3	java.lang.Long	0	0	0	0	0	1	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.780295	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
419	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	filePath	\N	\N	{"zh_CN":"路径","en":"Path","fr":"Chemin","de":"Pfad","it":"Percorso","ja":"パス","ko":"경로","ru":"Путь","pt_br":"Caminho","pt":"Caminho","es":"Ruta"}	6	java.lang.String	0	0	0	0	0	1	1	0	0	102	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.799935	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
420	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	fileName	\N	\N	{"zh_CN":"文档名","en":"Document Name","fr":"Nom du document","de":"Dokumentenname","it":"Nome documento","ja":"文書名","ko":"문서명","ru":"Название документа","pt_br":"Nome do documento","pt":"Nome do documento","es":"Nombre del documento"}	6	java.lang.String	1	0	0	0	0	1	1	0	260	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.807277	\N	\N	\N	1	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
377	com.tuna.platform.entity.I18n	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:47:30.492093	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
413	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.747211	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
390	com.tuna.knowledgeBase.entity.KnowledgeFile	categoryId	\N	\N	{"zh_CN":"分类","en":"Category","fr":"Catégorie","de":"Kategorie","it":"Categoria","ja":"分類","ko":"분류","ru":"Категория","pt_br":"Categoria","pt":"Categoria","es":"Categoría"}	3	java.lang.Long	0	0	0	0	0	1	1	0	0	100	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.10448	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
441	com.tuna.platform.entity.Dictionary	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.033334	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
392	com.tuna.knowledgeBase.entity.KnowledgeFile	fileName	\N	\N	{"zh_CN":"文件名","en":"File Name","fr":"Nom de fichier","de":"Dateiname","it":"Nome file","ja":"ファイル名","ko":"파일명","ru":"Имя файла","pt_br":"Nome do arquivo","pt":"Nome do ficheiro","es":"Nombre del archivo"}	6	java.lang.String	1	0	0	1	0	1	1	0	260	102	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:36:00.236286	3	knowledge-base-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
381	com.tuna.platform.entity.I18n	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.614247	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
422	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.820804	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
275	com.tuna.platform.entity.ResourceGroup	creatorId	创建人ID	com.tuna.entity.ImmutablyEntity.creatorId	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.812111	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
323	com.tuna.platform.vo.PersonalSettingsVO	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.82613	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
464	com.tuna.platform.entity.EntityDefine	name	\N	\N	{"zh_CN":"名称"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	102	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.698477	\N	\N	\N	1	platform-service	1	\N	\N	id	name	1	name		0	0	\N	\N	\N
282	com.tuna.platform.entity.Role	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35.852676	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
402	com.tuna.knowledgeBase.entity.KnowledgeCategory	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.448966	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
287	com.tuna.platform.entity.Role	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.371675	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
423	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.829323	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
398	com.tuna.knowledgeBase.entity.KnowledgeFile	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.204722	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
257	com.tuna.platform.entity.Resource	creatorId	创建人ID	com.tuna.entity.ImmutablyEntity.creatorId	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.516256	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
395	com.tuna.knowledgeBase.entity.KnowledgeFile	creatorId	\N	\N	{"zh_CN":"创建人编号","en":"Creator No","fr":"N° du créateur","de":"Ersteller-Nr.","it":"N° creatore","ja":"作成者番号","ko":"작성자 번호","ru":"№ создателя","pt_br":"Nº do criador","pt":"Nº do criador","es":"Nº del creador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.162819	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
288	com.tuna.platform.entity.Role	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.381987	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
258	com.tuna.platform.entity.Resource	createdBy	创建人	com.tuna.entity.ImmutablyEntity.createdBy	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.52716	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
276	com.tuna.platform.entity.ResourceGroup	createdBy	创建人	com.tuna.entity.ImmutablyEntity.createdBy	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:13.818972	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
407	com.tuna.knowledgeBase.entity.KnowledgeCategory	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.51946	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
465	com.tuna.platform.entity.EntityDefine	url	\N	\N	{"zh_CN":"接口地址"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	103	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.710769	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	url		0	0	\N	\N	\N
467	com.tuna.platform.entity.EntityDefine	titleColumnName	\N	\N	{"zh_CN":"标题属性列"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.741738	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	title_column_name		0	0	\N	\N	\N
468	com.tuna.platform.entity.EntityDefine	operationColumnWidth	\N	\N	{"zh_CN":"操作列宽度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	106	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.752092	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	operation_column_width		0	0	\N	\N	\N
469	com.tuna.platform.entity.EntityDefine	queryParams	\N	\N	{"zh_CN":"默认查询条件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.762454	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	query_params		0	0	\N	\N	\N
466	com.tuna.platform.entity.EntityDefine	remark	\N	\N	{"zh_CN":"说明"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	104	1024	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 15:46:39.953391	3	platform-service	0	\N	\N	id	name	1	remark		0	0	\N	\N	
324	com.tuna.platform.vo.PersonalSettingsVO	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator","fr":"Créateur","de":"Ersteller","it":"Autore","ja":"作成者","ko":"작성자","ru":"Создатель","pt_br":"Criador","pt":"Criador","es":"Creador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.840887	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
289	com.tuna.platform.entity.Role	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.390459	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
408	com.tuna.knowledgeBase.entity.KnowledgeCategory	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.525927	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
424	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.836505	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
397	com.tuna.knowledgeBase.entity.KnowledgeFile	createdTime	\N	\N	{"zh_CN":"创建时间","en":"Creation Time","fr":"Heure de création","de":"Erstellungszeit","it":"Ora di creazione","ja":"作成時間","ko":"생성 시간","ru":"Время создания","pt_br":"Hora de criação","pt":"Hora de criação","es":"Hora de creación"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.190685	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
290	com.tuna.platform.entity.Role	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.398821	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
260	com.tuna.platform.entity.Resource	updaterId	更新人ID	com.tuna.entity.AuditEntity.updaterId	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.549932	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
238	com.tuna.platform.entity.ResourceCategory	updatedTime	更新时间	com.tuna.entity.AuditEntity.updatedTime	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42.968411	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
386	com.tuna.platform.entity.I18n	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 16:45:56.664636	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
400	com.tuna.knowledgeBase.entity.KnowledgeFile	updatedTime	\N	\N	{"zh_CN":"更新时间","en":"Update Time","fr":"Date de mise à jour","de":"Aktualisierungszeit","it":"Data di aggiornamento","ja":"更新時間","ko":"수정 시간","ru":"Время обновления","pt_br":"Data de atualização","pt":"Data de atualização","es":"Fecha de actualización"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55.224653	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
291	com.tuna.platform.entity.Role	updatedBy	\N	\N	{"zh_CN":"更新人","en":"Updater","fr":"Metteur à jour","de":"Aktualisierer","it":"Aggiornatore","ja":"更新者","ko":"수정자","ru":"Обновитель","pt_br":"Atualizador","pt":"Atualizador","es":"Actualizador"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.407175	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
263	com.tuna.platform.entity.Resource	version	乐观锁	com.tuna.entity.AuditEntity.version	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00.575677	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
470	com.tuna.platform.entity.EntityDefine	formComponent	\N	\N	{"zh_CN":"表单组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.772344	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	form_component		0	0	\N	\N	\N
326	com.tuna.platform.vo.PersonalSettingsVO	updaterId	\N	\N	{"zh_CN":"更新人编号","en":"Updater No","fr":"N° du metteur à jour","de":"Aktualisierer-Nr.","it":"N° aggiornatore","ja":"更新者番号","ko":"수정자 번호","ru":"№ обновителя","pt_br":"Nº do atualizador","pt":"Nº do atualizador","es":"Nº del actualizador"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09.861564	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
412	com.tuna.knowledgeBase.entity.KnowledgeCategory	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.56147	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
251	com.tuna.platform.entity.Resource	showOrder	显示顺序	com.tuna.platform.entity.Resource.showOrder	{"zh_CN":"顺序","en":"Sort","fr":"Ordre","de":"Reihenfolge","it":"Ordine","ja":"順序","ko":"순서","ru":"Порядок","pt_br":"Ordem","pt":"Ordem","es":"Orden"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 09:36:46.163904	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
230	com.tuna.platform.entity.ResourceCategory	name	名称	com.tuna.platform.entity.ResourceCategory.name	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	0	0	1	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:33:42	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:05:22.759959	2	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
244	com.tuna.platform.entity.Resource	name	名称	com.tuna.platform.entity.Resource.name	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	200	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-22 16:34:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-25 15:03:58.619466	4	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
405	com.tuna.knowledgeBase.entity.KnowledgeCategory	parentId	\N	\N	{"zh_CN":"上级分类","en":"Parent Category","fr":"Catégorie parente","de":"Übergeordnete Kategorie","it":"Categoria padre","ja":"上位分類","ko":"상위 분류","ru":"Родительская категория","pt_br":"Categoria superior","pt":"Categoria superior","es":"Categoría superior"}	3	java.lang.Long	1	0	0	1	0	0	1	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:11.490812	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
418	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	lang	\N	\N	{"zh_CN":"语言","en":"Language","fr":"Langue","de":"Sprache","it":"Lingua","ja":"言語","ko":"언어","ru":"Язык","pt_br":"Idioma","pt":"Idioma","es":"Idioma"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	101	200	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36.793124	\N	\N	\N	1	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
416	com.tuna.knowledgeBase.entity.KnowledgeFileHistory	fileId	\N	\N	{"zh_CN":"文件编号","en":"File No","fr":"N° de fichier","de":"Dateinummer","it":"N° file","ja":"ファイル番号","ko":"파일 번호","ru":"Номер файла","pt_br":"Nº do arquivo","pt":"Nº do ficheiro","es":"Nº de archivo"}	3	java.lang.Long	1	0	0	0	0	0	0	0	120	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 11:05:36	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 14:01:37.624118	2	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
428	com.tuna.platform.entity.LoginLog	realName	\N	\N	{"zh_CN":"姓名"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.188313	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
429	com.tuna.platform.entity.LoginLog	ipAddress	\N	\N	{"zh_CN":"IP地址"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.200329	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
430	com.tuna.platform.entity.LoginLog	sessionId	\N	\N	{"zh_CN":"Session"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.209126	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
434	com.tuna.platform.entity.LoginLog	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	180	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03.255754	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
445	com.tuna.platform.entity.Dictionary	updatedBy	\N	\N	{"zh_CN":"更新人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.06036	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
432	com.tuna.platform.entity.LoginLog	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:06:44.740611	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
471	com.tuna.platform.entity.EntityDefine	formWidth	\N	\N	{"zh_CN":"表单组件宽度"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.788621	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	form_width		0	0	\N	\N	\N
447	com.tuna.platform.entity.Dictionary	version	\N	\N	{"zh_CN":"版本"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.073589	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
446	com.tuna.platform.entity.Dictionary	updatedTime	\N	\N	{"zh_CN":"更新时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:38.990057	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
427	com.tuna.platform.entity.LoginLog	username	\N	\N	{"zh_CN":"账号"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:13:04.035844	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
433	com.tuna.platform.entity.LoginLog	createdBy	\N	\N	{"zh_CN":"创建人","en":"Creator"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:15:28.334706	5	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
431	com.tuna.platform.entity.LoginLog	remark	\N	\N	{"zh_CN":"备注"}	6	java.lang.String	0	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:05:03	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 16:24:24.539572	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
473	com.tuna.platform.entity.EntityDefine	paginationVisible	\N	\N	{"zh_CN":"启用分页插件"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.80635	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	pagination_visible		0	0	\N	\N	\N
476	com.tuna.platform.entity.EntityDefine	excelImportComponent	\N	\N	{"zh_CN":"Excel导入组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	114	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 08:48:22.547802	2	platform-service	0	\N	\N	id	name	1	excel_import_component		0	0	/components/TunaComponentSelect.vue	\N	\N
435	com.tuna.platform.entity.Dictionary	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:11.990132	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
436	com.tuna.platform.entity.Dictionary	groupId	\N	\N	{"zh_CN":"资源分组"}	3	java.lang.Long	0	0	0	0	0	0	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:11.998497	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
437	com.tuna.platform.entity.Dictionary	code	\N	\N	{"zh_CN":"编码"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.00239	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
438	com.tuna.platform.entity.Dictionary	name	\N	\N	{"zh_CN":"名称"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.011733	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
474	com.tuna.platform.entity.EntityDefine	excelEnable	\N	\N	{"zh_CN":"启用Excel导入导出"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.815143	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	excel_enable		0	0	\N	\N	\N
475	com.tuna.platform.entity.EntityDefine	queryFormVisible	\N	\N	{"zh_CN":"启用查询条件"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	113	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.858058	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	query_form_visible		0	0	\N	\N	\N
477	com.tuna.platform.entity.EntityDefine	rowKey	\N	\N	{"zh_CN":"RowKey"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	115	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.920018	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	row_key		0	0	\N	\N	\N
478	com.tuna.platform.entity.EntityDefine	defaultExpandAll	\N	\N	{"zh_CN":"默认展开"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	116	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.933317	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	default_expand_all		0	0	\N	\N	\N
479	com.tuna.platform.entity.EntityDefine	treeProps	\N	\N	{"zh_CN":"树属性"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	117	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.942794	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	tree_props		0	0	\N	\N	\N
480	com.tuna.platform.entity.EntityDefine	queryMode	\N	\N	{"zh_CN":"启用查询模式"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	118	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.953107	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	query_mode		0	0	\N	\N	\N
482	com.tuna.platform.entity.EntityDefine	showLang	\N	\N	{"zh_CN":"显示语言"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	120	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.972873	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	show_lang		0	0	\N	\N	\N
483	com.tuna.platform.entity.EntityDefine	showSummary	\N	\N	{"zh_CN":"启用合计"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	121	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55.980741	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	show_summary		0	0	\N	\N	\N
486	com.tuna.platform.entity.EntityDefine	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.014252	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATOR_ID		0	0	\N	\N	\N
487	com.tuna.platform.entity.EntityDefine	createdBy	\N	\N	{"zh_CN":"创建人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.018689	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_BY		0	0	\N	\N	\N
488	com.tuna.platform.entity.EntityDefine	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.028514	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_TIME		0	0	\N	\N	\N
489	com.tuna.platform.entity.EntityDefine	updaterId	\N	\N	{"zh_CN":"更新人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.034928	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATER_ID		0	0	\N	\N	\N
490	com.tuna.platform.entity.EntityDefine	updatedBy	\N	\N	{"zh_CN":"更新人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.040167	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_BY		0	0	\N	\N	\N
491	com.tuna.platform.entity.EntityDefine	updatedTime	\N	\N	{"zh_CN":"更新时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.04912	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_TIME		0	0	\N	\N	\N
492	com.tuna.platform.entity.EntityDefine	version	\N	\N	{"zh_CN":"版本"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56.054558	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	VERSION		0	0	\N	\N	\N
284	com.tuna.platform.entity.Role	name	\N	\N	{"zh_CN":"名称","en":"Name","fr":"Nom","de":"Name","it":"Nome","ja":"名称","ko":"이름","ru":"Название","pt_br":"Nome","pt":"Nome","es":"Nombre"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	3	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:35.612731	4	platform-service	1	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
485	com.tuna.platform.entity.EntityDefine	operationButtons	\N	\N	{"zh_CN":"操作列拓展按钮"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	123	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:56	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:41:46.450823	5	platform-service	0	\N	\N	id	name	1	operation_buttons		0	0	/components/TunaStringArraySplitInput.vue	\N	\N
506	com.tuna.platform.entity.SchemaDefine	isOverflow	\N	\N	{"zh_CN":"是否省略"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.047748	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_overflow		0	0	\N	id	\N
507	com.tuna.platform.entity.SchemaDefine	isI18n	\N	\N	{"zh_CN":"是否多语言"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	111	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.056733	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_i18n		0	0	\N	id	\N
388	com.tuna.knowledgeBase.entity.KnowledgeFile	id	\N	\N	{"zh_CN":"编号","en":"No","fr":"Numéro","de":"Nr.","it":"N°","ja":"番号","ko":"번호","ru":"№","pt_br":"Número","pt":"Número","es":"Nº"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 09:04:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-01 15:12:30.86852	3	knowledge-base-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
461	com.tuna.platform.entity.EntityDefine	extendMap	\N	\N	{"zh_CN":"extendMap"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 08:22:50.169082	2	platform-service	0	\N	\N	id	name	1			0	0	\N	\N	\N
484	com.tuna.platform.entity.EntityDefine	queryBarButtons	\N	\N	{"zh_CN":"查询栏拓展按钮"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	122	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:41:39.337943	5	platform-service	0	\N	\N	id	name	1	query_bar_buttons		0	0	/components/TunaStringArraySplitInput.vue	\N	\N
283	com.tuna.platform.entity.Role	code	\N	\N	{"zh_CN":"编码","en":"Code"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	2	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-03 11:54:36.50328	3	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
463	com.tuna.platform.entity.EntityDefine	serviceCode	\N	\N	{"zh_CN":"服务"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-04 16:46:12.21525	3	platform-service	0	0	com.tuna.platform.entity.ServiceInstance	code	name	2	service_code		0	0	\N	\N	\N
495	com.tuna.platform.entity.SchemaDefine	id	\N	\N	{"zh_CN":"编号"}	3	java.lang.Long	1	0	0	0	0	0	0	0	0	0	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.887132	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	id		0	0	\N	id	\N
497	com.tuna.platform.entity.SchemaDefine	serviceName	\N	\N	{"zh_CN":"服务"}	6	java.lang.String	1	0	0	1	0	0	1	0	240	101	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.932085	\N	\N	\N	1	platform-service	0	0	com.tuna.platform.entity.ServiceInstance	code	name	1	service_name		0	0	\N	id	\N
498	com.tuna.platform.entity.SchemaDefine	className	\N	\N	{"zh_CN":"类"}	6	java.lang.String	1	0	0	1	0	0	1	0	240	102	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.945998	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	class_name		0	0	\N	id	\N
499	com.tuna.platform.entity.SchemaDefine	name	\N	\N	{"zh_CN":"属性名"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	103	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.958895	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	name		0	0	\N	id	\N
500	com.tuna.platform.entity.SchemaDefine	fieldName	\N	\N	{"zh_CN":"字段名"}	6	java.lang.String	1	0	0	1	0	0	1	0	0	104	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.971274	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	field_name		0	0	\N	id	\N
501	com.tuna.platform.entity.SchemaDefine	description	\N	\N	{"zh_CN":"说明"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.984559	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	description		0	0	\N	id	\N
502	com.tuna.platform.entity.SchemaDefine	displayName	\N	\N	{"zh_CN":"显示名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	106	80	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:30:59.99542	\N	\N	\N	1	platform-service	1	\N	\N	id	name	1	display_name		0	0	\N	id	\N
503	com.tuna.platform.entity.SchemaDefine	type	\N	\N	{"zh_CN":"字段类型"}	9	com.tuna.enums.ColumnType	1	0	0	0	0	0	1	0	0	107	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.010761	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	type		0	0	\N	id	\N
504	com.tuna.platform.entity.SchemaDefine	typeClassName	\N	\N	{"zh_CN":"字段类名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	108	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.024084	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	type_class_name		0	0	\N	id	\N
505	com.tuna.platform.entity.SchemaDefine	isShow	\N	\N	{"zh_CN":"是否显示"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	109	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.036461	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_show		0	0	\N	id	\N
508	com.tuna.platform.entity.SchemaDefine	showOrder	\N	\N	{"zh_CN":"显示顺序"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	112	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.06856	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	show_order		0	0	\N	id	\N
509	com.tuna.platform.entity.SchemaDefine	width	\N	\N	{"zh_CN":"显示宽度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	113	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.079905	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	width		0	0	\N	id	\N
510	com.tuna.platform.entity.SchemaDefine	isRequire	\N	\N	{"zh_CN":"是否必填"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	114	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.099625	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_require		0	0	\N	id	\N
511	com.tuna.platform.entity.SchemaDefine	isForm	\N	\N	{"zh_CN":"是否表单"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	115	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.119366	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_form		0	0	\N	id	\N
512	com.tuna.platform.entity.SchemaDefine	isSort	\N	\N	{"zh_CN":"是否排序"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	116	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.138615	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_sort		0	0	\N	id	\N
513	com.tuna.platform.entity.SchemaDefine	isSearch	\N	\N	{"zh_CN":"是否搜索"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	117	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.158028	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_search		0	0	\N	id	\N
293	com.tuna.platform.entity.Role	version	\N	\N	{"zh_CN":"版本","en":"Version","fr":"Version","de":"Version","it":"Versione","ja":"バージョン","ko":"버전","ru":"Версия","pt_br":"Versão","pt":"Versão","es":"Versión"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:21:36.422519	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
494	com.tuna.platform.entity.EntityDefine	tableName	\N	\N	{"zh_CN":"数据库表名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	104	1024	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-01 15:32:34.137969	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	table_name		0	0	\N	id	\N
514	com.tuna.platform.entity.SchemaDefine	isFixed	\N	\N	{"zh_CN":"是否固定"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	118	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.169311	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	is_fixed	在Table内是否固定	0	0	\N	id	\N
515	com.tuna.platform.entity.SchemaDefine	encrypt	\N	\N	{"zh_CN":"是否加密"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	119	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.187069	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	encrypt		0	0	\N	id	\N
516	com.tuna.platform.entity.SchemaDefine	desensitize	\N	\N	{"zh_CN":"是否脱敏"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	120	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.199356	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	desensitize		0	0	\N	id	\N
517	com.tuna.platform.entity.SchemaDefine	align	\N	\N	{"zh_CN":"对齐方式"}	9	com.tuna.enums.Align	1	0	0	0	0	0	1	0	0	121	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.210818	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	align	在Table内的对齐方式	0	0	\N	id	\N
518	com.tuna.platform.entity.SchemaDefine	maxLength	\N	\N	{"zh_CN":"最大长度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	122	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.221727	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	max_length		0	0	\N	id	\N
519	com.tuna.platform.entity.SchemaDefine	minLength	\N	\N	{"zh_CN":"最小长度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	123	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.232035	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	min_length		0	0	\N	id	\N
520	com.tuna.platform.entity.SchemaDefine	max	\N	\N	{"zh_CN":"最大值"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	124	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.24429	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	max		0	0	\N	id	\N
521	com.tuna.platform.entity.SchemaDefine	min	\N	\N	{"zh_CN":"最小值"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	125	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.255338	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	min		0	0	\N	id	\N
522	com.tuna.platform.entity.SchemaDefine	precision	\N	\N	{"zh_CN":"精度"}	2	java.lang.Integer	1	0	0	0	0	0	1	0	0	126	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.264177	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	precision		0	0	\N	id	\N
523	com.tuna.platform.entity.SchemaDefine	multiple	\N	\N	{"zh_CN":"是否多选"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	127	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.27547	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	multiple	当表格组件被用于对象筛选时起作用，控制是单选还是多选	0	0	\N	id	\N
524	com.tuna.platform.entity.SchemaDefine	pattern	\N	\N	{"zh_CN":"校验规则"}	9	com.tuna.enums.ValidatorPattern	1	0	0	0	0	0	1	0	0	128	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.296233	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	pattern	自定义校验规则	0	0	\N	id	\N
525	com.tuna.platform.entity.SchemaDefine	relationType	\N	\N	{"zh_CN":"关联类型"}	9	com.tuna.enums.RelationType	1	0	0	0	0	0	1	0	0	129	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.312996	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	relation_type	目前支持外键关联和明细表关联，1对1或者1对多	0	0	\N	id	\N
526	com.tuna.platform.entity.SchemaDefine	externalClass	\N	\N	{"zh_CN":"关联对象类名"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	130	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.335601	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	external_class		0	0	\N	id	\N
527	com.tuna.platform.entity.SchemaDefine	externalClassPrimaryField	\N	\N	{"zh_CN":"关联对象字段"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	131	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.346938	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	external_class_primary_field		0	0	\N	id	\N
528	com.tuna.platform.entity.SchemaDefine	innerPrimaryField	\N	\N	{"zh_CN":"明细表关联字段"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	132	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.356403	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	inner_primary_field	关联类型为明细表时数据库无真实字段，需要指明本对象的关联字段名	0	0	\N	id	\N
529	com.tuna.platform.entity.SchemaDefine	externalClassDisplayField	\N	\N	{"zh_CN":"关联对象展示字段"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	133	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.370679	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	external_class_display_field	用于展示关联对象的字段名称	0	0	\N	id	\N
530	com.tuna.platform.entity.SchemaDefine	uiComponent	\N	\N	{"zh_CN":"关联对象前端组件"}	9	com.tuna.enums.UiComponent	1	0	0	0	0	0	1	0	0	134	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.382948	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	ui_component	当字段是外键类型用于查询条件关联选择或者表单录入的时候关联选择	0	0	\N	id	\N
532	com.tuna.platform.entity.SchemaDefine	creatorId	\N	\N	{"zh_CN":"创建人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10000	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.406221	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATOR_ID		0	0	\N	id	\N
533	com.tuna.platform.entity.SchemaDefine	createdBy	\N	\N	{"zh_CN":"创建人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.418485	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_BY		0	0	\N	id	\N
534	com.tuna.platform.entity.SchemaDefine	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.430365	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	CREATED_TIME		0	0	\N	id	\N
535	com.tuna.platform.entity.SchemaDefine	updaterId	\N	\N	{"zh_CN":"更新人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.439271	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATER_ID		0	0	\N	id	\N
536	com.tuna.platform.entity.SchemaDefine	updatedBy	\N	\N	{"zh_CN":"更新人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10004	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.449584	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_BY		0	0	\N	id	\N
537	com.tuna.platform.entity.SchemaDefine	updatedTime	\N	\N	{"zh_CN":"更新时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10005	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.461555	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	UPDATED_TIME		0	0	\N	id	\N
538	com.tuna.platform.entity.SchemaDefine	version	\N	\N	{"zh_CN":"版本"}	3	java.lang.Long	0	0	0	0	0	0	0	0	0	10006	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00.471455	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1	VERSION		0	0	\N	id	\N
541	components.TunaBeanInput.vue	code	\N	\N	{"zh_CN":"字典编码"}	6	java.lang.String	1	0	0	0	0	1	1	0	120	1	200	1	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-07 08:37:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-07 08:49:32.305444	3	platform-service	0	\N	\N	\N	\N	\N	\N	字典组件元数据	0	0	\N	\N	\N
542	components.EnumSelect.vue	code	\N	\N	{"zh_CN":"枚举类名"}	6	java.lang.String	1	0	0	0	0	1	1	0	120	100	100	1	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 09:54:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 10:06:19.921693	2	platform-service	0	\N	\N	\N	\N	\N	\N	枚举类名	0	0	\N	\N	\N
539	com.tuna.platform.entity.SchemaDefine	columnComponent	\N	\N	{"zh_CN":"表格列自定义组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	136	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:48:35	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 11:49:28.683891	10	platform-service	0	\N	\N	id	name	1	column_component	Table Column自定义组件	0	0	/components/TunaComponentSelect.vue	id	
540	com.tuna.platform.entity.Dictionary	extendMap	\N	\N	{"zh_CN":"extendMap"}	11	java.util.Map	0	0	0	0	0	0	0	0	0	1	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:54:40.900051	\N	\N	\N	1	platform-service	0	\N	\N	id	name	1			0	0	\N	id	\N
531	com.tuna.platform.entity.SchemaDefine	fieldComponent	\N	\N	{"zh_CN":"表单自定义组件"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	135	512	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-05 14:31:00	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 11:49:50.349018	2	platform-service	0	\N	\N	id	name	1	field_component	绑定到字段上的自定义前端组件,查询表单和编辑表单共用	0	0	/components/TunaComponentSelect.vue	id	\N
472	com.tuna.platform.entity.EntityDefine	dialogModel	\N	\N	{"zh_CN":"启用对话框"}	9	com.tuna.enums.YesNo	1	0	0	0	0	0	1	0	0	110	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-28 16:07:55	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-08 13:50:31.189804	3	platform-service	0	\N	\N	id	name	1	dialog_model		0	0	\N	\N	
442	com.tuna.platform.entity.Dictionary	createdBy	\N	\N	{"zh_CN":"创建人"}	6	java.lang.String	1	0	0	0	0	0	0	0	0	10001	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.039736	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
444	com.tuna.platform.entity.Dictionary	updaterId	\N	\N	{"zh_CN":"更新人编号"}	6	java.lang.String	0	0	0	0	0	0	0	0	0	10003	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12.053952	\N	\N	\N	1	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
443	com.tuna.platform.entity.Dictionary	createdTime	\N	\N	{"zh_CN":"创建时间"}	8	java.time.LocalDateTime	1	0	0	0	0	0	0	0	0	10002	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:59:00.024095	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
440	com.tuna.platform.entity.Dictionary	remark	\N	\N	{"zh_CN":"备注"}	6	java.lang.String	1	0	0	0	0	0	1	0	0	105	50	0	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 11:58:12	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-07 16:43:16.488847	2	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
309	com.tuna.platform.vo.PersonalSettingsVO	username	\N	\N	{"zh_CN":"用户名","en":"User name"}	6	java.lang.String	1	0	0	1	0	1	1	0	0	1	20	4	2147483647	0	0	0	-1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-24 11:51:09	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-09 15:55:30.929736	4	platform-service	0	\N	\N	\N	\N	\N	\N	\N	1	1	\N	\N	\N
\.


--
-- Data for Name: platform_service_instance; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_service_instance (id, code, context_path, status, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
30	product-assistant-service	/product-assistant	1	{"zh_CN":"product-assistant-service"}	\N	product-assistant-service	product-assistant-service	2026-06-12 09:24:58.348269	\N	\N	\N	1
5	knowledge-base-service	/knowledge-base	1	{"zh_CN":"knowledge-base-service"}	\N	knowledge-base-service	knowledge-base-service	2026-06-01 14:40:38.715792	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-02 16:03:02.55111	2
3	platform-service	/platformSvr	1	{"zh_CN":"平台基础服务"}	\N	platform-service	platform-service	2026-05-21 15:49:33	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 09:55:36.768582	8
\.


--
-- Data for Name: platform_user; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_user (id, user_id, username, password, salt, account_expired_at, credentials_expired_at, real_name, nickname, gender, birthday, avatar, mobile, mobile_verified, email, email_verified, status, user_type, login_fail_count, lock_time, last_login_time, last_login_ip, tenant_id, realm_id, org_id, dept_id, ext_info, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, is_deleted, deleter_id, deleted_by, deleted_time, version) FROM stdin;
2	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	test	{bcrypt}$2a$10$P1QgrD14fkOwgSsNFGF0HOBnHlStv.hFBHl5qodDZk2Pu931ZaptC		\N	\N	管理员	风清扬	0	2026-02-02	/platformSvr/upload/files/public/2026/4/08164216.png	15315086265	0	xushuyang@mylgag.com	0	0	0	0	\N	\N		default	1	\N	\N	{}	测试	\N	\N	2026-03-03 01:11:13	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:51:52.740903	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:51:52.723221	18
4	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	administrator	{bcrypt}$2a$10$uFRl4/wjVkp9y/4WXeez5u9VXk2MGdpkvAqe1O4u81y3w/n5pI3T6		\N	\N	超级管理员		0	2026-04-21		15315086265	\N	xushuyang@lgag.com	\N	1	1	0	\N	\N		default	\N	\N	\N	{}	\N	ca029a30-41ad-4c15-81d0-e8c7116f1ad8	管理员	2026-04-09 15:38:43.214093	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-29 16:51:43.138431	0			\N	5
5	bae99f2a-8f2f-4f12-b2e7-6320f389b5a7	test	{bcrypt}$2a$10$r0/lRye8hET06q45FVADdOng.rbiNSZWzywD.HlalFgxwGA19K9CO		\N	\N	test		0	2026-05-11			\N		\N	1	0	25	\N	\N		default	\N	\N	\N	{}	\N	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-05-06 14:26:15.315869	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-06-06 16:59:26.036048	0			\N	29
\.


--
-- Data for Name: platform_user_group; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_user_group (id, code, name, remark, creator_id, created_by, created_time, updater_id, updated_by, updated_time, version) FROM stdin;
\.


--
-- Data for Name: platform_user_group_resource; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_user_group_resource (group_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_user_group_user; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_user_group_user (user_id, group_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_user_resource; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_user_resource (user_id, resource_id, creator_id, created_by, created_time) FROM stdin;
\.


--
-- Data for Name: platform_user_role; Type: TABLE DATA; Schema: tuna_cloud_platform; Owner: postgres
--

COPY tuna_cloud_platform.platform_user_role (user_id, role_id, creator_id, created_by, created_time) FROM stdin;
4	1	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 15:01:12.251246
4	2	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 15:01:12.253451
4	5	32a6d4bc-7f04-43a2-9fc9-0cb99766a993	超级管理员	2026-04-27 15:01:12.253939
\.


--
-- Name: platform_dictionary1_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_dictionary1_id_seq', 1, false);


--
-- Name: platform_dictionary_item_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_dictionary_item_id_seq', 91, true);


--
-- Name: platform_entity_define_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_entity_define_id_seq', 68, true);


--
-- Name: platform_group_i18n_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_group_i18n_id_seq', 9, true);


--
-- Name: platform_job_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_job_id_seq', 1, false);


--
-- Name: platform_login_log_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_login_log_id_seq', 172, true);


--
-- Name: platform_openapi_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_openapi_id_seq', 2, true);


--
-- Name: platform_org_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_org_id_seq', 1, false);


--
-- Name: platform_realm_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_realm_id_seq', 3, true);


--
-- Name: platform_resource_category_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_resource_category_id_seq', 65540, true);


--
-- Name: platform_resource_group_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_resource_group_id_seq', 9, true);


--
-- Name: platform_resource_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_resource_id_seq', 157, true);


--
-- Name: platform_role_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_role_id_seq', 18, true);


--
-- Name: platform_schema_define_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_schema_define_id_seq', 554, true);


--
-- Name: platform_service_instance_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_service_instance_id_seq', 30, true);


--
-- Name: platform_user_group_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_user_group_id_seq', 1, false);


--
-- Name: platform_user_id_seq; Type: SEQUENCE SET; Schema: byteasy_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('byteasy_cloud_platform.platform_user_id_seq', 5, true);


--
-- Name: knowledge_category_id_seq; Type: SEQUENCE SET; Schema: knowledge_base; Owner: postgres
--

SELECT pg_catalog.setval('knowledge_base.knowledge_category_id_seq', 26, true);


--
-- Name: knowledge_file_history_id_seq; Type: SEQUENCE SET; Schema: knowledge_base; Owner: postgres
--

SELECT pg_catalog.setval('knowledge_base.knowledge_file_history_id_seq', 9, true);


--
-- Name: knowledge_file_id_seq; Type: SEQUENCE SET; Schema: knowledge_base; Owner: postgres
--

SELECT pg_catalog.setval('knowledge_base.knowledge_file_id_seq', 8, true);


--
-- Name: model_code_serial_no_id_seq; Type: SEQUENCE SET; Schema: micro_product_assistant; Owner: postgres
--

SELECT pg_catalog.setval('micro_product_assistant.model_code_serial_no_id_seq', 54, true);


--
-- Name: model_env_pub_no_id_seq; Type: SEQUENCE SET; Schema: micro_product_assistant; Owner: postgres
--

SELECT pg_catalog.setval('micro_product_assistant.model_env_pub_no_id_seq', 1, false);


--
-- Name: nameplate_record_id_seq; Type: SEQUENCE SET; Schema: micro_product_assistant; Owner: postgres
--

SELECT pg_catalog.setval('micro_product_assistant.nameplate_record_id_seq', 5529, true);


--
-- Name: print_record_id_seq; Type: SEQUENCE SET; Schema: micro_product_assistant; Owner: postgres
--

SELECT pg_catalog.setval('micro_product_assistant.print_record_id_seq', 6, true);


--
-- Name: qr_sync_job_id_seq; Type: SEQUENCE SET; Schema: micro_product_assistant; Owner: postgres
--

SELECT pg_catalog.setval('micro_product_assistant.qr_sync_job_id_seq', 28, true);


--
-- Name: vin_generate_request_id_seq; Type: SEQUENCE SET; Schema: micro_product_assistant; Owner: postgres
--

SELECT pg_catalog.setval('micro_product_assistant.vin_generate_request_id_seq', 20, true);


--
-- Name: platform_dictionary1_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_dictionary1_id_seq', 1, false);


--
-- Name: platform_dictionary_item_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_dictionary_item_id_seq', 91, true);


--
-- Name: platform_entity_define_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_entity_define_id_seq', 68, true);


--
-- Name: platform_group_i18n_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_group_i18n_id_seq', 9, true);


--
-- Name: platform_job_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_job_id_seq', 1, false);


--
-- Name: platform_login_log_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_login_log_id_seq', 150, true);


--
-- Name: platform_openapi_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_openapi_id_seq', 2, true);


--
-- Name: platform_org_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_org_id_seq', 1, false);


--
-- Name: platform_realm_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_realm_id_seq', 3, true);


--
-- Name: platform_resource_category_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_resource_category_id_seq', 65540, true);


--
-- Name: platform_resource_group_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_resource_group_id_seq', 9, true);


--
-- Name: platform_resource_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_resource_id_seq', 157, true);


--
-- Name: platform_role_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_role_id_seq', 18, true);


--
-- Name: platform_schema_define_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_schema_define_id_seq', 542, true);


--
-- Name: platform_service_instance_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_service_instance_id_seq', 30, true);


--
-- Name: platform_user_group_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_user_group_id_seq', 1, false);


--
-- Name: platform_user_id_seq; Type: SEQUENCE SET; Schema: tuna_cloud_platform; Owner: postgres
--

SELECT pg_catalog.setval('tuna_cloud_platform.platform_user_id_seq', 5, true);


--
-- Name: oauth2_authorization_consent oauth2_authorization_consent_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_authorization_consent
    ADD CONSTRAINT oauth2_authorization_consent_pkey PRIMARY KEY (registered_client_id, principal_name);


--
-- Name: oauth2_authorization oauth2_authorization_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_authorization
    ADD CONSTRAINT oauth2_authorization_pkey PRIMARY KEY (id);


--
-- Name: oauth2_registered_client oauth2_registered_client_client_id_key; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_registered_client
    ADD CONSTRAINT oauth2_registered_client_client_id_key UNIQUE (client_id);


--
-- Name: oauth2_registered_client_openapi oauth2_registered_client_openapi_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_registered_client_openapi
    ADD CONSTRAINT oauth2_registered_client_openapi_pkey PRIMARY KEY (oauth2_registered_client_id, openapi_id);


--
-- Name: oauth2_registered_client oauth2_registered_client_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_registered_client
    ADD CONSTRAINT oauth2_registered_client_pkey PRIMARY KEY (id);


--
-- Name: platform_dictionary platform_dictionary1_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_dictionary
    ADD CONSTRAINT platform_dictionary1_pkey PRIMARY KEY (id);


--
-- Name: platform_dictionary_item platform_dictionary_item_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_dictionary_item
    ADD CONSTRAINT platform_dictionary_item_pkey PRIMARY KEY (id);


--
-- Name: platform_entity_define platform_entity_define_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_entity_define
    ADD CONSTRAINT platform_entity_define_pkey PRIMARY KEY (id);


--
-- Name: platform_group_i18n platform_group_i18n_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_group_i18n
    ADD CONSTRAINT platform_group_i18n_pkey PRIMARY KEY (id);


--
-- Name: platform_job platform_job_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job
    ADD CONSTRAINT platform_job_pkey PRIMARY KEY (id);


--
-- Name: platform_job_resource platform_job_resource_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job_resource
    ADD CONSTRAINT platform_job_resource_pkey PRIMARY KEY (job_id, resource_id);


--
-- Name: platform_job_user platform_job_user_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job_user
    ADD CONSTRAINT platform_job_user_pkey PRIMARY KEY (user_id, job_id);


--
-- Name: platform_login_log platform_login_log_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_login_log
    ADD CONSTRAINT platform_login_log_pkey PRIMARY KEY (id);


--
-- Name: platform_openapi platform_openapi_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_openapi
    ADD CONSTRAINT platform_openapi_pkey PRIMARY KEY (id);


--
-- Name: platform_org platform_org_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_org
    ADD CONSTRAINT platform_org_pkey PRIMARY KEY (id);


--
-- Name: platform_org_resource platform_org_resource_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_org_resource
    ADD CONSTRAINT platform_org_resource_pkey PRIMARY KEY (org_id, resource_id);


--
-- Name: platform_realm platform_realm_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_realm
    ADD CONSTRAINT platform_realm_pkey PRIMARY KEY (id);


--
-- Name: platform_resource_category platform_resource_category_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_category
    ADD CONSTRAINT platform_resource_category_pkey PRIMARY KEY (id);


--
-- Name: platform_resource_group platform_resource_group_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_group
    ADD CONSTRAINT platform_resource_group_pkey PRIMARY KEY (id);


--
-- Name: platform_resource_openapi platform_resource_openapi_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_openapi
    ADD CONSTRAINT platform_resource_openapi_pkey PRIMARY KEY (resource_id, openapi_id);


--
-- Name: platform_resource platform_resource_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource
    ADD CONSTRAINT platform_resource_pkey PRIMARY KEY (id);


--
-- Name: platform_role platform_role_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_role
    ADD CONSTRAINT platform_role_pkey PRIMARY KEY (id);


--
-- Name: platform_role_resource platform_role_resource_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_role_resource
    ADD CONSTRAINT platform_role_resource_pkey PRIMARY KEY (role_id, resource_id);


--
-- Name: platform_schema_define platform_schema_define_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_schema_define
    ADD CONSTRAINT platform_schema_define_pkey PRIMARY KEY (id);


--
-- Name: platform_service_instance platform_service_instance_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_service_instance
    ADD CONSTRAINT platform_service_instance_pkey PRIMARY KEY (id);


--
-- Name: platform_user_group platform_user_group_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group
    ADD CONSTRAINT platform_user_group_pkey PRIMARY KEY (id);


--
-- Name: platform_user_group_resource platform_user_group_resource_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group_resource
    ADD CONSTRAINT platform_user_group_resource_pkey PRIMARY KEY (group_id, resource_id);


--
-- Name: platform_user_group_user platform_user_group_user_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group_user
    ADD CONSTRAINT platform_user_group_user_pkey PRIMARY KEY (user_id, group_id);


--
-- Name: platform_user platform_user_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user
    ADD CONSTRAINT platform_user_pkey PRIMARY KEY (id);


--
-- Name: platform_user_resource platform_user_resource_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_resource
    ADD CONSTRAINT platform_user_resource_pkey PRIMARY KEY (user_id, resource_id);


--
-- Name: platform_user_role platform_user_role_pkey; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_role
    ADD CONSTRAINT platform_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: platform_job uk_platform_job_code; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job
    ADD CONSTRAINT uk_platform_job_code UNIQUE (code);


--
-- Name: platform_org uk_platform_org_code; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_org
    ADD CONSTRAINT uk_platform_org_code UNIQUE (code);


--
-- Name: platform_role uk_platform_role_code; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_role
    ADD CONSTRAINT uk_platform_role_code UNIQUE (code);


--
-- Name: platform_service_instance uk_platform_service_instance_code; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_service_instance
    ADD CONSTRAINT uk_platform_service_instance_code UNIQUE (code);


--
-- Name: platform_user uk_platform_user_user_id; Type: CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user
    ADD CONSTRAINT uk_platform_user_user_id UNIQUE (user_id);


--
-- Name: knowledge_category knowledge_category_pkey; Type: CONSTRAINT; Schema: knowledge_base; Owner: postgres
--

ALTER TABLE ONLY knowledge_base.knowledge_category
    ADD CONSTRAINT knowledge_category_pkey PRIMARY KEY (id);


--
-- Name: knowledge_file_history knowledge_file_history_pkey; Type: CONSTRAINT; Schema: knowledge_base; Owner: postgres
--

ALTER TABLE ONLY knowledge_base.knowledge_file_history
    ADD CONSTRAINT knowledge_file_history_pkey PRIMARY KEY (id);


--
-- Name: knowledge_file knowledge_file_pkey; Type: CONSTRAINT; Schema: knowledge_base; Owner: postgres
--

ALTER TABLE ONLY knowledge_base.knowledge_file
    ADD CONSTRAINT knowledge_file_pkey PRIMARY KEY (id);


--
-- Name: model_code_serial_no model_code_serial_no_pkey; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.model_code_serial_no
    ADD CONSTRAINT model_code_serial_no_pkey PRIMARY KEY (id);


--
-- Name: model_env_pub_no model_env_pub_no_pkey; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.model_env_pub_no
    ADD CONSTRAINT model_env_pub_no_pkey PRIMARY KEY (id);


--
-- Name: nameplate_record nameplate_record_pkey; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.nameplate_record
    ADD CONSTRAINT nameplate_record_pkey PRIMARY KEY (id);


--
-- Name: print_record print_record_pkey; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.print_record
    ADD CONSTRAINT print_record_pkey PRIMARY KEY (id);


--
-- Name: qr_sync_job qr_sync_job_pkey; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.qr_sync_job
    ADD CONSTRAINT qr_sync_job_pkey PRIMARY KEY (id);


--
-- Name: model_code_serial_no uk_model_code_serial_no_date; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.model_code_serial_no
    ADD CONSTRAINT uk_model_code_serial_no_date UNIQUE (model_code, serial_number, created_time);


--
-- Name: nameplate_record uk_nameplate_record_engine_serial_number; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.nameplate_record
    ADD CONSTRAINT uk_nameplate_record_engine_serial_number UNIQUE (engine_serial_number);


--
-- Name: nameplate_record uk_nameplate_record_vin; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.nameplate_record
    ADD CONSTRAINT uk_nameplate_record_vin UNIQUE (vin);


--
-- Name: vin_generate_request uk_vin_generate_request_engine_serial_number; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.vin_generate_request
    ADD CONSTRAINT uk_vin_generate_request_engine_serial_number UNIQUE (engine_serial_number);


--
-- Name: vin_generate_request uk_vin_generate_request_vin; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.vin_generate_request
    ADD CONSTRAINT uk_vin_generate_request_vin UNIQUE (vin);


--
-- Name: vin_generate_request vin_generate_request_pkey; Type: CONSTRAINT; Schema: micro_product_assistant; Owner: postgres
--

ALTER TABLE ONLY micro_product_assistant.vin_generate_request
    ADD CONSTRAINT vin_generate_request_pkey PRIMARY KEY (id);


--
-- Name: oauth2_authorization_consent oauth2_authorization_consent_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_authorization_consent
    ADD CONSTRAINT oauth2_authorization_consent_pkey PRIMARY KEY (registered_client_id, principal_name);


--
-- Name: oauth2_authorization oauth2_authorization_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_authorization
    ADD CONSTRAINT oauth2_authorization_pkey PRIMARY KEY (id);


--
-- Name: oauth2_registered_client oauth2_registered_client_client_id_key; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_registered_client
    ADD CONSTRAINT oauth2_registered_client_client_id_key UNIQUE (client_id);


--
-- Name: oauth2_registered_client_openapi oauth2_registered_client_openapi_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_registered_client_openapi
    ADD CONSTRAINT oauth2_registered_client_openapi_pkey PRIMARY KEY (oauth2_registered_client_id, openapi_id);


--
-- Name: oauth2_registered_client oauth2_registered_client_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_registered_client
    ADD CONSTRAINT oauth2_registered_client_pkey PRIMARY KEY (id);


--
-- Name: platform_dictionary platform_dictionary1_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_dictionary
    ADD CONSTRAINT platform_dictionary1_pkey PRIMARY KEY (id);


--
-- Name: platform_dictionary_item platform_dictionary_item_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_dictionary_item
    ADD CONSTRAINT platform_dictionary_item_pkey PRIMARY KEY (id);


--
-- Name: platform_entity_define platform_entity_define_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_entity_define
    ADD CONSTRAINT platform_entity_define_pkey PRIMARY KEY (id);


--
-- Name: platform_group_i18n platform_group_i18n_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_group_i18n
    ADD CONSTRAINT platform_group_i18n_pkey PRIMARY KEY (id);


--
-- Name: platform_job platform_job_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job
    ADD CONSTRAINT platform_job_pkey PRIMARY KEY (id);


--
-- Name: platform_job_resource platform_job_resource_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job_resource
    ADD CONSTRAINT platform_job_resource_pkey PRIMARY KEY (job_id, resource_id);


--
-- Name: platform_job_user platform_job_user_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job_user
    ADD CONSTRAINT platform_job_user_pkey PRIMARY KEY (user_id, job_id);


--
-- Name: platform_login_log platform_login_log_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_login_log
    ADD CONSTRAINT platform_login_log_pkey PRIMARY KEY (id);


--
-- Name: platform_openapi platform_openapi_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_openapi
    ADD CONSTRAINT platform_openapi_pkey PRIMARY KEY (id);


--
-- Name: platform_org platform_org_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_org
    ADD CONSTRAINT platform_org_pkey PRIMARY KEY (id);


--
-- Name: platform_org_resource platform_org_resource_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_org_resource
    ADD CONSTRAINT platform_org_resource_pkey PRIMARY KEY (org_id, resource_id);


--
-- Name: platform_realm platform_realm_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_realm
    ADD CONSTRAINT platform_realm_pkey PRIMARY KEY (id);


--
-- Name: platform_resource_category platform_resource_category_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_category
    ADD CONSTRAINT platform_resource_category_pkey PRIMARY KEY (id);


--
-- Name: platform_resource_group platform_resource_group_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_group
    ADD CONSTRAINT platform_resource_group_pkey PRIMARY KEY (id);


--
-- Name: platform_resource_openapi platform_resource_openapi_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_openapi
    ADD CONSTRAINT platform_resource_openapi_pkey PRIMARY KEY (resource_id, openapi_id);


--
-- Name: platform_resource platform_resource_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource
    ADD CONSTRAINT platform_resource_pkey PRIMARY KEY (id);


--
-- Name: platform_role platform_role_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_role
    ADD CONSTRAINT platform_role_pkey PRIMARY KEY (id);


--
-- Name: platform_role_resource platform_role_resource_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_role_resource
    ADD CONSTRAINT platform_role_resource_pkey PRIMARY KEY (role_id, resource_id);


--
-- Name: platform_schema_define platform_schema_define_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_schema_define
    ADD CONSTRAINT platform_schema_define_pkey PRIMARY KEY (id);


--
-- Name: platform_service_instance platform_service_instance_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_service_instance
    ADD CONSTRAINT platform_service_instance_pkey PRIMARY KEY (id);


--
-- Name: platform_user_group platform_user_group_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group
    ADD CONSTRAINT platform_user_group_pkey PRIMARY KEY (id);


--
-- Name: platform_user_group_resource platform_user_group_resource_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group_resource
    ADD CONSTRAINT platform_user_group_resource_pkey PRIMARY KEY (group_id, resource_id);


--
-- Name: platform_user_group_user platform_user_group_user_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group_user
    ADD CONSTRAINT platform_user_group_user_pkey PRIMARY KEY (user_id, group_id);


--
-- Name: platform_user platform_user_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user
    ADD CONSTRAINT platform_user_pkey PRIMARY KEY (id);


--
-- Name: platform_user_resource platform_user_resource_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_resource
    ADD CONSTRAINT platform_user_resource_pkey PRIMARY KEY (user_id, resource_id);


--
-- Name: platform_user_role platform_user_role_pkey; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_role
    ADD CONSTRAINT platform_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: platform_job uk_platform_job_code; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job
    ADD CONSTRAINT uk_platform_job_code UNIQUE (code);


--
-- Name: platform_org uk_platform_org_code; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_org
    ADD CONSTRAINT uk_platform_org_code UNIQUE (code);


--
-- Name: platform_role uk_platform_role_code; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_role
    ADD CONSTRAINT uk_platform_role_code UNIQUE (code);


--
-- Name: platform_service_instance uk_platform_service_instance_code; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_service_instance
    ADD CONSTRAINT uk_platform_service_instance_code UNIQUE (code);


--
-- Name: platform_user uk_platform_user_user_id; Type: CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user
    ADD CONSTRAINT uk_platform_user_user_id UNIQUE (user_id);


--
-- Name: idx_oauth2_client_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_oauth2_client_id ON byteasy_cloud_platform.oauth2_registered_client USING btree (client_id);


--
-- Name: idx_platform_group_i18n_group_id_code; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX idx_platform_group_i18n_group_id_code ON byteasy_cloud_platform.platform_group_i18n USING btree (group_id, code);


--
-- Name: idx_platform_job_user_job_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_job_user_job_id ON byteasy_cloud_platform.platform_job_user USING btree (job_id);


--
-- Name: idx_platform_job_user_user_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_job_user_user_id ON byteasy_cloud_platform.platform_job_user USING btree (user_id);


--
-- Name: idx_platform_resource_openapi_openapi_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_resource_openapi_openapi_id ON byteasy_cloud_platform.platform_resource_openapi USING btree (openapi_id);


--
-- Name: idx_platform_resource_openapi_resource_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_resource_openapi_resource_id ON byteasy_cloud_platform.platform_resource_openapi USING btree (resource_id);


--
-- Name: idx_platform_role_resource_resource_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_role_resource_resource_id ON byteasy_cloud_platform.platform_role_resource USING btree (resource_id);


--
-- Name: idx_platform_role_resource_role_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_role_resource_role_id ON byteasy_cloud_platform.platform_role_resource USING btree (role_id);


--
-- Name: idx_platform_schema_define_class_name; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_schema_define_class_name ON byteasy_cloud_platform.platform_schema_define USING btree (class_name);


--
-- Name: idx_platform_user_group_user_user_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_group_user_user_id ON byteasy_cloud_platform.platform_user_group_user USING btree (user_id);


--
-- Name: idx_platform_user_resource_resource_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_resource_resource_id ON byteasy_cloud_platform.platform_user_resource USING btree (resource_id);


--
-- Name: idx_platform_user_resource_user_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_resource_user_id ON byteasy_cloud_platform.platform_user_resource USING btree (user_id);


--
-- Name: idx_platform_user_role_role_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_role_role_id ON byteasy_cloud_platform.platform_user_role USING btree (role_id);


--
-- Name: idx_platform_user_role_user_id; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_role_user_id ON byteasy_cloud_platform.platform_user_role USING btree (user_id);


--
-- Name: uk_platform_dictionary_code1; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_dictionary_code1 ON byteasy_cloud_platform.platform_dictionary USING btree (code);


--
-- Name: uk_platform_realm_code; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_realm_code ON byteasy_cloud_platform.platform_realm USING btree (code);


--
-- Name: uk_platform_resource_code; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_resource_code ON byteasy_cloud_platform.platform_resource USING btree (code);


--
-- Name: uk_platform_user_group_code; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_user_group_code ON byteasy_cloud_platform.platform_user_group USING btree (code);


--
-- Name: uk_resource_category_code; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_resource_category_code ON byteasy_cloud_platform.platform_resource_category USING btree (code);


--
-- Name: uk_resource_group_code; Type: INDEX; Schema: byteasy_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_resource_group_code ON byteasy_cloud_platform.platform_resource_group USING btree (code);


--
-- Name: idx_oauth2_client_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_oauth2_client_id ON tuna_cloud_platform.oauth2_registered_client USING btree (client_id);


--
-- Name: idx_platform_group_i18n_group_id_code; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX idx_platform_group_i18n_group_id_code ON tuna_cloud_platform.platform_group_i18n USING btree (group_id, code);


--
-- Name: idx_platform_job_user_job_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_job_user_job_id ON tuna_cloud_platform.platform_job_user USING btree (job_id);


--
-- Name: idx_platform_job_user_user_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_job_user_user_id ON tuna_cloud_platform.platform_job_user USING btree (user_id);


--
-- Name: idx_platform_resource_openapi_openapi_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_resource_openapi_openapi_id ON tuna_cloud_platform.platform_resource_openapi USING btree (openapi_id);


--
-- Name: idx_platform_resource_openapi_resource_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_resource_openapi_resource_id ON tuna_cloud_platform.platform_resource_openapi USING btree (resource_id);


--
-- Name: idx_platform_role_resource_resource_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_role_resource_resource_id ON tuna_cloud_platform.platform_role_resource USING btree (resource_id);


--
-- Name: idx_platform_role_resource_role_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_role_resource_role_id ON tuna_cloud_platform.platform_role_resource USING btree (role_id);


--
-- Name: idx_platform_schema_define_class_name; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_schema_define_class_name ON tuna_cloud_platform.platform_schema_define USING btree (class_name);


--
-- Name: idx_platform_user_group_user_user_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_group_user_user_id ON tuna_cloud_platform.platform_user_group_user USING btree (user_id);


--
-- Name: idx_platform_user_resource_resource_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_resource_resource_id ON tuna_cloud_platform.platform_user_resource USING btree (resource_id);


--
-- Name: idx_platform_user_resource_user_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_resource_user_id ON tuna_cloud_platform.platform_user_resource USING btree (user_id);


--
-- Name: idx_platform_user_role_role_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_role_role_id ON tuna_cloud_platform.platform_user_role USING btree (role_id);


--
-- Name: idx_platform_user_role_user_id; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE INDEX idx_platform_user_role_user_id ON tuna_cloud_platform.platform_user_role USING btree (user_id);


--
-- Name: uk_platform_dictionary_code1; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_dictionary_code1 ON tuna_cloud_platform.platform_dictionary USING btree (code);


--
-- Name: uk_platform_realm_code; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_realm_code ON tuna_cloud_platform.platform_realm USING btree (code);


--
-- Name: uk_platform_resource_code; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_resource_code ON tuna_cloud_platform.platform_resource USING btree (code);


--
-- Name: uk_platform_user_group_code; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_platform_user_group_code ON tuna_cloud_platform.platform_user_group USING btree (code);


--
-- Name: uk_resource_category_code; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_resource_category_code ON tuna_cloud_platform.platform_resource_category USING btree (code);


--
-- Name: uk_resource_group_code; Type: INDEX; Schema: tuna_cloud_platform; Owner: postgres
--

CREATE UNIQUE INDEX uk_resource_group_code ON tuna_cloud_platform.platform_resource_group USING btree (code);


--
-- Name: oauth2_registered_client_openapi fk_oauth2_registered_client_openapi_openapi; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_registered_client_openapi
    ADD CONSTRAINT fk_oauth2_registered_client_openapi_openapi FOREIGN KEY (openapi_id) REFERENCES byteasy_cloud_platform.platform_openapi(id) ON DELETE CASCADE;


--
-- Name: oauth2_registered_client_openapi fk_oauth2_registered_client_openapi_registered_client_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.oauth2_registered_client_openapi
    ADD CONSTRAINT fk_oauth2_registered_client_openapi_registered_client_id FOREIGN KEY (oauth2_registered_client_id) REFERENCES byteasy_cloud_platform.oauth2_registered_client(id) ON DELETE CASCADE;


--
-- Name: platform_openapi fk_openapi_service_id_restrict; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_openapi
    ADD CONSTRAINT fk_openapi_service_id_restrict FOREIGN KEY (service_id) REFERENCES byteasy_cloud_platform.platform_service_instance(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: platform_dictionary_item fk_platform_dictionary_item_dictionary_id_restrict; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_dictionary_item
    ADD CONSTRAINT fk_platform_dictionary_item_dictionary_id_restrict FOREIGN KEY (dictionary_id) REFERENCES byteasy_cloud_platform.platform_dictionary(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: platform_entity_define fk_platform_entity_define_service_code; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_entity_define
    ADD CONSTRAINT fk_platform_entity_define_service_code FOREIGN KEY (service_code) REFERENCES byteasy_cloud_platform.platform_service_instance(code) ON DELETE CASCADE;


--
-- Name: platform_job_resource fk_platform_job_resource_job_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job_resource
    ADD CONSTRAINT fk_platform_job_resource_job_id FOREIGN KEY (job_id) REFERENCES byteasy_cloud_platform.platform_job(id) ON DELETE CASCADE;


--
-- Name: platform_job_resource fk_platform_job_resource_resource_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job_resource
    ADD CONSTRAINT fk_platform_job_resource_resource_id FOREIGN KEY (resource_id) REFERENCES byteasy_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_job_user fk_platform_job_user_job_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job_user
    ADD CONSTRAINT fk_platform_job_user_job_id FOREIGN KEY (job_id) REFERENCES byteasy_cloud_platform.platform_job(id) ON DELETE CASCADE;


--
-- Name: platform_job_user fk_platform_job_user_user_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_job_user
    ADD CONSTRAINT fk_platform_job_user_user_id FOREIGN KEY (user_id) REFERENCES byteasy_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: platform_org_resource fk_platform_org_resource_org_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_org_resource
    ADD CONSTRAINT fk_platform_org_resource_org_id FOREIGN KEY (org_id) REFERENCES byteasy_cloud_platform.platform_org(id) ON DELETE CASCADE;


--
-- Name: platform_org_resource fk_platform_org_resource_resource_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_org_resource
    ADD CONSTRAINT fk_platform_org_resource_resource_id FOREIGN KEY (resource_id) REFERENCES byteasy_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_resource_group fk_platform_resource_group_category_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_group
    ADD CONSTRAINT fk_platform_resource_group_category_id FOREIGN KEY (category_id) REFERENCES byteasy_cloud_platform.platform_resource_category(id) ON DELETE CASCADE;


--
-- Name: platform_resource fk_platform_resource_group_id_restrict; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource
    ADD CONSTRAINT fk_platform_resource_group_id_restrict FOREIGN KEY (group_id) REFERENCES byteasy_cloud_platform.platform_resource_group(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: platform_resource_openapi fk_platform_resource_openapi_openapi_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_openapi
    ADD CONSTRAINT fk_platform_resource_openapi_openapi_id FOREIGN KEY (openapi_id) REFERENCES byteasy_cloud_platform.platform_openapi(id) ON DELETE CASCADE;


--
-- Name: platform_resource_openapi fk_platform_resource_openapi_resource_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_resource_openapi
    ADD CONSTRAINT fk_platform_resource_openapi_resource_id FOREIGN KEY (resource_id) REFERENCES byteasy_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_role_resource fk_platform_role_resource_resource_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_resource_id FOREIGN KEY (resource_id) REFERENCES byteasy_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_role_resource fk_platform_role_resource_role_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_role_id FOREIGN KEY (role_id) REFERENCES byteasy_cloud_platform.platform_role(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_resource fk_platform_user_group_resource_group_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group_resource
    ADD CONSTRAINT fk_platform_user_group_resource_group_id FOREIGN KEY (group_id) REFERENCES byteasy_cloud_platform.platform_user_group(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_resource fk_platform_user_group_resource_resource_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group_resource
    ADD CONSTRAINT fk_platform_user_group_resource_resource_id FOREIGN KEY (resource_id) REFERENCES byteasy_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_user fk_platform_user_group_user_group_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group_user
    ADD CONSTRAINT fk_platform_user_group_user_group_id FOREIGN KEY (group_id) REFERENCES byteasy_cloud_platform.platform_user_group(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_user fk_platform_user_group_user_user_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_group_user
    ADD CONSTRAINT fk_platform_user_group_user_user_id FOREIGN KEY (user_id) REFERENCES byteasy_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: platform_user_resource fk_platform_user_resource_role_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_resource
    ADD CONSTRAINT fk_platform_user_resource_role_id FOREIGN KEY (resource_id) REFERENCES byteasy_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_user_resource fk_platform_user_resource_user_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_resource
    ADD CONSTRAINT fk_platform_user_resource_user_id FOREIGN KEY (user_id) REFERENCES byteasy_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: platform_user_role fk_platform_user_role_role_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_role
    ADD CONSTRAINT fk_platform_user_role_role_id FOREIGN KEY (role_id) REFERENCES byteasy_cloud_platform.platform_role(id) ON DELETE CASCADE;


--
-- Name: platform_user_role fk_platform_user_role_user_id; Type: FK CONSTRAINT; Schema: byteasy_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY byteasy_cloud_platform.platform_user_role
    ADD CONSTRAINT fk_platform_user_role_user_id FOREIGN KEY (user_id) REFERENCES byteasy_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: oauth2_registered_client_openapi fk_oauth2_registered_client_openapi_openapi; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_registered_client_openapi
    ADD CONSTRAINT fk_oauth2_registered_client_openapi_openapi FOREIGN KEY (openapi_id) REFERENCES tuna_cloud_platform.platform_openapi(id) ON DELETE CASCADE;


--
-- Name: oauth2_registered_client_openapi fk_oauth2_registered_client_openapi_registered_client_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.oauth2_registered_client_openapi
    ADD CONSTRAINT fk_oauth2_registered_client_openapi_registered_client_id FOREIGN KEY (oauth2_registered_client_id) REFERENCES tuna_cloud_platform.oauth2_registered_client(id) ON DELETE CASCADE;


--
-- Name: platform_openapi fk_openapi_service_id_restrict; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_openapi
    ADD CONSTRAINT fk_openapi_service_id_restrict FOREIGN KEY (service_id) REFERENCES tuna_cloud_platform.platform_service_instance(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: platform_dictionary_item fk_platform_dictionary_item_dictionary_id_restrict; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_dictionary_item
    ADD CONSTRAINT fk_platform_dictionary_item_dictionary_id_restrict FOREIGN KEY (dictionary_id) REFERENCES tuna_cloud_platform.platform_dictionary(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: platform_entity_define fk_platform_entity_define_service_code; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_entity_define
    ADD CONSTRAINT fk_platform_entity_define_service_code FOREIGN KEY (service_code) REFERENCES tuna_cloud_platform.platform_service_instance(code) ON DELETE CASCADE;


--
-- Name: platform_job_resource fk_platform_job_resource_job_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job_resource
    ADD CONSTRAINT fk_platform_job_resource_job_id FOREIGN KEY (job_id) REFERENCES tuna_cloud_platform.platform_job(id) ON DELETE CASCADE;


--
-- Name: platform_job_resource fk_platform_job_resource_resource_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job_resource
    ADD CONSTRAINT fk_platform_job_resource_resource_id FOREIGN KEY (resource_id) REFERENCES tuna_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_job_user fk_platform_job_user_job_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job_user
    ADD CONSTRAINT fk_platform_job_user_job_id FOREIGN KEY (job_id) REFERENCES tuna_cloud_platform.platform_job(id) ON DELETE CASCADE;


--
-- Name: platform_job_user fk_platform_job_user_user_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_job_user
    ADD CONSTRAINT fk_platform_job_user_user_id FOREIGN KEY (user_id) REFERENCES tuna_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: platform_org_resource fk_platform_org_resource_org_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_org_resource
    ADD CONSTRAINT fk_platform_org_resource_org_id FOREIGN KEY (org_id) REFERENCES tuna_cloud_platform.platform_org(id) ON DELETE CASCADE;


--
-- Name: platform_org_resource fk_platform_org_resource_resource_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_org_resource
    ADD CONSTRAINT fk_platform_org_resource_resource_id FOREIGN KEY (resource_id) REFERENCES tuna_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_resource_group fk_platform_resource_group_category_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_group
    ADD CONSTRAINT fk_platform_resource_group_category_id FOREIGN KEY (category_id) REFERENCES tuna_cloud_platform.platform_resource_category(id) ON DELETE CASCADE;


--
-- Name: platform_resource fk_platform_resource_group_id_restrict; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource
    ADD CONSTRAINT fk_platform_resource_group_id_restrict FOREIGN KEY (group_id) REFERENCES tuna_cloud_platform.platform_resource_group(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: platform_resource_openapi fk_platform_resource_openapi_openapi_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_openapi
    ADD CONSTRAINT fk_platform_resource_openapi_openapi_id FOREIGN KEY (openapi_id) REFERENCES tuna_cloud_platform.platform_openapi(id) ON DELETE CASCADE;


--
-- Name: platform_resource_openapi fk_platform_resource_openapi_resource_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_resource_openapi
    ADD CONSTRAINT fk_platform_resource_openapi_resource_id FOREIGN KEY (resource_id) REFERENCES tuna_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_role_resource fk_platform_role_resource_resource_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_resource_id FOREIGN KEY (resource_id) REFERENCES tuna_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_role_resource fk_platform_role_resource_role_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_role_id FOREIGN KEY (role_id) REFERENCES tuna_cloud_platform.platform_role(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_resource fk_platform_user_group_resource_group_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group_resource
    ADD CONSTRAINT fk_platform_user_group_resource_group_id FOREIGN KEY (group_id) REFERENCES tuna_cloud_platform.platform_user_group(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_resource fk_platform_user_group_resource_resource_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group_resource
    ADD CONSTRAINT fk_platform_user_group_resource_resource_id FOREIGN KEY (resource_id) REFERENCES tuna_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_user fk_platform_user_group_user_group_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group_user
    ADD CONSTRAINT fk_platform_user_group_user_group_id FOREIGN KEY (group_id) REFERENCES tuna_cloud_platform.platform_user_group(id) ON DELETE CASCADE;


--
-- Name: platform_user_group_user fk_platform_user_group_user_user_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_group_user
    ADD CONSTRAINT fk_platform_user_group_user_user_id FOREIGN KEY (user_id) REFERENCES tuna_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: platform_user_resource fk_platform_user_resource_role_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_resource
    ADD CONSTRAINT fk_platform_user_resource_role_id FOREIGN KEY (resource_id) REFERENCES tuna_cloud_platform.platform_resource(id) ON DELETE CASCADE;


--
-- Name: platform_user_resource fk_platform_user_resource_user_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_resource
    ADD CONSTRAINT fk_platform_user_resource_user_id FOREIGN KEY (user_id) REFERENCES tuna_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- Name: platform_user_role fk_platform_user_role_role_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_role
    ADD CONSTRAINT fk_platform_user_role_role_id FOREIGN KEY (role_id) REFERENCES tuna_cloud_platform.platform_role(id) ON DELETE CASCADE;


--
-- Name: platform_user_role fk_platform_user_role_user_id; Type: FK CONSTRAINT; Schema: tuna_cloud_platform; Owner: postgres
--

ALTER TABLE ONLY tuna_cloud_platform.platform_user_role
    ADD CONSTRAINT fk_platform_user_role_user_id FOREIGN KEY (user_id) REFERENCES tuna_cloud_platform.platform_user(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict NJzIlBIr0lAbbhlVPuFcNagLINb7UZWDz3XlJ7mQdOq0K36OZef3wfXbkXSjcnm

