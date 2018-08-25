function test
{
    foreach ($i in get-childitem | sort-object length)
    {
        $i  #highlight
        # comment here
        Write-Object (($j -gt 5) -and ($i -lt 15))
    }
}
function a()
{
    @"
 Date is: $( get-date )
"@
    Write-Object
}

function b()
{
    @"
 Date is: $( get-date )
"@
    Write-Object
}
function c()
{
    @'
 Date is: $( get-date )
'@
    Write-Object
}