0 23 * * * /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.insert_helpline_report_into_report();" >> LOGS_DIR/insert-helpline-report-into-report.log
0 23 * * * /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.generate_operational_mi_reports();" >> LOGS_DIR/generate-operational-mi-reports.log
0 0 7 27 2 ? 2017 /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.insert_university_report_into_report();" >> LOGS_DIR/insert-university-report-into-report.log
0 0 1 21 4 ? 2017 /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.insert_university_report_into_report();" >> LOGS_DIR/insert-university-report-into-report.log
0 0 1 28 4 ? 2017 /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.insert_university_report_into_report();" >> LOGS_DIR/insert-university-report-into-report.log
0 0 1 05 5 ? 2017 /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.insert_university_report_into_report();" >> LOGS_DIR/insert-university-report-into-report.log
0 0 1 12 5 ? 2017 /usr/bin/psql --host=HOST_NAME --username=SQL_USER --command="select casesvc.insert_university_report_into_report();" >> LOGS_DIR/insert-university-report-into-report.log
