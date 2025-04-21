package org.gbl.contacts.usecase.shared;

import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.contacts.usecase.list.ContactFilter;
import org.gbl.shared.QueryRepository;

public interface ContactQueryRepository extends QueryRepository<ContactOutput, ContactFilter> {
}
