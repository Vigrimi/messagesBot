package com.vinnikov.inbox.ru.pandabot.serviceBot;

import com.vinnikov.inbox.ru.pandabot.enums.EnumTextConstant;
import com.vinnikov.inbox.ru.pandabot.interfaces.MyDataHandler;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class ChromeWhatsappThread{
    private static WebDriver driver;
    private static WebElement sendMsgField;
    private static WebElement searchBox;
    private static final String nameOfGroupInWhatsApp = "SBB_Tickets"; //"FmMailToWhatsApp"; //"Статусы";
    private static final String xpathPoleDlyaOtpravkiTeksta =
            "//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[2]"; // в хроме 96+
    private static final String xpathSearchField = "//*[@id=\"side\"]/div[1]/div/label/div/div[2]";

    public ChromeWhatsappThread() {
    }

    public void runWhatsapWeb() throws InterruptedException {
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        //создание экземпляра драйвера
        driver = new ChromeDriver();

        //окно разворачивается на полный экран
        driver.manage().window().maximize();

        //задержка на выполнение = 10 сек.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //получение ссылки на страницу входа из файла настроек
        driver.get( "https://web.whatsapp.com/" );
        LOGGER.info("---runWhatsapWeb 15 -> " + LocalDateTime.now());

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        LOGGER.info("---runWhatsapWeb 16 -> " + LocalDateTime.now());
        searchBox = driver.findElement(By.xpath(xpathSearchField));

        // встать в поле поиск
        searchBox.click(); // и кликнуть в поле поиска
        sleep(700);
        searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер
        sleep(1_700);
        // встать в поле отправки сообщения
        sendMsgField = driver.findElement(By.xpath(xpathPoleDlyaOtpravkiTeksta));
        sleep(1_700);
        sendMsgField.sendKeys("Автоматическое сообщение. Программа ПандаБОТ работает. " +
                "Ватсап успешно подключён." + Keys.RETURN);
        LOGGER.info("---runWhatsapWeb 17 -> " + LocalDateTime.now());
    }

    @SneakyThrows
    public static void sendInWatsapWeb (String message){
        try{
            // вэбэлемент уведомление Зарядить смартфон
            WebElement lowBattery1 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]"));
            WebElement lowBattery2 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]/div[1]"));
            WebElement lowBattery3 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]/div[2]"));
            String chargeTheBattery = "ВНИМАНИЕ!!! У смартфона " + lowBattery2.getText().toLowerCase() + "! "
                    + lowBattery3.getText() + " на компьютере!" ;
            LOGGER.info("---runWhatsapWeb -914 lowBattery1-> " + LocalDateTime.now() + lowBattery1.getText());

            // встать вверх на название группы
            WebElement nameOfGroup = driver.findElement(By.xpath("//*[@id=\"main\"]/header/div[2]/div[1]/div/span"));
            String nameOfEqualAbonent = nameOfGroup.getText(); // взять название текущей группы
            // проверить, находимся ли мы в нужной группе
            if(!nameOfEqualAbonent.contains(nameOfGroupInWhatsApp)){
                // встать в поле поиск
                searchBox.click(); // и кликнуть в поле поиска
                searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер
            }
            // встать в поле для отправки текста и отправить нужное сообщение
            sendMsgField = driver.findElement(By.xpath(xpathPoleDlyaOtpravkiTeksta));
            sleep(1_000);
            sendMsgField.sendKeys(message + Keys.RETURN);
            // если аккум разряжен - отправить уведомление
            if(lowBattery1.getText().contains("разряжен")){
                sendMsgField.sendKeys(chargeTheBattery + Keys.RETURN);
            }
        } catch (NoSuchWindowException | NoSuchElementException nw){
            LOGGER.error("---runWhatsapWeb--хром ватсапвэб обвалился-> " + LocalDateTime.now() + nw);
            driver.navigate().refresh(); // обновить вкладку
            LOGGER.info("---runWhatsapWeb---11 обновил вкладку, жду 10 сек-> " + LocalDateTime.now());
            sleep(10_000);
            driver.navigate().refresh(); // обновить вкладку
            LOGGER.info("---runWhatsapWeb---12 обновил вкладку ещё раз, жду 11 сек, запускаю отправку снова-> "
                    + LocalDateTime.now());
            sleep(11_000);

            // встать в поле поиск
            searchBox.click(); // и кликнуть в поле поиска
            sleep(1_000);
            searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер
            LOGGER.info("---runWhatsapWeb---913 в поиске выбрал нужную группу и снова запускаю " +
                    "sendInWatsapWeb(message)-> " + LocalDateTime.now());
            sendInWatsapWeb(message);
        }
    }

    public static void sendImgWatsapWeb (int qtyRegisteredGTD){
        String imageFilePathName =
                "D:\\ITMO PandaBot\\pandabot\\picForQtyRegGTD\\qty_reg_gtd_" + qtyRegisteredGTD + ".png";
        try {
            // надо открыть гугл эксель, вставить в строку формул путь файла, скопировать его в буфер для
            // вставки ниже в ватсапе
            // буфер - мой эксель
            LOGGER.info("---runWhatsapWeb---175 sendImg -- открыть буфер эксель для копрования пути к файлу-> " +
                    LocalDateTime.now());
            WebDriver driverBuffer = new ChromeDriver();
            String link = MyDataHandler.getDataFromFileStatic(EnumTextConstant.FILENAME_BUFFER_GOOGLE_EXCEL.getText());
            driverBuffer.get(link);
            Thread.sleep(4_100);  // Let the user actually see something!
            // идём в буфер вставить ссылку в строку формул и получить её в переменную
            WebElement strokaFormulBuffer = driverBuffer
                    .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
            strokaFormulBuffer.click();
            strokaFormulBuffer.sendKeys(Keys.CONTROL +"a");
            Thread.sleep(1_000);  // Let the user actually see something!
            // вставить путь к файлу
            strokaFormulBuffer.sendKeys(imageFilePathName);
            Thread.sleep(1_000);  // Let the user actually see something!
            strokaFormulBuffer.sendKeys(Keys.CONTROL +"a");
            Thread.sleep(1_000);  // Let the user actually see something!
            // скопировать путь к файлу
            strokaFormulBuffer.sendKeys(Keys.CONTROL +"c");
            Thread.sleep(1_000);  // Let the user actually see something!

            driverBuffer.quit(); // закрыть окно

            // переходим в ватсап
            // нажать скрепку
            LOGGER.info("---runWhatsapWeb---169 sendImg -- нажать скрепку-> " + LocalDateTime.now());
            String attFileSkrepkaXpath = "//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[1]/div[2]/div/div";
            WebElement attFileBox = driver.findElement(By.xpath(attFileSkrepkaXpath));
            attFileBox.click();

            // во восплывшем меню нажать на файл
            LOGGER.info("---runWhatsapWeb---175 sendImg -- нажать прикрепить файл-> " + LocalDateTime.now());
            String attFileImgXpath = "//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[1]/div[2]/div/span/div[1]/div/ul/li[1]/button";
            WebElement attFileImgBox = driver.findElement(By.xpath(attFileImgXpath));
            attFileImgBox.click();

            // вставить путь к файлу из буфера памяти
            LOGGER.info("---runWhatsapWeb---210 sendImg -- Robot robott1 = null-> " + LocalDateTime.now());
            Robot robott1 = null;
            try {
                robott1 = new Robot();
                Thread.sleep(3_500);  // Let the user actually see something!
                robott1.keyPress(KeyEvent.VK_CONTROL);
                Thread.sleep(200);  // Let the user actually see something!
                robott1.keyPress(KeyEvent.VK_V);
                Thread.sleep(900);  // Let the user actually see something!
                robott1.keyRelease(KeyEvent.VK_V);
                Thread.sleep(200);  // Let the user actually see something!
                robott1.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(400);  // Let the user actually see something!

                robott1.keyPress(KeyEvent.VK_ENTER);
                Thread.sleep(700);  // Let the user actually see something!
                robott1.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(1100);  // Let the user actually see something!
            } catch (AWTException | NullPointerException e) {
                LOGGER.error("---runWhatsapWeb--- sendImg -- 226 catch-> " + LocalDateTime.now()
                        + "\n" + e);
            }

            // встать в поле текста в картинке перед отправкой картинки
            LOGGER.info("---runWhatsapWeb---243 sendImg --встать в поле текста в картинке перед отправкой картинки-> " + LocalDateTime.now());
            String poleTekstaVKartinkeXpath =
            "//*[@id=\"app\"]/div[1]/div[1]/div[2]/div[2]/span/div[1]/span/div[1]/div/div[2]/div/div[1]/div[3]/div/div/div[2]/div[1]/div[2]";
            WebElement poleTekstaVKartinkeBox = driver.findElement(By.xpath(poleTekstaVKartinkeXpath));
            Thread.sleep(1_000);  // Let the user actually see something!
            poleTekstaVKartinkeBox.sendKeys(qtyRegisteredGTD + "" + Keys.RETURN);
            Thread.sleep(500);  // Let the user actually see something!

        } catch (NoSuchWindowException | NoSuchElementException | InterruptedException
                | ElementNotInteractableException nw){
            nw.printStackTrace();
        }
    }
}
