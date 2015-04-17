package org.openmrs.module.mirebalaismetadata.constants;

import org.openmrs.module.mirebalaismetadata.descriptor.RoleDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Roles {

    public static RoleDescriptor ARCHIVIST_AIDE = new RoleDescriptor() {
        public String uuid() { return "2170e9bf-6d30-4ad9-9319-a454bf32dbf9"; }
        public String role() { return "Application Role: archivistAide"; }
        public String description() { return "Gives access to only the archives app"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_EMR_ARCHIVES_ROOM.privilege()
            ));}
    };

    public static RoleDescriptor CHECK_IN = new RoleDescriptor() {
        public String uuid() { return "d711c948-2d9b-4700-9c80-c32ec6a8ddeb"; }
        public String role() { return "Application Role: checkIn"; }
        public String description() { return "Gives user access to the Start a clinic visit app"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_EMR_CHECK_IN.privilege()
            ));}
    };

    public static RoleDescriptor CLINICAL = new RoleDescriptor() {
        public String uuid() { return "05a3c5fc-3c7f-4c93-87d2-c6013e71a102"; }
        public String role() { return "Application Role: clinical"; }
        public String description() { return "Application Role: clinical"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_COREAPPS_AWAITING_ADMISSION.privilege(),
                    Privileges.APP_EMR_INPATIENTS.privilege(),
                    Privileges.APP_EMR_OUTPATIENT_VITALS.privilege(),
                    Privileges.APP_COREAPPS_ACTIVE_VISITS.privilege(),
                    Privileges.APP_LEGACY_PATIENT_REGISTRATION_EDIT.privilege(),
                    Privileges.APP_REPORTINGUI_REPORTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_REQUEST_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_VIEW_CONFIDENTIAL.privilege(),
                    Privileges.TASK_COREAPPS_CREATE_VISIT.privilege(),
                    Privileges.TASK_EMR_ENTER_ADMISSION_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_CONSULT_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_ED_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_SURGICAL_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_VITALS_NOTE.privilege(),
                    Privileges.TASK_EMR_PRINT_LABELS.privilege(),
                    Privileges.TASK_EMR_RETRO_CLINICAL_NOTE_THIS_PROVIDER_ONLY.privilege(),
                    Privileges.TASK_EMR_DEATH_CERTIFICATE_FORM.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_CT.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_US.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_XRAY.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_TAB.privilege()
            ));}
    };

    public static RoleDescriptor DATA_ARCHIVES = new RoleDescriptor() {
        public String uuid() { return "0b30c2e0-6276-4bf5-a8ac-e38da035888a"; }
        public String role() { return "Application Role: dataArchives"; }
        public String description() { return "Application Role: dataArchives"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_COREAPPS_AWAITING_ADMISSION.privilege(),
                    Privileges.APP_EMR_ARCHIVES_ROOM.privilege(),
                    Privileges.APP_EMR_INPATIENTS.privilege(),
                    Privileges.APP_EMR_CHECK_IN.privilege(),
                    Privileges.APP_ZL_MPI.privilege(),
                    Privileges.APP_COREAPPS_ACTIVE_VISITS.privilege(),
                    Privileges.APP_LEGACY_PATIENT_REGISTRATION.privilege(),
                    Privileges.APP_LEGACY_PATIENT_REGISTRATION_EDIT.privilege(),
                    Privileges.APP_REPORTINGUI_REPORTS.privilege(),
                    Privileges.TASK_ARCHIVES_REQUEST_PAPER_RECORDS.privilege(),
                    Privileges.TASK_COREAPPS_CREATE_VISIT.privilege(),
                    Privileges.TASK_COREAPPS_CREATE_RETRO_VISIT.privilege(),
                    Privileges.TASK_COREAPPS_MERGE_VISITS.privilege(),
                    Privileges.TASK_EMR_CHECKIN.privilege(),
                    Privileges.TASK_EMR_ENTER_ADMISSION_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_CONSULT_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_ED_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_SURGICAL_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_VITALS_NOTE.privilege(),
                    Privileges.TASK_EMR_PRINT_LABELS.privilege(),
                    Privileges.TASK_EMR_RETRO_CLINICAL_NOTE.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_CT.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_US.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_XRAY.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_RETRO_ORDER.privilege()
            ));}
    };

    public static RoleDescriptor PATIENT_MEDICAL_INFORMATION = new RoleDescriptor() {
        public String uuid() { return "5d08f1df-f161-46f0-9246-90b2f24aa862"; }
        public String role() { return "Application Role: patientMedicalInformation"; }
        public String description() { return "Gives access to patient-level medical information, including access to patient dashboard"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_COREAPPS_FIND_PATIENT.privilege(),
                    Privileges.APP_COREAPPS_PATIENT_DASHBOARD.privilege(),
                    Privileges.APP_COREAPPS_PATIENT_VISITS.privilege()
            ));}
    };

    public static RoleDescriptor PHARMACIST = new RoleDescriptor() {
        public String uuid() { return "5da08e21-efd6-497c-9ef5-d50d7e33a63a"; }
        public String role() { return "Application Role: pharmacist"; }
        public String description() { return "Application Role: pharmacist"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_DISPENSING_APP_DISPENSE.privilege(),
                    Privileges.TASK_DISPENSING_DISPENSE.privilege(),
                    Privileges.TASK_EMR_CHECKIN.privilege()
            ));}
    };

    public static RoleDescriptor PHARMACY_AIDE = new RoleDescriptor() {
        public String uuid() { return "36fd044e-f76a-4a07-84b0-1af9d7ce0c59"; }
        public String role() { return "Application Role: pharmacyAide"; }
        public String description() { return "Application Role: pharmacyAide"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_DISPENSING_APP_DISPENSE.privilege(),
                    Privileges.TASK_DISPENSING_DISPENSE.privilege(),
                    Privileges.TASK_EMR_CHECKIN.privilege()
            ));}
    };

    public static RoleDescriptor RADIOLOGY = new RoleDescriptor() {
        public String uuid() { return "3672ebe7-ed59-461f-a662-5ae3cd2e46ac"; }
        public String role() { return "Application Role: radiology"; }
        public String description() { return "Application Role: radiology"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_EMR_INPATIENTS.privilege(),
                    Privileges.APP_COREAPPS_ACTIVE_VISITS.privilege(),
                    Privileges.APP_LEGACY_PATIENT_REGISTRATION_EDIT.privilege(),
                    Privileges.APP_REPORTINGUI_REPORTS.privilege(),
                    Privileges.TASK_COREAPPS_CREATE_VISIT.privilege(),
                    Privileges.TASK_EMR_PRINT_LABELS.privilege(),
                    Privileges.TASK_ARCHIVES_REQUEST_PAPER_RECORDS.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_CT.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_US.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_XRAY.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_TAB.privilege()
            ));}
    };

    public static RoleDescriptor REPORTS = new RoleDescriptor() {
        public String uuid() { return "343872bd-a927-4f9d-bf2c-75f895c44f6c"; }
        public String role() { return "Application Role: reports"; }
        public String description() { return "Rights  to view and run reports and data exports"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_REPORTINGUI_ADHOC_ANALYSIS.privilege(),
                    Privileges.APP_REPORTINGUI_REPORTS.privilege(),
                    Privileges.APP_ZL_REPORTS_DATA_EXPORTS.privilege()
            ));}
    };

    public static RoleDescriptor SCHEDULE_ADMINISTRATOR = new RoleDescriptor() {
        public String uuid() { return "f6fcff07-8da1-4c7f-8c62-07352101d0a8"; }
        public String role() { return "Application Role: scheduleAdministrator"; }
        public String description() { return "Gives user the ability to manage provider schedules and service types"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_APPOINTMENT_TYPES.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_PROVIDER_SCHEDULES.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_HOME.privilege()
            ));}
    };

    public static RoleDescriptor SCHEDULE_MANAGER = new RoleDescriptor() {
        public String uuid() { return "af138b30-b078-4b28-8ded-d0dfbcb8c783"; }
        public String role() { return "Application Role: scheduleManager"; }
        public String description() { return "Gives user access to all apps and tasks within the Appointment Scheduling UI module"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_APPOINTMENT_TYPES.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_PROVIDER_SCHEDULES.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_HOME.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_VIEW_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_BOOK_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_OVERBOOK_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_VIEW_CONFIDENTIAL.privilege()
            ));}
    };

    public static RoleDescriptor SCHEDULER = new RoleDescriptor() {
        public String uuid() { return "eeece3a4-94f8-4693-bc29-d862bbf6da0b"; }
        public String role() { return "Application Role: scheduler"; }
        public String description() { return "Gives user the ability to view and schedule appointments within the appointmentschedulingui module (but not to administer provider schedules or to overbook appointments)"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_HOME.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_VIEW_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_BOOK_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_VIEW_CONFIDENTIAL.privilege()
            ));}
    };

    public static RoleDescriptor SCHEDULE_VIEWER = new RoleDescriptor() {
        public String uuid() { return "7b82b7ff-6da7-4be2-adb3-44e000f95895"; }
        public String role() { return "Application Role: scheduleViewer"; }
        public String description() { return "Gives user the ability to view appointment schedules (but not to modify them)"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_HOME.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_VIEW_APPOINTMENTS.privilege()
            ));}
    };

    public static RoleDescriptor SYSTEM_ADMINISTRATOR = new RoleDescriptor() {
        public String uuid() { return "ba7772b3-89c1-4179-9b4d-e0eb96164da6"; }
        public String role() { return "Application Role: sysAdmin"; }
        public String description() { return "Application Role: sysAdmin"; }
        public Set<String> inherited() { return null; }
        public Set<String> privileges() {
            return new HashSet<String>(Arrays.asList(
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_APPOINTMENT_TYPES.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_PROVIDER_SCHEDULES.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_HOME.privilege(),
                    Privileges.APP_APPOINTMENTSCHEDULINGUI_VIEW_APPOINTMENTS.privilege(),
                    Privileges.APP_COREAPPS_AWAITING_ADMISSION.privilege(),
                    Privileges.APP_COREAPPS_FIND_PATIENT.privilege(),
                    Privileges.APP_COREAPPS_PATIENT_DASHBOARD.privilege(),
                    Privileges.APP_COREAPPS_PATIENT_VISITS.privilege(),
                    Privileges.APP_COREAPPS_ACTIVE_VISITS.privilege(),
                    Privileges.APP_DISPENSING_APP_DISPENSE.privilege(),
                    Privileges.APP_EMR_ARCHIVES_ROOM.privilege(),
                    Privileges.APP_EMR_CHECK_IN.privilege(),
                    Privileges.APP_EMR_INPATIENTS.privilege(),
                    Privileges.APP_EMR_OUTPATIENT_VITALS.privilege(),
                    Privileges.APP_EMR_SYSTEM_ADMINISTRATION.privilege(),
                    Privileges.APP_LEGACY_PATIENT_REGISTRATION.privilege(),
                    Privileges.APP_LEGACY_PATIENT_REGISTRATION_EDIT.privilege(),
                    Privileges.APP_LEGACY_ADMIN.privilege(),
                    Privileges.APP_REGISTRATION_REGISTER_PATIENT.privilege(),
                    Privileges.APP_REPORTINGUI_ADHOC_ANALYSIS.privilege(),
                    Privileges.APP_REPORTINGUI_REPORTS.privilege(),
                    Privileges.APP_ZL_MPI.privilege(),
                    Privileges.APP_ZL_REPORTS_DATA_EXPORTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_BOOK_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_OVERBOOK_APPOINTMENTS.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_VIEW_CONFIDENTIAL.privilege(),
                    Privileges.TASK_APPOINTMENTSCHEDULINGUI_REQUEST_APPOINTMENTS.privilege(),
                    Privileges.TASK_ARCHIVES_REQUEST_PAPER_RECORDS.privilege(),
                    Privileges.TASK_COREAPPS_CREATE_VISIT.privilege(),
                    Privileges.TASK_COREAPPS_CREATE_RETRO_VISIT.privilege(),
                    Privileges.TASK_COREAPPS_MERGE_VISITS.privilege(),
                    Privileges.TASK_DISPENSING_DISPENSE.privilege(),
                    Privileges.TASK_DISPENSING_EDIT.privilege(),
                    Privileges.TASK_EMR_CHECKIN.privilege(),
                    Privileges.TASK_EMR_DEATH_CERTIFICATE_FORM.privilege(),
                    Privileges.TASK_EMR_ENTER_ADMISSION_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_CONSULT_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_ED_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_SURGICAL_NOTE.privilege(),
                    Privileges.TASK_EMR_ENTER_VITALS_NOTE.privilege(),
                    Privileges.TASK_EMR_PATIENT_ENCOUNTER_DELETE.privilege(),
                    Privileges.TASK_EMR_PATIENT_ENCOUNTER_EDIT.privilege(),
                    Privileges.TASK_EMR_PATIENT_VISIT_DELETE.privilege(),
                    Privileges.TASK_EMR_PRINT_LABELS.privilege(),
                    Privileges.TASK_EMR_PRINT_WRISTBAND.privilege(),
                    Privileges.TASK_EMR_RETRO_CLINICAL_NOTE.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_CT.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_US.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_ORDER_XRAY.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_RETRO_ORDER.privilege(),
                    Privileges.TASK_RADIOLOGYAPP_TAB.privilege()
            ));}
    };

}
