package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.model.dto.AccountDTO;
import com.vsii.enamecard.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender emailSender;

    public MailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String emailResponsiblePerson, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailResponsiblePerson);
        message.setSubject(subject);
        message.setText(content);
        this.emailSender.send(message);
        log.info("sent mail to person");
    }

    @Override
    public void sendMailNotifyAccount(AccountDTO accountDTO) {
        String subject = "Thông báo khơi tạo tài khoản E-NameCard";
        String content = "Bạn đã được cấp tài khoản e-namecard. \n tên tài khoản: "+ accountDTO.getUsername() + "\n mật khẩu: "+ accountDTO.getPassword() + "\n tại địa chỉ: localhost:3000";
        this.sendMail(accountDTO.getEmail(),subject,content);
    }


}
