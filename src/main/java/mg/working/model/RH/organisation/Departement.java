package mg.working.model.RH.organisation;

public class Departement {

    private String name;
    private String owner;
    private String creation;
    private String modified;
    private String modified_by;
    private int docstatus;
    private int idx;
    private String department_name;
    private String parent_department;
    private String company;
    private int is_group;
    private int disabled;
    private int lft;
    private int rgt;
    private String old_parent;
    private String payroll_cost_center;
    private String leave_block_list;

    // --- Getters & Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public int getDocstatus() {
        return docstatus;
    }

    public void setDocstatus(int docstatus) {
        this.docstatus = docstatus;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getParent_department() {
        return parent_department;
    }

    public void setParent_department(String parent_department) {
        this.parent_department = parent_department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getIs_group() {
        return is_group;
    }

    public void setIs_group(int is_group) {
        this.is_group = is_group;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public int getLft() {
        return lft;
    }

    public void setLft(int lft) {
        this.lft = lft;
    }

    public int getRgt() {
        return rgt;
    }

    public void setRgt(int rgt) {
        this.rgt = rgt;
    }

    public String getOld_parent() {
        return old_parent;
    }

    public void setOld_parent(String old_parent) {
        this.old_parent = old_parent;
    }

    public String getPayroll_cost_center() {
        return payroll_cost_center;
    }

    public void setPayroll_cost_center(String payroll_cost_center) {
        this.payroll_cost_center = payroll_cost_center;
    }

    public String getLeave_block_list() {
        return leave_block_list;
    }

    public void setLeave_block_list(String leave_block_list) {
        this.leave_block_list = leave_block_list;
    }
}
