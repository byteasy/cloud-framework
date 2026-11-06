## 第三方系统免密单点登录（SSO）通用接入与配置指南

### 1. 核心设计理念
本平台提供了一套统一的、高度可扩展的免密单点登录（SSO）引擎，入口统一收敛在：
```text
GET /platformSvr/papi/sso/{thirdAppName}/callback
```
#### 核心亮点：
- 1.0 协议无关（Protocol-Agnostic）：

- 标准模式：既可作为客户端（RP）对接任意成熟第三方 IdP（如泛微 OA、企业微信、钉钉、飞书、GitHub、自建 OAuth2 等）；
  
- 自定义模式：也支持外部任意业务系统直接携带 token / 签名 / 业务参数发起 HTTP 跳转直连，无需遵循标准 OAuth2 协议。

- 2.0 极低耦合（动态 JS 脚本驱动）：

- 平台核心底层代码零硬编码，任何第三方平台的参数解析、Token 加解密、接口远程验签换取用户信息，全部收敛在对应的 {thirdAppName} 动态 JS 脚本中自由编写。

- 3.0 安全快捷（Ticket 一次性即焚）：

- 验证通过后，平台自动发放短时（15 秒）防重放凭证 BT_SSO_TICKET，安全过滤器识别后自动建立系统认证态，直达目标页面，全程无需用户二次登录。

### 2. 整体执行流程

```text
               ┌────────────────────────────────────────────────────────┐
               │ 场景 A：成熟第三方 OAuth2 (泛微/企微/钉钉/GitHub等)         │
               │ 用户触发 -> 访问第三方 authorize 链接 -> 授权后回调本系统   │
               └──────────────────────────┬─────────────────────────────┘
                                          │
                                          ▼ (携带 code, state 等)
[外部系统/浏览器] ─────────────────────────────────────────────────────────────► [本系统统一SSO入口]
               ▲                          │                                  /papi/sso/{thirdAppName}/callback
               │                          │
               └──────────────────────────┴─────────────────────────────┘
               │ 场景 B：外部系统自定义跳转 (非标准OAuth2)                  │
               │ 直接构造链接跳入本系统，自定义携带 token/ticket/sign/path   │
               └────────────────────────────────────────────────────────┘
                                          │
                                          ▼
                         [执行动态 JS 脚本: {thirdAppName}]
                         • 解析入参 params & 请求头 headers
                         • 调三方接口换取用户信息 或 本地解密 token
                         • 返回系统识别的 username 与跳转 path
                                          │
                                          ▼ (验证成功)
                         [平台注入短时凭证 Cookie (BT_SSO_TICKET)]
                                          │
                                          ▼ (302 重定向)
                         [安全过滤器识别 Cookie，自动建立登录态]
                                          │
                                          ▼
                         [浏览器直达目标业务页面 (/index.html#path)]
```

### 3. 详细接入步骤

- 第一步：在平台编写适配 JS 脚本（核心）

  进入系统后台的 动态脚本管理，新建或维护一个脚本。

        关键规范：脚本的英文标识必须与回调 URL 路径中的 {thirdAppName} 严格一致（例如：4TyH9fMA2、wecom、fanwei 等）。
  脚本接收的上下文变量：

        params：Map 结构，包含 URL Query 中的所有请求参数（如 code、state、token、path 等）。

        headers：Map 结构，包含本次请求的所有 HTTP 请求头。

  脚本必须返回的对象格式：

```js
{
    "username": "administrator", // 必填：映射到本系统的登录账号（String）
    "path": "/SystemManage/PlatformManage/PlatformConfig" // 选填：登录成功后直达的前端 Hash 路由路径（以 / 开头）
}
```

  脚本参考模板：

```js
// 1. 获取传入参数（完全根据你的业务自由获取）
var code = params.code;         // OAuth2 场景下的授权码
var token = params.token;       // 自定义直连场景下的业务 Token
var path = params.path;         // 目标业务页面路由
var state = params.state;

logInfo("收到三方登录请求, params: {}", params);

var finalUsername = "";

// 2. 根据业务模式处理认证逻辑
if (code) {
    // 场景 A：标准 OAuth2 模式 —— 拿着 code 调用三方系统的 token 接口换取用户信息
    // var userRes = httpClient.post("https://oa.xxx.com/api/user/info", { code: code });
    // finalUsername = userRes.loginId;
    finalUsername = "administrator"; // 示例模拟
} else if (token) {
    // 场景 B：非标准直连模式 —— 解密 token 或校验验签
    // finalUsername = decryptToken(token);
    finalUsername = "administrator";
}

// 3. 构建并返回最终结果
var result = {};
result.username = finalUsername; // 必须匹配本系统内的账号

// 跳转目标地址（优先采用传入的 path，若无则跳转默认配置页）
result.path = path ? path : "/SystemManage/PlatformManage/PlatformConfig";

result;
```

- 第二步：根据业务场景构造跳转链接

