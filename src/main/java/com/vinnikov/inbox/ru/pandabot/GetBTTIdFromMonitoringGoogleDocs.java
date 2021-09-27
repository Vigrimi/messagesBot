package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class GetBTTIdFromMonitoringGoogleDocs
{
    private static int coordinataYacheyki;

    public static String getBTTIdFromMonitoringGoogleDocs(String msgToDiscord1) throws InterruptedException
    {
        String idFromYacheika = "";
        String idFromYacheika1 = "";
        String findBar1of1String = "";
        String nameOfGood = "";
        String nameOfCompany = "";
        String yacheyka = "";
        int indexIdInArr = 0;
        int a0iz0 = 0;
        int to = 0;
        String[] words = msgToDiscord1.split(" ");
        //System.out.println("333 " + Arrays.toString(words));
        HashSet<String> foundIdFmMonitoringSet = new HashSet<>();
        HashSet<String> nameOfGoodsSet = new HashSet<>();
        ArrayList<String> foundInfoFmMonitoringList = new ArrayList<>();

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driverMonitoring = new ChromeDriver();
        driverMonitoring.manage().window().maximize();

// пандамониторинг
        driverMonitoring.get("https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing");
        Thread.sleep(14_000);  // Let the user actually see something!

        // перебираем мониторинг, вдруг есть одинаковые записи по контейнеру или фуре
        for (int i = 0; i < words.length; i++)
        {
            if (words[i].contains("онтейнеры") || (words[i].contains("Номер") && words[i+1].contains("ТС")) )
            {
                //System.out.println("99999999000 " + words[i]);
                for (int j = 1; j < words.length; j++)
                {
                    if (words[i].contains("Номер") && words[i+1].contains("ТС") ) j++;
                    int a = i + j;
                    String contNumber = "";
                    try {
                        contNumber = words[a].replaceAll(",","").replace(".","");
                    } catch (ArrayIndexOutOfBoundsException ae){
                        LOGGER.error("----getBTTIdFromMonit 60мистика, вышли из массива-> " + LocalDateTime.now() + ae);
                        break;
                    }

                    //System.out.println("99999999222 " + words[a]);
                    //System.out.println("99999999444 " + contNumber + " ** " + contNumber.length());
                    if(msgToDiscord1.contains("онтейнеры") && contNumber.length() != 11) break;
                    if(words[a].contains("нспектор") ) break;
                    else
                    {
                        boolean flag = true;

                        // открыть окно поиска для ввода номера контейнера
                        driverMonitoring.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"f");

                        // поле поиск //*[@id="docs-findbar-input"]/table/tbody/tr/td[1]/input
                        WebElement searchBox = driverMonitoring.findElement(By
                                .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[1]/input"));
                        Thread.sleep(800);  // Let the user actually see something!

                        //Thread.sleep(1_000);  // Let the user actually see something!
                        foundInfoFmMonitoringList.add(contNumber);
                        //пробую искать существующий номер контейнера
                        searchBox.sendKeys(contNumber + Keys.RETURN); // существующий

                        // ждём как отвиснет мониторинг при поиске
                        for (int kk = 0; kk < 15; kk++)
                        {
                            Thread.sleep(5_000);  // Let the user actually see something!
                            // в окне поиска взять 1из1    //*[@id="docs-findbar-input"]/table/tbody/tr/td[2]/span
                            WebElement findBar1of1 = driverMonitoring.findElement(By
                                    .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[2]/span"));
                            findBar1of1String = findBar1of1.getText();
                            LOGGER.info("----getBTTIdFromMonit--84 findBar1of1String-> " + LocalDateTime.now()
                                    + findBar1of1String);
                            if(!findBar1of1String.equalsIgnoreCase("0 из 0")) break;
                        }
                        // если всё же не нашлось ничего
                        if(findBar1of1String.equalsIgnoreCase("0 из 0"))
                        {
                            LOGGER.info("----getBTTIdFromMonit--90 findBar1of1String-> " + LocalDateTime.now()
                                    + findBar1of1String);
                            a0iz0++;
                            break;
                        }
                        Thread.sleep(300);  // Let the user actually see something!

                        // крестик в поле поиск //*[@id="docs-findbar-id"]/div/div/div[1]/div[2]/div/div/div
                        WebElement searchBox1 = driverMonitoring.findElement(By
                                .xpath("//*[@id=\"docs-findbar-id\"]/div/div/div[1]/div[2]/div/div/div/div"));
                        searchBox1.click();
                        Thread.sleep(500);  // Let the user actually see something!

                        nameOfGood = null;
                        nameOfCompany = null;
                        while (flag) // NoSuchSessionException ???
                        { // получаем айди и название товара
                            Robot robott = null;
                            try
                            {
                                for (int k1 = 0; k1 < 6; k1++)
                                {
                                    robott = new Robot();
                                    robott.keyPress(KeyEvent.VK_LEFT);
                                    Thread.sleep(200);
                                    robott.keyRelease(KeyEvent.VK_LEFT);
                                    Thread.sleep(200);
                                }

                                // получаю ссылку с номером ячейки
                                robott.keyPress(KeyEvent.VK_SHIFT);
                                robott.keyPress(KeyEvent.VK_F10);
                                Thread.sleep(900);  // Let the user actually see something!
                                robott.keyRelease(KeyEvent.VK_F10);
                                robott.keyRelease(KeyEvent.VK_SHIFT);
                                Thread.sleep(400);  // Let the user actually see something!
                                robott.keyPress(KeyEvent.VK_DOWN);
                                Thread.sleep(700);  // Let the user actually see something!
                                robott.keyRelease(KeyEvent.VK_DOWN);
                                Thread.sleep(100);  // Let the user actually see something!
                                robott.keyPress(KeyEvent.VK_ENTER);
                                Thread.sleep(700);  // Let the user actually see something!
                                robott.keyRelease(KeyEvent.VK_ENTER);
                                Thread.sleep(1100);  // Let the user actually see something!

                                //    тут
                                yacheyka = getBTTYacheykaFmBuffer();

                            } catch (AWTException | NullPointerException e) {
                                LOGGER.error("----getBTTIdFromMonit 147 catch-> " + LocalDateTime.now() + e);
                            } catch (NoSuchElementException | NoSuchSessionException ne) {
                                LOGGER.error("----getBTTIdFromMonit 149 catch-> " + LocalDateTime.now() + ne);
                            }

                            // если ячейка начинается с I - это название товара, если с С - это айди
                            if(yacheyka.startsWith("I"))
                            {
                                // идём в мониторинг получить название товара
                                WebElement strokaFormulMonitor = driverMonitoring
                                        .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                nameOfGood = strokaFormulMonitor.getText();

                                for (int kq = 0; kq < 4; kq++)
                                {
                                    Robot robott1 = null;
                                    try
                                    {
                                        robott1 = new Robot();
                                        robott1.keyPress(KeyEvent.VK_RIGHT);
                                        Thread.sleep(200);
                                        robott1.keyRelease(KeyEvent.VK_RIGHT);
                                        Thread.sleep(200);
                                    } catch (AWTException | NullPointerException e) {
                                        LOGGER.error("----getBTTIdFromMonit 169 catch-> " + LocalDateTime.now() + e);
                                    }
                                }
                            } else if (!yacheyka.startsWith("I") && nameOfGood == null) // если не попали в I и
                            // название товара ещё не взяли
                            {
                                while (nameOfGood == null)
                                {
                                    int where = checkBTTWhereItIs(yacheyka); // yacheyka.startsWith("I")) coordinataYacheyki = 9;
                                    int qty = 0;
                                    if (where > 9) qty = where - 9;
                                    else if (where < 9) qty = 9 - where;
                                    Robot robott1 = null;
                                    for (int rr = 0; rr < qty; rr++)
                                    {
                                        if (where > 9)
                                        {
                                            try
                                            {
                                                robott1 = new Robot();
                                                robott1.keyPress(KeyEvent.VK_LEFT);
                                                Thread.sleep(200);
                                                robott1.keyRelease(KeyEvent.VK_LEFT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 194 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        } else if (where < 9)
                                        {
                                            try
                                            {
                                                robott1 = new Robot();
                                                robott1.keyPress(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                                robott1.keyRelease(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 206 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        }
                                    }
                                    try
                                    {
                                        robott1 = new Robot();
                                        // получаю ссылку с номером ячейки
                                        robott1.keyPress(KeyEvent.VK_SHIFT);
                                        robott1.keyPress(KeyEvent.VK_F10);
                                        Thread.sleep(900);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_F10);
                                        robott1.keyRelease(KeyEvent.VK_SHIFT);
                                        Thread.sleep(400);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_DOWN);
                                        Thread.sleep(700);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_ENTER);
                                        Thread.sleep(700);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_ENTER);
                                        Thread.sleep(1100);  // Let the user actually see something!
                                    } catch (AWTException | NullPointerException e) {
                                        LOGGER.error("----getBTTIdFromMonit 229 catch-> " + LocalDateTime.now() + e);
                                    }

                                    yacheyka = getBTTYacheykaFmBuffer();

                                    // если ячейка начинается с I - это название товара, если с С - это айди
                                    if (yacheyka.startsWith("I"))
                                    {
                                        // идём в мониторинг получить название товара
                                        WebElement strokaFormulMonitor = driverMonitoring
                                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                        nameOfGood = strokaFormulMonitor.getText();

                                        for (int kq = 0; kq < 4; kq++)
                                        {
                                            Robot robott11 = null;
                                            try
                                            {
                                                robott11 = new Robot();
                                                robott11.keyPress(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                                robott11.keyRelease(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 253 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        }
                                    }
                                }
                            }
                            else if(!yacheyka.startsWith("G") && nameOfGood != null && nameOfCompany == null)
                                // название фирмы надо взять
                            {
                                while (nameOfCompany == null)
                                {
                                    int where = checkBTTWhereItIs(yacheyka); // yacheyka.startsWith("I")) coordinataYacheyki = 9;
                                    int qty = 0;
                                    if (where > 7) qty = where - 7;
                                    else if (where < 7) qty = 7 - where;
                                    Robot robott1 = null;
                                    for (int rr = 0; rr < qty; rr++)
                                    {
                                        if (where > 7)
                                        {
                                            try
                                            {
                                                robott1 = new Robot();
                                                robott1.keyPress(KeyEvent.VK_LEFT);
                                                Thread.sleep(200);
                                                robott1.keyRelease(KeyEvent.VK_LEFT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 281 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        } else if (where < 7)
                                        {
                                            try
                                            {
                                                robott1 = new Robot();
                                                robott1.keyPress(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                                robott1.keyRelease(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 293 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        }
                                    }
                                    try
                                    {
                                        robott1 = new Robot();
                                        // получаю ссылку с номером ячейки
                                        robott1.keyPress(KeyEvent.VK_SHIFT);
                                        robott1.keyPress(KeyEvent.VK_F10);
                                        Thread.sleep(900);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_F10);
                                        robott1.keyRelease(KeyEvent.VK_SHIFT);
                                        Thread.sleep(400);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_DOWN);
                                        Thread.sleep(700);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_ENTER);
                                        Thread.sleep(700);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_ENTER);
                                        Thread.sleep(1100);  // Let the user actually see something!
                                    } catch (AWTException | NullPointerException e) {
                                        LOGGER.error("----getBTTIdFromMonit 316 catch-> " + LocalDateTime.now() + e);
                                    }

                                    yacheyka = getBTTYacheykaFmBuffer();

                                    // если ячейка начинается с I - это название товара, если с С - это айди
                                    if (yacheyka.startsWith("G"))
                                    {
                                        // идём в мониторинг получить название товара
                                        WebElement strokaFormulMonitor = driverMonitoring
                                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                        nameOfCompany = strokaFormulMonitor.getText();

                                        for (int kq = 0; kq < 2; kq++)
                                        {
                                            Robot robott2 = null;
                                            try
                                            {
                                                robott2 = new Robot();
                                                robott2.keyPress(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                                robott2.keyRelease(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 340 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        }
                                    }
                                }
                            }
                            else if(yacheyka.startsWith("G")) // название фирмы надо взять
                            {
                                // идём в мониторинг получить название фирмы
                                WebElement strokaFormulMonitor = driverMonitoring
                                        .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                nameOfCompany = strokaFormulMonitor.getText();

                                for (int kq = 0; kq < 2; kq++)
                                {
                                    Robot robott1 = null;
                                    try
                                    {
                                        robott1 = new Robot();
                                        robott1.keyPress(KeyEvent.VK_RIGHT);
                                        Thread.sleep(200);
                                        robott1.keyRelease(KeyEvent.VK_RIGHT);
                                        Thread.sleep(200);
                                    } catch (AWTException | NullPointerException e) {
                                        LOGGER.error("----getBTTIdFromMonit 364 catch-> " + LocalDateTime.now() + e);
                                    }
                                }
                            }
                            else if(!yacheyka.startsWith("C") && nameOfGood != null && nameOfCompany != null)
                            {
                                while (!yacheyka.startsWith("C"))
                                {
                                    int where = checkBTTWhereItIs(yacheyka); // yacheyka.startsWith("I")) coordinataYacheyki = 9;
                                    int qty = 0;
                                    if (where > 3) qty = where - 3;
                                    else if (where < 3) qty = 3 - where;
                                    Robot robott1 = null;
                                    for (int rr = 0; rr < qty; rr++)
                                    {
                                        if (where > 3)
                                        {
                                            try
                                            {
                                                robott1 = new Robot();
                                                robott1.keyPress(KeyEvent.VK_LEFT);
                                                Thread.sleep(200);
                                                robott1.keyRelease(KeyEvent.VK_LEFT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 389 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        } else if (where < 3)
                                        {
                                            try
                                            {
                                                robott1 = new Robot();
                                                robott1.keyPress(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                                robott1.keyRelease(KeyEvent.VK_RIGHT);
                                                Thread.sleep(200);
                                            } catch (AWTException | NullPointerException e) {
                                                LOGGER.error("----getBTTIdFromMonit 401 catch-> "
                                                        + LocalDateTime.now() + e);
                                            }
                                        }
                                    }
                                    try
                                    {
                                        robott1 = new Robot();
                                        // получаю ссылку с номером ячейки
                                        robott1.keyPress(KeyEvent.VK_SHIFT);
                                        robott1.keyPress(KeyEvent.VK_F10);
                                        Thread.sleep(900);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_F10);
                                        robott1.keyRelease(KeyEvent.VK_SHIFT);
                                        Thread.sleep(400);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_DOWN);
                                        Thread.sleep(700);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_ENTER);
                                        Thread.sleep(700);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_ENTER);
                                        Thread.sleep(1100);  // Let the user actually see something!
                                    } catch (AWTException | NullPointerException e) {
                                        LOGGER.error("----getBTTIdFromMonit 424 catch-> " + LocalDateTime.now() + e);
                                    }

                                    yacheyka = getBTTYacheykaFmBuffer();

                                    // если ячейка начинается с I - это название товара, если с С - это айди
                                    if(yacheyka.startsWith("C"))
                                    {
                                        // идём в мониторингг получить айди
                                        WebElement strokaFormulMonitor = driverMonitoring
                                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                        idFromYacheika = strokaFormulMonitor.getText();
                                        flag = false;
                                    }
                                }
                            } else if(yacheyka.startsWith("C"))
                            {
                                // идём в мониторингг получить айди
                                WebElement strokaFormulMonitor = driverMonitoring
                                        .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                idFromYacheika = strokaFormulMonitor.getText();
                                flag = false;
                            }
                        }

                        try
                        {
                            if ((!findBar1of1String.equalsIgnoreCase("0 из 0")) &&
                                    (!findBar1of1String.equalsIgnoreCase("1 из 1")))
                            {
                                System.out.println("--BTT------306 idFMoni findBar1of1String:" + findBar1of1String + "!");
                                String[] arrFindBar1of1String = findBar1of1String.split(" ");
                                if (arrFindBar1of1String.length >= 2)
                                {
                                    to = Integer.parseInt(arrFindBar1of1String[2]);
                                }
                                if (to > 1) indexIdInArr++;
                            }
                        } catch (NumberFormatException e)
                        {
                            LOGGER.error("----getBTTIdFromMonit 464 catch-BTT-- при поиске в окошке вместо надписи " +
                                    "1 из 1 какой-то текст получился - findBar1of1String: " + findBar1of1String + "\n"
                                     + LocalDateTime.now() + e);
                            System.out.println("-BTT-- при поиске в окошке вместо надписи 1 из 1 какой-то текст " +
                                    "получился - findBar1of1String: " + findBar1of1String + "е: " + e);
                        }

                        foundInfoFmMonitoringList.add(nameOfGood);
                        nameOfGoodsSet.add(nameOfGood);

                        foundInfoFmMonitoringList.add(idFromYacheika);
                        foundIdFmMonitoringSet.add(idFromYacheika);
                        //System.out.println("********* " + foundIdFmMonitoringSet);
                        Thread.sleep(1_000);  // Let the user actually see something!
                    }
                    if(a == words.length-1) break;
                }
            }
        }
        LOGGER.info("---getBTTIdFromMonit 483 -> " + LocalDateTime.now() + "\n" + foundIdFmMonitoringSet);
        // формируем номера айди для текста
        for (String s : foundIdFmMonitoringSet)
        {
            idFromYacheika1 = idFromYacheika1 + s + ", ";
        }
        if(indexIdInArr > 0) idFromYacheika1 = idFromYacheika1 + "(возможно это старый id), ";
        LOGGER.info("---getBTTIdFromMonit 490 idFMoni indexIdInArr -> " + LocalDateTime.now() + "\n" + indexIdInArr);

        if(a0iz0 > 0) idFromYacheika1 = idFromYacheika1 + "(не нашёл id), ";
        LOGGER.info("---getBTTIdFromMonit 493 idFMoni a0iz0 -> " + LocalDateTime.now() + "\n" + a0iz0);

        LOGGER.info("---getBTTIdFromMonit 495 findBar1of1String -> " + LocalDateTime.now() + "\n" + findBar1of1String);

        // формируем товары после айди для текста
        for (String st : nameOfGoodsSet)
        {
            idFromYacheika1 = idFromYacheika1 + st + ", ";
        }

        // вставляем название фирмы после айди и после назв товаров
        idFromYacheika1 = idFromYacheika1 + "*" + nameOfCompany + "*, ";
        LOGGER.info("---getBTTIdFromMonit 505 idFromYacheika1-> " + LocalDateTime.now() + "\n" + idFromYacheika1);

        driverMonitoring.quit(); // закрывает окно

        //System.out.println("idFromYacheika1 " + idFromYacheika1);
        return idFromYacheika1;
    }

    public static String getBTTCompanyNameFromMonitoringGoogleDocs(String msgToDiscord1) throws InterruptedException
    {
        String findBar1of1String = "";
        String nameOfCompany = "";
        String yacheyka = "";
        String[] words = msgToDiscord1.split(" ");
        //System.out.println("333 " + Arrays.toString(words));

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driverMonitoring = new ChromeDriver();
        driverMonitoring.manage().window().maximize();

// пандамониторинг
        driverMonitoring.get("https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing");

        Thread.sleep(14_000);  // Let the user actually see something!

        // перебираем мониторинг, вдруг есть одинаковые записи по контейнеру или фуре
        for (int i = 0; i < words.length; i++)
        {
            if (words[i].contains("онтейнеры") || (words[i].contains("Номер") && words[i+1].contains("ТС")) )
            {
                //System.out.println("99999999000 " + words[i]);
                for (int j = 1; j < words.length; j++)
                {
                    if (words[i].contains("Номер") && words[i+1].contains("ТС") ) j++;
                    int a = i + j;
                    String contNumber = "";
                    try {
                        contNumber = words[a].replaceAll(",","").replace(".","");
                    } catch (ArrayIndexOutOfBoundsException ae){
                        LOGGER.error("----getBTTIdFromMonit getBTTCompanyName 544 catch мистика, вышли из массива-> "
                                + LocalDateTime.now() + ae);
                        break;
                    }

                    //System.out.println("99999999222 " + words[a]);
                    //System.out.println("99999999444 " + contNumber + " ** " + contNumber.length());
                    if(msgToDiscord1.contains("онтейнеры") && contNumber.length() != 11) break;
                    if(words[a].contains("нспектор") ) break;
                    else
                    {
                        // открыть окно поиска для ввода номера контейнера
                        driverMonitoring.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"f");

                        // поле поиск //*[@id="docs-findbar-input"]/table/tbody/tr/td[1]/input
                        WebElement searchBox = driverMonitoring.findElement(By
                                .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[1]/input"));
                        Thread.sleep(800);  // Let the user actually see something!

                        //пробую искать существующий номер контейнера
                        searchBox.sendKeys(contNumber + Keys.RETURN); // существующий

                        // ждём как отвиснет мониторинг при поиске
                        for (int kk = 0; kk < 15; kk++)
                        {
                            Thread.sleep(5_000);  // Let the user actually see something!
                            // в окне поиска взять 1из1    //*[@id="docs-findbar-input"]/table/tbody/tr/td[2]/span
                            WebElement findBar1of1 = driverMonitoring.findElement(By
                                    .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[2]/span"));
                            findBar1of1String = findBar1of1.getText();
                            LOGGER.info("---getBTTIdFromMonit getBTTCompanyName 574 findBar1of1String-> "
                                    + LocalDateTime.now() + "\n" + findBar1of1String);
                            if(!findBar1of1String.equalsIgnoreCase("0 из 0")) break;
                        }

                        Thread.sleep(300);  // Let the user actually see something!

                        // крестик в поле поиск //*[@id="docs-findbar-id"]/div/div/div[1]/div[2]/div/div/div
                        WebElement searchBox1 = driverMonitoring.findElement(By
                                .xpath("//*[@id=\"docs-findbar-id\"]/div/div/div[1]/div[2]/div/div/div/div"));
                        searchBox1.click();
                        Thread.sleep(500);  // Let the user actually see something!

                        nameOfCompany = null;
                        boolean flag = true;
                        while (flag) // NoSuchSessionException ???
                        { // получаем название фирмы из колонки G
                            Robot robott = null;
                            try
                            {
                                for (int k1 = 0; k1 < 6; k1++)
                                {
                                    robott = new Robot();
                                    robott.keyPress(KeyEvent.VK_LEFT);
                                    Thread.sleep(200);
                                    robott.keyRelease(KeyEvent.VK_LEFT);
                                    Thread.sleep(200);
                                }

                                // получаю ссылку с номером ячейки
                                robott.keyPress(KeyEvent.VK_SHIFT);
                                robott.keyPress(KeyEvent.VK_F10);
                                Thread.sleep(900);  // Let the user actually see something!
                                robott.keyRelease(KeyEvent.VK_F10);
                                robott.keyRelease(KeyEvent.VK_SHIFT);
                                Thread.sleep(400);  // Let the user actually see something!
                                robott.keyPress(KeyEvent.VK_DOWN);
                                Thread.sleep(700);  // Let the user actually see something!
                                robott.keyRelease(KeyEvent.VK_DOWN);
                                Thread.sleep(100);  // Let the user actually see something!
                                robott.keyPress(KeyEvent.VK_ENTER);
                                Thread.sleep(700);  // Let the user actually see something!
                                robott.keyRelease(KeyEvent.VK_ENTER);
                                Thread.sleep(1100);  // Let the user actually see something!

                                //    тут
                                yacheyka = getBTTYacheykaFmBuffer();

                            } catch (AWTException | NullPointerException e) {
                                LOGGER.error("---getBTTIdFromMonit getBTTCompanyName 623 catch-> "
                                        + LocalDateTime.now() + "\n" + e);
                            } catch (NoSuchElementException | NoSuchSessionException ne) {
                                LOGGER.error("---getBTTIdFromMonit getBTTCompanyName BTT ЧТО-ТО С ГУГЛ-ЭКСЕЛЕМ 626 " +
                                        "catch-> " + LocalDateTime.now() + "\n" + ne);
                            }

                            if(yacheyka.startsWith("G")) // название фирмы надо взять
                            {
                                // идём в мониторинг получить название фирмы
                                WebElement strokaFormulMonitor = driverMonitoring
                                        .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                nameOfCompany = "*" + strokaFormulMonitor.getText() + "*";
                                flag = false;
                            }
                            else if(!yacheyka.startsWith("G") && nameOfCompany == null) // название фирмы надо взять
                            {
                                    while (nameOfCompany == null)
                                    {
                                        int where = checkBTTWhereItIs(yacheyka); // yacheyka.startsWith("I")) coordinataYacheyki = 9;
                                        int qty = 0;
                                        if (where > 7) qty = where - 7;
                                        else if (where < 7) qty = 7 - where;
                                        Robot robott1 = null;
                                        for (int rr = 0; rr < qty; rr++)
                                        {
                                            if (where > 7)
                                            {
                                                try
                                                {
                                                    robott1 = new Robot();
                                                    robott1.keyPress(KeyEvent.VK_LEFT);
                                                    Thread.sleep(200);
                                                    robott1.keyRelease(KeyEvent.VK_LEFT);
                                                    Thread.sleep(200);
                                                } catch (AWTException | NullPointerException e) {
                                                    LOGGER.error("---getBTTIdFromMonit getBTTCompanyName 659 " +
                                                            "catch-> " + LocalDateTime.now() + "\n" + e);
                                                }
                                            } else if (where < 7)
                                            {
                                                try
                                                {
                                                    robott1 = new Robot();
                                                    robott1.keyPress(KeyEvent.VK_RIGHT);
                                                    Thread.sleep(200);
                                                    robott1.keyRelease(KeyEvent.VK_RIGHT);
                                                    Thread.sleep(200);
                                                } catch (AWTException | NullPointerException e) {
                                                    LOGGER.error("---getBTTIdFromMonit getBTTCompanyName 672 " +
                                                            "catch-> " + LocalDateTime.now() + "\n" + e);
                                                }
                                            }
                                        }
                                        try
                                        {
                                            robott1 = new Robot();
                                            // получаю ссылку с номером ячейки
                                            robott1.keyPress(KeyEvent.VK_SHIFT);
                                            robott1.keyPress(KeyEvent.VK_F10);
                                            Thread.sleep(900);  // Let the user actually see something!
                                            robott1.keyRelease(KeyEvent.VK_F10);
                                            robott1.keyRelease(KeyEvent.VK_SHIFT);
                                            Thread.sleep(400);  // Let the user actually see something!
                                            robott1.keyPress(KeyEvent.VK_DOWN);
                                            Thread.sleep(700);  // Let the user actually see something!
                                            robott1.keyRelease(KeyEvent.VK_DOWN);
                                            Thread.sleep(100);  // Let the user actually see something!
                                            robott1.keyPress(KeyEvent.VK_ENTER);
                                            Thread.sleep(700);  // Let the user actually see something!
                                            robott1.keyRelease(KeyEvent.VK_ENTER);
                                            Thread.sleep(1100);  // Let the user actually see something!
                                        } catch (AWTException | NullPointerException e) {
                                            LOGGER.error("---getBTTIdFromMonit getBTTCompanyName 697 " +
                                                    "catch-> " + LocalDateTime.now() + "\n" + e);
                                        }

                                        yacheyka = getBTTYacheykaFmBuffer();

                                        // если ячейка начинается с G - это название Company
                                        if (yacheyka.startsWith("G"))
                                        {
                                            // идём в мониторинг получить название товара
                                            WebElement strokaFormulMonitor = driverMonitoring
                                                    .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                            nameOfCompany = "*" + strokaFormulMonitor.getText() + "*";
                                            flag = false;
                                        }
                                    }
                            }
                        }

                        //System.out.println("********* " + foundIdFmMonitoringSet);
                        Thread.sleep(1_000);  // Let the user actually see something!
                    }
                    if(a == words.length-1) break;
                }
            }
        }

        // название фирмы
        nameOfCompany = nameOfCompany + ", ";
        driverMonitoring.quit(); // закрывает окно
        LOGGER.info("---getBTTIdFromMonit getBTTCompanyName 726-> " + LocalDateTime.now() + "\n" + nameOfCompany);

        return nameOfCompany;
    }

    public static int checkBTTWhereItIs(String yacheyka) // находим порядковый номер столбца
    {
        String[] arr = new String[]{"0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S"
                ,"T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN"
                ,"AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ","BA","BB","BC","BD","BE","BF","BG","BH"
                ,"BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ"};

        for (int i = 0; i < arr.length; i++)
        {
            if(yacheyka.startsWith(arr[i])) // сравнение найденной ячейки и поиск с каких букв начинается
            {
                if(yacheyka.startsWith("A") || yacheyka.startsWith("B") )
                { // если начинается с А или В, то проверяем есть ли вторая буква в индексе
                    for (int j = 27; j < arr.length; j++)
                    {
                        if(yacheyka.contains(arr[j])) coordinataYacheyki = j;
                        else coordinataYacheyki = i;
                    }
                } else
                {
                    coordinataYacheyki = i;
                }
                break;
            }
        }
        LOGGER.info("---getBTTIdFromMonit checkBTTWhereItIs coordinataYacheyki-> " + LocalDateTime.now()
                + "\n" + coordinataYacheyki);

        return coordinataYacheyki;
    }

    public static String getBTTYacheykaFmBuffer() throws InterruptedException
    {
        String yacheyka1 = "";
        try
        {
            // буфер - мой эксель
            WebDriver driverBuffer = new ChromeDriver();
            driverBuffer.get("https://docs.google.com/spreadsheets/d/11kc0xpiVTHTMwhGTdSU94qaULSAIAGKArfswg5_gauE/edit?usp=sharing");
            Thread.sleep(4_100);  // Let the user actually see something!
            // идём в буфер вставить ссылку в строку формул и получить её в переменную
            WebElement strokaFormulBuffer = driverBuffer
                    .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
            strokaFormulBuffer.click();
            strokaFormulBuffer.sendKeys(Keys.CONTROL +"a");
            Thread.sleep(1_000);  // Let the user actually see something!
            strokaFormulBuffer.sendKeys(Keys.CONTROL +"v");
            Thread.sleep(600);  // Let the user actually see something!
            String ssylkaFmBuffer = strokaFormulBuffer.getText();
//                            System.out.println("ssylka1->" + ssylkaFmBuffer);
            String[] arrSsylka = ssylkaFmBuffer.split("=");
            driverBuffer.quit();

            //[https://docs.google.com/spreadsheets/d/18rj1I5W8C5k/edit#gid, 0&range, C8616]
//                            System.out.println(Arrays.toString(arrSsylka));
            LOGGER.info("---getBTTIdFromMonit getBTTYacheykaFmBuffer arrSsylka[arrSsylka.length-1]-> "
                    + LocalDateTime.now() + "\n" + arrSsylka[arrSsylka.length-1]);

            yacheyka1 = arrSsylka[arrSsylka.length-1];
        } catch (NoSuchElementException | NoSuchSessionException ne) {
            LOGGER.error("---getBTTIdFromMonit getBTTYacheykaFmBuffer catch-> "
                    + LocalDateTime.now() + "\n" + ne);
        }
        return yacheyka1;
    }
}
