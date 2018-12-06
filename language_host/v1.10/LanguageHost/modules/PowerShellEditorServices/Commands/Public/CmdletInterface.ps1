#
# Copyright (c) Microsoft. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.
#

<#
.EXTERNALHELP ..\PowerShellEditorServices.Commands-help.xml
#>
function Register-EditorCommand {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory=$true)]
        [ValidateNotNullOrEmpty()]
        [string]$Name,

        [Parameter(Mandatory=$true)]
        [ValidateNotNullOrEmpty()]
        [string]$DisplayName,

        [Parameter(
            Mandatory=$true,
            ParameterSetName="Function")]
        [ValidateNotNullOrEmpty()]
        [string]$Function,

        [Parameter(
            Mandatory=$true,
            ParameterSetName="ScriptBlock")]
        [ValidateNotNullOrEmpty()]
        [ScriptBlock]$ScriptBlock,

        [switch]$SuppressOutput
    )

    Process
    {
        $commandArgs = @($Name, $DisplayName, $SuppressOutput.IsPresent)

        if ($ScriptBlock -ne $null)
        {
            Write-Verbose "Registering command '$Name' which executes a ScriptBlock"
            $commandArgs += $ScriptBlock
        }
        else
        {
            Write-Verbose "Registering command '$Name' which executes a function"
            $commandArgs += $Function
        }

        $editorCommand = New-Object Microsoft.PowerShell.EditorServices.Extensions.EditorCommand -ArgumentList $commandArgs
        if ($psEditor.RegisterCommand($editorCommand))
        {
            Write-Verbose "Registered new command '$Name'"
        }
        else
        {
            Write-Verbose "Updated existing command '$Name'"
        }
    }
}

<#
.EXTERNALHELP ..\PowerShellEditorServices.Commands-help.xml
#>
function Unregister-EditorCommand {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory=$true)]
        [ValidateNotNullOrEmpty()]
        [string]$Name
    )

    Process
    {
        Write-Verbose "Unregistering command '$Name'"
        $psEditor.UnregisterCommand($Name);
    }
}

<#
.SYNOPSIS
    Creates new files and opens them in your editor window
.DESCRIPTION
    Creates new files and opens them in your editor window
.EXAMPLE
    PS > New-EditorFile './foo.ps1'
    Creates and opens a new foo.ps1 in your editor
.EXAMPLE
    PS > Get-Process | New-EditorFile proc.txt
    Creates and opens a new foo.ps1 in your editor with the contents of the call to Get-Process
.EXAMPLE
    PS > Get-Process | New-EditorFile proc.txt -Force
    Creates and opens a new foo.ps1 in your editor with the contents of the call to Get-Process. Overwrites the file if it already exists
.INPUTS
    Path
    an array of files you want to open in your editor
    Value
    The content you want in the new files
    Force
    Overwrites a file if it exists
#>
function New-EditorFile {
    [CmdletBinding()]
    param(
        [Parameter()]
        [String[]]
        [ValidateNotNullOrEmpty()]
        $Path,

        [Parameter(ValueFromPipeline=$true)]
        $Value,

        [Parameter()]
        [switch]
        $Force
    )

    begin {
        $valueList = @()
    }

    process {
        $valueList += $Value
    }

    end {
        if ($Path) {
            foreach ($fileName in $Path)
            {
                if (-not (Test-Path $fileName) -or $Force) {
                    New-Item -Path $fileName -ItemType File | Out-Null

                    if ($Path.Count -gt 1) {
                        $preview = $false
                    } else {
                        $preview = $true
                    }

                    $psEditor.Workspace.OpenFile($fileName, $preview)
                    $psEditor.GetEditorContext().CurrentFile.InsertText(($valueList | Out-String))
                } else {
                    $PSCmdlet.WriteError( (
                        New-Object -TypeName System.Management.Automation.ErrorRecord -ArgumentList @(
                            [System.IO.IOException]"The file '$fileName' already exists.",
                            'NewEditorFileIOError',
                            [System.Management.Automation.ErrorCategory]::WriteError,
                            $fileName) ) )
                }
            }
        } else {
            $psEditor.Workspace.NewFile()
            $psEditor.GetEditorContext().CurrentFile.InsertText(($valueList | Out-String))
        }
    }
}

