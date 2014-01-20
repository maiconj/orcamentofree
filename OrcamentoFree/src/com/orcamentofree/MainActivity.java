package com.orcamentofree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.orcamentofree.base.OrcamentoFreeDao;

public class MainActivity extends Activity {

	private Button addOrcamentoBtn;
	private Intent intentOrcamento;
	private OrcamentoFreeDao dbHelp = null;
	private static final String LOG = "DESENV";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			carregaComponentes();

			/** Ação de carregar lista **/
			carregaListaOrcamentos();

			/** Ação do botão 'Adiconar Produto' **/
			btnOrcamentoAddAction();

		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}

		// TODO
		carregaListaOrcamentos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void carregaComponentes() {
		this.addOrcamentoBtn = (Button) findViewById(R.id.btn_orcamento_add);
		this.intentOrcamento = new Intent(this, OrcamentoActivity.class);
		dbHelp = new OrcamentoFreeDao(getApplicationContext());
	}

	public void btnOrcamentoAddAction() {
		addOrcamentoBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(intentOrcamento);
			}
		});
	}

	public void carregaListaOrcamentos() {
		// TODO
	}

}
