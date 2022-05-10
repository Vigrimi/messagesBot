package com.vinnikov.inbox.ru.pandabot.serviceBot;

import java.time.LocalDateTime;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public interface IEditTextsGetRoleFmCompanyName{
    default String getRoleFmCompanyNameInterf(String companyNameFmMail){
        String roleForDiscord = "*" + companyNameFmMail + "*";
        LOGGER.info("--Interf-EditTextsFmEmail--getRoleFmCompanyNam-> " + LocalDateTime.now() + "\n" + roleForDiscord);

        if (roleForDiscord.contains("ДЕТСКОЕ") && roleForDiscord.contains("ПИТАНИЕ")) roleForDiscord = "*НУТРИЦИЯ*";
        if (roleForDiscord.contains("КОМПАНИЯ") && roleForDiscord.contains("ПРОДУКТ")
                && roleForDiscord.contains("СЕРВИС")) roleForDiscord = "*ПСервис (ЛЕБО)*";
        if (roleForDiscord.contains("ФИННПАК")) roleForDiscord = "*ФП*";
        if (roleForDiscord.contains("ТПП") && roleForDiscord.contains("ВКУСНЫЕ")) roleForDiscord = "*КОНСЕРВЫ*";
        if (roleForDiscord.contains("АГРОИМПЭКС")) roleForDiscord = "*АИ*";
        if (roleForDiscord.contains("АБ-МАРКЕТ")) roleForDiscord = "*АБ МАРКЕТ*";
        if (roleForDiscord.contains("РУСАГРО-САХАР")) roleForDiscord = "*РАС*";
        if (roleForDiscord.contains("ГРУППА") && roleForDiscord.contains("КОМПАНИЙ")) roleForDiscord = "*РАС*";
        if (roleForDiscord.contains("ЦЕНТРСНАБ")) roleForDiscord = "*ЦентрСнаб*";
        if (roleForDiscord.contains("ФРУТИМПЭКС")) roleForDiscord = "*Фрутимпэкс*";
        if (roleForDiscord.contains("ЛЕКС")) roleForDiscord = "*ЛЕКС*";
        if (roleForDiscord.contains("ТД") && roleForDiscord.contains("ДОКТОР")) roleForDiscord = "*ДОКТОР АППЕТИТ*";
        if (roleForDiscord.contains("@И") && roleForDiscord.contains("ГРУПП")) roleForDiscord = "*И Групп*";
        if (roleForDiscord.contains("ГЛАТФЕЛТЕР")) roleForDiscord = "*Контейнершипс (ГЛАТФЕЛТЕР)*";
        if (roleForDiscord.contains("МАКАЛПАЙН")) roleForDiscord = "*Контейнершипс (МАКАЛПАЙН)*";
        if (roleForDiscord.contains("САМАРАМАЛТ")) roleForDiscord = "*Контейнершипс (САМАРАМАЛТ)*";
        if (roleForDiscord.contains("ТД") && roleForDiscord.contains("АПРИКО")) roleForDiscord = "*Априко*";
        if (roleForDiscord.contains("ФЕС") && roleForDiscord.contains("ПРОДУКТ")) roleForDiscord = "*ФЕС ПРОДУКТ*";
        if (roleForDiscord.contains("АУТСПАН") && roleForDiscord.contains("ИНТЕРНЕШНЛ")) roleForDiscord = "*АУТСПАН*";
        if (roleForDiscord.contains("ТОРГОВЫЙ") && roleForDiscord.contains("ДОМ")) roleForDiscord = "*ТДМ*";
        if (roleForDiscord.contains("ПИЩЕВОЙ") && roleForDiscord.contains("КОМБИНАТ")) roleForDiscord = "*ПК АЗОВСКИЙ*";
        if (roleForDiscord.contains("УНИТРОН") && roleForDiscord.contains("ПРОМ")) roleForDiscord = "*УНИТРОН ПРОМ*";
        if (roleForDiscord.contains("УНИТРОН") && roleForDiscord.contains("ФИРМА")) roleForDiscord = "*Ф.УНИ*";
        if (roleForDiscord.contains("АЛЬФА") && roleForDiscord.contains("ФУД")) roleForDiscord = "*АЛЬФА ФУД ИНГРЕДИЕНТС*";
        if (roleForDiscord.contains("ВИЛОМИКС") && roleForDiscord.contains("РУС")) roleForDiscord = "*ВИЛОМИКС*";

        return roleForDiscord;
    }
}
