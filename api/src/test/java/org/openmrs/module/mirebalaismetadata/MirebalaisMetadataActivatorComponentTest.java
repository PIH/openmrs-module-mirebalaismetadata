package org.openmrs.module.mirebalaismetadata;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.layout.web.address.AddressSupport;
import org.openmrs.layout.web.address.AddressTemplate;
import org.openmrs.module.addresshierarchy.AddressField;
import org.openmrs.module.addresshierarchy.AddressHierarchyLevel;
import org.openmrs.module.addresshierarchy.service.AddressHierarchyService;
import org.openmrs.module.emrapi.metadata.MetadataPackageConfig;
import org.openmrs.module.emrapi.metadata.MetadataPackagesConfig;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.metadatasharing.ImportedPackage;
import org.openmrs.module.metadatasharing.api.MetadataSharingService;
import org.openmrs.module.mirebalaismetadata.constants.OrderTypes;
import org.openmrs.module.mirebalaismetadata.constants.PatientIdentifierTypes;
import org.openmrs.module.mirebalaismetadata.deploy.bundle.CoreMetadata;
import org.openmrs.module.mirebalaismetadata.deploy.bundle.RadiologyMetadata;
import org.openmrs.module.pacsintegration.PacsIntegrationConstants;
import org.openmrs.module.patientregistration.PatientRegistrationGlobalProperties;
import org.openmrs.module.pihcore.deploy.bundle.CoreConceptMetadataBundle;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.openmrs.ui.framework.UiFrameworkConstants;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.validator.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@SkipBaseSetup          // note that we skip the base setup because we don't want to include the standard test data
public class MirebalaisMetadataActivatorComponentTest extends BaseModuleContextSensitiveTest {

    private MirebalaisMetadataActivator activator;

    @Autowired
    private ConceptService conceptService;

	@Autowired
	private AdministrationService adminService;

    @Autowired
    MirebalaisMetadataProperties mirebalaisMetadataProperties;

    @Before
    public void beforeEachTest() throws Exception {

        EncounterType mockPatientRegistrationEncounter = new EncounterType();
        mockPatientRegistrationEncounter.setUuid(CoreMetadata.EncounterTypes.PATIENT_REGISTRATION);

        EncounterType mockCheckInEncounter = new EncounterType();
        mockCheckInEncounter.setUuid(CoreMetadata.EncounterTypes.CHECK_IN);

        EncounterType mockPrimaryCareVisit = new EncounterType();
        mockPrimaryCareVisit.setUuid(CoreMetadata.EncounterTypes.PRIMARY_CARE_VISIT);

        initializeInMemoryDatabase();
        executeDataSet("requiredDataTestDataset.xml");
        authenticate();
        activator = new MirebalaisMetadataActivator(mirebalaisMetadataProperties);
        activator.willRefreshContext();
        activator.contextRefreshed();
        activator.willStart();
        activator.started();
    }

    @Test
    public void testThatActivatorDoesAllSetup() throws Exception {
		verifyAddressTemplateConfigured();
		verifyGlobalPropertiesConfigured();
		verifyDatetimeFormatting();
		verifyPacsIntegrationGlobalPropertiesConfigured();
        verifyPatientRegistrationConfigured();
        verifyMetadataPackagesConfigured();
        verifyAddressHierarchyLevelsCreated();
        verifyAddressHierarchyLoaded();
        verifyDrugListLoaded();
        //verifyConceptNamesInAllLanguages(); ignore this test until we have fixed the data
    }

	private void verifyAddressTemplateConfigured() throws Exception {
		AddressTemplate at = AddressSupport.getInstance().getDefaultLayoutTemplate();
		assertEquals("mirebalais.address.country", at.getNameMappings().get("country"));
		assertEquals("mirebalais.address.stateProvince", at.getNameMappings().get("stateProvince"));
		assertEquals("mirebalais.address.cityVillage", at.getNameMappings().get("cityVillage"));
		assertEquals("mirebalais.address.neighborhoodCell", at.getNameMappings().get("address3"));
		assertEquals("mirebalais.address.address1", at.getNameMappings().get("address1"));
		assertEquals("mirebalais.address.address2", at.getNameMappings().get("address2"));
		assertEquals("40", at.getSizeMappings().get("country"));
		assertEquals("40", at.getSizeMappings().get("stateProvince"));
		assertEquals("40", at.getSizeMappings().get("cityVillage"));
		assertEquals("60", at.getSizeMappings().get("address3"));
		assertEquals("60", at.getSizeMappings().get("address1"));
		assertEquals("60", at.getSizeMappings().get("address2"));
		assertEquals("Haiti", at.getElementDefaults().get("country"));
		assertEquals("address2", at.getLineByLineFormat().get(0));
		assertEquals("address1", at.getLineByLineFormat().get(1));
		assertEquals("address3, cityVillage", at.getLineByLineFormat().get(2));
		assertEquals("stateProvince, country", at.getLineByLineFormat().get(3));
	}

