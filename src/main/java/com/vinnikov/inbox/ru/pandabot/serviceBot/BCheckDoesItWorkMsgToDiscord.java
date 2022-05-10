package com.vinnikov.inbox.ru.pandabot.serviceBot;

import com.vinnikov.inbox.ru.pandabot.interfaces.MyDataHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class BCheckDoesItWorkMsgToDiscord implements Runnable, MyDataHandler {
    private String mesToBot;
    private final String discordTokenFile = "./secrets/discord_token.txt"; // grisha
                                        // = "./secrets/discord_token_panda.txt"; // panda

    public BCheckDoesItWorkMsgToDiscord(String mesToBot) {
        this.mesToBot = mesToBot;
    }

    @Override
    public void run()
    {
        String discordToken = getDataFromFile(discordTokenFile);
        try
        { // гр дискорд
            LOGGER.info("-----BCheckDoesItWorkMsgToDiscord start it -> " + LocalDateTime.now());
            JDABuilder jdaBuilder = JDABuilder.createDefault(discordToken);
            JDA jda = jdaBuilder.build();
            jda.awaitReady().getCategories().get(1).getTextChannels().get(0) //категория 1 - это категории
                    .sendMessage(mesToBot) //("1.0=testGr-1/0")            // (ТЕКСТОВЫЙ КАНАЛ) по порядку в дискорде, а
                    .timeout(3, TimeUnit.SECONDS).queue(); //.submit();    // канал - это по порядку в дискорде тема внутри
            sleep(1_500);
            jda.shutdown(); // аналог закрыть поток
            LOGGER.info("-----BCheckDoesItWorkMsgToDiscord finish it -> " + LocalDateTime.now());
        } catch (LoginException | InterruptedException e) {
            LOGGER.info("---BCheckDoesItWork catch-> " + LocalDateTime.now());
        }
    }
}
