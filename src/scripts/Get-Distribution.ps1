<#
    .SYNOPSIS
        This script gets the distribution file available in the path passed to it.
    .PARAMETER DistributionsPath
        Path to the directory containing compressed plugin distribution.
#>
param (
    [Parameter(Mandatory = $true)]
    [string] $DistributionsPath
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$file = Get-Item $DistributionsPath/*.zip
if (!$file) {
    throw "File not found in $DistributionsPath"
}
if (@($file).Count -gt 1) {
    throw "Found more files than expected in ${DistributionsPath}: $($file.Count)"
}

return $file