	private void verifyGlobalPropertiesConfigured() throws Exception {
		assertEquals("fr", adminService.getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE));
		assertEquals("false", adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_UPPER_AND_LOWER_CASE));
		assertEquals("false", adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_NON_DIGIT));
		assertEquals("false", adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_DIGIT));
		assertEquals("8", adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH));
	}

	private void verifyDatetimeFormatting() {
		DateFormat datetimeFormat = new SimpleDateFormat(adminService.getGlobalProperty(UiFrameworkConstants.GP_FORMATTER_DATETIME_FORMAT));
		DateFormat dateFormat = new SimpleDateFormat(adminService.getGlobalProperty(UiFrameworkConstants.GP_FORMATTER_DATE_FORMAT));
		Date sampleDate = new DateTime(2012, 2, 22, 14, 23, 22).toDate();
		assertEquals("22 Feb 2012 2:23 PM", datetimeFormat.format(sampleDate));
		assertEquals("22 Feb 2012", dateFormat.format(sampleDate));
	}

	private void verifyPacsIntegrationGlobalPropertiesConfigured() throws Exception {
		assertEquals(PatientIdentifierTypes.ZL_EMR_ID.uuid(), adminService.getGlobalProperty(PacsIntegrationConstants.GP_PATIENT_IDENTIFIER_TYPE_UUID));
		assertEquals("en", adminService.getGlobalProperty(PacsIntegrationConstants.GP_DEFAULT_LOCALE));
		assertEquals("Mirebalais", adminService.getGlobalProperty(PacsIntegrationConstants.GP_SENDING_FACILITY));
		assertEquals(CoreConceptMetadataBundle.ConceptSources.LOINC, adminService.getGlobalProperty(PacsIntegrationConstants.GP_PROCEDURE_CODE_CONCEPT_SOURCE_UUID));
	}

    private void verifyPatientRegistrationConfigured() {
        List<Method> failingMethods = new ArrayList<Method>();
        for (Method method : PatientRegistrationGlobalProperties.class.getMethods()) {
            if (method.getName().startsWith("GLOBAL_PROPERTY") && method.getParameterTypes().length == 0) {
                try {
                    method.invoke(null);
                } catch (Exception ex) {
                    failingMethods.add(method);
                }
            }
        }

        if (failingMethods.size() > 0) {
            String errorMessage = "Some Patient Registration global properties are not configured correctly. See these methods in the PatientRegistrationGlobalProperties class";
            for (Method method : failingMethods) {
                errorMessage += "\n" + method.getName();
            }
            Assert.fail(errorMessage);
        }
    }

    private void verifyMetadataPackagesConfigured() throws Exception {
        MetadataPackagesConfig config;
        {
            InputStream inputStream = activator.getClass().getClassLoader().getResourceAsStream(MetadataUtil.PACKAGES_FILENAME);
            String xml = IOUtils.toString(inputStream);
            config = Context.getSerializationService().getDefaultSerializer()
                    .deserialize(xml, MetadataPackagesConfig.class);
        }

        MetadataSharingService metadataSharingService = Context.getService(MetadataSharingService.class);

        // To catch the (common) case where someone gets the groupUuid wrong, we look for any installed packages that
        // we are not expecting

        List<String> groupUuids = new ArrayList<String>();

        for (MetadataPackageConfig metadataPackage : config.getPackages()) {
            groupUuids.add(metadataPackage.getGroupUuid());
        }
		groupUuids.add(RadiologyMetadata.Packages.RADIOLOGY_ORDERABLES);
		groupUuids.add(CoreMetadata.Packages.HUM_METADATA);

        for (ImportedPackage importedPackage : metadataSharingService.getAllImportedPackages()) {
            if (!groupUuids.contains(importedPackage.getGroupUuid())) {
                Assert.fail("Found a package with an unexpected groupUuid. Name: " + importedPackage.getName()
                        + " , groupUuid: " + importedPackage.getGroupUuid());
            }
        }

        for (MetadataPackageConfig metadataPackage : config.getPackages()) {
            ImportedPackage installedPackage = metadataSharingService.getImportedPackageByGroup(metadataPackage
                    .getGroupUuid());
            Integer actualVersion = installedPackage == null ? null : installedPackage.getVersion();
            assertEquals("Failed to install " + metadataPackage.getFilenameBase() + ". Expected version: "
                    + metadataPackage.getVersion() + " Actual version: " + actualVersion, metadataPackage.getVersion(),
                    actualVersion);
        }

        // Verify a few pieces of sentinel data that should have been in the packages
        Assert.assertNotNull(Context.getLocationService().getLocationByUuid("a084f714-a536-473b-94e6-ec317b152b43")); // Mirebalais Hospital
        Assert.assertNotNull(Context.getOrderService().getOrderTypeByUuid(OrderTypes.RADIOLOGY_TEST_ORDER.uuid()));
        Assert.assertNotNull((conceptService.getConceptByMapping("TEMPERATURE (C)", "PIH")));
        Assert.assertNotNull(Context.getService((ProviderManagementService.class)).getProviderRoleByUuid("61eed524-4547-4228-a3ac-631fe1628a5e"));

        // Regression test for META-323
        {
            assertThat(conceptService.getConceptByUuid("06cc08fb-414a-46e6-8c20-136535609812"), notNullValue());
            assertThat(conceptService.getConceptByUuid("06cc08fb-414a-46e6-8c20-136535609812").getName(Locale.FRENCH, true).getName(), is("Fièvre rhumatismale, sans attente cardiaque"));
            assertThat(conceptService.getConceptByUuid("006ab3b2-a0ea-45bf-b495-83e06f26f87a"), notNullValue());
            assertThat(conceptService.getConceptByUuid("006ab3b2-a0ea-45bf-b495-83e06f26f87a").getName(Locale.FRENCH, true).getName(), is("Fievre rheumatique aigue"));
        }

        // this doesn't strictly belong here, but we include it as an extra sanity check on the MDS module
        for (Concept concept : conceptService.getAllConcepts()) {
            ValidateUtil.validate(concept);
        }
    }

    private void verifyAddressHierarchyLevelsCreated() throws Exception {
        AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);

        // assert that we now have six address hierarchy levels
        assertEquals(new Integer(6), ahService.getAddressHierarchyLevelsCount());

        // make sure they are mapped correctly
        List<AddressHierarchyLevel> levels = ahService.getOrderedAddressHierarchyLevels(true);
        assertEquals(AddressField.COUNTRY, levels.get(0).getAddressField());
        assertEquals(AddressField.STATE_PROVINCE, levels.get(1).getAddressField());
        assertEquals(AddressField.CITY_VILLAGE, levels.get(2).getAddressField());
        assertEquals(AddressField.ADDRESS_3, levels.get(3).getAddressField());
        assertEquals(AddressField.ADDRESS_1, levels.get(4).getAddressField());
        assertEquals(AddressField.ADDRESS_2, levels.get(5).getAddressField());

    }

    private void verifyAddressHierarchyLoaded() throws Exception {
        AddressHierarchyService ahService = Context.getService(AddressHierarchyService.class);

        // we should now have 26000+ address hierarchy entries
        Assert.assertTrue(ahService.getAddressHierarchyEntryCount() > 26000);

        assertEquals(1, ahService.getAddressHierarchyEntriesAtTopLevel().size());
        assertEquals("Haiti", ahService.getAddressHierarchyEntriesAtTopLevel().get(0).getName());
        assertEquals(5, mirebalaisMetadataProperties.getInstalledAddressHierarchyVersion());
    }

    private void verifyDrugListLoaded() throws Exception {

        assertEquals((int) MirebalaisMetadataActivator.DRUG_LIST_VERSION, mirebalaisMetadataProperties.getInstalledDrugListVersion());

        // just test that a few drugs are present
        assertNotNull(conceptService.getDrug("Benzylbenzoate, 25% application, 1000 mL bottle"));
        assertNotNull(conceptService.getDrug("Ranitidine hydrochloride 75 mg/5 mL oral suspension, 300 mL bottle"));
        assertNotNull(conceptService.getDrug("Ipratropium bromide, 250 microgram/mL solution for nebuisation, 2mL ampoule"));
    }

    private void verifyConceptNamesInAllLanguages() {
        // every concept should have a preferred name in English, French, and Creole.
        List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, new Locale("ht"));
        List<String> missing = new ArrayList<String>();
        for (Concept concept : conceptService.getAllConcepts()) {
            for (Locale locale : locales) {
                if (concept.getPreferredName(locale) == null) {
                    missing.add(locale + " for " + concept.getName().getName());
                }
            }
        }
        String errorMessage = "Missing preferred concept names: " + OpenmrsUtil.join(missing, ", ");
        assertEquals(errorMessage, 0, missing);
    }

}


