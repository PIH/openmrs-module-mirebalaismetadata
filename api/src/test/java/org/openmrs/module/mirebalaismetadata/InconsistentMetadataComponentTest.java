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

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.emrapi.utils.MetadataUtil;

/**
 * If multiple MDS packages contain different versions of the same item, then loading them is order-dependent, which
 * is bad.
 * This test looks through all MDS packages, and throws an error in this situation.
 */

// we are ignoring this as we are now loading metadata packages in order of date created
@Ignore
public class InconsistentMetadataComponentTest extends BaseMirebalaisMetadataContextSensitiveTest {

    @Test
    public void testThatThereAreNoMdsPackagesWithInconsistentVersionsOfTheSameItem() throws Exception {
        MetadataUtil.verifyNoMdsPackagesWithInconsistentVersionsOfTheSameItem(getClass().getClassLoader());
    }

}
