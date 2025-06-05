package mg.working.model.RH.salaire;

import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;

import java.time.LocalDate;
import java.util.List;

public class SalarySlip {
    private String name;                  // Ex: Sal Slip/HR-EMP-00003/00001
    private String employee;             // Ex: HR-EMP-00003
    private String employeeName;         // Ex: Dupont
    private String company;              // Ex: KALOINA
    private String department;           // Ex: Ventes
    private String designation;          // Ex: Administrative Assistant
    private LocalDate postingDate;       // Ex: 2025-06-02
    private String currency;             // Ex: EUR
    private LocalDate startDate;         // Ex: 2025-06-01
    private LocalDate endDate;           // Ex: 2025-06-30
    private double grossPay;             // Ex: 1000.0
    private double totalDeduction;       // Ex: 5000.0
    private double netPay;               // Ex: -4000.0
    private String status;               // Ex: Draft
    private String salaryStructure;
    private List<Earning> earnings;
    private List<Deduction> deductions;


    // Getters and setters (ou @Data avec Lombok)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDate postingDate) {
        this.postingDate = postingDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalaryStructure() {
        return salaryStructure;
    }

    public void setSalaryStructure(String salaryStructure) {
        this.salaryStructure = salaryStructure;
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

