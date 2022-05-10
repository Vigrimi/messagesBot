package com.vinnikov.inbox.ru.pandabot;

import javax.security.auth.login.LoginException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalDateTime;

/**
 * 		5. в дискорде создать бота на нужном сервере в нужном канале, взять его токен и перейти к следующему пункту
 * 		6. в корне проекта, в директории secrets создать файл discord_token.txt и в него сохранить токен от бота из
 * 	дискорда.
 * 		10. в корне проекта, в директории secrets создать файлы email_login_my.txt и email_password_my.txt и внести
 * 	соответственно логин и пароль от яндекс почты
 * 		20. в классах AppMsgToDiscordBot и BCheckDoesItWorkMsgToDiscord правильно прописать номера текстовых каналов
 * 		22. скачать chromedriver https://sites.google.com/chromium.org/driver/downloads и скачать файл
 * 	chromedriver.exe и сохранить его в корень проекта, проверить путь к файлу в классах: ChromeWhatsappThread
 * 		30. в классе ChromeWhatsappThread прописать в поле nameOfGroupInWhatsApp название нужной группы в ватсапе
 * 		35. в гугл-документах создать лист Эксель, сделать его доступным для редактирования по ссылке, скопировать
 * 	ссылку на этот Эксель, в корне проекта, в директории secrets создать файл google_excel.txt и в него сохранить
 * 	скопированную ранее ссылку
 * 		40. на компе в текущей папке создать файл countOld.txt и внести текущее кол-во писем в папке Inbox
 *		50. на компе найти саму папку с Аутлуком и рабочий файл аккаунта и прописать их в EnumTextConstant в
 *	FILE_PATH_OUTLOOK_GR и FILENAME_OUTLOOK_GR
 *
 */

@SpringBootApplication
public class PandabotApplication{
	public static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PandabotApplication.class);

	public static void main(String[] args) throws LoginException, InterruptedException{
		LOGGER.info("==> BOT стартовал -> " + LocalDateTime.now());
//		LOGGER.info("==> спринг стартовал -> " + LocalDateTime.now());
//		SpringApplication.run(PandabotApplication.class, args);
//		System.setProperty("java.awt.headless", "false");
//		LOGGER.info("==> спринг закончил -> " + LocalDateTime.now());
		LOGGER.info("==> ProjectMainWindow стартовал -> " + LocalDateTime.now());
		ProjectMainWindow window = new ProjectMainWindow();
		window.setVisible(true); //все настройки рамки надо писать до того, как сделаете её видимой
		LOGGER.info("==> ProjectMainWindow закончил -> " + LocalDateTime.now());
	}
}
