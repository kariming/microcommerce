package com.ecommerce.microcommerce.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
// @JsonIgnoreProperties(value = {"prixAchat", "id"})
//@JsonFilter("monFiltreDynamique")
@Entity
public class Product {
	//information que nous ne souhaitons pas exposer
	//@JsonIgnore
	@Id
	@GeneratedValue
	private int id;
	
	@Length( min= 3 ,max=40 , message = "Nom trop long ou trop court. Et oui messages sont plus stylés que ceux de Spring")
	private String nom;
	@Min(value=1 , message ="Le produit avec prix de vente = 0 Svp Mettre Un prix de vente valide > 0 !!")
	private int  prix;
	//@JsonIgnore
	private int prixAchat;
	
	// constructeur par defaut
	public Product() {
		
	}
	
	// constructeur our nos tests
	public Product(int id, String nom, int prix, int prixAchat) {
		
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.prixAchat =prixAchat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}
	
	public int getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(int prixAcat) {
		this.prixAchat = prixAcat;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", nom=" + nom + ", prix=" + prix + ", prixAchat=" + prixAchat + "]";
	}

	
	
}
