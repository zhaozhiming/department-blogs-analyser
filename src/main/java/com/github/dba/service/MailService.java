package com.github.dba.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.github.dba.model.Blog;
import com.github.dba.model.MailInfo;
import com.github.dba.model.Top;
import com.google.common.collect.Maps;
import com.sina.sae.mail.SaeMail;

@Service
public class MailService {
    private static final Log log = LogFactory.getLog(MailService.class);
    private static final String MAIL_ENCODING = "UTF-8";

    @Value("${send_mail}")
    private String sendMail;

    @Value("${mail_password}")
    private String mailPassword;

    @Value("${smtp_host}")
    private String smtpHost;

    @Value("${smtp_port}")
    private int smtpPort;

    @Value("${to_mail}")
    private String toMail;

    @Resource(name = "velocityConfigurer")
    private VelocityConfigurer velocityConfigurer;

    public void sendNewBlogs(List<Blog> blogs) {
        log.debug("send new blogs mail start");
        log.debug(format("blogs: %s", blogs));

        SaeMail mail = createSaeMailInstance();
        Map<String, Object> model = Maps.newHashMap();
        model.put("blogs", blogs);

        sendMail("[通知]有人发文章啦~大家速顶!", mail, model, "new_blog_mail.vm");

        log.debug("send new blogs mail finish");
    }

    public void sendTops(List<Top> tops) {
        log.debug("send tops mail start");
        log.debug(format("tops: %s", tops));

        SaeMail mail = createSaeMailInstance();
        Map<String, Object> model = Maps.newHashMap();
        model.put("tops", tops);

        sendMail("[通知]本月文章排行榜", mail, model, "top_blog_mail.vm");

        log.debug("send tops mail finish");
    }
    
    public void sendMailDirectly(MailInfo mailInfo){
        SaeMail mail = createSaeMailInstance();
        mail.setTo(StringUtils.split(mailInfo.getTo(), ","));
        mail.setSubject(mailInfo.getSubject());
        mail.setContent(mailInfo.getContent());
        log.debug(format("mail to:%s|subject:%s|content:%s",
                mailInfo.getTo(), mailInfo.getSubject(),mailInfo.getContent()));
        if (!mail.send()) {
            log.debug("send mail fail");
        }
    }

    private void sendMail(String subject, SaeMail mail, Map<String, Object> model, String temple) {
        mail.setSubject(subject);
        mail.setTo(toMail.split(","));

        String content = VelocityEngineUtils.mergeTemplateIntoString(
                velocityConfigurer.getVelocityEngine(), temple,
                MAIL_ENCODING, model);
        log.debug(format("mail content:%s", content));

        mail.setContent(content);
        if (!mail.send()) {
            log.debug("send mail fail");
        }
    }

    private SaeMail createSaeMailInstance() {
        SaeMail mail = new SaeMail();
        mail.setFrom(sendMail);
        mail.setSmtpUsername(sendMail);
        mail.setSmtpPassword(mailPassword);
        mail.setSmtpHost(smtpHost);
        mail.setSmtpPort(smtpPort);

        mail.setContentType("HTML");
        mail.setChartset("UTF-8");
        return mail;
    }
}
