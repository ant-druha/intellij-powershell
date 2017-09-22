$items = 10,"blue",12.54e3,16.30D	# 1-D array of length 4
$items[1] = -2.345
$items[2] = "green"
$a = New-Object 'object[,]' 2,2		# 2-D array of length 4
$a[0,0] = 10
$a[0,1] = $false
$a[1,0] = "red"
$a[1,1] = $null

#array creation
$values = 10,20,30
for ($i = 0; $i -lt $values.Length; ++$i)
{
  "`$values[$i] = $($values[$i])"
}
$x = ,10									# x refers to an array of length 1
$x = @(10) 								# x refers to an array of length 1
$x = @()									# x refers to an array of length 0
$a = New-Object 'object[,]' 2,2	# create a 2x2 array of anything
$a[0,0] = 10							# set to an int value
$a[0,1] = $false						# set to a boolean value
$a[1,0] = "red"						# set to a string value
$a[1,1] = 10.50D						# set to a decimal value
foreach ($e in $a)					# enumerate over the whole array
{
  $e
}
$values[0] = 10
$values[1] = 20
$values[2] = 30

#Constraining element types
$a = [int[]](1,2,3,4)	# constrained to int
$a[1] = "abc"				# implementation-defined behavior
$a += 1.23					# new array is unconstrained
$a = [int[]](1,+2,-3)		# constrained to int
$b = [int[]](10,20)		# constrained to int
$c = $a + $b				# constraint not preserved
$c = [int[]]($a + $b)	# result explicitly constrained to int

#array as reference type
$a = 10,20			# $a refers to an array of length 2
$a = 10,20,30		# $a refers to a different array, of length 3
$a = "red",10.6	# $a refers to a different array, of length 2
$a = New-Object 'int[,]' 2,3	# $a refers to an array of rank 2

$a = 10,20,30
">$a<"
$b = $a			# make $b refer to the same array as $a
">$b<"
$a[0] = 6		# change value of [0] via $a
">$a<"
">$b<"			# change is reflected in $b
$b += 40			# make $b refer to a new array
$a[0] = 8		# change value of [0] via $a
">$a<"
">$b<"			# change is not reflected in $b

#arrays as elements
$colors = "red", "blue", "green"
$list = $colors, (,7), (1.2, "yes")	# parens in (,7) are redundant; they
$list2 = $colors, ,7, (1.2, "yes")	# parens in (,7) are redundant; they
# are intended to aid readability
"`$list refers to an array of length $($list.Length)"
">$($list[1][0])<"
">$($list[2][1])<"
$x = [string[]]("red","green")
$y = 12.5, $true, "blue"
$a = New-Object 'object[,]' 2,2
$a[0,0] = $x					# element is an array of 2 strings
$a[0,1] = 20					# element is an int
$a[1,0] = $y					# element is an array of 3 objects
$a[1,1] = [int[]](92,93) 	# element is an array of 2 ints

#copying
$a = [int[]](10,20,30)
$b = [int[]](0,1,2,3,4,5)
[Array]::Copy($a, $b, 2)			# $a[0]->$b[0], $a[1]->$b[1]
[Array]::Copy($a, 1, $b, 3, 2)	# $a[1]->$b[3], $a[2]->$b[4]

#enumerating
$a = 10,53,16,-43
foreach ($elem in $a) {
# do something with element via $e
}
foreach ($elem in -5..5) {
# do something with element via $e
}
$a = New-Object 'int[,]' 3,2
foreach ($elem in $a) {
# do something with element via $e
}

#9.12 Multidimensional array flattening
$a = "red",$true
$b = (New-Object 'int[,]' 2,2)
$b[0,0] = 10
$b[0,1] = 20
$b[1,0] = 30
$b[1,1] = 40
$c = $a + $b