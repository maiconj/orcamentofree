package com.orcamentofree;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.pojo.Produto;
import com.orcamentofree.pojo.UnidadeMedida;
import com.orcamentofree.utils.ImageUtils;
import com.orcamentofree.utils.MascaraMonetaria;
import com.orcamentofree.utils.SDCardUtils;

public class ProdutoActivity extends Activity{

	private Button btnSaveProduto;
	private Button btnDeleteProduto;
	private Button btnCancelProduto;
	private Button btnAddFotoProduto;
	
	private EditText txtProdutoCodigo;
	private EditText txtProdutoDescricao;
	private EditText txtProdutoPreco;
	private EditText txtProdutoQtd;	
	
	private Spinner umProduto;
	
	private ImageView imgProduto;
	private Intent intentCamera;
	
	private static final String LOG = "DESENV";
	private static final int MENU_SAVE_PRODUTO = 1;
	private static final int MENU_CANCEL_PRODUTO = 2;
	private static final int MENU_DELETE_PRODUTO = 3;
	private OrcamentoFreeDao dbHelp = null;
	private Orcamento orcamento;
	private Produto produto;
	private String unidadeMedida;
	private AlertDialog alertDeleteProduto;
	private File fotoFile;
	private boolean ADD_FOTO = false;	

	private static final String SAVE = "SAVE";
	private static final String DELETE = "DELETE";
	private static final String FIELDS_NULL = "FIELDS_NULL";
	private static final String PRODUTO_NULL = "PRODUTO_NULL";
	private final int DELAY = 300;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto);

		/** Inicializa componetes da tela **/
		carregaComponentes();

		/** Carrega PRODUTO selecionado**/
		carregaProduto();
		
		/** A��o do Spinner Unidade de Medida*/
		listenerUnidadeMedida();
		
		/** A��o do bot�o 'Salvar Orcamento' **/
		btnSaveProdutoAction();

		/** A��o do bot�o 'Excluir Orcamento' **/
		btnDeleteProdutoAction();

		/** A��o do bot�o 'Cancelar Orcamento' **/
		btnCancelProdutoAction();

		/** A��o do bot�o 'Adicionar Foto' **/ 
		btnAddFotoProdutoAction();
		
		this.imgProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				carregaFotoProduto();
				Toast t = new Toast(getApplicationContext());
				t.setView(imgProduto);
				t.show();
			}
		});
		

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
	
	private void btnAddFotoProdutoAction() {
		btnAddFotoProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addFotoProduto();
			}
		});
	}

	private void carregaComponentes() {
		this.btnSaveProduto = (Button) findViewById(R.id.btn_produto_save);
		this.btnDeleteProduto = (Button) findViewById(R.id.btn_produto_delete);
		this.btnCancelProduto = (Button) findViewById(R.id.btn_produto_cancel);
		this.btnAddFotoProduto = (Button) findViewById(R.id.btn_produto_foto);
		this.umProduto = (Spinner) findViewById(R.id.spn_um_produto);
		
		this.txtProdutoCodigo =  (EditText) findViewById(R.id.txt_produto_codigo);
		this.txtProdutoDescricao =  (EditText) findViewById(R.id.txt_produto_descricao);
		this.txtProdutoPreco =  (EditText) findViewById(R.id.txt_produto_preco);
		this.txtProdutoQtd =  (EditText) findViewById(R.id.txt_produto_qtd);
		
		carregaSpinnerUnidadeMedida();
		
		this.imgProduto = (ImageView) findViewById(R.id.img_produto); 
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
			carregaFotoProduto();
			this.umProduto.setSelection(UnidadeMedida.getId(this.produto.getUnidadeMedida()));
		} else if (it.getStringExtra("ID_ORCAMENTO_EDIT") != null) {
				this.orcamento = dbHelp.findOrcamentoById(Integer.valueOf(it.getStringExtra("ID_ORCAMENTO_EDIT")));
				this.produto = new Produto();
		}else{
			Toast.makeText(this,"Desculpe o transtorno, selecione um orcamento primeiro",Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	private void saveProduto() {
		boolean addProduto= false;
		try {
			if (this.produto.get_id() >= 1) {
				updateProduto();
			} else {
				newProduto();
				addProduto = true;
			}
			this.produto = dbHelp.findProdutoById(this.dbHelp.saveProduto(this.produto));
			//TODO adiciona foto
			if(this.ADD_FOTO && addProduto){
				//trocar nome arquivo
				SDCardUtils.remaneCardFile(this.fotoFile.getAbsolutePath(), this.fotoFile.getParent()+"/produto_"+this.produto.get_id()+".jpg");  
	            this.produto.setFoto("produto_"+this.produto.get_id()+".jpg");
	            this.produto = dbHelp.findProdutoById(this.dbHelp.saveProduto(this.produto));
			}	
			showMessage(SAVE);
			finish();
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}
	
	private void addFotoProduto(){
		try {
			if(this.produto.get_id()!=0 && this.produto.getFoto().equalsIgnoreCase("SEM_FOTO")){
				//produto salvo sem foto
				fotoFile=SDCardUtils.getSdCardFile("orcamentoFree", "produto_"+this.produto.get_id()+".jpg");
			}else if(this.produto.get_id()!=0 && !this.produto.getFoto().equalsIgnoreCase("SEM_FOTO")){
				//produto salvo com foto
				fotoFile=SDCardUtils.getSdCardFile("orcamentoFree", this.produto.getFoto());
			}else{
				//produto novo , sem/com foto
				fotoFile=SDCardUtils.getSdCardFile("orcamentoFree", "foto_produto_temp.jpg");
			}
			this.intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			this.intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,	Uri.fromFile(this.fotoFile));
			startActivityForResult(this.intentCamera, 0);			
		} catch (Exception e) {
			Log.e("DESENV", e.getMessage());
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
//			Log.e(LOG," - lt: "+ prod.getQuantidade());
//			Log.e(LOG," - id_orcamento: "+ prod.get_idOrcamento());
//			Log.e(LOG,"----------------------");
//		}
//	}
	
	private void deleteProduto() {
		try {
			SDCardUtils.deleteCardFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/orcamentoFree/"+this.produto.getFoto());
			this.dbHelp.deleteProdutoById(this.produto.get_id());
			this.produto = new Produto();
			limpaCampos();			
			showMessage(DELETE);
			finish();
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}
	
	
	private void confirmaProdutoSelecionado() {
		if (this.produto.get_id() >= 1) {
			exibeMensagemDelete();
		} else {
			showMessage(PRODUTO_NULL);			
		}
	}
	
	private void exibeMensagemDelete(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_action_error_white);
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
			this.produto.setUnidadeMedida(unidadeMedida);
			//TODO
//			this.produto.setPreco(Float.valueOf(new MascaraMonetaria().replaceField(this.txtProdutoPreco.getText().toString())));
			
			
			//TODO adiciona foto
			if(this.ADD_FOTO){
				//trocar nome arquivo
				SDCardUtils.remaneCardFile(this.fotoFile.getAbsolutePath(), this.fotoFile.getParent()+"/produto_"+this.produto.get_id()+".jpg");  
	            this.produto.setFoto("produto_"+this.produto.get_id()+".jpg");
			}else if(!this.ADD_FOTO && !this.produto.getFoto().equalsIgnoreCase("SEM_FOTO")){
				this.produto.setFoto("SEM_FOTO");
			}
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
			this.produto.setFoto("SEM_FOTO");
			this.produto.setUnidadeMedida(unidadeMedida);
		} catch (Exception e) {
			Log.e(LOG,e.getMessage());
		}
	}
	
	private void carregaFotoProduto() {
		// TODO
		// carregar foto
		fotoFile = SDCardUtils.getSdCardFile("orcamentoFree", this.produto.getFoto());
		if (fotoFile != null) {
			Bitmap bitmap = ImageUtils.getResizedImage( Uri.fromFile(this.fotoFile), 150, 150);
			this.imgProduto.setImageBitmap(bitmap);
		}
	}
	
	//Spinner
	private void listenerUnidadeMedida() {
		umProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int posicao, long arg3) {
				addUnidadeMedidaProduto(UnidadeMedida.getSigla(posicao));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	private void addUnidadeMedidaProduto(String um){
		this.unidadeMedida = um;
	}
	
	
	private void carregaSpinnerUnidadeMedida(){
		ArrayAdapter<String> adapterUmProduto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UnidadeMedida.getValues());
		adapterUmProduto.setDropDownViewResource(R.layout.unidade_medida_spinner_list);
		umProduto.setAdapter(adapterUmProduto);
	}
	
	public boolean camposValidos() {
		boolean erro = true;
		if (this.txtProdutoDescricao.getText().toString().length()<=0 
				|| this.txtProdutoPreco.getText().toString().length()<=0
				|| this.txtProdutoQtd.getText().toString().length()<=0) {
			showMessage(FIELDS_NULL);
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

	
	private void showMessage(String typeMsg) {
		View view = null;
		LayoutInflater inflater = getLayoutInflater();

		if (typeMsg.compareTo(SAVE) == 0) {
			view = inflater.inflate(R.layout.msg_produto_save, (ViewGroup) findViewById(R.id.produtoSaveLayout));
		} else if (typeMsg.compareTo(DELETE) == 0) {
			view = inflater.inflate(R.layout.msg_produto_delete,	(ViewGroup) findViewById(R.id.produtoDeleteLayout));
		} else if (typeMsg.compareTo(FIELDS_NULL) == 0) {
			view = inflater.inflate(R.layout.msg_produto_campos_null,(ViewGroup) findViewById(R.id.produtoFieldsNullLayout));
		} else if (typeMsg.compareTo(PRODUTO_NULL) == 0) {
			view = inflater.inflate(R.layout.msg_produto_not_selected, (ViewGroup) findViewById(R.id.produtoFieldsNullLayout));
		}
		Toast toast = new Toast(this);
		toast.setView(view);
		toast.setDuration(this.DELAY);
		toast.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, MENU_SAVE_PRODUTO, 0, "Salvar").setIcon(R.drawable.ic_action_save);
		item = menu.add(0, MENU_CANCEL_PRODUTO, 0, "Voltar").setIcon(R.drawable.ic_action_undo);
		item = menu.add(0, MENU_DELETE_PRODUTO, 0, "Excluir").setIcon(R.drawable.ic_action_discard);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE_PRODUTO:
			saveProduto();
			return true;
		case MENU_CANCEL_PRODUTO:
			finish();
			return true;
		case MENU_DELETE_PRODUTO:
			confirmaProdutoSelecionado();	
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				Bitmap bitmap = ImageUtils.getResizedImage(Uri.fromFile(fotoFile),	150, 150);
				this.imgProduto.setImageBitmap(bitmap);
				this.ADD_FOTO = true;
			}
		} catch (Exception e) {
			Log.e("DESENV", e.getMessage());
		}
	}
}
