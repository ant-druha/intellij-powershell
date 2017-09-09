mycmd.exe "./plink.exe" $escapeparser $Hostname -l $Username -pw $Password $Command

Get-ChildItem e:\ProgramdFiles\*.txt | Sort-Object -CaseSensitive | Process-File > results.txt

$fullCommand = '& "other.exe" --% ' + $commandArgs
Invoke-Expression $fullCommand

#switch parameter
Set-MyProcess -Strict
Set-MyProcess -Strict: $true #the same as above


#parameter with argument
Get-Power -base 5 -exponent 3
Get-Power -exponent 3 -base 5

#Positional argument
Get-Power 5 3