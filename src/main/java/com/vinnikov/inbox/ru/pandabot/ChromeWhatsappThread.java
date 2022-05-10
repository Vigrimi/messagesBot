package com.vinnikov.inbox.ru.pandabot;

import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;
import static java.lang.Thread.sleep;

public class ChromeWhatsappThread
{
    // нужно установить chromedriver на Windows OS https://sites.google.com/chromium.org/driver/downloads
    // у меня он лежит по адресу C:\Program Files (x86)\Google\Chrome\Application

    // xpath //*[@id="main"]/footer/div[1]/div[2]/div/div[1]/div/div[2]

    public static WebDriver driver;
    public static WebElement loginField;
    public static WebElement loginField1;
    private static final String nameOfGroupInWhatsApp = "Статусы";
    private static final String xpathPoleDlyaOtpravkiTeksta =
            //"//*[@id=\"main\"]/footer/div[1]/div/div/div[2]/div[1]/div/div[2]"; // в хроме 93
            "//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[2]"; // в хроме 94

    public ChromeWhatsappThread() {}

    // @BeforeClass
   // @Override
    public void runWhatsapWeb()
    {
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver",
        //        "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe" // при запуске jar похоже нет
                //        доступа к диску С:
                "D:\\grIdea\\pandabot\\chromedriver.exe"
        ); //ConfProperties.getProperty("chromedriver"));
        //создание экземпляра драйвера
        /*WebDriver*/ driver = new ChromeDriver();

        //окно разворачивается на полный экран
        driver.manage().window().maximize();
        //driver.get( "https://web.whatsapp.com/" );

        // пробую перебрать все открытые вкладки и выбрать первую (индекс 0) где открыт ватсап-вэб
//        ArrayList<String> availableWindows = //new ArrayList<String>(driver.getWindowHandles());
//                                            new ArrayList<>();
//        availableWindows.add("https://web.whatsapp.com/");
//        availableWindows.add("https://kvartplata.info/welcome/");
//        System.out.println("11-> " + availableWindows + " ** " + availableWindows.get(0));

        //задержка на выполнение теста = 10 сек.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //получение ссылки на страницу входа из файла настроек
        driver.get( "https://web.whatsapp.com/" ); // сюда сайт написать ConfProperties.getProperty("loginpage"));
        //driver.switchTo().window(availableWindows.get(1));
        LOGGER.info("---runWhatsapWeb 15 -> " + LocalDateTime.now());

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        LOGGER.info("---runWhatsapWeb 16 -> " + LocalDateTime.now());
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));

        // встать в поле поиск
        searchBox.click(); // и кликнуть в поле поиска
        searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер // "FmMailToWhatsApp"

        /*WebElement*/ loginField = driver.findElement(By
                .xpath( xpathPoleDlyaOtpravkiTeksta
                        //"//*[@id=\"main\"]/footer/div[1]/div/div/div[2]/div[1]/div/div[2]" // в хроме 93
                        //"//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[1]/div/div[2]" // в хроме 92
                ));
        loginField.sendKeys("Автоматическое сообщение. Программа ПандаБОТ работает. " +
                "Ватсап успешно подключён.\r\t" + Keys.RETURN);
        //loginPage.inputLogin("testFmComp\r\t");
        LOGGER.info("---runWhatsapWeb 17 -> " + LocalDateTime.now());
    }

    @SneakyThrows
    public static void sendInWatsapWeb (String message)
    {
        try
        {
/*System.out.println("--------911 обновил вкладку, жду 10 сек");
            driver.navigate().refresh(); // обновить вкладку и жду 10 секунд
            sleep(10_000);
System.out.println("--------912 обновилась вкладка");*/
            // встать в поле поиск
            WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
            /*searchBox.click(); // и кликнуть в поле поиска
            sleep(1_000);
            searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер
System.out.println("--------913 в поиске выбрал нужную группу");*/

            // вэбэлемент уведомление Зарядить смартфон //*[@id="side"]/span/div/div/div[2]
            // или //*[@id="side"]/span/div/div/div[2]/div[1] + //*[@id="side"]/span/div/div/div[2]/div[2]
            WebElement lowBattery1 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]"));
            WebElement lowBattery2 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]/div[1]"));
            WebElement lowBattery3 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]/div[2]"));
            String chargeTheBattery = "ВНИМАНИЕ!!! У смартфона " + lowBattery2.getText().toLowerCase() + "! "
                    + lowBattery3.getText() + " на компьютере!" ;
            LOGGER.info("---runWhatsapWeb -914 lowBattery1-> " + LocalDateTime.now() + lowBattery1.getText());

//            System.out.println("lowBattery2 " + lowBattery2.getText());
//            System.out.println("lowBattery3 " + lowBattery3.getText());

            // встать вверх на название группы
            WebElement searchBox1 = driver.findElement(By.xpath("//*[@id=\"main\"]/header/div[2]/div[1]/div/span"));
            String nameOfEqualAbonent = searchBox1.getText(); // взять название текущей группы
            if(nameOfEqualAbonent.contains(nameOfGroupInWhatsApp)) //"FmMailToWhatsApp"
            {
                // встать в поле для отправки текста и отправить нужное сообщение
                loginField1 = driver.findElement(By
                        .xpath(xpathPoleDlyaOtpravkiTeksta));
                loginField1.sendKeys(message + Keys.RETURN);

                // если аккум разряжен - отправить уведомление
                if(lowBattery1.getText().contains("разряжен"))
                {
                    loginField1.sendKeys(chargeTheBattery + Keys.RETURN);
                }
            } else
            {
                //WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
                // встать в поле поиск
                searchBox.click(); // и кликнуть в поле поиска
                searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер //"FmMailToWhatsApp"
                // встать в поле для отправки текста и отправить нужное сообщение
                loginField1 = driver.findElement(By
                        .xpath(xpathPoleDlyaOtpravkiTeksta));
                loginField1.sendKeys(message + Keys.RETURN);

                // если аккум разряжен - отправить уведомление
                if(lowBattery1.getText().contains("разряжен"))
                {
                    loginField1.sendKeys(chargeTheBattery + Keys.RETURN);
                }
            }
        } catch (NoSuchWindowException | NoSuchElementException nw)
        {
            LOGGER.error("---runWhatsapWeb--хром ватсапвэб обвалился-> " + LocalDateTime.now() + nw);
            driver.navigate().refresh(); // обновить вкладку
            LOGGER.info("---runWhatsapWeb---11 обновил вкладку, жду 10 сек-> " + LocalDateTime.now());
            sleep(10_000);
            driver.navigate().refresh(); // обновить вкладку
            LOGGER.info("---runWhatsapWeb---12 обновил вкладку ещё раз, жду 11 сек, запускаю отправку снова-> "
                    + LocalDateTime.now());
            sleep(11_000);

            // встать в поле поиск
            WebElement searchBox2 = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
            searchBox2.click(); // и кликнуть в поле поиска
            sleep(1_000);
            searchBox2.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер
            LOGGER.info("---runWhatsapWeb---913 в поиске выбрал нужную группу и снова запускаю " +
                    "sendInWatsapWeb(message)-> " + LocalDateTime.now());
            sendInWatsapWeb(message);
        }
    }
}
