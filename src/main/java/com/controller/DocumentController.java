package com.controller;

import com.model.Agent;
import com.model.Document;
import com.service.agent.AgentService;
import com.service.document.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(value = "documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
}
