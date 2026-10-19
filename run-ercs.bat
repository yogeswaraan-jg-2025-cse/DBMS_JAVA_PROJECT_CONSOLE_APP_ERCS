@echo off
setlocal
title ERCS - Emergency Response & Resource Coordination System

REM Local demo credentials. Existing environment values take priority.
if not defined ERCS_DB_USER set "ERCS_DB_USER=root"
if not defined ERCS_DB_PASSWORD set "ERCS_DB_PASSWORD=root"

if not exist out mkdir out

echo Compiling ERCS...
javac -cp "lib\mysql-connector-j-9.7.0.jar" -d out src\Main.java src\model\*.java src\dao\*.java src\service\*.java src\util\*.java
if errorlevel 1 (
    echo.
    echo Compilation failed. Review the error above.
    pause
    exit /b 1
)

echo.
echo Starting ERCS...
java -cp "out;lib\mysql-connector-j-9.7.0.jar" Main

echo.
pause
endlocal
