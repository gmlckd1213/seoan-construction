package com.seoan.construction.controller;

import com.seoan.construction.entity.Portfolio;
import com.seoan.construction.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PortfolioService portfolioService;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/portfolios")
    public String list(Model model) {
        model.addAttribute("portfolios", portfolioService.findAll());
        return "admin/list";
    }

    @GetMapping("/portfolios/new")
    public String createForm(Model model) {
        model.addAttribute("portfolio", new Portfolio());
        return "admin/form";
    }

    @PostMapping("/portfolios")
    public String create(@ModelAttribute Portfolio portfolio,
                         @RequestParam(value = "files", required = false) List<MultipartFile> files,
                         RedirectAttributes ra) throws IOException {
        portfolioService.save(portfolio, files);
        ra.addFlashAttribute("message", "시공실적이 등록되었습니다.");
        return "redirect:/admin/portfolios";
    }

    @GetMapping("/portfolios/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("portfolio", portfolioService.findById(id));
        return "admin/form";
    }

    @PostMapping("/portfolios/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Portfolio portfolio,
                         @RequestParam(value = "files", required = false) List<MultipartFile> files,
                         RedirectAttributes ra) throws IOException {
        portfolioService.update(id, portfolio, files);
        ra.addFlashAttribute("message", "시공실적이 수정되었습니다.");
        return "redirect:/admin/portfolios";
    }

    @PostMapping("/portfolios/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        portfolioService.delete(id);
        ra.addFlashAttribute("message", "시공실적이 삭제되었습니다.");
        return "redirect:/admin/portfolios";
    }

    @PostMapping("/portfolios/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long imageId,
                              @RequestParam Long portfolioId,
                              RedirectAttributes ra) {
        portfolioService.deleteImage(imageId);
        ra.addFlashAttribute("message", "이미지가 삭제되었습니다.");
        return "redirect:/admin/portfolios/" + portfolioId + "/edit";
    }
}
