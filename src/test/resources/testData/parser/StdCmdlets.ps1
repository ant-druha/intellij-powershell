Add-Content "J:\Test\File2.txt" -Value "`nLine 5"

#Adds a user-defined custom member to an instance of a PowerShell object.
(get-childitem)[0] | Add-Member -MemberType NoteProperty -Name Status -Value done
(get-childitem)[0] | Add-Member -MemberType AliasProperty -Name FileLength  -Value Length
$a = "a string"
$a = $a | Add-Member -MemberType NoteProperty -Name StringUse -Value Display -Passthru

#Deletes the contents of one or more items (§3.3), but does not delete them.
Clear-Content J:\Files\Test*.*

#This cmdlet deletes the value of one or more items, but it does not delete them.
#Clear-Item Variable:Count #todo no need to specify '$' for the variable?
Clear-Item $Variable:Count
Clear-Item $Alias:log* -Include *1* -Exclude *3* #todo SIMPLE_ID include '*'?

#Deletes the value of one or more existing variables.
Clear-Variable 'Count10?' -Exclude 'Count101','Count102'

#Compares two sets of objects.
#File1.txt contains the records red, green, yellow, blue, and black. File1.txt contains the records red, Green, blue, Black, and white.
Compare-Object -ReferenceObject $(Get-Content File1.txt) -DifferenceObject $(Get-Content File2.txt) -PassThru
Compare-Object -ReferenceObject $(Get-Content File1.txt) -DifferenceObject $(Get-Content File2.txt) -PassThru -CaseSensitive
Compare-Object -ReferenceObject $(Get-Content File1.txt) -DifferenceObject $(Get-Content File2.txt) -PassThru -ExcludeDifferent -IncludeEqual

#Converts a string containing one or more key/value pairs to a hash table.
$str = @'
Msg1 = The string parameter is required.
Msg2 = Credentials are required for this command.
Msg3 = The specified variable does not exist.
'@
$v = ConvertFrom-StringData -StringData $str
$v = $str | ConvertFrom-StringData
$v = ConvertFrom-StringData -StringData @'
   Name = Disks.ps1
   # Category is optional.
   Category = Storage
   Cost = Free
'@
$v = ConvertFrom-StringData -StringData "Top = Red `n Bottom = Blue"

#Converts a path (§3.4) from a PowerShell path to a PowerShell provider path.
Convert-Path -Path E:.
Convert-Path -Path E:.,G:\Temp\..

#Copies one or more items (§3.3) from one location to another.
Copy-Item -Path "J:\Test\File2.txt" -Destination "J:\Test\File2-v2.txt"
Copy-Item -Path "J:\Test\*","J:\Test3\*" -Destination "J:\" -PassThru
Copy-Item -Path "E:\Temp\*" -Destination "J:\Test" -Container -Recurse

#Exports alias information to a file.
Export-Alias 'E:\Powershell\Scripts\AliasList.txt' -As Csv
Export-Alias 'E:\Powershell\Scripts\Locals.ps1' -As Script -Scope "Local"

#Identifies the module (§3.14) members that are to be exported.
Export-ModuleMember -Function CToF,FToC -Alias c2f,f2c
Export-ModuleMember -Variable boolingTempC,boolingTempF
Export-ModuleMember -Variable freezingTempC,freezingTempF

#Performs an operation against each of a set of input objects. (alias %, foreach)
5,20,-3 | ForEach-Object -Process {$_ * 2}
5,20,-3 | % -Begin { "Setup" } -Process {$_ * $_} -End { "Cleanup" }

#Gets alias information.
Get-Alias -Name "Fun*"
Get-Alias -Definition F1,F2

#Gets the items (§3.3) and child items at one or more specified locations.
Get-ChildItem "J:\Test\File2.txt"
Get-ChildItem "J:\Test\File2.txt" -Name
Get-ChildItem "J:\F*.*" -Recurse

#This cmdlet gets information about cmdlets and other elements of commands.
Get-Command -CommandType Alias -TotalCount 10
Get-Command -Name "Get-Date" -ArgumentList "Day","Month","Year"
Get-Command -Name "Get-Date" -CommandType Cmdlet -Syntax

