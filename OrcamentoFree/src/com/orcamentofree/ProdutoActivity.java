package com.orcamentofree;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ProdutoActivity extends Activity {

	private Button btnSaveProduto;
	private Button btnDeleteProduto;
	private Button btnCancelProduto;
	private static final String LOG = "DESENV";
	private static final int MENU_SAVE_PRODUTO = 1;
	private static final int MENU_CANCEL_PRODUTO = 2;
	private static final int MENU_DELETE_PRODUTO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto);

		carregaComponentes();

		/** A��o do bot�o 'Salvar Orcamento' **/
		btnSaveProdutoAction();

		/** A��o do bot�o 'Excluir Orcamento' **/
		btnDeleteProdutoAction();

		/** A��o do bot�o 'Cancelar Orcamento' **/
		btnCancelProdutoAction();

	}

	private void btnCancelProdutoAction() {
		btnCancelProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnCancelOrcamento",
						"Aciondo bot�o de cancelar produto.");
				cancelProduto();
			}

		});
	}

	private void btnDeleteProdutoAction() {
		btnDeleteProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnDeleteOrcamento", "Aciondo bot�o de excluir produto.");
				deleteProduto();
			}
		});
	}

	private void btnSaveProdutoAction() {
		btnSaveProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnSaveOrcamento", "Aciondo bot�o de salvar produto.");
				saveProduto();
			}

		});
	}

	private void carregaComponentes() {
		this.btnSaveProduto = (Button) findViewById(R.id.btn_produto_save);
		this.btnDeleteProduto = (Button) findViewById(R.id.btn_produto_delete);
		this.btnCancelProduto = (Button) findViewById(R.id.btn_produto_cancel);
	}

	private void saveProduto() {
		finish();
	}

	private void deleteProduto() {
		finish();
	}

	private void cancelProduto() {
		finish();
	}

//	 @Override
//	 public boolean onCreateOptionsMenu(Menu menu) {
//	 getMenuInflater().inflate(R.menu.produto, menu);
//	 return true;
//	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, MENU_SAVE_PRODUTO, 0, "Salvar");
		item = menu.add(0, MENU_CANCEL_PRODUTO, 0, "Cancelar");
		item = menu.add(0, MENU_DELETE_PRODUTO, 0, "Excluir");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE_PRODUTO:
			Log.w(LOG, "Aciondo bot�o de salvar produto.");
			saveProduto();
			return true;
		case MENU_CANCEL_PRODUTO:
			finish();
			return true;
		case MENU_DELETE_PRODUTO:
			Log.w(LOG, "Aciondo bot�o de excluir produto.");
			deleteProduto();
			return true;
		}
		return false;
	}	
}
