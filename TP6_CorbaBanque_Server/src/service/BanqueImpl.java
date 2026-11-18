package service;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemotePOA;
import java.util.LinkedHashMap;
import java.util.Map;
import org.omg.CORBA.BAD_PARAM;

public class BanqueImpl extends IBanqueRemotePOA {

    // Taux indicatif : 1 EUR = 3.4 DT (a ajuster si besoin)
    private static final double TAUX_EUR_DT = 3.4;

    private final Map<Integer, Compte> comptes = new LinkedHashMap<Integer, Compte>();

    private Compte chercher(int code) {
        Compte c = comptes.get(code);
        if (c == null) {
            throw new BAD_PARAM("Compte introuvable : " + code);
        }
        return c;
    }

    @Override
    public synchronized void creerCompte(Compte cpte) {
        if (cpte.solde < 0) {
            throw new BAD_PARAM("Solde initial negatif");
        }
        if (comptes.containsKey(cpte.code)) {
            throw new BAD_PARAM("Le compte " + cpte.code + " existe deja");
        }
        comptes.put(cpte.code, new Compte(cpte.code, cpte.solde));
        System.out.println("[Serveur] Compte cree : " + cpte.code + " (solde " + cpte.solde + ")");
    }

    @Override
    public synchronized void verser(float mt, int code) {
        if (mt <= 0) {
            throw new BAD_PARAM("Montant invalide : " + mt);
        }
        Compte c = chercher(code);
        c.solde += mt;
        System.out.println("[Serveur] Versement de " + mt + " sur " + code);
    }

    @Override
    public synchronized void retirer(float mt, int code) {
        if (mt <= 0) {
            throw new BAD_PARAM("Montant invalide : " + mt);
        }
        Compte c = chercher(code);
        if (c.solde < mt) {
            throw new BAD_PARAM("Solde insuffisant sur le compte " + code);
        }
        c.solde -= mt;
        System.out.println("[Serveur] Retrait de " + mt + " sur " + code);
    }

    @Override
    public synchronized Compte getCompte(int code) {
        Compte c = chercher(code);
        return new Compte(c.code, c.solde);
    }

    @Override
    public synchronized Compte[] getComptes() {
        Compte[] tab = new Compte[comptes.size()];
        int i = 0;
        for (Compte c : comptes.values()) {
            tab[i++] = new Compte(c.code, c.solde);
        }
        return tab;
    }

    @Override
    public double conversion(float mt) {
        return mt * TAUX_EUR_DT;
    }
}
