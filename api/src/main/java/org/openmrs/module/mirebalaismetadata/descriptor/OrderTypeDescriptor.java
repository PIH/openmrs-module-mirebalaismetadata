package org.openmrs.module.mirebalaismetadata.descriptor;

public abstract class OrderTypeDescriptor extends MetadataDescriptor {

    public abstract String javaClassName();

    public abstract OrderTypeDescriptor parent();

}
