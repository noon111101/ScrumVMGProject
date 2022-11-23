package com.vmg.scrum.service.impl;

import com.vmg.scrum.exception.custom.FileNullException;
import com.vmg.scrum.payload.request.ImageLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileManagerService {
    @Autowired
    ServletContext app;
    @Value("${upload.path}")
    private String fileUpload;
    private Path getPath(String filename) {
        File dir = Paths.get(fileUpload).toFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }



    public void delete( String filename) {
        Path path = this.getPath(filename);
        path.toFile().delete();
    }

    public List<String> list(String folder) {
        List<String> filenames = new ArrayList<>();
        File dir = Paths.get(app.getRealPath("/files/"), folder).toFile();
        if(dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                filenames.add(file.getName());
            }
        }
        return filenames;
    }

    public String save(MultipartFile file) {
        try {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(filename);
                file.transferTo(path);
                return  filename;
            } catch (Exception e) {
                e.printStackTrace();
                return "default.png";
            }


    }
    public String saveLog(ImageLogRequest imageLogRequest) {
        try {
            String filePath = "C:\\Users\\ADMIN\\log\\"+ imageLogRequest.getCode() +"_" + imageLogRequest.getFullName() + "_" + LocalDate.now().toString();
            File dir = Paths.get(filePath).toFile();
            if(!dir.exists()) {
                dir.mkdirs();
            }
            String name = System.currentTimeMillis() + imageLogRequest.getFile().getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = Paths.get(dir.getAbsolutePath(), filename);
                imageLogRequest.getFile().transferTo(path);
                return  filename;
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }


    }
}
