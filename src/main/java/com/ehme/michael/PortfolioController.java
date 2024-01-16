package com.ehme.michael;

import com.ehme.michael.DTO.Resume;
import com.ehme.michael.components.EmailService;
import com.ehme.michael.records.SimpleEmail;
import com.ehme.michael.records.SimpleFile;
import com.ehme.michael.repositories.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PortfolioController {
    @Autowired
    private EmailService emailService;

    @Autowired
    ResumeRepository resumeRepository;
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    @RequestMapping(value="/emailService", method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> emailService(Model model, @ModelAttribute SimpleEmail simpleEmail) {
        try {
            System.out.println(model);
            emailService.sendSimpleMessage(simpleEmail);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin";
    }

    @RequestMapping(value="/uploadResume", method= RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> uploadResume(Model model, @ModelAttribute SimpleFile file) throws IOException {
        Resume resume = new Resume();
        resume.setContent(file.file().getBytes());
        resumeRepository.save(resume);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}