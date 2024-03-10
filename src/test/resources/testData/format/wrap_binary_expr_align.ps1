function foo
{
    Write-Object (($j -gt
                   5) -and
                  ($i -lt
                   15))

    ($Error -eq
     $Null) -or
    ($Error -ne
     $Null)

    ($a -and
     $b) -eq
    !(!$a -or
      !$b)

    ($a -gt
     $b) -and
    (($a -lt
      20) -or
     ($b -lt
      20))

    (1 -eq
     1) -and
    -not (2 -gt
          2)

    !($Null)

    $a -and
    $b

    $a -or
    $b

    $a -xor
    $b

    $a -band
    $b

    -not ($a)
    !($a)

    "abcdef" -like
    "abc*"
}
