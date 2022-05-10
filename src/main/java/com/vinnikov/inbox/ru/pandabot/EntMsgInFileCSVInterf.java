package com.vinnikov.inbox.ru.pandabot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

//@FunctionalInterface
public interface EntMsgInFileCSVInterf
{
    public default void saveInFile(EntityMessage entityMessage)
    {
        LOGGER.info("interface EntMsgInFileCSVInterf-начать интерфейс-> " + LocalDateTime.now());
        String lineForCSV = entityMessage.getComment() + ";" + entityMessage.getCompanyName() + ";" +
                entityMessage.getNumberDT() + ";" + entityMessage.getDolKgRbn() + ";" + entityMessage.getStatusDT()
                + ";" + entityMessage.getTransportNumber() + ";" + entityMessage.getInspector() + "\n";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter
                    ("D:\\grIdea\\pandabot\\source.csv",true));
            sleep(200);
            writer.write(lineForCSV);
            sleep(200);
            writer.flush();
            sleep(1_000);
            writer.close();
            sleep(200);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("interface EntMsgInFileCSVInterf--catch-> " + LocalDateTime.now() + "\n" + e);
        }
    }
}
