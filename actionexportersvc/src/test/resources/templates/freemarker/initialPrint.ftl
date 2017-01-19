<#list actionRequests as actionRequest>
${actionRequest.iac?trim}|${(actionRequest.caseRef)!}|${(actionRequest.address.organisationName)!}||||${(actionRequest.address.line1)!}|${(actionRequest.address.line2)!}|${(actionRequest.address.locality)!}|${(actionRequest.address.townName)!}|${(actionRequest.address.postcode)!}
</#list>