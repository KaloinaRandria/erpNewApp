package mg.working.model.RH.vivant;

public class Employe {
    private String employee_name;
    private String company;
    private String date_of_joining;
    private String gender;
    private String status;
    private String designation;
    private String department;
    private String employment_type;

    public String getEmployee_name() {
        return employee_name;
    }
    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getDate_of_joining() {
        return date_of_joining;
    }
    public void setDate_of_joining(String date_of_joining) {
        this.date_of_joining = date_of_joining;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getEmployment_type() {
        return employment_type;
    }
    public void setEmployment_type(String employment_type) {
        this.employment_type = employment_type;
    }
    public Employe(String employee_name, String company, String date_of_joining, String gender, String status,
            String designation, String department, String employment_type) {
        this.employee_name = employee_name;
        this.company = company;
        this.date_of_joining = date_of_joining;
        this.gender = gender;
        this.status = status;
        this.designation = designation;
        this.department = department;
        this.employment_type = employment_type;
    }
    public Employe() {
    }
    
    
}
