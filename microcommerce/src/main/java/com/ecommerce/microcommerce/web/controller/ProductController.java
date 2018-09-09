package com.ecommerce.microcommerce.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exception.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exception.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api( description="API pour es opérations CRUD sur les produits.")
@RestController
public class ProductController {
	
	@Autowired
	ProductDao productdao;
	/*
	//Récupérer la liste des produits
	   @RequestMapping(value = "/Produits2", method = RequestMethod.GET)

	   public MappingJacksonValue listeProduits2() {

	       List<Product> produits = productdao.findAll();

	       SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

	       FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

	       MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

	       produitsFiltres.setFilters(listDeNosFiltres);

	       return produitsFiltres;
	   }
	   */
	
	@RequestMapping(value="/Produits", method=RequestMethod.GET)
	public List<Product> listeProduits() {
		return productdao.findAll();
	}
	
	@ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
	@GetMapping(value="Produits/{id}")
	public Product afficherUnProduit(@PathVariable int id) {
		
		 Product produit = productdao.findById(id);

		 
		 if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

	       return produit;	
		
	}
	
	@GetMapping(value = "test/produits/{prixLimit}")
	public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
	    return productdao.findByPrixGreaterThan(400);
	}
	
	@GetMapping(value = "test2/produits/{prixLimit}")
	public List<Product> testeDeRequetes1(@PathVariable int prixLimit) {
	    return productdao.findByPrixAchatGreaterThan(prixLimit);
	}
	
	
	
	@PostMapping(value="Produits/add")
	public ResponseEntity<Void> ajouterUnProduit(@Valid @RequestBody Product produit) {
		
		if(produit.getPrix()==0) throw new ProduitGratuitException("Le produit avec prix de vente = 0 Svp Mettre Un prix de vente valide > 0 !!");
		
		Product productAdded= productdao.save(produit);
		if(productAdded == null) 
			return ResponseEntity.noContent().build();
			
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(productAdded.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping (value = "/Produits/{id}")
	   public void supprimerProduit(@PathVariable int id) {

		productdao.deleteById(id);

	}
	
	@PutMapping (value = "/Produits")
	  public void updateProduit(@RequestBody Product product) {

	      productdao.save(product);
	  }
	
	@GetMapping(value = "produits/limite/{prixLimit}")
	public List<Product> testeDeRequette(@PathVariable int prixLimit) {
	    return productdao.chercherUnProduitCher(prixLimit);
	}
	
	@RequestMapping(value="/AdminProduits", method=RequestMethod.GET)
	public List<String> calculMargeProduits() {
		List<String> calculesProd = new ArrayList<String>() ;
		int marge;
		for(Product p : productdao.findAll()) {
			
			marge=p.getPrix() - p.getPrixAchat();
			
			calculesProd.add(p.toString() +":"+ String.valueOf(marge));
		}
		 
		 return calculesProd;
	}
	
	@RequestMapping(value="/AdminProduits/tri", method=RequestMethod.GET)
	public List<Product> trierProduitsParOrdreAlphabetique() {
		
		 
		 return productdao.findAllByOrderByNomAsc() ;
	}
}
