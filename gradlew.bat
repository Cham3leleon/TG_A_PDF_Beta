@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle wrapper para Windows
@rem
@rem ##########################################################################

set APP_HOME=%~dp0

if exist "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" goto run
echo.
echo ERROR: No se encontró el archivo gradle-wrapper.jar
echo.
exit /b 1

:run
java -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %*
