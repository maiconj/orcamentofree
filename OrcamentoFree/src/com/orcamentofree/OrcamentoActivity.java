package com.orcamentofree;

//import com.orcamentofree.base.OrcamentoDataBaseHelper;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.utils.DateUtils;

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

	private OrcamentoFreeDao dbHelp = null;
	private Intent i;
	private static final String LOG = "DESENV";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_orcamento);

			/** Inicializa componetes da tela **/
			carregaComponentes();

			/** Carrega ORCAMENTO selecionado na listview**/
			CarregaOrcamentoEdit();
			
			/** Ação do botão 'Adiconar Produto' **/
			btnProdutoAddAction();

			/** Ação do botão 'Salvar Orcamento' **/
			btnOrcamentoSaveAction();

			/** Ação do botão 'Excluir Orcamento' **/
			btnOrcamentoDeleteAction();

			/** Ação do botão 'Cancelar Orcamento' **/
			btnOrcamentoCancelAction();		
		} catch (Exception e) {
			Log.e(LOG,"ERROR - DESENV: ----"+e.getMessage());
		}
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
				deleteOrcamento();
			}
		});
	}

	private void btnOrcamentoSaveAction() {
		btnOrcamentoSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (camposValidos()) {
					saveOrcamento();
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
		dbHelp = new OrcamentoFreeDao(getApplicationContext());

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

	private void CarregaOrcamentoEdit() {
		Intent it = getIntent();
		if (it.getStringExtra("ID_ORCAMENTO_EDIT") != null) {
			this.orcamento = dbHelp.findOrcamentoById( Integer.valueOf(it.getStringExtra("ID_ORCAMENTO_EDIT")));
			this.txtOrcamentoDescricao.setText(orcamento.getDescricao());
			this.txtOrcamentoLoja.setText(orcamento.getLoja());
			this.txtOrcamentoEndereco.setText(orcamento.getEndereco());
		} else {
			this.orcamento = new Orcamento();
		}
	}

	private void saveOrcamento() {
		try {
			if (this.orcamento.get_id() >= 1) {
				updateOrcamento();
			} else {
				newOrcamento();
			}
			this.orcamento = dbHelp.findOrcamentoById(this.dbHelp.saveOrcamento(this.orcamento));
			// TODO
			imprimeOrcamentos();
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
		Toast.makeText(this, "Orcamento Salvo com Sucesso", Toast.LENGTH_LONG).show();
	}

	/**
	 * Carrega os campos no orcamento para fazer update
	 * **/
	private void updateOrcamento() {
		this.orcamento.setDescricao(this.txtOrcamentoDescricao.getText().toString());
		this.orcamento.setLoja(this.txtOrcamentoLoja.getText().toString());
		this.orcamento.setData(this.dateUtils.getNewDate().toString());
		this.orcamento.setEndereco(this.txtOrcamentoEndereco.getText().toString());
	}
	

	/**
	 * Cria novo objeto de orcamento carregando os campos para salvar
	 * 
	 **/
	private void newOrcamento() {
		this.orcamento = new Orcamento(this.txtOrcamentoDescricao.getText().toString(), 
						               this.txtOrcamentoLoja.getText().toString(), 
						               this.dateUtils.getNewDate().toString(),
						               this.txtOrcamentoEndereco.getText().toString());
	}

	private void deleteOrcamento() {
		try {
			if (this.orcamento.get_id() >= 1) {
				this.dbHelp.deleteOrcamentoById(this.orcamento.get_id());
				this.orcamento = new Orcamento();
				Toast.makeText(this, "Orcamento deletado com sucesso!",	Toast.LENGTH_LONG).show();
				limpaCampos();
			} else {
				Toast.makeText(this, "Operação cancelada, você deve selecionar um orcamento!",	Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Log.e(LOG,e.getMessage());
		}
	}

	private void limpaCampos() {
		this.txtOrcamentoDescricao.setText("");
		this.txtOrcamentoLoja.setText("");
		this.txtOrcamentoEndereco.setText("");
	}

	private void cancelOrcamento() {
		finish();
	}

	public boolean camposValidos() {
		boolean erro = true;
		if (this.txtOrcamentoDescricao.getText().toString().length()<=0 
				|| this.txtOrcamentoLoja.getText().toString().length()<=0
				|| this.txtOrcamentoLoja.getText().toString().length()<=0) {
			Toast.makeText(this, "Operação cancelada, preencha os campos!",	Toast.LENGTH_LONG).show();
			erro = false;
		}
		return erro;
	}

	private void imprimeOrcamentos(){
		ArrayList<Orcamento> orcamentoLst = (ArrayList<Orcamento>) this.dbHelp.findOrcamento();
		Log.e(LOG,"numero orcamentos salvos: " + orcamentoLst.size());
		for (Orcamento orc : orcamentoLst) {
			Log.e(LOG," - _id: "+ orc.get_id());
			Log.e(LOG," - descricao: "+ orc.getDescricao());
			Log.e(LOG," - loja: "+ orc.getLoja());
			Log.e(LOG," - data: "+ orc.getData());
			Log.e(LOG," - endereco: "+ orc.getEndereco());
			Log.e(LOG,"----------------------");
		}
	}
	
	
	
	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	 // Inflate the menu; this adds items to the action bar if it is present.
	 getMenuInflater().inflate(R.menu.orcamento, menu);
	 return true;
	 }

}
