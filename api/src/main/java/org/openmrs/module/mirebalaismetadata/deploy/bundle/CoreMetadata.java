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

package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationTag;
import org.openmrs.PatientIdentifierType;
import org.openmrs.customdatatype.datatype.FreeTextDatatype;
import org.openmrs.module.emr.EmrConstants;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.htmlformentry.HtmlFormEntryConstants;
import org.openmrs.module.idgen.validator.LuhnMod30IdentifierValidator;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.namephonetics.NamePhoneticsConstants;
import org.openmrs.module.paperrecord.PaperRecordConstants;
import org.openmrs.module.patientregistration.PatientRegistrationGlobalProperties;
import org.openmrs.module.patientregistration.search.DefaultPatientRegistrationSearch;
import org.openmrs.module.printer.PrinterDatatype;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.ui.framework.UiFrameworkConstants;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.encounterRole;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.encounterType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.location;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationAttributeType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationTag;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.patientIdentifierType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.personAttributeType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.visitType;

/**
 * Core metadata bundle
 */
@Component
public class CoreMetadata extends MirebalaisMetadataBundle {

	protected Log log = LogFactory.getLog(getClass());

	public static final class Locations {
		public static final String UNKNOWN = "8d6c993e-c2cc-11de-8d13-0010c6dffd0f";
	}

	public static final class LocationTags {
		public static final String MEDICAL_RECORD_LOCATION = "71c99f93-bc0c-4a44-b573-a7ac096ff636";
        public static final String LOGIN_LOCATION = "b8bbf83e-645f-451f-8efe-a0db56f09676";
        public static final String VISIT_LOCATION = "0967d73d-f3c9-492b-abed-356478610a94";
        public static final String ADMISSION_LOCATION = "f5b9737b-14d5-402b-8475-dd558808e172";
        public static final String TRANSFER_LOCAITON = "9783aba6-df7b-4969-be6e-1e03e7a08965";
        public static final String CONSULT_NOTE_LOCATION = "dea8febf-0bbe-4111-8152-a9cf7df622b6";
        public static final String SURGERY_NOTE_LOCATION = "8ce9abcb-b0d9-4897-bc65-d8d2d3362b67";
        public static final String ED_NOTE_LOCATION = "fe742fba-840a-4155-bfc4-a5b35ac1bb03";
        public static final String DISPENSING_LOCATION = "c42b7bc3-b34d-4b8f-9796-09208f9dfd72";
        public static final String APPOINTMENT_LOCATION = "2b5c7110-d571-4f5f-b84e-500070b40ef8";
	}

    public static final class RetiredLocationTags {
        public static final String OUTPATIENT_TRANSFER_LOCATION = "84864264-5fda-4626-b22f-4b690b7279f3";
        public static final String INPATIENT_TRANSFER_LOCATION =" 160a6e45-9274-4dd8-abf7-e46af8cbb41a";
    }

	public static final class LocationAttributeTypes {
		public static final String LOCATION_CODE = "64f01c78-191d-4947-a201-7e0a7f0caf21";
        public static final String DEFAULT_WRISTBAND_PRINTER = "7f73ad30-0b89-11e4-9191-0800200c9a66";
        public static final String DEFAULT_LABEL_PRINTER = "bd6c1c10-38d3-11e2-81c1-0800200c9a66";
        public static final String DEFAULT_ID_CARD_PRINTER ="b48ef9a0-38d3-11e2-81c1-0800200c9a66";
        public static final String NAME_TO_PRINT_ON_ID_CARD = "a5fb5770-409a-11e2-a25f-0800200c9a66";
	}

	public static final class PatientIdentifierTypes {
		public static final String HIVEMR_V1 = "139766e8-15f5-102d-96e4-000c29c2a5d7";
		public static final String EXTERNAL_DOSSIER_NUMBER = "9dbea4d4-35a9-4793-959e-952f2a9f5347";
		public static final String DOSSIER_NUMBER = "e66645eb-03a8-4991-b4ce-e87318e37566";
		public static final String ZL_EMR_ID = "a541af1e-105c-40bf-b345-ba1fd6a59b85";
	}

	public static final class VisitTypes {
		public static final String CLINIC_OR_HOSPITAL_VISIT = "f01c54cb-2225-471a-9cd5-d348552c337c";
	}

