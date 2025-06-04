package mg.working.model.RH.dto;

public class ComponentAndStructureDTO {
    private String salaryStructure;
    private String name;
    private String abbr;
    private String type;
    private String valeur;
    private String company;

    // Getters et Setters

    public String getSalaryStructure() {
        return salaryStructure;
    }

    public void setSalaryStructure(String salaryStructure) {
        this.salaryStructure = salaryStructure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
