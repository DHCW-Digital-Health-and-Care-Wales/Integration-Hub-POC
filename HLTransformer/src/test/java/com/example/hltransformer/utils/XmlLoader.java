package com.example.hltransformer.utils;

import com.example.hltransformer.model.MainData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class XmlLoader {

    public static String loadXmlFromResource(String fileName) {
        // Use try-with-resources to ensure proper resource management
        try (InputStream inputStream = XmlLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + fileName);
            }

            // Convert InputStream to String
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static MainData loadMainDataObj(String fileName) throws JAXBException {
        MainData mainDataObj = new MainData();
        String xml = loadXmlFromResource(fileName);
        JAXBContext context = JAXBContext.newInstance(MainData.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml.replace("��",""));
        mainDataObj = (MainData) unmarshaller.unmarshal(reader);
        return mainDataObj;
    }
}
