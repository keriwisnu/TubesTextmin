/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tubes;

import Tubes.Dokumen;
import Tubes.AsalKata;
import java.util.ArrayList;

/**
 *
 * @author Keriwisnu
 */
public class KataVocabulary {

    private String kata;
    private final AsalKata[] daftarDoc;
    private int df;
    private double idf;

    public KataVocabulary(String kata, String judulDoc, ArrayList<Dokumen> daftarDocument) {
        this.kata = kata;
        this.daftarDoc = new AsalKata[daftarDocument.size()];

        for (int i = 0; i < daftarDoc.length; i++) {
            AsalKata AK = new AsalKata();
            AK.setJudulDoc(daftarDocument.get(i).getJudul());
            daftarDoc[i] = AK;
        }

        addFrequensiKata(judulDoc);
    }

    public String getKata() {
        return kata;
    }

    public AsalKata[] getAsalKata() {
        return daftarDoc;
    }

    public int getDF() {
        return df;
    }

    public double getIDF() {
        return idf;
    }

    public void setKata(String kata) {
        this.kata = kata;
    }

    public void setDF(int DF) {
        this.df = DF;
    }

    public void setIDF(int N, int DF) {
        double n = (double) N;
        double df = (double) DF;
        this.idf = Math.log10(n / df);
    }

    public void HitungDFdanIDF() {
        int DF = 0;
        for (int i = 0; i < daftarDoc.length; i++) {
            if (daftarDoc[i].getFrequensi() > 0) {
                DF++;
            }
        }
        setDF(DF);
        setIDF(daftarDoc.length, DF);
    }

    public void addFrequensiKata(String asal) {
        for (int i = 0; i < daftarDoc.length; i++) {
            if (daftarDoc[i].getJudulDoc().equals(asal)) {
                daftarDoc[i].addFrequensi();
            }
        }
    }

    public void DisplayKataVocabulary() {
        System.out.println("Kata: " + kata);
        System.out.println("Matriks Frekuensi :");
        for (int i = 0; i < daftarDoc.length; i++) {
            System.out.println(daftarDoc[i].getJudulDoc() + ": " + daftarDoc[i].getFrequensi());
        }
        System.out.println("DF: " + getDF());
        System.out.println("IDF: " + getIDF());
    }
}
