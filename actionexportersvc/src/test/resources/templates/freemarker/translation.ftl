<#list actionRequests as actionRequest>
${actionRequest.actionType}|${actionRequest.iac?trim}|${(actionRequest.caseRef)!}|${(actionRequest.contact.title)!}|${(actionRequest.contact.forename)!}|${(actionRequest.contact.surname)!}|${(actionRequest.address.organisationName)!}|${(actionRequest.address.line1)!}|${(actionRequest.address.line2)!}|${(actionRequest.address.locality)!}|${(actionRequest.address.townName)!}|${(actionRequest.address.postcode)!}
</#list>