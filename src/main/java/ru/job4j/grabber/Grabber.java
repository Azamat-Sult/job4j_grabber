package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.html.SqlRuParse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {

    private final Properties cfg = new Properties();

    private static int pagesToParse;

    public Store store() {
        return new PsqlStore(cfg);
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() {
        File propsFile = new File("src/main/resources/grabber.properties");
        try {
            cfg.load(new FileReader(propsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pagesToParse = Integer.parseInt(cfg.getProperty("pagesToParse"));
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");

            List<Post> allPagesPosts = new ArrayList<>();

            for (int page = 1; page <= pagesToParse; page++) {
                System.out.println("Parsing " + page + " page of " + pagesToParse + " pages");
                List<Post> onePagePosts = null;
                try {
                    onePagePosts = parse.list("https://www.sql.ru/forum/job-offers/" + page);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (onePagePosts != null) {
                    allPagesPosts.addAll(onePagePosts);
                }
            }

            System.out.println("Parsing done. Found " + allPagesPosts.size() + " posts");

            for (Post post : allPagesPosts) {
                store.save(post);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(new SqlRuDateTimeParser()), store, scheduler);
    }
}
