#Consider a function call Test1 that has the following param block, and which is called as shown:
function Test1 {
  param ([Parameter(Mandatory = $true)] #creation of an attribute instance of a type Parameter with named parameter Mandatory set to $true
  [Alias("CN")]
  [Alias("name", "system")]
  [string[]] $ComputerName)
  $v=1;
}
Test1 "Mars", "Saturn"            # pass argument by position
Test1 -ComputerName "Mars", "Saturn"  # pass argument by name
Test1 -CN "Mars", "Saturn"        # pass argument using first alias
Test1 -name "Mars", "Saturn"        # pass argument using second alias
Test1 -sys "Mars", "Saturn"        # pass argument using third alias

function Test2 {
  param ([Parameter(Mandatory=$true, ValueFromPipelineByPropertyName=$true)]
  [Alias('PSPath')]
  [string] $LiteralPath)
  $v=1;
}
#Cmdlet GetChildItem (alias Dir) adds to the object it returns a new NoteProperty of type string, called PSPath.
Dir "E:\*.txt" | Test2 -LiteralPath {$_ ;"`n`t"; $_.FullName + ".bak"}
Dir "E:\*.txt" | Test2

function Process-Date
{
  param (
  [Parameter(ValueFromPipelineByPropertyName=$true)]
  [int]$Year,
  [Parameter(ValueFromPipelineByPropertyName=$true)]
  [int]$Month,
  [Parameter(ValueFromPipelineByPropertyName=$true)]
  [int]$Day
  )
  process {…}
}
Get-Date | Process-Date

function Test5
{
  param ([ValidateNotNull()]
  [string[]] $Names)
  process {…}
}

function TestPattern
{
  param ([ValidatePattern('^[A-Z][1-5][0-9]$')]
  [string] $Code,
  [ValidatePattern('^(0x|0X)([A-F]|[a-f]|[0-9])([A-F]|[a-f]|[0-9])$')]
  [string] $HexNum,
  [ValidatePattern('^[+|-]?[1-9]$')]
  [int] $Minimum)
  $Minimum = 1;
}
TestPattern -c A12        # matches pattern
TestPattern -c A63        # does not match pattern
TestPattern -h 0x4f      # matches pattern
TestPattern -h "0XB2"      # matches pattern
TestPattern -h 0xK3      # does not match pattern


#12.3.16 The ValidateScript attribute
function ValidateScriptTest
{
  param ([Parameter(Mandatory = $true)]
  [ValidateScript({($_ -ge 1 -and $_ -le 3) -or ($_ -ge 20)})]
  [int] $Count)
  $Count = 0
}
ValidateScriptTest 2				# ok, valid value
ValidateScriptTest 25				# ok, valid value
ValidateScriptTest 5				# invalid value
ValidateScriptTest 0				# invalid value