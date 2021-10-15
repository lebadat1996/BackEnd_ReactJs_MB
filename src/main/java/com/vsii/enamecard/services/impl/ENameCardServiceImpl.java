package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.exceptions.HttpErrorException;
import com.vsii.enamecard.jwt.model.CustomUserDetails;
import com.vsii.enamecard.model.dto.AccountDTO;
import com.vsii.enamecard.model.entities.AccountEntity;
import com.vsii.enamecard.model.entities.ENameCardEntity;
import com.vsii.enamecard.model.request.CreateENameCardRequest;
import com.vsii.enamecard.model.request.EditNameCardRequest;
import com.vsii.enamecard.model.response.ENameCardResponse;
import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.repositories.ENameCardRepository;
import com.vsii.enamecard.services.AccountService;
import com.vsii.enamecard.services.ENameCardService;
import com.vsii.enamecard.services.MailService;
import com.vsii.enamecard.utils.Constant;
import com.vsii.enamecard.utils.HttpCodeResponse;
import com.vsii.enamecard.utils.QRCodeUtils;
import com.vsii.enamecard.utils.StringResponse;
import com.vsii.enamecard.utils.filters.SearchCriteria;
import com.vsii.enamecard.utils.filters.SearchOperator;
import com.vsii.enamecard.utils.filters.SpecificationBuilder;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ENameCardServiceImpl implements ENameCardService {

    private final ENameCardRepository repository;
    private final String baseQRCode;
    private final String baseZaloLink;
    private final String pathTemplate;
    private final MailService mailService;
    private final String pathFolderImg;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Autowired
    public ENameCardServiceImpl(ENameCardRepository eNameCardRepository,
                                @Value("${base.qr.code}") String baseQRCode,
                                @Value("${base.zalo.link}") String baseZaloLink,
                                @Value("${path.template}") String pathTemplate,
                                MailService mailService,
                                @Value("${path.folder.img}") String pathFolderImg,
                                AccountService accountService,
                                ModelMapper modelMapper) {
        repository = eNameCardRepository;
        this.baseQRCode = baseQRCode;
        this.baseZaloLink = baseZaloLink;
        this.pathTemplate = pathTemplate;
        this.mailService = mailService;
        this.pathFolderImg = pathFolderImg;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }


    @Override
    public SystemResponse<PageImpl<ENameCardResponse>> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("status");
        searchCriteria.setOperation(SearchOperator.EQUAL);
        searchCriteria.setValue(ENameCardEntity.Status.ACTIVE);

        SpecificationBuilder<ENameCardEntity> specificationBuilder = new SpecificationBuilder();
        specificationBuilder.add(searchCriteria);

        Page eNameCardPage = repository.findAll(specificationBuilder,pageable);
        List<ENameCardResponse> eNameCardResponses = (List<ENameCardResponse>) eNameCardPage.getContent().stream().map(eNameCard -> modelMapper.map(eNameCard,ENameCardResponse.class)).collect(Collectors.toList());

        return new SystemResponse<>(HttpCodeResponse.SUCCESS, StringResponse.OK, new PageImpl<>(eNameCardResponses, pageable, eNameCardPage.getTotalElements()));
    }

    @Override
    public SystemResponse<ENameCardResponse> create(CreateENameCardRequest createENameCardRequest) {

        ENameCardEntity eNameCardEntity = modelMapper.map(createENameCardRequest,ENameCardEntity.class);

        eNameCardEntity.setZaloLink(this.baseZaloLink + eNameCardEntity.getPhone());
        eNameCardEntity.setStatus(ENameCardEntity.Status.ACTIVE);

        eNameCardEntity = repository.save(eNameCardEntity);
        AccountDTO accountDTO = accountService.createAccountDefault(eNameCardEntity);
        mailService.sendMailNotifyAccount(accountDTO);
        return new SystemResponse<>(HttpCodeResponse.SUCCESS, StringResponse.OK);
    }

    @Override
    public SystemResponse<ENameCardResponse> edit(EditNameCardRequest editNameCardRequest, int id) {

        ENameCardEntity eNameCardEntity = repository.getById(id);
        eNameCardEntity.setFullName(editNameCardRequest.getFullName());
        AccountEntity accountEntity = accountService.findUserByUsername(eNameCardEntity.getPhone());
        eNameCardEntity.setPhone(editNameCardRequest.getPhone());
        eNameCardEntity.setEmail(editNameCardRequest.getEmail());
        eNameCardEntity.setPositions(editNameCardRequest.getPositions());
        eNameCardEntity.setFacebookLink(editNameCardRequest.getFacebookLink());

        eNameCardEntity = repository.save(eNameCardEntity);

        accountEntity.setEmail(eNameCardEntity.getEmail());
        accountEntity.setUsername(eNameCardEntity.getPhone());
        accountService.save(accountEntity);

        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setStatus(HttpCodeResponse.SUCCESS);
        systemResponse.setError("update successful");
        return systemResponse;
    }

    @Override
    public SystemResponse<List<ENameCardResponse>> importBatch(MultipartFile file) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        List<ENameCardEntity> eNameCardEntities = new ArrayList<>();

        for (int i = 11; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            ENameCardEntity eNameCardEntity = getENameCardEntityFromRow(row);
            eNameCardEntities.add(eNameCardEntity);
        }

        eNameCardEntities = repository.saveAll(eNameCardEntities);

        eNameCardEntities.forEach(eNameCard -> {
           AccountDTO accountDTO = accountService.createAccountDefault(eNameCard);
           mailService.sendMailNotifyAccount(accountDTO);
        });

        return new SystemResponse<>(HttpCodeResponse.SUCCESS, StringResponse.OK);
    }

    private ENameCardEntity getENameCardEntityFromRow( Row row) {
        DataFormatter formatter = new DataFormatter();
        ENameCardEntity eNameCardEntity = new ENameCardEntity();
        String fullName = formatter.formatCellValue(row.getCell(0));
        if (Pattern.matches(Constant.PATTERN_FULL_NAME,fullName)){
            throw HttpErrorException.badRequest("this full name in row "  + row.getRowNum() + " is invalid format");
        }
        eNameCardEntity.setFullName(fullName);
        String phone = formatter.formatCellValue(row.getCell(2));

        if (!Pattern.matches(Constant.PATTERN_PHONE,phone)){
            throw HttpErrorException.badRequest("this phone in row "  + row.getRowNum() + " is invalid format");
        }
        if (this.existsByPhone(phone)){
            throw HttpErrorException.badRequest("this phone in row "  + row.getRowNum() + " is existed");
        }
        eNameCardEntity.setPhone(formatter.formatCellValue(row.getCell(2)));

        String email = formatter.formatCellValue(row.getCell(3));
        if (!Pattern.matches(Constant.PATTERN_EMAIL,email)){
            throw HttpErrorException.badRequest("this email in row "  + row.getRowNum() + " is invalid format");
        }
        if (this.existsByEmail(email)){
            throw HttpErrorException.badRequest("this email in row "  + row.getRowNum() + " is existed");
        }
        eNameCardEntity.setEmail(formatter.formatCellValue(row.getCell(3)));
        eNameCardEntity.setFacebookLink(formatter.formatCellValue(row.getCell(4)));
        eNameCardEntity.setPositions(formatter.formatCellValue(row.getCell(1)));
        eNameCardEntity.setDateCreate(OffsetDateTime.now());
        eNameCardEntity.setDateModify(OffsetDateTime.now());
        eNameCardEntity.setZaloLink(this.baseZaloLink + eNameCardEntity.getPhone());
        return eNameCardEntity;
    }

    @Override
    public Resource exportFile() throws IOException {

        byte[] fileArr = Files.readAllBytes(Paths.get(this.pathTemplate));

        ByteArrayResource byteArrayResource = new ByteArrayResource(fileArr);

        return byteArrayResource;
    }

    @Override
    public SystemResponse<ENameCardResponse> findById(int id) {

        ENameCardEntity eNameCardEntity;
        eNameCardEntity = repository.findById(id).orElseThrow(() -> HttpErrorException.badRequest(StringResponse.E_NAME_CARD_NOT_EXISTED));

        ENameCardResponse eNameCardResponse = modelMapper.map(eNameCardEntity,ENameCardResponse.class);

        return new SystemResponse<>().setData(eNameCardResponse).setError(StringResponse.OK).setStatus(HttpCodeResponse.SUCCESS);
    }


    public Resource exportQRCode(int id) throws Exception {
        String data = this.baseQRCode + id;
        BufferedImage imageQR = QRCodeUtils.generateEAN13BarcodeImage(data, 300, 300);
        return QRCodeUtils.toByteArrayAutoClosable(imageQR, "png");
    }

    @Override
    public SystemResponse uploadAvatar(MultipartFile file) {

        File newFile = saveFile(file);

        return new SystemResponse().setData(newFile.getName()).setStatus(HttpCodeResponse.SUCCESS).setError(StringResponse.OK);
    }

    private File saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File folder = new File(this.pathFolderImg);
        File newFile = null;
        try {
            if (!folder.exists()){
                folder.mkdir();
            }

            newFile = new File(folder.getAbsolutePath() + File.separator + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newFile));
            stream.write(file.getBytes());
            stream.close();

        } catch (IOException e) {
        }
        return newFile;
    }

    @Override
    public SystemResponse updateAvatar(MultipartFile file) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ENameCardEntity eNameCardEntity = repository.findById(customUserDetails.getAccountEntity().getENameCardId()).orElseThrow(() -> HttpErrorException.badRequest("e name card not exists"));
        if (eNameCardEntity.getAvatar() != null){
            removeAvatar(eNameCardEntity);
        }
        File newFile = saveFile(file);
        eNameCardEntity.setAvatar(newFile.getName());
        repository.save(eNameCardEntity);
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setStatus(HttpCodeResponse.SUCCESS);
        systemResponse.setError("update avatar successful");
        return systemResponse;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return repository.existsByPhone(phone);
    }

    @Override
    public SystemResponse delete(int id) {
        ENameCardEntity eNameCardEntity = repository.getById(id);
        eNameCardEntity.setStatus(ENameCardEntity.Status.INACTIVE);
        accountService.updateStatus(eNameCardEntity.getPhone(), AccountEntity.Status.INACTIVE);
        repository.save(eNameCardEntity);
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setStatus(HttpCodeResponse.SUCCESS);
        systemResponse.setError("delete successful");
        return systemResponse;
    }

    private void removeAvatar(ENameCardEntity eNameCardEntity) {
        try {
            Files.delete(Paths.get(this.pathFolderImg + File.separator + eNameCardEntity.getAvatar()));
        } catch (IOException e) {
            throw HttpErrorException.badRequest("delete image error");
        }
    }
}
