package com.orcamentofree.listAdapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orcamentofree.pojo.Produto;

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
			TextView produto_descricao = (TextView) view.findViewById(com.orcamentofree.R.id.produto_descricao);
			TextView produto_codigo = (TextView) view.findViewById(com.orcamentofree.R.id.produto_codigo);
			TextView produto_quantidade = (TextView) view.findViewById(com.orcamentofree.R.id.produto_quantidade);
			TextView produto_preco = (TextView) view.findViewById(com.orcamentofree.R.id.produto_preco);

			produto_descricao.setText(produto.getDescricao());
			produto_codigo.setText(produto.getCodigo());
			produto_quantidade.setText(String.valueOf(produto.getQuantidade()));
			produto_preco.setText(String.valueOf(produto.getPreco()));
			
			return view;
		} catch (Exception e) {
			Log.e("DESENV", e.getMessage());
		}
		return null;
	}

}
