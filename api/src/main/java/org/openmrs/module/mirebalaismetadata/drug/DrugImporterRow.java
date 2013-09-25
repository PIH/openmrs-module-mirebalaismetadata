package org.openmrs.module.mirebalaismetadata.drug;

/**
 *
 */
public class DrugImporterRow {

    public static final String[] FIELD_COLUMNS = { "openBoxesCode", "productName" /*, "concept" */ };

    private String openBoxesCode;

    private String productName;

    private String concept;

    public String getOpenBoxesCode() {
        return openBoxesCode;
    }

    public void setOpenBoxesCode(String openBoxesCode) {
        this.openBoxesCode = openBoxesCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }
}
