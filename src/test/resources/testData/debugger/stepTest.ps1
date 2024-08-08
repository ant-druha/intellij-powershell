function Add-Numbers {
    param(
        [double]$num1,
        [double]$num2
    )

    $result = $num1 + $num2
    return $result
}
Write-Output "Starting the script..."
$result = Add-Numbers -num1 10 -num2 5
Write-Output "The result is: $result"
Write-Output "End of the script."
