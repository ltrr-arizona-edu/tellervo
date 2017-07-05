--
-- PostgreSQL database dump
--
-- Creates the admin user and basic groups with the default security permissions

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;
SELECT pg_catalog.setval('tblsecuritygroup_securitygroupid_seq', 6, true);
SELECT pg_catalog.setval('tblsecurityuser_securityuserid_seq', 1, true);
SELECT pg_catalog.setval('tblsecurityusermembership_tblsecurityusermembershipid_seq', 1, true);
SELECT pg_catalog.setval('tblsecuritydefault_securitydefaultid_seq', 9, true);

INSERT INTO tblsecuritygroup VALUES (1, 'Administrator', 'Tellervo super users', true);
INSERT INTO tblsecuritygroup VALUES (2, 'Staff', 'Dendro lab staff and faculty', true);
INSERT INTO tblsecuritygroup VALUES (3, 'Students', 'Current students', true);
INSERT INTO tblsecuritygroup VALUES (6, 'Read only', 'Read only userse', true);
INSERT INTO tblsecurityuser VALUES (1, 'admin', 'bd38976c388a4f04381b82a7d6c62ae5', 'Admin', 'user', true);
INSERT INTO tblsecurityusermembership VALUES (1, 1, 1);
INSERT INTO tblsecuritydefault VALUES (1, 2, 2);
INSERT INTO tblsecuritydefault VALUES (2, 2, 3);
INSERT INTO tblsecuritydefault VALUES (3, 2, 4);
INSERT INTO tblsecuritydefault VALUES (4, 2, 5);
INSERT INTO tblsecuritydefault VALUES (5, 3, 2);
INSERT INTO tblsecuritydefault VALUES (6, 3, 3);
INSERT INTO tblsecuritydefault VALUES (7, 3, 4);
INSERT INTO tblsecuritydefault VALUES (8, 3, 5);
INSERT INTO tblsecuritydefault VALUES (9, 6, 2);
