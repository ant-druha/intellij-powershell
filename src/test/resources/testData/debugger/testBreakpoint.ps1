$val = 0
$result = Get-Variable
while($val -ne 3)
{
    $val++
    Write-Host $val
}
