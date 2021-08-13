package com.vinnikov.inbox.ru.pandabot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Category;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppMsgToDiscordBot implements Runnable,AutoCloseable
{
//    public static void main(String[] args) throws LoginException, InterruptedException { }
    private String meToBot;

    public AppMsgToDiscordBot(String meToBot) {
        this.meToBot = meToBot;
    }

    @Override
    public void run()
    {
        try
        {
            JDABuilder jdaBuilder = JDABuilder.createDefault("1");
            JDA jda = jdaBuilder.build();
            jda.awaitReady().getCategories().get(1).getTextChannels().get(0) //категория 1 - это категории
                    .sendMessage(meToBot) //("1.0=testGr-1/0")            // (ТЕКСТОВЫЙ КАНАЛ) по порядку в дискорде, а
                    .timeout(3, TimeUnit.SECONDS).submit();    // канал - это по порядку в дискорде тема внутри
        } catch (LoginException | InterruptedException e) {      // текстового канала
            e.printStackTrace();
        }


//        JDABuilder jdaBuilder = JDABuilder.createDefault("ODU5NTMyMjIxOTY5ODU4NTcw.YNuDww.VpwXIyoa1Ku9n4TmeYNBxHLC_i8");
//        JDA jda = jdaBuilder.build();
//        jda.awaitReady().getCategories().get(1).getTextChannels().get(0).sendMessage("testGr-1/0")
//                .timeout(3, TimeUnit.SECONDS).submit();
    }

    @Override
    public void close() throws Exception {

    }
}