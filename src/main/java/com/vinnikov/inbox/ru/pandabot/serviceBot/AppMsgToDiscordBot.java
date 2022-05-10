package com.vinnikov.inbox.ru.pandabot.serviceBot;

import com.vinnikov.inbox.ru.pandabot.interfaces.MyDataHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class AppMsgToDiscordBot implements Runnable, MyDataHandler { //,AutoCloseable
    private final String meToBot;
    private final String discordTokenFile = "./secrets/discord_token.txt"; // grisha
                                        // = "./secrets/discord_token_panda.txt"; // panda

    public AppMsgToDiscordBot(String meToBot) {
        this.meToBot = meToBot;
    }

    @Override
    public void run(){
        String discordToken = getDataFromFile(discordTokenFile);
        try{
            LOGGER.info("-----AppMsgToDiscordBot start it -> " + LocalDateTime.now());
            // гр дискорд
            JDABuilder jdaBuilder = JDABuilder.createDefault(discordToken);
            JDA jda = jdaBuilder.build();
            jda.awaitReady().getCategories().get(1).getTextChannels().get(0) //категория 1 - это категории
                    .sendMessage(meToBot) //("1.0=testGr-1/0")            // (ТЕКСТОВЫЙ КАНАЛ) по порядку в дискорде, а
                    .timeout(3, TimeUnit.SECONDS).queue(); //.submit();    // канал - это по порядку в дискорде тема внутри

            // panda
            /*
            JDABuilder jdaBuilder = JDABuilder.createDefault(discordTokenFile);
            JDA jda = jdaBuilder.build();
            jda.awaitReady().getCategories()
                    .get(5) // 2 работало до 8 окт; 5й длина 6
                    .getTextChannels() //категория 1 - это категории
                    .get(5) // 5 работало до 8 окт, 4 работало до 6 окт, и потом стало 5. пробовал и не работает: 6,5,4,3,2,8,7,1
                    .sendMessage(meToBot) //("1.0=testGr-1/0")            // (ТЕКСТОВЫЙ КАНАЛ) по порядку в дискорде, а
                    .timeout(3, TimeUnit.SECONDS).queue(); //.submit();    // канал - это по порядку в дискорде тема внутри*/

            sleep(1_500);
            jda.shutdown(); // аналог закрыть поток
            LOGGER.info("-----AppMsgToDiscordBot finish it -> " + LocalDateTime.now());
        } catch (LoginException | InterruptedException e) {
            LOGGER.error("---AppMsgToDiscordBot catch-> " + LocalDateTime.now());
        }
    }
}