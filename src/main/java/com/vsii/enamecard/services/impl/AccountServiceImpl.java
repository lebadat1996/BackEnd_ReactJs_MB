package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.exceptions.HttpErrorException;
import com.vsii.enamecard.jwt.JwtTokenProvider;
import com.vsii.enamecard.jwt.LoginSession;
import com.vsii.enamecard.jwt.model.CustomUserDetails;
import com.vsii.enamecard.model.dto.AccountDTO;
import com.vsii.enamecard.model.entities.AccountEntity;
import com.vsii.enamecard.model.entities.ENameCardEntity;
import com.vsii.enamecard.model.request.ChangePasswordRequest;
import com.vsii.enamecard.model.request.LoginRequest;
import com.vsii.enamecard.model.response.LoginResponse;
import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.repositories.AccountRepository;
import com.vsii.enamecard.services.AccountService;
import com.vsii.enamecard.services.MailService;
import com.vsii.enamecard.services.RoleService;
import com.vsii.enamecard.utils.Constant;
import com.vsii.enamecard.utils.EncryptUtils;
import com.vsii.enamecard.utils.StringResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final LoginSession loginSession;
    private final RoleService roleService;
    private final MailService mailService;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository,
                              AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider,
                              PasswordEncoder passwordEncoder,
                              LoginSession loginSession,
                              RoleService roleService,
                              MailService mailService,
                              ModelMapper modelMapper) {
        this.repository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.loginSession = loginSession;
        this.roleService = roleService;
        this.mailService = mailService;
        this.modelMapper = modelMapper;
    }

    public AccountEntity findUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public AccountEntity findUserById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public SystemResponse login(LoginRequest loginRequest) {

        SystemResponse systemResponse = new SystemResponse();

        AccountEntity accountEntity = repository.findByUsernameAndPassword(loginRequest.getUsername(), passwordEncoder.encode(loginRequest.getPassword()));

        if (accountEntity == null) {
            systemResponse.setError(StringResponse.BAD_REQUEST).setStatus(HttpStatus.UNAUTHORIZED.value()).setData("username or password is wrong");
            return systemResponse;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        loginSession.revokeLoginSession(String.valueOf(userDetails.getAccountEntity().getId()));

        String jwt = jwtTokenProvider.generateToken(userDetails);
        LoginResponse loginResponse = modelMapper.map(userDetails.getAccountEntity(),LoginResponse.class);
        loginResponse.setRoleName(roleService.findById(loginResponse.getRoleId()).getName());
        loginResponse.setToken(jwt);
        systemResponse.setData(loginResponse);
        systemResponse.setStatus(HttpStatus.OK.value());
        systemResponse.setError(StringResponse.OK);
        return systemResponse;
    }

    public SystemResponse logout() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loginSession.revokeLoginSession(String.valueOf(customUserDetails.getAccountEntity().getId()));

        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setData(customUserDetails.getUsername() + " logged out");
        systemResponse.setStatus(HttpStatus.OK.value());
        systemResponse.setError(StringResponse.OK);
        return systemResponse;
    }

    public AccountDTO createAccountDefault(ENameCardEntity eNameCardEntity) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(eNameCardEntity.getPhone());
        String pass = EncryptUtils.getAlphaNumericString(10);
        accountEntity.setPassword(passwordEncoder.encode(pass));
        accountEntity.setFirstLogin(true);
        accountEntity.setStatus(AccountEntity.Status.ACTIVE);
        accountEntity.setRoleId(roleService.findByName(Constant.ROLE_NAME_AGENT).getId());
        accountEntity.setDateCreate(OffsetDateTime.now());
        accountEntity.setEmail(eNameCardEntity.getEmail());
        accountEntity.setENameCardId(eNameCardEntity.getId());
        repository.save(accountEntity);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername(accountEntity.getUsername());
        accountDTO.setPassword(pass);
        accountDTO.setEmail(eNameCardEntity.getEmail());
        return accountDTO;
    }

    @Override
    public SystemResponse changePassword(ChangePasswordRequest changePasswordRequest) {

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountEntity accountEntity = customUserDetails.getAccountEntity();
        if (!repository.existsByUsernameAndPassword(accountEntity.getUsername(),passwordEncoder.encode(changePasswordRequest.getOldPassword()))){
            throw HttpErrorException.badRequest("old password is wrong");
        }
        accountEntity.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
//        loginSession.revokeLoginSession(String.valueOf(customUserDetails.getAccountEntity().getId()));
        accountEntity.setFirstLogin(false);
        repository.save(accountEntity);

        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setStatus(HttpStatus.OK.value());
        systemResponse.setError("change password successful");
        return systemResponse;
    }

    @Override
    public SystemResponse forGotPassword(String username) {

        AccountEntity accountEntity = repository.findByUsername(username);
        if (accountEntity == null){
            throw HttpErrorException.badRequest("account not exists");
        }
        String pass = EncryptUtils.getAlphaNumericString(10);
        accountEntity.setPassword(passwordEncoder.encode(pass));
        accountEntity.setFirstLogin(true);
        repository.save(accountEntity);
        mailService.sendMail(accountEntity.getEmail(),"Thông cáo cấp lại mật khẩu","mật khẩu mới tại localhost:3000 là: " + pass);
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setStatus(HttpStatus.OK.value());
        systemResponse.setError("sent new password to your mail!");
        return systemResponse;
    }

    @Override
    public void updateStatus(String username, AccountEntity.Status status) {
        AccountEntity accountEntity = repository.findByUsername(username);
        accountEntity.setStatus(status);
        repository.save(accountEntity);
    }

    @Override
    public AccountEntity save(AccountEntity accountEntity) {
        return repository.save(accountEntity);
    }
}
