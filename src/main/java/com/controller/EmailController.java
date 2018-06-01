package com.controller;

import com.dao.UserDAO;
import com.errors.Error;
import com.model.User;
import com.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "api/v1/email")
@Api(tags = "Email", description = "APIs for working with email", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation(value = "Send new password on email", produces = MediaType.APPLICATION_JSON_VALUE, response = Object.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return success: true", response = Object.class),
            @ApiResponse(code = 201, message = "Return success: true", response = Object.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> sendMessage(@RequestBody Map<String, String> message) {
        Error error;
        String email = message.get("email");
        if("".equals(email)) {
            error = new Error(Error.EMPTY_FIELD_MESSAGE, Error.EMPTY_FIELD_STATUS, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
        }
        User user = userDAO.findByEmail(email);
        if(user == null) {
            error = new Error(Error.USER_IS_NOT_REGISTERED_MESSAGE, Error.USER_IS_NOT_REGISTERED_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        }
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("sanya250497@gmail.com");
        mail.setTo(email);
        mail.setSubject("Password");
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        String password = randomStringGenerator.generate(8);
        userService.updatePassword(user.getUsername(), password);
        mail.setText("New password: " + password);
        mailSender.send(mail);
        return new ResponseEntity<>("{\"success\":true}", HttpStatus.OK);
    }
}
