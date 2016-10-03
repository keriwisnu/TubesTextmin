/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tubes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Range;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * 
 */
public class Classification extends Thread {

    static String classTrain = "E:\\Java_Project\\TubesTextmin13\\Training.arff";
    static String classTest = "E:\\Java_Project\\TubesTextmin13\\Testing.arff";
    boolean finish = false;

    public Classification() {
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {

        StringToWordVector filter = new StringToWordVector();

        File training = new File(classTrain);
        File testing = new File(classTest);

        BufferedReader readTrain = new BufferedReader(new FileReader(training));
        BufferedReader readTest = new BufferedReader(new FileReader(testing));

        Instances dataTrain = new Instances(readTrain);
        Instances dataTest = new Instances(readTest);

        filter.setInputFormat(dataTrain);
        dataTrain = Filter.useFilter(dataTrain, filter);

        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
        dataTest.setClassIndex(dataTest.numAttributes() - 1);

        Classification classify = new Classification();
        NaiveBayes bayes = new NaiveBayes();
//        RandomForest rf = new RandomForest();
//        BayesNet bayesNet = new BayesNet();
        LibSVM libSVM = new LibSVM();
        System.out.println("==========================Naive Bayes Evaluation===========================");
        Evaluation eval = classify.runClassifier(bayes, dataTrain, dataTest);
        System.out.println(eval.toSummaryString() + "\n");
        System.out.println(eval.toClassDetailsString() + "\n");
        System.out.println(eval.toMatrixString() + "\n");
        System.out.println("===========================================================================");
//
//        ====System.out.println("==============================Random Forest================================");
//        Evaluation eval2 = classify.runClassifier(rf, dataTrain, dataTest);
//        System.out.println(eval2.toSummaryString() + "\n");
//        System.out.println(eval2.toClassDetailsString() + "\n");
//        System.out.println(eval2.toMatrixString() + "\n");
//        System.out.println("=======================================================================");
//
//        System.out.println("==============================Bayesian Network================================");
//        Evaluation eval3 = classify.runClassifier(bayesNet, dataTrain, dataTest);
//        System.out.println(eval3.toSummaryString() + "\n");
//        System.out.println(eval3.toClassDetailsString() + "\n");
//        System.out.println(eval3.toMatrixString() + "\n");
//        System.out.println("===========================================================================");

        System.out.println("==============================LibSVM================================");
        libSVM.setCacheSize(512); // MB
        libSVM.setNormalize(true);
        libSVM.setShrinking(true);
        libSVM.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
        libSVM.setDegree(3);
        libSVM.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));
        Evaluation eval4 = classify.runClassifier(libSVM, dataTrain, dataTest);
        System.out.println(eval4.toSummaryString() + "\n");
        System.out.println(eval4.toClassDetailsString() + "\n");
        System.out.println(eval4.toMatrixString() + "\n");
        System.out.println("===========================================================================");
    }

    public Evaluation runClassifier(Classifier model, Instances training, Instances testing) {
        try {
            Evaluation eval_train = new Evaluation(training);
            model.buildClassifier(training);
  
            eval_train.evaluateModel(model, testing);
            return eval_train;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
