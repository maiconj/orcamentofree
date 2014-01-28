package com.orcamentofree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.pojo.Produto;
import com.orcamentofree.utils.MascaraMonetaria;

public class ProdutoActivity extends Activity{

	private Button btnSaveProduto;
	private Button btnDeleteProduto;
	private Button btnCancelProduto;
	
	private EditText txtProdutoCodigo;
	private EditText txtProdutoDescricao;
	private EditText txtProdutoPreco;
	private EditText txtProdutoQtd;	
	
	private static final String LOG = "DESENV";
	private static final int MENU_SAVE_PRODUTO = 1;
	private static final int MENU_CANCEL_PRODUTO = 2;
	private static final int MENU_DELETE_PRODUTO = 3;
	private OrcamentoFreeDao dbHelp = null;
	private Orcamento orcamento;
	private Produto produto;
	private AlertDialog alertDeleteProduto;
	private final int DELAY = 800;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto);

		/** Inicializa componetes da tela **/
		carregaComponentes();

		/** Carrega PRODUTO selecionado**/
		carregaProduto();
		
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
				confirmaProdutoSelecionado();				
			}
		});
	}

	private void btnSaveProdutoAction() {
		btnSaveProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (camposValidos()) {
					saveProduto();
				}
			}

		});
	}

	private void carregaComponentes() {
		this.btnSaveProduto = (Button) findViewById(R.id.btn_produto_save);
		this.btnDeleteProduto = (Button) findViewById(R.id.btn_produto_delete);
		this.btnCancelProduto = (Button) findViewById(R.id.btn_produto_cancel);
		
		this.txtProdutoCodigo =  (EditText) findViewById(R.id.txt_produto_codigo);
		this.txtProdutoDescricao =  (EditText) findViewById(R.id.txt_produto_descricao);
		this.txtProdutoPreco =  (EditText) findViewById(R.id.txt_produto_preco);
		this.txtProdutoQtd =  (EditText) findViewById(R.id.txt_produto_qtd);
		
		this.dbHelp = new OrcamentoFreeDao(getApplicationContext());
		//add mascara
		//TODO
		//txtProdutoPreco.addTextChangedListener(new MascaraMonetaria(txtProdutoPreco));
		
	}

	private void carregaProduto() {
		Intent it = getIntent();
		if (it.getStringExtra("ID_PRODUTO_EDIT") != null) {
			this.produto = dbHelp.findProdutoById(Integer.valueOf(it.getStringExtra("ID_PRODUTO_EDIT")));
			this.orcamento = dbHelp.findOrcamentoById(Integer.valueOf(this.produto.get_idOrcamento()));
			this.txtProdutoCodigo.setText(this.produto.getCodigo());
			this.txtProdutoDescricao.setText(this.produto.getDescricao());
			this.txtProdutoPreco.setText(this.produto.getPreco().toString());
			this.txtProdutoQtd.setText(this.produto.getQuantidade().toString());
		} else if (it.getStringExtra("ID_ORCAMENTO_EDIT") != null) {
				this.orcamento = dbHelp.findOrcamentoById(Integer.valueOf(it.getStringExtra("ID_ORCAMENTO_EDIT")));
				this.produto = new Produto();
		}else{
			Toast.makeText(this,"Desculpe o transtorno, selecione um orcamento primeiro",Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	private void saveProduto() {
		try {
			if (this.produto.get_id() >= 1) {
				updateProduto();
			} else {
				newProduto();
			}
			this.produto = dbHelp.findProdutoById(this.dbHelp.saveProduto(this.produto));
			Toast.makeText(this, "Produto Salvo com Sucesso", this.DELAY).show();
			finish();
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}

	//TODO
//	private void imprimeProdutos(){
//		ArrayList<Produto> produtoLst = (ArrayList<Produto>) this.dbHelp.findProduto();
//		Log.e(LOG,"numero produtos salvos: " + produtoLst.size());
//		for (Produto prod : produtoLst) {
//			Log.e(LOG," - _id: "+ prod.get_id());
//			Log.e(LOG," - codigo: "+ prod.getCodigo());
//			Log.e(LOG," - descricao: "+ prod.getDescricao());
//			Log.e(LOG," - preco: "+ prod.getPreco());
//			Log.e(LOG," - qtd: "+ prod.getQuantidade());
//			Log.e(LOG," - id_orcamento: "+ prod.get_idOrcamento());
//			Log.e(LOG,"----------------------");
//		}
//	}
	
	private void deleteProduto() {
		try {
			this.dbHelp.deleteProdutoById(this.produto.get_id());
			this.produto = new Produto();
			limpaCampos();
			Toast.makeText(this, "Produto deletado com sucesso!",	this.DELAY).show();
			finish();
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}
	
	
	private void confirmaProdutoSelecionado() {
		if (this.produto.get_id() >= 1) {
			exibeMensagemDelete();
		} else {
			Toast.makeText(this, "Voc� deve selecionar um Produto!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void exibeMensagemDelete(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir Produto");
        builder.setMessage("Isto ir� excluir o produto selecionado.\nVoc� tem certeza?");
    
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	deleteProduto();
            }
        });
       
        builder.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	Log.i(LOG, "Produto nao deletado.");
            }
        });
        alertDeleteProduto = builder.create();
        alertDeleteProduto.show();
	}	


	private void cancelProduto() {
		finish();
	}
	
	/**
	 * Carrega os campos no PRODUTO para fazer update
	 * **/
	private void updateProduto() {
		try {
			this.produto.setCodigo(this.txtProdutoCodigo.getText().toString());
			this.produto.setDescricao(this.txtProdutoDescricao.getText().toString());
			this.produto.setQuantidade(Float.valueOf(this.txtProdutoQtd.getText().toString()));
			this.produto.setPreco(Float.valueOf(this.txtProdutoPreco.getText().toString()));
			//TODO
//			this.produto.setPreco(Float.valueOf(new MascaraMonetaria().replaceField(this.txtProdutoPreco.getText().toString())));
			this.produto.setFoto("FOTO TESTE");
		} catch (Exception e) {
			Log.e(LOG,e.getMessage());
		}
	}
	

	/**
	 * Cria novo objeto de PRODUTO carregando os campos para salvar
	 **/
	private void newProduto() {
		try {
			this.produto = new Produto();
			this.produto.setCodigo(this.txtProdutoCodigo.getText().toString());
			this.produto.setDescricao(this.txtProdutoDescricao.getText().toString());
			this.produto.setQuantidade(Float.valueOf(this.txtProdutoQtd.getText().toString()));
			this.produto.setPreco(Float.valueOf(new MascaraMonetaria().replaceField(this.txtProdutoPreco.getText().toString())));
			this.produto.set_idOrcamento(this.orcamento.get_id());
			//TODO
			this.produto.setFoto("FOTO TESTE");
			
		} catch (Exception e) {
			Log.e(LOG,e.getMessage());
		}
	}
	
	public boolean camposValidos() {
		boolean erro = true;
		if (this.txtProdutoDescricao.getText().toString().length()<=0 
				|| this.txtProdutoPreco.getText().toString().length()<=0
				|| this.txtProdutoQtd.getText().toString().length()<=0) {
			Toast.makeText(this, "Opera��o cancelada, preencha os campos!",	Toast.LENGTH_LONG).show();
			erro = false;
		}
		return erro;
	}


	private void limpaCampos() {
		this.txtProdutoCodigo.setText("");
		this.txtProdutoDescricao.setText("");
		this.txtProdutoQtd.setText("");
		this.txtProdutoPreco.setText("");
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
