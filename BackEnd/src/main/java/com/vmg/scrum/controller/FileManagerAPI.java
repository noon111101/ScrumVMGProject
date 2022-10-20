package com.vmg.scrum.controller;

import com.vmg.scrum.service.impl.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class FileManagerAPI {
    @Autowired
    FileManagerService fileManagerService;

    @GetMapping("files/{folder}")
    public byte[] download (@PathVariable("folder") String folder, @RequestParam("file") String file) {
        return fileManagerService.read(folder, file);
    }
}
