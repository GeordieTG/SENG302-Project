INSERT INTO `sport` (`name`)
VALUES ('Football'),
       ('League'),
       ('Running'),
       ('Hockey'),
       ('Rowing'),
       ('Lacrosse'),
       ('Rugby'),
       ('Cricket'),
       ('Baseball'),
       ('Basketball'),
       ('Volleyball'),
       ('Sailing'),
       ('Rolling'),
       ('Futsal');

INSERT INTO `location` (`city`, `country`, `line_1`, `line_2`, `postcode`, `suburb`)
VALUES ('Albany', 'New Zealand', '', '', NULL, ''),
       ('Albany', 'New Zealand', '', '', NULL, ''),
       ('Marsellie', 'England', '', '', NULL, ''),
       ('Marsellie', 'England', '', '', NULL, ''),
       ('chch', 'nz', '', '', NULL, ''),
       ('Viet', 'Japan', '', '', NULL, ''),
       ('Viet', 'Japan', '', '', NULL, ''),
       ('Osaka', 'Netherlands', '', '', NULL, ''),
       ('Osaka', 'Netherlands', '', '', NULL, ''),
       ('Kyoto', 'China', '', '', NULL, ''),
       ('Kyoto', 'China', '', '', NULL, ''),
       ('Seoul', 'South Korea', '', '', NULL, ''),
       ('Seoul', 'South Korea', '', '', NULL, ''),
       ('Heaven', 'Brazil', '', '', NULL, ''),
       ('Heaven', 'Brazil', '', '', NULL, ''),
       ('EDZ', 'Russia', '', '', NULL, ''),
       ('EDZ', 'Russia', '', '', NULL, ''),
       ('Albany', 'New Zealand', '', '', NULL, ''),
       ('Albany', 'New Zealand', '', '', NULL, ''),
       ('Marsellie', 'England', '', '', NULL, ''),
       ('Marsellie', 'England', '', '', NULL, ''),
       ('Viet', 'Japan', '', '', NULL, ''),
       ('Viet', 'Japan', '', '', NULL, ''),
       ('Osaka', 'Netherlands', '', '', NULL, ''),
       ('Osaka', 'Netherlands', '', '', NULL, '');


INSERT INTO `tab_user` (`confirmation`, `date_of_birth`, `email`, `expiry_time`, `favourite_sport`,
                        `first_name`, `last_name`, `password`, `profile_picture`,
                        `reset_password_expiry_time`, `reset_password_token`, `address_id`)
VALUES (NULL, '1999-12-26', 'test0@email.com', NULL, 'Football', 'Raith', 'Fullam',
        '$2a$10$NvmUOO0uJ22P/C5MoccEZe.LUAASkH/9Icvj/OGk2FXTv2IaxOd/e', 'images/default.jpg', NULL,
        NULL, 1),
       (NULL, '1999-12-26', 'test1@email.com', NULL, 'League', 'Natalie', 'Kim',
        '$2a$10$EMaaK2ykOGqcCLDxXGSD1Ox7spSqLuHCRNgqoXb65G1Vyj33Za52S', 'images/default.jpg', NULL,
        NULL, 3),
       (NULL, '1999-12-26', 'test2@email.com', NULL, 'Running', 'Rachel', 'Hodgson',
        '$2a$10$M/exwAkXyTiYBLuPASuXTOB4l6f5XOrZa/bH5ejcEJrLle6iUS3AS', 'images/default.jpg', NULL,
        NULL, 6),
       (NULL, '1999-12-26', 'test3@email.com', NULL, 'Hockey', 'Liam', 'Cuthbert',
        '$2a$10$RKTelgpASt7gVMcMTBgs9uSb.18XPGBCHnPVcQfcSDqbzFkkeGuqm', 'images/default.jpg', NULL,
        NULL, 8),
       (NULL, '1999-12-26', 'test4@email.com', NULL, 'Rowing', 'Nathan', 'Fronda',
        '$2a$10$B6YQh2DJKBmtkT1ne7VTdu4vPCmGwiy6tPmkHesQBJpdVDIq4V9TO', 'images/default.jpg', NULL,
        NULL, 10),
       (NULL, '1999-12-26', 'test5@email.com', NULL, 'Lacrosse', 'Morgan', 'English',
        '$2a$10$gScS6/ApgAEvDXraOEyuBOIlAGaMHdRbefIn18cCmnC7St./AwPNe', 'images/default.jpg', NULL,
        NULL, 12),
       (NULL, '1999-12-26', 'test6@email.com', NULL, 'Rugby', 'Geordie', 'Gibson',
        '$2a$10$mGqCOYK48zEbsYaP1UPJf.rzJxGZCRvD2So4wgYQwKLOUU8cBMF5a', 'images/default.jpg', NULL,
        NULL, 14),
       (NULL, '1999-12-26', 'test7@email.com', NULL, 'Futsal', 'Daniel', 'Neal',
        '$2a$10$Hh00IqybXM83dAwHwQIqp.Ags5CKZ01bpUuUhUQRsVn0rcR.XnhdC', 'images/default.jpg', NULL,
        NULL, 16),
       (NULL, '1999-12-26', 'test8@email.com', NULL, 'Football', 'Fabian', 'Gilson',
        '$2a$10$GSHo6tf.xu/0.srhFvvZ9ucuj8Y/7kyX.28VO4zA1ID8.r6h4vuKe', 'images/default.jpg', NULL,
        NULL, 18),
       (NULL, '1999-12-26', 'test9@email.com', NULL, 'Golf', 'Phyu', 'Lwin',
        '$2a$10$xCWcLiXLUzi.qWD1c.cJWeckjuROe7Uvq6KRDa/E6dOkirlUA1cL.', 'images/default.jpg', NULL,
        NULL, 20),
       (NULL, '1999-12-26', 'test10@email.com', NULL, 'Running', 'Mustapha', 'Conteh',
        '$2a$10$mpZMxAoC0HXE8CtQSum0juDfPVP5A3pLuk2gp6FPR1S/sBHw6gzM2', 'images/default.jpg', NULL,
        NULL, 22),
       (NULL, '1999-12-26', 'test11@email.com', NULL, 'Hockey', 'Caleb', 'Davey',
        '$2a$10$wnkD2ibqQeeqCgxFSbLOluMjU2.sropGJio1wZzW1It3JGMdCO5/a', 'images/default.jpg', NULL,
        NULL, 24);

