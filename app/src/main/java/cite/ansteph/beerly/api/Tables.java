package cite.ansteph.beerly.api;

/**
 * Created by loicstephan on 2017/10/19.
 */

public interface Tables {

  String  ESTTYPE_Table  =    "EstType";
    String  ESTPROFILE_Table= "EstProfile";
    String  BEERLOVERS_Table = "BeerLovers";
    String  BEER_Table = "Beer";
    String BEERSIZE_Table = "BeerSize";
    String ESTACCOUNT_Table= "EstAccount" ;
    String  PREFERENCE_Table = "Preference";
    String  PROMOTION_Table = "Promotion";

    String   ESTABLISMENT_Table = "Establisment";




}





/*
* CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Establisment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Establisment` (
  `est_Id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `liquorLicence` VARCHAR(45) NULL,
  `HSLicence` VARCHAR(45) NULL,
  `dateLastInspection` VARCHAR(45) NULL,
  `contactPerson` VARCHAR(45) NULL,
  `contactNumber` VARCHAR(45) NULL,
  `url` VARCHAR(45) NULL,
  `geotag` VARCHAR(45) NULL,
  `dateCreated` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  PRIMARY KEY (`est_Id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`EstType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`EstType` (
  `id` INT NOT NULL,
  `typeName` VARCHAR(45) NULL,
  `typeDesc` VARCHAR(45) NULL,
  `Establisment_est_Id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_EstType_Establisment1_idx` (`Establisment_est_Id` ASC),
  CONSTRAINT `fk_EstType_Establisment1`
    FOREIGN KEY (`Establisment_est_Id`)
    REFERENCES `mydb`.`Establisment` (`est_Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`EstProfile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`EstProfile` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `synopsis` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `geotag` VARCHAR(45) NULL,
  `speciality` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  `Establisment_est_Id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_EstProfile_Establisment1_idx` (`Establisment_est_Id` ASC),
  CONSTRAINT `fk_EstProfile_Establisment1`
    FOREIGN KEY (`Establisment_est_Id`)
    REFERENCES `mydb`.`Establisment` (`est_Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`BeerLovers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`BeerLovers` (
  `uuid` INT NOT NULL,
  `dateofbirth` VARCHAR(45) NULL,
  `dateCreated` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Beer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Beer` (
  `beerID` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `desc` VARCHAR(45) NULL,
  `vendor` VARCHAR(45) NULL,
  `percentage` VARCHAR(45) NULL,
  PRIMARY KEY (`beerID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`BeerSize`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`BeerSize` (
  `id` INT NOT NULL,
  `volume` VARCHAR(45) NULL,
  `Beer_beerID` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_BeerSize_Beer1_idx` (`Beer_beerID` ASC),
  CONSTRAINT `fk_BeerSize_Beer1`
    FOREIGN KEY (`Beer_beerID`)
    REFERENCES `mydb`.`Beer` (`beerID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`EstAccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`EstAccount` (
  `id` INT NOT NULL,
  `dateOpened` VARCHAR(45) NULL,
  `balance` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  `Establisment_est_Id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_EstAccount_Establisment1_idx` (`Establisment_est_Id` ASC),
  CONSTRAINT `fk_EstAccount_Establisment1`
    FOREIGN KEY (`Establisment_est_Id`)
    REFERENCES `mydb`.`Establisment` (`est_Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Preference`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Preference` (
  `logid` INT NOT NULL,
  `preferenceNumber` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  `BeerLovers_uuid` INT NOT NULL,
  `Beer_beerID` INT NOT NULL,
  PRIMARY KEY (`logid`),
  INDEX `fk_Preference_BeerLovers_idx` (`BeerLovers_uuid` ASC),
  INDEX `fk_Preference_Beer1_idx` (`Beer_beerID` ASC),
  CONSTRAINT `fk_Preference_BeerLovers`
    FOREIGN KEY (`BeerLovers_uuid`)
    REFERENCES `mydb`.`BeerLovers` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Preference_Beer1`
    FOREIGN KEY (`Beer_beerID`)
    REFERENCES `mydb`.`Beer` (`beerID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Menu` (
  `logid` INT NOT NULL,
  `dateCreated` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  `normalPrice` VARCHAR(45) NULL,
  `Beer_beerID` INT NOT NULL,
  `Establisment_est_Id` INT NOT NULL,
  PRIMARY KEY (`logid`),
  INDEX `fk_Menu_Beer1_idx` (`Beer_beerID` ASC),
  INDEX `fk_Menu_Establisment1_idx` (`Establisment_est_Id` ASC),
  CONSTRAINT `fk_Menu_Beer1`
    FOREIGN KEY (`Beer_beerID`)
    REFERENCES `mydb`.`Beer` (`beerID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Menu_Establisment1`
    FOREIGN KEY (`Establisment_est_Id`)
    REFERENCES `mydb`.`Establisment` (`est_Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Promotion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Promotion` (
  `logid` INT NOT NULL,
  `title` VARCHAR(45) NULL,
  `dateOn` VARCHAR(45) NULL,
  `dateOff` VARCHAR(45) NULL,
  `timeOn` VARCHAR(45) NULL,
  `timeOff` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `lastUpdated` VARCHAR(45) NULL,
  `dateCreated` VARCHAR(45) NULL,
  `price` VARCHAR(45) NULL,
  `Establisment_est_Id` INT NOT NULL,
  `Beer_beerID` INT NOT NULL,
  PRIMARY KEY (`logid`),
  INDEX `fk_Promotion_Establisment1_idx` (`Establisment_est_Id` ASC),
  INDEX `fk_Promotion_Beer1_idx` (`Beer_beerID` ASC),
  CONSTRAINT `fk_Promotion_Establisment1`
    FOREIGN KEY (`Establisment_est_Id`)
    REFERENCES `mydb`.`Establisment` (`est_Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Promotion_Beer1`
    FOREIGN KEY (`Beer_beerID`)
    REFERENCES `mydb`.`Beer` (`beerID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

*
*
* */