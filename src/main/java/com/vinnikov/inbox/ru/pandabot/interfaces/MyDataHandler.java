package com.vinnikov.inbox.ru.pandabot.interfaces;

import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public interface MyDataHandler {
    default String getDataFromFile(String fileName){
        String data = null;
        // попробовать 5 раз, если файл не отвечает
        for (int i = 0; i < 5; i++) {
            if (data == null || data.isEmpty() || data.isBlank()){
                data = MyFileHandler.readFmFileString(fileName);
            } else break;
        }

        if (data == null){
            data = "can_not_read_data_from_file";
            LOGGER.info("-------------- DataHandler data -> " + data + " -- " + LocalDateTime.now());
        }

        return data;
    }

    static String getDataFromFileStatic(String fileName){
        String data = null;
        // попробовать 5 раз, если файл не отвечает
        for (int i = 0; i < 5; i++) {
            if (data == null || data.isEmpty() || data.isBlank()){
                data = MyFileHandler.readFmFileString(fileName);
            } else break;
        }

        if (data == null){
            data = "can_not_read_data_from_file";
            LOGGER.info("-------------- DataHandler data -> " + data + " -- " + LocalDateTime.now());
        }

        return data;
    }
}
