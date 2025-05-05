package org.gbl.in;

import jakarta.inject.Inject;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import org.gbl.out.Pagination;
import org.gbl.out.SearchRequest;
import org.gbl.out.SortingOrder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "search", mixinStandardHelpOptions = true, description = "Searches contacts by " +
        "name and/or birthdate")
public class SearchContacts implements Callable<Integer> {

    private final ContactsGateway gateway;

    @Inject
    public SearchContacts(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    @Option(names = {"-p", "--page"}, description = "The current page of the pagination.",
            defaultValue = "1", showDefaultValue = Visibility.ALWAYS)
    public int page;

    @Option(names = {"-s", "--size"}, description = "The size of the list pagination.",
            defaultValue = "15", showDefaultValue = Visibility.ALWAYS)
    public int size;

    @Option(names = {"-o", "--order"}, description = "The order asc/desc by name of the list.",
            defaultValue = "asc",
            converter = SortingOrderConverter.class,
            showDefaultValue = Visibility.ALWAYS)
    public SortingOrder order;

    static class SortingOrderConverter implements ITypeConverter<SortingOrder> {
        @Override
        public SortingOrder convert(String value) {
            return SortingOrder.of(value);
        }
    }

    @Override
    public Integer call() {
        final var searchRequest = new SearchRequest(page, size, order, null);
        final var response = gateway.search(searchRequest)
                .onSuccess(this::onSuccess)
                .onFailure(this::onFailure);
        return response.isFailure() ? 1 : 0;
    }

    private void onFailure(Throwable throwable) {
        System.err.println("Error on search contacts: " + throwable.getMessage());
    }

    private void onSuccess(Pagination<ContactResponse> pagination) {
        System.out.println("Contacts retrieved");
        System.out.println();
        System.out.println("_____________________________________________________________________");
        System.out.println("id | name | birthdate");
        for (var contact : pagination.values()) {
            System.out.printf("%s | %s | %s%n", contact.id(), contact.name(), contact.birthdate());
        }
        System.out.println("_____________________________________________________________________");
        System.out.printf("current_page: %s | last_page: %s | total: %s%n",
                          pagination.page(), pagination.lastPage(), pagination.total());
    }
}
