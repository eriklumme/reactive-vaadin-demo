package dev.lumme.reactivedemo.backend.controller;

import dev.lumme.reactivedemo.backend.service.FileService;
import dev.lumme.reactivedemo.common.client.FileClient;
import dev.lumme.reactivedemo.common.dto.FileDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(FileClient.GetFiles.PATH)
    public List<FileDTO> getFiles(
            @RequestParam(FileClient.GetFiles.OFFSET) int offset,
            @RequestParam(FileClient.GetFiles.LIMIT) int limit) {
        return fileService.getFiles(offset, limit);
    }

    @GetMapping(FileClient.CountFiles.PATH)
    public int countFiles() {
        return fileService.countFiles();
    }

}
