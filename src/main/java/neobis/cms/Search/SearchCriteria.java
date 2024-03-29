package neobis.cms.Search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class SearchCriteria {
    private String key;
    private String field;
    private List<Long> value;
    private SearchOperation operation;

}