- 模式一：对接成熟第三方系统的标准 OAuth2 认证（如企业微信、钉钉、泛微OA、GitHub 等）

  在第三方平台的开放平台/开发者后台中配置：
```text
1. 申请/查看客户端凭证：获取第三方的 client_id。
2. 配置重定向白名单（Redirect URI）：
在第三方后台登记本平台的回调地址：
http://{本系统域名}/platformSvr/papi/sso/{thirdAppName}/callback
3. 构造外部入口跳转链接（用户在第三方系统中点击该链接发起免密）：
https://{第三方IdP域名}/oauth2/authorize?client_id={clientId}&response_type=code&redirect_uri={经过URLEncode的回调地址}&state={自定义防重放随机串}
```

- 模式二：外部系统自定义参数直接跳入（无需依赖 OAuth2 协议）

  如果外部系统不需要走繁琐的 OAuth2 授权码协商握手，可以直接生成一个一次性 token / 签名，直接在浏览器中跳转至本系统的回调接口：

```text
1. 直接跳转链接格式：
http://{本系统域名}/platformSvr/papi/sso/{thirdAppName}/callback?token={一次性Token}&timestamp={当前时间戳}&path={目标页面路由}
2. 实际调用示例：
http://tuna.net/platformSvr/papi/sso/4TyH9fMA2/callback?token=eyJhbGciOi...&path=/SystemManage/PlatformManage/PlatformConfig
3. 工作机制：
浏览器访问该链接后，平台直接执行脚本 4TyH9fMA2，脚本内校验 params.token 合法后返回 username，平台自动写 Cookie 并直达指定的前端业务页面。
```

### 4. 常见问题排查与注意事项

```text
跳转后依然停留在登录页？
排查 1：检查脚本返回值中的 username 是否为空，或者该用户名在当前本系统数据库中是否存在。
排查 2：查看平台日志中是否存在 third app: xxx auto login failed 错误，确认脚本中是否有语法异常或远程接口超时。
登录成功但没有跳转到预期菜单，而是停在默认首页？
排查：检查传入的 path 格式。path 对应的是前端单页面应用的 Hash 路由，格式应以 / 开头（例如：/SystemManage/PlatformManage/PlatformConfig，不要在参数中自带 # 字符，系统跳转时会自动拼装 index.html#）。
第三方回调报 redirect_uri mismatch？
排查：使用模式一（标准 OAuth2）时，成熟的第三方平台通常会强校验 redirect_uri 是否与在他们开放平台后台配置的白名单完全匹配，请检查后台白名单配置是否遗漏或协议（HTTP/HTTPS）不一致。
```

### 5. 下面以github的跳转自动登录为例做详细说明

1. 登录http://byteasy.cn后代，进入低代码平台的Openapi管理界面，创建Openapi登录脚本接口，主要作用：根据第三方系统传递过来的参数，完成安全校验，获取第三方系统用户信息，映射或者自动创建本平台的账号，完成绑定。
   ![创建Openapi执行脚本](https://pic3.zhimg.com/80/v2-f8cf6c876bbd7d2483e9f2d59bfa4744_720w.webp)
2. 编写处理github自动登录脚本
```text
var code = params.code;
var state = params.state;
var path = params.path;
logInfo("state: {}", state);
// 这里仅做演示，具体如何调用github的api获取具体的账号信息，此处省略
var result = {};
// 这里直接绑定github的所有的用户为administrator
result.username = "administrator";
// 指定跳转过来的页面路由，自动登录成功后，会自动跳转到这个页面，此处也可以改为回调地址传参获取不同页面地址
result.path = "/SystemManage/PlatformManage/PlatformConfig";
result.path = path;

result;
```
3. 配置登录脚本白名单
   登录nacos配置中心，找到platform-service配置，增加如下配置
```text
platform:
  dataRootDir: /data/app/platform/data
  thirdLoginPermittedFunctionCodes: 4TyH9fMA2
```
4. 登录github，进入settings --> applications --> My GitHub Apps --> OAuth Apps --> New OAuth app,输入自己的域名及对应的回调地址（https://www.byteasy.cn/platformSvr/papi/sso/4TyH9fMA2/callback）
   ![创建github Oauth app](https://pic4.zhimg.com/80/v2-d4922f227bece745a9049ca84da12fdb_720w.webp)
   ![创建github Oauth app](https://picx.zhimg.com/80/v2-2604bae7c4b40317018003d2be39cb1b_720w.webp)
   ![创建github Oauth app](https://pic3.zhimg.com/80/v2-e761b8c8854c1f0cf42433b24d1260a2_720w.webp)
5. 创建好OAuth app后，记录自己的clientId，然后构造跳转链接：
   https://github.com/login/oauth/authorize?client_id=Ov23li4LnQADaqv95N4Z&scope=read:user&state=test_github_123&redirect_uri=https%3A%2F%2Fwww.byteasy.cn%2FplatformSvr%2Fpapi%2Fsso%2F4TyH9fMA2%2Fcallback
   点击上述链接，完成自动登录。