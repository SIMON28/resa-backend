package com.asptt.resabackend.resources.adherent;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asptt.resabackend.entity.Adherent;

@RestController
@RequestMapping("/adh")
public class AdherentController {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdherentDaoImpl.class);

	@Autowired
	private AdherentService service;

	@GetMapping
    public List<Adherent> find() {
		LOGGER.debug("find all adherent");
        return service.find();
    }
	
}
