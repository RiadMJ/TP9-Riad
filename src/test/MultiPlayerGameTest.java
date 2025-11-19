package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiPlayerGameTest {

    private PartieMultiJoueurs partie;
    private String[] joueurs = {"Alice", "Bob", "Charlie"};

    @BeforeEach
    public void setUp() {
        partie = new PartieMultiJoueurs();
    }

    @Test
    void demarrageEtPremierTir() {
        String msg = partie.demarreNouvellePartie(joueurs);
        assertTrue(msg.contains("Prochain tir : joueur Alice, tour n° 1, boule n° 1"));
    }

    @Test
    void toutDansLaRigoleMulti() {
        partie.demarreNouvellePartie(joueurs);
        for (int i = 0; i < 20 * joueurs.length; i++) {
            String msg = partie.enregistreLancer(0);
        }
        for (String joueur : joueurs) {
            assertEquals(0, partie.scorePour(joueur));
        }
    }

    @Test
    void uneQuilleChaqueLancerMulti() {
        partie.demarreNouvellePartie(joueurs);
        for (int i = 0; i < 20 * joueurs.length; i++) {
            partie.enregistreLancer(1);
        }
        for (String joueur : joueurs) {
            assertEquals(20, partie.scorePour(joueur));
        }
    }

    @Test
    void testPerfectGameMulti() {
        partie.demarreNouvellePartie(joueurs);
        for (int i = 0; i < 12 * joueurs.length; i++) {
            partie.enregistreLancer(10);
        }
        for (String joueur : joueurs) {
            assertEquals(300, partie.scorePour(joueur));
        }
    }

    @Test
    void testAlternanceJoueurs() {
        partie.demarreNouvellePartie(joueurs);
        assertTrue(partie.enregistreLancer(5).contains("Bob"));
        assertTrue(partie.enregistreLancer(3).contains("Charlie"));
        assertTrue(partie.enregistreLancer(7).contains("Alice"));
    }

    @Test
    void testExceptionDemarrage() {
        assertThrows(IllegalArgumentException.class, () -> partie.demarreNouvellePartie(new String[]{}));
        assertThrows(IllegalArgumentException.class, () -> partie.demarreNouvellePartie(null));
    }

    @Test
    void testExceptionScorePour() {
        partie.demarreNouvellePartie(joueurs);
        assertThrows(IllegalArgumentException.class, () -> partie.scorePour("Inconnu"));
    }

    @Test
    void testExceptionLancerSansPartie() {
        assertThrows(IllegalStateException.class, () -> partie.enregistreLancer(5));
    }

    @Test
    void testFinDePartieMulti() {
        partie.demarreNouvellePartie(joueurs);
        String msg = "";
        for (int i = 0; i < 20 * joueurs.length; i++) {
            msg = partie.enregistreLancer(0);
        }
        assertEquals("Partie terminée", msg);
    }

}