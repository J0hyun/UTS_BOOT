package com.mbc.service;

import com.mbc.entity.MemberImg;
import com.mbc.repository.ItemRepository;
import com.mbc.repository.MemberImgRepository;
import com.mbc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberImgService {

    @Value("${itemImgLocation}")
    private String memberImgLocation;

    private final MemberImgRepository memberImgRepository;

    private final FileService fileService;

    public void saveMemberImg(MemberImg memberImg, MultipartFile profileImgFile) throws Exception {
        String oriImgName = profileImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(memberImgLocation, oriImgName, profileImgFile.getBytes());
            imgUrl = "/images/member/" + imgName;

            // 이미지 정보 업데이트 및 저장
            memberImg.updateMemberImg(oriImgName, imgName, imgUrl);
            memberImgRepository.save(memberImg);
        }
    }

}
