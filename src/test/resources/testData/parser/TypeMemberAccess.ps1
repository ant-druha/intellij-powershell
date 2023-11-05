[System.Environment]::Exit(0)
[System.Environment]::ExitCode = 0
$null = Register-ObjectEvent -InputObject $process -EventName 'Exited' -Action { [System.Environment]::Exit(0) }
