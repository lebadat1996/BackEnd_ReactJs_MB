package com.vsii.enamecard.controller;

import com.vsii.enamecard.model.request.ChangePasswordRequest;
import com.vsii.enamecard.model.request.ForgotPasswordRequest;
import com.vsii.enamecard.model.request.LoginRequest;
import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping(value = "/account")
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<SystemResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(accountService.login(loginRequest));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<SystemResponse> logout(){
        return ResponseEntity.ok(accountService.logout());
    }

    @PutMapping(value = "/password")
    public ResponseEntity<SystemResponse> changePassword (@RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok(accountService.changePassword(changePasswordRequest));
    }

    @PutMapping(value = "/password-mail")
    public ResponseEntity<SystemResponse> forGotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return ResponseEntity.ok(accountService.forGotPassword(forgotPasswordRequest.getUsername()));
    }
}
