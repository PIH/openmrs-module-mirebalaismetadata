package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatadeploy.bundle.MetadataBundle;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SkipBaseSetup
public class ConsultNoteConceptsTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private CoreMetadata coreMetadata;

    @Autowired
    private ConsultNoteConcepts consultNoteConcepts;

    @Autowired
    private MetadataDeployService deployService;

    @Before
    public void setUp() throws Exception {
        initializeInMemoryDatabase();
        executeDataSet("requiredDataTestDataset.xml");
        executeDataSet("moreConceptMetadata.xml");
        authenticate();
    }

    @Test
    public void testInstall() throws Exception {
        deployService.installBundles(Arrays.<MetadataBundle>asList(coreMetadata, consultNoteConcepts));

        Concept construct = MetadataUtils.existing(Concept.class, ConsultNoteConcepts.Concepts.PAST_MEDICAL_HISTORY_CONSTRUCT);
        assertThat(construct.getUuid(), is(ConsultNoteConcepts.Concepts.PAST_MEDICAL_HISTORY_CONSTRUCT));
        assertThat(construct.getConceptSets().size(), is(3));

        construct = MetadataUtils.existing(Concept.class, ConsultNoteConcepts.Concepts.FAMILY_HISTORY_CONSTRUCT);
        assertThat(construct.getUuid(), is(ConsultNoteConcepts.Concepts.FAMILY_HISTORY_CONSTRUCT));
        assertThat(construct.getConceptSets().size(), is(4));
    }
}