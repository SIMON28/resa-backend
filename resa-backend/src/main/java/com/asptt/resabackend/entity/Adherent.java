package com.asptt.resabackend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Adherent implements Serializable {

	private static final long serialVersionUID = -8569233748603210161L;

	private String numeroLicense; // ID
	private String nom;
	private String prenom;
	private TypeEncadrement encadrement;
	private NiveauAutonomie niveau;
	private Aptitude aptitude;
	private boolean pilote;
	private boolean tiv;
	private boolean dp;
	private int actif;
	private Byte[] photo; // au format jpeg ??
	private String telephone;
	private String mail;
	private List<TypeRoles> roles;
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
		actif = 1;
		niveau = NiveauAutonomie.P0;
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

	public boolean isDp() {
		return dp;
	}

//	public boolean isEncadrent() {
//		if (null == getEncadrement()) {
//			return false;
//		} else {
//			return true;
//		}
//	}

	public void setEncadrement(TypeEncadrement encadrement) {
//		if (null == encadrement) {
//			this.encadrement = null;
//		} else {
//			if (encadrement.equals(TypeEncadrement.E3) || encadrement.equals(TypeEncadrement.E4)) {
//				setDp(true);
//			}
//			this.encadrement = encadrement;
//		}
		this.encadrement = encadrement;
	}

	public TypeEncadrement getEncadrement() {
		return encadrement;
	}

	public NiveauAutonomie getNiveau() {
//		if (null == niveau) {
//			return NiveauAutonomie.P0;
//		} else {
//			return niveau;
//		}
		return niveau;
	}

	public void setNiveau(NiveauAutonomie niveau) {
//		if (niveau.equals(NiveauAutonomie.P5)) {
//			setDp(true);
//		}
		this.niveau = niveau;
	}

	public boolean isAptitude() {
		if (null == getAptitude() || getAptitude().name().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public Aptitude getAptitude() {
		return aptitude;
	}

	public void setAptitude(Aptitude aptitude) {
		if (null == aptitude || aptitude.getText().equals("")) {
			this.aptitude = null;
		} else {
			this.aptitude = aptitude;
		}
	}

	public String getAptitudeFS() {
		String result = "";
		if (null == aptitude || aptitude.toString().equals("")) {
//			if (isEncadrent()) {
			if (null != getEncadrement()) {
				result = getEncadrement().name();
			} else {
				switch (getNiveau()) {
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

	public boolean isPilote() {
		return pilote;
	}

	public void setPilote(boolean pilote) {
		this.pilote = pilote;
	}

	public int getActif() {
		return actif;
	}

	public void setActif(int actif) {
		this.actif = actif;
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

	public List<TypeRoles> getRoles() {
		if (null == roles) {
			roles = new ArrayList<>();
			roles.add(TypeRoles.USER);
		}
		return roles;
	}

	public void setRoles(List<String> l_roles) {
		this.roles = new ArrayList<>();
		for (String s_role : l_roles) {
			this.roles.add(TypeRoles.valueOf(s_role));
		}
	}

	public Date getDateCM() {
		if (null != dateCM) {
			return dateCM;
		} else {
			return new Date(1900, 1, 1);
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
