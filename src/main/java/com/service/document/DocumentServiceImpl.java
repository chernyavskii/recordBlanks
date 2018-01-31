package com.service.document;

import com.dao.AgentDAO;
import com.dao.DocumentDAO;
import com.model.Agent;
import com.model.Document;
import com.service.agent.AgentService;
import com.utils.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDAO documentDAO;

    public Object addDocument(byte[] document)
    {
        Document doc = new Document();
        doc.setDocument(document);
        return documentDAO.save(doc);
    }
}
