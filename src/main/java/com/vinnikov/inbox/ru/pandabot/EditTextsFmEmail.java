package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.NoSuchElementException;

import java.util.Arrays;

import static java.lang.Thread.sleep;

public class EditTextsFmEmail
{
    private String msgToDiscord1;

    public EditTextsFmEmail() {
        msgToDiscord1 = "";
    }

    public void getResult(String[] arrSubjectFmEmail, String[] arrTextsFmEmail) throws Exception
    {
        for (int i = 0; i < arrSubjectFmEmail.length; i++)
        {
            if(arrSubjectFmEmail[i] != null)
            {
                String tema = arrSubjectFmEmail[i];
                String text1 = arrTextsFmEmail[i];
                String text2 = text1.replaceAll("\r"," ");
                String text = text2.replaceAll("\n"," ");
/*// тут добавить - скидывать в базу для мобприлож
                String info = tema + ", " + text;
                PutTextInDatabase.insertIntoTables(info);*/

                if(tema.contains("ДТ зарегистрирована. Регистрационный номер ДТ"))
                {msgToDiscord1 = getMessageRegistered(tema,text);}
                else if(tema.contains("Выпуск товаров разрешен. Решение по товарам"))
                {msgToDiscord1 = getMessageReleased(tema,text);}
                else if(tema.contains("Запрос на предоставление оригинала") ||
                (tema.contains("Уведомление о способе предоставления оригинала документа")) ||
                        (tema.contains("Результат сканирования оригинала")) )
                {msgToDiscord1 = getAllAboutScanningOfOriginals(tema,text);}

                arrSubjectFmEmail[i] = null;
                arrTextsFmEmail[i] = null;

                // присоединить айди из мониторинга
                String idNumbersFmMonitoring = GetIdFromMonitoringGoogleDocs
                        .getIdFromMonitoringGoogleDocs(msgToDiscord1);
                msgToDiscord1 = idNumbersFmMonitoring + msgToDiscord1;

                // передаём для отправки в дискорд
    System.out.println("\n------------!!!!!!!!!!---888 " + msgToDiscord1);
                AppMsgToDiscordBot bot = new AppMsgToDiscordBot(msgToDiscord1);
                bot.run();


                // передаём для отправки в Ватсап
                try
                {
                    String msgToWhatsapp = msgToDiscord1.replaceAll("@","");
                    ChromeWhatsappThread.sendInWatsapWeb(msgToWhatsapp);
                } catch (NoSuchElementException e)
                {
                System.out.println("!!!!** ватсап не работает: ___" + e);
                    AppMsgToDiscordBot bot1 =
                            new AppMsgToDiscordBot("@декларант ВНИМАНИЕ!!! Ватсап не работает. Надо проверить " +
                                    "подключён ли ватсап на компьютере при помощи QR-кода? ");
                    bot1.run();
                    sleep(160_000); //160 СЕКУНД
                    bot1.close();
                }
                sleep(60_000); //60 СЕКУНД
                bot.close(); //!!!!!!!!!!!! пробую закрывать канал дискорда, чтобы не зависало !!!!!!!!!!
            } else if(arrSubjectFmEmail[i] == null) break;
        }
    }

