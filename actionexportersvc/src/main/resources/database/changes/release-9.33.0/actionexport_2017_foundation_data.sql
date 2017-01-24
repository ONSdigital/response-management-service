--inserts setup data into actionexportersvc for 2017 test.

set schema 'actionexporter';

--
-- TOC entry 3473 (class 0 OID 126454)
-- Dependencies: 353
-- Data for Name: template; Type: TABLE DATA; Schema: actionexporter; Owner: actionexportersvc
--

INSERT INTO template VALUES ('initialPrint', '<#list actionRequests as actionRequest>
${actionRequest.iac?trim}|${(actionRequest.caseRef)!}|${(actionRequest.address.organisationName)!}||||${(actionRequest.address.line1)!}|${(actionRequest.address.line2)!}|${(actionRequest.address.locality)!}|${(actionRequest.address.townName)!}|${(actionRequest.address.postcode)!}
</#list>
', '2017-01-23 11:10:01.975+00');
INSERT INTO template VALUES ('translation', '<#list actionRequests as actionRequest>
${actionRequest.actionType}|${actionRequest.iac?trim}|${(actionRequest.caseRef)!}|${(actionRequest.address.organisationName)!}|${(actionRequest.contact.title)!}|${(actionRequest.contact.forename)!}|${(actionRequest.contact.surname)!}|${(actionRequest.address.line1)!}|${(actionRequest.address.line2)!}|${(actionRequest.address.locality)!}|${(actionRequest.address.townName)!}|${(actionRequest.address.postcode)!}
</#list>
', '2017-01-23 11:10:30.984+00');

--
-- TOC entry 3472 (class 0 OID 126451)
-- Dependencies: 352
-- Data for Name: templatemapping; Type: TABLE DATA; Schema: actionexporter; Owner: actionexportersvc
--

INSERT INTO templatemapping VALUES ('ICL1_2003', 'initialPrint', 'ICL1_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('ICL2W_2003', 'initialPrint', 'ICL2W_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('ICL1_2703', 'initialPrint', 'ICL1_2703', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('ICLAD1_2003', 'initialPrint', 'ICLAD1_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('1RL1_0504', 'initialPrint', '1RL1_0504', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('1RL2W_0504', 'initialPrint', '1RL2W_0504', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('1RLAD1_0504', 'initialPrint', '1RLAD1_0504', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('2RL1_1804', 'initialPrint', '2RL1_1804', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('2RL2W_1804', 'initialPrint', '2RL2W_1804', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('2RLAD1_1804', 'initialPrint', '2RLAD1_1804', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('3RL1_2604', 'initialPrint', '3RL1_2604', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('3RL2W_2604', 'initialPrint', '3RL2W_2604', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('3RLAD1_2604', 'initialPrint', '3RLAD1_2604', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H1S_2003', 'initialPrint', 'H1S_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H2S_2003', 'initialPrint', 'H2S_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H1_2003', 'initialPrint', 'H1_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H2_2003', 'initialPrint', 'H2_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H1_2604QR', 'initialPrint', 'H1_2604QR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGPOL', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGCAN', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGSPA', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGARA', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGSOM', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGMAN', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGBEN', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGPOR', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGGUR', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGSHA', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGTUR', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGLIT', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGURD', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('QGGUJ', 'translation', 'TRANSLATION', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('I1S_OR', 'initialPrint', 'I1S_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('I1_OR', 'initialPrint', 'I1_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H1S_OR', 'initialPrint', 'H1S_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H1_OR', 'initialPrint', 'H1_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('I2S_OR', 'initialPrint', 'I2S_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('I2WS_OR', 'initialPrint', 'I2WS_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('I2_OR', 'initialPrint', 'I2_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('I2W_OR', 'initialPrint', 'I2W_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H2WS_OR', 'initialPrint', 'H2WS_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H2W_OR', 'initialPrint', 'H2W_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H2S_OR', 'initialPrint', 'H2S_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('H2_OR', 'initialPrint', 'H2_OR', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('ICLH1_2003', 'initialPrint', 'ICLH1_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('ICLHR1_2003', 'initialPrint', 'ICLHR1_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('ICLB1_2003', 'initialPrint', 'ICLB1_2003', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('1RLB1_0504', 'initialPrint', '1RLB1_0504', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('2RLB1_1804', 'initialPrint', '2RLB1_1804', '2017-01-23 11:10:54.28+00');
INSERT INTO templatemapping VALUES ('3RLB1_2604', 'initialPrint', '3RLB1_2604', '2017-01-23 11:10:54.28+00');
