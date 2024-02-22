package com.ehme.michael;

import com.ehme.michael.DTO.Resume;
import com.ehme.michael.components.EmailService;
import com.ehme.michael.config.WebSecurityConfig;
import com.ehme.michael.repositories.ResumeRepository;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest
@ContextConfiguration(classes={PortfolioController.class, WebSecurityConfig.class})
public class PortfolioControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ResumeRepository resumeRepository;

    @Mock
    private Resume resume;

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }
    @Test
    public void testEmailService() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/emailService")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("from", "FROM")
                        .param("subject", "SUBJECT")
                        .param("text", "TEXT"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void testAdminWithAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin"));
    }

    @Test
    public void testUploadResume() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "resume.pdf", MediaType.APPLICATION_PDF_VALUE, "resume".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.
                multipart("/uploadResume")
                .file(mockMultipartFile)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("content-type", MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void testUploadResumeWithAuth() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "resume.pdf", MediaType.APPLICATION_PDF_VALUE, "resume".getBytes());
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/uploadResume")
                        .file(mockMultipartFile)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("content-type", MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDownloadResume() throws  Exception {

        Mockito.when(resumeRepository.findFirstByOrderByIdDesc()).thenReturn(resume);
        Mockito.when(resume.getContent()).thenReturn("resume".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.get("/downloadResume/resume.pdf"))
                .andExpect(MockMvcResultMatchers.content().bytes("resume".getBytes()));
    }


}
