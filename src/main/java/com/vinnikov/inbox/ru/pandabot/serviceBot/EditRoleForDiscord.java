package com.vinnikov.inbox.ru.pandabot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static com.vinnikov.inbox.ru.pandabot.PandabotApplication.LOGGER;

public class EditRoleForDiscord
{ // формирование специальной формы роли для дискорда
    private static final HashMap<String, String> companyAndRole1 = new HashMap<>((Map.of(
            "АБ МАРКЕТ","<@&786263954664980522>",
            "АВЕНТА","<@&786263759110537266>",
            "АГРОКРОПС","<@&801719868382249021>",
            "Априко","<@&786265238524657664>",
            "АУТСПАН","<@&788315732575911937>",
            "АИ","<@&791279992788877312>",
            "АЛЬФА ФУД ИНГРЕДИЕНТС", "<@&894976191927312407>",
            "АНАНАС СПБ", "<@&904739386132930641>",
            "БАРРИ КАЛЛЕБАУТ","<@&788317520250798129>",
            "БОГОРОДСКАЯ","<@&798079048802631720>"
    )));
    private static final HashMap<String, String> companyAndRole2 = new HashMap<>((Map.of(
            "ВИТАНОВА","<@&795671648379863080>",
            "ВИЛОМИКС","<@&788317524642627584>",
            "ВАХТЕЛЬ-СЕРВИС","<@&842048737014710283>",
            "ГРИН ЛАЙН","<@&842773528641798204>",
            "ГРИНКОФ","<@&786265150297997372>",
            "ДЕКЛАРАНТ","<@&785778541060292628>",
            "ДОКТОР АППЕТИТ","<@&867778090939580416>",
            "ИЛАЙ","<@&867777814522888192>",
            "ИП БЕЛОГУБ","<@&869126132203675668>",
            "И ГРУПП","<@&788317522683756544>"
    )));
    private static final HashMap<String, String> companyAndRole3 = new HashMap<>((Map.of(
            "КРЕС-НЕВА","<@&788065434783842355>",
            "КНАУФ","<@&786264968478720000>",
            "КОМИНТРЕЙД","<@&788316522291527711>",
            "Контейнершипс (МАКАЛПАЙН)","<@&788066649790087229> (МАКАЛПАЙН)",
            "Контейнершипс (ГЛАТФЕЛТЕР)","<@&788066649790087229> (ГЛАТФЕЛТЕР)",
            "Контейнершипс (САМАРАМАЛТ)","<@&788066649790087229> (САМАРАМАЛТ)",
            "КОСМОПЛЕНКА","<@&789040117947957259>",
            "КОНСЕРВЫ","<@&803228389863063554>",
            "КОСМОПЛЁНКА","<@&789040117947957259>",
            "КАЙСА","<@&786265896041709589>"
    )));
    private static final HashMap<String, String> companyAndRole4 = new HashMap<>((Map.of(
            "ЛОДЖИСТИК-ВК","<@&803228389863063554> (ЛОДЖИСТИК-ВК)",
            "ЛЕКС","<@&786265397795487804>",
            "МАСТЕРФУД","<@&843776075803656202>",
            "МАРС","<@&788065861676040222>",
            "НИКА","<@&788316526011875369>",
            "НУТРИЦИЯ","<@&786264260208623618>",
            "ОРЕШКИН","<@&789060521327460383>",
            "ПОРТ","<@&785808375782309908>",
            "ПРОММАШ","<@&788359648147013642>",
            "ПСервис (ЛЕБО)","<@&786264260577722378>"
    )));
    private static final HashMap<String, String> companyAndRole5 = new HashMap<>((Map.of(
            "РАС","<@&788359650046640160>",
            "РОТ-ФРОНТ","<@&788065936129130526>",
            "РФ","<@&788065936129130526>",
            "СМАРТТУЛЗ","<@&788318404258955326>",
            "СТАР ПРЕМИУМ","<@&860478614645374996>",
            "СЕЛЛ-СЕРВИС","<@&786265460055212122>",
            "СЕМУШКА","<@&786265800775827476>",
            "СОЮЗАГРО","<@&801802702148468737>",
            "СПЕКТР ФУД","<@&808217270593257473>",
            "СКИМ","<@&855148505446613082>"
    )));
    private static final HashMap<String, String> companyAndRole6 = new HashMap<>((Map.of(
            "ТД-ХОЛДИНГ","<@&788318407367721030>",
            "ТДМ","<@&788318405877956648>",
            "ТАТА","<@&788687981552336917>",
            "ТЕХНО-КОМПЛЕКТ","<@&893036805841100801>",
            "ТРАНС СПЕКТР","<@&839108766812864533>",
            "УНИТРОН ПРОМ","<@&786264611808215110>",
            "Ф.УНИ","<@&786264725804679178>",
            "ФП","<@&788361393418141696>",
            "Фрутимпэкс","<@&786264495688515626>",
            "ФЕС ПРОДУКТ","<@&842773673031106581>"
    )));
    private static final HashMap<String, String> companyAndRole7 = new HashMap<>((Map.of(
            "ХИМСПЕКТР","<@&819952014406058024>",
            "ЦЕНТАВР","<@&898539665068539905>",
            "ЦентрСнаб","<@&818129215093604372>",
            "ЧАЙНО-КОФЕЙНАЯ МАНУФАКТУРА","<@&847849871507324928>",
            "ЯКОБС (МЕКСИКА)","<@&788066297434734603>",
            "ЯКОБС","<@&786263609495781376>"
    )));

