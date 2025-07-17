package org.example.courtsystem.model.people;

// Abstract CourtMember: represents people directly participating in court decisions (Lawyer, Judge)
public abstract class CourtMember extends Person {
    private int yearsOfService;

    public CourtMember(String name, int yearsOfService) {
        super(name);
        this.yearsOfService = yearsOfService;
    }

    public int getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(int yearsOfService) {
        this.yearsOfService = yearsOfService;
    }

    public abstract void performDuty();
}