	public static final class PersonAttributeTypes {
		public static final String PROVIDER_IDENTIFIER = "6de6c415-97a2-4cca-817a-9501cd9ef382";
		public static final String TELEPHONE_NUMBER = "14d4f066-15f5-102d-96e4-000c29c2a5d7";
		public static final String UNKNOWN_PATIENT = "8b56eac7-5c76-4b9c-8c6f-1deab8d3fc47";
	}

	public static final class EncounterTypes {
		public static final String PATIENT_REGISTRATION = "873f968a-73a8-4f9c-ac78-9f4778b751b6";
		public static final String CHECK_IN = "55a0d3ea-a4d7-4e88-8f01-5aceb2d3c61b";
		public static final String PAYMENT = "f1c286d0-b83f-4cd4-8348-7ea3c28ead13";
		public static final String VITALS = "4fb47712-34a6-40d2-8ed3-e153abbd25b7";
		public static final String PRIMARY_CARE_VISIT = "1373cf95-06e8-468b-a3da-360ac1cf026d";
		public static final String CONSULTATION = "92fd09b4-5335-4f7e-9f63-b2a663fd09a6";
		public static final String MEDICATION_DISPENSED = "8ff50dea-18a1-4609-b4c9-3f8f2d611b84";
		public static final String POST_OPERATIVE_NOTE = "c4941dee-7a9b-4c1c-aa6f-8193e9e5e4e5";
		public static final String TRANSFER = "436cfe33-6b81-40ef-a455-f134a9f7e580";
		public static final String ADMISSION = "260566e1-c909-4d61-a96f-c1019291a09d";
		public static final String CANCEL_ADMISSION = "edbb857b-e736-4296-9438-462b31f97ef9";
		public static final String EXIT_FROM_CARE = "b6631959-2105-49dd-b154-e1249e0fbcd7";

	}

	public static final class EncounterRoles {
		public static final String DISPENSER = "bad21515-fd04-4ff6-bfcd-78456d12f168";
		public static final String NURSE = "98bf2792-3f0a-4388-81bb-c78b29c0df92";
		public static final String CONSULTING_CLINICIAN = "4f10ad1a-ec49-48df-98c7-1391c6ac7f05";
		public static final String ADMINISTRATIVE_CLERK = "cbfe0b9d-9923-404c-941b-f048adc8cdc0";
		public static final String ORDERING_PROVIDER = "c458d78e-8374-4767-ad58-9f8fe276e01c";
	}

	public static final class Concepts { // TODO: Confirm all below are in Hum_Metadata package
		public static final String UNKNOWN = "3cd6fac4-26fe-102b-80cb-0017a47871b2";
		public static final String DIAGNOSIS_SET_OF_SETS = "8fcd0b0c-f977-4a66-a1b5-ad7ce68e6770";
		public static final String PAYMENT_AMOUNT = "5d1bc5de-6a35-4195-8631-7322941fe528";
		public static final String PAYMENT_REASON = "36ba7721-fae0-4da4-aef2-7e476cc04bdf";
		public static final String PAYMENT_RECEIPT_NUMBER = "20438dc7-c5b4-4d9c-8480-e888f4795123";
		public static final String PAYMENT_CONSTRUCT = "7a6330f1-9503-465c-8d63-82e1ad914b47";
	}

	public static final class ConceptSources {  // TODO: Confirm all are in bundle and are interpreted correctly
		public static final String LOINC = "2889f378-f287-40a5-ac9c-ce77ee963ed7";
	}

	public static final class Forms {
		public static final String ADMISSION = "43acf930-eb1b-11e2-91e2-0800200c9a66";  // TODO: Install in bundle
		public static final String TRANSFER_WITHIN_HOSPITAL = "d068bc80-fb95-11e2-b778-0800200c9a66";  // TODO: Install in bundle
		public static final String EXIT_FROM_INPATIENT = "e0a26c20-fba6-11e2-b778-0800200c9a66";  // TODO: Install in bundle
	}

	public static final class Packages {
		public static final String HUM_METADATA = "fa25ad0c-66cc-4715-8464-58570f7b5132";
	}

