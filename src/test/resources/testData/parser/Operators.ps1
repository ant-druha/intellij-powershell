#7.1.6 $(…) operator
$j = 20
$()                # null
$($i = 10)        # pipeline gets nothing
$(($i = 10))      # pipeline gets int 10
$($i = 10; $j)    # pipeline gets int 20
$(($i = 10); $j)    # pipeline gets [object[]](10,20)
$(($i = 10); ++$j)  # pipeline gets int 10
$(($i = 10); (++$j))  # pipeline gets [object[]](10,22)
$(2, 4, 6)        # pipeline gets [object[]](2,4,6)

#7.1.7 @(…) operator
$j = 20
@()                # null
@($i = 10)        # 10 not written to pipeline, result is array of 0
@(($i = 10))      # pipeline gets 10, result is array of 1
@($i = 10; $j)    # 10 not written to pipeline, result is array of 1
@(($i = 10); $j)    # pipeline gets 10, result is array of 2
@(($i = 10); ++$j)  # pipeline gets 10, result is array of 1
@(($i = 10); (++$j))  # pipeline gets both values, result is array of 2
@($i = 10; ++$j)    # pipeline gets nothing, result is array of 0
$a = @(2, 4, 6)      # result is array of 3
@($a)            # result is the same array of 3
@(@($a))        # result is the same array of 3


#unary op
$a = , 10        # create an unconstrained array of 1 element, $a[0], which has type int
$a = , (10, "red")  # create an unconstrained array of 1 element, $a[0],  which is an unconstrained array of 2 elements,
# $a[0][0] an int, and $a[0][1] a string
$a = ,, 10      # create an unconstrained array of 1 element, which is an unconstrained array of 1 element, which is an int
# $a[0][0] is the int. Contrast this with @(@(10))
-join 124
-split 124
!122
++12
--323

#logical not
-not $true				# False
-not -not $false		# False
-not 0					# True
-not 1.23				# False
!"xyz"					# False

#bitwise
-bnot $true				      # int with value 0xFFFFFFFE
-bnot 10					      # int with value 0xFFFFFFF5
-bnot 2147483648.1	    # long with value 0xFFFFFFFF7FFFFFFF
-bnot $null				      # int with value 0xFFFFFFFF
-bnot "0xabc"			      # int with value 0xFFFFF543
0x0F0F -band 14.6       # long with value 0xF
0x0F0F -bor 0xFEL       # long with value 0xFFF
0x0F0F -bxor 0xFE       # int with value 0xFF1
0x0F0F -bxor 14.40D     # long with value 0xF01

#unary plus/minus
+123L				# type long, value 123
+0.12340D		# type decimal, value 0.12340
+"0xabc"			# type int, value 2748
-$true			# type int, value -1
-123L				# type long, value -123
-0.12340D		# type decimal, value -0.12340

$i = 0            # $i = 0
++$i							# $i is incremented by 1
$j = --$i          # $i is decremented then $j takes on the value of $i

#range
1..10           # ascending range 1..10
-500..-495      # descending range -500..-495
123kb..34
123L..34
2.1..12
$x = 1.5
$x..5.40D       # ascending range 2..5
$true..3        # ascending range 1..3
-2..$null       # ascending range -2..0
"0xf".."0xa"    # descending range 15..10

#format op
"{2} <= {0} + {1}`n" -f $i,$j,($i+$j)
$format = ">{0:x8}<"
$format -f    123455
$format -F    -1234.56

#multiplication
12 * -10L            # long result -120
-10.300D * 12        # decimal result -123.600
10.6 * 12            # double result 127.2
12 * "0xabc"        # int result 32976
3*3
$abc * "0xabc"
"red" * "3"          # string replicated 3 times
"red" * 4            # string replicated 4 times
(New-Object 'float[,]' 2, 3) * 2    # [object[]], Length 2*2

#division
10/-10      # int result -1.2
12/-10      # double result -1.2
12/-10D     # decimal result 1.2
12/10.6     # double result 1.13207547169811
12/"0xabc"  # double result 0.00436681222707424

#reminder
10%3	    					# int result 1
10.0 % 0.3					# double result 0.1
10.00D%"0x4"				# decimal result 2.00

#additive
12 + -10L					# long result 2
-10.300D + 12			# decimal result 1.700
10.6 + 12					# double result 22.6
12 + "0xabc"			# int result 2760

#string concat
"red" + "123"						  # "red123"
"red" + 123							  # "red123"
"red" + 123.456e+5				# "red12345600"
"red" + (20,30,40)				# "red20 30 40"

#array concat
$a + (New-Object 'float[,]' 2,3)		# [object[]], Length 8

#substraction
12 - -10L				# long result 2c
-10.300D - 12			# decimal result -22.300
10.6 - 12				# double result -1.4
12 - "0xabc"			# int result -2736

#comparisoin: equality
10 -eq "010"				# True, int comparison
"010" -eq 10				# False, string comparison
"RED" -eq "Red"			# True, case-insensitive comparison
"RED" -ceq "Red"			# False, case-sensitive comparison
"ab" -lt "abc"				# True
10,20,30,20,10 -ne 20	# 10,30,10, Length 3
10,20,30,20,10 -eq 40	# Length 0
10,20,30,20,10 -gt 25	# 30, Length 1
0,1,30 -ne $true			# 0,30, Length 2
0,"00" -eq "0"				# 0 (int), Length 1

