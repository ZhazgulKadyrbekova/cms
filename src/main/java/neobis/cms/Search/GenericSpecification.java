package neobis.cms.Search;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private List<SearchCriteria> list;
    public GenericSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            switch (criteria.getOperation()) {
                case EQUAL:
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case GREATER_THAN_EQUAL:
                    if (criteria.getValue() instanceof LocalDateTime) {
//                        predicates.add(builder.between(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue(), LocalDateTime.now()));
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue()));
                    }
                    else
                        predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    if (criteria.getValue() instanceof LocalDateTime) {
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue()));
                    }
                    else
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case MATCH:
                    if (criteria.getKey2() != null)
                        predicates.add(builder.like(builder.lower(root.get(criteria.getKey()).get(criteria.getKey2())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    else
                        predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}

// 2021-03-05 19:15:10.078000