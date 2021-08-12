package com.vinnikov.inbox.ru.pandabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileWriteInOldCountEmails
{
    protected File file;

    public FileWriteInOldCountEmails(File file)
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

    public boolean writeToFileString(int b)
    {
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
            System.out.println("файл не был найден");
        } catch (IOException e) //что-то пошло не так
        {
            System.out.println("ошибка записи в файл");
        }
        return result;
    }
}
