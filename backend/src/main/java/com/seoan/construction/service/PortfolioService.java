package com.seoan.construction.service;

import com.seoan.construction.entity.Portfolio;
import com.seoan.construction.entity.PortfolioImage;
import com.seoan.construction.repository.PortfolioImageRepository;
import com.seoan.construction.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<Portfolio> findAll() {
        return portfolioRepository.findAllByOrderBySortOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<Portfolio> findByCategory(String category) {
        return portfolioRepository.findByCategoryOrderBySortOrderAsc(category);
    }

    @Transactional(readOnly = true)
    public Portfolio findById(Long id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("시공실적을 찾을 수 없습니다. ID: " + id));
    }

    public Portfolio save(Portfolio portfolio, List<MultipartFile> files) throws IOException {
        Portfolio saved = portfolioRepository.save(portfolio);

        if (files != null) {
            int order = saved.getImages().size();
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String storedName = fileStorageService.store(file);
                PortfolioImage image = PortfolioImage.builder()
                        .portfolio(saved)
                        .fileName(storedName)
                        .originalName(file.getOriginalFilename())
                        .imageUrl("/uploads/" + storedName)
                        .sortOrder(order++)
                        .build();
                saved.getImages().add(image);
            }
            portfolioRepository.save(saved);
        }

        return saved;
    }

    public Portfolio update(Long id, Portfolio updated, List<MultipartFile> files) throws IOException {
        Portfolio existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setCategory(updated.getCategory());
        existing.setCategoryLabel(updated.getCategoryLabel());
        existing.setSubtitle(updated.getSubtitle());
        existing.setScale(updated.getScale());
        existing.setPeriod(updated.getPeriod());
        existing.setLocation(updated.getLocation());
        existing.setClient(updated.getClient());
        existing.setDescription(updated.getDescription());
        existing.setSortOrder(updated.getSortOrder());

        if (files != null) {
            int order = existing.getImages().size();
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String storedName = fileStorageService.store(file);
                PortfolioImage image = PortfolioImage.builder()
                        .portfolio(existing)
                        .fileName(storedName)
                        .originalName(file.getOriginalFilename())
                        .imageUrl("/uploads/" + storedName)
                        .sortOrder(order++)
                        .build();
                existing.getImages().add(image);
            }
        }

        return portfolioRepository.save(existing);
    }

    public void delete(Long id) {
        Portfolio portfolio = findById(id);
        for (PortfolioImage img : portfolio.getImages()) {
            fileStorageService.delete(img.getFileName());
        }
        portfolioRepository.delete(portfolio);
    }

    public void deleteImage(Long imageId) {
        PortfolioImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));
        fileStorageService.delete(image.getFileName());
        imageRepository.delete(image);
    }
}
