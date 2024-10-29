# Code Repo for Baccarat Electronic Table Live

## Tools Used
IntelliJ IDEA 2022
Scala Plugin
SBT 1.4.9
SBT Plugin - Play 2.8.4
JDK 11(e.g. Liberica JDK 11.)


## CI/CD Pipeline
GitHub Actions for continuous integration and deployment.[YTC]

## Build Assembly
[poker-webapp-server] $ sbt clean compile dist

## Run App
[poker-webapp-server] $ sbt run

### Use Google Chrome Browser to use web SPA apps,
Baccarat Billboard - http://localhost:9000/baccarat/topper
Baccarat Admin - /http://localhost:9000/baccarat/admin#/