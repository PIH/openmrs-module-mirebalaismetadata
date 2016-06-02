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
import org.openmrs.module.dispensing.importer.DrugImporter;
import org.openmrs.module.dispensing.importer.ImportNotes;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.resolver.Resolver;
import org.openmrs.module.metadatasharing.resolver.impl.ConceptReferenceTermResolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByNameResolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByUuidResolver;
import org.openmrs.module.pihcore.config.Config;
import org.openmrs.module.pihcore.config.ConfigDescriptor;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.mirebalaismetadata.MirebalaisMetadataProperties.GP_INSTALLED_DRUG_LIST_VERSION;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class MirebalaisMetadataActivator extends BaseModuleActivator {

    protected Log log = LogFactory.getLog(getClass());

    protected static final Integer DRUG_LIST_VERSION = 7;

    private MirebalaisMetadataProperties mirebalaisMetadataProperties;

    private DrugImporter drugImporter;

    private ConceptService conceptService;

    private MessageSource messageSource;

    private AdministrationService administrationService;

    private Config config;

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
        ObjectByUuidResolver byUuidResolver = Context.getRegisteredComponent("metadatasharing.ObjectByUuidResolver", ObjectByUuidResolver.class);
        ObjectByNameResolver byNameResolver =Context.getRegisteredComponent("metadatasharing.ObjectByNameResolver", ObjectByNameResolver.class);
        ConceptReferenceTermResolver referenceTermResolver =  Context.getRegisteredComponent("metadatasharing.ConceptReferenceTermResolver", ConceptReferenceTermResolver.class);

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

        if (config == null) {  // hack to allow injecting a mock config for testing
            config = Context.getRegisteredComponents(Config.class).get(0); // currently only one of these
        }

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

            installMetadataPackages(config);

            if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
                retireOldConcepts();
                installDrugList();
            }
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

    private void installMetadataPackages(Config config) throws Exception {

        if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Radiology_Orderables");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Metadata");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Clinical_Concepts");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Dispensing_Concepts");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Medication");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Disposition_Concepts");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Surgery");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Scheduling");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Provider_Roles");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Oncology");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Allergies");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Exam");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Labs");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_History");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Pediatric_Feeding");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Pediatric_Supplements");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_NCD");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Mental_Health");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Emergency_Triage");
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.LIBERIA)) {
            // TODO this package should really be renamed to just Provider Roles, or PIH Provider Roles
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Provider_Roles");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Liberia_Concepts");
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.SIERRA_LEONE)) {
            // TODO this package should really be renamed to just Provider Roles, or PIH Provider Roles
            // TODO make custom MDS pacakage for Sierra Leone
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Provider_Roles");
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Liberia_Concepts");
        }


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

    // hack to allow us to inject a mock config during testing
    public void setConfig(Config config) {
        this.config = config;
    }
}
