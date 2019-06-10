package controllers.produto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.transaction.Transactional;

import model.entities.DiarioEstoque;
import model.entities.Produtos;
import model.enums.RazaoMovimentoEstoque;
import model.enums.TipoMovimento;
import model.sessionbeans.DiarioEstoqueFacade;
import model.sessionbeans.ProdutosFacade;
import java.io.Serializable;

@Named(value = "estoqueController")
@SessionScoped
public class EstoqueController implements Serializable {

	private static final long serialVersionUID = 1L;
	Integer codRazao;
	String quantidade;
	Produtos produto;
	String descProd = "";

	@EJB
	private DiarioEstoqueFacade diarioEstoqueFacade;
	@EJB
	private ProdutosFacade produtosFacade;

	public List<DiarioEstoque> listarMovimentos() {

		return this.diarioEstoqueFacade.findAll();
	}

	public void limparCampos() {
		this.quantidade = "";
		this.produto = null;
		this.descProd = null;
	}

	public void salvar() {
		try {
			if (!isDadosValidos()) {
				return;
			}

			Double qtd = Double.parseDouble(quantidade);
			boolean isSucesso = this.registrarMovimentoEstoque(produto, RazaoMovimentoEstoque.toEnum(codRazao), qtd);

			if (isSucesso) {
				FacesMessage msg = new FacesMessage("Sucesso", "Operação realizada com sucesso!");
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				refresh();
				this.limparCampos();
			} else {
				FacesMessage msg = new FacesMessage("Falha!!!", "Não foi possível registrar a operação! ");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Falha!!!", e.getMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
	}

	private void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}

	public void setProdutoSelecionado(Produtos produto) {
		System.out.println(produto.toString());
		this.descProd = produto.getNome();
		this.produto = produto;
	}

	private boolean isDadosValidos() {
		if (this.produto == null) {
			FacesMessage msg = new FacesMessage("Preencha todos os campos!!!", "Selecione um produto");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}

		try {
			Double qtd = Double.parseDouble(this.quantidade);
			if (qtd == 0.0) {
				throw new Exception("");
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Dados inválidos!!!", "Informe um valor válido para a  quantidade");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}

		return true;
	}

	public List<SelectItem> getValuesComboBox() {
		List<SelectItem> toReturn = new ArrayList<SelectItem>();
		for (RazaoMovimentoEstoque razao : RazaoMovimentoEstoque.values()) {
			toReturn.add(new SelectItem(razao.getCod(), razao.getDescricao()));
		}
		return toReturn;
	}

	@Transactional
	private boolean registrarMovimentoEstoque(Produtos produto, RazaoMovimentoEstoque razaoMovimento, double qtd) {
		if (produto == null || razaoMovimento == null || produto.getId() == null) {
			return false;
		}

		DiarioEstoque diarioEstoque = new DiarioEstoque();

		diarioEstoque.setData(new Date());
		diarioEstoque.setProduto(produto);
		diarioEstoque.setRazaoMovimentoEstoque(razaoMovimento);

		if (razaoMovimento.getTipoMovimento() == TipoMovimento.SAIDA) {
			qtd = qtd * -1;
		}

		if (produto.getQuantidade() == null) {
			produto = produtosFacade.find(produto.getId());
		}

		produto.setQuantidade(produto.getQuantidade() + qtd);
		diarioEstoque.setQuantidade(qtd);
		try {
			diarioEstoqueFacade.create(diarioEstoque);
			produtosFacade.edit(produto);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Integer getCodRazao() {
		return codRazao;
	}

	public void setCodRazao(Integer codRazao) {
		this.codRazao = codRazao;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescProd() {
		return descProd;
	}

	public void setDescProd(String descProd) {
		this.descProd = descProd;
	}

	public Produtos getProduto() {
		return produto;
	}

	public void setProduto(Produtos produto) {
		this.produto = produto;
	}

}
