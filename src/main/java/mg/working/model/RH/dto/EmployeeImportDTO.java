package mg.working.model.RH.dto;

import java.util.Date;
import java.text.SimpleDateFormat;

public class EmployeeImportDTO {
    private String ref;
    private String nom;
    private String prenom;
    private String genre;
    private String dateEmbauche;
    private String dateNaissance;
    private String company;


    public void parseDate(String dateString)throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        try {
            Date date = formatter.parse(dateString);
            System.out.println("date: " + date.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String mapGenre(String genre) {
        return switch (genre.toLowerCase()) {
            case "masculin" -> "Male";
            case "feminin" -> "Female";
            default -> "Other";
        };
    }

    // Getters et Setters

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        try {
            this.genre = mapGenre(genre);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(String dateEmbauche) throws Exception {
        try {
            parseDate(dateEmbauche);
            this.dateEmbauche = dateEmbauche;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) throws Exception {
        try {
            parseDate(dateNaissance);
            this.dateNaissance = dateNaissance;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
