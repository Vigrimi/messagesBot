package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// TLLU5171430 1800 . FCIU2822167 1799 . H871KO32/AM878132 1801 . 822MPF/417YHV 1802 . MSKU8109652 1803 .
public class GetIdFromMonitoringGoogleDocs
{
    //private String msgToDiscord1;

    public static String getIdFromMonitoringGoogleDocs(String msgToDiscord1) throws InterruptedException
    {
        String idFromYacheika1 = "";
        int indexIdInArr = 0;
        int a0iz0 = 0;
        int to = 0;
        String[] words = msgToDiscord1.split(" ");
        //System.out.println("333 " + Arrays.toString(words));
        HashSet<String> foundIdFmMonitoringSet = new HashSet<>();
        HashSet<String> nameOfGoodsSet = new HashSet<>();
        ArrayList<String> foundInfoFmMonitoringList = new ArrayList<>();

System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

// панда мониторинг https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing

        driver.get("https://docs.google.com/spreadsheets/d/18rj1wk5k8-7lthDGSlQYxiTFwskgUSwF1s5YI5W8C5k/edit?usp=sharing");
        Thread.sleep(14_000);  // Let the user actually see something!

        // перебираем мониторинг, вдруг есть одинаковые записи по контейнеру или фуре
        for (int i = 0; i < words.length; i++)
        {
            if (words[i].contains("онтейнер"))
            {
                //System.out.println("99999999000 " + words[i]);
                for (int j = 1; j < 100; j++)
                {
                    int a = i + j;
                    String contNumber = words[a].replaceAll(",","");
                    //System.out.println("99999999222 " + words[a]);
                    //System.out.println("99999999444 " + contNumber + " ** " + contNumber.length());
                    if(contNumber.length() != 11) break;
                    else
                    {
                        // открыть окно поиска для ввода номера контейнера
                        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"f");

                        // поле поиск //*[@id="docs-findbar-input"]/table/tbody/tr/td[1]/input
                        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[1]/input"));
                        Thread.sleep(800);  // Let the user actually see something!

                        //Thread.sleep(1_000);  // Let the user actually see something!
                        foundInfoFmMonitoringList.add(contNumber);
                        //пробую искать существующий номер контейнера
                        searchBox.sendKeys(contNumber + Keys.RETURN); // существующий

                        Thread.sleep(20_000);  // Let the user actually see something!

                        // в окне поиска взять 1из1    //*[@id="docs-findbar-input"]/table/tbody/tr/td[2]/span
                        WebElement findBar1of1 = driver.findElement(By
                                .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[2]/span"));
                        String findBar1of1String = findBar1of1.getText();
                        if(findBar1of1String.equalsIgnoreCase("0 из 0")) a0iz0++;

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

                        Thread.sleep(1_000);  // Let the user actually see something!

                        // крестик в поле поиск //*[@id="docs-findbar-id"]/div/div/div[1]/div[2]/div/div/div
                        WebElement searchBox1 = driver.findElement(By
                                .xpath("//*[@id=\"docs-findbar-id\"]/div/div/div[1]/div[2]/div/div/div/div"));
                        searchBox1.click();
                        Thread.sleep(500);  // Let the user actually see something!

   // для панды, чтобы взять НАЗВАНИЕ ТОВАРА, каретку влево на нужное кол-во позиций - до колонки I
                        for (int k = 0; k < 6; k++)
                        {
                            Robot robot2 = null;
                            try {
                                robot2 = new Robot();
                                robot2.keyPress(KeyEvent.VK_LEFT);
                                Thread.sleep(200);
                            } catch (AWTException e) {
                                e.printStackTrace();
                            }
                        }
                        Thread.sleep(1_200);  // Let the user actually see something!
                        // строка формул //*[@id="t-formula-bar-input"]/div
                        WebElement strokaFormul = driver.findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                        // берём текст из ячейки
                        String nameOfGood = strokaFormul.getText();
                        foundInfoFmMonitoringList.add(nameOfGood);
                        nameOfGoodsSet.add(nameOfGood);

 // для панды, чтобы ID взять, каретку влево на нужное кол-во позиций - до колонки C
                        for (int k = 0; k < 6; k++)
                        {
                            Robot robot1 = null;
                            try {
                                robot1 = new Robot();
                                robot1.keyPress(KeyEvent.VK_LEFT);
                                Thread.sleep(200);
                            } catch (AWTException e) {
                                e.printStackTrace();
                            }
                        }

                        Thread.sleep(1_200);  // Let the user actually see something!

                        // берём текст из ячейки
                        String idFromYacheika = strokaFormul.getText();
                        if(idFromYacheika.length() > 5) idFromYacheika = "";
                        foundInfoFmMonitoringList.add(idFromYacheika);
                        foundIdFmMonitoringSet.add(idFromYacheika);
                        //System.out.println("********* " + foundIdFmMonitoringSet);
                        Thread.sleep(1_000);  // Let the user actually see something!

                        //if(!findBar1of1String.equalsIgnoreCase("1 из 1")) indexIdInArr++;
                    }
                    if(a == words.length-1) break;
                }
            } else if (words[i].contains("Номер") && words[i+1].contains("ТС"))
            {
                //for (int j = 2; j < 4; j++)
                //{
                    int a = i + 2;
                    String qwe = words[a].replaceAll(" ","").replaceAll(",","");
                //System.out.println("qwe " + qwe);
                    String autoNumber = qwe.replace(".","");
                    //System.out.println("7777777000 " + autoNumber);
                    foundInfoFmMonitoringList.add(autoNumber);

                    // открыть окно поиска для ввода номера контейнера
                    driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"f");

                    // поле поиск //*[@id="docs-findbar-input"]/table/tbody/tr/td[1]/input
                    WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[1]/input"));
                    Thread.sleep(800);  // Let the user actually see something!

                    //пробую искать существующий номер авто
                    searchBox.sendKeys(autoNumber + Keys.RETURN); // существующий

                    Thread.sleep(20_000);  // Let the user actually see something!

                    // в окне поиска взять 1из1    //*[@id="docs-findbar-input"]/table/tbody/tr/td[2]/span
                    WebElement findBar1of1 = driver.findElement(By
                            .xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[2]/span"));
                    String findBar1of1String = findBar1of1.getText();
                    if(findBar1of1String.equalsIgnoreCase("0 из 0")) a0iz0++;

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

                    // крестик в поле поиск //*[@id="docs-findbar-id"]/div/div/div[1]/div[2]/div/div/div
                    WebElement searchBox1 = driver.findElement(By
                            .xpath("//*[@id=\"docs-findbar-id\"]/div/div/div[1]/div[2]/div/div/div/div"));
                    searchBox1.click();
                    Thread.sleep(500);  // Let the user actually see something!

                // для панды, чтобы взять НАЗВАНИЕ ТОВАРА, каретку влево на 4 позиций
                for (int k = 0; k < 6; k++)
                {
                    Robot robot2 = null;
                    try {
                        robot2 = new Robot();
                        robot2.keyPress(KeyEvent.VK_LEFT);
                        Thread.sleep(200);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(1_200);  // Let the user actually see something!
                // строка формул //*[@id="t-formula-bar-input"]/div
                WebElement strokaFormul = driver.findElement(By.xpath("//*[@id=\"t-formula-bar-input\"]/div"));
                // берём текст из ячейки
                String nameOfGood = strokaFormul.getText();
                foundInfoFmMonitoringList.add(nameOfGood);
                nameOfGoodsSet.add(nameOfGood);

                // для панды, чтобы ID взять, каретку влево на 10 позиций
                for (int k = 0; k < 6; k++)
                    {
                        Robot robot1 = null;
                        try {
                            robot1 = new Robot();
                            robot1.keyPress(KeyEvent.VK_LEFT);
                            Thread.sleep(200);
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(1_200);  // Let the user actually see something!

                    // берём текст из ячейки
                    String idFromYacheika = strokaFormul.getText();
                    if(idFromYacheika.length() > 5) idFromYacheika = "";
                    foundInfoFmMonitoringList.add(idFromYacheika);
                    foundIdFmMonitoringSet.add(idFromYacheika);


                //}
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

        driver.quit(); // закрывает окно
        //System.out.println("idFromYacheika1 " + idFromYacheika1);
        return idFromYacheika1;
    }
}
