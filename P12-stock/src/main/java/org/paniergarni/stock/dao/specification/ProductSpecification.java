package org.paniergarni.stock.dao.specification;

import lombok.AllArgsConstructor;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.exception.CriteriaException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

/**
 * Spécification d'un produit
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // évite les doublons
        query.distinct(true);
        Path path;
        try {
            // si la clée comporte un point, ex -> object.value
            if (criteria.getKey().indexOf('.') >= 0) {
                // on récupère les parties
                String partOne = criteria.getKey().split("\\.")[0];
                String partTwo = criteria.getKey().split("\\.")[1];
                // et on utilise une jointure
                path = root.join(partOne).get(partTwo);
            } else {
                // sinon on utilise la clée de base
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

                    String toLower = (String) criteria.getValue();
                    // recherche insensible à la casse
                    return builder.like(builder.lower(path), "%" + toLower.toLowerCase() + "%");

                } else if (path.getJavaType() == Date.class) {

                    return builder.equal(path, new Date((Long) criteria.getValue()));

                } else {
                    return builder.equal(path, criteria.getValue());
                }
            } else if (criteria.getOperation().equalsIgnoreCase("ORDER_BY_DESC")) {
                // order décroissant
                query.orderBy(builder.desc(path));
            } else if (criteria.getOperation().equalsIgnoreCase("ORDER_BY_ASC")) {
                // order croissant
                query.orderBy(builder.asc(path));
            }
            return null;
        } catch (Exception e) {
            throw new CriteriaException("search.criteria.not.correct");
        }
    }

}
