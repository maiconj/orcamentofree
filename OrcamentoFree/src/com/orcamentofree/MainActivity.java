package com.orcamentofree;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.listAdapter.OrcamentoListAdapter;
import com.orcamentofree.pojo.Orcamento;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView listView;
	private Button addOrcamentoBtn;
	private Intent intentOrcamento;
	private Intent intentOrcamentoHelp;
	private OrcamentoFreeDao dbHelp = null;
	private static final String LOG = "DESENV";
	private static final int ADD_ORCAMENTO = 0;
	private static final int EDIT_ORCAMENTO = 1;
	private static final int DELET_ORCAMENTO = 2;
	private static final int SAVE_ORCAMENTO = 3;

	private static final int MENU_ADD_ORCAMENTO = 1;
	private static final int MENU_AJUDA = 3;
	private static final int MENU_SAIR = 2;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			/** Carrega componentes **/
			carregaComponentes();

			/** Carrega Lista de orcamentos **/
			atualizaListaOrcamentos();

			/** Ação do botão 'Adiconar Orcamento' **/
			btnOrcamentoAddAction();

		} catch (Exception e) {
			Log.e(LOG, e.getMessage());
		}
	}

	public void carregaComponentes() {
		this.addOrcamentoBtn = (Button) findViewById(R.id.btn_orcamento_add);
		dbHelp = new OrcamentoFreeDao(getApplicationContext());
	}

	public void atualizaListaOrcamentos() {
		ArrayList<Orcamento> orcamentoLst = new ArrayList<Orcamento>();
		orcamentoLst.clear();
		orcamentoLst.addAll(dbHelp.findOrcamento());
		OrcamentoListAdapter orcamentoAdapter = new OrcamentoListAdapter(this,orcamentoLst);
		listView = (ListView) findViewById(R.id.orcamentoList);
		listView.setAdapter(orcamentoAdapter);
		listView.setOnItemClickListener(this);
	}

	public void btnOrcamentoAddAction() {
		addOrcamentoBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				initOrcamentoADD(ADD_ORCAMENTO);
			}
		});
	}
	
	private void initOrcamentoADD(int requestCode) {
		this.intentOrcamento = new Intent(this, OrcamentoActivity.class);
		startActivityForResult(intentOrcamento, requestCode);
	}

	private void initOrcamentoEdit(int requestCode) {
		startActivityForResult(intentOrcamento, requestCode);
	}

	private void initOrcamentoAjuda() {
		this.intentOrcamentoHelp = new Intent(this, OrcamentoHelpActivity.class);
		startActivity(intentOrcamentoHelp);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Orcamento orc = (Orcamento) listView.getAdapter().getItem(arg2);
		this.intentOrcamento = new Intent(this, OrcamentoActivity.class);
		intentOrcamento.putExtra("ID_ORCAMENTO_EDIT", String.valueOf(orc.get_id()));
		initOrcamentoEdit(EDIT_ORCAMENTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.intentOrcamento = new Intent(this, OrcamentoActivity.class);
		atualizaListaOrcamentos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, MENU_ADD_ORCAMENTO, 0, "Novo Orçamento").setIcon(R.drawable.ic_action_new);
		item = menu.add(0, MENU_AJUDA, 0, "Ajuda").setIcon(R.drawable.ic_action_help);
		item = menu.add(0, MENU_SAIR, 0, "Sair").setIcon(R.drawable.ic_action_remove);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD_ORCAMENTO:
			initOrcamentoADD(ADD_ORCAMENTO);
			return true;
		case MENU_AJUDA:
			initOrcamentoAjuda();
			return true;			
		case MENU_SAIR:
			finish();
			return true;
		}
		return false;
	}		
}
