package com.service.sharing;

import com.dao.AgentDAO;
import com.dao.DocumentDAO;
import com.dao.UserDAO;
import com.model.Agent;
import com.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class SharingServiceImpl implements SharingService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AgentDAO agentDao;

    @Autowired
    private DocumentDAO documentDAO;

    public Set<Document> getAllSharedDocuments(String username) {
        Set<Document> documents = new HashSet<Document>();
        String unp = userDAO.findByUsername(username).getUnp();
        for(Agent agent : agentDao.findAllByUnp(unp)) {
            for(Document document : agent.getDocuments()) {
                documents.add(document);
            }
        }
        return documents;
    }

    public Document getSharedDocumentById(String username, Long id) {
        String unp = userDAO.findByUsername(username).getUnp();
        for(Agent agent : agentDao.findAllByUnp(unp)) {
            for(Document doc : agent.getDocuments()) {
                if(id == doc.getId()) {
                    return documentDAO.findOne(id);
                }
            }
        }
        return null;
    }

    public Document shareDocument(String username, Long agent_id, Long document_id) {
        Document document = new Document();
        for(Document doc : userDAO.findByUsername(username).getDocuments()) {
            if(document_id == doc.getId()) {
                document = doc;
            }
        }
        for(Agent agnt : userDAO.findByUsername(username).getAgents()) {
            if(agent_id == agnt.getId()) {
                document.setAgent(agnt);
                return documentDAO.save(document);
            }
        }
        return null;
    }

    public Document deleteSharedDocument(String username, Long id) {
        /*for(Document doc : userDAO.findByUsername(username).getDocuments()) {
            if(id == doc.getId()) {
                Document document = documentDAO.findOne(id);
                document.setAgent(null);
                documentDAO.save(document);
                return document;
            }
        }
        return null;*/
        Document document = getSharedDocumentById(username, id);
        document.setAgent(null);
        documentDAO.save(document);
        return document;
    }
}
