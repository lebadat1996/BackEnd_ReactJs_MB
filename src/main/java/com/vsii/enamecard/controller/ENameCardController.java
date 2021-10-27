//package com.vsii.enamecard.controller;
//
//import com.vsii.enamecard.model.request.CreateENameCardRequest;
//import com.vsii.enamecard.model.request.EditNameCardRequest;
//import com.vsii.enamecard.model.response.ENameCardResponse;
//import com.vsii.enamecard.model.response.SystemResponse;
//import com.vsii.enamecard.services.ENameCardService;
//import org.springframework.core.io.Resource;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//import javax.validation.constraints.PositiveOrZero;
//import java.io.IOException;
//
//
//@RestController
//@RequestMapping("/e-name-card")
//@Validated
//@CrossOrigin(origins = "*")
//public class ENameCardController {
//
//    private ENameCardService service;
//
//    public ENameCardController(ENameCardService eNameCardService) {
//        this.service = eNameCardService;
//    }
//
//    @GetMapping()
//    ResponseEntity<SystemResponse<PageImpl<ENameCardResponse>>> getList(@PositiveOrZero @RequestParam(required = false, defaultValue = "1") int page,
//                                                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "20") int size) {
//        return ResponseEntity.ok(service.getList(page, size));
//    }
//
//    @PostMapping()
//    ResponseEntity<SystemResponse> create(@Valid @RequestBody CreateENameCardRequest createENameCardRequest) {
//        return ResponseEntity.ok(service.create(createENameCardRequest));
//    }
//
//    @PutMapping("/{id}")
//    ResponseEntity<SystemResponse> update(@Valid @RequestBody EditNameCardRequest editNameCardRequest, @PathVariable int id) {
//        return ResponseEntity.ok(service.edit(editNameCardRequest,id));
//    }
//
//    @DeleteMapping("/{id}")
//    ResponseEntity<SystemResponse> delete(@PathVariable int id) {
//        return ResponseEntity.ok(service.delete(id));
//    }
//
//
////    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    ResponseEntity<SystemResponse> importBatch(@RequestParam MultipartFile file) throws IOException {
////        return ResponseEntity.ok(service.importBatch(file));
////    }
//
//    @GetMapping(value = "/{id}")
//    ResponseEntity<SystemResponse<ENameCardResponse>> findById(@PathVariable int id) {
//        return ResponseEntity.ok(service.findById(id));
//    }
//
//
//    @GetMapping(value = "/excel")
//    ResponseEntity<Resource> exportFileExcel() throws IOException {
//
//        Resource resource = service.exportFile();
//
//        return ResponseEntity.ok()
//                .header("Content-disposition", "attachment;")
//                .contentLength(resource.contentLength())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
//    }
//
//    @GetMapping(value = "/qr-code/{id}")
//    ResponseEntity<Resource> exportFileExcel(@PathVariable int id) throws Exception {
//
//        Resource resource = service.exportQRCode(id);
//
//        return ResponseEntity.ok()
//                .header("Content-disposition", "attachment")
//                .contentLength(resource.contentLength())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
//    }
//
//    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    ResponseEntity<SystemResponse> uploadAvatar(@RequestParam MultipartFile file){
//        return ResponseEntity.ok(service.uploadAvatar(file));
//    }
//
//    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    ResponseEntity<SystemResponse> updateAvatar(@RequestParam MultipartFile file){
//        return ResponseEntity.ok(service.updateAvatar(file));
//    }
//
//}
