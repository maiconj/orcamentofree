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

	private String[] scriptSQLCreateTable = new String[] {
			"CREATE TABLE orcamento ( _id integer primary key  autoincrement, "
					+ " descricao text not null, " + " loja text not null, "
					+ " data_hora date_time not null, " + " endereco text); ",
			"CREATE TABLE produto ( _id integer primary key  autoincrement, "
					+ " codigo text null, " + " descricao text not null, "
					+ " quantidade FLOAT not null, "
					+ " preco FLOAT not null, " + " foto text null, "
					+ " id_orcamento integer not null, "
					+ " FOREIGN KEY(id_orcamento) REFERENCES orcamento(_id)); " };

	private String[] scriptSQLPopulaOrcamento = new String[] {
			"insert into orcamento (descricao, loja, data_hora,endereco) values('AZULEJO BANHEIRO','TAQI'       ,'10/05/2013 18:56','PAROBE');",
			"insert into orcamento (descricao, loja, data_hora,endereco) values('AZULEJO COZIHA'  ,'QUERO-QUERO','09/09/2013 19:00','TAQUARA');",
			"insert into orcamento (descricao, loja, data_hora,endereco) values('PISO SALA'       ,'COLOMBO'    ,'11/12/2013 21:06','APIRANGA');",
			"insert into orcamento (descricao, loja, data_hora,endereco) values('JANELA'          ,'MACOFER'    ,'08/05/2013 12:55','N. HAMBURGO');",
			"insert into orcamento (descricao, loja, data_hora,endereco) values('PS3'             ,'AMERICANAS' ,'06/06/2013 11:45','RJ');",
			"insert into orcamento (descricao, loja, data_hora,endereco) values('TV'              ,'SUBMARINO'  ,'01/01/2014 09:32','SP');" };

	public SQLiteHelper(Context context) {
		super(context, NOMEBANCO, null, versao);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOG, "Criando banco de dados");
		int qtdeScripts = this.scriptSQLCreateTable.length;

		for (int i = 0; i < qtdeScripts; i++) {
			String sql = this.scriptSQLCreateTable[i];
			Log.i(LOG, sql);
			db.execSQL(sql);
		}

		Log.i(LOG, "populando TABLE ORCAMENTO");
		int qtdeScriptsPopula = this.scriptSQLPopulaOrcamento.length;

		for (int i = 0; i < qtdeScriptsPopula; i++) {
			String sq = this.scriptSQLPopulaOrcamento[i];
			Log.i(LOG, "sql_popula: " + sq);
			db.execSQL(sq);
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
