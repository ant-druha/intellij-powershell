$val = 0
$result = Get-Variable

.".\secondFileTest.ps1"
Write-Host (GetVal)

while($val -ne 3)
{
  $val++
  Write-Host $val
}
