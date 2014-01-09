package com.orcamentofree;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ProdutoActivity extends Activity {

	private Button btnSaveProduto;
	private Button btnDeleteProduto;
	private Button btnCancelProduto;

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

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	 // Inflate the menu; this adds items to the action bar if it is present.
	 getMenuInflater().inflate(R.menu.produto, menu);
	 return true;
	 }


}
