package banlife;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;

@Slf4j
public class NoTransactionExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        var applicationContext = SpringExtension.getApplicationContext(extensionContext);

        validateTransactionalAnnotationExists(extensionContext);
        cleanDatabase(applicationContext);
    }

    private static void validateTransactionalAnnotationExists(ExtensionContext extensionContext) {
        var testClass = extensionContext.getRequiredTestClass();
        var testMethod = extensionContext.getRequiredTestMethod();

        if (testClass.isAnnotationPresent(Transactional.class) ||
                testMethod.isAnnotationPresent(Transactional.class)) {
            Assertions.fail("@Transactional 어노테이션이 테스트 클래스나 메서드에 존재합니다.");
        }

        var jakartaTxClassName = "jakarta.transaction.Transactional";
        if (ClassUtils.isPresent(jakartaTxClassName, NoTransactionExtension.class.getClassLoader())) {
            try {
                var jakartaTxClass = Class.forName(jakartaTxClassName);

                if (hasAnnotationByName(testClass, jakartaTxClass) || hasAnnotationByName(testMethod, jakartaTxClass)) {
                    Assertions.fail("@jakarta.transaction.Transactional 어노테이션이 테스트 클래스나 메서드에 존재합니다.");
                }

            } catch (ClassNotFoundException e) {
                log.debug("jakarta.transaction.Transactional 클래스가 없어서 어노테이션 검사를 생략합니다.");
            } catch (Exception e) {
                log.warn("jakarta.transaction 어노테이션 검사 중 예외 발생", e);
            }
        }
    }

    private static boolean hasAnnotationByName(AnnotatedElement element, Class<?> annotationClass) {
        try {
            for (var annotation : element.getAnnotations()) {
                if (annotation != null && annotation.annotationType().getName().equals(annotationClass.getName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn("어노테이션 검사 중 예외 발생 (element: {})", element, e);
        }
        return false;
    }

    private static void cleanDatabase(ApplicationContext applicationContext) {
        try {
            DatabaseCleaner.clear(applicationContext);
        } catch (NoSuchBeanDefinitionException e) {
            log.debug("DatabaseCleaner Bean이 없어 DB 초기화를 생략합니다.");
        }
    }
}