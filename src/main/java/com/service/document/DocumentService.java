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
    Object getDocumentById(String username, Long id) throws IOException;
    Object addDocumentTN(String username, Long id, List<Product> products) throws IOException;
    Object deleteDocument(String username, Long id);
    File createFileTN();
    HSSFWorkbook preparationFileTN(File file, String username, Long id, List<Product> products) throws IOException;
    File writeToFileTN(File file, HSSFWorkbook workbook) throws IOException;
    //Object writeToFileTN(String username, Long id, List<Product> products) throws IOException;
}

