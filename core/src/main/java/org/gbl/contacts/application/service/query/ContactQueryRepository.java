package org.gbl.contacts.application.service.query;

import org.gbl.contacts.application.usecase.get.ContactOutput;

public interface ContactQueryRepository extends QueryRepository<ContactOutput, ContactFilter> {
}
