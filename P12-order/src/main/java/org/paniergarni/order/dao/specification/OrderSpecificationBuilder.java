package org.paniergarni.order.dao.specification;

import org.paniergarni.order.entities.Order;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class OrderSpecificationBuilder {

    private List<SearchCriteria> params;


    public OrderSpecificationBuilder(List<SearchCriteria> searchCriteriaList) {
        params = searchCriteriaList;
    }

    public OrderSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Order> build() {
        if (params == null || params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(OrderSpecification::new)
                .collect(Collectors.toList());

        Specification<Order> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
