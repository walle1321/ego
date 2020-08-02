package com.ego.mail;

import com.ego.commons.pojo.MailPojo;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyMailSender {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public void send(MailPojo mailPojo) {
        try {
            // 整个邮件对象
            MimeMessage message = javaMailSender.createMimeMessage();
            // 让mimemessage使用更加方便
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            helper.setFrom(from);
            helper.setTo(mailPojo.getMail());
            helper.setSubject("易购订单创建成功确认邮件");
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("jqk.ftl");
            Map<String,Object> map = new HashMap<>();
            map.put("orderId",mailPojo.getId());
            String page = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            // 如果内容包含html，第二个参数一定要设置为true，表示解析html，否则把html当作字符串处理。
            helper.setText(page,true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
