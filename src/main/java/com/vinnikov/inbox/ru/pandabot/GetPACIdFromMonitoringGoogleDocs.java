package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class GetPACIdFromMonitoringGoogleDocs
{
    public static String getPACIdFromMonitoringGoogleDocs(String msgToDiscord1) throws InterruptedException
    {
        String idFromYacheika = "";
        String idFromYacheika1 = "";
        String findBar1of1String = "";
        String nameOfGood = "";
        String yacheyka = "";
        //int indexIdInArr = 0;
        int a0iz0 = 0;
        int to = 0;
        String[] words = msgToDiscord1.split(" ");
System.out.println("------333 " + Arrays.toString(words));
        HashSet<String> foundIdFmMonitoringSet = new HashSet<>();
        HashSet<String> nameOfGoodsSet = new HashSet<>();
        ArrayList<String> foundInfoFmMonitoringList = new ArrayList<>();

System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driverMonitoring = new ChromeDriver();
        driverMonitoring.manage().window().maximize();

// пандамониторинг
        driverMonitoring.get("https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing");

        Thread.sleep(14_000);  // Let the user actually see something!

        // перебираем мониторинг, чтобы получить номер коносамента
        for (int i = 0; i < words.length; i++)
        {
            if(words[i].contains("TH:") || words[i].contains("ТН:")) // англ и рус написание
            {
                String conosament = words[i+1].replaceAll(",","");
                System.out.println("conosament:" + conosament + "!");
                boolean flag = true;
                // открыть окно поиска для ввода номера контейнера
                driverMonitoring.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"f");

                // поле поиск //*[@id="docs-findbar-input"]/table/tbody/tr/td[1]/input
                WebElement searchBox = driverMonitoring.findElement(By
                        .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[1]/input"));
                Thread.sleep(800);  // Let the user actually see something!

                //Thread.sleep(1_000);  // Let the user actually see something!
                foundInfoFmMonitoringList.add(conosament);
                //пробую искать существующий номер коносамента-разнорядки
                searchBox.sendKeys(conosament + Keys.RETURN); // существующий
                //Thread.sleep(20_000);  // Let the user actually see something!

                // ждём как отвиснет мониторинг
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

                while (flag) // NoSuchSessionException ???
                {
                    Robot robott = null;
                    try
                    {
                        robott = new Robot();
                        robott.keyPress(KeyEvent.VK_LEFT);
                        Thread.sleep(200);
                        robott.keyRelease(KeyEvent.VK_LEFT);
                        Thread.sleep(200);

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

                    // буфер - мой эксель
                    WebDriver driverBuffer = new ChromeDriver();
driverBuffer.get("https://docs.google.com/spreadsheets/d/11kc0xpiVTHTMwhGTdSU94qaULSAIAGKArfswg5_gauE/edit?usp=sharing");
                    Thread.sleep(3100);  // Let the user actually see something!
                    // идём в буфер вставить ссылку в строку формул и получить её в переменную
                    WebElement strokaFormulBuffer = driverBuffer
                            .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                    strokaFormulBuffer.click();
                    strokaFormulBuffer.sendKeys(Keys.CONTROL +"a");
                    Thread.sleep(1_000);  // Let the user actually see something!
                    strokaFormulBuffer.sendKeys(Keys.CONTROL +"v");
                    String ssylkaFmBuffer = strokaFormulBuffer.getText();
// System.out.println("ssylka1->" + ssylkaFmBuffer);
                    String[] arrSsylka = ssylkaFmBuffer.split("=");
                    driverBuffer.quit();
                    //[https://docs.google.com/spreadsheets/d/18rj1wk5I5W8C5k/edit#gid, 0&range, C8616]
// System.out.println(Arrays.toString(arrSsylka));
                    System.out.println("**" + arrSsylka[arrSsylka.length-1] + "**");
                    yacheyka = arrSsylka[arrSsylka.length-1];
                    } catch (AWTException e) {
                        e.printStackTrace();
                    } catch (NoSuchElementException | NoSuchSessionException ne) {
                        System.out.println("ЧТО-ТО С ГУГЛ-ЭКСЕЛЕМ: " + ne + " ** " + LocalDateTime.now());
                    }

                    // если ячейка начинается с I - это название товара, если с С - это айди
                    if(yacheyka.startsWith("I"))
                    {
                        // идём в буфер вставить ссылку в строку формул и получить её в переменную
                        WebElement strokaFormulMonitor = driverMonitoring
                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                        nameOfGood = strokaFormulMonitor.getText();
                    } else
                    if(yacheyka.startsWith("C"))
                    {
                        // идём в буфер вставить ссылку в строку формул и получить её в переменную
                        WebElement strokaFormulMonitor = driverMonitoring
                                .findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                        idFromYacheika = strokaFormulMonitor.getText();
                        flag = false;
                    }
                }

                /*// если найдено типа 1 из 2х или из 3х или из 4х или из 5х и тп
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
                }*/

                foundInfoFmMonitoringList.add(nameOfGood);
                nameOfGoodsSet.add(nameOfGood);

                foundInfoFmMonitoringList.add(idFromYacheika);
                foundIdFmMonitoringSet.add(idFromYacheika);
                //System.out.println("********* " + foundIdFmMonitoringSet);
                Thread.sleep(1_000);  // Let the user actually see something!

                break;
            }
        }
        System.out.println("444444 " + foundIdFmMonitoringSet);
        idFromYacheika1 = idFromYacheika + ", "; // + nameOfGood + ", " ;
        //if(indexIdInArr > 0) idFromYacheika1 = idFromYacheika1 + "(возможно это старый id), ";
        if(a0iz0 > 0) idFromYacheika1 = idFromYacheika1 + "(не нашёл id), ";

        // формируем товары после айди для текста
        idFromYacheika1 = idFromYacheika1 + nameOfGood + ", " ;

        driverMonitoring.quit(); // закрывает окно

        return idFromYacheika1;
    }
}
