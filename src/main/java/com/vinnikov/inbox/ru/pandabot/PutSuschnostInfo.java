package com.vinnikov.inbox.ru.pandabot;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tb_info_gtd_mail")
public class PutSuschnostInfo
{
    @Id // первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автоинкремент - значения увеличиваются на единицу
    private int id;

    @Column(nullable = false,length = 20_000)
    private String infoText;

    public PutSuschnostInfo(String infoText) {
        this.infoText = infoText;
    }

    @Override
    public String toString() {
        return "PutSuschnostInfo{" +
                "id=" + id +
                ", infoText='" + infoText + '\'' +
                '}';
    }
}
