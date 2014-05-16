package com.orcamentofree;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.orcamentofree.base.OrcamentoFreeDao;
import com.orcamentofree.listAdapter.OrcamentoListAdapter;
import com.orcamentofree.pojo.ConfigExportaOrcamentos;
import com.orcamentofree.pojo.Orcamento;
import com.orcamentofree.pojo.Produto;
import com.orcamentofree.utils.SDCardUtils;

public class MainActivity extends Activity implements OnItemClickListener  {

	private ListView listView;
	private Button addOrcamentoBtn;
	private Intent intentOrcamento;
	private Intent intentOrcamentoHelp;
	private OrcamentoFreeDao dbHelp = null;
	private static final String LOG = "DESENV";
	private static final String EXPORT = "EXPORT";
	private static final String ERROR = "ERROR";
	private static final String NO_SDCARD = "NO_SDCARD";
	private static final int ADD_ORCAMENTO = 0;
	private static final int EDIT_ORCAMENTO = 1;
	
	private static final int MENU_ADD_ORCAMENTO = 1;
	private static final int MENU_AJUDA = 3;
	private static final int MENU_EXPORTAR = 4;
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
		OrcamentoListAdapter orcamentoAdapter = new OrcamentoListAdapter(this,orcamentoLst,getApplicationContext());
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
		
	private void exportOrcamento(){

		boolean isPresent = Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED);
		
		if(isPresent){
			
			File file = criaArquivoPdf();
			ConfigExportaOrcamentos configExport = new ConfigExportaOrcamentos();
			Paragraph linha = new Paragraph(configExport.separaLinha);
			if (file != null) {
				//carrega orcamento
				ArrayList<Orcamento> orcamentosLst = new ArrayList<Orcamento>();
				orcamentosLst.addAll(dbHelp.findOrcamento());
				Document document = new Document(PageSize.A4, 42, 42, 42, 42);
				try {
					PdfWriter.getInstance(document, new FileOutputStream(file));
					document.open();
					//loop nos orcamentos
					for (Orcamento orcamentoTemp : orcamentosLst) {
						//carrega produtos do orcamento
						ArrayList<Produto> produtosLst = new ArrayList<Produto>();
						produtosLst.addAll(dbHelp.findProdutoByOrcamento(orcamentoTemp));			
						//adiciona dados orcamento
						document.add(createParagrafoOrcamento(orcamentoTemp , produtosLst));
						
						if (produtosLst.size() < 1) {
							Paragraph produtosnull = new Paragraph(	"SEM PRODUTOS CADASTRADOS");
							document.add(produtosnull);
						} else {
							// adicionar tabela produtos
							float column[] = { 20f, 10f };
							PdfPTable table = new PdfPTable(column);
							PdfPCell header = new PdfPCell(	new Paragraph("Produtos"));
							header.setColspan(3);
							table.addCell(header);
		    		    
							// loop produtos na tabela
							for (Produto produtotemp : produtosLst) {
								table.addCell(configExport.pdesc + produtotemp.getDescricao() + "\n"
											+ configExport.pcod   + produtotemp.getCodigo() + "\n"
											+ configExport.pqtde  + produtotemp.getQuantidade() + "  " + produtotemp.getUnidadeMedida() + "\n"
											+ configExport.pvalor + produtotemp.getPreco() + "\n"								
											+ configExport.ptotal + produtotemp.getTotal());
					
								if(produtotemp.getFoto().compareTo("SEM_FOTO") == 0){
									table.addCell("      \n\nFOTO NÃO CADASTRADA");
								}else{
									Image img = Image.getInstance(SDCardUtils.getSdCardFile("orcamentoFree", produtotemp.getFoto()).toString());
									table.addCell(img);
								}							
							}
							document.add(table);
							document.addHeader("arg0", "arg1");
						}
					document.add(linha);						
					}
				} catch (Exception e) {
					Log.e(LOG, e.getMessage());
					showMessage("ERROR");
				}
				document.close();
				showMessage(EXPORT);				
			}
		}
		else{			
			showMessage(NO_SDCARD);
		}
	}

	public File criaArquivoPdf(){
		File file = null;
		try {
			String fileName = "lista_orcamentos.pdf";
			File sdcard = android.os.Environment.getExternalStorageDirectory();
			// Meus Arquivos - pasta "Listas de Orcamentos" 
			File dir = new File(sdcard, "LISTA DE ORCAMENTOS");
			if (!dir.exists()) {
				dir.mkdir();
			}
			file = new File(dir, fileName);
		}catch (Exception e) {
			Log.e(LOG, e.getMessage());
		} 
		return file;
	}
	
	
	private Paragraph createParagrafoOrcamento(Orcamento orcamentoTemp,	ArrayList<Produto> produtosLst) {
		ConfigExportaOrcamentos configExport = new ConfigExportaOrcamentos();
		Paragraph orcamento = null;
		
		try{
			// calcula valor orcamento
			BigDecimal totalOrcamento = BigDecimal.ZERO;
			for (Produto produto : produtosLst) {
				totalOrcamento = totalOrcamento.add(produto.getTotal());
			}
			// formata data dd/MM/yy - EEE
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			Date date = (Date) dateFormat.parse(orcamentoTemp.getData());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy - EEE");
			String dataOrc = formatter.format(date);
	
			orcamento = new Paragraph(
					configExport.orcamentoDesc	+ orcamentoTemp.getDescricao() +configExport.espaco+ configExport.orcamentoData + dataOrc + "\n"
					+ configExport.orcamentoLoja + orcamentoTemp.getLoja() + "\n"
					+ configExport.orcamentoEndereco + orcamentoTemp.getEndereco()
					+ "\n" + configExport.orcamentoTotal + "R$ " + totalOrcamento
					+ "\n\n");
			}catch(Exception e) {
				Log.e(LOG, e.getMessage());
		}
		return orcamento;

	}
	
	private void showMessage(String typeMsg) {
		View view = null;
		LayoutInflater inflater = getLayoutInflater();

		if (typeMsg.compareTo(EXPORT) == 0) {
			view = inflater.inflate(R.layout.msg_orcamento_export, (ViewGroup) findViewById(R.id.orcamentoExportLayout));
		}else if (typeMsg.compareTo(ERROR) == 0) {
			view = inflater.inflate(R.layout.msg_orcamento_export_error, (ViewGroup) findViewById(R.id.orcamentoExportError));
		 }else if (typeMsg.compareTo(NO_SDCARD) == 0) {
			 view = inflater.inflate(R.layout.msg_orcamento_export_no_sdcard, (ViewGroup) findViewById(R.id.orcamentoExportNoSdcard));
		 } 
		
		Toast toast = new Toast(this);
		toast.setView(view);		
		toast.show();
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
		item = menu.add(0, MENU_EXPORTAR, 0, "Exportar Orçamentos").setIcon(R.drawable.ic_action_new_label);
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
		case MENU_EXPORTAR:
			Toast.makeText(this, "Por favor, aguarde...", 2000).show();
			exportOrcamento();
			return true;
		case MENU_SAIR:
			finish();
			return true;
		}
		return false;
	}

		
}
