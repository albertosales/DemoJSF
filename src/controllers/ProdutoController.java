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
	List<model.entities.Produtos> produtos;
	Produtos produto = new Produtos(); // acrescentado para possibilitar o uso do getter
	// private EntityManager emObj =
	// Persistence.createEntityManagerFactory("OnLine-ejbPU").createEntityManager();

	private Produtos selProduto;

	// persistir dados dos produtos
	private Integer id;
	private String nome;
	private String descricao;
	private double preco;
	private UploadedFile file;
	byte[] bImagem; // = new byte[(int) file.length()];
    private StreamedContent sc;
	
	
	@EJB
	private ProdutosFacade produtosFacade;
	@EJB
	private GerenciarCarrinho carrinho;
	@EJB
	private UsuariosFacade usuariosFacade;

	public List<model.entities.Produtos> todosProdutos() {
		return produtosFacade.findAll();
	}

	public List<model.entities.Produtos> getProdutos() {
		produtos = produtosFacade.findAll();
		return produtos;
	}

	public Produtos getProduto(Integer id) {
		produto = produtosFacade.find(id);
		return produto;
	}

	public void adicionarItemCarro(int id) {
		carrinho.adicionarItem(id);
	}

	public void removeItemDoCarro(int id) {
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
		if (usuariosFacade.estaLogado() || usuariosFacade.estaAutenticado(usuario, senha)) {
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
		System.out.println("preço inserido " + preco);
	}

	public byte[] getbImagem() {
		return bImagem;
	}

	public void setbImagem(byte[] bImagem) {
		this.bImagem = bImagem;
	}

	public Produtos getSelProduto() {
		return selProduto;
	}

	public void setSelProduto(Produtos selProduto) {
		this.selProduto = selProduto;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.bImagem = new byte[(int) file.getSize()];
		this.bImagem = file.getContents();
		this.file = file;
	}

	public void reiniciarVariaveis() {
		this.nome = "";
		this.descricao = "";
		this.preco = 0;
		this.bImagem = null;
	}

	public String criarProduto() {
		this.produto.setNome(this.getNome());
		//System.out.println("produto " + this.nome);
		this.produto.setDescricao(this.getDescricao());
		//System.out.println("descrição " + this.descricao);
		this.produto.setPreco(this.getPreco());
		//System.out.println("preco " + this.preco);
		this.produto.setImagem(this.getbImagem());
		/*
		System.out.println("imagem: " + this.preco);
		System.out.println("Informação do Produto");
		System.out.println("produto nome " + produto.getNome() + " foi criado");
		System.out.println("produto desc " + produto.getDescricao() + " foi criado");
		System.out.println("produto preco " + produto.getPreco() + " foi criado");
		System.out.println("produto imagem " + produto.getImagem() + " foi criado");
		System.out.println("produto id " + produto.getId() + " foi criado");
		*/
		this.produtosFacade.create(this.produto);
		this.reiniciarVariaveis();
		return "";
	}

	public String atualizarProduto() {
		this.produtosFacade.edit(this.selProduto);
		// return "index.xhtml?faces-redirect=true";
		return "";
	}

	public String deletarProduto(Produtos prod) {
		this.produtosFacade.remove(prod);
		// return "index.xhtml?faces-redirect=true";
		return "";
	}

	public void upload() {
		if (file != null) {
			FacesMessage message = new FacesMessage("Sucesso", file.getFileName() + " foi enviado.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		this.file = event.getFile();
		this.bImagem = event.getFile().getContents();
		String fileName = file.getFileName();
		long fileSize = file.getSize();
		String infoAboutFile = "Arquivo recebido:" + fileName + " Tamanho:" + fileSize;
		FacesMessage msg = new FacesMessage("Sucesso!!!",
				event.getFile().getFileName() + " foi lido. " + infoAboutFile);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void uploadAction(FileUploadEvent event) {
		this.bImagem = event.getFile().getContents();
	}

	// Helper Method 1 - Fetch Maximum School Id From The Database
	/*
	 * private int getMaxProdId() {
	 * 
	 * int maxProdId = 1; Query queryObj =
	 * emObj.createQuery("SELECT MAX(s.id)+1 FROM Produtos s");
	 * if(queryObj.getSingleResult() != null) { maxProdId = (Integer)
	 * queryObj.getSingleResult(); } return maxProdId; }
	 */
	public BufferedImage exibirFoto(int id) {
		// byte[] foto = null;

		// LENDO E COPIANDO IMAGEM ##############################################
		BufferedImage img = null;
		// getProduto(id);

		try {
			img = ImageIO.read(new ByteArrayInputStream(getProduto(id).getImagem()));
			// lblFoto.setIcon(new ImageIcon(img));
			// ImageIO.write(img, "PNG", new File("h.png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}

    public StreamedContent getSc() {
    	return sc;
    }
    
    public void setSc(StreamedContent sc) {
    	this.sc = sc;
    }

	public StreamedContent getImagem(int id) {
		return(new DefaultStreamedContent(new ByteArrayInputStream(getProduto(id).getImagem())));
	}
}