    public String getMessageRegistered(String tema,String text) // сообщение если номер присвоен
    {
        int a = 0;
        if(text.contains("Контейнер")) a = 1 ;
        if(!text.contains("Контейнер")) a = 2 ;

        // берём нужное из темы
        String[] arrTema = tema.split(" ");
System.out.println("--------------900 " + Arrays.toString(arrTema));
        // слова из названия импортёра
        String companyName = arrTema[2].replaceAll("\"","");
        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
                .replaceAll(" ","").replaceAll(",","");
        String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";
        // получить номер ДТ
        String numberDT = "";
        for (int i = 0; i < 10; i++)
        {
            if(arrTema[3+i].startsWith("10"))
            {
                numberDT = arrTema[3+i];
                msgToDiscord1 = role + " " + numberDT + " , " + arrTema[3+i+1] + " " + arrTema[3+i+2] ;
                break;
            }
        }

        // берём нужное из тела письма
        String[] arrText = text.split(" ");
System.out.println("--------------911 " + Arrays.toString(arrText));
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнер") && a == 1)
            {
                msgToDiscord1 = getAllContainersNumbers(arrText);
            } else if(arrText[j].contains("ТС:") && a == 2)
            {
                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ". ";
            }

            if(arrText[j].contains("Инспектор"))
            {
                String inspektor = "";
                if(arrText[j].contains("/>")) inspektor = arrText[j].replaceAll("/>","");
                else inspektor = arrText[j];

                if (arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j+1] + ". ";
                } else if (!arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
                            + " " + arrText[j+3];
                }
            }
        }
        return msgToDiscord1;
    }

    public String getAllAboutScanningOfOriginals(String tema,String text) // сообщения про сканирования оригиналов
    {
        int a = 0;
        if(text.contains("Контейнер")) a = 1 ;
        if(!text.contains("Контейнер")) a = 2 ;

        // берём нужное из темы
        String[] arrTema = tema.split(" ");
System.out.println("---getAllAboutScanni arrTema " + Arrays.toString(arrTema));
        // слова из названия импортёра
        String companyName = arrTema[2].replaceAll("\"","");
        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
                .replaceAll(" ","").replaceAll(",","");
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
        {msgToDiscord1 = role + " " + numberDT /*номерДТ*/ + " . От таможни получен: Запрос на предоставление " +
                "оригинала документа. ";}
        else if(tema.contains("Уведомление о способе предоставления оригинала документа"))
        {msgToDiscord1 = role + " " + numberDT /*номерДТ*/ + " . В таможню отправлено: Уведомление о способе " +
                "предоставления оригинала документа. ";}
        else if(tema.contains("Результат сканирования оригинала"))
        {msgToDiscord1 = role + " " + numberDT /*номерДТ*/ + " . От таможни получен: Результат " +
                "сканирования оригинала документа. ";}

        // берём нужное из тела письма
        String[] arrText = text.split(" ");
System.out.println("---getAllAboutScanni arrText " + Arrays.toString(arrText));
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнер") && a == 1)
            {
                msgToDiscord1 = getAllContainersNumbers(arrText);
            } else if(arrText[j].contains("ТС:") && a == 2)
            {
                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ". ";
            }

            if(arrText[j].contains("Инспектор"))
            {
                String inspektor = arrText[j].replaceAll("/>","");
                if (arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + arrText[j+1] + ". ";
                } else if (!arrText[j+1].contains("АВТОРЕГИСТРАЦ"))
                {
                    msgToDiscord1 = msgToDiscord1 + " " + inspektor + " " + arrText[j+1] + " " + arrText[j+2]
                            + " " + arrText[j+3];
                }
            }
        }
        return msgToDiscord1;
    }

    public String getMessageReleased(String tema,String text) // сообщение если выпуск разрешён
    {
        int a = 0;
        if(text.contains("Контейнер")) a = 1 ;
        if(!text.contains("Контейнер")) a = 2 ;

        // берём нужное из темы
        String[] arrTema = tema.split(" ");
        // слова из названия импортёра
        String companyName = arrTema[2].replaceAll("\"","");
        if (!arrTema[3].contains("10")) companyName = companyName + " " + arrTema[3].replaceAll("\"","")
                .replaceAll(" ","").replaceAll(",","");
        String role = getRoleFmCompanyName(companyName); // "@" + companyName + ",";
        //msgToDiscord1 = role + " " + arrTema[3] /*номерДТ*/ + " , " + arrTema[4] + " " + arrTema[6] ;
        // получить номер ДТ
        String numberDT = "";
        for (int i = 0; i < 10; i++)
        {
            if(arrTema[3+i].startsWith("10"))
            {
                numberDT = arrTema[3+i];
                msgToDiscord1 = role + " " + numberDT + " , " + arrTema[3+i+1] + " " + arrTema[3+i+3] ;
                break;
            }
        }

        // берём нужное из тела письма
        String[] arrText = text.split(" ");
System.out.println("----!!!--159 " + Arrays.toString(arrText));
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнер") && a == 1)
            {
                msgToDiscord1 = getAllContainersNumbers(arrText);
System.out.println("---!!!--160 " + msgToDiscord1);
            } else if(arrText[j].contains("ТС:") && a == 2)
            {
                msgToDiscord1 = msgToDiscord1 + " " + arrText[j-1].replaceAll("/>","") +
                        " " + arrText[j] + " " + arrText[j+1].replaceAll("<br","") + ". ";
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
                            + " " + arrText[j+3];
                }
            }
        }
        return msgToDiscord1;
    }

    public String getAllContainersNumbers(String[] arrText) // получить текст со всеми номерами контейнеров
    {
System.out.println("****getAllContainersNumb arrText" + Arrays.toString(arrText));
        String container = "";
        for (int j = 0; j < arrText.length; j++)
        {
            if(arrText[j].contains("Контейнер"))
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
                    } else if(containerNumber.length() != 11)
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
//        msgToDiscord1 = msgToDiscord1.replaceAll("000","");
//        msgToDiscord1 = msgToDiscord1.replaceAll("\(","");
        msgToDiscord1 = msgToDiscord1.replaceAll("\\(000\\)","");
        msgToDiscord1 = msgToDiscord1.replaceAll("АВТОРЕГИСТРАЦИЯ","");
        return msgToDiscord1;
    }

    public String getRoleFmCompanyName(String companyNameFmMail)
    {
        String roleForDiscord = "@" + companyNameFmMail + ",";
        System.out.println("----------getRoleFmCompanyNam " + roleForDiscord);

        if (roleForDiscord.contains("ДЕТСКОЕ") && roleForDiscord.contains("ПИТАНИЕ")) roleForDiscord = "@НУТРИЦИЯ";
        if (roleForDiscord.contains("КОМПАНИЯ") && roleForDiscord.contains("ПРОДУКТ")
                && roleForDiscord.contains("СЕРВИС")) roleForDiscord = "@ПСервис (ЛЕБО)";
        if (roleForDiscord.contains("ФИННПАК")) roleForDiscord = "@ФП";
        if (roleForDiscord.contains("ТПП") && roleForDiscord.contains("ВКУСНЫЕ")) roleForDiscord = "@КОНСЕРВЫ";
        if (roleForDiscord.contains("АГРОИМПЭКС")) roleForDiscord = "@АИ";
        if (roleForDiscord.contains("АБ-МАРКЕТ")) roleForDiscord = "@АБ МАРКЕТ";
        if (roleForDiscord.contains("РУСАГРО-САХАР")) roleForDiscord = "@РАС";
        if (roleForDiscord.contains("ЦЕНТРСНАБ")) roleForDiscord = "@ЦентрСнаб";
        if (roleForDiscord.contains("ФРУТИМПЭКС")) roleForDiscord = "@Фрутимпэкс";
        if (roleForDiscord.contains("ЛЕКС")) roleForDiscord = "@ЛЕКС";
        if (roleForDiscord.contains("ТД") && roleForDiscord.contains("ДОКТОР")) roleForDiscord = "@ДОКТОР АППЕТИТ";
        if (roleForDiscord.contains("@И") && roleForDiscord.contains("ГРУПП")) roleForDiscord = "@И Групп";
        if (roleForDiscord.equals("@ГЛАТФЕЛТЕР,")) roleForDiscord = "@Контейнершипс";

        return roleForDiscord;
    }
}