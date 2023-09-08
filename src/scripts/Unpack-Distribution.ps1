<#
    .SYNOPSIS
        The purpose of this script is to unpack the compressed plugin artifact.

        It is used during CI builds to generate the layout for uploading.
    .PARAMETER SourceRootPath
        Path to the project source root.
    .PARAMETER DistributionsPath
        Path to the directory containing compressed plugin distribution.
#>
param (
    [string] $SourceRootPath = "$PSScriptRoot/../..",
    [string] $DistributionsPath = "$SourceRootPath/build/distributions"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$file = & "$PSScriptRoot/Get-Distribution.ps1" -DistributionsPath $DistributionsPath

Expand-Archive -Path $file -DestinationPath $DistributionsPath/unpacked
