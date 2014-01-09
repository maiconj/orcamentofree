package com.orcamentofree;

//import com.orcamentofree.base.OrcamentoDataBaseHelper;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.utils.DateUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrcamentoActivity extends Activity {

	private Button btnProdutoAdd;
	private Button btnOrcamentoSave;
	private Button btnOrcamentoDelete;
	private Button btnOrcamentoCancel;

	private EditText txtOrcamentoDescricao;
	private EditText txtOrcamentoLoja;
	private EditText txtOrcamentoEndereco;

	private Orcamento orcamento;
	private DateUtils dateUtils;

//	private OrcamentoDataBaseHelper dbHelp = null;
	private Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orcamento);
		carregaComponentes();

		/** Ação do botão 'Adiconar Produto' **/
		btnProdutoAddAction();

		/** Ação do botão 'Salvar Orcamento' **/
		btnOrcamentoSaveAction();

		/** Ação do botão 'Excluir Orcamento' **/
		btnOrcamentoDeleteAction();

		/** Ação do botão 'Cancelar Orcamento' **/
		btnOrcamentoCancelAction();

	}

	private void btnOrcamentoCancelAction() {
		btnOrcamentoCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancelOrcamento();
			}

		});
	}

	private void btnOrcamentoDeleteAction() {
		btnOrcamentoDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	//			deleteOrcamento(orcamento, dbHelp);
			}
		});
	}

	private void btnOrcamentoSaveAction() {
		btnOrcamentoSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (camposValidos()) {
		//			saveOrcamento(dbHelp);
				}
			}

		});
	}

	private void btnProdutoAddAction() {
		btnProdutoAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(i);
			}

		});
	}

	private void carregaComponentes() {
		//dbHelp = new OrcamentoDataBaseHelper(getApplicationContext());

		this.btnProdutoAdd = (Button) findViewById(R.id.btn_produto_add);
		this.btnOrcamentoSave = (Button) findViewById(R.id.btn_orcamento_save);
		this.btnOrcamentoDelete = (Button) findViewById(R.id.btn_orcamento_delete);
		this.btnOrcamentoCancel = (Button) findViewById(R.id.btn_orcamento_cancel);

		this.txtOrcamentoDescricao = (EditText) findViewById(R.id.txt_orcamento_descricao);
		this.txtOrcamentoLoja = (EditText) findViewById(R.id.txt_orcamento_loja);
		this.txtOrcamentoEndereco = (EditText) findViewById(R.id.txt_orcamento_endereco);

		this.dateUtils = new DateUtils();
		this.i = new Intent(this, ProdutoActivity.class);
	}

	//private void saveOrcamento(OrcamentoDataBaseHelper dbHelp) {
//		try {
//			ContentValues values = new ContentValues();
//			values.put("orcamento_descricao",  this.txtOrcamentoDescricao.getText().toString());
//			values.put("orcamento_loja",  this.txtOrcamentoLoja.getText().toString());
//			values.put("orcamento_endereco",  this.txtOrcamentoEndereco.getText().toString());
//			values.put("orcamento_data",  this.dateUtils.getNewDate().toString());
//			this.dbHelp.orcamentoSave(values);
//			Cursor c = this.dbHelp.orcamentoListAll();
//			c.getCount();
//		} catch (Exception e) {
//			e.getMessage();
//		}
		//finish();
	//}

//	private void deleteOrcamento(Orcamento orc, OrcamentoDataBaseHelper dbHelp) {
//		try {
//			this.dbHelp.orcamentoDelete();
//			// this.orcamentoDao.delete(orc,dbHelp);
//		} catch (Exception e) {
//			e.getMessage();
//		}
//		finish();
	//}

	private void cancelOrcamento() {
		finish();
	}

	public boolean camposValidos() {
		boolean erro = true;
		if (this.txtOrcamentoDescricao.getText().toString().isEmpty()
				|| this.txtOrcamentoLoja.getText().toString().isEmpty()
				|| this.txtOrcamentoLoja.getText().toString().isEmpty()) {
			erro = false;
		}
		return erro;
	}

	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	 // Inflate the menu; this adds items to the action bar if it is present.
	 getMenuInflater().inflate(R.menu.orcamento, menu);
	 return true;
	 }

}
