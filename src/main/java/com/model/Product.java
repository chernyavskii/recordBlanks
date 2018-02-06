package com.model;

public class Product
{
    private String name;
    private String measure;
    private Integer number;
    private Double price;
    private String note;

    public Product(String name, String measure, Integer number, Double price, String note)
    {
        this.name = name;
        this.measure = measure;
        this.number = number;
        this.price = price;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
