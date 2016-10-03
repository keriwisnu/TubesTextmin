/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tubes;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;
import java.io.IOException;
/**
 *
 * @author adityanap
 */
public class CSV2Arff {


  public void konvert(String lokasi1,String lokasi2) throws IOException{
       // load CSV
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File(lokasi1));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File(lokasi2));

    saver.writeBatch();  
  }
}
