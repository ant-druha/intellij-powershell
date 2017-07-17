#1) value expression in single line 2) cmdlet name token
foreach ($i in get-childitem | sort-object length)
 {
 $i  #highlight
 $sum += $i.length
 }

# 3) variable name token: '$_.length ' and 'get-childitem'
switch -regex -casesensitive (get-childitem | sort length)
{
 "^5" {"length for $_ started with 5" ; continue}
 { $_.length > 20000 } {"length of $_ is greater than 20000"}
 default {"Didn't match anything else..."}
}

# 4) statement termination by new line:
while ($i -lt 100)
 {
 echo i is $i
 $i -= 1
 }


# 5) param argument name and cmdlets
#Here are examples showing what pipelines may look like:
 get-childitem -recurse -filter *.ps1 | sort name
 (2+3),3,4 | sort
 & "c:\a path\with spaces.ps1" | % { $_.length }
 get-childitem | sort-object > c:/tmp/junk.txt

Get-Process | Where {$_.PM -gt 100MB}

# 6) parameter names (DS_ALNUM)
param ([string]$str, [int]$start_pos = 0)
1


# 7) variable variable names:
$totalCost
$Maximum_Count_26
$végösszeg							# Hungarian
$итог									# Russian
$総計									# Japanese (Kanji)
${Maximum_Count_26}
${Name with`twhite space and `{punctuation`}}
${E:\File.txt}

# 8) verbatim command argument:
echoargs.exe --% "%path%"  # %path% is replaced with the value $env:path

# 9) command name token:
echoargs.exe "my arg value"