package mg.working.model.RH.salaire;

import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;

import java.util.List;

public class StatistiqueSalaire {

    private String month;
    private List<Earning> earnings;     // cumulés par type
    private List<Deduction> deductions; // cumulés par type
    private double grossTotal;
    private double netTotal;
    private double deductionTotal;

    // Getters & setters
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public List<Earning> getEarnings() { return earnings; }
    public void setEarnings(List<Earning> earnings) { this.earnings = earnings; }

    public List<Deduction> getDeductions() { return deductions; }
    public void setDeductions(List<Deduction> deductions) { this.deductions = deductions; }

    public double getGrossTotal() { return grossTotal; }
    public void setGrossTotal(double grossTotal) { this.grossTotal = grossTotal; }

    public double getNetTotal() { return netTotal; }
    public void setNetTotal(double netTotal) { this.netTotal = netTotal; }

    public double getDeductionTotal() { return deductionTotal; }
    public void setDeductionTotal(double deductionTotal) { this.deductionTotal = deductionTotal; }
}
