package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {

        String[] split = parse.split(" ");

        if (split.length == 4) {
            switch (split[1]) {
                case "янв" -> parse = parse.replace("янв", "янв.");
                case "фев" -> parse = parse.replace("фев", "февр.");
                case "мар" -> parse = parse.replace("мар", "мар.");
                case "апр" -> parse = parse.replace("апр", "апр.");
                case "май" -> parse = parse.replace("май", "мая");
                case "июн" -> parse = parse.replace("июн", "июн.");
                case "июл" -> parse = parse.replace("июл", "июл.");
                case "авг" -> parse = parse.replace("авг", "авг.");
                case "сен" -> parse = parse.replace("сен", "сент.");
                case "окт" -> parse = parse.replace("окт", "окт.");
                case "ноя" -> parse = parse.replace("ноя", "нояб.");
                case "дек" -> parse = parse.replace("дек", "дек.");
            }
        }

        if (split.length == 2) {
            switch (split[0]) {
                case "сегодня," -> {
                    String todayDate =
                            LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("d MMM yy, "));
                    parse = todayDate + split[1];
                }
                case "вчера," -> {
                    String yesterdayDate =
                            LocalDate.now()
                                    .minusDays(1)
                                    .format(DateTimeFormatter.ofPattern("d MMM yy, "));
                    parse = yesterdayDate + split[1];
                }
            }
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("d MMM yy, HH:mm", new Locale("ru", "RU"));
        return LocalDateTime.parse(parse, formatter);
    }

    public static void main(String[] args) {
        String dateTime = "вчера, 13:43";
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        System.out.println(parser.parse(dateTime));
    }

}
