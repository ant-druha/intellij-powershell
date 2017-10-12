$h1 = @{FirstName = "James"; LastName = "Anderson"; IDNum = 123}
$h2 = $h1
$h1.FirstName = "John"      # change key's value in $h1
$h2.FirstName            # change is reflected in $h2

#removal
$h1 = @{FirstName = "James"; LastName = "Anderson"; IDNum = 123}
$h1.Dept = "Finance"          # adds element Finance
$h1["Salaried"] = $false      # adds element Salaried
$h1.Remove("Salaried")        # removes element Salaried

#enumerating
$h1 = @{ FirstName = "James"; LastName = "Anderson"; IDNum = 123 }
foreach ($e in $h1.Keys)
{
  "Key is " + $e + ", Value is " + $h1[$e]
}

$getTargetResourceResult = @{
                           Name = $Website.Name;
                           Ensure = $ensureResult;
                           PhysicalPath = $Website.physicalPath;
                           State = $Website.state;
                           ID = $Website.id;
                           ApplicationPool = $Website.applicationPool;
                           Protocol = $Website.bindings.Collection.protocol;
                           Binding = $Website.bindings.Collection.bindingInformation;
                         }