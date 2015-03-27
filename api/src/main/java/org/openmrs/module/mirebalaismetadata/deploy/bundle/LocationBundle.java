package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.metadatadeploy.bundle.CoreConstructors;
import org.openmrs.module.mirebalaismetadata.constants.LocationAttributeTypes;
import org.openmrs.module.mirebalaismetadata.constants.LocationTags;
import org.openmrs.module.mirebalaismetadata.constants.Locations;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationAttributeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationAttributeTypeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationTagDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.location;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationAttribute;

@Component
public class LocationBundle extends MirebalaisMetadataBundle {

    @Autowired
    private FeatureToggleProperties featureToggles;

    @Override
    public void install() throws Exception {

        log.info("Installing LocationTags");

        install(LocationTags.LOGIN_LOCATION);
        install(LocationTags.MEDICAL_RECORD_LOCATION);
        install(LocationTags.ARCHIVES_LOCATION);
        install(LocationTags.VISIT_LOCATION);
        install(LocationTags.ADMISSION_LOCATION);
        install(LocationTags.TRANSFER_LOCAITON);
        install(LocationTags.CONSULT_NOTE_LOCATION);
        install(LocationTags.SURGERY_NOTE_LOCATION);
        install(LocationTags.ED_NOTE_LOCATION);
        install(LocationTags.DISPENSING_LOCATION);
        install(LocationTags.APPOINTMENT_LOCATION);

        log.info("Retiring old LocationTags");

        uninstall(possible(LocationTag.class, LocationTags.RETIRED_OUTPATIENT_TRANSFER_LOCATION.uuid()), "never used");
        uninstall(possible(LocationTag.class, LocationTags.RETIRED_INPATIENT_TRANSFER_LOCATION.uuid()), "never used");

        log.info("Installing LocationAttributeTypes");

        install(LocationAttributeTypes.LOCATION_CODE);
        install(LocationAttributeTypes.DEFAULT_LABEL_PRINTER);
        install(LocationAttributeTypes.DEFAULT_ID_CARD_PRINTER);
        install(LocationAttributeTypes.DEFAULT_WRISTBAND_PRINTER);
        install(LocationAttributeTypes.NAME_TO_PRINT_ON_ID_CARD);

        log.info("Installing Locations");

        installLocation(Locations.UNKNOWN, null);
        installLocation(Locations.LACOLLINE, null);

        // Top level Locations at Mirebalais
        // Pre-CDI, Mirebalais Hospital is the visit location and medical record location for all patients
        // Post-CDI, we convert this to be the Mirebalais / CDI parent location, which is used as the visit location so that patients can only have one active visit between the two
        // and we ceate a new location to represent Mirebalais Hospital, which is used as the medical record location containing archives for patients at Mirebalais Hospital specifically

        LocationDescriptor parentLocation = Locations.MIREBALAIS_CDI_PARENT;
        if (featureToggles.isFeatureEnabled("cdi")) {
            parentLocation = Locations.MIREBALAIS_HOSPITAL;
            installLocation(Locations.MIREBALAIS_CDI_PARENT, null);
            installLocation(Locations.MIREBALAIS_HOSPITAL_POST_CDI, Locations.MIREBALAIS_CDI_PARENT);
        }
        else {
            installLocation(Locations.MIREBALAIS_HOSPITAL, null);
        }

        // Locations within Mirebalais Hospital

        installLocation(Locations.ANTEPARTUM_WARD, parentLocation);
        installLocation(Locations.BLOOD_BANK, parentLocation);
        installLocation(Locations.CENTRAL_ARCHIVES, parentLocation);
        installLocation(Locations.CHEMOTHERAPY, parentLocation);
        installLocation(Locations.CLINIC_REGISTRATION, parentLocation);
        installLocation(Locations.COMMUNITY_HEALTH, parentLocation);
        installLocation(Locations.DENTAL, parentLocation);
        installLocation(Locations.EMERGENCY, parentLocation);
        installLocation(Locations.EMERGENCY_DEPARTMENT_RECEPTION, parentLocation);
        installLocation(Locations.FAMILY_PLANNING, parentLocation);
        installLocation(Locations.ICU, parentLocation);
        installLocation(Locations.ISOLATION, parentLocation);
        installLocation(Locations.LABOR_AND_DELIVERY, parentLocation);
        installLocation(Locations.MAIN_LABORATORY, parentLocation);
        installLocation(Locations.MENS_INTERNAL_MEDICINE, parentLocation);
        installLocation(Locations.MENS_INTERNAL_MEDICINE_A, Locations.MENS_INTERNAL_MEDICINE);
        installLocation(Locations.MENS_INTERNAL_MEDICINE_B, Locations.MENS_INTERNAL_MEDICINE);
        installLocation(Locations.NICU, parentLocation);
        installLocation(Locations.OPERATING_ROOMS, parentLocation);
        installLocation(Locations.OUTPATIENT_CLINIC, parentLocation);
        installLocation(Locations.OUTPATIENT_CLINIC_PHARMACY, parentLocation);
        installLocation(Locations.PEDIATRICS, parentLocation);
        installLocation(Locations.PEDIATRICS_A, Locations.PEDIATRICS);
        installLocation(Locations.PEDIATRICS_B, Locations.PEDIATRICS);
        installLocation(Locations.PRE_OP_PACU, parentLocation);
        installLocation(Locations.POST_OP_GYN, parentLocation);
        installLocation(Locations.POSTPARTUM_WARD, parentLocation);
        installLocation(Locations.RADIOLOGY, parentLocation);
        installLocation(Locations.REHABILITATION, parentLocation);
        installLocation(Locations.SURGICAL_WARD, parentLocation);
        installLocation(Locations.WOMENS_CLINIC, parentLocation);
        installLocation(Locations.WOMENS_AND_CHILDRENS_PHARMACY, Locations.WOMENS_CLINIC); // out of order because of above location
        installLocation(Locations.WOMENS_INTERNAL_MEDICINE, parentLocation);
        installLocation(Locations.WOMENS_INTERNAL_MEDICINE_A, Locations.WOMENS_INTERNAL_MEDICINE);
        installLocation(Locations.WOMENS_INTERNAL_MEDICINE_B, Locations.WOMENS_INTERNAL_MEDICINE);
        installLocation(Locations.WOMENS_OUTPATIENT_LABORATORY, parentLocation);
        installLocation(Locations.WOMENS_TRIAGE, parentLocation);

        if (featureToggles.isFeatureEnabled("cdi")) {
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL, Locations.MIREBALAIS_CDI_PARENT);
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_ACHIV_SANTRAL, Locations.CDI_KLINIK_EKSTEN_JENERAL);
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_BIWO_RANDEVOU, Locations.CDI_KLINIK_EKSTEN_JENERAL);
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_FAMASI, Locations.CDI_KLINIK_EKSTEN_JENERAL);
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_LABORATWA, Locations.CDI_KLINIK_EKSTEN_JENERAL);
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_RADYOGRAFI, Locations.CDI_KLINIK_EKSTEN_JENERAL);
            installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_SAL_PWOSEDI, Locations.CDI_KLINIK_EKSTEN_JENERAL);
        }

        log.info("Retiring old Mirebalais Locations");

        uninstall(possible(Location.class, Locations.RETIRED_CDI_KLINIK_EKSTEN_JENERAL_OLD.uuid()), "no longer used");  // a CDI location that was only temporarily added
        uninstall(possible(Location.class, Locations.RETIRED_ED_OBSERVATION.uuid()), "no longer used");
        uninstall(possible(Location.class, Locations.RETIRED_ED_BOARDING.uuid()), "no longer used");

        log.info("Voiding old location attributes");

        uninstall(possible(LocationAttribute.class, "d24c621b-fa4f-4d99-a15c-933186d9e8cd"), "belongs to retired location");
        uninstall(possible(LocationAttribute.class, "f170427f-8cc7-421e-bd52-350c3580ea90"), "belongs to retired location");
    }

    //***** BUNDLE INSTALLATION METHODS FOR DESCRIPTORS

    protected void install(LocationTagDescriptor d) {
        install(CoreConstructors.locationTag(d.name(), d.description(), d.uuid()));
    }

    protected void install(LocationAttributeTypeDescriptor d) {
        LocationAttributeType type = CoreConstructors.locationAttributeType(d.name(), d.description(), d.datatype(), d.datatypeConfig(), d.minOccurs(), d.maxOccurs(), d.uuid());
        install(type);
    }

    protected void installLocation(LocationDescriptor location, LocationDescriptor parent) {

        // First install the location and it's tags
        String parentUuid = parent == null ? null : parent.uuid();
        List<String> tagUuids = new ArrayList<String>();
        if (location.tags() != null) {
            for (LocationTagDescriptor tagDescriptor : location.tags()) {
                tagUuids.add(tagDescriptor.uuid());
            }
        }
        install(location(location.name(), location.description(), location.uuid(), parentUuid, tagUuids));

        // Then, install the location attribute(s) if applicable
        if (location.attributes() != null) {
            for (LocationAttributeDescriptor lad : location.attributes()) {
                if (!lad.location().uuid().equals(location.uuid())) {
                    throw new IllegalStateException("Location Attribute with uuid " + lad.uuid() + " is configured with a different location than it the Location it is associated with");
                }
                install(locationAttribute(lad.location().uuid(), lad.type().uuid(), lad.value(), lad.uuid()));
            }
        }
    }


}
