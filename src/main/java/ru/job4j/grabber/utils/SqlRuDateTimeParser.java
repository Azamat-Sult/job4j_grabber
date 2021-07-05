package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {

        Map<String, String> months = Map.ofEntries(
                entry("янв", "янв."),
                entry("фев", "февр."),
                entry("мар", "мар."),
                entry("апр", "апр."),
                entry("май", "мая"),
                entry("июн", "июн."),
                entry("июл", "июл."),
                entry("авг", "авг."),
                entry("сен", "сент."),
                entry("окт", "окт."),
                entry("ноя", "нояб."),
                entry("дек", "дек.")
        );

        String[] split = parse.split(" ");

        if (split.length == 4) {
            parse =  parse.replace(split[1], months.get(split[1]));
        }

        if (split.length == 2) {
            if (split[0].equals("сегодня,")) {
                parse = LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("d MMM yy, ")) + split[1];
            }
            if (split[0].equals("вчера,")) {
                parse = LocalDate.now()
                        .minusDays(1).format(DateTimeFormatter.ofPattern("d MMM yy, ")) + split[1];
            }
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("d MMM yy, HH:mm", new Locale("ru", "RU"));
        return LocalDateTime.parse(parse, formatter);
    }

    public static void main(String[] args) {
        String dateTime = "вчера, 17:29";
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        System.out.println(parser.parse(dateTime));
    }
}
