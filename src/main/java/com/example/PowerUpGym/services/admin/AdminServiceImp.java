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
import com.example.PowerUpGym.entity.classesGym.ClassesEntity;
import com.example.PowerUpGym.entity.classesGym.PlayerClassEnrollment;
import com.example.PowerUpGym.entity.packagesGym.PackagesEntity;
import com.example.PowerUpGym.entity.payments.PaymentsEntity;
import com.example.PowerUpGym.entity.users.*;
import com.example.PowerUpGym.enums.Role;
import com.example.PowerUpGym.repositories.AdminEntityRepository;
import com.example.PowerUpGym.services.classes.ClassService;
import com.example.PowerUpGym.services.packagee.PackageService;
import com.example.PowerUpGym.services.payment.PaymentService;
import com.example.PowerUpGym.services.player.PlayerService;
import com.example.PowerUpGym.services.roles.UserRoleService;
import com.example.PowerUpGym.services.trainer.TrainerService;
import com.example.PowerUpGym.services.users.UserService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

@Service
public class AdminServiceImp implements AdminService{

    private final AdminEntityRepository adminEntityRepository;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final TrainerService trainerService;
    private final PackageService packageService;
    private final UserService userService;
    private final PlayerService playerService;
    private final PaymentService paymentService;
    private final ClassService classService;

    public AdminServiceImp(AdminEntityRepository adminEntityRepository, UserRoleService userRoleService, PasswordEncoder passwordEncoder, TrainerService trainerService, PackageService packageService, UserService userService, PlayerService playerService, PaymentService paymentService, ClassService classService) {
        this.adminEntityRepository = adminEntityRepository;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.trainerService = trainerService;
        this.packageService = packageService;
        this.userService = userService;
        this.playerService = playerService;
        this.paymentService = paymentService;
        this.classService = classService;
    }
    @Override
    public AdminEntity signupAdmin(AdminEntity admin) {
        adminEntityRepository.save(admin);
        return admin;
    }
    @Override
    public AdminEntity getAdminById(Long id) {
        return adminEntityRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Admin not found for this ID : " + id));
    }
    @Override
    public AdminEntity getAdminByUsername(String username) {
        return adminEntityRepository.findByUserUsername(username);
    }



