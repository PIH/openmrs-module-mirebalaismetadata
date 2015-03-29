package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;
import org.springframework.stereotype.Component;

@Component
@Requires(CoreMetadata.class)
public class ConsultNoteConcepts extends AbstractMetadataBundle {

    public static final class Concepts {
//        public static final String PAST_MEDICAL_HISTORY_CONSTRUCT = "1633AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        public static final String PAST_MEDICAL_HISTORY_FINDING = "1628AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        public static final String PAST_MEDICAL_HISTORY_PRESENCE = "1729AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        public static final String PAST_MEDICAL_HISTORY_COMMENT = "160221AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//
//        public static final String FAMILY_HISTORY_CONSTRUCT = "160593AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        public static final String FAMILY_HISTORY_DIAGNOSIS = "160592AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        public static final String FAMILY_HISTORY_RELATIONSHIP = "1560AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        public static final String FAMILY_HISTORY_PRESENCE = "1729AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; // same as PMH_PRESENCE
//        public static final String FAMILY_HISTORY_COMMENT = "160618AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        // installed by this class



//        // from MDS packages
//        public static final String ASTHMA = "3ccc4bf6-26fe-102b-80cb-0017a47871b2"; // PIH:ASTHMA
//        public static final String CANCER = "116031AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; // CIEL:116031 (no PIH!)
//        public static final String TUBERCULOSIS = "3ccca7cc-26fe-102b-80cb-0017a47871b2"; // PIH:TUBERCULOSIS
    }

    @Override
    public void install() throws Exception {


    }

}
