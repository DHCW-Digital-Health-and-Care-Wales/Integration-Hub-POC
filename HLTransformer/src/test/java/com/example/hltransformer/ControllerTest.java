package com.example.hltransformer;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.hltransformer.utils.XmlLoader.loadXmlFromResource;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Hl7Transformer hl7Transformer;

    @Test
    void testPostCall() throws Exception {
        String xml = loadXmlFromResource("sample_Fullpayload.xml");
        // Mock the behavior of the Hl7Transformer
//        when(hl7Transformer.transformXmlToHl7(xml)).thenReturn(hl7Response);


        when(hl7Transformer.transformXmlToHl7(anyString())).thenReturn(new String());
//        doReturn(anyString()).when(hl7Transformer).transformXmlToHl7(xml);
        mockMvc.perform(MockMvcRequestBuilders.post("/hl7/transform")
                        .contentType(MediaType.APPLICATION_XML_VALUE)  // Set the content type to JSON
                        .content(xml))  // Set the request body (the HL7 message JSON)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
