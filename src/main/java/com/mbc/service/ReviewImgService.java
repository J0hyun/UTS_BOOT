package com.mbc.service;

import com.mbc.entity.ReviewImg;
import com.mbc.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImgService {

    @Value("C:/shop/review")
    private String reviewImgLocation;

    private final ReviewImgRepository reviewImgRepository;

    private final FileService fileService;

    public void saveReviewImg(ReviewImg reviewImg, MultipartFile reviewImgFile) throws Exception {
        String oriImgName = reviewImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(reviewImgLocation, oriImgName,
                    reviewImgFile.getBytes());
            imgUrl = "/images/review/" + imgName;

            //상품 이미지 정보 저장
            reviewImg.updateReviewImg(oriImgName, imgName, imgUrl);
            reviewImgRepository.save(reviewImg);
        }

    }
}
