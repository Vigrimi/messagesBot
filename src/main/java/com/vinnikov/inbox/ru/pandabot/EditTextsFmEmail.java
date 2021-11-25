package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.openqa.selenium.NoSuchElementException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class EditTextsFmEmail
{
    private String msgToDiscord1;

    public EditTextsFmEmail() {}

    public void getResult(String[] arrSubjectFmEmail, String[] arrTextsFmEmail) throws Exception
    {
        for (int i = 0; i < arrSubjectFmEmail.length; i++)
        {
            if(arrSubjectFmEmail[i] != null)
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
                LOGGER.info("----EditTextsFmEmail-----arrTextForComment-> " + LocalDateTime.now() + "\n"
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
                LOGGER.info("---EditTextsFmEmail-----commentFmTKS-> " + LocalDateTime.now() + "\n" + commentFmTKS);

                if(tema.contains("ДТ зарегистрирована. Регистрационный номер ДТ"))
                {msgToDiscord1 = getMessageRegistered(tema,text);}
                else if(tema.contains("Выпуск товаров разрешен. Решение по товарам"))
                {msgToDiscord1 = getMessageReleased(tema,text);}
                else if(tema.contains("Отказано в выпуске товаров") && tema.contains("Решение по товарам ДТ"))
                {msgToDiscord1 = getMessageReleased(tema,text);}
                else if(tema.contains("Запрос на предоставление оригинала") ||
                (tema.contains("Уведомление о способе предоставления оригинала документа")) ||
                        (tema.contains("Результат сканирования оригинала")) )
                {msgToDiscord1 = getAllAboutScanningOfOriginals(tema,text);}
                else if(tema.contains("предусмотренными статьей 121 Таможенного кодекса") //  121 Таможенного
                        && tema.contains("Выпуск товаров") && tema.contains("Решение по товарам") )
                {msgToDiscord1 = getMessageUslovnoReleased(tema,text);}
                else if(tema.contains("предусмотренными статьей 122 Таможенного кодекса") //  121 Таможенного
        && tema.contains("Выпуск товаров") && tema.contains("Заключение") && tema.contains("таможенной экспертизы"))
                {msgToDiscord1 = getMessageUslovnoReleased(tema,text);}
                else if(tema.contains("Выпуск товаров с особенностями, предусмотренными статьей 122 Таможенного кодекса Евразийского эконом. Решение по тов"))
                {msgToDiscord1 = getMessageUslovnoReleased(tema,text);}
                else if(tema.contains("Досмотр товара")
                        && tema.contains("Уведомление о принятии решения по проведению фактического контроля")
                        && tema.contains("Требование о предъявлении товаров") )
                {msgToDiscord1 = getMessageDosmotr(tema,text);}
                else if(tema.contains("Запрос документов и сведений. Уведомление о проведении таможенной экспертизы") )
                {msgToDiscord1 = getMessageDosmotr(tema,text);}
                else if(tema.contains("Результаты фактического контроля") )
                {msgToDiscord1 = getMessageDosmotr(tema,text);}
                else if(tema.contains("НАЗНАЧИЛИ ИДК") )
                {msgToDiscord1 = getMessageDosmotr(tema,text);}
                else if(tema.contains("НАЗНАЧЕН ДОСМОТР") )
                {msgToDiscord1 = getMessageDosmotr(tema,text);}
                else if(tema.contains("назначен ДОСМОТР") )
                {msgToDiscord1 = getMessageDosmotr(tema,text);}
                else if(tema.contains("ТД зарегистрирована. Регистрация ЭТД") )
                {msgToDiscord1 = getMessageRegistered(tema,text);}
                else if(tema.contains("Выпуск товара. Выпуск товар") ) // BTT
                {msgToDiscord1 = getMessageReleased(tema,text);}
                else if(tema.contains("Подтверждение о прибытии. Подтверждение о прибытии") ) // BTT
                {msgToDiscord1 = getMessageReleased(tema,text);}
                else if(tema.contains("Транзит завершен. Завершение таможенного транзит") ) // BTT
                {msgToDiscord1 = getMessageReleased(tema,text);}
                else if(tema.contains("Выпуск при условии обеспечения уплаты таможенных платежей. Решение по товарам") )
                {msgToDiscord1 = getMessageUslovnoReleased(tema,text);}
                else if(tema.contains("Рекомендовано уплатить  ввозные таможенные пошлины, налоги") )
                {msgToDiscord1 = getMessageReleased(tema,text);}

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
                    LOGGER.info("---EditTextsFmEmail----5-1:-> " + LocalDateTime.now() + "\n" + textDiscBuffer);
                    textDiscBuffer = textDiscBuffer.replaceAll(regEx, "");
                    textDisc = textDiscBuffer;
                    LOGGER.info("---EditTextsFmEmail----5-2:-> " + LocalDateTime.now() + "\n" + textDiscBuffer);
                    LOGGER.info("---EditTextsFmEmail---6:-> " + LocalDateTime.now() + "\n" + textDisc);
                }

                // если ДОСМОТР или НАЗНАЧИЛИ ИДК на балтика или санпит - добавить роль ПОРТ
                if(textDisc.contains("ДОСМОТР") || textDisc.contains("ИДК!"))
                {
                    if(textDisc.contains("10216170") || textDisc.contains("10228010"))
                    {
                        textDisc = textDisc + " <@&785808375782309908>";
                    }
                }
                LOGGER.info("---EditTextsFmEmail---!!-textDisc--888-> " + LocalDateTime.now() + "\n" + textDisc);
                LOGGER.info("---EditTextsFmEmail---!!-msgToDiscord1-whatsap-999-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
                try {
                    LOGGER.info("---EditTextsFmEmail---ОТПРАВЛЯЮ ДИСКОРД-> " + LocalDateTime.now());
                    AppMsgToDiscordBot bot = new AppMsgToDiscordBot(textDisc);
                    bot.run();
                    LOGGER.info("---EditTextsFmEmail---ОТПРАВЛЕНО В ДИСКОРД-> " + LocalDateTime.now());
                } catch (InsufficientPermissionException ie)
                {
                    ChromeWhatsappThread.sendInWatsapWeb("БОТ в дискорде не работает. Возможно добавлен новый канал " +
                            "и цифровой индекс канала ДТ-СТАТУСа съехал.");
                    LOGGER.error("---EditTextsFmEmail---!!-catch 169-> " + LocalDateTime.now() + "\n" + ie);
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
                    LOGGER.info("---EditTextsFmEmail---!!-ОТПРАВЛЯЮ ВАТСАП-> " + LocalDateTime.now());
                    ChromeWhatsappThread.sendInWatsapWeb(msgToWhatsapp);
                    LOGGER.info("---EditTextsFmEmail---!!-ОТПРАВЛЕНО В ВАТСАП-> " + LocalDateTime.now());
                } catch (NoSuchElementException e)
                {
                    LOGGER.error("---EditTextsFmEmail--!** ватсап не работает-> " + LocalDateTime.now() + "\n" + e);
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

    public String getMessageRegistered(String tema,String text) // сообщение если номер присвоен
    {
        try
        {
            int a = 0;
            if (text.contains("Контейнеры")) a = 1;
            if (!text.contains("Контейнеры")) a = 2;
            String conosamentPAC = "";

            // берём нужное из темы
            String[] arrTema = tema.split(" ");
            LOGGER.info("---EditTextsFmEmail---900-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrTema));

            // слова из названия импортёра
            String companyName = arrTema[2].replaceAll("\"", "");
            int errorInCompName = 0;
            if(companyName.isEmpty()) errorInCompName++;
            if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"", "")
                    .replaceAll(" ", "").replaceAll(",", "");
            if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
            String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";

            // получить номер ДТ
            String numberDT = "";
            for (int i = 0; i < 10; i++) {
                if (arrTema[3 + i].startsWith("10")) {
                    numberDT = arrTema[3 + i];
                    if (arrTema[3 + i + 1].contains("ТД")) msgToDiscord1 = role + ", " + numberDT
                            + ", присвоили ВТТ, <@&785808375782309908>,";
                    else
                        msgToDiscord1 = role + ", " + numberDT + ", " + arrTema[3 + i + 1] + " " + arrTema[3 + i + 2] + ",";
                    break;
                }
            }

            // берём нужное из тела письма
            String[] arrText = text.split(" ");
            LOGGER.info("---EditTextsFmEmail---911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
            for (int j = 0; j < arrText.length; j++) {
                if (arrText[j].contains("Контейнеры") && a == 1) {
                    msgToDiscord1 = getAllContainersNumbers(arrText);
                } else if (arrText[j].contains("ТС:") && a == 2 && !tema.contains("РУСАГРО")) {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j - 1].replaceAll("/>", "") +
                            " " + arrText[j] + " " + arrText[j + 1].replaceAll("<br", "") + ",";
                } else if (arrText[j].contains("ТС:") && a == 2 && tema.contains("РУСАГРО")) {
                    LOGGER.info("---EditTextsFmEmail--присвоено ищу конос РАС-> " + LocalDateTime.now());
                    for (int w = 0; w < arrText.length; w++) {
                        if ((arrText[w].contains("ТН:")) || (arrText[w].contains("TH:"))) // сначала на рус + на англ
                        {
                            conosamentPAC = arrText[w + 1];
                            msgToDiscord1 = msgToDiscord1 + " " + arrText[w] + " " + conosamentPAC + ",";
                            LOGGER.info("---EditTextsFmEmail----msgToDiscord1-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
                        }
                    }
                }

                if (arrText[j].contains("Инспектор"))
                {
                    String inspektor = "";
                    if (arrText[j].contains("/>")) inspektor = arrText[j].replaceAll("/>", "");
                    else inspektor = arrText[j];

                    if (arrText[j + 1].contains("АВТОРЕГИСТРАЦ") || arrText[j + 1].contains("АВТОМАТ"))
                    {
                        msgToDiscord1 = msgToDiscord1 + " " + arrText[j + 1] + ", ";
                    } else
                        //if (!arrText[j + 1].contains("АВТОРЕГИСТРАЦ"))
                        {
                        msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j + 1] + " " + arrText[j + 2]
                                + " " + arrText[j + 3] + ", ";
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ai)
        {
            LOGGER.error("---EditTextsFmEmail--REGISTERED вылезли из массива-> " + LocalDateTime.now() + "\n" + ai);
        }
        return msgToDiscord1;
    }

    public String getAllAboutScanningOfOriginals(String tema,String text) // сообщения про сканирования оригиналов
    {
        int a = 0;
        if(text.contains("Контейнеры")) a = 1 ;
        if(!text.contains("Контейнеры")) a = 2 ;

        // берём нужное из темы
        String[] arrTema = tema.split(" ");
        LOGGER.info("---EditTextsFmEmail--getAllAboutScanni arrTem-> " + LocalDateTime.now() + "\n"
                + Arrays.toString(arrTema));
        // слова из названия импортёра
        String companyName = arrTema[2].replaceAll("\"","");
        int errorInCompName = 0;
        if(companyName.isEmpty()) errorInCompName++;
        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
                .replaceAll(" ","").replaceAll(",","");
        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
        String role = getRoleFmCompanyName(companyName); //  "@" + companyName + ",";

        // получить номер ДТ
        String numberDT = "";
        for (int i = 0; i < 10; i++)
        {
            if(arrTema[3+i].startsWith("10"))
            {
                numberDT = arrTema[3+i];
                //msgToDiscord1 = role + " " + numberDT + " , " + arrTema[3+i+1] + " " + arrTema[3+i+2] ;
                break;
            }
        }

        if(tema.contains("Запрос на предоставление оригинала"))
        {msgToDiscord1 = role + ", " + numberDT /*номерДТ*/ + ". Таможня запросила фА,";}
        else if(tema.contains("Уведомление о способе предоставления оригинала документа"))
        {msgToDiscord1 = role + ", " + numberDT /*номерДТ*/ + ". Мы запросили тпфк сканирование,";}
        else if(tema.contains("Результат сканирования оригинала"))
        {msgToDiscord1 = role + ", " + numberDT /*номерДТ*/ + ". Скан фА прилетел,";}

        // берём нужное из тела письма
        String[] arrText = text.split(" ");
        LOGGER.info("---EditTextsFmEmail--getAllAboutScanni arrText-> " + LocalDateTime.now() + "\n"
                + Arrays.toString(arrText));
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнеры") && a == 1)
            {
                msgToDiscord1 = getAllContainersNumbers(arrText);
            } else if(arrText[j].contains("ТС:") && a == 2)
            {
                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ",";
            }

            if(arrText[j].contains("Инспектор"))
            {
                String inspektor = arrText[j].replaceAll("/>","");
                if (arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j+1] + ", ";
                } else if (!arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
                            + " " + arrText[j+3] + ", ";
                }
            }
        }
        return msgToDiscord1;
    }

    public String getMessageReleased(String tema,String text) // сообщение если выпуск разрешён
    {
        try
        {
            String conosamentPAC = "";
            int a = 0;
            if(text.contains("Контейнеры")) a = 1 ;
            if(!text.contains("Контейнеры")) a = 2 ;

            // берём нужное из темы
            String[] arrTema = tema.split(" ");

            // слова из названия импортёра
            String companyName = arrTema[2].replaceAll("\"","");
            int errorInCompName = 0;
            if(companyName.isEmpty()) errorInCompName++;
            if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
             .replaceAll(" ","").replaceAll(",","");
            if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
            String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";
            //msgToDiscord1 = role + " " + arrTema[3] /*номерДТ*/ + " , " + arrTema[4] + " " + arrTema[6] ;

            // получить номер ДТ
            String numberDT = "";
            for (int i = 0; i < 10; i++)
            {
                if((3+i < arrTema.length) && arrTema[3+i].startsWith("10"))
                {
                    numberDT = arrTema[3+i];
                    if(tema.contains("Выпуск товара. Выпуск товар")) msgToDiscord1 = role + ", " + numberDT
                        + ", Выпуск ВТТ, <@&785808375782309908>,";
                    else if(tema.contains("Отказано в выпуске товаров. Решение по товарам ДТ"))
                        msgToDiscord1 = role + ", " + numberDT + ", Отказано в выпуске ДТ,";
                    else if(tema.contains("Рекомендовано уплатить  ввозные таможенные пошлины, налоги"))
                        msgToDiscord1 = role + ", " + numberDT
                                + ", Рекомендовано уплатить ввозные таможенные пошлины, налоги,";
                    else if(tema.contains("Подтверждение о прибытии. Подтверждение о прибытии")) msgToDiscord1 = role + ", "
                        + numberDT + ", ВТТ в пункте назначения,";
                    else if(tema.contains("Транзит завершен. Завершение таможенного транзит")) msgToDiscord1 = role + ", "
                        + numberDT + ", ВТТ завершён,";
                    else msgToDiscord1 = role + ", " + numberDT + ", " + arrTema[3+i+1] + ", "/* + arrTema[3+i+3]*/ ; // просто
                    //слово Выпуск, разрешён - сейчас скрыто
                    break;
                }
            }

            // берём нужное из тела письма
            String[] arrText = text.split(" ");
            LOGGER.info("---EditTextsFmEmail--!!!--159-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
            for (int j = 0; j < arrText.length; j++)
            {
                if(arrText[j].contains("Контейнеры") && a == 1)
                {
                    msgToDiscord1 = getAllContainersNumbers(arrText);
                    LOGGER.info("---EditTextsFmEmail--!!!--160-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
                } else if(arrText[j].contains("ТС:") && a == 2 && !tema.contains("РУСАГРО"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ", ";
                } else if(arrText[j].contains("ТС:") && a == 2 && tema.contains("РУСАГРО"))
                {
                    for (int w = 0; w < arrText.length; w++)
                    {
                        if( (arrText[w].contains("ТН:")) || (arrText[w].contains("TH:")) ) // сначала на рус + на англ
                        {
                            conosamentPAC = arrText[w+1];
                            msgToDiscord1 = msgToDiscord1 + arrText[w] + " " + conosamentPAC + ",";
                        }
                    }
                }

                if(arrText[j].contains("Инспектор"))
                {
                    String inspektor = arrText[j].replaceAll("/>","");
                    if (arrText[j+2].contains("000") || arrText[j+1].contains("АВТОМАТ"))
                    {
                        msgToDiscord1 = msgToDiscord1 + " АВТОВЫПУСК. ";
                    } else
                        //if (!arrText[j+2].contains("000"))
                    {
                        msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
                            + " " + arrText[j+3] + ", ";
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ai)
        {
            LOGGER.error("---EditTextsFmEmail-RELEASED вылезли из массива-> " + LocalDateTime.now() + "\n" + ai);
        }
        return msgToDiscord1;
    }

    public String getMessageUslovnoReleased(String tema,String text)
    {
        int a = 0;
        if(text.contains("Контейнеры")) a = 1 ;
        if(!text.contains("Контейнеры")) a = 2 ;

        int statya = 0; // Выпуск при условии обеспечения уплаты таможенных платежей. Решение по товарам
        if(tema.contains("предусмотренными статьей 121 Таможенного")) statya = 121 ;
        if(tema.contains("предусмотренными статьей 122 Таможенного")) statya = 122 ;

        // берём нужное из темы
        String[] arrTema = tema.split(" ");
        // слова из названия импортёра
        String companyName = arrTema[2].replaceAll("\"","");
        int errorInCompName = 0;
        if(companyName.isEmpty()) errorInCompName++;
        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
                .replaceAll(" ","").replaceAll(",","");
        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
        String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";
        //msgToDiscord1 = role + " " + arrTema[3] /*номерДТ*/ + " , " + arrTema[4] + " " + arrTema[6] ;

        // получить номер ДТ
        String numberDT = "";
        for (int i = 0; i < 10; i++)
        {
            if(arrTema[3+i].startsWith("10"))
            {
                numberDT = arrTema[3+i];
                if(statya == 121)
                {
                    msgToDiscord1 = role + ", " + numberDT + ", Выпуск условный по стоимости, " ;
                    break;
                } else if(statya == 122)
                {
                    msgToDiscord1 = role + ", " + numberDT + ", Выпуск условный по экспертизе, " ;
                    break;
                } else //if(statya == 0)
                {
                    msgToDiscord1 = role + ", " + numberDT + ", Выпуск при условии обеспечения уплаты таможенных платежей, " ;
                    break;
                }
            }
        }

        // берём нужное из тела письма
        String[] arrText = text.split(" ");
        LOGGER.info("---EditTextsFmEmail--!!условный--159-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнеры") && a == 1)
            {
                msgToDiscord1 = getAllContainersNumbers(arrText);
                LOGGER.info("---EditTextsFmEmail--!!условный--160-> " + LocalDateTime.now() + "\n" + msgToDiscord1);
            } else if(arrText[j].contains("ТС:") && a == 2)
            {
                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ", ";
            }

            if(arrText[j].contains("Инспектор"))
            {
                String inspektor = arrText[j].replaceAll("/>","");
                if (arrText[j+2].contains("000"))
                {
                    msgToDiscord1 = msgToDiscord1 + " АВТОВЫПУСК. ";
                } else if (!arrText[j+2].contains("000"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
                            + " " + arrText[j+3] + ", ";
                }
            }
        }
        return msgToDiscord1;
    }

    public String getMessageDosmotr(String tema,String text)
    {
        int a = 0;
        if(text.contains("Контейнеры")) a = 1 ;
        if(!text.contains("Контейнеры")) a = 2 ;
        String conosamentPAC = "";

        // берём нужное из темы
        String[] arrTema = tema.split(" ");
        LOGGER.info("---EditTextsFmEmail--Dosmotr--900-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrTema));
        // слова из названия импортёра
        String companyName = arrTema[2].replaceAll("\"","");
        int errorInCompName = 0;
        if(companyName.isEmpty()) errorInCompName++;
        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
                .replaceAll(" ","").replaceAll(",","");
        if(errorInCompName > 0) companyName = companyName.replaceAll(" ","");
        String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";

        // получить номер ДТ
        String numberDT = "";
        for (int i = 0; i < 10; i++)
        {
            if(arrTema[3+i].startsWith("10"))
            {
                numberDT = arrTema[3+i];
                if(tema.contains("НАЗНАЧИЛИ ИДК"))
                {
                    msgToDiscord1 = role + ", " + numberDT + ", ИДК! " ;
                } else if(tema.contains("Досмотр товара") || tema.contains("НАЗНАЧЕН ДОСМОТР")
                        || tema.contains("назначен ДОСМОТР"))
                {
                    msgToDiscord1 = role + ", " + numberDT + ", ДОСМОТР! , " ;
                } else if (tema.contains("Результаты фактического контроля"))
                {
                    msgToDiscord1 = role + ", " + numberDT + " , Прилетел акт досмотра, " ;
                } else if (tema.contains("Запрос документов и сведений. Уведомление о проведении таможенной экспертизы"))
                {
                    msgToDiscord1 = role + ", " + numberDT + " , *Проведение Таможенной экспертизы!* " ;
                }
                break;
            }
        }

        // берём нужное из тела письма
        String[] arrText = text.split(" ");
        LOGGER.info("---EditTextsFmEmail--Dosmotr--911-> " + LocalDateTime.now() + "\n" + Arrays.toString(arrText));
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнеры") && a == 1)
            {
                msgToDiscord1 = getAllContainersNumbers(arrText);
            } else if(arrText[j].contains("ТС:") && a == 2 && !tema.contains("РУСАГРО"))
            {
                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ", ";
            } else if(arrText[j].contains("ТС:") && a == 2 && tema.contains("РУСАГРО"))
            {
                for (int w = 0; w < arrText.length; w++)
                {
                    if(arrText[w].contains("TH:"))
                    {
                        conosamentPAC = arrText[w+1];
                        msgToDiscord1 = msgToDiscord1 + " " + arrText[w] + " " + conosamentPAC + ", ";
                    }
                }
            }

            if(arrText[j].contains("Инспектор"))
            {
                String inspektor = "";
                if(arrText[j].contains("/>")) inspektor = arrText[j].replaceAll("/>","");
                else inspektor = arrText[j];

                if (arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j+1] + ", ";
                } else if (!arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
                            + " " + arrText[j+3] + ", ";
                }
            }
        }
        return msgToDiscord1;
    }

    public String getAllContainersNumbers(String[] arrText) // получить текст со всеми номерами контейнеров
    {
        LOGGER.info("---EditTextsFmEmail-*getAllContainersNumb arrText-> " + LocalDateTime.now() + "\n"
                + Arrays.toString(arrText));
        String container = "";
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнеры"))
            {
                if(arrText[j].contains("/>"))
                {container = arrText[j].replaceAll("/>","");}
                else container = arrText[j];
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
        return msgToDiscord1;
    }

    public String getRoleFmCompanyName(String companyNameFmMail)
    {
        String roleForDiscord = "*" + companyNameFmMail + "*";
        LOGGER.info("---EditTextsFmEmail--getRoleFmCompanyNam-> " + LocalDateTime.now() + "\n" + roleForDiscord);

        if (roleForDiscord.contains("ДЕТСКОЕ") && roleForDiscord.contains("ПИТАНИЕ")) roleForDiscord = "*НУТРИЦИЯ*";
        if (roleForDiscord.contains("КОМПАНИЯ") && roleForDiscord.contains("ПРОДУКТ")
                && roleForDiscord.contains("СЕРВИС")) roleForDiscord = "*ПСервис (ЛЕБО)*";
        if (roleForDiscord.contains("ФИННПАК")) roleForDiscord = "*ФП*";
        if (roleForDiscord.contains("ТПП") && roleForDiscord.contains("ВКУСНЫЕ")) roleForDiscord = "*КОНСЕРВЫ*";
        if (roleForDiscord.contains("АГРОИМПЭКС")) roleForDiscord = "*АИ*";
        if (roleForDiscord.contains("АБ-МАРКЕТ")) roleForDiscord = "*АБ МАРКЕТ*";
        if (roleForDiscord.contains("РУСАГРО-САХАР")) roleForDiscord = "*РАС*";
        if (roleForDiscord.contains("ГРУППА") && roleForDiscord.contains("КОМПАНИЙ")) roleForDiscord = "*РАС*";
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