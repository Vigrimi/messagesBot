package com.vinnikov.inbox.ru.pandabot;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class TextGetContentTypeMultipart
{
    public static String text;
    public Multipart part;

    public TextGetContentTypeMultipart() {
        text = "";
    }

//    public String getText(Multipart part) throws MessagingException, IOException {
//        //parseMultiparted(/*text, */part);
//        return parseMultiparted(/*text, */part); // text;
//    }

      //  если в тексте html то этот блок не нужен
     public /*static*/ /*void*/ String parseMultiparted(/*String text, */Multipart part) throws MessagingException, IOException
    {
        LOGGER.info("---TextGetContentTypeMultipart started-> " + LocalDateTime.now());
        //String text = "";
        for (int i = 0; i < 1 /*part.getCount()*/; i++)
        {
            //parsePart(part.getBodyPart(i));
            BodyPart part1 = part.getBodyPart(i);
            String type = part1.getContentType();
            //String text = "";
            if (type.contains("multipart/"))
            {
                //System.out.println( parseMultiparted((Multipart)part.getContent()) );
                parseMultiparted((Multipart) part1.getContent());
                LOGGER.info("---TextGetContentTypeMultipart 42-> " + LocalDateTime.now() + "\n"
                        + part1.getContent().toString());
            }
            else if (type.contains("text/"))
            {
                text = part1.getContent().toString().trim()
                        .replaceAll("\r"," ").replaceAll("\n"," ");

                //String text1 = text.substring(0, 11);//endIndex) // взять часть букв из фразы
                //System.out.println("\n105-> " + text1);
            } else if (Part.ATTACHMENT.equalsIgnoreCase(part1.getDisposition()))
            {
                //System.out.println("");
                LOGGER.info("---TextGetContentTypeMultipart 54-> " + LocalDateTime.now() + "\n"
                        + MimeUtility.decodeText(part1.getFileName()));
            }
        }
        LOGGER.info("---TextGetContentTypeMultipart 58 text-> " + LocalDateTime.now() + "\n" + text);

        if(text.contains("Присвоен номер ДТ") ||
                text.contains("ыпуск товаров разреше"))
        {
            System.out.println("------TextGetContentTypeMultipart---AAAAAAAA:" + text);
        }

        return text;
    }

    /*static void *//*String*//* parsePart(BodyPart part) throws MessagingException, IOException
    {
        String type = part.getContentType();
        //String text = "";
        if (type.contains("multipart/"))
        {
            //System.out.println( parseMultiparted((Multipart)part.getContent()) );
            parseMultiparted((Multipart) part.getContent());
            System.out.println("96-> " + part.getContent().toString());
        }
        else if (type.contains("text/"))
        {
            String text = part.getContent().toString().trim();

            //String text1 = text.substring(0, 11);//endIndex) // взять часть букв из фразы
            //System.out.println("\n105-> " + text1);
        } else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
        {
            //System.out.println("");
            System.out.println("99-> " + MimeUtility.decodeText(part.getFileName()));
        }
        //return text;
    }*/

}
