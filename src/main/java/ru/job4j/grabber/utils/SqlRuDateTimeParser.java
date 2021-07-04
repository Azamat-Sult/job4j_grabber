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
            parse = switch (split[1]) {
                case "янв" -> parse.replace("янв", "янв.");
                case "фев" -> parse.replace("фев", "февр.");
                case "мар" -> parse.replace("мар", "мар.");
                case "апр" -> parse.replace("апр", "апр.");
                case "май" -> parse.replace("май", "мая");
                case "июн" -> parse.replace("июн", "июн.");
                case "июл" -> parse.replace("июл", "июл.");
                case "авг" -> parse.replace("авг", "авг.");
                case "сен" -> parse.replace("сен", "сент.");
                case "окт" -> parse.replace("окт", "окт.");
                case "ноя" -> parse.replace("ноя", "нояб.");
                case "дек" -> parse.replace("дек", "дек.");
                default -> throw new IllegalStateException("Unexpected value: " + split[1]);
            };
        }

        if (split.length == 2) {
            parse = switch (split[0]) {
                case "сегодня," -> LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("d MMM yy, ")) + split[1];
                case "вчера," -> LocalDate.now()
                        .minusDays(1).format(DateTimeFormatter.ofPattern("d MMM yy, ")) + split[1];
                default -> throw new IllegalStateException("Unexpected value: " + split[0]);
            };
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
