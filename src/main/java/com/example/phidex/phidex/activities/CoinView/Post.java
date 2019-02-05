package com.example.phidex.phidex.activities.CoinView;

public class Post {
    private String score;
    private String title;
    private long time_created;
    private String author;
    private String subreddit;
    private String num_comments;
    private String url;

    static final String BASE_URL = "https://reddit.com/";

    public Post(String score, String title, long time_created, String author, String subreddit, String num_comments, String url) {
        this.score = trimScore(score);
        this.title = title;
        this.time_created = time_created;
        this.author = author;
        this.subreddit = subreddit;
        this.num_comments = num_comments;

        if (url.startsWith("/")) {
            this.url = BASE_URL + url.substring(1);
        } else {
            this.url = BASE_URL + url;
        }
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeCreated() {
        return time_created;
    }

    public void setTimeCreated(long time_created) {
        this.time_created = time_created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getNumComments() {
        return num_comments;
    }

    public void setNumComments(String num_comments) {
        this.num_comments = num_comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String trimScore (String score) {
        double s = Double.parseDouble(score);
        if (s > 9999) {
            s = round(s/1000, 1);
            return Double.toString(s) + "k";
        }
        return score;
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}