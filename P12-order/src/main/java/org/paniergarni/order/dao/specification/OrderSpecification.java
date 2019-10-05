package org.paniergarni.order.dao.specification;

import lombok.AllArgsConstructor;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.exception.CriteriaException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;


@AllArgsConstructor
public class OrderSpecification implements Specification<Order> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // évite les doublons
        query.distinct(true);
        Path path;
        try {

            if (criteria.getKey().indexOf('.') >= 0) {
                String partOne = criteria.getKey().split("\\.")[0];
                String partTwo = criteria.getKey().split("\\.")[1];
                path = root.join(partOne).get(partTwo);
            } else {
                path = root.get(criteria.getKey());
            }

            if (criteria.getOperation().equalsIgnoreCase(">")) {
                if (path.getJavaType() == Date.class) {

                    return builder.greaterThan(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.greaterThan(path, criteria.getValue().toString());
                }

            } else if (criteria.getOperation().equalsIgnoreCase(">=")) {
                if (path.getJavaType() == Date.class) {

                    return builder.greaterThanOrEqualTo(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.greaterThanOrEqualTo(path, criteria.getValue().toString());
                }

            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                if (path.getJavaType() == Date.class) {

                    return builder.lessThan(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.lessThan(path, criteria.getValue().toString());
                }

            } else if (criteria.getOperation().equalsIgnoreCase("<=")) {
                if (path.getJavaType() == Date.class) {

                    return builder.lessThanOrEqualTo(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.lessThanOrEqualTo(path, criteria.getValue().toString());
                }

            } else if (criteria.getOperation().equalsIgnoreCase(":")) {

                if (path.getJavaType() == String.class) {

                    return builder.like(path, "%" + criteria.getValue() + "%");

                } else if (path.getJavaType() == Date.class) {

                    return builder.equal(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.equal(path, criteria.getValue());
                }
            } else if (criteria.getOperation().equalsIgnoreCase("ORDER_BY_DESC")) {
                query.orderBy(builder.desc(path));
            } else if (criteria.getOperation().equalsIgnoreCase("ORDER_BY_ASC")) {
                query.orderBy(builder.asc(path));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CriteriaException("search.criteria.not.correct");
        }
    }

}
