package helloandroid.ut3.mini_projet.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Restaurant {

    String id;
    String nom;
    String address;
    String[] photos;
    GeoPoint coordinates;
    String description;
    Map<String, ArrayList<Long>> horaires;
    String type;
    float note;


    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAddress() {
        return address;
    }

    public String[] getPhotos() {
        return photos;
    }

    public org.osmdroid.util.GeoPoint getCoordinates() {
        return new org.osmdroid.util.GeoPoint(coordinates.getLatitude(),coordinates.getLongitude());
    }

    public String getDescription() {
        return description;
    }

    public Map<String, ArrayList<Long>>  getHoraires() {
        return horaires;
    }

    public String getType() {
        return type;
    }

    public float getNote() {
        return note;
    }

    public Restaurant(String id, String nom, String address, String[] photos, GeoPoint coordinates, String description, Map<String, ArrayList<Long>>  horaires, String type, float note) {
        this.id = id;
        this.nom = nom;
        this.address = address;
        this.photos = photos;
        this.coordinates = coordinates;
        this.description = description;
        this.horaires = horaires;
        this.type = type;
        this.note = note;
    }

    public boolean isOpen() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String dayOfWeekString;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                dayOfWeekString = "Lundi";
                break;
            case Calendar.TUESDAY:
                dayOfWeekString = "Mardi";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekString = "Mercredi";
                break;
            case Calendar.THURSDAY:
                dayOfWeekString = "Jeudi";
                break;
            case Calendar.FRIDAY:
                dayOfWeekString = "Vendredi";
                break;
            case Calendar.SATURDAY:
                dayOfWeekString = "Samedi";
                break;
            case Calendar.SUNDAY:
                dayOfWeekString = "Dimanche";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }

        ArrayList<Long> hours;
        if (horaires.containsKey(dayOfWeekString)) {
            hours = horaires.get(dayOfWeekString);
        } else {
            hours = new ArrayList<>();
        }

        for (int i = 0; i < hours.size(); i += 2) {
            long openingHour = hours.get(i);
            long closingHour = hours.get(i + 1);

            if (hour >= openingHour && hour < closingHour) {
                return true;
            }
        }

        return false;
    }


    public String getHoursString() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String dayOfWeekString;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                dayOfWeekString = "Lundi";
                break;
            case Calendar.TUESDAY:
                dayOfWeekString = "Mardi";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekString = "Mercredi";
                break;
            case Calendar.THURSDAY:
                dayOfWeekString = "Jeudi";
                break;
            case Calendar.FRIDAY:
                dayOfWeekString = "Vendredi";
                break;
            case Calendar.SATURDAY:
                dayOfWeekString = "Samedi";
                break;
            case Calendar.SUNDAY:
                dayOfWeekString = "Dimanche";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }

        ArrayList<Long> hours;
        if (horaires.containsKey(dayOfWeekString)) {
            hours = horaires.get(dayOfWeekString);
        } else {
            hours = new ArrayList<>();
        }

        StringBuilder hoursString = new StringBuilder();

        for (int i = 0; i < hours.size(); i += 2) {
            Long openingHour = hours.get(i);
            Long closingHour = hours.get(i + 1);

            if (i > 0) {
                hoursString.append(", ");
            }

            hoursString.append(openingHour).append("h - ").append(closingHour).append("h");
        }

        return hoursString.toString();
    }

}
