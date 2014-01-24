package com.orcamentofree.listAdapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orcamentofree.pojo.Orcamento;

public class OrcamentoListAdapter extends BaseAdapter {

	private Context context;
	private List<Orcamento> orcamentolst;

	public OrcamentoListAdapter(Context contex, List<Orcamento> lista) {
		this.context = contex;
		this.orcamentolst = lista;
	}

	@Override
	public int getCount() {
		return orcamentolst.size();
	}

	@Override
	public Object getItem(int position) {
		return orcamentolst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			Orcamento orcmnt = orcamentolst.get(position);
			LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(com.orcamentofree.R.layout.orcamento_line_view, null);
			
			TextView orcamento_descricao = (TextView) view.findViewById(com.orcamentofree.R.id.orcamento_descricao);
			TextView orcamento_loja = (TextView) view.findViewById(com.orcamentofree.R.id.orcamento_loja);
			TextView orcamento_endereco = (TextView) view.findViewById(com.orcamentofree.R.id.orcamento_endereco);
			TextView orcamento_data = (TextView) view.findViewById(com.orcamentofree.R.id.orcamento_data);
					
			orcamento_descricao.setText("Desc.: " + orcmnt.getDescricao());
			orcamento_loja.setText("Loja: " + orcmnt.getLoja());
			orcamento_endereco.setText("Endere�o: " + orcmnt.getEndereco());
			orcamento_data.setText(orcmnt.getData());
			
			return view;
		}catch(Exception e){
			Log.e("DESENV", e.getMessage());			
		}
		return null;
	}

}
