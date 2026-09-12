-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE oauth2_registered_client_openapi
    ADD CONSTRAINT fk_oauth2_registered_client_openapi_registered_client_id
        FOREIGN KEY (oauth2_registered_client_id)
            REFERENCES oauth2_registered_client(id) ON DELETE CASCADE;

ALTER TABLE oauth2_registered_client_openapi
    ADD CONSTRAINT fk_oauth2_registered_client_openapi_openapi
        FOREIGN KEY (openapi_id)
            REFERENCES platform_openapi(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_job_resource
    ADD CONSTRAINT fk_platform_job_resource_job_id
        FOREIGN KEY (job_id)
            REFERENCES platform_job(id) ON DELETE CASCADE;

ALTER TABLE platform_job_resource
    ADD CONSTRAINT fk_platform_job_resource_resource_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_job_user
    ADD CONSTRAINT fk_platform_job_user_user_id
        FOREIGN KEY (user_id)
            REFERENCES platform_user(id) ON DELETE CASCADE;

ALTER TABLE platform_job_user
    ADD CONSTRAINT fk_platform_job_user_job_id
        FOREIGN KEY (job_id)
            REFERENCES platform_job(id) ON DELETE CASCADE;


-- 2. 级联更新 + 限制删除（推荐使用）
-- 当platform_service中的code更新时，自动更新openapi中的service_code
-- 当platform_service中的记录被删除时，如果openapi中有关联记录，则阻止删除
ALTER TABLE platform_openapi
    ADD CONSTRAINT fk_openapi_service_id_restrict
        FOREIGN KEY (service_id)
            REFERENCES platform_service_instance(id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_org_resource
    ADD CONSTRAINT fk_platform_org_resource_org_id
        FOREIGN KEY (org_id)
            REFERENCES platform_org(id) ON DELETE CASCADE;

ALTER TABLE platform_org_resource
    ADD CONSTRAINT fk_platform_org_resource_resource_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

-- 2. 级联更新，group改了子表自动改，group删除不允许删除
ALTER TABLE platform_resource
    ADD CONSTRAINT fk_platform_resource_group_id_restrict
        FOREIGN KEY (group_id)
            REFERENCES platform_resource_group(id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_resource_openapi
    ADD CONSTRAINT fk_platform_resource_openapi_resource_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

ALTER TABLE platform_resource_openapi
    ADD CONSTRAINT fk_platform_resource_openapi_openapi_id
        FOREIGN KEY (openapi_id)
            REFERENCES platform_openapi(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_role_id
        FOREIGN KEY (role_id)
            REFERENCES platform_role(id) ON DELETE CASCADE;

ALTER TABLE platform_role_resource
    ADD CONSTRAINT fk_platform_role_resource_resource_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_user_group_resource
    ADD CONSTRAINT fk_platform_user_group_resource_group_id
        FOREIGN KEY (group_id)
            REFERENCES platform_user_group(id) ON DELETE CASCADE;

ALTER TABLE platform_user_group_resource
    ADD CONSTRAINT fk_platform_user_group_resource_resource_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_user_group_user
    ADD CONSTRAINT fk_platform_user_group_user_user_id
        FOREIGN KEY (user_id)
            REFERENCES platform_user(id) ON DELETE CASCADE;

ALTER TABLE platform_user_group_user
    ADD CONSTRAINT fk_platform_user_group_user_group_id
        FOREIGN KEY (group_id)
            REFERENCES platform_user_group(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_user_resource
    ADD CONSTRAINT fk_platform_user_resource_user_id
        FOREIGN KEY (user_id)
            REFERENCES platform_user(id) ON DELETE CASCADE;

ALTER TABLE platform_user_resource
    ADD CONSTRAINT fk_platform_user_resource_role_id
        FOREIGN KEY (resource_id)
            REFERENCES platform_resource(id) ON DELETE CASCADE;

-- 2. 确保父表已存在后，再添加外键约束
ALTER TABLE platform_user_role
    ADD CONSTRAINT fk_platform_user_role_user_id
        FOREIGN KEY (user_id)
            REFERENCES platform_user(id) ON DELETE CASCADE;

ALTER TABLE platform_user_role
    ADD CONSTRAINT fk_platform_user_role_role_id
        FOREIGN KEY (role_id)
            REFERENCES platform_role(id) ON DELETE CASCADE;

ALTER TABLE platform_resource_group
    ADD CONSTRAINT fk_platform_resource_group_category_id
        FOREIGN KEY (category_id)
            REFERENCES platform_resource_category(id) ON DELETE CASCADE;


ALTER TABLE platform_entity_define
    ADD CONSTRAINT fk_platform_entity_define_service_code
        FOREIGN KEY (service_code)
            REFERENCES platform_service_instance(code) ON DELETE CASCADE;

ALTER TABLE platform_openapi
    ADD CONSTRAINT fk_platform_openapi_js_script_id
        FOREIGN KEY (script_id)
            REFERENCES platform_js_script(id) ON DELETE CASCADE;

ALTER TABLE platform_resource_group
    ADD CONSTRAINT fk_platform_resource_group_service_code
        FOREIGN KEY (service_code)
            REFERENCES platform_service_instance(code) ON DELETE CASCADE;

ALTER TABLE platform_script_function
    ADD CONSTRAINT fk_platform_script_function_service_name
        FOREIGN KEY (service_name)
            REFERENCES platform_service_instance(code) ON DELETE CASCADE;

ALTER TABLE "platform_field_define"
    ADD CONSTRAINT uk_platform_field_define_class_name_name UNIQUE (class_name, name);