a painkiller library for people struggling with @Transactional issues in test case

# no-transactional(English)
## Introduction
inspired by the discussion on whether using the @Transactional annotation provided by the Spring Framework in test cases is appropriate,
**no-transactional** is a module made for people who want to test a class or method without using @Transactional,

## Features
* Initializing test data before executing each test without using the @Transactional annotation.
* Forcibly interrupting tests If there are tests using the @Transactional annotation on a class or method.   
(Including cases where @Transactional is embedded internally like @DataJpaTest)

## How To Use
1. Add the following code to the build.gradle of the module where you want to use the no-transactional library:
```
repositories {
    maven { url 'https://jitpack.io' }
}

testImplementation 'com.github.banlife:no-transactional:preRelease-4'
```

2. Add a file named **META-INF/services/org.junit.jupiter.api.extension.Extension** under the `src/test/resources` package of the module,   
and enter the following code:
<img width="372" alt="image" src="https://github.com/banlife/no-transactional/assets/155510835/788b1eaf-19ce-4ed7-ab2c-10aa1faef3a1">

```
banlife.NoTransactionExtension
```

3. Add a file named `junit-platform.properties` under the `resources` package, and enter the following code:
<img width="247" alt="image" src="https://github.com/banlife/no-transactional/assets/155510835/a36f2358-2616-4e93-8d5e-3db597db73ec">
   
```
junit.jupiter.extensions.autodetection.enabled=true
```
   
4. Happy hacking! 🚀
---

# no-transactional(Korean)
## 소개
'Spring Framework에서 제공하는 @Transactional 어노테이션을 테스트 케이스에서 사용하는 것이 적절한가?' 에 대한 논의에서 영감을 얻은,   
테스트 클래스 또는 메서드에서 @Transactional을 사용하지 않고 테스트하고자 할 때 유용하게 사용할 수 있는 모듈입니다.

## 기능
* @Transactional 어노테이션을 사용하지 않아도 각 테스트를 실행하기 전 테스트 데이터를 초기화합니다.
* 클래스 또는 메서드에 @Transactional 어노테이션을 사용하는 테스트가 있을 경우, 해당 테스트를 강제로 중단합니다.   
(@DataJpaTest 처럼 내부에 @Transactional을 내장하는 경우도 포함)

## 사용법
1. no-tracsactional 라이브러리를 사용할 모듈의 build.gradle에 아래 코드를 추가합니다.
```
repositories {
    maven { url 'https://jitpack.io' }
}

testImplementation 'com.github.banlife:no-transactional:preRelease-4'
```
   
2. 모듈의 src.test.resources 패키지 하위에 **META-INF/services/org.junit.jupiter.api.extension.Extension** 파일을 추가한 후,   
아래 코드를 입력합니다.
<img width="372" alt="image" src="https://github.com/banlife/no-transactional/assets/155510835/788b1eaf-19ce-4ed7-ab2c-10aa1faef3a1">

```
banlife.NoTransactionExtension
```
   
3. resources 패키지 하위에 **junit-platform.properties** 파일을 추가한 후,   
아래 코드를 입력합니다.
<img width="247" alt="image" src="https://github.com/banlife/no-transactional/assets/155510835/a36f2358-2616-4e93-8d5e-3db597db73ec">
   
```
junit.jupiter.extensions.autodetection.enabled=true
```
   
4. Happy hacking! 🚀
