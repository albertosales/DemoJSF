package controllers.produto;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import model.entities.Categoria;
import model.entities.Produtos;
import model.sessionbeans.CategoriaFacade;
import model.sessionbeans.ProdutosFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "crudProdutoController")
@SessionScoped
public class CrudProdutoController implements Serializable {

	private static final long serialVersionUID = 1L;

	String nome = "";
	String descricao = "";
	String preco;
	String referencia;
	String precoPromocional;
	String marca;  
	Integer id;
	String quantidade;
	String quantidadeMinima;
	Long categoria;

	@EJB
	private ProdutosFacade produtosFacade;

	@EJB
	private CategoriaFacade categoriaFacade;

	private UploadedFile file;
	private byte[] bImagem;

	public void salvar() {
		try {

			if (!isDadosValidos()) {
				return;
			}

			Produtos produto = new Produtos();
			Categoria categoria = new Categoria();
			categoria.setId(this.categoria);

			produto.setImagem(bImagem);
			produto.setDescricao(descricao);
			produto.setNome(nome);
			produto.setMarca(marca);
			produto.setQuantidadeMinima(Double.parseDouble(quantidadeMinima));
			
			if (precoPromocional != null && !precoPromocional.isEmpty()) {
				produto.setPrecoPromocional(Double.parseDouble(precoPromocional));
			}
			produto.setId(id);
			produto.setCategoria(categoria);
			produto.setReferencia(referencia);
			produto.setPreco(Double.parseDouble(preco));

			if (id == null) {
				this.produtosFacade.create(produto);
			} else {
				this.produtosFacade.edit(produto);
			}

			FacesMessage msg = new FacesMessage("Sucesso", "Opera��o realizada com sucesso!");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			refresh();
			this.limparCampos();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Falha!!!", e.getMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
	}

	private boolean isDadosValidos() {
		try {
			Double.parseDouble(preco);
			return true;
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Falha!!!", e.getMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return false;
	}

	public void limpar() {
		this.limparCampos();
	}

	public void excluir(Produtos entidade) {
		try {
			produtosFacade.remove(entidade);

			FacesMessage msg = new FacesMessage("Sucesso", "Opera��o realizada com sucesso!");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Falha!!!", "N�o foi poss�vel excluir o produto");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public List<SelectItem> getValuesComboBox() {
		List<SelectItem> toReturn = new ArrayList<SelectItem>();
		List<Categoria> categorias = categoriaFacade.findAll();
		for (Categoria categoria : categorias) {
			toReturn.add(new SelectItem(categoria.getId(), categoria.getNome()));
		}

		return toReturn;
	}

	public void alterar(Produtos entidade) {
		this.nome = entidade.getNome();
		this.descricao = entidade.getDescricao();
		this.id = entidade.getId();
		this.categoria = entidade.getCategoria().getId();
		this.referencia = entidade.getReferencia();
		this.preco = "" + entidade.getPreco();
		this.quantidadeMinima = "" + entidade.getQuantidadeMinima();
		this.quantidade = "" + (entidade.getQuantidade() == null ? "0.0" : entidade.getQuantidade());
		this.bImagem = entidade.getImagem();
		this.precoPromocional = "" + (entidade.getPrecoPromocional() == null ? "" : entidade.getPrecoPromocional());
		this.marca = entidade.getMarca();
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
		this.categoria = null;
		this.referencia = "";
		this.preco = "";
		this.marca = "";
		this.precoPromocional = "";
		this.bImagem = null;
		this.quantidadeMinima = null;
		this.quantidade = null;
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

	public Long getCategoria() {
		return categoria;
	}

	public void setCategoria(Long categoria) {
		this.categoria = categoria;
	}

	
	public String getPrecoPromocional() {
		return precoPromocional;
	}

	public void setPrecoPromocional(String precoPromocional) {
		this.precoPromocional = precoPromocional;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(String quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}

	public void handleFileUpload(FileUploadEvent event) {
		this.file = event.getFile();
		this.bImagem = event.getFile().getContents();
		String fileName = file.getFileName();
		long fileSize = file.getSize();
		String infoAboutFile = "Arquivo recebido:" + fileName + " Tamanho:" + fileSize;
		FacesMessage msg = new FacesMessage("Sucesso!!!",
				event.getFile().getFileName() + " foi lido. " + infoAboutFile);
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}

}
