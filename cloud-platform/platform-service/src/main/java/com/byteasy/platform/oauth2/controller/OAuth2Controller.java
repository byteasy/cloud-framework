package com.byteasy.platform.oauth2.controller;

import com.byteasy.platform.service.DictionaryUtil;
import com.byteasy.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.byteasy.platform.Constant.*;

@Controller
public class OAuth2Controller {

    @Autowired
    private DictionaryUtil dictionaryUtil;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject(DIC_SETTING_FAVICON, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_FAVICON, "/platformSvr/assets/favicon.ico"));
        mav.addObject(DIC_SETTING_LOGO_IMG, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_LOGO_IMG, "/platformSvr/assets/logo.png"));
        mav.addObject(DIC_SETTING_BACKGROUND_IMG, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_BACKGROUND_IMG, "/platformSvr/assets/login-background.svg"));
        mav.addObject(DIC_SETTING_TITLE, dictionaryUtil.getStr(DICTIONARY_PLATFORM_SETTING, DIC_SETTING_TITLE, I18nUtil.getLanguageValue("login.pageTitle")));
        return mav;
    }
}

