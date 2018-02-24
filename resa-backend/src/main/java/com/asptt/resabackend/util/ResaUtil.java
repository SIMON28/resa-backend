package com.asptt.resabackend.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.slf4j.LoggerFactory;

import com.asptt.resa.commons.exception.TechnicalException;

public class ResaUtil {

    static final long ONE_DAY_IN_MILLIS = 86400000;
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(ResaUtil.class);

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
//		System.out.println("DATE de DEBUT:"+sdf.format(dateCM));

        GregorianCalendar gcFin = new GregorianCalendar();
        gcFin.setTime(dateCM);
        gcFin.add(GregorianCalendar.YEAR, +1);
        Date dateDeFin = new Date(gcFin.getTimeInMillis());
//		System.out.println("DATE de FIN:"+sdf.format(dateDeFin));

        GregorianCalendar gcDuJour = new GregorianCalendar();
        gcDuJour.setTime(dateDuJour);
//		System.out.println("DATE du JOUR:"+sdf.format(dateDuJour));

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

}
