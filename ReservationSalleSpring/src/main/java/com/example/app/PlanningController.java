package com.example.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlanningController {
	
	Entreprise e = Entreprise.getInstance();
	
	@GetMapping("/company")
	public String company(Model model) {
		model.addAttribute("entreprise", e);
		return "company";
	}
	
	@GetMapping("/planning")
	public String planning(Model model) {
		model.addAttribute("reservations", createPlanning(e.getLocaux(),e.getReunions()));
		return "planning";
	}
	
	public List<Reservation> createPlanning(List<Salle> salles, List<Reunion> reunions){
		List<Reservation > reservations = new ArrayList<Reservation>();
		
		//On crée une Map qui associant à chaque réunion la liste des salles
        Map<Reunion,List<Salle>> ReunionsNonReservees = new TreeMap<>();
        for(Reunion r : reunions){
            ReunionsNonReservees.put(r,new ArrayList<>(salles));
        }

        //On enlève les salles ne respectant pas les restrictions de capacité
        for(Map.Entry<Reunion,List<Salle>> rnr : ReunionsNonReservees.entrySet()){
            rnr.getValue().removeIf(x->x.getCapacite()*0.7 < rnr.getKey().getNbPersonnes());
        }

        //Tant qu'il reste des réunions a assigner
        while(ReunionsNonReservees.size()>0){
            //Tant qu'il y a des réunions avec une seule salle possible, on les cherche pour les réserver
            while(ReunionsNonReservees.values().stream().anyMatch(x -> x.size()==1)){
                for(Map.Entry<Reunion,List<Salle>> rnr : ReunionsNonReservees.entrySet()){
                    if(rnr.getValue().size()==1){
                        Reunion r = rnr.getKey();
                        Salle s = rnr.getValue().get(0);
                        //On vérifie si la salle est disponible, si c'est le cas, on crée une nouvelle réservation
                        if(isSalleDisponible(reservations, s,r.getCreneau()) && isEquipementDisponible(reservations, r,s,e.getEquipementsLibres())){
                            Reservation newRes = new Reservation(r,s);
                            newRes.setEquipementReserve(new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0)));
                            //On ajoute l'équipement manquant à la salle
                            switch(r.getType()) {
                                case VC:
                                    if (s.getEquipements().get(TypeEquipement.Ecran) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Ecran, 1);
                                    if (s.getEquipements().get(TypeEquipement.Pieuvre) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Pieuvre, 1);
                                    if (s.getEquipements().get(TypeEquipement.Webcam) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Webcam, 1);
                                    break;
                                case SPEC:
                                    if (s.getEquipements().get(TypeEquipement.Tableau) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Tableau, 1);
                                    break;
                                case RS:
                                    break;
                                case RC:
                                    if (s.getEquipements().get(TypeEquipement.Ecran) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Ecran, 1);
                                    if (s.getEquipements().get(TypeEquipement.Pieuvre) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Pieuvre, 1);
                                    if (s.getEquipements().get(TypeEquipement.Tableau) == 0)
                                        newRes.getEquipementReserve().put(TypeEquipement.Tableau, 1);
                                    break;
                            }
                            reservations.add(newRes);
                        }
                        else{
                            reservations.add(new Reservation(r,null));
                        }
                    }
                }
                //On enlève les réunions assignées de la Map
                for(Reservation r : reservations){
                    ReunionsNonReservees.remove(r.getReunion());
                }

                //On enlève les salles déjà prises des salles possible
                for(Map.Entry<Reunion,List<Salle>> rnr : ReunionsNonReservees.entrySet()){
                    rnr.getValue().removeIf(x -> reservations.stream().anyMatch(y -> y.getSalle()==x &&( y.getReunion().getCreneau()==rnr.getKey().getCreneau() || y.getReunion().getCreneau()==rnr.getKey().getCreneau()+1 || y.getReunion().getCreneau()==rnr.getKey().getCreneau()-1)));
                }
            }

            //Quand il n'y a plus de réunions avec une seule salle possible, on cherche celles avec le moins de salles
            int min = Collections.min(ReunionsNonReservees.values().stream().map(List::size).collect(Collectors.toList()));
            for(Map.Entry<Reunion,List<Salle>> rnr : ReunionsNonReservees.entrySet()){
                if(rnr.getValue().size()==min){
                    //Parmi les salles, on choisit la salle avec la plus petite capacité
                    Reunion r =  rnr.getKey();
                    Salle s = rnr.getValue().stream().sorted().collect(Collectors.toList()).get(0);
                    //Si la salle est disponible, on crée une nouvelle réservation
                    if(isSalleDisponible(reservations, s,r.getCreneau()) && isEquipementDisponible(reservations, r,s,e.getEquipementsLibres())){
                        Reservation newRes = new Reservation(r,s);
                        newRes.setEquipementReserve(new HashMap<>(Map.of(TypeEquipement.Ecran, 0, TypeEquipement.Pieuvre, 0, TypeEquipement.Webcam, 0, TypeEquipement.Tableau, 0)));
                        //On ajoute l'équipement manquant à la salle
                        switch(r.getType()) {
                            case VC:
                                if (s.getEquipements().get(TypeEquipement.Ecran) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Ecran, 1);
                                if (s.getEquipements().get(TypeEquipement.Pieuvre) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Pieuvre, 1);
                                if (s.getEquipements().get(TypeEquipement.Webcam) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Webcam, 1);
                                break;
                            case SPEC:
                                if (s.getEquipements().get(TypeEquipement.Tableau) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Tableau, 1);
                                break;
                            case RS:
                                break;
                            case RC:
                                if (s.getEquipements().get(TypeEquipement.Ecran) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Ecran, 1);
                                if (s.getEquipements().get(TypeEquipement.Pieuvre) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Pieuvre, 1);
                                if (s.getEquipements().get(TypeEquipement.Tableau) == 0)
                                    newRes.getEquipementReserve().put(TypeEquipement.Tableau, 1);
                                break;
                        }
                        reservations.add(newRes);
                    }else{
                        //Si la salle n'est pas dispo ou qu'on ne peut pas y ajouter l'équipement, on la retire des salles possibles
                        rnr.getValue().remove(s);
                    }
                }
            }

            //On enlève les réunions assignées de la Map
            for(Reservation r : reservations){
                ReunionsNonReservees.remove(r.getReunion());
            }

            //On enlève les salles déjà prises des salles possible
            for(Map.Entry<Reunion,List<Salle>> rnr : ReunionsNonReservees.entrySet()){
                rnr.getValue().removeIf(x -> reservations.stream().anyMatch(y -> y.getSalle()==x &&( y.getReunion().getCreneau()==rnr.getKey().getCreneau() || y.getReunion().getCreneau()==rnr.getKey().getCreneau()+1 || y.getReunion().getCreneau()==rnr.getKey().getCreneau()-1)));
            }
        }
        
        
		return reservations;
	}

	//Vérifie si la salle est réservée à ce créneau (ou aux créneaux adjacents à cause des restrictions COVID)
    public boolean isSalleDisponible(List<Reservation> reservations, Salle s, int creneau){
        for(Reservation r : reservations){
            if(r.getSalle()==s && (r.getReunion().getCreneau()==creneau || r.getReunion().getCreneau()-1==creneau || r.getReunion().getCreneau()+1==creneau)) {
                return false;
            }
        }
        return true;
    }

    //Vérifie si il reste assez d'équipement disponible à ce créneau pour pouvoir tenir la réunion dans cette salle.
    public boolean isEquipementDisponible(List<Reservation> reservations,Reunion r, Salle s, Map<TypeEquipement,Integer> equipementsLibres){
        int creneau = r.getCreneau();
        boolean result = true;
        Map<TypeEquipement,Integer> equipementReserveCeCreneau = new HashMap<>();
        for(TypeEquipement te : TypeEquipement.values()){
            int total = 0;
            for(Reservation res : reservations){
                if(res.getSalle()==null)
                    continue;
                if(res.getReunion().getCreneau() == creneau){
                    total += res.getEquipementReserve().get(te);
                }
            }
            equipementReserveCeCreneau.put(te,total);
        }
        switch(r.getType()){
            case VC:
                if (s.getEquipements().get(TypeEquipement.Ecran) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Ecran)>= equipementsLibres.get(TypeEquipement.Ecran))
                    result = false;
                if (s.getEquipements().get(TypeEquipement.Pieuvre) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Pieuvre)>= equipementsLibres.get(TypeEquipement.Pieuvre))
                    result = false;
                if (s.getEquipements().get(TypeEquipement.Webcam) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Webcam)>= equipementsLibres.get(TypeEquipement.Webcam))
                    result = false;
                break;
            case SPEC:
                if (s.getEquipements().get(TypeEquipement.Tableau) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Tableau)>= equipementsLibres.get(TypeEquipement.Tableau))
                    result = false;
                break;
            case RS:
                break;
            case RC:
                if (s.getEquipements().get(TypeEquipement.Ecran) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Ecran)>= equipementsLibres.get(TypeEquipement.Ecran))
                    result = false;
                if (s.getEquipements().get(TypeEquipement.Pieuvre) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Pieuvre)>= equipementsLibres.get(TypeEquipement.Pieuvre))
                    result = false;
                if (s.getEquipements().get(TypeEquipement.Tableau) == 0 && equipementReserveCeCreneau.get(TypeEquipement.Tableau)>= equipementsLibres.get(TypeEquipement.Tableau))
                    result = false;
        }
        return result;
    }

}
