package com.vsii.enamecard.utils.filters;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

public class CriteriaStorage {

    public static final Map<SearchOperator, TriFunction<SearchCriteria, CriteriaBuilder, Root, Predicate>> FILTER_CRITERIA = new HashMap<>();

    static {
        FILTER_CRITERIA.put(SearchOperator.GREATER_THAN,(criteria, criteriaBuilder, root) -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), (Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.LESS_THAN,(criteria, criteriaBuilder, root) -> criteriaBuilder.lessThan(root.get(criteria.getKey()),(Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.EQUAL,(criteria, criteriaBuilder, root) -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.LIKE,(criteria, criteriaBuilder, root) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(criteria.getKey())),"%"+criteria.getValue().toString().toUpperCase()+"%"));
        FILTER_CRITERIA.put(SearchOperator.IN,(criteria, criteriaBuilder, root) -> criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.NOT_EQUAL,(criteria, criteriaBuilder, root) -> criteriaBuilder.notEqual(root.get(criteria.getKey()),criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.GREATER_THAN_EQUAL,(criteria, criteriaBuilder, root) -> criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()),(Comparable) criteria.getValue()));
        FILTER_CRITERIA.put(SearchOperator.LESS_THAN_EQUAL,(criteria, criteriaBuilder, root) -> criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()),(Comparable) criteria.getValue()));
    }
}
