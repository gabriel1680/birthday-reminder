package org.gbl.contacts.application.service.query;

import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;

public interface ContactQueryRepository extends QueryRepository<ContactOutput, ContactFilter> {
}
