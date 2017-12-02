<# 
    .DESCRIPTION 
        This will create a Public IP address for the failed over VM - only in test failover. 
         
        Pre-requisites 
        1. when you create a new Automation Account, make sure you have chosen to create a run-as account with it. 
        2. If you create a run as account on your own, give the Connection Name in the variable - $connectionName 
                 
        What all you need to change in this script? 
        1. Give the name of the Automation account in the variable - $AutomationAccountName 
        2. Give the Resource Group name of the Automation Account in $AutomationAccountRg 
         
        Do you want to add a NSG to the failed over VM? If yes, follow the below steps - you can skip this step if you dont want to add an NSG. 
        1. Create the NSG that you want to apply 
        2. Create a new Azure automation string variable <RecoveryPlanName>-NSG (example testrp-NSG). Save it with the value of the NSG you want to use. 
        3. Create a new Azure automation string variable <RecoveryPlanName>-NSGRG (example testrp-NSGRG). Save it with the value of the NSG's Resource group you want to use. 
         
        How to add the script? 
        Add this script as a post action in boot up group for which you need a public IP. All the VMs in the group will get a public IP assigned. 
        If the NSG parameters are specified, all the VM's NICs will get the same NSG attached. 
         
        Clean up test failover behavior 
        Clean up test failover will not delete the IP address. You will need to delete the IP address manually 
 
 
    .NOTES 
        AUTHOR: RuturajD@microsoft.com 
        LASTEDIT: 27 January, 2017 
#>

#comment
command#notcomment
command #comment