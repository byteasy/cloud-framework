import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
    srcDir: "../doc",

    title: "Byteasy",
    description: "易码之道（码道）",
    themeConfig: {
        nav: [
            { text: '首页', link: '/' },
            { text: '文档', link: '/2_配置说明/第三方系统免密单点登录（SSO）通用接入与配置指南' }
        ],

        sidebar: [
            {
                text: '配置说明',
                items: [
                    { text: '单点登录', link: '/2_配置说明/第三方系统免密单点登录（SSO）通用接入与配置指南' },
                ]
            },
            {
                text: '功能介绍',
                items: [
                    { text: '业务对象的审计', link: '/3_功能介绍/业务对象的审计' },
                ]
            },
            {
                text: '常见问题',
                items: [
                    { text: '多层网关转发ssl终止后如何让oidc暴漏的服务是https', link: '/4_常见问题/多层网关转发ssl终止后如何让oidc暴漏的服务是https' },
                ]
            },
        ],

        socialLinks: [
            { icon: 'github', link: 'https://github.com/byteasy/cloud-framework' }
        ]
    }
})