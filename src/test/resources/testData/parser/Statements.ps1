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
The caller to Get-Factorial gets back an int.
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