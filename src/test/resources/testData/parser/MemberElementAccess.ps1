#member access:
$a = 10, 20, 30
$a.Length              # get instance property
(10, 20, 30).Length
$property = "Length"
$a.$property            # property name is a variable
$h1 = @{FirstName = "James"; LastName = "Anderson"; IDNum = 123}
$h1.FirstName            # designates the key FirstName
$h1.Keys                # gets the collection of keys
[int]::MinValue          # get static property
[double]::PositiveInfinity    # get static property
$property = "MinValue"
[long]::$property          # property name is a variable
foreach ($t in [byte], [int], [long])
{
  $t::MaxValue          # get static property
}
$a = @{ID=1}, @{ID=2}, @{ID=3}
$a.ID                  # get ID from each element in the array

#element access:
$a = [int[]](10, 20, 30)    # [int[]], Length 3
$a[1]                # returns int 20
$a[20]              # no such position, returns $null
$a[-1]              # returns int 30, i.e., $a[$a.Length-1]
$a[2] = 5            # changes int 30 to int 5
$a[20] = 5            # implementation-defined behavior
$a = New-Object 'double[,]' 3, 2
$a[0, 0] = 10.5          # changes 0.0 to 10.5
$a[0, 0]++            # changes 10.5 to 10.6
$list = ("red", $true, 10), 20, (1.2, "yes")
$list[2][1]            # returns string "yes"
$a = @{A = 10}, @{B = $true}, @{C = 123.45}
$a[1]["B"]            # $a[1] is a Hashtable, where B is a key
$a = "red", "green"
$a[1][4]              # returns string "n" from string in $a[1]