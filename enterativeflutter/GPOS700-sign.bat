TITLE ASSINA APK DELPHI
@ECHO OFF
SET REQUIRED_JAVA_HOME=C:\Program Files\Java\jdk1.8.0_191
SET REQUIRED_ANDROID_HOME=C:\Users\Bill\AppData\Local\Android\Sdk
SET FINAL_BUILD_TOOLS=
SET HOME_DIRECTORY=%~dp0

cls
echo   ====================================================================================================================
echo   =  MENU                                                                                                            
echo   ====================================================================================================================
echo   =  1 - Normal (PAUSE entre builds)                                                                                 
echo   =  2 - Rapido (sem PAUSE entre builds)                                                                             
echo   ====================================================================================================================
SET /P M=Digite sua escolha: 

cls
echo   ====================================================================================================================
echo   =  COPIANDO enterative                                                                                                           
echo   ====================================================================================================================
COPY "%HOME_DIRECTORY%\build\app\outputs\apk\release\app-release.apk" enterative.apk

if NOT EXIST enterative.apk (
	echo   ====================================================================================================================
	echo   =  NAO FOI ENCONTRADO ARQUIVO PARA enterative.apk ASSINAR !
	echo   ====================================================================================================================
	PAUSE
	EXIT
)

echo   ====================================================================================================================
echo   = CONFIGURANDO BUILD TOOLS
echo   ====================================================================================================================
setlocal enableextensions EnableDelayedExpansion
SET /a targets=1
SET "dots=..."

CD "%ANDROID_HOME%\build-tools\"
FOR /D %%G IN ("*") DO (
	SET vector[!targets!]=%ANDROID_HOME%\build-tools\%%~nxG
	SET /A targets+=1
)

SET /A targets-=1

CLS
:buildtools
echo   ====================================================================================================================
echo   =  Installed Android Build Tools
echo   ====================================================================================================================
FOR /l %%i IN (1,1,%targets%) DO (
 ECHO(  %%i %dots% !vector[%%i]!)
)

SET mychoice=
SET /p mychoice="  Choose a Build Tools 1..%targets% : "
SET BUILD_TOOLS=!vector[%mychoice%]!

if NOT EXIST "%BUILD_TOOLS%\lib\apksigner.jar" (
	echo   ====================================================================================================================
	echo   =  Nao foi identificado o apksigner no build-tools selecionado :
	echo   =  "%BUILD_TOOLS%\lib\apksigner.jar"
	echo   =
	echo   =  Favor selecionar outro Build Tools                                    
	echo   ====================================================================================================================
	PAUSE
	GOTO buildtools
)

ENDLOCAL & SET FINAL_BUILD_TOOLS=%BUILD_TOOLS%

if "%FINAL_BUILD_TOOLS%" == "" (
	echo   ====================================================================================================================
	echo   =  Nao foi definido um build-tools valido :
	echo   =  "%FINAL_BUILD_TOOLS%"
	echo   =
	echo   =  Favor selecionar outro Build Tools                                    
	echo   ====================================================================================================================
	PAUSE
	EXIT
)
IF %M%==1 PAUSE
IF %M%==1 cls

echo   ====================================================================================================================
echo   = FAZENDO BACKUP DO APLICATIVO                                                                                     
echo   ====================================================================================================================
COPY enterative.apk enterative_BKP.apk
IF %M%==1 PAUSE
IF %M%==1 cls

echo   ====================================================================================================================
echo   = ASSINANDO APLICATIVO                                                                                             
echo   ====================================================================================================================
"%JAVA_HOME%\bin\java" -jar "%FINAL_BUILD_TOOLS%\lib\apksigner.jar" sign --ks "%HOME_DIRECTORY%\Development_ChartSistemas_EnhancedAPP.jks" --ks-pass pass:Development@ChartSistemas2018 --key-pass pass:Development@ChartSistemas2018 --ks-key-alias developmentchartsistemas_enhancedapp --min-sdk-version 21 --v1-signing-enabled true --v2-signing-enabled false "%HOME_DIRECTORY%\enterative.apk"
IF %M%==1 PAUSE
IF %M%==1 cls

echo   ====================================================================================================================
echo   = APLICATIVO ASSINADO COM SUCESSO !                                                                                
echo   ====================================================================================================================
dir "%HOME_DIRECTORY%\*.apk"
PAUSE
