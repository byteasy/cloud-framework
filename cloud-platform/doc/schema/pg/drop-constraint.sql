-- 1. 删除 oauth2_registered_client_openapi 表的外键约束
ALTER TABLE oauth2_registered_client_openapi
DROP CONSTRAINT IF EXISTS fk_oauth2_registered_client_openapi_registered_client_id;

ALTER TABLE oauth2_registered_client_openapi
DROP CONSTRAINT IF EXISTS fk_oauth2_registered_client_openapi_openapi;

-- 2. 删除 platform_job_resource 表的外键约束
ALTER TABLE platform_job_resource
DROP CONSTRAINT IF EXISTS fk_platform_job_resource_job_id;

ALTER TABLE platform_job_resource
DROP CONSTRAINT IF EXISTS fk_platform_job_resource_resource_id;

-- 3. 删除 platform_job_user 表的外键约束
ALTER TABLE platform_job_user
DROP CONSTRAINT IF EXISTS fk_platform_job_user_user_id;

ALTER TABLE platform_job_user
DROP CONSTRAINT IF EXISTS fk_platform_job_user_job_id;

-- 4. 删除 platform_openapi 表的外键约束
ALTER TABLE platform_openapi
DROP CONSTRAINT IF EXISTS fk_openapi_service_id_restrict;

-- 5. 删除 platform_org_resource 表的外键约束
ALTER TABLE platform_org_resource
DROP CONSTRAINT IF EXISTS fk_platform_org_resource_org_id;

ALTER TABLE platform_org_resource
DROP CONSTRAINT IF EXISTS fk_platform_org_resource_resource_id;

-- 6. 删除 platform_resource 表的外键约束
ALTER TABLE platform_resource
DROP CONSTRAINT IF EXISTS fk_platform_resource_group_id_restrict;

-- 7. 删除 platform_resource_openapi 表的外键约束
ALTER TABLE platform_resource_openapi
DROP CONSTRAINT IF EXISTS fk_platform_resource_openapi_resource_id;

ALTER TABLE platform_resource_openapi
DROP CONSTRAINT IF EXISTS fk_platform_resource_openapi_openapi_id;

-- 8. 删除 platform_role_resource 表的外键约束
ALTER TABLE platform_role_resource
DROP CONSTRAINT IF EXISTS fk_platform_role_resource_role_id;

ALTER TABLE platform_role_resource
DROP CONSTRAINT IF EXISTS fk_platform_role_resource_resource_id;

-- 9. 删除 platform_user_group_resource 表的外键约束
ALTER TABLE platform_user_group_resource
DROP CONSTRAINT IF EXISTS fk_platform_user_group_resource_group_id;

ALTER TABLE platform_user_group_resource
DROP CONSTRAINT IF EXISTS fk_platform_user_group_resource_resource_id;

-- 10. 删除 platform_user_group_user 表的外键约束
ALTER TABLE platform_user_group_user
DROP CONSTRAINT IF EXISTS fk_platform_user_group_user_user_id;

ALTER TABLE platform_user_group_user
DROP CONSTRAINT IF EXISTS fk_platform_user_group_user_group_id;

-- 11. 删除 platform_user_resource 表的外键约束
ALTER TABLE platform_user_resource
DROP CONSTRAINT IF EXISTS fk_platform_user_resource_user_id;

ALTER TABLE platform_user_resource
DROP CONSTRAINT IF EXISTS fk_platform_user_resource_role_id;  -- 原语句中约束名笔误（关联resource_id但命名为role_id）

-- 12. 删除 platform_user_role 表的外键约束
ALTER TABLE platform_user_role
DROP CONSTRAINT IF EXISTS fk_platform_user_role_user_id;

ALTER TABLE platform_user_role
DROP CONSTRAINT IF EXISTS fk_platform_user_role_role_id;

ALTER TABLE platform_resource_group
DROP CONSTRAINT IF EXISTS fk_platform_resource_group_category_id;

ALTER TABLE platform_entity_define
DROP CONSTRAINT IF EXISTS fk_platform_entity_define_service_code;

ALTER TABLE platform_openapi
DROP CONSTRAINT IF EXISTS fk_platform_openapi_js_script_id;

ALTER TABLE platform_resource_group
DROP CONSTRAINT IF EXISTS fk_platform_resource_group_service_code;

ALTER TABLE platform_script_function
DROP CONSTRAINT IF EXISTS fk_platform_script_function_service_name;