C:\MyPath\To\Command.exe 3
Get-ChildItem e:\d-.txt
Get-ChildItem e:\*.txt
Get-ChildItem e:\ProgramFiles\sample.txt
Get-ChildItem ..
Get-ChildItem \
Get-ChildItem .
Get-ChildItem e:\
Get-ChildItem $PSScriptRoot\
Import-Module $PSScriptRoot\..\SampleModule.psd1
Import-Module $PSScriptRoot\..\$var_module
Import-Module ModuleName\::C:\ProgramFiles\sample.txt
Import-Module ModuleName1\::ModuleName2\::C:\ProgramFiles\sample.txt
New-File -Path ..\Examples\READMENew.md
New-File -Path ..\Examples\README.md, XYZZY.ps1 #Processes multiple relative path via -Path param
. .\Stop-Process2.ps1 # vs . ./Stop-Process2.ps1

Not-Path-Arg $PSScriptRoot
Not-Path-Arg 42d