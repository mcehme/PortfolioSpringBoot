package com.ehme.michael.records;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.web.multipart.MultipartFile;
public record SimpleFile(MultipartFile file) {
}
