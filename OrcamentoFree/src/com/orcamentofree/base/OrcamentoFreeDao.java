package com.orcamentofree.base;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orcamentofree.pojo.Orcamento;

public class OrcamentoFreeDao {

	private static final String LOG = "DESENV";
	public static final String NOME_TABELA_ORCAMENTO = "orcamento";
	public static final String NOME_TABELA_PRODUTO = "produto";
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;	

	public OrcamentoFreeDao(Context ctx) {
		// Abre o banco de dados já existente
		// db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE,
		// null);
		dbHelper = new SQLiteHelper(ctx);
		db = dbHelper.getWritableDatabase();
	}

	// Salva o Orcamento, insere um novo ou atualiza
	public long saveOrcamento(Orcamento orcamento) {
		long id = orcamento.get_id();
		if (id != 0) {
			UpdateOrcamento(orcamento);
		} else {
			id = InsertOrcamento(orcamento);
		}
		return id;
	}

	// Insere um novo orcamento
	private long InsertOrcamento(Orcamento orcamento) {
		ContentValues values = new ContentValues();
		values.put("descricao", orcamento.getDescricao());
		values.put("loja", orcamento.getLoja());
		values.put("data_hora", orcamento.getData());
		values.put("endereco", orcamento.getEndereco());
		long id = db.insert(NOME_TABELA_ORCAMENTO, "", values);
		return id;
	}

	// Atualiza o orcamento no banco. O id do orcamento é utilizado.
	private int UpdateOrcamento(Orcamento orcamento) {
		ContentValues values = new ContentValues();
		values.put("descricao", orcamento.getDescricao());
		values.put("loja", orcamento.getLoja());
		values.put("data_hora", orcamento.getData());
		values.put("endereco", orcamento.getEndereco());

		String _id = String.valueOf(orcamento.get_id());
		String where = "_id=?";
		String[] whereArgs = new String[] { _id };
		int count = db.update(NOME_TABELA_ORCAMENTO, values, where, whereArgs);
		Log.i(LOG, "Atualizou [" + count + "] registros");
		return count;
	}

	// Deleta o orcamento com o id fornecido
	public int deleteOrcamentoById(long id) {
		String where = "_id=?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };
		int count = db.delete(NOME_TABELA_ORCAMENTO, where, whereArgs);
		Log.i(LOG, "Deletou [" + count + "] registros");
		return count;
	}

	// Busca o orcamento pelo id
	public Orcamento findOrcamentoById(long id) {
		// select * from orcamento where _id=?
		Cursor c = db.query(true, NOME_TABELA_ORCAMENTO, Orcamento.colunas,	"_id=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			Orcamento orcamento = new Orcamento();
			orcamento.set_id(c.getInt(0));
			orcamento.setDescricao(c.getString(1));
			orcamento.setLoja(c.getString(2));
			orcamento.setData(c.getString(3));
			orcamento.setEndereco(c.getString(4));
			return orcamento;
		}
		return null;
	}

	// Retorna um cursor com todos os orcamentos
	public Cursor getOrcamentoCursor() {
		try {
			// select * from orcamentos
			return db.query(NOME_TABELA_ORCAMENTO, Orcamento.colunas, null, null, null, null, null, null);
		} catch (SQLException e) {
			Log.e(LOG, "Erro ao buscar os orcamentos: " + e.toString());
			return null;
		}
	}
	

	// Retorna uma lista com todos os orcamentos
	public List<Orcamento> findOrcamento() {
		Cursor c = getOrcamentoCursor(); 
		List<Orcamento> orcamentos = new ArrayList<Orcamento>();
		if (c.moveToFirst()) {
			// Recupera os índices das colunas
			int idxId = c.getColumnIndex("_id");
			int idxDescricao = c.getColumnIndex("descricao");
			int idxLoja = c.getColumnIndex("loja");
			int idxData = c.getColumnIndex("data_hora");
			int idxEndereco = c.getColumnIndex("endereco");

			// Loop até o final
			do {
				Orcamento orcamento = new Orcamento();
				orcamentos.add(orcamento);
				// recupera os atributos de orcamento
				orcamento.set_id(c.getInt(idxId));
				orcamento.setDescricao(c.getString(idxDescricao));
				orcamento.setLoja(c.getString(idxLoja));
				orcamento.setData(c.getString(idxData));
				orcamento.setEndereco(c.getString(idxEndereco));
			} while (c.moveToNext());
		}
		return orcamentos;
	}

	// Busca o orcamento pelo nome "select * from orcamento where nome=?"
	public Orcamento findOrcamentoByNome(String nome) {
		Orcamento orcamento = null;
		try {
			// Idem a: SELECT _id,nome,placa,ano from orcamento where nome = ?
			Cursor c = db.query(NOME_TABELA_ORCAMENTO, Orcamento.colunas, "nome='" + nome + "'", null, null, null, null);
			// Se encontrou...
			if (c.moveToNext()) {
				orcamento = new Orcamento();
				// utiliza os métodos getLong(), getString(), getInt(), etc para
				// recuperar os valores
				orcamento.set_id(c.getInt(0));
				orcamento.setDescricao(c.getString(1));
				orcamento.setLoja(c.getString(2));
				orcamento.setData(c.getString(3));
				orcamento.setEndereco(c.getString(4));
			}
		} catch (SQLException e) {
			Log.e(LOG, "Erro ao buscar o orcamento pelo nome: " + e.toString());
			return null;
		}
		return orcamento;
	}

	// Busca um orcamento utilizando as configurações definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider de orcamento
	// public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
	// String selection, String[] selectionArgs, String groupBy,
	// String having, String orderBy) {
	// Cursor c = queryBuilder.query(this.db, projection, selection,
	// selectionArgs, groupBy, having, orderBy);
	// return c;
	// }

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
