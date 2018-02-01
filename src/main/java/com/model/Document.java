package com.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;

@Entity
public class Document {
    private Long id;
    private String name;
    private byte[] document;
    private Set<User> users;

    @Id
    @Column(name = "id")
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
    @Column(name = "document")
    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserDocument", schema = "testDB", joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false))
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
