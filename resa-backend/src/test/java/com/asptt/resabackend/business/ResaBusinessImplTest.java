package com.asptt.resabackend.business;

import static org.mockito.Mockito.when;

import java.text.MessageFormat;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asptt.resa.commons.exception.BadUsageException;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resabackend.ApplicationTest;
import com.asptt.resabackend.util.ResaBackendMessage;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTest.class)
@ActiveProfiles(profiles = "test")
public class ResaBusinessImplTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResaBusinessImplTest.class);

	@Autowired
	private ResaBusiness resaBusiness;

	@Test
	public void checkOrderForDiveInvalidAction() {
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
		queryParameters.add("param", "value");
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Integer plongeeId = 2770;
		OrderForDive order = new OrderForDive();
		// order.setAction(TypeOrderForDive.ADD);
		order.setAdherentId("245738");
		try {
		resaBusiness.checkOrderForDive(uriInfo, plongeeId, order);
		} catch (BadUsageException e) {
			LOGGER.info(e.toString()+"   code=["+e.getCode().getCode()+"]  category=["+e.getCategory()+"]  message=["+e.getMessage()+"]");
			Assert.assertEquals(ResaBackendMessage.REGISTRATION_ORDER_ACTION_NOT_FOUND, e.getMessage());
			Assert.assertEquals(4, e.getCode().getCode());
			Assert.assertEquals(400, e.getCategory());
		}
	}

	@Test
	public void checkOrderForDiveInvalidAdherentId() {
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
		queryParameters.add("param", "value");
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Integer plongeeId = 2770;
		OrderForDive order = new OrderForDive();
		order.setAction(TypeOrderForDive.ADD);
//		order.setAdherentId("245738");
		try {
			resaBusiness.checkOrderForDive(uriInfo, plongeeId, order);
		}
		catch (BadUsageException e) {
			LOGGER.info(e.toString()+"   code=["+e.getCode().getCode()+"]  category=["+e.getCategory()+"]  message=["+e.getMessage()+"]");
			Assert.assertEquals(ResaBackendMessage.REGISTRATION_ORDER_ADHERENTID_NOT_FOUND, e.getMessage());
			Assert.assertEquals(4, e.getCode().getCode());
			Assert.assertEquals(400, e.getCategory());
		}
	}

	@Test
	public void checkOrderForDiveAdherentNotFound() {
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
		queryParameters.add("param", "value");
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Integer plongeeId = 2770;
		OrderForDive order = new OrderForDive();
		order.setAction(TypeOrderForDive.ADD);
		order.setAdherentId("toto");
		try {
			resaBusiness.checkOrderForDive(uriInfo, plongeeId, order);
		}
		catch (NotFoundException e) {
			LOGGER.info(e.toString()+"   code=["+e.getCode().getCode()+"]  category=["+e.getCategory()+"]  message=["+e.getMessage()+"]");
			Assert.assertEquals(MessageFormat.format(ResaBackendMessage.ADHERENT_NOT_FOUND, order.getAdherentId()), e.getMessage()); 
			Assert.assertEquals(0, e.getCode().getCode());
			Assert.assertEquals(404, e.getCategory());
		}
	}

	@Test
	public void checkOrderForDivePlongeeNotFound() {
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
		queryParameters.add("param", "value");
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Integer plongeeId = 99991777;
		OrderForDive order = new OrderForDive();
		order.setAction(TypeOrderForDive.ADD);
		order.setAdherentId("toto");
		try {
			resaBusiness.checkOrderForDive(uriInfo, plongeeId, order);
		}
		catch (NotFoundException e) {
			LOGGER.info(e.toString()+"   code=["+e.getCode().getCode()+"]  category=["+e.getCategory()+"]  message=["+e.getMessage()+"]");
			Assert.assertEquals(MessageFormat.format(ResaBackendMessage.PLONGEE_NOT_FOUND, plongeeId.toString()), e.getMessage()); 
			Assert.assertEquals(0, e.getCode().getCode());
			Assert.assertEquals(404, e.getCategory());
		}
	}

//	@Test
	public void inscrirePlongee() {
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
		queryParameters.add("action", "consulter");
		queryParameters.add("param2", "value2");
		queryParameters.add("fields", "toto,titi,tata");
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Integer plongeeId = 2770;
		OrderForDive order = new OrderForDive();
		order.setAction(TypeOrderForDive.ADD);
		order.setAdherentId("245738");
		resaBusiness.checkOrderForDive(uriInfo, plongeeId, order);
	}
}
