package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.openqa.selenium.NoSuchElementException;
import java.time.LocalDateTime;
import java.util.Arrays;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class EditTextsFmEmailAlta
{
    private String msgToDiscord1;

    public EditTextsFmEmailAlta() {}

    public void getResultAlta(String[] arrSubjectFmEmail, String[] arrTextsFmEmail) throws Exception
    {
        for (int i = 0; i < arrTextsFmEmail.length; i++)
        {
            if(arrTextsFmEmail[i] != null)
            {
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
                LOGGER.info("----EditTextsFmEmailAlta-----arrTextForComment-> " + LocalDateTime.now() + "\n"
                        + Arrays.toString(arrTextForComment));
                int flagComment = 0;
                for (int jj = 0; jj < arrTextForComment.length; jj++)
                {
                    if(arrTextForComment[jj].contains("Комментарий"))
                    {
                        jj++;
                        commentFmTKS = "";
                        for (int jk = jj; jk < arrTextForComment.length; jk++)
                        {
                            commentFmTKS = commentFmTKS + arrTextForComment[jk] + " ";
                            flagComment++;
                            if (flagComment == 7) break;
                        }
                        break;
                    }
                }
                LOGGER.info("---EditTextsFmEmailAlta-----commentFmTKS-> " + LocalDateTime.now() + "\n" + commentFmTKS);

                if(text.contains("новый статус: \"Присвоен номер ДТ"))
                {msgToDiscord1 = getMessageRegisteredAlta(text);}
                else if(text.contains("Решение по товарам: Выпуск товаров разрешен"))
                {msgToDiscord1 = getMessageReleasedAlta(text);}

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
                        msgToDiscord1 = commentFmTKS + ", " + msgToDiscord1;
                    } else
                    {
                        msgToDiscord1 = commentFmTKS + ", " + msgToDiscord1;
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
                    LOGGER.info("---EditTextsFmEmailAlta----5-1:-> " + LocalDateTime.now() + "\n" + textDiscBuffer);
                    textDiscBuffer = textDiscBuffer.replaceAll(regEx, "");
                    textDisc = textDiscBuffer;
                    LOGGER.info("---EditTextsFmEmailAlta----5-2:-> " + LocalDateTime.now() + "\n" + textDiscBuffer);
                    LOGGER.info("---EditTextsFmEmailAlta---6:-> " + LocalDateTime.now() + "\n" + textDisc);
                }

                // если ДОСМОТР или НАЗНАЧИЛИ ИДК на балтика или санпит - добавить роль ПОРТ
                if(textDisc.contains("ДОСМОТР") || textDisc.contains("ИДК!"))
                {
                    if(textDisc.contains("10216170") || textDisc.contains("10228010"))
                    {
                        textDisc = textDisc + " <@&785808375782309908>";
                    }
                }
                LOGGER.info("---EditTextsFmEmailAlta---!!-textDisc--888-> " + LocalDateTime.now() + "\n" + textDisc);
                LOGGER.info("---EditTextsFmEmailAlta---!!-msgToDiscord1-whatsap-999-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
                try {
                    LOGGER.info("---EditTextsFmEmailAlta---ОТПРАВЛЯЮ ДИСКОРД-> " + LocalDateTime.now());
                    AppMsgToDiscordBot bot = new AppMsgToDiscordBot(textDisc);
                    bot.run();
                    LOGGER.info("---EditTextsFmEmailAlta---ОТПРАВЛЕНО В ДИСКОРД-> " + LocalDateTime.now());
                } catch (InsufficientPermissionException ie)
                {
                    ChromeWhatsappThread.sendInWatsapWeb("БОТ в дискорде не работает. Возможно добавлен новый канал " +
                            "и цифровой индекс канала ДТ-СТАТУСа съехал.");
                    LOGGER.error("---EditTextsFmEmailAlta---!!-catch 169-> " + LocalDateTime.now() + "\n" + ie);
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
                    LOGGER.info("---EditTextsFmEmailAlta---!!-ОТПРАВЛЯЮ ВАТСАП-> " + LocalDateTime.now());
                    ChromeWhatsappThread.sendInWatsapWeb(msgToWhatsapp);
                    LOGGER.info("---EditTextsFmEmailAlta---!!-ОТПРАВЛЕНО В ВАТСАП-> " + LocalDateTime.now());
                } catch (NoSuchElementException e)
                {
                    LOGGER.error("---EditTextsFmEmailAlta--!** ватсап не работает-> " + LocalDateTime.now() + "\n" + e);
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

    public String getMessageRegisteredAlta(String text) // сообщение если номер присвоен
    {
        try
        {
            int a = 0;
            if (text.contains("онтейнер")) a = 1;
            if (!text.contains("онтейнер")) a = 2;
            String conosamentPAC = "";
            String role = "";

            // берём нужное из тела письма
            String[] arrText = text.split(" ");
            LOGGER.info("---EditTextsFmEmailAlta---911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
            for (int j = 0; j < arrText.length; j++)
            {
                // получить номер ДТ
                String numberDT = "";
                //System.out.println("--regist--arrText[j]:" + arrText[j]);
                if (arrText[j].startsWith("10"))
                {
                    numberDT = arrText[j];
                    msgToDiscord1 = numberDT;
//                    if (arrTema[3 + i + 1].contains("ТД")) msgToDiscord1 = role + ", " + numberDT
//                            + ", присвоили ВТТ, <@&785808375782309908>,";
//                    else
//                        msgToDiscord1 = role + ", " + numberDT + ", " + arrTema[3 + i + 1] + " " + arrTema[3 + i + 2] + ",";
//                    break;
                }

                // слова из названия импортёра
                if(arrText[j].contains("Декларант"))
                {
                    String companyName = arrText[j+2].replaceAll("\"", "").trim();
                    int errorInCompName = 0;
                    if(companyName.isEmpty()) errorInCompName++;
                    if (!arrText[j+3].contains("Номер")) companyName = companyName + " "
                            + arrText[j+3].replaceAll("\"", "")
                            .replaceAll(" ", "").replaceAll(",", "");
                    if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
                    companyName = companyName.trim();
                    role = getRoleFmCompanyNameAlta(companyName); // "@" + companyName + ",";
                    msgToDiscord1 = role + ", " + msgToDiscord1 + ", ДТ зарегистрирована,";
                }

                if (arrText[j].contains("онтейнер") && a == 1)
                {
                    msgToDiscord1 = getAllContainersNumbersAlta(arrText);
                } else
                    if (arrText[j].contains("ТС:") && a == 2 && !text.contains("РУСАГРО"))
                    {
                        msgToDiscord1 = msgToDiscord1 + " " + arrText[j - 1].replaceAll("/>", "") +
                                " " + arrText[j] + " " + arrText[j + 1].replaceAll("<br", "") + ",";
                    } else
                        if (arrText[j].contains("ТС:") && a == 2 && text.contains("РУСАГРО"))
                        {
                        LOGGER.info("---EditTextsFmEmailAlta--присвоено ищу конос РАС-> " + LocalDateTime.now());
                        for (int w = 0; w < arrText.length; w++)
                        {
                            if ((arrText[w].contains("ТН:")) || (arrText[w].contains("TH:"))) // сначала на рус + на англ
                            {
                                conosamentPAC = arrText[w + 1];
                                msgToDiscord1 = msgToDiscord1 + " " + arrText[w] + " " + conosamentPAC + ",";
                                LOGGER.info("---EditTextsFmEmailAlta----msgToDiscord1-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
                            }
                        }
                }

                if (arrText[j].contains("нспектор"))
                {
                    String inspektor = " Инспектор:"+ " " + arrText[j + 1] + " " + arrText[j + 2]
                            + " " + arrText[j + 3] + ", ";
                    if (inspektor.contains("аможн") || inspektor.contains("АМОЖН")
                            || inspektor.contains("Кругликов Евгений Викторович"))
                        inspektor = "АВТОРЕГИСТРАЦИЯ,";

                    msgToDiscord1 = msgToDiscord1 + " " + inspektor;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ai)
        {
            LOGGER.error("---EditTextsFmEmailAlta--REGISTERED вылезли из массива-> " + LocalDateTime.now() + "\n" + ai);
        }
        return msgToDiscord1;
    }

//    public String getAllAboutScanningOfOriginals(String tema,String text) // сообщения про сканирования оригиналов
//    {
//        int a = 0;
//        if(text.contains("Контейнеры")) a = 1 ;
//        if(!text.contains("Контейнеры")) a = 2 ;
//
//        // берём нужное из темы
//        String[] arrTema = tema.split(" ");
//        LOGGER.info("---EditTextsFmEmail--getAllAboutScanni arrTem-> " + LocalDateTime.now() + "\n"
//                + Arrays.toString(arrTema));
//        // слова из названия импортёра
//        String companyName = arrTema[2].replaceAll("\"","");
//        int errorInCompName = 0;
//        if(companyName.isEmpty()) errorInCompName++;
//        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
//                .replaceAll(" ","").replaceAll(",","");
//        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
//        String role = getRoleFmCompanyName(companyName); //  "@" + companyName + ",";
//
//        // получить номер ДТ
//        String numberDT = "";
//        for (int i = 0; i < 10; i++)
//        {
//            if(arrTema[3+i].startsWith("10"))
//            {
//                numberDT = arrTema[3+i];
//                //msgToDiscord1 = role + " " + numberDT + " , " + arrTema[3+i+1] + " " + arrTema[3+i+2] ;
//                break;
//            }
//        }
//
//        if(tema.contains("Запрос на предоставление оригинала"))
//        {msgToDiscord1 = role + ", " + numberDT /*номерДТ*/ + ". Таможня запросила фА,";}
//        else if(tema.contains("Уведомление о способе предоставления оригинала документа"))
//        {msgToDiscord1 = role + ", " + numberDT /*номерДТ*/ + ". Мы запросили тпфк сканирование,";}
//        else if(tema.contains("Результат сканирования оригинала"))
//        {msgToDiscord1 = role + ", " + numberDT /*номерДТ*/ + ". Скан фА прилетел,";}
//
//        // берём нужное из тела письма
//        String[] arrText = text.split(" ");
//        LOGGER.info("---EditTextsFmEmail--getAllAboutScanni arrText-> " + LocalDateTime.now() + "\n"
//                + Arrays.toString(arrText));
//        for (int j = 0; j < arrText.length; j++)
//        {
//            if(arrText[j].contains("Контейнеры") && a == 1)
//            {
//                msgToDiscord1 = getAllContainersNumbers(arrText);
//            } else if(arrText[j].contains("ТС:") && a == 2)
//            {
//                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
//                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ",";
//            }
//
//            if(arrText[j].contains("Инспектор"))
//            {
//                String inspektor = arrText[j].replaceAll("/>","");
//                if (arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
//                {
//                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j+1] + ", ";
//                } else if (!arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
//                {
//                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
//                            + " " + arrText[j+3] + ", ";
//                }
//            }
//        }
//        return msgToDiscord1;
//    }

    public String getMessageReleasedAlta(String text) // сообщение если выпуск разрешён
    {
        try
        {
            String conosamentPAC = "";
            String role = "";
            int a = 0;
            if(text.contains("онтейнер")) a = 1 ;
            if(!text.contains("онтейнер")) a = 2 ;

            // берём нужное из тела письма
            String[] arrText = text.split(" ");
            LOGGER.info("---EditTextsFmEmail-ReleasedAlta--911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
            for (int j = 0; j < arrText.length; j++)
            {
                // получить номер ДТ
                String numberDT = "";
                //System.out.println("--releas--arrText[j]:" + arrText[j]);
                if (arrText[j].startsWith("10"))
                {
                    numberDT = arrText[j];
                    System.out.println("-----numberDT:" + numberDT);
                    msgToDiscord1 = numberDT;
//                    if (arrTema[3 + i + 1].contains("ТД")) msgToDiscord1 = role + ", " + numberDT
//                            + ", присвоили ВТТ, <@&785808375782309908>,";
//                    else
//                        msgToDiscord1 = role + ", " + numberDT + ", " + arrTema[3 + i + 1] + " " + arrTema[3 + i + 2] + ",";
//                    break;
                }

                // слова из названия импортёра
                if(arrText[j].contains("Декларант"))
                {
                    String companyName = arrText[j+2].replaceAll("\"", "");
                    int errorInCompName = 0;
                    if(companyName.isEmpty()) errorInCompName++;
                    if (!arrText[j+3].contains("Номер")) companyName = companyName + " "
                            + arrText[j+3].replaceAll("\"", "")
                            .replaceAll(" ", "").replaceAll(",", "");
                    if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
                    companyName = companyName.trim();
                    role = getRoleFmCompanyNameAlta(companyName); // "@" + companyName + ",";
                    msgToDiscord1 = role + ", " + msgToDiscord1 + ", Выпуск,";
                }

            //LOGGER.info("---EditTextsFmEmailAlta--!!!--159-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));

                if (arrText[j].contains("онтейнер") && a == 1)
                {
                    msgToDiscord1 = getAllContainersNumbersAlta(arrText);
                    LOGGER.info("---EditTextsFmEmailAlta--!!!--160-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
                } else
                    if (arrText[j].contains("ТС:") && a == 2 && !text.contains("РУСАГРО"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j - 1].replaceAll("/>", "") +
                            " " + arrText[j] + " " + arrText[j + 1].replaceAll("<br", "") + ",";
                } else
                    if (arrText[j].contains("ТС:") && a == 2 && text.contains("РУСАГРО"))
                {
                    for (int w = 0; w < arrText.length; w++)
                    {
                        if ((arrText[w].contains("ТН:")) || (arrText[w].contains("TH:"))) // сначала на рус + на англ
                        {
                            conosamentPAC = arrText[w + 1];
                            msgToDiscord1 = msgToDiscord1 + arrText[w] + " " + conosamentPAC + ",";
                        }
                    }
                }

                if(arrText[j].contains("нспектор"))
                {
                    String inspektor = " Инспектор:"+ " " + arrText[j + 1] + " " + arrText[j + 2]
                            + " " + arrText[j + 3] + ", ";
                    if (inspektor.contains("аможн") || inspektor.contains("АМОЖН")
                            || inspektor.contains("Кругликов Евгений Викторович"))
                        inspektor = " АВТОВЫПУСК,";

                    msgToDiscord1 = msgToDiscord1 + " " + inspektor;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ai)
        {
            LOGGER.error("---EditTextsFmEmailAlta-RELEASED вылезли из массива-> " + LocalDateTime.now() + "\n" + ai);
        }
        LOGGER.info("---EditTextsFmEmailAlta-Released-!!!--msgToDiscord1-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
        return msgToDiscord1;
    }

//    public String getMessageUslovnoReleased(String tema,String text)
//    {
//        int a = 0;
//        if(text.contains("Контейнеры")) a = 1 ;
//        if(!text.contains("Контейнеры")) a = 2 ;
//
//        int statya = 0; // Выпуск при условии обеспечения уплаты таможенных платежей. Решение по товарам
//        if(tema.contains("предусмотренными статьей 121 Таможенного")) statya = 121 ;
//        if(tema.contains("предусмотренными статьей 122 Таможенного")) statya = 122 ;
//
//        // берём нужное из темы
//        String[] arrTema = tema.split(" ");
//        // слова из названия импортёра
//        String companyName = arrTema[2].replaceAll("\"","");
//        int errorInCompName = 0;
//        if(companyName.isEmpty()) errorInCompName++;
//        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
//                .replaceAll(" ","").replaceAll(",","");
//        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
//        String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";
//        //msgToDiscord1 = role + " " + arrTema[3] /*номерДТ*/ + " , " + arrTema[4] + " " + arrTema[6] ;
//
//        // получить номер ДТ
//        String numberDT = "";
//        for (int i = 0; i < 10; i++)
//        {
//            if(arrTema[3+i].startsWith("10"))
//            {
//                numberDT = arrTema[3+i];
//                if(statya == 121)
//                {
//                    msgToDiscord1 = role + ", " + numberDT + ", Выпуск условный по стоимости, " ;
//                    break;
//                } else if(statya == 122)
//                {
//                    msgToDiscord1 = role + ", " + numberDT + ", Выпуск условный по экспертизе, " ;
//                    break;
//                } else //if(statya == 0)
//                {
//                    msgToDiscord1 = role + ", " + numberDT + ", Выпуск при условии обеспечения уплаты таможенных платежей, " ;
//                    break;
//                }
//            }
//        }
//
//        // берём нужное из тела письма
//        String[] arrText = text.split(" ");
//        LOGGER.info("---EditTextsFmEmail--!!условный--159-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
//        for (int j = 0; j < arrText.length; j++)
//        {
//            if(arrText[j].contains("Контейнеры") && a == 1)
//            {
//                msgToDiscord1 = getAllContainersNumbers(arrText);
//                LOGGER.info("---EditTextsFmEmail--!!условный--160-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
//            } else if(arrText[j].contains("ТС:") && a == 2)
//            {
//                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
//                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ", ";
//            }
//
//            if(arrText[j].contains("Инспектор"))
//            {
//                String inspektor = arrText[j].replaceAll("/>","");
//                if (arrText[j+2].contains("000"))
//                {
//                    msgToDiscord1 = msgToDiscord1 + " АВТОВЫПУСК. ";
//                } else if (!arrText[j+2].contains("000"))
//                {
//                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
//                            + " " + arrText[j+3] + ", ";
//                }
//            }
//        }
//        return msgToDiscord1;
//    }

//    public String getMessageDosmotr(String tema,String text)
//    {
//        int a = 0;
//        if(text.contains("Контейнеры")) a = 1 ;
//        if(!text.contains("Контейнеры")) a = 2 ;
//        String conosamentPAC = "";
//
//        // берём нужное из темы
//        String[] arrTema = tema.split(" ");
//        LOGGER.info("---EditTextsFmEmail--Dosmotr--900-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrTema));
//        // слова из названия импортёра
//        String companyName = arrTema[2].replaceAll("\"","");
//        int errorInCompName = 0;
//        if(companyName.isEmpty()) errorInCompName++;
//        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
//                .replaceAll(" ","").replaceAll(",","");
//        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
//        String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";
//
//        // получить номер ДТ
//        String numberDT = "";
//        for (int i = 0; i < 10; i++)
//        {
//            if(arrTema[3+i].startsWith("10"))
//            {
//                numberDT = arrTema[3+i];
//                if(tema.contains("НАЗНАЧИЛИ ИДК"))
//                {
//                    msgToDiscord1 = role + ", " + numberDT + ", ИДК! " ;
//                } else if(tema.contains("Досмотр товара") || tema.contains("НАЗНАЧЕН ДОСМОТР")
//                        || tema.contains("назначен ДОСМОТР"))
//                {
//                    msgToDiscord1 = role + ", " + numberDT + ", ДОСМОТР! , " ;
//                } else if (tema.contains("Результаты фактического контроля"))
//                {
//                    msgToDiscord1 = role + ", " + numberDT + " , Прилетел акт досмотра, " ;
//                } else if (tema.contains("Запрос документов и сведений. Уведомление о проведении таможенной экспертизы"))
//                {
//                    msgToDiscord1 = role + ", " + numberDT + " , *Проведение Таможенной экспертизы!* " ;
//                }
//                break;
//            }
//        }
//
//        // берём нужное из тела письма
//        String[] arrText = text.split(" ");
//        LOGGER.info("---EditTextsFmEmail--Dosmotr--911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
//        for (int j = 0; j < arrText.length; j++)
//        {
//            if(arrText[j].contains("Контейнеры") && a == 1)
//            {
//                msgToDiscord1 = getAllContainersNumbers(arrText);
//            } else if(arrText[j].contains("ТС:") && a == 2 && !tema.contains("РУСАГРО"))
//            {
//                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
//                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ", ";
//            } else if(arrText[j].contains("ТС:") && a == 2 && tema.contains("РУСАГРО"))
//            {
//                for (int w = 0; w < arrText.length; w++)
//                {
//                    if(arrText[w].contains("TH:"))
//                    {
//                        conosamentPAC = arrText[w+1];
//                        msgToDiscord1 = msgToDiscord1 + " " + arrText[w] + " " + conosamentPAC + ", ";
//                    }
//                }
//            }
//
//            if(arrText[j].contains("Инспектор"))
//            {
//                String inspektor = "";
//                if(arrText[j].contains("/>")) inspektor = arrText[j].replaceAll("/>","");
//                else inspektor = arrText[j];
//
//                if (arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
//                {
//                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j+1] + ", ";
//                } else if (!arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
//                {
//                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
//                            + " " + arrText[j+3] + ", ";
//                }
//            }
//        }
//        return msgToDiscord1;
//    }

    public String getAllContainersNumbersAlta(String[] arrText) // получить текст со всеми номерами контейнеров
    {
        LOGGER.info("---EditTextsFmEmailAlta-*getAllContainersNumb arrText-> " + LocalDateTime.now() + "\n"
                + Arrays.toString(arrText));
        String container = "";
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("онтейнер"))
            {
                container = "Контейнеры:";
                msgToDiscord1 = msgToDiscord1 + " " + container + " ";
                for (int k = 0; k < 100; k++)
                {
                    String containerNumber = arrText[j+1+k].replaceAll(" ","")
                            .replaceAll(",","");
                    if(containerNumber.length() == 11)
                    {
                        msgToDiscord1 = msgToDiscord1 + containerNumber + ", ";
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

                    if(j+1+k == arrText.length-1) break;
                    //break;
                }
                break;
            }
        }
        msgToDiscord1 = msgToDiscord1.replaceAll("\\(000\\)","");
        msgToDiscord1 = msgToDiscord1.replaceAll("АВТОРЕГИСТРАЦИЯ","");
        LOGGER.info("---EditTextsFmEmailAlta-*getAllContainersNumb 600 msgToDiscord1-> " + LocalDateTime.now() + "\n"
                + msgToDiscord1);
        return msgToDiscord1;
    }

    public String getRoleFmCompanyNameAlta(String companyNameFmMail)
    {
        String roleForDiscord = "*" + companyNameFmMail + "*";
        LOGGER.info("---EditTextsFmEmailAlta--getRoleFmCompanyNam-> " + LocalDateTime.now() + "\n" + roleForDiscord);

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
