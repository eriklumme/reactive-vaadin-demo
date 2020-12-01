package dev.lumme.reactivedemo.common.client;

import dev.lumme.reactivedemo.common.dto.FileDTO;

import java.util.List;

public interface FileClient {

    interface GetFiles {
        String PATH = "/files";
        String OFFSET = "offset";
        String LIMIT = "limit";
    }

    interface CountFiles {
        String PATH = "/count";
    }

    List<FileDTO> getFiles(int offset, int limit);

    Integer countFiles();
}
