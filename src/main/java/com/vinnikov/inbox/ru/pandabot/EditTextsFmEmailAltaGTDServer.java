package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.openqa.selenium.NoSuchElementException;
import java.time.LocalDateTime;
import java.util.Arrays;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class EditTextsFmEmailAltaGTDServer
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
                entityMessage.setComment(commentFmTKS.trim());
                LOGGER.info("---EditTextsFmEmailAltaGTDServer-----commentFmTKS-> " + LocalDateTime.now() + "\n" + commentFmTKS);

                if(tema.contains("Присвоен номер"))
                {
                    entityMessage.setStatusDT(Enums.REGISTERED_DT.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---60 msgToDiscord1:" + msgToDiscord1);
                } else
                if(tema.contains("Отказано в выпуске"))
                {
                    entityMessage.setStatusDT(Enums.RELEAS_PROHIBITED_DT.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---61 msgToDiscord1:" + msgToDiscord1);
                } else
                if(tema.contains("Выпуск разрешен"))
                {
                    entityMessage.setStatusDT(Enums.RELEASED_DT.getTitle());
                    msgToDiscord1 = getMessageAltaGTDServer(tema, text);
                    System.out.println("---62 msgToDiscord1:" + msgToDiscord1);
                }

                arrSubjectFmEmail[i] = null;
                arrTextsFmEmail[i] = null;

                // присоединить айди из мониторинга
                if(commentFmTKS != null) // если комментарий пришёл в мэйле
                {
                    if(msgToDiscord1.contains("ВТТ"))
                    {
                        String BTTCompNameFmMonit = GetBTTIdFromMonitoringGoogleDocs
                                .getBTTCompanyNameFromMonitoringGoogleDocs(msgToDiscord1);
                        msgToDiscord1 = msgToDiscord1.replaceAll("\\*ПАНДА ТРАНС\\*,",BTTCompNameFmMonit);
                        msgToDiscord1 = /*commentFmTKS + ", " +*/ msgToDiscord1;
                    } else
                    {
                        msgToDiscord1 = /*commentFmTKS + ", " +*/ msgToDiscord1;
                    }
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
            } else if(arrSubjectFmEmail[i] == null) break;
        }
    }

    public String getMessageAltaGTDServer(String tema, String text) // сообщение если номер присвоен
    {
        try
        {
            //int flagExport = 0;
            String conosamentPAC = "";
            String numberTC = "";
            String role = "";
//            String poluchatel = null;
//            String otpravitel = null;
            String companyName = "";

                    // взять из темы: номер ДТ
            String[] arrTema = tema.split(" ");
            String numberDT = arrTema[1];
            entityMessage.setNumberDT(numberDT);
            // слова из названия импортёра
            for (int i = 0; i < arrTema.length; i++)
            {
                if(arrTema[i].contains("ООО") || arrTema[i].contains("АО"))
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
                }
            }

            // берём нужное из тела письма
            String contNumbers = "";
            String[] arrText = text.split(" ");
            LOGGER.info("---EditTextsFmEmailAltaGTDServer---911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
            for (int j = 0; j < arrText.length; j++)
            {
                /*// слова из названия импортёра
                if(arrText[j].contains("Получатель:"))
                {
                    companyName = arrText[j+1].replaceAll("\"", "").trim();
                    int errorInCompName = 0;
                    if(companyName.isEmpty()) errorInCompName++;
                    if (!arrText[j+2].contains("Отправитель")) companyName = companyName + " "
                            + arrText[j+2].replaceAll("\"", "")
                            .replaceAll(" ", "").replaceAll(",", "");
                    if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
                    companyName = companyName.trim();
                    role = getRoleFmCompanyNameAltaGTDServer(companyName); // "@" + companyName + ",";
                    entityMessage.setCompanyName(role);
                }
                System.out.println("--------aaa--entityMessage:" + entityMessage);
                if(arrText[j].contains("Отправитель:"))
                {
                    if(arrText[j+1].contains("ООО"))
                    {
                        flagExport = 1;
                        companyName = arrText[j+2].replaceAll("\"", "").trim();
                        int errorInCompName = 0;
                        if(companyName.isEmpty()) errorInCompName++;
                        if (!arrText[j+3].contains("Товаров")) companyName = companyName + " "
                                + arrText[j+3].replaceAll("\"", "")
                                .replaceAll(" ", "").replaceAll(",", "");
                        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
                        companyName = companyName.trim();
                        role = getRoleFmCompanyNameAltaGTDServer(companyName); // "@" + companyName + ",";
                        entityMessage.setCompanyName(role);
                    }
                }*/

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
                    System.out.println("--------bbb--msgToDiscord1:" + contNumbers);
                }

                //System.out.println("--------ccc--msgToDiscord1:" + msgToDiscord1);
                if (arrText[j].contains("нспектор"))
                {
                    String inspektor = " Инспектор:"+ " " + arrText[j + 1] + " " + arrText[j + 2]
                            + " " + arrText[j + 3];
                    if (inspektor.contains("аможн") || inspektor.contains("АМОЖН") || inspektor.isBlank()
                            || inspektor.contains("Кругликов Евгений Викторович") || inspektor.isEmpty() ||
                            inspektor.contains("омментарий"))
                        inspektor = "АВТОРЕГИСТРАЦИЯ";
                    entityMessage.setInspector(inspektor);
//                    msgToDiscord1 = msgToDiscord1 + inspektor;
                    System.out.println("--------ddd--entityMessage:" + entityMessage);
                    break;
                }

            }
            // что внести в транспорт: контейнеры или фуру или разнорядку
            // импорт+экспорт, фура
            System.out.println("---------------9999numberTC:" + numberTC);
            if(contNumbers == null && !numberTC.isEmpty()) entityMessage.setTransportNumber(numberTC);
            if(contNumbers == null && !numberTC.isBlank()) entityMessage.setTransportNumber(numberTC);
            // импорт контейнеры
            if(contNumbers != null) entityMessage.setTransportNumber(contNumbers);
            // экспорт контейнеры - вносим ТН (в дт нет контейнеров и нет номера ТС)
            if(contNumbers == null && numberTC.isEmpty()) entityMessage.setTransportNumber(conosamentPAC);
            if(contNumbers == null && numberTC.isBlank()) entityMessage.setTransportNumber(conosamentPAC);

            // аторегистрация или автовыпуск
            if (entityMessage.getInspector().contains("АВТОРЕГИСТРАЦИЯ")
                    && entityMessage.getStatusDT().contains("Выпуск"))
            {
                entityMessage.setInspector("АВТОВЫПУСК");
            }

            System.out.println("--------eee--entityMessage:" + entityMessage);
        } catch (ArrayIndexOutOfBoundsException ai)
        {
            LOGGER.error("---EditTextsFmEmailAltaGTDServer--REGISTERED вылезли из массива-> "
                    + LocalDateTime.now() + "\n" + ai);
        }
        LOGGER.info("---EditTextsFmEmailAltaGTDServer--REGISTERED entityMessage-> " + LocalDateTime.now()
                + "\n" + entityMessage);
        msgToDiscord1 = entityMessage.toString();

        LOGGER.info("---EditTextsFmEmailAltaGTDServer--REGISTERED msgToDiscord1-> " + LocalDateTime.now()
                + "\n" + msgToDiscord1);
        return msgToDiscord1;
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
        String roleForDiscord = "*" + companyNameFmMail + "*";
        LOGGER.info("---EditTextsFmEmailAltaGTDServer--getRoleFmCompanyNam-> " + LocalDateTime.now()
                + "\n" + roleForDiscord);

        if (roleForDiscord.contains("ДЕТСКОЕ") && roleForDiscord.contains("ПИТАНИЕ")) roleForDiscord = "*НУТРИЦИЯ*";
        if (roleForDiscord.contains("КОМПАНИЯ") && roleForDiscord.contains("ПРОДУКТ")
                && roleForDiscord.contains("СЕРВИС")) roleForDiscord = "*ПСервис (ЛЕБО)*";
        if (roleForDiscord.contains("ФИННПАК")) roleForDiscord = "*ФП*";
        if (roleForDiscord.contains("ТПП") && roleForDiscord.contains("ВКУСНЫЕ")) roleForDiscord = "*КОНСЕРВЫ*";
        if (roleForDiscord.contains("АГРОИМПЭКС")) roleForDiscord = "*АИ*";
        if (roleForDiscord.contains("АБ-МАРКЕТ")) roleForDiscord = "*АБ МАРКЕТ*";
        if (roleForDiscord.contains("РУСАГРО-САХАР")) roleForDiscord = "*РАС*";
        if (roleForDiscord.contains("ЦЕНТРСНАБ")) roleForDiscord = "*ЦентрСнаб*";
        if (roleForDiscord.contains("ФРУТИМПЭКС")) roleForDiscord = "*Фрутимпэкс*";
        if (roleForDiscord.contains("ЛЕКС")) roleForDiscord = "*ЛЕКС*";
        if (roleForDiscord.contains("ТД") && roleForDiscord.contains("ДОКТОР")) roleForDiscord = "*ДОКТОР АППЕТИТ*";
        if (roleForDiscord.contains("@И") && roleForDiscord.contains("ГРУПП")) roleForDiscord = "*И Групп*";
        if (roleForDiscord.contains("ГЛАТФЕЛТЕР")) roleForDiscord = "*Контейнершипс (ГЛАТФЕЛТЕР)*";
        if (roleForDiscord.contains("МАКАЛПАЙН")) roleForDiscord = "*Контейнершипс (МАКАЛПАЙН)*";
        if (roleForDiscord.contains("САМАРАМАЛТ")) roleForDiscord = "*Контейнершипс (САМАРАМАЛТ)*";
        if (roleForDiscord.contains("ТД") && roleForDiscord.contains("АПРИКО")) roleForDiscord = "*Априко*";
        if (roleForDiscord.contains("ФЕС") && roleForDiscord.contains("ПРОДУКТ")) roleForDiscord = "*ФЕС ПРОДУКТ*";
        if (roleForDiscord.contains("АУТСПАН") && roleForDiscord.contains("ИНТЕРНЕШНЛ")) roleForDiscord = "*АУТСПАН*";
        if (roleForDiscord.contains("ТОРГОВЫЙ") && roleForDiscord.contains("ДОМ")) roleForDiscord = "*ТДМ*";
        if (roleForDiscord.contains("ПИЩЕВОЙ") && roleForDiscord.contains("КОМБИНАТ")) roleForDiscord = "*ПК АЗОВСКИЙ*";
        if (roleForDiscord.contains("УНИТРОН") && roleForDiscord.contains("ПРОМ")) roleForDiscord = "*УНИТРОН ПРОМ*";
        if (roleForDiscord.contains("УНИТРОН") && roleForDiscord.contains("ФИРМА")) roleForDiscord = "*Ф.УНИ*";
        if (roleForDiscord.contains("АЛЬФА") && roleForDiscord.contains("ФУД")) roleForDiscord = "*АЛЬФА ФУД ИНГРЕДИЕНТС*";

        return roleForDiscord;
    }

}
