package sn.imc.bean;

import java.io.Serializable;

/**
 * Bean MonIMC : masse, taille, imc, interprétation, nom, prénom
 * (+ id, clé primaire dans la table patient).
 */
public class MonIMC implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nom;
    private String prenom;
    private double masse;          // en kg
    private double taille;         // en m
    private double imc;            // IMC = masse / taille²
    private String interpretation; // selon les normes OMS

    public MonIMC() {
    }

    public MonIMC(String nom, String prenom, double masse, double taille) {
        this.nom = nom;
        this.prenom = prenom;
        this.masse = masse;
        this.taille = taille;
        calculer();
    }

    /** Calcule l'IMC (arrondi à 2 décimales) et déduit l'interprétation. */
    public void calculer() {
        if (taille > 0) {
            this.imc = Math.round(masse / (taille * taille) * 100.0) / 100.0;
            this.interpretation = interpreter(this.imc);
        } else {
            this.imc = 0;
            this.interpretation = "";
        }
    }

    private static String interpreter(double imc) {
        if (imc < 18.5) return "Insuffisance pondérale";
        if (imc < 25)   return "Corpulence normale";
        if (imc < 30)   return "Surpoids";
        if (imc < 35)   return "Obésité modérée";
        if (imc < 40)   return "Obésité sévère";
        return "Obésité morbide";
    }

    // --- Getters / Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public double getMasse() { return masse; }
    public void setMasse(double masse) { this.masse = masse; }

    public double getTaille() { return taille; }
    public void setTaille(double taille) { this.taille = taille; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
}