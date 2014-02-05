package com.orcamentofree.listAdapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orcamentofree.R;
import com.orcamentofree.pojo.Produto;
import com.orcamentofree.utils.ImageUtils;
import com.orcamentofree.utils.SDCardUtils;

public class ProdutoListAdapter extends BaseAdapter {

	private Context context;
	private List<Produto> produtolst;

	public ProdutoListAdapter(Context contex, List<Produto> lista) {
		this.context = contex;
		this.produtolst = lista;
	}

	@Override
	public int getCount() {
		return produtolst.size();
	}

	@Override
	public Object getItem(int position) {
		return produtolst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			Produto produto = produtolst.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(com.orcamentofree.R.layout.produto_line_view, null);
			TextView produto_descricao = (TextView) view.findViewById(R.id.produto_descricao);
			TextView produto_codigo = (TextView) view.findViewById(R.id.produto_codigo);
			TextView produto_quantidade = (TextView) view.findViewById(R.id.produto_quantidade);
			TextView produto_preco = (TextView) view.findViewById(R.id.produto_preco);
			TextView produto_total = (TextView) view.findViewById(R.id.produto_total);
			ImageView produto_foto = (ImageView) view.findViewById(R.id.img_produto_line); 
			
			
			produto_descricao.setText(produto.getDescricao());
			produto_codigo.setText(produto.getCodigo());
			produto_quantidade.setText("Qtde: " + String.valueOf(produto.getQuantidade()) +" " + produto.getUnidadeMedida());
			produto_preco.setText("Preço: R$ " + String.valueOf(produto.getPreco()));
			produto_total.setText("Total: R$ " + String.valueOf(produto.getPreco() * produto.getQuantidade()));
			
			
			
			if(produto.getFoto().compareTo("SEM_FOTO") == 0){
				produto_foto.setImageResource(R.drawable.ic_action_no_camera);
			}else{
				File fotoFile=SDCardUtils.getSdCardFile("orcamentoFree", produto.getFoto());
				Bitmap bitmap = ImageUtils.getResizedImage(Uri.fromFile(fotoFile),	45, 45);
				produto_foto.setImageBitmap(bitmap);
			}
			
			return view;
		} catch (Exception e) {
			Log.e("DESENV", e.getMessage());
		}
		return null;
	}

}
