package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.module.metadatadeploy.bundle.CoreConstructors;
import org.openmrs.module.mirebalaismetadata.constants.LocationAttributeTypes;
import org.openmrs.module.mirebalaismetadata.constants.LocationTags;
import org.openmrs.module.mirebalaismetadata.constants.Locations;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationAttributeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationAttributeTypeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationTagDescriptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.location;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationAttribute;

@Component
public class LocationBundle extends MirebalaisMetadataBundle {

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
        install(LocationTags.ADMISSION_NOTE_LOCATION);
        install(LocationTags.DISPENSING_LOCATION);
        install(LocationTags.APPOINTMENT_LOCATION);
        install(LocationTags.VITALS_LOCATION);
        install(LocationTags.INPATIENTS_APP_LOCATION);
        install(LocationTags.CHECKIN_LOCATION);
        install(LocationTags.REGISTRATION_LOCATION);
        install(LocationTags.ED_REGISTRATION_LOCATION);
        install(LocationTags.ORDER_RADIOLOGY_STUDY_LOCATION);

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

        installLocation(Locations.UNKNOWN);
        installLocation(Locations.LACOLLINE);

        // Top level Locations at Mirebalais
        installLocation(Locations.MIREBALAIS_CDI_PARENT);
        installLocation(Locations.MIREBALAIS_HOSPITAL);

        // Locations within Mirebalais Hospital
        installLocation(Locations.ANTEPARTUM_WARD);
        installLocation(Locations.BLOOD_BANK);
        installLocation(Locations.CENTRAL_ARCHIVES);
        installLocation(Locations.CHEMOTHERAPY);
        installLocation(Locations.CLINIC_REGISTRATION);
        installLocation(Locations.COMMUNITY_HEALTH);
        installLocation(Locations.DENTAL);
        installLocation(Locations.EMERGENCY);
        installLocation(Locations.EMERGENCY_DEPARTMENT_RECEPTION);
        installLocation(Locations.FAMILY_PLANNING);
        installLocation(Locations.ICU);
        installLocation(Locations.ISOLATION);
        installLocation(Locations.LABOR_AND_DELIVERY);
        installLocation(Locations.MAIN_LABORATORY);
        installLocation(Locations.MENS_INTERNAL_MEDICINE);
        installLocation(Locations.MENS_INTERNAL_MEDICINE_A);
        installLocation(Locations.MENS_INTERNAL_MEDICINE_B);
        installLocation(Locations.NICU);
        installLocation(Locations.OPERATING_ROOMS);
        installLocation(Locations.OUTPATIENT_CLINIC);
        installLocation(Locations.OUTPATIENT_CLINIC_PHARMACY);
        installLocation(Locations.PEDIATRICS);
        installLocation(Locations.PEDIATRICS_A);
        installLocation(Locations.PEDIATRICS_B);
        installLocation(Locations.PRE_OP_PACU);
        installLocation(Locations.POST_OP_GYN);
        installLocation(Locations.POSTPARTUM_WARD);
        installLocation(Locations.RADIOLOGY);
        installLocation(Locations.REHABILITATION);
        installLocation(Locations.SURGICAL_WARD);
        installLocation(Locations.WOMENS_CLINIC);
        installLocation(Locations.WOMENS_AND_CHILDRENS_PHARMACY); // out of order because of above location
        installLocation(Locations.WOMENS_INTERNAL_MEDICINE);
        installLocation(Locations.WOMENS_INTERNAL_MEDICINE_A);
        installLocation(Locations.WOMENS_INTERNAL_MEDICINE_B);
        installLocation(Locations.WOMENS_OUTPATIENT_LABORATORY);
        installLocation(Locations.WOMENS_TRIAGE);

        // Locations at CDI
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL);
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_ACHIV_SANTRAL);
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_BIWO_RANDEVOU);
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_FAMASI);
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_LABORATWA);
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_RADYOGRAFI);
        installLocation(Locations.CDI_KLINIK_EKSTEN_JENERAL_SAL_PWOSEDI);


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

    protected void installLocation(LocationDescriptor location) {

        // First install the location and it's tags
        String parentUuid = location.parent() == null ? null : location.parent().uuid();
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
