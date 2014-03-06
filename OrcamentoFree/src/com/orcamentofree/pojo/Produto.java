package com.orcamentofree.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.util.Log;

public class Produto {

	private int _id;
	private String codigo;
	private String descricao;
	private Float quantidade;
	private Float preco;
	private String foto;
	private String unidadeMedida;
	
	private int id_orcamento;

	public static String[] colunas = new String[] { "_id", "codigo", "descricao", "quantidade", "preco", "foto", "id_orcamento" , "unidade_medida" };

	public Produto() {
	}

	public Produto(int id, String codigo, String descricao, Float quantidade,
			Float preco, String foto, int idOrcamento, String unidadeMedida) {
		super();
		this.set_id(id);
		this.setCodigo(codigo);
		this.setDescricao(descricao);
		this.setQuantidade(quantidade);
		this.setPreco(preco);
		this.setFoto(foto);
		this.set_idOrcamento(idOrcamento);
		this.setUnidadeMedida(unidadeMedida);
	}

	public Produto(String codigo, String descricao, String quantidade,
			String preco, String unidadeMedida) {
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
		this.setUnidadeMedida(unidadeMedida);
	}

	public Produto(String id, String codigo, String descricao,
			String quantidade, String preco, String foto, String idOrcamento, String unidadeMedida) {
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
		this.setUnidadeMedida(unidadeMedida);

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
	
	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public BigDecimal getTotal() {
		BigDecimal retorno = null;
		try {
			retorno = BigDecimal.valueOf(this.getPreco() * this.getQuantidade()).setScale(2, RoundingMode.HALF_UP);
		} catch (Exception e) {
			Log.e("DESENV", e.getMessage());
			retorno = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		}
		return retorno;
	}

}
