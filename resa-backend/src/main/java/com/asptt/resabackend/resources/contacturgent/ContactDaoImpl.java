package com.asptt.resabackend.resources.contacturgent;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.Technical;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.mapper.ContactRowMapper;
import com.asptt.resabackend.mapper.SqlSearchCriteria;
import com.asptt.resabackend.resources.NomResources;
import com.asptt.resabackend.util.ResaUtil;
import com.mysql.jdbc.Statement;

@Repository("contactDao")
public class ContactDaoImpl implements ContactDao {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ContactDaoImpl.class);


	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ContactUrgent create(ContactUrgent contact) throws TechnicalException {
		// TODO
		return null;
	}

	@Override
	public ContactUrgent get(String id) {
		ContactUrgent contact = null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cu.idCONTACT, cu.TITRE, cu.NOM, cu.PRENOM, cu.TELEPHONE, cu.TELEPHTWO");
		sb.append(" FROM CONTACT_URGENT cu");
		sb.append(" where cu.IDCONTACT = ?");
		try {
			contact = jdbcTemplate.queryForObject(sb.toString(), new ContactRowMapper(),
					id);
			return contact;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "Aucun ContactUrgent avec l'id [" + id + "]");
		}
	}

	@Override
	public List<ContactUrgent> find() {
		String sql = "select * from CONTACT_URGENT order by NOM";
		List<ContactUrgent> contacts = new ArrayList<>();
		try {
			contacts = jdbcTemplate.query(sql, new ContactRowMapper());
			return contacts;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC, "PB Avec le find() contacts [" + e.getMessage() + "]");
		}
	}

	@Override
	public List<ContactUrgent> find(MultivaluedMap<String, String> criteria) {
		StringBuffer sql = new StringBuffer("select * from CONTACT_URGENT cu");
		SqlSearchCriteria param = ResaUtil.createSqlParameters(criteria, false, NomResources.CONTACTURGENT);
		List<ContactUrgent> contacts = new ArrayList<>();
		try {
			if (param.getNbParam() > 0) {
				sql.append(param.getSql());
			}
			LOGGER.info("requete SQL:" + sql.toString());
			contacts = jdbcTemplate.query(sql.toString(), param.getArgs(), new ContactRowMapper());
			return contacts;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC,
					"PB Avec le find(MultivaluedMap) contacts [" + e.getMessage() + "]");
		}
	}

	@Override
	public ContactUrgent update(ContactUrgent resource) throws TechnicalException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE CONTACT_URGENT");
			sb.append(" SET TITRE = ?,");
			sb.append(" NOM = ?,");
			sb.append(" PRENOM = ?,");
			sb.append(" TELEPHONE = ?,");
			sb.append(" TELEPHTWO = ?");
			sb.append(" WHERE IDCONTACT = ?");
			// update de l'adherent
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, resource.getTitre());
				ps.setString(2, resource.getNom());
				ps.setString(3, resource.getPrenom());
				ps.setString(4, resource.getTelephone());
				ps.setString(5, resource.getTelephtwo());
				ps.setInt(6, resource.getId());
				return ps;
			}, keyHolder);
		} catch(DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC,
					"Le Contact id : " + resource.getId() + "n'a pu être mis à jour");
			
		}
		return resource;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ContactUrgent> findContactsForAdherent(String adherentId) throws TechnicalException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.idCONTACT, c.TITRE, c.NOM, c.PRENOM, c.TELEPHONE, c.TELEPHTWO");
		sql.append(" FROM REL_ADHERENT_CONTACT rel , CONTACT_URGENT c");
		sql.append(" WHERE rel.CONTACT_URGENT_idCONTACT = c.idCONTACT");
		sql.append(" AND rel.ADHERENT_LICENSE = '" + adherentId + "'");
		List<ContactUrgent> contactUrgents = new ArrayList<>();
		try {
			contactUrgents = jdbcTemplate.query(sql.toString(), new ContactRowMapper());
			return contactUrgents;
		} catch (DataAccessException e) {
			throw new NotFoundException(NotFound.GENERIC,
					"PB Avec le findContactsForAdherent() [" + e.getMessage() + "]");
		}
	}

	@Override
	public List<ContactUrgent> createContactsForAdherent(List<ContactUrgent> contacts, String adherentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO CONTACT_URGENT (`TITRE`, `NOM`, `PRENOM`, `TELEPHONE`, `TELEPHTWO`)");
		sql.append(" VALUES (?, ?, ?, ?, ?)");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		List<ContactUrgent> contactsCreated = new ArrayList<>();
		for (ContactUrgent contact : contacts) {
			// creation du ContactUrgent
			try {
				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, contact.getTitre());
					ps.setString(2, contact.getNom());
					ps.setString(3, contact.getPrenom());
					ps.setString(4, contact.getTelephone());
					ps.setString(5, contact.getTelephtwo());
					return ps;
				}, keyHolder);
				// recup de l'id du contact
				Number idContact = keyHolder.getKey();
				contact.setId(new Integer(idContact.intValue()));
				contactsCreated.add(contact);
				LOGGER.info("Contact creer avec l'id [" + idContact + "]");
				StringBuffer sb2 = new StringBuffer();
				sb2.append("INSERT INTO REL_ADHERENT_CONTACT (`ADHERENT_LICENSE`, `CONTACT_URGENT_IDCONTACT`)");
				sb2.append(" VALUES (?, ?)");
				try {
					// Creation dans la table de relation
					jdbcTemplate.update(connection -> {
						PreparedStatement ps = connection.prepareStatement(sb2.toString());
						ps.setString(1, adherentId);
						ps.setInt(2, idContact.intValue());
						return ps;
					});
				} catch (DataAccessException e) {
					throw new TechnicalException(Technical.GENERIC, "Pb creation Relation Adherent-Contact :"
							+ adherentId + ", " + contact.getNom() + "erreur : " + e.getMessage());
				}
			} catch (DataAccessException e) {
				throw new TechnicalException(Technical.GENERIC, "Pb creation Contact :" + contact.getNom() + ", "
						+ contact.getPrenom() + "erreur : " + e.getMessage());
			}
		}
		return contactsCreated;
	}

	@Override
	public List<ContactUrgent> updateContactsForAdherent(List<ContactUrgent> contacts, String adherentId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// 1ieme temps : on supprime les contact existant dans la table de relation
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM REL_ADHERENT_CONTACT WHERE ADHERENT_LICENSE = ? ");
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, adherentId);
				return ps;
			}, keyHolder);
			// 2ieme temps : on re-cree les contacts
			createContactsForAdherent(contacts, adherentId);
		} catch (DataAccessException e) {
			throw new TechnicalException(Technical.GENERIC,
					"Pb modification Contact pour l'adherent :" + adherentId + ",  erreur : " + e.getMessage());
		}
		return contacts;
	}

	@Override
	public void deleteContactsForAdherent(String adherentId) throws TechnicalException {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM REL_ADHERENT_CONTACT");
		sql.append(" WHERE ADHERENT_LICENSE = ?");
		sql.append(" AND CONTACT_URGENT_IDCONTACT = ?");
		List<ContactUrgent> contacts = findContactsForAdherent(adherentId);
		for (ContactUrgent contact : contacts) {
			// On supprime dans la table de relation
			try {
				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(sql.toString());
					ps.setString(1, adherentId);
					ps.setInt(2, contact.getId());
					return ps;
				});
				// On supprime le contact
				StringBuffer sb2 = new StringBuffer();
				sb2.append("DELETE FROM CONTACT_URGENT");
				sb2.append(" WHERE IDCONTACT = ?");
				try {
					jdbcTemplate.update(connection -> {
						PreparedStatement ps = connection.prepareStatement(sb2.toString());
						ps.setInt(1, contact.getId());
						return ps;
					});
				} catch (DataAccessException e) {
					throw new TechnicalException(Technical.GENERIC, "Pb suppression Contact :" + contact.getId() + ", "
							+ contact.getNom() + "erreur : " + e.getMessage());
				}
			} catch (DataAccessException e) {
				throw new TechnicalException(Technical.GENERIC, "Pb suppression  table Relation Adherent-Contact :"
						+ adherentId + ", " + contact.getId() + "erreur : " + e.getMessage());
			}
		}
	}

}
