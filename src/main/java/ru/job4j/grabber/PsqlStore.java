package ru.job4j.grabber;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = cfg.getProperty("url");
        String login = cfg.getProperty("login");
        String password = cfg.getProperty("password");
        try {
            cnn = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        Post postWithId = insertAndReturnID(post);
    }

    @Override
    public List<Post> getAll() {
        String request = "select * from post";
        return findBySQLReq(request);
    }

    @Override
    public Post findById(int id) {
        String request = "select * from post where id = " + id;
        List<Post> result = findBySQLReq(request);
        return result.size() == 1 ? result.get(0) : null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public Post insertAndReturnID(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "insert into post (name, text, link, created) values (?, ?, ?, ?) on conflict do nothing",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public List<Post> findBySQLReq(String sql) {
        List<Post> result = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {

        Properties props = new Properties();
        File propsFile = new File("src/main/resources/grabber.properties");
        try {
            props.load(new FileReader(propsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PsqlStore pStore = new PsqlStore(props);

        Post post1 = new Post(0, "Vacancy 1",
                "http...1", "description 1",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Post post2 = new Post(0, "Vacancy 2",
                "http...2", "description 2",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Post post3 = new Post(0, "Vacancy 3",
                "http...3", "description 3",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        pStore.save(post1);
        pStore.save(post2);
        pStore.save(post3);

        System.out.println(pStore.findById(21));

        List<Post> allPosts = pStore.getAll();
        for (Post post : allPosts) {
            System.out.println(post);
        }
    }
}
