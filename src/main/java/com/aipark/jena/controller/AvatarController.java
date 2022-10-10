package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestAvatar;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAvatar;
import com.aipark.jena.service.AvatarService;
import com.aipark.jena.service.AvatarServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @GetMapping
    public ResponseEntity<Response.Body> avatarList(){
        return avatarService.avatarList();
    }

    @GetMapping("/{avatarId}")
    public ResponseEntity<Response.Body> selectAvatar(@PathVariable Long avatarId){
        return avatarService.selectAvatar(avatarId);
    }

    @PostMapping("/createAvatar")
    public ResponseEntity<Response.Body> createAvatar(@RequestBody RequestAvatar.RequestCreateAvatar requestCreateAvatar){
        return avatarService.createAvatar(requestCreateAvatar);
    }
}
