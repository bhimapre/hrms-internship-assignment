package com.example.hrms_backend.utils;

import org.aspectj.apache.bcel.classfile.ClassParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class EmailTemplateUtil {
    public String loadTemplate(String templateName) {

        try {
            ClassPathResource resource =
                    new ClassPathResource("templates/emailTemplates/" + templateName);

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template");
        }
    }
}
