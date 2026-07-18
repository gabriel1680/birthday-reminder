package org.gbl.app;

import jakarta.inject.Inject;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;

import java.time.format.DateTimeFormatter;
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

    @Option(names = {"-n", "--name"}, description = "The name of contact to filter for." )
    public String name;

    @Option(names = {"-bf", "--birthdateFrom"}, description = "The birthdate start range to filter for." )
    public String birthdateFrom;

    @Option(names = {"-bt", "--birthdateTo"}, description = "The birthdate end range to filter for." )
    public String birthdateTo;

    @Override
    public Integer call() {
        final var filter = name == null && birthdateFrom == null && birthdateTo == null
                ? null
                : new ContactFilter(name, birthdateFrom, birthdateTo);
        final var searchRequest = new SearchRequest<>(page, size, order, filter);
        try {
            onSuccess(gateway.search(searchRequest));
            return 0;
        } catch (RuntimeException error) {
            onFailure(error);
            return 1;
        }
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
            final var birthdate = contact.birthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.printf("%s | %s | %s%n", contact.id(), contact.name(), birthdate);
        }
        System.out.println("_____________________________________________________________________");
        System.out.printf("current_page: %s | last_page: %s | total: %s%n",
                          pagination.page(), pagination.lastPage(), pagination.total());
    }
}
