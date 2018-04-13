package com.asptt.resabackend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.asptt.resabackend.entity.NiveauAutonomie;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypePlongee;
@Component
public class PlongeeRowMapper implements RowMapper<Plongee> {

	@Override
	public Plongee mapRow(ResultSet rs, int arg1) throws SQLException {
		int id = rs.getInt("idPLONGEES");
		Date datePlongee = rs.getTimestamp("DATE_PLONGEE");
		Date dateReservation = rs.getTimestamp("DATE_RESERVATION");
		TypePlongee typePlongee = TypePlongee.valueOf(rs.getString("TYPEPLONGEE"));
		String nMin = rs.getString("NIVEAU_MINI");
		NiveauAutonomie niveauMini = NiveauAutonomie.P0;
		if (null != nMin) {
			niveauMini = NiveauAutonomie.valueOf(nMin);
		}
		int ouvertForcee = rs.getInt("OUVERTURE_FORCEE");
		int nbMaxPlongeur = rs.getInt("NB_MAX_PLG");
		String warning = rs.getString("WARNING");
		if (null == warning) {
			warning = "";
		}
		Plongee plongee = new Plongee();
		plongee.setId(id);
		plongee.setTypePlongee(typePlongee);
		// Mise à jour de la date
		// maj de l'heure de la plongée en fonction du type
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(datePlongee);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		plongee.setDatePlongee(datePlongee);
		plongee.setDateReservation(dateReservation);
		plongee.setEnumNiveauMinimum(niveauMini);
		plongee.setNbMaxPlaces(nbMaxPlongeur);
		if (ouvertForcee == 1) {
			plongee.setOuvertureForcee(true);
		} else {
			plongee.setOuvertureForcee(false);
		}
		// Ne pas oublier de mettre à jour les liste de participants
//		plongee.setParticipants(adhInscrits);
//		plongee.setParticipantsEnAttente(adhAttente);
		plongee.setWarning(warning);
		return plongee;
	}

}
