@echo off
REM Run from project root. Use path in quotes because folder name contains spaces and "&".
cd /d "%~dp0frontend"
if not exist "node_modules" (
  echo Installing dependencies...
  call npm install
)
call npm run dev
