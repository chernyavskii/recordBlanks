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
import java.text.SimpleDateFormat;
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

    public Object writeToFileTN(String username, Long id, List<Product> products) throws IOException
    {
        User user = userDAO.findByUsername(username);
        Agent agent = new Agent();
        for(Agent agnt : user.getAgents())
        {
            if(id == agnt.getId())
            {
                agent = agnt;
            }
        }
        File file = new File(getClass().getClassLoader().getResource("files/tn.xls").getFile());
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        //RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        //nf.format(456)
        sheet.getRow(4).getCell(25).setCellValue(user.getUnp());
        sheet.getRow(4).getCell(44).setCellValue(agent.getUnp());
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("dd");
        sheet.getRow(15).getCell(27).setCellValue(day.format(date));
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat month = new SimpleDateFormat("MMMM", locale);
        sheet.getRow(15).getCell(34).setCellValue(month.format(date));
        SimpleDateFormat year = new SimpleDateFormat("yy");
        sheet.getRow(15).getCell(60).setCellValue(year.format(date));
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
                sheet.getRow(29 + i).getCell(0).setCellValue(products.get(i).getName());
                sheet.getRow(29 + i).getCell(22).setCellValue(products.get(i).getMeasure());
                sheet.getRow(29 + i).getCell(32).setCellValue(products.get(i).getNumber());
                sheet.getRow(29 + i).getCell(41).setCellValue(products.get(i).getPrice());
                sheet.getRow(29 + i).getCell(51).setCellValue(products.get(i).getNumber() * products.get(i).getPrice());
                sheet.getRow(29 + i).getCell(62).setCellValue(20);
                sheet.getRow(29 + i).getCell(71).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() * 0.2);
                sheet.getRow(29 + i).getCell(80).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() * 0.2 + products.get(i).getNumber() * products.get(i).getPrice());
                sheet.getRow(29 + i).getCell(91).setCellValue(products.get(i).getNote());
            }
            Long sumNumber = 0L;
            for(int i=0; i < products.size(); i++)
            {
                sumNumber += products.get(i).getNumber();
            }
            sheet.getRow(32).getCell(32).setCellValue(sumNumber);
        }
        sheet.getRow(40).getCell(17).setCellValue(user.getPosition() + " " + user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        sheet.getRow(43).getCell(21).setCellValue(user.getPosition() + " " + user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        sheet.getRow(47).getCell(23).setCellValue(agent.getPosition() + " " + agent.getLastName() + " " + agent.getFirstName() + " " + agent.getMiddleName());
        sheet.getRow(53).getCell(23).setCellValue(agent.getPosition() + " " + agent.getLastName() + " " + agent.getFirstName() + " " + agent.getMiddleName());
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        byte[] data = Files.readAllBytes(file.toPath());
        return this.addDocument(username, data, file.getName());
    }
}
