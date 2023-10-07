INSERT INTO mst_roles (id, role, description) VALUES (1, 'PLAYER', 'Role for players group');
INSERT INTO mst_roles (id, role, description) VALUES (2, 'ADMIN', 'Role for admins group');
INSERT INTO mst_roles (id, role, description) VALUES (3, 'TRAINER', 'Role for trainers group');
INSERT INTO mst_roles (id, role, description) VALUES (4, 'SUPER_ADMIN', 'Role for super admin ');

INSERT INTO mst_users (full_name, user_name, email, phone_number, password, image, role_id)
VALUES ('Admin Full Name', 'admin', 'admin@example.com', '1234567890', '$2a$12$6OtJoxiELYDS3rwEn9sswunrMMWD.y0Weadvm7FphlPagS/5Ywd..', '/assets/admin_img.png', 4);