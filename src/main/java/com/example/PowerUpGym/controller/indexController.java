package com.example.PowerUpGym.controller;

import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.entity.users.TrainerEntity;
import com.example.PowerUpGym.repositories.ClassesEntityRepository;
import com.example.PowerUpGym.repositories.PackagesEntityRepository;
import com.example.PowerUpGym.repositories.TrainerEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class indexController {

    @Autowired
    private TrainerEntityRepository trainerEntityRepository;

    @Autowired
    private PackagesEntityRepository packagesEntityRepository;

    @Autowired
    private ClassesEntityRepository classesEntityRepository;

    @GetMapping("/homeTrainers")
    public String getTrainers(Model model){
        List<TrainerEntity> trainer = trainerEntityRepository.findAll();
        model.addAttribute("trainers", trainer);
        return "/nonLoggedUsers/homeTrainers";
    }

    @GetMapping("/homePackages")
    public String getPackages(Model model){
        List<PackagesEntity> packages = packagesEntityRepository.findAll();
        model.addAttribute("packages", packages);
        return "/nonLoggedUsers/homePackages";
    }

    @GetMapping("/homeClasses")
    public String getClasses(Model model){
        List<ClassesEntity> classes = classesEntityRepository.findAll();
        model.addAttribute("classes", classes);
        return "/nonLoggedUsers/homeClasses";
    }
}