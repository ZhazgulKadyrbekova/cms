package neobis.cms.Search;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SearchCriteria {
    private String key;
    private String key2;
    private Object value;
    private SearchOperation operation;

}
