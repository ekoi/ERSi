SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `ersydb` ;
CREATE SCHEMA IF NOT EXISTS `ersydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `ersydb` ;

DROP TABLE IF EXISTS ersydb.mining_process ;

CREATE  TABLE IF NOT EXISTS ersydb.mining_process (
  mpid INT NOT NULL AUTO_INCREMENT ,
  method_name VARCHAR(50) NOT NULL UNIQUE,
  execution_date TIMESTAMP,
  PRIMARY KEY (mpid) )
ENGINE = InnoDB;

DROP TABLE IF EXISTS ersydb.pid_relevancy ;

CREATE TABLE pid_relevancy (
  id INT(20)  NOT NULL AUTO_INCREMENT,
  mpid INT NOT NULL,
  pid VARCHAR(250)  NOT NULL,
  pid_rel VARCHAR(250)  NOT NULL,
  distance DOUBLE NOT NULL,
  rating INT NOT NULL DEFAULT 0, 
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (mpid)
    REFERENCES ersydb.mining_process (mpid)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
CREATE INDEX index_pid ON pid_relevancy(pid);
CREATE INDEX index_pid_rel ON pid_relevancy(pid_rel);




USE `ersydb` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



-- -----------------------------------------------------
-- Table `ersydb`.`employee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ersydb`.`cluster` ;

CREATE  TABLE IF NOT EXISTS `ersydb`.`cluster` (
  `CLUSTER_ID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  PRIMARY KEY (`CLUSTER_ID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ersydb`.`phone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ersydb`.`dataset` ;

CREATE  TABLE IF NOT EXISTS `ersydb`.`dataset` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `CLUSTER_ID` INT NULL ,
  `PID` VARCHAR(50) NULL ,
  `Distance` DOUBLE NULL ,
  `Rating` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_dataset_cluster_idx` (`CLUSTER_ID` ASC) ,
  CONSTRAINT `fk_dataset_cluster`
    FOREIGN KEY (`CLUSTER_ID` )
    REFERENCES `ersydb`.`cluster` (`CLUSTER_ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


DROP TABLE IF EXISTS ersydb.mining_process ;

CREATE  TABLE IF NOT EXISTS ersydb.mining_process (
  mpid INT NOT NULL AUTO_INCREMENT ,
  method_name VARCHAR(50) NOT NULL UNIQUE,
  execution_date TIMESTAMP,
  PRIMARY KEY (mpid) )
ENGINE = InnoDB;

DROP TABLE IF EXISTS ersydb.pid_relevancy ;

CREATE TABLE pid_relevancy (
  id INT(20)  NOT NULL AUTO_INCREMENT,
  mpid INT NOT NULL,
  pid VARCHAR(250)  NOT NULL,
  pid_rel VARCHAR(250)  NOT NULL,
  distance DOUBLE NOT NULL,
  rating INT NOT NULL DEFAULT 0, 
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (mpid)
    REFERENCES ersydb.mining_process (mpid)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
CREATE INDEX index_pid ON pid_relevancy(pid);
CREATE INDEX index_pid_rel ON pid_relevancy(pid_rel);




USE `ersydb` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
