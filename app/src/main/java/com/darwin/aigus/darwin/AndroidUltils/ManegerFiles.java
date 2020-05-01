package com.darwin.aigus.darwin.AndroidUltils;

import android.os.Environment;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.Modelos.Levantamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManegerFiles {
    private HelperManegerFile helperManegerFile;

    public interface HelperManegerFile{
        void dirNotFound();
        void getPermission();
        void createFile();
    }

    public ManegerFiles(HelperManegerFile helperManegerFile) {
        this.helperManegerFile = helperManegerFile;
    }

    List<String> getListKmlFiles(){
        return kmlFiles();
    }
    //ARRAY DE AQUIVOS DA PASTA KML
    private List<String> kmlFiles() {
        ArrayList<String> Arquivos = new ArrayList<>();
        File diretorio = createDirDarwinKml();
        assert diretorio != null;
        File[] arquivos = diretorio.listFiles();
        if (arquivos != null) {
            for (File f : arquivos) {
                if (f.isFile()) {
                    Arquivos.add(f.getName());
                }
            }

        }
        return Arquivos;
    }
    File getDirSDCard(String dir, String file){
        return getDirFromSDCard(dir, file);
    }
    //OTBTEM O ARQUIVO DE DIRETORIO ESPECIFICO
    private File getDirFromSDCard(String diretorio, String file) {
        createDirDarwin();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory()
                    .getAbsoluteFile();
            File dir = new File(sdcard, "Darwin" + File.separator + diretorio + File.separator + file);
            if (!dir.exists())
                dir.mkdir();
            return dir;
        } else {
            return null;
        }
    }

    public File getDirDarwinKml(){
        return createDirDarwinKml();
    }
    //CRIA O DITEORIO Darwin/Kml
    public File createDirDarwinKml() {
        createDirDarwin();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory()
                    .getAbsoluteFile();
            File dir = new File(sdcard, "Darwin" + File.separator + "Kml");
            if (!dir.exists())
                dir.mkdir();
            return dir;
        } else {
            return null;
        }
    }

    File getDirDarwin(){
        return createDirDarwin();
    }
    //CRIA O DIRETORIO Drawin
    public File createDirDarwin() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory().getAbsoluteFile();
            File dir = new File(sdcard, "Darwin");
            if (!dir.exists())
                dir.mkdir();
            return dir;
        } else {
            return null;
        }
    }

    //cCRIA O DITEORIO Darwin/Amostras
    public File createDirDarwinAmostras() {
        createDirDarwin();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory()
                    .getAbsoluteFile();
            File dir = new File(sdcard, "Darwin" + File.separator + "Amostras");
            if (!dir.exists())
                dir.mkdir();
            return dir;
        } else {
            return null;
        }
    }

    public boolean createFileEspecies(byte[] data, String file) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream(file);
        //FileOutputStream out = new FileOutputStream(createDirDarwinAmostras() + File.separator + fileName);
        try {
            out.write(data);
            out.flush();
            out.close();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public byte[] getBaytesEspecies(Levantamento levantamento, Amostra amostra, List<Especie> especies) {
        List<String> strings = new ArrayList<>();
        //levantamento, amostra, longitute, latitude, especie, nome_cientifico, familia, altura, diametro, data
        strings.add("levantamento,amostra,longitute,latitude,especie,nome_cientifico,familia,altura,diametro,data\n");

        for (Especie especie : especies) {
            strings.add(levantamento.getLev_name()+","+amostra.getAm_name()+","+amostra.getAm_geodata_long()+","+amostra.getAm_geodata_lat()+","+especie.getEs_name()+","+especie.getEs_name_cient()+","+especie.getEs_familia()+","+especie.getEs_height()+ ","+especie.getEs_diameter()+","+especie.getEsp_data()+"\n");
        }
        StringBuilder finalString = new StringBuilder();

        for (String s: strings){
            finalString.append(s);
        }
        return finalString.toString().getBytes();
    }
}
