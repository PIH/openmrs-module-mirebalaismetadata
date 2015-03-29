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


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.addresshierarchy.AddressField;
import org.openmrs.module.addresshierarchy.AddressHierarchyLevel;
import org.openmrs.module.addresshierarchy.service.AddressHierarchyService;
import org.openmrs.module.addresshierarchy.util.AddressHierarchyImportUtil;
import org.openmrs.module.dispensing.importer.DrugImporter;
import org.openmrs.module.dispensing.importer.ImportNotes;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.resolver.ConceptReferenceTerm19Resolver;
import org.openmrs.module.metadatasharing.resolver.Resolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByNameResolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByUuidResolver;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.GP_INSTALLED_ADDRESS_HIERARCHY_VERSION;
import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.GP_INSTALLED_DRUG_LIST_VERSION;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class MirebalaisMetadataActivator extends BaseModuleActivator {

    protected Log log = LogFactory.getLog(getClass());

    private final String ADDRESS_HIERARCHY_CSV_FILE = "org/openmrs/module/mirebalaismetadata/addresshierarchy/haiti_address_hierarchy_entries";

    private static final Integer ADDRESS_HIERARCHY_VERSION = 5;

    protected static final Integer DRUG_LIST_VERSION = 4;

    private MirebalaisMetadataProperties mirebalaisMetadataProperties;

    private DrugImporter drugImporter;

    private ConceptService conceptService;

    private MessageSource messageSource;

    private AdministrationService administrationService;

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
        // except for in specific ways. (Specifically we don't want to use ConceptByMappingResolver, but in general we
        // want to avoid unexpected behavior.)
        // See https://tickets.openmrs.org/browse/META-323
        ObjectByUuidResolver byUuidResolver = new ObjectByUuidResolver();
        ObjectByNameResolver byNameResolver = new ObjectByNameResolver();
        ConceptReferenceTerm19Resolver referenceTermResolver = new ConceptReferenceTerm19Resolver(byNameResolver, byUuidResolver);

        List<Resolver<?>> supportedResolvers = new ArrayList<Resolver<?>>();
        supportedResolvers.add(byUuidResolver);
        supportedResolvers.add(byNameResolver);
        supportedResolvers.add(referenceTermResolver);
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
        if (drugImporter == null) {
            drugImporter = Context.getRegisteredComponents(DrugImporter.class).get(0);
        }
        if (conceptService == null) {
            conceptService = Context.getConceptService();
        }
        if (messageSource == null) {
            messageSource = Context.getMessageSourceService().getActiveMessageSource();
        }
        if (administrationService == null) {
            administrationService = Context.getAdministrationService();
        }

        try {
            retireOldConcepts();
			installBundles();
            installMetadataPackages();
            installDrugList();
            setupAddressHierarchy();
        }
		catch (Exception e) {
            Module mod = ModuleFactory.getModuleById("mirebalaismetadata");
            ModuleFactory.stopModule(mod);
            throw new RuntimeException("Failed to start the mirebalaismetadata module", e);
        }
        log.info("Mirebalais Metadata module started");
    }

    private EmrApiProperties getEmrApiProperties() {
        return Context.getRegisteredComponents(EmrApiProperties.class).iterator().next();
    }

    private void retireOldConcepts() {
        // we need to retire this old concept if it exists because another concept we will install later has the same French name
        // #UHM-1789 - Removed concept "cerebellar infarction”from HUM ED set, and added “cerebral infarction"
        Concept concept = conceptService.getConceptByUuid("145906AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        if (concept != null) {
            conceptService.retireConcept(concept, "replaced with by 155479AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
    }

	private void installBundles() throws Exception {
		MetadataManager manager = Context.getRegisteredComponents(MetadataManager.class).get(0);
		manager.refresh();
	}

    private void installMetadataPackages() throws Exception {
        MetadataUtil.setupStandardMetadata(getClass().getClassLoader());
        Context.flushSession();
    }

    private void installDrugList() throws IOException {

        int installedDrugListVersion = getIntegerByGlobalProperty(MirebalaisMetadataProperties.GP_INSTALLED_DRUG_LIST_VERSION, -1);

        if (installedDrugListVersion < DRUG_LIST_VERSION) {

            // special-case to retire any demo drugs before installing the first package
            if (installedDrugListVersion == 0) {
                retireExistingDemoDrugs();
            }

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("HUM_Drug_List-"
                    + DRUG_LIST_VERSION + ".csv");
            InputStreamReader reader = new InputStreamReader(inputStream);
            ImportNotes notes = drugImporter.importSpreadsheet(reader);

            if (notes.hasErrors()) {
                System.out.println(notes);
                throw new RuntimeException("Unable to install drug list");
            }
            else {

                // update the installed version
                GlobalProperty installedDrugListVersionObject = Context.getAdministrationService()
                        .getGlobalPropertyObject(GP_INSTALLED_DRUG_LIST_VERSION);
                if (installedDrugListVersionObject == null) {
                    installedDrugListVersionObject = new GlobalProperty();
                    installedDrugListVersionObject.setProperty(GP_INSTALLED_DRUG_LIST_VERSION);
                }
                installedDrugListVersionObject.setPropertyValue(DRUG_LIST_VERSION.toString());
                Context.getAdministrationService().saveGlobalProperty(installedDrugListVersionObject);
            }
        }
    }

    private void retireExistingDemoDrugs() {

        for (Drug drug : conceptService.getAllDrugs(true)) {
            conceptService.retireDrug(drug, "sample drug");
        }

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
            country.setRequired(true);
            ahService.saveAddressHierarchyLevel(country);

            AddressHierarchyLevel stateProvince = new AddressHierarchyLevel();
            stateProvince.setAddressField(AddressField.STATE_PROVINCE);
            stateProvince.setParent(country);
            stateProvince.setRequired(true);
            ahService.saveAddressHierarchyLevel(stateProvince);

            AddressHierarchyLevel cityVillage = new AddressHierarchyLevel();
            cityVillage.setAddressField(AddressField.CITY_VILLAGE);
            cityVillage.setParent(stateProvince);
            cityVillage.setRequired(true);
            ahService.saveAddressHierarchyLevel(cityVillage);

            AddressHierarchyLevel address3 = new AddressHierarchyLevel();
            address3.setAddressField(AddressField.ADDRESS_3);
            address3.setParent(cityVillage);
            address3.setRequired(true);
            ahService.saveAddressHierarchyLevel(address3);

            AddressHierarchyLevel address1 = new AddressHierarchyLevel();
            address1.setAddressField(AddressField.ADDRESS_1);
            address1.setParent(address3);
            address1.setRequired(true);
            ahService.saveAddressHierarchyLevel(address1);

            AddressHierarchyLevel address2 = new AddressHierarchyLevel();
            address2.setAddressField(AddressField.ADDRESS_2);
            address2.setParent(address1);
            address2.setRequired(false);
            ahService.saveAddressHierarchyLevel(address2);
        }
        // at least verify that the right levels exist
        // also set required status of each field (as we'll start using it now)
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
                level.setRequired(!level.getAddressField().equals(AddressField.ADDRESS_2));
                ahService.saveAddressHierarchyLevel(level);
                i++;
            }
        }

        // load in the csv file if necessary
        int installedAddressHierarchyVersion = getIntegerByGlobalProperty(MirebalaisMetadataProperties.GP_INSTALLED_ADDRESS_HIERARCHY_VERSION, -1);

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

	protected Integer getIntegerByGlobalProperty(String globalPropertyName, Integer defaultValue) {
		String value = administrationService.getGlobalProperty(globalPropertyName);
		if (StringUtils.isNotEmpty(value)) {
			try {
				return Integer.valueOf(value);
			}
			catch (Exception e) {
				throw new IllegalStateException("Global property " + globalPropertyName + " value of " + value + " is not parsable as an Integer");
			}
		}
		else {
			return defaultValue;
		}
	}
}
