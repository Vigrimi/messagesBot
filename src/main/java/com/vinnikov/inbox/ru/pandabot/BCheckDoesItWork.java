package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class BCheckDoesItWork  implements Runnable
{
    private String mesToBot;

    public BCheckDoesItWork(String mesToBot) {
        this.mesToBot = mesToBot;
    }

    @Override
    public void run()
    {
        try
        {
            JDABuilder jdaBuilder = JDABuilder.createDefault("ODU5NTMyMjIxOTY5ODU4NTcw.YNuDww.OjR3RxbtsRcch-eUPg-Yh5GHDu8");
            JDA jda = jdaBuilder.build();
            jda.awaitReady().getCategories().get(1).getTextChannels().get(0) //категория 1 - это категории
                    .sendMessage(mesToBot) //("1.0=testGr-1/0")            // (ТЕКСТОВЫЙ КАНАЛ) по порядку в дискорде, а
                    .timeout(3, TimeUnit.SECONDS).queue(); //.submit();    // канал - это по порядку в дискорде тема внутри
            sleep(1_500);
            jda.shutdown(); // попробовать, будет ли работать??? типа аналог закрыть поток???
        } catch (LoginException | InterruptedException e) {      // текстового канала
            LOGGER.info("---BCheckDoesItWork catch-> " + LocalDateTime.now());
        }
    }
}