#Gets the content of the items (§3.3) at the specified locations.
Get-Content "J:\Test\File2.txt" -TotalCount 3
Get-Content "Env:\Path"
Get-Content "Variable:\Count"

#Gets a credential object (§4.5.23) based on a user name and password.
$v = Get-Credential
$v = Get-Credential -Credential "User10"

#This cmdlet gets a date-time object (§4.5.19) that represents the current or the specified date.
#When run on 2010-03-15 at 12:38:01
Get-Date -Date "2010-2-1 10:12:14 pm"			# 02/01/2010 22:12:14
Get-Date -Format "m"									# March 15
Get-Date -Format "dd-MMM-yyyy"					# 15-Mar-2010
Get-Date -UFormat "%Y-%m-%d %A %Z"				# 2010-03-15 Monday -04
Get-Date -Day 2 -Month 3 -Year 2006				# 03/02/2006 12:38:01
Get-Date -Hour 11 -Minute 3 -Second 23			# 03/15/2010 11:03:23

#Displays information about the specified command. See §A for information about creating script files that contain help comments.
Get-Help Get-*
Get-Help Add-Content -Examples
Get-Help Add-Content -Full
Get-Help E:\Scripts\Help\Get-Power

#Gets one or more items from the specified locations.
Get-Item -Path "J:\Test","J:\Test3"
Get-Item Env:Day1,Env:Day2
Get-Item -Path "Function:MyFun2"
Get-Item -Path "Variable:MyVar1"

#Gets information about the current working location (§3.1.4) for the specified drive(s), or the working locations for the specified stack(s).
Get-Location							# get location of current drive
Get-Location -PSDrive G,C,D		# get location of specified drives
Get-Location -Stack					# get all locations from current stack
Get-Location -StackName "Stack2","Stack1" # get all locations from named stacks

#Gets the specified members of an object.
$v = New-Item -Force -Path "I:\" -Name "Test" -Itemtype "Directory"
$v | Get-Member
$v | Get-Member -Force
$v | Get-Member -Name Name,Extension,CreationTime
$v | Get-Member -MemberType NoteProperty,ScriptProperty
$v | Get-Member -View All

#Gets an object for each module (§3.14) that has been imported or that can be imported into the current session.
Get-Module -All -ListAvailable
Get-Module PSTest_Temperature

#Gets the PowerShell drives (§3.1) in the current session.
Get-PSDrive
Get-PSDrive D
Get-PSDrive -PSProvider filesystem

#Gets information about one or more providers (§3.1).
Get-PSProvider								# request information for all providers
Get-PSProvider "Alias","Variable"	# request information for 2 providers
$v = Get-PSProvider "FileSystem"		# request information for 1 provider
foreach ($e in $v.Drives) { … }		# process each drive

#Writes information about the specified variables to the pipeline.
Get-Variable 'Count10?' -Exclude 'Count101','Count102' -Scope 'Script'

#Groups objects that contain the same value for specified properties.
Get-ChildItem *.xml | Group-Object -Property Length -AsHashTable
Get-ChildItem *.* | Sort-Object -Property Extension | Group-Object -Property Extension

#Adds one or more modules (§3.14) to the current session.
Import-Module -Name Lib1,Lib2 -Verbose
Import-Module "E:\Modules\PSTest_Temperature"

#Perform the default action on the specified item(s) (§3.3).
Invoke-Item File2.txt
Invoke-Item "J:\Manual.pdf"
Invoke-Item "J:\Capture.jpg","J:\Action list.doc"

#Combines a path (§3.4) and a child path into a single path.
Join-Path -Path c:,e:\Main -ChildPath File1.txt
Join-Path -Path G:\ -ChildPath Temp??.txt
Join-Path -Path G:\ -ChildPath Temp\Data*.* -Resolve

#Calculates the numeric properties of objects, and the characters, words, and lines in string objects, such as files of text.
Get-ChildItem *.* | Measure-Object -Property Length -Maximum -Minimum -Average -Sum
Get-ChildItem Test.txt | Measure-Object -Character -Line -Word

