package com.asptt.resabackend.resources.adherent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.asptt.resabackend.entity.Adherent;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AdherentControllerMVCTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdherentControllerMVCTest.class);

	@Value("${pagination.limit}")
	private String basePath;
	
	@Autowired
    private MockMvc mockMvc;
    
	@Test
	public void createAdherent() throws Exception {
		LOGGER.info("recup variable dans les propoerties =["+basePath+"]");

		Adherent adh = new Adherent();
		adh.setNumeroLicense("unnumero");

//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.post("adherent",adh)
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON)
//				.header("Authorization", "");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.get("/api/v1/adherent/{id}", "096042").contextPath("/api/v1")
				.get("/adh")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());


	}
	
}
