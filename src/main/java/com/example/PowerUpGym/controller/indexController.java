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
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/aboutUs")
    public String getAboutUs(){
        return "/nonLoggedUsers/aboutUs";
    }

//musuaab rami waleed alborini 
    @GetMapping("/calculateBMI")
    public String calculateBMI(@RequestParam("weight") double weight, @RequestParam("height") double height, Model model) {
        if (weight <= 0 || height <= 0) {
            model.addAttribute("bmiResult", "Invalid input. Please enter valid values.");
            model.addAttribute("bmiExplanation", "");
        } else {

            double bmi = weight / (height * height);
            model.addAttribute("bmiResult", "Your BMI is: " + String.format("%.2f", bmi));

            String explanation = "";
            if (bmi < 18.5) {
                explanation = "You are underweight.";
            } else if (bmi >= 18.5 && bmi < 24.9) {
                explanation = "You have a normal weight.";
            } else if (bmi >= 25 && bmi < 29.9) {
                explanation = "You are overweight.";
            } else {
                explanation = "You are obese.";
            }
            model.addAttribute("bmiExplanation", explanation);
        }



        return "index.html";
    }
}