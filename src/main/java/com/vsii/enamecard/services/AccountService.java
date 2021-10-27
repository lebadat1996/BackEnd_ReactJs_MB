package com.vsii.enamecard.services;


import com.vsii.enamecard.model.dto.AccountDTO;
import com.vsii.enamecard.model.entities.AccountEntity;
import com.vsii.enamecard.model.entities.ENameCardEntity;
import com.vsii.enamecard.model.request.ChangePasswordRequest;
import com.vsii.enamecard.model.request.LoginRequest;
import com.vsii.enamecard.model.response.SystemResponse;

public interface AccountService {

    AccountEntity findUserByUsername(String username);

    AccountEntity findUserById(Integer id);

    SystemResponse login(LoginRequest loginRequest);

    SystemResponse logout();

    AccountDTO createAccountDefault(ENameCardEntity eNameCardEntity,int roleId,AccountDTO currentAccountContext);

    SystemResponse changePassword(ChangePasswordRequest changePasswordRequest);

    SystemResponse forGotPassword(String username);

    void updateStatusByUsername(String username, AccountEntity.Status status);

    AccountEntity save (AccountEntity accountEntity);

    AccountDTO getCurrentAccountContext();
}
