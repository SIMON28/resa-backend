package com.asptt.resabackend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.asptt.resabackend.business.ResaBusinessRGUtil;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Aptitude;
import com.asptt.resabackend.entity.NiveauAutonomie;
import com.asptt.resabackend.entity.TypeEncadrement;

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
		Adherent adherent = new Adherent();
		adherent.setNumeroLicense(licence);
		adherent.setNom(nom);
		adherent.setPrenom(prenom);
		adherent.setNiveau(niveau);
		adherent.setTelephone(telephone);
		adherent.setMail(mail);
		TypeEncadrement encadrant = null;
		if (null != rs.getString("ENCADRANT")) {
			encadrant = TypeEncadrement.valueOf(rs.getString("ENCADRANT"));
		}
		adherent.setEncadrement(encadrant);
		//pour le boolean dp est mis à jour par les set de niveau ou d'encadrement
//		adherent.setDp(adherent.isDp());
		adherent.setDp(ResaBusinessRGUtil.isDp(adherent));

//		adherent.setRoles(roles);
//		adherent.setActifInt(rs.getInt("ACTIF"));
		adherent.setActif(rs.getInt("ACTIF"));
		int pilote = rs.getInt("PILOTE");
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
		adherent.setAptitude(aptitude);
		return adherent;
	}

}
