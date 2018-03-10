package com.asptt.resabackend.resources.adherent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.Technical;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Roles;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.mapper.AdherentRowMapper;
import com.asptt.resabackend.mapper.SqlSearchCriteria;
import com.asptt.resabackend.resources.NomResources;
import com.asptt.resabackend.util.ResaUtil;

@Repository("adherentDao")
public class AdherentDaoImpl implements AdherentDao {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdherentDaoImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;;

	@Autowired
	private Dao<ContactUrgent> contactUrgentDao;

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
	public Adherent create(Adherent adh) throws TechnicalException {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					"INSERT INTO ADHERENT (`LICENSE`, `NOM`, `PRENOM`, `NIVEAU`, `TELEPHONE`, `MAIL`, `ENCADRANT`, `PILOTE`, `DATE_DEBUT`, `ACTIF`, `PASSWORD`, `DATE_CM`, `ANNEE_COTI`, `TIV`, `COMMENTAIRE`, `APTITUDE`)");
			sb.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement st = conex.prepareStatement(sb.toString());
			st.setString(1, adh.getNumeroLicense());
			st.setString(2, adh.getNom());
			st.setString(3, adh.getPrenom());
			st.setString(4, adh.getNiveau());
			st.setString(5, adh.getTelephone());
			st.setString(6, adh.getMail());
			if (adh.isEncadrent()) {
				st.setString(7, adh.getEncadrement());
			} else {
				st.setString(7, null);
			}
			if (adh.isPilote()) {
				st.setInt(8, 1);
			} else {
				st.setInt(8, 0);
			}
			st.setInt(9, adh.getActifInt());
			st.setString(10, adh.getNumeroLicense());
			Timestamp tsCm = new Timestamp(adh.getDateCM().getTime());
			st.setTimestamp(11, tsCm);
			st.setInt(12, adh.getAnneeCotisation());
			if (adh.isTiv()) {
				st.setInt(13, 1);
			} else {
				st.setInt(13, 0);
			}
			st.setString(14, adh.getCommentaire());
			if (adh.isAptitude()) {
				st.setString(15, adh.getAptitude());
			} else {
				st.setString(15, null);
			}
			if (st.executeUpdate() == 0) {
				throw new TechnicalException(Technical.GENERIC,
						"L'adhérent n'a pu être créé pour le numero de license [" + adh.getNumeroLicense() + "]");
			}

