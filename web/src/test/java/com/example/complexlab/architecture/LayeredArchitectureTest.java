package com.example.complexlab.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class LayeredArchitectureTest {

    @Test
    void webShouldNotDependOnPersistence() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.example.complexlab");
        noClasses().that()
            .resideInAPackage("..web.api..")
            .or().resideInAPackage("..web.controller..")
            .should().dependOnClassesThat()
            .resideInAPackage("..persistence..");
    }

    @Test
    void coreShouldNotDependOnServletOrJdbc() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.example.complexlab");
        noClasses().that().resideInAPackage("..core..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.servlet..", "javax.servlet..", "org.springframework.jdbc..")
                .check(classes);
    }

    @Test
    void controllersShouldBeInWeb() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.example.complexlab");
        classes().that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..web..")
                .check(classes);
    }

    @Test
    void repositoriesShouldBeInPersistence() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.example.complexlab");
        classes().that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..persistence..")
                .check(classes);
    }
}