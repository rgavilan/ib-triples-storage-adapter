#Author: Izertis
#Keywords Summary : Triples Storage Delta Controller
Feature: Versions Delta communications to triple Storage 

  Scenario: Triples Storage recives a versions Delta
    Given delta controller recives versions to process
    Then delta service recive versions and process file data