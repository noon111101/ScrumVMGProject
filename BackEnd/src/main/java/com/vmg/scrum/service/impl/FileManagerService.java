package com.vmg.scrum.service.impl;

import com.vmg.scrum.exception.custom.FileNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileManagerService {
    @Autowired
    ServletContext app;
    private Path getPath(String folder, String filename) {
        File dir = Paths.get("src\\main\\resources\\static\\uploads", folder).toFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }

    public byte[] read(String folder, String filename) {
        Path path = this.getPath(folder, filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String folder, String filename) {
        Path path = this.getPath(folder, filename);
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
// nhiều ảnh
    public List<String> save(String folder, MultipartFile[] files) {
        List<String> filenames = new ArrayList<>();
        if(Arrays.stream(files).toList().size() <=1) {
            if (Arrays.stream(files).toList().get(0).getSize() <=0) {
                throw new FileNullException(400, "File can't not null","files", "blogForm");
            }
        }
        for( MultipartFile file: files) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(folder, filename);
            try {
                file.transferTo(path);
                filenames.add(filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filenames;
    }
// 1 ảnh
    public String save(String folder, MultipartFile file) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(folder, filename);
            try {
                file.transferTo(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return filename;
    }
}