function Open-EditorFile {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory=$true, ValueFromPipeline=$true)]
        [ValidateNotNullOrEmpty()]
        $Path
    )

    begin {
        $Paths = @()
    }

    process {
        $Paths += $Path
    }

    end {
        if ($Paths.Count -gt 1) {
            $preview = $false
        } else {
            $preview = $true
        }

        Get-ChildItem $Paths -File | ForEach-Object {
            $psEditor.Workspace.OpenFile($_.FullName, $preview)
        }
    }
}
Set-Alias psedit Open-EditorFile -Scope Global

Export-ModuleMember -Function Open-EditorFile,New-EditorFile

# SIG # Begin signature block
# MIIkSAYJKoZIhvcNAQcCoIIkOTCCJDUCAQExDzANBglghkgBZQMEAgEFADB5Bgor
# BgEEAYI3AgEEoGswaTA0BgorBgEEAYI3AgEeMCYCAwEAAAQQH8w7YFlLCE63JNLG
# KX7zUQIBAAIBAAIBAAIBAAIBADAxMA0GCWCGSAFlAwQCAQUABCAg3EaoDsJtfK0t
# ckHgxRjzDijjpPNlWqXazM5LihRwkKCCDYEwggX/MIID56ADAgECAhMzAAABA14l
# HJkfox64AAAAAAEDMA0GCSqGSIb3DQEBCwUAMH4xCzAJBgNVBAYTAlVTMRMwEQYD
# VQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdSZWRtb25kMR4wHAYDVQQKExVNaWNy
# b3NvZnQgQ29ycG9yYXRpb24xKDAmBgNVBAMTH01pY3Jvc29mdCBDb2RlIFNpZ25p
# bmcgUENBIDIwMTEwHhcNMTgwNzEyMjAwODQ4WhcNMTkwNzI2MjAwODQ4WjB0MQsw
# CQYDVQQGEwJVUzETMBEGA1UECBMKV2FzaGluZ3RvbjEQMA4GA1UEBxMHUmVkbW9u
# ZDEeMBwGA1UEChMVTWljcm9zb2Z0IENvcnBvcmF0aW9uMR4wHAYDVQQDExVNaWNy
# b3NvZnQgQ29ycG9yYXRpb24wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB
# AQDRlHY25oarNv5p+UZ8i4hQy5Bwf7BVqSQdfjnnBZ8PrHuXss5zCvvUmyRcFrU5
# 3Rt+M2wR/Dsm85iqXVNrqsPsE7jS789Xf8xly69NLjKxVitONAeJ/mkhvT5E+94S
# nYW/fHaGfXKxdpth5opkTEbOttU6jHeTd2chnLZaBl5HhvU80QnKDT3NsumhUHjR
# hIjiATwi/K+WCMxdmcDt66VamJL1yEBOanOv3uN0etNfRpe84mcod5mswQ4xFo8A
# DwH+S15UD8rEZT8K46NG2/YsAzoZvmgFFpzmfzS/p4eNZTkmyWPU78XdvSX+/Sj0
# NIZ5rCrVXzCRO+QUauuxygQjAgMBAAGjggF+MIIBejAfBgNVHSUEGDAWBgorBgEE
# AYI3TAgBBggrBgEFBQcDAzAdBgNVHQ4EFgQUR77Ay+GmP/1l1jjyA123r3f3QP8w
# UAYDVR0RBEkwR6RFMEMxKTAnBgNVBAsTIE1pY3Jvc29mdCBPcGVyYXRpb25zIFB1
# ZXJ0byBSaWNvMRYwFAYDVQQFEw0yMzAwMTIrNDM3OTY1MB8GA1UdIwQYMBaAFEhu
# ZOVQBdOCqhc3NyK1bajKdQKVMFQGA1UdHwRNMEswSaBHoEWGQ2h0dHA6Ly93d3cu
# bWljcm9zb2Z0LmNvbS9wa2lvcHMvY3JsL01pY0NvZFNpZ1BDQTIwMTFfMjAxMS0w
# Ny0wOC5jcmwwYQYIKwYBBQUHAQEEVTBTMFEGCCsGAQUFBzAChkVodHRwOi8vd3d3
# Lm1pY3Jvc29mdC5jb20vcGtpb3BzL2NlcnRzL01pY0NvZFNpZ1BDQTIwMTFfMjAx
# MS0wNy0wOC5jcnQwDAYDVR0TAQH/BAIwADANBgkqhkiG9w0BAQsFAAOCAgEAn/XJ
# Uw0/DSbsokTYDdGfY5YGSz8eXMUzo6TDbK8fwAG662XsnjMQD6esW9S9kGEX5zHn
# wya0rPUn00iThoj+EjWRZCLRay07qCwVlCnSN5bmNf8MzsgGFhaeJLHiOfluDnjY
# DBu2KWAndjQkm925l3XLATutghIWIoCJFYS7mFAgsBcmhkmvzn1FFUM0ls+BXBgs
# 1JPyZ6vic8g9o838Mh5gHOmwGzD7LLsHLpaEk0UoVFzNlv2g24HYtjDKQ7HzSMCy
# RhxdXnYqWJ/U7vL0+khMtWGLsIxB6aq4nZD0/2pCD7k+6Q7slPyNgLt44yOneFuy
# bR/5WcF9ttE5yXnggxxgCto9sNHtNr9FB+kbNm7lPTsFA6fUpyUSj+Z2oxOzRVpD
# MYLa2ISuubAfdfX2HX1RETcn6LU1hHH3V6qu+olxyZjSnlpkdr6Mw30VapHxFPTy
# 2TUxuNty+rR1yIibar+YRcdmstf/zpKQdeTr5obSyBvbJ8BblW9Jb1hdaSreU0v4
# 6Mp79mwV+QMZDxGFqk+av6pX3WDG9XEg9FGomsrp0es0Rz11+iLsVT9qGTlrEOla
# P470I3gwsvKmOMs1jaqYWSRAuDpnpAdfoP7YO0kT+wzh7Qttg1DO8H8+4NkI6Iwh
# SkHC3uuOW+4Dwx1ubuZUNWZncnwa6lL2IsRyP64wggd6MIIFYqADAgECAgphDpDS
# AAAAAAADMA0GCSqGSIb3DQEBCwUAMIGIMQswCQYDVQQGEwJVUzETMBEGA1UECBMK
# V2FzaGluZ3RvbjEQMA4GA1UEBxMHUmVkbW9uZDEeMBwGA1UEChMVTWljcm9zb2Z0
# IENvcnBvcmF0aW9uMTIwMAYDVQQDEylNaWNyb3NvZnQgUm9vdCBDZXJ0aWZpY2F0
# ZSBBdXRob3JpdHkgMjAxMTAeFw0xMTA3MDgyMDU5MDlaFw0yNjA3MDgyMTA5MDla
# MH4xCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdS
# ZWRtb25kMR4wHAYDVQQKExVNaWNyb3NvZnQgQ29ycG9yYXRpb24xKDAmBgNVBAMT
# H01pY3Jvc29mdCBDb2RlIFNpZ25pbmcgUENBIDIwMTEwggIiMA0GCSqGSIb3DQEB
# AQUAA4ICDwAwggIKAoICAQCr8PpyEBwurdhuqoIQTTS68rZYIZ9CGypr6VpQqrgG
# OBoESbp/wwwe3TdrxhLYC/A4wpkGsMg51QEUMULTiQ15ZId+lGAkbK+eSZzpaF7S
# 35tTsgosw6/ZqSuuegmv15ZZymAaBelmdugyUiYSL+erCFDPs0S3XdjELgN1q2jz
# y23zOlyhFvRGuuA4ZKxuZDV4pqBjDy3TQJP4494HDdVceaVJKecNvqATd76UPe/7
# 4ytaEB9NViiienLgEjq3SV7Y7e1DkYPZe7J7hhvZPrGMXeiJT4Qa8qEvWeSQOy2u
# M1jFtz7+MtOzAz2xsq+SOH7SnYAs9U5WkSE1JcM5bmR/U7qcD60ZI4TL9LoDho33
# X/DQUr+MlIe8wCF0JV8YKLbMJyg4JZg5SjbPfLGSrhwjp6lm7GEfauEoSZ1fiOIl
# XdMhSz5SxLVXPyQD8NF6Wy/VI+NwXQ9RRnez+ADhvKwCgl/bwBWzvRvUVUvnOaEP
# 6SNJvBi4RHxF5MHDcnrgcuck379GmcXvwhxX24ON7E1JMKerjt/sW5+v/N2wZuLB
# l4F77dbtS+dJKacTKKanfWeA5opieF+yL4TXV5xcv3coKPHtbcMojyyPQDdPweGF
# RInECUzF1KVDL3SV9274eCBYLBNdYJWaPk8zhNqwiBfenk70lrC8RqBsmNLg1oiM
# CwIDAQABo4IB7TCCAekwEAYJKwYBBAGCNxUBBAMCAQAwHQYDVR0OBBYEFEhuZOVQ
# BdOCqhc3NyK1bajKdQKVMBkGCSsGAQQBgjcUAgQMHgoAUwB1AGIAQwBBMAsGA1Ud
# DwQEAwIBhjAPBgNVHRMBAf8EBTADAQH/MB8GA1UdIwQYMBaAFHItOgIxkEO5FAVO
# 4eqnxzHRI4k0MFoGA1UdHwRTMFEwT6BNoEuGSWh0dHA6Ly9jcmwubWljcm9zb2Z0
# LmNvbS9wa2kvY3JsL3Byb2R1Y3RzL01pY1Jvb0NlckF1dDIwMTFfMjAxMV8wM18y
# Mi5jcmwwXgYIKwYBBQUHAQEEUjBQME4GCCsGAQUFBzAChkJodHRwOi8vd3d3Lm1p
# Y3Jvc29mdC5jb20vcGtpL2NlcnRzL01pY1Jvb0NlckF1dDIwMTFfMjAxMV8wM18y
# Mi5jcnQwgZ8GA1UdIASBlzCBlDCBkQYJKwYBBAGCNy4DMIGDMD8GCCsGAQUFBwIB
# FjNodHRwOi8vd3d3Lm1pY3Jvc29mdC5jb20vcGtpb3BzL2RvY3MvcHJpbWFyeWNw
# cy5odG0wQAYIKwYBBQUHAgIwNB4yIB0ATABlAGcAYQBsAF8AcABvAGwAaQBjAHkA
# XwBzAHQAYQB0AGUAbQBlAG4AdAAuIB0wDQYJKoZIhvcNAQELBQADggIBAGfyhqWY
# 4FR5Gi7T2HRnIpsLlhHhY5KZQpZ90nkMkMFlXy4sPvjDctFtg/6+P+gKyju/R6mj
# 82nbY78iNaWXXWWEkH2LRlBV2AySfNIaSxzzPEKLUtCw/WvjPgcuKZvmPRul1LUd
# d5Q54ulkyUQ9eHoj8xN9ppB0g430yyYCRirCihC7pKkFDJvtaPpoLpWgKj8qa1hJ
# Yx8JaW5amJbkg/TAj/NGK978O9C9Ne9uJa7lryft0N3zDq+ZKJeYTQ49C/IIidYf
# wzIY4vDFLc5bnrRJOQrGCsLGra7lstnbFYhRRVg4MnEnGn+x9Cf43iw6IGmYslmJ
# aG5vp7d0w0AFBqYBKig+gj8TTWYLwLNN9eGPfxxvFX1Fp3blQCplo8NdUmKGwx1j
# NpeG39rz+PIWoZon4c2ll9DuXWNB41sHnIc+BncG0QaxdR8UvmFhtfDcxhsEvt9B
# xw4o7t5lL+yX9qFcltgA1qFGvVnzl6UJS0gQmYAf0AApxbGbpT9Fdx41xtKiop96
# eiL6SJUfq/tHI4D1nvi/a7dLl+LrdXga7Oo3mXkYS//WsyNodeav+vyL6wuA6mk7
# r/ww7QRMjt/fdW1jkT3RnVZOT7+AVyKheBEyIXrvQQqxP/uozKRdwaGIm1dxVk5I
# RcBCyZt2WwqASGv9eZ/BvW1taslScxMNelDNMYIWHTCCFhkCAQEwgZUwfjELMAkG
# A1UEBhMCVVMxEzARBgNVBAgTCldhc2hpbmd0b24xEDAOBgNVBAcTB1JlZG1vbmQx
# HjAcBgNVBAoTFU1pY3Jvc29mdCBDb3Jwb3JhdGlvbjEoMCYGA1UEAxMfTWljcm9z
# b2Z0IENvZGUgU2lnbmluZyBQQ0EgMjAxMQITMwAAAQNeJRyZH6MeuAAAAAABAzAN
# BglghkgBZQMEAgEFAKCBrjAZBgkqhkiG9w0BCQMxDAYKKwYBBAGCNwIBBDAcBgor
# BgEEAYI3AgELMQ4wDAYKKwYBBAGCNwIBFTAvBgkqhkiG9w0BCQQxIgQgHcRyFk2M
# 8BASv/EXAButaOADwthx0dQs+r0gR9V14M8wQgYKKwYBBAGCNwIBDDE0MDKgFIAS
# AE0AaQBjAHIAbwBzAG8AZgB0oRqAGGh0dHA6Ly93d3cubWljcm9zb2Z0LmNvbTAN
# BgkqhkiG9w0BAQEFAASCAQBQdXssqgCL5iaufY6kUaoLirmX3f9oSoW7gfY/RsWS
# TmGuyuEmZvFOorjTycqgRPfKaT/JThZvld9Dla78BxZDnqWS0V+sT6Q/scaOWjll
# 3W/U70ZhvZcePYEwflo0/RRpSCdH9pWyHzH5XJ56bM/aVXsxtUgVSZA4F3ZQm+4o
# RcF5LMn1+6hEzHAVtAzpJcXZZNaAKHy3wjx79BBA3ISFEhVzWNRw7rZ1u6RiZQXt
# VqQEd++a7mJbNutJNAJgYwq4RsbtNb5mJZrgc3wHhvfPNkCXY6LrTJUZVAYWPb/g
# pIETXBz3/8+O5WCt4ujLbeQ+IyrNj5vDptwTOKcmv24QoYITpzCCE6MGCisGAQQB
# gjcDAwExghOTMIITjwYJKoZIhvcNAQcCoIITgDCCE3wCAQMxDzANBglghkgBZQME
# AgEFADCCAVQGCyqGSIb3DQEJEAEEoIIBQwSCAT8wggE7AgEBBgorBgEEAYRZCgMB
# MDEwDQYJYIZIAWUDBAIBBQAEIEVCyNeV9C/kzghDDG+E/Fkm4SpkVOL9bXfkIRrJ
# ll0QAgZb/FAZvUgYEzIwMTgxMjAzMjAwMjQ2LjA5OFowBwIBAYACAfSggdCkgc0w
# gcoxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdS
# ZWRtb25kMR4wHAYDVQQKExVNaWNyb3NvZnQgQ29ycG9yYXRpb24xJTAjBgNVBAsT
# HE1pY3Jvc29mdCBBbWVyaWNhIE9wZXJhdGlvbnMxJjAkBgNVBAsTHVRoYWxlcyBU
# U1MgRVNOOjEyQjQtMkQ1Ri04N0Q0MSUwIwYDVQQDExxNaWNyb3NvZnQgVGltZS1T
# dGFtcCBTZXJ2aWNloIIPEzCCBnEwggRZoAMCAQICCmEJgSoAAAAAAAIwDQYJKoZI
# hvcNAQELBQAwgYgxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpXYXNoaW5ndG9uMRAw
# DgYDVQQHEwdSZWRtb25kMR4wHAYDVQQKExVNaWNyb3NvZnQgQ29ycG9yYXRpb24x
# MjAwBgNVBAMTKU1pY3Jvc29mdCBSb290IENlcnRpZmljYXRlIEF1dGhvcml0eSAy
# MDEwMB4XDTEwMDcwMTIxMzY1NVoXDTI1MDcwMTIxNDY1NVowfDELMAkGA1UEBhMC
# VVMxEzARBgNVBAgTCldhc2hpbmd0b24xEDAOBgNVBAcTB1JlZG1vbmQxHjAcBgNV
# BAoTFU1pY3Jvc29mdCBDb3Jwb3JhdGlvbjEmMCQGA1UEAxMdTWljcm9zb2Z0IFRp
# bWUtU3RhbXAgUENBIDIwMTAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB
# AQCpHQ28dxGKOiDs/BOX9fp/aZRrdFQQ1aUKAIKF++18aEssX8XD5WHCdrc+Zitb
# 8BVTJwQxH0EbGpUdzgkTjnxhMFmxMEQP8WCIhFRDDNdNuDgIs0Ldk6zWczBXJoKj
# RQ3Q6vVHgc2/JGAyWGBG8lhHhjKEHnRhZ5FfgVSxz5NMksHEpl3RYRNuKMYa+YaA
# u99h/EbBJx0kZxJyGiGKr0tkiVBisV39dx898Fd1rL2KQk1AUdEPnAY+Z3/1ZsAD
# lkR+79BL/W7lmsqxqPJ6Kgox8NpOBpG2iAg16HgcsOmZzTznL0S6p/TcZL2kAcEg
# CZN4zfy8wMlEXV4WnAEFTyJNAgMBAAGjggHmMIIB4jAQBgkrBgEEAYI3FQEEAwIB
# ADAdBgNVHQ4EFgQU1WM6XIoxkPNDe3xGG8UzaFqFbVUwGQYJKwYBBAGCNxQCBAwe
# CgBTAHUAYgBDAEEwCwYDVR0PBAQDAgGGMA8GA1UdEwEB/wQFMAMBAf8wHwYDVR0j
# BBgwFoAU1fZWy4/oolxiaNE9lJBb186aGMQwVgYDVR0fBE8wTTBLoEmgR4ZFaHR0
# cDovL2NybC5taWNyb3NvZnQuY29tL3BraS9jcmwvcHJvZHVjdHMvTWljUm9vQ2Vy
# QXV0XzIwMTAtMDYtMjMuY3JsMFoGCCsGAQUFBwEBBE4wTDBKBggrBgEFBQcwAoY+
# aHR0cDovL3d3dy5taWNyb3NvZnQuY29tL3BraS9jZXJ0cy9NaWNSb29DZXJBdXRf
# MjAxMC0wNi0yMy5jcnQwgaAGA1UdIAEB/wSBlTCBkjCBjwYJKwYBBAGCNy4DMIGB
# MD0GCCsGAQUFBwIBFjFodHRwOi8vd3d3Lm1pY3Jvc29mdC5jb20vUEtJL2RvY3Mv
# Q1BTL2RlZmF1bHQuaHRtMEAGCCsGAQUFBwICMDQeMiAdAEwAZQBnAGEAbABfAFAA
# bwBsAGkAYwB5AF8AUwB0AGEAdABlAG0AZQBuAHQALiAdMA0GCSqGSIb3DQEBCwUA
# A4ICAQAH5ohRDeLG4Jg/gXEDPZ2joSFvs+umzPUxvs8F4qn++ldtGTCzwsVmyWrf
# 9efweL3HqJ4l4/m87WtUVwgrUYJEEvu5U4zM9GASinbMQEBBm9xcF/9c+V4XNZgk
# Vkt070IQyK+/f8Z/8jd9Wj8c8pl5SpFSAK84Dxf1L3mBZdmptWvkx872ynoAb0sw
# RCQiPM/tA6WWj1kpvLb9BOFwnzJKJ/1Vry/+tuWOM7tiX5rbV0Dp8c6ZZpCM/2pi
# f93FSguRJuI57BlKcWOdeyFtw5yjojz6f32WapB4pm3S4Zz5Hfw42JT0xqUKloak
# vZ4argRCg7i1gJsiOCC1JeVk7Pf0v35jWSUPei45V3aicaoGig+JFrphpxHLmtgO
# R5qAxdDNp9DvfYPw4TtxCd9ddJgiCGHasFAeb73x4QDf5zEHpJM692VHeOj4qEir
# 995yfmFrb3epgcunCaw5u+zGy9iCtHLNHfS4hQEegPsbiSpUObJb2sgNVZl6h3M7
# COaYLeqN4DMuEin1wC9UJyH3yKxO2ii4sanblrKnQqLJzxlBTeCG+SqaoxFmMNO7
# dDJL32N79ZmKLxvHIa9Zta7cRDyXUHHXodLFVeNp3lfB0d4wwP3M5k37Db9dT+md
# Hhk4L7zPWAUu7w2gUDXa7wknHNWzfjUeCLraNtvTX4/edIhJEjCCBPEwggPZoAMC
# AQICEzMAAADlTkVnanJ00cwAAAAAAOUwDQYJKoZIhvcNAQELBQAwfDELMAkGA1UE
# BhMCVVMxEzARBgNVBAgTCldhc2hpbmd0b24xEDAOBgNVBAcTB1JlZG1vbmQxHjAc
# BgNVBAoTFU1pY3Jvc29mdCBDb3Jwb3JhdGlvbjEmMCQGA1UEAxMdTWljcm9zb2Z0
# IFRpbWUtU3RhbXAgUENBIDIwMTAwHhcNMTgwODIzMjAyNzA5WhcNMTkxMTIzMjAy
# NzA5WjCByjELMAkGA1UEBhMCVVMxEzARBgNVBAgTCldhc2hpbmd0b24xEDAOBgNV
# BAcTB1JlZG1vbmQxHjAcBgNVBAoTFU1pY3Jvc29mdCBDb3Jwb3JhdGlvbjElMCMG
# A1UECxMcTWljcm9zb2Z0IEFtZXJpY2EgT3BlcmF0aW9uczEmMCQGA1UECxMdVGhh
# bGVzIFRTUyBFU046MTJCNC0yRDVGLTg3RDQxJTAjBgNVBAMTHE1pY3Jvc29mdCBU
# aW1lLVN0YW1wIFNlcnZpY2UwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB
# AQDmGmIS3KeNHkxbc/tqOByYh1pEbRQaCrB3XVwUe3JZz72nJ2LcAPzuUnC2m4FR
# t0kXFI0fNDfwnkqQPq70Lp+tpP0lioAzEh62H4O2FkROYcFF6dBQz8ZGp1ceKLT2
# EFbuGsVBgzIAne7xRY4opC1n2NYkR6cj4kTw3KL6GmJa6jGUJ8a9FHkWMX8y4i9K
# DDWFk2gwxGavLv1GXZwuka15+tZo1Lz5+I9e6abdDJyMXhXDi6m0ujkBzC7uQcvT
# UFYtFOuA9Qp6N9YXsIBu34IK6aY1+6S0TGUvHbvPFshc1KViHP197BnEqsxAlhSZ
# uZ0VszSKzAbt01owDi6AgrDJAgMBAAGjggEbMIIBFzAdBgNVHQ4EFgQUENZZ4E0c
# H4/ijCBw80gIXHMOZrEwHwYDVR0jBBgwFoAU1WM6XIoxkPNDe3xGG8UzaFqFbVUw
# VgYDVR0fBE8wTTBLoEmgR4ZFaHR0cDovL2NybC5taWNyb3NvZnQuY29tL3BraS9j
# cmwvcHJvZHVjdHMvTWljVGltU3RhUENBXzIwMTAtMDctMDEuY3JsMFoGCCsGAQUF
# BwEBBE4wTDBKBggrBgEFBQcwAoY+aHR0cDovL3d3dy5taWNyb3NvZnQuY29tL3Br
# aS9jZXJ0cy9NaWNUaW1TdGFQQ0FfMjAxMC0wNy0wMS5jcnQwDAYDVR0TAQH/BAIw
# ADATBgNVHSUEDDAKBggrBgEFBQcDCDANBgkqhkiG9w0BAQsFAAOCAQEAMWOGtgzb
# /tNyD1Qma01PMxOScT4xoGdFmDdTeIqVxD1tNMIVA6EfJTlAf0CdorUaNPNt3Jsl
# IG3OGwCXVUqFwK3cmehbZTRLyfSGOSX+0q2yvp0llTxRzr9+elZwb0CMz9JQbL7N
# 2nkCeRlXbO8on1vKmNXRZn643dTChXAgAmSV+6Cn1ROTVWE59Y9KT49h031rwp60
# lz+Gqh/l9TeFf4Qm3TSwxuEyp0HrSnkkRe1pGUKVwcAzCPaYcqArveIaKSto0+ZF
# /umK2Ms2AWPUHrvxPVtGC4EiRpGMMnystNDLdx3jnwCue0NeKV1F0utk7E63DeNz
# X7io/uEugCIYMaGCA6UwggKNAgEBMIH6oYHQpIHNMIHKMQswCQYDVQQGEwJVUzET
# MBEGA1UECBMKV2FzaGluZ3RvbjEQMA4GA1UEBxMHUmVkbW9uZDEeMBwGA1UEChMV
# TWljcm9zb2Z0IENvcnBvcmF0aW9uMSUwIwYDVQQLExxNaWNyb3NvZnQgQW1lcmlj
# YSBPcGVyYXRpb25zMSYwJAYDVQQLEx1UaGFsZXMgVFNTIEVTTjoxMkI0LTJENUYt
# ODdENDElMCMGA1UEAxMcTWljcm9zb2Z0IFRpbWUtU3RhbXAgU2VydmljZaIlCgEB
# MAkGBSsOAwIaBQADFQCm0wAyZYK+xFDI7EX2JOeOISafQ6CB2jCB16SB1DCB0TEL
# MAkGA1UEBhMCVVMxEzARBgNVBAgTCldhc2hpbmd0b24xEDAOBgNVBAcTB1JlZG1v
# bmQxHjAcBgNVBAoTFU1pY3Jvc29mdCBDb3Jwb3JhdGlvbjElMCMGA1UECxMcTWlj
# cm9zb2Z0IEFtZXJpY2EgT3BlcmF0aW9uczEnMCUGA1UECxMebkNpcGhlciBOVFMg
# RVNOOjI2NjUtNEMzRi1DNURFMSswKQYDVQQDEyJNaWNyb3NvZnQgVGltZSBTb3Vy
# Y2UgTWFzdGVyIENsb2NrMA0GCSqGSIb3DQEBBQUAAgUA3695TjAiGA8yMDE4MTIw
# MzA5NDM0MloYDzIwMTgxMjA0MDk0MzQyWjB0MDoGCisGAQQBhFkKBAExLDAqMAoC
# BQDfr3lOAgEAMAcCAQACAgUMMAcCAQACAhiRMAoCBQDfsMrOAgEAMDYGCisGAQQB
# hFkKBAIxKDAmMAwGCisGAQQBhFkKAwGgCjAIAgEAAgMW42ChCjAIAgEAAgMHoSAw
# DQYJKoZIhvcNAQEFBQADggEBAAuvOFFsNKUenOXbuVREQa/RHiUrrVWg73rARCks
# Ym3/BHDtQxLuWwK02eoB5KnorP2Rp3sNwCxIdLnkzVWiCvqMyl0N60bl7dphnwWp
# H1rTW48sArp+odRFjOeTrvO91n2poEGBcd9RJFnCMJqVDuBvWdq7WYBsiMXNhD7A
# FbqSmvYEkCTZcHp/QEf9y82VZ+Wrz/0xaq5FV2Dh5cNyvU75RYKTwYlopUfy0c/D
# GuP0Np4d5Q4tbvRqhOAOwKqO+fb8GEgYKYGGC+3WnwGGQVf/ywLcbLUCQ6Faa2Xn
# A7WVo6Mcld+Za8CuIgDCcJvsY1CdaURiOc/e7k855QEjzKExggL1MIIC8QIBATCB
# kzB8MQswCQYDVQQGEwJVUzETMBEGA1UECBMKV2FzaGluZ3RvbjEQMA4GA1UEBxMH
# UmVkbW9uZDEeMBwGA1UEChMVTWljcm9zb2Z0IENvcnBvcmF0aW9uMSYwJAYDVQQD
# Ex1NaWNyb3NvZnQgVGltZS1TdGFtcCBQQ0EgMjAxMAITMwAAAOVORWdqcnTRzAAA
# AAAA5TANBglghkgBZQMEAgEFAKCCATIwGgYJKoZIhvcNAQkDMQ0GCyqGSIb3DQEJ
# EAEEMC8GCSqGSIb3DQEJBDEiBCAqRP75FIy5CyhkdddSlnblCO0lHFste7Is8c20
# RzpfjjCB4gYLKoZIhvcNAQkQAgwxgdIwgc8wgcwwgbEEFKbTADJlgr7EUMjsRfYk
# 544hJp9DMIGYMIGApH4wfDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCldhc2hpbmd0
# b24xEDAOBgNVBAcTB1JlZG1vbmQxHjAcBgNVBAoTFU1pY3Jvc29mdCBDb3Jwb3Jh
# dGlvbjEmMCQGA1UEAxMdTWljcm9zb2Z0IFRpbWUtU3RhbXAgUENBIDIwMTACEzMA
# AADlTkVnanJ00cwAAAAAAOUwFgQUtovCJb3Kgta8C9W50rkbzVud358wDQYJKoZI
# hvcNAQELBQAEggEAy621NurLVsTdg7H1f2HcPC/2m7Hb5IQEXiAvC70s7lIEoz5U
# qjL6mza99MsfSX+Bf0cozGiYf9Z0qxpMnaanBnwmqNfcr9ZW+ITbTNctpzSpwUyg
# A2hJcOkCbbLFLn/97vctoLXBWxRXu/TcaoL1g7eAXx+8Ph9/4/esLPv2mkQN02/f
# Yu5RhiEtifQG4vQpKPrmoCofDdjyGWAffxDmyfJVAgOG3xd1xE/jEUsWHb16exzB
# glqiicc4nZfjMBT3SvJbWt0UONvGyU7zraw43wSgNmBac/4ma6UhAwcuRlP7Fd5M
# VMNvtR0o4sjRMbIIjD3mGzdtIm53D47/3umYZA==
# SIG # End signature block
