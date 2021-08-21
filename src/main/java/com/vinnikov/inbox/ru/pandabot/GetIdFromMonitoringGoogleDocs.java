package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.NoSuchElementException;

// TLLU5171430 1800 . FCIU2822167 1799 . H871KO32/AM878132 1801 . 822MPF/417YHV 1802 . MSKU8109652 1803 .
public class GetIdFromMonitoringGoogleDocs
{
    //private String msgToDiscord1;
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
        String[] words = msgToDiscord1.split(" ");
        //System.out.println("333 " + Arrays.toString(words));
        HashSet<String> foundIdFmMonitoringSet = new HashSet<>();
        HashSet<String> nameOfGoodsSet = new HashSet<>();
        ArrayList<String> foundInfoFmMonitoringList = new ArrayList<>();

System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driverMonitoring = new ChromeDriver();
        driverMonitoring.manage().window().maximize();

// панда мониторинг https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing
        driverMonitoring.get("https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing");

        Thread.sleep(14_000);  // Let the user actually see something!

        // перебираем мониторинг, вдруг есть одинаковые записи по контейнеру или фуре
        for (int i = 0; i < words.length; i++)
        {
            if (words[i].contains("онтейнер") || (words[i].contains("Номер") && words[i+1].contains("ТС")) )
            {
                //System.out.println("99999999000 " + words[i]);
                for (int j = 1; j < 100; j++)
                {
                    if (words[i].contains("Номер") && words[i+1].contains("ТС") ) j++;
                    int a = i + j;
                    String contNumber = words[a].replaceAll(",","").replace(".","");

                    //System.out.println("99999999222 " + words[a]);
                    //System.out.println("99999999444 " + contNumber + " ** " + contNumber.length());
                    if(msgToDiscord1.contains("онтейнер") && contNumber.length() != 11) break;
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
                            if(!findBar1of1String.equalsIgnoreCase("0 из 0")) break;
                        }
                        // если всё же не нашлось ничего
                        if(findBar1of1String.equalsIgnoreCase("0 из 0"))
                        {
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
                            Thread.sleep(200);  // Let the user actually see something!
                            robott.keyRelease(KeyEvent.VK_F10);
                            robott.keyRelease(KeyEvent.VK_SHIFT);
                            Thread.sleep(400);  // Let the user actually see something!
                            robott.keyPress(KeyEvent.VK_DOWN);
                            Thread.sleep(100);  // Let the user actually see something!
                            robott.keyRelease(KeyEvent.VK_DOWN);
                            Thread.sleep(100);  // Let the user actually see something!
                            robott.keyPress(KeyEvent.VK_ENTER);
                            Thread.sleep(100);  // Let the user actually see something!
                            robott.keyRelease(KeyEvent.VK_ENTER);
                            Thread.sleep(1100);  // Let the user actually see something!

                            //    тут
                            yacheyka = getYacheykaFmBuffer();

                            } catch (AWTException | NullPointerException e) {
                                e.printStackTrace();
                            } catch (NoSuchElementException | NoSuchSessionException ne) {
                                System.out.println("ЧТО-ТО С ГУГЛ-ЭКСЕЛЕМ: " + ne + " ** " + LocalDateTime.now());
                            }

                            // если ячейка начинается с I - это название товара, если с С - это айди
                            if(yacheyka.startsWith("I"))
                            {
                                // идём в мониторинг получить название товара
                                WebElement strokaFormulMonitor = driverMonitoring
                                        .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                nameOfGood = strokaFormulMonitor.getText();
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
                                                e.printStackTrace();
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
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    try
                                    {
                                        robott1 = new Robot();
                                        // получаю ссылку с номером ячейки
                                        robott1.keyPress(KeyEvent.VK_SHIFT);
                                        robott1.keyPress(KeyEvent.VK_F10);
                                        Thread.sleep(200);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_F10);
                                        robott1.keyRelease(KeyEvent.VK_SHIFT);
                                        Thread.sleep(400);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_ENTER);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_ENTER);
                                        Thread.sleep(1100);  // Let the user actually see something!
                                    } catch (AWTException | NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                    yacheyka = getYacheykaFmBuffer();

                                    // если ячейка начинается с I - это название товара, если с С - это айди
                                    if (yacheyka.startsWith("I"))
                                    {
                                        // идём в мониторинг получить название товара
                                        WebElement strokaFormulMonitor = driverMonitoring
                                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                                        nameOfGood = strokaFormulMonitor.getText();
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
                                                e.printStackTrace();
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
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    try
                                    {
                                        robott1 = new Robot();
                                        // получаю ссылку с номером ячейки
                                        robott1.keyPress(KeyEvent.VK_SHIFT);
                                        robott1.keyPress(KeyEvent.VK_F10);
                                        Thread.sleep(200);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_F10);
                                        robott1.keyRelease(KeyEvent.VK_SHIFT);
                                        Thread.sleep(400);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_DOWN);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyPress(KeyEvent.VK_ENTER);
                                        Thread.sleep(100);  // Let the user actually see something!
                                        robott1.keyRelease(KeyEvent.VK_ENTER);
                                        Thread.sleep(1100);  // Let the user actually see something!
                                    } catch (AWTException | NullPointerException e) {
                                        e.printStackTrace();
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
                                String[] arrFindBar1of1String = findBar1of1String.split(" ");
                                if (arrFindBar1of1String.length >= 2)
                                {
                                    to = Integer.parseInt(arrFindBar1of1String[2]);
                                }
                                if (to > 1) indexIdInArr++;
                            }
                        } catch (NumberFormatException e)
                        {
                            System.out.println("--- при поиске в окошке вместо надписи 1 из 1 какой-то текст " +
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
        System.out.println("444444 " + foundIdFmMonitoringSet);
        // формируем номера айди для текста
        for (String s : foundIdFmMonitoringSet)
        {
            idFromYacheika1 = idFromYacheika1 + s + ", ";
        }
        if(indexIdInArr > 0) idFromYacheika1 = idFromYacheika1 + "(возможно это старый id), ";
        if(a0iz0 > 0) idFromYacheika1 = idFromYacheika1 + "(не нашёл id), ";

        // формируем товары после айди для текста
        for (String st : nameOfGoodsSet)
        {
            idFromYacheika1 = idFromYacheika1 + st + ", ";
        }

        driverMonitoring.quit(); // закрывает окно

        //System.out.println("idFromYacheika1 " + idFromYacheika1);
        return idFromYacheika1;
    }

    public static int checkWhereItIs(String yacheyka)
    {
        if(yacheyka.startsWith("A")) coordinataYacheyki = 1;
        else if(yacheyka.startsWith("B")) coordinataYacheyki = 2;
        else if(yacheyka.startsWith("C")) coordinataYacheyki = 3;
        else if(yacheyka.startsWith("D")) coordinataYacheyki = 4;
        else if(yacheyka.startsWith("E")) coordinataYacheyki = 5;
        else if(yacheyka.startsWith("F")) coordinataYacheyki = 6;
        else if(yacheyka.startsWith("G")) coordinataYacheyki = 7;
        else if(yacheyka.startsWith("H")) coordinataYacheyki = 8;
        else if(yacheyka.startsWith("I")) coordinataYacheyki = 9;
        else if(yacheyka.startsWith("J")) coordinataYacheyki = 10;
        else if(yacheyka.startsWith("K")) coordinataYacheyki = 11;
        else if(yacheyka.startsWith("L")) coordinataYacheyki = 12;
        else if(yacheyka.startsWith("M")) coordinataYacheyki = 13;
        else if(yacheyka.startsWith("N")) coordinataYacheyki = 14;
        else if(yacheyka.startsWith("O")) coordinataYacheyki = 15;
        else if(yacheyka.startsWith("P")) coordinataYacheyki = 16;
        else if(yacheyka.startsWith("Q")) coordinataYacheyki = 17;
        else if(yacheyka.startsWith("R")) coordinataYacheyki = 18;
        else if(yacheyka.startsWith("S")) coordinataYacheyki = 19;
        else if(yacheyka.startsWith("T")) coordinataYacheyki = 20;
        System.out.println("coordinataYacheyki " + coordinataYacheyki);
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
            String ssylkaFmBuffer = strokaFormulBuffer.getText();
//                            System.out.println("ssylka1->" + ssylkaFmBuffer);
            String[] arrSsylka = ssylkaFmBuffer.split("=");
            driverBuffer.quit();

            //[https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit#gid, 0&range, C8616]
//                            System.out.println(Arrays.toString(arrSsylka));
            System.out.println("**" + arrSsylka[arrSsylka.length-1] + "**");
            yacheyka1 = arrSsylka[arrSsylka.length-1];
        } catch (NoSuchElementException | NoSuchSessionException ne) {
            System.out.println("ЧТО-ТО С ГУГЛ-ЭКСЕЛЕМ: " + ne + " ** " + LocalDateTime.now());
        }
        return yacheyka1;
    }
}
