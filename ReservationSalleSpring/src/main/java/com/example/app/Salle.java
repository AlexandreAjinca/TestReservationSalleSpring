package com.example.app;

import java.util.HashMap;
import java.util.Map;

public class Salle implements Comparable{

    private String nom;
    private int capacite;
    //Représente les équipements présents dans la Salle
    private Map<TypeEquipement,Integer> equipements = new HashMap<>();

    public Salle(String n, int c, Map<TypeEquipement,Integer> e){
        this.nom = n;
        this.capacite = c;
        this.equipements = e;
    }

    //GETTERS ET SETTERS
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public Map<TypeEquipement,Integer> getEquipements() {
        return equipements;
    }

    public void setEquipements(Map<TypeEquipement,Integer> equipements) {
        this.equipements = equipements;
    }

    @Override
    public int compareTo(Object o) {
        Salle element = (Salle) o;
        return this.capacite-element.getCapacite();
    }

    @Override
    public String toString(){
        String result = "[SALLE " + nom + " ; " + capacite + " personnes ; équipement : " + equipements + "]";
        return result;
    }
}
