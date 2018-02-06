package com.service.document;

import com.dao.DocumentDAO;
import com.dao.UserDAO;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.model.Document;
import com.model.User;
import com.utils.Error;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDAO documentDAO;

    @Autowired
    private UserDAO userDAO;

    public Set<Document> findAll(String username)
    {
        return userDAO.findByUsername(username).getDocuments();
    }

    public Object findById(String username, Long id) throws IOException
    {
        Document document = new Document();
        for(Document doc : userDAO.findByUsername(username).getDocuments())
        {
            if(id == doc.getId())
            {
                document = doc;
                Files.write(Paths.get("d:/files/" + doc.getName()), doc.getDocument());
            }
        }
        return  document;
    }

    public Object addDocument(String username, byte[] document, String name)
    {
        Document doc = new Document();
        doc.setDocument(document);
        doc.setName(name);
        doc.setUser(userDAO.findByUsername(username));
        return documentDAO.save(doc);
    }

    public Object deleteDocument(String username, Long id)
    {
        Document document = new Document();
        for(Document doc : userDAO.findByUsername(username).getDocuments())
        {
            if(id == doc.getId())
            {
                document = doc;
                documentDAO.delete(id);
            }
        }
        return document;
    }

    public Object writeToFileTN(String username, String name) throws IOException
    {
        File file = new File(getClass().getClassLoader().getResource("files/tn.xls").getFile());
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        sheet.getRow(29).getCell(0).setCellValue(nf.format(456));
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        byte[] data = Files.readAllBytes(file.toPath());
        return this.addDocument(username, data, file.getName());
    }
}
