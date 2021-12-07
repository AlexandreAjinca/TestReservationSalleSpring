package com.example.app;

import java.util.HashMap;
import java.util.Map;

public class Reservation implements Comparable<Object>{

    private Reunion reunion;
    private Salle salle;
    //Représente l'equipement réservés pour cette réunion
    private Map<TypeEquipement,Integer> equipementReserve = new HashMap<>();

    public Reservation(Reunion reunion, Salle salle) {
        this.reunion = reunion;
        this.salle = salle;
    }

    //GETTERS ET SETTERS
    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Map<TypeEquipement,Integer> getEquipementReserve() {
        return equipementReserve;
    }

    public void setEquipementReserve(Map<TypeEquipement,Integer> equipementReserve) {
        this.equipementReserve = equipementReserve;
    }

    @Override
    public int compareTo(Object o) {
        Reservation element = (Reservation) o;
        return this.reunion.getCreneau()-element.reunion.getCreneau();
    }

    @Override
    public String toString(){
        return "[RESERVATION : " + reunion + " ; " + salle + " ; equipement réservé : " + equipementReserve +"]";
    }

}
