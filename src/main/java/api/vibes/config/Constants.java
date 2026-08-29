package api.vibes.config;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final int maxResultsQueries = 50;

    private Constants() {}

    /*\/ cria paginação(Pageable) de minimo de 20 resultados e máximo de 50; */
    public static Pageable createPageable(Optional<Integer> page, Optional<Integer> size) {
        int minResults = 20;
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(minResults);
        if(pageSize > maxResultsQueries) pageSize = minResults;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return pageable;
    }

}
