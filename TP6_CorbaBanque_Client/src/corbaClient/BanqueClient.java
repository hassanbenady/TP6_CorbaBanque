package corbaClient;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;

public class BanqueClient {

    private static void afficher(Compte c) {
        System.out.println("   -> Compte " + c.code + " | solde = " + c.solde);
    }

    public static void main(String[] args) {
        try {
            // a. Proprietes JNDI (jndi.properties + ORB)
            ORB orb = ORB.init(args, null);
            Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put("java.naming.corba.orb", orb);
            Context ctx = new InitialContext(env);

            // b. Reference de l'objet distant
            org.omg.CORBA.Object obj = (org.omg.CORBA.Object) ctx.lookup("BanqueService");

            // c. narrow -> Stub
            IBanqueRemote banque = IBanqueRemoteHelper.narrow(obj);

            // d. Test de chaque methode
            System.out.println("1) creerCompte");
            banque.creerCompte(new Compte(1, 1000f));
            banque.creerCompte(new Compte(2, 250.5f));
            banque.creerCompte(new Compte(3, 0f));

            System.out.println("2) verser 500 sur le compte 1");
            banque.verser(500f, 1);
            afficher(banque.getCompte(1));

            System.out.println("3) retirer 200 du compte 2");
            banque.retirer(200f, 2);
            afficher(banque.getCompte(2));

            System.out.println("4) getCompte(3)");
            afficher(banque.getCompte(3));

            System.out.println("5) getComptes");
            for (Compte c : banque.getComptes()) {
                afficher(c);
            }

            System.out.println("6) conversion 100 EUR -> DT");
            System.out.println("   -> 100 EUR = " + banque.conversion(100f) + " DT");

            System.out.println("7) cas d'erreur");
            try {
                banque.retirer(99999f, 1);
            } catch (SystemException e) {
                System.out.println("   -> Retrait refuse : " + e.getMessage());
            }
            try {
                banque.getCompte(42);
            } catch (SystemException e) {
                System.out.println("   -> " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Erreur client : " + e);
            e.printStackTrace();
        }
    }
}
