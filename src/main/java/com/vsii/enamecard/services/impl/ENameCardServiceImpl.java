package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.exceptions.HttpErrorException;
import com.vsii.enamecard.jwt.model.CustomUserDetails;
import com.vsii.enamecard.model.dto.AccountDTO;
import com.vsii.enamecard.model.entities.*;
import com.vsii.enamecard.model.request.CreateENameCardRequest;
import com.vsii.enamecard.model.request.EditNameCardRequest;
import com.vsii.enamecard.model.response.ENameCardResponse;
import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.repositories.ENameCardRepository;
import com.vsii.enamecard.services.*;
import com.vsii.enamecard.utils.*;
import com.vsii.enamecard.utils.filters.SearchCriteria;
import com.vsii.enamecard.utils.filters.SearchOperator;
import com.vsii.enamecard.utils.filters.SpecificationBuilder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//@Service
//public class ENameCardServiceImpl implements ENameCardService {
//
//    private static final Logger log = LoggerFactory.getLogger(ENameCardServiceImpl.class);
//
//    private final ENameCardRepository repository;
//    private final String baseQRCode;
//    private final String baseZaloLink;
//    private final String pathTemplate;
//    private final String pathTemplateErrorFile;
//    private final MailService mailService;
//    private final String pathFolderImg;
//    private final AccountService accountService;
//    private final ModelMapper modelMapper;
//    private final RoleService roleService;
//    private final ChannelService channelService;
//    private final ENameCardHistoryService eNameCardHistoryService;
//
//    @Autowired
//    public ENameCardServiceImpl(ENameCardRepository eNameCardRepository,
//                                @Value("${base.qr.code}") String baseQRCode,
//                                @Value("${base.zalo.link}") String baseZaloLink,
//                                @Value("${path.template}") String pathTemplate,
//                                @Value("${path.template.error.file.enamecard}") String pathTemplateErrorFile,
//                                MailService mailService,
//                                @Value("${path.folder.img}") String pathFolderImg,
//                                AccountService accountService,
//                                ModelMapper modelMapper,
//                                RoleService roleService,
//                                ChannelService channelService,
//                                ENameCardHistoryService eNameCardHistoryService) {
//        repository = eNameCardRepository;
//        this.baseQRCode = baseQRCode;
//        this.baseZaloLink = baseZaloLink;
//        this.pathTemplate = pathTemplate;
//        this.pathTemplateErrorFile = pathTemplateErrorFile;
//        this.mailService = mailService;
//        this.pathFolderImg = pathFolderImg;
//        this.accountService = accountService;
//        this.modelMapper = modelMapper;
//        this.roleService = roleService;
//        this.channelService = channelService;
//        this.eNameCardHistoryService = eNameCardHistoryService;
//    }
//
//
//    @Override
//    public SystemResponse<PageImpl<ENameCardResponse>> getList(int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
//
//        List<SearchCriteria> searchCriteria = new ArrayList<>();
//        searchCriteria.add(new SearchCriteria().setKey("status").setOperation(SearchOperator.EQUAL).setValue(ENameCardEntity.Status.ACTIVE));
//        AccountDTO currentUserContext = accountService.getCurrentAccountContext();
//        if (isNotAdmin(currentUserContext.getRoleName(), Constant.NAME_ROLE_ADMIN)){
//            searchCriteria.add(new SearchCriteria().setKey("channelId").setOperation(SearchOperator.EQUAL).setValue(currentUserContext.getChannelId()));
//        }
//        SpecificationBuilder<ENameCardEntity> specificationBuilder = new SpecificationBuilder<>(searchCriteria);
//
//        Page eNameCardPage = repository.findAll(specificationBuilder,pageable);
//        List<ENameCardResponse> eNameCardResponses = (List<ENameCardResponse>) eNameCardPage.getContent().stream().map(eNameCard -> modelMapper.map(eNameCard,ENameCardResponse.class)).collect(Collectors.toList());
//
//        return new SystemResponse<>(HttpCodeResponse.SUCCESS, StringResponse.OK, new PageImpl<>(eNameCardResponses, pageable, eNameCardPage.getTotalElements()));
//    }
//
//
//    @Override
//    public SystemResponse<ENameCardResponse> create(CreateENameCardRequest createENameCardRequest) {
//
//        ENameCardEntity eNameCardEntity = modelMapper.map(createENameCardRequest,ENameCardEntity.class);
//
//        eNameCardEntity.setZaloLink(this.baseZaloLink + eNameCardEntity.getPhone());
//        eNameCardEntity.setStatus(ENameCardEntity.Status.ACTIVE);
//        eNameCardEntity.setCreatorId(accountService.getCurrentAccountContext().getId());
//
//        eNameCardEntity = repository.save(eNameCardEntity);
//
//        AccountDTO currentAccountContext = accountService.getCurrentAccountContext();
//        List<ChannelEntity> channelEntities = channelService.getAllChannelEntity();
//        List<RoleEntity> roleEntities = roleService.getAll();
//        int roleId = getRoleIdOfAccountDefault(eNameCardEntity.getChannelId(),channelEntities,roleEntities);
//        AccountDTO accountDTO = accountService.createAccountDefault(eNameCardEntity,roleId,currentAccountContext);
//
//        mailService.sendMailNotifyAccount(accountDTO);
//        return new SystemResponse<>(HttpCodeResponse.SUCCESS, StringResponse.OK);
//    }
//
//    private int getRoleIdOfAccountDefault(Integer channelId,List<ChannelEntity> channelEntities,List<RoleEntity> roleEntities) {
//        ChannelEntity channelEntity = channelEntities.parallelStream().filter(channel -> channel.getId().equals(channelId)).findFirst().orElseThrow(() -> HttpErrorException.badRequest("channel is not exits"));// miss validate channel Id at service
//        if (channelEntity.getName().equals(Constant.CHANNEL_AGENCY)){
//            return roleEntities.parallelStream().filter(role -> role.getName().equals(Constant.ROLE_NAME_AGENT)).findFirst().orElseThrow(() -> HttpErrorException.badRequest("role is not exited")).getId();
//        }
//        return roleEntities.parallelStream().filter(role -> role.getName().equals(Constant.ROLE_NAME_BANCAS)).findFirst().orElseThrow(() -> HttpErrorException.badRequest("role is not exited")).getId();
//    }
//
//    @Override
//    public SystemResponse<ENameCardResponse> edit(EditNameCardRequest editNameCardRequest, int id) {
//
//        ENameCardEntity eNameCardEntity = repository.findById(id).orElseThrow(() -> HttpErrorException.badRequest("e name card not found!"));
//        AccountEntity accountEntity = accountService.findUserByUsername(eNameCardEntity.getCodeAgent());
//        AccountDTO currentAccountContext = accountService.getCurrentAccountContext();
//
//        Field[] fieldsNameCardRequest = editNameCardRequest.getClass().getDeclaredFields();
//        Field[] fieldsNameCardEntity = eNameCardEntity.getClass().getDeclaredFields();
//
//        ENameCardEntity finalENameCardEntity = eNameCardEntity;
//        List<ENameCardHistoryEntity> eNameCardHistories = Arrays.stream(fieldsNameCardRequest).map(fieldRequest -> {
//
//            Field fieldEntity = Arrays.stream(fieldsNameCardEntity).filter(field -> field.getName().equals(fieldRequest.getName())).findFirst().orElseThrow(() -> HttpErrorException.conflict("field name not found of " + fieldRequest.getName()));
//            String oldValue = Utils.getValueFromFieldObject(finalENameCardEntity,fieldEntity);
//            String newValue = Utils.getValueFromFieldObject(editNameCardRequest,fieldRequest);
//            ENameCardHistoryEntity eNameCardHistoryEntity = null;
//
//            if (!oldValue.equals(newValue)){
//                eNameCardHistoryEntity = new ENameCardHistoryEntity()
//                        .seteNameCardId(id)
//                        .setFieldName(ENameCardStorage.fieldNameDescription.get(fieldEntity.getName()))
//                        .setOldValue(oldValue)
//                        .setNewValue(newValue)
//                        .setDateModify(OffsetDateTime.now())
//                        .setModifierId(currentAccountContext.getId());
//            }
//            return eNameCardHistoryEntity;
//        }).filter(Objects::nonNull).collect(Collectors.toList());
//
//        eNameCardEntity.setFullName(editNameCardRequest.getFullName())
//                .setPhone(editNameCardRequest.getPhone())
//                .setEmail(editNameCardRequest.getEmail())
//                .setFacebookLink(editNameCardRequest.getFacebookLink())
//                .setPositions(editNameCardRequest.getPositions())
//                .setCodeAgent(editNameCardRequest.getCodeAgent());
//        eNameCardEntity = repository.save(eNameCardEntity);
//
//        eNameCardHistoryService.saveAll(eNameCardHistories);
//
//        accountEntity.setEmail(eNameCardEntity.getEmail());
//        accountEntity.setUsername(eNameCardEntity.getCodeAgent());
//        accountService.save(accountEntity);
//
//        SystemResponse systemResponse = new SystemResponse();
//        systemResponse.setStatus(HttpCodeResponse.SUCCESS);
//        systemResponse.setError("update successful");
//        return systemResponse;
//    }
//
//
//    @Override
//    public Resource importBatch(MultipartFile file) throws IOException {
//        log.info("start import file");
//        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        List<ENameCardEntity> eNameCardEntities = new ArrayList<>();
//        List<ChannelEntity> channelEntities = channelService.getAllChannelEntity();
//        AccountDTO currentAccountContext = accountService.getCurrentAccountContext();
//
//        boolean fileInvalid = false;
//        for (int i = 11; i <= sheet.getLastRowNum(); i++) {
//
//            Row row = sheet.getRow(i);
//            ENameCardEntity eNameCardEntity = new ENameCardEntity();
//            if (rowIsInvalid(row,channelEntities,currentAccountContext,eNameCardEntity)){
//                fileInvalid = true;
//            }else {
//                eNameCardEntities.add(eNameCardEntity);
//            }
//        }
//
//        ByteArrayResource byteArrayResource = null;
//
//        if (fileInvalid){
//
//            byteArrayResource = exportFileError(file, workbook);
//            return byteArrayResource;
//        }
//        eNameCardEntities = repository.saveAll(eNameCardEntities);
//
//        AccountDTO currentUserContext = accountService.getCurrentAccountContext();
//        List<RoleEntity> roleEntities = roleService.getAll();
//        eNameCardEntities.forEach(eNameCard -> {
//            int roleId = this.getRoleIdOfAccountDefault(eNameCard.getChannelId(),channelEntities,roleEntities);
//            AccountDTO accountDTO = accountService.createAccountDefault(eNameCard,roleId,currentUserContext);
//            mailService.sendMailNotifyAccount(accountDTO);
//        });
//        log.info("import file sucessfull");
//        return byteArrayResource;
//    }
//
//    private ByteArrayResource exportFileError(MultipartFile file, XSSFWorkbook workbook) throws IOException {
//        ByteArrayResource byteArrayResource;
//        String pathFileError = this.pathTemplateErrorFile + file.getOriginalFilename() + EncryptUtils.getAlphaNumericString(10);
//        FileOutputStream fileOutputStream = new FileOutputStream(pathFileError);
//        workbook.write(fileOutputStream);
//        fileOutputStream.close();
//        byte[] fileArr = Files.readAllBytes(Paths.get(pathFileError));
//        byteArrayResource =  new ByteArrayResource(fileArr);
//        Files.delete(Paths.get(pathFileError));
//        return byteArrayResource;
//    }
//
//    private boolean rowIsInvalid(Row row, List<ChannelEntity> channelEntities, AccountDTO currentAccountContext, ENameCardEntity eNameCardEntity) {
//        DataFormatter formatter = new DataFormatter();
//        StringBuilder errorDescription = new StringBuilder("");
//        boolean rowInvalid = false;
//
//        String codeAgent = formatter.formatCellValue(row.getCell(0));
//        if (repository.existsByCodeAgent(codeAgent)){
//            errorDescription.append("code agent row" + row.getRowNum() + " is exists, ");
//        }
//        eNameCardEntity.setCodeAgent(codeAgent);
//        String fullName = formatter.formatCellValue(row.getCell(1));
//        if (Pattern.matches(Constant.PATTERN_FULL_NAME,fullName)){
//            errorDescription.append("this full name in row "  + row.getRowNum() + " is invalid format, ");
//        }
//        eNameCardEntity.setFullName(fullName);
//
//        eNameCardEntity.setPositions(formatter.formatCellValue(row.getCell(2)));
//
//        String phone = formatter.formatCellValue(row.getCell(3));
//        if (!Pattern.matches(Constant.PATTERN_PHONE,phone)){
//            errorDescription.append("this phone in row "  + row.getRowNum() + " is invalid format, ");
//        }
//        eNameCardEntity.setPhone(phone);
//
//        String email = formatter.formatCellValue(row.getCell(4));
//        if (!Pattern.matches(Constant.PATTERN_EMAIL,email)){
//            errorDescription.append("this email in row "  + row.getRowNum() + " is invalid format, ");
//        }
//        eNameCardEntity.setEmail(email);
//
//        eNameCardEntity.setFacebookLink(formatter.formatCellValue(row.getCell(5)));
//
//        String channelName = formatter.formatCellValue(row.getCell(6));
//        Optional<ChannelEntity> channelEntity = channelEntities.stream().filter(channel -> channel.getName().equals(channelName)).findFirst();
//        if (channelEntity.isEmpty()){
//            errorDescription.append("channel is invalid format!, ");
//        }else {
//            if (isNotAdmin(currentAccountContext.getRoleName(), Constant.NAME_ROLE_ADMIN)){
//                if (!(channelEntity.get().getId().equals(currentAccountContext.getChannelId()))){
//                    errorDescription.append("Current account cannot choose this channel!, ");
//                }
//            }
//            eNameCardEntity.setChannelId(channelEntity.get().getId());
//        }
//
//        eNameCardEntity.setCreatorId(currentAccountContext.getId());
//        eNameCardEntity.setDateCreate(OffsetDateTime.now());
//        eNameCardEntity.setDateModify(OffsetDateTime.now());
//        eNameCardEntity.setStatus(ENameCardEntity.Status.ACTIVE);
//        eNameCardEntity.setAvatar(Constant.AVATAR_DEFAULT);
//        eNameCardEntity.setZaloLink(this.baseZaloLink + eNameCardEntity.getPhone());
//
//        if (StringUtils.hasText(errorDescription.toString())){
//            Cell cellErrorDescription = row.createCell(7);
//            cellErrorDescription.setCellValue(errorDescription.toString());
//            rowInvalid = true;
//        }
//        return rowInvalid;
//    }
//
//    private boolean isNotAdmin(String roleName, String nameRoleAdmin) {
//        return !(roleName.equals(nameRoleAdmin));
//    }
//
//    @Override
//    public Resource exportFile() throws IOException {
//
//        byte[] fileArr = Files.readAllBytes(Paths.get(pathTemplate));
//
//        ByteArrayResource byteArrayResource = new ByteArrayResource(fileArr);
//
//        return byteArrayResource;
//    }
//
//    @Override
//    public SystemResponse<ENameCardResponse> findById(int id) {
//
//        ENameCardEntity eNameCardEntity;
//        eNameCardEntity = repository.findById(id).orElseThrow(() -> HttpErrorException.badRequest(StringResponse.E_NAME_CARD_NOT_EXISTED));
//
//        ENameCardResponse eNameCardResponse = modelMapper.map(eNameCardEntity,ENameCardResponse.class);
//
//        return new SystemResponse<>().setData(eNameCardResponse).setError(StringResponse.OK).setStatus(HttpCodeResponse.SUCCESS);
//    }
//
//
//    public Resource exportQRCode(int id) throws Exception {
//        String data = this.baseQRCode + id;
//        BufferedImage imageQR = QRCodeUtils.generateEAN13BarcodeImage(data, 300, 300);
//        return QRCodeUtils.toByteArrayAutoClosable(imageQR, "png");
//    }
//
//    @Override
//    public SystemResponse uploadAvatar(MultipartFile file) {
//
//        File newFile = saveFile(file);
//
//        return new SystemResponse().setData(newFile.getName()).setStatus(HttpCodeResponse.SUCCESS).setError(StringResponse.OK);
//    }
//
//    private File saveFile(MultipartFile file) {
//        String fileName = file.getOriginalFilename();
//        File folder = new File(this.pathFolderImg);
//        File newFile = null;
//        try {
//            if (!folder.exists()){
//                folder.mkdir();
//            }
//
//            newFile = new File(folder.getAbsolutePath() + File.separator + fileName);
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newFile));
//            stream.write(file.getBytes());
//            stream.close();
//
//        } catch (IOException e) {
//        }
//        return newFile;
//    }
//
//    @Override
//    public SystemResponse updateAvatar(MultipartFile file) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        ENameCardEntity eNameCardEntity = repository.findById(customUserDetails.getAccountEntity().getENameCardId()).orElseThrow(() -> HttpErrorException.badRequest("e name card not exists"));
//        if (!eNameCardEntity.getAvatar().equals(Constant.AVATAR_DEFAULT)){
//            removeAvatar(eNameCardEntity);
//        }
//        File newFile = saveFile(file);
//        eNameCardEntity.setAvatar(newFile.getName());
//        repository.save(eNameCardEntity);
//        SystemResponse systemResponse = new SystemResponse();
//        systemResponse.setStatus(HttpCodeResponse.SUCCESS);
//        systemResponse.setError("update avatar successful");
//        return systemResponse;
//    }
//
//    @Override
//    public boolean existsByEmail(String email) {
//        return repository.existsByEmail(email);
//    }
//
//    @Override
//    public boolean existsByPhone(String phone) {
//        return repository.existsByPhone(phone);
//    }
//
//    @Override
//    public SystemResponse delete(int id) {
//
//        ENameCardEntity eNameCardEntity = repository.getById(id);
//        eNameCardEntity.setStatus(ENameCardEntity.Status.INACTIVE);
//        accountService.updateStatusByUsername(eNameCardEntity.getCodeAgent(), AccountEntity.Status.INACTIVE);
//        repository.save(eNameCardEntity);
//
//        SystemResponse systemResponse = new SystemResponse();
//        systemResponse.setStatus(HttpCodeResponse.SUCCESS);
//        systemResponse.setError("delete successful");
//        return systemResponse;
//    }
//
//    @Override
//    public boolean existsByCodeAgent(String codeAgent) {
//        return repository.existsByCodeAgent(codeAgent);
//    }
//
//    private void removeAvatar(ENameCardEntity eNameCardEntity) {
//        try {
//            Files.delete(Paths.get(this.pathFolderImg + File.separator + eNameCardEntity.getAvatar()));
//        } catch (IOException e) {
//            throw HttpErrorException.badRequest("delete image error");
//        }
//    }
//}
