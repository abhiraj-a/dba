package com.dba.Controller;

import com.dba.ClerkClient;
import com.dba.DTO.Dto;
import com.dba.Entity.FileEntity;
import com.dba.Entity.User;
import com.dba.ExceptionHandler.UserNotFoundException;
import com.dba.Repository.FileEntityRepo;
import com.dba.Repository.FileTransferRepo;
import com.dba.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserRepo userRepo;
    private final ClerkClient client;
    private final FileTransferRepo fileTransferRepo;
    private final FileEntityRepo fileEntityRepo;

    @GetMapping("/get-all")
    public ResponseEntity<?> getall(){
        return ResponseEntity.ok(userRepo.findAllByIsDeletedFalse());
    }

    @PostMapping("/get/via-email")
    public ResponseEntity<?> getUser(@RequestBody Dto dto){
        User user = userRepo.findByEmail(dto.getString()).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update-credits/via-email/add/{credits}")
    public ResponseEntity<?> updateCredits(@RequestBody Dto dto,@PathVariable("credits")int credits){
        User user = userRepo.findByEmail(dto.getString()).orElseThrow(UserNotFoundException::new);
        int c = user.getCredits();
        user.setCredits(c+credits);
        userRepo.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/update-credits/via-email/deduct/{credits}")
    public ResponseEntity<?> updateCreditsd(@RequestBody Dto dto,@PathVariable("credits")int credits){
        User user = userRepo.findByEmail(dto.getString()).orElseThrow(UserNotFoundException::new);
        int c = user.getCredits();
        if(credits<c) {
            user.setCredits(c - credits);
            userRepo.saveAndFlush(user);
        }else {
            throw new RuntimeException();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete-user")
    @Transactional
    public ResponseEntity<?> delete(@RequestBody Dto dto) {
        User user = userRepo.findByEmailAndIsDeletedFalse(dto.getString()).orElseThrow(UserNotFoundException::new);
        String clerkId = user.getClerkId();

        int joinRows = fileTransferRepo.deleteJoinRowsByUser(user.getId());
//        System.out.println("Deleted join rows = " + joinRows);
        fileTransferRepo.flush();

        List<FileEntity> files = fileEntityRepo.findAllByOwnerId(user.getId());
//        for (FileEntity f : files) {
//            storageServiceImpl.delete(f.getStoragePath());
//        }
        fileEntityRepo.deleteFilesByUser(user.getId());
        fileEntityRepo.flush();

        user.setDeleted(true);
        userRepo.save(user);
        client.deleteUser(clerkId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-deleted")
    public ResponseEntity<?> findAllDeleted(){
        List<User> users = userRepo.findAllByIsDeletedTrue();
        return ResponseEntity.ok(users);
    }

}
