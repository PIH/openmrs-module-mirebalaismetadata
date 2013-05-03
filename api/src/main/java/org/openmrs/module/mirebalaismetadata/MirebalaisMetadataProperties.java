package org.openmrs.module.mirebalaismetadata;

import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component("mirebalaisMetadataProperties")
public class MirebalaisMetadataProperties extends ModuleProperties {

    public static final String GP_INSTALLED_ADDRESS_HIERARCHY_VERSION = "mirebalais.installedAddressHierarchyVersion";

    public int getInstalledAddressHierarchyVersion() {
        String globalProperty = getGlobalProperty(GP_INSTALLED_ADDRESS_HIERARCHY_VERSION, false);
        try {
            return Integer.valueOf(globalProperty);
        } catch (Exception ex) {
            return 0;
        }
    }

}
