package com.vinnikov.inbox.ru.pandabot.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum EnumStatuses{
    REGISTERED_DT ("ДТ зарегистрирована"),
    RELEASED_DT ("Выпуск"),
    RELEASED_USLOVNO_121 ("Выпуск условный по стоимости"),
    RELEASED_USLOVNO_122 ("Выпуск условный по экспертизе"),
    RELEAS_PROHIBITED_DT ("Отказано в выпуске"),
    BROKER_REQUEST_SCAN ("Таможня запросила фА"), //"Мы запросили тпфк сканирование"),
    CSTMS_CODE_CHECK ("Доп. проверка по ТНВЭД"),
    DOSMOTR ("ДОСМОТР!"),
    CSTMS_PRICE_CHECK ("Доп. проверка по стоимости");

    private final String title;

    EnumStatuses(String title) {
        this.title = title;
    }

}
