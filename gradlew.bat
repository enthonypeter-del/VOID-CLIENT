@echo off
setlocal
set DIR=%~dp0
if "%JAVA_HOME%"=="" (
    set JAVA_EXE=java
) else (
    set JAVA_EXE=%JAVA_HOME%\bin\java
)
set APP_BASE_NAME=%~n0
set APP_HOME=%DIR%
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar;%APP_HOME%gradle\wrapper\gradle-wrapper-shared.jar
"%JAVA_EXE%" %GRADLE_OPTS% %JAVA_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
