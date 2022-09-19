package com.aipark.jena.service;

import com.aipark.jena.domain.Avatar;
import com.aipark.jena.domain.AvatarRepository;
import com.aipark.jena.domain.avatarCategory.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AvatarServiceImpl implements AvatarService{

    private final AvatarRepository avatarRepository;
    private final AccessoriesRepository accessoriesRepository;
    private final AttitudeRepository attitudeRepository;
    private final ClothesRepository clothesRepository;

    private final Response response;

    // 아바타 리스트
    public ResponseEntity<Response.Body> avatarList(){
         List<Avatar> avatarList = avatarRepository.findAll();
         return response.success(avatarList,"아바타리스트 입니다 ", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    //아바타
    public ResponseEntity<Response.Body> createAvatar(Long avatarId){

        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow();

        // list로 변경하기
        List<Accessories>  accessories =  accessoriesRepository.findAllByAvatarId(avatarId);
        List<Clothes> clothes = clothesRepository.findAllByAvatarId(avatarId);
        List<Attitude> attitude = attitudeRepository.findAllByAvatarId(avatarId);

        // responseAvatar 에 값 주입
        ResponseAvatar responseAvatar = new ResponseAvatar(
                avatar.getName(),
                avatar.getThumbNail(),
                accessories,
                clothes,
                attitude);

        return response.success(responseAvatar,"아바타 선택 완료 ",HttpStatus.OK);
    }

}
