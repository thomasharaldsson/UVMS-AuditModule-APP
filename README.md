# AuditModule

Handles logging of events in modules.

## External interfaces
### JMS

Audit listens for requests from other modules over JMS

* Request queue: UVMSAuditEvent
* Response queue: UVMSAudit

### REST

Requests from the frontend are performed over REST

* POST /audit/rest/audit/list
 - List audit events for request
