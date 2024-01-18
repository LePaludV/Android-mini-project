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

Les ***photos*** sont des strings qui correspondent à un requête **GET**  pour aller chercher la photo
C'est un tableau trié, la premère photo correspond à la photo d'accueil.


## Avis

Un avis est décrit de la façon suivante :

    {
	    id:number,
	    utilisateur:string,
	    avis:string,
	    note: float,
	    photos: Array<string>
    }

Les ***photos*** sont des strings qui correspondent à un requête **GET**  pour aller chercher la photo
C'est un tableau trié, la premère photo correspond à la photo d'accueil.

## Photos
Une requête **GET** *photos/:id* pour récupérer une photo
Une requête **POST** *photos* pour créer une photo
