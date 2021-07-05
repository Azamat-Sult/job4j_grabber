package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuParse {

    //private static final int pagesToParse = 5;

    public void parsePost(String url) throws IOException {
        Document page = Jsoup.connect(url).get();

        Elements postTextBlock
                = page.select("table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)");
        Elements postDateBlock
                = page.select("table:nth-child(3) > tbody > tr:nth-child(3) > td");

        Element postText = postTextBlock.get(0);
        String post = postText.text();
        System.out.println(post);

        Element postDate = postDateBlock.get(0);
        String[] split = postDate.text().split(" ");
        String dateOfPost = "";
        if (split.length == 8) {
            dateOfPost = split[0] + " " + split[1];
        }
        if (split.length == 10) {
            dateOfPost = split[0] + " " + split[1] + " " + split[2] + " " + split[3];
        }
        System.out.println(dateOfPost);
    }

    public static void main(String[] args) throws Exception {
        /*for (int page = 1; page <= pagesToParse; page++) {
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
        }*/
        SqlRuParse parser = new SqlRuParse();
        parser.parsePost("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
    }
}