    public String getEditAdminProfile(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            UserEntity userEntity = userService.findUserByUsername(username);

            if (userEntity != null) {
                model.addAttribute("user", userEntity);
                AdminEntity admin = userEntity.getAdmin();
                model.addAttribute("admin", admin);
                return "adminPages/updateAdmin.html";
            }
        }
        return "redirect:/error";
    }


    public String postSignupAdmin(UserRegistrationRequest userRequest, BindingResult bindingResult) {
        UserRoleEntity userRole = userRoleService.findRoleByRole(Role.ADMIN);

        if (bindingResult.hasErrors()) {
            return "adminPages/signupAdmin";
        }

        try {
            if (userRole == null) {
                throw new RuntimeException("Role not found: " + userRequest.getRole());
            }

            UserEntity user = createUserFromRequest(userRequest, userRole.getRole());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setImage("/assets/profileImg.png");

            userService.signupUser(user);

            AdminEntity admin = AdminEntity.builder().user(user).build();
            signupAdmin(admin);

            return "adminPages/adminProfile"; // Redirect to the admin page after successful registration
        } catch (Exception e) {
            return "adminPages/adminProfile"; // Handle the exception appropriately
        }
    }


    // ============== Helper Method To Create User From UserRegistrationRequest ==============
    private UserEntity createUserFromRequest(UserRegistrationRequest userRequest, Role role) {
        UserRoleEntity userRole = userRoleService.findRoleByRole(role);

        if (userRole == null) {
            throw new RuntimeException("Role not found: " + role);
        }

        return UserEntity.builder()
                .fullName(userRequest.getFullName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRole)
                .image(userRequest.getImage())
                .build();
    }

    public RedirectView getUpdateAdmin(UserUpdateRequest userUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return new RedirectView("updateAdmin?error=true");
        }
        try {
        UserEntity existingUser = userService.findUserById(userUpdateRequest.getUserId());

        UserEntity updatedUser = updateUser(existingUser, userUpdateRequest);
        userService.saveUser(updatedUser);

        // Update the username in the principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // retrieves the current authentication information from the security context using SecurityContextHolder. It provides access to the current user's authentication details
        UsernamePasswordAuthenticationToken updatedAuthentication = new UsernamePasswordAuthenticationToken(updatedUser.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        return new RedirectView("adminProfile");}
        catch (Exception e) {
            return new RedirectView("updateAdmin?error=true");}
    }

    // ============== Helper Method To Update User ==============
    private UserEntity updateUser(UserEntity existingUser, UserUpdateRequest userUpdateRequest) {
        String newPassword = (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty())
                ? passwordEncoder.encode(userUpdateRequest.getPassword())
                : existingUser.getPassword();

        return UserEntity.builder()
                .id(existingUser.getId())
                .fullName(userUpdateRequest.getFullName())
                .username(userUpdateRequest.getUsername())
                .email(userUpdateRequest.getEmail())
                .phoneNumber(userUpdateRequest.getPhoneNumber())
                .password(newPassword)
                .role(existingUser.getRole())
                .image(existingUser.getImage())
                .build();
    }

    // ============== Helper Method To Create Trainer From TrainerRegistrationRequest ==============
    private TrainerEntity createTrainerFromRequest(TrainerRegistrationRequest trainerRequest, UserEntity user, String adminUsername) {
        return TrainerEntity.builder()
                .age(trainerRequest.getAge())
                .experience(trainerRequest.getExperience())
                .admin(getAdminByUsername(adminUsername))
                .user(user)
                .build();
    }

    public RedirectView signupTrainer(@Valid UserRegistrationRequest userRequest,@Valid TrainerRegistrationRequest trainerRequest, Principal principal, BindingResult bindingResult) {

        if (userRequest.getImage().isEmpty()) {
            userRequest.setImage("/assets/profileImg.png");
        }

        UserRoleEntity trainerRole = userRoleService.findRoleByRole(Role.TRAINER);
        UserEntity user = createUserFromRequest(userRequest, trainerRole.getRole());

        userService.signupUser(user);

        TrainerEntity trainerEntity = createTrainerFromRequest(trainerRequest, user, principal.getName());

        trainerService.signupTrainer(trainerEntity);

        return new RedirectView("/adminPage");
    }

    // ============== Helper Method To Create Player From PlayerRegistrationRequest ==============
    private PlayersEntity createPlayerFromRequest(PlayerRegistrationRequest playerRequest, UserEntity user, String adminUsername) {
        PackagesEntity selectedPackage = packageService.getPackageById(playerRequest.getPackageId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(selectedPackage.getDuration());

        return PlayersEntity.builder()
                .admin(getAdminByUsername(adminUsername))
                .user(user)
                .address(playerRequest.getAddress())
                .age(playerRequest.getAge())
                .height(playerRequest.getHeight())
                .weight(playerRequest.getWeight())
                .start_date(startDate)
                .end_date(endDate)
                .selectedPackage(selectedPackage)
                .accountEnabled(true)
                .build();
    }


    private PlayersEntity createPlayerFromRegistrationRequests(RegistrationRequests registrationRequests,String adminUsername) {
        PackagesEntity selectedPackage = packageService.getPackageById(registrationRequests.getPackageId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(selectedPackage.getDuration());
        String pass = passwordEncoder.encode(registrationRequests.getPassword());
        UserRoleEntity playerRole = UserRoleEntity.builder().role(Role.PLAYER).build();
        return PlayersEntity.builder()
                .admin(getAdminByUsername(adminUsername))
                .user(UserEntity.builder()
                        .admin(getAdminByUsername(adminUsername))
                        .fullName(registrationRequests.getFullName())
                        .username(registrationRequests.getUsername())
                        .email(registrationRequests.getEmail())
                        .phoneNumber(registrationRequests.getPhoneNumber())
                        .password(pass)
                        .image("/assets/profileImg.png")
                        .role(playerRole).build())
                .address(registrationRequests.getAddress())
                .age(registrationRequests.getAge())
                .height(registrationRequests.getHeight())
                .weight(registrationRequests.getWeight())
                .start_date(startDate)
                .end_date(endDate)
                .selectedPackage(selectedPackage)
                .accountEnabled(true)
                .build();
    }
    // ============== Helper Method To Create Payment ==============
    private PaymentsEntity createPaymentForPlayer(PlayersEntity player, String paymentMethod) {
        return PaymentsEntity.builder()
                .userEntity(player.getUser())
                .amount(player.getSelectedPackage().getPrice())
                .paymentMethod(paymentMethod) // Use the parameter
                .paymentDate(LocalDate.now())
                .paymentStatus(true)
                .build();
    }

    public String signupPlayer(RegistrationRequests registrationRequests, Principal principal, BindingResult bindingResult, Model model) {
        UserRoleEntity userRole = userRoleService.findRoleByRole(Role.ADMIN);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "adminPages/signupPlayer";
        }

        try {
            if (userRole == null) {
                throw new RuntimeException("Role not found: " + registrationRequests.getRole());
            }

        if (registrationRequests.getImage().isEmpty()) {
            registrationRequests.setImage("/assets/profileImg.png");
        }

//        UserRoleEntity playerRole = UserRoleEntity.builder().role(Role.PLAYER).build();
//        UserEntity user = createUserFromRequest(registrationRequests, playerRole.getRole());
        PlayersEntity player = createPlayerFromRegistrationRequests(registrationRequests, principal.getName());
        PaymentsEntity payment = createPaymentForPlayer(player, registrationRequests.getPaymentMethod());

        playerService.signupPlayer(player);
        paymentService.savePayment(payment);
        //    sendPasswordViaSMS(playerRequest.getPhoneNumber(), playerRequest.getPassword());
            return "adminPages/adminProfile";
        }
        catch (Exception e) {
            return "adminPages/adminProfile";
        }
    }

    public RedirectView renewSubscription(@RequestParam(name = "playerId") Long playerId,
                                          @RequestParam(name = "newPackageId") Long newPackageId) {
        PlayersEntity player = playerService.getPlayerById(playerId);
        PackagesEntity newPackage = packageService.getPackageById(newPackageId);

        if (player != null && newPackage != null) {
            int newAmount = newPackage.getPrice();
            player.setSelectedPackage(newPackage);
            LocalDate newEndDate = LocalDate.now().plusMonths(newPackage.getDuration());
            player.setEnd_date(newEndDate);
            playerService.signupPlayer(player);
            PaymentsEntity payment = paymentService.getPaymentByPlayer(player);

            if (payment != null) {
                payment.setAmount(newAmount);
                paymentService.savePayment(payment);
            } else {
                return new RedirectView("/errorNullPayment");
            }
            return new RedirectView("/adminPage/allplayers");
        } else {
            return new RedirectView("/error");
        }
    }

    public RedirectView updateAccountStatus(
            @RequestParam(name = "playerId") Long playerId,
            @RequestParam(name = "accountStatus") String accountStatus) {
        PlayersEntity player = playerService.getPlayerById(playerId);

        if (player != null) { // Update the player's account status based on the selected option
            if ("ENABLED".equals(accountStatus)) {
                player.setAccountEnabled(true);
            } else if ("DISABLED".equals(accountStatus)) {
                player.setAccountEnabled(false);
            }

            playerService.signupPlayer(player);

            return new RedirectView("/adminPage/allplayers");
        } else {
            return new RedirectView("/error");
        }
    }

    // ==========  Helper Method To Create Package From AddPackageRequest  ====================
    private PackagesEntity createPackage(AddPackageRequest packageRequest, AdminEntity admin) {
        return PackagesEntity.builder()
                .packageName(packageRequest.getPackageName())
                .price(packageRequest.getPrice())
                .duration(packageRequest.getDuration())
                .description(packageRequest.getDescription())
                .admin(admin)
                .build();
    }

    public RedirectView addPackage( AddPackageRequest packageRequest, Principal principal) {
        AdminEntity admin = getAdminByUsername(principal.getName());
        PackagesEntity packageEntity = createPackage(packageRequest, admin);
        packageService.addPackage(packageEntity);
        return new RedirectView("/adminPage");
    }

    public RedirectView addClass(AddClassRequest classRequest, Principal principal) {
        TrainerEntity trainer = trainerService.getTrainerById(classRequest.getTrainerId());
        AdminEntity admin = getAdminByUsername(principal.getName());
        ClassesEntity classEntity = createClass(classRequest, trainer, admin);
        classService.addClass(classEntity);
        return new RedirectView("/adminPage");
    }

    // ==========  Helper Method To Create Class From AddClassRequest  ====================
    private ClassesEntity createClass(AddClassRequest classRequest, TrainerEntity trainer, AdminEntity admin) {
        return ClassesEntity.builder()
                .className(classRequest.getClassName())
                .scheduleDescription(classRequest.getScheduleDescription())
                .description(classRequest.getDescription())
                .trainer(trainer)
                .admin(admin)
                .build();
    }

    public RedirectView addPlayerToClass(AddPlayerToClassRequest request) {
        Long selectedPlayerId = Long.parseLong(request.getPlayerId());

        PlayersEntity player = playerService.getPlayerById(selectedPlayerId);
        ClassesEntity classEntity = classService.getClassById(request.getClassId());

        PlayerClassEnrollment enrollment = PlayerClassEnrollment.builder()
                .player(player)
                .enrolledClass(classEntity)
                .enrollmentDateTime(LocalDate.now())
                .build();

        playerService.addPlayerClassEnrollment(enrollment);
        return new RedirectView("/adminPage/allClasses");
    }

    // ========== Method to send password via SMS using Twilio ============
    private void sendPasswordViaSMS(String phoneNumber, String password) {
        // Your Twilio Account SID and Auth Token
        String ACCOUNT_SID = "AC406561e7beb7c929d6719c7236d85bed";
        String AUTH_TOKEN = "62c0fa1180daf3956b38c4c67e2b6d1f";
        // Initialize Twilio with your credentials
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Create a message
        Message message = Message.creator(
                        new PhoneNumber(phoneNumber), // Player's phone number
                        new PhoneNumber("+14345973383"), // Your Twilio phone number
                        "Your password is: " + password)
                .create();
        System.out.println("Password sent via SMS. Message SID: " + message.getSid());
    }

    public RedirectView postEditClassDetails(ClassUpdateRequest classUpdateRequest , Principal principal, @RequestParam Long trainerId) {

        ClassesEntity classEntity = classService.getClassById(classUpdateRequest.getId());
        AdminEntity admin = getAdminByUsername(principal.getName());
        TrainerEntity trainer = trainerService.getTrainerById(trainerId);

        ClassesEntity updateClass=editClasses(classEntity ,classUpdateRequest , admin , trainer);

        classService.addClass(updateClass);

        return new RedirectView("/adminPage/allClasses");
    }

    private ClassesEntity editClasses( ClassesEntity existingClass,ClassUpdateRequest classUpdateRequest , AdminEntity admin , TrainerEntity trainer){

        return  ClassesEntity.builder()
                .id(existingClass.getId())
                .scheduleDescription(classUpdateRequest.getScheduleDescription())
                .description(classUpdateRequest.getDescription())
                .className(classUpdateRequest.getClassName())
                .trainer(trainer)
                .admin(admin)
                .build();
    }

}
