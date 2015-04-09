@echo off 

:: Set here where you have cloned / dowloaded the icon set,
:: you can find it here: https://github.com/google/material-design-icons
set ICPATH=%ANDROID_HOME%\_extra\material-design-icons-1.0.1

md drawable-hdpi
md drawable-mdpi
md drawable-xhdpi
md drawable-xxhdpi
md drawable-xxxhdpi

:: ----- ASSETS LOOKUP -----
call :cpy_asset     social          ic_share_white_24dp
call :cpy_asset     social          ic_share_grey600_24dp

call :cpy_asset     toggle          ic_star_white_24dp
call :cpy_asset     toggle          ic_star_grey600_24dp
call :cpy_asset     toggle          ic_star_outline_white_24dp
call :cpy_asset     toggle          ic_star_outline_grey600_24dp

call :cpy_asset     navigation      ic_more_vert_white_24dp
:: ----- END ASSETS LOOKUP -----

goto :eof


:cpy_asset
copy /Y "%ICPATH%\%1\drawable-hdpi\%2.png" drawable-hdpi
if %ERRORLEVEL% neq 0 goto :err
copy /Y "%ICPATH%\%1\drawable-mdpi\%2.png" drawable-mdpi
if %ERRORLEVEL% neq 0 goto :err
copy /Y "%ICPATH%\%1\drawable-xhdpi\%2.png" drawable-xhdpi
if %ERRORLEVEL% neq 0 goto :err
copy /Y "%ICPATH%\%1\drawable-xxhdpi\%2.png" drawable-xxhdpi
if %ERRORLEVEL% neq 0 goto :err
copy /Y "%ICPATH%\%1\drawable-xxxhdpi\%2.png" drawable-xxxhdpi
if %ERRORLEVEL% neq 0 goto :err
goto :eof

:err
echo File not found "%1:%2".
pause>nul
