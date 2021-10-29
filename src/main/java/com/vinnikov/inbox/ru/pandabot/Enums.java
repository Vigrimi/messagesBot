package com.vinnikov.inbox.ru.pandabot;

public enum Enums 
{
    REGISTERED_DT ("ДТ зарегистрирована"),
    RELEASED_DT ("Выпуск"),
    RELEAS_PROHIBITED_DT ("Отказано в выпуске"),
    BROKER_REQUEST_SCAN ("Мы запросили тпфк сканирование"),
    CSTMS_CODE_CHECK ("Доп. проверка по ТНВЭД"),
    CSTMS_PRICE_CHECK ("Доп. проверка по стоимости");

    private String title;

    Enums(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Enums{" +
                "title='" + title + '\'' +
                '}';
    }
}
