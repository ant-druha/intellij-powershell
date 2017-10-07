">$a.Length<"                           #results in >100 200.Length<
">$($a.Length)<"                        #results in >2<

$count = 10
"The value of `$count is $count"        #The value of $count is 10.

$a = "red","blue"
# second [0] is taken literally
"`$a[0] is $a[0], `$a[0] is $($a[0])"	#result: $a[0] is red blue[0], $a[0] is red

$count = 10
"$count + 5 is $($count + 5)"
"$count + 5 is `$($count + 5)"
"$count + 5 is `$(`$count + 5)"

$i = 5; $j = 10; $k = 15
"`$i, `$j, and `$k have the values $( $i; $j; $k )" #$i, $j, and $k have the values 5 10 15
"`$i, `$j, and `$k have the values $(($i = 5); ($j = 10); ($k = 15))"
"First 10 squares: $(for ($i = 1; $i -le 10; ++$i) { "$i $($i*$i) " })"

$a = 10
$s1 = "`$a = $($a; ++$a)"
"`$s1 = >$s1<"
$s2 = "`$a = $($a; ++$a)"
"`$s2 = >$s2<"
$s2 = $s1
"`$s2 = >$s2<"

"lonely dollar $ is here$"

"$sss::log"