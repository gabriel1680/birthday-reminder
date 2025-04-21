package org.gbl.contacts.application.usecase.shared;

import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.shared.QueryRepository;

public interface ContactQueryRepository extends QueryRepository<ContactOutput, ContactFilter> {
}
