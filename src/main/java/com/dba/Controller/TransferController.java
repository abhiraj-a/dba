package com.dba.Controller;

import com.dba.DTO.FileTransferDTO;
import com.dba.Entity.FileEntity;
import com.dba.Entity.FileTransfer;
import com.dba.Entity.User;
import com.dba.ExceptionHandler.TransferNotFoundException;
import com.dba.ExceptionHandler.UserNotFoundException;
import com.dba.Repository.FileTransferRepo;
import com.dba.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/transfer")
public class TransferController {

    private final UserRepo userRepo;
    private final FileTransferRepo fileTransferRepo;
    @GetMapping("/user/{transferId}")
    public ResponseEntity<?> findTranfer(@PathVariable("transferId") String t){
        FileTransfer transfer = fileTransferRepo.findByTransferId(t).orElseThrow(TransferNotFoundException::new);
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

    @GetMapping("/user/{emaail}")
    public ResponseEntity<?> findTranferViaEmail(@PathVariable("email") String t){
        User u = userRepo.findByEmail(t).orElseThrow(UserNotFoundException::new);
        FileTransfer transfer = fileTransferRepo.findAllByOwner(u).orElseThrow(TransferNotFoundException::new);
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

}