INSERT INTO `club` (`image`, `name`, `sport`, `address_id`, `owner_id`)
VALUES ('images/default.jpg', 'Manchester', 'Hockey', 1, 2);

INSERT INTO `team` (`creation_date`, `current_token`, `current_token_regen_time`, `draws`, `image`,
                    `losses`, `name`, `sport`, `wins`, `club_id`, `address_id`)
VALUES ('2023-07-31 10:29:28.328000', 'AAAAAAAAAAAA', NULL, 0, 'images/default.jpg', 0, 'Chelsea',
        'Football', 0, NULL, 2),
       ('2023-07-31 10:29:28.905000', 'AAAAAAAAAAAC', NULL, 0, 'images/default.jpg', 0,
        'Scotland National Team', 'Hockey', 0, NULL, 4),
       ('2023-07-31 10:29:29.535000', 'AAAAAAAAAAAB', NULL, 0, 'images/default.jpg', 0,
        'Kenya National Team', 'Running', 0, NULL, 7),
       ('2023-07-31 10:29:29.951000', NULL, NULL, 0, 'images/default.jpg', 0,
        'Netherlands National Team', 'Hockey', 0, NULL, 9),
       ('2023-07-31 10:29:30.299000', NULL, NULL, 0, 'images/default.jpg', 0,
        'New Zealand National Team', 'Rowing', 0, NULL, 11),
       ('2023-07-31 10:29:30.690000', NULL, NULL, 0, 'images/default.jpg', 0,
        'Canada National Team', 'Lacrosse', 0, NULL, 13),
       ('2023-07-31 10:29:30.993000', NULL, NULL, 0, 'images/default.jpg', 0, 'Crusaders', 'Rugby',
        0, NULL, 15),
       ('2023-07-31 10:29:31.311000', NULL, NULL, 0, 'images/default.jpg', 0,
        'Brighton & Hove Albion', 'Futsal', 0, NULL, 17),
       ('2023-07-31 10:29:31.609000', NULL, NULL, 0, 'images/default.jpg', 0,
        'Brazilian National Team', 'Football', 0, NULL, 19),
       ('2023-07-31 10:29:31.960000', NULL, NULL, 0, 'images/default.jpg', 0, 'Lydia Kim', 'Golf',
        0, NULL, 21),
       ('2023-07-31 10:29:32.239000', NULL, NULL, 0, 'images/default.jpg', 0, 'Kenya Juniors',
        'Running', 0, NULL, 23),
       ('2023-07-31 10:29:32.648000', NULL, NULL, 0, 'images/default.jpg', 0, 'New Zealand u23s',
        'Hockey', 0, NULL, 25),
       ('2023-07-31 10:29:32.648000', NULL, NULL, 0, 'images/default.jpg', 0, 'New Zealand u33s',
        'Hockey', 0, 1, 24);


INSERT INTO `activity` (`dtype`, `description`, `end_time`, `formation`, `position`,
                        `start_time`, `type`, `home_score`, `opposition_score`, `outcome`,
                        `address_id`, `team_id`, `user_id`)
VALUES ('Game', 'hey guys big game this weekend', '2023-07-31T11:29', NULL, NULL,
        '2023-07-31T10:29', 'Game', NULL, NULL, 'UNDECIDED', 5, 2, 2);


INSERT INTO `authority` (`role`, `user_id`)
VALUES ('ROLE_USER', 1),
       ('ROLE_USER', 2),
       ('ROLE_USER', 3),
       ('ROLE_USER', 4),
       ('ROLE_USER', 5),
       ('ROLE_USER', 6),
       ('ROLE_USER', 7),
       ('ROLE_USER', 8),
       ('ROLE_USER', 9),
       ('ROLE_USER', 10),
       ('ROLE_USER', 11),
       ('ROLE_USER', 12);

