param (
    [Parameter(Mandatory = $false)][string]$Currency,
    [Parameter(Mandatory = $false)][string]$Locale, [Parameter(Mandatory = $false)][string]$RegionInfo,
    [Parameter(Mandatory = $false)][string]$OfferDurableId,
    [Parameter(Mandatory = $false)][bool]$propagatetags = $true,
    [Parameter(Mandatory = $false)][string]$syncInterval,
    [Parameter(Mandatory = $false)] [bool] $clearLocks = $false
)
#region Login to Azure account and select the subscription.
#Authenticate to Azure with SPN section
Write-Verbose "Logging in to Azure..."
$Conn = Get-AutomationConnection -Name AzureRunAsConnection