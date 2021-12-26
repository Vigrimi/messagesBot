package com.vinnikov.inbox.ru.pandabot;

import java.io.*;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class FileReadFromOldCountEmails
{
    protected File file;
    private final String countOldFilePathName = "D:\\grIdea\\pandabot\\countOld.txt";

    public FileReadFromOldCountEmails(File file)
    {
        setFile(file);
    }

    public void setFile(File file)
    { // типа это txt файл - объект который хранит путь к файлу
        LOGGER.info("---FileReadFromOldCountEmails setFile стартовал-> " + LocalDateTime.now());
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
                LOGGER.info("---FileReadFromOldCountEmails setFile файл создан-> " + LocalDateTime.now());
            } else
            {
                LOGGER.info("---FileReadFromOldCountEmails setFile файл уже существует . -> " + LocalDateTime.now());
            }
        } catch (IOException e)
        { // если вообще никак не создаётся
            LOGGER.error("---FileReadFromOldCountEmails setFile файл не был создан-> " + LocalDateTime.now() + "\n" + e);
        }
    }

    public byte[] readFromFile() // читать из файла частями
    {
        LOGGER.info("---FileReadFromOldCountEmails readFromFile файл создан-> " + LocalDateTime.now());
        byte[] result = null; // если не удастся прочитать, то нал так и останется
        try (FileInputStream fileInput = new FileInputStream(file);
             ByteArrayOutputStream byteArray = new ByteArrayOutputStream() )
        // some text in file // читать текст в массив промежуточный byte[] частями и перекладываем в
        // byteArray []
        {
            byte[] buf = new byte[512];
            int readCount;
            while ( (readCount = fileInput.read(buf)) != -1) //метод read возвращает кол-во прочитанных байт,
            // когда достиг конца возвращает -1
            {
                byteArray.write(buf,0,readCount); //данные полученные идут в буфер, и потом берём из буфера
                // и перекладываем в массив
                // write(buf,0,readCount) - значит так:
            }
            result = byteArray.toByteArray();
        } catch (FileNotFoundException e)
        {
            LOGGER.error("---FileReadFromOldCountEmails readFromFile файл не был создан-> " + LocalDateTime.now()
                    + "\n" + e);
        } catch (IOException e) //что-то пошло не так
        {
            LOGGER.error("---FileReadFromOldCountEmails readFromFile ошибка чтения из файла-> " + LocalDateTime.now()
                    + "\n" + e);
        }
        return result;
        // если надо прочитать всё из файла - есть специальные ниопакеты и не надо изобретать велосипед
        //return new byte[0];
    }
}