#Moves one or more items (§3.3) from one location to another.
Move -Item -Path "J:\Test\*","J:\Test3\*" -Destination "J:\"
Move -Item -Path "E:\Temp\*" -Destination "J:\Test"

#Creates a new alias.
New-Alias 'Func1' 'F1'
New-Alias 'Func2' 'Func1'
New-Alias 'Script1' 'E:\Powershell\Scripts\script1.ps1'
New-Alias 'Script1' 'E:\Powershell\Scripts\script2.ps1' -Force
New-Alias 'Func3' 'F1' -Scope Global
function F1 { … }

#Creates a new item with the specified name and type in the specified path.
New-Item -Path "J:\" -Name "Test" -Itemtype "Directory"
New-Item -Path "J:\Test2" -Itemtype "Directory"
New-Item -Path "J:\Test" -Name "File1.txt" -Itemtype "File"
New-Item -Path "J:\Test" -Name "File2.txt" -Itemtype "File" -Value "Hello`n"
New-Item -Path "J:\Test","J:\Test3" -Name "File3.txt" -Itemtype "File"
New-Item -Path "Alias:" -Name "MyName1" -Value "F1"
New-Item -Path "Env:" -Name "MyEnv1" -Value "Max=200"
New-Item -Path "Function:" -Name "MyFun2" -Value { param ($p1,$p2) $p1*$p2 }
New-Item -Path "Variable:" -Name "MyVar1" -Value 100

#Creates a dynamic module (§11.7).
New-Module -Name DynMod1 -ScriptBlock {function F1 { … } }
New-Module -Name DynMod2 -ArgumentList 123,"abc" -ScriptBlock
{ param ($p1,$p2) function F2 { … } }
New-Module -Name DynMod3 -AsCustomObject -ScriptBlock {function F3 { … } }
New-Module -Name DynMod4 -ReturnResult -ScriptBlock {function F4 { … } ; }

#Creates an object of the given type.
New-Object 'bool'
New-Object 'string' 'A red house'
New-Object 'int[]' 0
New-Object 'double[,]' 3, 2
New-Object -ArgumentList 2, 4, 3 -TypeName 'int[,,]'

#Creates a new variable.
# The following are equivalent; they all create a variable called $Count1
New-Variable -Name 'Count1' -Value 100
New-Variable -Name 'Count1' 100
New-Variable 'Count1' -Value 100
New-Variable 'Count1' 100
$name = 'Count1'; $value = 100
New-Variable $name $value
$Count1 = 100
New-Variable -Name Count30 -Value 150 -Option ReadOnly
$Count30 = -40    		# rejected as $Count30 is read-only

New-Variable -Name Count30 -Value 151 -Force 	# overwrites Count30
New-Variable -Name Count51 -Value 200 -PassThru | CommandX
New-Variable -Name Count61 -Value 200 -Scope "Script"
New-Variable -Name Count64 -Value 200 -Scope "0"		# local scope
New-Variable -Name Count70 -Value 150 -Option Constant
$Count70 = -40    # rejected as $count70 is not writable
New-Variable -Name Count71 -Value 150 -Option Private
New-Variable -Name Count72 -Value 150 -Option AllScope
New-Variable -Name Count80 -Value 150 -Confirm
New-Variable -Name Count90 -Value 150 -WhatIf

#Sets the current working location (§4.5.5) to that on the top of the specified working location stack.
Pop-Location
$v = Pop-Location -StackName "Stack3" -PassThru

#Adds the current or a specified working location to the top of a given working location stack.
$v = Push-Location -PassThru
Push-Location -Path "E:\temp" -StackName "Stack3"

#Deletes one or more items (§3.3).
Remove-Item C:\Test\*.* -Recurse
Remove-Item * -Include *.doc -Exclude *1*
Remove-Item -Path C:\Test\hidden-RO-file.txt -force

#Removes one or more modules (§3.14) from the current session.
Remove-Module PSTest_Temperature
Remove-Module Lib1,Lib2

