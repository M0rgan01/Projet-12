package org.paniergarni.stock.dao.specification;

import lombok.AllArgsConstructor;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.exception.CriteriaException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;


@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
                if (root.get(criteria.getKey()).getJavaType() == Date.class) {

                    return builder.greaterThan(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.greaterThan(path, criteria.getValue().toString());
                }

            } else if (criteria.getOperation().equalsIgnoreCase(">=")) {
                if (root.get(criteria.getKey()).getJavaType() == Date.class) {

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
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CriteriaException("search.criteria.not.correct");
        }
    }

}
