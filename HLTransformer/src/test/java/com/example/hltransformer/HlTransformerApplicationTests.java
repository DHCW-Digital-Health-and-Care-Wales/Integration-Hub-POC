package com.example.hltransformer;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HlTransformerApplicationTests {

    @InjectMocks
    private Hl7Transformer hl7Transformer;


    @Test
    void contextLoads() {
    }

}
