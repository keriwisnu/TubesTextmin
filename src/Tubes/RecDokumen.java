/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tubes;

import com.sun.xml.internal.fastinfoset.vocab.Vocabulary;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Keriwisnu
 */
public class RecDokumen {

//    private String judul;
    private ArrayList<Term> daftartfIdf = new ArrayList<>();
    private String label;
//
//    public String getJudul() {
//        return judul;
//    }

    public String getLabel() {
        return label;
    }

    public ArrayList<Term> getDaftartfIdf() {
        return daftartfIdf;
    }
//
//    public void setJudul(String judul) {
//        this.judul = judul.replaceAll("[^A-Za-z0-9\\s\\:\\~\\-\\.]", "");
//    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDaftartfIdf(int index, ArrayList<KataVocabulary> Vocabulary) {
        //Sorting
        Collections.sort(Vocabulary, new Comparator<KataVocabulary>() {
            public int compare(KataVocabulary k1, KataVocabulary k2) {
                return k2.getDF() - k1.getDF();
            }
        });

        for (int i = 0; i < (Vocabulary.size() / 2); i++) {
            Term t = new Term();
            t.setTerm(Vocabulary.get(i).getKata());

            //Hitung TFxIDF
            double tf = (double) Vocabulary.get(i).getAsalKata()[index].getFrequensi();
            t.setTfIdf(TFxIDF(tf, Vocabulary.get(i).getIDF()));
            daftartfIdf.add(t);
        }
    }

    public Double TFxIDF(Double tf, Double idf) {
        return tf * idf;
    }

    public void DisplayData() {
//        System.out.print(getJudul() + ",");
        for (int i = 0; i < getDaftartfIdf().size(); i++) {
            System.out.print(getDaftartfIdf().get(i).getTfIdf() + ",");
        }
        System.out.print(getLabel());
    }

    public String getData() {
//        String data = getJudul() + ",";
        String data = "";
        for (int i = 0; i < getDaftartfIdf().size(); i++) {
            data += getDaftartfIdf().get(i).getTfIdf() + ",";
        }
        data += getLabel();
        return data;
    }

    public void setDaftartfIdfTesting(int index, ArrayList<KataVocabulary> VocabularyTraining, ArrayList<KataVocabulary> VocabularyTesting) {
        //Sorting
        Collections.sort(VocabularyTraining, new Comparator<KataVocabulary>() {
            public int compare(KataVocabulary k1, KataVocabulary k2) {
                return k2.getDF() - k1.getDF();
            }
        });

        for (int i = 0; i < (VocabularyTraining.size() / 2); i++) {
            Term t = new Term();
            t.setTerm(VocabularyTraining.get(i).getKata());

            //Hitung TFxIDF
            //Cari Term di VocabularyTesting
            int lokasi = -1;
            for (int j = 0; j < VocabularyTesting.size(); j++) {
                if (VocabularyTesting.get(j).getKata().equals(VocabularyTraining.get(i).getKata())) {
                    lokasi = j;
                }
            }
            double tf;
            if (lokasi != -1) {
                tf = (double) VocabularyTesting.get(lokasi).getAsalKata()[index].getFrequensi();
                t.setTfIdf(TFxIDF(tf, VocabularyTesting.get(lokasi).getIDF()));
            } else {
                t.setTfIdf(0);
            }
            daftartfIdf.add(t);
        }
    }
}
