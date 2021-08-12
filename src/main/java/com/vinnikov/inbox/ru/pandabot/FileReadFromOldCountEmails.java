package com.vinnikov.inbox.ru.pandabot;

import java.io.*;

public class FileReadFromOldCountEmails
{
    protected File file;

    public FileReadFromOldCountEmails(File file)
    {
        setFile(file);
    }

    public void setFile(File file)
    { // типа это txt файл - объект который хранит путь к файлу
        if (file == null || !file.getName().endsWith("txt") || file.isDirectory() )
        // проверяем на НАЛ, гетнэйм - имя файла строкой, или директория
        {
            this.file = new File("countOld.txt");
        } else
        {
            this.file = file;
        }
        try {
            if ( this.file.createNewFile() ) //надо делать эксепшин трай-кетч
            // новая диерктория, если уже такая есть - то фолз, если нет то создаст и вернёт тру
            {  // если файла не существует, то прочитать из него не получится
                System.out.println(this.file.getName() + " создан");
            } else
            {
                System.out.println(/*this.file.getName() + */"."); //уже существует
            }
        } catch (IOException e)
        {
            System.out.println("файл не был создан " + e.getMessage()); // если вообще никак не создаётся
        }
    }

    public byte[] readFromFile() // читать из файла частями
    {
        byte[] result = null; // если не удастся прочитать то нал так и останется
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
            System.out.println("файл не был найден");
        } catch (IOException e) //что-то пошло не так
        {
            System.out.println("ошибка чтения из файла");
        }
        return result;
        // если надо прочитать всё из файла - есть специальные ниопакеты и не надо изобретать велосипед
        //return new byte[0];
    }
}