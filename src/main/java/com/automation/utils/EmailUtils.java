package com.automation.utils;

import com.automation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * Email Utilities - Provides methods for sending test reports via email.
 */
public class EmailUtils {
    
    private static final Logger logger = LogManager.getLogger(EmailUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private EmailUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Send email with test report attachment
     */
    public static void sendReportEmail(String reportPath, String subject, String body) {
        String host = config.getProperty("email.host", "smtp.gmail.com");
        String port = config.getProperty("email.port", "587");
        String username = config.getProperty("email.username");
        String password = config.getProperty("email.password");
        String from = config.getProperty("email.from");
        String to = config.getProperty("email.to");
        
        if (username == null || password == null || from == null || to == null) {
            logger.warn("Email configuration incomplete. Skipping email notification.");
            return;
        }
        
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host);
        
        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            
            // Create multipart message
            Multipart multipart = new MimeMultipart();
            
            // Add body text
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);
            
            // Add attachment if report exists
            if (reportPath != null && new File(reportPath).exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(reportPath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(new File(reportPath).getName());
                multipart.addBodyPart(attachmentPart);
            }
            
            message.setContent(multipart);
            
            // Send message
            Transport.send(message);
            logger.info("Email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    /**
     * Send email with multiple attachments
     */
    public static void sendReportEmail(String[] reportPaths, String subject, String body) {
        String host = config.getProperty("email.host", "smtp.gmail.com");
        String port = config.getProperty("email.port", "587");
        String username = config.getProperty("email.username");
        String password = config.getProperty("email.password");
        String from = config.getProperty("email.from");
        String to = config.getProperty("email.to");
        
        if (username == null || password == null || from == null || to == null) {
            logger.warn("Email configuration incomplete. Skipping email notification.");
            return;
        }
        
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host);
        
        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            
            // Create multipart message
            Multipart multipart = new MimeMultipart();
            
            // Add body text
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);
            
            // Add attachments
            for (String reportPath : reportPaths) {
                if (reportPath != null && new File(reportPath).exists()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(reportPath);
                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName(new File(reportPath).getName());
                    multipart.addBodyPart(attachmentPart);
                }
            }
            
            message.setContent(multipart);
            
            // Send message
            Transport.send(message);
            logger.info("Email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    /**
     * Send simple email without attachment
     */
    public static void sendEmail(String subject, String body) {
        sendReportEmail((String) null, subject, body);
    }
    
    /**
     * Send test completion notification
     */
    public static void sendTestCompletionEmail(int passed, int failed, int skipped, String reportPath) {
        String subject = String.format("Test Execution Report - Passed: %d, Failed: %d, Skipped: %d", 
            passed, failed, skipped);
        
        StringBuilder body = new StringBuilder();
        body.append("Test Execution Summary\n");
        body.append("======================\n\n");
        body.append(String.format("Total Tests: %d\n", passed + failed + skipped));
        body.append(String.format("Passed: %d\n", passed));
        body.append(String.format("Failed: %d\n", failed));
        body.append(String.format("Skipped: %d\n", skipped));
        body.append(String.format("Pass Rate: %.2f%%\n\n", 
            (passed * 100.0) / (passed + failed + skipped)));
        body.append("Please find the detailed report attached.\n");
        
        sendReportEmail(reportPath, subject, body.toString());
    }
}
