## DEVELOPERS NOTES & CONVENTIONS
##
##  1. All text displayed to the user except for Write-Debug (or $PSCmdlet.WriteDebug()) text must be added to the
##     string tables in:
##         en-US\Plaster.psd1
##         Plaster.psm1
##  2. If a new manifest element is added, it must be added to the Schema\PlasterManifest-v1.xsd file and then
##     processed in the appropriate function in this script.  Any changes to <parameter> attributes must be
##     processed not only in the ProcessParameter function but also in the dynamicparam function.
##
##  3. Non-exported functions should avoid using the PowerShell standard Verb-Noun naming convention.
##     They should use PascalCase instead.
##
##  4. Please follow the scripting style of this file when adding new script.

function Invoke-Plaster {
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSAvoidShouldContinueWithoutForce', '', Scope='Function', Target='CopyFileWithConflictDetection')]
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSAvoidUsingConvertToSecureStringWithPlainText', '', Scope='Function', Target='ProcessParameter')]
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSShouldProcess', '', Scope='Function', Target='CopyFileWithConflictDetection')]
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSShouldProcess', '', Scope='Function', Target='ProcessFile')]
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSShouldProcess', '', Scope='Function', Target='ProcessModifyFile')]
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSShouldProcess', '', Scope='Function', Target='ProcessNewModuleManifest')]
    [System.Diagnostics.CodeAnalysis.SuppressMessage('PSShouldProcess', '', Scope='Function', Target='ProcessRequireModule')]
    [CmdletBinding(SupportsShouldProcess=$true)]
    param(
        [Parameter(Position = 0, Mandatory = $true)]
        [ValidateNotNullOrEmpty()]
        [string]
        $TemplatePath,

        [Parameter(Position = 1, Mandatory = $true)]
        [ValidateNotNullOrEmpty()]
        [string]
        $DestinationPath,

        [Parameter()]
        [switch]
        $Force,

        [Parameter()]
        [switch]
        $NoLogo,

        [Parameter()]
        [switch]
        $PassThru
    )

    # Process the template's Plaster manifest file to convert parameters defined there into dynamic parameters.
    dynamicparam {
        $paramDictionary = New-Object System.Management.Automation.RuntimeDefinedParameterDictionary

        $manifest = $null
        $manifestPath = $null
        $templateAbsolutePath = $null

        # Nothing to do until the TemplatePath parameter has been provided.
        if ($null -eq $TemplatePath) {
            return
        }

        try {
            # Let's convert non-terminating errors in this function to terminating so we
            # catch and format the error message as a warning.
            $ErrorActionPreference = 'Stop'

            # The constrained runspace is not available in the dynamicparam block.  Shouldn't be needed
            # since we are only evaluating the parameters in the manifest - no need for EvaluateConditionAttribute as we
            # are not building up multiple parametersets.  And no need for EvaluateAttributeValue since we are only
            # grabbing the parameter's value which is static.
            $templateAbsolutePath = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($TemplatePath)
            if (!(Test-Path -LiteralPath $templateAbsolutePath -PathType Container)) {
                throw ($LocalizedData.ErrorTemplatePathIsInvalid_F1 -f $templateAbsolutePath)
            }

            # Load manifest file using culture lookup
            $manifestPath = GetPlasterManifestPathForCulture $templateAbsolutePath $PSCulture
            if (($null -eq $manifestPath) -or (!(Test-Path $manifestPath))) {
                return
            }

            $manifest = Plaster\Test-PlasterManifest -Path $manifestPath -ErrorAction Stop 3>$null

            # The user-defined parameters in the Plaster manifest are converted to dynamic parameters
            # which allows the user to provide the parameters via the command line.
            # This enables non-interactive use cases.
            foreach ($node in $manifest.plasterManifest.parameters.ChildNodes) {
                if ($node -isnot [System.Xml.XmlElement]) {
                    continue
                }

                $name = $node.name
                $type = $node.type
                $prompt = if ($node.prompt) { $node.prompt } else { $LocalizedData.MissingParameterPrompt_F1 -f $name }

                if (!$name -or !$type) { continue }

                # Configure ParameterAttribute and add to attr collection
                $attributeCollection = New-Object System.Collections.ObjectModel.Collection[System.Attribute]
                $paramAttribute = New-Object System.Management.Automation.ParameterAttribute
                $paramAttribute.HelpMessage = $prompt
                $attributeCollection.Add($paramAttribute)

                switch -regex ($type) {
                    'text|user-fullname|user-email' {
                        $param = New-Object System.Management.Automation.RuntimeDefinedParameter `
                                     -ArgumentList ($name, [string], $attributeCollection)
                        break
                    }

                    'choice|multichoice' {
                        $choiceNodes = $node.ChildNodes
                        $setValues = New-Object string[] $choiceNodes.Count
                        $i = 0

                        foreach ($choiceNode in $choiceNodes){
                            $setValues[$i++] = $choiceNode.value
                        }

                        $validateSetAttr = New-Object System.Management.Automation.ValidateSetAttribute $setValues
                        $attributeCollection.Add($validateSetAttr)
                        $type = if ($type -eq 'multichoice') { [string[]] } else { [string] }
                        $param = New-Object System.Management.Automation.RuntimeDefinedParameter `
                                     -ArgumentList ($name, $type, $attributeCollection)
                        break
                    }

                    default { throw ($LocalizedData.UnrecognizedParameterType_F2 -f $type,$name) }
                }

                $paramDictionary.Add($name, $param)
            }
        }
        catch {
            Write-Warning ($LocalizedData.ErrorProcessingDynamicParams_F1 -f $_)
        }

        $paramDictionary
    }

    begin {
        # Write out the Plaster logo if necessary
        $plasterLogo = @'
  ____  _           _
 |  _ \| | __ _ ___| |_ ___ _ __
 | |_) | |/ _` / __| __/ _ \ '__|
 |  __/| | (_| \__ \ ||  __/ |
 |_|   |_|\__,_|___/\__\___|_|
'@

        if (!$NoLogo) {
            $versionString = "v$PlasterVersion"
            Write-Host $plasterLogo
            Write-Host ((" " * (50 - $versionString.Length)) + $versionString)
            Write-Host ("=" * 50)
        }

        $boundParameters = $PSBoundParameters
        $constrainedRunspace = $null
        $templateCreatedFiles = @{}
        $defaultValueStore = @{}
        $fileConflictConfirmNoToAll = $false
        $fileConflictConfirmYesToAll = $false
        $flags = @{
            DefaultValueStoreDirty = $false
        }

        # Verify TemplatePath parameter value is valid.
        $templateAbsolutePath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath($TemplatePath)
        if (!(Test-Path -LiteralPath $templateAbsolutePath -PathType Container)) {
            throw ($LocalizedData.ErrorTemplatePathIsInvalid_F1 -f $templateAbsolutePath)
        }

        # We will have a null manifest if the dynamicparam scriptblock was unable to load the template manifest
        # or it wasn't valid. If so, let's try to load it here. If anything, we can provide better errors here.
        if ($null -eq $manifest) {
            if ($null -eq $manifestPath) {
                $manifestPath = GetPlasterManifestPathForCulture $templateAbsolutePath $PSCulture
            }

            if (Test-Path -LiteralPath $manifestPath -PathType Leaf) {
                $manifest = Plaster\Test-PlasterManifest -Path $manifestPath -ErrorAction Stop 3>$null
                $PSCmdlet.WriteDebug("In begin, loading manifest file '$manifestPath'")
            }
            else {
                throw ($LocalizedData.ManifestFileMissing_F1 -f $manifestPath)
            }
        }

        # If the destination path doesn't exist, create it.
        $destinationAbsolutePath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath($DestinationPath)
        if (!(Test-Path -LiteralPath $destinationAbsolutePath)) {
            New-Item $destinationAbsolutePath -ItemType Directory > $null
        }

        # Prepare output object if user has specified the -PassThru parameter.
        if ($PassThru) {
            $InvokePlasterInfo = [PSCustomObject]@{
                TemplatePath = $templateAbsolutePath
                DestinationPath = $destinationAbsolutePath
                Success = $false
                TemplateType = if ($manifest.plasterManifest.templateType) {$manifest.plasterManifest.templateType} else {'Unspecified'}
                CreatedFiles = [string[]]@()
                UpdatedFiles = [string[]]@()
                MissingModules = [string[]]@()
                OpenFiles = [string[]]@()
            }
        }

        # Create the pre-defined Plaster variables.
        InitializePredefinedVariables $templateAbsolutePath $destinationAbsolutePath

        # Check for any existing default value store file and load default values if file exists.
        $templateId = $manifest.plasterManifest.metadata.id
        $templateVersion = $manifest.plasterManifest.metadata.version
        $templateName = $manifest.plasterManifest.metadata.name
        $storeFilename = "$templateName-$templateVersion-$templateId.clixml"
        $defaultValueStorePath = Join-Path $ParameterDefaultValueStoreRootPath $storeFilename
        if (Test-Path $defaultValueStorePath) {
            try {
                $PSCmdlet.WriteDebug("Loading default value store from '$defaultValueStorePath'.")
                $defaultValueStore = Import-Clixml $defaultValueStorePath -ErrorAction Stop
            }
            catch {
                Write-Warning ($LocalizedData.ErrorFailedToLoadStoreFile_F1 -f $defaultValueStorePath)
            }
        }

        function NewConstrainedRunspace() {
            $iss = [System.Management.Automation.Runspaces.InitialSessionState]::Create()
            if (!$IsCoreCLR) {
                $iss.ApartmentState = [System.Threading.ApartmentState]::STA
            }
            $iss.LanguageMode = [System.Management.Automation.PSLanguageMode]::ConstrainedLanguage
            $iss.DisableFormatUpdates = $true

            $sspe = New-Object System.Management.Automation.Runspaces.SessionStateProviderEntry 'Environment',([Microsoft.PowerShell.Commands.EnvironmentProvider]),$null
            $iss.Providers.Add($sspe)

            $sspe = New-Object System.Management.Automation.Runspaces.SessionStateProviderEntry 'FileSystem',([Microsoft.PowerShell.Commands.FileSystemProvider]),$null
            $iss.Providers.Add($sspe)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-Content',([Microsoft.PowerShell.Commands.GetContentCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-Date',([Microsoft.PowerShell.Commands.GetDateCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-ChildItem',([Microsoft.PowerShell.Commands.GetChildItemCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-Item',([Microsoft.PowerShell.Commands.GetItemCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-ItemProperty',([Microsoft.PowerShell.Commands.GetItemPropertyCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-Module',([Microsoft.PowerShell.Commands.GetModuleCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Get-Variable',([Microsoft.PowerShell.Commands.GetVariableCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Test-Path',([Microsoft.PowerShell.Commands.TestPathCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Out-String',([Microsoft.PowerShell.Commands.OutStringCommand]),$null
            $iss.Commands.Add($ssce)

            $ssce = New-Object System.Management.Automation.Runspaces.SessionStateCmdletEntry 'Compare-Object',([Microsoft.PowerShell.Commands.CompareObjectCommand]),$null
            $iss.Commands.Add($ssce)

            $scopedItemOptions = [System.Management.Automation.ScopedItemOptions]::AllScope
            $plasterVars = Get-Variable -Name PLASTER_*,PSVersionTable
            if (Test-Path Variable:\IsLinux) {
                $plasterVars += Get-Variable -Name IsLinux
            }
            if (Test-Path Variable:\IsOSX) {
                $plasterVars += Get-Variable -Name IsOSX
            }
            if (Test-Path Variable:\IsMacOS) {
                $plasterVars += Get-Variable -Name IsMacOS
            }
            if (Test-Path Variable:\IsWindows) {
                $plasterVars += Get-Variable -Name IsWindows
            }
            foreach ($var in $plasterVars) {
                $ssve = New-Object System.Management.Automation.Runspaces.SessionStateVariableEntry `
                            $var.Name,$var.Value,$var.Description,$scopedItemOptions
                $iss.Variables.Add($ssve)
            }

            # Create new runspace with the above defined entries. Then open and set its working dir to $destinationAbsolutePath
            # so all condition attribute expressions can use a relative path to refer to file paths e.g.
            # condition="Test-Path src\${PLASTER_PARAM_ModuleName}.psm1"
            $runspace = [System.Management.Automation.Runspaces.RunspaceFactory]::CreateRunspace($iss)
            $runspace.Open()
            if ($destinationAbsolutePath) {
                $runspace.SessionStateProxy.Path.SetLocation($destinationAbsolutePath) > $null
            }
            $runspace
        }

        function ExecuteExpressionImpl([string]$Expression) {
            try {
                $powershell = [PowerShell]::Create()

                if ($null -eq $constrainedRunspace) {
                    $constrainedRunspace = NewConstrainedRunspace
                }
                $powershell.Runspace = $constrainedRunspace

                try {
                    $powershell.AddScript($Expression) > $null
                    $res = $powershell.Invoke()
                    $res
                }
                catch {
                    throw ($LocalizedData.ExpressionInvalid_F2 -f $Expression,$_)
                }

                # Check for non-terminating errors.
                if ($powershell.Streams.Error.Count -gt 0) {
                    $err = $powershell.Streams.Error[0]
                    throw ($LocalizedData.ExpressionNonTermErrors_F2 -f $Expression,$err)
                }
            }
            finally {
                if ($powershell) {
                    $powershell.Dispose()
                }
            }
        }

        function InterpolateAttributeValue([string]$Value, [string]$Location) {
            if ($null -eq $Value) {
                return [string]::Empty
            }
            elseif ([string]::IsNullOrWhiteSpace($Value)) {
                return $Value
            }

            try {
                $res = @(ExecuteExpressionImpl "`"$Value`"")
                [string]$res[0]
            }
            catch {
                throw ($LocalizedData.InterpolationError_F3 -f $Value.Trim(),$Location,$_)
            }
        }

        function EvaluateConditionAttribute([string]$Expression, [string]$Location) {
            if ($null -eq $Expression) {
                return [string]::Empty
            }
            elseif ([string]::IsNullOrWhiteSpace($Expression)) {
                return $Expression
            }

            try {
                $res = @(ExecuteExpressionImpl $Expression)
                [bool]$res[0]
            }
            catch {
                throw ($LocalizedData.ExpressionInvalidCondition_F3 -f $Expression,$Location,$_)
            }
        }

        function EvaluateExpression([string]$Expression, [string]$Location) {
            if ($null -eq $Expression) {
                return [string]::Empty
            }
            elseif ([string]::IsNullOrWhiteSpace($Expression)) {
                return $Expression
            }

            try {
                $res = @(ExecuteExpressionImpl $Expression)
                [string]$res[0]
            }
            catch {
                throw ($LocalizedData.ExpressionExecError_F2 -f $Location,$_)
            }
        }

        function EvaluateScript([string]$Script, [string]$Location) {
            if ($null -eq $Script) {
                return @([string]::Empty)
            }
            elseif ([string]::IsNullOrWhiteSpace($Script)) {
                return $Script
            }

            try {
                $res = @(ExecuteExpressionImpl $Script)
                [string[]]$res
            }
            catch {
                throw ($LocalizedData.ExpressionExecError_F2 -f $Location,$_)
            }
        }

        function GetErrorLocationFileAttrVal([string]$ElementName, [string]$AttributeName) {
            $LocalizedData.ExpressionErrorLocationFile_F2 -f $ElementName,$AttributeName
        }

        function GetErrorLocationModifyAttrVal([string]$AttributeName) {
            $LocalizedData.ExpressionErrorLocationModify_F1 -f $AttributeName
        }

        function GetErrorLocationNewModManifestAttrVal([string]$AttributeName) {
            $LocalizedData.ExpressionErrorLocationNewModManifest_F1 -f $AttributeName
        }

        function GetErrorLocationParameterAttrVal([string]$ParameterName, [string]$AttributeName) {
            $LocalizedData.ExpressionErrorLocationParameter_F2 -f $ParameterName,$AttributeName
        }

        function GetErrorLocationRequireModuleAttrVal([string]$ModuleName, [string]$AttributeName) {
            $LocalizedData.ExpressionErrorLocationRequireModule_F2 -f $ModuleName,$AttributeName
        }

        function ConvertToDestinationRelativePath($Path) {
            $fullDestPath = $DestinationPath
            if (![System.IO.Path]::IsPathRooted($fullDestPath)) {
                $fullDestPath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath($DestinationPath)
            }

            $fullPath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath($Path)
            if (!$fullPath.StartsWith($fullDestPath, 'OrdinalIgnoreCase')) {
                throw ($LocalizedData.ErrorPathMustBeUnderDestPath_F2 -f $fullPath, $fullDestPath)
            }

            $fullPath.Substring($fullDestPath.Length).TrimStart('\','/')
        }

        function VerifyPathIsUnderDestinationPath([ValidateNotNullOrEmpty()][string]$FullPath) {
            if (![System.IO.Path]::IsPathRooted($FullPath)) {
                $PSCmdlet.WriteDebug("The FullPath parameter '$FullPath' must be an absolute path.")
            }

            $fullDestPath = $DestinationPath
            if (![System.IO.Path]::IsPathRooted($fullDestPath)) {
                $fullDestPath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath($DestinationPath)
            }

            if (!$FullPath.StartsWith($fullDestPath, [StringComparison]::OrdinalIgnoreCase)) {
                throw ($LocalizedData.ErrorPathMustBeUnderDestPath_F2 -f $FullPath, $fullDestPath)
            }
        }

        function WriteContentWithEncoding([string]$path, [string[]]$content, [string]$encoding) {
            if ($encoding -match '-nobom') {
                $encoding,$dummy = $encoding -split '-'

                $noBomEncoding = $null
                switch ($encoding) {
                    'utf8' { $noBomEncoding = New-Object System.Text.UTF8Encoding($false) }
                }

                if ($null -eq $content) {
                    $content = [string]::Empty
                }

                [System.IO.File]::WriteAllLines($path, $content, $noBomEncoding)
            }
            else {
                Set-Content -LiteralPath $path -Value $content -Encoding $encoding
            }
        }

        function ColorForOperation($operation) {
            switch ($operation) {
                $LocalizedData.OpConflict      { 'Red' }
                $LocalizedData.OpCreate        { 'Green' }
                $LocalizedData.OpForce         { 'Yellow' }
                $LocalizedData.OpIdentical     { 'Cyan' }
                $LocalizedData.OpModify        { 'Magenta' }
                $LocalizedData.OpUpdate        { 'Green' }
                $LocalizedData.OpMissing       { 'Red' }
                $LocalizedData.OpVerify        { 'Green' }
                default { $Host.UI.RawUI.ForegroundColor }
            }
        }

        function GetMaxOperationLabelLength {
            ($LocalizedData.OpCreate,   $LocalizedData.OpIdentical,
             $LocalizedData.OpConflict, $LocalizedData.OpForce,
             $LocalizedData.OpMissing,  $LocalizedData.OpModify,
             $LocalizedData.OpUpdate,   $LocalizedData.OpVerify |
                 Measure-Object -Property Length -Maximum).Maximum
        }

        function WriteOperationStatus($operation, $message) {
            $maxLen = GetMaxOperationLabelLength
            Write-Host ("{0,$maxLen} " -f $operation) -ForegroundColor (ColorForOperation $operation) -NoNewline
            Write-Host $message
        }

        function WriteOperationAdditionalStatus([string[]]$Message) {
            $maxLen = GetMaxOperationLabelLength
            foreach ($msg in $Message) {
                $lines = $msg -split "`n"
                foreach ($line in $lines) {
                    Write-Host ("{0,$maxLen} {1}" -f "",$line)
                }
            }
        }

        function GetGitConfigValue($name) {
            # Very simplistic git config lookup
            # Won't work with namespace, just use final element, e.g. 'name' instead of 'user.name'

            # The $Home dir may not be reachable e.g. if on network share and/or script not running as admin.
            # See issue https://github.com/PowerShell/Plaster/issues/92
            if (!(Test-Path -LiteralPath $Home)) {
                return
            }

            $gitConfigPath = Join-Path $Home '.gitconfig'
            $PSCmdlet.WriteDebug("Looking for '$name' value in Git config: $gitConfigPath")

            if (Test-Path -LiteralPath $gitConfigPath) {
                $matches = Select-String -LiteralPath $gitConfigPath -Pattern "\s+$name\s+=\s+(.+)$"
                if (@($matches).Count -gt 0)
                {
                    $matches.Matches.Groups[1].Value
                }
            }
        }

        function PromptForInput($prompt, $default) {
            do {
                $value = Read-Host -Prompt $prompt
                if (!$value -and $default) {
                    $value = $default
                }
            } while (!$value)

            $value
        }

        function PromptForChoice([string]$ParameterName, [ValidateNotNull()]$ChoiceNodes, [string]$prompt,
                                 [int[]]$defaults, [switch]$IsMultiChoice) {
            $choices = New-Object 'System.Collections.ObjectModel.Collection[System.Management.Automation.Host.ChoiceDescription]'
            $values = New-Object object[] $ChoiceNodes.Count
            $i = 0

            foreach ($choiceNode in $ChoiceNodes) {
                $label = InterpolateAttributeValue $choiceNode.label (GetErrorLocationParameterAttrVal $ParameterName label)
                $help  = InterpolateAttributeValue $choiceNode.help  (GetErrorLocationParameterAttrVal $ParameterName help)
                $value = InterpolateAttributeValue $choiceNode.value (GetErrorLocationParameterAttrVal $ParameterName value)

                $choice = New-Object System.Management.Automation.Host.ChoiceDescription -Arg $label,$help
                $choices.Add($choice)
                $values[$i++] = $value
            }

            $retval = [PSCustomObject]@{Values=@(); Indices=@()}

            if ($IsMultiChoice) {
                $selections = $Host.UI.PromptForChoice('', $prompt, $choices, $defaults)
                foreach ($selection in $selections) {
                    $retval.Values += $values[$selection]
                    $retval.Indices += $selection
                }
            }
            else {
                if ($defaults.Count -gt 1) {
                    throw ($LocalizedData.ParameterTypeChoiceMultipleDefault_F1 -f $ChoiceNodes.ParentNode.name)
                }

                $selection = $Host.UI.PromptForChoice('', $prompt, $choices, $defaults[0])
                $retval.Values = $values[$selection]
                $retval.Indices = $selection
            }

            $retval
        }

        # All Plaster variables should be set via this method so that the ConstrainedRunspace can be
        # configured to use the new variable. This method will null out the ConstrainedRunspace so that
        # later, when we need to evaluate script in that runspace, it will get recreated first with all
        # the latest Plaster variables.
        function SetPlasterVariable() {
            param(
                [Parameter(Mandatory=$true)]
                [ValidateNotNullOrEmpty()]
                [string]$Name,

                [Parameter(Mandatory=$true)]
                [AllowNull()]
                $Value,

                [Parameter()]
                [bool]
                $IsParam = $true
            )

            # Variables created from a <parameter> in the Plaster manifset are prefixed PLASTER_PARAM all others
            # are just PLASTER_.
            $variableName = if ($IsParam) { "PLASTER_PARAM_$Name" } else { "PLASTER_$Name" }

            Set-Variable -Name $variableName -Value $Value -Scope Script -WhatIf:$false

            # If the constrained runspace has been created, it needs to be disposed so that the next string
            # expansion (or condition eval) gets an updated runspace that contains this variable or its new value.
            if ($null -ne $script:ConstrainedRunspace) {
                $script:ConstrainedRunspace.Dispose()
                $script:ConstrainedRunspace = $null
            }
        }

        function ProcessParameter([ValidateNotNull()]$Node) {
            $name = $Node.name
            $type = $Node.type
            $store = $Node.store

            $condition = $Node.condition
            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)>'")) {
                # Define the parameter so later conditions can use it but its value will be $null
                SetPlasterVariable -Name $name -Value $null -IsParam $true
                $PSCmdlet.WriteDebug("Skipping parameter $($Node.localName), condition evaluated to false.")
                return
            }

            $prompt = InterpolateAttributeValue $Node.prompt (GetErrorLocationParameterAttrVal $name prompt)
            $default = InterpolateAttributeValue $Node.default (GetErrorLocationParameterAttrVal $name default)

            # Check if parameter was provided via a dynamic parameter.
            if ($boundParameters.ContainsKey($name)) {
                $value = $boundParameters[$name]
            }
            else {
                # Not a dynamic parameter so prompt user for the value but first check for a stored default value.
                if ($store -and ($null -ne $defaultValueStore[$name])) {
                    $default = $defaultValueStore[$name]
                    $PSCmdlet.WriteDebug("Read default value '$default' for parameter '$name' from default value store.")

                    if (($store -eq 'encrypted') -and ($default -is [System.Security.SecureString])) {
                        try {
                            $cred = New-Object -TypeName PSCredential -ArgumentList 'jsbplh',$default
                            $default = $cred.GetNetworkCredential().Password
                            $PSCmdlet.WriteDebug("Unencrypted default value for parameter '$name'.")
                        }
                        catch [System.Exception] {
                            Write-Warning ($LocalizedData.ErrorUnencryptingSecureString_F1 -f $name)
                        }
                    }
                }

                # If the prompt message failed to evaluate or was empty, supply a diagnostic prompt message
                if (!$prompt) {
                    $prompt = $LocalizedData.MissingParameterPrompt_F1 -f $name
                }

                # Some default values might not come from the template e.g. some are harvested from .gitconfig if it exists.
                $defaultNotFromTemplate = $false

                # Now prompt user for parameter value based on the parameter type.
                switch -regex ($type) {
                    'text' {
                        # Display an appropriate "default" value in the prompt string.
                        if ($default) {
                            if ($store -eq 'encrypted') {
                                $obscuredDefault = $default -replace '(....).*', '$1****'
                                $prompt += " ($obscuredDefault)"
                            }
                            else {
                                $prompt += " ($default)"
                            }
                        }
                        # Prompt the user for text input.
                        $value = PromptForInput $prompt $default
                        $valueToStore = $value
                    }
                    'user-fullname' {
                        # If no default, try to get a name from git config.
                        if (!$default) {
                            $default = GetGitConfigValue('name')
                            $defaultNotFromTemplate = $true
                        }

                        if ($default) {
                            if ($store -eq 'encrypted') {
                                $obscuredDefault = $default -replace '(....).*', '$1****'
                                $prompt += " ($obscuredDefault)"
                            }
                            else {
                                $prompt += " ($default)"
                            }
                        }

                        # Prompt the user for text input.
                        $value = PromptForInput $prompt $default
                        $valueToStore = $value
                    }
                    'user-email' {
                        # If no default, try to get an email from git config
                        if (-not $default) {
                            $default = GetGitConfigValue('email')
                            $defaultNotFromTemplate = $true
                        }

                        if ($default) {
                            if ($store -eq 'encrypted') {
                                $obscuredDefault = $default -replace '(....).*', '$1****'
                                $prompt += " ($obscuredDefault)"
                            }
                            else {
                                $prompt += " ($default)"
                            }
                        }

                        # Prompt the user for text input.
                        $value = PromptForInput $prompt $default
                        $valueToStore = $value
                    }
                    'choice|multichoice' {
                        $choices = $Node.ChildNodes
                        $defaults = [int[]]($default -split ',')

                        # Prompt the user for choice or multichoice selection input.
                        $selections = PromptForChoice $name $choices $prompt $defaults -IsMultiChoice:($type -eq 'multichoice')
                        $value = $selections.Values
                        $OFS = ","
                        $valueToStore = "$($selections.Indices)"
                    }
                    default  { throw ($LocalizedData.UnrecognizedParameterType_F2 -f $type, $Node.LocalName) }
                }

                # If parameter specifies that user's input be stored as the default value,
                # store it to file if the value has changed.
                if ($store -and (($default -ne $valueToStore) -or $defaultNotFromTemplate)) {
                    if ($store -eq 'encrypted') {
                        $PSCmdlet.WriteDebug("Storing new, encrypted default value for parameter '$name' to default value store.")
                        $defaultValueStore[$name] = ConvertTo-SecureString -String $valueToStore -AsPlainText -Force
                    }
                    else {
                        $PSCmdlet.WriteDebug("Storing new default value '$valueToStore' for parameter '$name' to default value store.")
                        $defaultValueStore[$name] = $valueToStore
                    }

                    $flags.DefaultValueStoreDirty = $true
                }
            }

            # Make template defined parameters available as a PowerShell variable PLASTER_PARAM_<parameterName>.
            SetPlasterVariable -Name $name -Value $value -IsParam $true
        }

        function ProcessMessage([ValidateNotNull()]$Node) {
            $text = InterpolateAttributeValue $Node.InnerText '<message>'
            $nonewline = $Node.nonewline -eq 'true'

            # Eliminate whitespace before and after the text that just happens to get inserted because you want
            # the text on different lines than the start/end element tags.
            $trimmedText = $text -replace '^[ \t]*\n','' -replace '\n[ \t]*$',''

            $condition  = $Node.condition
            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)>'")) {
                $debugText = $trimmedText -replace '\r|\n',' '
                $maxLength = [Math]::Min(40, $debugText.Length)
                $PSCmdlet.WriteDebug("Skipping message '$($debugText.Substring(0, $maxLength))', condition evaluated to false.")
                return
            }

            Write-Host $trimmedText -NoNewline:($nonewline -eq 'true')
        }

        function CopyModuleManifestPropertyToHashtable([PSModuleInfo]$oldModuleManifest, [hashtable]$hashtable, [string[]]$Property) {
            foreach ($prop in $Property) {
                if ($oldModuleManifest.$prop) {
                    $hashtable["$prop"] = $oldModuleManifest.$prop
                }
            }
        }

        function ProcessNewModuleManifest([ValidateNotNull()]$Node) {
            $moduleVersion = InterpolateAttributeValue $Node.moduleVersion (GetErrorLocationNewModManifestAttrVal moduleVersion)
            $rootModule = InterpolateAttributeValue $Node.rootModule (GetErrorLocationNewModManifestAttrVal rootModule)
            $author = InterpolateAttributeValue $Node.author (GetErrorLocationNewModManifestAttrVal author)
            $companyName = InterpolateAttributeValue $Node.companyName (GetErrorLocationNewModManifestAttrVal companyName)
            $description = InterpolateAttributeValue $Node.description (GetErrorLocationNewModManifestAttrVal description)
            $dstRelPath = InterpolateAttributeValue $Node.destination (GetErrorLocationNewModManifestAttrVal destination)
            $powerShellVersion = InterpolateAttributeValue $Node.powerShellVersion (GetErrorLocationNewModManifestAttrVal powerShellVersion)
            $nestedModules = InterpolateAttributeValue $Node.NestedModules (GetErrorLocationNewModManifestAttrVal NestedModules)
            $dscResourcesToExport = InterpolateAttributeValue $Node.DscResourcesToExport (GetErrorLocationNewModManifestAttrVal DscResourcesToExport)

            # We could choose to not check this if the condition eval'd to false
            # but I think it is better to let the template author know they've broken the
            # rules for any of the file directives (not just the ones they're testing/enabled).
            if ([System.IO.Path]::IsPathRooted($dstRelPath)) {
                throw ($LocalizedData.ErrorPathMustBeRelativePath_F2 -f $dstRelPath,$Node.LocalName)
            }

            $dstPath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath((Join-Path $DestinationPath $dstRelPath))

            $condition  = $Node.condition
            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)>'")) {
                $PSCmdlet.WriteDebug("Skipping module manifest generation for '$dstPath', condition evaluated to false.")
                return
            }

            $encoding = $Node.encoding
            if (!$encoding) {
                $encoding = $DefaultEncoding
            }

            if ($PSCmdlet.ShouldProcess($dstPath, $LocalizedData.ShouldProcessNewModuleManifest)) {
                $manifestDir = Split-Path $dstPath -Parent
                if (!(Test-Path $manifestDir)) {
                    VerifyPathIsUnderDestinationPath $manifestDir
                    Write-Verbose ($LocalizedData.NewModManifest_CreatingDir_F1 -f $manifestDir)
                    New-Item $manifestDir -ItemType Directory > $null
                }

                $newModuleManifestParams = @{}

                # If there is an existing module manifest, load it so we can reuse old values not specified by
                # template.
                if (Test-Path -LiteralPath $dstPath) {
                    $oldModuleManifest = Test-ModuleManifest -Path $dstPath -ErrorAction SilentlyContinue
                    if ($? -and $oldModuleManifest) {
                        $props = 'Guid', 'Description', 'DefaultCommandPrefix', 'RootModule', 'AliasesToExport',
                                 'CmdletsToExport', 'DscResourcesToExport', 'VariablesToExport',
                                 'FormatsToProcess', 'TypesToProcess', 'ScriptsToProcess', 'NestedModules',
                                 'PowerShellVersion'

                        CopyModuleManifestPropertyToHashtable $oldModuleManifest $newModuleManifestParams $props
                    }
                }

                if (![string]::IsNullOrWhiteSpace($moduleVersion)) {
                    $newModuleManifestParams['ModuleVersion'] = $moduleVersion
                }
                if (![string]::IsNullOrWhiteSpace($rootModule)) {
                    $newModuleManifestParams['RootModule'] = $rootModule
                }
                if (![string]::IsNullOrWhiteSpace($author)) {
                    $newModuleManifestParams['Author'] = $author
                }
                if (![string]::IsNullOrWhiteSpace($companyName)) {
                    $newModuleManifestParams['CompanyName'] = $companyName
                }
                if (![string]::IsNullOrWhiteSpace($description)) {
                    $newModuleManifestParams['Description'] = $description
                }
                if (![string]::IsNullOrWhiteSpace($powerShellVersion)) {
                    $newModuleManifestParams['PowerShellVersion'] = $powerShellVersion
                }
                if (![string]::IsNullOrWhiteSpace($nestedModules)) {
                    $newModuleManifestParams['NestedModules'] = $nestedModules
                }
                if (![string]::IsNullOrWhiteSpace($dscResourcesToExport)) {
                    $newModuleManifestParams['DscResourcesToExport'] = $dscResourcesToExport
                }

                $tempFile = $null

                try {
                    $tempFileBaseName = "moduleManifest-" + [Guid]::NewGuid()
                    $tempFile = [System.IO.Path]::GetTempPath() + "${tempFileBaseName}.psd1"
                    $PSCmdlet.WriteDebug("Created temp file for new module manifest - $tempFile")
                    $newModuleManifestParams['Path'] = $tempFile

                    # Generate manifest into a temp file.
                    New-ModuleManifest @newModuleManifestParams

                    # Typically the manifest is re-written with a new encoding (UTF8-NoBOM) because Git hates UTF-16.
                    $content = Get-Content -LiteralPath $tempFile -Raw

                    # Replace the temp filename in the generated manifest file's comment header with the actual filename.
                    $dstBaseName = [System.IO.Path]::GetFileNameWithoutExtension($dstPath)
                    $content = $content -replace "(?<=\s*#.*?)$tempFileBaseName", $dstBaseName

                    WriteContentWithEncoding -Path $tempFile -Content $content -Encoding $encoding

                    CopyFileWithConflictDetection $tempFile $dstPath

                    if ($PassThru -and ($Node.openInEditor -eq 'true')) {
                        $InvokePlasterInfo.OpenFiles += $dstPath
                    }
                }
                finally {
                    if ($tempFile -and (Test-Path $tempFile)) {
                        Remove-Item -LiteralPath $tempFile
                        $PSCmdlet.WriteDebug("Removed temp file for new module manifest - $tempFile")
                    }
                }
            }
        }

        #
        # Begin ProcessFile helper methods
        #
        function NewBackupFilename([string]$Path) {
            $dir = [System.IO.Path]::GetDirectoryName($Path)
            $filename = [System.IO.Path]::GetFileName($Path)
            $backupPath = Join-Path -Path $dir -ChildPath "${filename}.bak"
            $i = 1;
            while (Test-Path -LiteralPath $backupPath) {
                $backupPath = Join-Path -Path $dir -ChildPath "${filename}.bak$i"
                $i++
            }

            $backupPath
        }

        function AreFilesIdentical($Path1, $Path2) {
            $file1 = Get-Item -LiteralPath $Path1 -Force
            $file2 = Get-Item -LiteralPath $Path2 -Force

            if ($file1.Length -ne $file2.Length) {
                return $false
            }

            $hash1 = (Get-FileHash -LiteralPath $path1 -Algorithm SHA1).Hash
            $hash2 = (Get-FileHash -LiteralPath $path2 -Algorithm SHA1).Hash

            $hash1 -eq $hash2
        }

        function NewFileSystemCopyInfo([string]$srcPath, [string]$dstPath) {
            [PSCustomObject]@{SrcFileName=$srcPath; DstFileName=$dstPath}
        }

        function ExpandFileSourceSpec([string]$srcRelPath, [string]$dstRelPath) {
            $srcPath = Join-Path $templateAbsolutePath $srcRelPath
            $dstPath = Join-Path $destinationAbsolutePath $dstRelPath

            if ($srcRelPath.IndexOfAny([char[]]('*','?')) -lt 0) {
                # No wildcard spec in srcRelPath so return info on single file.
                # Also, if dstRelPath is empty, then use source rel path.
                if (!$dstRelPath) {
                    $dstPath = Join-Path $destinationAbsolutePath $srcRelPath
                }

                return NewFileSystemCopyInfo $srcPath $dstPath
            }

            # Prepare parameter values for call to Get-ChildItem to get list of files based on wildcard spec.
            $gciParams = @{}
            $parent = Split-Path $srcPath -Parent
            $leaf = Split-Path $srcPath -Leaf
            $gciParams['LiteralPath'] = $parent
            $gciParams['File'] = $true

            if ($leaf -eq '**') {
                $gciParams['Recurse'] = $true
            }
            else {
                if ($leaf.IndexOfAny([char[]]('*','?')) -ge 0) {
                    $gciParams['Filter'] = $leaf
                }

                $leaf = Split-Path $parent -Leaf
                if ($leaf -eq '**') {
                    $parent = Split-Path $parent -Parent
                    $gciParams['LiteralPath'] = $parent
                    $gciParams['Recurse'] = $true
                }
            }

            $srcRelRootPathLength = $gciParams['LiteralPath'].Length

            # Generate a FileCopyInfo object for every file expanded by the wildcard spec.
            $files = @(Microsoft.PowerShell.Management\Get-ChildItem @gciParams)
            foreach ($file in $files) {
                $fileSrcPath = $file.FullName
                $relPath = $fileSrcPath.Substring($srcRelRootPathLength)
                $fileDstPath = Join-Path $dstPath $relPath
                NewFileSystemCopyInfo $fileSrcPath $fileDstPath
            }

            # Copy over empty directories - if any.
            $gciParams.Remove('File')
            $gciParams['Directory'] = $true
            $dirs = @(Microsoft.PowerShell.Management\Get-ChildItem @gciParams |
                          Where-Object {$_.GetFileSystemInfos().Length -eq 0})
            foreach ($dir in $dirs) {
                $dirSrcPath = $dir.FullName
                $relPath = $dirSrcPath.Substring($srcRelRootPathLength)
                $dirDstPath = Join-Path $dstPath $relPath
                NewFileSystemCopyInfo $dirSrcPath $dirDstPath
            }
        }

        # Plaster zen for file handling.  All file related operations should use this method
        # to actually write/overwrite/modify files in the DestinationPath.  This method
        # handles detecting conflicts, gives the user a chance to determine how to handle
        # conflicts.  The user can choose to use the Force parameter to force the overwriting
        # of existing files at the destination path.
        # File processing (expanding substitution variable, modifying file contents) should always
        # be done to a temp file (be sure to always remove temp file when done).  That temp file
        # is what gets passed to this function as the $SrcPath.  This allows Plaster to alert the
        # user when the repeated application of a template will modify any existing file.
        # NOTE: Plaster keeps track of which files it has "created" (as opposed to overwritten)
        # so that any later change to that file doesn't trigger conflict handling.
        function CopyFileWithConflictDetection([string]$SrcPath, [string]$DstPath) {
            # Just double-checking that DstPath parameter is an absolute path otherwise
            # it could fail the check that the DstPath is under the overall DestinationPath.
            if (![System.IO.Path]::IsPathRooted($DstPath)) {
                $DstPath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath($DstPath)
            }

            # Check if DstPath file conflicts with an existing SrcPath file.
            $operation = $LocalizedData.OpCreate
            $opmessage = (ConvertToDestinationRelativePath $DstPath)
            if (Test-Path -LiteralPath $DstPath) {
                if (AreFilesIdentical $SrcPath $DstPath) {
                    $operation = $LocalizedData.OpIdentical
                }
                elseif ($templateCreatedFiles.ContainsKey($DstPath)) {
                    # Plaster created this file previously during template invocation
                    # therefore, there is no conflict.  We're simply updating the file.
                    $operation = $LocalizedData.OpUpdate
                }
                elseif ($Force) {
                    $operation = $LocalizedData.OpForce
                }
                else  {
                    $operation = $LocalizedData.OpConflict
                }
            }

            # Copy the file to the destination
            if ($PSCmdlet.ShouldProcess($DstPath, $operation)) {
                WriteOperationStatus $operation $opmessage

                if ($operation -eq $LocalizedData.OpIdentical) {
                    # If the files are identical, no need to do anything
                    return
                }

                if (($operation -eq $LocalizedData.OpCreate) -or ($operation -eq $LocalizedData.OpUpdate)) {
                    Copy-Item -LiteralPath $SrcPath -Destination $DstPath
                    if ($PassThru) {
                        $InvokePlasterInfo.CreatedFiles += $DstPath
                    }
                    $templateCreatedFiles[$DstPath] = $null
                }
                elseif ($Force -or $PSCmdlet.ShouldContinue(($LocalizedData.OverwriteFile_F1 -f $DstPath),
                                                             $LocalizedData.FileConflict,
                                                             [ref]$fileConflictConfirmYesToAll,
                                                             [ref]$fileConflictConfirmNoToAll)) {
                    $backupFilename = NewBackupFilename $DstPath
                    Copy-Item -LiteralPath $DstPath -Destination $backupFilename
                    Copy-Item -LiteralPath $SrcPath -Destination $DstPath
                    if ($PassThru) {
                        $InvokePlasterInfo.UpdatedFiles += $DstPath
                    }
                    $templateCreatedFiles[$DstPath] = $null
                }
            }
        }

        #
        # End ProcessFile helper methods
        #

        # Processes both the <file> and <templateFile> directives.
        function ProcessFile([ValidateNotNull()]$Node) {
            $srcRelPath = InterpolateAttributeValue $Node.source (GetErrorLocationFileAttrVal $Node.localName source)
            $dstRelPath = InterpolateAttributeValue $Node.destination (GetErrorLocationFileAttrVal $Node.localName destination)

            # We could choose to not check this if the condition eval'd to false
            # but I think it is better to let the template author know they've broken the
            # rules for any of the file directives (not just the ones they're testing/enabled).
            if ([System.IO.Path]::IsPathRooted($srcRelPath)) {
                throw ($LocalizedData.ErrorPathMustBeRelativePath_F2 -f $srcRelPath,$Node.LocalName)
            }

            if ([System.IO.Path]::IsPathRooted($dstRelPath)) {
                throw ($LocalizedData.ErrorPathMustBeRelativePath_F2 -f $dstRelPath,$Node.LocalName)
            }

            $condition  = $Node.condition
            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)>'")) {
                $PSCmdlet.WriteDebug("Skipping $($Node.localName) '$srcRelPath' -> '$dstRelPath', condition evaluated to false.")
                return
            }

            # Check if node is the specialized, <templateFile> node.
            # Only <templateFile> nodes expand templates and use the encoding attribute.
            $isTemplateFile = $Node.localName -eq 'templateFile'
            if ($isTemplateFile) {
                $encoding = $Node.encoding
                if (!$encoding) {
                    $encoding = $DefaultEncoding
                }
            }

            # Check if source specifies a wildcard and if so, expand the wildcard
            # and then process each file system object (file or empty directory).
            $fileSystemCopyInfoObjs = ExpandFileSourceSpec $srcRelPath $dstRelPath
            foreach ($fileSystemCopyInfo in $fileSystemCopyInfoObjs) {
                $srcPath = $fileSystemCopyInfo.SrcFileName
                $dstPath = $fileSystemCopyInfo.DstFileName

                # The file's destination path must be under the DestinationPath specified by the user.
                VerifyPathIsUnderDestinationPath $dstPath

                # Check to see if we're copying an empty dir
                if (Test-Path -LiteralPath $srcPath -PathType Container) {
                    if (!(Test-Path -LiteralPath $dstPath)) {
                        if ($PSCmdlet.ShouldProcess($parentDir, $LocalizedData.ShouldProcessCreateDir)) {
                            WriteOperationStatus $LocalizedData.OpCreate `
                                ($dstRelPath.TrimEnd(([char]'\'),([char]'/')) + [System.IO.Path]::DirectorySeparatorChar)
                            New-Item -Path $dstPath -ItemType Directory > $null
                        }
                    }

                    continue
                }

                # If the file's parent dir doesn't exist, create it.
                $parentDir = Split-Path $dstPath -Parent
                if (!(Test-Path -LiteralPath $parentDir)) {
                    if ($PSCmdlet.ShouldProcess($parentDir, $LocalizedData.ShouldProcessCreateDir)) {
                        New-Item -Path $parentDir -ItemType Directory > $null
                    }
                }

                $tempFile = $null

                try {
                    # If processing a <templateFile>, copy to a temp file to expand the template file,
                    # then apply the normal file conflict detection/resolution handling.
                    $target = $LocalizedData.TempFileTarget_F1 -f (ConvertToDestinationRelativePath $dstPath)
                    if ($isTemplateFile -and $PSCmdlet.ShouldProcess($target, $LocalizedData.ShouldProcessExpandTemplate)) {
                        $content = Get-Content -LiteralPath $srcPath -Raw

                        # Eval script expression delimiters
                        if ($content -and ($content.Count -gt 0)) {
                            $newContent = [regex]::Replace($content, '(<%=)(.*?)(%>)', {
                                param($match)
                                $expr = $match.groups[2].value
                                $res = EvaluateExpression $expr "templateFile '$srcRelPath'"
                                $PSCmdlet.WriteDebug("Replacing '$expr' with '$res' in contents of template file '$srcPath'")
                                $res
                            },  @('IgnoreCase'))

                            # Eval script block delimiters
                            $newContent = [regex]::Replace($newContent, '(^<%)(.*?)(^%>)', {
                                param($match)
                                $expr = $match.groups[2].value
                                $res = EvaluateScript $expr "templateFile '$srcRelPath'"
                                $res = $res -join [System.Environment]::NewLine
                                $PSCmdlet.WriteDebug("Replacing '$expr' with '$res' in contents of template file '$srcPath'")
                                $res
                            },  @('IgnoreCase', 'SingleLine', 'MultiLine'))

                            $srcPath = $tempFile = [System.IO.Path]::GetTempFileName()
                            $PSCmdlet.WriteDebug("Created temp file for expanded templateFile - $tempFile")

                            WriteContentWithEncoding -Path $tempFile -Content $newContent -Encoding $encoding
                        }
                        else {
                            $PSCmdlet.WriteDebug("Skipping template file expansion for $($Node.localName) '$srcPath', file is empty.")
                        }
                    }

                    CopyFileWithConflictDetection $srcPath $dstPath

                    if ($PassThru -and ($Node.openInEditor -eq 'true')) {
                        $InvokePlasterInfo.OpenFiles += $dstPath
                    }
                }
                finally {
                    if ($tempFile -and (Test-Path $tempFile)) {
                        Remove-Item -LiteralPath $tempFile
                        $PSCmdlet.WriteDebug("Removed temp file for expanded templateFile - $tempFile")
                    }
                }
            }
        }

        function ProcessModifyFile([ValidateNotNull()]$Node) {
            $path = InterpolateAttributeValue $Node.path (GetErrorLocationModifyAttrVal path)

            # We could choose to not check this if the condition eval'd to false
            # but I think it is better to let the template author know they've broken the
            # rules for any of the file directives (not just the ones they're testing/enabled).
            if ([System.IO.Path]::IsPathRooted($path)) {
                throw ($LocalizedData.ErrorPathMustBeRelativePath_F2 -f $path,$Node.LocalName)
            }

            $filePath = $PSCmdlet.GetUnresolvedProviderPathFromPSPath((Join-Path $DestinationPath $path))

            # The file's path must be under the DestinationPath specified by the user.
            VerifyPathIsUnderDestinationPath $filePath

            $condition = $Node.condition
            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)>'")) {
                $PSCmdlet.WriteDebug("Skipping $($Node.LocalName) of '$filePath', condition evaluated to false.")
                return
            }

            $fileContent = [string]::Empty
            if (Test-Path -LiteralPath $filePath) {
                $fileContent = Get-Content -LiteralPath $filePath -Raw
            }

            # Set a Plaster (non-parameter) variable in this and the constrained runspace.
            SetPlasterVariable -Name FileContent -Value $fileContent -IsParam $false

            $encoding = $Node.encoding
            if (!$encoding) {
                $encoding = $DefaultEncoding
            }

            # If processing a <modify> directive, write the modified contents to a temp file,
            # then apply the normal file conflict detection/resolution handling.
            $target = $LocalizedData.TempFileTarget_F1 -f $filePath
            if ($PSCmdlet.ShouldProcess($target, $LocalizedData.OpModify)) {
                WriteOperationStatus $LocalizedData.OpModify ($LocalizedData.TempFileOperation_F1 -f (ConvertToDestinationRelativePath $filePath))

                $modified = $false

                foreach ($childNode in $Node.ChildNodes) {
                    if ($childNode -isnot [System.Xml.XmlElement]) { continue }

                    switch ($childNode.LocalName) {
                        'replace' {
                            $condition  = $childNode.condition
                            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)><$($childNode.LocalName)>'")) {
                                $PSCmdlet.WriteDebug("Skipping $($Node.LocalName) $($childNode.LocalName) of '$filePath', condition evaluated to false.")
                                continue
                            }

                            if ($childNode.original -is [string]) {
                                $original = $childNode.original
                            }
                            else {
                                $original = $childNode.original.InnerText
                            }

                            if ($childNode.original.expand -eq 'true') {
                                $original = InterpolateAttributeValue $original (GetErrorLocationModifyAttrVal original)
                            }

                            if ($childNode.substitute -is [string]) {
                                $substitute = $childNode.substitute
                            }
                            else {
                                $substitute = $childNode.substitute.InnerText
                            }

                            if ($childNode.substitute.expand -eq 'true') {
                                $substitute = InterpolateAttributeValue $substitute (GetErrorLocationModifyAttrVal substitute)
                            }

                            $fileContent = $fileContent -replace $original,$substitute

                            # Update the Plaster (non-parameter) variable's value in this and the constrained runspace.
                            SetPlasterVariable -Name FileContent -Value $fileContent -IsParam $false

                            $modified = $true
                        }
                        default { throw ($LocalizedData.UnrecognizedContentElement_F1 -f $childNode.LocalName) }
                    }
                }

                $tempFile = $null

                try {
                    # We could use CopyFileWithConflictDetection to handle the "identical" (not modified) case
                    # but if nothing was changed, I'd prefer not to generate a temp file, copy the unmodified contents
                    # into that temp file with hopefully the right encoding and then potentially overwrite the original file
                    # (different encoding will make the files look different) with the same contents but different encoding.
                    # If the intent of the <modify> was simply to change an existing file's encoding then the directive will
                    # need to make a whitespace change to the file.
                    if ($modified) {
                        $tempFile = [System.IO.Path]::GetTempFileName()
                        $PSCmdlet.WriteDebug("Created temp file for modified file - $tempFile")

                        WriteContentWithEncoding -Path $tempFile -Content $PLASTER_FileContent -Encoding $encoding
                        CopyFileWithConflictDetection $tempFile $filePath

                        if ($PassThru -and ($Node.openInEditor -eq 'true')) {
                            $InvokePlasterInfo.OpenFiles += $filePath
                        }
                    }
                    else {
                        WriteOperationStatus $LocalizedData.OpIdentical (ConvertToDestinationRelativePath $filePath)
                    }
                }
                finally {
                    if ($tempFile -and (Test-Path $tempFile)) {
                        Remove-Item -LiteralPath $tempFile
                        $PSCmdlet.WriteDebug("Removed temp file for modified file - $tempFile")
                    }
                }
            }
        }

        function ProcessRequireModule([ValidateNotNull()]$Node) {
            $name = $Node.name

            $condition = $Node.condition
            if ($condition -and !(EvaluateConditionAttribute $condition "'<$($Node.LocalName)>'")) {
                $PSCmdlet.WriteDebug("Skipping $($Node.localName) for module '$name', condition evaluated to false.")
                return
            }

            $message = InterpolateAttributeValue $Node.message (GetErrorLocationRequireModuleAttrVal $name message)
            $minimumVersion = $Node.minimumVersion
            $maximumVersion = $Node.maximumVersion
            $requiredVersion = $Node.requiredVersion

            $getModuleParams = @{
                ListAvailable = $true
                ErrorAction = 'SilentlyContinue'
            }

            # Configure $getModuleParams with correct parameters based on parameterset to be used.
            # Also construct an array of version strings that can be displayed to the user.
            $versionInfo = @()
            if ($requiredVersion) {
                $getModuleParams["FullyQualifiedName"] = @{ModuleName = $name; RequiredVersion = $requiredVersion}
                $versionInfo += $LocalizedData.RequireModuleRequiredVersion_F1 -f $requiredVersion
            }
            elseif ($minimumVersion -or $maximumVersion) {
                $getModuleParams["FullyQualifiedName"] = @{ModuleName = $name}

                if ($minimumVersion) {
                    $getModuleParams.FullyQualifiedName["ModuleVersion"] = $minimumVersion
                    $versionInfo += $LocalizedData.RequireModuleMinVersion_F1 -f $minimumVersion
                }
                if ($maximumVersion) {
                    $getModuleParams.FullyQualifiedName["MaximumVersion"] = $maximumVersion
                    $versionInfo += $LocalizedData.RequireModuleMaxVersion_F1 -f $maximumVersion
                }
            }
            else {
                $getModuleParams["Name"] = $name
            }

            # Flatten array of version strings into a single string.
            $versionRequirements = ""
            if ($versionInfo.Length -gt 0) {
                $OFS = ", "
                $versionRequirements = " ($versionInfo)"
            }

            # PowerShell v3 Get-Module command does not have the FullyQualifiedName parameter.
            if ($PSVersionTable.PSVersion.Major -lt 4) {
                $getModuleParams.Remove("FullyQualifiedName")
                $getModuleParams["Name"] = $name
            }

            $module = Get-Module @getModuleParams

            $moduleDesc = if ($versionRequirements) { "${name}:$versionRequirements" } else { $name }

            if ($null -eq $module) {
                WriteOperationStatus $LocalizedData.OpMissing ($LocalizedData.RequireModuleMissing_F2 -f $name,$versionRequirements)
                if ($message) {
                    WriteOperationAdditionalStatus $message
                }
                if ($PassThru) {
                    $InvokePlasterInfo.MissingModules += $moduleDesc
                }
            }
            else {
                if ($PSVersionTable.PSVersion.Major -gt 3) {
                    WriteOperationStatus $LocalizedData.OpVerify ($LocalizedData.RequireModuleVerified_F2 -f $name,$versionRequirements)
                }
                else {
                    # On V3, we have to the version matching with the results that Get-Module return.
                    $installedVersion = $module | Sort-Object Version -Descending | Select-Object -First 1 | Foreach-Object Version
                    if ($installedVersion.Build -eq -1) {
                        $installedVersion = [System.Version]"${installedVersion}.0.0"
                    }
                    elseif ($installedVersion.Revision -eq -1) {
                        $installedVersion = [System.Version]"${installedVersion}.0"
                    }

                    if (($requiredVersion -and ($installedVersion -ne $requiredVersion)) -or
                        ($minimumVersion -and ($installedVersion -lt $minimumVersion)) -or
                        ($maximumVersion -and ($installedVersion -gt $maximumVersion))) {

                        WriteOperationStatus $LocalizedData.OpMissing ($LocalizedData.RequireModuleMissing_F2 -f $name,$versionRequirements)
                        if ($PassThru) {
                            $InvokePlasterInfo.MissingModules += $moduleDesc
                        }
                    }
                    else {
                        WriteOperationStatus $LocalizedData.OpVerify ($LocalizedData.RequireModuleVerified_F2 -f $name,$versionRequirements)
                    }
                }
            }
        }
    }

    end {
        try {
            # Process parameters
            foreach ($node in $manifest.plasterManifest.parameters.ChildNodes) {
                if ($node -isnot [System.Xml.XmlElement]) { continue }
                switch ($node.LocalName) {
                    'parameter'  { ProcessParameter $node }
                    default      { throw ($LocalizedData.UnrecognizedParametersElement_F1 -f $node.LocalName) }
                }
            }

            # Outputs the processed template parameters to the debug stream
            $parameters = Get-Variable -Name PLASTER_* | Out-String
            $PSCmdlet.WriteDebug("Parameter values are:`n$($parameters -split "`n")")

            # Stores any updated default values back to the store file.
            if ($flags.DefaultValueStoreDirty) {
                $directory = Split-Path $defaultValueStorePath -Parent
                if (!(Test-Path $directory)) {
                    $PSCmdlet.WriteDebug("Creating directory for template's DefaultValueStore '$directory'.")
                    New-Item $directory -ItemType Directory > $null
                }

                $PSCmdlet.WriteDebug("DefaultValueStore is dirty, saving updated values to '$defaultValueStorePath'.")
                $defaultValueStore | Export-Clixml -LiteralPath $defaultValueStorePath
            }

            # Output the DestinationPath
            $LocalizedData.DestPath_F1 -f $destinationAbsolutePath

            # Process content
            foreach ($node in $manifest.plasterManifest.content.ChildNodes) {
                if ($node -isnot [System.Xml.XmlElement]) { continue }

                switch -Regex ($node.LocalName) {
                    'file|templateFile' { ProcessFile $node; break }
                    'message'           { ProcessMessage $node; break }
                    'modify'            { ProcessModifyFile $node; break }
                    'newModuleManifest' { ProcessNewModuleManifest $node; break }
                    'requireModule'     { ProcessRequireModule $node; break }
                    default             { throw ($LocalizedData.UnrecognizedContentElement_F1 -f $node.LocalName) }
                }
            }

            if ($PassThru) {
                $InvokePlasterInfo.Success = $true
                $InvokePlasterInfo
            }
        }
        finally {
            # Dispose of the ConstrainedRunspace.
            if ($constrainedRunspace) {
                $constrainedRunspace.Dispose()
                $constrainedRunspace = $null
            }
        }
    }
}

###############################################################################
# Helper functions
###############################################################################

function InitializePredefinedVariables([string]$TemplatePath, [string]$DestPath) {
    # Always set these variables, even if the command has been run with -WhatIf
    $WhatIfPreference = $false

    Set-Variable -Name PLASTER_TemplatePath -Value $TemplatePath.TrimEnd('\','/') -Scope Script

    $destName = Split-Path -Path $DestPath -Leaf
    Set-Variable -Name PLASTER_DestinationPath -Value $DestPath.TrimEnd('\','/') -Scope Script
    Set-Variable -Name PLASTER_DestinationName -Value $destName -Scope Script
    Set-Variable -Name PLASTER_DirSepChar      -Value ([System.IO.Path]::DirectorySeparatorChar) -Scope Script
    Set-Variable -Name PLASTER_HostName        -Value $Host.Name -Scope Script
    Set-Variable -Name PLASTER_Version         -Value $MyInvocation.MyCommand.Module.Version -Scope Script

    Set-Variable -Name PLASTER_Guid1 -Value ([Guid]::NewGuid()) -Scope Script
    Set-Variable -Name PLASTER_Guid2 -Value ([Guid]::NewGuid()) -Scope Script
    Set-Variable -Name PLASTER_Guid3 -Value ([Guid]::NewGuid()) -Scope Script
    Set-Variable -Name PLASTER_Guid4 -Value ([Guid]::NewGuid()) -Scope Script
    Set-Variable -Name PLASTER_Guid5 -Value ([Guid]::NewGuid()) -Scope Script

    $now = [DateTime]::Now
    Set-Variable -Name PLASTER_Date -Value ($now.ToShortDateString()) -Scope Script
    Set-Variable -Name PLASTER_Time -Value ($now.ToShortTimeString()) -Scope Script
    Set-Variable -Name PLASTER_Year -Value ($now.Year) -Scope Script
}

function GetPlasterManifestPathForCulture([string]$TemplatePath, [ValidateNotNull()][CultureInfo]$Culture) {
    if (![System.IO.Path]::IsPathRooted($TemplatePath)) {
        $TemplatePath = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($TemplatePath)
    }

    # Check for culture-locale first.
    $plasterManifestBasename = "plasterManifest"
    $plasterManifestFilename = "${plasterManifestBasename}_$($culture.Name).xml"
    $plasterManifestPath = Join-Path $TemplatePath $plasterManifestFilename
    if (Test-Path $plasterManifestPath) {
        return $plasterManifestPath
    }

    # Check for culture next.
    if ($culture.Parent.Name) {
        $plasterManifestFilename = "${plasterManifestBasename}_$($culture.Parent.Name).xml"
        $plasterManifestPath = Join-Path $TemplatePath $plasterManifestFilename
        if (Test-Path $plasterManifestPath) {
            return $plasterManifestPath
        }
    }

    # Fallback to invariant culture manifest.
    $plasterManifestPath = Join-Path $TemplatePath "${plasterManifestBasename}.xml"
    if (Test-Path $plasterManifestPath) {
        return $plasterManifestPath
    }

    $null
}

# SIG # Begin signature block
# MIIdhQYJKoZIhvcNAQcCoIIddjCCHXICAQExCzAJBgUrDgMCGgUAMGkGCisGAQQB
# gjcCAQSgWzBZMDQGCisGAQQBgjcCAR4wJgIDAQAABBAfzDtgWUsITrck0sYpfvNR
# AgEAAgEAAgEAAgEAAgEAMCEwCQYFKw4DAhoFAAQUFPNLfhcKbWWCYo/iVy9EFbr/
# MaugghhTMIIEwjCCA6qgAwIBAgITMwAAAL+RbPt8GiTgIgAAAAAAvzANBgkqhkiG
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
# MRYEFE/BZX6PFzVZ9cRUIGOw8PutEDY5MFAGCisGAQQBgjcCAQwxQjBAoBaAFABQ
# AG8AdwBlAHIAUwBoAGUAbABsoSaAJGh0dHA6Ly93d3cubWljcm9zb2Z0LmNvbS9Q
# b3dlclNoZWxsIDANBgkqhkiG9w0BAQEFAASCAQCu+NADzkbPCrYLvMxE4oy/HbuN
# osvdujYQlwrfQLFAV6WBbMSnYcvcxkx+wlC+Y9ERlGCL4XM4LREkmZymvm8cbI0i
# Dc7rGKzKLBANjor70xG3c53qHQX3beMnAe6fu9h6WKYSmwr9TstAd+IPw6sZvxFe
# grdSpfJGuMR94OjzcYzx5wTeQ1Yyh1F3n6lsSTkNtAieS5NnqRdHy3hazGYnNZxB
# IvJzKnA5rbfFr9ypBnAN1DPSZyynqZCUCNB0+//AyD9VfhQrz2kucFLu/NURP3uV
# uUwLHhQOz2+bu34oQ4Yq3wL2Vr4jTGBgjzlqaKdsV/ihiaefEkr4WEx3i8W1oYIC
# KDCCAiQGCSqGSIb3DQEJBjGCAhUwggIRAgEBMIGOMHcxCzAJBgNVBAYTAlVTMRMw
# EQYDVQQIEwpXYXNoaW5ndG9uMRAwDgYDVQQHEwdSZWRtb25kMR4wHAYDVQQKExVN
# aWNyb3NvZnQgQ29ycG9yYXRpb24xITAfBgNVBAMTGE1pY3Jvc29mdCBUaW1lLVN0
# YW1wIFBDQQITMwAAAL+RbPt8GiTgIgAAAAAAvzAJBgUrDgMCGgUAoF0wGAYJKoZI
# hvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMTcxMDI3MTI0NzU0
# WjAjBgkqhkiG9w0BCQQxFgQUJ/0ANYONx78ivzVMAWNwZmUbrukwDQYJKoZIhvcN
# AQEFBQAEggEASfs/V5p27iL14Cyx2hlfZ2Comx6MRcS8S7pllfVAk9pI1FtdlH4Y
# wSSvJxj2lvfYWcG3MyGdZFTGqVxqBfHM45E4k9YjiWOdoTFJfaSzVQnI7RLFSPfl
# gq6Kv6r7IRiREExJ3K/lmtgp0ZZu3PibI6HKc2OJevE4a7nqggGbzAtq3w+Y4xmf
# nKJleXdxFkpJ+dsKdsVEFqcSt/Yr5SRCznDNy1KhObmjtwJbFws6cR2cpwM7d6qQ
# douRv4H+BHUCYnpeOZl3aMGu/m7mPPMH7ovNpXUcJf59t6jBO60eO8jSIHgN91Du
# VpQKlqsX7sbJ/KNueAIjhaYdlUrRlWcv2g==
# SIG # End signature block
