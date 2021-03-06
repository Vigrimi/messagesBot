package com.vinnikov.inbox.ru.pandabot;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class ProjectMainWindow extends JFrame
{
    private Image img = new ImageIcon("D:\\grIdea\\pandabot\\infoPng.png").getImage();
    private ImageIcon imgIcon = new ImageIcon("D:\\grIdea\\pandabot\\rob1Png.png"); //.getImage();
    private final String helloMsgToDiscord =
            "<@&785778541060292628> ВНИМАНИЕ! ПандаБОТ перезапустился! Надо войти в ватсап-вэб по QR-коду!";

    public ProjectMainWindow()
    {
        LOGGER.info("--------------------ProjectMainWindow 1 -> " + LocalDateTime.now());
        setTitle("PandaBot");
        setSize(400,400); //2 метод. Размеры окна

        setResizable(false); //false чтобы нельзя было бы поменять размеры рамки, true -можно
        setLocationRelativeTo(null); //создаёт его посередине экрана
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Просим программу закрыться при закрытии фрейма
        setIconImage(imgIcon.getImage()); // лого сверху слева окна
    }

    @SneakyThrows
    public void paint(Graphics g)
    { // вставил инфокартинку
        g.drawImage(img, 0, 0,/*frame.getWidth()*/ 400, /*frame.getHeight()*/ 360, null);
        sleep(3_000);
        LOGGER.info("--------------------ProjectMainWindow 2 -> " + LocalDateTime.now());
        startPandaBot();
    }

    private void startPandaBot() throws LoginException, InterruptedException
    {
        try
        {
            LOGGER.info("-----ProjectMainWindow let's start it -> " + LocalDateTime.now());
            // передаём для отправки в дискорд сообщение о перезапуске программы
            AppMsgToDiscordBot bot = new AppMsgToDiscordBot(helloMsgToDiscord);
            bot.run();
            //запускаем всё
            ChromeWhatsappThread chromeWhatsappThread = new ChromeWhatsappThread();
            chromeWhatsappThread.runWhatsapWeb();
            sleep(60_000);
            new WatchDirectory().run();
            //https://youtu.be/9S0-vDSf5Rw
            System.out.println("555");
        } catch (InsufficientPermissionException ie)
        {
            ChromeWhatsappThread chromeWhatsappThread = new ChromeWhatsappThread();
            chromeWhatsappThread.runWhatsapWeb();
            sleep(6_000);
            ChromeWhatsappThread.sendInWatsapWeb("БОТ в дискорде не работает. Возможно добавлен новый канал " +
                    "и цифровой индекс канала ДТ-СТАТУСа съехал.");
            LOGGER.error("---EditTextsFmEmail---!!-catch 169-> " + LocalDateTime.now() + "\n" + ie);
        }
        catch (NoSuchElementException ns)
        {
            LOGGER.error("----надо войти в ватсап-вэб! NoSuchElementException -> " + LocalDateTime.now() + "\n" + ns);
        } catch (WebDriverException wd)
        {
            LOGGER.error("-----надо войти в ватсап-вэб! WebDriverExceptio -> " + LocalDateTime.now() + "\n" + wd);
        } finally {
            LOGGER.info("--aaa---ProjectMainWindow finally rebooting new WatchDirectory().run() -> " + LocalDateTime.now());
            new WatchDirectory().run();
            LOGGER.info("--bbb---ProjectMainWindow finally rebooting new WatchDirectory().run() -> " + LocalDateTime.now());
        }
    }
}