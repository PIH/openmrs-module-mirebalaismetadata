package org.openmrs.module.mirebalaismetadata;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.emrapi.metadata.MetadataPackageConfig;
import org.openmrs.module.emrapi.metadata.MetadataPackagesConfig;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatasharing.ImportedPackage;
import org.openmrs.module.metadatasharing.api.MetadataSharingService;
import org.openmrs.module.mirebalaismetadata.deploy.bundle.ConceptsFromMetadataSharing;
import org.openmrs.module.pihcore.config.Config;
import org.openmrs.module.pihcore.config.ConfigDescriptor;
import org.openmrs.module.pihcore.deploy.bundle.haiti.HaitiMetadataBundle;
import org.openmrs.module.pihcore.deploy.bundle.haiti.mirebalais.MirebalaisBundle;
import org.openmrs.module.pihcore.deploy.bundle.haiti.mirebalais.MirebalaisRadiologyBundle;
import org.openmrs.module.pihcore.metadata.core.OrderTypes;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.test.SkipBaseSetup;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.validator.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SkipBaseSetup          // note that we skip the base setup because we don't want to include the standard test data
public class MirebalaisMetadataActivatorComponentTest extends BaseMirebalaisMetadataContextSensitiveTest {

    private MirebalaisMetadataActivator mirebalaisMetadataActivator;

    @Autowired
    private ConceptService conceptService;

	@Autowired
	private MetadataDeployService metadataDeployService;

    @Autowired
    MirebalaisMetadataProperties mirebalaisMetadataProperties;

    @Autowired
    private ConceptsFromMetadataSharing conceptsFromMetadataSharing;

    @Before
    public void beforeEachTest() throws Exception {

        initializeInMemoryDatabase();
        executeDataSet("requiredDataTestDataset.xml");

        authenticate();

        // set up metadata from pih core first
        metadataDeployService.installBundle(conceptsFromMetadataSharing);
        metadataDeployService.installBundle(Context.getRegisteredComponents(MirebalaisBundle.class).get(0));

        Config config = mock(Config.class);
        when(config.getCountry()).thenReturn(ConfigDescriptor.Country.HAITI);
        when(config.getSite()).thenReturn(ConfigDescriptor.Site.MIREBALAIS);

        mirebalaisMetadataActivator = new MirebalaisMetadataActivator(mirebalaisMetadataProperties);
        mirebalaisMetadataActivator.setConfig(config);
        mirebalaisMetadataActivator.willRefreshContext();
        mirebalaisMetadataActivator.contextRefreshed();
        mirebalaisMetadataActivator.willStart();
        mirebalaisMetadataActivator.started();
    }

    @Test
    @Ignore
    public void testThatActivatorDoesAllSetup() throws Exception {
        verifyMetadataPackagesConfigured();
        verifyDrugListLoaded();
        //verifyConceptNamesInAllLanguages(); ignore this test until we have fixed the data
    }


    private void verifyMetadataPackagesConfigured() throws Exception {
        MetadataPackagesConfig config;
        {
            InputStream inputStream = mirebalaisMetadataActivator.getClass().getClassLoader().getResourceAsStream(MetadataUtil.PACKAGES_FILENAME);
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
		groupUuids.add(MirebalaisRadiologyBundle.Packages.RADIOLOGY_ORDERABLES);
		groupUuids.add(HaitiMetadataBundle.Packages.HUM_METADATA);

        for (ImportedPackage importedPackage : metadataSharingService.getAllImportedPackages()) {
            if (!groupUuids.contains(importedPackage.getGroupUuid())) {
                Assert.fail("Found a package with an unexpected groupUuid. Name: " + importedPackage.getName()
                        + " , groupUuid: " + importedPackage.getGroupUuid());
            }
        }

        for (MetadataPackageConfig metadataPackage : config.getPackages()) {
            ImportedPackage installedPackage = metadataSharingService.getImportedPackageByGroup(metadataPackage
                    .getGroupUuid());

            // hack to ignore Liberia package, which we are not installing in Mirebalais (which is what we are testing here)
            if (!metadataPackage.getFilenameBase().equals("Liberia_Concepts")) {
                Integer actualVersion = installedPackage == null ? null : installedPackage.getVersion();
                        assertEquals("Failed to install " + metadataPackage.getFilenameBase() + ". Expected version: "
                            + metadataPackage.getVersion() + " Actual version: " + actualVersion, metadataPackage.getVersion(),
                    actualVersion);
            }
            else {
                // liberia package should not be installed in Mirebalais
                assertNull(installedPackage);
            }
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


