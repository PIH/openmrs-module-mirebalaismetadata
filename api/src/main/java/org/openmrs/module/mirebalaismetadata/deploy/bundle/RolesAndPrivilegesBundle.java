package org.openmrs.module.mirebalaismetadata.deploy.bundle;

import org.openmrs.Privilege;
import org.openmrs.module.metadatadeploy.bundle.CoreConstructors;
import org.openmrs.module.mirebalaismetadata.constants.Privileges;
import org.openmrs.module.mirebalaismetadata.constants.Roles;
import org.openmrs.module.mirebalaismetadata.descriptor.PrivilegeDescriptor;
import org.openmrs.module.mirebalaismetadata.descriptor.RoleDescriptor;
import org.springframework.stereotype.Component;

@Component
public class RolesAndPrivilegesBundle extends MirebalaisMetadataBundle {

    @Override
    public void install() throws Exception {

        log.info("Installing Privileges");

        install(Privileges.APP_APPOINTMENTSCHEDULINGUI_APPOINTMENT_TYPES);
        install(Privileges.APP_APPOINTMENTSCHEDULINGUI_HOME);
        install(Privileges.APP_APPOINTMENTSCHEDULINGUI_PROVIDER_SCHEDULES);
        install(Privileges.APP_APPOINTMENTSCHEDULINGUI_VIEW_APPOINTMENTS);
        install(Privileges.APP_COREAPPS_ACTIVE_VISITS);
        install(Privileges.APP_COREAPPS_AWAITING_ADMISSION);
        install(Privileges.APP_COREAPPS_DATA_MANAGEMENT);
        install(Privileges.APP_COREAPPS_FIND_PATIENT);
        install(Privileges.APP_COREAPPS_PATIENT_DASHBOARD);
        install(Privileges.APP_COREAPPS_PATIENT_VISITS);
        install(Privileges.APP_DISPENSING_APP_DISPENSE);
        install(Privileges.APP_EMR_ARCHIVES_ROOM);
        install(Privileges.APP_EMR_CHECK_IN);
        install(Privileges.APP_EMR_INPATIENTS);
        install(Privileges.APP_EMR_OUTPATIENT_VITALS);
        install(Privileges.APP_EMR_SYSTEM_ADMINISTRATION);
        install(Privileges.APP_LEGACY_ADMIN);
        install(Privileges.APP_LEGACY_PATIENT_REGISTRATION);
        install(Privileges.APP_LEGACY_PATIENT_REGISTRATION_EDIT);
        install(Privileges.APP_REGISTRATION_REGISTER_PATIENT);
        install(Privileges.APP_REPORTINGUI_ADHOC_ANALYSIS);
        install(Privileges.APP_REPORTINGUI_REPORTS);
        install(Privileges.APP_ZL_MPI);
        install(Privileges.APP_ZL_REPORTS_DATA_EXPORTS);
        install(Privileges.TASK_ALLERGIES_MODIFY);
        install(Privileges.TASK_APPOINTMENTSCHEDULINGUI_BOOK_APPOINTMENTS);
        install(Privileges.TASK_APPOINTMENTSCHEDULINGUI_OVERBOOK_APPOINTMENTS);
        install(Privileges.TASK_APPOINTMENTSCHEDULINGUI_REQUEST_APPOINTMENTS);
        install(Privileges.TASK_APPOINTMENTSCHEDULINGUI_VIEW_CONFIDENTIAL);
        install(Privileges.TASK_ALLERGIES_MODIFY);
        install(Privileges.TASK_ARCHIVES_REQUEST_PAPER_RECORDS);
        install(Privileges.TASK_COREAPPS_CREATE_RETRO_VISIT);
        install(Privileges.TASK_COREAPPS_CREATE_VISIT);
        install(Privileges.TASK_COREAPPS_END_VISIT);
        install(Privileges.TASK_COREAPPS_DELETE_VISIT);
        install(Privileges.TASK_COREAPPS_MERGE_VISITS);
        install(Privileges.TASK_DISPENSING_DISPENSE);
        install(Privileges.TASK_DISPENSING_EDIT);
        install(Privileges.TASK_EMR_CHECK_IN);
        install(Privileges.TASK_EMR_DEATH_CERTIFICATE_FORM);
        install(Privileges.TASK_EMR_ENTER_ADMISSION_NOTE);
        install(Privileges.TASK_EMR_ENTER_CONSULT_NOTE);
        install(Privileges.TASK_EMR_ENTER_ED_NOTE);
        install(Privileges.TASK_EMR_ENTER_SURGICAL_NOTE);
        install(Privileges.TASK_EMR_ENTER_VITALS_NOTE);
        install(Privileges.TASK_EMR_PATIENT_ENCOUNTER_DELETE);
        install(Privileges.TASK_EMR_PATIENT_ENCOUNTER_EDIT);
        install(Privileges.TASK_EMR_PRINT_LABELS);
        install(Privileges.TASK_EMR_PRINT_WRISTBAND);
        install(Privileges.TASK_EMR_RETRO_CLINICAL_NOTE);
        install(Privileges.TASK_EMR_RETRO_CLINICAL_NOTE_THIS_PROVIDER_ONLY);
        install(Privileges.TASK_RADIOLOGYAPP_ORDER_CT);
        install(Privileges.TASK_RADIOLOGYAPP_ORDER_US);
        install(Privileges.TASK_RADIOLOGYAPP_ORDER_XRAY);
        install(Privileges.TASK_RADIOLOGYAPP_RETRO_ORDER);
        install(Privileges.TASK_RADIOLOGYAPP_TAB);

        log.info("Retiring old privileges");

        uninstall(possible(Privilege.class, Privileges.RETIRED_APP_APPOINTMENTSCHEDULING_OLDUI.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_APP_APPOINTMENTSCHEDULINGUI_SCHEDULE_APPOINTMENT.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_APP_EMR_ACTIVE_VISITS.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_APP_ZL_REPORTS.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_ENTER_CLINICAL_FORMS.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_ORDER_CT.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_ORDER_US.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_ORDER_XRAY.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_ORDER_XRAY_ANOTHER.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_TASK_EMR_PRINT_ID_CARD_LABEL.uuid()), "no longer used");
        uninstall(possible(Privilege.class, Privileges.RETIRED_TASK_EMR_PRINT_PAPER_RECORD_LABELS.uuid()), "no longer used");

        log.info("Installing roles");

        install(Roles.SYSTEM_ADMINISTRATOR);
        install(Roles.SCHEDULE_MANAGER);
        install(Roles.PHARMACIST);
        install(Roles.PHARMACY_AIDE);
        install(Roles.GENERAL_ADMINISTRATION);
        install(Roles.CLINICAL_MANAGER);
        install(Roles.MEQ);
        install(Roles.ARCHIVIST_CLERK);
        install(Roles.ARCHIVIST_MANAGER);
        install(Roles.AUXILIARY_NURSE);
        install(Roles.NURSE);
        install(Roles.ADVANCED_PRACTICE_NURSE);
        install(Roles.RADIOLOGY_TECHNICIAN);
        install(Roles.PHARMACIST);
        install(Roles.PHARMACY_AIDE);
        install(Roles.PHARMACY_MANAGER);
        install(Roles.MEDICAL_STUDENT);
        install(Roles.RESIDENT);
        install(Roles.PHYSICIAN);
        install(Roles.SURGEON);
        install(Roles.ANAESTHETIST);
        install(Roles.PSYCHOLOGIST);
        install(Roles.PHYSICAL_THERAPIST);
        install(Roles.SOCIAL_WORKER);

        install(Roles.LEGACY_CLINICAL);
        install(Roles.LEGACY_DATA_ARCHIVES);
        install(Roles.LEGACY_RADIOLOGY);
        install(Roles.LEGACY_REPORTS);
        install(Roles.LEGACY_ARCHIVIST_AIDE);
        install(Roles.LEGACY_SCHEDULE_ADMINISTRATOR);
        install(Roles.LEGACY_SCHEDULER);
        install(Roles.LEGACY_SCHEDULE_VIEWER);
        install(Roles.LEGACY_PATIENT_MEDICAL_INFORMATION);
        install(Roles.LEGACY_CHECK_IN);


    }


    protected void install(PrivilegeDescriptor d) {
        install(CoreConstructors.privilege(d.privilege(), d.description(), d.uuid()));

    }

    protected void install(RoleDescriptor d) {
        install(CoreConstructors.role(d.role(), d.description(), d.inherited(), d.privileges(), d.uuid()));

    }

}
