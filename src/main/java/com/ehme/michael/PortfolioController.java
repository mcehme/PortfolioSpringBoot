package com.ehme.michael;

import com.ehme.michael.DTO.Resume;
import com.ehme.michael.components.CaptchaService;
import com.ehme.michael.components.EmailService;
import com.ehme.michael.records.ReCaptchaToken;
import com.ehme.michael.records.SimpleEmail;
import com.ehme.michael.records.SimpleFile;
import com.ehme.michael.repositories.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
public class PortfolioController {
    @Autowired
    EmailService emailService;

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    CaptchaService captchaService;

    Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin";
    }

    @PostMapping(value="/emailService")
    @ResponseBody
    public void email(Model model, @ModelAttribute SimpleEmail simpleEmail) {
        try {
            emailService.sendSimpleMessage(simpleEmail);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid inputs");
        }
    }

    @PostMapping(value="/uploadResume", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public void uploadResume(Model model, @ModelAttribute SimpleFile file, @ModelAttribute ReCaptchaToken reCaptchaToken) throws IOException {
        try {
            captchaService.validate(reCaptchaToken);
        } catch (Exception e) {
            return;
        }
        if(file.file().getBytes().length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty");
        }
        Resume resume = new Resume();
        resume.setContent(file.file().getBytes());
        resumeRepository.deleteAll();
        resumeRepository.save(resume);
    }

    @PostMapping (value="/downloadResume/resume.pdf", produces=MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public Resource downloadResume(Model model, @ModelAttribute ReCaptchaToken reCaptchaToken) {
        try {
            captchaService.validate(reCaptchaToken);
            return new ByteArrayResource(resumeRepository.findFirstByOrderByIdDesc().getContent());

        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }
}