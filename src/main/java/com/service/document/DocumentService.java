package com.service.document;

import com.model.Agent;
import com.model.Document;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    List<Document> findAll();
    Object findById(Long id) throws IOException;
    Object addDocument(byte[] document, String name);
    Object deleteDocument(Long id);
}

