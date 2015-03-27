/**
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

package org.openmrs.module.mirebalaismetadata.descriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the information needed to create a new Location
 */
public abstract class LocationDescriptor extends MetadataDescriptor {

    // TODO: Add parent in here directly once the CDI rollout is complete.
    // Not really possible before since we are changing the parent locations around
    // and these classes do not have access to feature toggles (do they?)

    public List<LocationAttributeDescriptor> attributes() {
        return new ArrayList<LocationAttributeDescriptor>();
    }

    public List<LocationTagDescriptor> tags() {
        return new ArrayList<LocationTagDescriptor>();
    }
}