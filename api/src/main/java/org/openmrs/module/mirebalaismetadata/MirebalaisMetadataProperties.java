package org.openmrs.module.mirebalaismetadata;

import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component("mirebalaisMetadataProperties")
public class MirebalaisMetadataProperties extends ModuleProperties {

    public static final String GP_INSTALLED_ADDRESS_HIERARCHY_VERSION = "mirebalais.installedAddressHierarchyVersion";
    public static final String GP_INSTALLED_DRUG_LIST_VERSION = "mirebalaismetadata.installedDrugListVersion";

    public int getInstalledDrugListVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_DRUG_LIST_VERSION);
    }

    public int getInstalledAddressHierarchyVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_ADDRESS_HIERARCHY_VERSION);
    }
}
