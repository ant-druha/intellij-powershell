#statement value
$v = for ($i = 1; $i -le 5; ++$i) { }     # The value is $null
$v = for ($i = 1; $i -le 5; ++$i) { $i }  # The value of the statement is object[] of Length 5.