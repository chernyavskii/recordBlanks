package com.service.document;

import com.model.Agent;
import com.model.Document;
import com.model.Product;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DocumentService {
    Set<Document> getAllDocuments(String username);
    Document getDocumentById(String username, Long id) throws IOException;
    Document addDocumentTN(String username, Long id, List<Product> products) throws IOException;
    Document addDocumentTTN(String username, Long agent_id, Long driver_id, List<Product> products) throws IOException;
    Document deleteDocument(String username, Long id);
    File createFileTN();
    File createFileTTN();
    HSSFWorkbook preparationFileTN(File file, String username, Long id, List<Product> products) throws IOException;
    HSSFWorkbook preparationFileTTN(File file, String username, Long agent_id, Long driver_id, List<Product> products) throws IOException;
    File writeToFileTN(File file, HSSFWorkbook workbook) throws IOException;
    File writeToFileTTN(File file, HSSFWorkbook workbook) throws IOException;
}

