package com.mbc.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    public String uploadFile(String uploadPath, String originalFilename,
                             byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFilename.substring(originalFilename
                            .lastIndexOf("."));

        //원래 파일명에서 마지막 점(.)의 위치를 찾습니다.
        //파일 확장자를 추출하기 위해 점(.) 이후의 부분을 잘라냅니다.

        log.info("파일확장자: " + extension);

        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
