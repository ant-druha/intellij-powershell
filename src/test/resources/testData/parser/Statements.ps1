#statement value
$v = for ($i = 1; $i -le 5; ++$i) { }     # The value is $null
$v = for ($i = 1; $i -le 5; ++$i) { $i }  # The value of the statement is object[] of Length 5.

# if $count is not currently defined then define it with int value 10
$count = if ($count -eq $null) { 10 } else { $count }
$i = 1
$v = while ($i -le 5)
     {
       $i							# $i is written to the pipeline
       if ($i -band 1)
       {
         "odd"					# conditionally written to the pipeline
       }
       ++$i						# not written to the pipeline
     }
# $v is object[], Length 8, value 1,"odd",2,3,"odd",4,5,"odd"

#pipelines
Get-Factorial 5
& Get-Factorial 5
& "Get-Factorial" 5

function Get-Power([int]$x, [int]$y)
{
  if ($y -gt 0) { return $x * (Get-Power $x (--$y)) }
  else { return 1 }
}

New-Object 'int[,]' 3,2
New-Object -ArgumentList 3,2 -TypeName 'int[,]'
dir e:\PowerShell\Scripts\*statement*.ps1 | Foreach-Object {$_.Length}
dir e:\PowerShell\Scripts\*.ps1 | Select-String -List "catch" | Format-Table

#if
$grade = 92
if ($grade -ge 90) { "Grade A" }
elseif ($grade -ge 80) { "Grade B" }
elseif ($grade -ge 70) { "Grade C" }
elseif ($grade -ge 60) { "Grade D" }
else { "Grade F" }

#iterations
#while
while ($i -le 5)						# loop 5 times
{
  "{0,1}`t{1,2}" -f $i, ($i*$i)
  ++$i
}

#do while
do
{
  "{0,1}`t{1,2}" -f $i, ($i*$i)
}
while (++$i -le 5)					# loop 5 times

#do until
do
{
  "{0,1}`t{1,2}" -f $i, ($i*$i)
}
until (++$i -gt 5)					# loop 5 times

#for
for ($i = 5; $i -ge 1; --$i)		# loop 5 times
{
  "{0,1}`t{1,2}" -f $i, ($i*$i)
}
for (;$i -ge 1;)						# equivalent behavior
{
  "{0,1}`t{1,2}" -f $i, ($i*$i)
  --$i
}

#foreach
foreach ($e in $a)
{
  ;
}

foreach ($e in -5..5)
{
#;
}
foreach ($t in [byte],[int],[long])
{
  $t::MaxValue					# get static property
}
foreach ($f in dir *.txt)
{
#…
}
$h1 = @{ FirstName = "James"; LastName = "Anderson"; IDNum = 123 }
foreach ($e in $h1.Keys)
{
  "Key is " + $e + ", Value is " + $h1[$e]
}

#control flow
while (…)
{
  …
  if (…)
  {
    continue						# start next iteration of current loop
  }
  …
}
$lab = "go_here"
:go_here
for (…; …; …)
{
  if (…)
  {
    continue $lab			# start next iteration of labeled loop
  }
}
:labelA
for ($i = 1; $i -le 2; $i++)
{
  :labelB
  for ($j = 1; $j -le 2; $j++)
  {
    :labelC
    for ($k = 1; $k -le 3; $k++)
    {
      if (…) { continue labelB }
    }
  }
}

#throw
throw
throw 100
throw "No such record in file"

#return
function Get-Factorial ($v)
{
  if ($v -eq 1)
  {
    return 1									# return is not optional
  }

  return $v * (Get-Factorial ($v - 1))	# return is optional
}
#The caller to Get-Factorial gets back an int.
function Test
{
  "text1"							# "text1" is written to the pipeline
  # …
  "text2"							# "text2" is written to the pipeline
  # …
  return 123						# 123 is written to the pipeline
}

#exit
exit $count			# terminate the script with some accumulated count

#switch
$s = "ABC def`nghi`tjkl`fmno @#$"
$charCount = 0; $pageCount = 0; $lineCount = 0; $otherCount = 0
for ($i = 0; $i -lt $s.Length; ++$i)
{
  ++$charCount
  switch ($s[$i])
  {
  "`n"	  { ++$lineCount }
  "`f"	  { ++$pageCount }
  "`t"	  { }
  " "	  { }
  default { ++$otherCount }
  }
}
switch -wildcard ("abc")
{
a*      { "a*, $_" }
?B?     { "?B? , $_" }
default { "default, $_" }
}
switch -regex -casesensitive ("abc")
{
^a* { "a*" }
^A* { "A*" }
}
switch (0,1,19,20,21)
{
{ $_ -lt 20 }	{ "-lt 20" }
{ $_ -band 1 }	{ "Odd" }
{ $_ -eq 19 }	{ "-eq 19" }
default			{ "default" }
}

#try/finally
$a = new-object 'int[]' 10
$i = 20                # out-of-bounds subscript
while ($true)
{
  try
  {
    $a[$i] = 10
    "Assignment completed without error"
    break
  }
  catch [IndexOutOfRangeException]
  {
    "Handling out-of-bounds index, >$_<`n"
    $i = 5
  }
  catch
  {
    "Caught unexpected exception"
  }
  finally
  {
  # …
  }
}

#trap
$j = 0; $v = 10/$j; "Done"
trap { $j = 2; continue }
Trap { $j = 2; break }
&{trap{}; throw '...'; 1}
trap{} &{throw '...'; 1}; 2
trap{} {throw '...'; 1}; 2
trap{} {trap{}}

