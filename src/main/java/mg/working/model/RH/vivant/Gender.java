package mg.working.model.RH.vivant;

public class Gender {
    private String name;
    private String owner;
    private String creation;
    private String modified;
    private String modified_by;
    private int docstatus;
    private int idx;
    private String gender;

    // Getters et setters
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

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}

