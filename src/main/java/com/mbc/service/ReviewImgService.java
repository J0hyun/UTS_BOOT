package com.mbc.service;

import com.mbc.entity.Review;
import com.mbc.entity.ReviewImg;
import com.mbc.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImgService {

    @Value("C:/shop/review")
    private String reviewImgLocation;

    private final ReviewImgRepository reviewImgRepository;

    private final FileService fileService;

    public void saveReviewImgs(List<ReviewImg> reviewImgs, List<MultipartFile> reviewImgFiles, Review review) throws Exception {
        for (int i = 0; i < reviewImgFiles.size(); i++) {
            MultipartFile reviewImgFile = reviewImgFiles.get(i);
            String oriImgName = reviewImgFile.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";

            // 파일 업로드
            if (!StringUtils.isEmpty(oriImgName)) {
                // 파일 이름을 고유하게 처리하여 중복을 방지
                imgName = fileService.uploadFile(reviewImgLocation, oriImgName, reviewImgFile.getBytes());
                imgUrl = "/images/review/" + imgName;  // 업로드된 이미지의 URL 설정

                // ReviewImg 객체 생성 및 업데이트
                ReviewImg reviewImg = new ReviewImg();
                reviewImg.setReview(review);  // Review와 연결
                reviewImg.updateReviewImg(oriImgName, imgName, imgUrl);

                // 중복 체크: 동일한 oriImgName과 review가 이미 DB에 존재하는지 확인
                if (!reviewImgRepository.existsByReviewAndOriImgName(review, oriImgName)) {
                    reviewImgs.add(reviewImg);  // 중복되지 않으면 리스트에 추가
                } else {
                    // 중복된 이미지가 발견되면 처리
                    System.out.println("중복된 이미지가 발견되었습니다: " + oriImgName);
                    // 중복된 이미지를 저장하지 않고 건너뜁니다.
                }
            }
        }

        // 중복되지 않은 이미지들만 DB에 저장
        if (!reviewImgs.isEmpty()) {
            reviewImgRepository.saveAll(reviewImgs);
        }
    }
}
