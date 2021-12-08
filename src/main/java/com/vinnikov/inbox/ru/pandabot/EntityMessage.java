package com.vinnikov.inbox.ru.pandabot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;

//@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EntityMessage implements EntMsgInFileCSVInterf
{
    private String comment;
    private String companyName;
    private String numberDT;
    private String dolKgRbn;
    private String statusDT;
    private String transportNumber;
    private String inspector;

//    public static void saveEntityInFileCSV(EntityMessage entityMessage)
//    {
//        String lineInFile =
//    }

    @Override
    public String toString()
    {
        return "" + comment + ", " + companyName + ", " + numberDT + ", " + dolKgRbn + ", " +
                statusDT + ", " + transportNumber + ", " + inspector;
//                "EntityMessage{" +
//                "comment='" + comment + '\'' +
//                ", companyName='" + companyName + '\'' +
//                ", numberDT='" + numberDT + '\'' +
//                ", statusDT='" + statusDT + '\'' +
//                ", transportNumber='" + transportNumber + '\'' +
//                ", inspector='" + inspector + '\'' +
//                '}';
    }
}
