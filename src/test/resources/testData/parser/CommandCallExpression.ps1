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

$a = New-Object 'double[,]' 3, 2

#Performs an operation against each of a set of input objects. (alias %, foreach)
5,20,-3 | ForEach-Object -Process {$_ * 2}
5,20,-3 | % -Begin { "Setup" } -Process {$_ * $_} -End { "Cleanup" }

#Creates a filter to determine which input objects are be passed along a command pipeline.
Get-ChildItem "E:\Files\*.*" | Where-Object { $_.Length -le 1000 }
Get-ChildItem "E:\Files\*.*" | Where-Object Length –eq 0