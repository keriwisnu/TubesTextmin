/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tubes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Keriwisnu
 */
public class Dataset {

    private ArrayList<Dokumen> daftarDocument = new ArrayList<Dokumen>();
    private String[] daftarNamaFile;
    private String[] daftarLabelFile = {"no", "no", "no", "no", "no", "no", "no", "no", "yes", "yes", "yes", "no", "no", "yes", "yes", "no", "no", "no", "no", "yes", "yes", "no", "no"};
    private ArrayList<KataVocabulary> Vocabulary = new ArrayList<KataVocabulary>();
    private ArrayList<KataVocabulary> VocabularyTesting = new ArrayList<KataVocabulary>();
    private ArrayList<String> daftarStopWord = new ArrayList<String>();
    private ArrayList<RecDokumen> dataTraining = new ArrayList<RecDokumen>();

    private String[] FileTesting;
    private ArrayList<Dokumen> daftarDocTesting = new ArrayList<Dokumen>();
    private ArrayList<RecDokumen> dataTesting = new ArrayList<RecDokumen>();

    private int posisi;
    private int posisiTesting;
    CSV2Arff csa = new CSV2Arff();

    public ArrayList<Dokumen> getDaftarDocument() {
        return daftarDocument;
    }

    public ArrayList<KataVocabulary> getVocabulary() {
        return Vocabulary;
    }

    public void setDaftarDocument() {
        final File folder = new File("E:\\Java_Project\\TubesTextmin13\\dataset\\Training101\\");
        listFilesForFolder(folder);
        for (int i = 0; i < daftarNamaFile.length; i++) {
            Dokumen d = new Dokumen();
            d.ReadFile("E:\\Java_Project\\TubesTextmin13\\dataset\\Training101\\" + daftarNamaFile[i]); //alamat file dan nama file yang dibaca);
            d.setJudul();
            d.setIsi();
            d.setLabel(daftarLabelFile[i]);
            daftarDocument.add(d);
        }
    }

    public void setFileTesting() {
        final File folder = new File("E:\\Java_Project\\TubesTextmin13\\dataset\\Test101\\");
        listFilesTesting(folder);
        for (int i = 0; i < FileTesting.length; i++) {
            Dokumen d = new Dokumen();
            d.ReadFile("E:\\Java_Project\\TubesTextmin13\\dataset\\Test101\\" + FileTesting[i]); //alamat file dan nama file yang dibaca);
            d.setJudul();
            d.setIsi();
            d.setLabel(FileTesting[i]);
            daftarDocTesting.add(d);
        }
        setLabelTesting();
    }

