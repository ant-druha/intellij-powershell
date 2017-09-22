function Convert-CentigradeToFahrenheit ([double]$tempC)
{
  return ($tempC * (9.0/5.0)) + 32.0
}
New-Alias c2f Convert-CentigradeToFahrenheit
function Convert-FahrenheitToCentigrade ([double]$tempF)
{
  return ($tempF - 32.0) * (5.0/9.0)
}
New-Alias f2c Convert-FahrenheitToCentigrade
Export-ModuleMember -Function Convert-CentigradeToFahrenheit
Export-ModuleMember -Function Convert-FahrenheitToCentigrade
Export-ModuleMember -Alias c2f, f2c

#Consider the following script that uses these functions and aliases defined in §11.2:
Import-Module "E:\Scripts\Modules\PSTest_Temperature" -Verbose

"0 degrees C is " + (Convert-CentigradeToFahrenheit 0) + " degrees F"
"100 degrees C is " + (c2f 100) + " degrees F"
"32 degrees F is " + (Convert-FahrenheitToCentigrade 32) + " degrees C"
"212 degrees F is " + (f2c 212) + " degrees C"

#invokes the function F in module M, and passes it the argument 100.
& M\F 100

#Windoes: In a script module, it is possible to specify code that is to be executed prior to that module's removal:
$MyInvocation.MyCommand.ScriptBlock.Module.OnRemove = {on-removal-code}

#example of the manifest file (.psd1 file extension):
@{
ModuleVersion = '1.0'
Author = 'John Doe'
RequiredModules = @()
FunctionsToExport = 'Set*', 'Get*', 'Process*'
}

#dynamic module import (in-memory)
$sb = {
        function Convert-CentigradeToFahrenheit ([double]$tempC)
        {
          return ($tempC * (9.0/5.0)) + 32.0
        }
        New-Alias c2f Convert-CentigradeToFahrenheit
        function Convert-FahrenheitToCentigrade ([double]$tempF)
        {
          return ($tempF - 32.0) * (5.0/9.0)
        }
        New-Alias f2c Convert-FahrenheitToCentigrade
        Export-ModuleMember -Function Convert-CentigradeToFahrenheit
        Export-ModuleMember -Function Convert-FahrenheitToCentigrade
        Export-ModuleMember -Alias c2f,f2c
      }
New-Module -Name MyDynMod -ScriptBlock $sb
Convert-CentigradeToFahrenheit 100
c2f 100

#closures
#Each time a new closure is created by GetNewClosure, a new dynamic module is created, and the variables
#in the caller‘s scope (in this case, the script block containing the increment) are copied into this new module.
function Get-NextID ([int]$startValue = 1)
{
  $nextID = $startValue
  {
    ($script:nextID++)
  }.GetNewClosure()
}
$v1 = Get-NextID			# get a scriptblock with $startValue of 0
&$v1							# invoke Get-NextID getting back 1
&$v1							# invoke Get-NextID getting back 1
$v2 = Get-NextID 100		# get a scriptblock with $startValue of 100
&$v2							# invoke Get-NextID getting back 100
&$v2							# invoke Get-NextID getting back 101
#
$v3 = &{						# get a scriptblock with $startValue of 200
         param ([int]$startValue = 1)
         $nextID = $startValue
         {
           ($script:nextID++)
         }.GetNewClosure()
       } 200
&$v3							# invoke script getting back 200
&$v3							# invoke script getting back 201