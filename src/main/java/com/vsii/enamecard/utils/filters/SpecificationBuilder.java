package com.vsii.enamecard.utils.filters;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> implements Specification<T> {

    private final List<SearchCriteria> list = new ArrayList<>();

    public SpecificationBuilder(){
    }

    public void add(SearchCriteria criteria){
        this.list.add(criteria);
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria searchCriteria : this.list) {
            predicates.add(CriteriaStorage.FILTER_CRITERIA.get(searchCriteria.getOperation()).apply(searchCriteria,criteriaBuilder,root));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public SpecificationBuilder(List<SearchCriteria> list) {
        this.list.addAll(list);
    }
}
