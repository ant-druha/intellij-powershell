<#
  .DESCRIPTION
    This script will update the PowerShell modules used by the plugin.
#>
param (
  $SourceRoot = "$PSScriptRoot/../..",
  $GradleProperties = "$SourceRoot/gradle.properties"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function ReadCurrentVersions
{
  $properties = @{}
  foreach ($line in Get-Content $GradleProperties)
  {
    if ($line.Trim().StartsWith('#') -or $line.Trim().Length -eq 0)
    {
      continue
    }
    $pair = $line.Split('=', 2)
    $properties[$pair[0].Trim()] = $pair[1].Trim()
  }

  @{
    PSScriptAnalyzer = [Version] $properties['psScriptAnalyzerVersion']
    PowerShellEditorServices = [Version] $properties['psesVersion']
  }
}

$psScriptAnalyzer = Invoke-RestMethod `
    'https://api.github.com/repos/PowerShell/PSScriptAnalyzer/releases?per_page=1'
$pses = Invoke-RestMethod 'https://api.github.com/repos/PowerShell/PowerShellEditorServices/releases?per_page=1'

function ReadLatestVersions
{
  @{
    PSScriptAnalyzer = [Version] $psScriptAnalyzer.tag_name
    PowerShellEditorServices = [Version] $pses.tag_name.Trim('v')
  }
}

function FetchVersionDetails($dependency)
{
  $details = switch ($dependency)
  {
    'PSScriptAnalyzer' { $psScriptAnalyzer }
    'PowerShellEditorServices' { $pses }
    default { throw "Unknown dependency: $dependency" }
  }
  if ($details.assets.Count -gt 1)
  {
    throw "Dependency $dependency has more than 1 release asset ($($details.assets.Count))."
  }

  $releaseAsset = $details.assets[0]
  @{
    Name = $dependency
    Version = $details.tag_name.Trim('v')
    ReleaseNotes = $details.body
    DownloadUrl = $releaseAsset.browser_download_url
  }
}

function UpdateProperty($text, $key, $value)
{
  $text -replace "$([Regex]::Escape($key))=[^\n]*", "$key=$value"
}

function UpdateProperties($properties, $updates, $hashes)
{
  foreach ($dependency in $updates.Keys)
  {
    $key = switch ($dependency)
    {
      'PSScriptAnalyzer' { 'psScriptAnalyzer' }
      'PowerShellEditorServices' { 'pses' }
      default { throw "Unknown dependency: $dependency" }
    }
    $properties = UpdateProperty $properties "$($key)Version" $updates[$dependency].Version
    $properties = UpdateProperty $properties "$($key)Sha256Hash" $hashes[$dependency]
  }

  $properties
}

function GetArtifactHashes($updates)
{
  $hashes = @{}
  foreach ($dependency in $updates.Keys)
  {
    $tempFile = New-TemporaryFile
    Invoke-RestMethod $updates[$dependency].DownloadUrl -OutFile $tempFile
    $hash = (Get-FileHash -Algorithm SHA256 $tempFile).Hash
    $hashes[$dependency] = $hash
  }
  $hashes
}

function ApplyUpdates($updates)
{
  $properties = Get-Content -Raw $GradleProperties
  $hashes = GetArtifactHashes $updates
  $newContent = UpdateProperties $properties $updates $hashes
  [IO.File]::WriteAllText($GradleProperties, $newContent)
}

$currentVersions = ReadCurrentVersions
$latestVersions = ReadLatestVersions
$updates = @{ }

foreach ($dependency in $currentVersions.Keys)
{
  $currentVersion = $currentVersions[$dependency]
  $latestVersion = $latestVersions[$dependency]
  if ($latestVersion -gt $currentVersion)
  {
    Write-Host "A dependency $dependency has version $currentVersion and can be updated to $latestVersion."
    $updates[$dependency] = FetchVersionDetails $dependency
  }
}

$hasChanges = $updates.Count -gt 0
if ($hasChanges)
{
  $branchName = 'dependencies/' + ($updates.Keys -join '+')
  [array] $versionUpdateStrings = $updates.Values | ForEach-Object { "$($_.Name) to v$($_.Version)" }
  $prTitle = 'Update ' + ($versionUpdateStrings -join ' and ')
  $commitMessage = $prTitle
  [array] $updateReleaseNoteStrings = $updates.Values | ForEach-Object {
    @"
## $($_.Name) v$($_.Version)
$($_.ReleaseNotes)
"@
  }
  $prBody = @'
# Maintainer Note
> [!WARNING]
> This PR will not trigger CI by default. Please close it and reopen manually to trigger the CI.
>
> Unfortunately, this is a consequence of the current GitHub Action security model (by default, PRs created by bots
> aren't allowed to trigger other bots).

The updates packages' release notes follow below.

'@ + $updateReleaseNoteStrings -join "`n"
}

ApplyUpdates $updates

$result = if ($hasChanges)
{
  $prBodyPath = New-TemporaryFile
  $prBody > $prBodyPath
  @"
has-changes=true
branch-name=$branchName
commit-message=$commitMessage
pr-title=$prTitle
pr-body-path=$prBodyPath
"@
}
else
{
  'has-changes=false'
}

$result >> $env:GITHUB_OUTPUT