INSERT INTO `team_roles` (`team_id`, `user_id`, `role`, `total_games`, `total_points`, `total_time`)
VALUES (1, 1, 'Manager', 0, 0, 0),
       (1, 2, 'Member', 0, 0, 0),
       (1, 3, 'Member', 0, 0, 0),
       (1, 4, 'Member', 0, 0, 0),
       (1, 5, 'Member', 0, 0, 0),
       (1, 6, 'Member', 0, 0, 0),
       (1, 7, 'Member', 0, 0, 0),
       (1, 8, 'Member', 0, 0, 0),
       (1, 9, 'Member', 0, 0, 0),
       (1, 10, 'Member', 0, 0, 0),
       (1, 11, 'Member', 0, 0, 0),
       (1, 12, 'Member', 0, 0, 0),
       (2, 2, 'Manager', 0, 0, 0),
       (3, 1, 'Member', 0, 0, 0),
       (3, 2, 'Member', 0, 0, 0),
       (3, 3, 'Manager', 0, 0, 0),
       (3, 4, 'Member', 0, 0, 0),
       (3, 5, 'Member', 0, 0, 0),
       (3, 6, 'Member', 0, 0, 0),
       (3, 7, 'Member', 0, 0, 0),
       (3, 8, 'Member', 0, 0, 0),
       (3, 9, 'Member', 0, 0, 0),
       (3, 10, 'Member', 0, 0, 0),
       (3, 11, 'Member', 0, 0, 0),
       (3, 12, 'Member', 0, 0, 0),
       (4, 1, 'Member', 0, 0, 0),
       (4, 2, 'Member', 0, 0, 0),
       (4, 3, 'Member', 0, 0, 0),
       (4, 4, 'Manager', 0, 0, 0),
       (4, 5, 'Member', 0, 0, 0),
       (4, 6, 'Member', 0, 0, 0),
       (4, 7, 'Member', 0, 0, 0),
       (4, 8, 'Member', 0, 0, 0),
       (4, 9, 'Member', 0, 0, 0),
       (4, 10, 'Member', 0, 0, 0),
       (4, 11, 'Member', 0, 0, 0),
       (4, 12, 'Member', 0, 0, 0),
       (5, 1, 'Member', 0, 0, 0),
       (5, 2, 'Member', 0, 0, 0),
       (5, 3, 'Member', 0, 0, 0),
       (5, 4, 'Member', 0, 0, 0),
       (5, 5, 'Manager', 0, 0, 0),
       (5, 6, 'Member', 0, 0, 0),
       (5, 7, 'Member', 0, 0, 0),
       (5, 8, 'Member', 0, 0, 0),
       (5, 9, 'Member', 0, 0, 0),
       (5, 10, 'Member', 0, 0, 0),
       (5, 11, 'Member', 0, 0, 0),
       (5, 12, 'Member', 0, 0, 0),
       (6, 1, 'Member', 0, 0, 0),
       (6, 2, 'Member', 0, 0, 0),
       (6, 3, 'Member', 0, 0, 0),
       (6, 4, 'Member', 0, 0, 0),
       (6, 5, 'Member', 0, 0, 0),
       (6, 6, 'Manager', 0, 0, 0),
       (6, 7, 'Member', 0, 0, 0),
       (6, 8, 'Member', 0, 0, 0),
       (6, 9, 'Member', 0, 0, 0),
       (6, 10, 'Member', 0, 0, 0),
       (6, 11, 'Member', 0, 0, 0),
       (6, 12, 'Member', 0, 0, 0),
       (7, 1, 'Member', 0, 0, 0),
       (7, 2, 'Member', 0, 0, 0),
       (7, 3, 'Member', 0, 0, 0),
       (7, 4, 'Member', 0, 0, 0),
       (7, 5, 'Member', 0, 0, 0),
       (7, 6, 'Member', 0, 0, 0),
       (7, 7, 'Manager', 0, 0, 0),
       (7, 8, 'Member', 0, 0, 0),
       (7, 9, 'Member', 0, 0, 0),
       (7, 10, 'Member', 0, 0, 0),
       (7, 11, 'Member', 0, 0, 0),
       (7, 12, 'Member', 0, 0, 0),
       (8, 1, 'Member', 0, 0, 0),
       (8, 2, 'Member', 0, 0, 0),
       (8, 3, 'Member', 0, 0, 0),
       (8, 4, 'Member', 0, 0, 0),
       (8, 5, 'Member', 0, 0, 0),
       (8, 6, 'Member', 0, 0, 0),
       (8, 7, 'Member', 0, 0, 0),
       (8, 8, 'Manager', 0, 0, 0),
       (8, 9, 'Member', 0, 0, 0),
       (8, 10, 'Member', 0, 0, 0),
       (8, 11, 'Member', 0, 0, 0),
       (8, 12, 'Member', 0, 0, 0),
       (9, 1, 'Member', 0, 0, 0),
       (9, 2, 'Member', 0, 0, 0),
       (9, 3, 'Member', 0, 0, 0),
       (9, 4, 'Member', 0, 0, 0),
       (9, 5, 'Member', 0, 0, 0),
       (9, 6, 'Member', 0, 0, 0),
       (9, 7, 'Member', 0, 0, 0),
       (9, 8, 'Member', 0, 0, 0),
       (9, 9, 'Manager', 0, 0, 0),
       (9, 10, 'Member', 0, 0, 0),
       (9, 11, 'Member', 0, 0, 0),
       (9, 12, 'Member', 0, 0, 0),
       (10, 1, 'Member', 0, 0, 0),
       (10, 2, 'Member', 0, 0, 0),
       (10, 3, 'Member', 0, 0, 0),
       (10, 4, 'Member', 0, 0, 0),
       (10, 5, 'Member', 0, 0, 0),
       (10, 6, 'Member', 0, 0, 0),
       (10, 7, 'Member', 0, 0, 0),
       (10, 8, 'Member', 0, 0, 0),
       (10, 9, 'Member', 0, 0, 0),
       (10, 10, 'Manager', 0, 0, 0),
       (10, 11, 'Member', 0, 0, 0),
       (10, 12, 'Member', 0, 0, 0),
       (11, 1, 'Member', 0, 0, 0),
       (11, 2, 'Member', 0, 0, 0),
       (11, 3, 'Member', 0, 0, 0),
       (11, 4, 'Member', 0, 0, 0),
       (11, 5, 'Member', 0, 0, 0),
       (11, 6, 'Member', 0, 0, 0),
       (11, 7, 'Member', 0, 0, 0),
       (11, 8, 'Member', 0, 0, 0),
       (11, 9, 'Member', 0, 0, 0),
       (11, 10, 'Member', 0, 0, 0),
       (11, 11, 'Manager', 0, 0, 0),
       (11, 12, 'Member', 0, 0, 0),
       (12, 1, 'Member', 0, 0, 0),
       (12, 2, 'Member', 0, 0, 0),
       (12, 3, 'Member', 0, 0, 0),
       (12, 4, 'Member', 0, 0, 0),
       (12, 5, 'Member', 0, 0, 0),
       (12, 6, 'Member', 0, 0, 0),
       (12, 7, 'Member', 0, 0, 0),
       (12, 8, 'Member', 0, 0, 0),
       (12, 9, 'Member', 0, 0, 0),
       (12, 10, 'Member', 0, 0, 0),
       (12, 11, 'Member', 0, 0, 0),
       (12, 12, 'Manager', 0, 0, 0),
       (13, 2, 'Manager', 0, 0, 0);

