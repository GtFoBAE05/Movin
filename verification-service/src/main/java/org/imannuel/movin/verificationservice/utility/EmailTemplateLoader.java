package org.imannuel.movin.verificationservice.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class EmailTemplateLoader {
    public static String loadTemplate(String fileName) {
        log.info("Loading email template: {}", fileName);
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            String content = Files.readString(resource.getFile().toPath());
            log.info("Successfully loaded email template: {}", fileName);
            return content;
        } catch (IOException e) {
            log.error("Failed to load email template: {}", fileName, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry, currently unavailable");
        }
    }
}