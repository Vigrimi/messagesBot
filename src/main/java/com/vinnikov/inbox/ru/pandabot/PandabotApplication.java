package com.vinnikov.inbox.ru.pandabot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.security.auth.login.LoginException;

/*
 * 		+10. нужен логин+пароль от мэйла яндекс, куда приходят мэйлы из таможенной программы - внести
 * 		в public class WatchEmailRunnable - vasilii@pandatrans.ru - tvcft665
 * 		+20. на компе в текущей папке создать файл countOld.txt и внести текущее кол-во писем в папке Inbox
 * 		+22. скачать и установить хром веб драйвер и прописать в двух классах
 * 		+25. в папке с аутлуком найти папку и файл отвечающий за отслеживанием именно этого аккаунта и указать путь
 * 		и название файла в public class WatchDirectory
 * 30. в дискорде создать бота на нужном сервере в нужном канале, взять его токен и внести в public
 * class AppMsgToDiscordBot
 * 40. в ватсп-вэб сканить QR-код
 * 45. в public class ChromeWhatsappThread прописать название нужной группы в ватсапе
 *
 *
 */

@SpringBootApplication
public class PandabotApplication
{
	public static void main(String[] args) throws LoginException, InterruptedException
	{
		ProjectMainWindow window = new ProjectMainWindow();
		window.setVisible(true); //все настройки рамки надо писать до того, как вы сделаете её видимой
	}

}
