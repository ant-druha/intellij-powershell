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

#logical not
-not $true				# False
-not -not $false		# False
-not 0					# True
-not 1.23				# False
!"xyz"					# False

#bitwise not
-bnot $true				# int with value 0xFFFFFFFE
-bnot 10					# int with value 0xFFFFFFF5
-bnot 2147483648.1	# long with value 0xFFFFFFFF7FFFFFFF
-bnot $null				# int with value 0xFFFFFFFF
-bnot "0xabc"			# int with value 0xFFFFF543

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