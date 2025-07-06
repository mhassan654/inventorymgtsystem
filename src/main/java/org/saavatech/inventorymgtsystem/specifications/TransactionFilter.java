package org.saavatech.inventorymgtsystem.specifications;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.saavatech.inventorymgtsystem.models.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {
    public static Specification<Transaction> byFilter(String searchValue)
    {
        return ((root,query,criteriaBuilder)->{
            if (searchValue == null || searchValue.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + searchValue.toLowerCase() + "%";

//            create a list to hold my predicates
            List<Predicate> predicates = new ArrayList<>();

//            search within transaction fields
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("note")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionType")),searchPattern));

            //safety join to check the user fields
            if (root.getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("user"))){
                root.join("user", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user",JoinType.LEFT).get("name")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user",JoinType.LEFT).get("email")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user",JoinType.LEFT).get("phoneNumber")),searchPattern));

            // safety join to check the supplier fields
             if (root.getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("user"))){
                 root.join("supplier", JoinType.LEFT);
             }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("supplier",JoinType.LEFT).get("name")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("supplier",JoinType.LEFT).get("contactInfo")),searchPattern));

            if (root.getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("user"))){
                root.join("product", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).get("name")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).get("sku")),searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).get("description")),searchPattern));

            if (root.getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("user"))){
                root.join("category", JoinType.LEFT);
            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("category",JoinType.LEFT).get("name")),searchPattern));

            if (root.getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("product")) &&
                root.join("product").getJoins().stream().noneMatch(j->j.getAttribute().getName().equals("category"))){
                    root.join("product", JoinType.LEFT).join("category", JoinType.LEFT);

            }
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("product",JoinType.LEFT).join("category",JoinType.LEFT).get("name")),searchPattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        });

    }

    public static Specification<Transaction> byMonthAndYear(int month,int year)
    {
        return (root,query,criteriaBuilder)->{
            Expression<Integer> monthExpression = criteriaBuilder.function("month",Integer.class,root.get("createdAt"));
            Expression<Integer> yearExpression = criteriaBuilder.function("year",Integer.class,root.get("createdAt"));

            Predicate monthPredicate = criteriaBuilder.equal(monthExpression,month);
            Predicate yearPredicate = criteriaBuilder.equal(monthExpression,year);

            return criteriaBuilder.and(monthPredicate, yearPredicate);
        };

    }
}
