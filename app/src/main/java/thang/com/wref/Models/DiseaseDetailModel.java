package thang.com.wref.Models;

public class DiseaseDetailModel {
    private int id;
    private String diseaseName;
    private String cause;
    private String symptom;
    private String prevention;

    public DiseaseDetailModel(int id, String diseaseName, String cause, String symptom, String prevention) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.cause = cause;
        this.symptom = symptom;
        this.prevention = prevention;
    }

    @Override
    public String toString() {
        return "DiseaseDetailModel{" +
                "id=" + id +
                ", diseaseName='" + diseaseName + '\'' +
                ", cause='" + cause + '\'' +
                ", symptom='" + symptom + '\'' +
                ", prevention='" + prevention + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }
}