    public void setLabelTesting() {
        try (
                BufferedReader br = new BufferedReader(new FileReader("E:\\Java_Project\\TubesTextmin13\\dataset\\topic\\Test101.txt"))) {
            String sCurrentLine;

            //Read and Make Array
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] hasilSplit = sCurrentLine.split(" ");
                daftarDocTesting.get(i).setLabel(hasilSplit[2]);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayDocument() {
        for (int i = 0; i < daftarDocument.size(); i++) {
            System.out.print(i + ".");
            System.out.println("Judul: " + daftarDocument.get(i).getJudul());
            System.out.println("Isi: " + daftarDocument.get(i).getIsi());
            System.out.println("");
        }
    }

    public void BuildDictionary() {
        String doc = "";
        for (int i = 0; i < daftarDocument.size(); i++) {
            //Initialisasi Dokumen
            doc = daftarDocument.get(i).getJudul().toLowerCase().replaceAll("[^A-Za-z0-9\\s]", "") + " " + daftarDocument.get(i).getIsi().toLowerCase().replaceAll("[^A-Za-z0-9\\s]", "");
            //Initialisasi StopWord
            initialisasiStopWord();

            //Tokenisasi
            String kata[] = doc.split(" ");

            //Hitung TF
            for (int j = 0; j < kata.length; j++) {
                if (stopWordRemoval(kata[j]) == false && kata[j].matches(".*[0-9\\s].*") == false
                        && !kata[j].equals("")) { //StopWordRemoval, yang stopword lewattt
                    //Stemming
                    Stemming s = new Stemming();
                    kata[j] = s.stripAffixes(kata[j]);

                    //Jumlah kata
                    if (cekKata(kata[j]) == true) {
                        Vocabulary.get(posisi).addFrequensiKata(daftarDocument.get(i).getJudul());
                    } else {
                        KataVocabulary KV = new KataVocabulary(kata[j], daftarDocument.get(i).getJudul(), daftarDocument);
                        Vocabulary.add(KV);
                    }
                }

            }
        }

        //Hitung DF dan IDF
        for (int i = 0; i < Vocabulary.size(); i++) {
            Vocabulary.get(i).HitungDFdanIDF();
        }
    }

    public boolean cekKata(String kata) {
        boolean kondisi = false;
        for (int i = 0; i < Vocabulary.size(); i++) {
            if (Vocabulary.get(i).getKata().equalsIgnoreCase(kata)) {
                kondisi = true;
                posisi = i;
            }
        }
        return kondisi;
    }

    public boolean cekKataTesting(String kata) {
        boolean kondisi = false;
        for (int i = 0; i < VocabularyTesting.size(); i++) {
            if (VocabularyTesting.get(i).getKata().equalsIgnoreCase(kata)) {
                kondisi = true;
                posisiTesting = i;
            }
        }
        return kondisi;
    }

    public void DisplayVocabulary() {
        System.out.println("Vocabulary:");
        for (int i = 0; i < Vocabulary.size(); i++) {
            System.out.println(i + ".");
            Vocabulary.get(i).DisplayKataVocabulary();
        }
    }

    public void initialisasiStopWord() {
        String SentencePatern = ".*[A-Za-z0-9].*";
        Pattern pattern = Pattern.compile(SentencePatern);

        //Read File TXT
        try (BufferedReader br = new BufferedReader(new FileReader("E:\\Java_Project\\TubesTextmin13\\en.txt"))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(sCurrentLine);
                if (matcher.matches() == true) {
                    daftarStopWord.add(sCurrentLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean stopWordRemoval(String kata) {
        boolean StopWord = false;
        //Lakukan Removal
        for (int i = 0; i < daftarStopWord.size(); i++) {
            if (daftarStopWord.get(i).equals(kata)) {
                StopWord = true;
            }
        }
        return StopWord;
    }

    public void BuildDataTraining() {
        for (int i = 0; i < daftarDocument.size(); i++) {
            RecDokumen rd = new RecDokumen();
//            rd.setJudul(daftarDocument.get(i).getJudul());
            rd.setLabel(daftarDocument.get(i).getLabel());
            rd.setDaftartfIdf(i, Vocabulary);
            dataTraining.add(rd);
        }
    }

    public ArrayList<RecDokumen> getDataTraining() {
        return dataTraining;
    }

    public ArrayList<RecDokumen> getDataTesting() {
        return dataTesting;
    }

    public void DisplayDataTraining() {
        System.out.print("Dokumen,");
        for (int i = 0; i < getDataTraining().get(1).getDaftartfIdf().size(); i++) {
            System.out.print(getDataTraining().get(1).getDaftartfIdf().get(i).getTerm() + ",");
        }
        System.out.println("Label");
        for (int i = 0; i < getDataTraining().size(); i++) {
            getDataTraining().get(i).DisplayData();
            System.out.println("");
        }
    }

    public void write() throws Exception  {
        FileWriter writer;
        try {
            writer = new FileWriter("E:\\Java_Project\\TubesTextmin13\\\\Training.csv");
            BufferedWriter bw = new BufferedWriter(writer);
            String kolom = "";
            for (int i = 0; i < getDataTraining().get(1).getDaftartfIdf().size(); i++) {
                kolom += getDataTraining().get(1).getDaftartfIdf().get(i).getTerm() + ",";
            }
            kolom += "Label";
            bw.write(kolom);
            bw.newLine();

            for (int i = 0; i < getDataTraining().size(); i++) {
                bw.write(getDataTraining().get(i).getData());
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Method.class.getName()).log(Level.SEVERE, null, ex);
        }
         csa.konvert("E:\\Java_Project\\TubesTextmin13\\Training.csv", "E:\\Java_Project\\TubesTextmin13\\Training.arff");
    }

    public void listFilesForFolder(final File folder) {
        String judul = "";
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                judul += fileEntry.getName() + ",";
            } else {
                judul += fileEntry.getName() + ",";
            }
        }
        this.daftarNamaFile = judul.split(",").clone();
    }

    public void listFilesTesting(final File folder) {
        String judul = "";
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                judul += fileEntry.getName() + ",";
            } else {
                judul += fileEntry.getName() + ",";
            }
        }
        this.FileTesting = judul.split(",").clone();
    }

    public void BuildDictionaryTesting() {
        String doc = "";
        for (int i = 0; i < daftarDocTesting.size(); i++) {
            //Initialisasi Dokumen
            doc = daftarDocTesting.get(i).getJudul().toLowerCase().replaceAll("[^A-Za-z0-9\\s]", "") + " "
                    + daftarDocTesting.get(i).getIsi().toLowerCase().replaceAll("[^A-Za-z0-9\\s]", "");
            //Initialisasi StopWord
            //initialisasiStopWord();

            //Tokenisasi
            String kata[] = doc.split(" ");

            //Hitung TF
            for (int j = 0; j < kata.length; j++) {
                if (stopWordRemoval(kata[j]) == false && kata[j].matches(".*[0-9\\s].*") == false
                        && !kata[j].equals("")) { //StopWordRemoval, yang stopword lewattt
                    //Stemming
                    Stemming s = new Stemming();
                    kata[j] = s.stripAffixes(kata[j]);

                    //Jumlah kata
                    if (cekKataTesting(kata[j]) == true) {
                        VocabularyTesting.get(posisiTesting).addFrequensiKata(daftarDocTesting.get(i).getJudul());
                    } else {
                        KataVocabulary KV = new KataVocabulary(kata[j], daftarDocTesting.get(i).getJudul(), daftarDocTesting);
                        VocabularyTesting.add(KV);
                    }
                }
            }
        }
        //Hitung DF dan IDF
        for (int i = 0; i < VocabularyTesting.size(); i++) {
            VocabularyTesting.get(i).HitungDFdanIDF();
        }
    }

    public void DisplayVocabularyTesting() {
        System.out.println("Vocabulary Testing:");
        System.out.println(VocabularyTesting.size());
        for (int i = 0; i < VocabularyTesting.size(); i++) {
            System.out.println(i + ".");
//            VocabularyTesting.get(i).DisplayKataVocabulary();
            System.out.println(VocabularyTesting.get(i).getKata());
        }
    }

    public void BuildDataTesting() {
        for (int i = 0; i < daftarDocTesting.size(); i++) {
            RecDokumen rd = new RecDokumen();
//            rd.setJudul(daftarDocTesting.get(i).getJudul());
            rd.setLabel(daftarDocTesting.get(i).getLabel());
            rd.setDaftartfIdfTesting(i, Vocabulary, VocabularyTesting);
            dataTesting.add(rd);
        }
    }

    public void writeTesting() throws Exception {
        FileWriter writer;
        try {
            writer = new FileWriter("E:\\Java_Project\\TubesTextmin13\\Testing.csv");
            BufferedWriter bw = new BufferedWriter(writer);
            String kolom = "";
            for (int i = 0; i < dataTesting.get(1).getDaftartfIdf().size(); i++) {
                kolom += dataTesting.get(1).getDaftartfIdf().get(i).getTerm() + ",";
            }
            kolom += "Label";
            bw.write(kolom);
            bw.newLine();

            for (int i = 0; i < dataTesting.size(); i++) {
                bw.write(dataTesting.get(i).getData());
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Method.class.getName()).log(Level.SEVERE, null, ex);
        }
         csa.konvert("E:\\Java_Project\\TubesTextmin13\\Testing.csv", "E:\\Java_Project\\TubesTextmin13\\Testing.arff");
        
    }
}