INSERT INTO `club` (`image`, `name`, `sport`, `address_id`, `owner_id`)

VALUES ('images/default.jpg', 'Liam''s Club', 'Football', 1, 2),
       ('images/default.jpg', 'Raith''s Club', 'Football', 2, 1),
       ('images/default.jpg', 'Daniel''s Club', 'Football', 2, 3);

INSERT INTO `recorded_food` (`DATE_RECORDED`, `fdc_id`, `PORTION_SIZE_NUMBER`, `QUANTITY`, `USER_ID`) VALUES (CURRENT_DATE, 170720, 87146, 1, 3);

-- Maintain Meals
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Greek Yogurt with Berries and Honey', 'Maintain', 'honey_pot');
SET @mealId = 1;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 171304, 247.35, 22.95, 12.75, 10.149, 10.2, 255, 'Yogurt'), -- Yogurt
    (@mealId, 171711, 42.75, 0.555, 0.2475, 10.875, 7.47, 75, 'Blueberries'), -- Blueberries
    (@mealId, 169640, 21.55, 0.0212, 0, 5.83, 5.81, 7.08, 'Honey'); -- Honey

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Scrambled Eggs with Spinach and Tomatoes', 'Maintain', 'cooking_eggs');
SET @mealId = 2;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172187, 298, 19.98, 22, 3.22, 2.78, 200, 'Eggs'), -- Eggs
    (@mealId, 168462, 40.25, 4.995, 0.6825, 6.3525, 0.735, 175, 'Spinach'), -- Spinach
    (@mealId, 170457, 7.56, 0.3696, 0.084, 1.6308, 1.1046, 42, 'Tomatoes'); -- Tomatoes


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Lentil Soup with a Side of Whole-grain Bread', 'Maintain', 'soup');
SET @mealId = 3;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 171577, 104.94, 5.742, 2.1008, 18.81, 5.148, 198, 'Lentil'), -- Lentil
    (@mealId, 168013, 201.4, 10.184, 3.2238, 32.918, 4.8594, 76, 'Whole-grain bread'); -- Whole-grain bread


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Baked Salmon with Steamed Broccoli and Quinoa', 'Maintain', 'fish');
SET @mealId = 4;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 175168, 293.2, 31.402, 17.648, 0, 0, 142, 'Salmon'), -- Salmon
    (@mealId, 169967, 37.275, 2.5317, 0.43765, 7.6577, 1.48135, 106.5, 'Broccoli'), -- Broccoli
    (@mealId, 168917, 108, 3.96, 1.728, 19.17, 0.783, 90, 'Quinoa'); -- Quinoa


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Turkey and Vegetable Stir-fry with a Small Serving of Noodles', 'Maintain', 'turkey');
SET @mealId = 5;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 171506, 507.5, 68.5, 26, 0, 0, 250, 'Turkey'), -- Turkey
    (@mealId, 169370, 26, 3.58, 0.33, 4.04, 0, 100, 'Soybeans stir-fry'), -- Soybeans stir-fry
    (@mealId, 169253, 125, 13.1, 7.1, 9.4, 0, 100, 'Mushrooms stir-fry'), -- Mushrooms stir-fry
    (@mealId, 169732, 207, 6.81, 3.105, 37.8, 0.6, 150, 'Noodle'); -- Noodle


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('A Small Handful of Mixed Nuts', 'Maintain', 'nuts');
SET @mealId = 6;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 170187, 196.2, 7.6, 19.56, 4.11, 0.783, 30, 'Walnuts'), -- Walnuts
    (@mealId, 170162, 165.9, 5.46, 13.14, 9.06, 2.955, 30, 'Cashews'); -- Cashews


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Carrot and Celery Sticks with Hummus', 'Maintain', 'carrot');
SET @mealId = 7;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 170393, 41, 0.93, 0.24, 9.58, 4.74, 100, 'Carrots'), -- Carrots
    (@mealId, 169988, 21, 1.035, 0.255, 4.455, 2.01, 150, 'Celery'), -- Celery
    (@mealId, 174289, 118.5, 3.89, 8.9, 7.5, 0.31, 50, 'Hummus'); -- Hummus


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Miso Soup with Tofu', 'Maintain', 'soup');
SET @mealId = 8;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172442, 396, 25.6, 12.02, 50.8, 24.8, 200, 'Miso'), -- Miso
    (@mealId, 167722, 47, 8.75, 0.9, 8, 6.2, 50, 'Tofu'); -- Tofu


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Eggs on Toast', 'Maintain', 'cooking_eggs');
SET @mealId = 9;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172186, 286.5, 18.75, 14.205, 1.065, 0.555, 150, 'Eggs'), -- Eggs
    (@mealId, 171705, 80, 1, 7.35, 4.265, 0.33, 50, 'Avocados'), -- Avocados
    (@mealId, 174914, 219.12, 11.14, 3.46, 39.16, 5.61, 76, 'Whole-grain toast'); -- Whole-grain toast


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Soba Noodles and Vegetables', 'Maintain', 'ramen');
SET @mealId = 10;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 168907, 198, 10.12, 0.2, 42.8, 0, 200, 'Soba Noodles'), -- Soba Noodles
    (@mealId, 171705, 80, 1, 7.35, 4.265, 0.33, 50, 'Avocados'), -- Avocados
    (@mealId, 170393, 41, 0.93, 0.24, 9.58, 4.74, 100, 'Carrots'), -- Carrots
    (@mealId, 168411, 60.5, 5.95, 2.6, 4.455, 1.09, 50, 'Edamame'); -- Edamame


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Chia Pudding with Berries and Maple Syrup', 'Maintain', 'strawberry');
SET @mealId = 11;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 170554, 243, 8.25, 15.35, 21.05, 0, 50, 'Chia seeds'), -- Chia seeds
    (@mealId, 174832, 45, 0.12, 2.88, 3.93, 2.43, 300, 'Almond milk'), -- Almond milk
    (@mealId, 170276, 40.5, 0, 0, 10.11, 8.985, 15, 'Maple syrup'), -- Maple syrup
    (@mealId, 167762, 32, 0.67, 0.3, 7.68, 4.89, 100, 'Strawberry'), -- Strawberry
    (@mealId, 171711, 28.5, 0.37, 0.165, 7.25, 4.98, 50, 'Blueberry'); -- Blueberry


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Minestrone Soup with Vegetables and Pasta', 'Maintain', 'soup');
SET @mealId = 12;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 171578, 60, 2.55, 0.795, 12.6, 2.7, 150, 'Chia seeds'), -- Chia seeds
    (@mealId, 168916, 477, 17.46, 4.5, 94.5, 2.55, 300, 'Whole-grain pasta'), -- Whole-grain pasta
    (@mealId, 173515, 72, 2, 1, 16.5, 3.14, 200, 'Vegetables'); -- Vegetables


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Pork Tenderloin with Roasted Vegetables', 'Maintain', 'pork');
SET @mealId = 13;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 167905, 367.5, 65, 9.9, 0, 0, 250, 'Pork Tenderloin'), -- Pork Tenderloin
    (@mealId, 170394, 35, 0.76, 0.18, 8.22, 3.45, 100, 'Cooked Carrots'), -- Cooked Carrots
    (@mealId, 170434, 92, 2.1, 0.15, 21.1, 1.53, 100, 'Potatoes'); -- Potatoes

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Cottage Cheese With Peaches and Honey', 'Maintain', 'cheese');
SET @mealId = 14;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172179, 147, 11.1, 4.3, 3.38, 2.67, 150, 'Cottage Cheese'), -- Cottage Cheese
    (@mealId, 171336, 52, 0.752, 0.264, 11.6, 9.2, 80, 'Peaches'), -- Peaches
    (@mealId, 169640, 60.8, 0.06, 0, 16.5, 16.42, 20, 'Honey'); -- Honey

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Vietnamese Pho', 'Maintain', 'ramen');
SET @mealId = 15;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 174575, 230, 18.6, 3.69, 0.64, 0.29, 200, 'Beef'), -- Beef
    (@mealId, 169732, 138, 4.54, 2.07, 25.2, 0.4, 100, 'Noodles'), -- Noodles
    (@mealId, 169213, 29, 4.2, 0.5, 4.1, 0, 50, 'Bean Sprouts'); -- Bean Sprouts



