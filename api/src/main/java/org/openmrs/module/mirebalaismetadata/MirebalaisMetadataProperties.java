package org.openmrs.module.mirebalaismetadata;

import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component("mirebalaisMetadataProperties")
public class MirebalaisMetadataProperties extends ModuleProperties {

    public static final String GP_INSTALLED_DRUG_LIST_VERSION = "mirebalaismetadata.installedDrugListVersion";

    public static final String GP_INSTALLED_CES_DRUG_LIST_VERSION = "mirebalaismetadata.installedCesDrugListVersion";

    public static final String GP_INSTALLED_SL_DRUG_LIST_VERSION = "mirebalaismetadata.installedSlDrugListVersion";

    public int getInstalledDrugListVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_DRUG_LIST_VERSION);
    }

    public int getInstalledCesDrugListVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_CES_DRUG_LIST_VERSION);
    }

    public int getInstalledSlDrugListVersion() {
        return getIntegerByGlobalProperty(GP_INSTALLED_SL_DRUG_LIST_VERSION);
    }
}
