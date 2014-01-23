package com.orcamentofree.pojo;

public class Produto {

	private int _id;
	private String codigo;
	private String descricao;
	private Float quantidade;
	private Float preco;
	private String foto;
	private int id_orcamento;

	public static String[] colunas = new String[] { "_id", "codigo", "descricao", "quantidade", "preco", "foto", "id_orcamento" };

	public Produto() {
	}

	public Produto(int id, String codigo, String descricao, Float quantidade,
			Float preco, String foto, int idOrcamento) {
		super();
		this.set_id(id);
		this.setCodigo(codigo);
		this.setDescricao(descricao);
		this.setQuantidade(quantidade);
		this.setPreco(preco);
		this.setFoto(foto);
		this.set_idOrcamento(idOrcamento);
	}

	public Produto(String codigo, String descricao, String quantidade,
			String preco) {
		super();
		this.setCodigo(codigo);
		this.setDescricao(descricao);

		if (quantidade == null) {
			this.setQuantidade(Float.valueOf(0));
		} else {
			this.setQuantidade(Float.valueOf(quantidade));
		}

		if (preco == null) {
			this.setPreco(Float.valueOf(0));
		} else {
			this.setPreco(Float.valueOf(preco));
		}
	}

	public Produto(String id, String codigo, String descricao,
			String quantidade, String preco, String foto, String idOrcamento) {
		super();

		if (id == null ) {
			this.set_id(Integer.valueOf(0));
		} else {
			this.set_id(Integer.valueOf(id));
		}

		this.setCodigo(codigo);
		this.setDescricao(descricao);

		if (quantidade == null) {
			this.setQuantidade(Float.valueOf(0));
		} else {
			this.setQuantidade(Float.valueOf(quantidade));
		}

		if (preco == null) {
			this.setPreco(Float.valueOf(0));
		} else {
			this.setPreco(Float.valueOf(preco));
		}

		if (idOrcamento == null) {
			this.set_idOrcamento(Integer.valueOf(0));
		} else {
			this.set_idOrcamento(Integer.valueOf(idOrcamento));
		}
		this.setFoto(foto);

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
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

	public int get_idOrcamento() {
		return id_orcamento;
	}

	public void set_idOrcamento(int idOrcamento) {
		this.id_orcamento = idOrcamento;
	}

	public String getTotal() {
		String retorno = "0.00";
		if (!Float.isNaN(this.getQuantidade()) && !Float.isNaN(this.getPreco())) {
			retorno = String.valueOf(this.getQuantidade() * this.getPreco());
		}
		return retorno;
	}

}
