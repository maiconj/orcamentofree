package com.orcamentofree;

//import com.orcamentofree.base.OrcamentoDataBaseHelper;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.listAdapter.ProdutoListAdapter;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.pojo.Produto;
import com.orcamentofree.utils.DateUtils;

public class OrcamentoActivity extends Activity  implements OnItemClickListener {

	ListView listView;
	private Button btnProdutoAdd;
	private Button btnOrcamentoSave;
	private Button btnOrcamentoDelete;
	private Button btnOrcamentoCancel;

	private EditText txtOrcamentoDescricao;
	private EditText txtOrcamentoLoja;
	private EditText txtOrcamentoEndereco;

	private Orcamento orcamento;
	private DateUtils dateUtils;
	private AlertDialog alertDeleteOrcamento;

	private OrcamentoFreeDao dbHelp = null;
	private Intent intentProduto;
	private static final String LOG = "DESENV";
	private static final int MENU_SAVE_ORCAMENTO = 1;
	private static final int MENU_CANCEL_ORCAMENTO = 2;
	private static final int MENU_DELETE_ORCAMENTO = 3;
	private static final int ORCAMENTO_ADD_PRODUTO = 1;
	private static final int ORCAMENTO_EDIT_PRODUTO = 2;
	
	private static final String SAVE = "SAVE";
	private static final String DELETE = "DELETE";
	private static final String FIELDS_NULL = "FIELDS_NULL";
	private static final String ORCAMENTO_NULL = "ORC_NULL";
	private final int DELAY = 300;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_orcamento);

			/** Inicializa componetes da tela **/
			carregaComponentes();

			/** Carrega ORCAMENTO selecionado na listview**/
			CarregaOrcamentoEdit();
			
			/** Carrega Lista de produtos **/
			atualizaListaProdutos();
			
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
				confirmaOrcamentoSelecionado();
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
				// salva o orcamento antes de mudar para tela de produto
				if (camposValidos()) {
					saveBeforeProdutoOrcamento();
					initProduto(ORCAMENTO_ADD_PRODUTO);
				}
			}
		});
	}

	private void initProduto(int requestCode){
		this.intentProduto = new Intent(this, ProdutoActivity.class);
		intentProduto.putExtra("ID_ORCAMENTO_EDIT", String.valueOf(this.orcamento.get_id()));
		startActivityForResult(intentProduto, requestCode);
	}

	private void carregaComponentes() {
		this.dbHelp = new OrcamentoFreeDao(getApplicationContext());
		this.btnProdutoAdd = (Button) findViewById(R.id.btn_produto_add);
		this.btnOrcamentoSave = (Button) findViewById(R.id.btn_orcamento_save);
		this.btnOrcamentoDelete = (Button) findViewById(R.id.btn_orcamento_delete);
		this.btnOrcamentoCancel = (Button) findViewById(R.id.btn_orcamento_cancel);
		this.txtOrcamentoDescricao = (EditText) findViewById(R.id.txt_orcamento_descricao);
		this.txtOrcamentoLoja = (EditText) findViewById(R.id.txt_orcamento_loja);
		this.txtOrcamentoEndereco = (EditText) findViewById(R.id.txt_orcamento_endereco);
		this.dateUtils = new DateUtils();
		this.intentProduto = new Intent(this, ProdutoActivity.class);
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
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
		showMessage(SAVE);
	}
	
	private void showMessage(String typeMsg) {
		View view = null;
		LayoutInflater inflater = getLayoutInflater();

		if (typeMsg.compareTo(SAVE) == 0) {
			view = inflater.inflate(R.layout.msg_orcamento_save, (ViewGroup) findViewById(R.id.orcamentoSaveLayout));
		} else if (typeMsg.compareTo(DELETE) == 0) {
			view = inflater.inflate(R.layout.msg_orcamento_delete,	(ViewGroup) findViewById(R.id.orcamentoDeleteLayout));
		} else if (typeMsg.compareTo(FIELDS_NULL) == 0) {
			view = inflater.inflate(R.layout.msg_orcamento_campos_null,(ViewGroup) findViewById(R.id.orcamentoFieldsNullLayout));
		} else if (typeMsg.compareTo(ORCAMENTO_NULL) == 0) {
			view = inflater.inflate(R.layout.msg_orcamento_not_selected, (ViewGroup) findViewById(R.id.orcamentoFieldsNullLayout));
		}
		Toast toast = new Toast(this);
		toast.setView(view);
//		toast.setDuration(this.DELAY);
		toast.show();
	}
	
	/**Metodo usado para salvar o Orcamento quando o usuario clicar no botão "Adicionar Produto'*/
	private void saveBeforeProdutoOrcamento() {
		try {
			if (this.orcamento.get_id() >= 1) {
				updateOrcamento();
			} else {
				newOrcamento();
			}
			this.orcamento = dbHelp.findOrcamentoById(this.dbHelp.saveOrcamento(this.orcamento));
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
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
			this.dbHelp.deleteOrcamentoByIdAndProdutos(this.orcamento.get_id());
			this.orcamento = new Orcamento();
			limpaCampos();
			atualizaListaProdutos();
			showMessage(DELETE);
			finish();
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}

	private void confirmaOrcamentoSelecionado() {
		if (this.orcamento.get_id() >= 1) {
			exibeMensagemDelete();
		} else {
			showMessage(ORCAMENTO_NULL);
		}
	}

	private void exibeMensagemDelete(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_action_error_white);
        builder.setTitle("Excluir Orçamento");
        builder.setMessage("Isto irá deletar todos os produtos associados.\nVocê tem certeza?");
    
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
               deleteOrcamento();
            }
        });
       
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	Log.i(LOG, "Orcamento não deletado.");
            }
        });
        alertDeleteOrcamento = builder.create();
        alertDeleteOrcamento.show();
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
			showMessage(FIELDS_NULL);
			erro = false;
		}
		return erro;
	}

	//TODO
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
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.orcamento, menu);
//		return true;
//	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, MENU_SAVE_ORCAMENTO, 0, "Salvar").setIcon(R.drawable.ic_action_save);
		item = menu.add(0, MENU_CANCEL_ORCAMENTO, 0, "Voltar").setIcon(R.drawable.ic_action_undo);
		item = menu.add(0, MENU_DELETE_ORCAMENTO, 0, "Excluir").setIcon(R.drawable.ic_action_discard);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE_ORCAMENTO:
			if (camposValidos()) {
				saveOrcamento();
			}
			return true;
		case MENU_CANCEL_ORCAMENTO:
			finish();
			return true;
		case MENU_DELETE_ORCAMENTO:
			confirmaOrcamentoSelecionado();
			return true;
		}
		return false;
	}		
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		atualizaListaProdutos();
	}
	
	public void atualizaListaProdutos() {
		try {
			ArrayList<Produto> produtoLst = new ArrayList<Produto>();
			produtoLst.clear();
			produtoLst.addAll(dbHelp.findProdutoByOrcamento(this.orcamento));
			ProdutoListAdapter produtoAdapter = new ProdutoListAdapter(this, produtoLst);
			listView = (ListView) findViewById(R.id.produtoList);
			listView.setAdapter(produtoAdapter);
			listView.setOnItemClickListener(this);
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Produto  produto = (Produto) listView.getAdapter().getItem(arg2);
		this.intentProduto = new Intent(this, ProdutoActivity.class);
		intentProduto.putExtra("ID_PRODUTO_EDIT", String.valueOf(produto.get_id()));
		startActivityForResult(intentProduto, ORCAMENTO_EDIT_PRODUTO);
	}

}
