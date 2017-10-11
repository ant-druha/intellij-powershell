class A: B, C {}

class Person {
  [int]$Age
  [int]$Age
  Person($oo) {}
  Person([int]$a)
  {
    $this.Age = $a
  }
}

#constructor override
class Child: Person
{
  [string]$School
  Child([int]$a, [string]$s): base($a) {
    $this.School = $s
  }
}
[Child]$littleone = [Child]::new(10, "Silver Fir Elementary School")
$littleone.Age

#methods override
class BaseClass
{
  [int]days() {return 1}
}
class ChildClass1 : BaseClass
{
  [int]days () {return 2}
}
[ChildClass1]::new().days()

#calling the base method
class BaseClass
{
  [int]days() {return 1}
}
class ChildClass1 : BaseClass
{
  [int]days () {return 2}
  [int]basedays() {return ([BaseClass]$this).days()}
}

[ChildClass1]::new().days()
[ChildClass1]::new().basedays()

#methods
class Device {
  [string]$VendorSku
  #property validation
  [ValidateNotNullOrEmpty()][string]$Brand
  [ValidateNotNullOrEmpty()][string]$Model

  [string]ToString(){
    return ("{0}|{1}|{2}" -f $this.Brand, $this.Model, $this.VendorSku)
  }
}

class Rack {
  [int] hidden $Slots = 8
  static [Rack[]]$InstalledRacks = @()
  [string]$Brand
  [string]$Model
  [string]$VendorSku
  [string]$AssetId
  [Device[]]$Devices = [Device[]]::new($this.Slots)

  [void] AddDevice([Device]$dev, [int]$slot){
  ## Add argument validation logic here
    $this.Devices[$slot] = $dev
  }

  [void]RemoveDevice([int]$slot){
  ## Add argument validation logic here
    $this.Devices[$slot] = $null
  }

  [int[]] GetAvailableSlots(){
    [int]$i = 0
    return @($this.Devices.foreach{ if($_ -eq $null){$i}; $i++})
  }
}

$rack = [Rack]::new()

$surface = [Device]::new()
$surface.Brand = "Microsoft"
$surface.Model = "Surface Pro 4"
$surface.VendorSku = "5072641000"

$rack.AddDevice($surface, 2)

$rack
$rack.GetAvailableSlots()

#class attribute
<#
         This resource manages the file in a specific path.
         [DscResource()] indicates the class is a DSC resource
#>
[DscResource()]
class FileResource
{
<#
             This property is the fully qualified path to the file that is
             expected to be present or absent.

             The [DscProperty(Key)] attribute indicates the property is a
             key and its value uniquely identifies a resource instance.
             Defining this attribute also means the property is required
             and DSC will ensure a value is set before calling the resource.

             A DSC resource must define at least one key property.
#>

  [DscProperty(Key)]
  [string]$Path
<#
              This property indicates if the settings should be present or absent
              on the system. For present, the resource ensures the file pointed
              to by $Path exists. For absent, it ensures the file point to by
              $Path does not exist.

              The [DscProperty(Mandatory)] attribute indicates the property is
              required and DSC will guarantee it is set.

              If Mandatory is not specified or if it is defined as
              Mandatory=$false, the value is not guaranteed to be set when DSC
              calls the resource.  This is appropriate for optional properties.
#>
  [DscProperty(Mandatory)]
  [Ensure] $Ensure
<#
              This method is equivalent of the Set-TargetResource script function.
              It sets the resource to the desired state.
#>
  [void] Set()
  {
    $fileExists = $this.TestFilePath($this.Path)
    if ($this.ensure -eq [Ensure]::Present)
    {
      if (-not $fileExists)
      {
        $this.CopyFile()
      }
    }
    else
    {
      if ($fileExists)
      {
        Write-Verbose -Message "Deleting the file $($this.Path)"
        Remove-Item -LiteralPath $this.Path -Force
      }
    }
  }

<#
              This method is equivalent of the Test-TargetResource script function.
              It should return True or False, showing whether the resource
              is in a desired state.
#>
  [bool] Test()
  {
    $present = $this.TestFilePath($this.Path)

    if ($this.Ensure -eq [Ensure]::Present)
    {
      return $present
    }
    else
    {
      return -not $present
    }
  }

<#
              This method is equivalent of the Get-TargetResource script function.
              The implementation should use the keys to find appropriate resources.
              This method returns an instance of this class with the updated key
               properties.
#>
  [FileResource] Get()
  {
    $present = $this.TestFilePath($this.Path)

    if ($present)
    {
      $file = Get-ChildItem -LiteralPath $this.Path
      $this.CreationTime = $file.CreationTime
      $this.Ensure = [Ensure]::Present
    }
    else
    {
      $this.CreationTime = $null
      $this.Ensure = [Ensure]::Absent
    }
    return $this
  }
}