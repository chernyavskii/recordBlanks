package com.service.sharing;

import com.model.Document;
import com.model.Driver;

import java.util.Set;

public interface SharingService {
    Set<Document> getAllSharedDocuments(String username);
    Document getSharedDocumentById(String username, Long id);
    Document shareDocument(String username, Long agent_id, Long document_id);
    Document deleteSharedDocument(String username, Long id);
}

