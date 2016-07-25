--initial data to insert into action
-- tables are actionplan, actionplanjobstate, actionrule, actionstate, actiontype, survey
--this script populates with the data used for 2016 Census Test.

SET schema 'action';

INSERT INTO survey (surveyid, surveydate, name) VALUES (1, '2016-06-30 23:00:00+00', '2016 Test');


--
-- Data for Name: actionplan; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (1, 1, 'HH', 'Household Action Plan', 'SYSTEM', '2016-07-22 12:19:34.387116+00', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (2, 1, 'CH', 'Care Home Action Plan', 'SYSTEM', '2016-07-22 12:19:34.387116+00', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (3, 1, 'HGH', 'Hotel and Guest House Action Plan', 'SYSTEM', '2016-07-22 12:19:34.387116+00', NULL);


--
-- Data for Name: actiontype; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1, 'HouseholdInitialContact', 'Household initial contact', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2, 'HouseholdUploadIAC', 'Household upload IAC', 'HHSurvey', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3, 'HouseholdCreateVisit', 'Household create visit', 'Field', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4, 'CareHomeInitialContact', 'Care home initial contact', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (5, 'CareHomeCreateVisit', 'Care home create visit', 'Field', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (6, 'HotelGuestHouseInitialContact', 'Hotel and guest house initial contact', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (7, 'HotelUploadIAC', 'Hotel upload IAC', 'HotelSurvey', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (8, 'HotelGuestHouseCreateVisit', 'Hotel and guest house create visit', 'Field', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (12, 'AddressCheck', 'Address check', 'Field', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (9, 'GeneralEscalation', 'General escalation', 'CensusSupport', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (10, 'SurveyEscalation', 'Survey escalation', 'CensusSupport', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (11, 'ComplaintEscalation', 'Complaint escalation', 'CensusSupport', true);


--
-- Data for Name: actionrule; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (1, 1, 1, 'HH_IC-39', 'Create Household Print List for: All Cases', -39, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (2, 1, 2, 'HH_IAC-42', 'Upload Household IAC codes to online questionnaire for: All Cases', -42, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (3, 1, 3, 'HH_CV+7', 'Create Household Visit Job for: All Cases where State = INIT', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (4, 2, 4, 'CH_IC-39', 'Create Care Home Print List for: All Cases', -39, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (5, 2, 5, 'CH_Cv+7', 'Create Care Home Visit Job for: All Cases where State = INIT', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (6, 3, 6, 'HGH_IC-39', 'Create Hotel and Guest House Print List for: All Cases', -39, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (7, 3, 7, 'HGH_IAC-42', 'Upload Hotel and Guest House IAC codes to online questionnaire for: All Cases', -42, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (8, 3, 8, 'HGH_CV+7', 'Create Hotel and Guest House Visit Job for: All Cases where State = INIT or State = RESPONDED', 7, 3);


--
-- Data for Name: actionstate; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actionstate (state) VALUES ('SUBMITTED');
INSERT INTO actionstate (state) VALUES ('PENDING');
INSERT INTO actionstate (state) VALUES ('ACTIVE');
INSERT INTO actionstate (state) VALUES ('COMPLETED');
INSERT INTO actionstate (state) VALUES ('CANCEL_SUBMITTED');
INSERT INTO actionstate (state) VALUES ('CANCELLED');
INSERT INTO actionstate (state) VALUES ('CANCEL_PENDING');
INSERT INTO actionstate (state) VALUES ('CANCELLING');
INSERT INTO actionstate (state) VALUES ('ABORTED');


--
-- Data for Name: actionplanjobstate; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actionplanjobstate (state) VALUES ('SUBMITTED');
INSERT INTO actionplanjobstate (state) VALUES ('STARTED');
INSERT INTO actionplanjobstate (state) VALUES ('COMPLETED');
INSERT INTO actionplanjobstate (state) VALUES ('FAILED');



