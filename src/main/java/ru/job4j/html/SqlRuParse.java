package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {

    private static int pagesToParse = 5;

    public static void main(String[] args) throws Exception {
        for (int page = 1; page <= pagesToParse; page++) {
            System.out.println("////////////////////////////////////////////////////////");
            System.out.println("Parsing " + page + " page of " + pagesToParse + " pages");
            System.out.println("////////////////////////////////////////////////////////");
            System.out.println();
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + page).get();
            Elements row1 = doc.select(".postslisttopic");
            Elements row2 = doc.select("td:nth-child(6)");
            for (int index = 0; index < row1.size(); index++) {
                Element href = row1.get(index).child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(row2.get(index).text());
                System.out.println("-----------------------------------------------------------------");
            }
        }
    }
}