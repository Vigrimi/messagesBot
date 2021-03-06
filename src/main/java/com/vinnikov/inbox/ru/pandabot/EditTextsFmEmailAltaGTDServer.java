package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.openqa.selenium.NoSuchElementException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class EditTextsFmEmailAltaGTDServer implements EditTextsGetRoleFmCompanyNameInterf, CheckIfItIsRegistrationMail
{
    private String msgToDiscord1;
    private EntityMessage entityMessage;

    public EditTextsFmEmailAltaGTDServer() {}

    public void getResultAltaGTDServer(String[] arrSubjectFmEmail, String[] arrTextsFmEmail) throws Exception
    {
        for (int i = 0; i < arrTextsFmEmail.length; i++)
        {
            if(arrTextsFmEmail[i] != null)
            {
                entityMessage = new EntityMessage();
                msgToDiscord1 = "";
                Boolean flagStop = true;
                String tema = arrSubjectFmEmail[i];
                String text1 = arrTextsFmEmail[i];
                String text2 = text1.replaceAll("\r"," ");
                String text = text2.replaceAll("\n"," ");
                String commentFmTKS = null;
/*// тут добавить - скидывать в базу для мобприлож
                String info = tema + ", " + text;
                PutTextInDatabase.insertIntoTables(info);*/

                // взять 7 слов из комментария
                String[] arrTextForComment = text.split(" ");
                LOGGER.info("----EditTextsFmEmailAltaGTDServer-----arrTextForComment-> " + LocalDateTime.now() + "\n"
                        + Arrays.toString(arrTextForComment));
                int flagComment = 0;
                for (int jj = 0; jj < arrTextForComment.length-1; jj++)
                {
                    if(arrTextForComment[jj].contains("Комментарий"))
                    {
                        jj++;
                        commentFmTKS = "";
                        for (int jk = jj; jk < arrTextForComment.length; jk++)
                        {
                            if (arrTextForComment[jk].contains("Решение")) break;
                            commentFmTKS = commentFmTKS + arrTextForComment[jk] + " ";
                            flagComment++;
                            if (flagComment == 8) break;
                        }
                        break;
                    }
                }
                commentFmTKS = commentFmTKS.trim();
                if(commentFmTKS.isBlank() || commentFmTKS.isEmpty()) commentFmTKS = null;
                entityMessage.setComment(commentFmTKS);
                LOGGER.info("---EditTextsFmEmailAltaGTDServer-----commentFmTKS-> " + LocalDateTime.now()
                        + "\n" + commentFmTKS);

                if(tema.contains("Присвоен номер"))
                {
                    entityMessage.setStatusDT(Enums.REGISTERED_DT.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---60 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else
                if(tema.contains("Отказано в выпуске"))
                {
                    entityMessage.setStatusDT(Enums.RELEAS_PROHIBITED_DT.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---61 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else
                if(tema.contains("Выпуск разрешен"))
                {
                    entityMessage.setStatusDT(Enums.RELEASED_DT.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---62 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else
                if(text.contains("Статус процедуры: Сканирование оригиналов")) // брокер запросил таможню сканировать
                {
                    entityMessage.setStatusDT(Enums.BROKER_REQUEST_SCAN.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---63 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else
                if(text.contains("Статус процедуры: Проверка ТНВЭД")) // брокер запросил таможню сканировать
                {
                    entityMessage.setStatusDT(Enums.CSTMS_CODE_CHECK.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---64 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else
                if(text.contains("Статус процедуры: Проверка стоимости")) // брокер запросил таможню сканировать
                {
                    entityMessage.setStatusDT(Enums.CSTMS_PRICE_CHECK.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---65 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else // статьей 121 Таможенного - Выпуск условный по стоимости
                if(text.contains("ВЫПУСК ТОВАРОВ С ОСОБЕННОСТЯМИ, ПРЕДУСМОТРЕННЫМИ СТАТЬЕЙ 121") ||
                        text.contains("ВЫПУСК ПРИ УСЛОВИИ ОБЕСПЕЧЕНИЯ УПЛАТЫ"))
                {
                    entityMessage.setStatusDT(Enums.RELEASED_USLOVNO_121.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---66 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else // статьей 122 Таможенного - Выпуск условный по экспертизе
                if(text.contains("ВЫПУСК ТОВАРОВ С ОСОБЕННОСТЯМИ, ПРЕДУСМОТРЕННЫМИ СТАТЬЕЙ 122"))
                {
                    entityMessage.setStatusDT(Enums.RELEASED_USLOVNO_122.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---67 msgToDiscord1:" + msgToDiscord1);
                    flagStop = false;
                } else
                    if(text.contains("Уведомление о досмотре"))
                    {
                        entityMessage.setStatusDT(Enums.DOSMOTR.getTitle());
                        msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                        System.out.println("---68 msgToDiscord1:" + msgToDiscord1);
                        flagStop = false;
                    }

                arrSubjectFmEmail[i] = null;
                arrTextsFmEmail[i] = null;

                // остановить если сообщение не надо обрабатывать
                if (flagStop)
                {
                    LOGGER.info("---EditTextsFmEmailAltaGTDServer-не надо обрабатывать--flagStop-> "
                            + LocalDateTime.now() + "\n" + flagStop);
                } else
                {
                    // присоединить айди из мониторинга
                    if(commentFmTKS != null) // если комментарий пришёл в мэйле
                    {
                        if(msgToDiscord1.contains("ВТТ"))
                        {
                            String BTTCompNameFmMonit = GetBTTIdFromMonitoringGoogleDocs
                                    .getBTTCompanyNameFromMonitoringGoogleDocs(msgToDiscord1);
                            msgToDiscord1 = msgToDiscord1.replaceAll("\\*ПАНДА ТРАНС\\*,",BTTCompNameFmMonit);
                            //msgToDiscord1 = commentFmTKS + ", " + msgToDiscord1;
                        }
//                    else
//                    {
//                        msgToDiscord1 = commentFmTKS + ", " + msgToDiscord1;
//                    }
                    } else // if(commentFmTKS == null) // если комментарий НЕ пришёл в мэйле
                    {
                        if(msgToDiscord1.contains("ВТТ"))
                        {
                            String idNumbersFmMonitoring = GetBTTIdFromMonitoringGoogleDocs
                                    .getBTTIdFromMonitoringGoogleDocs(msgToDiscord1);
                            msgToDiscord1 = idNumbersFmMonitoring + msgToDiscord1;
                            msgToDiscord1 = msgToDiscord1.replaceAll("\\*ПАНДА ТРАНС\\*,","");
                        } else if(msgToDiscord1.contains("РАС"))
                        {
                            String idNumbersFmMonitoring = GetPACIdFromMonitoringGoogleDocs
                                    .getPACIdFromMonitoringGoogleDocs(msgToDiscord1);
                            msgToDiscord1 = idNumbersFmMonitoring + msgToDiscord1;
                        } else
                        {
                            System.out.println("---------------4444msgToDiscord1:" + msgToDiscord1);
                            String idNumbersFmMonitoring = GetIdFromMonitoringGoogleDocs
                                    .getIdFromMonitoringGoogleDocs(msgToDiscord1);
                            msgToDiscord1 = idNumbersFmMonitoring + msgToDiscord1;
                        }
                    }

                    //передаём для отправки в дискорд Вариант если есть айди ролей
                    if (entityMessage.getCompanyName() == null)
                        entityMessage.setCompanyName("*ОЙ*");
                    String textDisc = EditRoleForDiscord.getRoleForDiscord(msgToDiscord1);
                    // если не получилось найти айди такой роли
                    if(!textDisc.contains("<@&"))
                    {
//Вариант без айди ролей: передаём для отправки в дискорд и меняем звёздочки в названии фирмы для ватсапа на собачку
                        String textDiscBuffer = msgToDiscord1, regEx = "\\*", rep = "@";
                        textDiscBuffer = textDiscBuffer.replaceFirst(regEx, rep);
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer----5-1:-> " + LocalDateTime.now() + "\n" + textDiscBuffer);
                        textDiscBuffer = textDiscBuffer.replaceAll(regEx, "");
                        textDisc = textDiscBuffer;
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer----5-2:-> " + LocalDateTime.now() + "\n" + textDiscBuffer);
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer---6:-> " + LocalDateTime.now() + "\n" + textDisc);
                    }

                    // если ДОСМОТР или НАЗНАЧИЛИ ИДК на балтика или санпит - добавить роль ПОРТ
                    if(textDisc.contains("ДОСМОТР") || textDisc.contains("ИДК!"))
                    {
                        if(textDisc.contains("10216170") || textDisc.contains("10228010"))
                        {
                            textDisc = textDisc + " <@&785808375782309908>";
                        }
                    }
                    LOGGER.info("---EditTextsFmEmailAltaGTDServer---!!-textDisc--888-> " + LocalDateTime.now() + "\n"
                            + textDisc);
                    LOGGER.info("---EditTextsFmEmailAltaGTDServer---!!-msgToDiscord1-whatsap-999-> "
                            + LocalDateTime.now() + "\n" + msgToDiscord1);
                    try {
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer---ОТПРАВЛЯЮ ДИСКОРД-> " + LocalDateTime.now());
                        AppMsgToDiscordBot bot = new AppMsgToDiscordBot(textDisc);
                        bot.run();
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer---ОТПРАВЛЕНО В ДИСКОРД-> " + LocalDateTime.now());
                    } catch (InsufficientPermissionException ie)
                    {
                        ChromeWhatsappThread.sendInWatsapWeb("БОТ в дискорде не работает. Возможно добавлен новый канал " +
                                "и цифровой индекс канала ДТ-СТАТУСа съехал.");
                        LOGGER.error("---EditTextsFmEmailAltaGTDServer---!!-catch 169-> " + LocalDateTime.now() + "\n" + ie);
                    }

                    // если ВТТ и есть роль ПОРТ - то убрать её из сообщения для Ватсапа
                    if(msgToDiscord1.contains("ВТТ") && msgToDiscord1.contains("<@&785808375782309908>"))
                    {
                        msgToDiscord1 = msgToDiscord1.replaceAll("<@&785808375782309908>, ","");
                    }

                    // передаём для отправки в Ватсап
                    try
                    {
                        String msgToWhatsapp = msgToDiscord1.replaceAll("@","")
                                .replaceAll("Контейнеры: ","");
                        msgToWhatsapp = msgToWhatsapp.replaceAll("ДОСМОТР!","*ДОСМОТР!*")
                                .replaceAll("ИДК!","*ИДК!*");
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer---!!-ОТПРАВЛЯЮ ВАТСАП-> " + LocalDateTime.now());
                        ChromeWhatsappThread.sendInWatsapWeb(msgToWhatsapp);
                        LOGGER.info("---EditTextsFmEmailAltaGTDServer---!!-ОТПРАВЛЕНО В ВАТСАП-> " + LocalDateTime.now());
                    } catch (NoSuchElementException e)
                    {
                        LOGGER.error("---EditTextsFmEmailAltaGTDServer--!** ватсап не работает-> " + LocalDateTime.now() + "\n" + e);
                        AppMsgToDiscordBot bot1 =
                                new AppMsgToDiscordBot("<@&785778541060292628> ВНИМАНИЕ!!! Ватсап не работает. " +
                                        "Надо проверить подключён ли ватсап на компьютере при помощи QR-кода? "); // @декларант
                        bot1.run();
                    }

                    // посчитать общее кол-во присвоенных ДТ за месяц
                    if(msgToDiscord1.contains("зарегистрирован") || msgToDiscord1.contains("присво"))
                    {
                        CalculateQtyGTDPerMonth.getCalculateQtyGTDPerMonth();
                    }
                    // .....
                    entityMessage.saveInFile(entityMessage);
                }
            } else if(arrSubjectFmEmail[i] == null) break;
        }
    }

    public String getMessageAltaGTDServer(String tema, String text) // сообщение если номер присвоен, выпуск
    {
        //boolean THIS_IS_REGISTRATION_MAIL = true;
        try
        {
            int flagOOOorAO = 0;
            String conosamentPAC = "";
            String numberTC = "";
            String role = "";
//            String poluchatel = null;
//            String otpravitel = null;
            String companyName = "";

                    // взять из темы: номер ДТ
            String[] arrTema = tema.split(" ");
            String numberDT = "";

            // слова из названия импортёра
            for (int i = 0; i < arrTema.length; i++)
            {
                if(arrTema[i].equalsIgnoreCase("ДТ"))
                {
                    numberDT = arrTema[i+1];
                    entityMessage.setNumberDT(numberDT);
                }

                if(arrTema[i].equalsIgnoreCase("ООО") || arrTema[i].equalsIgnoreCase("АО"))
                {
                    //flagExport = 1;
                    companyName = arrTema[i+1].replaceAll("\"", "").trim();
                    int errorInCompName = 0;
                    if(companyName.isEmpty()) errorInCompName++;
                    if( (i+2) <= (arrTema.length-1) )
                    {
                        companyName = companyName + " " + arrTema[i+2].replaceAll("\"", "")
                                .replaceAll(" ", "").replaceAll(",", "");
                    }

                    if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
                    companyName = companyName.trim();
                    role = getRoleFmCompanyNameAltaGTDServer(companyName); // "@" + companyName + ",";
                    entityMessage.setCompanyName(role);
                    flagOOOorAO = 1;
                }
            }

            // берём нужное из тела письма
            String contNumbers = "";
            String[] arrText = text.split(" ");
            LOGGER.info("---EditTextsFmEmailAltaGTDServer---911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
            for (int j = 0; j < arrText.length; j++)
            {
                if (arrText[j].contains("Цена:") || arrText[j].contains("цена:") || arrText[j].contains("ЦЕНА:")
                        || arrText[j].contains("ИТС:") )
                {
                    String dollarKg = arrText[j];
                    for (int k = j + 1; k < arrText.length; k++)
                    {
                        if(arrText[k].contains("олучатель") || arrText[k].contains("ОЛУЧАТЕЛЬ")) break;
                        if(arrText[k].contains("тправитель") || arrText[k].contains("ТПРАВИТЕЛЬ")) break;
                        if(arrText[k].contains("тоимость") || arrText[k].contains("СТОИМОСТЬ")) break;
                        if(arrText[k].contains("омментарий") || arrText[k].contains("ОММЕНТАРИЙ")) break;
                        dollarKg = dollarKg + " " + arrText[k];
                    }
                    dollarKg = dollarKg.trim();
                    entityMessage.setDolKgRbn(dollarKg);
                }

                if (arrText[j].contains("ТС:") /*&& !text.contains("РУСАГРО")*/)
                {
                    numberTC = "Номер ТС: " + arrText[j+1].trim();
                }

                if (arrText[j].contains("ТН:") /*&& flagExport == 1*/) //text.contains("РУСАГРО"))
                {
                    LOGGER.info("---EditTextsFmEmailAltaGTDServer--присвоено ищу конос РАС-> " + LocalDateTime.now());
                    conosamentPAC = "ТН: " + arrText[j+1];
                }

                if (arrText[j].contains("онтейнер"))
                {
                    contNumbers = getAllContainersNumbersAltaGTDServer(arrText);
                    LOGGER.info("--------bbb--msgToDiscord1:" + contNumbers);
                }

                //System.out.println("--------ccc--msgToDiscord1:" + msgToDiscord1);
                if (arrText[j].contains("нспектор"))
                {
                    String inspektor = "Инспектор:"+ " " + arrText[j + 1] + " " + arrText[j + 2]
                            + " " + arrText[j + 3];

                    if (inspektor.contains("аможн") || inspektor.contains("АМОЖН") || inspektor.isBlank()
                            || inspektor.contains("Кругликов Евгений Викторович") || inspektor.isEmpty() ||
                            inspektor.contains("омментарий") || inspektor.contains("CUSTOM ROUTER")
                            || inspektor.contains("ТАМОЖЕН") || inspektor.contains("таможен")
                            || inspektor.contains("КРУГЛИКОВ ЕВГЕНИЙ ВИКТОРОВИЧ") || inspektor.contains("АВТОМАТ")
                            || inspektor.contains("КУДРО ЮРИЙ ВИКТОРОВИЧ") || inspektor.contains("кудро юрий викторович")
                            || inspektor.contains("Кудро Юрий Викторович") )
                    {
                        LOGGER.info("---8888-----ddd--inspektor:" + inspektor);
                        inspektor = "АВТОРЕГИСТРАЦИЯ";
                    }
                    entityMessage.setInspector(inspektor);
//                    msgToDiscord1 = msgToDiscord1 + inspektor;
                    LOGGER.info("--------ddd--entityMessage:" + entityMessage);
                    break;
                }

            }
            // если в теме ни ООО, ни АО, надо что-то положить
            if(flagOOOorAO == 0)
                entityMessage.setCompanyName(getCompanyNotOOONotAO(arrTema));
            LOGGER.info("---EditTextsFmEmailAltaGTDServer--329--REGISTERED entityMessage-> " + LocalDateTime.now()
                    + "\n" + entityMessage);

            // что внести в транспорт: контейнеры или фуру или разнорядку
            // импорт+экспорт, фура
            LOGGER.info("---------------9999numberTC:" + numberTC);
            if(contNumbers == null && !numberTC.equalsIgnoreCase("Номер ТС: "))
                entityMessage.setTransportNumber(numberTC);
            // импорт контейнеры
            if(contNumbers != null) entityMessage.setTransportNumber(contNumbers);
            // экспорт контейнеры - вносим ТН (в дт нет контейнеров и нет номера ТС)
            if(contNumbers == null && numberTC.equalsIgnoreCase("Номер ТС: "))
                entityMessage.setTransportNumber(conosamentPAC);

            // аторегистрация или автовыпуск
            if (entityMessage.getInspector().contains("АВТОРЕГИСТРАЦИЯ")
                    && entityMessage.getStatusDT().contains("Выпуск"))
            {
                entityMessage.setInspector("АВТОВЫПУСК");
            }

            // проверка, если это присвоено - внести номер дт и инспектор в файл регистраций, если ВЫПУСК то
            // проверить было ли БОТом обработано сообщение о присвоении и текущая ли дата в номере ДТ
            String nowDateStr = LocalDate.now().getDayOfMonth() + "";
            String secondPartDateFmNumberGTD = getsecondPartDateFmNumberGTD(entityMessage.getNumberDT());
            // если дт присвоена - внести в файл регистраций номер и инспектор
            if(entityMessage.getStatusDT().equalsIgnoreCase(Enums.REGISTERED_DT.getTitle()))
            {
                saveRegisteredNumbers(entityMessage.getNumberDT() + ";" + entityMessage.getInspector() + ";");
            } else // альта гтд-сервер не может обрабатывать заявления УЭО о выпуске до подачи - при любых
                // сообщениях (присвоено ЗВ, начата проверка, запросили доки, выпуск) - всегда пишет ВЫПУСК, надо
                // считать, если в файле регистраций этот номер только один раз, значит присвоили, если два - Выпуск
            if (entityMessage.getStatusDT().equalsIgnoreCase(Enums.RELEASED_DT.getTitle()) &&
                    entityMessage.getNumberDT().contains("В")) // В кириллицей
            {
                entityMessage = isItRegMail(entityMessage);
            } else // если сообщение о выпуске и номер ДТ от текущей даты, то надо проверить, было ли БОТом
                // обработано сообщение о присвоении
            if (secondPartDateFmNumberGTD.contains(nowDateStr) &&
                    entityMessage.getStatusDT().equalsIgnoreCase(Enums.RELEASED_DT.getTitle()))
            {
                entityMessage = isItRegMail(entityMessage);
            }

            LOGGER.info("--------eee--entityMessage:" + entityMessage);
        } catch (ArrayIndexOutOfBoundsException ai)
        {
            LOGGER.error("---EditTextsFmEmailAltaGTDServer--REGISTERED вылезли из массива-> "
                    + LocalDateTime.now() + "\n" + ai);
        }
        LOGGER.info("---EditTextsFmEmailAltaGTDServer--REGISTERED entityMessage-> " + LocalDateTime.now()
                + "\n" + entityMessage);

        if(entityMessage.getDolKgRbn() == null) entityMessage.setDolKgRbn("");
        msgToDiscord1 = entityMessage.toString();

        LOGGER.info("---EditTextsFmEmailAltaGTDServer--REGISTERED msgToDiscord1-> " + LocalDateTime.now()
                + "\n" + msgToDiscord1);
        return msgToDiscord1;
    }

    public String getCompanyNotOOONotAO(String[] arrTema)
    {
        // ДТ 10005030/021121/0570017 - Присвоен номер 5928 АГАПОВ СЕРГЕЙ ИГОРЕВИЧ ИНДИВИДУАЛЬНЫЙ ПРЕДПРИНИМАТЕЛЬ
        // ДТ 10005030/021121/0570017 - Выпуск разрешен 5928 АГАПОВ СЕРГЕЙ ИГОРЕВИЧ ИНДИВИДУАЛЬНЫЙ ПРЕДПРИНИМАТЕЛЬ
        String comment = entityMessage.getComment();
        String companyName = "";
        int index = 0;
        if(comment == null)
        {
            companyName = arrTema[arrTema.length-1];
            LOGGER.info("-375--EditTextsFmEmailAltaGTDServer--getCompanyNotOOONotAO--companyName-> " + LocalDateTime.now()
                    + "\n" + companyName);
        } else
        {
            String[] arrComment = comment.split(" ");
            String controlWord = arrComment[arrComment.length-1].trim().replaceAll(",","");
            for (int i = 0; i < arrTema.length; i++)
            {
                if(arrTema[i].contains(controlWord))
                {
                    index = i + 1;
                    break;
                }
            }
        }
        for (int i = index; i < arrTema.length; i++)
        {
            companyName = companyName + arrTema[i] + " ";
        }
        LOGGER.info("-394--EditTextsFmEmailAltaGTDServer--getCompanyNotOOONotAO--companyName-> " + LocalDateTime.now()
                + "\n" + companyName);
        companyName = companyName.trim().replace("ИНДИВИДУАЛЬНЫЙ ПРЕДПРИНИМАТЕЛЬ", "ИП");
        LOGGER.info("-397--EditTextsFmEmailAltaGTDServer--getCompanyNotOOONotAO--companyName-> " + LocalDateTime.now()
                + "\n" + companyName);
        String role = getRoleFmCompanyNameAltaGTDServer(companyName);
        LOGGER.info("-400--EditTextsFmEmailAltaGTDServer--getCompanyNotOOONotAO--role-> " + LocalDateTime.now()
                + "\n" + role);
        return role;
    }

    public String getAllContainersNumbersAltaGTDServer(String[] arrText) // получить текст со всеми номерами контейнеров
    {
        LOGGER.info("---EditTextsFmEmailAltaGTDServer-*getAllContainersNumb arrText-> " + LocalDateTime.now() + "\n"
                + Arrays.toString(arrText));
        String container = "";
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("онтейнер"))
            {
                //container = "Контейнеры: ";
                //msgToDiscord1 = msgToDiscord1 + " " + container;
                if(arrText[j+1].contains(":"))
                { // собрать номера контейнеров
                    container = "Контейнеры:";
                    for (int k = 1; k < 100; k++)
                    {
                        String containerNumber = arrText[j+1+k].replaceAll(" ","")
                                .replaceAll(",","");
                        if(containerNumber.length() == 11)
                        {
                            container = container + " " + containerNumber + ",";
                        } else //if(containerNumber.length() != 11)
                        {
                            break;
                        }

                   /* // попробую по другому брать номера контейнеров - см выше
                    if(!arrText[j+1+k].contains("<br") || !arrText[j+1+k+1].contains("Инспектор"))
                    {
                        msgToDiscord1 = msgToDiscord1 + arrText[j+1+k] + " ";
                    }
                    else if(arrText[j+1+k].contains("<br") || arrText[j+1+k+1].contains("Инспектор"))
                    {
                        String containerNumber = arrText[j+1+k].replaceAll("<br","");
                        msgToDiscord1 = msgToDiscord1 + containerNumber + " ";
                        break;
                    }*/
                        if(arrText[j+1+k].contains("Инспектор")) break;
                        if(j+1+k == arrText.length-1) break;
                        //break;
                    }
                } else // значит нет контейнеров
                {
                    container = null;
                }
                break;
            }
        }
        LOGGER.info("---EditTextsFmEmailAltaGTDServer-*getAllContainersNumb 600 container-> "
                + LocalDateTime.now() + "\n" + container);
        return container;
    }

    public String getRoleFmCompanyNameAltaGTDServer(String companyNameFmMail)
    {
        return getRoleFmCompanyNameInterf(companyNameFmMail);
    }

    public String getsecondPartDateFmNumberGTD(String numberDT)
    {
        String[] arr = numberDT.split("/");
        return arr[1].trim();
    }

}
