package com.empresa.pedidos.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/** Verifica automáticamente las reglas de la arquitectura hexagonal. */
@AnalyzeClasses(packages = "com.empresa.pedidos", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaTest {

    @ArchTest
    static final ArchRule dominioNoDependeDeNadaExterno = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..application..", "..infrastructure..", "org.springframework..", "jakarta..",
                    "com.fasterxml..");

    @ArchTest
    static final ArchRule aplicacionNoDependeDeInfraestructuraNiSpring = noClasses().that()
            .resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..", "org.springframework..");

    @ArchTest
    static final ArchRule adaptadoresDeEntradaNoConocenAdaptadoresDeSalida = noClasses().that()
            .resideInAPackage("..adapter.in..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.out..");

    @ArchTest
    static final ArchRule adaptadoresDeSalidaNoConocenAdaptadoresDeEntrada = noClasses().that()
            .resideInAPackage("..adapter.out..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.in..");

    @ArchTest
    static final ArchRule loscasosDeUsoSeAccedenSoloPorSusPuertos = classes().that()
            .resideInAPackage("..application.interactor..")
            .should().onlyBeAccessed().byClassesThat().resideInAnyPackage(
                    "..application.interactor..", "..infrastructure.config..");
}
