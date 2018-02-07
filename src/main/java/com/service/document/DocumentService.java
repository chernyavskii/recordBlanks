package com.service.document;

import com.model.Agent;
import com.model.Document;
import com.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DocumentService {
    Set<Document> findAll(String username);
    Object findById(String username, Long id) throws IOException;
    Object addDocument(String username, byte[] document, String name);
    Object deleteDocument(String username, Long id);
    Object writeToFileTN(String username, Long id, List<Product> products) throws IOException;
}

