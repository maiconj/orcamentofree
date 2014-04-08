package com.orcamentofree;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
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
import com.orcamentofree.utils.MascaraQtde;
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
	private EditText txtProdutoTotal;	
	
	
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
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_produto);
	
			/** Inicializa componetes da tela **/
			carregaComponentes();
	
			/** Carrega PRODUTO selecionado**/
			carregaProduto();
			
			/** Ação do Spinner Unidade de Medida*/
			listenerUnidadeMedida();
		
			/** Ação do Spinner Unidade de Medida*/
			listenerTotalProdutoPreco();
			
			/** Ação do botão 'Salvar Orcamento' **/
			btnSaveProdutoAction();
	
			/** Ação do botão 'Excluir Orcamento' **/
			btnDeleteProdutoAction();
	
			/** Ação do botão 'Cancelar Orcamento' **/
			btnCancelProdutoAction();
	
			/** Ação do botão 'Adicionar Foto' **/ 
			btnAddFotoProdutoAction();
			
			/**
			 * Ação de clicar na imagem do produto , a a activity
			 * ProdutoFotoActivity que mostra o imagen do produto ampliada
			 */
			listenerProdutoFoto();
		}catch(Exception e ){
			Log.e(LOG,e.getMessage());
		}
		
		
	}
	
	private void btnCancelProdutoAction() {
		btnCancelProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.w("btnCancelOrcamento", "Aciondo botão de cancelar produto.");
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
		this.txtProdutoTotal =  (EditText) findViewById(R.id.txt_produto_total);
		
		carregaSpinnerUnidadeMedida();
		
		this.imgProduto = (ImageView) findViewById(R.id.img_produto); 
		this.dbHelp = new OrcamentoFreeDao(getApplicationContext());
		txtProdutoPreco.addTextChangedListener(new MascaraMonetaria(txtProdutoPreco));
		txtProdutoQtd.addTextChangedListener(new MascaraQtde(txtProdutoQtd));
	}
	
	private void carregaProduto() {
		Intent it = getIntent();
		if (it.getStringExtra("ID_PRODUTO_EDIT") != null) {
			this.produto = dbHelp.findProdutoById(Integer.valueOf(it.getStringExtra("ID_PRODUTO_EDIT")));
			this.orcamento = dbHelp.findOrcamentoById(Integer.valueOf(this.produto.get_idOrcamento()));
			this.txtProdutoCodigo.setText(this.produto.getCodigo());
			this.txtProdutoDescricao.setText(this.produto.getDescricao());
			this.txtProdutoPreco.setText(Float.valueOf((this.produto.getPreco() * 100)).toString());
			this.txtProdutoQtd.setText(Float.valueOf(this.produto.getQuantidade() * 10).toString());
			this.txtProdutoTotal.setText(calculaTotal());
			this.umProduto.setSelection(UnidadeMedida.getId(this.produto.getUnidadeMedida()));
			carregaFotoProduto();
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
			
			//TODO SALVAR DIRETORIO FOTO TEMPORARIA
//			InternalStorage.salvarInternalStorage(fotoFile.getAbsolutePath().toString(), getFilesDir());
//			Log.e(LOG, InternalStorage.acessaInternalStorage(getFilesDir()));
//			Log.e(LOG, String.valueOf(InternalStorage.deleteInternalStorageFile(getFilesDir())));
//			
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}
	
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
        builder.setMessage("Isto irá excluir o produto selecionado.\nVocê tem certeza?");
    
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	deleteProduto();
            }
        });
       
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
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
			this.produto.setQuantidade(Float.valueOf(new MascaraQtde().replaceField(this.txtProdutoQtd.getText().toString())));
			this.produto.setPreco(Float.valueOf(new MascaraMonetaria().replaceField(this.txtProdutoPreco.getText().toString())));
			this.produto.setUnidadeMedida(unidadeMedida);
			
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
			this.produto.setQuantidade(Float.valueOf(new MascaraQtde().replaceField(this.txtProdutoQtd.getText().toString())));
			this.produto.setPreco(Float.valueOf(new MascaraMonetaria().replaceField(this.txtProdutoPreco.getText().toString())));
			this.produto.set_idOrcamento(this.orcamento.get_id());		
			this.produto.setFoto("SEM_FOTO");
			this.produto.setUnidadeMedida(unidadeMedida);
		} catch (Exception e) {
			Log.e(LOG,e.getMessage());
		}
	}
	
	private void carregaFotoProduto() {
		try {
			if (this.produto.getFoto().compareTo("SEM_FOTO") != 0) {
				fotoFile = SDCardUtils.getSdCardFile("orcamentoFree", this.produto.getFoto());
				Bitmap myBitmap = BitmapFactory.decodeFile(fotoFile.getAbsolutePath());
				if (fotoFile != null) {
					Bitmap bitmap = ImageUtils.getResizedImage( Uri.fromFile(fotoFile), 200 , 200); 
					//TODO
					//myBitmap.getWidth() / 2, myBitmap.getHeight() / 2);
					this.imgProduto.setImageBitmap(bitmap);
				}
			}
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}

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
	
	private void listenerTotalProdutoPreco(){
		this.txtProdutoQtd.setOnFocusChangeListener(new OnFocusChangeListener() {          
		    public void onFocusChange(View v, boolean hasFocus) {
		        if(!hasFocus) {
		           calculaTotalListener();
		        }
		    }
		});

		this.txtProdutoPreco.setOnFocusChangeListener(new OnFocusChangeListener() {          
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					calculaTotalListener();
				}
			}
		});
	}
	
	private void calculaTotalListener() {
		try{
			if (!this.txtProdutoPreco.getText().toString().isEmpty() && !this.txtProdutoQtd.getText().toString().isEmpty()) {
				BigDecimal qtde = new BigDecimal(new MascaraQtde().replaceField(this.txtProdutoQtd.getText().toString()));
				BigDecimal preco = new BigDecimal(new MascaraMonetaria().replaceField(this.txtProdutoPreco.getText().toString()));
				this.txtProdutoTotal.setText("R$: " + String.valueOf(qtde.multiply(preco).setScale(2,RoundingMode.HALF_UP)));
			}else{
				this.txtProdutoTotal.setText("R$: Total");
			}
		}catch(Exception e ){
			Log.e(LOG,e.getMessage());
		}
	}

	private String calculaTotal() {
		String total = null;
		try{
			if (!this.produto.getQuantidade().toString().isEmpty() && !this.produto.getPreco().toString().isEmpty()) {
				BigDecimal qtde = new BigDecimal(this.produto.getQuantidade());
				BigDecimal preco = new BigDecimal(this.produto.getPreco());
				total = String.valueOf("R$: " + qtde.multiply(preco).setScale(2,RoundingMode.HALF_UP));
			}
		}catch(Exception e ){
			Log.e(LOG,e.getMessage());
		}
		return total;
	}
	
	
	private void listenerProdutoFoto(){
		this.imgProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					initProdutoFoto();
			}
		});
	}
	
	private void initProdutoFoto(){
		Intent intentFoto = new Intent(this, ProdutoFotoActivity.class);
		intentFoto.putExtra("ID_PRODUTO", String.valueOf(this.produto.get_id()));
		intentFoto.putExtra("FOTO_FILE", fotoFile.getAbsolutePath().toString());
		startActivity(intentFoto);		
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
//		toast.setDuration(this.DELAY);
		toast.show();
	}
	
	@Override
	@SuppressWarnings("unused")
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
			if (camposValidos()) {
				saveProduto();
			}
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
				Bitmap myBitmap = BitmapFactory.decodeFile(fotoFile.getAbsolutePath());
				Bitmap bitmap = ImageUtils.getResizedImage(Uri.fromFile(fotoFile),	200,200);
				//TODO
				//myBitmap.getWidth()/2, myBitmap.getHeight()/2);
				this.imgProduto.setImageBitmap(bitmap);
				this.ADD_FOTO = true;
			}
		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}
}
