package com.seoan.construction.controller;

import com.seoan.construction.dto.PortfolioResponse;
import com.seoan.construction.entity.Portfolio;
import com.seoan.construction.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioApiController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> list(@RequestParam(required = false) String category) {
        List<Portfolio> portfolios;
        if (category != null && !category.isEmpty() && !category.equals("all")) {
            portfolios = portfolioService.findByCategory(category);
        } else {
            portfolios = portfolioService.findAll();
        }

        List<PortfolioResponse> response = portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponse> detail(@PathVariable Long id) {
        Portfolio portfolio = portfolioService.findById(id);
        return ResponseEntity.ok(PortfolioResponse.from(portfolio));
    }
}
