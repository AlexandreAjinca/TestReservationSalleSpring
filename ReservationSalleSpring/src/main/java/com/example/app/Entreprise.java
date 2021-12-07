package com.example.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.example.app.Reunion.TypeReunion;
import com.example.app.Reunion.TypeReunion.*;
import com.example.app.TypeEquipement.*;

/**
 * Représente l'entreprise avec les réunions, les salles, le matériel libre et le planning de réservations.
 * Contient également les fonctions permettant la création du planning
 */
public class Entreprise {

    private List<Reunion> reunions = new ArrayList<>();
    private List<Salle> locaux = new ArrayList<>();
    //L'équipement pouvant être emprunté pour une réunion
    private Map<TypeEquipement,Integer> equipementsLibres = new HashMap<>();
    //La liste des réservations de l'entreprise
    private List<Reservation> reservations = new ArrayList<>();

    //Pattern singleton
    private static Entreprise instance = new Entreprise();
    public static Entreprise getInstance(){
        if(instance==null){
            instance = new Entreprise();
        }
        return instance;
    }
    
    //Initialise l'entreprise avec la liste de réunions et de salles
    private Entreprise(){
    	initializeCompany();
    }

    //Initialise la liste des salles et des réunions.
    private void initializeCompany(){
        reunions.add(new Reunion(1,9,TypeReunion.VC,8));
        reunions.add(new Reunion(2,9, TypeReunion.VC,6));
        reunions.add(new Reunion(3,11,TypeReunion.RC,4));
        reunions.add(new Reunion(4,11, TypeReunion.RS,2));
        reunions.add(new Reunion(5,11,TypeReunion.SPEC,9));

        reunions.add(new Reunion(6,9,TypeReunion.RC,7));
        reunions.add(new Reunion(7,8, TypeReunion.VC,9));
        reunions.add(new Reunion(8,8,TypeReunion.SPEC,10));
        reunions.add(new Reunion(9,9,TypeReunion.SPEC,5));
        reunions.add(new Reunion(10,9, TypeReunion.RS,4));

        reunions.add(new Reunion(11,9,TypeReunion.RC,10));
        reunions.add(new Reunion(12,11, TypeReunion.VC,12));
        reunions.add(new Reunion(13,11, TypeReunion.SPEC,5));
        reunions.add(new Reunion(14,8, TypeReunion.VC,3));
        reunions.add(new Reunion(15,8, TypeReunion.SPEC,2));

        reunions.add(new Reunion(16,8, TypeReunion.VC,12));
        reunions.add(new Reunion(17,10,TypeReunion.VC,6));
        reunions.add(new Reunion(18,11,TypeReunion.RS,2));
        reunions.add(new Reunion(19,9,TypeReunion.RS,4));
        reunions.add(new Reunion(20,9,TypeReunion.RC,7));

        locaux.add(new Salle("E1001",23, new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E1002",10, new HashMap<>(Map.of(TypeEquipement.Ecran, 1, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E1003",8, new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 1, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E1004",4, new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 1))));
        locaux.add(new Salle("E2001",4, new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E2002",15,new HashMap<>(Map.of(TypeEquipement.Ecran, 1, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 1, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E2003",7,new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E2004",9,new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 1))));
        locaux.add(new Salle("E3001",13,new HashMap<>(Map.of(TypeEquipement.Ecran, 1, TypeEquipement.Pieuvre, 1, TypeEquipement.Webcam, 1, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E3002",8,new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E3003",9,new HashMap<>(Map.of(TypeEquipement.Ecran, 1, TypeEquipement.Pieuvre, 1, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));
        locaux.add(new Salle("E3004",4,new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0))));

        equipementsLibres = new HashMap<>(Map.of(TypeEquipement.Ecran, 5, TypeEquipement.Pieuvre, 4, TypeEquipement.Webcam, 4, TypeEquipement.Tableau, 2));
    }

    /**
     * Affiche la liste des réservations
     */
//    public void displayReservations() {
//        System.out.println("LISTE DES RESERVATIONS : ");
//        for(Reservation e : reservations){
//            if(e.getSalle()==null){
//                System.out.println(e.getReunion() + " Aucune salle ne respectait les conditions pour cette réunion\n");
//            }else{
//                System.out.println(e.getReunion()+"\n\t" +e.getSalle()+"\n\t\t Équipement réservé :"+e.getEquipementReserve()+"\n");
//            }
//        }
//    }

    //GETTERS ET SETTERS
    public List<Reunion> getReunions() {
        return reunions;
    }

    public void setReunions(List<Reunion> reunions) {
        this.reunions = reunions;
    }

    public List<Salle> getLocaux() {
        return locaux;
    }

    public void setLocaux(List<Salle> locaux) {
        this.locaux = locaux;
    }

    public Map<TypeEquipement, Integer> getEquipementsLibres() {
        return equipementsLibres;
    }

    public void setEquipementsLibres(Map<TypeEquipement, Integer> equipementsLibres) {
        this.equipementsLibres = equipementsLibres;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        String result = "REUNIONS : \n";
        int count = 1;
        for (Reunion r:
                reunions) {
            result+="Reunion " + count + " : " + r.getCreneau() + "h ; Type : " + r.getType() +" ; Nombre : " + r.getNbPersonnes() + "\n";
            count++;
        }
        result+="\nSALLES : \n";
        for (Salle s:
                locaux) {
            result+="Salle " + s.getNom() + " : " + s.getCapacite() + " personnes Max ; Equipement : ";
            for (Map.Entry<TypeEquipement,Integer> e :
                 s.getEquipements().entrySet()) {
                result+= e.getKey() + " " + e.getValue() + " ";
            }
            result+="\n";
        }
        result+="\nEQUIPEMENTS LIBRES : \n";
        for (Map.Entry<TypeEquipement,Integer> el:
                equipementsLibres.entrySet()) {
            result+= el.getKey() + " " + el.getValue() + " ";
            result+="\n";
        }
        return result;
    }

}
