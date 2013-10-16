package org.openmrs.module.mirebalaismetadata.drug;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
@SkipBaseSetup
@Ignore
public class DrugImporterComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
   // DrugImporter drugImporter;

    @Before
    public void beforeEachTest() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet("requiredDataTestDataset.xml");
        executeDataSet("globalPropertiesTestDataset.xml");
        authenticate();
    }


    @Test
    public void validateSpreadsheet() throws Exception {
        System.out.println("Importing MDS package with drug concepts...");
        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "HUM_Medication");
        System.out.println("Done importing drug concepts");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drug-list.csv");
        InputStreamReader reader = new InputStreamReader(inputStream);

       // ImportNotes notes = drugImporter.verifySpreadsheet(reader);

       // System.out.println(notes);
    }

}