#comparison: containment
10,20,30,20,10 -contains 42.9		# False
10,20,30 -contains "10"				# True
"010",20,30 -contains 10			# False
10,20,30,20,10 -notcontains 15	# True
"Red",20,30 -ccontains "RED"		# False

#comparison: type testing
$a = 10							# value 10 has type int
$a -is [int]					# True
$t = [int]
$a -isnot $t					# False
$a -is "int"					# True
$a -isnot [double] 			# True
$x = [int[]](10,20)
$x -is [int[]]					# True
$a = "abcd"						# string is derived from object
$a -is [object] 				# True
$x = [double]
10.60D -as $t

#comparison: pattern matching
"Hello" -like "h*"				# True, starts with h
"Hello" -clike "h*"				# False, does not start with lowercase h
"#$%^&" -notlike "*[A-Za-z]"	# True, does not end with alphabetic character
"abc","abbcde","abcgh" -like "abc*"	# object[2], values "abc" and "abcgh"

"Hello" -match '^h.*o$'			# True, $matches key/value is 0/"Hello"
"Hello" -cmatch '^h.*o$'		# False, $matches not set
"abc" -notmatch "[A-Za-z]"		# False
"abc","abbcde","abcgh" -match "abc.*"	# Length is 2, values "abc", "abcgh"

#text replacement operator
"Analogous","an apple" -replace "a","*"	# "*n*logous","*n *pple"
"Analogous" -creplace "[aeiou]","?"			# "An?l?g??s"
"Analogous","an apple" -replace '^a',"%%A" # "%%Analogous","%%An apple"
"Analogous" -replace "[aeiou]",'$&$&'		# "AAnaaloogoouus"

#binary join
(10, 20, 30) -join "|"      # result is "10|20|30"
12345 -join ","          # result is "12345", no separator needed
($null, $null) -join "<->"    # result is "<->", two zero-length values

#binry split
"one,forty two,," -split ","		# 5 strings: "one" "forty two" "" ""
"abc","de" -split ""					# 9 strings: "" "a" "b" "c" "" "" "d"
"10X20x30" -csplit "X", 0, "SimpleMatch"	# 2 strings: "10" "20x30"

#shift op
0x0408 -shl 1					# int with value 0x0810
0x100000000 -shr 0xfff81	# long with value 0x80000000

#logical
($j -gt 5) -and (++$k -lt 15)		# True -and False -> False
($j -eq 10) -or (++$k -le 20)		# False -or False -> False
($j -eq 10) -xor ($k -gt 15)		# False -xor True -> True

#assignment
$hypot = [Math]::Sqrt(3*3 + 4*4)  # type double, value 5
$a = $b = $c = 10.20D             # all have type decimal, value 10.20
$a = (10, 20, 30), (1, 2)         # type [object[]], Length 2
[int]$x = 10.6                    # type int, value 11
[long]$x = "0xabc"                # type long, value 0xabc
$a = [float]                      # value type literal [float]
$i, $j, $k = 10, "red", $true     # $i is 10, $j is "red", $k is True
$i, $j = 10, "red", $true         # $i is 10, $j is [object[]], Length 2
$i, $j, $k = 10                   # $i is 10, $j is $null, $k is $null
$h = @{}
[int] $h.Lower, [int] $h.Upper = -split "10 100"
$h1 = @{FirstName = "James"; LastName = "Anderson"; IDNum = 123}
$h1.Dept = "Finance"              # adds element Finance
$h1["City"] = "New York"          # adds element City
[int]$Variable:v = 123.456       # v takes on the value 123
${E:output.txt} = "a"				      # write text to the given file
$Env:MyPath = "x:\data\file.txt"	# define the environment variable
$Function:F = { param ($a, $b) "Hello there, $a, $b" }
F 10 "red"			    					    # define and invoke a function
function Demo { "Hi there from inside Demo" }
$Alias:A = "Demo"		  				    # create alias for function Demo
A											            # invoke function Demo via the alias

#compound assignment
$a = 1234; $a *= (3 + 2)	    # type is int, value is 1234 * (3 + 2)
$b = 10,20,30					        # $b[1] has type int, value 20
$b[1] /= 6						        # $b[1] has type double, value 3.33…
$i = 0
$b[++$i] += 2					        # side effect evaluated only once
[int]$Variable:v = 10		      # v takes on the value 10
$Variable:v -= 3				      # 3 is subtracted from v
${E:output.txt} += "b"		    # append text to the file giving ab
${E:output.txt} *= 4			    # replicate ab 4 times giving abababab

#redirection op
$i = 200                      # pipeline gets nothing
$i                            # pipeline gets result
$i > output1.txt              # result redirected to named file
++$i >> output1.txt           # result appended to named file
type file1.txt 2> e_rror1.txt  # e_rror output redirected to named file
type file2.txt 2>> e_rror1.txt # e_rror output appended to named file
dir –Verbose 4> verbose1.txt  # verbose output redirected to named file
dir –Verbose *>> verbose1.txt # verbose output redirected to named file

# Send all output to output2.txt
dir –Verbose –Debug –WarningAction Continue *> output2.txt

# e_rror output redirected to named file, verbose output redirected
# to the same location as e_rror output
dir –Verbose 4>&2 2> e_rror2.txt

1 -eq$aa
1 -eqaa
1 -xoraa
1 -xor$aa
