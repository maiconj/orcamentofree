package com.orcamentofree;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.listAdapter.OrcamentoListAdapter;
import com.orcamentofree.pojo.Orcamento;

public class MainActivity extends Activity implements OnItemClickListener {

	ListView listView;
	private Button addOrcamentoBtn;
	private Intent intentOrcamento;
	private OrcamentoFreeDao dbHelp = null;
	private static final String LOG = "DESENV";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			carregaComponentes();

			/** Carrega Lista de orcamentos **/
			carregaListaOrcamentos();
			
			/** Ação do botão 'Adiconar Produto' **/
			btnOrcamentoAddAction();

		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}

	public void carregaComponentes() {
		this.addOrcamentoBtn = (Button) findViewById(R.id.btn_orcamento_add);
		this.intentOrcamento = new Intent(this, OrcamentoActivity.class);
		dbHelp = new OrcamentoFreeDao(getApplicationContext());
	}
	
	public void carregaListaOrcamentos() {
		ArrayList<Orcamento> orcamentoLst = new ArrayList<Orcamento>();
		orcamentoLst.clear();
		orcamentoLst.addAll(dbHelp.findOrcamento());
		OrcamentoListAdapter orcamentoAdapter = new OrcamentoListAdapter(this, orcamentoLst);
		listView = (ListView) findViewById(R.id.orcamentoList);
		listView.setAdapter(orcamentoAdapter);
		listView.setOnItemClickListener(this);
	}

	public void btnOrcamentoAddAction() {
		addOrcamentoBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(intentOrcamento);
			}
		});
	}

	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Orcamento orc = (Orcamento) listView.getAdapter().getItem(arg2);
		Toast.makeText(this, "Orcamento Selecionado- id: "+ orc.get_id()+" - descricao: "+orc.getDescricao(), Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
