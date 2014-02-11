package com.orcamentofree.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.util.Log;

public abstract class InternalStorage {

	private static String LOG = "DESENV";

	public InternalStorage() {
	}

	public static void salvarInternalStorage(String texto, File fileDir) {
		FileWriter fileWriter = null;

		try {
			// Cria o arquivo onde serão salvas as informações.
			File file = new File(fileDir.getAbsolutePath()+"foto_temp.txt");
			fileWriter = new FileWriter(file, true);
			fileWriter.append(texto);
			fileWriter.append("n");// Quebra de linha.
			fileWriter.flush();

		} catch (Exception e) {
			Log.e(LOG,"Erro ao salvar usando Internal Storage - " + e.getMessage());
		} finally {
			// Fecha os recursos.
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static String acessaInternalStorage(File fileDir) {

		String line;
		String conteudo = "";

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileDir.getAbsolutePath()+"foto_temp.txt"));
			while ((line = br.readLine()) != null) {
				conteudo += line;
			}

		} catch (Exception e) {
			Log.e(LOG,"Erro ao salvar usando Internal Storage - " + e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
			}
		}
		// Retorna o conteúdo do arquivo.
		return conteudo;
	}
	
	public static boolean deleteInternalStorageFile(File fileDir) {
		boolean success= false;
		try {
			File file = new File(fileDir.getAbsolutePath()+"foto_temp.txt");
			success=file.delete();
		} catch (Exception e) {
			Log.e(LOG,e.getMessage());
		}		
		return success;
	}

}