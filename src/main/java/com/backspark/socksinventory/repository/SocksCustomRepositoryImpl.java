package com.backspark.socksinventory.repository;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.entity.Socks;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Setter
public class SocksCustomRepositoryImpl implements SocksCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Socks> findWithFilters(SocksFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Socks> cq = cb.createQuery(Socks.class);
        Root<Socks> root = cq.from(Socks.class);

        List<Predicate> predicates = new ArrayList<>();

        addColorPredicate(filter, cb, root, predicates);
        addOperationPredicate(filter, cb, root, predicates);
        addRangePredicate(filter, cb, root, predicates);

        cq.where(predicates.toArray(new Predicate[0]));

        applySorting(filter, cb, cq, root);

        return entityManager.createQuery(cq).getResultList();
    }

    private void addColorPredicate(SocksFilterRequest filter, CriteriaBuilder cb, Root<Socks> root, List<Predicate> predicates) {
        if (filter.getColor() != null) {
            predicates.add(cb.equal(root.get("color"), filter.getColor()));
        }
    }

    private void addOperationPredicate(SocksFilterRequest filter, CriteriaBuilder cb, Root<Socks> root, List<Predicate> predicates) {
        if (filter.getOperation() != null && filter.getCottonPart() != null) {
            switch (filter.getOperation()) {
                case "moreThan" -> predicates.add(cb.greaterThan(root.get("cottonPart"), filter.getCottonPart()));
                case "lessThan" -> predicates.add(cb.lessThan(root.get("cottonPart"), filter.getCottonPart()));
                case "equal" -> predicates.add(cb.equal(root.get("cottonPart"), filter.getCottonPart()));
            }
        }
    }

    private void addRangePredicate(SocksFilterRequest filter, CriteriaBuilder cb, Root<Socks> root, List<Predicate> predicates) {
        if (filter.getCottonPartFrom() != null && filter.getCottonPartTo() != null) {
            predicates.add(cb.between(root.get("cottonPart"), filter.getCottonPartFrom(), filter.getCottonPartTo()));
        }
    }

    private void applySorting(SocksFilterRequest filter, CriteriaBuilder cb, CriteriaQuery<Socks> cq, Root<Socks> root) {
        if (filter.getSortBy() != null) {
            Path<?> sortPath = switch (filter.getSortBy()) {
                case "color" -> root.get("color");
                case "cottonPart" -> root.get("cottonPart");
                default -> root.get("id");
            };

            cq.orderBy("desc".equals(filter.getDirection()) ? cb.desc(sortPath) : cb.asc(sortPath));
        }
    }

}
