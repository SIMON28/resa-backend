package com.asptt.resabackend.resources.plongee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.Technical;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypeActionReturnPlongee;
import com.asptt.resabackend.mapper.PlongeeRowMapper;
import com.asptt.resabackend.mapper.SqlSearchCriteria;
import com.asptt.resabackend.resources.NomResources;
import com.asptt.resabackend.util.ResaBackendMessage;
import com.asptt.resabackend.util.ResaUtil;

@Repository("plongeeDao")
public class PlongeeDaoImpl implements PlongeeDao {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PlongeeDaoImpl.class);
	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Plongee create(Plongee resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plongee get(String id) {
		Plongee plongee = null;
		try {
			plongee = jdbcTemplate.queryForObject("select * from PLONGEE where idPLONGEES = ?",
					new PlongeeRowMapper(), id);
			plongee.setParticipants(getAdherentsInscrits(new Integer(id), null, null, null));
			plongee.setParticipantsEnAttente(getAdherentsWaiting(new Integer(id)));
			return plongee;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, MessageFormat.format(ResaBackendMessage.PLONGEE_NOT_FOUND,id));
		}

	}

	@Override
	public List<Plongee> find() {
		String limit = env.getProperty("pagination.limit");
		String sql = "select * from PLONGEE order by DATE_PLONGEE desc LIMIT " + limit;
		List<Plongee> plongees = new ArrayList<>();
		try {
			LOGGER.info("requete SQL:" + sql);
			plongees = jdbcTemplate.query(sql, new PlongeeRowMapper());
			// population des participants + attente
			for (Plongee plongee : plongees) {
				plongee.setParticipants(getAdherentsInscrits(plongee.getId(), null, null, null));
				plongee.setParticipantsEnAttente(getAdherentsWaiting(plongee.getId()));
			}
			return plongees;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le find() plongee [" + e.getMessage() + "]");
		}
	}

	@Override
	public Integer findCount(MultivaluedMap<String, String> criteria) {
		StringBuffer sql = new StringBuffer("select count(*) from PLONGEE p ");
		SqlSearchCriteria param = ResaUtil.createSqlParameters(criteria, true, NomResources.PLONGEE);
		Integer count;
		try {
			if (param.getNbParam() > 0) {
				sql.append(param.getSql());
				LOGGER.info("NbParam:" + param.getNbParam() + "SQL:" + sql.toString() + " taille args ["
						+ param.getArgs().length + "]");
				for (int i = 0; i < param.getArgs().length; i++) {
					LOGGER.info("arg" + i + " = [" + param.getArgs()[i] + "]");
				}
				count = jdbcTemplate.queryForObject(sql.toString(), param.getArgs(), Integer.class);
			} else {
				LOGGER.info("FindCount() sans parametre SQL:" + sql.toString());
				count = jdbcTemplate.queryForObject(sql.toString(), Integer.class);
			}
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le findCount()  [" + e.getMessage() + "]");
		}
		LOGGER.info("Trouver [" + count + "] enreg pour sql:" + sql.toString());
		return count;
	}

	@Override
	public List<Plongee> find(MultivaluedMap<String, String> criteria) {
		StringBuffer sql = new StringBuffer("select * from PLONGEE p");
		SqlSearchCriteria param = ResaUtil.createSqlParameters(criteria, false, NomResources.PLONGEE);
		List<Plongee> plongees = new ArrayList<>();
		try {
//			if (param.getNbParam() > 0) {
				sql.append(param.getSql());
//			}
			LOGGER.info("requete SQL:" + sql.toString());
			plongees = jdbcTemplate.query(sql.toString(), param.getArgs(), new PlongeeRowMapper());
			// population des participants + attente
			for (Plongee plongee : plongees) {
				plongee.setParticipants(getAdherentsInscrits(plongee.getId(), null, null, null));
				plongee.setParticipantsEnAttente(getAdherentsWaiting(plongee.getId()));
			}
			return plongees;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le find() plongee [" + e.getMessage() + "]");
		}
	}

	@Override
	public Plongee update(Plongee resource) {
		Connection conex = null;
		try {
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
			//// st.setString(1, resource.getNiveau());
			//// st.setString(2, resource.getTelephone());
			//// st.setString(3, resource.getMail());
			//// if (resource.isEncadrent()) {
			//// st.setString(4, resource.getEncadrement());
			//// } else {
			//// st.setString(4, null);
			//// }
			//// if (resource.isPilote()) {
			//// st.setInt(5, 1);
			//// } else {
			//// st.setInt(5, 0);
			//// }
			//// st.setInt(6, resource.getActifInt());
			//// st.setString(7, resource.getNom());
			//// st.setString(8, resource.getPrenom());
			//// Timestamp ts = new Timestamp(resource.getDateCM().getTime());
			//// st.setTimestamp(9, ts);
			//// st.setInt(10, resource.getAnneeCotisation());
			//// if (resource.isTiv()) {
			//// st.setInt(11, 1);
			//// } else {
			//// st.setInt(11, 0);
			//// }
			//// st.setString(12, resource.getCommentaire());
			//// if (resource.isAptitude()) {
			//// st.setString(13, resource.getAptitude());
			//// } else {
			//// st.setString(13, null);
			//// }
			//// st.setString(14, resource.getNumeroLicense());
			// if (st.executeUpdate() == 0) {
			// throw new TechnicalException(Technical.GENERIC,
			// "L'adhérent n'a pu être modifié");
			// }
			// sb = new StringBuffer();
			//
			// // gestion des roles 1er temps : on supprime les roles
			// sb.append("DELETE FROM REL_ADHERENT_ROLES WHERE ADHERENT_LICENSE = ? ");
			// PreparedStatement st1 = conex.prepareStatement(sb.toString());
			// st1.setString(1, resource.getNumeroLicense());
			// if (st1.executeUpdate() == 0) {
			// LOGGER.info("Impossible de supprimer les roles de l'plongee");
			//// throw new TechnicalException(Technical.GENERIC,"Impossible de supprimer les
			//// roles de l'plongee");
			// }
			// // gestion des roles 2ieme temps : on les re-creer
			// Iterator it = resource.getRoles().iterator();
			// sb = new StringBuffer();
			// while (it.hasNext()) {
			// sb.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`,
			//// `ROLES_idROLES`)");
			// sb.append(" VALUES (?, ?)");
			// st = conex.prepareStatement(sb.toString());
			// st.setString(1, resource.getNumeroLicense());
			// Plongee.Roles role =(Plongee.Roles)it.next();
			// int id = getIdRole(role.name());
			// st.setInt(2, id);
			// if (st.executeUpdate() == 0) {
			// throw new TechnicalException(Technical.GENERIC,
			// "Le role n'a pu être enregistré");
			// }
			// sb = null;
			// sb = new StringBuffer();
			// }
			// //Mise à jour des contacts
			// updateContactsForPlongee(resource);

			return resource;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
//			closeConnexion(conex);
		}
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}


	/**
	 * Donne la liste des adherents inscrits à la plongée
	 * @param plongeeId
	 *            Integer id de la plongée
	 * @param niveauPlongeur
	 *            String si non null, restreint la recherche à ce niveau de plongeur
	 * @param niveauEncadrement
	 *            String si null: pas de recherche sur niveau d'encadrement, 'TOUS'
	 *            retourne tous les encadrants, 'Ex' retourne ce niveau d'ancadrant
	 * @param trie
	 *            String si null ou = 'date' order by date_inscription sinon order
	 *            by nom
	 * @return List<String> liste des numero de license des adherents inscrits à la plongée
	 * @throws FunctionalException
	 */
	public List<String> getAdherentsInscrits(Integer plongeeId, String niveauPlongeur, String niveauEncadrement,
			String trie) throws TechnicalException {
		StringBuffer sql = new StringBuffer("select a.LICENSE from PLONGEE p, INSCRIPTION_PLONGEE i, ADHERENT a ");
		sql.append(" where idPLONGEES = "+plongeeId);
		sql.append(" and idPLONGEES = PLONGEES_idPLONGEES ");
		sql.append(" and ADHERENT_LICENSE = LICENSE");
		sql.append(" and DATE_ANNUL_PLONGEE is null");
		if (null != niveauPlongeur) {
			sql.append(" and a.NIVEAU = "+niveauPlongeur);
		}
		if (null != niveauEncadrement) {
			if (niveauEncadrement.equalsIgnoreCase("TOUS")) {
				sql.append(" and a.ENCADRANT is not null");
			} else {
				sql.append(" and a.ENCADRANT = "+niveauEncadrement);
			}
		}
		if (null == trie || trie.equalsIgnoreCase("date")) {
			sql.append(" order by DATE_INSCRIPTION");
		} else if (trie.equalsIgnoreCase("nom")) {
			sql.append(" order by NOM");
		}
		try {
			List<String> participantsId = new ArrayList<>();
			participantsId = jdbcTemplate.queryForList(sql.toString(), String.class);
			return participantsId;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, "PB Avec le getAdherentsInscrits  [" + e.getMessage() + "]");
		}

	}

	/**
	 * Donne la liste des adherents inscrits en liste d'attente à la plongée 
	 * @param plongeeId Integer id de la plongee 
	 * @return List<String> liste des numero de license des adherents inscrits en liste d'attente
	 * @throws FunctionalException
	 */
	public List<String> getAdherentsWaiting(Integer plongeeId) throws TechnicalException {
		StringBuffer sql = new StringBuffer("select a.LICENSE from PLONGEE p, LISTE_ATTENTE la, ADHERENT a ");
			sql.append(" where idPLONGEES = "+plongeeId);
			sql.append(" and idPLONGEES = PLONGEES_idPLONGEES ");
			sql.append(" and ADHERENT_LICENSE = LICENSE ");
			sql.append(" and DATE_INSCRIPTION is null");
			sql.append(" and SUPPRIMER = 0");
			sql.append(" order by DATE_ATTENTE");
		try {
			List<String> participantsId = new ArrayList<>();
			participantsId = jdbcTemplate.queryForList(sql.toString(), String.class);
			return participantsId;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, "PB Avec le getAdherentsWaiting  [" + e.getMessage() + "]");
		}
	}

	
	@Override
	public List<Plongee> findPlongeeForEncadrant(String nbJourReserv, String nbHourApres) {
		StringBuilder sb = new StringBuilder("SELECT * FROM PLONGEE p");
		sb.append(" WHERE OUVERTURE_FORCEE=1");
		sb.append(" and DATE_PLONGEE > CURRENT_DATE()");
		sb.append(" and now() >= DATE_ADD(DATE_RESERVATION, INTERVAL " + nbJourReserv + " DAY)");
		sb.append(" and now() < DATE_ADD(DATE_PLONGEE, INTERVAL " + nbHourApres + " HOUR)");
		sb.append(" ORDER BY DATE_PLONGEE");
		List<Plongee> plongees = new ArrayList<>();
		try {
			LOGGER.info("requete SQL:" + sb.toString());
			plongees = jdbcTemplate.query(sb.toString(), new PlongeeRowMapper());
			// population des participants + attente
			for (Plongee plongee : plongees) {
				plongee.setParticipants(getAdherentsInscrits(plongee.getId(), null, null, null));
				plongee.setParticipantsEnAttente(getAdherentsWaiting(plongee.getId()));
			}
			return plongees;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, 
					"PB Avec le findPlongeeForEncadrant() [" + e.getMessage() + "]");
		}
	}

	@Override
	public List<Plongee> findPlongeeForAdherent(TypeActionReturnPlongee action) {
		StringBuilder sb = new StringBuilder("SELECT * FROM PLONGEE p");
		sb.append(" WHERE OUVERTURE_FORCEE=1");
		sb.append(" and DATE_PLONGEE > CURRENT_TIMESTAMP()");
        if(action == TypeActionReturnPlongee.RESERVER){
            sb.append(" and now() >= DATE_RESERVATION");
        }
		sb.append(" ORDER BY DATE_PLONGEE");
		List<Plongee> plongees = new ArrayList<>();
		try {
			LOGGER.info("requete SQL:" + sb.toString());
			plongees = jdbcTemplate.query(sb.toString(), new PlongeeRowMapper());
			// population des participants + attente
			for (Plongee plongee : plongees) {
				plongee.setParticipants(getAdherentsInscrits(plongee.getId(), null, null, null));
				plongee.setParticipantsEnAttente(getAdherentsWaiting(plongee.getId()));
			}
			return plongees;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, 
					"PB Avec le findPlongeeForAdherent() [" + e.getMessage() + "]");
		}
	}

}
