package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptNameType;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.metadatadeploy.builder.ConceptBuilder;
import org.openmrs.module.metadatadeploy.builder.ConceptMapBuilder;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Requires(CoreMetadata.class)
public class ConsultNoteConcepts extends AbstractMetadataBundle {

    public static final class Concepts {
        public static final String PAST_MEDICAL_HISTORY_CONSTRUCT = "1633AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String PAST_MEDICAL_HISTORY_FINDING = "1628AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String PAST_MEDICAL_HISTORY_PRESENCE = "1729AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String PAST_MEDICAL_HISTORY_COMMENT = "160221AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        public static final String FAMILY_HISTORY_CONSTRUCT = "160593AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String FAMILY_HISTORY_DIAGNOSIS = "160592AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String FAMILY_HISTORY_RELATIONSHIP = "1560AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        public static final String FAMILY_HISTORY_PRESENCE = "1729AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; // same as PMH_PRESENCE
        public static final String FAMILY_HISTORY_COMMENT = "160618AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        // installed by this class
        public static final String PRESENTING_HISTORY = "3cd65c90-26fe-102b-80cb-0017a47871b2"; // PIH:PRESENTING HISTORY
        public static final String CARDIOPATHY = "6c07611c-a10e-4de7-be64-28cdf76144e9"; // PIH:CARDIOPATHY

        // from MDS packages
        public static final String ASTHMA = "3ccc4bf6-26fe-102b-80cb-0017a47871b2"; // PIH:ASTHMA
        public static final String CANCER = "116031AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; // CIEL:116031 (no PIH!)
        public static final String TUBERCULOSIS = "3ccca7cc-26fe-102b-80cb-0017a47871b2"; // PIH:TUBERCULOSIS
    }

