package mg.working.model.RH.salaire.component;

public class SalaryComponent {
    private String salary_component;
    private Double amount;
    private double year_to_date;
    private boolean selected;
    private String formula;


    public SalaryComponent() {}
    public SalaryComponent(String salary_component, Double amount, double year_to_date, boolean selected, String formula) {
        this.salary_component = salary_component;
        this.amount = amount;
        this.year_to_date = year_to_date;
        this.selected = selected;
        this.formula = formula;
    }

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
