package com.seoan.construction.dto;

import com.seoan.construction.entity.Portfolio;
import com.seoan.construction.entity.PortfolioImage;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PortfolioResponse {
    private Long id;
    private String title;
    private String category;
    private String categoryLabel;
    private String subtitle;
    private String scale;
    private String period;
    private String location;
    private String client;
    private String description;
    private String thumbnailUrl;
    private List<ImageDto> images;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class ImageDto {
        private Long id;
        private String imageUrl;
        private int sortOrder;
    }

    public static PortfolioResponse from(Portfolio p) {
        return PortfolioResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .category(p.getCategory())
                .categoryLabel(p.getCategoryLabel())
                .subtitle(p.getSubtitle())
                .scale(p.getScale())
                .period(p.getPeriod())
                .location(p.getLocation())
                .client(p.getClient())
                .description(p.getDescription())
                .thumbnailUrl(p.getThumbnailUrl())
                .images(p.getImages().stream()
                        .map(img -> ImageDto.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .sortOrder(img.getSortOrder())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
