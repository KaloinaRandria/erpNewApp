package mg.working.model.RH.dto;

public class EmployeeSalaryDTO {
    private String mois;
    private int refEmploye;
    private double salaireBase;
    private String salaire;

    // Constructeur
    public EmployeeSalaryDTO(String mois, int refEmploye, double salaireBase, String salaire) {
        this.mois = mois;
        this.refEmploye = refEmploye;
        this.salaireBase = salaireBase;
        this.salaire = salaire;
    }

    // Getters et Setters
    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public int getRefEmploye() {
        return refEmploye;
    }

    public void setRefEmploye(int refEmploye) {
        this.refEmploye = refEmploye;
    }

    public double getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(double salaireBase) {
        this.salaireBase = salaireBase;
    }

    public String getSalaire() {
        return salaire;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }
}
