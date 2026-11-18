package corbaServer;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import service.BanqueImpl;

public class BanqueServer {

    public static final String NOM_SERVICE = "BanqueService";

    public static void main(String[] args) {
        try {
            // a. Initialiser l'ORB
            ORB orb = ORB.init(args, null);

            // b. Reference sur le RootPOA + activation du POAManager
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            // c. Configurer l'annuaire JNDI (jndi.properties + ORB partage)
            Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put("java.naming.corba.orb", orb);
            Context ctx = new InitialContext(env);

            // d. Creer le servant
            BanqueImpl banque = new BanqueImpl();

            // e. Enregistrer sa reference dans l'annuaire
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(banque);
            ctx.rebind(NOM_SERVICE, ref);
            System.out.println("Serveur Banque pret : '" + NOM_SERVICE + "' enregistre dans l'annuaire.");

            // f. Attendre les requetes des clients
            orb.run();
        } catch (Exception e) {
            System.err.println("Erreur serveur : " + e);
            e.printStackTrace();
        }
    }
}
