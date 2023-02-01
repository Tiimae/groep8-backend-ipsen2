# groep8-backend

Dit is de instalatie manier om de springboot applicatie van de CGI werkplekken reserverings app op te zetten op een server zo dat we die kunnen bereiken.

## Vooraf

Zorg ervoor dat de volgende dingen staan geinstalleerd op de server

- Java 17
- maven

## Applicatie starten
1. Zorg ervoor dat je het project cloned op de server. Doe dit ddor de command
```git clone https://github.com/Tiimae/groep8-backend-ipsen2.git```
2. Daarna moet je de application.properties aanpassen. Die kan je vinden in de map ``/src/main/resources/application.properties``
3. Vul de Appliction.properties in

```
# Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url=jdbc:mariadb://<host van de database>:<port van de database>/<database naam op de server>
spring.datasource.username= <username van de database>
spring.datasource.password= <wachtwoord die bij username hoort>

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MariaDB103Dialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update

# Spring boot email config
spring.mail.host=<mail host>
spring.mail.port=<mail port>
spring.mail.username=<username mail server>
spring.mail.password=<wachtwoord mail server>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
shared_secret=<Shared secret die gedeeld is met de frondend>
jwt_secret=<een random code die geheim moet blijven (Je hand over het toetsenbord kan)>
```

4. Zet een nieuwe screen op op je linux server met de command ```screen -S <Screen naam>```
5. Ga in de root map zitten van het project met de command ``cd /<map naam>``
6. Doe de comando ``mvn spring-boot:run`` om de applicatie te starten