#Deletes one or more variables.
Remove-Variable 'Count10?' -Exclude 'Count101','Count102'

#Renames an item (§3.3).
Rename-Item J:\Test\File1.txt File1.tmp

#Resolves the wildcard characters in one or more paths (§3.4), and outputs the path(s) contents.
Resolve-Path -Path E:\Temp\W*,E:\Temp\Action*
Resolve-Path -Path E:\Temp\Action* -Relative

#Selects specified properties of an object or set of objects. It can also select unique objects from an array of objects, or
#it can select a specified number of objects from the beginning or end of an array of objects.
"b", "a","b","a","a","c","a" | Select-Object -Unique
Get-Date | Select-Object -Property Year,Month,Day
$dates = $d1,$d2,$d3,$d4,$d5		# a collection of 5 date/time values
$dates | Select-Object -Property Year,Month,Day
$dates | Select-Object -First 1 -Last 2

#Sets one or more characteristics of one or more existing aliases. If the aliases do not exist, creates them with those characteristics.
New-Alias 'Func1' 'F1'
Set-Alias 'Func1' 'F1' -Description "…"
Set-Alias 'Func1' 'F1' -Scope Global
function F1 { … }

#Replaces the content in an item (§3.3) with new content.
Set-Content "J:\Test\File2.txt" -Value "Line A","Line B"

#Changes the value of one or more items (§3.3).
Set-Item Variable:Count -Value 200
Set-Item Env:Day1,Env:Day2 -Value "Monday" -PassThru

#Sets the current working location or sets the current working location stack.
Set-Location c:\temp
Set-Location G:
Set-Location -StackName "Stack1" -PassThru

#Sets one or more characteristics of one or more existing variables or, if the variables do not exist, creates them with those characteristics.
Set-Variable 'Count100' 100								# create new
Set-Variable 'Count101','Count102','Count103' 110	# create new
Set-Variable 'Count100' 200								# change existing
Set-Variable 'Count101','Count102','Count104' 210	# change 2, create 1
Set-Variable 'Count10?' 111 -Exclude 'Count101','Count102'

#Sorts objects by one or more property values.
$d1 = Get-Date "03/04/2006 6:15:23 AM"
$d5 = Get-Date "03/01/2006 11:23:56 PM"
$dates = $d1,$d2,$d3,$d4,$d5

$v = $dates | Sort-Object
$v = $dates | Sort-Object -Descending -Unique
$v = $dates | Sort-Object -Property @{Expression="Day" ; Ascending=$true},@{Expression="Hour" ; Ascending=$false}

#Retrieves the specified part of one or more paths (§3.4) or reports on certain characteristics of those paths.
Split-Path -Path ..\Temp -IsAbsolute						# False
Split-Path -Path J:\,J:\main\sub1\sub2 -Leaf				# "J:\","sub2"
Split-Path -Path J:\,J:\main\sub1\sub2 -Parent			# "",J:\main\sub1"
Split-Path -Path J:\ -Qualifier								# "J:"
Split-Path -Path J:\,J:\main\sub1\sub2 -NoQualifier # "\","\main\sub1\sub2"

#Saves command output in a file or variable as well as writing it to the pipeline.
123 | Tee-Object -FilePath "E:\Temp\Log.txt"
"red" | Tee-Object -Variable Copy

#Determines whether all elements of a path (§3.4) exist.
Test-Path -Path j:\Files
Test-Path -Path j:\Files\MyFile1.txt -IsValid
Test-Path -Path j:\Files\File1.txt,j:\Files\File2.txt,j:\Files\File3.txt
Test-Path -Path j:\Files\* -Exclude *.txt
Test-Path -Path j:\Files -PathType Container
Test-Path -Path Variable:Count
Test-Path -Path Function:F1
Test-Path -Path Env:Path

#Creates a filter to determine which input objects are be passed along a command pipeline.
Get-ChildItem "E:\Files\*.*" | Where-Object { $_.Length -le 1000 }
Get-ChildItem "E:\Files\*.*" | Where-Object Length –eq 0
Get-ChildItem "E:\Files\*.*" | Where-Object IsReadOnly