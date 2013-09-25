package org.openmrs.module.mirebalaismetadata.drug;

import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.emrapi.concept.EmrConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Component
public class DrugImporter {

    @Autowired
    private EmrConceptService emrConceptService;

    @Autowired
    private ConceptService conceptService;

    public DrugImporter() {
    }

    private CellProcessor[] getCellProcessors() {
        return new CellProcessor[] {
                new Trim(),
                new Trim() //,
                //new Optional(new Trim())
        };
    }

    /**
     * For unit tests -- normally this is autowired
     * @param conceptService
     */
    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public void setEmrConceptService(EmrConceptService emrConceptService) {
        this.emrConceptService = emrConceptService;
    }

    public ImportNotes verifySpreadsheet(Reader csvFileReader) throws IOException {
        ImportNotes notes = new ImportNotes();

        CsvBeanReader csv = null;
        try {
            csv = new CsvBeanReader(csvFileReader, CsvPreference.EXCEL_PREFERENCE);
            csv.getHeader(true);
            CellProcessor[] cellProcessors = getCellProcessors();
            Set<String> openBoxesCodes = new HashSet<String>();
            Set<String> productNames = new HashSet<String>();
            while (true) {
                DrugImporterRow row = csv.read(DrugImporterRow.class, DrugImporterRow.FIELD_COLUMNS, cellProcessors);
                if (row == null) {
                    break;
                }

                if (openBoxesCodes.contains(row.getOpenBoxesCode())) {
                    notes.addError("Duplicate OpenBoxes Code: " + row.getOpenBoxesCode());
                }
                openBoxesCodes.add(row.getOpenBoxesCode());

                if (productNames.contains(row.getProductName())) {
                    notes.addError("Duplicate Product Name: " + row.getProductName());
                }
                productNames.add(row.getProductName());

                if (row.getConcept() != null) {
                    Concept concept = emrConceptService.getConcept(row.getConcept());
                    if (concept == null) {
                        notes.addError("Specified concept not found: " + row.getConcept());
                    } else {
                        notes.addNote(row.getConcept() + " -> " + concept.getId());
                    }
                } else {
                    String productName = row.getProductName();
                    String genericName = productName.split(",")[0].trim();
                    List<Concept> possibleConcepts = conceptService.getConceptsByName(genericName);
                    if (possibleConcepts.size() == 0) {
                        notes.addError("At " + productName + ", cannot find concept named " + genericName);
                    } else if (possibleConcepts.size() > 1) {
                        notes.addError("At " + productName + ", found multiple candidate concepts named " + genericName + ": " + possibleConcepts);
                    } else {
                        notes.addNote(productName + " -> (auto-mapped) " + possibleConcepts.get(0).getName().getName());
                    }
                }
            }
        } finally {
            if (csv != null) {
                csv.close();
            }
        }

        return notes;
    }

}
