# ************************************************************
# Sequel Pro SQL dump
# Version 4529
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.16)
# Database: ndrtool
# Generation Time: 2017-08-09 03:05:38 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table nfr_catalog_thesis
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nfr_catalog_thesis`;

CREATE TABLE `nfr_catalog_thesis` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `timestamp` bigint(20) DEFAULT NULL,
  `comment` varchar(300) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `element_names` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `nfr_catalog_thesis` WRITE;
/*!40000 ALTER TABLE `nfr_catalog_thesis` DISABLE KEYS */;

INSERT INTO `nfr_catalog_thesis` (`id`, `name`, `timestamp`, `comment`, `version`, `element_names`)
VALUES
	(1,'Security',1496510528716,'StarUML XMI',1,'External-confidentiality;Biometrics;Encrypted-password;One-sided;Identification;Card-Key;Security;Cost;Audit-trail-based;Simple;Fingerprint-verification;Voice-recognition;Accuracy;Completeness;Mutual-multi-layer-password;Password;Availability;Multi-layer;Internal-confidentiality;Face-recognition;Access-rule-validation;Multi-layer-password;Confidentiality;Single;Authentication;Mutual;Access-authorization;Performance;Usability;Integrity'),
	(2,'Traceability',1496510617094,'StarUML XMI',1,'Make-a-historic-of-places;Performance[time];Links-embeded-in-each-step;Store-last-name-change;Security;Store-all-states;Traceability;Trace-changes;Trace-creation;Trace-what-thing-composes-another-thing;Trace-who-changed;Have-steps-linked;Trace-changes[time];Store-time-of-the-last-moving;Have-information-about-who-changed;Safety;Trace-destruction;Store-all-the-names;Store-information-about-changes;Store-time-of-the-last-change;Trace-time;Store-information-about-moving;Store-Informationabout-parts-to-later-use;Traceability[Things];Store-all-the-times-thing-has-moved;Trace-places;Trace-whole-part-relationship;Performance[space];Store-the-last-location;Trace-what-changed;Store-last-state;Trace-moving;Have-changes-traced;Store-all-the-times-thing-has-changed;Trace-when-changed;Reliability;Use-a-table-of-links;Traceability[Process]'),
	(3,'Transparency',1496510629716,'StarUML XMI',1,'Transparency;Data-Collection-and-Use;Detailing;Adaptability;Consistency;Informativeness;Auditing;Operability;Traceability;Accuracy;Friendliness;Intuitiveness;Completeness;Decomposability;Controllability;Uniformity;Availability;Exposure-to-Personal-Information;Verifiability;Dependability;Anonymity;Portability;Correctness;Conciseness;Clarity;Composability;Validity;Comparability;Storage;Publicity;Accessbility;Externability;Cloud;Accountability;Understandability;Performance;Usability;Current;Simplicity;Integrity'),
	(4,'Usability',1496510677262,'StarUML XMI',1,'Assure-Applicability-of-Information;Availability;Use-Online;Assure-Trustworthiness-of-Information;Assure-Comprehension-of-Information;Provide-Appropriate-Information;Guarantee-Constant-Access;Use-GUI-Cognitive-Aspects;Portability;Improve-Cognition;Assure-Timeliness-of-Information;Security;Cost;Provide-Appropriate-Level-of-Information;Use-Tablets;Provide-Appropriate-Amount-of-Information;Provide-Continuous-Access;Usefulness;Conduct-Usability-Testing;Use-Offline;Use-Desktop;Use-PDA;Performance;Usability'),
	(5,'Privacy',1496510691392,'StarUML XMI',1,'Restrict-or-Eliminate-automatic-disclosure-and-collection-of-personal-data;Profiling;Negotiate-online;Let-users-decide-access-to-data;Use-council-or-europe-model-contract;Data-share-practice;Use-traditional-mechanisms;Security;PI-sharing-by-multiple-systems;Allow-individual-participation;Use-revised-ICC-model-contract;Use-anonymizing-intermediary;DB-Merger;Privacy;Post-privacy-policies;Educate-companies;Data-minimization;Use-anonymous-remaileirs;Societal-norms;Present-Terms-and-Conditions;Unauthorized-data-mining;Provide-users-data-disclosure-options;Prevent-receipt-of-unsolicited-email;Use-smart-cards;PI-protection;Limit-use-and-disclosure-of-data;Use-optional-datafields-and-click-box-choices;Data-will-not-be-shared-with-third-parties;Use-digital-coins;Have-policies-clear;Use-of-contextual-metadata;Ensure-complaint-resolution-procedures;Centralization-of-PI;Accountability;Opt-out;Profiling-with-errors;Performance;Use-telephone;Control-use-of-data-after-collection;Ensure-compliance-with-privacy-standards;Use-digital-certificate;Use-of-different-semantics;Data-Collection-and-Use;Use-online-procedure;Block-transfer-of-automatically-generated-data;Data-Collection;Use-regular-mail;Data-mining;Data-will-be-shared-with-third-parties;Use-public-key-cryptography;Enforce-privacy-principles;Subjective-profiling;Use-anonymous-profiles;Inform-users-about-Online-Privacy-Policies;Ask-user-preference-online;Reduce-need-for-personal-data-disclosure;Provide-access-to-personal-data;Minimize-disclosure-and-collection-of-personal-data;Hide-directory-structure;Use-intermediary-sites;Video-surveillance-and-profiling;Use-anonymous-payment-system;Opt-out-of-anonymous-profile;Data-caveats;Educate-users-and-private-sector;Data-Use;Data-will-be-shared-with-authorized-third-parties;Use-digital-labels;Identify-potential-recipient-of-data;Manage-cookies;Usability;Educate-users;Openness-of-purpose;Protecting-Privacy-through-transborder-data-flow-contracts;Lack-of-clarity-of-data-handling-process');

/*!40000 ALTER TABLE `nfr_catalog_thesis` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table play_evolutions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `play_evolutions`;

CREATE TABLE `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `apply_script` text,
  `revert_script` text,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `play_evolutions` WRITE;
/*!40000 ALTER TABLE `play_evolutions` DISABLE KEYS */;

INSERT INTO `play_evolutions` (`id`, `hash`, `applied_at`, `apply_script`, `revert_script`, `state`, `last_problem`)
VALUES
	(1,'d8196e1865ffdf13e7ba16ae7faace92732762cc','2017-06-03 13:17:24','create table user (\nid                        bigint auto_increment not null,\nusername                  varchar(255),\npassword                  varchar(255),\ntimestamp                 bigint,\nconstraint pk_user primary key (id))\n;','SET FOREIGN_KEY_CHECKS=0;\n\ndrop table user;\n\nSET FOREIGN_KEY_CHECKS=1;','applied','');

/*!40000 ALTER TABLE `play_evolutions` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `timestamp` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `username`, `password`, `timestamp`)
VALUES
	(1,'admin','nimda40',1499828587809);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
