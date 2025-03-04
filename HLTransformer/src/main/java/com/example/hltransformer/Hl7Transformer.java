package com.example.hltransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.message.ADT_A39;
import ca.uhn.hl7v2.parser.PipeParser;
import com.example.hltransformer.mapper.A39Mapper;
import com.example.hltransformer.model.MainData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Service
public class Hl7Transformer {

    @Autowired
    private A39Mapper a39Mapper;

    public String transformXmlToHl7(String xmlData) throws JAXBException, HL7Exception {

        try {
            // Create JAXBContext for the MainData class
            JAXBContext context = JAXBContext.newInstance(MainData.class);

            // Create Unmarshaller to convert XML to Java object
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Use StringReader to read the XML string
            StringReader reader = new StringReader(xmlData.replace("��",""));

            // Unmarshal the XML into the MainData Java object
            MainData mainDataObj = (MainData) unmarshaller.unmarshal(reader);


        // Step 3: Use HAPI HL7 to create an HL7 message
            ADT_A39 adtMessage = a39Mapper.ADT_A39Mapper(mainDataObj);
            PipeParser pipeParser = new PipeParser();
            String sd = pipeParser.encode(adtMessage);
            return pipeParser.encode(adtMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

