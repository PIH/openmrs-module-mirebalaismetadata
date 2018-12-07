## Managing Metadata for a PIH EMR instance of OpenMRS

Metadata is packaged for deployment across PIH EMR instances using two OpenMRS modules: [Metadata Sharing](https://wiki.openmrs.org/display/docs/Metadata+Sharing+Module) and [Metadata Deploy](https://wiki.openmrs.org/display/docs/Metadata+Deploy+Module). Please read the documentation in the links.

### PIH EMR Concept Management

This is described on the OpenMRS wiki:

[https://wiki.openmrs.org/display/docs/Metadata+Server+Management](https://wiki.openmrs.org/display/docs/Metadata+Server+Management)

### Process 

1. Does the concept exist in PIH EMR package?  Confirm by looking at one of the deployments (ci.pih-emr.org)

2. If it does not exist, 

    1. Search CIEL concept dictionary for the concept -  Use [OpenConceptLab (OCL) ](https://www.openconceptlab.org/)to view CIEL dictionary 

    2. If CIEL has the concept, record the CIEL concept_id/mapping (ie. CIEL:123456) (data dictionary spreadsheet?)

    3. If the concept does not exist in CIEL/OCL, propose new concept to CIEL.

    4. If concept does not include Spanish translated name, propose Spanish name.

3. Use Metadata Sharing (mds) to add the concept to the PIH EMR package.  

    5. Create mds package with select CIEL concepts.  

    6. Download/Export the CIEL mds package.

    7. Import the CIEL mds package into the PIH concepts server.

    8. Add the concepts to one of the PIH EMR mds packages.  Zip file updates are pushed to the repo [https://github.com/PIH/openmrs-module-mirebalaismetadata](https://github.com/PIH/openmrs-module-mirebalaismetadata)

### Metadata Sharing (mds)

Currently used for concepts. 

New concepts are created using the Concept Dictionary Maintenance UI: [https://concepts.pih-emr.org/openmrs/dictionary/index.htm](https://concepts.pih-emr.org/openmrs/dictionary/index.htm)** (NOTE: Do not add/edit concepts without first consulting Ellen Ball)**

Concepts are bundled into zip files using the Metadata Sharing module in the admin UI.

Zip file updates are pushed to the repo [https://github.com/PIH/openmrs-module-mirebalaismetadata](https://github.com/PIH/openmrs-module-mirebalaismetadata)

The metadata module is built daily using Bamboo and deployed to all configured servers.

#### Examples

This commit adds the concept "Name of community health worker": [https://github.com/PIH/openmrs-module-mirebalaismetadata/commit/9d5cc404e8c96dcb40ca1bd9a8f0c57e693a6fd7](https://github.com/PIH/openmrs-module-mirebalaismetadata/commit/9d5cc404e8c96dcb40ca1bd9a8f0c57e693a6fd7)

Open HUM_Dispensing_Concepts-35.zip to view the concept XML file. As a reference, the concept can also be viewed in the administrative UI: [https://concepts.pih-emr.org/openmrs/dictionary/concept.htm?conceptId=11631](https://concepts.pih-emr.org/openmrs/dictionary/concept.htm?conceptId=11631)

### Metadata Deploy

Used for code level configuration of ALL other metadata. Metadata is set up programmatically in pih-core via methods provided by the metadata deploy module. [https://github.com/PIH/openmrs-module-pihcore](https://github.com/PIH/openmrs-module-pihcore)

#### Examples

Location and Patient Identifier Type for Mexico: [https://github.com/PIH/openmrs-module-pihcore/tree/master/api/src/main/java/org/openmrs/module/pihcore/metadata/mexico](https://github.com/PIH/openmrs-module-pihcore/tree/master/api/src/main/java/org/openmrs/module/pihcore/metadata/mexico)

Other common metadata:

[https://github.com/PIH/openmrs-module-pihcore/tree/master/api/src/main/java/org/openmrs/module/pihcore/metadata/core](https://github.com/PIH/openmrs-module-pihcore/tree/master/api/src/main/java/org/openmrs/module/pihcore/metadata/core)

### Configuring Concepts for Chiapas

1. On the concepts server (concepts.pih-emr.org), create a "Mexico MoH (Ministry of Health, or equivalent..) concept set", similar to the “[Liberia MoH diagnosis set](https://concepts.pih-emr.org/openmrs/dictionary/concept.htm?conceptId=10595)”. Create child sets, e.g. “Mexico MoH diagnosis”, “Mexico MoH Labs”, etc. Add concepts to these subsets.

2. For each concept in the source data dictionary:

    1. If there is an existing concept in the concepts server that is an exact match, add a mapping to the "Mexico MoH" vocabulary item, and a Spanish translation if required.

    2. If there is no existing concept, create it and add vocabulary mapping and translation.

    3. Add this concept to appropriate concept set.

3. In the Metadata Sharing module, create a package called "Mexico Concepts" and create a new version. See example here for “[Liberia Concepts](https://concepts.pih-emr.org/openmrs/module/metadatasharing/export/details.form?group=c0dc491e-a26e-4dee-99c4-c4dc5cb2e787)”.

4. Download the zipped package of this version.

5. Add the zip file to the PIH openmrs-module-mirebalais-metadata Github repo [here](https://github.com/PIH/openmrs-module-mirebalaismetadata/tree/master/api/src/main/resources). This will add the metadata concepts to our build pipeline.

6. The concepts should then be available companero staging server.

#### Diagnoses

Add diagnoses concept set setting here: [https://github.com/PIH/openmrs-module-pihcore/blob/master/api/src/main/java/org/openmrs/module/pihcore/deploy/bundle/mexico/MexicoMetadataBundle.java](https://github.com/PIH/openmrs-module-pihcore/blob/master/api/src/main/java/org/openmrs/module/pihcore/deploy/bundle/mexico/MexicoMetadataBundle.java)

Example: [https://github.com/PIH/openmrs-module-pihcore/blob/master/api/src/main/java/org/openmrs/module/pihcore/deploy/bundle/haiti/HaitiMetadataBundle.java#L100](https://github.com/PIH/openmrs-module-pihcore/blob/master/api/src/main/java/org/openmrs/module/pihcore/deploy/bundle/haiti/HaitiMetadataBundle.java#L100)
