-- ----------------------------------------------------------------
-- Create Schema and User
--
--   file during your MySQL Setup.  You MUST be logged in as MySQL superuser
--   account 'root@'localhost' for these statements to work:
DROP SCHEMA IF EXISTS `data8319`;
CREATE SCHEMA IF NOT EXISTS `data8319` DEFAULT CHARACTER SET utf8mb4;
DROP USER IF EXISTS `cst8319`@`localhost`;
CREATE USER IF NOT EXISTS 'cst8319'@'localhost' IDENTIFIED BY '8319';
GRANT ALL ON `data8319`.* TO 'cst8319'@'localhost';
--
-- ----------------------------------------------------------------
select * from `data8319`.`specialty`;
select * from `data8319`.`interview`;
SHOW DATABASES;

-- -----------------------------------------------------
-- Create Table `databank`.`interview`
-- -----------------------------------------------------
USE `data8319`;
CREATE TABLE IF NOT EXISTS `data8319`.`interview`(
  `id` INT NOT NULL AUTO_INCREMENT,
  `last_name` VARCHAR(50) NOT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NULL,
  `phone` VARCHAR(10) NULL,
  `specialty` VARCHAR(45) NULL,
   `content` VARCHAR(400) NULL,
  `summary` VARCHAR(200) NULL,
  `created` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `data8319`.`specialty`(
  `name` VARCHAR(45) NULL)
ENGINE = InnoDB;

INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Anatomical Pathology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Anesthesiology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Cardiology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Cardiovascular/Thoracic Surgery');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Clinical Immunology/Allergy');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Critical Care Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Dermatology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Diagnostic Radiology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Emergency Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Endocrinology and Metabolism');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Family Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Gastroenterology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('General Internal Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('General Surgery');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('General/Clinical Pathology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Geriatric Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Hematology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Medical Biochemistry');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Medical Genetics');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Medical Microbiology and Infectious Diseases');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Medical Oncology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Nephrology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Neurology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Neurosurgery');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Nuclear Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Obstetrics/Gynecology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Occupational Medicine');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Ophthalmology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Orthopedic Surgery');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Otolaryngology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Pediatrics');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Physical Medicine and Rehabilitation (PM & R)');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Plastic Surgery');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Psychiatry');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Public Health and Preventive Medicine (PhPm)');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Radiation Oncology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Respirology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Rheumatology');
INSERT INTO `data8319`.`specialty`(`name`) VALUES ('Rheumatology');

