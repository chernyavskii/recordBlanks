package com.tests.services;

import com.model.Document;
import com.model.Product;
import com.model.Work;
import com.service.document.DocumentServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {
    private DocumentServiceImpl documentService;

    private Document document1;

    private Document document2;

    private Document document3;

    private Document document4;

    private Product product1;

    private Product product2;

    private Work work1;

    private Work work2;

    private Set<Document> documents;

    private List<Product> products;

    private List<Work> works;

    @Before
    public void setUp() {
        document1 = new Document();
        document2 = new Document();
        document3 = new Document();
        document4 = new Document();
        product1 = new Product();
        product2 = new Product();
        work1 = new Work();
        work2 = new Work();
        documents = new HashSet<>();
        products = new ArrayList<>();
        works = new ArrayList<>();

        document1.setId(1L);
        document2.setId(2L);
        document3.setId(3L);
        document4.setId(4L);

        document1.setName("ТН");
        document2.setName("Счет-фактура");
        document3.setName("ТТН");
        document4.setName("АСПР");

        documents.add(document1);
        documents.add(document2);

        product1.setName("Фото");
        product2.setName("Листовки");

        products.add(product1);
        products.add(product2);

        work1.setName("Печать фото");
        work2.setName("Печать листовок");

        works.add(work1);
        works.add(work2);

        documentService = mock(DocumentServiceImpl.class);
    }

    @Test
    public void testGetAllDocuments() {
        when(documentService.getAllDocuments(anyString())).thenReturn(documents);

        Set<Document> documentSet = documentService.getAllDocuments("alexandr");

        assertNotNull(documentSet);
        assertEquals(documentSet, documents);
    }

    @Test
    public void testGetDocumentById() throws IOException {
        when(documentService.getDocumentById(anyString(), anyLong())).thenReturn(document1);

        Document document = documentService.getDocumentById("alexandr", 1L);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }

    @Test
    public void testGetDocument() {
        when(documentService.getDocument(anyLong())).thenReturn(document1);

        Document document = documentService.getDocument(1L);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }

    @Test
    public void testDeleteDocument() {
        when(documentService.getDocument(anyLong())).thenReturn(document1);

        Document document = documentService.getDocument(1L);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }

    @Test
    public void testAddDocumentTN() throws IOException {
        when(documentService.addDocumentTN(anyString(), anyString(), anyLong(), (List<Product>) any())).thenReturn(document1);

        Document document = documentService.addDocumentTN("alexandr", "TN", 1L, products);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }

    @Test
    public void testAddDocumentTTN() throws IOException {
        when(documentService.addDocumentTTN(anyString(), anyString(), anyLong(), anyLong(), (List<Product>) any())).thenReturn(document3);

        Document document = documentService.addDocumentTTN("alexandr", "ТТН", 1L, 1L, products);

        assertNotNull(document);
        assertTrue(document.getId() == 3);
        assertEquals(document.getName(), "ТТН");
    }

    @Test
    public void testAddDocumentSF() throws IOException {
        when(documentService.addDocumentSF(anyString(), anyString(), anyLong(), (List<Product>) any())).thenReturn(document2);

        Document document = documentService.addDocumentSF("alexandr", "Счет-фактура", 1L, products);

        assertNotNull(document);
        assertTrue(document.getId() == 2);
        assertEquals(document.getName(), "Счет-фактура");
    }

    @Test
    public void testAddDocumentASPR() throws IOException {
        when(documentService.addDocumentASPR(anyString(), anyString(), anyLong(), (List<Work>) any())).thenReturn(document4);

        Document document = documentService.addDocumentASPR("alexandr", "АСПР", 1L, works);

        assertNotNull(document);
        assertTrue(document.getId() == 4);
        assertEquals(document.getName(), "АСПР");
    }

    @Test
    public void testCreateFileTN()  {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\tn.xls");

        when(documentService.createFileTN()).thenReturn(file1);

        File file = documentService.createFileTN();

        assertNotNull(file);
    }

    @Test
    public void testCreateFileTTN() {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\ttn.xls");

        when(documentService.createFileTTN()).thenReturn(file1);

        File file = documentService.createFileTTN();

        assertNotNull(file);
    }

    @Test
    public void testCreateFileASPR() {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\aspr.xlsx");

        when(documentService.createFileASPR()).thenReturn(file1);

        File file = documentService.createFileASPR();

        assertNotNull(file);
    }

    @Test
    public void testCreateFileSF() {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\sf.xls");

        when(documentService.createFileASPR()).thenReturn(file1);

        File file = documentService.createFileASPR();

        assertNotNull(file);
    }

    @Test
    public void testPreparationFileTN() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\tn.xls");
        FileInputStream inputStream1 = new FileInputStream(file1);
        HSSFWorkbook workbook1 = new HSSFWorkbook(inputStream1);

        when(documentService.preparationFileTN((File) any(), anyString(), anyLong(), (List<Product>) any())).thenReturn(workbook1);

        HSSFWorkbook workbook = documentService.preparationFileTN(file1, "ТН", 1L, products);

        assertNotNull(workbook);
    }

    @Test
    public void testPreparationFileTTN() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\ttn.xls");
        FileInputStream inputStream1 = new FileInputStream(file1);
        HSSFWorkbook workbook1 = new HSSFWorkbook(inputStream1);

        when(documentService.preparationFileTTN((File) any(), anyString(), anyLong(), anyLong(), (List<Product>) any())).thenReturn(workbook1);

        HSSFWorkbook workbook = documentService.preparationFileTTN(file1, "TТН", 1L, 1L, products);

        assertNotNull(workbook);
    }

    @Test
    public void testPreparationFileASPR() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\aspr.xlsx");
        FileInputStream inputStream1 = new FileInputStream(file1);
        XSSFWorkbook workbook1 = new XSSFWorkbook(inputStream1);

        when(documentService.preparationFileASPR((File) any(), anyString(), anyLong(), (List<Work>) any())).thenReturn(workbook1);

        XSSFWorkbook workbook = documentService.preparationFileASPR(file1, "АСПР", 1L, works);

        assertNotNull(workbook);
    }

    @Test
    public void testPreparationFileSF() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\sf.xls");
        FileInputStream inputStream1 = new FileInputStream(file1);
        HSSFWorkbook workbook1 = new HSSFWorkbook(inputStream1);

        when(documentService.preparationFileTN((File) any(), anyString(), anyLong(), (List<Product>) any())).thenReturn(workbook1);

        HSSFWorkbook workbook = documentService.preparationFileTN(file1, "Счет-фактура", 1L, products);

        assertNotNull(workbook);
    }

    @Test
    public void testWriteToFileTN() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\tn.xls");
        FileInputStream inputStream1 = new FileInputStream(file1);
        HSSFWorkbook workbook1 = new HSSFWorkbook(inputStream1);

        when(documentService.writeToFileTN((File) any(), (HSSFWorkbook) any())).thenReturn(file1);

        File file = documentService.writeToFileTN(file1, workbook1);

        assertNotNull(file);
    }

    @Test
    public void testWriteToFileTTN() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\ttn.xls");
        FileInputStream inputStream1 = new FileInputStream(file1);
        HSSFWorkbook workbook1 = new HSSFWorkbook(inputStream1);

        when(documentService.writeToFileTTN((File) any(), (HSSFWorkbook) any())).thenReturn(file1);

        File file = documentService.writeToFileTTN(file1, workbook1);

        assertNotNull(file);
    }

    @Test
    public void testWriteToFileASPR() throws FileNotFoundException, IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\aspr.xlsx");
        FileInputStream inputStream1 = new FileInputStream(file1);
        XSSFWorkbook workbook1 = new XSSFWorkbook(inputStream1);

        when(documentService.writeToFileASPR((File) any(), (XSSFWorkbook) any())).thenReturn(file1);

        File file = documentService.writeToFileASPR(file1, workbook1);

        assertNotNull(file);
    }

    @Test
    public void testWriteToFileSF() throws IOException {
        File file1 = new File("D:\\ПОИТ\\Диплом\\recordBlanks\\src\\main\\resources\\files\\sf.xls");
        FileInputStream inputStream1 = new FileInputStream(file1);
        HSSFWorkbook workbook1 = new HSSFWorkbook(inputStream1);

        when(documentService.writeToFileSF((File) any(), (HSSFWorkbook) any())).thenReturn(file1);

        File file = documentService.writeToFileSF(file1, workbook1);

        assertNotNull(file);
    }
}
