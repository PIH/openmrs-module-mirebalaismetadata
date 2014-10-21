package org.openmrs.module.mirebalaismetadata;

import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.LocationTag;
import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.openmrs.module.mirebalaismetadata.deploy.bundle.CoreMetadata;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component("mirebalaisMetadataProperties")
public class MirebalaisMetadataProperties extends ModuleProperties {

    public static final String GP_INSTALLED_ADDRESS_HIERARCHY_VERSION = "mirebalais.installedAddressHierarchyVersion";
    public static final String GP_INSTALLED_DRUG_LIST_VERSION = "mirebalaismetadata.installedDrugListVersion";

    // encounter types
    public static final String DEATH_CERTIFICATE_ENCOUNTER_TYPE_UUID = "1545d7ff-60f1-485e-9c95-5740b8e6634b";

    public int getInstalledDrugListVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_DRUG_LIST_VERSION);
    }

    public int getInstalledAddressHierarchyVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_ADDRESS_HIERARCHY_VERSION);
    }

    public EncounterRole getDispenserEncounterRole() {
        return encounterService.getEncounterRoleByUuid(CoreMetadata.EncounterRoles.DISPENSER);
    }

    public LocationTag getSupportsConsultNoteTag() {
        return locationService.getLocationTagByName(CoreMetadata.LocationTags.CONSULT_NOTE_LOCATION);
    }

    public LocationTag getSupportsEDNoteTag() {
        return locationService.getLocationTagByName(CoreMetadata.LocationTags.ED_NOTE_LOCATION);
    }

    public LocationTag getSupportsSurgeryNoteTag() {
        return locationService.getLocationTagByName(CoreMetadata.LocationTags.SURGERY_NOTE_LOCATION);
    }

    public LocationTag getSupportsDispensingMedicationTag() {
        return locationService.getLocationTagByName(CoreMetadata.LocationTags.DISPENSING_LOCATION);
    }

    public LocationTag getSupportsAppointmentsTag() {
        return locationService.getLocationTagByName(CoreMetadata.LocationTags.APPOINTMENT_LOCATION);
    }

    public EncounterType getDeathCertificateEncounterType() {
        return getEncounterTypeByUuid(DEATH_CERTIFICATE_ENCOUNTER_TYPE_UUID, true);
    }

}
