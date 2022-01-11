package com.vinnikov.inbox.ru.pandabot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.FileWriteInRegisteredNumbersInterf.registeredFileName;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public interface CheckIfItIsRegistrationMail extends FileWriteInRegisteredNumbersInterf
{
    default EntityMessage isItRegMail(EntityMessage entityMessage)
    {
        String numberDT = entityMessage.getNumberDT();
        String fileName = registeredFileName;
        String textFmRegistFile = "";
        int NOT_FOUND = 0;
        int ONCE_FOUND = 1;
        int TWICE_FOUND = 2;
        LOGGER.info("interface CheckIfItIsRegistrationMail-начать интерфейс-> " + LocalDateTime.now());
        // проверить, есть ли номер ДТ в файле присвоенных номеров: если нет, то это присвоение номера и надо поменять
        // статус у entityMessage и в файл регистраций внести номер ДТ и инспектора
        try
        {
            // считать всё из файла с присвоенными номерам
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.ready())
            {
                textFmRegistFile = reader.readLine();
            }
            reader.close();
        } catch (IOException ie)
        {
            LOGGER.error("CheckIfItIsRegistrationMail -readFmFile--catch-> " + LocalDateTime.now() + "\n" + ie);
        }
        String[] arrWordsFmRegFile = textFmRegistFile.split(";");
        boolean FLAG_NUMBER_IS_IN_REGIST_FILE = false;

        // если это заявление до выпуска ЗВ УЭО, то надо проверить сколько раз номер присутствует в файле регистраций:
        // если нету - то это регистрация и БОТ должен отправить сообщ о регистрации и записать в файл регистр;
        // если есть один раз - значит
        // БОТ уже отправлял сообщ о присвоении, а это какое-то промежуточное сообщение и ещё не выпуск; если есть
        // два раза - значит это сообщение о выпуске и БОТ должен отправить сообщ Выпуск
        if (numberDT.contains("В")) // В кириллицей
        {
            LOGGER.info("interface CheckIfItIsRegistrationMail- проверка ЗВ начало-> " + LocalDateTime.now());
            int qtyFoundedInRegistFile = 0;
            for (int i = 0; i < arrWordsFmRegFile.length; i++)
            {
                if(arrWordsFmRegFile[i].equalsIgnoreCase(numberDT))
                {
                    qtyFoundedInRegistFile++;
                    if(qtyFoundedInRegistFile == TWICE_FOUND)
                    { // если есть второй раз - взять фио инспектора выпускающего
                        entityMessage.setInspector(arrWordsFmRegFile[i+1]);
                    }
                }
            }
            LOGGER.info("interface CheckIfItIsRegistrationMail- проверка ЗВ найдено раз->" + qtyFoundedInRegistFile);
            if(qtyFoundedInRegistFile == NOT_FOUND)
            { // это регистрация и БОТ должен отправить сообщ о регистрации и записать в файл регистр
                entityMessage.setStatusDT(Enums.REGISTERED_DT.getTitle());
                entityMessage.setInspector("АВТОРЕГИСТРАЦИЯ");
            } else // один раз - значит БОТ уже отправлял сообщ о присвоении, а это какое-то промежуточное сообщение
                if(qtyFoundedInRegistFile == ONCE_FOUND)
                {
                    entityMessage.setStatusDT("Идёт проверка");
                    entityMessage.setInspector("АВТОРЕГИСТРАЦИЯ");
                    FLAG_NUMBER_IS_IN_REGIST_FILE = true; // в файл не записывать ничего
                } else // два раза - значит это сообщение о выпуске и БОТ должен отправить сообщ Выпуск
                    if(qtyFoundedInRegistFile >= TWICE_FOUND)
                    {
                        FLAG_NUMBER_IS_IN_REGIST_FILE = true; // в файл не записывать ничего
                    }
        } else
        {
            // перебрать массив слов из файла с присвоенными номерами и сравнить с пришедшим номером
            for (String s : arrWordsFmRegFile)
            {
                // номер ДТ есть в файле с регистрационными номерами, значит это реально Выпуск
                if(s.equalsIgnoreCase(numberDT))
                {
                    FLAG_NUMBER_IS_IN_REGIST_FILE = true;
                    break;
                }
            }
        }
        LOGGER.info("interface CheckIfItIsRegistrationMail-FLAG_NUMBER_IS_IN_REGIST_FILE-> " + FLAG_NUMBER_IS_IN_REGIST_FILE);
        // номер ДТ есть в файле с регистрационными номерами, значит это реально Выпуск у entityMessage ничего не менять
        if(FLAG_NUMBER_IS_IN_REGIST_FILE)
            return entityMessage;
        else // если нет номера ДТ в файле с регистрационными номерами, значит это не выпуск
        { // в файл регистраций внести номер ДТ и инспектора
            String numberDtAndInspektor = numberDT + ";" + entityMessage.getInspector() + ";";
            saveRegisteredNumbers(numberDtAndInspektor);
            // поменять статус у entityMessage на Присовение
            entityMessage.setStatusDT(Enums.REGISTERED_DT.getTitle());
            // автовыпуск поменять на аторегистрация
            if (entityMessage.getInspector().contains("АВТОВЫПУСК"))
            {
                entityMessage.setInspector("АВТОРЕГИСТРАЦИЯ");
            }
            LOGGER.info("interface CheckIfItIsRegistrationMail-конец интерфейс-> " + LocalDateTime.now()
                    + entityMessage);
            return entityMessage;
        }
    }
}
