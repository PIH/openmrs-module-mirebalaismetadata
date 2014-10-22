package org.openmrs.module.mirebalaismetadata;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.customdatatype.datatype.FreeTextDatatype;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.location;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationAttribute;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationAttributeType;

@Ignore
public class CustomLocationDeployHandlerTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private MetadataDeployService deployService;

    @Test
    public void integration_redeployShouldNotOverrideExistingAttributes() {

        // Set up the existing objects that we will need
        deployService.installObject(locationAttributeType("New name", "New desc", FreeTextDatatype.class, null, 0, 1, "attribute-type-uuid"));
        deployService.installObject(location("New name", "New desc", "location-uuid"));
        deployService.installObject(locationAttribute("location-uuid", "attribute-type-uuid", "test me", "attribute-uuid"));

        Context.flushSession();

        // sanity check
        Location location = Context.getLocationService().getLocationByUuid("location-uuid");
        Assert.assertThat(location.getAttributes().size(), is(1));

        // now re-create the location and check to make sure the location isn't blown away
        deployService.installObject(location("New name", "New desc", "location-uuid"));
        location = Context.getLocationService().getLocationByUuid("location-uuid");
        Assert.assertThat(location.getAttributes().size(), is(1));

    }


}
