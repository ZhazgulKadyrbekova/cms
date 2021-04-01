package neobis.cms.Search;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class OshClientSpecification<OshClient> implements Specification<OshClient> {
    private List<SearchCriteria> list;
    public OshClientSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<OshClient> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate predicate;
        List<Predicate> fieldPredicatesList = new ArrayList<>();
        for (SearchCriteria criteria : list) {
            List<Predicate> predicateList = new ArrayList<>();
            if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                for (int itemID = 0; itemID < criteria.getValue().size(); itemID++)
                    predicateList.add(builder.equal(root.get(criteria.getKey()), criteria.getValue().get(itemID)));
                predicate = builder.or(predicateList.toArray(new Predicate[0]));
                predicates.add(predicate);
            }
            else if (criteria.getOperation().equals(SearchOperation.MATCH))
                fieldPredicatesList.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getField().toLowerCase() + "%"));
        }
        predicate = builder.or(fieldPredicatesList.toArray(new Predicate[0]));
        predicates.add(predicate);
        predicates.toArray(new Predicate[0]);
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
