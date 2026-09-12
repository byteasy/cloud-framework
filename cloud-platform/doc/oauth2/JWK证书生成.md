# 进入项目 resource 目录（如 src/main/resources/keys）
mkdir -p src/main/resources/keys
cd src/main/resources/keys

# 生成 RSA 私钥（PKCS8 格式，Java 推荐）
openssl genrsa -out private.key 2048
# 从私钥导出公钥
openssl rsa -in private.key -pubout -out public.key

src/main/resources/
└── oauth2/
├── private.key  # RSA 私钥
└── public.key   # RSA 公钥

openssl pkcs8 -topk8 -inform PEM -in private.key -out private_pkcs8.key -nocrypt
# 替换原私钥（可选）
mv private_pkcs8.key private.key