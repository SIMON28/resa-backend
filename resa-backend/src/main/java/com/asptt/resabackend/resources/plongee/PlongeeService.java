package com.asptt.resabackend.resources.plongee;

import java.util.List;

import com.asptt.resa.commons.service.Service;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypeActionReturnPlongee;

public interface PlongeeService extends Service<Plongee> {

	List<Plongee> findPlongeeForEncadrant(String property, String property2);

	List<Plongee> findPlongeeForAdherent(TypeActionReturnPlongee action);

	List<String> getAdherentsInscrits(Integer plongeeId, Object object, Object object2, Object object3);

	List<String> getAdherentsWaiting(Integer plongeeId);

}
