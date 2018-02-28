package com.asptt.resabackend.resources.plongee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resa.commons.exception.Technical;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.entity.NiveauAutonomie;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypePlongee;

@Repository("plongeeDao")
public class PlongeeDaoImpl implements Dao<Plongee> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PlongeeDaoImpl.class);

	@Autowired
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void closeConnexion(Connection connexion) {
		try {
			if (null != connexion) {
				connexion.close();
			}
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, "Impossible de cloturer la connexion");
		}
		connexion = null;
	}

	@Override
	public Plongee create(Plongee resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plongee get(String id) {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = conex.prepareStatement(
					"select * from PLONGEE where idPLONGEES = ? ");
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			Plongee plongee = null;
			if (rs.next()) {
				plongee = wrapPlongee(rs);
			}
			return plongee;
		} catch (SQLException e) {
			// log.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	
	}

	@Override
	public List<Plongee> find() {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = conex.prepareStatement("select * from PLONGEE order by DATE_PLONGEE");
			ResultSet rs = st.executeQuery();
			List<Plongee> plongees = new ArrayList<>();
			while (rs.next()) {
				Plongee plongee = wrapPlongee(rs);
				plongees.add(plongee);
			}
			return plongees;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}

	}

	@Override
	public List<Plongee> find(MultivaluedMap<String, String> criteria) {

		PreparedStatement st;
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();

			StringBuffer sb = new StringBuffer("select * from PLONGEE p");
			if (criteria.isEmpty()) {
				st = conex.prepareStatement(sb.toString());
			} else {
				int num = 0;
				Map<Integer, List<String>> parameters = new HashMap<>();
				for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
					num = num + 1;
					LOGGER.debug("Index=" + num + "Key : " + entry.getKey() + " Value : " + entry.getValue());
					List<String> ent = new ArrayList<>();
					ent.add(entry.getKey());
					ent.add(entry.getValue().get(0));
					parameters.put(Integer.valueOf(num), ent);
				}
				sb.append(" where ");
				parameters.entrySet().stream().forEach(entry -> {
					sb.append(" " + entry.getValue().get(0) + " LIKE ? AND ");
				});
				sb.delete(sb.length()-5, sb.length()-1);
				sb.append(" order by NOM");
				st = conex.prepareStatement(sb.toString());
				for(Map.Entry<Integer, List<String>> entry : parameters.entrySet()){
					try {
						st.setString(entry.getKey(), entry.getValue().get(1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}
			LOGGER.info("Requete SQL Find avec critera=" + st.toString());
			ResultSet rs = st.executeQuery();
			List<Plongee> plongees = new ArrayList<>();
			while (rs.next()) {
				Plongee plongee = wrapPlongee(rs);
				plongees.add(plongee);
			}
			return plongees;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	@Override
	public Plongee update(Plongee resource) {
        Connection conex = null;
        try {
            conex = getDataSource().getConnection();
            StringBuffer sb = new StringBuffer();
            sb.append("UPDATE ADHERENT");
            sb.append(" SET NIVEAU = ?,");
            sb.append(" TELEPHONE = ?,");
            sb.append(" MAIL = ?,");
            sb.append(" ENCADRANT = ?,");
            sb.append(" PILOTE = ?,");
            sb.append(" ACTIF = ?,");
            sb.append(" NOM = ?,");
            sb.append(" PRENOM = ?,");
            sb.append(" DATE_CM = ?,");
            sb.append(" ANNEE_COTI = ?,");
            sb.append(" TIV = ?,");
            sb.append(" COMMENTAIRE = ?,");
            sb.append(" APTITUDE = ?");
            sb.append(" WHERE license = ?");

            PreparedStatement st = conex.prepareStatement(sb.toString());
////            st.setString(1, resource.getNiveau());
////            st.setString(2, resource.getTelephone());
////            st.setString(3, resource.getMail());
////            if (resource.isEncadrent()) {
////                st.setString(4, resource.getEncadrement());
////            } else {
////                st.setString(4, null);
////            }
////            if (resource.isPilote()) {
////                st.setInt(5, 1);
////            } else {
////                st.setInt(5, 0);
////            }
////            st.setInt(6, resource.getActifInt());
////            st.setString(7, resource.getNom());
////            st.setString(8, resource.getPrenom());
////            Timestamp ts = new Timestamp(resource.getDateCM().getTime());
////            st.setTimestamp(9, ts);
////            st.setInt(10, resource.getAnneeCotisation());
////            if (resource.isTiv()) {
////                st.setInt(11, 1);
////            } else {
////                st.setInt(11, 0);
////            }
////            st.setString(12, resource.getCommentaire());
////            if (resource.isAptitude()) {
////                st.setString(13, resource.getAptitude());
////            } else {
////                st.setString(13, null);
////            }
////            st.setString(14, resource.getNumeroLicense());
//            if (st.executeUpdate() == 0) {
//                throw new TechnicalException(Technical.GENERIC,
//                        "L'adhérent n'a pu être modifié");
//            }
//            sb = new StringBuffer();
//
//            // gestion des roles 1er temps : on supprime les roles
//            sb.append("DELETE FROM REL_ADHERENT_ROLES WHERE ADHERENT_LICENSE = ? ");
//            PreparedStatement st1 = conex.prepareStatement(sb.toString());
//            st1.setString(1, resource.getNumeroLicense());
//            if (st1.executeUpdate() == 0) {
//            	LOGGER.info("Impossible de supprimer les roles de l'plongee");
////                throw new TechnicalException(Technical.GENERIC,"Impossible de supprimer les roles de l'plongee");
//            }
//            // gestion des roles 2ieme temps : on les re-creer
//            Iterator it = resource.getRoles().iterator();
//            sb = new StringBuffer();
//            while (it.hasNext()) {
//                sb.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`, `ROLES_idROLES`)");
//                sb.append(" VALUES (?, ?)");
//                st = conex.prepareStatement(sb.toString());
//                st.setString(1, resource.getNumeroLicense());
//                Plongee.Roles role =(Plongee.Roles)it.next();
//                int id = getIdRole(role.name());
//                st.setInt(2, id);
//                if (st.executeUpdate() == 0) {
//                    throw new TechnicalException(Technical.GENERIC,
//                            "Le role n'a pu être enregistré");
//                }
//                sb = null;
//                sb = new StringBuffer();
//            }
//            //Mise à jour des contacts
//            updateContactsForPlongee(resource);

            return resource;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
        } finally {
            closeConnexion(conex);
        }
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

    private Plongee wrapPlongee(ResultSet rs) throws SQLException, TechnicalException {
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
        if(null == warning){
            warning="";
        }
        Plongee plongee = new Plongee();
        plongee.setId(id);
        plongee.setTypePlongee(typePlongee);
        //Mise à jour de la date
        //maj de l'heure de la plongée en fonction du type
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
        List<String> participants = getAdherentsInscrits(plongee, null, null, null);
        plongee.setParticipants(participants);
//        plongee.setDp();
//        for (Adherent a : participants) {
//            if (a.isPilote()) {
//                plongee.setPilote(a);
//            }
//        }
        List<String> attente = getAdherentsWaiting(plongee);
        plongee.setParticipantsEnAttente(attente);
        plongee.setWarning(warning);
        return plongee;
    }


    public List<String> getAdherentsInscrits(Plongee plongee, String niveauPlongeur, String niveauEncadrement, String trie)
            throws TechnicalException {
        PreparedStatement st;
        Connection conex = null;
        try {
            conex = getDataSource().getConnection();
            StringBuffer sb = new StringBuffer("select * from PLONGEE p, INSCRIPTION_PLONGEE i, ADHERENT a ");
            sb.append(" where idPLONGEES = ?");
            sb.append(" and idPLONGEES = PLONGEES_idPLONGEES ");
            sb.append(" and ADHERENT_LICENSE = LICENSE");
            sb.append(" and DATE_ANNUL_PLONGEE is null");
            if (null != niveauPlongeur) {
                sb.append(" and a.NIVEAU = ?");
            }
            if (null != niveauEncadrement) {
                if (niveauEncadrement.equalsIgnoreCase("TOUS")) {
                    sb.append(" and a.ENCADRANT is not null");
                } else {
                    sb.append(" and a.ENCADRANT = ?");
                }
            }
            if (null == trie || trie.equalsIgnoreCase("date")) {
                sb.append(" order by DATE_INSCRIPTION");
            } else if (trie.equalsIgnoreCase("nom")) {
                sb.append(" order by NOM");
            }
            st = conex.prepareStatement(sb.toString());
            st.setInt(1, plongee.getId());
            if (null != niveauPlongeur) {
                st.setString(2, niveauPlongeur);
                if (null != niveauEncadrement && !niveauEncadrement.equalsIgnoreCase("TOUS")) {
                    st.setString(3, niveauEncadrement);
                }
            } else {
                if (null != niveauEncadrement && !niveauEncadrement.equalsIgnoreCase("TOUS")) {
                    st.setString(2, niveauEncadrement);
                }
            }
            ResultSet rs = st.executeQuery();
            List<String> adherents = new ArrayList<>();
            while (rs.next()) {
//                Adherent adherent = wrapAdherent(rs);
            	String licence = rs.getString("LICENSE");
                adherents.add(licence);
            }
            return adherents;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
        } finally {
            closeConnexion(conex);
        }
    }

    /**
     * Retourne la liste des adherents en liste d'attente sur la plongée trie
     * par le DATE_ATTENTE
     */
    public List<String> getAdherentsWaiting(Plongee plongee)
            throws TechnicalException {
        PreparedStatement st;
        Connection conex = null;
        try {
            conex = getDataSource().getConnection();
            StringBuffer sb = new StringBuffer(
                    "select * from PLONGEE p, LISTE_ATTENTE la, ADHERENT a ");
            sb.append(" where idPLONGEES = ?");
            sb.append(" and idPLONGEES = PLONGEES_idPLONGEES ");
            sb.append(" and ADHERENT_LICENSE = LICENSE ");
            sb.append(" and DATE_INSCRIPTION is null");
            sb.append(" and SUPPRIMER = 0");
            sb.append(" order by DATE_ATTENTE");
            st = conex.prepareStatement(sb.toString());
            st.setInt(1, plongee.getId());
            ResultSet rs = st.executeQuery();
            List<String> adherents = new ArrayList<>();
            while (rs.next()) {
//                Adherent adherent = wrapAdherent(rs);
            	String licence = rs.getString("LICENSE");
                adherents.add(licence);
            }
            return adherents;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
        } finally {
            closeConnexion(conex);
        }
    }

}
