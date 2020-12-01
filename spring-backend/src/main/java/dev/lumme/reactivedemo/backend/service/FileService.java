package dev.lumme.reactivedemo.backend.service;

import com.github.javafaker.Faker;
import dev.lumme.reactivedemo.common.dto.FileDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FileService {

    private static final Faker faker = new Faker();

    public List<FileDTO> getFiles(int offset, int limit) {
        offset = Math.min(offset, countFiles());
        limit = Math.min(limit, countFiles());

        return IntStream.range(offset, offset + limit)
                .mapToObj(i -> new FileDTO(faker.file().fileName(), "testfile.foo"))
                .collect(Collectors.toList());
    }

    public int countFiles() {
        return 10;
    }
}