-- Bulk meals

-- Grilled Chicken on Quinoa
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Grilled Chicken on Quinoa', 'Bulk', 'chicken');
SET @mealId = 16;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 171534, 302, 61, 6.34, 0, 0, 200, 'Chicken'), -- Chicken
    (@mealId, 169967, 52.5, 3.57, 0.615, 10.77, 2.085, 150, 'Broccoli'), -- Broccoli
    (@mealId, 168917, 180, 6.6, 2.88, 31.95, 1.305, 150, 'Quinoa'); -- Quinoa

-- Beef and Rice Bowl
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Beef and Rice Bowl', 'Bulk', 'beef');
SET @mealId = 17;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 174756, 350, 59, 11, 0, 0, 200, 'Lean Beef'), -- Lean Beef
    (@mealId, 169704, 184.5, 4.11, 1.455, 38.4, 0.36, 150, 'Brown Rice'), -- Brown Rice
    (@mealId, 170472, 65, 2.86, 0.15, 13.1, 3.12, 100, 'Mixed Veges'); -- Mixed Veges

-- Smoked Salmon with Kumara
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Smoked Salmon with Kumara', 'Bulk', 'fish');
SET @mealId = 18;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 169813, 517.5, 90.9, 17.1, 0, 0, 150, 'Smoked Salmon'), -- Smoked Salmon
    (@mealId, 170134, 180, 4.02, 0.3, 41.4, 129.6, 200, 'Kumara'), -- Kumara
    (@mealId, 168390, 22, 2.4, 0.22, 4.11, 1.3, 100, 'Asparagus'); -- Asparagus

