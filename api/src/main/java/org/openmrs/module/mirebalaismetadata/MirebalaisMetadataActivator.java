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
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.resolver.Resolver;
import org.openmrs.module.metadatasharing.resolver.impl.ConceptReferenceTermResolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByNameResolver;
import org.openmrs.module.metadatasharing.resolver.impl.ObjectByUuidResolver;
import org.openmrs.module.pihcore.config.Config;
import org.openmrs.module.pihcore.config.ConfigDescriptor;
import org.openmrs.module.pihcore.deploy.bundle.haiti.HaitiMetadataToInstallAfterConceptsBundle;
import org.openmrs.module.pihcore.deploy.bundle.liberia.LiberiaMetadataToInstallAfterConceptsBundle;
import org.openmrs.module.pihcore.deploy.bundle.mexico.MexicoMetadataToInstallAfterConceptsBundle;
import org.openmrs.module.pihcore.deploy.bundle.sierraLeone.SierraLeoneMetadataToInstallAfterConceptsBundle;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class MirebalaisMetadataActivator extends BaseModuleActivator {

    protected Log log = LogFactory.getLog(getClass());

    protected static final Integer DRUG_LIST_VERSION = 16;

    protected static final Integer CES_DRUG_LIST_VERSION = 4;

    protected static final Integer SL_DRUG_LIST_VERSION = 2;

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
            installMetadataBundles(config);

            if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
                retireOldConcepts();
            }

            installDrugList(config);
        }
		catch (Exception e) {
            Module mod = ModuleFactory.getModuleById("mirebalaismetadata");
            ModuleFactory.stopModule(mod, true, true);
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

        // set up the concept sources first because of TRUNK-5326
        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "PIH_Concept_Sources");

        // make sure we load these all with one call to "setupSpecificMetadata" because that method makes sure to
        // sort the loading by dateCreated to avoid issues with inconsistent metadata
        if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(),
                    "HUM_Radiology_Orderables",
                    "HUM_Metadata",
                    "HUM_Clinical_Concepts",
                    "HUM_Medication",
                    "HUM_Dispensing_Concepts",
                    "HUM_Disposition_Concepts",
                    "HUM_Surgery",
                    "HUM_Scheduling",
                    "HUM_Provider_Roles",
                    "HUM_Oncology",
                    "PIH_Allergies",
                    "PIH_Exam",
                    "PIH_Labs",
                    "PIH_History",
                    "PIH_Pediatric_Feeding",
                    "PIH_Pediatric_Supplements",
                    "HUM_NCD",
                    "PIH_Mental_Health",
                    "HUM_Emergency_Triage",
                    "HUM_Pathology",
                    "Haiti_HIV",
                    "Haiti_Zika",
                    "PIH_Socio_Economics",
                    "PIH_Maternal_Child_Health",
                    "Oncology");
        }
        // TODO changed this so we install all concepts on Liberia servers as well (plus Liberia Concept set which may no longer be necessary)
        // TODO is this okay?
        else if (config.getCountry().equals(ConfigDescriptor.Country.LIBERIA)) {
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(),
                  "Liberia_Concepts",
                    "HUM_Radiology_Orderables",
                    "HUM_Metadata",
                    "HUM_Clinical_Concepts",
                    "HUM_Medication",
                    "HUM_Dispensing_Concepts",
                    "HUM_Disposition_Concepts",
                    "HUM_Surgery",
                    "HUM_Scheduling",
                    "HUM_Provider_Roles",
                    "HUM_Oncology",
                    "PIH_Allergies",
                    "PIH_Exam",
                    "PIH_Labs",
                    "PIH_History",
                    "PIH_Pediatric_Feeding",
                    "PIH_Pediatric_Supplements",
                    "HUM_NCD",
                    "PIH_Mental_Health",
                    "HUM_Emergency_Triage",
                    "HUM_Pathology",
                    "Haiti_HIV",
                    "Haiti_Zika",
                    "PIH_Socio_Economics",
                    "PIH_Maternal_Child_Health");

        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.SIERRA_LEONE)) {
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(),
		    "Haiti_HIV",
                    "HUM_Radiology_Orderables",
                    "HUM_Clinical_Concepts",
                    "HUM_Dispensing_Concepts",
                    "HUM_Disposition_Concepts",
		            "HUM_Emergency_Triage",
                    "HUM_Surgery",
                    "HUM_Medication",
                    "HUM_Metadata",
                    "HUM_NCD",  // provides hypertension program
		            "HUM_Pathology",
                    "HUM_Provider_Roles",
                    "PIH_Allergies",
                    "PIH_Exam",
                    "PIH_History",
                    "PIH_Labs",
                    "PIH_Maternal_Child_Health",  // programs dependency
                    "PIH_Mental_Health",
                    "PIH_Pediatric_Feeding",
                    "PIH_Pediatric_Supplements",
                    "PIH_Socio_Economics",
                    "Sierra_Leone_Concepts");
        } else if (config.getCountry().equals(ConfigDescriptor.Country.MEXICO)) {
            MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(),
                    "Mexico_Concepts",
                    "HUM_Clinical_Concepts",
                    "HUM_Dispensing_Concepts",
                    "HUM_Disposition_Concepts",
                    "HUM_Medication",
                    "HUM_Metadata",
                    "HUM_NCD",  // provides program concepts
                    "HUM_Provider_Roles",
                    "PIH_Allergies",
                    "PIH_Exam",
                    "PIH_History",
                    "PIH_Labs",
                    "PIH_Maternal_Child_Health",  // "programs" component dependency
                    "PIH_Mental_Health",
                    "PIH_Pediatric_Feeding",
                    "PIH_Pediatric_Supplements",
                    "PIH_Socio_Economics");
        }

        Context.flushSession();
    }


    private void installMetadataBundles(Config config) {

        MetadataDeployService deployService = Context.getService(MetadataDeployService.class);

        // make this more dynamic, less dependent on if-thens
        if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
            deployService.installBundle(Context.getRegisteredComponents(HaitiMetadataToInstallAfterConceptsBundle.class).get(0));
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.LIBERIA)) {
            deployService.installBundle(Context.getRegisteredComponents(LiberiaMetadataToInstallAfterConceptsBundle.class).get(0));
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.MEXICO)) {
            deployService.installBundle(Context.getRegisteredComponents(MexicoMetadataToInstallAfterConceptsBundle.class).get(0));
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.SIERRA_LEONE)) {
            deployService.installBundle(Context.getRegisteredComponents(SierraLeoneMetadataToInstallAfterConceptsBundle.class).get(0));
        }

    }


    private void installDrugList(Config config) throws IOException {

        if (config.getCountry().equals(ConfigDescriptor.Country.HAITI) || config.getCountry().equals(ConfigDescriptor.Country.LIBERIA)) {
            installSpecificDrugList("HUM_Drug_List-", DRUG_LIST_VERSION, MirebalaisMetadataProperties.GP_INSTALLED_DRUG_LIST_VERSION);
        } else if (config.getCountry().equals(ConfigDescriptor.Country.MEXICO)) {
            installSpecificDrugList("CES_Drug_List-", CES_DRUG_LIST_VERSION, MirebalaisMetadataProperties.GP_INSTALLED_CES_DRUG_LIST_VERSION);
        } else if (config.getCountry().equals(ConfigDescriptor.Country.SIERRA_LEONE)) {
            installSpecificDrugList("SL_Drug_List-", SL_DRUG_LIST_VERSION, MirebalaisMetadataProperties.GP_INSTALLED_SL_DRUG_LIST_VERSION);
        }
    }

    private void installSpecificDrugList(String csvFileNamePrefix, Integer version, String installedDrugListVersionGp) throws IOException {

        int installedDrugListVersion = getIntegerByGlobalProperty(installedDrugListVersionGp, -1);

        if (installedDrugListVersion < version) {

            // special-case to retire any demo drugs before installing the first package
            if (installedDrugListVersion == 0) {
                retireExistingDemoDrugs();
            }

            String csvFileName = csvFileNamePrefix + version + ".csv";
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            ImportNotes notes = drugImporter.importSpreadsheet(reader);

            if (notes.hasErrors()) {
                System.out.println(notes);
                throw new RuntimeException("Unable to install drug list");
            } else {

                // update the installed version
                GlobalProperty installedDrugListVersionObject = Context.getAdministrationService()
                        .getGlobalPropertyObject(installedDrugListVersionGp);
                if (installedDrugListVersionObject == null) {
                    installedDrugListVersionObject = new GlobalProperty();
                    installedDrugListVersionObject.setProperty(installedDrugListVersionGp);
                }
                installedDrugListVersionObject.setPropertyValue(version.toString());
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
