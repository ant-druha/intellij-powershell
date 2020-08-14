$fruit = <fold text='@(...)'>@("Apples", "Oranges",
"Bananas")</fold>

$( for ($i = 1; $i -le 10; ++$i) {<fold text='...'>
    "$i $( $i*$i ) "
</fold>} )

function foo() {<fold text='...'>
    $i = 1
    while ($i -le 10) {<fold text='...'>
        $str = "Output $i"
        Write-Output $str
        $i = $i + 1
        Start-Sleep -Milliseconds $DelayMilliseconds
    </fold>}
</fold>}