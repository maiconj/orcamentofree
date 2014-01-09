package com.orcamentofree.pojo;


public class Produto {

	private int id;
	private String codigo;
	private String descricao;
	private Float quantidade;
	private Float preco;
	private String foto;
	private int idOrcamento;

	
	public Produto() {
	}

	public Produto(int id, String codigo, String descricao, Float quantidade, Float preco, String foto, int idOrcamento) {
		super();
		this.setId(id);
		this.setCodigo(codigo);
		this.setDescricao(descricao);
		this.setQuantidade(quantidade);
		this.setPreco(preco);
		this.setFoto(foto);
		this.setIdOrcamento(idOrcamento);
	}
	
	public Produto(String codigo, String descricao, String quantidade, String preco) {
		super();
		this.setCodigo(codigo);
		this.setDescricao(descricao);
		
		if (quantidade == null || quantidade.isEmpty()) {
			this.setQuantidade(Float.valueOf(0));
		} else {
			this.setQuantidade(Float.valueOf(quantidade));
		}
		
		if (preco == null || preco.isEmpty()) {
			this.setPreco(Float.valueOf(0));
		} else {
			this.setPreco(Float.valueOf(preco));
		}
	}

	public Produto(String id, String codigo, String descricao, String quantidade, String preco, String foto, String idOrcamento) {
		super();
		
		if (id == null || id.isEmpty()) {
			this.setId(Integer.valueOf(0));
		} else {
			this.setId(Integer.valueOf(id));
		}
		
		this.setCodigo(codigo);
		this.setDescricao(descricao);
		
		if (quantidade == null || quantidade.isEmpty()) {
			this.setQuantidade(Float.valueOf(0));
		} else {
			this.setQuantidade(Float.valueOf(quantidade));
		}
		
		if (preco == null || preco.isEmpty()) {
			this.setPreco(Float.valueOf(0));
		} else {
			this.setPreco(Float.valueOf(preco));
		}
		
		
		if (idOrcamento == null || idOrcamento.isEmpty()) {
			this.setIdOrcamento(Integer.valueOf(0));
		} else {
			this.setIdOrcamento(Integer.valueOf(idOrcamento));
		}
		this.setFoto(foto);

	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Float getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Float quantidade) {
		this.quantidade = quantidade;
	}

	public Float getPreco() {
		return preco;
	}

	public void setPreco(Float preco) {
		this.preco = preco;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(int idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public String getTotal() {
		String retorno = "0.00";
		if(!Float.isNaN(this.getQuantidade()) && !Float.isNaN(this.getPreco())){
			retorno = String.valueOf(this.getQuantidade() * this.getPreco());
		}
		return retorno;
	}

}
