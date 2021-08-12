package com.vinnikov.inbox.ru.pandabot;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.*;

public class PutTextInDatabase
{
    //private EntityManager manager;

    public PutTextInDatabase(/*EntityManager manager*/) {
        /*this.manager = manager;*/
    }

    public static void insertIntoTables(String info)
    {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpalesson");
        EntityManager manager = factory.createEntityManager();

        PutSuschnostInfo putSuschnostInfo = new PutSuschnostInfo(info);

        System.out.println("\n22AAA " + putSuschnostInfo);

        try {
            manager.getTransaction().begin();
            manager.persist(putSuschnostInfo);
            manager.getTransaction().commit();
        } catch (PersistenceException exception) //(SqlExceptionHelper exceptionHelper)
        {
            System.out.println(exception);
        }


    }
}
