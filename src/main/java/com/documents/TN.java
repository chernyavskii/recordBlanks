package com.documents;

import com.model.Agent;
import com.model.User;

import java.util.Date;

public class TN
{
    private User user;
    private Agent agent;
    private Date date;
    private String product;
    private String measure;
    private Integer number;
    private Double price;
    private Integer rateNDS;
    private Double sumNDS;
    private String note;

    public TN(User user, Agent agent, Date date, String product, String measure, Integer number, Double price, Integer rateNDS, Double sumNDS, String note)
    {
        this.user = user;
        this.agent = agent;
        this.date = date;
        this.product = product;
        this.measure = measure;
        this.number = number;
        this.price = price;
        this.rateNDS = rateNDS;
        this.sumNDS = sumNDS;
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProduct() { return product; }

    public void setDate(String product) {
        this.product = product;
    }

    public String getMeasure() { return measure; }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Integer getNumber() { return number; }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getPrice() { return price; }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getRateNDS() { return rateNDS; }

    public void setRateNDS(Integer rateNDS) {
        this.rateNDS = rateNDS;
    }

    public Double getSumNDS() { return sumNDS; }

    public void setSumNDS(Double sumNDS) {
        this.sumNDS = sumNDS;
    }

    public String getNote() { return note; }

    public void setNote(String note) {
        this.note = note;
    }
}
