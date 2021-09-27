package com.vinnikov.inbox.ru.pandabot;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class EntityPutInPostgreSQL
{       // обработка сообщения, получить и наполнить элементы сущности

    private static Connection connection;
    private static Statement statement;
    private static String nameOfTable = "tb_panda_gtd";

    public static EntityForPostgreSQL fillInEntity (String msgForWhatsap)
    {
// 4495 БЕЗ ПРЕФ , *МАСТЕРФУД*, 10216170/250921/0287469, ДТ зарегистрирована., TLLU2073620,  АВТОРЕГИСТРАЦИЯ,
// 4495 БЕЗ ПРЕФ , *МАСТЕРФУД*, 10216170/250921/0287469, Выпуск,  TLLU2073620,  Инспектор: ИБРАГИМОВ ИЛЬЯС МАГОМЕДОВИЧ
        LOGGER.info("---EntityPutInPostgreSQL fillInEntity msgForWhatsap-> " + LocalDateTime.now() + "\n" + msgForWhatsap);
        EntityForPostgreSQL entity = new EntityForPostgreSQL();

        // фразу сообщения в массив разделение по запятой
        String[] arrWords = msgForWhatsap.split(",");
        int indexNumbGTD = -1, indexInspektorAvtoRegVyp = -1;

        // найти индексы номера гтд и инспектор или авторег/автовып
        for (int i = 0; i < arrWords.length; i++)
        {
            if(arrWords[i].contains("10") && arrWords[i].contains("/") && arrWords[i].length() > 22)
                indexNumbGTD = i;
            if(arrWords[i].contains("нспектор") || arrWords[i].contains("АВТОРЕГИСТРАЦИ")
                    || arrWords[i].contains("АВТОВЫПУСК"))
                indexInspektorAvtoRegVyp = i;
        }

        // private String commentWithMonitorID;
        String commentWithMonitorID = "";
        for (int i = 0; i < (indexNumbGTD - 1); i++)
        {
            commentWithMonitorID = commentWithMonitorID + arrWords[i] + ", ";
        }
        entity.setCommentWithMonitorID(commentWithMonitorID);

        // private String companyName;
        String companyName = arrWords[indexNumbGTD - 1].replaceAll("\\*","");
        entity.setCompanyName(companyName);

        // private String numberGTD; 10216170/250921/0287469
        String numberGTD = arrWords[indexNumbGTD];
        entity.setNumberGTD(numberGTD);

        // private String prefixNumberGTD;
        String[] arrNumberGTD = numberGTD.split("/");
        String prefixNumberGTD = arrNumberGTD[0];
        entity.setPrefixNumberGTD(prefixNumberGTD);

        // private long digitsNumberGTD;
        long digitsNumberGTD = Long.parseLong(arrNumberGTD[2]);
        entity.setDigitsNumberGTD(digitsNumberGTD);

        // private String dateRegistred;
        String dateInGTD = arrNumberGTD[1];
        String dayReg = dateInGTD.substring(0,2);
        String monthReg = dateInGTD.substring(2,4);
        String yearReg = dateInGTD.substring(4,6);
        String dateReg = dayReg + "/" + monthReg + "/" + yearReg;
        entity.setDateRegistred(dateReg);

        // private String dateReleased;
        String statusRegistReleas = arrWords[indexNumbGTD + 1];
        String dateReleased = null;
        if(statusRegistReleas.contains("ыпус"))
        {
            LocalDate localDate = LocalDate.now();
            String dayReleased = localDate.getDayOfMonth() + "";
            String monthReleased = localDate.getMonthValue() + "";
            String yearReleased = localDate.getYear() + "";
            dateReleased = dayReleased + "/" + monthReleased + "/" + yearReleased;
        }
        entity.setDateReleased(dateReleased);

        // private String statusRegistReleas;
        entity.setStatusRegistReleas(statusRegistReleas);

        // private String containerNumber;
        String containerNumber = "";
        int flagIsThereInspector = 1;
        if(indexInspektorAvtoRegVyp < 0) // это значит, что инспектор или авторег/автовып отсутствует
        {
            indexInspektorAvtoRegVyp = arrWords.length;
            flagIsThereInspector = 2;
        }
        for (int i = (indexNumbGTD + 2); i < indexInspektorAvtoRegVyp; i++)
        {
            containerNumber = containerNumber + arrWords[i] + ", ";
        }
        entity.setContainerNumber(containerNumber);

        // private String inspector;
        String inspector = "";
        if(flagIsThereInspector == 2) inspector = ".";
        else inspector = arrWords[indexInspektorAvtoRegVyp];
        entity.setInspector(inspector);

        LOGGER.info("---EntityPutInPostgreSQL fillInEntity сама сущность-> " + LocalDateTime.now() + "\n" + entity);
        return entity;
    }

    public static void insertIntoTables(EntityForPostgreSQL entity)
    {
        // получить заполненную сущность и внести в РБД ПостгреСКуЭль
        try
        {
            Class.forName("org.postgresql.Driver"); // http://www.team55.ru/2015/02/postgresql-java.html
            connection = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:5432/lessons","grig", "!Academy2002");
            connection.setAutoCommit(false); // 127.0.0.1   localhost
            LOGGER.info("---EntityPutInPostgreSQL insertIntoTables 125-- Opened database successfully-> "
                    + LocalDateTime.now());
            String sql;

            //-------------- CREATE TABLE ---------------
            statement = connection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS " + nameOfTable + " " +
                    "(" +
                    "ID         INT PRIMARY KEY NOT NULL, " +
                    "commentWithMonitorID TEXT  NOT NULL, " +
                    "companyName          TEXT  NOT NULL, " +
                    "numberGTD            TEXT  NOT NULL, " +
                    "prefixNumberGTD      TEXT  NOT NULL, " +
                    "digitsNumberGTD      LONG  NOT NULL, " +
                    "statusRegistReleas   TEXT  NOT NULL, " +
                    "dateRegistred        TEXT  NOT NULL, " +
                    "dateReleased         TEXT  NOT NULL, " +
                    "containerNumber      TEXT  NOT NULL, " +
                    "inspector            TEXT  NOT NULL" +
                    ")";
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            LOGGER.info("---EntityPutInPostgreSQL insertIntoTables 148--Table created successfully-> " + LocalDateTime.now());

            //--------------- INSERT ROWS ---------------
// sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (1, 'Paul', 32, 'California', 20000.00 );";
            statement = connection.createStatement();
            // айди автоматически присваивается
            sql = "INSERT INTO " + nameOfTable + " (commentWithMonitorID,companyName,numberGTD,prefixNumberGTD," +
                  "digitsNumberGTD,statusRegistReleas,dateRegistred,dateReleased,containerNumber,inspector) VALUES (" +
                    entity.getCommentWithMonitorID() + ", " +
                    entity.getCompanyName() + ", " +
                    entity.getNumberGTD() + ", " +
                    entity.getPrefixNumberGTD() + ", " +
                    entity.getDigitsNumberGTD() + ", " +
                    entity.getStatusRegistReleas() + ", " +
                    entity.getDateRegistred() + ", " +
                    entity.getDateReleased() + ", " +
                    entity.getContainerNumber() + ", " +
                    entity.getInspector() +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            LOGGER.info("---EntityPutInPostgreSQL insertIntoTables 170--Records created successfully-> "
                    + LocalDateTime.now() + "\n" + sql);

            //-------------- DELETE DATA ----------------------
            /*statement = connection.createStatement();
            sql = "DELETE from tb_info_gtd_mail where ID=3;";
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
            System.out.println("-- Operation DELETE done successfully");*/

            //-------------- UPDATE DATA ------------------
            /*statement = connection.createStatement();
            sql = "UPDATE " + nameOfTable + " SET companyName = 25000.00 WHERE ID=3;";
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
            System.out.println("-- Operation UPDATE done successfully");*/

            //--------------- SELECT DATA ------------------
            /*statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery( "SELECT * FROM COMPANY;" );
            while ( resultSet.next() ) {
                int id = resultSet.getInt("id");
                String  name = resultSet.getString("name");
                int age  = resultSet.getInt("age");
                String  address = resultSet.getString("address");
                float salary = resultSet.getFloat("salary");
                System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s",id,name,age,address,salary));
            }
            resultSet.close();
            statement.close();
            connection.commit();
            System.out.println("-- Operation SELECT done successfully");*/

            connection.close();

        } catch (SQLException throwables) {
            LOGGER.error("---EntityPutInPostgreSQL insertIntoTables 208 SQLException-> "
                    + LocalDateTime.now() + "\n" + throwables);
        } catch (ClassNotFoundException e) {
            LOGGER.error("---EntityPutInPostgreSQL insertIntoTables 211 ClassNotFoundException-> "
                    + LocalDateTime.now() + "\n" + e);
            e.printStackTrace();
        }
    }
}
