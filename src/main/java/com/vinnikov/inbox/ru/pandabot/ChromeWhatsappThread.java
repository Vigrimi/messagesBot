package com.vinnikov.inbox.ru.pandabot;

import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class ChromeWhatsappThread
{
    // нужно установить chromedriver на Windows OS https://sites.google.com/a/chromium.org/chromedriver/downloads
    // у меня он лежит по адресу C:\Program Files (x86)\Google\Chrome\Application

    // xpath //*[@id="main"]/footer/div[1]/div[2]/div/div[1]/div/div[2]

    public static WebDriver driver;
    public static WebElement loginField;
    public static WebElement loginField1;
    private static final String nameOfGroupInWhatsApp = "Статусы";

    public ChromeWhatsappThread() {
    }

    // @BeforeClass
   // @Override
    public void runWhatsapWeb()
    {
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver"
                , "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe" ); //ConfProperties.getProperty("chromedriver"));
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
        System.out.println("15 " + LocalDateTime.now());
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        System.out.println("16 " + LocalDateTime.now());
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
        // встать в поле поиск
        searchBox.click(); // и кликнуть в поле поиска
        searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер // "FmMailToWhatsApp"
        /*WebElement*/ loginField = driver.findElement(By
                .xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[1]/div/div[2]"));
        loginField.sendKeys("Автоматическое сообщение. Программа ПандаБОТ работает. " +
                "Ватсап успешно подключён.\r\t" + Keys.RETURN);
        //loginPage.inputLogin("testFmComp\r\t");
        System.out.println("17 " + LocalDateTime.now());
    }

    @SneakyThrows
    public static void sendInWatsapWeb (String message)
    {
        try
        {
            // вэбэлемент уведомление Зарядить смартфон //*[@id="side"]/span/div/div/div[2]
            // или //*[@id="side"]/span/div/div/div[2]/div[1] + //*[@id="side"]/span/div/div/div[2]/div[2]
            WebElement lowBattery1 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]"));
            WebElement lowBattery2 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]/div[1]"));
            WebElement lowBattery3 = driver.findElement(By.xpath("//*[@id=\"side\"]/span/div/div/div[2]/div[2]"));
            String chargeTheBattery = "ВНИМАНИЕ!!! У смартфона " + lowBattery2.getText().toLowerCase() + "! "
                    + lowBattery3.getText() + " на компьютере!" ;
//            System.out.println("lowBattery1 " + lowBattery1.getText());
//            System.out.println("lowBattery2 " + lowBattery2.getText());
//            System.out.println("lowBattery3 " + lowBattery3.getText());

            // встать вверх на название группы
            WebElement searchBox1 = driver.findElement(By.xpath("//*[@id=\"main\"]/header/div[2]/div[1]/div/span"));
            String nameOfEqualAbonent = searchBox1.getText(); // взять название текущей группы
            if(nameOfEqualAbonent.contains(nameOfGroupInWhatsApp)) //"FmMailToWhatsApp"
            {
                // встать в поле для отправки текста и отправить нужное сообщение
                loginField1 = driver.findElement(By
                        .xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[1]/div/div[2]"));
                loginField1.sendKeys(message + Keys.RETURN);

                // если аккум разряжен - отправить уведомление
                if(lowBattery1.getText().contains("разряжен"))
                {
                    loginField1.sendKeys(chargeTheBattery + Keys.RETURN);
                }
            } else
            {
                WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
                // встать в поле поиск
                searchBox.click(); // и кликнуть в поле поиска
                searchBox.sendKeys(nameOfGroupInWhatsApp + Keys.RETURN); // ввести текст в поиске и энтер //"FmMailToWhatsApp"
                // встать в поле для отправки текста и отправить нужное сообщение
                loginField1 = driver.findElement(By
                        .xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div/div[1]/div/div[2]"));
                loginField1.sendKeys(message + Keys.RETURN);

                // если аккум разряжен - отправить уведомление
                if(lowBattery1.getText().contains("разряжен"))
                {
                    loginField1.sendKeys(chargeTheBattery + Keys.RETURN);
                }
            }
        } catch (NoSuchWindowException nw)
        {
            System.out.println("----хром ватсапвэб обвалился: " + nw);
            driver.navigate().refresh(); // обновить вкладку
            System.out.println("--------11 обновил вкладку, жду 10 сек");
            sleep(10_000);
            driver.navigate().refresh(); // обновить вкладку
            System.out.println("------12 обновил вкладку ещё раз, жду 11 сек, запускаю отправку снова");
            sleep(11_000);
            sendInWatsapWeb(message);
        }
    }
}
