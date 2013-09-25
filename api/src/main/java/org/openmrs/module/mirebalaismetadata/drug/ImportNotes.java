package org.openmrs.module.mirebalaismetadata.drug;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ImportNotes {

    private List<String> notes = new ArrayList<String>();

    private List<String> errors = new ArrayList<String>();

    public ImportNotes() {
    }

    public List<String> getNotes() {
        return notes;
    }

    public List<String> getErrors() {
        return errors;
    }

    public ImportNotes addNote(String note) {
        notes.add(note);
        return this;
    }

    public ImportNotes addError(String error) {
        errors.add(error);
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Errors ===\n");
        if (errors.size() == 0) {
            sb.append("None\n");
        }
        for (String error : errors) {
            sb.append(error).append("\n");
        }
        sb.append("=== Notes ===\n");
        if (notes.size() == 0) {
            sb.append("None\n");
        }
        for (String note : notes) {
            sb.append(note).append("\n");
        }
        return sb.toString();
    }

}
