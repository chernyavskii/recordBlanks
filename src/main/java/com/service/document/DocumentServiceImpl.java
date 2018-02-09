package com.service.document;

import com.dao.DocumentDAO;
import com.dao.UserDAO;
import com.ibm.icu.text.RuleBasedNumberFormat;
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

    public Set<Document> getAllDocuments(String username)
    {
        return userDAO.findByUsername(username).getDocuments();
    }

    public Document getDocumentById(String username, Long id) throws IOException
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

    public File createFileTN()
    {
        File file = new File(getClass().getClassLoader().getResource("files/tn.xls").getFile());
        return file;
    }

    public HSSFWorkbook preparationFileTN(File file, String username, Long id, List<Product> products) throws IOException
    {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat month = new SimpleDateFormat("MMMM", locale);
        SimpleDateFormat year = new SimpleDateFormat("yy");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        User user = userDAO.findByUsername(username);
        Agent agent = new Agent();
        for(Agent agnt : user.getAgents())
        {
            if(id == agnt.getId())
            {
                agent = agnt;
            }
        }
        Long sumNumber = 0L;
        Double sumCost = 0D;
        Double sumNDS = 0D;
        Double sumCostNDS = 0D;
        sheet.getRow(4).getCell(25).setCellValue(user.getUnp());
        sheet.getRow(4).getCell(44).setCellValue(agent.getUnp());
        sheet.getRow(15).getCell(27).setCellValue(day.format(date));
        sheet.getRow(15).getCell(34).setCellValue(month.format(date));
        sheet.getRow(15).getCell(60).setCellValue(year.format(date));
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
                sumNumber += products.get(i).getNumber();
                sheet.getRow(29 + i).getCell(41).setCellValue(products.get(i).getPrice());
                sheet.getRow(29 + i).getCell(51).setCellValue(products.get(i).getNumber() * products.get(i).getPrice());
                sumCost += products.get(i).getNumber() * products.get(i).getPrice();
                sheet.getRow(29 + i).getCell(62).setCellValue(20);
                sheet.getRow(29 + i).getCell(71).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() * 0.2);
                sumNDS += products.get(i).getNumber() * products.get(i).getPrice() * 0.2;
                sheet.getRow(29 + i).getCell(80).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() * 0.2 + products.get(i).getNumber() * products.get(i).getPrice());
                sumCostNDS += products.get(i).getNumber() * products.get(i).getPrice() * 0.2 + products.get(i).getNumber() * products.get(i).getPrice();
                sheet.getRow(29 + i).getCell(91).setCellValue(products.get(i).getNote());
            }
            Long rubNDS = sumNDS.longValue();
            Double copNDS = (sumNDS - rubNDS) * 100;
            Long rubCostNDS = sumCostNDS.longValue();
            Double copCostNDS = (sumCostNDS - rubCostNDS) * 100;
            sheet.getRow(32).getCell(32).setCellValue(sumNumber);
            sheet.getRow(32).getCell(51).setCellValue(sumCost);
            sheet.getRow(32).getCell(71).setCellValue(sumNDS);
            sheet.getRow(32).getCell(80).setCellValue(sumCostNDS);
            sheet.getRow(34).getCell(18).setCellValue(nf.format(rubNDS));
            sheet.getRow(34).getCell(75).setCellValue(copNDS);
            sheet.getRow(37).getCell(22).setCellValue(nf.format(rubCostNDS));
            sheet.getRow(37).getCell(75).setCellValue(copCostNDS);
        }
        sheet.getRow(40).getCell(17).setCellValue(user.getPosition() + " " + user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        sheet.getRow(43).getCell(21).setCellValue(user.getPosition() + " " + user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        sheet.getRow(47).getCell(23).setCellValue(agent.getPosition() + " " + agent.getLastName() + " " + agent.getFirstName() + " " + agent.getMiddleName());
        sheet.getRow(53).getCell(23).setCellValue(agent.getPosition() + " " + agent.getLastName() + " " + agent.getFirstName() + " " + agent.getMiddleName());
        return workbook;
    }

    public File writeToFileTN(File file, HSSFWorkbook workbook) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        return file;
    }

    public Object addDocumentTN(String username, Long id, List<Product> products) throws IOException
    {
        File file = createFileTN();
        HSSFWorkbook workbook = preparationFileTN(file, username, id, products);
        File newFile = writeToFileTN(file, workbook);
        byte[] document = Files.readAllBytes(newFile.toPath());
        Document doc = new Document();
        doc.setDocument(document);
        doc.setName(newFile.getName());
        doc.setUser(userDAO.findByUsername(username));
        return documentDAO.save(doc);
    }
}
