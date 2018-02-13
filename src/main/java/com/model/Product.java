package com.model;

public class Product
{
    private String name;
    private String measure;
    private Long number;
    private Double price;
    private Long packageNumber;
    private Long weight;
    private String note = "-";

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

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(Long packageNumber) {
        this.packageNumber = packageNumber;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
