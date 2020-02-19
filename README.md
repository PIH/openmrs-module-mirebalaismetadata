## Managing Metadata for a PIH EMR instance of OpenMRS

Metadata is packaged for deployment across PIH EMR instances using two OpenMRS modules: [Metadata Sharing](https://wiki.openmrs.org/display/docs/Metadata+Sharing+Module) and [Metadata Deploy](https://wiki.openmrs.org/display/docs/Metadata+Deploy+Module). Please read the documentation in the links.

### Metadata Server Management

This is described on the [OpenMRS wiki](https://wiki.openmrs.org/display/docs/Metadata+Server+Management).

### MDS Package Search Tool

You will at some point want to look up whether a concept exists in some or
another package.

To accomplish this you can use the tool in mds-search. `cd` into `mds-search`.
Then run `./update.sh` to unzip all the MDS packages in this repo into the
mds-search directory. Then use `./find-concept.sh 123` to find the MDS packages
that contain the concept with PIH concept ID `123`. The other `find-concept-`
scripts work similarly. Execute any one of them with no arguments to see usage
info.

The script `find-package-by-name.sh <name>` will show you which of the header.xml and
metadata.xml files (which only have number suffixes) has `<name>` in the name.
Try `find-package-by-name.sh ncd`.

### PIH Concept Managers

There are two people at PIH who are "Concept Managers" in the sense intended below:
- Ellen Ball
- Brandon Istenes

### PIH EMR Concept Management Process

This is a process for defining a new form.

1. Write down your required question/answer/diagnosis in a row in your requirements spreadsheet (LINK TO EXAMPLE).
1. Go to humci.pih-emr.org/openmrs/admin/concepts and, for each question/answer/diagnosis/etc, search for a concept that might be appropriate.
    1. If there is a suitable concept in HUM-CI,
        1. If there is a CIEL mapping, add that to your requirements sheet.
        1. If there is no CIEL mapping, add the PIH "name" mapping to your requirements sheet.
    1. If there is no suitable concept in HUM-CI, search for a suitable CIEL concept in [mdsbuilder](https://mdsbuilder.openmrs.org/openmrs/)
        1. If the concept exists in CIEL, add it to the [CIEL request sheet]. Tag Ellen in a comment to request that she import the concept.
        1. If there is no suitable concept in CIEL, add it to the [CIEL request sheet](https://docs.google.com/spreadsheets/d/1hAJLuKBVwzJEvo3hDp2tRqeRWKMSlxgphK1rc-Nm3IA/edit#gid=0). Either Andy Kanter will either add it to CIEL and we'll import it to PIH EMR, or he won't and we'll create it ourselves. In the meantime, after running it by a Concept Manager, feel free to add it to the PIH EMR.
1. Once you have the concept in the PIH concept server, make sure it has PIH or CIEL reference term mappings. The ones imported from CIEL should certainly have the CIEL code as a mapping -- if it isn't present, definitely ask Ellen about it. Concepts which don't correspond to anything in CIEL should have PIH reference term mappings, for the ID (e.g. "PIH:12345") and for the name (e.g. "PIH:HAS BOO BOO"). The ID one probably isn't important, actually, but it'll probably exist.
1. Check that a translation of the concept name exists in your implementation’s language. If it doesn't, evaluate whether or not the display name you want for the concept is a direct translation of the English concept name.
    1. If it is, add the display name as the translation for the concept.
    1. If it isn't, translate the English concept name as best you (or a bilingual colleague) can. You'll add the display name to the `messages.properties` file later.
1. Use the MDS Package Search Tool (mds-search), documented above, to find out whether the concept you want is already in an MDS package.
1. If the concept is new or is for some other reason is not yet in an MDS package, you or someone from MedInfo will have to add it to one.
    1. Identify the MDS package it should go in. Ask someone if you're not sure.
    1. Add your concept to a concept set that goes into that package. Ask someone if you're not sure.
    1. Go to Administration > Export Metadata.
    1. Click on "New Version"
    1. I don't think we use publication, but feel free to check "2. Publish package" anyway. Click "Next."
    1. If you have a new concept set that's not yet in the package, then
        1. Under "4. Review selected items," click "Choose Individually."
        1. Find and select the concept set you'd like to include.
        1. Click "Save."
    1. Click "Export."
    1. Download this newly created MDS package.
    1. Open the "openmrs-module-mirebalaismetadata" repository on your computer. If you don't have it, check it out from [GitHub](https://github.com/PIH/openmrs-module-mirebalaismetadata). If you are using the OpenMRS SDK, be sure to watch it with `openmrs-sdk:watch`.
    1. Drop the new version of the MDS package into `api/src/main/resources`.
    1. Update the filename in `api/src/main/resources/packages.xml` to reflect the new version number.
1. To make sure that your site is importing that MDS package, look at `mirebalaismetadata/.../MirebalaisMetadataActivator.java`. It should have a list of MDS packages under your country's name. Make sure your MDS package is named there, adding it if necessary.
1. Now that you know that the concept is being imported via an MDS package, you can use it in a form (or whatever). Refer to your concept by Reference Term Mapping. If a CIEL Metadata Term Mapping ("CIEL:3456") is available, always prefer that. If it's not obvious from context what the concept is, add a comment with the concept's name. If the concept is not a CIEL concept, use the PIH name mapping ("PIH:TUMMY ACHE"), creating it if necessary.
1. Add the display name to the correct `messages.properties` file, with the correct key (I haven't done this yet and can't be more helpful than that).

### Importing Concepts from CIEL to PIH Server

Use Metadata Sharing (mds) to add the concept to the PIH EMR package.

1. Create mds package with select CIEL concepts from [mdsbuilder](https://mdsbuilder.openmrs.org/openmrs/).
1. Download/Export the CIEL mds package.
1. Import the CIEL mds package into the PIH concepts server.
    1. Use "From peer"
    1. Uncheck "dates differ"
    1. Review the matches identified by the importer
1. Add the concepts to one of the PIH EMR mds packages.  Zip file updates are pushed to the repo [https://github.com/PIH/openmrs-module-mirebalaismetadata](https://github.com/PIH/openmrs-module-mirebalaismetadata)

### Metadata Sharing (mds)

Currently used for deploying concepts.

Each mirebalaismetadata mds package is [described](https://github.com/PIH/openmrs-module-mirebalaismetadata/blob/master/api/src/main/resources/README.md). 

New concepts are created using the Concept Dictionary Maintenance UI: [https://concepts.pih-emr.org/openmrs/dictionary/index.htm](https://concepts.pih-emr.org/openmrs/dictionary/index.htm)** (NOTE: Do not add/edit concepts without first consulting Ellen Ball)**

Concepts are bundled into zip files using the Metadata Sharing module in the admin UI.  For transparency of the contents of each mds package, we are improving the packages to include only one ConvSet concepts for each mds.  For example, to add APGAR score to the MCH mds package, APGAR score is added to the "Maternal Child Health concept set".  See [README](https://github.com/PIH/openmrs-module-mirebalaismetadata/blob/master/api/src/main/resources/README.md). 

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
