package org.openmrs.module.mirebalaismetadata;

import org.junit.Ignore;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Properties;

@Ignore
public class BaseMirebalaisMetadataContextSensitiveTest extends BaseModuleContextSensitiveTest {

    @Override
    public Properties getRuntimeProperties() {
        Properties p = super.getRuntimeProperties();
        p.setProperty("pih.config", "pihcore");
        return p;
    }

}
