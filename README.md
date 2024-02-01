# Android-mini-project

# Application de restauration

Hello ici la description du projet

# Infos

Back-end : Firebase

## Restaurant

Un restaurant est décrit de la façon suivante :

    {
        id: number,
    	nom: string,
    	address : string,
    	photos: Array<string>,
    	lat: float,
    	long: float,
    	description: string,
    	horaire:string,
    	type:string,
    	note_moyenne: float
    }

Les **_photos_** sont des strings qui correspondent à un requête **GET** pour aller chercher la photo
C'est un tableau trié, la premère photo correspond à la photo d'accueil.

### GET ALL

[GET] https://firestore.googleapis.com/v1/projects/androidminiproject-a2fa8/databases/(default)/documents/restaurants?key={apikey}

### GET UNITAIRE

[GET] https://firestore.googleapis.com/v1/projects/androidminiproject-a2fa8/databases/(default)/documents/restaurants/{id}?key={apikey}

## Avis

Un avis est décrit de la façon suivante :

    {
        id:number,
        utilisateur:string,
        avis:string,
        note: float,
        photos: Array<string>
    }

### GET ALL

[GET] https://firestore.googleapis.com/v1/projects/androidminiproject-a2fa8/databases/(default)/documents/avis?key={apikey}

### GET UNITAIRE

[GET] https://firestore.googleapis.com/v1/projects/androidminiproject-a2fa8/databases/(default)/documents/avis/{id}?key={apikey}

## Photos

L'id des photos d'un restaurant ce trouve dans `restaurant.photos` Array de string

**L'élément 0 correspond à la photo principal du restaurant**

[GET] **https://firebasestorage.googleapis.com/v0/b/androidminiproject-a2fa8.appspot.com/o/{nom_photo}?alt=media**
