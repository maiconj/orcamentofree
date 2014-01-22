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
import com.orcamentofree.pojo.Produto;

public class OrcamentoFreeDao {

	private static final String LOG = "DESENV";
	public static final String NOME_TABELA_ORCAMENTO = "orcamento";
	public static final String NOME_TABELA_PRODUTO = "produto";
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;	

	public OrcamentoFreeDao(Context ctx) {
		// Abre o banco de dados já existente
		// db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
		dbHelper = new SQLiteHelper(ctx);
		db = dbHelper.getWritableDatabase();
	}

	/**BEGIN ORCAMENTO*/
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
	
	/**END ORCAMENTO*/
	
	/**BEGIN PRODUTO*/
		// Salva o Produto, insere um novo ou atualiza
	public long saveProduto(Produto produto) {
		long id = produto.get_id();
		if (id != 0) {
			updateProduto(produto);
		} else {
			id = insertProduto(produto);
		}
		return id;
	}

	// Insere um novo produto
	private long insertProduto(Produto produto) {
		ContentValues values = new ContentValues();
		values.put("descricao", produto.getDescricao());
		values.put("codigo", produto.getCodigo());
		values.put("quantidade", produto.getQuantidade());
		values.put("preco", produto.getPreco());
		values.put("foto", produto.getFoto());
		values.put("id_orcamento", produto.get_idOrcamento());
		long id = db.insert(NOME_TABELA_PRODUTO, "", values);
		return id;
	}
	
	// Atualiza o produto no banco. O id do produto é utilizado.
	private int updateProduto(Produto produto) {
		ContentValues values = new ContentValues();
		values.put("descricao", produto.getDescricao());
		values.put("codigo", produto.getCodigo());
		values.put("quantidade", produto.getQuantidade());
		values.put("preco", produto.getPreco());
		values.put("foto", produto.getFoto());
		values.put("id_orcamento", produto.get_idOrcamento());
		String _id = String.valueOf(produto.get_id());
		String where = "_id=?";
		String[] whereArgs = new String[] { _id };
		int count = db.update(NOME_TABELA_PRODUTO, values, where, whereArgs);
		Log.i(LOG, "Atualizou [" + count + "] registros");
		return count;
	}

	// Deleta o produto com o id fornecido
	public int deleteProdutoById(long id) {
		String where = "_id=?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };
		int count = db.delete(NOME_TABELA_PRODUTO, where, whereArgs);
		Log.i(LOG, "Deletou [" + count + "] registros");
		return count;
	}

	// Busca o produto pelo id
	public Produto findProdutoById(long id) {
		// select * from produto where _id=?
		Cursor c = db.query(true, NOME_TABELA_PRODUTO, Produto.colunas,	"_id=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			Produto produto = new Produto();
			produto.set_id(c.getInt(0));
			produto.setDescricao(c.getString(1));
			produto.setCodigo(c.getString(2));
			produto.setQuantidade(c.getFloat(3));
			produto.setPreco(c.getFloat(4));
			produto.setFoto(c.getString(5));
			produto.set_idOrcamento(c.getInt(6));
			return produto;
		}
		return null;
	}

	// Retorna um cursor com todos os produtos
	public Cursor getProdutoCursor() {
		try {
			// select * from produtos
			return db.query(NOME_TABELA_PRODUTO, Produto.colunas, null, null, null, null, null, null);
		} catch (SQLException e) {
			Log.e(LOG, "Erro ao buscar os produtos: " + e.toString());
			return null;
		}
	}

	// Retorna uma lista com todos os produtos
	public List<Produto> findProduto() {
		Cursor c = getProdutoCursor(); 
		List<Produto> produtos = new ArrayList<Produto>();
		if (c.moveToFirst()) {
			// Loop até o final
			do {
				Produto produto = new Produto();
				produtos.add(produto);
				// recupera os atributos de produto
				produto.set_id(c.getInt(0));
				produto.setDescricao(c.getString(1));
				produto.setCodigo(c.getString(2));
				produto.setQuantidade(c.getFloat(3));
				produto.setPreco(c.getFloat(4));
				produto.setFoto(c.getString(5));
				produto.set_idOrcamento(c.getInt(6));
			} while (c.moveToNext());
		}
		return produtos;
	}

	// Busca o produto pelo nome "select * from produto where nome=?"
	public Produto findProdutoBydescricao(String descricao) {
		Produto produto = null;
		try {
			// Idem a: SELECT _id,nome,placa,ano from produto where descricao = ?
			Cursor c = db.query(NOME_TABELA_PRODUTO, Produto.colunas, "descricao='" + descricao + "'", null, null, null, null);
			// Se encontrou...
			if (c.moveToNext()) {
				produto = new Produto();
				// utiliza os métodos getLong(), getString(), getInt(), etc para
				// recuperar os valores
				produto.set_id(c.getInt(0));
				produto.setDescricao(c.getString(1));
				produto.setCodigo(c.getString(2));
				produto.setQuantidade(c.getFloat(3));
				produto.setPreco(c.getFloat(4));
				produto.setFoto(c.getString(5));
				produto.set_idOrcamento(c.getInt(6));
			}
		} catch (SQLException e) {
			Log.e(LOG, "Erro ao buscar o produto pela descricao: " + e.toString());
			return null;
		}
		return produto;
	}

	// Busca um produto utilizando as configurações definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider de produto
	// public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
	// String selection, String[] selectionArgs, String groupBy,
	// String having, String orderBy) {
	// Cursor c = queryBuilder.query(this.db, projection, selection,
	// selectionArgs, groupBy, having, orderBy);
	// return c;
	// }
	 /**END produto **/
	

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
