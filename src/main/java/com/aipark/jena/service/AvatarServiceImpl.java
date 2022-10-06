package com.aipark.jena.service;

import com.aipark.jena.domain.Avatar;
import com.aipark.jena.domain.AvatarRepository;
import com.aipark.jena.domain.Project;
import com.aipark.jena.domain.ProjectRepository;
import com.aipark.jena.domain.avatarCategory.*;
import com.aipark.jena.dto.RequestAvatar;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseAvatar;
import com.aipark.jena.dto.ResponseAvatarCategory.ResponseAccessories;
import com.aipark.jena.dto.ResponseAvatarCategory.ResponseClothes;
import com.aipark.jena.dto.ResponseAvatarCategory.ResponseHat;
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
    private final HatRepository hatRepository;
    private final ClothesRepository clothesRepository;

    private final ProjectRepository projectRepository;
    private final Response response;

    // 아바타 리스트
    public ResponseEntity<Response.Body> avatarList(){

        List<Avatar> avatarList = avatarRepository.findAll();

        List<ResponseAvatar.ResponseAvatarList> responseAvatarList = new ArrayList<ResponseAvatar.ResponseAvatarList>();

        //entity -> dto
        for (int i = 0; i<avatarList.size() ; i++) {
            ResponseAvatar.ResponseAvatarList responseAvatar = new ResponseAvatar.ResponseAvatarList(
            avatarList.get(i).getId(),
            avatarList.get(i).getName(),
            avatarList.get(i).getThumbNail()
            );
            responseAvatarList.add(responseAvatar);
        }

        return response.success(responseAvatarList,"아바타리스트 입니다 ", HttpStatus.OK);
    }

    //아바타 선택시 해당 아바타가 가지고있는 아이템 리턴
    @Transactional(readOnly = true)
    public ResponseEntity<Response.Body> selectAvatar(Long avatarId){

        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow();

        // list로 변경하기
        List<Accessories>  accessories =  accessoriesRepository.findAllByAvatar(avatar);
        List<Clothes> clothes = clothesRepository.findAllByAvatar(avatar);
        List<Hat> hats = hatRepository.findAllByAvatar(avatar);

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
        //hats entity -> dto
        List<ResponseHat> responseHatList= new ArrayList<ResponseHat>();

        for (int i = 0; i <hats.size() ; i++) {
            ResponseHat responseHat = new ResponseHat(
                    hats.get(i).getId(),
                    hats.get(i).getHatUrl()
            );
            responseHatList.add(responseHat);
        }

        // responseAvatar 에 값 주입
        ResponseAvatar responseAvatar = new ResponseAvatar(
                avatar.getName(),
                avatar.getThumbNail(),
                responseAccessoriesList,
                responseClothesList,
                responseHatList);

        return response.success(responseAvatar,"해당 아바타에서 선택 가능한 옵션입니다. ",HttpStatus.OK);
    }

    // 아바타 생성
    @Transactional
    @Override
    public ResponseEntity<Response.Body> createAvatar(RequestAvatar.RequestCreateAvatar requestCreateAvatar) {

        Avatar avatar = avatarRepository.findById(requestCreateAvatar.getAvatarId()).orElseThrow();
        Project project = projectRepository.findById(requestCreateAvatar.getProjectId()).orElseThrow();

        // 해당 아바타의 옳은 url 을 찾기위한 로직
        Long accessoriesNum = requestCreateAvatar.getAccessoryId();
        Long clothesNum = requestCreateAvatar.getClothesId();
        Long hatNum = requestCreateAvatar.getHatId();
        final Long CHECK_NUM = 3*(avatar.getId()-1);
        if(avatar.getId() != 1){
            accessoriesNum -= CHECK_NUM;
            clothesNum -= CHECK_NUM;
            hatNum -= CHECK_NUM;
        }

        if(!accessoriesRepository.existsByIdAndAvatar(requestCreateAvatar.getAccessoryId(),avatar)){
            return response.fail("해당 악세서리는 "+avatar.getName()+"이(가) 사용할 수 없습니다.",HttpStatus.BAD_REQUEST);
        }

        if(!hatRepository.existsByIdAndAvatar(requestCreateAvatar.getHatId(),avatar)){
            return response.fail("해당 모자는 "+avatar.getName()+"이(가) 사용할 수 없습니다.",HttpStatus.BAD_REQUEST);
        }

        if(!clothesRepository.existsByIdAndAvatar(requestCreateAvatar.getClothesId(),avatar)){
            return response.fail("해당 옷은 "+avatar.getName()+"이(가) 사용할 수 없습니다.",HttpStatus.BAD_REQUEST);
        }
        String resultUrl = "https://jenapark.s3.ap-northeast-2.amazonaws.com/avatar/"+avatar.getName()+"/"+avatar.getName()+"-"+accessoriesNum+"-"+clothesNum+"-"+hatNum+".png";

        project.updateAvatarUrl(resultUrl);
        projectRepository.save(project);

        return response.success(resultUrl,"아바타 생성 완료",HttpStatus.OK);
    }

}
