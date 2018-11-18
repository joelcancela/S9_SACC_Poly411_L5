# S9_SACC_Poly411_L5

## Team members :

    - Bounouas Nassim
    - Cancela Vaz Joël
    - Mortara Johann
    - Novac Pierre-Emmanuel
    - Rousseau Nikita

## Main link

https://polar-winter-218511.appspot.com/

## What's done

- Chaque utilisateur doit créer un compte (email comme identifiant). Lien: https://polar-winter-218511.appspot.com/signup.jsp

- Un utilisateur se rend sur une page d'accueil où il peut uploader des fichiers. Lien: https://polar-winter-218511.appspot.com/upload.jsp

- Upload:
    - Chaque fichier uploadé a un identifiant unique qui peut être utilisé par d'autres utilisateurs pour le télécharger.

    - À chaque fois qu'un fichier est uploadé, un email récapitulatif avec un lien de téléchargement est envoyé à l'utilisateur
    - 1MB uploadé = 1 pt gagné
- Download:
    - Une requête est envoyée et par retour d'email on reçoit un lien vers le fichier demandé
    - Ce lien est valide 5 minutes
- Un système de score permet de récompenser les utilisateurs qui uploadent des fichiers populaires
- Un leaderboard est accessible sous forme de page web. Lien: https://polar-winter-218511.appspot.com/users

## Notes/Bugs

- L'identifiant utilisé dans toutes les requêtes pour identifier l'utilisateur est son nom
- L'identifiant utilisé pour identifier un fichier est son nom.
- Possibilité de "s'auto-boost" en téléchargeant ses propres fichiers.

## TODO

- Déduplication des fichiers
- Scénarios de test (partie Scénarios de test)
- Verifier l'API admin (partie Particularités pour le projet)

## Commands

### Launch DB

```bash
gcloud beta emulators datastore start # Local only
```

### Launch server

```bash
launch_server.cmd
```

### Create Bucket on Google Cloud Storage

```bash
gsutil mb gs://polar-winter-218511
gsutil defacl set public-read gs://polar-winter-218511
```

### Push an update to AppEngine

```bash
mvn appengine:update
```