    @Override
    public void install() throws Exception {
        ConceptDatatype notApplicable = MetadataUtils.existing(ConceptDatatype.class, CoreMetadata.ConceptDatatypes.N_A);
        ConceptDatatype coded = MetadataUtils.existing(ConceptDatatype.class, CoreMetadata.ConceptDatatypes.CODED);
        ConceptDatatype text = MetadataUtils.existing(ConceptDatatype.class, CoreMetadata.ConceptDatatypes.TEXT);
        ConceptClass misc = MetadataUtils.existing(ConceptClass.class, CoreMetadata.ConceptClasses.MISC);
        ConceptClass diagnosis = MetadataUtils.existing(ConceptClass.class, CoreMetadata.ConceptClasses.DIAGNOSIS);
        ConceptClass question = MetadataUtils.existing(ConceptClass.class, CoreMetadata.ConceptClasses.QUESTION);
        ConceptClass convSet = MetadataUtils.existing(ConceptClass.class, CoreMetadata.ConceptClasses.CONV_SET);
        ConceptMapType sameAs = MetadataUtils.existing(ConceptMapType.class, CoreMetadata.ConceptMapTypes.SAME_AS);
        ConceptSource ciel = MetadataUtils.existing(ConceptSource.class, CoreMetadata.ConceptSources.CIEL);
        ConceptSource pih = MetadataUtils.existing(ConceptSource.class, CoreMetadata.ConceptSources.PIH);
        ConceptSource snomedCt = MetadataUtils.existing(ConceptSource.class, CoreMetadata.ConceptSources.SNOMED_CT);

        // TODO ensure that these concepts are not loaded
        // PREVIOUS DIAGNOSIS CONSTRUCT: 3cd675b8-26fe-102b-80cb-0017a47871b2

        Concept pmhWhich = install(new ConceptBuilder(Concepts.PAST_MEDICAL_HISTORY_FINDING)
                .datatype(coded)
                .conceptClass(diagnosis)
                        // in CIEL this is called PAST MEDICAL HISTORY ADDED
                .name("1908BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Past medical history finding", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .description("1470FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", "Coded list of past medical history problems but not procedures which are coded under Past Surgical History.", Locale.ENGLISH)
                .mapping(new ConceptMapBuilder("171868ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(ciel, "1628")
                        .build())
                .build());

        Concept isSymptomPresent = install(new ConceptBuilder(Concepts.PAST_MEDICAL_HISTORY_PRESENCE)
                .datatype(coded)
                .conceptClass(diagnosis)
                .name("2009BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Sign/symptom present", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .answers(
                        MetadataUtils.existing(Concept.class, CoreMetadata.Concepts.YES),
                        MetadataUtils.existing(Concept.class, CoreMetadata.Concepts.NO),
                        MetadataUtils.existing(Concept.class, CoreMetadata.Concepts.UNKNOWN))
                .mapping(new ConceptMapBuilder("171968ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                    .type(sameAs)
                    .ensureTerm(ciel, "1729")
                    .build())
                .build());

        // TODO waiting for confirmation from Andy that this is appropriate as comments
        Concept pmhComment = install(new ConceptBuilder(Concepts.PAST_MEDICAL_HISTORY_COMMENT)
                .datatype(text)
                .conceptClass(question)
                .name("108123BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Past medical history added (text)", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .mapping(new ConceptMapBuilder("217354ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                    .type(sameAs)
                    .ensureTerm(ciel, "160221")
                    .build())
                .build());

        Concept pmhConstruct = install(new ConceptBuilder(Concepts.PAST_MEDICAL_HISTORY_CONSTRUCT)
                .datatype(notApplicable)
                .conceptClass(misc)
                .name("1913BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Past medical history", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .name("86916BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "PMH", Locale.ENGLISH, ConceptNameType.SHORT)
                .setMembers(pmhWhich, isSymptomPresent, pmhComment)
                .mapping(new ConceptMapBuilder("171873ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                    .type(sameAs)
                    .ensureTerm(ciel, "1633")
                    .build())
                .build());


        Concept famHxDiagnosis = install(new ConceptBuilder(Concepts.FAMILY_HISTORY_DIAGNOSIS)
                .datatype(coded)
                .conceptClass(misc) // CIEL says Diagnosis, but this is wrong
                .name("109016BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Family history diagnosis", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .mapping(new ConceptMapBuilder("217715ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(ciel, "160592")
                        .build())
                .mapping(new ConceptMapBuilder("217715ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(snomedCt, "416471007")
                        .build())
                .build());

        Concept famHxRelationship = install(new ConceptBuilder(Concepts.FAMILY_HISTORY_RELATIONSHIP)
                .datatype(coded)
                .conceptClass(misc)
                .name("1825BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Family member", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .answers() // TODO
                .mapping(new ConceptMapBuilder("171802ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(ciel, "1560")
                        .build())
                .mapping(new ConceptMapBuilder("132750ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(snomedCt, "303071001")
                        .build())
                .build());

        Concept famHxComment = install(new ConceptBuilder(Concepts.FAMILY_HISTORY_COMMENT)
                .datatype(text)
                .conceptClass(question)
                .name("109055BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Family history comment", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .mapping(new ConceptMapBuilder("217741ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(ciel, "160618")
                        .build())
                .build()
        );

        Concept famHxConstruct = install(new ConceptBuilder(Concepts.FAMILY_HISTORY_CONSTRUCT)
                .datatype(notApplicable)
                .conceptClass(convSet) // CIEL says Finding but this is wrong
                .name("109017BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Patient's family history list", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .name("109019BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", "Family history list", Locale.ENGLISH, null)
                .setMembers(
                        famHxDiagnosis,
                        famHxRelationship,
                        isSymptomPresent,
                        famHxComment)
                .mapping(new ConceptMapBuilder("217716ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(ciel, "160593")
                        .build())
                .mapping(new ConceptMapBuilder("144705ABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                        .type(sameAs)
                        .ensureTerm(snomedCt, "57177007")
                        .build())
                .build());


        install(new ConceptBuilder(Concepts.PRESENTING_HISTORY)
                .datatype(text)
                .conceptClass(question)
                .name("3e141cbe-26fe-102b-80cb-0017a47871b2", "Presenting history", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .name("3e141f8e-26fe-102b-80cb-0017a47871b2", "Anamnèse", Locale.FRENCH, ConceptNameType.FULLY_SPECIFIED)
                .description("ece2fe5e-07fe-102c-b5fa-0017a47871b2", "History of the patients presenting complaint", Locale.ENGLISH)
                .description("ece30192-07fe-102c-b5fa-0017a47871b2", "Complaintes actuelles", Locale.FRENCH)
                .mapping(new ConceptMapBuilder("8373bf92-40f8-404a-bfad-34da8639a426")
                        .type(sameAs)
                        .ensureTerm(ciel, "1390")
                        .build())
                .mapping(new ConceptMapBuilder("b20be720-4864-102e-96e9-000c29c2a5d7")
                        .type(sameAs)
                        .ensureTerm(pih, "974")
                        .build())
                .mapping(new ConceptMapBuilder("753e6658-4943-102e-96e9-000c29c2a5d7")
                        .type(sameAs)
                        .ensureTerm(pih, "PRESENTING HISTORY")
                        .build())
                .build());


        // is it appropriate to start creating _some_ diagnosis concepts this way? or should we just put these in the MDS packages?
        install(new ConceptBuilder(Concepts.CARDIOPATHY)
                .datatype(notApplicable)
                .conceptClass(misc)
                .name("a4a83322-db96-4689-8d64-891b4a87986a", "Cardiopathy", Locale.ENGLISH, ConceptNameType.FULLY_SPECIFIED)
                .description("f28f6b59-18f2-4127-802f-b6f1f8d9c3c8", "A disease or disorder of the heart", Locale.ENGLISH)
                .mapping(new ConceptMapBuilder("0f2760de-a543-102e-a00e-000c29c2a5d7")
                        .type(sameAs)
                        .ensureTerm(pih, "7041")
                        .build())
                .mapping(new ConceptMapBuilder("cf1a37c8-a542-102e-a00e-000c29c2a5d7")
                        .type(sameAs)
                        .ensureTerm(pih, "CARDIOPATHY")
                        .build())
                .build());
    }

}
