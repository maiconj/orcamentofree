package com.orcamentofree;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.pojo.Produto;
import com.orcamentofree.utils.ImageUtils;
import com.orcamentofree.utils.SDCardUtils;

public class ProdutoFotoActivity extends Activity {

	private Produto produto;
	private ImageView imgFotoProduto;
	private OrcamentoFreeDao dbHelp = null;
	private static final int MENU_BACK_PRODUTO = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto_foto);
		carregaComponentes();
		carregaProduto();
		carregaFotoProduto();
		listenerProdutoFoto();	
	}

	private void carregaComponentes() {
		this.dbHelp = new OrcamentoFreeDao(getApplicationContext());
		this.imgFotoProduto = (ImageView) findViewById(R.id.produto_foto);
	}

	private void carregaProduto() {
		Intent it = getIntent();
		if (it.getStringExtra("ID_PRODUTO") != null) {
			this.produto = dbHelp.findProdutoById(Integer.valueOf(it
					.getStringExtra("ID_PRODUTO")));
		} else {
			finish();
		}
	}

	private void carregaFotoProduto() {
		File imgFile =SDCardUtils.getSdCardFile("orcamentoFree", this.produto.getFoto());
		if (imgFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());			
			this.imgFotoProduto.setImageBitmap(ImageUtils.getResizedImage( Uri.fromFile(imgFile), myBitmap.getWidth(), myBitmap.getHeight()));
		} else {
			showMessage();
			finish();
		}
	}
	
	private void listenerProdutoFoto(){
		this.imgFotoProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					finish();
			}
		});
	}

	private void showMessage() {
		View view = null;
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.msg_produto_foto_not_selected, (ViewGroup) findViewById(R.id.produtoFotoNotSelected));
		Toast toast = new Toast(this);
		toast.setView(view);
		toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, MENU_BACK_PRODUTO, 0, "Voltar").setIcon(R.drawable.ic_action_undo);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_BACK_PRODUTO:
			finish();
			return true;
		}
		return false;
	}

}
