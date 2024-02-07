package com.ehme.michael.DTO;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class ResumeTests {

    @Test
    public void testResumeNotNull(){
        Resume resume = new Resume();
        assertThat(resume).isNotNull();
    }

    @Test
    public void testResumeContentNull(){
        Resume resume = new Resume();
        assertThat(resume.getContent()).isNull();
    }
    @Test
    public void testResumeIdUndefined(){
        Resume resume = new Resume();
        assertThat(resume.getId()).isEqualTo(0L);
    }

    @Test
    public void testResumeContentNotNull(){
        Resume resume = new Resume();
        resume.setContent("Test Content".getBytes());
        assertThat(resume.getContent()).isNotNull();
        assertThat(resume.getContent()).isEqualTo("Test Content".getBytes());
        assertThat(resume.getContent()).isNotEqualTo("Test Content2".getBytes());
    }
    @Test
    public void testResumeIdDefined(){
        Resume resume = new Resume();
        resume.setId(1L);
        assertThat(resume.getId()).isEqualTo(1L);
        assertThat(resume.getId()).isNotEqualTo(2L);
    }

}
