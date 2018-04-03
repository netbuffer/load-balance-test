@echo off
:start-app
if "%1"=="" goto end
start "tomcat-%1" java -Xms10m -Xmx50m -jar target/load-balance-test.jar --server.port=%1
shift
goto start-app

pause

:end
echo end