function Test-PlasterManifest {
    [CmdletBinding()]
    [OutputType([System.Xml.XmlDocument])]
    param(
        [Parameter(Position=0,
                   ParameterSetName="Path",
                   ValueFromPipeline=$true,
                   ValueFromPipelineByPropertyName=$true,
                   HelpMessage="Specifies a path to a plasterManifest.xml file.")]
        [Alias("PSPath")]
        [ValidateNotNullOrEmpty()]
        [string[]]
        $Path = @("$pwd\plasterManifest.xml")
    )

    begin {
        $schemaPath = [System.IO.Path]::Combine($PSScriptRoot, "Schema", "PlasterManifest-v1.xsd")

        # Schema validation is not available on .NET Core - at the moment.
        if ('System.Xml.Schema.XmlSchemaSet' -as [type]) {
            $xmlSchemaSet = New-Object System.Xml.Schema.XmlSchemaSet
            $xmlSchemaSet.Add($TargetNamespace, $schemaPath) > $null
        }
        else {
            $PSCmdLet.WriteWarning($LocalizedData.TestPlasterNoXmlSchemaValidationWarning)
        }
    }

    process {
        foreach ($aPath in $Path) {
            $aPath = $PSCmdLet.GetUnresolvedProviderPathFromPSPath($aPath)

            if (!(Test-Path -LiteralPath $aPath)) {
                $ex = New-Object System.Management.Automation.ItemNotFoundException ($LocalizedData.ErrorPathDoesNotExist_F1 -f $aPath)
                $category = [System.Management.Automation.ErrorCategory]::ObjectNotFound
                $errRecord = New-Object System.Management.Automation.ErrorRecord $ex,'PathNotFound',$category,$aPath
                $PSCmdLet.WriteError($errRecord)
                return
            }

            $filename = Split-Path $aPath -Leaf

            # Verify the manifest has the correct filename. Allow for localized template manifest files as well.
            if (!(($filename -eq 'plasterManifest.xml') -or ($filename -match 'plasterManifest_[a-zA-Z]+(-[a-zA-Z]+){0,2}.xml'))) {
                Write-Error ($LocalizedData.ManifestWrongFilename_F1 -f $filename)
                return
            }

            # Verify the manifest loads into an XmlDocument i.e. verify it is well-formed.
            $manifest = $null
            try {
                $manifest = [xml](Get-Content $aPath)
            }
            catch {
                $ex = New-Object System.Exception ($LocalizedData.ManifestNotWellFormedXml_F2 -f $aPath, $_.Exception.Message), $_.Exception
                $category = [System.Management.Automation.ErrorCategory]::InvalidData
                $errRecord = New-Object System.Management.Automation.ErrorRecord $ex,'InvalidManifestFile',$category,$aPath
                $psCmdlet.WriteError($errRecord)
                return
            }

            # Validate the manifest contains the required root element and target namespace that the following
            # XML schema validation will apply to.
            if (!$manifest.plasterManifest) {
                Write-Error ($LocalizedData.ManifestMissingDocElement_F2 -f $aPath,$TargetNamespace)
                return
            }

            if ($manifest.plasterManifest.NamespaceURI -cne $TargetNamespace) {
                Write-Error ($LocalizedData.ManifestMissingDocTargetNamespace_F2 -f $aPath,$TargetNamespace)
                return
            }

            # Valid flag is stashed in a hashtable so the ValidationEventHandler scriptblock can set the value.
            $manifestIsValid = @{Value = $true}

            # Configure an XmlReader and XmlReaderSettings to perform schema validation on xml file.
            $xmlReaderSettings = New-Object System.Xml.XmlReaderSettings

            # Schema validation is not available on .NET Core - at the moment.
            if ($xmlSchemaSet) {
                $xmlReaderSettings.ValidationFlags = [System.Xml.Schema.XmlSchemaValidationFlags]::ReportValidationWarnings
                $xmlReaderSettings.ValidationType = [System.Xml.ValidationType]::Schema
                $xmlReaderSettings.Schemas = $xmlSchemaSet
            }

            # Schema validation is not available on .NET Core - at the moment.
            if ($xmlSchemaSet) {
                # Event handler scriptblock for the ValidationEventHandler event.
                $validationEventHandler = {
                    param($sender, $eventArgs)

                    if ($eventArgs.Severity -eq [System.Xml.Schema.XmlSeverityType]::Error)
                    {
                        Write-Verbose ($LocalizedData.ManifestSchemaValidationError_F2 -f $aPath,$eventArgs.Message)
                        $manifestIsValid.Value = $false
                    }
                }

                $xmlReaderSettings.add_ValidationEventHandler($validationEventHandler)
            }

            [System.Xml.XmlReader]$xmlReader = $null
            try {
                $xmlReader = [System.Xml.XmlReader]::Create($aPath, $xmlReaderSettings)
                while ($xmlReader.Read()) {}
            }
            catch {
                Write-Error ($LocalizedData.ManifestErrorReading_F1 -f $_)
                $manifestIsValid.Value = $false
            }
            finally {
                # Schema validation is not available on .NET Core - at the moment.
                if ($xmlSchemaSet) {
                    $xmlReaderSettings.remove_ValidationEventHandler($validationEventHandler)
                }
                if ($xmlReader) { $xmlReader.Dispose() }
            }

            # Validate default values for choice/multichoice parameters containing 1 or more ints
            $xpath = "//tns:parameter[@type='choice'] | //tns:parameter[@type='multichoice']"
            $choiceParameters = Select-Xml -Xml $manifest -XPath $xpath  -Namespace @{tns=$TargetNamespace}
            foreach ($choiceParameterXmlInfo in $choiceParameters) {
                $choiceParameter = $choiceParameterXmlInfo.Node
                if (!$choiceParameter.default) { continue }

                if ($choiceParameter.type -eq 'choice') {
                    if ($null -eq ($choiceParameter.default -as [int])) {
                        $PSCmdLet.WriteVerbose(($LocalizedData.ManifestSchemaInvalidChoiceDefault_F3 -f $choiceParameter.default,$choiceParameter.name,$aPath))
                        $manifestIsValid.Value = $false
                    }
                }
                else {
                    if ($null -eq (($choiceParameter.default -split ',') -as [int[]])) {
                        $PSCmdLet.WriteVerbose(($LocalizedData.ManifestSchemaInvalidMultichoiceDefault_F3 -f $choiceParameter.default,$choiceParameter.name,$aPath))
                        $manifestIsValid.Value = $false
                    }
                }
            }

            # Validate that the requireModule attribute requiredVersion is mutually exclusive from both
            # the version and maximumVersion attributes.
            $requireModules = Select-Xml -Xml $manifest -XPath '//tns:requireModule' -Namespace @{tns = $TargetNamespace}
            foreach ($requireModuleInfo in $requireModules) {
                $requireModuleNode = $requireModuleInfo.Node
                if ($requireModuleNode.requiredVersion -and ($requireModuleNode.minimumVersion -or $requireModuleNode.maximumVersion)) {
                    $PSCmdLet.WriteVerbose(($LocalizedData.ManifestSchemaInvalidRequireModuleAttrs_F2 -f $requireModuleNode.name,$aPath))
                    $manifestIsValid.Value = $false
                }
            }

            # Validate that all the condition attribute values are valid PowerShell script.
            $conditionAttrs = Select-Xml -Xml $manifest -XPath '//@condition'
            foreach ($conditionAttr in $conditionAttrs) {
                $tokens = $errors = $null
                $null = [System.Management.Automation.Language.Parser]::ParseInput($conditionAttr.Node.Value, [ref] $tokens, [ref] $errors)
                if ($errors.Count -gt 0) {
                    $msg = $LocalizedData.ManifestSchemaInvalidCondition_F3 -f $conditionAttr.Node.Value, $aPath, $errors[0]
                    $PSCmdLet.WriteVerbose($msg)
                    $manifestIsValid.Value = $false
                }
            }

            # Validate all interpolated attribute values are valid within a PowerShell string interpolation context.
            $interpolatedAttrs  = @(Select-Xml -Xml $manifest -XPath '//tns:parameter/@default' -Namespace @{tns = $TargetNamespace})
            $interpolatedAttrs += @(Select-Xml -Xml $manifest -XPath '//tns:parameter/@prompt' -Namespace @{tns = $TargetNamespace})
            $interpolatedAttrs += @(Select-Xml -Xml $manifest -XPath '//tns:content/tns:*/@*' -Namespace @{tns = $TargetNamespace})
            foreach ($interpolatedAttr in $interpolatedAttrs) {
                $name = $interpolatedAttr.Node.LocalName
                if ($name -eq 'condition') { continue }

                $tokens = $errors = $null
                $value = $interpolatedAttr.Node.Value
                $null = [System.Management.Automation.Language.Parser]::ParseInput("`"$value`"", [ref] $tokens, [ref] $errors)
                if ($errors.Count -gt 0) {
                    $ownerName = $interpolatedAttr.Node.OwnerElement.LocalName
                    $msg = $LocalizedData.ManifestSchemaInvalidAttrValue_F5 -f $name, $value, $ownerName, $aPath, $errors[0]
                    $PSCmdLet.WriteVerbose($msg)
                    $manifestIsValid.Value = $false
                }
            }

            if ($manifestIsValid.Value) {
                # Verify manifest schema version is supported.
                $manifestSchemaVersion = [System.Version]$manifest.plasterManifest.schemaVersion

                # Use a simplified form (no patch version) of semver for checking XML schema version compatibility.
                if (($manifestSchemaVersion.Major -gt $LatestSupportedSchemaVersion.Major) -or
                    (($manifestSchemaVersion.Major -eq $LatestSupportedSchemaVersion.Major) -and
                     ($manifestSchemaVersion.Minor -gt $LatestSupportedSchemaVersion.Minor))) {

                    Write-Error ($LocalizedData.ManifestSchemaVersionNotSupported_F2 -f $manifestSchemaVersion,$aPath)
                    return
                }

                # Verify that the plasterVersion is supported.
                if ($manifest.plasterManifest.plasterVersion) {
                    $requiredPlasterVersion = [System.Version]$manifest.plasterManifest.plasterVersion

                    # Is user specifies major.minor, change build to 0 (from default of -1) so compare works correctly.
                    if ($requiredPlasterVersion.Build -eq -1) {
                        $requiredPlasterVersion = [System.Version]"${requiredPlasterVersion}.0"
                    }

                    if ($requiredPlasterVersion -gt $MyInvocation.MyCommand.Module.Version) {
                        $plasterVersion = $manifest.plasterManifest.plasterVersion
                        Write-Error ($LocalizedData.ManifestPlasterVersionNotSupported_F2 -f $aPath,$plasterVersion)
                        return
                    }
                }

                $manifest
            }
            else {
                if ($PSBoundParameters['Verbose']) {
                    Write-Error ($LocalizedData.ManifestNotValid_F1 -f $aPath)
                }
                else {
                    Write-Error ($LocalizedData.ManifestNotValidVerbose_F1 -f $aPath)
                }
            }
        }
    }
}

# SIG # Begin signature block
# MIIdhQYJKoZIhvcNAQcCoIIddjCCHXICAQExCzAJBgUrDgMCGgUAMGkGCisGAQQB
# gjcCAQSgWzBZMDQGCisGAQQBgjcCAR4wJgIDAQAABBAfzDtgWUsITrck0sYpfvNR
# AgEAAgEAAgEAAgEAAgEAMCEwCQYFKw4DAhoFAAQU0QgVwbSqnJ0JMszdRH3efgGM
# 0YygghhTMIIEwjCCA6qgAwIBAgITMwAAAL+RbPt8GiTgIgAAAAAAvzANBgkqhkiG
# 9w0BAQUFADB3MQswCQYDVQQGEwJVUzETMBEGA1UECBMKV2FzaGluZ3RvbjEQMA4G
# A1UEBxMHUmVkbW9uZDEeMBwGA1UEChMVTWljcm9zb2Z0IENvcnBvcmF0aW9uMSEw
# HwYDVQQDExhNaWNyb3NvZnQgVGltZS1TdGFtcCBQQ0EwHhcNMTYwOTA3MTc1ODQ5
# WhcNMTgwOTA3MTc1ODQ5WjCBsjELMAkGA1UEBhMCVVMxEzARBgNVBAgTCldhc2hp
# bmd0b24xEDAOBgNVBAcTB1JlZG1vbmQxHjAcBgNVBAoTFU1pY3Jvc29mdCBDb3Jw
# b3JhdGlvbjEMMAoGA1UECxMDQU9DMScwJQYDVQQLEx5uQ2lwaGVyIERTRSBFU046
# NTdDOC0yRDE1LTFDOEIxJTAjBgNVBAMTHE1pY3Jvc29mdCBUaW1lLVN0YW1wIFNl
# cnZpY2UwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCt7X+GwPaidVcV
# TRT2yohV/L1dpTMCvf4DHlCY0GUmhEzD4Yn22q/qnqZTHDd8IlI/OHvKhWC9ksKE
# F+BgBHtUQPSg7s6+ZXy69qX64r6m7X/NYizeK31DsScLsDHnqsbnwJaNZ2C2u5hh
# cKsHvc8BaSsv/nKlr6+eg2iX2y9ai1uB1ySNeunEtdfchAr1U6Qb7AJHrXMTdKl8
# ptLov67aFU0rRRMwQJOWHR+o/gQa9v4z/f43RY2PnMRoF7Dztn6ditoQ9CgTiMdS
# MtsqFWMAQNMt5bZ8oY1hmgkSDN6FwTjVyUEE6t3KJtgX2hMHjOVqtHXQlud0GR3Z
# LtAOMbS7AgMBAAGjggEJMIIBBTAdBgNVHQ4EFgQU5GwaORrHk1i0RjZlB8QAt3kX
# nBEwHwYDVR0jBBgwFoAUIzT42VJGcArtQPt2+7MrsMM1sw8wVAYDVR0fBE0wSzBJ
# oEegRYZDaHR0cDovL2NybC5taWNyb3NvZnQuY29tL3BraS9jcmwvcHJvZHVjdHMv
# TWljcm9zb2Z0VGltZVN0YW1wUENBLmNybDBYBggrBgEFBQcBAQRMMEowSAYIKwYB
# BQUHMAKGPGh0dHA6Ly93d3cubWljcm9zb2Z0LmNvbS9wa2kvY2VydHMvTWljcm9z
# b2Z0VGltZVN0YW1wUENBLmNydDATBgNVHSUEDDAKBggrBgEFBQcDCDANBgkqhkiG
# 9w0BAQUFAAOCAQEAjt62jcZ+2YBqm7RKit827DRU9OKioi6HEERT0X0bL+JjUTu3
# 7k4piPcK3J/0cfktWuPjrYSuySa/NbkmlvAhQV4VpoWxipx3cZplF9HK9IH4t8AD
# YDxUI5u1xb2r24aExGIzWY+1uH92bzTKbAjuwNzTMQ1z10Kca4XXPI4HFZalXxgL
# fbjCkV3IKNspU1TILV0Dzk0tdKAwx/MoeZN1HFcB9WjzbpFnCVH+Oy/NyeJOyiNE
# 4uT/6iyHz1+XCqf2nIrV/DXXsJYKwifVlOvSJ4ZrV40MYucq3lWQuKERfXivLFXl
# dKyXQrS4eeToRPSevRisc0GBYuZczpkdeN5faDCCBgAwggPooAMCAQICEzMAAADD
# Dpun2LLc9ywAAAAAAMMwDQYJKoZIhvcNAQELBQAwfjELMAkGA1UEBhMCVVMxEzAR
# BgNVBAgTCldhc2hpbmd0b24xEDAOBgNVBAcTB1JlZG1vbmQxHjAcBgNVBAoTFU1p
# Y3Jvc29mdCBDb3Jwb3JhdGlvbjEoMCYGA1UEAxMfTWljcm9zb2Z0IENvZGUgU2ln
# bmluZyBQQ0EgMjAxMTAeFw0xNzA4MTEyMDIwMjRaFw0xODA4MTEyMDIwMjRaMHQx
# CzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdSZWRt
# b25kMR4wHAYDVQQKExVNaWNyb3NvZnQgQ29ycG9yYXRpb24xHjAcBgNVBAMTFU1p
# Y3Jvc29mdCBDb3Jwb3JhdGlvbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC
# ggEBALtX1zjRsQZ/SS2pbbNjn3q6tjohW7SYro3UpIGgxXXFLO+CQCq3gVN382MB
# CrzON4QDQENXgkvO7R+2/YBtycKRXQXH3FZZAOEM61fe/fG4kCe/dUr8dbJyWLbF
# SJszYgXRlZSlvzkirY0STUZi2jIZzqoiXFZIsW9FyWd2Yl0wiKMvKMUfUCrZhtsa
# ESWBwvT1Zy7neR314hx19E7Mx/znvwuARyn/z81psQwLYOtn5oQbm039bUc6x9nB
# YWHylRKhDQeuYyHY9Jkc/3hVge6leegggl8K2rVTGVQBVw2HkY3CfPFUhoDhYtuC
# cz4mXvBAEtI51SYDDYWIMV8KC4sCAwEAAaOCAX8wggF7MB8GA1UdJQQYMBYGCisG
# AQQBgjdMCAEGCCsGAQUFBwMDMB0GA1UdDgQWBBSnE10fIYlV6APunhc26vJUiDUZ
# rzBRBgNVHREESjBIpEYwRDEMMAoGA1UECxMDQU9DMTQwMgYDVQQFEysyMzAwMTIr
# YzgwNGI1ZWEtNDliNC00MjM4LTgzNjItZDg1MWZhMjI1NGZjMB8GA1UdIwQYMBaA
# FEhuZOVQBdOCqhc3NyK1bajKdQKVMFQGA1UdHwRNMEswSaBHoEWGQ2h0dHA6Ly93
# d3cubWljcm9zb2Z0LmNvbS9wa2lvcHMvY3JsL01pY0NvZFNpZ1BDQTIwMTFfMjAx
# MS0wNy0wOC5jcmwwYQYIKwYBBQUHAQEEVTBTMFEGCCsGAQUFBzAChkVodHRwOi8v
# d3d3Lm1pY3Jvc29mdC5jb20vcGtpb3BzL2NlcnRzL01pY0NvZFNpZ1BDQTIwMTFf
# MjAxMS0wNy0wOC5jcnQwDAYDVR0TAQH/BAIwADANBgkqhkiG9w0BAQsFAAOCAgEA
# TZdPNH7xcJOc49UaS5wRfmsmxKUk9N9E1CS6s2oIiZmayzHncJv/FB2wBzl/5DA7
# EyLeDsiVZ7tufvh8laSQgjeTpoPTSQLBrK1Z75G3p2YADqJMJdTc510HAsooNGU7
# OYOtlSqOyqDoCDoc/j57QEmUTY5UJQrlsccK7nE3xpteNvWnQkT7vIewDcA12SaH
# X/9n7yh094owBBGKZ8xLNWBqIefDjQeDXpurnXEfKSYJEdT1gtPSNgcpruiSbZB/
# AMmoW+7QBGX7oQ5XU8zymInznxWTyAbEY1JhAk9XSBz1+3USyrX59MJpX7uhnQ1p
# gyfrgz4dazHD7g7xxIRDh+4xnAYAMny3IIq5CCPqVrAY1LK9Few37WTTaxUCI8aK
# M4c60Zu2wJZZLKABU4QBX/J7wXqw7NTYUvZfdYFEWRY4J1O7UPNecd/311HcMdUa
# YzUql36fZjdfz1Uz77LKvCwjqkQe7vtnSLToQsMPilFYokYCYSZaGb9clOmoQHDn
# WzBMfIDUUGeipe4O6z218eV5HuH1WBlvu4lteOIgWCX/5Eiz5q/xskAEF0ZQ1Axs
# kRR97sri9ibeGzsEZ1EuD6QX90L/P5GJMfinvLPlOlLcKjN/SmSRZdhlEbbbare0
# bFL8v4txFsQsznOaoOldCMFFRaUphuwBMW1edMZWMQswggYHMIID76ADAgECAgph
# Fmg0AAAAAAAcMA0GCSqGSIb3DQEBBQUAMF8xEzARBgoJkiaJk/IsZAEZFgNjb20x
# GTAXBgoJkiaJk/IsZAEZFgltaWNyb3NvZnQxLTArBgNVBAMTJE1pY3Jvc29mdCBS
# b290IENlcnRpZmljYXRlIEF1dGhvcml0eTAeFw0wNzA0MDMxMjUzMDlaFw0yMTA0
# MDMxMzAzMDlaMHcxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpXYXNoaW5ndG9uMRAw
# DgYDVQQHEwdSZWRtb25kMR4wHAYDVQQKExVNaWNyb3NvZnQgQ29ycG9yYXRpb24x
# ITAfBgNVBAMTGE1pY3Jvc29mdCBUaW1lLVN0YW1wIFBDQTCCASIwDQYJKoZIhvcN
# AQEBBQADggEPADCCAQoCggEBAJ+hbLHf20iSKnxrLhnhveLjxZlRI1Ctzt0YTiQP
# 7tGn0UytdDAgEesH1VSVFUmUG0KSrphcMCbaAGvoe73siQcP9w4EmPCJzB/LMySH
# nfL0Zxws/HvniB3q506jocEjU8qN+kXPCdBer9CwQgSi+aZsk2fXKNxGU7CG0OUo
# Ri4nrIZPVVIM5AMs+2qQkDBuh/NZMJ36ftaXs+ghl3740hPzCLdTbVK0RZCfSABK
# R2YRJylmqJfk0waBSqL5hKcRRxQJgp+E7VV4/gGaHVAIhQAQMEbtt94jRrvELVSf
# rx54QTF3zJvfO4OToWECtR0Nsfz3m7IBziJLVP/5BcPCIAsCAwEAAaOCAaswggGn
# MA8GA1UdEwEB/wQFMAMBAf8wHQYDVR0OBBYEFCM0+NlSRnAK7UD7dvuzK7DDNbMP
# MAsGA1UdDwQEAwIBhjAQBgkrBgEEAYI3FQEEAwIBADCBmAYDVR0jBIGQMIGNgBQO
# rIJgQFYnl+UlE/wq4QpTlVnkpKFjpGEwXzETMBEGCgmSJomT8ixkARkWA2NvbTEZ
# MBcGCgmSJomT8ixkARkWCW1pY3Jvc29mdDEtMCsGA1UEAxMkTWljcm9zb2Z0IFJv
# b3QgQ2VydGlmaWNhdGUgQXV0aG9yaXR5ghB5rRahSqClrUxzWPQHEy5lMFAGA1Ud
# HwRJMEcwRaBDoEGGP2h0dHA6Ly9jcmwubWljcm9zb2Z0LmNvbS9wa2kvY3JsL3By
# b2R1Y3RzL21pY3Jvc29mdHJvb3RjZXJ0LmNybDBUBggrBgEFBQcBAQRIMEYwRAYI
# KwYBBQUHMAKGOGh0dHA6Ly93d3cubWljcm9zb2Z0LmNvbS9wa2kvY2VydHMvTWlj
# cm9zb2Z0Um9vdENlcnQuY3J0MBMGA1UdJQQMMAoGCCsGAQUFBwMIMA0GCSqGSIb3
# DQEBBQUAA4ICAQAQl4rDXANENt3ptK132855UU0BsS50cVttDBOrzr57j7gu1BKi
# jG1iuFcCy04gE1CZ3XpA4le7r1iaHOEdAYasu3jyi9DsOwHu4r6PCgXIjUji8FMV
# 3U+rkuTnjWrVgMHmlPIGL4UD6ZEqJCJw+/b85HiZLg33B+JwvBhOnY5rCnKVuKE5
# nGctxVEO6mJcPxaYiyA/4gcaMvnMMUp2MT0rcgvI6nA9/4UKE9/CCmGO8Ne4F+tO
# i3/FNSteo7/rvH0LQnvUU3Ih7jDKu3hlXFsBFwoUDtLaFJj1PLlmWLMtL+f5hYbM
# UVbonXCUbKw5TNT2eb+qGHpiKe+imyk0BncaYsk9Hm0fgvALxyy7z0Oz5fnsfbXj
# pKh0NbhOxXEjEiZ2CzxSjHFaRkMUvLOzsE1nyJ9C/4B5IYCeFTBm6EISXhrIniIh
# 0EPpK+m79EjMLNTYMoBMJipIJF9a6lbvpt6Znco6b72BJ3QGEe52Ib+bgsEnVLax
# aj2JoXZhtG6hE6a/qkfwEm/9ijJssv7fUciMI8lmvZ0dhxJkAj0tr1mPuOQh5bWw
# ymO0eFQF1EEuUKyUsKV4q7OglnUa2ZKHE3UiLzKoCG6gW4wlv6DvhMoh1useT8ma
# 7kng9wFlb4kLfchpyOZu6qeXzjEp/w7FW1zYTRuh2Povnj8uVRZryROj/TCCB3ow
# ggVioAMCAQICCmEOkNIAAAAAAAMwDQYJKoZIhvcNAQELBQAwgYgxCzAJBgNVBAYT
# AlVTMRMwEQYDVQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdSZWRtb25kMR4wHAYD
# VQQKExVNaWNyb3NvZnQgQ29ycG9yYXRpb24xMjAwBgNVBAMTKU1pY3Jvc29mdCBS
# b290IENlcnRpZmljYXRlIEF1dGhvcml0eSAyMDExMB4XDTExMDcwODIwNTkwOVoX
# DTI2MDcwODIxMDkwOVowfjELMAkGA1UEBhMCVVMxEzARBgNVBAgTCldhc2hpbmd0
# b24xEDAOBgNVBAcTB1JlZG1vbmQxHjAcBgNVBAoTFU1pY3Jvc29mdCBDb3Jwb3Jh
# dGlvbjEoMCYGA1UEAxMfTWljcm9zb2Z0IENvZGUgU2lnbmluZyBQQ0EgMjAxMTCC
# AiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAKvw+nIQHC6t2G6qghBNNLry
# tlghn0IbKmvpWlCquAY4GgRJun/DDB7dN2vGEtgL8DjCmQawyDnVARQxQtOJDXlk
# h36UYCRsr55JnOloXtLfm1OyCizDr9mpK656Ca/XllnKYBoF6WZ26DJSJhIv56sI
# UM+zRLdd2MQuA3WraPPLbfM6XKEW9Ea64DhkrG5kNXimoGMPLdNAk/jj3gcN1Vx5
# pUkp5w2+oBN3vpQ97/vjK1oQH01WKKJ6cuASOrdJXtjt7UORg9l7snuGG9k+sYxd
# 6IlPhBryoS9Z5JA7La4zWMW3Pv4y07MDPbGyr5I4ftKdgCz1TlaRITUlwzluZH9T
# upwPrRkjhMv0ugOGjfdf8NBSv4yUh7zAIXQlXxgotswnKDglmDlKNs98sZKuHCOn
# qWbsYR9q4ShJnV+I4iVd0yFLPlLEtVc/JAPw0XpbL9Uj43BdD1FGd7P4AOG8rAKC
# X9vAFbO9G9RVS+c5oQ/pI0m8GLhEfEXkwcNyeuBy5yTfv0aZxe/CHFfbg43sTUkw
# p6uO3+xbn6/83bBm4sGXgXvt1u1L50kppxMopqd9Z4DmimJ4X7IvhNdXnFy/dygo
# 8e1twyiPLI9AN0/B4YVEicQJTMXUpUMvdJX3bvh4IFgsE11glZo+TzOE2rCIF96e
# TvSWsLxGoGyY0uDWiIwLAgMBAAGjggHtMIIB6TAQBgkrBgEEAYI3FQEEAwIBADAd
# BgNVHQ4EFgQUSG5k5VAF04KqFzc3IrVtqMp1ApUwGQYJKwYBBAGCNxQCBAweCgBT
# AHUAYgBDAEEwCwYDVR0PBAQDAgGGMA8GA1UdEwEB/wQFMAMBAf8wHwYDVR0jBBgw
# FoAUci06AjGQQ7kUBU7h6qfHMdEjiTQwWgYDVR0fBFMwUTBPoE2gS4ZJaHR0cDov
# L2NybC5taWNyb3NvZnQuY29tL3BraS9jcmwvcHJvZHVjdHMvTWljUm9vQ2VyQXV0
# MjAxMV8yMDExXzAzXzIyLmNybDBeBggrBgEFBQcBAQRSMFAwTgYIKwYBBQUHMAKG
# Qmh0dHA6Ly93d3cubWljcm9zb2Z0LmNvbS9wa2kvY2VydHMvTWljUm9vQ2VyQXV0
# MjAxMV8yMDExXzAzXzIyLmNydDCBnwYDVR0gBIGXMIGUMIGRBgkrBgEEAYI3LgMw
# gYMwPwYIKwYBBQUHAgEWM2h0dHA6Ly93d3cubWljcm9zb2Z0LmNvbS9wa2lvcHMv
# ZG9jcy9wcmltYXJ5Y3BzLmh0bTBABggrBgEFBQcCAjA0HjIgHQBMAGUAZwBhAGwA
# XwBwAG8AbABpAGMAeQBfAHMAdABhAHQAZQBtAGUAbgB0AC4gHTANBgkqhkiG9w0B
# AQsFAAOCAgEAZ/KGpZjgVHkaLtPYdGcimwuWEeFjkplCln3SeQyQwWVfLiw++MNy
# 0W2D/r4/6ArKO79HqaPzadtjvyI1pZddZYSQfYtGUFXYDJJ80hpLHPM8QotS0LD9
# a+M+By4pm+Y9G6XUtR13lDni6WTJRD14eiPzE32mkHSDjfTLJgJGKsKKELukqQUM
# m+1o+mgulaAqPyprWEljHwlpblqYluSD9MCP80Yr3vw70L01724lruWvJ+3Q3fMO
# r5kol5hNDj0L8giJ1h/DMhji8MUtzluetEk5CsYKwsatruWy2dsViFFFWDgycSca
# f7H0J/jeLDogaZiyWYlobm+nt3TDQAUGpgEqKD6CPxNNZgvAs0314Y9/HG8VfUWn
# duVAKmWjw11SYobDHWM2l4bf2vP48hahmifhzaWX0O5dY0HjWwechz4GdwbRBrF1
# HxS+YWG18NzGGwS+30HHDiju3mUv7Jf2oVyW2ADWoUa9WfOXpQlLSBCZgB/QACnF
# sZulP0V3HjXG0qKin3p6IvpIlR+r+0cjgPWe+L9rt0uX4ut1eBrs6jeZeRhL/9az
# I2h15q/6/IvrC4DqaTuv/DDtBEyO3991bWORPdGdVk5Pv4BXIqF4ETIheu9BCrE/
# +6jMpF3BoYibV3FWTkhFwELJm3ZbCoBIa/15n8G9bW1qyVJzEw16UM0xggScMIIE
# mAIBATCBlTB+MQswCQYDVQQGEwJVUzETMBEGA1UECBMKV2FzaGluZ3RvbjEQMA4G
# A1UEBxMHUmVkbW9uZDEeMBwGA1UEChMVTWljcm9zb2Z0IENvcnBvcmF0aW9uMSgw
# JgYDVQQDEx9NaWNyb3NvZnQgQ29kZSBTaWduaW5nIFBDQSAyMDExAhMzAAAAww6b
# p9iy3PcsAAAAAADDMAkGBSsOAwIaBQCggbAwGQYJKoZIhvcNAQkDMQwGCisGAQQB
# gjcCAQQwHAYKKwYBBAGCNwIBCzEOMAwGCisGAQQBgjcCARUwIwYJKoZIhvcNAQkE
# MRYEFN+DZ0bL+taYVrVAuX8heT7hE2v4MFAGCisGAQQBgjcCAQwxQjBAoBaAFABQ
# AG8AdwBlAHIAUwBoAGUAbABsoSaAJGh0dHA6Ly93d3cubWljcm9zb2Z0LmNvbS9Q
# b3dlclNoZWxsIDANBgkqhkiG9w0BAQEFAASCAQBE2LLd7wkv5dW3o2h7mNXaZoIh
# rKQdSf9sSF3L4ArxsHxjn/66YPcNb4ThTkvdtY8NM8r7HaQuObsEKTyxrcHO+yyp
# dVYGTFJGXdj1bGLa662eXMylZhNIDhDL6zbGge3HI36cpZmngrW7cJnFaSqkOhRr
# DogIBv9jNY9qK46FMJEQa9MCmZRK54nN5iviAwEe+TVD84dYqalxsH0IQofDJYTi
# evVA1q1vfw3ZDQHen2D8Rr2cvgLvueDW36yGATq7HKE0DxzQr+Ba+F5VARrrPUl6
# okyYx0CI0FSxIUEauDnyFq424zFvTljYB1lU5WSqmlUzKKUSbR85GldDJweGoYIC
# KDCCAiQGCSqGSIb3DQEJBjGCAhUwggIRAgEBMIGOMHcxCzAJBgNVBAYTAlVTMRMw
# EQYDVQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdSZWRtb25kMR4wHAYDVQQKExVN
# aWNyb3NvZnQgQ29ycG9yYXRpb24xITAfBgNVBAMTGE1pY3Jvc29mdCBUaW1lLVN0
# YW1wIFBDQQITMwAAAL+RbPt8GiTgIgAAAAAAvzAJBgUrDgMCGgUAoF0wGAYJKoZI
# hvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMTcxMDI3MTI0NzQ5
# WjAjBgkqhkiG9w0BCQQxFgQUARjeOKJIZmGhM5GwLSeBdP9PVZcwDQYJKoZIhvcN
# AQEFBQAEggEAcXZeegd1utVGjAqEov4lCrL3LvtpuTq1FYOzXHjXnQAm4dqoO97g
# MgHRj4XQb+/M3IEtCFHHWQlKPb9Ai/iWYxQSjI+SF12xtyrGh9l36ur3yJAGPB5g
# pwHQBmbdTxtIU0fTjMo1bdjlOsPbQzoTnGqvafV5IVdLraOApl2gYO5TZ5SDNdEg
# 9i28htNbuTCxiDoOYsIQrOnvR+5JZgrqTeCbrEWHWLBhQFC04dkaCvPwRHXtFg7k
# Hm/xCWZoTUpMO0NlveGf/HqT7kbrHNcCEBkra7xA39dlS2dlQKxnep/D5gkWUdws
# ER3LrBsIcfrW2sjMUZjDpjP2B1c7+Rxb+g==
# SIG # End signature block
