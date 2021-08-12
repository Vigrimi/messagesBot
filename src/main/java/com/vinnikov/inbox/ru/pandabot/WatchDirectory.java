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

public class WatchDirectory implements Runnable
{
    public String[] arrTextsFmEmail;
    private FileReadFromOldCountEmails fileReadFromOldCountEmails;

    public WatchDirectory()
    {
        fileReadFromOldCountEmails = new FileReadFromOldCountEmails(new File("countOld.txt"));
    }

    @Override
        public void run()
        {
            int countOld = 0;
            int a = 0;
        try
        {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("C:\\Users\\user.NAVEX\\Documents\\Файлы Outlook");
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey key;
            // получаем ключ
            System.out.println("watchService запущен");
            while ((key = watchService.take()) != null)  // получаем событие и смотрим как на него отреагировать
            {
                // получаем список произошедших событий
                for (WatchEvent<?> event : key.pollEvents()) {
                    if(event.context().toString().contains("vasilii@pandatrans.ru - 1"))
                    {
                        countOld = Integer.parseInt( new String(fileReadFromOldCountEmails.readFromFile()) );
                            new WatchEmailRunnable(countOld).run();
                            System.out.println("wwwwwwwwwwwwwwww");
                        a++;
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException /*| MessagingException*/ e) {
            e.printStackTrace();
        }
    }
}
