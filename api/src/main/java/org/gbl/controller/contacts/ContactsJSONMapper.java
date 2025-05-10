package org.gbl.controller.contacts;

import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.usecase.add.AddContactOutput;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContactsJSONMapper {

    public JSONObject toJson(AddContactOutput output) {
        return new JSONObject()
                .put("id", output.id())
                .put("name", output.name())
                .put("birthdate", output.birthdate());
    }

    public JSONObject toJson(ContactOutput contact) {
        return new JSONObject()
                .put("id", contact.id())
                .put("name", contact.name())
                .put("birthdate", contact.birthdate());
    }

    public JSONObject toJson(PaginationOutput<ContactOutput> output) {
        final var values = output.values().stream()
                .reduce(new JSONArray(),
                        (acc, next) -> acc.put(toJson(next)),
                        JSONArray::putAll);
        return new JSONObject()
                .put("current_page", output.page())
                .put("size", output.size())
                .put("total", output.total())
                .put("last_page", output.lastPage())
                .put("values", values);
    }
}
