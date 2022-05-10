package com.vinnikov.inbox.ru.pandabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class FileWriteInOldCountEmails
{
    protected File file;
    private final String countOldFilePathName = "D:\\grIdea\\pandabot\\countOld.txt";

    public FileWriteInOldCountEmails(File file)
    {
        setFile(file);
    }

    public void setFile(File file)
    { // типа это txt файл - объект который хранит путь к файлу
        LOGGER.info("---FileWriteInOldCountEmails setFile стартовал-> " + LocalDateTime.now());
        if (file == null || !file.getName().endsWith("txt") || file.isDirectory() )
        // проверяем на НАЛ, гетнэйм - имя файла строкой, или директория
        {
            this.file = new File(countOldFilePathName);
        } else
        {
            this.file = file;
        }
        try {
            if ( this.file.createNewFile() ) //надо делать эксепшин трай-кетч
            // новая диерктория, если уже такая есть - то фолз, если нет то создаст и вернёт тру
            {  // если файла не существует, то прочитать из него не получится
                LOGGER.info("---FileWriteInOldCountEmails setFile файл создан-> " + LocalDateTime.now());
            } else
            {
                LOGGER.info("---FileWriteInOldCountEmails setFile файл уже существует . -> " + LocalDateTime.now());
            }
        } catch (IOException e)
        { // если вообще никак не создаётся
            LOGGER.error("---FileWriteInOldCountEmails setFile файл не был создан-> " + LocalDateTime.now()
            + "\n" + e.getMessage());
        }
    }

    public boolean writeToFileString(int b)
    {
        LOGGER.info("---FileWriteInOldCountEmails writeToFileString стартовал-> " + LocalDateTime.now());
        boolean result = false;
        try (FileOutputStream outputStream = new FileOutputStream(file,false))  // данные из программы в файл
        // FileOutputStream outputStream = new FileOutputStream(file,true) - это создание объекта,
        // если создайтся несколько объектов, то их надо разделять точкой с запятой
        // в круглых скобках можно создавать любые объекты классы которых имплементируют интерфейс AutoCloseable
        // и тогда наличие этого интерфейса обяжет саму программу организвать КЛОУЗ и закрыть поток после
        // того как всё завершится или будет выброшен эксепшин
        // если дописал ТРУ то будет дозапись в конец в файле, не затирая что там есть, есть фолз -
        // то будет перезапись того что есть в файле
        {
            String bStr = b + "";
            outputStream.write(bStr.getBytes(StandardCharsets.UTF_8)); // массив в файл
            result = true;
        } catch (FileNotFoundException e) // нет файла
        {
            LOGGER.error("---FileWriteInOldCountEmails writeToFileString файл не был найден-> " + LocalDateTime.now()
                    + "\n" + e);
        } catch (IOException e) //что-то пошло не так
        {
            LOGGER.error("---FileWriteInOldCountEmails writeToFileString ошибка записи в файл-> " + LocalDateTime.now()
                    + "\n" + e);
        }
        return result;
    }
}
