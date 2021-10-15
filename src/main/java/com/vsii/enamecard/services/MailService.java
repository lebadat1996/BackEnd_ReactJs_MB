package com.vsii.enamecard.services;


import com.vsii.enamecard.model.dto.AccountDTO;

public interface MailService {
    void sendMail(String emailResponsiblePerson, String subject, String content);

    void sendMailNotifyAccount(AccountDTO accountDTO);
}
