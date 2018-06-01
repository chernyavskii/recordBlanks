package com.tests.services;

import com.model.Document;
import com.service.sharing.SharingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SharingServiceTest {
    private SharingServiceImpl sharingService;

    private Document document1;

    private Document document2;

    private Set<Document> documents;

    @Before
    public void setUp() {
        document1 = new Document();
        document2 = new Document();
        documents = new HashSet<>();

        document1.setId(1L);
        document2.setId(2L);

        document1.setName("ТН");
        document2.setName("Счет-фактура");

        documents.add(document1);
        documents.add(document2);

        sharingService = mock(SharingServiceImpl.class);
    }

    @Test
    public void testGetAllSharedDocuments() {
        when(sharingService.getAllSharedDocuments(anyString())).thenReturn(documents);

        Set<Document> documentSet = sharingService.getAllSharedDocuments("alexandr");

        assertNotNull(documentSet);
        assertEquals(documentSet, documents);
    }

    @Test
    public void testGetSharedDocumentById() {
        when(sharingService.getSharedDocumentById(anyString(), anyLong())).thenReturn(document1);

        Document document = sharingService.getSharedDocumentById("alexandr", 1L);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }

    @Test
    public void testShareDocument() {
        when(sharingService.shareDocument(anyString(), anyLong(), anyLong())).thenReturn(document1);

        Document document = sharingService.shareDocument("alexandr", 1L, 1L);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }

    @Test
    public void testDeleteSharedDocument() {
        when(sharingService.deleteSharedDocument(anyString(), anyLong())).thenReturn(document1);

        Document document = sharingService.deleteSharedDocument("alexandr", 1L);

        assertNotNull(document);
        assertTrue(document.getId() == 1);
        assertEquals(document.getName(), "ТН");
    }
}
