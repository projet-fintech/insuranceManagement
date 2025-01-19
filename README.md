
---

### **Insurance Management Microservice (README)**


![image](https://github.com/user-attachments/assets/b31d743e-cbfd-4e4b-8e80-78aeb467a5ec)


```markdown
# Insurance Management Microservice

## Description
Ce microservice gère les demandes d'assurance des clients. Il intègre deux applications Flask pour effectuer des prédictions selon le type d'assurance (automobile ou santé).

## Fonctionnalités
- Gestion des demandes d'assurance.
- Prédictions basées sur Flask.

## Dépendances
- Spring Boot
- Flask (externe)
- Eureka Client

## Configuration
- Configurez les URLs des applications Flask dans `application.yml`.

## Lancer le service
```bash
mvn clean install
java -jar target/insurance-management-service.jar
