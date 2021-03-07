package neobis.cms.Search;

import neobis.cms.Entity.Bishkek.BishCourses;
import neobis.cms.Entity.Bishkek.BishOccupation;
import neobis.cms.Entity.Bishkek.BishStatuses;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        String key2 = null;
        for (SearchCriteria criteria : list) {
            if (criteria.getValue() instanceof BishCourses || criteria.getValue() instanceof BishStatuses || criteria.getValue() instanceof BishOccupation)
                key2 = "id";
            switch (criteria.getOperation()) {
                case EQUAL:
                    if (key2 != null)
                        predicates.add(builder.equal(root.get(criteria.getKey()).get(key2), criteria.getValue()));
                    else
                        predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    break;
                case MATCH:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}

// 2021-03-05 19:15:10.078000