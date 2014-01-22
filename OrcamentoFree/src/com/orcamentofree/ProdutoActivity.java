package com.orcamentofree;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.pojo.Orcamento;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ProdutoActivity extends Activity {

	private Button btnSaveProduto;
	private Button btnDeleteProduto;
	private Button btnCancelProduto;
	private static final String LOG = "DESENV";
	private static final int MENU_SAVE_PRODUTO = 1;
	private static final int MENU_CANCEL_PRODUTO = 2;
	private static final int MENU_DELETE_PRODUTO = 3;
	private OrcamentoFreeDao dbHelp = null;
	private Orcamento orcamento;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto);

		/** Inicializa componetes da tela **/
		carregaComponentes();

		/** Carrega ORCAMENTO selecionado**/
		carregaOrcamento();

		/** Ação do botão 'Salvar Orcamento' **/
		btnSaveProdutoAction();

		/** Ação do botão 'Excluir Orcamento' **/
		btnDeleteProdutoAction();

		/** Ação do botão 'Cancelar Orcamento' **/
		btnCancelProdutoAction();

	}

	private void btnCancelProdutoAction() {
		btnCancelProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnCancelOrcamento",
						"Aciondo botão de cancelar produto.");
				cancelProduto();
			}

		});
	}

	private void btnDeleteProdutoAction() {
		btnDeleteProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnDeleteOrcamento", "Aciondo botão de excluir produto.");
				deleteProduto();
			}
		});
	}

	private void btnSaveProdutoAction() {
		btnSaveProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnSaveOrcamento", "Aciondo botão de salvar produto.");
				saveProduto();
			}

		});
	}

	private void carregaComponentes() {
		this.btnSaveProduto = (Button) findViewById(R.id.btn_produto_save);
		this.btnDeleteProduto = (Button) findViewById(R.id.btn_produto_delete);
		this.btnCancelProduto = (Button) findViewById(R.id.btn_produto_cancel);
		this.dbHelp = new OrcamentoFreeDao(getApplicationContext());
	}

	private void carregaOrcamento() {
		try {
			Intent it = getIntent();
			if (it.getStringExtra("ID_ORCAMENTO_EDIT") != null) {
				this.orcamento = dbHelp.findOrcamentoById(Integer.valueOf(it.getStringExtra("ID_ORCAMENTO_EDIT")));
				Toast.makeText(this,"ID do Orcamento Selecionado" + this.orcamento, 2000).show();
				Log.e(LOG, " - _id: " + orcamento.get_id());
				Log.e(LOG, " - descricao: " + orcamento.getDescricao());
				Log.e(LOG, " - loja: " + orcamento.getLoja());
				Log.e(LOG, " - data: " + orcamento.getData());
				Log.e(LOG, " - endereco: " + orcamento.getEndereco());
				Log.e(LOG, "----------------------");
			} else {
				Toast.makeText(this,"Desculpe o transtorno, selecione um orcamento primeiro",2000).show();
				finish();
			}
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
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
			Log.w(LOG, "Aciondo botão de salvar produto.");
			saveProduto();
			return true;
		case MENU_CANCEL_PRODUTO:
			finish();
			return true;
		case MENU_DELETE_PRODUTO:
			Log.w(LOG, "Aciondo botão de excluir produto.");
			deleteProduto();
			return true;
		}
		return false;
	}	
}