-- Turkey, Avocado, & Mayo Wrap
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Turkey, Avocado, & Mayo Wrap', 'Bulk', 'burrito');
SET @mealId = 19;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172941, 159, 22.2, 5.655, 3.3, 1.365, 150, 'Turkey'), -- Turkey
    (@mealId, 171706, 50.19, 0.588, 4.62, 0.498, 0.018, 30, 'Avocado'), -- Avocado
    (@mealId, 168440, 1.4, 0.15, 0.02, 0.18, 0.15, 10, 'Spinach'), -- Spinach
    (@mealId, 173594, 47.6, 0.074, 4.44, 0.41, 0.712, 20, 'Aioli'), -- Aioli
    (@mealId, 174081, 15.1, 4.88, 4.88, 2.23, 22.95, 50, 'Tortilla'); -- Tortilla

-- Tofu and Sesame Stir-Fry
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Tofu and Sesame Stir-Fry', 'Bulk', 'stir-fry');
SET @mealId = 20;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172451, 101, 37.6, 40.4, 3.24, 17.72, 200, 'Tofu'), -- Tofu
    (@mealId, 169704, 123, 2.74, 0.97, 25.6, 0.24, 100, 'Brown Rice'), -- Brown Rice
    (@mealId, 170472, 97.5, 4.29, 0.225, 19.65, 4.68, 150, 'Mixed Veges'), -- Mixed Veges
    (@mealId, 170150, 28.65, 0.885, 2.485, 2.225, 1.17, 5, 'Sesame Seeds'); -- Sesame Seeds

-- Spaghetti Bolognese
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Spaghetti Bolognese', 'Bulk', 'spaghetti');
SET @mealId = 21;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 167680, 670, 32.1, 19.9, 90.9, 10.1, 554, 'Spaghetti'); -- Spaghetti

-- Oatmeal with Nuts and Bananas
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Oatmeal with Nuts and Bananas', 'Bulk', 'banana');
SET @mealId = 22;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172989, 371, 13.7, 6.87, 68.2, 1.42, 100, 'Oats'), -- Oats
    (@mealId, 169428, 182.1, 5.37, 15, 15.03, 1.056, 30, 'Mixed Nuts'), -- Mixed Nuts
    (@mealId, 173944, 29.4, 0.222, 0.072, 6.9, 4.74, 30, 'Banana'), -- Banana
    (@mealId, 170873, 107.5, 8.7, 2.425, 12.425, 0, 250, 'Whole Milk'); -- Whole Milk

-- Protein Yoghurt with Granola and Banana
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Protein Yoghurt with Granola and Banana', 'Bulk', 'banana');
SET @mealId = 23;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 171286, 210, 9.72, 2.82, 18.6, 1.03, 200, 'Yoghurt'), -- Yoghurt
    (@mealId, 173944, 89, 1.09, 0.33, 22.8, 12.2, 100, 'Banana'), -- Banana
    (@mealId, 171646, 146.7, 4.11, 7.29, 16.17, 5.94, 30, 'Granola'); -- Granola

-- Pulled Pork with Baked potatoes
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Pulled Pork with Baked potatoes', 'Bulk', 'pork');
SET @mealId = 24;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 173344, 978, 27.4, 40.6, 107.8, 39.6, 200, 'Pulled Pork'), -- Pulled pork
    (@mealId, 170112, 279, 5.88, 0.3, 64.8, 5.1, 300, 'Roast Potatoes'); -- Roast Potatoes

-- High Protein Big Mac with Fries and a Strawberry Shake
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('High Protein Big Mac with Fries and a Strawberry Shake', 'Bulk', 'burger');
SET @mealId = 25;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 170720, 563, 25.8, 32.8, 44, 8.69, 219, 'Big Mac'), -- Big Mac
    (@mealId, 170721, 497.42, 5.25, 23.87, 65.804, 0.3234, 154, 'Fries'), -- Fries
    (@mealId, 172062, 275.32, 5.5456, 6.861, 48.814, 43.922, 174, 'Strawberry Shake'); -- Strawberry Shake

