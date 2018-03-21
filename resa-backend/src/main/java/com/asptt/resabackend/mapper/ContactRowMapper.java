package com.asptt.resabackend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.asptt.resabackend.entity.ContactUrgent;

public class ContactRowMapper implements RowMapper<ContactUrgent> {

	@Override
	public ContactUrgent mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("idCONTACT");
		String titre = rs.getString("TITRE");
		String nom = rs.getString("NOM");
		String prenom = rs.getString("PRENOM");
		String telephone = rs.getString("TELEPHONE");
		String mail = rs.getString("TELEPHTWO");

		ContactUrgent contact = new ContactUrgent();
		contact.setId(id);
		contact.setTitre(titre);
		contact.setNom(nom);
		contact.setPrenom(prenom);
		contact.setTelephone(telephone);
		contact.setTelephtwo(mail);

		return contact;
	}

}
