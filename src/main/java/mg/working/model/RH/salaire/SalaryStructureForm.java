package mg.working.model.RH.salaire;

import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;

import java.util.List;

public class SalaryStructureForm {
    private String name;
    private List<Earning> earnings;
    private List<Deduction> deductions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Earning> getEarnings() {
        return earnings;
    }

    public void setEarnings(List<Earning> earnings) {
        this.earnings = earnings;
    }

    public List<Deduction> getDeductions() {
        return deductions;
    }

    public void setDeductions(List<Deduction> deductions) {
        this.deductions = deductions;
    }
}
