-- Update InitialPrint template

SET SCHEMA 'actionexporter';

UPDATE template SET content = '<#list actionRequests as actionRequest>
${actionRequest.iac?trim}|${(actionRequest.caseRef)!}|${(actionRequest.address.organisationName)!}|${(actionRequest.contact.title)!}|${(actionRequest.contact.forename)!}|${(actionRequest.contact.surname)!}|${(actionRequest.address.line1)!}|${(actionRequest.address.line2)!}|${(actionRequest.address.locality)!}|${(actionRequest.address.townName)!}|${(actionRequest.address.postcode)!}
</#list>
'
WHERE name = 'initialPrint';