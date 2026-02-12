package com.example.hrms_backend.services;

import com.example.hrms_backend.utils.EmailTemplateUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateUtil templateUtil;

    // Send email when employee referral
    public void sendReferralEmail(String toEmail, String candidateName, String jobTitle, String referredBy) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Job Referral - " + jobTitle);

            String template = templateUtil.loadTemplate("job-referral-template.html");

            template = template.replace("{{candidateName}}", candidateName);
            template = template.replace("{{jobTitle}}", jobTitle);
            template = template.replace("{{referredBy}}", referredBy);

            helper.setText(template, true);

            mailSender.send(mimeMessage);

            log.info("Referral email sent to={}", toEmail);

        }
        catch (Exception e) {
            log.error("Email sending failed for={} : message={}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email");
        }
    }

    // Send email when employee share job
    public void sendJobShareEmail(String toEmail, String jobTitle, String location, Integer experience, String sharedBy) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Job Opportunity - " + jobTitle);

            String template = templateUtil.loadTemplate("job-share-template.html");

            template = template.replace("{{jobTitle}}", jobTitle);
            template = template.replace("{{location}}", location);
            template = template.replace("{{experience}}", String.valueOf(experience));
            template = template.replace("{{sharedBy}}", sharedBy);

            helper.setText(template, true);

            mailSender.send(mimeMessage);

            log.info("Job share email sent to={}", toEmail);
        }
        catch (Exception e) {
            log.error("Job share email failed for={} : message={}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email");
        }
    }

}
