package com.orcamentofree.utils;

import java.text.NumberFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MascaraMonetaria implements TextWatcher {

	final EditText campo;
	
	public MascaraMonetaria() {
		this.campo = null;
	}

	public MascaraMonetaria(EditText campo) {
		super();
		this.campo = campo;
	}

	private boolean isUpdating = false;
	// Pega a formatacao do sistema, se for brasil R$ se EUA US$
	private NumberFormat nf = NumberFormat.getCurrencyInstance();

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int after) {
		// Evita que o método seja executado varias vezes.
		// Se tirar ele entre em loop
		if (isUpdating) {
			isUpdating = false;
			return;
		}

		isUpdating = true;
		String str = s.toString();
		// Verifica se já existe a máscara no texto.
		boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str.indexOf(".") > -1 || str.indexOf(",") > -1));
		// Verificamos se existe máscara
		if (hasMask) {
			// Retiramos a máscara.
			str = str.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
		}

		try {
			// Transformamos o número que está escrito no EditText em
			// monetário.
			nf.setMinimumFractionDigits(2);
			str = nf.format(Float.parseFloat(str) / 100);
			campo.setText(str);
			campo.setSelection(campo.getText().length());
		} catch (NumberFormatException e) {
			s = "";
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
		// Não utilizado
	}

	@Override
	public void afterTextChanged(Editable s) {
		// Não utilizado
	}
	
	public String replaceField(String valor){
		String retorno =  null;
		retorno = valor.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
		return retorno;		
	}
	
	//TODO
//	string valor = "r$1.234,59";
//	valor = "1.200.00";
//	valor = "1.200. 00";
//	
//	
//	valor.replaceall("[r$]", "").replaceall("[,]", "");
//	
//	if(valor.length()>=6){
//		valor.re
//	}
//	valor.replaceall("[r$]", "").replaceall("[,]", "").indexof(".")==1;
//	valor.indexof(".");
	

}