package com.aipark.jena.service;

import com.aipark.jena.domain.Avatar;
import com.aipark.jena.domain.AvatarRepository;
import com.aipark.jena.domain.avatarCategory.*;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAvatar;
import com.aipark.jena.dto.ResponseAvatarCategory.ResponseAccessories;
import com.aipark.jena.dto.ResponseAvatarCategory.ResponseAttitude;
import com.aipark.jena.dto.ResponseAvatarCategory.ResponseClothes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public ResponseEntity<Response.Body> selectAvatar(Long avatarId){

        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow();

        // list로 변경하기
        List<Accessories>  accessories =  accessoriesRepository.findAllByAvatar(avatar);
        List<Clothes> clothes = clothesRepository.findAllByAvatar(avatar);
        List<Attitude> attitude = attitudeRepository.findAllByAvatar(avatar);

        //악세사리 entity->dto
        List<ResponseAccessories> responseAccessoriesList= new ArrayList<ResponseAccessories>();

        for (int i = 0; i <accessories.size() ; i++) {
            ResponseAccessories responseAccessories = new ResponseAccessories(
                    accessories.get(i).getId(),
                    accessories.get(i).getAccessoryUrl()
            );
            responseAccessoriesList.add(responseAccessories);
        }

        //clothes entity -> dto
        List<ResponseClothes> responseClothesList= new ArrayList<ResponseClothes>();

        for (int i = 0; i <clothes.size() ; i++) {
            ResponseClothes responseClothes = new ResponseClothes(
                    clothes.get(i).getId(),
                    clothes.get(i).getClothesUrl()
            );
            responseClothesList.add(responseClothes);
        }
        //attitude entity -> dto
        List<ResponseAttitude> responseAttitudeList= new ArrayList<ResponseAttitude>();

        for (int i = 0; i <attitude.size() ; i++) {
            ResponseAttitude responseAttitude = new ResponseAttitude(
                    attitude.get(i).getId(),
                    attitude.get(i).getAttitudeUrl()
            );
            responseAttitudeList.add(responseAttitude);
        }


        // responseAvatar 에 값 주입
        ResponseAvatar responseAvatar = new ResponseAvatar(
                avatar.getName(),
                avatar.getThumbNail(),
                responseAccessoriesList,
                responseClothesList,
                responseAttitudeList);

        return response.success(responseAvatar,"아바타 선택 완료 ",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response.Body> createAvatar(Long avatarId, Long accessoryId, Long attitudeId, Long clothesId) {
        // 1-3-4-3
        return response.success("","",HttpStatus.OK);
    }

}
