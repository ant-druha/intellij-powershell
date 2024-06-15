Param(
    [string]$script,
    [array]$bps
    )

$scriptToDebug = $script

$breakpointAction = {
    Write-Host "Breakpoint hit at line: $($PSItem.Line)"
    Write-Host "Script: $($PSItem.Script)"
    Write-Host "Stack Trace Details:"
    Get-PSCallStack | ForEach-Object {
        Write-Host $_
    }
}

foreach ($breakpointLine in $bps)
{
    Set-PSBreakpoint -Script $scriptToDebug -Line $breakpointLine -Action $breakpointAction
}

& $scriptToDebug

Get-PSBreakpoint | Remove-PSBreakpoint
