/*
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

import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.OpenmrsMetadata;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.emrapi.metadata.MetadataPackageConfig;
import org.openmrs.module.emrapi.metadata.MetadataPackagesConfig;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.metadatasharing.ImportConfig;
import org.openmrs.module.metadatasharing.ImportedItem;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.wrapper.PackageImporter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FindMetadataInPackageTest extends BaseModuleContextSensitiveTest {

    public List<String> uuidsToFind = Arrays.asList("3cd6f600-26fe-102b-80cb-0017a47871b2");

    public boolean printAll = true;
//    public boolean printAll = false;

//    public String onlyPackage = null;
    public String onlyPackage = "HUM_Death";

    @Test
    public void findMetadata() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        MetadataPackagesConfig allConfigs = MetadataUtil.getMetadataPackagesForModule(classLoader);
        for (MetadataPackageConfig config : allConfigs.getPackages()) {
            String filenameBase = config.getFilenameBase();
            if (onlyPackage != null && !filenameBase.startsWith(onlyPackage)) {
                continue;
            }
            String filename = filenameBase + "-" + config.getVersion().toString() + ".zip";
            if (printAll) {
                System.out.println("Inspecting " + filename);
            }

            PackageImporter metadataImporter = MetadataSharing.getInstance().newPackageImporter();
            metadataImporter.setImportConfig(ImportConfig.valueOf(config.getImportMode()));
            metadataImporter.loadSerializedPackageStream(classLoader.getResourceAsStream(filename));
            for (int i = 0; i < metadataImporter.getPartsCount(); ++i) {
                Collection<ImportedItem> items = metadataImporter.getImportedItems(i);
                for (ImportedItem item : items) {
                    if (printAll) {
                        System.out.println("\t" + format(item));
                    }
                    String actualUuid = ((OpenmrsObject) item.getIncoming()).getUuid();
                    if (uuidsToFind.contains(actualUuid)) {
                        System.out.println("***** Found " + actualUuid + " in " + filename + " *****");
                    }
                }
            }
        }
    }

    private String format(ImportedItem item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getIncomingClassSimpleName() + " ");
        sb.append(((OpenmrsObject) item.getIncoming()).getUuid() + " ");
        try {
            sb.append(((OpenmrsMetadata) item.getIncoming()).getName() + " ");
        } catch (Exception ex) { }
        try {
            sb.append(((Concept) item.getIncoming()).getName().getName() + " ");
        } catch (Exception ex) { }
        return sb.toString();
    }

}

