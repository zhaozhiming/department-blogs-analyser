package com.github.dba.service;

import com.github.dba.model.Blog;
import com.sina.sae.mail.SaeMail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {
    private static final Log log = LogFactory.getLog(MailService.class);

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

    public void sendNewBlogs(List<Blog> blogs) {
        SaeMail mail = new SaeMail();

        mail.setFrom(sendMail);
        mail.setSmtpUsername(sendMail);
        mail.setSmtpPassword(mailPassword);
        mail.setSmtpHost(smtpHost);
        mail.setSmtpPort(smtpPort);

        mail.setTo(new String[]{toMail});
        mail.setSubject("[通知]有人发文章啦~大家速顶!");
        mail.setContentType("HTML");
        mail.setChartset("UTF-8");
        mail.setContent("fyi");

        if (!mail.send()) {
            log.debug("send new blogs mail fail");
        }
    }
}
