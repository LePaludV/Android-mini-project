# InsTable

### Developed by: Doré Romain, Le Palud Valentin, Lascombes Yven

### M2 SDL at Paul Sabatier University Toulouse, France

## Fonctionnalités

- Affichage de la liste des restaurants sous forme de liste ET de carte
- Vue détaillée pour chaque restaurant
- Réservation de table en précisant la date et le nombre de personnes
- Possibilité de laisser un avis avec photo
- Retouche de photo avec des filtres et des stickers

## Technologies utilisées

- Firebase pour le backend :
  - Firestore Database pour le stockage des données de restaurants et des avis
  - Firebase Storage pour le stockage des photos
- CameraX pour la prise de photo et l'accès à l'appareil photo
- Glide pour l'affichage des images
- OSMDROID et le provider MAPNIK pour l'affichage de la carte
- Utilisation de la dépendance play-services-location pour une localisation précise

## Description des données

### Restaurant

Un restaurant est décrit de la façon suivante :

```
{
    id: string,
    nom: string,
    address : string,
    photos: Array<string>, // nom des photos dans le Storage
    coordinates: geopoint,
    description: string,
    horaire: Map<string,Array<number>>, //key: jour, value : horaire d'ouverture
    type: string,
    note_moyenne: number
}
```

### Avis

Un avis est décrit de la façon suivante :

```
{
    id: number,
    username: string,
    review: string,
    note: number,
    photos: Array<string>, // nom des photos dans le Storage
    date: timestamp,
    restaurant_id: string
}
```
