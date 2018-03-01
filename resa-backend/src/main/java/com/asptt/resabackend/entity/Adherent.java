package com.asptt.resabackend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Adherent implements Serializable {

	private static final long serialVersionUID = -8569233748603210161L;

	public static enum Encadrement {

		E2, E3, E4
	}

	public static enum Roles {
		ADMIN, USER, SECRETARIAT, DP, ENCADRANT
	}

	private String numeroLicense; // ID
	private String nom;
	private String prenom;
	private Encadrement encadrement;
	private NiveauAutonomie niveau;
	private Aptitude aptitude;
	private boolean pilote;
	private boolean tiv;
	private boolean dp;
	private boolean actif;
	private int intActif = 1;
	private Byte[] photo; // au format jpeg ??
	private String telephone;
	private String mail;
	private List<Roles> roles;
	// private String role;
	private String password;
	private String commentaire;
	private Date dateCM;
	private Integer anneeCotisation;
	private List<String> contacts = null;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Adherent() {
		// valeurs par défaut
		pilote = false;
		dp = false;
		actif = false;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNumeroLicense() {
		return numeroLicense;
	}

	public void setNumeroLicense(String numeroLicense) {
		this.numeroLicense = numeroLicense;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Encadrement getEnumEncadrement() {
		return encadrement;
	}

	public boolean isEncadrent() {
		if (null == getEncadrement()) {
			return false;
		} else {
			return true;
		}
	}

	public String getEncadrement() {
		if (null == encadrement) {
			return null;
		} else {
			return encadrement.toString();
		}
	}

	public String getPrerogative() {
		if (isEncadrent()) {
			return getEncadrement();
		} else {
			// petite verrue du 19/05/2011 demandée exclusivement pour notre ami Bernard
			// SANGUEDOLCE
			if (this.getNumeroLicense().equalsIgnoreCase("095821")) {
				return "E1";
			} else {
				return getNiveau();
			}
		}
	}

	public void setEnumEncadrement(Encadrement encadrement) {
		if (null == encadrement) {
			this.encadrement = null;
		} else {
			if (encadrement.equals(Encadrement.E3) || encadrement.equals(Encadrement.E4)) {
				setDp(true);
			}
			this.encadrement = encadrement;
		}
	}

	public void setEncadrement(String encadrement) {
		if (null != encadrement) {
			if (encadrement.equals("E3") || encadrement.equals("E4")) {
				setDp(true);
			}
			setEnumEncadrement(Encadrement.valueOf(encadrement));
		}
	}

	public NiveauAutonomie getEnumNiveau() {
		if (null == niveau) {
			return NiveauAutonomie.P0;
		} else {
			return niveau;
		}
	}

	public String getNiveau() {
		if (null == niveau) {
			return NiveauAutonomie.P0.toString();
		} else {
			return niveau.toString();
		}
	}

	public void setEnumNiveau(NiveauAutonomie niveau) {
		if (niveau.equals(NiveauAutonomie.P5)) {
			setDp(true);
		}
		this.niveau = niveau;
	}

	public void setNiveau(String niveau) {
		setEnumNiveau(NiveauAutonomie.valueOf(niveau));
	}

	public Aptitude getEnumAptitude() {
		return aptitude;
	}

	public boolean isAptitude() {
		if (null == getAptitude() || getAptitude().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public String getAptitude() {
		if (null == aptitude) {
			return null;
		} else {
			return aptitude.getText();
		}
	}

	public String getAptitudeFS() {
		String result = "";
		if (null == aptitude || aptitude.toString().equals("")) {
			if (isEncadrent()) {
				result = getEncadrement();
			} else {
				switch (getEnumNiveau()) {
				case BATM:
					result = "BATM";
					break;
				case P0:
					result = "P0";
					break;
				case P1:
					result = "PE20";
					break;
				case P2:
					result = "PA20 / PE40";
					break;
				case P3:
					result = "PA60";
					break;
				case P4:
					result = "GP / PA60";
					break;
				case P5:
					result = "GP";
					break;
				}
			}
			return result;
		} else {
			return aptitude.toString();
		}
	}

	public void setAptitudeFS(String patitudeFS) {

	}

	public void setEnumAptitude(Aptitude aptitude) {
		if (null == aptitude || aptitude.getText().equals("")) {
			this.aptitude = null;
		} else {
			this.aptitude = aptitude;
		}
	}

	public void setAptitude(String aptitude) {
		setEnumAptitude(Aptitude.valueOf(aptitude));
	}

	public boolean isPilote() {
		return pilote;
	}

	public void setPilote(boolean pilote) {
		this.pilote = pilote;
	}

	public boolean isActif() {
		return actif;
	}

	public int getActifInt() {
		return intActif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
		if (actif) {
			this.intActif = 1;
		} else {
			this.intActif = 0;
		}
	}

	public void setActifInt(int actif) {
		this.intActif = actif;
		if (actif == 1) {
			this.actif = true;
		} else {
			this.actif = false;
		}
	}

	public boolean isDp() {
		if (getEnumNiveau().equals(NiveauAutonomie.P5)) {
			return true;
		} else {
			if (getEncadrement() != null) {
				if (getEnumEncadrement().equals(Encadrement.E3) || getEnumEncadrement().equals(Encadrement.E4)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Une veste rouge peut-être : Un encadrant, un DP ou u Pilote Mais pas un
	 * externe => le test : et actif =1
	 *
	 * @return
	 */
	public boolean isVesteRouge() {
		return ((getEncadrement() != null || isDp() || isPilote()) && getActifInt() == 1);
	}

	public void setDp(boolean dp) {
		this.dp = dp;
	}

	public Byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(Byte[] photo) {
		this.photo = photo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	// public String getRole() {
	// if (null == roles) {
	// return Roles.USER.toString();
	// }
	// return roles.valueOf(String<>, name);
	// }
	//
	// public void setRole(String role) {
	// if (null == this.roles) {
	// this.roles = Roles.valueOf(role);
	// }
	// }

	public List<Roles> getRoles() {
		if (null == roles) {
			roles = new ArrayList<>();
			roles.add(Roles.USER);
		}
		return roles;
	}

	public void setRoles(List<String> l_roles) {
		this.roles = new ArrayList<>();
		for (String s_role : l_roles) {
			this.roles.add(Roles.valueOf(s_role));
		}
	}

	public String getNomComplet() {
		// Dès que le plongeur est encadrant, on affiche son niveau d'encadrement
		String niveauAffiche;
		if (getEncadrement() != null) {
			niveauAffiche = getEncadrement();
		} else {
			niveauAffiche = getNiveau();
		}

		// Pour les externes, le niveau est suffixé par (Ext.)
		if (getActifInt() == 2) {
			niveauAffiche = niveauAffiche + " (Ext.)";
		}

		return nom + " " + prenom + " " + niveauAffiche + " (" + telephone + ")";
	}

	public String getNomCompletFS() {

		// Pour les externes, ne nom est suffixé par (Ext.)
		String ext = "";
		if (getActifInt() == 2) {
			ext = ext + " (Ext.)";
		}

		return nom + " " + prenom + " " + ext;
	}

	public Date getDateCM() {
		if (null != dateCM) {
			return dateCM;
		} else {
			return new Date(1900,1,1);
		}
	}

	public void setDateCM(Date dateCM) {
		this.dateCM = dateCM;
	}

	public Integer getAnneeCotisation() {
		return anneeCotisation;
	}

	public void setAnneeCotisation(Integer anneeCotisation) {
		this.anneeCotisation = anneeCotisation;
	}

	public List<String> getContacts() {
		if (null == contacts) {
			contacts = new ArrayList<>();
		}
		return contacts;
	}

	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}

	public boolean isTiv() {
		return tiv;
	}

	public void setTiv(boolean tiv) {
		this.tiv = tiv;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
}
