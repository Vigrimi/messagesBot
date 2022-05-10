package com.vinnikov.inbox.ru.pandabot.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum EnumTextConstant {
    FILENAME_BUFFER_GOOGLE_EXCEL ("./secrets/google_excel.txt"),
    FILENAME_EMAIL_LOGIN ("./secrets/email_login_my.txt"),
    FILENAME_EMAIL_PASSWORD ("./secrets/email_password_my.txt"),
    FILE_PATH_COUNTOLD ("D:\\ITMO PandaBot\\pandabot\\countOld.txt"),
    FILE_PATH_OUTLOOK_GR ("C:\\Users\\Гриша\\AppData\\Local\\Microsoft\\Outlook"),
    FILENAME_OUTLOOK_GR ("Outlptpsk@yandex.ru-00000005");

    private final String text;

    EnumTextConstant(String text) {
        this.text = text;
    }
}
