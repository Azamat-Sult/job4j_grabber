package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row1 = doc.select(".postslisttopic");
        Elements row2 = doc.select("td:nth-child(6)");
        for (int index = 0; index < row1.size(); index++) {
            Element href = row1.get(index).child(0);
            System.out.println("-----------------------------------------------------");
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            System.out.println(row2.get(index).text());
        }
    }
}