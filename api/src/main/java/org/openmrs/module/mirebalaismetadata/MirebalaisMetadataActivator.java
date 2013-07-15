/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.mirebalaismetadata;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.addresshierarchy.AddressField;
import org.openmrs.module.addresshierarchy.AddressHierarchyLevel;
import org.openmrs.module.addresshierarchy.service.AddressHierarchyService;
import org.openmrs.module.addresshierarchy.util.AddressHierarchyImportUtil;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.resolver.Resolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByNameResolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByUuidResolver;
import org.openmrs.module.patientregistration.PatientRegistrationGlobalProperties;
import org.openmrs.module.radiologyapp.RadiologyConstants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.ANTEPARTUM_WARD_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.COMMUNITY_HEALTH_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.DENTAL_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.EMERGENCY_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.GP_INSTALLED_ADDRESS_HIERARCHY_VERSION;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.ICU_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.ISOLATION_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.LABOR_AND_DELIVERY_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.LACOLLINE_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.MENS_INTERNAL_MEDICINE_A_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.MENS_INTERNAL_MEDICINE_B_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.MENS_INTERNAL_MEDICINE_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.MIREBALAIS_HOSPITAL_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.NICU_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.OUTPATIENT_CLINIC_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.PEDIATRICS_A_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.PEDIATRICS_B_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.PEDIATRICS_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.POSTPARTUM_WARD_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.POST_OP_GYN_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.PRE_OP_PACU_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.SURGICAL_WARD_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.UNKNOWN_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.WOMENS_CLINIC_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.WOMENS_INTERNAL_MEDICINE_A_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.WOMENS_INTERNAL_MEDICINE_B_LOCATION_UUID;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.WOMENS_INTERNAL_MEDICINE_LOCATION_UUID;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class MirebalaisMetadataActivator extends BaseModuleActivator {

    protected Log log = LogFactory.getLog(getClass());

    private final String ADDRESS_HIERARCHY_CSV_FILE = "org/openmrs/module/mirebalaismetadata/addresshierarchy/haiti_address_hierarchy_entries";

    private final Integer ADDRESS_HIERARCHY_VERSION = 5;

    private MirebalaisMetadataProperties mirebalaisMetadataProperties;

    public MirebalaisMetadataActivator() {
    }

    /**
     * For unit tests
     *
     * @param mirebalaisMetadataProperties
     */
    public MirebalaisMetadataActivator(MirebalaisMetadataProperties mirebalaisMetadataProperties) {
        this.mirebalaisMetadataProperties = mirebalaisMetadataProperties;
    }

    /**
     * @see ModuleActivator#contextRefreshed()
     */
    public void contextRefreshed() {
        if (mirebalaisMetadataProperties == null) {
            mirebalaisMetadataProperties = Context.getRegisteredComponents(MirebalaisMetadataProperties.class).get(0);
        }

        // Since we do all MDS import programmatically, in mirror or parent-child mode, we don't want items being matched
        // except for in specific ways.
        // see https://tickets.openmrs.org/browse/META-323
        List<Resolver<?>> supportedResolvers = new ArrayList<Resolver<?>>();
        supportedResolvers.add(new ObjectByUuidResolver());
        supportedResolvers.add(new ObjectByNameResolver());
        MetadataSharing.getInstance().getResolverEngine().setResolvers(supportedResolvers);

        log.info("Mirebalais Metadata module refreshed");
    }

    /**
     * @see ModuleActivator#started()
     */
    public void started() {
        if (mirebalaisMetadataProperties == null) {
            mirebalaisMetadataProperties = Context.getRegisteredComponents(MirebalaisMetadataProperties.class).get(0);
        }
        try {
            installMetadataPackages();
            setLocationTags(Context.getLocationService(), getEmrApiProperties());
            verifyRadiologyConceptsPresent();
            setupPatientRegistrationGlobalProperties();
            setupAddressHierarchy();
        } catch (Exception e) {
            Module mod = ModuleFactory.getModuleById("mirebalaismetadata");
            ModuleFactory.stopModule(mod);
            throw new RuntimeException("Failed to start the mirebalaismetadata module", e);
        }
        log.info("Mirebalais Metadata module started");
    }

    private EmrApiProperties getEmrApiProperties() {
        return Context.getRegisteredComponents(EmrApiProperties.class).iterator().next();
    }

    private void setLocationTags(LocationService locationService, EmrApiProperties emrApiProperties) {
        List<Location> allLocations = locationService.getAllLocations();
        Set<String> allLocationUuids = new HashSet<String>();
        for (Location location : allLocations) {
            allLocationUuids.add(location.getUuid());
        }

        // allow logging in to all locations MINUS exceptions
        List<String> loginLocationUuids = new ArrayList<String>(allLocationUuids);
        loginLocationUuids.removeAll(Arrays.asList(
                UNKNOWN_LOCATION_UUID,
                MIREBALAIS_HOSPITAL_LOCATION_UUID,
                LACOLLINE_LOCATION_UUID,
                PEDIATRICS_A_LOCATION_UUID,
                PEDIATRICS_B_LOCATION_UUID,
                MENS_INTERNAL_MEDICINE_A_LOCATION_UUID,
                MENS_INTERNAL_MEDICINE_B_LOCATION_UUID,
                WOMENS_INTERNAL_MEDICINE_A_LOCATION_UUID,
                WOMENS_INTERNAL_MEDICINE_B_LOCATION_UUID));
        setLocationTagFor(locationService, emrApiProperties.getSupportsLoginLocationTag(), allLocations, loginLocationUuids);

        // allow admission at specified locations
        List<String> admitLocationUuids = Arrays.asList(
                SURGICAL_WARD_LOCATION_UUID,
                ANTEPARTUM_WARD_LOCATION_UUID,
                LABOR_AND_DELIVERY_LOCATION_UUID,
                POSTPARTUM_WARD_LOCATION_UUID);
        setLocationTagFor(locationService, emrApiProperties.getSupportsAdmissionLocationTag(), allLocations, admitLocationUuids);

        // allow transfer at specified locations
        List<String> transferLocationUuids = Arrays.asList(
                SURGICAL_WARD_LOCATION_UUID,
                PRE_OP_PACU_LOCATION_UUID,
                POST_OP_GYN_LOCATION_UUID,
                ANTEPARTUM_WARD_LOCATION_UUID,
                LABOR_AND_DELIVERY_LOCATION_UUID,
                POSTPARTUM_WARD_LOCATION_UUID,
                MENS_INTERNAL_MEDICINE_LOCATION_UUID,
                WOMENS_INTERNAL_MEDICINE_LOCATION_UUID,
                PEDIATRICS_LOCATION_UUID,
                ICU_LOCATION_UUID,
                NICU_LOCATION_UUID,
                ISOLATION_LOCATION_UUID,
                EMERGENCY_LOCATION_UUID,
                COMMUNITY_HEALTH_LOCATION_UUID,
                OUTPATIENT_CLINIC_LOCATION_UUID,
                WOMENS_CLINIC_LOCATION_UUID,
                DENTAL_LOCATION_UUID);
        setLocationTagFor(locationService, emrApiProperties.getSupportsTransferLocationTag(), allLocations, transferLocationUuids);
    }

    private void setLocationTagFor(LocationService service, LocationTag tag, List<Location> allLocations, Collection<String> uuidsThatGetTag) {
        for (Location candidate : allLocations) {
            boolean expected = uuidsThatGetTag.contains(candidate.getUuid());
            boolean actual = candidate.hasTag(tag.getName());
            if (actual && !expected) {
                candidate.removeTag(tag);
                service.saveLocation(candidate);
            } else if (!actual && expected) {
                candidate.addTag(tag);
                service.saveLocation(candidate);
            }
        }
    }

    private void installMetadataPackages() throws Exception {
        MetadataUtil.setupStandardMetadata(getClass().getClassLoader());
        Context.flushSession();
    }

    private void setupAddressHierarchy() {
        AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);

        // first check to see if we need to configure the address hierarchy levels
        int numberOfLevels = ahService.getAddressHierarchyLevelsCount();

        // if not 0 or 6 levels, we are in a weird state we can't recover from
        if (numberOfLevels != 0 && numberOfLevels != 6) {
            throw new RuntimeException("Unable to configure address hierarchy as it is currently misconfigured with "
                    + numberOfLevels + "levels");
        }

        // add the address hierarchy levels & entries if they don't exist, otherwise verify that they are correct
        if (numberOfLevels == 0) {
            AddressHierarchyLevel country = new AddressHierarchyLevel();
            country.setAddressField(AddressField.COUNTRY);
            ahService.saveAddressHierarchyLevel(country);

            AddressHierarchyLevel stateProvince = new AddressHierarchyLevel();
            stateProvince.setAddressField(AddressField.STATE_PROVINCE);
            stateProvince.setParent(country);
            ahService.saveAddressHierarchyLevel(stateProvince);

            AddressHierarchyLevel cityVillage = new AddressHierarchyLevel();
            cityVillage.setAddressField(AddressField.CITY_VILLAGE);
            cityVillage.setParent(stateProvince);
            ahService.saveAddressHierarchyLevel(cityVillage);

            AddressHierarchyLevel address3 = new AddressHierarchyLevel();
            address3.setAddressField(AddressField.ADDRESS_3);
            address3.setParent(cityVillage);
            ahService.saveAddressHierarchyLevel(address3);

            AddressHierarchyLevel address1 = new AddressHierarchyLevel();
            address1.setAddressField(AddressField.ADDRESS_1);
            address1.setParent(address3);
            ahService.saveAddressHierarchyLevel(address1);

            AddressHierarchyLevel address2 = new AddressHierarchyLevel();
            address2.setAddressField(AddressField.ADDRESS_2);
            address2.setParent(address1);
            ahService.saveAddressHierarchyLevel(address2);
        }
        // at least verify that the right levels exist
        // TODO: perhaps do more validation here?
        else {
            AddressField[] fields = {AddressField.COUNTRY, AddressField.STATE_PROVINCE, AddressField.CITY_VILLAGE,
                    AddressField.ADDRESS_3, AddressField.ADDRESS_1, AddressField.ADDRESS_2};
            int i = 0;

            for (AddressHierarchyLevel level : ahService.getOrderedAddressHierarchyLevels(true)) {
                if (level.getAddressField() != fields[i]) {
                    throw new RuntimeException("Address field " + i + " improperly configured: is "
                            + level.getAddressField() + " but should be " + fields[i]);
                }
                i++;
            }
        }

        // load in the csv file if necessary
        int installedAddressHierarchyVersion = mirebalaisMetadataProperties.getInstalledAddressHierarchyVersion();

        if (installedAddressHierarchyVersion < ADDRESS_HIERARCHY_VERSION) {
            // delete any existing entries
            Context.getService(AddressHierarchyService.class).deleteAllAddressHierarchyEntries();

            // import the new file
            InputStream file = getClass().getClassLoader().getResourceAsStream(ADDRESS_HIERARCHY_CSV_FILE + "_"
                    + ADDRESS_HIERARCHY_VERSION + ".csv");
            AddressHierarchyImportUtil.importAddressHierarchyFile(file, "\\|", "\\^");

            // update the installed version
            GlobalProperty installedAddressHierarchyVersionObject = Context.getAdministrationService()
                    .getGlobalPropertyObject(GP_INSTALLED_ADDRESS_HIERARCHY_VERSION);
            if (installedAddressHierarchyVersionObject == null) {
                installedAddressHierarchyVersionObject = new GlobalProperty();
                installedAddressHierarchyVersionObject.setProperty(GP_INSTALLED_ADDRESS_HIERARCHY_VERSION);
            }
            installedAddressHierarchyVersionObject.setPropertyValue(ADDRESS_HIERARCHY_VERSION.toString());
            Context.getAdministrationService().saveGlobalProperty(installedAddressHierarchyVersionObject);
        }
    }

    private void verifyRadiologyConceptsPresent() {
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_PROCEDURE, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_REPORT_TYPE, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_REPORT_BODY, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_ACCESSION_NUMBER, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_IMAGES_AVAILABLE, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_REPORT_CORRECTION, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_REPORT_FINAL, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_REPORT_PRELIM, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_STUDY_SET, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
        verifyConceptPresent(RadiologyConstants.CONCEPT_CODE_RADIOLOGY_REPORT_SET, EmrApiConstants.EMR_CONCEPT_SOURCE_NAME);
    }

    private void verifyConceptPresent(String conceptCode, String conceptSource) {
        if (Context.getConceptService().getConceptByMapping(conceptCode, conceptSource) == null) {
            throw new RuntimeException("No concept tagged with code " + conceptCode + " from source " + conceptSource);
        }
    }

    private void setupPatientRegistrationGlobalProperties() {
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.SUPPORTED_TASKS,
                "patientRegistration|primaryCareReception|edCheckIn|patientLookup");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.SEARCH_CLASS,
                "org.openmrs.module.patientregistration.search.DefaultPatientRegistrationSearch");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.LABEL_PRINT_COUNT, "1");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PROVIDER_ROLES, "LacollineProvider");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PROVIDER_IDENTIFIER_PERSON_ATTRIBUTE_TYPE,
                "Provider Identifier");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PRIMARY_IDENTIFIER_TYPE, "ZL EMR ID");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.URGENT_DIAGNOSIS_CONCEPT,
                "PIH: Haiti nationally urgent diseases");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.NOTIFY_DIAGNOSIS_CONCEPT,
                "PIH: Haiti nationally notifiable diseases");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.NON_CODED_DIAGNOSIS_CONCEPT,
                "PIH: ZL Primary care diagnosis non-coded");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.NEONATAL_DISEASES_CONCEPT,
                "PIH: Haiti neonatal diseases");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PRIMARY_CARE_VISIT_ENCOUNTER_TYPE,
                "Primary Care Visit");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.CODED_DIAGNOSIS_CONCEPT,
                "PIH: HUM Outpatient diagnosis");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.AGE_RESTRICTED_CONCEPT,
                "PIH: Haiti age restricted diseases");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.RECEIPT_NUMBER_CONCEPT,
                "PIH: Receipt number|en:Receipt Number|ht:Nimewo Resi a");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PAYMENT_AMOUNT_CONCEPT,
                "PIH: Payment amount|en:Payment amount|ht:Kantite lajan");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.VISIT_REASON_CONCEPT,
                "PIH: Reason for HUM visit|en:Visit reason|ht:Rezon pou vizit");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PRIMARY_CARE_RECEPTION_ENCOUNTER_TYPE, "Check-in");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PATIENT_REGISTRATION_ENCOUNTER_TYPE,
                "Patient Registration");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.NUMERO_DOSSIER, "e66645eb-03a8-4991-b4ce-e87318e37566");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.EXTERNAL_NUMERO_DOSSIER, "9dbea4d4-35a9-4793-959e-952f2a9f5347");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.ID_CARD_PERSON_ATTRIBUTE_TYPE, "Telephone Number");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.PATIENT_VIEWING_ATTRIBUTE_TYPES, "Telephone Number");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.ID_CARD_LABEL_TEXT, "Zanmi Lasante Patient ID Card");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.ICD10_CONCEPT_SOURCE, "ICD-10");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.BIRTH_YEAR_INTERVAL, "1");
        setExistingGlobalProperty(PatientRegistrationGlobalProperties.MEDICAL_RECORD_LOCATION_TAG,
                "71c99f93-bc0c-4a44-b573-a7ac096ff636");

    }

    /**
     * Sets global property value or throws an exception if that global property does not already exist
     * (Set as protected so we can override it for testing purposes)
     *
     * @param propertyName
     * @param propertyValue
     */
    protected void setExistingGlobalProperty(String propertyName, String propertyValue) {
        AdministrationService administrationService = Context.getAdministrationService();
        GlobalProperty gp = administrationService.getGlobalPropertyObject(propertyName);
        if (gp == null) {
            throw new RuntimeException("global property " + propertyName + " does not exist");
        }
        gp.setPropertyValue(propertyValue);
        administrationService.saveGlobalProperty(gp);
    }


}
