package org.openmrs.module.mirebalaismetadata;

import org.openmrs.EncounterRole;
import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component("mirebalaisMetadataProperties")
public class MirebalaisMetadataProperties extends ModuleProperties {

    public static final String GP_INSTALLED_ADDRESS_HIERARCHY_VERSION = "mirebalais.installedAddressHierarchyVersion";
    public static final String GP_INSTALLED_DRUG_LIST_VERSION = "mirebalaismetadata.installedDrugListVersion";

    // locations

    public static final String UNKNOWN_LOCATION_UUID = "8d6c993e-c2cc-11de-8d13-0010c6dffd0f";
    public static final String MIREBALAIS_HOSPITAL_LOCATION_UUID = "a084f714-a536-473b-94e6-ec317b152b43";
    public static final String LACOLLINE_LOCATION_UUID = "23e7bb0d-51f9-4d5f-b34b-2fbbfeea1960";

    public static final String CLINIC_REGISTRATION_LOCATION_UUID = "787a2422-a7a2-400e-bdbb-5c54b2691af5";
    public static final String EMERGENCY_DEPARTMENT_RECEPTION_LOCATION_UUID = "afa09010-43b6-4f19-89e0-58d09941bcbd";
    public static final String CENTRAL_ARCHIVES_LOCATION_UUID = "be50d584-26b2-4371-8768-2b9565742b3b";

    public static final String OUTPATIENT_CLINIC_LOCATION_UUID = "199e7d87-92a0-4398-a0f8-11d012178164";
    public static final String EMERGENCY_LOCATION_UUID = "f3a5586e-f06c-4dfb-96b0-6f3451a35e90";
    public static final String COMMUNITY_HEALTH_LOCATION_UUID = "dce85b2e-6946-4798-9037-d00f123df7bd";
    public static final String DENTAL_LOCATION_UUID = "4f2d9af1-7eec-4228-bbd6-7b0774c6c267";

    public static final String WOMENS_CLINIC_LOCATION_UUID = "9b2066a2-7087-47f6-9b3a-b001037432a3";
    public static final String WOMENS_TRIAGE_LOCATION_UUID = "d65eb8cf-d781-4ea8-9d9a-2b3e03c6074c";
    public static final String LABOR_AND_DELIVERY_LOCATION_UUID = "dcfefcb7-163b-47e5-84ae-f715cf3e0e92";
    public static final String ANTEPARTUM_WARD_LOCATION_UUID = "272bd989-a8ee-4a16-b5aa-55bad4e84f5c";
    public static final String POSTPARTUM_WARD_LOCATION_UUID = "950852f3-8a96-4d82-a5f8-a68a92043164";
    public static final String POST_OP_GYN_LOCATION_UUID = "ec9d2302-b525-45ce-bebe-42ea1d38b251";

    public static final String SURGICAL_WARD_LOCATION_UUID = "7d6cc39d-a600-496f-a320-fd4985f07f0b";
    public static final String OPERATING_ROOMS_LOCATION_UUID = "41736d90-12f9-4c16-b5a1-5af170b7bf2a";
    public static final String PRE_OP_PACU_LOCATION_UUID = "109a13a9-029d-498d-b66e-bab5eb396996";

    public static final String MAIN_LABORATORY_LOCATION_UUID = "53cd4959-5f4d-4ddc-9971-7fc27e0b7946";
    public static final String WOMENS_OUTPATIENT_LABORATORY_LOCATION_UUID = "0198d2c3-9c08-45b6-88df-cbb60446ef00";
    public static final String RADIOLOGY_LOCATION_UUID = "cfe92278-0181-4315-ab7d-1731753120f0";

    public static final String MENS_INTERNAL_MEDICINE_LOCATION_UUID = "e5db0599-89e8-44fa-bfa2-07e47d63546f";
    public static final String MENS_INTERNAL_MEDICINE_A_LOCATION_UUID = "3b6f010b-5d44-4ded-85b3-70ea7ba79fd5";
    public static final String MENS_INTERNAL_MEDICINE_B_LOCATION_UUID = "a296d225-0312-4ce5-836c-cb2d8e952f47";
    public static final String WOMENS_INTERNAL_MEDICINE_LOCATION_UUID = "2c93919d-7fc6-406d-a057-c0b640104790";
    public static final String WOMENS_INTERNAL_MEDICINE_A_LOCATION_UUID = "8e4e930b-e801-44c5-8895-e7ef6feb6d73";
    public static final String WOMENS_INTERNAL_MEDICINE_B_LOCATION_UUID = "195d0af1-3887-4a28-a542-b832099fe3ee";
    public static final String PEDIATRICS_LOCATION_UUID = "c9ab4c5c-0a8a-4375-b986-f23c163b2f69";
    public static final String PEDIATRICS_A_LOCATION_UUID = "e4ae2d28-d57c-42ba-b7aa-f6fcac421b2c";
    public static final String PEDIATRICS_B_LOCATION_UUID = "e752ec51-5633-489f-aef1-01f285c03f38";
    public static final String ED_BOARDING = "8355ad15-c3c9-4471-a1e7-dc18b7983087";

    public static final String ICU_LOCATION_UUID = "b6fcd85f-5995-43c9-875f-3bb2299087ff";
    public static final String NICU_LOCATION_UUID = "62a9500e-a1a5-4235-844f-3a8cc0765d53";
    public static final String ISOLATION_LOCATION_UUID = "29437276-aeae-4ea8-8219-720886cdc87f";
    public static final String CHEMOTHERAPY_LOCATION_UUID = "dc8413be-1075-48b5-9857-9bd4954686ed";

    public static final String DISPENSER_ENCOUNTER_ROLE = "bad21515-fd04-4ff6-bfcd-78456d12f168";


    public int getInstalledDrugListVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_DRUG_LIST_VERSION);
    }

    public int getInstalledAddressHierarchyVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_ADDRESS_HIERARCHY_VERSION);
    }

    public EncounterRole getDispenserEncounterRole() {
        return encounterService.getEncounterRoleByUuid(DISPENSER_ENCOUNTER_ROLE);
    }

}
