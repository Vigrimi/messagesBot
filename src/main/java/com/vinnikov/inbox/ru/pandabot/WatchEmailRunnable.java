package com.vinnikov.inbox.ru.pandabot;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Properties;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class WatchEmailRunnable implements Runnable//, AutoCloseable
{
    public int countOld;
    private String textFmEmail;
    public String[] arrSubjectFmEmail;
    public String[] arrTextsFmEmail;
    private FileWriteInOldCountEmails fileWriteInOldCountEmails;

    public WatchEmailRunnable(int countOld) {
        this.countOld = countOld;
        textFmEmail = "";
        arrSubjectFmEmail = new String[300];
        arrTextsFmEmail = new String[700];
        fileWriteInOldCountEmails = new FileWriteInOldCountEmails (new File("countOld.txt"));
///////
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
    }

    @Override
    public void run()
    {
        String textFmEmail = "";
        LOGGER.info("---поехали WatchEmailRunnable-> " + LocalDateTime.now());
        //Объект properties содержит параметры соединения
        Properties properties = new Properties();
        //Так как для чтения Yandex требует SSL-соединения - нужно использовать фабрику SSL-сокетов
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //Создаем соединение для чтения почтовых сообщений
        Session session = Session.getDefaultInstance(properties);
        //Это хранилище почтовых сообщений. По сути - это и есть почтовый ящик=)
        Store store = null;

        try
        {
            //Это папка, которую будем читать
            Folder inbox = null;
            //Для чтения почтовых сообщений используем протокол IMAP.
            //Почему? Так Yandex сказал: https://yandex.ru/support/mail/mail-clients.html
            //см. раздел "Входящая почта"
            store = session.getStore("imap");
            try
            {
                LOGGER.info("---Подключаемся к почтовому ящику-> " + LocalDateTime.now());
                //Подключаемся к почтовому ящику
                store.connect("imap.yandex.ru", 993, "vru", "t5");
                LOGGER.info("---Подключились к почтовому ящику-> " + LocalDateTime.now());

                LOGGER.info("---Читаем папку Входящие сообщения-> " + LocalDateTime.now());
                //Читаем папку "Входящие сообщения"
                inbox = store.getFolder("INBOX");
                //Будем только читать сообщение, не меняя их
                inbox.open(Folder.READ_ONLY);

                //Получаем количество сообщения в папке
                int countNew = inbox.getMessageCount();
                LOGGER.info("---Получили количество сообщений во входящих-> " + LocalDateTime.now());
                if(countNew == countOld)
                {
                    LOGGER.info("---НЕТ НОВЫХ ПИСЕМ _ -> " + LocalDateTime.now());
                } else if(countNew > countOld)
                {
                    LOGGER.info("---countOld = QuantityEmails.getCountOldQuantityEmails()-> " + LocalDateTime.now()
                            + "\n" + countOld + "old ** new" + countNew);

                    //Вытаскиваем сообщения в диапазоне
                    Message[] messages = inbox.getMessages(countOld+1, countNew);

                    fileWriteInOldCountEmails.writeToFileString(countNew);

                    int index = 0;
                    //Циклом пробегаемся по всем сообщениям
                    for (Message message : messages)
                    {
                        if(message.getSubject().contains("ДТ подана. Регистрационный номер заявления о выпуске товаров до подачи"))
                        { // ЗВ, пока не обрабатывается, просто увеличить кол-во присвоенных ДТ
                            CalculateQtyGTDPerMonth.getCalculateQtyGTDPerMonth();
                        } else
                        if( (message.getSubject().contains("ДТ зарегистрирована. Регистрационный номер ДТ")) ||
                                (message.getSubject().contains("Выпуск товаров разрешен. Решение по товарам")) ||
                                (message.getSubject().contains("Запрос на предоставление оригинала")) ||
                                (message.getSubject().contains("Уведомление о способе предоставления оригинала документа")) ||
                                (message.getSubject().contains("Результат сканирования оригинала")) ||
                                (message.getSubject().contains("предусмотренными статьей 121 Таможенного кодекса Евразийского эконом. Решение по товарам")) ||
                                message.getSubject().contains("предусмотренными статьей 122 Таможенного кодекса Евразийского эконом. Заключение") ||
                                message.getSubject().contains("Выпуск товаров с особенностями, предусмотренными статьей 122 Таможенного кодекса Евразийского эконом. Решение по тов") ||
                                message.getSubject().contains("Досмотр товара. Уведомление о принятии решения по проведению фактического контроля") ||
                                message.getSubject().contains("Результаты фактического контроля") ||
                                message.getSubject().contains("ТД зарегистрирована. Регистрация ЭТД") ||
                                message.getSubject().contains("Выпуск товара. Выпуск товар") ||
                                message.getSubject().contains("Подтверждение о прибытии. Подтверждение о прибытии") ||
                                message.getSubject().contains("Транзит завершен. Завершение таможенного транзит") ||
                                message.getSubject().contains("Отказано в выпуске товаров. Решение по товарам ДТ") ||
                                message.getSubject().contains("Запрос документов и сведений. Уведомление о проведении таможенной экспертизы") ||
                                message.getSubject().contains("Отказано в выпуске товаров на осн. пп") ||
                                message.getSubject().contains("Выпуск при условии обеспечения уплаты таможенных платежей. Решение по товарам")
                        )
                        {
                            //От кого
                            String from = ((InternetAddress) message.getFrom()[0]).getAddress();
                            //System.out.println("\nFROM: " + from);

                            //Тема письма
                            String TEXTgetSubject = message.getSubject();
                            LOGGER.info("---WatchEmailRunnable TEXTgetSubject-> " + LocalDateTime.now() + "\n"
                                    + TEXTgetSubject);
                            arrSubjectFmEmail[index] = TEXTgetSubject;
                            // тело письма
                            String TEXTgetContent = message.getContent().toString();
                            arrTextsFmEmail[index] = TEXTgetContent;
                            LOGGER.info("---WatchEmailRunnable TEXTgetContent-> " + LocalDateTime.now() + "\n"
                                    + TEXTgetContent); // да, для панды так как хтмл и показывает, а простой текст нет

                            //System.out.println("TEXTgetDisposition: " + message.getDisposition()); // Нет
                            //System.out.println("TEXTgetFlags: " + message.getFlags()); // Нет
                            //System.out.println("TEXTgetContent.toString: " + message.getContent().toString()); // Нет
                            String TEXTgetContentType = message.getContentType();
                            LOGGER.info("---WatchEmailRunnable TEXTgetContentType-> " + LocalDateTime.now() + "\n"
                                    + TEXTgetContentType); // нет
                            if(TEXTgetContentType.contains("multipart"))
                            {
                                LOGGER.info("---WatchEmailRunnable if(TEXTgetContentType.contains(\"multipart\")-> "
                                        + LocalDateTime.now());
                                //parseMultiparted(part);
                                TextGetContentTypeMultipart textGetContentTypeMultipart = new TextGetContentTypeMultipart();
                                Multipart part = (Multipart) message.getContent();
                                arrTextsFmEmail[index] = textGetContentTypeMultipart.parseMultiparted(part);
                                LOGGER.info("---WatchEmailRunnable if(TEXTgetContentType.contains(\"multipart\") 222 -> "
                                        + LocalDateTime.now() + "\n" + arrTextsFmEmail[index]);
                            }

                            //System.out.println("TEXTgetDescription: " + message.getDescription()); //TEXTgetDescription: null
                            //System.out.println("TEXTgetFolder: " + message.getFolder()); // это название папки где лежит письмо

                            //Multipart part = (Multipart) (new MimeMessage(session)).getContent();
                            //           Multipart part = (Multipart) message.getContent();
                            //           Multipart mp = (Multipart) message.getContent();
                            //if(TEXTgetContentType.contains("multipart")) parseMultiparted(part);

                    /*    // ***** похоже эта часть для работы с файлом - начало
                        else if(TEXTgetContentType.contains("text/"))
                        {
                            //parseMultiparted(part);
                            for (int i = 0; i < mp.getCount(); i++)
                            {
                                BodyPart bp = mp.getBodyPart(i);
                                if (bp.getFileName() == null)
                                    System.out.println("    " + i + ". сообщение : '" +
                                            bp.getContent() + "'");
                                else
                                    System.out.println("    " + i + ". файл : '" +
                                            bp.getFileName() + "'");
                            }
                        } // ***** похоже эта часть для работы с файлом - конец*/

                   /* // ************************************
                    // Если контент письма состоит из нескольких частей
                    if(mimeMessage.isMimeType("multipart/alternative"))
                    {
                        // getContent() возвращает содержимое тела письма, либо его части.
                        // Возвращаемый тип - Object, делаем каст в Multipart
                        Multipart multipart = (Multipart) mimeMessage.getContent();
                        // Перебираем все части составного тела письма
                        for(int i = 0; i < multipart.getCount(); i ++)
                        {
                            BodyPart part = multipart.getBodyPart(i);
                            //Для html-сообщений создается две части, "text/plain" и "text/html" (для клиентов
                            // без возможности чтения html сообщений), так что если нам не важна разметка:
                            if(part.isMimeType("text/plain")){
                                System.out.println(part.getContent().toString());
                            }
                            // Проверяем является ли part вложением
                            else if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
                            {
                                // Опускаю проверку на совпадение имен. Имя может быть закодировано, используем decode
                                String fileName = MimeUtility.decodeText(part.getFileName());
                                // Получаем InputStream
                                InputStream is = part.getInputStream();
                                // Далее можем записать файл, или что-угодно от нас требуется
                                System.out.println(is);
                 //....
                            }
                        }
                    }
// Сообщение состоит только из одного блока с текстом сообщения
                    else if(mimeMessage.isMimeType("text/plain")){
                        System.out.println(mimeMessage.getContent().toString());
                    }

                    // *************************************   */
                            LOGGER.info("---приехали1 WatchEmailRunnable-> " + LocalDateTime.now());
                            index++;
                        }
                    }
// !!!!!!!!!!!!!! тут по идее можно закрыть сторе и инбокс
                    LOGGER.info("---WatchEmailRunnable 154WER-> " + LocalDateTime.now() + "\n"
                            + Arrays.toString(arrSubjectFmEmail));
                    LOGGER.info("---WatchEmailRunnable 155WER-> " + LocalDateTime.now() + "\n"
                            + Arrays.toString(arrTextsFmEmail));
                    EditTextsFmEmail editTextsFmEmail = new EditTextsFmEmail();
                    editTextsFmEmail.getResult(arrSubjectFmEmail,arrTextsFmEmail);
                }
            } catch (InterruptedException | ClassCastException e) {
                LOGGER.error("---WatchEmailRunnable 226 catch-> " + LocalDateTime.now() + "\n" + e);
            } catch (RuntimeErrorException | RuntimeOperationsException re)
            {
                LOGGER.error("---WatchEmailRunnable 229 catch-> " + LocalDateTime.now() + "\n" + re);
            } catch (Exception e) {
                LOGGER.error("---WatchEmailRunnable 231 catch-> " + LocalDateTime.now() + "\n" + e);
            } finally
            {
                if (inbox != null)
                {
                    //Не забываем закрыть собой папку сообщений.
                    LOGGER.info("---WatchEmailRunnable 233 Не забываем закрыть собой папку сообщений. начало " +
                            "inbox.close-> " + LocalDateTime.now());
                    inbox.close(false);
                    LOGGER.info("---WatchEmailRunnable 235 Не забываем закрыть собой папку сообщений. конец " +
                            "inbox.close-> " + LocalDateTime.now());
                }
            }
        } catch (MessagingException /*| IOException*/ e) {
            fileWriteInOldCountEmails.writeToFileString(countOld);
            LOGGER.error("---WatchEmailRunnable 247 catch-> " + LocalDateTime.now() + "\n" + e);
        } catch (ClassCastException cse) {
            LOGGER.error("---WatchEmailRunnable 249 catch-> " + LocalDateTime.now() + "\n" + cse);
        } finally
        {
            if (store != null)
            {
                //И сам почтовый ящик тоже закрываем
                try {
                    LOGGER.info("---WatchEmailRunnable 248 И сам почтовый ящик тоже закрываем начало " +
                            "store.close-> " + LocalDateTime.now());
                    store.close();
                    LOGGER.info("---WatchEmailRunnable 250 И сам почтовый ящик тоже закрываем конец " +
                            "store.close-> " + LocalDateTime.now());
                } catch (MessagingException e) {
                    LOGGER.error("---WatchEmailRunnable 262 catch-> " + LocalDateTime.now() + "\n" + e);
                }
            }
        }
    }

//    @Override
//    public String toString() {
//        return super.toString();
//    }

  /*  //  если в тексте html то этот блок не нужен
    *//*static*//* void parseMultiparted(Multipart part) throws MessagingException, IOException
    {
        for (int i = 0; i < 1 *//*part.getCount()*//*; i++)
            parsePart(part.getBodyPart(i));
    }

    *//*static*//* void parsePart(BodyPart part) throws MessagingException, IOException
    {
        String type = part.getContentType();
        if (type.contains("multipart/"))
        {
            //System.out.println( parseMultiparted((Multipart)part.getContent()) );
            parseMultiparted((Multipart) part.getContent());
            System.out.println("96-> " + part.getContent().toString());
        }
        else if (type.contains("text/"))
        {
            *//*String text*//* textFmEmail = part.getContent().toString().trim();
            for (int j = 0; j < arrTextsFmEmail.length; j++)
            {
                if(arrTextsFmEmail[j] == null)
                {
                    arrTextsFmEmail[j] = textFmEmail;
                    break;
                }
            }
            //String text1 = text.substring(0, 11);//endIndex) // взять часть букв из фразы
            //System.out.println("\n105-> " + text1);
        } else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
        {
            //System.out.println("");
            System.out.println("99-> " + MimeUtility.decodeText(part.getFileName()));
        }
    }*/

    /*@Override
    public void close() throws Exception {

    }*/
}
