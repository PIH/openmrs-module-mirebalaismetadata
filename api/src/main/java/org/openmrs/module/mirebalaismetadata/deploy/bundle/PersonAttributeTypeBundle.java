package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.module.metadatadeploy.bundle.CoreConstructors;
import org.openmrs.module.mirebalaismetadata.constants.PersonAttributeTypes;
import org.openmrs.module.mirebalaismetadata.descriptor.PersonAttributeTypeDescriptor;
import org.springframework.stereotype.Component;

@Component
public class PersonAttributeTypeBundle extends MirebalaisMetadataBundle {

    @Override
    public void install() throws Exception {

        log.info("Installing PersonAttributeTypes");
        install(PersonAttributeTypes.TELEPHONE_NUMBER);
        install(PersonAttributeTypes.PROVIDER_IDENTIFIER);
        install(PersonAttributeTypes.UNKNOWN_PATIENT);
        install(PersonAttributeTypes.MOTHERS_FIRST_NAME);
        install(PersonAttributeTypes.BIRTHPLACE);
    }

    //***** BUNDLE INSTALLATION METHODS FOR DESCRIPTORS

    protected void install(PersonAttributeTypeDescriptor d) {
        install(CoreConstructors.personAttributeType(d.name(), d.description(), d.format(), d.foreignKey(), d.searchable(), d.sortWeight(), d.uuid()));
    }
}
