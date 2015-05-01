package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.apache.commons.lang.StringUtils;
import org.openmrs.OrderType;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.mirebalaismetadata.metadata.OrderTypes;
import org.openmrs.module.pihcore.deploy.bundle.PihMetadataBundle;
import org.openmrs.module.pihcore.descriptor.OrderTypeDescriptor;
import org.springframework.stereotype.Component;

@Component
public class OrderTypeBundle extends PihMetadataBundle {

    @Override
    public void install() throws Exception {
        log.info("Installing OrderTypes");

        install(OrderTypes.DRUG_ORDER);
        install(OrderTypes.TEST_ORDER);
        install(OrderTypes.RADIOLOGY_TEST_ORDER);
    }

    protected void install(OrderTypeDescriptor d) {
        install(orderType(d.name(), d.description(), d.uuid(), d.javaClassName(), d.parent() != null ? d.parent().uuid() : null));
    }


    private static OrderType orderType(String name, String description, String uuid, String javaClassName, String parentUuid) {
        OrderType obj = new OrderType();
        obj.setName(name);
        obj.setDescription(description);
        obj.setUuid(uuid);
        obj.setJavaClassName(javaClassName);

        if (StringUtils.isNotBlank(parentUuid)) {
            obj.setParent(MetadataUtils.existing(OrderType.class, parentUuid));
        }

        return obj;
    }
}
