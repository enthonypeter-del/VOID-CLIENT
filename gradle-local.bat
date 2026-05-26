@echo off
setlocal
set BASE_DIR=%~dp0
set GRADLE_CMD=%BASE_DIR%gradle-dist\gradle-9.3.0\bin\gradle.bat
if not exist "%GRADLE_CMD%" (
    echo Gradle local nao encontrado. Execute gradle-local.bat apenas no diretorio do projeto.
    exit /b 1
)
"%GRADLE_CMD%" %*
endlocal
