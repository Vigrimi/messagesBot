package com.vinnikov.inbox.ru.pandabot;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class WatchEmailRunnable implements Runnable, FileWriteInRegisteredNumbersInterf//, AutoCloseable
{
    public int countOld;
    private String textFmEmail;
    public String[] arrSubjectFmEmailTKS;
    public String[] arrSubjectFmEmailAlta;
    public String[] arrTextsFmEmailTKS;
    public String[] arrTextsFmEmailAlta;
    private FileWriteInOldCountEmails fileWriteInOldCountEmails;
    private int flagTKSorAlta;
    private final int TKS = 0;
    private final int SVD_ALTA = 1;
    private final int ALTA_GTD_SERVER = 2;
    private final String countOldFilePathName = "D:\\grIdea\\pandabot\\countOld.txt";

    public WatchEmailRunnable(int countOld) {
        this.countOld = countOld;
        textFmEmail = "";
        arrSubjectFmEmailTKS = new String[300];
        arrSubjectFmEmailAlta = new String[300];
        arrTextsFmEmailTKS = new String[700];
        arrTextsFmEmailAlta = new String[700];
        fileWriteInOldCountEmails = new FileWriteInOldCountEmails (new File(countOldFilePathName));
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
        //String textFmEmail = "";
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
                store.connect("imap.yandex.ru", 993, "v.ru", "t5");
                        //store.addConnectionListener(store.connect());
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
                    flagTKSorAlta = TKS;
                    int indexTKS = 0; // индекс элемента в массиве
                    int indexAlta = 0; // индекс элемента в массиве
                    //Циклом пробегаемся по всем сообщениям
                    for (Message message : messages)
                    {
//                        System.out.println("\nTEXTgetDisposition: " + message.getDisposition()); // Нет
//                        System.out.println("TEXTgetFlags: " + message.getFlags()); // Нет
//                        System.out.println("TEXTgetContent.toString: " + message.getContent().toString()); // Нет
//                        System.out.println("TEXTgetDescription: " + message.getDescription()); //TEXTgetDescription: null
//                        System.out.println("TEXTgetFolder: " + message.getFolder()); // это название папки где лежит письмо
//                        System.out.println(message.getSubject());
//                        System.out.println(message.getFrom().toString());
//                        System.out.println(message.getSession());
                        if(message.getSubject().contains("ДТ подана. Регистрационный номер заявления о выпуске товаров до подачи"))
                        { // ЗВ, пока не обрабатывается, просто увеличить кол-во присвоенных ДТ
                            CalculateQtyGTDPerMonth.getCalculateQtyGTDPerMonth();
                        } else
                        if(message.getSubject().contains("работает?"))
                        {
                            String msgItWorks = "ПандаБот работает, " + LocalDateTime.now();
                            BCheckDoesItWork bot = new BCheckDoesItWork(msgItWorks);
                            bot.run();
                        } else
                        if( // для ТКС
                                (message.getSubject().contains("ДТ зарегистрирована. Регистрационный номер ДТ")) ||
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
                                message.getSubject().contains("Выпуск при условии обеспечения уплаты таможенных платежей. Решение по товарам") ||
                                message.getSubject().contains("НАЗНАЧИЛИ ИДК") ||
                                message.getSubject().contains("НАЗНАЧЕН ДОСМОТР") ||
                                message.getSubject().contains("назначен ДОСМОТР") ||
                                message.getSubject().contains("Рекомендовано уплатить  ввозные таможенные пошлины, налоги") ||
                                // для Альты svd-alta
                                message.getSubject().contains("Уведомление об изменении статуса процедуры ЭД") ||
                                // для Альты (ГТД-Сервер)  Выпуск разрешен
                                message.getSubject().contains("Присвоен номер") ||
                                message.getSubject().contains("Отказано в выпуске") ||
                                message.getSubject().contains("Выпуск разрешен") ||
                                message.getSubject().contains("Идет проверка") ||
                                message.getSubject().contains("Выпуск с обеспечением") ||
                                message.getSubject().contains("Решение различно по товарам") // + добавить ниже во флаг
                        )
                        {
                            System.out.println("-----ЗАШЁЛ---");
                            String textFmTypeMultipart = "";
                            flagTKSorAlta = TKS;
                            //От кого
                            //String from = ((InternetAddress) message.getFrom()[0]).getAddress();
                            //System.out.println("\nFROM: " + from);

                            //Тема письма
                            String TEXTgetSubject = message.getSubject();
                            // проверка - если сообщение от svd-альты, то флагАльта = 1; если альта-гтд-сервер = 2

                            /*// !!!!!!!!!! так как в данное время БОТ не обрабатывает мэйлы с svd-альты, то присваивание
                            // этого флага отключено
                            if (TEXTgetSubject.contains("Уведомление об изменении статуса процедуры ЭД"))
                                flagTKSorAlta = SVD_ALTA;*/

                            if (TEXTgetSubject.contains("Присвоен номер") || TEXTgetSubject.contains("Отказано в выпуске")
                            || TEXTgetSubject.contains("Выпуск разрешен") || TEXTgetSubject.contains("Идет проверка")
                            || TEXTgetSubject.contains("Выпуск с обеспечением") || TEXTgetSubject
                                    .contains("Решение различно по товарам") )
                                flagTKSorAlta = ALTA_GTD_SERVER;
                            LOGGER.info("---WatchEmailRunnable TEXTgetSubject-> " + LocalDateTime.now() + "\n"
                                    + TEXTgetSubject);
                            // Альта или ТКС - добавить в свой нужный массив
                            if(flagTKSorAlta == TKS) arrSubjectFmEmailTKS[indexTKS] = TEXTgetSubject;
                            else // если БОТ работает только по ALTA_GTD_SERVER
                                if(flagTKSorAlta == ALTA_GTD_SERVER)
                                    arrSubjectFmEmailAlta[indexAlta] = TEXTgetSubject;
                            // тело письма
                            String TEXTgetContent = message.getContent().toString();
                            //arrTextsFmEmailTKS[index] = TEXTgetContent;
                            LOGGER.info("---WatchEmailRunnable TEXTgetContent-> " + LocalDateTime.now() + "\n"
                                    + TEXTgetContent); // да, для панды так как хтмл и показывает, а простой текст нет

                            String TEXTgetContentType = message.getContentType();
                            LOGGER.info("---WatchEmailRunnable TEXTgetContentType-> " + LocalDateTime.now() + "\n"
                                    + TEXTgetContentType); // нет
                            if(TEXTgetContentType.contains("ultipart"))
                            {
                                LOGGER.info("---WatchEmailRunnable if(TEXTgetContentType.contains(\"multipart\")-> "
                                        + LocalDateTime.now());
                                //parseMultiparted(part);
                                TextGetContentTypeMultipart textGetContentTypeMultipart = new TextGetContentTypeMultipart();
                                Multipart part = (Multipart) message.getContent();

                                textFmTypeMultipart = textGetContentTypeMultipart.parseMultiparted(part);
                            } else // TEXTgetContent
                            {
                                textFmTypeMultipart = TEXTgetContent.replaceAll("\r"," ")
                                        .replaceAll("\n"," ");
                                System.out.println("------207 textFmTypeMultipart:" + textFmTypeMultipart);
                            }

                            // для Альта ГТД Сервер - записать в файл регистраций выпуск ЗВ
                            if (textFmTypeMultipart.contains("Выпуск заявления"))
                                saveRegisteredNumbers(textFmTypeMultipart);

                            // проверка, если сообщение из Альты, но не для обработки, то понизить индекс
                            if (flagTKSorAlta == SVD_ALTA) // svd-alta
                            {
//                                BCheckDoesItWork bot = new BCheckDoesItWork(textFmTypeMultipart);
//                                bot.run();

                                // !!!!!!! что надо обрабатывать из Альты
                                System.out.println("---7777:" + textFmTypeMultipart);
                                if ( //textFmTypeMultipart.contains("Присвоен номер ДТ")
                                        //textFmTypeMultipart.contains("Заявление зарегистрировано в ТО")
                                        textFmTypeMultipart.contains("Выпуск заявления")
                                        // || textFmTypeMultipart.contains("ыпуск товаров разреше")
                                        // || textFmTypeMultipart.contains("Доп. проверка по классификации")
                                        //textFmTypeMultipart.contains("Статус процедуры: Сканирование оригиналов")
                                )
                                {
                                    /* // разблокировать это если надо, чтобы БОТ обрабатывал текст из SVD_ALTA
                                    arrTextsFmEmailAlta[indexAlta] = textFmTypeMultipart;*/

                                    // сейчас сообщения из SVD_ALTA не обрабатываются БОТом, а нужны для статистики,
                                    // чтобы присвоения записывать в файл и БОТ мог сверяться с выпусками из
                                    // ГТД-СЕРВЕРА и если сообщение о регистрации прозевал, то написать его перед тем
                                    // как написать о выпуске
                                    // если ЗВ УЭО присвоено или выпущено - записать в файл регистраций
                                    saveRegisteredNumbers(textFmTypeMultipart);
                                    indexAlta--;
                                    //int q = 1; // просто так
                                } else
                                {
                                    textFmTypeMultipart = null;
                                    arrTextsFmEmailAlta[indexAlta] = textFmTypeMultipart;
                                    System.out.println("---8888:" + arrTextsFmEmailAlta[indexAlta]);
                                    indexAlta--;
                                }
                            } else
                            if (flagTKSorAlta == ALTA_GTD_SERVER) // alta гтд сервер
                            {
//                                BCheckDoesItWork bot = new BCheckDoesItWork(textFmTypeMultipart);
//                                bot.run();
                                // !!!!!!! что надо обрабатывать из Альты
                                System.out.println("---7777:" + textFmTypeMultipart);
                                arrTextsFmEmailAlta[indexAlta] = textFmTypeMultipart;
                                System.out.println("---8888:" + arrTextsFmEmailAlta[indexAlta]);
                            } else // если ткс в теме: "ЭД:  10" или "Уведомление об изменении статуса процедуры ЭД"
                            { // arrSubjectFmEmail[index] = TEXTgetSubject;
                                // такое игнорить
                                if (TEXTgetSubject.contains("ЭД:  10")
                                || TEXTgetSubject.contains("Уведомление об изменении статуса процедуры ЭД") )
                                {
                                    arrSubjectFmEmailTKS[indexTKS] = null;
                                    arrTextsFmEmailTKS[indexTKS] = null;
                                    System.out.println("---9999:" + arrTextsFmEmailTKS[indexTKS]);
                                    indexTKS--;
                                } else
                                    arrTextsFmEmailTKS[indexTKS] = textFmTypeMultipart;
                            }

                            if (indexAlta < 0)
                                LOGGER.info("---WatchEmailRunnable if(TEXTgetContentType.contains(\"multipart\") 222 -> "
                                        + LocalDateTime.now() + "\n" + arrTextsFmEmailAlta[indexAlta + 1]);
                            else
                                LOGGER.info("---WatchEmailRunnable if(TEXTgetContentType.contains(\"multipart\") 222 -> "
                                        + LocalDateTime.now() + "\n" + arrTextsFmEmailAlta[indexAlta]);

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
                            if(flagTKSorAlta == TKS) indexTKS++;
                            else /*if(flagAlta != TKS)*/ indexAlta++;
                            LOGGER.info(indexTKS + "-indexTKS--------------indexAlta:" + indexAlta);
                        }
                    }
// !!!!!!!!!!!!!! тут по идее можно закрыть сторе и инбокс
                    LOGGER.info("---WatchEmailRunnable 154WER-> " + LocalDateTime.now() + "\n"
                            + Arrays.toString(arrSubjectFmEmailTKS));
                    LOGGER.info("---WatchEmailRunnable 155WER-> " + LocalDateTime.now() + "\n"
                            + Arrays.toString(arrTextsFmEmailTKS));
                    LOGGER.info("---WatchEmailRunnable 954WER-> " + LocalDateTime.now() + "\n"
                            + Arrays.toString(arrSubjectFmEmailAlta));
                    LOGGER.info("---WatchEmailRunnable 955WER-> " + LocalDateTime.now() + "\n"
                            + Arrays.toString(arrTextsFmEmailAlta));
                    /*// если мэйл из ТКС или Альты
                    if(arrTextsFmEmailTKS[0] != null) // мэйл из ТКС
                    {
                        EditTextsFmEmail editTextsFmEmail = new EditTextsFmEmail();
                        editTextsFmEmail.getResult(arrSubjectFmEmailTKS, arrTextsFmEmailTKS);
                    }
                    if(arrTextsFmEmailAlta[0] != null && flagAlta == 1) // мэйл из Альты svd-alta
                    {
                        EditTextsFmEmailAlta editTextsFmEmailAlta = new EditTextsFmEmailAlta();
                        editTextsFmEmailAlta.getResultAlta(arrSubjectFmEmailAlta, arrTextsFmEmailAlta);
                    }
                    if(arrTextsFmEmailAlta[0] != null && flagAlta == 2) // мэйл из Альты гтд-сервер
                    {
                        EditTextsFmEmailAltaGTDServer editTextsFmEmailAltaGTDServer = new EditTextsFmEmailAltaGTDServer();
                        editTextsFmEmailAltaGTDServer.getResultAltaGTDServer(arrSubjectFmEmailAlta, arrTextsFmEmailAlta);
                    }*/
                }
            } catch (/*InterruptedException |*/ ClassCastException e) {
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
            //fileWriteInOldCountEmails.writeToFileString(countOld);
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
            // если мэйл из ТКС или Альты
            if(arrTextsFmEmailTKS[0] != null) // мэйл из ТКС
            {
                EditTextsFmEmail editTextsFmEmail = new EditTextsFmEmail();
                try {
                    editTextsFmEmail.getResult(arrSubjectFmEmailTKS, arrTextsFmEmailTKS);
                } catch (Exception e) {
                    LOGGER.error("---WatchEmailRunnable 374 catch-> " + LocalDateTime.now() + "\n" + e);
                }
            }
            if(arrTextsFmEmailAlta[0] != null && flagTKSorAlta == SVD_ALTA) // мэйл из Альты svd-alta
            {
                EditTextsFmEmailAlta editTextsFmEmailAlta = new EditTextsFmEmailAlta();
                try {
                    editTextsFmEmailAlta.getResultAlta(arrSubjectFmEmailAlta, arrTextsFmEmailAlta);
                } catch (Exception e) {
                    LOGGER.error("---WatchEmailRunnable 383 catch-> " + LocalDateTime.now() + "\n" + e);
                }
            }
            if(arrTextsFmEmailAlta[0] != null && flagTKSorAlta != SVD_ALTA) // мэйл из Альты гтд-сервер
            {
                EditTextsFmEmailAltaGTDServer editTextsFmEmailAltaGTDServer = new EditTextsFmEmailAltaGTDServer();
                try {
                    editTextsFmEmailAltaGTDServer.getResultAltaGTDServer(arrSubjectFmEmailAlta, arrTextsFmEmailAlta);
                } catch (Exception e) {
                    LOGGER.error("---WatchEmailRunnable 392 catch-> " + LocalDateTime.now() + "\n" + e);
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
