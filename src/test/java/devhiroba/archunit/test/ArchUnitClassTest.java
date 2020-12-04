package devhiroba.archunit.test;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import devhiroba.archunit.Application;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Application.class がある場所のパッケージをテスト対象にする
 * ここでは devhiroba.archunit パッケージの直下のすべてのパッケージがテスト対象になる
 */
@AnalyzeClasses(packagesOf = Application.class)
public class ArchUnitClassTest {
    /**
     * クラス名が Controller で終わるクラスは Service, model で終わるクラスを参照可能
     * 必ず参照しなければならない（各 Controller クラスで Service 又は domain クラスを参照しないとエラーになる）
     */
    @ArchTest
    ArchRule controllerClassRule = classes().that().haveSimpleNameEndingWith("Controller")
            .should().accessClassesThat().haveSimpleNameEndingWith("Service")
            .orShould().accessClassesThat().haveSimpleNameEndingWith("domain");

    /**
     * クラス名が Repository で終わるクラスは Service, Controller で終わるクラスを参照不可
     */
    @ArchTest
    ArchRule repositoryClassRule = noClasses().that().haveSimpleNameEndingWith("Repository")
            .should().accessClassesThat().haveSimpleNameEndingWith("Service")
            .orShould().accessClassesThat().haveSimpleNameEndingWith("Controller");

    /**
     * クラス名が Controller で終わるクラスは controller パッケージに作成する
     */
    @ArchTest
    ArchRule controllerClassesRule = classes().that().haveSimpleNameStartingWith("Service")
            .should().resideInAnyPackage("..service..");
}
