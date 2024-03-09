<#
    .SYNOPSIS
        This script publishes the plugin distribution from the $DistributionsLocation to the JetBrains Marketplace.
    .PARAMETER SourceRootPath
        Path to the project source root.
    .PARAMETER DistributionsPath
        Path to the directory containing compressed plugin distribution.
    .PARAMETER PluginXmlId
        Plugin identifier.
    .PARAMETER Channel
        Channel name to publish the plugin.
    .PARAMETER AuthToken
        Token to authenticate to the Marketplace.
#>
param (
    [string] $SourceRootPath = "$PSScriptRoot/../..",
    [string] $DistributionsPath = "$SourceRootPath/build/distributions",
    [string] $PluginXmlId = 'com.intellij.plugin.adernov.powershell',
    [Parameter(Mandatory = $true)]
    [string] $AuthToken
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$file = & "$PSScriptRoot/Get-Distribution.ps1" -DistributionsPath $DistributionsPath
curl -i `
    --fail-with-body `
    --header "Authorization: Bearer $AuthToken" `
    -F xmlId=$PluginXmlId `
    -F file=@$file `
    https://plugins.jetbrains.com/plugin/uploadPlugin

if (!$?) {
   throw "Curl failed with exit code $LASTEXITCODE"
}
