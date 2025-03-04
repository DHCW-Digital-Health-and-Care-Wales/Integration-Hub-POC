package com.example.hltransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hl7")
public class Controller {

    @Autowired
    private final Hl7Transformer hl7Transformer;

    public Controller(Hl7Transformer hl7Transformer) {
        this.hl7Transformer = hl7Transformer;
    }

    @PostMapping(value = "/transform",consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> transformXmlToHl7(@RequestBody String xmlData) {
        try {
            String hl = hl7Transformer.transformXmlToHl7(xmlData);
            if(hl != null) {
                return new ResponseEntity<>(hl, HttpStatus.OK);
            }
            return new ResponseEntity<>("In-completed", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("In-completed", HttpStatus.BAD_REQUEST);
        }
    }
}
