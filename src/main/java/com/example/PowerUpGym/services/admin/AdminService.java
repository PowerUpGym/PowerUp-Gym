package com.example.PowerUpGym.services.admin;

import com.example.PowerUpGym.bo.auth.AddClassRequest;
import com.example.PowerUpGym.bo.auth.AddPackageRequest;
import com.example.PowerUpGym.bo.auth.AddPlayerToClassRequest;
import com.example.PowerUpGym.bo.auth.update.ClassUpdateRequest;
import com.example.PowerUpGym.bo.auth.update.UserUpdateRequest;
import com.example.PowerUpGym.bo.auth.users.PlayerRegistrationRequest;
import com.example.PowerUpGym.bo.auth.users.RegistrationRequests;
import com.example.PowerUpGym.bo.auth.users.TrainerRegistrationRequest;
import com.example.PowerUpGym.bo.auth.users.UserRegistrationRequest;
import com.example.PowerUpGym.entity.users.AdminEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;

public interface AdminService {

    AdminEntity signupAdmin(AdminEntity admin);

    AdminEntity getAdminById(Long id);

    AdminEntity getAdminByUsername(String username);

    String postSignupAdmin(UserRegistrationRequest userRequest, BindingResult bindingResult);

    String getEditAdminProfile(Principal principal, Model model);
    RedirectView getUpdateAdmin( UserUpdateRequest userUpdateRequest,BindingResult bindingResult);

    RedirectView signupTrainer(UserRegistrationRequest userRequest,TrainerRegistrationRequest trainerRequest, Principal principal,BindingResult bindingResult);

    String signupPlayer(RegistrationRequests registrationRequests,  Principal principal, BindingResult bindingResult, Model model);

    RedirectView renewSubscription(@RequestParam(name = "playerId") Long playerId,
                                   @RequestParam(name = "newPackageId") Long newPackageId);
    RedirectView updateAccountStatus(@RequestParam(name = "playerId") Long playerId,
                                     @RequestParam(name = "accountStatus") String accountStatus);
    RedirectView addPackage(AddPackageRequest packageRequest, Principal principal);

    RedirectView addClass(AddClassRequest classRequest, Principal principal);

    RedirectView addPlayerToClass(AddPlayerToClassRequest request);

    RedirectView postEditClassDetails(ClassUpdateRequest classUpdateRequest , Principal principal, @RequestParam Long trainerId);


//    UserEntity findUserByUsername(String username);
}
