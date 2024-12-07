package sep490.g13.pms_be.service.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sep490.g13.pms_be.utils.Constant;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendSimpleMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            return Constant.EMAIL_SEND_SUCCESS;
        } catch (Exception e) {
            System.out.println("Reason: " + e.getMessage());
            return Constant.EMAIL_SEND_FAIL;
        }
    }

    public String sendHTMLMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            return Constant.EMAIL_SEND_SUCCESS;
        } catch (MessagingException e) {
            System.out.println("Reason: " + e.getMessage());
            return Constant.EMAIL_SEND_FAIL;
        }
    }

    public String sendMailWithAttachment(String to, String subject, String text, String attachment) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            //Setting mail attribute
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);

            //Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(attachment));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            //Sending the mail
            mailSender.send(mimeMessage);
            return Constant.EMAIL_SEND_SUCCESS;
        } catch (MessagingException e) {
            System.out.println("Reason: " + e.getMessage());
            return Constant.EMAIL_SEND_FAIL;
        }
    }
}
