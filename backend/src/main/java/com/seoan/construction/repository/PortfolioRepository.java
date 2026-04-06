package com.seoan.construction.repository;

import com.seoan.construction.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findAllByOrderBySortOrderAsc();
    List<Portfolio> findByCategoryOrderBySortOrderAsc(String category);
}