-- Sirloin Steak with Garlic Bread
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Sirloin Steak with Garlic Bread', 'Bulk', 'steak');
SET @mealId = 26;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 169016, 390, 59.6, 16.96, 0, 1.1, 200, 'Steak'), -- Steak
    (@mealId, 173410, 71.7, 0.085, 8.11, 0.006, 0.006, 10, 'Butter'), -- Butter
    (@mealId, 167939, 560, 13.376, 26.56, 66.72, 5.904, 160, 'Garlic Bread'); -- Garlic Bread

-- Tuna and Ricotta Salad
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Tuna and Ricotta Salad', 'Bulk', 'fish');
SET @mealId = 27;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 175160, 561, 48, 27.78, 28.23, 0, 300, 'Tuna Salad'), -- Tuna Salad
    (@mealId, 170851, 150, 7.54, 10.2, 7.27, 0.27, 100, 'Ricotta'); -- Ricotta

-- Protein Shake
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Protein Shake', 'Bulk', 'milkshake');
SET @mealId = 28;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 172989, 371, 13.7, 6.87, 68.2, 1.42, 100, 'Oats'), -- Oats
    (@mealId, 173944, 178, 1.09, 0.33, 45.6, 24.4, 200, 'Banana'), -- Banana
    (@mealId, 170873, 43, 3.48, 0.97, 4.97, 0, 100, 'Whole Milk'), -- Whole Milk
    (@mealId, 171286, 157.5, 7.29, 2.115, 27.9, 0, 150, 'Yoghurt'), -- Yoghurt
    (@mealId, 173180, 105.6, 23.43, 0.468, 1.875, 0, 30, 'Protein Powder'), -- Protein Powder
    (@mealId, 170554, 97.2, 3.3, 6.14, 8.42, 0, 20, 'Chia seeds'); -- Chia seeds

-- Protein Pancakes with Maple Syrup
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Protein Pancakes with Maple Syrup', 'Bulk', 'pancake');
SET @mealId = 29;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 175047, 908, 27.2, 37.2, 114.8, 0, 400, 'Pancakes'), -- Pancakes
    (@mealId, 170276, 810, 0, 0, 202.2, 179.7, 300, 'Maple Syrup'); -- Maple Syrup

-- Vegan Chickpea Curry
INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('KFC', 'Bulk', 'chicken');
SET @mealId = 30;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES
    (@mealId, 170744, 490, 46.4, 27.8, 13.3, 0, 212, 'KFC Original chicken'), -- KFC Original chicken
    (@mealId, 170349, 222, 16.7, 14.3, 6.45, 0, 81, 'KFC crispy chicken'); -- KFC crispy chicken

