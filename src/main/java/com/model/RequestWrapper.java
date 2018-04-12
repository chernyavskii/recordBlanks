package com.model;

import java.util.List;

public class RequestWrapper
{
    private String documentName;
    private Long agent_id;
    private Long driver_id;
    private Long user_id;
    private Long document_id;
    private User user;
    private Agent agent;
    private List<Product> products;
    private List<Work> works;
    private String documentPdf;
    private String documentPng;
    private String role;

    public String getDocumentName() { return documentName; }

    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public Long getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Long agent_id) {
        this.agent_id = agent_id;
    }

    public Long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Long driver_id) {
        this.driver_id = driver_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getDocument_id() { return document_id; }

    public void setDocument_id(Long document_id) { this.document_id = document_id; }

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public String getDocumentPdf() { return documentPdf; }

    public void setDocumentPdf(String documentPdf) { this.documentPdf = documentPdf; }

    public String getDocumentPng() { return documentPng; }

    public void setDocumentPng(String documentPng) { this.documentPng = documentPng; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }
}
