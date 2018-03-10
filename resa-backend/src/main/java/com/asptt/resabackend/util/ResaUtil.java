package com.asptt.resabackend.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.mapper.SqlSearchCriteria;
import com.asptt.resabackend.resources.NomResources;

@Component("resaUtil")
public class ResaUtil {

	static final long ONE_DAY_IN_MILLIS = 86400000;
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResaUtil.class);

	private static Environment envstat;

	@Autowired
	private Environment env;

	@PostConstruct
	private void initStaticEnv() {
		envstat = this.env;
	}

	public static int calculNbHeure(Date dateDuJour, Date datePlongee) throws TechnicalException {
		long diffMilli = datePlongee.getTime() - dateDuJour.getTime();
		int nombreHeure = Long.valueOf((diffMilli / 1000) / 3600).intValue();
		return nombreHeure;
	}

	public static int calculNbJour(Date dateDebut, Date dateFin) throws TechnicalException {
		GregorianCalendar gcDeb = new GregorianCalendar();
		gcDeb.setTime(dateDebut);
		GregorianCalendar gcFin = new GregorianCalendar();
		gcFin.setTime(dateFin);
		gcDeb.set(GregorianCalendar.HOUR, 0);
		gcDeb.set(GregorianCalendar.MINUTE, 0);
		gcDeb.set(GregorianCalendar.SECOND, 0);
		gcFin.set(GregorianCalendar.HOUR, 0);
		gcFin.set(GregorianCalendar.MINUTE, 0);
		gcFin.set(GregorianCalendar.SECOND, 0);
		dateDebut.setTime(gcDeb.getTimeInMillis());
		dateFin.setTime(gcFin.getTimeInMillis());
		long diffMilli = dateFin.getTime() - dateDebut.getTime();
		float nombreJour = ((diffMilli / (float) 1000) / 3600) / 24;
		return Double.valueOf(Math.ceil(nombreJour)).intValue();
	}

	/**
	 * Calcul le nombre de jour entre deux dates
	 *
	 * @param dateCM
	 * @param dateDuJour
	 * @return long nombreJour
	 * @throws TechnicalException
	 */
	public static long checkDateCM(Date dateCM, Date dateDuJour) throws TechnicalException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		GregorianCalendar gcCM = new GregorianCalendar();
		gcCM.setTime(dateCM);
		dateCM.setTime(gcCM.getTimeInMillis());
		// System.out.println("DATE de DEBUT:"+sdf.format(dateCM));

		GregorianCalendar gcFin = new GregorianCalendar();
		gcFin.setTime(dateCM);
		gcFin.add(GregorianCalendar.YEAR, +1);
		Date dateDeFin = new Date(gcFin.getTimeInMillis());
		// System.out.println("DATE de FIN:"+sdf.format(dateDeFin));

		GregorianCalendar gcDuJour = new GregorianCalendar();
		gcDuJour.setTime(dateDuJour);
		// System.out.println("DATE du JOUR:"+sdf.format(dateDuJour));

		long nombreJour = (dateDeFin.getTime() - dateDuJour.getTime()) / ONE_DAY_IN_MILLIS;

		return nombreJour;

	}

	public static String getDateString(Date e_d) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(e_d);
	}

	public static String capitalizeFirstLetter(String value) {
		if (value == null) {
			return null;
		}
		if (value.length() == 0) {
			return value;
		}

		StringBuilder result = new StringBuilder(value.toLowerCase());
		result.replace(0, 1, result.substring(0, 1).toUpperCase());
		return result.toString();
	}

	public static String getJourDatePlongee(Date date) {
		// Mise en forme de la date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		StringBuilder sb = new StringBuilder(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.FRANCE));
		sb.append(" ");
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append(" ");
		sb.append(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRANCE));
		sb.append(" ");
		sb.append(cal.get(Calendar.YEAR));
		return sb.toString();
	}

	public static String getHeurePlongee(Date date) {
		// Mise en forme de la date
		Calendar cal = Calendar.getInstance();
		String heure = "";
		String minute = "";
		cal.setTime(date);
		if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
			heure = "0" + cal.get(Calendar.HOUR_OF_DAY);
		} else {
			heure = "" + cal.get(Calendar.HOUR_OF_DAY);
		}
		if (cal.get(Calendar.MINUTE) < 10) {
			minute = "0" + cal.get(Calendar.MINUTE);
		} else {
			minute = "" + cal.get(Calendar.MINUTE);
		}
		String heureAffichee = heure + "h" + minute;
		return heureAffichee;
	}

	/**
	 * Construit le moceaux de la requete SQL avec les criteres de recherche
	 * 
	 * @param criteria
	 *            MultivaluedMap<String, String> criteria dont on extrait les
	 *            criteres hors limit et count
	 * @param forCount
	 *            Boolean si true requete destinée pour un count et n'inclus pas les
	 *            limit et order by
	 * @return SqlSearchCriteria
	 */
	public static SqlSearchCriteria createSqlParameters(MultivaluedMap<String, String> criteria, Boolean forCount,
			NomResources resource) {
		String limit = envstat.getProperty("plongee.limit");
		String offset = envstat.getProperty("plongee.offset");
		StringBuffer sb = new StringBuffer();
		int nbParam = 0;
		List<Plongee> plongees = new ArrayList<>();
		Object[] args = null;
		int num = 0;
		Map<Integer, List<String>> parameters = new HashMap<>();
		// boucle pour constistuer la liste des parametres en supprimant la limit et
		// offset
		// et en attribuant un numero d'ordre pour les arguments
		for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
			LOGGER.debug("Index=" + num + "Key : " + entry.getKey() + " Value : " + entry.getValue());
			if (entry.getKey().equals("limit") || entry.getKey().equals("offset")) {
				if (entry.getKey().equals("limit")) {
					limit = entry.getValue().get(0);
				}
				if (entry.getKey().equals("offset")) {
					offset = entry.getValue().get(0);
				}
			} else {
				num = num + 1;
				List<String> ent = new ArrayList<>();
				ent.add(entry.getKey());
				ent.add(entry.getValue().get(0));
				parameters.put(Integer.valueOf(num), ent);
			}
		}
		nbParam = parameters.size();
		if (nbParam > 0) {
			args = new Object[] { nbParam };
			sb.append(" where ");
			// boucle pour constituer la clause where
			parameters.entrySet().stream().forEach(entry -> {
				sb.append(" " + entry.getValue().get(0) + " LIKE ? AND ");
			});
			sb.delete(sb.length() - 5, sb.length() - 1);
			// boucle pour populer le preparedStatement 'st'
			for (Map.Entry<Integer, List<String>> entry : parameters.entrySet()) {
				// st.setString(entry.getKey(), entry.getValue().get(1));
				LOGGER.info("Constitution tableau args indice [" + entry.getKey() + "]  valeur ["
						+ entry.getValue().get(1) + "]");
				args[(entry.getKey() - 1)] = entry.getValue().get(1);
			}
		}
		if (!forCount) {
			switch (resource) {
			case PLONGEE:
				sb.append(" order by DATE_PLONGEE desc");
				break;
			case ADHERENT:
				sb.append(" order by NOM");
				break;
			case CONTACTURGENT:
				sb.append(" order by NOM");
				break;
			}
			sb.append(" LIMIT " + offset + " , " + limit + " ");
		}
		return new SqlSearchCriteria(nbParam, sb, args);
	}

}
