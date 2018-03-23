package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/email/send", method = RequestMethod.POST)
    public @ResponseBody Object email() {
        System.setProperty( "http.proxyHost", "192.168.71.2" );
        System.setProperty( "http.proxyPort", "3128" );
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("sanya250497@gmail.com");
        email.setTo("sanya250497@gmail.com");
        email.setSubject("Password");
        email.setText("New password: test");
        mailSender.send(email);
        return null;
    }


}
