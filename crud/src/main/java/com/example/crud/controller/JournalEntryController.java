package com.example.crud.controller;

import com.example.crud.models.JournalEntry;
import com.example.crud.repo.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryRepository repository;

    @GetMapping
    public String getAllEntries(Model model) {
        List<JournalEntry> entries = repository.findAll();
        model.addAttribute("entries", entries);
        return "journal/index";  
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("entry", new JournalEntry());
        return "journal/create";  
    }

    @PostMapping("/create")
    public String createEntry(@Valid @ModelAttribute("entry") JournalEntry entry, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "journal/create";
        }
        repository.save(entry);
        return "redirect:/journal";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return repository.findById(id).map(entry -> {
            model.addAttribute("entry", entry);
            return "journal/edit"; 
        }).orElse("redirect:/journal?error=notfound");  // Redirect with an error message
    }


    @PostMapping("/edit/{id}")
    public String updateEntry(@PathVariable Long id, @Valid @ModelAttribute("entry") JournalEntry updatedEntry, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "journal/edit";
        }
        updatedEntry.setId(id);  
        repository.save(updatedEntry);
        return "redirect:/journal";
    }

    @GetMapping("/delete/{id}")
    public String deleteEntry(@PathVariable Long id) {
        JournalEntry entry = repository.findById(id).orElse(null);
        if (entry != null) {
            repository.delete(entry);
        }
        return "redirect:/journal";
    }
}
