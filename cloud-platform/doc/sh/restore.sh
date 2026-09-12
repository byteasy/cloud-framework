#!/bin/bash
set -e

# 数据库、用户变量
DB_NAME="byteasy"
DB_USER="byteasy"
DB_PWD="byteasy@A13"
BACKUP_FILE="/opt/archives/pg_backup/pg_backup.sql"

sh /data/app/platform/shutdown.sh

# 1. 删除数据库
sudo -u postgres dropdb --if-exists ${DB_NAME}

# 2. 级联删除用户，防止依赖报错
sudo -u postgres psql -U postgres -c "
DO \$\$
DECLARE
    r RECORD;
BEGIN
    IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'byteasy') THEN
        FOR r IN SELECT datname FROM pg_database LOOP
            EXECUTE 'REVOKE ALL PRIVILEGES ON DATABASE ' || quote_ident(r.datname) || ' FROM byteasy';
        END LOOP;
        DROP ROLE byteasy;
    END IF;
END \$\$;
"

# 3. 创建用户、设置密码、赋予建库权限
sudo -u postgres psql -U postgres -c "CREATE USER ${DB_USER} WITH PASSWORD '${DB_PWD}';"
sudo -u postgres psql -U postgres -c "ALTER ROLE ${DB_USER} CREATEDB;"

# 4. 创建数据库
sudo -u postgres createdb -U postgres ${DB_NAME}

# 5. 库级别授权
sudo -u postgres psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_USER};"

# 6. 恢复备份SQL
echo "开始恢复备份文件: ${BACKUP_FILE}"
sudo -u postgres psql -U postgres -d ${DB_NAME} -f ${BACKUP_FILE}

# 7. 修改三个schema所有者（彻底解决DDL权限不足）
sudo -u postgres psql -U postgres -d ${DB_NAME} -c "
ALTER SCHEMA byteasy_cloud_platform OWNER TO ${DB_USER};
ALTER SCHEMA knowledge_base OWNER TO ${DB_USER};
ALTER SCHEMA tuna_cloud_platform OWNER TO ${DB_USER};
ALTER SCHEMA micro_product_assistant OWNER TO ${DB_USER};
"

# 8. 三个schema完整授权
sudo -u postgres psql -U postgres -d ${DB_NAME} -c "
GRANT USAGE, CREATE ON SCHEMA micro_product_assistant,tuna_cloud_platform, byteasy_cloud_platform, knowledge_base TO ${DB_USER};

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA micro_product_assistant,tuna_cloud_platform, byteasy_cloud_platform, knowledge_base TO ${DB_USER};
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA micro_product_assistant,tuna_cloud_platform, byteasy_cloud_platform, knowledge_base TO ${DB_USER};

ALTER DEFAULT PRIVILEGES IN SCHEMA micro_product_assistant,tuna_cloud_platform, byteasy_cloud_platform, knowledge_base
GRANT ALL ON TABLES TO ${DB_USER};

ALTER DEFAULT PRIVILEGES IN SCHEMA micro_product_assistant,tuna_cloud_platform, byteasy_cloud_platform, knowledge_base
GRANT ALL ON SEQUENCES TO ${DB_USER};
"

# 9. 设置用户默认搜索路径
sudo -u postgres psql -U postgres -c "
ALTER ROLE ${DB_USER} SET search_path TO micro_product_assistant,tuna_cloud_platform, byteasy_cloud_platform, knowledge_base, public;
"

sudo -u postgres psql -U postgres -d byteasy -c "
UPDATE byteasy_cloud_platform.oauth2_registered_client
SET
redirect_uris = array(
    SELECT REPLACE(uri, 'http://tuna.net', 'https://www.byteasy.cn')
    FROM unnest(redirect_uris) AS uri
),
post_logout_redirect_uris = array(
    SELECT REPLACE(uri, 'http://tuna.net', 'https://www.byteasy.cn')
    FROM unnest(post_logout_redirect_uris) AS uri
)
WHERE
    array_to_string(redirect_uris, ',') LIKE '%tuna%'
    OR array_to_string(post_logout_redirect_uris, ',') LIKE '%tuna%';
"

sudo -u postgres psql -U postgres -d byteasy -c "
update byteasy_cloud_platform.platform_resource_group set entry = '//{host}/platformSvr/common/index.html' where id = 5
"

echo "PostgreSQL 数据库初始化、恢复、权限配置全部完成！"