	public static final String DEFAULT_DATE_FORMAT = "dd MMM yyyy";
	public static final String DEFAULT_DATETIME_FORMAT = "dd MMM yyyy hh:mm aa";
	public static final String DEFAULT_LOCALE = "fr";

	private static final String ZL_EMR_ID_NAME = "ZL EMR ID";
	private static final String PROVIDER_IDENTIFIER_NAME = "Provider Identifier";
	private static final String TELEPHONE_NUMBER_NAME = "Telephone Number";
	private static final String REGISTRATION_ENCOUNTER_NAME = "Enregistrement de patient";
	private static final String CHECK_IN_ENCOUNTER_NAME = "Inscription";
	private static final String PRIMARY_CARE_VISIT_ENCOUNTER_NAME = "Consultation soins de base";
	private static final String DOUBLE_METAPHONE_ALTERNATE_NAME = "Double Metaphone Alternate";


	/**
	 * @see AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {

		log.info("Installing Core Identifier Types");

		install(patientIdentifierType("HIVEMR-V1", "Internal EMR ID for this Patient in the Haiti EMR V1 system",
				null, null, null, PatientIdentifierType.LocationBehavior.NOT_USED, false, PatientIdentifierTypes.HIVEMR_V1));

		install(patientIdentifierType("External Nimewo Dosye", "External Dossier number",
				null, null, null, PatientIdentifierType.LocationBehavior.NOT_USED, false, PatientIdentifierTypes.EXTERNAL_DOSSIER_NUMBER));

		install(patientIdentifierType("Nimewo Dosye", "Patient&apos;s Dossier number",
				"A\\d{6}", "A000001", null, PatientIdentifierType.LocationBehavior.REQUIRED, false, PatientIdentifierTypes.DOSSIER_NUMBER));

		install(patientIdentifierType(ZL_EMR_ID_NAME, "A unique identifier issued to all patients by the ZL EMR.  Blocks of this identifier are issued to each site to prevent duplication, so the identifier is unique across all sites.  The identifier uses six digits and is alphanumeric base 30, omitting the letters B, I, O, Q, S and Z as these can be confused with 8, 1, 0, 0, 5 and 2",
				null, null, LuhnMod30IdentifierValidator.class, PatientIdentifierType.LocationBehavior.NOT_USED, false, PatientIdentifierTypes.ZL_EMR_ID));


		log.info("Installing Core Visit Types");

		install(visitType("Clinic or Hospital Visit", "Patient visits the clinic/hospital (as opposed to a home visit, or telephone contact)", VisitTypes.CLINIC_OR_HOSPITAL_VISIT));

		log.info("Installing core Person Attribute Types");

		install(personAttributeType(PROVIDER_IDENTIFIER_NAME, "", String.class, null, true, 12, PersonAttributeTypes.PROVIDER_IDENTIFIER));
		install(personAttributeType(TELEPHONE_NUMBER_NAME, "The telephone number for the person", String.class, null, false, 7, PersonAttributeTypes.TELEPHONE_NUMBER));
		install(personAttributeType("Unknown patient", "Used to flag patients that cannot be identified during the check-in process", String.class, null, false, 13, PersonAttributeTypes.UNKNOWN_PATIENT));

		log.info("Installing core Encounter Types");

		install(encounterType(REGISTRATION_ENCOUNTER_NAME, "Patient registration -- normally a new patient", EncounterTypes.PATIENT_REGISTRATION));
		install(encounterType(CHECK_IN_ENCOUNTER_NAME, "Check-in encounter, formerly known as Primary care reception", EncounterTypes.CHECK_IN));
		install(encounterType(PRIMARY_CARE_VISIT_ENCOUNTER_NAME, "Primary care visit (In Kreyol, it&apos;s &apos;vizit swen primè&apos;)", EncounterTypes.PRIMARY_CARE_VISIT));
		install(encounterType("Rencontre de paiement", "Encounter used to capture patient payments", EncounterTypes.PAYMENT));
		install(encounterType("Signes vitaux", "Encounter where vital signs were captured, and triage may have been done, possibly for triage purposes, but a complete exam was not done.", EncounterTypes.VITALS));
		install(encounterType("Consultation", "Encounter where a full or abbreviated examination is done, leading to a presumptive or confirmed diagnosis", EncounterTypes.CONSULTATION));
		install(encounterType("Médicaments administrés", "When someone gets medicine from the pharmacy", EncounterTypes.MEDICATION_DISPENSED));
		install(encounterType("Note de chirurgie", "The surgeons&apos; notes after performing surgery", EncounterTypes.POST_OPERATIVE_NOTE));
		install(encounterType("Transfert", "Indicates that a patient is being transferred into a different department within the hospital. (Transfers out of the hospital should not use this encounter type.)", EncounterTypes.TRANSFER));
		install(encounterType("Admission aux soins hospitaliers", "Indicates that the patient has been admitted for inpatient care, and is not expected to leave the hospital unless discharged.", EncounterTypes.ADMISSION));
		install(encounterType("Annuler l'admission", "An encounter that notes that a request to admit a patient (via giving them a dispositon of &quot;admit&quot; on another form) is being overridden", EncounterTypes.CANCEL_ADMISSION));
		install(encounterType("Sortie de soins hospitaliers", "Indicates that a patient&apos;s inpatient care at the hospital is ending, and they are expected to leave soon", EncounterTypes.EXIT_FROM_CARE));

		log.info("Installing core Encounter Roles");

		install(encounterRole("Dispenser", "Provider that dispenses medications or other products", EncounterRoles.DISPENSER));
		install(encounterRole("Nurse", "A person educated and trained to care for the sick or disabled.", EncounterRoles.NURSE));
		install(encounterRole("Consulting Clinician", "Clinician who is primarily responsible for examining and diagnosing a patient", EncounterRoles.CONSULTING_CLINICIAN));
		install(encounterRole("Administrative Clerk", "This role is used for creating a Check-in encounter", EncounterRoles.ADMINISTRATIVE_CLERK));
		install(encounterRole("Ordering Provider", "For encounters associated with orders, used to store the provider responsible for placing the order", EncounterRoles.ORDERING_PROVIDER));

        log.info("Installing Locations");

        install(location("Unknown Location", "Unknown Location", Locations.UNKNOWN));

        log.info("Installing location tags");

        install(locationTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_LOGIN,"When a user logs in and chooses a session location, they may only choose one with this tag",LocationTags.LOGIN_LOCATION));
        install(locationTag(PaperRecordConstants.LOCATION_TAG_MEDICAL_RECORD_LOCATION,"Notes that this location is a valid identifier location for an identifier that references a paper medical record", LocationTags.MEDICAL_RECORD_LOCATION));
        install(locationTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_VISITS,"Visits are only allowed to happen at locations tagged with this location tag or at locations that descend from a location tagged with this tag.", LocationTags.VISIT_LOCATION));
        install(locationTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_ADMISSION,"Patients may only be admitted to inpatient care in a location with this tag", LocationTags.ADMISSION_LOCATION));
        install(locationTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_TRANSFER,"Patients may only be transfer to inpatient care in a location with this tag", LocationTags.TRANSFER_LOCAITON));
        install(locationTag("Consult Note Location","A location where a consult note can be written", LocationTags.CONSULT_NOTE_LOCATION));
        install(locationTag("Surgery Note Location","A location where a surgery note can be written", LocationTags.SURGERY_NOTE_LOCATION));
        install(locationTag("ED Note Location","A location where an ED note can be written", LocationTags.ED_NOTE_LOCATION));
        install(locationTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_DISPENSING,"A location where a pharmacist or pharmacist aide can dispensed medication.", LocationTags.DISPENSING_LOCATION));
        install(locationTag("Appointment Location","Signifies a Location where appointments can be scheduled", LocationTags.APPOINTMENT_LOCATION));

        log.info("Retired old location tags");
        uninstall(possible(LocationTag.class,RetiredLocationTags.OUTPATIENT_TRANSFER_LOCATION),  "never used");
        uninstall(possible(LocationTag.class,RetiredLocationTags.INPATIENT_TRANSFER_LOCATION),  "never used");

        log.info("Install location attributes");

        install(locationAttributeType("Location Code","Unique identifier assigned to the location.  (Currently used as a key when exchanging information with McKesson PACS via the PACS Integration module.)",
                FreeTextDatatype.class, null, 0, 1, LocationAttributeTypes.LOCATION_CODE));
        install(locationAttributeType("Default Label Printer", "The default label printer for this location",
                PrinterDatatype.class, "LABEL", 0, 1, LocationAttributeTypes.DEFAULT_LABEL_PRINTER));
        install(locationAttributeType("Default ID card Printer", "The default id card printer for this location",
                PrinterDatatype.class, "ID_CARD", 0, 1, LocationAttributeTypes.DEFAULT_ID_CARD_PRINTER));
        install(locationAttributeType("Default Wristband Printer", "The default wristband printer for this location",
                PrinterDatatype.class, "WRISTBAND", 0, 1, LocationAttributeTypes.DEFAULT_WRISTBAND_PRINTER));
        install(locationAttributeType("Name to print on ID card", "The name to use when printing a location on an id card",
                FreeTextDatatype.class, null, 0, 1, LocationAttributeTypes.NAME_TO_PRINT_ON_ID_CARD));

        log.info("Installing core Concepts");

		installMetadataSharingPackage("HUM_Metadata-57.zip", Packages.HUM_METADATA);

		log.info("Setting Global Properties");

		Map<String, String> properties = new LinkedHashMap<String, String>();
		
		// OpenMRS Core
		properties.put(OpenmrsConstants.GLOBAL_PROPERTY_LOCALE_ALLOWED_LIST, "ht, fr, en");
		properties.put(OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE, DEFAULT_LOCALE);
		properties.put(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8");
		properties.put(OpenmrsConstants.GP_PASSWORD_REQUIRES_DIGIT, "false");
		properties.put(OpenmrsConstants.GP_PASSWORD_REQUIRES_NON_DIGIT, "false");
		properties.put(OpenmrsConstants.GP_PASSWORD_REQUIRES_UPPER_AND_LOWER_CASE, "false");

		// TODO: Use Java API to produce xml and set this in same class that handles the address hierarchy
		properties.put(OpenmrsConstants.GLOBAL_PROPERTY_ADDRESS_TEMPLATE, "<org.openmrs.layout.web.address.AddressTemplate><nameMappings class=\"properties\"><property name=\"country\" value=\"mirebalais.address.country\"/><property name=\"stateProvince\" value=\"mirebalais.address.stateProvince\"/><property name=\"cityVillage\" value=\"mirebalais.address.cityVillage\"/><property name=\"address3\" value=\"mirebalais.address.neighborhoodCell\"/><property name=\"address1\" value=\"mirebalais.address.address1\"/><property name=\"address2\" value=\"mirebalais.address.address2\"/></nameMappings><sizeMappings class=\"properties\"><property name=\"country\" value=\"40\"/><property name=\"stateProvince\" value=\"40\"/><property name=\"cityVillage\" value=\"40\"/><property name=\"address3\" value=\"60\"/><property name=\"address1\" value=\"60\"/><property name=\"address2\" value=\"60\"/></sizeMappings><elementDefaults class=\"properties\"><property name=\"country\" value=\"Haiti\"/></elementDefaults><lineByLineFormat><string>address2</string><string>address1</string><string>address3, cityVillage</string><string>stateProvince, country</string></lineByLineFormat></org.openmrs.layout.web.address.AddressTemplate>");

		// Html Form Entry
		properties.put(HtmlFormEntryConstants.GP_DATE_FORMAT, DEFAULT_DATE_FORMAT);
		properties.put(HtmlFormEntryConstants.GP_SHOW_DATE_FORMAT, "false");
		properties.put(HtmlFormEntryConstants.GP_UNKNOWN_CONCEPT, Concepts.UNKNOWN);
		
		// Reporting
		properties.put(ReportingConstants.DEFAULT_LOCALE_GP_NAME, DEFAULT_LOCALE);
		properties.put(ReportingConstants.GLOBAL_PROPERTY_TEST_PATIENTS_COHORT_DEFINITION, "");

		// UI Framework
		properties.put(UiFrameworkConstants.GP_FORMATTER_DATE_FORMAT, DEFAULT_DATE_FORMAT);
		properties.put(UiFrameworkConstants.GP_FORMATTER_DATETIME_FORMAT, DEFAULT_DATETIME_FORMAT);
		
		// EMR API
		properties.put(EmrApiConstants.GP_CLINICIAN_ENCOUNTER_ROLE, EncounterRoles.CONSULTING_CLINICIAN);
		properties.put(EmrApiConstants.GP_ORDERING_PROVIDER_ENCOUNTER_ROLE, EncounterRoles.ORDERING_PROVIDER);
		properties.put(EmrApiConstants.GP_CHECK_IN_CLERK_ENCOUNTER_ROLE, EncounterRoles.ADMINISTRATIVE_CLERK);
		properties.put(EmrApiConstants.GP_AT_FACILITY_VISIT_TYPE, VisitTypes.CLINIC_OR_HOSPITAL_VISIT);
		properties.put(EmrApiConstants.GP_VISIT_NOTE_ENCOUNTER_TYPE, EncounterTypes.CONSULTATION);
		properties.put(EmrApiConstants.GP_CHECK_IN_ENCOUNTER_TYPE, EncounterTypes.CHECK_IN);
		properties.put(EmrApiConstants.GP_ADMISSION_ENCOUNTER_TYPE, EncounterTypes.ADMISSION);
		properties.put(EmrApiConstants.GP_EXIT_FROM_INPATIENT_ENCOUNTER_TYPE, EncounterTypes.EXIT_FROM_CARE);
		properties.put(EmrApiConstants.GP_TRANSFER_WITHIN_HOSPITAL_ENCOUNTER_TYPE, EncounterTypes.TRANSFER);
		properties.put(EmrApiConstants.GP_EXTRA_PATIENT_IDENTIFIER_TYPES, PatientIdentifierTypes.DOSSIER_NUMBER + "," + PatientIdentifierTypes.HIVEMR_V1);
		properties.put(EmrApiConstants.PRIMARY_IDENTIFIER_TYPE, ZL_EMR_ID_NAME);
		properties.put(EmrApiConstants.GP_DIAGNOSIS_SET_OF_SETS, Concepts.DIAGNOSIS_SET_OF_SETS);
		properties.put(EmrApiConstants.GP_UNKNOWN_LOCATION, Locations.UNKNOWN);
		properties.put(EmrApiConstants.GP_ADMISSION_FORM, Forms.ADMISSION);
		properties.put(EmrApiConstants.GP_TRANSFER_WITHIN_HOSPITAL_FORM, Forms.TRANSFER_WITHIN_HOSPITAL);
		properties.put(EmrApiConstants.GP_EXIT_FROM_INPATIENT_FORM, Forms.EXIT_FROM_INPATIENT);

		// REST
		// These do not use constants from the rest module due to the omod dependency when provided in maven.
		// These are used to increase the number of results that rest web services returns (for the appointment scheduling module)
		properties.put("webservices.rest.maxResultsAbsolute", "1000");
		properties.put("webservices.rest.maxResultsDefault", "500");
		
		// EMR
		properties.put(EmrConstants.PAYMENT_AMOUNT_CONCEPT, Concepts.PAYMENT_AMOUNT);
		properties.put(EmrConstants.PAYMENT_REASON_CONCEPT, Concepts.PAYMENT_REASON);
		properties.put(EmrConstants.PAYMENT_RECEIPT_NUMBER_CONCEPT, Concepts.PAYMENT_RECEIPT_NUMBER);
		properties.put(EmrConstants.PAYMENT_CONSTRUCT_CONCEPT, Concepts.PAYMENT_CONSTRUCT);
		
		// Name Phonetics
		properties.put(NamePhoneticsConstants.GIVEN_NAME_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);
		properties.put(NamePhoneticsConstants.MIDDLE_NAME_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);
		properties.put(NamePhoneticsConstants.FAMILY_NAME_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);
		properties.put(NamePhoneticsConstants.FAMILY_NAME2_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);

		// Patient Registration
		properties.put(PatientRegistrationGlobalProperties.SUPPORTED_TASKS, "patientRegistration|primaryCareReception|edCheckIn|patientLookup");
		properties.put(PatientRegistrationGlobalProperties.SEARCH_CLASS, DefaultPatientRegistrationSearch.class.getName());
		properties.put(PatientRegistrationGlobalProperties.LABEL_PRINT_COUNT, "1");
		properties.put(PatientRegistrationGlobalProperties.PROVIDER_ROLES, "LacollineProvider");  // TODO: Look into why this is set this way
		properties.put(PatientRegistrationGlobalProperties.ID_CARD_LABEL_TEXT, "Zanmi Lasante Patient ID Card");
		properties.put(PatientRegistrationGlobalProperties.BIRTH_YEAR_INTERVAL, "1");
		properties.put(PatientRegistrationGlobalProperties.MEDICAL_RECORD_LOCATION_TAG, LocationTags.MEDICAL_RECORD_LOCATION);
		properties.put(PatientRegistrationGlobalProperties.PRIMARY_IDENTIFIER_TYPE, ZL_EMR_ID_NAME);
		properties.put(PatientRegistrationGlobalProperties.NUMERO_DOSSIER, PatientIdentifierTypes.DOSSIER_NUMBER);
		properties.put(PatientRegistrationGlobalProperties.EXTERNAL_NUMERO_DOSSIER, PatientIdentifierTypes.EXTERNAL_DOSSIER_NUMBER);
		properties.put(PatientRegistrationGlobalProperties.PROVIDER_IDENTIFIER_PERSON_ATTRIBUTE_TYPE, PROVIDER_IDENTIFIER_NAME);
		properties.put(PatientRegistrationGlobalProperties.ID_CARD_PERSON_ATTRIBUTE_TYPE, TELEPHONE_NUMBER_NAME);
		properties.put(PatientRegistrationGlobalProperties.PATIENT_VIEWING_ATTRIBUTE_TYPES, TELEPHONE_NUMBER_NAME);
		properties.put(PatientRegistrationGlobalProperties.PATIENT_REGISTRATION_ENCOUNTER_TYPE, REGISTRATION_ENCOUNTER_NAME);
		properties.put(PatientRegistrationGlobalProperties.PRIMARY_CARE_RECEPTION_ENCOUNTER_TYPE, CHECK_IN_ENCOUNTER_NAME);
		properties.put(PatientRegistrationGlobalProperties.PRIMARY_CARE_VISIT_ENCOUNTER_TYPE, PRIMARY_CARE_VISIT_ENCOUNTER_NAME);

		// TODO: For all of these, determine best way to set constants against these
		properties.put(PatientRegistrationGlobalProperties.URGENT_DIAGNOSIS_CONCEPT, "PIH: Haiti nationally urgent diseases");
		properties.put(PatientRegistrationGlobalProperties.NOTIFY_DIAGNOSIS_CONCEPT, "PIH: Haiti nationally notifiable diseases");
		properties.put(PatientRegistrationGlobalProperties.NON_CODED_DIAGNOSIS_CONCEPT, "PIH: ZL Primary care diagnosis non-coded");
		properties.put(PatientRegistrationGlobalProperties.NEONATAL_DISEASES_CONCEPT, "PIH: Haiti neonatal diseases");
		properties.put(PatientRegistrationGlobalProperties.CODED_DIAGNOSIS_CONCEPT, "PIH: HUM Outpatient diagnosis");
		properties.put(PatientRegistrationGlobalProperties.AGE_RESTRICTED_CONCEPT, "PIH: Haiti age restricted diseases");
		properties.put(PatientRegistrationGlobalProperties.RECEIPT_NUMBER_CONCEPT, "PIH: Receipt number|en:Receipt Number|ht:Nimewo Resi a");
		properties.put(PatientRegistrationGlobalProperties.PAYMENT_AMOUNT_CONCEPT, "PIH: Payment amount|en:Payment amount|ht:Kantite lajan");
		properties.put(PatientRegistrationGlobalProperties.VISIT_REASON_CONCEPT, "PIH: Type of HUM visit|en:Visit reason|ht:Tip de Vizit");
		properties.put(PatientRegistrationGlobalProperties.ICD10_CONCEPT_SOURCE, "ICD-10");
		
		// Paper Record
		properties.put(PaperRecordConstants.GP_PAPER_RECORD_IDENTIFIER_TYPE, PatientIdentifierTypes.DOSSIER_NUMBER);
		properties.put(PaperRecordConstants.GP_EXTERNAL_DOSSIER_IDENTIFIER_TYPE, PatientIdentifierTypes.EXTERNAL_DOSSIER_NUMBER);
		
		setGlobalProperties(properties);
	}
}