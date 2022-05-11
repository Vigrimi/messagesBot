package com.vinnikov.inbox.ru.pandabot.serviceBot;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class TextGetContentTypeMultipart{
    public static String text;
    public Multipart part;

    public TextGetContentTypeMultipart() {
        text = "";
    }

    // если в тексте html то этот блок не нужен
    public String parseMultiparted(Multipart part) throws MessagingException, IOException{
        LOGGER.info("---TextGetContentTypeMultipart started-> " + LocalDateTime.now());
        for (int i = 0; i < 1; i++){
            //parsePart(part.getBodyPart(i));
            BodyPart part1 = part.getBodyPart(i);
            String type = part1.getContentType();

            if (type.contains("multipart/")){
                parseMultiparted((Multipart) part1.getContent());
                LOGGER.info("---TextGetContentTypeMultipart 42-> " + LocalDateTime.now() + "\n"
                        + part1.getContent().toString());
            } else if (type.contains("text/")){
                text = part1.getContent().toString().trim()
                        .replaceAll("\r"," ").replaceAll("\n"," ");
            } else if (Part.ATTACHMENT.equalsIgnoreCase(part1.getDisposition())){
                LOGGER.info("---TextGetContentTypeMultipart 54-> " + LocalDateTime.now() + "\n"
                        + MimeUtility.decodeText(part1.getFileName()));
            }
        }
        LOGGER.info("---TextGetContentTypeMultipart 58 text-> " + LocalDateTime.now() + "\n" + text);

        if(text.contains("Присвоен номер ДТ") || text.contains("ыпуск товаров разреше")){
            System.out.println("------TextGetContentTypeMultipart---AAAAAAAA:" + text);
        }
        return text;
    }
}
