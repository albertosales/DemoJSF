package controllers.produto;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import model.entities.Produtos;
import model.sessionbeans.ProdutosFacade;

import java.io.Serializable;

@Named(value = "crudProdutoController")
@SessionScoped
public class CrudProdutoController implements Serializable {

	private static final long serialVersionUID = 1L;

	String nome = "";
	String descricao = "";
	String preco;
	String referencia;
	Integer id;

	@EJB
	private ProdutosFacade produtosFacade;

	public void salvar() {
		try {
			Produtos produto = new Produtos();

			produto.setDescricao(descricao);
			produto.setNome(nome);
			produto.setId(id);
			produto.setReferencia(referencia);
			produto.setPreco(Double.parseDouble(preco));

			if (id == null) {
				this.produtosFacade.create(produto);
			} else {
				this.produtosFacade.edit(produto);
			}

			this.limparCampos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excluir(Produtos entidade) {

		produtosFacade.remove(entidade);
	}

	public void alterar(Produtos entidade) {
		this.nome = entidade.getNome();
		this.descricao = entidade.getDescricao();
		this.id = entidade.getId();
		this.referencia = entidade.getReferencia();
		this.preco = "" + entidade.getPreco();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void limparCampos() {
		this.nome = "";
		this.descricao = "";
		this.id = null;
		this.referencia = "";
		this.preco = "";
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	
}
