package com.service.document;

import com.dao.AgentDAO;
import com.dao.DocumentDAO;
import com.model.Agent;
import com.model.Document;
import com.service.agent.AgentService;
import com.utils.Error;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardTypeComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
}
