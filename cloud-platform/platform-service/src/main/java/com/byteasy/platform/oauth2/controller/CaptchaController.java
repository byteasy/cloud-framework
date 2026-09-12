package com.byteasy.platform.oauth2.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.byteasy.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController("CaptchaController")
@RequestMapping(Constant.PAPI + "/captcha")
@Log4j2
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 生成验证码图片
     */
    @GetMapping("/generate")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码文本
        String captchaText = defaultKaptcha.createText();
        request.getSession().setAttribute("captcha_key", captchaText);
        // 生成验证码图片
        BufferedImage image = defaultKaptcha.createImage(captchaText);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "jpg", response.getOutputStream());
    }
}
