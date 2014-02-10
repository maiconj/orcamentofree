package com.orcamentofree.utils;

import java.text.NumberFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MascaraQtde implements TextWatcher {

	private final EditText campo;
	private final String LOG = "DESENV";
	
	public MascaraQtde() {
		this.campo = null;
	}

	public MascaraQtde(EditText campo) {
		super();
		this.campo = campo;
	}

	private boolean isUpdating = false;
	// Pega a formatacao do sistema, se for brasil R$ se EUA US$
	private NumberFormat nf = NumberFormat.getNumberInstance();

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
		boolean hasMask = (str.indexOf(".") > -1 || str.indexOf(",") > -1);
		// Verificamos se existe máscara
		if (hasMask) {
			// Retiramos a máscara.
			str = str.replaceAll("[,]", "").replaceAll("[.]", "");
		}

	
			// Transformamos o número que está escrito no EditText em
			// monetário.
		try {
				
			nf.setMinimumFractionDigits(2);
			str = nf.format(Float.parseFloat(str) / 100);
			campo.setText(str);
			campo.setSelection(campo.getText().length());
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
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

	public String replaceField(String valor) {
		return  valor.replace(",", ".");		
	}

}