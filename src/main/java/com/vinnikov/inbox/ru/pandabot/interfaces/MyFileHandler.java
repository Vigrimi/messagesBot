package com.vinnikov.inbox.ru.pandabot.interfaces;

import java.io.*;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public interface MyFileHandler {
    static int readFmFileInteger(String fileName){
        int digit = 0;
        try
        {
            // считать из файла число
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while (reader.ready())
            {
                String textFromFile = reader.readLine();
                // TODO сделать проверку, что из файла получено число
                digit = Integer.parseInt(textFromFile);
            }
            reader.close();
        } catch (IOException ie)
        {
            LOGGER.error("FileHandler -readFmFileInteger--catch-> " + LocalDateTime.now() + "\n" + ie);
        }
        return digit;
    }

    static String readFmFileString(String fileName){
        String fromFile = "";
        try{
            // считать из файла текст
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while (reader.ready()){
                fromFile = reader.readLine();
            }
            reader.close();
        } catch (IOException ie){
            LOGGER.error("FileHandler -readFmFileString--catch-> " + LocalDateTime.now() + "\n" + ie);
        }
        return fromFile;
    }

    static void writeTextInFile(String text,String fileName){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,false));
            sleep(200);
            writer.write(text);
            sleep(200);
            writer.flush();
            sleep(1_000);
            writer.close();
            sleep(200);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("FileHandler -writeTextInFile--catch-> " + LocalDateTime.now() + "\n" + e);;
        }
    }
}