			// gestion des roles
			sb = new StringBuffer();
			if (adh.getActifInt() == 1) {
				// On gere les role uniquement pour les actifs
				Iterator<Roles> it = adh.getRoles().iterator();
				while (it.hasNext()) {
					sb.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`, `ROLES_idROLES`)");
					sb.append(" VALUES (?, ?)");
					st = conex.prepareStatement(sb.toString());
					st.setString(1, adh.getNumeroLicense());
					Adherent.Roles role = it.next();
					int id = getIdRole(role.name());
					st.setInt(2, id);
					if (st.executeUpdate() == 0) {
						throw new TechnicalException(Technical.GENERIC,
								"Le role [" + id + "] n'a pu être enregistré pour le numero de license ["
										+ adh.getNumeroLicense() + "]");
					}
					sb = new StringBuffer();
				}
			}
			// gestion des Contacts creer les contact ulterieurement avec la sous resource contactUrgent
			
		} catch (SQLException e) {
			LOGGER.error("Impossible de supprimer les roles de l'adherent dans la table de relation");
		} finally {
			closeConnexion(conex);
		}
		return adh;
	}

	@Override
	public Adherent get(String license) {
		Adherent adh = null;
		try {
			adh = jdbcTemplate.queryForObject("select * from ADHERENT where LICENSE = ?", new AdherentRowMapper(), license);
			adh.setRoles(getStrRoles(license));
			adh.setContacts(getContacts(license));
			return adh;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "Aucun Adherent avec la license [" + license + "]");
		}
	}

	@Override
	public List<Adherent> find() {
		String sql = "select * from ADHERENT order by NOM";
		List<Adherent> adherents = new ArrayList<>();
		try {
			adherents = jdbcTemplate.query(sql,  new AdherentRowMapper());
			for(Adherent adh : adherents) {
				adh.setRoles(getStrRoles(adh.getNumeroLicense()));
				adh.setContacts(getContacts(adh.getNumeroLicense()));
			}
			return adherents;
		} catch(DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le find() adherents [" + e.getMessage() + "]");
		}
	}

	@Override
	public List<Adherent> find(MultivaluedMap<String, String> criteria) {
		StringBuffer sql = new StringBuffer("select * from ADHERENT a");
		SqlSearchCriteria param = ResaUtil.createSqlParameters(criteria, false, NomResources.ADHERENT);
		List<Adherent> adherents = new ArrayList<>();
		try {
			if (param.getNbParam() > 0) {
				sql.append(param.getSql());
			}
			LOGGER.info("requete SQL:" + sql.toString());
			adherents = jdbcTemplate.query(sql.toString(), param.getArgs(), new AdherentRowMapper());
			for(Adherent adh : adherents) {
				adh.setRoles(getStrRoles(adh.getNumeroLicense()));
				adh.setContacts(getContacts(adh.getNumeroLicense()));
			}
			return adherents;
		} catch(DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le find(MultivaluedMap) adherents [" + e.getMessage() + "]");
		}
						
	}

	@Override
	public Adherent update(Adherent resource) {
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
			st.setString(1, resource.getNiveau());
			st.setString(2, resource.getTelephone());
			st.setString(3, resource.getMail());
			if (resource.isEncadrent()) {
				st.setString(4, resource.getEncadrement());
			} else {
				st.setString(4, null);
			}
			if (resource.isPilote()) {
				st.setInt(5, 1);
			} else {
				st.setInt(5, 0);
			}
			st.setInt(6, resource.getActifInt());
			st.setString(7, resource.getNom());
			st.setString(8, resource.getPrenom());
			Timestamp ts = new Timestamp(resource.getDateCM().getTime());
			st.setTimestamp(9, ts);
			st.setInt(10, resource.getAnneeCotisation());
			if (resource.isTiv()) {
				st.setInt(11, 1);
			} else {
				st.setInt(11, 0);
			}
			st.setString(12, resource.getCommentaire());
			if (resource.isAptitude()) {
				st.setString(13, resource.getAptitude());
			} else {
				st.setString(13, null);
			}
			st.setString(14, resource.getNumeroLicense());
			if (st.executeUpdate() == 0) {
				throw new TechnicalException(Technical.GENERIC, "L'adhérent n'a pu être modifié");
			}

			// gestion des roles 1er temps : on supprime les roles
			sb = new StringBuffer();
			sb.append("DELETE FROM REL_ADHERENT_ROLES WHERE ADHERENT_LICENSE = ? ");
			PreparedStatement st1 = conex.prepareStatement(sb.toString());
			st1.setString(1, resource.getNumeroLicense());
			if (st1.executeUpdate() == 0) {
				LOGGER.info("Impossible de supprimer les roles de l'adherent dans la table de relation");
			} else {
				LOGGER.info("suppression des roles de l'adherent [" + resource.getNumeroLicense()
						+ "] dans la table de relation");
			}

			// gestion des roles 2ieme temps : on les re-creer
			Iterator<Roles> itRoles = resource.getRoles().iterator();
			sb = new StringBuffer();
			while (itRoles.hasNext()) {
				sb.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`, `ROLES_idROLES`)");
				sb.append(" VALUES (?, ?)");
				PreparedStatement strole = conex.prepareStatement(sb.toString());
				strole.setString(1, resource.getNumeroLicense());
				Adherent.Roles role = itRoles.next();
				int id = getIdRole(role.name());
				strole.setInt(2, id);
				if (strole.executeUpdate() == 0) {
					throw new TechnicalException(Technical.GENERIC, "Le role n'a pu être enregistré");
				}
				sb = null;
				sb = new StringBuffer();
			}

