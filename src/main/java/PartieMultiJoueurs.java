import bowling.IPartieMultiJoueurs;
import bowling.PartieMonoJoueur;

import java.util.HashMap;
import java.util.Map;

public class PartieMultiJoueurs implements IPartieMultiJoueurs{
    private String[] nomsDesJoueurs;
    private Map<String, PartieMonoJoueur> parties;
    private int joueurCourant;
    private boolean partieDemarree;

    public PartieMultiJoueurs() {
        parties = new HashMap<>();
        partieDemarree = false;
    }

    @Override
    public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
        if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
            throw new IllegalArgumentException("Il faut au moins un joueur");
        }
        this.nomsDesJoueurs = nomsDesJoueurs;
        parties.clear();
        for (String nom : nomsDesJoueurs) {
            parties.put(nom, new PartieMonoJoueur());
        }
        joueurCourant = 0;
        partieDemarree = true;
        return prochainTirMessage();
    }

    @Override
    public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
        if (!partieDemarree) {
            throw new IllegalStateException("La partie n'est pas démarrée.");
        }
        String nomJoueur = nomsDesJoueurs[joueurCourant];
        PartieMonoJoueur partie = parties.get(nomJoueur);
        partie.enregistreLancer(nombreDeQuillesAbattues);
        // Si le joueur a fini son tour, passer au joueur suivant
        if (partie.estTerminee() || partie.numeroTourCourant() == 0 || partie.numeroProchainLancer() == 1) {
            joueurCourant = (joueurCourant + 1) % nomsDesJoueurs.length;
        }
        // Vérifier si tous les joueurs ont terminé
        boolean tousTermines = true;
        for (String nom : nomsDesJoueurs) {
            if (!parties.get(nom).estTerminee()) {
                tousTermines = false;
                break;
            }
        }
        if (tousTermines) {
            partieDemarree = false;
            return "Partie terminée";
        }
        return prochainTirMessage();
    }

    @Override
    public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
        if (!parties.containsKey(nomDuJoueur)) {
            throw new IllegalArgumentException("Ce joueur ne joue pas dans cette partie");
        }
        return parties.get(nomDuJoueur).score();
    }

    private String prochainTirMessage() {
        String nomJoueur = nomsDesJoueurs[joueurCourant];
        PartieMonoJoueur partie = parties.get(nomJoueur);
        int tour = partie.numeroTourCourant();
        int boule = partie.numeroProchainLancer();
        return String.format("Prochain tir : joueur %s, tour n° %d, boule n° %d", nomJoueur, tour, boule);
    }
}
