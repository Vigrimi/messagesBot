package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Category;
import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class AppMsgToDiscordBot implements Runnable //,AutoCloseable
{
    private String meToBot;

    public AppMsgToDiscordBot(String meToBot) {
        this.meToBot = meToBot;
    }

    @Override
    public void run()
    {
        try
        {
            JDABuilder jdaBuilder = JDABuilder.createDefault("");
            JDA jda = jdaBuilder.build();
            jda.awaitReady().getCategories().get(2).getTextChannels().get(4) //категория 1 - это категории
                    .sendMessage(meToBot) //("1.0=testGr-1/0")            // (ТЕКСТОВЫЙ КАНАЛ) по порядку в дискорде, а
                    .timeout(3, TimeUnit.SECONDS).queue(); //.submit();    // канал - это по порядку в дискорде тема внутри
            sleep(1_500);
            jda.shutdown(); // попробовать, будет ли работать??? типа аналог закрыть поток???
        } catch (LoginException | InterruptedException e) {      // текстового канала
            LOGGER.info("---AppMsgToDiscordBot catch-> " + LocalDateTime.now());
        }
    }
}