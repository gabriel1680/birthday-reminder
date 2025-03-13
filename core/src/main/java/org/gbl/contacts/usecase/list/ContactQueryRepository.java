package org.gbl.contacts.usecase.list;

import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.shared.QueryRepository;

public interface ContactQueryRepository extends QueryRepository<ContactOutput, ContactFilter> {
}
