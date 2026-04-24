package com.dba.Controller;

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

    @GetMapping("/get/{email}")
    public ResponseEntity<?> getUser(@PathVariable("email") String email){
        User user = userRepo.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update-credits/add/{email}/{credits}")
    public ResponseEntity<?> updateCredits(@PathVariable("email") String email,@PathVariable("credits")int credits){
        User user = userRepo.findByEmail(email).orElseThrow(UserNotFoundException::new);
        int c = user.getCredits();
        user.setCredits(c+credits);
        userRepo.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/update-credits/deduct/{email}/{credits}")
    public ResponseEntity<?> updateCreditsd(@PathVariable("email") String email,@PathVariable("credits")int credits){
        User user = userRepo.findByEmail(email).orElseThrow(UserNotFoundException::new);
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
