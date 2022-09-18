package com.aipark.jena.service;

import com.aipark.jena.dto.Response;
import org.springframework.http.ResponseEntity;

public interface AvatarService {
    ResponseEntity<Response.Body> avatarList();
    ResponseEntity<Response.Body> createAvatar(Long avatarId);
}
