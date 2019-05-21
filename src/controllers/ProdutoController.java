/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import model.sessionbeans.GerenciarCarrinho;
import model.sessionbeans.ProdutosFacade;
import model.sessionbeans.UsuariosFacade;

/**
 *
 * @author alberto sales
 */
@Named(value = "produtoController")
@SessionScoped
public class ProdutoController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String usuario = "";
    String senha = "";
    
    @EJB
    private ProdutosFacade produtosFacade;
    @EJB
    private GerenciarCarrinho carrinho;
    @EJB
    private UsuariosFacade usuariosFacade;

    public List<model.entities.Produtos> todosProdutos() {
        return produtosFacade.findAll();
    }
    
    public ProdutoController() {
    }
    
    public void adicionarItemCarro(int id){
        carrinho.adicionarItem(id);
    }
    
    public void removeItemDoCarro(int id){
        carrinho.removeItem(id);
    }
    
    public List<model.entities.Item> todosItemsDoCarro() {
        return carrinho.getItems();
    }
    
    public int carroTam() {
        return carrinho.getCarrinhoTam();
    }
    
    public void checkout() {
        carrinho.removeTodos();
    }
    
    public String autenticar(String acao) {
        if(usuariosFacade.estaLogado() || usuariosFacade.estaAutenticado(usuario, senha)){
            return acao;
        }
        return "login";
    }

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
