package com.vsii.enamecard.services;
import com.vsii.enamecard.model.request.CreateENameCardRequest;
import com.vsii.enamecard.model.request.EditNameCardRequest;
import com.vsii.enamecard.model.response.ENameCardResponse;
import com.vsii.enamecard.model.response.SystemResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ENameCardService {

    SystemResponse<PageImpl<ENameCardResponse>> getList(int page, int size);

    SystemResponse<ENameCardResponse> create(CreateENameCardRequest createENameCardRequest);

    SystemResponse<ENameCardResponse> edit(EditNameCardRequest editNameCardRequest, int id);

    Resource importBatch(MultipartFile file) throws IOException;

    Resource exportFile() throws IOException;

    SystemResponse<ENameCardResponse> findById(int id);

    Resource exportQRCode(int id) throws Exception;

    SystemResponse uploadAvatar(MultipartFile file);

    SystemResponse updateAvatar(MultipartFile file);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    SystemResponse delete(int id);

    boolean existsByCodeAgent(String codeAgent);
}
