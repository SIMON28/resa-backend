package com.asptt.resabackend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Encadrement;
import com.asptt.resabackend.entity.Aptitude;
import com.asptt.resabackend.entity.NiveauAutonomie;
@Component
public class AdherentRowMapper implements RowMapper<Adherent> {

	@Override
	public Adherent mapRow(ResultSet rs, int rowNum) throws SQLException {
		String licence = rs.getString("LICENSE");
		String nom = rs.getString("NOM");
		String prenom = rs.getString("PRENOM");
		NiveauAutonomie niveau = NiveauAutonomie.valueOf(rs.getString("NIVEAU"));
		String telephone = rs.getString("TELEPHONE");
		String mail = rs.getString("MAIL");
		int pilote = rs.getInt("PILOTE");
		Adherent adherent = new Adherent();
		adherent.setNumeroLicense(licence);
		adherent.setNom(nom);
		adherent.setPrenom(prenom);
		adherent.setEnumNiveau(niveau);
		adherent.setTelephone(telephone);
		adherent.setMail(mail);
		Encadrement encadrant = null;
		if (null != rs.getString("ENCADRANT")) {
			encadrant = Encadrement.valueOf(rs.getString("ENCADRANT"));
		}
		adherent.setEnumEncadrement(encadrant);
//		adherent.setRoles(roles);
		adherent.setActifInt(rs.getInt("ACTIF"));
		if (pilote == 1) {
			adherent.setPilote(true);
		} else {
			adherent.setPilote(false);
		}
		int tiv = rs.getInt("TIV");
		if (tiv == 1) {
			adherent.setTiv(true);
		} else {
			adherent.setTiv(false);
		}
		adherent.setCommentaire(rs.getString("COMMENTAIRE"));
		adherent.setPassword(rs.getString("PASSWORD"));
		// Pour les Contacts
//		adherent.setContacts(contactUrgents);

		// Pour les nouveaux champs date du certificat medical et annee de cotisation
		Date dateCM = rs.getDate("DATE_CM");
		adherent.setDateCM(dateCM);
		adherent.setAnneeCotisation(rs.getInt("ANNEE_COTI"));
		Aptitude aptitude = null;
		if (null != rs.getString("APTITUDE")) {
			aptitude = Aptitude.valueOf(rs.getString("APTITUDE"));
		}
		adherent.setEnumAptitude(aptitude);
		return adherent;
	}

}
