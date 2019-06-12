package controllers.produto;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import model.entities.Categoria;
import model.sessionbeans.CategoriaFacade;
import java.io.Serializable;

@Named(value = "categoriaController")
@SessionScoped
public class CategoriaController implements Serializable {

	private static final long serialVersionUID = 1L;
	String nome;
	Long id;
	
	@EJB
	private CategoriaFacade categoriaFacade;
	
	public void salvar() {
		try {
			Categoria categoria = new Categoria();

			categoria.setNome(nome);
			categoria.setId(id);

			if (id == null) {
				this.categoriaFacade.create(categoria);
			} else {
				this.categoriaFacade.edit(categoria);
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

	public void excluir(Categoria entidade) {
		try {
			categoriaFacade.remove(entidade);

			FacesMessage msg = new FacesMessage("Sucesso", "Operação realizada com sucesso!");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Falha!!!", "Não foi possível excluir a Categoria");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void alterar(Categoria entidade) {
		this.nome = entidade.getNome();
		this.id = entidade.getId();
	}
	
	
	private void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();  
        Application application = context.getApplication();  
        ViewHandler viewHandler = application.getViewHandler();  
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());  
        context.setViewRoot(viewRoot);  
        context.renderResponse();  
		
	}

	public List<Categoria> getCategorias(){
		return this.categoriaFacade.findAll();
	}
	
	public void limparCampos() {
		this.id = null;
		this.nome = null;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
