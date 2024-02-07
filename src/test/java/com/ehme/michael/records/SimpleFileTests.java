package com.ehme.michael.records;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleFileTests {

    @Mock
    MultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testSimpleFile() throws IOException {
        Mockito.when(mockMultipartFile.getBytes()).thenReturn("Test Content".getBytes());
        SimpleFile simpleFile = new SimpleFile(mockMultipartFile);
        assertThat(simpleFile.file()).isNotNull();
        assertThat(simpleFile.file().getBytes()).isEqualTo("Test Content".getBytes());
    }
}
