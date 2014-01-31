package com.orcamentofree;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class OrcamentoHelpActivity extends Activity {

	private static final int MENU_SAIR = 0;
	private ImageButton exitOrcamentoHelpBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orcamento_help);
		
		/**Carrega componentes da tela*/
		carregaComponentes();
		
		/** A��o do bot�o 'Sair' **/
		btnExitOrcamentoHelpAction();
	}

	private void carregaComponentes() {
 		this.exitOrcamentoHelpBtn = (ImageButton) findViewById(R.id.btn_orcamento_help_exit);
 	}
	
	private void btnExitOrcamentoHelpAction() {
		exitOrcamentoHelpBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, MENU_SAIR, 0, "Voltar").setIcon(R.drawable.ic_action_back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAIR:
			finish();
			return true;
		}
		return false;
	}	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.orcamento_help, menu);
//		return true;
//	}

}
