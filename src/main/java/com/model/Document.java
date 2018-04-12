package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Document {
    private Long id;
    private String name;
    private String type;
    private byte[] documentExcel;
    private byte[] documentPdf;
    private byte[] documentPng;
    private String date;
    private User user;
    private Agent agent;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "documentExcel")
    @JsonIgnore
    public byte[] getDocumentExcel() {
        return documentExcel;
    }

    public void setDocumentExcel(byte[] documentExcel) {
        this.documentExcel = documentExcel;
    }

    @Basic
    @Column(name = "documentPdf")
    @JsonIgnore
    public byte[] getDocumentPdf() {
        return documentPdf;
    }

    public void setDocumentPdf(byte[] documentPdf) {
        this.documentPdf = documentPdf;
    }

    @Basic
    @Column(name = "documentPng")
    @JsonIgnore
    public byte[] getDocumentPng() {
        return documentPng;
    }

    public void setDocumentPng(byte[] documentPng) {
        this.documentPng = documentPng;
    }

    @Basic
    @Column(name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "Sharing", joinColumns = { @JoinColumn(name = "document_id") }, inverseJoinColumns = { @JoinColumn(name = "agent_id") })
    @JsonIgnore
    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) { this.agent = agent; }
}
