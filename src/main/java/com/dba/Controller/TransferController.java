package com.dba.Controller;

import com.dba.DTO.FileTransferDTO;
import com.dba.DTO.Dto;
import com.dba.Entity.FileEntity;
import com.dba.Entity.FileTransfer;
import com.dba.Entity.User;
import com.dba.ExceptionHandler.TransferNotFoundException;
import com.dba.ExceptionHandler.UserNotFoundException;
import com.dba.Repository.FileTransferRepo;
import com.dba.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/transfer")
public class TransferController {

    private final UserRepo userRepo;
    private final FileTransferRepo fileTransferRepo;
    @GetMapping("/user/via-id")
    public ResponseEntity<?> findTranfer(@RequestBody Dto dto){
        FileTransfer transfer = fileTransferRepo.findByTransferId(dto.getString()).orElseThrow(TransferNotFoundException::new);
        List<FileEntity> fileEntities = transfer.getFiles();

        return ResponseEntity.ok(FileTransferDTO.builder()
                .verificationCode(transfer.getVerificationCode())
                .transferId(transfer.getId())
                .expiresAt(transfer.getExpiresAt())
                .fileMetaDataList(transfer.getFiles().stream().map(ft-> FileTransferDTO.FileMetaData.builder()
                        .id(ft.getId())
                        .fileSize(ft.getFileSize())
                        .originalFileName(ft.getOriginalFileName())
                        .contentType(ft.getContentType())
                        .build()).toList())
                .fileCount(transfer.getFiles().size())
                .build());
    }

    @GetMapping("/user/via-email}")
    public ResponseEntity<?> findTranferViaEmail(@RequestBody Dto dto){
        User u = userRepo.findByEmail(dto.getString()).orElseThrow(UserNotFoundException::new);
        List<FileTransfer> transfers = fileTransferRepo.findAllByOwner(u).orElseThrow(TransferNotFoundException::new);

        return ResponseEntity.ok(transfers.stream().map(transfer ->
                FileTransferDTO.builder()
                        .verificationCode(transfer.getVerificationCode())
                        .transferId(transfer.getId())
                        .expiresAt(transfer.getExpiresAt())
                        .fileCount(transfer.getFiles().size())
                        .fileMetaDataList(transfer.getFiles().stream().map(file ->
                                FileTransferDTO.FileMetaData.builder()
                                        .id(file.getId())
                                        .fileSize(file.getFileSize())
                                        .originalFileName(file.getOriginalFileName())
                                        .contentType(file.getContentType())
                                        .build()
                        ).toList())
                        .build()
        ).toList());
    }

}
