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
		// Evita que o m�todo seja executado varias vezes.
		// Se tirar ele entre em loop
		if (isUpdating) {
			isUpdating = false;
			return;
		}

		isUpdating = true;
		String str = s.toString();
		// Verifica se j� existe a m�scara no texto.
		boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str.indexOf(".") > -1 || str.indexOf(",") > -1));
		// Verificamos se existe m�scara
		if (hasMask) {
			// Retiramos a m�scara.
			str = str.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
		}

		try {
			// Transformamos o n�mero que est� escrito no EditText em
			// monet�rio.
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
		// N�o utilizado
	}

	@Override
	public void afterTextChanged(Editable s) {
		// N�o utilizado
	}

	public String replaceField(String valor) {
		String valorReplace = null;
		valorReplace = valor.replaceAll("RS", "").replace(",", ".");
		if (valor.length() >= 6) {
			valor = valorReplace.substring(0, 1) + valorReplace.substring(1, valorReplace.length());
		}
		return valor;
	}

}