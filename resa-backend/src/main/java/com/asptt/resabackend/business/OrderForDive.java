package com.asptt.resabackend.business;

import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public class OrderForDive {

	public OrderForDive() {
		// TODO Auto-generated constructor stub
	}
	
	TypeOrderForDive action;
	String adherentId;
	Date registerDate;
	Date waitingDate;
	Date cancelDate;
	
	
	public TypeOrderForDive getAction() {
		return action;
	}
	public void setAction(TypeOrderForDive action) {
		this.action = action;
	}
	public String getAdherentId() {
		return adherentId;
	}
	public void setAdherentId(String adherentId) {
		this.adherentId = adherentId;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public Date getWaitingDate() {
		return waitingDate;
	}
	public void setWaitingDate(Date waitingDate) {
		this.waitingDate = waitingDate;
	}
	
}
