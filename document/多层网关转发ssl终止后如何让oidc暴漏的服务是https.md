## SSL 卸载（TLS Termination）场景下 Spring Authorization Server 端点自适应 HTTPS 配置指南

1. 背景与问题

在现代微服务与云原生架构中，通常会在最前端的云厂商负载均衡器（SLB/ALB）、CDN（如 Cloudflare）、硬件负载均衡（F5）或 API 网关（如 APISIX） 层统一配置 SSL 证书并完成 SSL/TLS 终止（SSL Offloading/Termination）。流量进入网关内部后，通常以纯明文的 HTTP 协议转发至后端的微服务应用。
然而，Spring Authorization Server 作为 OIDC 认证中心时，客户端访问其服务发现端点：
```text
GET https://{host}/oauth2/.well-known/openid-configuration
```
其默认行为会根据后端容器（Tomcat）接收到的请求协议来动态计算颁发者（Issuer）及各个接口端点（如 authorization_endpoint、token_endpoint 等）。

由于网关转发给后端的是 HTTP 流量，Spring 默认会返回以 http:// 开头的端点地址。这会导致客户端在后续请求时遭遇跨协议重定向拦截、Mixed Content（混合内容）安全警告，甚至被严格遵守 OIDC 规范的客户端直接拒绝连接。
为了让 Spring Authorization Server 能够准确感知终端用户的真实访问协议并自动发布全量 https:// 端点，需要进行网关与服务的协同配置。

2. 解决方案与核心配置

步骤一：后端服务开启框架级代理头感知策略

在后端的 Spring Boot 配置文件（application.yml）中添加以下配置

```text
server:
  forward-headers-strategy: framework
```

配置作用原理解析：
- 激活 ForwardedHeaderFilter：Spring Boot 内部会自动注册该过滤器，拦截所有进入的 HTTP 请求。
- 协议与主机自适应改写：它会优先解析 RFC 7239 标准的 Forwarded 请求头（以及兼容传统的 X-Forwarded-Proto、X-Forwarded-Host、X-Forwarded-Port）。
- 透明重写底层上下文：检测到上游传递了 proto=https 时，该过滤器会自动将当前 Servlet 请求的 request.getScheme() 改写为 https，端口改写为 443。
- 驱动 OIDC 动态生成：Spring Authorization Server 在构造 OIDC 端点映射时，直接基于重写后的安全上下文动态生成，从而输出合规的 https:// 完整地址。

步骤二：在 APISIX 网关中配置请求头重写

在反向代理网关（以 Apache APISIX 为例）的路由插件中，启用 proxy-rewrite 插件，向上游服务注入符合 RFC 7239 国际标准 的转发头：
```text
  "plugins": {
    "proxy-rewrite": {
      "headers": {
        "Forwarded": "proto=https;host=tuna.net"
      }
    }
```
        💡 说明与最佳实践：
推荐使用标准 Forwarded 头：相比传统的 X-Forwarded-Proto，Forwarded: proto=https;host={host} 是 IETF 发布的正式标准头，不易被中间代理软件的默认规则误篡改或意外丢弃。
动态主机适配（可选）：如果该路由支持多个域名，可结合 APISIX 变量配置为 "Forwarded": "proto=https;host=$host"，实现多域名的自适应透传。


3. 验证与效果确认

完成上述两步配置并重启/重载服务后，在外网以 HTTPS 发起验证：
```text
curl -s "https://tuna.net/platformSvr/oauth2/.well-known/openid-configuration" | jq .
```

预期返回结果：

端点全量以 https:// 正确发布，没有任何 http:// 明文风险，符合 OpenID Connect Discovery 1.0 严格规范：

```json
{
  "issuer": "https://tuna.net/platformSvr",
  "authorization_endpoint": "https://tuna.net/platformSvr/oauth2/authorize",
  "token_endpoint": "https://tuna.net/platformSvr/oauth2/token",
  "jwks_uri": "https://tuna.net/platformSvr/oauth2/jwks",
  "userinfo_endpoint": "https://tuna.net/platformSvr/userinfo",
  "end_session_endpoint": "https://tuna.net/platformSvr/connect/logout"
}
```

