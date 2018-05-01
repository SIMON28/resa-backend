package com.asptt.resabackend.resources.adherent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import com.asptt.resabackend.Application;
import com.asptt.resabackend.entity.Adherent;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration (classes=Application.class)
public class AdherentResourceImplTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdherentControllerMVCTest.class);

    private StandaloneMockMvcBuilder mockMvc;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(AdherentResourceImpl.class)
                .setMessageConverters(new MappingJackson2HttpMessageConverter());

    }
    
    @Test
	public void createAdherent() throws Exception {
			Adherent adh = new Adherent();
		adh.setNumeroLicense("unnumero");

//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.post("adherent",adh)
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON)
//				.header("Authorization", "");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.get("/api/v1/adherent/{id}", "096042").contextPath("/api/v1")
				.get("/api/v1/adherent")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
		MvcResult result = mockMvc.build().perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		
	}


}
