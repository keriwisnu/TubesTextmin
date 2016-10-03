/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tubes;

import java.io.File;

/**
 *
 * @author Keriwisnu
 */
public class Tubes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //Training
        Dataset DS = new Dataset();
        DS.setDaftarDocument();
        DS.displayDocument();

        DS.BuildDictionary();
        DS.DisplayVocabulary();

        DS.BuildDataTraining();
        //DS.DisplayDataTraining();
        DS.write();

        //Testing
        DS.setFileTesting();
        DS.BuildDictionaryTesting();
        //DS.DisplayVocabularyTesting();
        DS.BuildDataTesting();
        DS.writeTesting();
    }

}
