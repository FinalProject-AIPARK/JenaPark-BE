package com.aipark.jena.controller;

import com.aipark.jena.dto.Response;
import com.aipark.jena.service.AvatarService;
import com.aipark.jena.service.AvatarServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @GetMapping("/list")
    public ResponseEntity<Response.Body> avatarList(){
        return avatarService.avatarList();
    }

    @GetMapping("/{avatarId}")
    public ResponseEntity<Response.Body> createAvatar(@PathVariable Long avatarId){
        return avatarService.createAvatar(avatarId);
    }
}