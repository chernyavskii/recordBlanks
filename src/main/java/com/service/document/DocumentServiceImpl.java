package com.service.document;

import com.dao.DocumentDAO;
import com.dao.UserDAO;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
                //Files.write(Paths.get("d:/files/" + doc.getName()), doc.getDocument());
                return document;
            }
        }
        return  null;
    }

    public Document deleteDocument(String username, Long id)
    {
        Document document = new Document();
        for(Document doc : userDAO.findByUsername(username).getDocuments())
        {
            if(id == doc.getId())
            {
                document = doc;
                documentDAO.delete(id);
                return document;
            }
        }
        return null;
    }

    public File createFileTN()
    {
        File file = new File(getClass().getClassLoader().getResource("files/tn.xls").getFile());
        return file;
    }

    public File createFileTTN()
    {
        File file = new File(getClass().getClassLoader().getResource("files/ttn.xls").getFile());
        return file;
    }

    public File createFileASPR()
    {
        File file = new File(getClass().getClassLoader().getResource("files/aspr.xlsx").getFile());
        return file;
    }

    public HSSFWorkbook preparationFileTN(File file, String username, Long id, List<Product> products) throws IOException
    {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFSheet sheet1 = workbook.getSheetAt(1);
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
        if(products.size() > 3)
        {
            sheet.getRow(29).getCell(0).setCellValue("Товар в приложении №1");
            for(int i = 0; i < 3; i++)
            {
                sheet.getRow(29 + i).getCell(22).setCellValue("x");
                sheet.getRow(29 + i).getCell(41).setCellValue("x");
                sheet.getRow(29 + i).getCell(62).setCellValue("x");
            }
            for(int i = 0; i < products.size(); i++)
            {
                sheet1.getRow(2).getCell(6).setCellValue(new SimpleDateFormat("dd.MM.yyyy").format(date));
                sheet1.getRow(6 + i).getCell(0).setCellValue(products.get(i).getName());
                sheet1.getRow(6 + i).getCell(1).setCellValue(products.get(i).getMeasure());
                sheet1.getRow(6 + i).getCell(2).setCellValue(products.get(i).getNumber());
                sumNumber += products.get(i).getNumber();
                sheet1.getRow(6 + i).getCell(3).setCellValue(products.get(i).getPrice());
                sheet1.getRow(6 + i).getCell(4).setCellValue(products.get(i).getNumber() * products.get(i).getPrice());
                sumCost += products.get(i).getNumber() * products.get(i).getPrice();
                sheet1.getRow(6 + i).getCell(5).setCellValue(20);
                sheet1.getRow(6 + i).getCell(6).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() * 0.2);
                sumNDS += products.get(i).getNumber() * products.get(i).getPrice() * 0.2;
                sheet1.getRow(6 + i).getCell(7).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() + products.get(i).getNumber() * products.get(i).getPrice() * 0.2);
                sumCostNDS += products.get(i).getNumber() * products.get(i).getPrice() + products.get(i).getNumber() * products.get(i).getPrice() * 0.2;
                sheet1.getRow(6 + i).getCell(8).setCellValue(products.get(i).getNote());
            }
            Long rubNDS = sumNDS.longValue();
            Double copNDS = (sumNDS - rubNDS) * 100;
            Long rubCostNDS = sumCostNDS.longValue();
            Double copCostNDS = (sumCostNDS - rubCostNDS) * 100;
            sheet1.getRow(47).getCell(2).setCellValue(sumNumber);
            sheet1.getRow(47).getCell(4).setCellValue(sumCost);
            sheet1.getRow(47).getCell(6).setCellValue(sumNDS);
            sheet1.getRow(47).getCell(7).setCellValue(sumCostNDS);
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

    public HSSFWorkbook preparationFileTTN(File file, String username, Long agent_id, Long driver_id, List<Product> products) throws IOException
    {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFSheet sheet1 = workbook.getSheetAt(1);
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat month = new SimpleDateFormat("MMMM", locale);
        SimpleDateFormat year = new SimpleDateFormat("yy");
        User user = userDAO.findByUsername(username);
        Agent agent = new Agent();
        for(Agent agnt : user.getAgents())
        {
            if(agent_id == agnt.getId())
            {
                agent = agnt;
            }
        }
        Driver driver = new Driver();
        for(Driver drv : user.getDrivers())
        {
            if(driver_id == drv.getId())
            {
                driver = drv;
            }
        }
        Long sumNumber = 0L;
        Double sumCost = 0D;
        Double sumNDS = 0D;
        Double sumCostNDS = 0D;
        Long sumWeight = 0L;
        Long sumPackageNumber = 0L;
        sheet.getRow(3).getCell(37).setCellValue(user.getUnp());
        sheet.getRow(3).getCell(50).setCellValue(agent.getUnp());
        sheet.getRow(15).getCell(2).setCellValue(day.format(date));
        sheet.getRow(15).getCell(9).setCellValue(month.format(date));
        sheet.getRow(15).getCell(35).setCellValue(year.format(date));
        sheet.getRow(16).getCell(13).setCellValue(driver.getCarModel() + " " + driver.getCarNumber());
        if(driver.getTrailerModel() == null || driver.getTrailerNumber() == null)
        {
            sheet.getRow(16).getCell(87).setCellValue("-");
        }
        else
        {
            sheet.getRow(16).getCell(87).setCellValue(driver.getTrailerModel() + " " + driver.getTrailerNumber());
        }
        sheet.getRow(20).getCell(10).setCellValue(driver.getLastName() + " " + driver.getFirstName() + " " + driver.getMiddleName());
        sheet.getRow(26).getCell(17).setCellValue(user.getOrganization() + " " + user.getAddress());
        sheet.getRow(28).getCell(17).setCellValue(agent.getOrganization() + " " + agent.getAddress());
        sheet.getRow(32).getCell(16).setCellValue(user.getAddress());
        sheet.getRow(32).getCell(86).setCellValue(agent.getAddress());
        if(products.size() == 1)
        {
            sumNDS = products.get(0).getNumber() * products.get(0).getPrice() * 0.2;
            sumCostNDS = sumNDS + products.get(0).getNumber() * products.get(0).getPrice();
            sumWeight = products.get(0).getWeight();
            sumPackageNumber = products.get(0).getPackageNumber();
            Long rubNDS = sumNDS.longValue();
            Double copNDS = (sumNDS - rubNDS) * 100;
            Long rubCostNDS = sumCostNDS.longValue();
            Double copCostNDS = (sumCostNDS - rubCostNDS) * 100;
            sheet.getRow(42).getCell(0).setCellValue(products.get(0).getName());
            sheet.getRow(42).getCell(31).setCellValue(products.get(0).getMeasure());
            sheet.getRow(42).getCell(49).setCellValue(products.get(0).getPrice());
            sheet.getRow(42).getCell(74).setCellValue(20);
            for(int i = 0; i < 2; i++)
            {
                sheet.getRow(42 + i).getCell(40).setCellValue(products.get(0).getNumber());
                sheet.getRow(42 + i).getCell(62).setCellValue(products.get(0).getNumber() * products.get(0).getPrice());
                sheet.getRow(42 + i).getCell(81).setCellValue(sumNDS);
                sheet.getRow(42 + i).getCell(92).setCellValue(sumCostNDS);
                sheet.getRow(42 + i).getCell(104).setCellValue(sumPackageNumber);
                sheet.getRow(42 + i).getCell(113).setCellValue(sumWeight);
            }
            sheet.getRow(42).getCell(123).setCellValue(products.get(0).getNote());
            sheet.getRow(45).getCell(18).setCellValue(nf.format(rubNDS));
            sheet.getRow(45).getCell(107).setCellValue(copNDS);
            sheet.getRow(48).getCell(22).setCellValue(nf.format(rubCostNDS));
            sheet.getRow(48).getCell(107).setCellValue(copCostNDS);
            sheet.getRow(50).getCell(14).setCellValue(nf.format(sumWeight) + " кг.");
            sheet.getRow(50).getCell(86).setCellValue(nf.format(sumPackageNumber));
        }
        if(products.size() > 1)
        {
            sheet.getRow(42).getCell(0).setCellValue("Товар в приложении №1");
            sheet.getRow(42).getCell(31).setCellValue("x");
            sheet.getRow(42).getCell(49).setCellValue("x");
            sheet.getRow(42).getCell(74).setCellValue("x");
            sheet.getRow(42).getCell(123).setCellValue("x");
            for(int i = 0; i < products.size(); i++)
            {
                sheet1.getRow(2).getCell(8).setCellValue(new SimpleDateFormat("dd.MM.yyyy").format(date));
                sheet1.getRow(6 + i).getCell(0).setCellValue(products.get(i).getName());
                sheet1.getRow(6 + i).getCell(1).setCellValue(products.get(i).getMeasure());
                sheet1.getRow(6 + i).getCell(2).setCellValue(products.get(i).getNumber());
                sumNumber += products.get(i).getNumber();
                sheet1.getRow(6 + i).getCell(3).setCellValue(products.get(i).getPrice());
                sheet1.getRow(6 + i).getCell(4).setCellValue(products.get(i).getNumber() * products.get(i).getPrice());
                sumCost += products.get(i).getNumber() * products.get(i).getPrice();
                sheet1.getRow(6 + i).getCell(5).setCellValue(20);
                sheet1.getRow(6 + i).getCell(6).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() * 0.2);
                sumNDS += products.get(i).getNumber() * products.get(i).getPrice() * 0.2;
                sheet1.getRow(6 + i).getCell(7).setCellValue(products.get(i).getNumber() * products.get(i).getPrice() + products.get(i).getNumber() * products.get(i).getPrice() * 0.2);
                sumCostNDS += products.get(i).getNumber() * products.get(i).getPrice() + products.get(i).getNumber() * products.get(i).getPrice() * 0.2;
                sheet1.getRow(6 + i).getCell(8).setCellValue(products.get(i).getPackageNumber());
                sumPackageNumber += products.get(i).getPackageNumber();
                sheet1.getRow(6 + i).getCell(9).setCellValue(products.get(i).getWeight());
                sumWeight += products.get(i).getWeight();
                sheet1.getRow(6 + i).getCell(10).setCellValue(products.get(i).getNote());
            }
            Long rubNDS = sumNDS.longValue();
            Double copNDS = (sumNDS - rubNDS) * 100;
            Long rubCostNDS = sumCostNDS.longValue();
            Double copCostNDS = (sumCostNDS - rubCostNDS) * 100;
            sheet1.getRow(46).getCell(2).setCellValue(sumNumber);
            sheet1.getRow(46).getCell(4).setCellValue(sumCost);
            sheet1.getRow(46).getCell(6).setCellValue(sumNDS);
            sheet1.getRow(46).getCell(7).setCellValue(sumCostNDS);
            sheet1.getRow(46).getCell(8).setCellValue(sumPackageNumber);
            sheet1.getRow(46).getCell(9).setCellValue(sumWeight);
            sheet.getRow(45).getCell(18).setCellValue(nf.format(rubNDS));
            sheet.getRow(45).getCell(107).setCellValue(copNDS);
            sheet.getRow(48).getCell(22).setCellValue(nf.format(rubCostNDS));
            sheet.getRow(48).getCell(107).setCellValue(copCostNDS);
            sheet.getRow(50).getCell(14).setCellValue(nf.format(sumWeight) + "кг.");
            sheet.getRow(50).getCell(86).setCellValue(nf.format(sumPackageNumber));
        }
        sheet.getRow(52).getCell(14).setCellValue(user.getPosition());
        sheet.getRow(52).getCell(82).setCellValue(agent.getPosition());
        sheet.getRow(54).getCell(0).setCellValue(user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        sheet.getRow(54).getCell(62).setCellValue(agent.getLastName() + " " + agent.getFirstName() + " " + agent.getMiddleName());
        sheet.getRow(56).getCell(18).setCellValue(user.getPosition());
        sheet.getRow(58).getCell(0).setCellValue(user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        sheet.getRow(60).getCell(81).setCellValue(agent.getPosition());
        sheet.getRow(62).getCell(62).setCellValue(agent.getLastName() + " " + agent.getFirstName() + " " + agent.getMiddleName());
        return workbook;
    }

    public XSSFWorkbook preparationFileASPR(File file, String username, Long agent_id, List<Work> works) throws IOException
    {
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
        User user = userDAO.findByUsername(username);
        Agent agent = new Agent();
        for(Agent agnt : user.getAgents())
        {
            if(agent_id == agnt.getId())
            {
                agent = agnt;
            }
        }
        Double sumNDS = 0D;
        Double sumCostNDS = 0D;
        sheet.getRow(7).getCell(10).setCellValue(user.getOrganization());
        sheet.getRow(8).getCell(8).setCellValue(agent.getOrganization());
        if(works.size() <= 3)
        {
            for(int i = 0; i < works.size(); i++)
            {
                sheet.getRow(12 + i).getCell(0).setCellValue(1 + i);
                sheet.getRow(12 + i).getCell(1).setCellValue(works.get(i).getName());
                sheet.getRow(12 + i).getCell(10).setCellValue(works.get(i).getPrice());
                sheet.getRow(12 + i).getCell(14).setCellValue(works.get(i).getPrice() * 0.2);
                sheet.getRow(12 + i).getCell(17).setCellValue(works.get(i).getPrice() * 0.2 + works.get(i).getPrice());
                sumNDS += works.get(i).getPrice() * 0.2;
                sumCostNDS += works.get(i).getPrice();
            }
            sumCostNDS += sumNDS;
            Long rubCostNDS = sumCostNDS.longValue();
            Double copCostNDS = (sumCostNDS - rubCostNDS) * 100;
            sheet.getRow(15).getCell(17).setCellValue(sumCostNDS);
            sheet.getRow(18).getCell(0).setCellValue(nf.format(rubCostNDS) + " руб. " + copCostNDS.longValue() + " коп.");
            sheet.getRow(19).getCell(3).setCellValue(user.getOrganization());
            sheet.getRow(19).getCell(13).setCellValue(agent.getOrganization());
            sheet.getRow(21).getCell(1).setCellValue(user.getUnp());
            sheet.getRow(21).getCell(12).setCellValue(agent.getUnp());
            sheet.getRow(22).getCell(1).setCellValue(user.getAddress());
            sheet.getRow(22).getCell(12).setCellValue(agent.getAddress());
            sheet.getRow(24).getCell(1).setCellValue(user.getRs());
            sheet.getRow(24).getCell(11).setCellValue(agent.getRs());
            sheet.getRow(25).getCell(1).setCellValue(user.getKs());
            sheet.getRow(25).getCell(11).setCellValue(agent.getKs());
            sheet.getRow(26).getCell(1).setCellValue(user.getBank());
            sheet.getRow(26).getCell(12).setCellValue(agent.getBank());
            sheet.getRow(28).getCell(1).setCellValue(user.getBik());
            sheet.getRow(28).getCell(12).setCellValue(agent.getBik());
            sheet.getRow(29).getCell(2).setCellValue(user.getPhone());
            sheet.getRow(29).getCell(13).setCellValue(agent.getPhone());
        }
        return workbook;
    }

    public File writeToFileTN(File file, HSSFWorkbook workbook) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        return file;
    }

    public File writeToFileTTN(File file, HSSFWorkbook workbook) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        return file;
    }

    public File writeToFileASPR(File file, XSSFWorkbook workbook) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        return file;
    }

    public Document addDocumentTN(String username, Long id, List<Product> products) throws IOException
    {
        Document doc = new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss");
        Date date = new Date();
        File file = createFileTN();
        doc.setType(FilenameUtils.getExtension(file.getName()));
        byte[] b = Files.readAllBytes(file.toPath());
        HSSFWorkbook workbook = preparationFileTN(file, username, id, products);
        File newFile = writeToFileTN(file, workbook);
        byte[] document = Files.readAllBytes(newFile.toPath());
        FileUtils.writeByteArrayToFile(file, b);
        doc.setDocument(document);
        doc.setName(newFile.getName().substring(0, newFile.getName().indexOf('.')) + " (" + sdf.format(date) + ")");
        doc.setDate(sdf.format(date));
        doc.setUser(userDAO.findByUsername(username));
        return documentDAO.save(doc);
    }

    public Document addDocumentTTN(String username, Long agent_id, Long driver_id, List<Product> products) throws IOException
    {
        Document doc = new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss");
        Date date = new Date();
        File file = createFileTTN();
        doc.setType(FilenameUtils.getExtension(file.getName()));
        byte[] b = Files.readAllBytes(file.toPath());
        HSSFWorkbook workbook = preparationFileTTN(file, username, agent_id, driver_id, products);
        File newFile = writeToFileTTN(file, workbook);
        byte[] document = Files.readAllBytes(newFile.toPath());
        FileUtils.writeByteArrayToFile(file, b);
        doc.setDocument(document);
        doc.setName(newFile.getName().substring(0, newFile.getName().indexOf('.')) + " (" + sdf.format(date) + ")");
        doc.setDate(sdf.format(date));
        doc.setUser(userDAO.findByUsername(username));
        return documentDAO.save(doc);
    }

    public Document addDocumentASPR(String username, Long agent_id, List<Work> works) throws IOException
    {
        Document doc = new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss");
        Date date = new Date();
        File file = createFileASPR();
        doc.setType(FilenameUtils.getExtension(file.getName()));
        byte[] b = Files.readAllBytes(file.toPath());
        XSSFWorkbook workbook = preparationFileASPR(file, username, agent_id, works);
        File newFile = writeToFileASPR(file, workbook);
        byte[] document = Files.readAllBytes(newFile.toPath());
        FileUtils.writeByteArrayToFile(file, b);
        doc.setDocument(document);
        doc.setName(newFile.getName().substring(0, newFile.getName().indexOf('.')) + " (" + sdf.format(date) + ")");
        doc.setDate(sdf.format(date));
        doc.setUser(userDAO.findByUsername(username));
        return documentDAO.save(doc);
    }
}
