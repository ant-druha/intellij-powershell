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
#Converts a path (§3.4) from a PowerShell path to a PowerShell provider path.
Convert-Path -Path E:.
Convert-Path -Path E:.,G:\Temp\..

Not-Path-Arg $PSScriptRoot
Not-Path-Arg 42d

#in older releases of Windows PowerShell, you can run the following command to get the value of the Application Base property
#of the PowerShellEngine registry key:
(Get-ItemProperty -Path HKLM:\SOFTWARE\Microsoft\PowerShell\3\PowerShellEngine -Name ApplicationBase).ApplicationBase
#Starting in Windows PowerShell 5.0, you can run
Get-ItemPropertyValue -Path HKLM:\SOFTWARE\Microsoft\PowerShell\3\PowerShellEngine -Name ApplicationBase

cd (Get-Location)\pip #command with 2 arguments: (Get-Location) and \pip todo: check it is really works
cd (Get-Location)\pip\(Get-Location)\
cd \pip
cd \pip\wwe

#drive name in unc path 
$acl = Get-Acl \$( hostname )\e$\"some folder"