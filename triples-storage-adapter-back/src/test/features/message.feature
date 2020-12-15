#Author: Izertis
#Keywords Summary : Triples Storage Message Controller
Feature: Messages communications to triple Storage 

  Scenario: Triples Storage recives a message to insert in trellis
    Given message controller recives a message to insert
    Then service process message and insert on trellis
