package com.asptt.resabackend.resources.plongee;

import java.util.List;

import com.asptt.resa.commons.service.Service;
import com.asptt.resabackend.entity.Plongee;

public interface PlongeeService extends Service<Plongee> {

	List<Plongee> findPlongeeForEncadrant(String property, String property2);

	List<Plongee> findPlongeeForAdherent();

}
