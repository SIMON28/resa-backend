package com.asptt.resabackend.resources.adherent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.asptt.resabackend.entity.Adherent.Encadrement;
import com.asptt.resabackend.entity.Aptitude;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.entity.NiveauAutonomie;

@Repository("adherentDao")
public class AdherentDaoImpl implements Dao<Adherent> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(AdherentDaoImpl.class);

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
	public Adherent create(Adherent resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Adherent get(String id) {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = conex.prepareStatement(
					"select * from ADHERENT where LICENSE = ? and ACTIF <> 0 and (DATE_FIN is null or CURRENT_TIMESTAMP < DATE_FIN)");
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			Adherent adherent = null;
			if (rs.next()) {
				adherent = wrapAdherent(rs);
			}
			return adherent;
		} catch (SQLException e) {
			// log.error(e.getMessage(), e);
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	
	}

	@Override
	public List<Adherent> find() {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			PreparedStatement st = conex.prepareStatement("select * from ADHERENT order by NOM");
			ResultSet rs = st.executeQuery();
			List<Adherent> adherents = new ArrayList<>();
			while (rs.next()) {
				Adherent adherent = wrapAdherent(rs);
				adherents.add(adherent);
			}
			return adherents;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}

	}

	@Override
	public List<Adherent> find(MultivaluedMap<String, String> criteria) {

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
			List<Adherent> adherents = new ArrayList<>();
			while (rs.next()) {
				Adherent adherent = wrapAdherent(rs);
				adherents.add(adherent);
			}
			return adherents;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
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
                throw new TechnicalException(Technical.GENERIC,
                        "L'adhérent n'a pu être modifié");
            }
            sb = new StringBuffer();

            // gestion des roles 1er temps : on supprime les roles
            sb.append("DELETE FROM REL_ADHERENT_ROLES WHERE ADHERENT_LICENSE = ? ");
            PreparedStatement st1 = conex.prepareStatement(sb.toString());
            st1.setString(1, resource.getNumeroLicense());
            if (st1.executeUpdate() == 0) {
            	LOGGER.info("Impossible de supprimer les roles de l'adherent");
//                throw new TechnicalException(Technical.GENERIC,"Impossible de supprimer les roles de l'adherent");
            }
            // gestion des roles 2ieme temps : on les re-creer
            Iterator it = resource.getRoles().iterator();
            sb = new StringBuffer();
            while (it.hasNext()) {
                sb.append("INSERT INTO REL_ADHERENT_ROLES (`ADHERENT_LICENSE`, `ROLES_idROLES`)");
                sb.append(" VALUES (?, ?)");
                st = conex.prepareStatement(sb.toString());
                st.setString(1, resource.getNumeroLicense());
                Adherent.Roles role =(Adherent.Roles)it.next();
                int id = getIdRole(role.name());
                st.setInt(2, id);
                if (st.executeUpdate() == 0) {
                    throw new TechnicalException(Technical.GENERIC,
                            "Le role n'a pu être enregistré");
                }
                sb = null;
                sb = new StringBuffer();
            }
            //Mise à jour des contacts
            updateContactsForAdherent(resource);

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

	public List<String> getStrRoles(Adherent adherent) throws TechnicalException {
		PreparedStatement st;
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			StringBuffer sb = new StringBuffer("SELECT r.LIBELLE FROM REL_ADHERENT_ROLES rel, ROLES r ");
			sb.append(" where rel.ROLES_idROLES = r.idROLES ");
			sb.append(" and rel.ADHERENT_LICENSE = ?");
			st = conex.prepareStatement(sb.toString());
			st.setString(1, adherent.getNumeroLicense());
			ResultSet rs = st.executeQuery();
			List<String> result = new ArrayList<>();
			while (rs.next()) {
				result.add(rs.getString("LIBELLE"));
			}
			return result;
		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	public List<ContactUrgent> getContacts(Adherent adh) throws TechnicalException {
		Connection conex = null;
		try {
			conex = getDataSource().getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT cu.idCONTACT, cu.TITRE, cu.NOM, cu.PRENOM, cu.TELEPHONE, cu.TELEPHTWO");
			sb.append(" FROM REL_ADHERENT_CONTACT rel , CONTACT_URGENT cu");
			sb.append(" where rel.ADHERENT_LICENSE = ?");
			sb.append(" and  rel.CONTACT_URGENT_IDCONTACT = cu.IDCONTACT");
			sb.append(" order by cu.IDCONTACT");

			PreparedStatement st = conex.prepareStatement(sb.toString());
			st.setString(1, adh.getNumeroLicense());
			ResultSet rs = st.executeQuery();

			List<ContactUrgent> contacts = new ArrayList<>();
			while (rs.next()) {
				ContactUrgent contact = wrapContact(rs);
				contacts.add(contact);
			}
			return contacts;

		} catch (SQLException e) {
			throw new TechnicalException(Technical.GENERIC, e.getMessage());
		} finally {
			closeConnexion(conex);
		}
	}

	private Adherent wrapAdherent(ResultSet rs) throws SQLException, TechnicalException {
		String licence = rs.getString("LICENSE");
		String nom = rs.getString("NOM");
		String prenom = rs.getString("PRENOM");
		NiveauAutonomie niveau = NiveauAutonomie.valueOf(rs.getString("NIVEAU"));
		String telephone = rs.getString("TELEPHONE");
		String mail = rs.getString("MAIL");
		int pilote = rs.getInt("PILOTE");
		Adherent adherent = new Adherent();
		adherent.setNumeroLicense(licence);
		adherent.setNom(nom);
		adherent.setPrenom(prenom);
		adherent.setEnumNiveau(niveau);
		adherent.setTelephone(telephone);
		adherent.setMail(mail);
		Encadrement encadrant = null;
		if (null != rs.getString("ENCADRANT")) {
			encadrant = Encadrement.valueOf(rs.getString("ENCADRANT"));
		}
		adherent.setEnumEncadrement(encadrant);
		adherent.setRoles(getStrRoles(adherent));
		adherent.setActifInt(rs.getInt("ACTIF"));
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
		adherent.setContacts(getContacts(adherent));

		// Pour les nouveaux champs date du certificat medical et annee de cotisation
		Date dateCM = rs.getDate("DATE_CM");
		adherent.setDateCM(dateCM);
		adherent.setAnneeCotisation(rs.getInt("ANNEE_COTI"));
		Aptitude aptitude = null;
		if (null != rs.getString("APTITUDE")) {
			aptitude = Aptitude.valueOf(rs.getString("APTITUDE"));
		}
		adherent.setEnumAptitude(aptitude);
		return adherent;
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
    public int getIdRole(String libelle) throws TechnicalException {
        PreparedStatement st;
        Connection conex = null;
        try {
            conex = getDataSource().getConnection();
            StringBuffer sb = new StringBuffer("select idRoles from ROLES where libelle=? ");
            st = conex.prepareStatement(sb.toString());
            st.setString(1, libelle);
            ResultSet rs = st.executeQuery();
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("IdRoles");
            }
            return id;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
        } finally {
            closeConnexion(conex);
        }
    }

    public void updateContactsForAdherent(Adherent adh) throws TechnicalException {

        List<ContactUrgent> anciensContacts = getContacts(adh);
        List<ContactUrgent> contactsAMettreAjour = adh.getContacts();

        List<ContactUrgent> newContacts = new ArrayList<>();
        List<ContactUrgent> delContacts = new ArrayList<>();
        List<ContactUrgent> majContacts = new ArrayList<>();

        for (ContactUrgent contact : contactsAMettreAjour) {
            ContactUrgent result = findContact(contact, adh);
            if (null == result) {
                //Ce contact n'existe pas : on le met dans la liste des contacts à créer
                newContacts.add(contact);
            } else {
                //Contact à mettre à jour
                if (anciensContacts.contains(contact)) {
                    contact.setId(result.getId());
                    majContacts.add(contact);
                }
            }
        }
        // Recherche des contacts à supprimer
        for (ContactUrgent contact : anciensContacts) {
            if (!contactsAMettreAjour.contains(contact)) {
                delContacts.add(contact);
            }
        }

        createContacts(newContacts, adh);
        updateContacts(majContacts);
        deleteContacts(delContacts, adh);

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
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
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
                    //on recupere l'id du contact que l'on vient de créer 
                    StringBuffer sb1 = new StringBuffer();
                    sb1.append("SELECT max(idCONTACT) FROM CONTACT_URGENT c");
                    st = conex.prepareStatement(sb1.toString());
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        int idContact = rs.getInt("max(idCONTACT)");
                        //On crée la relation
                        StringBuffer sb2 = new StringBuffer();
                        sb2.append("INSERT INTO REL_ADHERENT_CONTACT (`ADHERENT_LICENSE`, `CONTACT_URGENT_IDCONTACT`)");
                        sb2.append(" VALUES (?, ?)");
                        st = conex.prepareStatement(sb2.toString());
                        st.setString(1, adh.getNumeroLicense());
                        st.setInt(2, idContact);
                        if (st.executeUpdate() == 0) {
                            throw new TechnicalException(Technical.GENERIC,
                                    "La relation adherent" + adh.getNom() + " le contact :" + contact.getNom() + ", " + contact.getPrenom() + "n'a pu être creé");
                        }
                    } else {
                        throw new TechnicalException(Technical.GENERIC,
                                "Imossible de recuperer le dernier contact créer");
                    }


                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
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
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
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
                    throw new TechnicalException(Technical.GENERIC,
                            "La relation contact :" + contact.getNom() + " adherent " + adh.getNom() + " n'a pu être supprimée");
                } else {
                    //On supprime le contact
                    StringBuffer sb2 = new StringBuffer();
                    sb2.append("DELETE FROM CONTACT_URGENT");
                    sb2.append(" WHERE IDCONTACT = ?");
                    st = conex.prepareStatement(sb2.toString());
                    st.setInt(1, contact.getId());
                    if (st.executeUpdate() == 0) {
                        throw new TechnicalException(Technical.GENERIC,
                                "La contact :" + contact.getNom() + ", " + contact.getPrenom() + "n'a pu être supprimé");
                    }

                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TechnicalException(Technical.GENERIC,e.getMessage());
        } finally {
            closeConnexion(conex);
        }
    }

}
