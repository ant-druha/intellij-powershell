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