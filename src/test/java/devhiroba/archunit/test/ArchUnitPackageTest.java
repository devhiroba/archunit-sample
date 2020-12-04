package devhiroba.archunit.test;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import devhiroba.archunit.Application;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Application.class がある場所のパッケージをテスト対象にする
 * ここでは devhiroba.archunit パッケージの直下のすべてのパッケージがテスト対象になる
 */
@AnalyzeClasses(packagesOf = Application.class)
public class ArchUnitPackageTest {

    /**
     * repository パッケージにあるクラスを参照できるのは repository, service パッケージのみ
     * 必ず参照しなけらばならないわけではない（参照しなくてもよい）
     */
    @ArchTest
    ArchRule repositoryPackageRule = classes().that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("..repository..", "..service..");

    /**
     * domain パッケージにあるクラスは controller パッケージを参照禁止
     */
    @ArchTest
    ArchRule domainPackageRule = noClasses().that().resideInAPackage("..domain..")
            .should().accessClassesThat().resideInAPackage("..controller..");

    /**
     * controller 以外のパッケージからは controller パッケージを参照禁止
     * controller パッケージは controller パッケージのみ参照可能
     */
    @ArchTest
    ArchRule controllerPackageRule = noClasses().that().resideOutsideOfPackage("..controller..")
            .should().accessClassesThat().resideInAnyPackage("..controller..");

    /**
     * 循環参照チェック
     * devhiroba.archunit パッケージの直下にあるパッケージ( controller, domain, repository, service )は循環参照禁止
     */
    @ArchTest
    ArchRule freeOfCycles = slices().matching("..archunit.(*)..")
            .should().beFreeOfCycles();

}
