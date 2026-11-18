# TP6 – Service Banque en CORBA

**Matière :** Développement d'applications réparties – LSI3 – FSS 2026/2027
**Étudiant :** Med Hassan Ben Adi

## Description
Application client-serveur en Java (JDK 8) avec CORBA pour la gestion de comptes bancaires :
créer un compte, verser, retirer, consulter un compte, lister les comptes, convertir € → DT.

## Structure
```
TP6_CorbaBanque/
├── TP6_CorbaBanque_Server/src/   Banque.idl, jndi.properties, service/BanqueImpl, corbaServer/BanqueServer
├── TP6_CorbaBanque_Client/src/   Banque.idl, jndi.properties, corbaClient/BanqueClient
└── run.sh
```

## Étape 1 – Préparation de l'espace de travail
Dossier de travail, IDE, vérification de `java -version` (1.8) et du PATH.

> 📸 *capture : `java -version` et `javac -version`*

## Étape 2 – Serveur
1. Projet `TP6_CorbaBanque_Server`
2. Interface `Banque.idl` (module `corbaBanque`, struct `Compte`, typedef `tabComptes`, interface `IBanqueRemote`)
3. Compilation : `idlj -fall -v Banque.idl`
4. Servant `service.BanqueImpl` (hérite de `IBanqueRemotePOA`)
5. `jndi.properties` (port 1050, voir remarque plus bas)
6. `corbaServer.BanqueServer` : ORB → RootPOA + POAManager → JNDI → servant → `rebind` → `orb.run()`

> 📸 *captures : IDL, fichiers générés après idlj, BanqueImpl, jndi.properties, BanqueServer*

## Étape 3 – Client
1. Projet `TP6_CorbaBanque_Client`, copie de `Banque.idl` et `jndi.properties`, recompilation IDL
2. `corbaClient.BanqueClient` : JNDI → `lookup` → `IBanqueRemoteHelper.narrow` (Stub) → appel de chaque méthode

> 📸 *captures : fichiers générés côté client, BanqueClient*

## Étape 4 – Exécution
Ordre : annuaire → serveur → client.
```bash
tnameserv -ORBInitialPort 1050
java -cp bin corbaServer.BanqueServer
java -cp bin corbaClient.BanqueClient
```
> 📸 *captures : tnameserv, serveur démarré, sortie du client*

**Remarque port :** l'énoncé utilise le port 900, mais sous Linux il est privilégié (< 1024) ; j'ai utilisé 1050, à la fois dans `tnameserv -ORBInitialPort` et dans `jndi.properties`.

## Synthèse
*(à rédiger avec tes mots : rôle de l'IDL, du Stub/Skeleton, de l'ORB, du POA, du Naming Service, difficultés rencontrées, résultats)*
