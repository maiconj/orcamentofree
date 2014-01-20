package com.orcamentofree.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String NOMEBANCO = "orcamentofree";
	private static int versao = 1;
	private static final String LOG = "DESENV";
	private String[] scriptSQLDelete = new String[] {
			"DROP TABLE IF EXISTS orcamento;", "DROP TABLE IF EXISTS produto;" };

	private String[] scriptSQLCreate = new String[] { 
			"CREATE TABLE orcamento ( _id integer primary key  autoincrement, "
									+ " descricao text not null, "
									+ " loja text not null, "
									+ " data_hora date_time not null, "
									+ " endereco text); ", 
	        "CREATE TABLE produto ( _id integer primary key  autoincrement, "
							        + " codigo text null, "
							        + " descricao text not null, "
							        + " quantidade FLOAT not null, "
							        + " preco FLOAT not null, "
							        + " foto text not null, "
							        + " id_orcamento integer not null, "
							        + " FOREIGN KEY(id_orcamento) REFERENCES orcamento(_id)); "};

	

	public SQLiteHelper(Context context) {
		super(context, NOMEBANCO, null, versao);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOG, "Criando banco de dados");
		int qtdeScripts = this.scriptSQLCreate.length;

		for (int i = 0; i < qtdeScripts; i++) {
			String sql = this.scriptSQLCreate[i];
			Log.i(LOG, sql);
			db.execSQL(sql);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG, "Atualizando da versao:" + oldVersion + "para a versao: "
				+ newVersion);
		int qtdeScripts = this.scriptSQLDelete.length;
		for (int i = 0; i < qtdeScripts; i++) {
			String sql = this.scriptSQLDelete[i];
			Log.i(LOG, sql);
			db.execSQL(sql);
		}
		onCreate(db);
	}

}
