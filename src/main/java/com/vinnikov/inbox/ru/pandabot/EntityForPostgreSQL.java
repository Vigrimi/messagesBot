package com.vinnikov.inbox.ru.pandabot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tb_panda_gtd")
public class EntityForPostgreSQL // Сущность с инфой для РБД
{
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автоинкремент - значения увеличиваются на единицу
    private int id;

    @Column(nullable = false,length = 20_000)
    private String commentWithMonitorID;

    @Column(nullable = false,length = 20_000)
    private String companyName;

    @Column(nullable = false,length = 20_000)
    private String numberGTD;

    @Column(nullable = false,length = 20_000)
    private String prefixNumberGTD;

    @Column(nullable = false)
    private long digitsNumberGTD;

    @Column(nullable = false,length = 20_000)
    private String dateRegistred;

    @Column(length = 20_000)
    private String dateReleased;

    @Column(nullable = false,length = 20_000)
    private String statusRegistReleas;

    @Column(nullable = false,length = 20_000)
    private String containerNumber;

    @Column(nullable = false,length = 20_000)
    private String inspector;

    public EntityForPostgreSQL() {}

    public EntityForPostgreSQL(String commentWithMonitorID, String companyName, String numberGTD,
                               String prefixNumberGTD, long digitsNumberGTD, String dateRegistred, String dateReleased,
                               String statusRegistReleas, String containerNumber, String inspector)
    {
        this.commentWithMonitorID = commentWithMonitorID;
        this.companyName = companyName;
        this.numberGTD = numberGTD;
        this.prefixNumberGTD = prefixNumberGTD;
        this.digitsNumberGTD = digitsNumberGTD;
        this.dateRegistred = dateRegistred;
        this.dateReleased = dateReleased;
        this.statusRegistReleas = statusRegistReleas;
        this.containerNumber = containerNumber;
        this.inspector = inspector;
    }

    @Override
    public String toString()
    {
        return "EntityForPostgreSQL{" +
                "id=" + id +
                ", commentWithMonitorID='" + commentWithMonitorID + '\'' +
                ", companyName='" + companyName + '\'' +
                ", numberGTD='" + numberGTD + '\'' +
                ", prefixNumberGTD='" + prefixNumberGTD + '\'' +
                ", digitsNumberGTD=" + digitsNumberGTD +
                ", dateRegistred='" + dateRegistred + '\'' +
                ", dateReleased='" + dateReleased + '\'' +
                ", statusRegistReleas='" + statusRegistReleas + '\'' +
                ", containerNumber='" + containerNumber + '\'' +
                ", inspector='" + inspector + '\'' +
                '}';
    }
}
