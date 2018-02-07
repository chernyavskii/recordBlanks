package com.service.document;

import com.dao.DocumentDAO;
import com.dao.UserDAO;
import com.model.Agent;
import com.model.Document;
import com.model.Product;
import com.model.User;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    public Object writeToFileTN(String username, Agent agent, List<Product> products) throws IOException
    {
        User user = userDAO.findByUsername(username);
        File file = new File(getClass().getClassLoader().getResource("files/tn.xls").getFile());
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        //RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        //nf.format(456)
        sheet.getRow(4).getCell(25).setCellValue(user.getUnp());
        sheet.getRow(4).getCell(44).setCellValue(agent.getUnp());
        Date date = new Date();
        sheet.getRow(15).getCell(27).setCellValue(date.getDay());
        sheet.getRow(15).getCell(34).setCellValue(date.getMonth());
        sheet.getRow(15).getCell(60).setCellValue(date.getYear());
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        sheet.getRow(17).getCell(17).setCellStyle(cs);
        sheet.getRow(17).setHeightInPoints ((2 * sheet.getDefaultRowHeightInPoints ()));
        sheet.getRow(17).getCell(17).setCellValue(user.getOrganization() + "\n" + user.getAddress());
        sheet.getRow(20).getCell(17).setCellStyle(cs);
        sheet.getRow(20).setHeightInPoints ((2 * sheet.getDefaultRowHeightInPoints ()));
        sheet.getRow(20).getCell(17).setCellValue(agent.getOrganization() + "\n" + agent.getAddress());
        if(products.size() <= 3)
        {
            for(int i=0; i < products.size(); i++)
            {
                sheet.getRow(29 + i).getCell(0).setCellValue(products.get(0).getName());
            }
        }
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        byte[] data = Files.readAllBytes(file.toPath());
        return this.addDocument(username, data, file.getName());
    }
}
