/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;

import model.entities.Produtos;
import model.sessionbeans.GerenciarCarrinho;
import model.sessionbeans.ListaFacade;
import model.sessionbeans.ProdutosFacade;
import model.sessionbeans.UsuariosFacade;

@Named(value = "listaController")
@SessionScoped
public class ListaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<model.entities.Produtos> produtos;
	Produtos produto = new Produtos();
	private Produtos selProduto;
	
	
	@EJB
	private ProdutosFacade produtosFacade;
	@EJB
	private ListaFacade listafacede;
	
	
	
	public List<model.entities.Produtos> todosProdutos() {
		return listafacede.getProdutos();
	}
	
	
	public List<model.entities.Produtos> getProdutos() {
		produtos = produtosFacade.findAll();
		return produtos;
	}

	public Produtos getProduto(Integer id) {
		produto = produtosFacade.find(id);
		return produto;
	}

	public Produtos getSelProduto() {
		return selProduto;
	}

	public void setSelProduto(Produtos selProduto) {
		this.selProduto = selProduto;
	}

}