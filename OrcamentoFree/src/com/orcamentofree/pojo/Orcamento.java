package com.orcamentofree.pojo;

public class Orcamento {

	private int _id;
	private String descricao;
	private String loja;
	private String data;
	private String endereco;
	public static String[] colunas = new String[] { "_id", "descricao", "loja",
			"data_hora", "endereco" };

	public Orcamento() {
	}

	public Orcamento(int id, String descricao, String loja, String endereco,
			String data) {
		super();
		this.set_id(id);
		this.setDescricao(descricao);
		this.setLoja(loja);
		this.setData(data);
		this.setEndereco(endereco);
	}

	public Orcamento(String id, String descricao, String loja, String data,
			String endereco) {
		super();
		if (id == null) {
			this.set_id(Integer.valueOf(0));
		} else {
			this.set_id(Integer.valueOf(id));
		}
		this.setDescricao(descricao);
		this.setLoja(loja);
		this.setData(data);
		this.setEndereco(endereco);
	}

	public Orcamento(String descricao, String loja, String data, String endereco) {
		super();
		this.setDescricao(descricao);
		this.setLoja(loja);
		this.setData(data);
		this.setEndereco(endereco);
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

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLoja() {
		return loja;
	}

	public void setLoja(String loja) {
		this.loja = loja;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}
