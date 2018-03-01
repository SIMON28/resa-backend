package com.asptt.resabackend.resources.adherent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;

@Repository("contactUrgentDao")
public class ContactUrgentDaoImp implements Dao<ContactUrgent> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ContactUrgentDaoImp.class);

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
	public ContactUrgent create(ContactUrgent adh) throws TechnicalException {
		// TODO
		return null;
	}

	@Override
	public ContactUrgent get(String id) {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT cu.idCONTACT, cu.TITRE, cu.NOM, cu.PRENOM, cu.TELEPHONE, cu.TELEPHTWO");
			sb.append(" FROM CONTACT_URGENT cu");
			sb.append(" where cu.IDCONTACT = ?");

			PreparedStatement st = conex.prepareStatement(sb.toString());
			st.setString(1, id);
			ResultSet rs = st.executeQuery();

			ContactUrgent contact = null;
			while (rs.next()) {
				contact = wrapContact(rs);
			}
			return contact;

		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	@Override
	public List<ContactUrgent> find() {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = conex.prepareStatement("select * from ADHERENT order by NOM");
			ResultSet rs = st.executeQuery();
			List<ContactUrgent> contactUrgents = new ArrayList<>();
			while (rs.next()) {
				ContactUrgent contactUrgent = wrapContact(rs);
				contactUrgents.add(contactUrgent);
			}
			return contactUrgents;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}

	}

	@Override
	public List<ContactUrgent> find(MultivaluedMap<String, String> criteria) {

		PreparedStatement st;
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();

			StringBuffer sb = new StringBuffer("select * from ADHERENT a");
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
				sb.delete(sb.length() - 5, sb.length() - 1);
				sb.append(" order by NOM");
				st = conex.prepareStatement(sb.toString());
				for (Map.Entry<Integer, List<String>> entry : parameters.entrySet()) {
					try {
						st.setString(entry.getKey(), entry.getValue().get(1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				;
			}
			LOGGER.info("Requete SQL Find avec critera=" + st.toString());
			ResultSet rs = st.executeQuery();
			List<ContactUrgent> contactUrgents = new ArrayList<>();
			while (rs.next()) {
				ContactUrgent contactUrgent = wrapContact(rs);
				contactUrgents.add(contactUrgent);
			}
			return contactUrgents;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	@Override
	public ContactUrgent update(ContactUrgent resource) {
		// TODO
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	private ContactUrgent wrapContact(ResultSet rs) throws SQLException, TechnicalException {
		int id = rs.getInt("idCONTACT");
		String titre = rs.getString("TITRE");
		String nom = rs.getString("NOM");
		String prenom = rs.getString("PRENOM");
		String telephone = rs.getString("TELEPHONE");
		String mail = rs.getString("TELEPHTWO");

		ContactUrgent contact = new ContactUrgent();
		contact.setId(id);
		contact.setTitre(titre);
		contact.setNom(nom);
		contact.setPrenom(prenom);
		contact.setTelephone(telephone);
		contact.setTelephtwo(mail);

		return contact;
	}

	public ContactUrgent findContact(ContactUrgent contact, Adherent adh) throws TechnicalException {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT c.idCONTACT, c.TITRE, c.NOM, c.PRENOM, c.TELEPHONE, c.TELEPHTWO");
			sb.append(" FROM REL_ADHERENT_CONTACT rel , CONTACT_URGENT c");
			sb.append(" WHERE rel.CONTACT_URGENT_idCONTACT = c.idCONTACT");
			sb.append(" AND c.NOM = ?");
			sb.append(" AND c.PRENOM = ?");
			sb.append(" AND rel.ADHERENT_LICENSE = ?");

			PreparedStatement st = conex.prepareStatement(sb.toString());
			st.setString(1, contact.getNom());
			st.setString(2, contact.getPrenom());
			st.setString(3, adh.getNumeroLicense());
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				ContactUrgent result = wrapContact(rs);
				return result;
			} else {
				return null;
			}

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	public void createContacts(List<ContactUrgent> contacts, Adherent adh) throws TechnicalException {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = null;
			for (ContactUrgent contact : contacts) {
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT INTO CONTACT_URGENT (`TITRE`, `NOM`, `PRENOM`, `TELEPHONE`, `TELEPHTWO`)");
				sb.append(" VALUES (?, ?, ?, ?, ?)");
				st = conex.prepareStatement(sb.toString());
				st.setString(1, contact.getTitre());
				st.setString(2, contact.getNom());
				st.setString(3, contact.getPrenom());
				st.setString(4, contact.getTelephone());
				st.setString(5, contact.getTelephtwo());
				if (st.executeUpdate() == 0) {
					throw new TechnicalException(Technical.GENERIC,
							"Le contact :" + contact.getNom() + "n'a pu être creé");
				} else {
					// on recupere l'id du contact que l'on vient de créer
					StringBuffer sb1 = new StringBuffer();
					sb1.append("SELECT max(idCONTACT) FROM CONTACT_URGENT c");
					st = conex.prepareStatement(sb1.toString());
					ResultSet rs = st.executeQuery();
					if (rs.next()) {
						int idContact = rs.getInt("max(idCONTACT)");
						// On crée la relation
						StringBuffer sb2 = new StringBuffer();
						sb2.append("INSERT INTO REL_ADHERENT_CONTACT (`ADHERENT_LICENSE`, `CONTACT_URGENT_IDCONTACT`)");
						sb2.append(" VALUES (?, ?)");
						st = conex.prepareStatement(sb2.toString());
						st.setString(1, adh.getNumeroLicense());
						st.setInt(2, idContact);
						if (st.executeUpdate() == 0) {
							throw new TechnicalException(Technical.GENERIC,
									"La relation adherent" + adh.getNom() + " le contact :" + contact.getNom() + ", "
											+ contact.getPrenom() + "n'a pu être creé");
						}
					} else {
						throw new TechnicalException(Technical.GENERIC,
								"Imossible de recuperer le dernier contact créer");
					}

				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	public void updateContacts(List<ContactUrgent> contacts) throws TechnicalException {
		Connection conex = null;
		try {
			for (ContactUrgent contact : contacts) {
				conex = getDataSource().getConnection();
				StringBuffer sb = new StringBuffer();
				sb.append("UPDATE CONTACT_URGENT");
				sb.append(" SET TITRE = ?,");
				sb.append(" NOM = ?,");
				sb.append(" PRENOM = ?,");
				sb.append(" TELEPHONE = ?,");
				sb.append(" TELEPHTWO = ?");
				sb.append(" WHERE IDCONTACT = ?");

				PreparedStatement st = conex.prepareStatement(sb.toString());
				st.setString(1, contact.getTitre());
				st.setString(2, contact.getNom());
				st.setString(3, contact.getPrenom());
				st.setString(4, contact.getTelephone());
				st.setString(5, contact.getTelephtwo());
				st.setInt(6, contact.getId());
				if (st.executeUpdate() == 0) {
					throw new TechnicalException(Technical.GENERIC,
							"Le Contact id : " + contact.getId() + "n'a pu être mis à jour");
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	public void deleteContacts(List<ContactUrgent> contacts, Adherent adh) throws TechnicalException {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = null;
			for (ContactUrgent contact : contacts) {
				StringBuffer sb = new StringBuffer();
				sb.append("DELETE FROM REL_ADHERENT_CONTACT");
				sb.append(" WHERE ADHERENT_LICENSE = ?");
				sb.append(" AND CONTACT_URGENT_IDCONTACT = ?");
				st = conex.prepareStatement(sb.toString());
				st.setString(1, adh.getNumeroLicense());
				st.setInt(2, contact.getId());
				if (st.executeUpdate() == 0) {
					throw new TechnicalException(Technical.GENERIC, "La relation contact :" + contact.getNom()
							+ " adherent " + adh.getNom() + " n'a pu être supprimée");
				} else {
					// On supprime le contact
					StringBuffer sb2 = new StringBuffer();
					sb2.append("DELETE FROM CONTACT_URGENT");
					sb2.append(" WHERE IDCONTACT = ?");
					st = conex.prepareStatement(sb2.toString());
					st.setInt(1, contact.getId());
					if (st.executeUpdate() == 0) {
						throw new TechnicalException(Technical.GENERIC, "La contact :" + contact.getNom() + ", "
								+ contact.getPrenom() + "n'a pu être supprimé");
					}

				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	
	
}


