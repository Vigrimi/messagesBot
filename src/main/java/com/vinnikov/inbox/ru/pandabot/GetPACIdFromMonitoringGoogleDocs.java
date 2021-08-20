package com.vinnikov.inbox.ru.pandabot;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class GetPACIdFromMonitoringGoogleDocs
{
    public static String getPACIdFromMonitoringGoogleDocs(String msgToDiscord1) throws InterruptedException
    {
        String idFromYacheika1 = "";
        String idFromYacheika = "";
        String nameOfGood = "";
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
            if(words[i].contains("ТН:"))
            {
                String conosament = words[i+1];

                // открыть окно поиска для ввода номера контейнера
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"f");

                // поле поиск //*[@id="docs-findbar-input"]/table/tbody/tr/td[1]/input
                WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"docs-findbar-input\"]/table/tbody/tr/td[1]/input"));
                Thread.sleep(800);  // Let the user actually see something!

                //пробую искать существующий номер коносамента-разнорядки
                searchBox.sendKeys(conosament + Keys.RETURN); // существующий
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

                // для панды, чтобы взять НАЗВАНИЕ ТОВАРА, каретку влево на 4 позиций
                for (int k = 0; k < 4; k++)
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
                /*String*/ nameOfGood = strokaFormul.getText();
                foundInfoFmMonitoringList.add(nameOfGood);
                nameOfGoodsSet.add(nameOfGood);

                // для панды, чтобы ID взять, каретку влево на 6 позиций
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
                /*String*/ idFromYacheika = strokaFormul.getText();
                if(idFromYacheika.length() > 5) idFromYacheika = "";
                //System.out.println("********* " + foundIdFmMonitoringSet);
                Thread.sleep(1_000);  // Let the user actually see something!

                break;
            }
        }
        driver.quit(); // закрывает окно
        // формируем номера айди для текста
        idFromYacheika1 = idFromYacheika + ", " + nameOfGood + ", " ;
        return idFromYacheika1;
    }
}