-- Recommended Cutting Meals

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Grilled chicken breast with steamed broccoli', 'Cut', 'chicken');
SET @mealId = 31;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 171534, 302, 61, 6.34, 0, 0, 200, 'Chicken'), -- Chicken
       (@mealId, 169967, 37.275, 2.5317, 0.43765, 7.6577, 1.48135, 106.5, 'Broccoli'); -- Broccoli

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Salmon with a side of mixed greens', 'Cut', 'fish');
SET @mealId = 32;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 173688, 206, 22.1, 12.4, 0, 0, 100, 'Salmon'), -- Salmon
       (@mealId, 169085, 37, 1.4, 0.2, 7.31, 2.41, 100, 'Mixed Greens'); -- Mixed Greens

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Mushroom and Spinach Omelette', 'Cut', 'cooking_eggs');
SET @mealId = 33;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 172185, 154, 10.6, 11.7, 0.64, 0.31, 100, 'Omelette'), -- Omelette
       (@mealId, 169400, 11.5, 1.49, 0.39, 1.36, 0, 50, 'Spinach'), -- Spinach
       (@mealId, 169242, 17, 1.12, 0.25, 3.395, 1.19, 50, 'Mushrooms'); -- Mushrooms

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Greek Salad with Chicken', 'Cut', 'salad');
SET @mealId = 34;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 171534, 151, 30.5, 3.17, 0, 0, 100, 'Chicken'), -- Chicken
       (@mealId, 168409, 7.8, 0.338, 0.057, 1.89, 0.868, 52, 'Cucumber'), -- Cucumber
       (@mealId, 173420, 101, 5.4, 8.17, 1.47, 0, 38, 'Feta Cheese'), -- Feta Cheese
       (@mealId, 169094, 19.48, 0.142, 1.832, 1.014, 0, 16.8, 'Olives'); -- Olives

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Steak and Spinach Salad', 'Cut', 'salad');
SET @mealId = 35;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 168620, 390.5, 49.5, 19.8, 0, 0, 180, 'Steak'), -- Steak
       (@mealId, 169400, 11.5, 1.49, 0.39, 1.36, 0, 50, 'Spinach'), -- Spinach
       (@mealId, 170502, 25.3, 1.83, 0.3, 5.02, 0, 158, 'Tomato'); -- Tomato

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Grilled Chicken with Brown Rice', 'Cut', 'chicken');
SET @mealId = 36;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 171534, 151, 30.5, 3.17, 0, 0, 100, 'Chicken'), -- Chicken
       (@mealId, 169703, 367, 7.54, 3.2, 76.2, 0.66, 100, 'Brown Rice'); -- Brown Rice


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Grilled Steak with White Rice', 'Cut', 'steak');
SET @mealId = 37;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 168620, 390.5, 49.5, 19.8, 0, 0, 180, 'Steak'), -- Steak
       (@mealId, 169756, 365, 7.13, 0.66, 80, 0.12, 100, 'White Rice'); -- White Rice

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Pork on Rice with Mixed Greens', 'Cut', 'pork');
SET @mealId = 38;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 168380, 265, 32.4, 14.5, 1.09, 0, 131, 'Pork chop'), -- Pork chop
       (@mealId, 169756, 365, 7.13, 0.66, 80, 0.12, 100, 'White Rice'), -- White Rice
       (@mealId, 169085, 37, 1.4, 0.2, 7.31, 2.41, 100, 'Mixed Greens'); -- Mixed Greens

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Beef and Tomato Salad', 'Cut', 'steak');
SET @mealId = 39;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 174756, 175, 29.5, 5.46, 0, 0, 100, 'Lean Ground Beef'), -- Lean Ground Beef
       (@mealId, 170502, 25.3, 1.83, 0.3, 5.02, 0, 158, 'Tomato'), -- Tomato
       (@mealId, 169249, 5.4, 0.49, 0.054, 1.03, 0.281, 36, 'Shredded Lettuce'); -- Shredded Lettuce

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Beef and Bell Pepper Stir-Fry', 'Cut', 'beef');
SET @mealId = 40;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 174756, 175, 29.5, 5.46, 0, 0, 100, 'Lean Ground Beef'), -- Lean Ground Beef
       (@mealId, 168550, 141, 1.1, 13.6, 6.96, 4.54, 106, 'Bell Pepper'), -- Bell Pepper
       (@mealId, 170000, 44, 1.21, 0.11, 10.3, 4.66, 110, 'Onion'), -- Onion
       (@mealId, 169230, 13.4, 0.572, 0.045, 2.98, 0.09, 9, 'Garlic'); -- Garlic

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Beef and Cabbage Stir-Fry', 'Cut', 'beef');
SET @mealId = 41;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 174756, 175, 29.5, 5.46, 0, 0, 100, 'Lean Ground Beef'), -- Lean Ground Beef
       (@mealId, 168516, 24, 1.8, 0.09, 5.41, 0, 100, 'Cabbage'), -- Cabbage
       (@mealId, 170000, 44, 1.21, 0.11, 10.3, 4.66, 110, 'Onion'), -- Onion
       (@mealId, 169230, 13.4, 0.572, 0.045, 2.98, 0.09, 9, 'Garlic'), -- Garlic
       (@mealId, 169967, 37.275, 2.5317, 0.43765, 7.6577, 1.48135, 106.5, 'Broccoli'); -- Broccoli

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Steak and Spinach Omelette', 'Cut', 'steak');
SET @mealId = 42;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 168620, 390.5, 49.5, 19.8, 0, 0, 180, 'Steak'), -- Steak
       (@mealId, 169400, 11.5, 1.49, 0.39, 1.36, 0, 50, 'Spinach'), -- Spinach
       (@mealId, 172185, 154, 10.6, 11.7, 0.64, 0.31, 100, 'Omelette'); -- Omelette


INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Mango Protein shake', 'Cut', 'mango');
SET @mealId = 43;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 173180, 113, 25, 0.499, 2, 0, 32, 'Protein powder'), -- Protein powder
       (@mealId, 170873, 105, 8.53, 2.38, 12.2, 0, 245, 'Milk'), -- Milk
       (@mealId, 169910, 99, 1.35, 0.627, 24.8, 22.6, 135, 'Mango'), -- Mango
       (@mealId, 171286, 238, 11, 3.2, 42.2, 0, 227, 'Protein Yogurt'); -- Protein Yogurt

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Banana Protein shake', 'Cut', 'banana');
SET @mealId = 44;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 173180, 113, 25, 0.499, 2, 0, 32, 'Protein powder'), -- Protein powder
       (@mealId, 170873, 105, 8.53, 2.38, 12.2, 0, 245, 'Milk'), -- Milk
       (@mealId, 173944, 121, 1.48, 0.449, 31, 16.6, 136, 'Banana'), -- Banana
       (@mealId, 171286, 238, 11, 3.2, 42.2, 0, 227, 'Protein Yogurt'); -- Protein Yogurt

INSERT INTO `recommended_meal` (name, calorie_preference, icon)
VALUES ('Protein Oats', 'Cut', 'milk');
SET @mealId = 45;
INSERT INTO `food` (meal_id, fdc_id, calories, protein, fat, carbs, sugar, portion, name)
VALUES (@mealId, 173180, 113, 25, 0.499, 2, 0, 32, 'Protein powder'), -- Protein powder
       (@mealId, 170873, 52.5, 4.265, 1.19, 6.1, 0, 122.5, 'Milk'), -- Milk
       (@mealId, 168873, 87.6, 7.03, 1.88, 25, 0, 219, 'Oats'); -- Oats
