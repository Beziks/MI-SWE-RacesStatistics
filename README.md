MI-SWE-RacesStatistics
======================
School project for MI-SWE.

"Dotazovani nad vysledky bezeckych zavodu"
------------------------------------------

SPARQL querying over results of run competitions.

1) ADMIN TAB

     - Upload score sheets in defined html format.

     - Parse score sheets -> save to DB (JPA entities)

     - Generate RDF file (will be saved in DB)

2) QUERYING TAB

     - Write/select SPARQL query

     - Search...

Technologies
------------
JavaEE - EJB, JPA, CDI

Maven

JenaAPI

Vaadin (GUI, based on GWT)


Deployment
----------
Unfortunately, I was not able to put it on some public server hosting, but I will try...

Use JBOSS 7.1.1.

Datasource needed to connect to db - see WEB-INF/RacesStatistics-ds.xml


<connection-url>jdbc:mysql://localhost:3306/races_statistics</connection-url>

<driver>mysql-connector-java-5.1.22.jar</driver>

<security>

	<user-name>root</user-name>

	<password>123456</password>

</security>


Final RDF can be about 3 MB -> configure DB accordingly.
