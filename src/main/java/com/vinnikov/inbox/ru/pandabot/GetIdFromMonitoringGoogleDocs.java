package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.NoSuchElementException;

import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class GetIdFromMonitoringGoogleDocs
{
    private static int coordinataYacheyki;

    public static String getIdFromMonitoringGoogleDocs(String msgToDiscord1) throws InterruptedException
    {
        String idFromYacheika = "";
        String idFromYacheika1 = "";
        String findBar1of1String = "";
        String nameOfGood = "";
        String yacheyka = "";
        int indexIdInArr = 0;
        int a0iz0 = 0;
        int to = 0;
        int a = 0;
        String[] words = msgToDiscord1.split(" ");
        LOGGER.info("---getIdFromMonitoring 30-> " + LocalDateTime.now() + "\n" + Arrays.toString(words));

        HashSet<String> foundIdFmMonitoringSet = new HashSet<>();
        HashSet<String> nameOfGoodsSet = new HashSet<>();
        ArrayList<String> foundInfoFmMonitoringList = new ArrayList<>();

        System.setProperty("webdriver.chrome.driver",
                //        "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe" // при запуске jar похоже нет
                //        доступа к диску С:
                "D:\\grIdea\\pandabot\\chromedriver.exe"
        );
        WebDriver driverMonitoring = new ChromeDriver();
        driverMonitoring.manage().window().maximize();

// пандамониторинг
        driverMonitoring.get("https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing");

        Thread.sleep(14_000);  // Let the user actually see something!

        // перебираем мониторинг, вдруг есть одинаковые записи по контейнеру или фуре
        for (int i = 0; i < words.length; i++)
        {
            if (words[i].contains("онтейнер") || (words[i].contains("Номер") && words[i+1].contains("ТС")) )
            {
                //System.out.println("99999999000 " + words[i]);
                for (int j = 1; j < words.length; j++)
                {
                    String contNumber = "";
                    try {
                        if (words[i].contains("Номер") && words[i+1].contains("ТС") ) j++;
                        a = i + j;
                        contNumber = words[a].replaceAll(",","").replace(".","");
                    } catch (ArrayIndexOutOfBoundsException ae){
                        LOGGER.error("---getIdFromMonitoring 59 мистика, вышли из массива-> " + LocalDateTime.now()
                                + "\n" + ae);
                        break;
                    }

                    LOGGER.info("---getIdFromMonitoring 64-> " + LocalDateTime.now() + "\n" + words[a]);
                    //System.out.println("99999999444 " + contNumber + " ** " + contNumber.length());
                    if(msgToDiscord1.contains("онтейнеры") && contNumber.length() != 11) break;
                    if(words[a].equalsIgnoreCase(" ") || words[a].isEmpty()) break;
                    if(words[a].contains("нспектор") || words[a].contains("АВТОРЕГИСТРАЦ")
                            || words[a].contains("АВТОВЫПУС") ) break;
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
                            LOGGER.info("---getIdFromMonitoring 94 findBar1of1String-> " + LocalDateTime.now()
                                    + "\n" + findBar1of1String);
                            if(!findBar1of1String.equalsIgnoreCase("0 из 0")) break;
                        }
                        // если всё же не нашлось ничего
                        if(findBar1of1String.equalsIgnoreCase("0 из 0"))
                        {
                            LOGGER.info("---getIdFromMonitoring 101 findBar1of1String-> " + LocalDateTime.now()
                                    + "\n" + findBar1of1String);
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
                        int goLeft = 4;
                        while (flag) // NoSuchSessionException ???
                        { // получаем айди и название товара
                            Robot robott = null;
                            try
                            {
                                for (int k1 = 0; k1 < goLeft; k1++)
                                {
                                    robott = new Robot();
                                    robott.keyPress(KeyEvent.VK_LEFT);
                                    Thread.sleep(200);
                                    robott.keyRelease(KeyEvent.VK_LEFT);
                                    Thread.sleep(200);
                                }
                                Thread.sleep(500);  // Let the user actually see something!
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
                            yacheyka = getYacheykaFmBuffer();

                            } catch (AWTException | NullPointerException e) {
                                LOGGER.error("---getIdFromMonitoring 149 catch-> " + LocalDateTime.now() + "\n" + e);
                            } catch (NoSuchElementException | NoSuchSessionException ne) {
                                LOGGER.error("---getIdFromMonitoring 152 ЧТО-ТО С ГУГЛ-ЭКСЕЛЕМ-> "
                                        + LocalDateTime.now() + "\n" + ne);
                            }

                            // если ячейка начинается с I - это название товара, если с С - это айди
                            if(yacheyka.startsWith("I"))
                            {
                                // идём в мониторинг получить название товара
                                WebElement strokaFormulMonitor = driverMonitoring
                                        .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                nameOfGood = strokaFormulMonitor.getText();
                                goLeft = 6;
                            } else if (!yacheyka.startsWith("I") && nameOfGood == null) // если не попали в I и
                                // название товара ещё не взяли
                            {
                                while (nameOfGood == null)
                                {
                                    int where = checkWhereItIs(yacheyka); // yacheyka.startsWith("I")) coordinataYacheyki = 9;
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
                                                LOGGER.error("---getIdFromMonitoring 184 catch-> "
                                                        + LocalDateTime.now() + "\n" + e);
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
                                                LOGGER.error("---getIdFromMonitoring 196 catch-> "
                                                        + LocalDateTime.now() + "\n" + e);
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
                                        LOGGER.error("---getIdFromMonitoring 219 catch-> " + LocalDateTime.now()
                                                + "\n" + e);
                                    }

                                    yacheyka = getYacheykaFmBuffer();

                                    // если ячейка начинается с I - это название товара, если с С - это айди
                                    if (yacheyka.startsWith("I"))
                                    {
                                        // идём в мониторинг получить название товара
                                        WebElement strokaFormulMonitor = driverMonitoring
                                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                        nameOfGood = strokaFormulMonitor.getText();
                                        goLeft = 6;
                                    }
                                }
                            } else if(!yacheyka.startsWith("C") && nameOfGood != null)
                            {
                                while (!yacheyka.startsWith("C"))
                                {
                                    int where = checkWhereItIs(yacheyka); // yacheyka.startsWith("I")) coordinataYacheyki = 9;
                                    int qty = 0;
                                    if (where > 3) qty = where - 3;
                                    else if (where < 3) qty = 3 - where;
        System.out.println("----if(!yacheyka.startsWith(C) && nameOfGood != null)--qty:" + qty);
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
                                                LOGGER.error("---getIdFromMonitoring 254 catch-> "
                                                        + LocalDateTime.now() + "\n" + e);
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
                                                LOGGER.error("---getIdFromMonitoring 266 catch-> "
                                                        + LocalDateTime.now() + "\n" + e);
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
                                        LOGGER.error("---getIdFromMonitoring 289 catch-> " + LocalDateTime.now()
                                                + "\n" + e);
                                    }

                                    yacheyka = getYacheykaFmBuffer();

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
                                LOGGER.info("---getIdFromMonitoring 319 findBar1of1String-> " + LocalDateTime.now()
                                        + "\n" + findBar1of1String);
                                String[] arrFindBar1of1String = findBar1of1String.split(" ");
                                if (arrFindBar1of1String.length >= 2)
                                {
                                    to = Integer.parseInt(arrFindBar1of1String[2]);
                                }
                                if (to > 1) indexIdInArr++;
                            }
                        } catch (NumberFormatException e)
                        {
                            LOGGER.error("---getIdFromMonitoring 330 catch - при поиске в окошке вместо надписи 1 из 1 " +
                                    "какой-то текст получился - findBar1of1String-> " + LocalDateTime.now() + "\n"
                                    + findBar1of1String + "е: " + e);
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
        LOGGER.info("---getIdFromMonitoring 347 foundIdFmMonitoringSet-> " + LocalDateTime.now() + "\n"
                + foundIdFmMonitoringSet);
        // формируем номера айди для текста
        for (String s : foundIdFmMonitoringSet)
        {
            idFromYacheika1 = idFromYacheika1 + s + ", ";
        }
        if(indexIdInArr > 0) idFromYacheika1 = idFromYacheika1 + "(возможно это старый id), ";
        LOGGER.info("---getIdFromMonitoring 355 indexIdInArr-> " + LocalDateTime.now() + "\n" + indexIdInArr);

        if(a0iz0 > 0) idFromYacheika1 = idFromYacheika1 + "(не нашёл id), ";
        LOGGER.info("---getIdFromMonitoring 358 a0iz0-> " + LocalDateTime.now() + "\n" + a0iz0);

        LOGGER.info("---getIdFromMonitoring 360 findBar1of1String-> " + LocalDateTime.now() + "\n" + findBar1of1String);

        // формируем товары после айди для текста
        for (String st : nameOfGoodsSet)
        {
            st = st.replaceAll("\r",", ").replaceAll("\n",", ");
            idFromYacheika1 = idFromYacheika1 + st + ", ";
        }

        driverMonitoring.quit(); // закрывает окно

        //System.out.println("idFromYacheika1 " + idFromYacheika1);
        return idFromYacheika1;
    }

    public static int checkWhereItIs(String yacheyka) // находим порядковый номер столбца
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
                        if(yacheyka.contains(arr[j]))
                        {
                            coordinataYacheyki = j;
                            break;
                        }
                        else
                        {
                            coordinataYacheyki = i;
                        }
                    }
                } else
                {
                    coordinataYacheyki = i;
                }
                break;
            }
        }
        LOGGER.info("---getIdFromMonitoring checkWhereItIs 402 coordinataYacheyki-> " + LocalDateTime.now()
                + "\n" + coordinataYacheyki);
        return coordinataYacheyki;
    }

    public static String getYacheykaFmBuffer() throws InterruptedException
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
            LOGGER.info("---getIdFromMonitoring getYacheykaFmBuffer 431 arrSsylka[arrSsylka.length-1]-> "
                    + LocalDateTime.now() + "\n" + arrSsylka[arrSsylka.length-1]);
            yacheyka1 = arrSsylka[arrSsylka.length-1];
        } catch (NoSuchElementException | NoSuchSessionException ne) {
            LOGGER.error("---getIdFromMonitoring getYacheykaFmBuffer 435 ЧТО-ТО С ГУГЛ-ЭКСЕЛЕМ catch-> "
                    + LocalDateTime.now() + "\n" + ne);
        }
        return yacheyka1;
    }
}
