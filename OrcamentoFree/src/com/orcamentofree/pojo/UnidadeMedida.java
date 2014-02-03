package com.orcamentofree.pojo;


public enum UnidadeMedida {
	 UN(0, "UN", "Unidade"),MT(1, "MT", "Metros"), QTD(2, "QTD", "Quantidade"), KG(3, "KG", "Quilo");

	private final int id;
	private final String sigla;
	private final String descricao;

	private UnidadeMedida(int id, String sigla, String desc) {
		this.id = id;
		this.sigla = sigla;
		this.descricao = desc;
	}

	public int getId() {
		return id;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public static String getDescricao(int valor) {
		String retorno = null;
		switch (valor) {
		case 0:
			retorno = UN.descricao;
			break;
		case 1:
			retorno = MT.descricao;
			break;
		case 2:
			retorno = QTD.descricao;
			break;
		case 3:
			retorno = KG.descricao;
			break;
		}
		return retorno;
	}
	public static String getSigla(int valor) {
		String retorno = null;
		switch (valor) {
		case 0:
			retorno = UN.sigla;
			break;
		case 1:
			retorno = MT.sigla;
			break;
		case 2:
			retorno = QTD.sigla;
			break;
		case 3:
			retorno = KG.sigla;
			break;
		}
		return retorno;
	}
	
	public static int getId(String desc) {
		int retorno = 0;
		if (desc.compareTo(MT.sigla)==0){
			retorno = MT.id;
		}else if (desc.compareTo(KG.sigla)==0){
			retorno = KG.id;
		}else if (desc.compareTo(UN.sigla)==0){
			retorno = UN.id;
		}else if (desc.compareTo(QTD.sigla)==0){
			retorno = QTD.id;
		}
		return retorno;
	}

	public static String[] getValues() {
		String[] umProdutos = {UN.sigla,MT.sigla,QTD.sigla,KG.sigla};
		return umProdutos;
	}

}
