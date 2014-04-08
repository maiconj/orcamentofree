package com.orcamentofree.listAdapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.pojo.Produto;

public class OrcamentoListAdapter extends BaseAdapter {

	private Context context;
	private List<Orcamento> orcamentolst;
	private OrcamentoFreeDao dbHelp = null;

	public OrcamentoListAdapter(Context contex, List<Orcamento> lista, Context applicationContext) {
		this.context = contex;
		this.orcamentolst = lista;
		this.dbHelp  = new OrcamentoFreeDao(applicationContext);
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

	@SuppressLint("SimpleDateFormat")
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
			TextView orcamento_total = (TextView) view.findViewById(com.orcamentofree.R.id.orcamento_total);
					
			orcamento_descricao.setText(orcmnt.getDescricao());
			orcamento_loja.setText("Loja: " + orcmnt.getLoja());
			orcamento_endereco.setText("Endereço: " + orcmnt.getEndereco());			
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");  
			Date date = (Date)dateFormat.parse(orcmnt.getData());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy - EEE");
			orcamento_data.setText(formatter.format(date));
			
			orcamento_total.setText("R$: "+(calculaTotal(orcmnt)));			
			
			return view;
		}catch(Exception e){
			Log.e("DESENV", e.getMessage());			
		}
		return null;
	}
	
	private String calculaTotal(Orcamento orc){
		ArrayList<Produto> produtoLst  = new ArrayList<Produto>(); 
		produtoLst.addAll(dbHelp.findProdutoByOrcamento(orc));
		BigDecimal totalOrcamento = new BigDecimal(BigInteger.ZERO);
		
		if(produtoLst.size()>0){
			for(Produto p : produtoLst){
				totalOrcamento = totalOrcamento.add(p.getTotal());
			}
		}
		return  totalOrcamento.setScale(2, RoundingMode.HALF_UP).toString();		
	}
}