			// Mise à jour des contacts
			// 1er temps :vérification de l'existance des contacts à mettre à jour
			for (String contactId : resource.getContacts()) {
				// ContactUrgent contact = getContactUrgentById(contactId);
				ContactUrgent contact = contactUrgentDao.get(contactId);

				if (null == contact) {
					// Problème ce nouveau contact n'existe pas
					throw new FunctionalException(Functional.AUTHENTICATION,
							"Le contactUrgent id [" + contactId + "] n'a pas pu etre ajouter : existe pas ");
				}
			}
			// 2ieme temps : on supprime les contact existant dans la table de relation
			sb = new StringBuffer();
			sb.append("DELETE FROM REL_ADHERENT_CONTACT WHERE ADHERENT_LICENSE = ? ");
			PreparedStatement stContact = conex.prepareStatement(sb.toString());
			stContact.setString(1, resource.getNumeroLicense());
			if (stContact.executeUpdate() == 0) {
				LOGGER.info("Impossible de supprimer les contact de l'adherent dans la table de relation");
			} else {
				LOGGER.info("suppression des contacts de l'adherent [" + resource.getNumeroLicense()
						+ "] dans la table de relation");
			}

			// 3ieme temps : on re-cree les contacts dans la table de relation
			sb = new StringBuffer();
			for (String contactId : resource.getContacts()) {
				sb.append("INSERT INTO REL_ADHERENT_CONTACT (`ADHERENT_LICENSE`, `CONTACT_URGENT_IDCONTACT`)");
				sb.append(" VALUES (?, ?)");
				PreparedStatement st2 = conex.prepareStatement(sb.toString());
				st2.setString(1, resource.getNumeroLicense());
				st2.setString(2, contactId);
				if (st2.executeUpdate() == 0) {
					throw new TechnicalException(Technical.GENERIC, "Le contact n'a pu être enregistré");
				}
				sb = null;
				sb = new StringBuffer();
			}

			// updateContactsForAdherent(resource.getNumeroLicense());

			return resource;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	public List<String> getStrRoles(String adherentId) {
		StringBuffer sql = new StringBuffer("SELECT r.LIBELLE FROM REL_ADHERENT_ROLES rel, ROLES r ");
		sql.append(" where rel.ROLES_idROLES = r.idROLES ");
		sql.append(" and rel.ADHERENT_LICENSE = "+adherentId);
		try {
			List<String> rolesId = new ArrayList<>();
			rolesId = jdbcTemplate.queryForList(sql.toString(), String.class);
			return rolesId;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, "PB Avec le getStrRoles  [" + e.getMessage() + "]");
		}
	}

	public int getIdRole(String libelle) throws TechnicalException {
		StringBuffer sb = new StringBuffer("select idRoles from ROLES where libelle = '"+libelle+"'");
		try {
			Integer rID = jdbcTemplate.queryForObject(sb.toString(), Integer.class);
			return rID.intValue();
		} catch(DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le getIdRole()  [" + e.getMessage() + "]");
		}
	}

	public List<String> getContacts(String adherentId) throws TechnicalException {
		StringBuffer sql = new StringBuffer(" SELECT cu.idCONTACT ");
		sql.append(" FROM REL_ADHERENT_CONTACT rel , CONTACT_URGENT cu");
		sql.append(" where rel.ADHERENT_LICENSE = "+adherentId);
		sql.append(" and  rel.CONTACT_URGENT_IDCONTACT = cu.IDCONTACT");
		sql.append(" order by cu.IDCONTACT");
		try {
			List<String> contactsId = new ArrayList<>();
			contactsId = jdbcTemplate.queryForList(sql.toString(), String.class);
			return contactsId;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, "PB Avec le getContacts  [" + e.getMessage() + "]");
		}
	}

}
