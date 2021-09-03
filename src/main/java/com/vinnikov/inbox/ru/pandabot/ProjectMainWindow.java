package com.vinnikov.inbox.ru.pandabot;

import lombok.SneakyThrows;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;

public class ProjectMainWindow extends JFrame
{
    private Image img = new ImageIcon("infoPng.png").getImage();
    private ImageIcon imgIcon = new ImageIcon("rob1Png.png"); //.getImage();
    private final String helloMsgToDiscord = "@декларант ВНИМАНИЕ! ПандаБОТ перезапустился! Надо войти в ватсап-вэб по QR-коду!";

    public ProjectMainWindow()
    {
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
        startPandaBot();
    }

    private void startPandaBot() throws LoginException, InterruptedException
    {
        try
        {
            System.out.println("let's start it");
            // передаём для отправки в дискорд сообщение о перезапуске программы
            AppHelloMsgToDiscordBot bot = new AppHelloMsgToDiscordBot(helloMsgToDiscord);
            bot.run();
            //запускаем всё
            ChromeWhatsappThread chromeWhatsappThread = new ChromeWhatsappThread();
            chromeWhatsappThread.runWhatsapWeb();
            sleep(60_000);
            new WatchDirectory().run();
            //https://youtu.be/9S0-vDSf5Rw
            System.out.println("555");
        } catch (NoSuchElementException ns)
        {
            System.out.println("----надо войти в ватсап-вэб! NoSuchElementException: " + ns);
        } catch (WebDriverException wd)
        {
            System.out.println("----надо войти в ватсап-вэб! WebDriverException: " + wd);
        } finally {
            System.out.println("aaa");
            new WatchDirectory().run();
            System.out.println("bbb");
        }
    }
}