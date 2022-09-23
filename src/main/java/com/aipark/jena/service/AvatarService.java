package com.aipark.jena.service;

import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAvatar;
import org.springframework.http.ResponseEntity;

public interface AvatarService {
    ResponseEntity<Response.Body> avatarList();
    ResponseEntity<Response.Body> selectAvatar(Long avatarId);
    ResponseEntity<Response.Body> createAvatar(ResponseAvatar.ResponseCreateAvatar responseCreateAvatar);
}
