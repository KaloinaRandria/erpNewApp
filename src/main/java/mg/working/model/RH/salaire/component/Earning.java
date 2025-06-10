package mg.working.model.RH.salaire.component;

public class Earning {
    private String salary_component;
    private Double amount;
    private double year_to_date;
    private boolean selected;
    private String formula;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    // Getters et Setters

    public String getSalary_component() {
        return salary_component;
    }

    public void setSalary_component(String salary_component) {
        this.salary_component = salary_component;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public double getYear_to_date() {
        return year_to_date;
    }

    public void setYear_to_date(double year_to_date) {
        this.year_to_date = year_to_date;
    }
}

