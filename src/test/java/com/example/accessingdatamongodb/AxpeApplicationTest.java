package com.example.accessingdatamongodb;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AxpeApplicationTest {
	
	@InjectMocks
    private AxpeApplication axpeApplication;


    @Test
    public void contextLoads() {
        assertDoesNotThrow(() -> AxpeApplication.main(new String[]{}));
        assertNotNull(axpeApplication);
    }
}