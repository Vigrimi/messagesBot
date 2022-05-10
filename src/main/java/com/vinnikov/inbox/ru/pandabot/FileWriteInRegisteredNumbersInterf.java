package com.vinnikov.inbox.ru.pandabot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public interface FileWriteInRegisteredNumbersInterf
{
    public final String registeredFileName = "D:\\grIdea\\pandabot\\registnumbers.txt";

    default void saveRegisteredNumbers(String textForRegistFile)
    { // записывать в файл номера дт - факт присвоения номера
        LOGGER.info("interface FileWriteInRegisteredNumbers-начать интерфейс-> " + LocalDateTime.now());
        int FLAG_TEXT_FM_MAIL = 150;

        // если длина textForRegistFile больше 150 символов, то это текст из мэйла,
        // а если меньше, то это номер дт + фио инспектора
        if(textForRegistFile.length() > FLAG_TEXT_FM_MAIL)
        {
            String[] arrTextFmMail = textForRegistFile.split(" ");
            String numberDT = "";
            String inspektor = "";
            for (int i = 0; i < arrTextFmMail.length; i++)
            {
                // взять номер ЗВ
                if(arrTextFmMail[i].contains("еклараци")) numberDT = arrTextFmMail[i+2];
                // взять фио инспектора
                if(arrTextFmMail[i].contains("инспектор"))
                {
                    inspektor = arrTextFmMail[i+1];
                    if(i+2 < arrTextFmMail.length) inspektor = inspektor + " " + arrTextFmMail[i+2];
                    if(i+3 < arrTextFmMail.length) inspektor = inspektor + " " + arrTextFmMail[i+3];
                    break;
                }
            }
            inspektor = inspektor.trim() + ";";
            int DT_NUMBER_BASIC_LENGTH = 23;
            int THIRD_PART_DTNUMBER = 2;
            // если длина номера ДТ меньше 23, то это ЗВ без буквы "В" в номере - надо добавить букву "В"
            if(numberDT.length() < DT_NUMBER_BASIC_LENGTH)
            {
                String[] arrThreeParts = numberDT.split("/");
                if(THIRD_PART_DTNUMBER < arrThreeParts.length)
                {
                    String thirdPart = arrThreeParts[THIRD_PART_DTNUMBER];
                    if(thirdPart.length() < 7) thirdPart = "В" + thirdPart; // русская буква В
                    arrThreeParts[THIRD_PART_DTNUMBER] = thirdPart;
                    numberDT = arrThreeParts[THIRD_PART_DTNUMBER - 2] + "/" +
                            arrThreeParts[THIRD_PART_DTNUMBER - 1] + "/" +
                            arrThreeParts[THIRD_PART_DTNUMBER];
                }
            }
            numberDT = numberDT.trim() + ";";
            textForRegistFile = numberDT + inspektor;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter
                    ( registeredFileName,true));
            sleep(200);
            writer.write(textForRegistFile);
            sleep(200);
            writer.flush();
            sleep(1_000);
            writer.close();
            sleep(200);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("interface FileWriteInRegisteredNumbers--catch-> " + LocalDateTime.now() + "\n" + e);
        }
    }
}
