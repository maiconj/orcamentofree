package com.orcamentofree;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button addOrcamentoBtn;
	private Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			this.addOrcamentoBtn = (Button) findViewById(R.id.btn_orcamento_add);
			this.i = new Intent(this, OrcamentoActivity.class);

			addOrcamentoBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity(i);
				}

			});
		} catch (Exception e) {
			Log.e("ORCAMENTOFREE", e.getMessage());
		}

		
		//TODO
		carregaListaOrcamentos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void carregaListaOrcamentos(){
		//TODO
	}

}
