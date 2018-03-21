package com.asptt.resabackend.resources.adherent;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.Technical;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Roles;
import com.asptt.resabackend.mapper.AdherentRowMapper;
import com.asptt.resabackend.mapper.SqlSearchCriteria;
import com.asptt.resabackend.resources.NomResources;
import com.asptt.resabackend.util.ResaUtil;
import com.mysql.jdbc.Statement;

@Repository("adherentDao")
public class AdherentDaoImpl implements AdherentDao {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdherentDaoImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Adherent create(Adherent adh) throws TechnicalException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(
					"INSERT INTO ADHERENT (`LICENSE`, `NOM`, `PRENOM`, `NIVEAU`, `TELEPHONE`, `MAIL`, `ENCADRANT`, `PILOTE`, `DATE_DEBUT`, `ACTIF`, `PASSWORD`, `DATE_CM`, `ANNEE_COTI`, `TIV`, `COMMENTAIRE`, `APTITUDE`)");
			sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?)");
			// creation de l'adherent
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, adh.getNumeroLicense());
				ps.setString(2, adh.getNom());
				ps.setString(3, adh.getPrenom());
				ps.setString(4, adh.getNiveau());
				ps.setString(5, adh.getTelephone());
				ps.setString(6, adh.getMail());
				if (adh.isEncadrent()) {
					ps.setString(7, adh.getEncadrement());
				} else {
					ps.setString(7, null);
				}
				if (adh.isPilote()) {
					ps.setInt(8, 1);
				} else {
					ps.setInt(8, 0);
				}
				ps.setInt(9, adh.getActifInt());
				ps.setString(10, adh.getNumeroLicense());
				Timestamp tsCm = new Timestamp(adh.getDateCM().getTime());
				ps.setTimestamp(11, tsCm);
				ps.setInt(12, adh.getAnneeCotisation());
				if (adh.isTiv()) {
					ps.setInt(13, 1);
				} else {
					ps.setInt(13, 0);
				}
				ps.setString(14, adh.getCommentaire());
				if (adh.isAptitude()) {
					ps.setString(15, adh.getAptitude());
				} else {
					ps.setString(15, null);
				}
				return ps;
			}, keyHolder);
			Number idAdh = keyHolder.getKey();
			// gestion des roles
			if (adh.getActifInt() == 1) {
				// On gere les role uniquement pour les actifs
				try {
					Iterator<Roles> it = adh.getRoles().iterator();
					StringBuffer sql1 = new StringBuffer();
					sql1.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`, `ROLES_idROLES`)");
					sql1.append(" VALUES (?, ?)");
					while (it.hasNext()) {
						jdbcTemplate.update(connection -> {
							PreparedStatement ps = connection.prepareStatement(sql1.toString(),
									Statement.RETURN_GENERATED_KEYS);
							ps.setString(1, adh.getNumeroLicense());
							Adherent.Roles role = it.next();
							int id = getIdRole(role.name());
							ps.setInt(2, id);
							return ps;
						}, keyHolder);
					}
				} catch (DataAccessException e) {
					throw new TechnicalException(Technical.GENERIC,
							"Pb creation Table relation :" + adh.getNumeroLicense() + ", erreur : " + e.getMessage());
				}
			}
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC, "Pb creation Adherent :" + adh.getNumeroLicense() + ", "
					+ adh.getNom() + ", " + adh.getPrenom() + "erreur : " + e.getMessage());
		}
		return adh;
	}

	@Override
	public Adherent get(String license) {
		Adherent adh = null;
		try {
			adh = jdbcTemplate.queryForObject("select * from ADHERENT where LICENSE = ?", new AdherentRowMapper(),
					license);
			adh.setRoles(getStrRoles(license));
			adh.setContacts(getContacts(license));
			return adh;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "Aucun Adherent avec la license [" + license + "]");
		}
	}

	@Override
	public Integer findCount(MultivaluedMap<String, String> criteria) {
		StringBuffer sql = new StringBuffer("select count(*) from ADHERENT a ");
		SqlSearchCriteria param = ResaUtil.createSqlParameters(criteria, true, NomResources.ADHERENT);
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
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le findCount() Adherent [" + e.getMessage() + "]");
		}
		LOGGER.info("Trouver [" + count + "] enreg pour sql:" + sql.toString());
		return count;
	}

	@Override
	public List<Adherent> find() {
		String limit = env.getProperty("pagination.limit");
		String sql = "select * from ADHERENT order by NOM desc LIMIT " + limit;
		List<Adherent> adherents = new ArrayList<>();
		try {
			adherents = jdbcTemplate.query(sql, new AdherentRowMapper());
			for (Adherent adh : adherents) {
				adh.setRoles(getStrRoles(adh.getNumeroLicense()));
				adh.setContacts(getContacts(adh.getNumeroLicense()));
			}
			return adherents;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le find() adherents [" + e.getMessage() + "]");
		}
	}

	@Override
	public List<Adherent> find(MultivaluedMap<String, String> criteria) {
		StringBuffer sql = new StringBuffer("select * from ADHERENT a");
		SqlSearchCriteria param = ResaUtil.createSqlParameters(criteria, false, NomResources.ADHERENT);
		List<Adherent> adherents = new ArrayList<>();
		try {
//			if (param.getNbParam() > 0) {
				sql.append(param.getSql());
//			}
			LOGGER.info("requete SQL:" + sql.toString());
			adherents = jdbcTemplate.query(sql.toString(), param.getArgs(), new AdherentRowMapper());
			for (Adherent adh : adherents) {
				adh.setRoles(getStrRoles(adh.getNumeroLicense()));
				adh.setContacts(getContacts(adh.getNumeroLicense()));
			}
			return adherents;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC,
					"PB Avec le find(MultivaluedMap) adherents [" + e.getMessage() + "]");
		}

	}

	@Override
	public Adherent update(Adherent resource) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE ADHERENT");
			sql.append(" SET NIVEAU = ?,");
			sql.append(" TELEPHONE = ?,");
			sql.append(" MAIL = ?,");
			sql.append(" ENCADRANT = ?,");
			sql.append(" PILOTE = ?,");
			sql.append(" ACTIF = ?,");
			sql.append(" NOM = ?,");
			sql.append(" PRENOM = ?,");
			sql.append(" DATE_CM = ?,");
			sql.append(" ANNEE_COTI = ?,");
			sql.append(" TIV = ?,");
			sql.append(" COMMENTAIRE = ?,");
			sql.append(" APTITUDE = ?");
			sql.append(" WHERE license = ?");
			// update de l'adherent
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, resource.getNiveau());
				ps.setString(2, resource.getTelephone());
				ps.setString(3, resource.getMail());
				if (resource.isEncadrent()) {
					ps.setString(4, resource.getEncadrement());
				} else {
					ps.setString(4, null);
				}
				if (resource.isPilote()) {
					ps.setInt(5, 1);
				} else {
					ps.setInt(5, 0);
				}
				ps.setInt(6, resource.getActifInt());
				ps.setString(7, resource.getNom());
				ps.setString(8, resource.getPrenom());
				Timestamp ts = new Timestamp(resource.getDateCM().getTime());
				ps.setTimestamp(9, ts);
				ps.setInt(10, resource.getAnneeCotisation());
				if (resource.isTiv()) {
					ps.setInt(11, 1);
				} else {
					ps.setInt(11, 0);
				}
				ps.setString(12, resource.getCommentaire());
				if (resource.isAptitude()) {
					ps.setString(13, resource.getAptitude());
				} else {
					ps.setString(13, null);
				}
				ps.setString(14, resource.getNumeroLicense());
				return ps;
			}, keyHolder);
			// gestion des roles 1er temps : on supprime les roles
			// // Mise à jour des contacts
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC, "Pb modification Adherent :" + resource.getNumeroLicense()
					+ ", " + resource.getNom() + ", " + resource.getPrenom() + "erreur : " + e.getMessage());
		}

		return resource;

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createRoleForAdherent(String numeroDeLicense, Roles role) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			StringBuffer sql2 = new StringBuffer();
			sql2.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`, `ROLES_idROLES`)");
			sql2.append(" VALUES (?, ?)");
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql2.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, numeroDeLicense);
				int id = getIdRole(role.name());
				ps.setInt(2, id);
				return ps;
			}, keyHolder);
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC, "Pb creation table relation REL_ADHERENT_ROLES : ["
					+ numeroDeLicense + "], [" + role.name() + "] erreur : " + e.getMessage());
		}

	}

	@Override
	public void deleteRoleForAdherent(String numeroDeLicense, Roles role) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			StringBuffer sql1 = new StringBuffer();
			sql1.append("DELETE FROM REL_ADHERENT_ROLES WHERE ADHERENT_LICENSE = ?  and ROLES_idROLES = ?");
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql1.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, numeroDeLicense);
				int id = getIdRole(role.name());
				ps.setInt(2, id);
				return ps;
			}, keyHolder);
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC, "Pb suppression table relation REL_ADHERENT_ROLES : ["
					+ numeroDeLicense + "], [" + role.name() + "] erreur : " + e.getMessage());
		}
	}

	public List<String> getStrRoles(String adherentId) {
		StringBuffer sql = new StringBuffer("SELECT r.LIBELLE FROM REL_ADHERENT_ROLES rel, ROLES r ");
		sql.append(" where rel.ROLES_idROLES = r.idROLES ");
		sql.append(" and rel.ADHERENT_LICENSE = '" + adherentId + "'");
		try {
			List<String> rolesId = new ArrayList<>();
			rolesId = jdbcTemplate.queryForList(sql.toString(), String.class);
			return rolesId;
		} catch (DataAccessException e) {
			throw new FunctionalException(Functional.GENERIC, "PB Avec le getStrRoles  [" + e.getMessage() + "]");
		}
	}

	public int getIdRole(String libelle) {
		StringBuffer sb = new StringBuffer("select idRoles from ROLES where libelle = '" + libelle + "'");
		try {
			Integer rID = jdbcTemplate.queryForObject(sb.toString(), Integer.class);
			return rID.intValue();
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le getIdRole()  [" + e.getMessage() + "]");
		}
	}

	public List<String> getContacts(String adherentId) {
		StringBuffer sql = new StringBuffer(" SELECT cu.idCONTACT ");
		sql.append(" FROM REL_ADHERENT_CONTACT rel , CONTACT_URGENT cu");
		sql.append(" where rel.ADHERENT_LICENSE = '" + adherentId + "'");
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

	@Override
	public void deleteContactForAdherent(String numeroDeLicense, String contactId) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			StringBuffer sql3 = new StringBuffer();
			sql3.append("DEL FROM REL_ADHERENT_CONTACT WHERE ADHERENT_LICENSE = ? and CONTACT_URGENT_IDCONTACT =  ?");
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql3.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, numeroDeLicense);
				ps.setString(2, contactId);
				return ps;
			}, keyHolder);
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC, "Pb Suppression des Contacts table REL_ADHERENT_CONTACT :"
					+ numeroDeLicense + "], [" + contactId + "] erreur : " + e.getMessage());
		}
	}

	@Override
	public void createContactForAdherent(String numeroDeLicense, String contactId) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			StringBuffer sql4 = new StringBuffer();
			sql4.append("INSERT INTO REL_ADHERENT_CONTACT (`ADHERENT_LICENSE`, `CONTACT_URGENT_IDCONTACT`)");
			sql4.append(" VALUES (?, ?)");
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql4.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, numeroDeLicense);
				ps.setString(2, contactId);
				return ps;
			}, keyHolder);
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC, "Pb Creation des Contacts table REL_ADHERENT_CONTACT :"
					+ numeroDeLicense + "], [" + contactId + "] erreur : " + e.getMessage());
		}
	}

}