    private static String compNameFmMessage;
    private static String compNameRoleToDiscord;

    public static String getRoleForDiscord(String msgToEdit)
    {
        companyAndRole2.put("ЗУЛАЛ ФУД","<@&926080936754421810>");
        companyAndRole2.put("ИМПЭНЕРГО","<@&788359646204395531>");
        companyAndRole3.put("КОНКОРД","<@&789137474609479690>");
        companyAndRole4.put("ПК АЗОВСКИЙ","<@&867777557063008347>");
        companyAndRole4.put("ЛЕНТА","<@&910087940552675368>");
        companyAndRole5.put("СДМ","<@&893036349773475840>");
        companyAndRole5.put("СКМ","<@&904676081112604682>");
        companyAndRole5.put("РосПродукт","<@&849651401071525948>");
        String msgToDiscordWithRole = "";

        // из сообщения взять название фирмы
// 4435+4436 DE ИТС 4,725 , *Контейнершипс (ГЛАТФЕЛТЕР)*, 10216170/050921/0266450, Выпуск.  Контейнеры: CNEU4566536
// , AMCU8002851,  Инспектор: ГУМАРОВА ЕЛЕНА ЮРЬЕВНА
        String[] arrWordsFmMsgToEdit = msgToEdit.split("\\*");
        compNameFmMessage = arrWordsFmMsgToEdit[1];

        // по названию фирмы из сообщения взять нужную роль из хэшмапа
        compNameRoleToDiscord = null;
        if(compNameFmMessage.startsWith("А") || compNameFmMessage.startsWith("Б"))
        {
            for (HashMap.Entry entry: companyAndRole1.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        } else
        if(compNameFmMessage.startsWith("В") || compNameFmMessage.startsWith("Г") || compNameFmMessage.startsWith("Д")
                || compNameFmMessage.startsWith("Е") || compNameFmMessage.startsWith("Ё") || compNameFmMessage.startsWith("Ж")
                || compNameFmMessage.startsWith("З") || compNameFmMessage.startsWith("И"))
        {
            for (HashMap.Entry entry: companyAndRole2.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        } else
        if(compNameFmMessage.startsWith("К"))
        {
            for (HashMap.Entry entry: companyAndRole3.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        } else
        if(compNameFmMessage.startsWith("Л") || compNameFmMessage.startsWith("М")
                || compNameFmMessage.startsWith("Н") || compNameFmMessage.startsWith("О")
                || compNameFmMessage.startsWith("П"))
        {
            for (HashMap.Entry entry: companyAndRole4.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        } else
        if(compNameFmMessage.startsWith("Р") || compNameFmMessage.startsWith("С"))
        {
            for (HashMap.Entry entry: companyAndRole5.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        } else
        if(compNameFmMessage.startsWith("Т") || compNameFmMessage.startsWith("У")
                || compNameFmMessage.startsWith("Ф") )
        {
            for (HashMap.Entry entry: companyAndRole6.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        } else
        if(compNameFmMessage.startsWith("Х") || compNameFmMessage.startsWith("Ц") || compNameFmMessage.startsWith("Ч")
                || compNameFmMessage.startsWith("Ш") || compNameFmMessage.startsWith("Щ")
                || compNameFmMessage.startsWith("Ы") || compNameFmMessage.startsWith("Э")
                || compNameFmMessage.startsWith("Ю") || compNameFmMessage.startsWith("Я"))
        {
            for (HashMap.Entry entry: companyAndRole7.entrySet())
            {
                if(entry.getKey().equals(compNameFmMessage))
                {
                    //System.out.println("-----22:" + entry.getValue());
                    compNameRoleToDiscord = entry.getValue().toString();
                    //System.out.println("comp:" + comp + "!");
                    arrWordsFmMsgToEdit[1] = compNameRoleToDiscord;
                    break;
                }
            }
        }

        // формирование итогового сообщения с ролью для отправки в дискорд
        if(compNameRoleToDiscord == null)
        {
            msgToDiscordWithRole = msgToEdit;
        } else
        {
            for (int i = 0; i < arrWordsFmMsgToEdit.length; i++)
            {
                msgToDiscordWithRole = msgToDiscordWithRole + arrWordsFmMsgToEdit[i];
            }
        }
        LOGGER.info("---EditRoleForDiscord---compNameRoleToDiscord-> " + LocalDateTime.now() + "_" + compNameRoleToDiscord +
                "\n---msgToDiscordWithRole:" + msgToDiscordWithRole);

        return msgToDiscordWithRole;
    }
}