#data
data -supportedCommand ConvertTo-XML
{
  Format-XML -strings "string1", "string2", "string3"
}
$messagesS = data
             {
               ConvertFrom-StringData -stringdata @"
		Greeting = Buenos días
		Yes = sí
		No = no
"@
             }
data messages
{
  ConvertFrom-StringData -stringdata @"
		Greeting = Buenos días
		Yes = sí
		No = no
"@
}

#filter functions
filter Get-Square2		# make the function a filter
{
  $_ * $_					# access current object from the collection
}

-3..3 | Get-Square2		# collection has 7 elements
6,10,-3 | Get-Square2	# collection has 3 elements

#function arguments
function Get-Power ([long]$base, [int]$exponent)
{
  $result = 1
  for ($i = 1; $i -le $exponent; ++$i)
  {
    $result *= $base
  }
  return $result
}

function F ($a, $b, $c, $d) { … }
F -b 3 -d 5 2 4       # $a is 2, $b is 3, $c is 4, $d is 5, $args Length 0
F -a 2 -d 3 4 5       # $a is 2, $b is 4, $c is 5, $d is 3, $args Length 0
F 2 3 4 5 -c 7 -a 1   # $a is 1, $b is 2, $c is 7, $d is 3, $args Length 2

function Find-Str ([string]$str, [int]$start_pos = 0) { … }
Find-Str "abcabc"		# 2nd argument omitted, 0 used for $start_pos
Find-Str "abcabc" 2	# 2nd argument present, so it is used for $start_pos

function until($i, $i2) { return $I2+$i; }; until 1 6;
function parallel($i, $i2) { return $I2+$i; }; parallel 1 6;
function catch($i, $i2) { return $I2+$i; }; catch 1 6;
function dynamicparam($i, $i2) { return $I2+$i; }; dynamicparam 1 6;
function param($i, $i2) { return $I2+$i; }; param 1 6;

#the [switch] type constraint
function Process ([switch]$trace, $p1, $p2) { … }

Process 10 20					# $trace is False, $p1 is 10, $p2 is 20
Process 10 -trace 20			# $trace is True, $p1 is 10, $p2 is 20
Process 10 20 -trace			# $trace is True, $p1 is 10, $p2 is 20
Process 10 20 -trace:$false	# $trace is False, $p1 is 10, $p2 is 20
Process 10 20 -trace:$true		# $trace is True, $p1 is 10, $p2 is 20

#pipelines & functions
function Get-Square1
{
  foreach ($i in $input)		# iterate over the collection
  {
    $i * $i
  }
}

-3..3 | Get-Square1			# collection has 7 elements
6,10,-3 | Get-Square1		# collection has 3 elements

#funciton named blocks
function Sample
{
  Param ([String]$Name, [String]$Path)
  DynamicParam
  {
    if ($path -match "*HKLM*:")
    {
      $dynParam1 = New-Object
      System.Management.Automation.RuntimeDefinedParameter("dp1",
                                                           [Int32], $attributeCollection)
      $attributes = New-Object
      System.Management.Automation.ParameterAttribute
      $attributes.ParameterSetName = 'pset1'
      $attributes.Mandatory = $false
      $attributeCollection = New-Object -Type
      System.Collections.ObjectModel.Collection``1[System.Attribute]
      $attributeCollection.Add($attributes)
      $paramDictionary = New-Object
      System.Management.Automation.RuntimeDefinedParameterDictionary
      $paramDictionary.Add("dp1", $dynParam1)
      return $paramDictionary
    }
  }
}

#param block
function FindStr1 ([string]$str, [int]$start_pos = 0) { … }
function FindStr2 { param ([string]$str, [int]$start_pos = 0) … }

#parallel statement
Parallel { continue; }

#sequence
Sequence { continue; }

#inlinescript
inlinescript { $i=0; }

#parameter binding
function Get-Power ([long]$base, [int]$exponent) { … }
Get-Power 5 3	# argument 5 is bound to parameter $base in position 0
# argument 3 is bound to parameter $exponent in position 1
# no conversion is needed, and the result is 5 to the power 3
Get-Power 4.7 3.2	# double argument 4.7 is rounded to int 5, double argument
# 3.2 is rounded to int 3, and result is 5 to the power 3
Get-Power 5	# $exponent has value $null, which is converted to int 0
Get-Power	# both parameters have value $null, which is converted to int 0

function Get-Hypot ([double]$side1, [double]$side2)
{
  return [Math]::Sqrt($side1 * $side1 + $side2 * $side2)
}

function Test
{
  [CmdletBinding(DefaultParameterSetname = "SetB")]
  param ([Parameter(Position = 0, ParameterSetname = "SetA")]
  [decimal]$dec,
  [Parameter(Position = 0, ParameterSetname = "SetB")]
  [int]$in
  )
  $PsCmdlet.ParameterSetName
}

Test 42d  # outputs "SetA"
Test 42   # outputs "SetB"

$fields = @(
          New-Object "System.Management.Automation.Host.FieldDescription" "Input"
          New-Object "System.Management.Automation.Host.FieldDescription" "Input List"
          )
$fields = $(
          New-Object "System.Management.Automation.Host.FieldDescription" "Input"
          New-Object "System.Management.Automation.Host.FieldDescription" "Input List"
          )
$fields[1].SetParameterType([int[]])

#call arguments
$SomeVar.call($($d = 3))
$SomeVar.call($param1, $($d = 3))
$SomeVar.call($param1, $(1,2,3))
$SomeVar.call({$d = 3})
$SomeVar.call{$d = 3}
#nls
$a.Length
(1)
$a.Length    #
(1)
$a.Length    #
1