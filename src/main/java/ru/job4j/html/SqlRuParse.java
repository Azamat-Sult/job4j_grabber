package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private static final int PAGES_TO_PARSE = 5;

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> rsl = new ArrayList<>();

        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");

        for (Element td : row) {
            Element href = td.child(0);
            String linkPost = href.attr("href");
            rsl.add(detail(linkPost));
        }

        return rsl;
    }

    @Override
    public Post detail(String link) throws IOException {

        Document postPage = Jsoup.connect(link).get();

        Elements postTitleBlock
                = postPage.select("td.messageHeader");
        Elements postTextBlock
                = postPage.select("td.msgBody");
        Elements postDateBlock
                = postPage.select("td.msgFooter");

        Element postTitle = postTitleBlock.get(0);
        Element postText = postTextBlock.get(1);
        Element postDate = postDateBlock.get(0);

        String[] split = postDate.text().split(" ");
        String dateOfPost = "";
        if (split.length == 8) {
            dateOfPost = split[0] + " " + split[1];
        }
        if (split.length == 10) {
            dateOfPost = split[0] + " " + split[1] + " " + split[2] + " " + split[3];
        }

        SqlRuDateTimeParser dtParser = new SqlRuDateTimeParser();

        Post rslPost = new Post();
        rslPost.setTitle(postTitle.text().replace(" [new]", ""));
        rslPost.setLink(link);
        rslPost.setDescription(postText.text());
        rslPost.setCreated(dtParser.parse(dateOfPost));

        return rslPost;
    }

    public static void main(String[] args) throws Exception {

        SqlRuParse parser = new SqlRuParse();

        List<Post> allPagesPosts = new ArrayList<>();

        for (int page = 1; page <= PAGES_TO_PARSE; page++) {
            System.out.println("Parsing " + page + " page of " + PAGES_TO_PARSE + " pages");
            List<Post> onePagePosts = parser.list("https://www.sql.ru/forum/job-offers/" + page);
            allPagesPosts.addAll(onePagePosts);
        }

        System.out.println("Posts found:" + allPagesPosts.size());

        for (Post post : allPagesPosts) {
            System.out.println("Id = " + post.getId());
            System.out.println("Title: " + post.getTitle());
            System.out.println("URL: " + post.getLink());
            System.out.println("Description: " + post.getDescription());
            System.out.println("Created: " + post.getCreated());
            System.out.println("---------------------------------------------------------------------");
        }
    }
}