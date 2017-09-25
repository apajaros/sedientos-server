package com.sedientos.data.model;

import java.util.Date;

public class Review {

    private User user;
    private int stars;
    private String body;
    private Date date;

    public Review() {}

    public Review(User user, int stars, String body, Date date) {
        this.user = user;
        this.stars = stars;
        this.body = body;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Review{" +
                "user=" + user +
                ", stars=" + stars +
                ", body='" + body + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (stars != review.stars) return false;
        if (!user.equals(review.user)) return false;
        if (!body.equals(review.body)) return false;
        return date.equals(review.date);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + stars;
        result = 31 * result + body.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
