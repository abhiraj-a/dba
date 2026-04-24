package com.dba.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FileTransferDTO {

    private String verificationCode;

    private UUID transferId;

    private int fileCount;

    private boolean isRevoked;

    private List<FileMetaData> fileMetaDataList;

    private Instant expiresAt;

    @Data
    @Builder
    public static class FileMetaData{
        private UUID id;
        private String originalFileName;
        private long fileSize;
        private String contentType;

    }



}
