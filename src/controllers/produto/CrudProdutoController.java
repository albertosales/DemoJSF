package controllers.produto;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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

	private UploadedFile file;
	private byte[] bImagem;

	public void salvar() {
		try {
			Produtos produto = new Produtos();

			produto.setImagem(bImagem);
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

			FacesMessage msg = new FacesMessage("Sucesso", "Operação realizada com sucesso!");
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

	public void limpar() {
		this.limparCampos();
	}
	
	public void excluir(Produtos entidade) {
		try {
			produtosFacade.remove(entidade);

			FacesMessage msg = new FacesMessage("Sucesso", "Operação realizada com sucesso!");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Falha!!!", "Não foi possível excluir o produto");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void alterar(Produtos entidade) {
		this.nome = entidade.getNome();
		this.descricao = entidade.getDescricao();
		this.id = entidade.getId();
		this.referencia = entidade.getReferencia();
		this.preco = "" + entidade.getPreco();
		this.bImagem = entidade.getImagem();
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
