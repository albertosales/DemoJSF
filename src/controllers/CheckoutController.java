package controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import model.entities.Pagamento;
import model.entities.Pedido;
import model.entities.Produtos;
import model.entities.Item;
import model.entities.ItemPedido;
import model.entities.Usuarios;
import model.entities.enuns.TipoPagamento;
import model.sessionbeans.GerenciarCarrinho;
import model.sessionbeans.PedidoFacade;
import model.sessionbeans.ProdutosFacade;
import model.sessionbeans.UsuariosFacade;

@Named
@SessionScoped
public class CheckoutController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private UsuariosFacade usuariosFacade;
	
	@EJB
	private GerenciarCarrinho carrinho;
	
	@EJB
	private ProdutosFacade produtosFacade;
	
	@EJB
	private PedidoFacade pedidoFacade;
	
	private Integer formaPagamento;
	private Pedido pedido;
	
	@PostConstruct
	public void init() {
		this.pedido = new Pedido();
		this.pedido.setPagamento(new Pagamento());
		this.pedido.setCliente(this.getUsuario());
		this.recuperarCarrinho();
	}
	
	private void recuperarCarrinho() {
		if (carrinho == null || carrinho.getItems() == null) {
			return;
		}
		for (Item item : carrinho.getItems()) {
			Produtos produto = produtosFacade.find(item.getId());
			
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setPedido(this.pedido);
			itemPedido.setProduto(produto);
			itemPedido.setQuantidade(item.getQtde());
			this.pedido.getItemPedidos().add(itemPedido);
		}
	}
	
	public String finalizarPedido() {
		TipoPagamento tipoPagamento = null;
		for (TipoPagamento tp: TipoPagamento.values()) {
			if ((tp.ordinal() + 1) == this.formaPagamento) {
				tipoPagamento = tp;
			}
		}
		
		this.pedido.getPagamento().setTipoPagamento(tipoPagamento);
		pedidoFacade.create(this.pedido);
		carrinho.removeTodos();
		this.init();
		
		return "pedido-completo";
	}
	
	public String revisao() {
		recuperarCarrinho();
		return "checkout-revisao";
	}
	
	public Usuarios getUsuario() {
		return this.usuariosFacade.getUsuarioLogado();
	}

	public Integer getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(Integer formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}	
}
