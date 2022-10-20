package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.impl.FileManagerService;
import com.vmg.scrum.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("api/file")
public class FileController {
    @Autowired
    FileManagerService fileManagerService;

    @Autowired
    UserRepository userRepository;
    @GetMapping("files/{folder}")
    public byte[] download (@PathVariable("folder") String folder, @RequestParam("file") String file) {
        return fileManagerService.read(folder, file);
    }

    @GetMapping("info")
    public Map<String,Double> download ( @RequestParam("code") Double code) {
        User user = userRepository.findByCode(code);
        Map<String,Double> listInfo = new HashMap<>();
        String pathFile = "http://localhost:8080/uploads/images/"+user.getCover();
        listInfo.put(pathFile,user.getCode());
        return listInfo;
    }
}
