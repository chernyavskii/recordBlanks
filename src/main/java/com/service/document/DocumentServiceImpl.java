package com.service.document;

import com.dao.DocumentDAO;
import com.model.Document;
import com.utils.Error;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDAO documentDAO;

    public List<Document> findAll()
    {
        return documentDAO.findAll();
    }

    public Object findById(Long id) throws IOException
    {
        Document doc = new Document();
        doc = documentDAO.findOne(id);
        if(doc == null)
        {
            return (new Error("Entity not found", "entity_not_found", 404));
        }
        Files.write(Paths.get("d:/files/" + doc.getName()), doc.getDocument());
        return doc;
    }

    public Object addDocument(byte[] document, String name)
    {
        Document doc = new Document();
        if(name == "")
        {
            return (new Error("Null value", "null_value", 400));
        }
        doc.setDocument(document);
        doc.setName(name);
        return documentDAO.save(doc);
    }

    public Object deleteDocument(Long id)
    {
        Document doc = new Document();
        doc = documentDAO.findOne(id);
        if(doc == null)
        {
            return (new Error("Entity not found", "entity_not_found", 404));
        }
        documentDAO.delete(id);
        return doc;
    }

    public Object writeToFile(String name) throws IOException
    {
        File file = new File(getClass().getClassLoader().getResource("files/tn.xls").getFile());
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        sheet.getRow(29).getCell(0).setCellValue(name);
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        byte[] data = Files.readAllBytes(file.toPath());
        return this.addDocument(data, file.getName());
    }
}
