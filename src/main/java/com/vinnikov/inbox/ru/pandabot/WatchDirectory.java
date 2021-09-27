package com.vinnikov.inbox.ru.pandabot;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class WatchDirectory implements Runnable
{
    //public String[] arrTextsFmEmail;
    private FileReadFromOldCountEmails fileReadFromOldCountEmails;

    public WatchDirectory()
    {
        fileReadFromOldCountEmails = new FileReadFromOldCountEmails(new File("countOld.txt"));
    }

    @Override
        public void run()
        {
            int countOld = 0;
        try
        {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("C:\\Users\\user.NAVEX\\Documents\\Файлы Outlook");
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey key;
            // получаем ключ
            LOGGER.info("--WatchDirectory-watchService запущен-> " + LocalDateTime.now());
            while ((key = watchService.take()) != null)  // получаем событие и смотрим как на него отреагировать
            {
                // получаем список произошедших событий
                for (WatchEvent<?> event : key.pollEvents())
                {
                    if(event.context().toString().contains("vasilii@pandatrans.ru - 1"))
                    {
                        countOld = Integer.parseInt( new String(fileReadFromOldCountEmails.readFromFile()) );
                        new WatchEmailRunnable(countOld).run();
                        int hoursRightNow = LocalDateTime.now().getHour();
                        // если ночь, то тайм-аут делать 15 минут, днём = 45 секунд
                        if(hoursRightNow <= 7 || hoursRightNow >= 23)
                        {
                            LOGGER.info("-WatchDirectory-НОЧЬ тайм-аут 15 минут (900 секунд), чтобы яндекс не " +
                                    "расценил как ддос атака-> " + LocalDateTime.now());
                            sleep(900_000);
                        } else
                        {
                            LOGGER.info("-WatchDirectory-тайм-аут 45 секунд, чтобы яндекс не расценил как ддос " +
                                    "атака-> " + LocalDateTime.now());
                            sleep(45_000);
                        }
                        LOGGER.info("wwwwwwwwwwwwwwww-> " + LocalDateTime.now());
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException /*| MessagingException*/ e) {
            LOGGER.error("---WatchDirectory-что-то пошло не так catch-> " + LocalDateTime.now() + "\n" + e);
        }
    }
}
