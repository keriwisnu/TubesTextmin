/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tubes;

/**
 *
 * @author Keriwisnu
 */
public class Term {
    private String term;
    private double tfIdf;

    public String getTerm() {
        return term;
    }
    public double getTfIdf() {
        return tfIdf;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }
}
