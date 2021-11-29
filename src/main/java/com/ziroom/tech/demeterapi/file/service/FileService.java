package com.ziroom.tech.demeterapi.file.service;

import com.ziroom.tech.demeterapi.file.model.FileModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件中心 此处需要替换为自己的文件中心
 * @author xuzyeu
 */
@Slf4j
@Component
public class FileService {

    @Value("${file.path}")
    private String filePath;

    /**
     * 上传文件 返回文件路径
     */
    public FileModel saveFile(MultipartFile attachment) throws IOException {
        byte[] bytes = attachment.getBytes();
        String fileId = "i-" + System.currentTimeMillis();
        String realPath = filePath + "/" + fileId;
        OutputStream os = null;

        try {
            File audioFile = new File(realPath);
            if (!audioFile.exists()){
                audioFile.createNewFile();
            }
            os = new FileOutputStream(audioFile);
            os.write(bytes);

            FileModel fileModel = new FileModel();
            fileModel.setFileName(fileId);
            fileModel.setUrl("http://127.0.0.1:8081/api/task/get/outcome?path=" + fileId);
            return fileModel;

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }finally {
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    public byte[] getFile(String fileId) throws Exception {
        InputStream in = null;
        String realPath = filePath + "/" + fileId;
        try {
            in = new FileInputStream(realPath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = in.read(buffer)) != -1){
                bos.write(buffer, 0, length);
            }
            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            throw new Exception();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

}
