package com.vinnikov.inbox.ru.pandabot;

import java.io.*;
import java.time.LocalDateTime;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class CalculateQtyGTDPerMonth
{
    private static final String fileNameFlag = "flag.txt";
    private static final String fileNameQtyGTD = "countQtyGTDPerMonth.txt";

    public static void getCalculateQtyGTDPerMonth()
    {
        LOGGER.info("---getCalculateQtyGTDPerMonth стартовал-> " + LocalDateTime.now());
        LocalDateTime rightNow = LocalDateTime.now();
        //LocalDateTime controlDate = LocalDateTime.of(2021,10,1,00,01,00);
        int qtyGTDFmFile = 0;

        // считать из файла число кол-ва ГТД за месяц
        qtyGTDFmFile = readFmFile(fileNameQtyGTD) + 1; // увеличить на единицу

        // записать в файл текущее кол-во дт или обнулить, если месяц закончился
        // если первые числа месяца - проверка обнулить или записать кол-во в файл
        if (rightNow.getDayOfMonth() <= 10)
        {
            // считать из файла флаг
            int flag = readFmFile(fileNameFlag);

            // если флаг = 2, надо обнулить кол-во ДТ за месяц и записать в файл
            if (flag == 2)
            {
                flag = 1;
                writeInOutputFile(flag, fileNameFlag);

                // отправить сообщение с общим кол-вом ДТ за прошлый месяц
                String msg = "Было присвоено *" + qtyGTDFmFile + "* ДТ за прошлый месяц.";
                ChromeWhatsappThread.sendInWatsapWeb(msg);

                // обнуляем счётчик в файле
                qtyGTDFmFile = 1;
                writeInOutputFile(qtyGTDFmFile, fileNameQtyGTD);
            }
            // если флаг = 1, значит уже обнулились и надо просто перезаписать кол-во в файл
            if (flag == 1)
            {
                writeInOutputFile(qtyGTDFmFile, fileNameQtyGTD);
            }
        } else // если НЕ первые числа месяца - проверка обнулить или записать кол-во в файл
        //if(rightNow.getDayOfMonth() > 10)
        {
            LOGGER.info("getCalculateQtyGTDPerMonth --rightNow.getDayOfMonth()-> " + LocalDateTime.now() +
                    "\n" + rightNow.getDayOfMonth());
            // считать из файла флаг
            int flag = readFmFile(fileNameFlag);

            // если флаг = 1, значит уже обнулялись ранее и надо флаг перезаписать
            if (flag == 1)
            {
                flag = 2;
                writeInOutputFile(flag, fileNameFlag);
                writeInOutputFile(qtyGTDFmFile, fileNameQtyGTD);
            }
            // если флаг = 2, то просто записать новое кол-во
            if (flag == 2)
            {
                writeInOutputFile(qtyGTDFmFile, fileNameQtyGTD);
            }
        }

        // в ватсап отправить юбилейное кол-во ДТ
        if (qtyGTDFmFile < 350 && qtyGTDFmFile % 50 == 0) // каждые 50дт при кол-ве до 349
        {
            String msg = "Присвоено *" + qtyGTDFmFile + "* ДТ (в т.ч. ВТТ, ЗВ) в этом месяце.";
            ChromeWhatsappThread.sendInWatsapWeb(msg);
        }
        if (qtyGTDFmFile >= 350 && qtyGTDFmFile % 10 == 0) // от 350 дт каждые 10 ДТ
        {
            String msg = "Присвоено *!!_" + qtyGTDFmFile + "_!!* ДТ (в т.ч. ВТТ, ЗВ) в этом месяце.";
            //if (qtyGTDFmFile >= 430) msg = msg + " Идём на рекорд! <;)";
            ChromeWhatsappThread.sendInWatsapWeb(msg);
        }
        LOGGER.info("getCalculateQtyGTDPerMonth ---qtyGTDFmFile-> " + LocalDateTime.now() +
                "\n" + qtyGTDFmFile);
    }

    public static int readFmFile(String fileName)
    {
        int digit = 0;
        try
        {
            // считать из файла число
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while (reader.ready())
            {
                String file = reader.readLine();
                digit = Integer.parseInt(file);
            }
            reader.close();
        } catch (IOException ie)
        {
            LOGGER.error("getCalculateQtyGTDPerMonth -readFmFile--catch-> " + LocalDateTime.now() + "\n" + ie);
        }
        return digit;
    }

    public static void writeInOutputFile(int digit,String fileName)
    {
        String digitStr = digit + "";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,false));
            sleep(200);
            writer.write(digitStr);
            sleep(200);
            writer.flush();
            sleep(1_000);
            writer.close();
            sleep(200);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("getCalculateQtyGTDPerMonth -writeInOutputFile--catch-> " + LocalDateTime.now() + "\n" + e);;
        }
    }
}
