/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tubes;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Keriwisnu
 */
public class Dokumen {

    private String judul = "";
    private String isi = "";
    private String label = "";
    private Document doc;

    public String getJudul() {
        return judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setJudul() {
        NodeList nList = doc.getElementsByTagName("newsitem");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                this.judul = eElement.getElementsByTagName("title").item(0).getTextContent();
            }
        }
    }

    public void setIsi() {
        NodeList nList = doc.getElementsByTagName("newsitem");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                //Sentence Pattern for Regex
                String SentencePatern = ".*^[A-Z,\",0-9].*";

                //Pengambilan semua isi dengan merubah root
                NodeList isi = doc.getElementsByTagName("p");
                for (int tempIsi = 0; tempIsi < isi.getLength(); tempIsi++) {
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        //Make Match user Regex Beetwen Sentence Pattern and First Character of Word
                        Pattern pattern = Pattern.compile(SentencePatern);
                        Matcher matcher = pattern.matcher(eElement.getElementsByTagName("p").item(tempIsi).getTextContent());

                        //Jika benar kaliamat diambil, jika tidak dibuang
                        if (matcher.matches() == true) {
                            this.isi = this.isi + eElement.getElementsByTagName("p").item(tempIsi).getTextContent() + " ";
                        }
                    }
                }
            }
        }
    }

    public void ReadFile(String fileAddress) {
        try {
            File fXmlFile = new File(fileAddress); //Masukkan alamat disini
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            this.doc = doc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
