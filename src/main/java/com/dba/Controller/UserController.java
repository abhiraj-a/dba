package com.dba.Controller;

import com.dba.DTO.Dto;
import com.dba.Entity.User;
import com.dba.ExceptionHandler.UserNotFoundException;
import com.dba.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserRepo userRepo;

    @GetMapping("/get/via-email")
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
    @PostMapping("/update-credits/via-email/deduct//{credits}")
    public ResponseEntity<?> updateCreditsd(@RequestBody Dto dto,@PathVariable("credits")int credits){
        User user = userRepo.findByEmail(dto.getString()).orElseThrow(UserNotFoundException::new);
        int c = user.getCredits();
        if(credits<c) {
            user.setCredits(c + credits);
            userRepo.saveAndFlush(user);
        }else {
            throw new RuntimeException();
        }
        return ResponseEntity.ok(user);
    }




}
