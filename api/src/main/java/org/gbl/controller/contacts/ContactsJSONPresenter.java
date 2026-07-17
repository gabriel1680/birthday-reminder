package org.gbl.controller.contacts;

import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

public class ContactsJSONPresenter {

    public JSONObject toJson(ContactOutput contact) {
        return new JSONObject()
                .put("id", contact.id())
                .put("name", contact.name())
                .put("birthdate", contact.birthdate());
    }

    public JSONObject toJson(PaginationOutput<ContactOutput> output) {
        return new JSONObject()
                .put("current_page", output.page())
                .put("size", output.size())
                .put("total", output.total())
                .put("last_page", output.lastPage())
                .put("values", toJson(output.values()));
    }

    public JSONArray toJson(Collection<ContactOutput> output) {
        return output.stream()
                .reduce(new JSONArray(),
                        (acc, next) -> acc.put(toJson(next)),
                        JSONArray::putAll);
    }
}
