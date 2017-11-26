foreach ($i in get-childitem | sort-object length) {
    $i  #highlight
    $i = $i + 1

    $sum += $i.length
    0x0F0F -band $i

    $j = 20 + $i
    # comment here
    Write-Object (($j -gt 5) -and ($i -lt 15))
}
#logical not
-not-not$false         # False
-not1.23                # False
!"xyz"                # False
[int] $x = 10.6         # type int, value 11
switch -regex (asas)
{
    " ^5" {


    }
}
# 3) variable name token: '$_.length ' and 'get-childitem'
switch -regex -casesensitive (get-childitem | sort length)
{
    " ^5" {

        "length for $_ started with 5"; continue
            "length for $_ started with 5"; continue
    }
    { $_.length >20000 } {
        "length of $_ is greater than 20000"
        "length of $_ is greater than 20000"
    }
    default { "Didn't match anything else..." }
}
function sjsjsj
{
}
function Get-Power(
        [int]
        $x,
        [int]
        $y)
{
    $dgdgd = 1
    $dgdgd = 1
    if ($y -gt 0) {
        return $x * (Get-Power $x (--$y))
    }
    elseif ($y -eq 0)
    { return 1 }
    else
    { return 1
    }
}
(New-Object 'float[,]' 2, 3) / 2    # [object[]], Length 2/2
$values = 10, 20, 30
for ($i = 0; $i -lt $values.Length; ++$i)
{
    "`$values[$i] = $( $values[$i] )"
    "`$values[$i] = $( $values[$i] )"

}
$a = new-object 'int[]' 10
$i = 20                # out-of-bounds subscript :go_here
while ($true)
{
    try
    {
        Write-Object "j = $j"
        $a[$i] = 10; "Assignment completed without error"
        break
    }
    catch [IndexOutOfRangeException],
          [IndexOutOfRangeException],
          [IndexOutOfRangeException]
    {
        "Handling out-of-bounds index, >$_<`n"
        $i = 5
    }
    catch
    {
        "Caught unexpected exception"
        "Caught unexpected exception"
    }
    finally
    {
        # …
    }
}
function ValidateScriptTest
{
    param (
    [Parameter(Mandatory = $true)]
    [ValidateScript({
                        ($_ -ge 1 -and $_ -le 3) -or ($_ -ge 20)
                    })]
    [int]
    $Count)
    $Count = 0
    $Count += 0
}

[DscResource()]
[DscResource()]
class Person
{
    [int] [int] $Age
    Person($foo)
    {
    }
    Person(
            [int]
            $a)
    {
        $this.Age = $a
    }
    [sdsdsd]
    [sdsdsd]
    DoSomething($x)
    {
        $this.DoSomething($x)
        $this.DoSomethingElse()
    }
    [dsds]
    [dsds]
    DoSomethingElse()
    {
    }
}
#constructor override
class Child : Person
{
    [string]
    $School

    Child(
            [int]
            $a,
            [string]
            $s) : base($a)
    {
        $this.School = $s
    }
}
[Child]$littleOne = [Child]::
        new(10, "Silver Fir Elementary School")
$littleOne.Age

do
{
    "{0,1}`t{1,2}" -f $i, ($i * $i)
} while (++$i -le 5)                    # loop 5 times