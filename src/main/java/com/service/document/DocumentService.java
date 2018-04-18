package com.service.document;

import com.model.Document;
import com.model.Product;
import com.model.Work;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DocumentService {
    Set<Document> getAllDocuments(String username);
    Document getDocumentById(String username, Long id) throws IOException;
    Document getDocument(Long id);
    Document addDocumentTN(String username, String documentName, Long id, List<Product> products) throws IOException;
    Document addDocumentTTN(String username, String documentName, Long agent_id, Long driver_id, List<Product> products) throws IOException;
    Document addDocumentASPR(String username, String documentName, Long agent_id, List<Work> works) throws IOException;
    Document addDocumentSF(String username, String documentName, Long agent_id, List<Product> products) throws IOException;
    Document convert(String username, Long document_id, byte[] documentPdf, byte[] documentPng);
    Document deleteDocument(String username, Long id);
    File createFileTN();
    File createFileTTN();
    File createFileASPR();
    File createFileSF();
    HSSFWorkbook preparationFileTN(File file, String username, Long id, List<Product> products) throws IOException;
    HSSFWorkbook preparationFileTTN(File file, String username, Long agent_id, Long driver_id, List<Product> products) throws IOException;
    XSSFWorkbook preparationFileASPR(File file, String username, Long agent_id, List<Work> works) throws IOException;
    HSSFWorkbook preparationFileSF(File file, String username, Long agent_id, List<Product> products) throws IOException;
    File writeToFileTN(File file, HSSFWorkbook workbook) throws IOException;
    File writeToFileTTN(File file, HSSFWorkbook workbook) throws IOException;
    File writeToFileASPR(File file, XSSFWorkbook workbook) throws IOException;
    File writeToFileSF(File file, HSSFWorkbook workbook) throws IOException;
}

