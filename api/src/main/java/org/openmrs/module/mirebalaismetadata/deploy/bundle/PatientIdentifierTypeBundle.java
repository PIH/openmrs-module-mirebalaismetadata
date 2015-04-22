package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.module.metadatadeploy.bundle.CoreConstructors;
import org.openmrs.module.mirebalaismetadata.constants.LocationAttributeTypes;
import org.openmrs.module.mirebalaismetadata.constants.LocationTags;
import org.openmrs.module.mirebalaismetadata.constants.Locations;
import org.openmrs.module.mirebalaismetadata.constants.PatientIdentifierTypes;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationAttributeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationAttributeTypeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.LocationTagDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.PatientIdentifierTypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.location;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationAttribute;

@Component
public class PatientIdentifierTypeBundle extends MirebalaisMetadataBundle {

    @Override
    public void install() throws Exception {

        log.info("Installing PatientIdentifierTypes");

        install(PatientIdentifierTypes.ZL_EMR_ID);
        install(PatientIdentifierTypes.DOSSIER_NUMBER);
        install(PatientIdentifierTypes.EXTERNAL_DOSSIER_NUMBER);
        install(PatientIdentifierTypes.HIVEMR_V1);
    }

    //***** BUNDLE INSTALLATION METHODS FOR DESCRIPTORS

    protected void install(PatientIdentifierTypeDescriptor d) {
        install(CoreConstructors.patientIdentifierType(d.name(), d.description(), d.format(), d.formatDescription(), d.validator(), d.locationBehavior(), d.required(), d.uuid()));
    }
}
