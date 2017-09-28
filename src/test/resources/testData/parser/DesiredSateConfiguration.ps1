Configuration MyDscConfiguration {
  Node "TEST-PC1" {
    WindowsFeature MyFeatureInstance {
      Ensure = "Present"
      Name =  "RSAT"
    }
    WindowsFeature My2ndFeatureInstance {
      Ensure = "Present"
      Name = "Bitlocker"
    }
  }
}

Configuration MyDscConfiguration {
  param (
  [string[]]$ComputerName="localhost"
  )
  Node $ComputerName {
    WindowsFeature MyFeatureInstance {
      Ensure = "Present"
      Name =  "RSAT"
    }
    WindowsFeature My2ndFeatureInstance {
      Ensure = "Present"
      Name = "Bitlocker"
    }
  }
}

Configuration DependsOnExample {
  Node Test-PC1 {
    Group GroupExample {
      Ensure = "Present"
      GroupName = "TestGroup"
    }

    User UserExample {
      Ensure = "Present"
      UserName = "TestUser"
      FullName = "TestUser"
      DependsOn = "[Group]GroupExample"
    }
  }
}

# A simple example of using credentials
configuration CredentialEncryptionExample
{
  param(
  [Parameter(Mandatory=$true)]
  [ValidateNotNullorEmpty()]
  [PsCredential] $credential
  )

  Node $AllNodes.NodeName
  {
    File exampleFile
    {
      SourcePath = "\\server\share\file.txt"
      DestinationPath = "C:\Users\user"
      Credential = $credential
    }

    LocalConfigurationManager
    {
      CertificateId = $node.Thumbprint
    }
  }
}

#https://powershell.org/2017/09/26/using-azure-desired-state-configuration-part-ii/
configuration AzureDesiredStateExample {
  $AdminCreds = Get-AutomationPSCredential -Name $AdminName

  Node ($AllNodes.Where{$_.Role -eq "WebServer"}).NodeName
  {
    JoinDomain DomainJoin
    {
      DependsOn = "[WindowsFeature]RemoveUI"
      DomainName = $DomainName
      Admincreds = $Admincreds
      RetryCount = 20
      RetryIntervalSec = 60
    }
  }
}

configuration DNSServer
{
    Import-DscResource -module 'xDnsServer','xNetworking', 'PSDesiredStateConfiguration'

    Node $AllNodes.Where{$_.Role -eq 'DNSServer'}.NodeName {

      WindowsFeature DNS
        {
            Ensure  = 'Present'
            Name    = 'DNS'
        }

        xDnsServerPrimaryZone $Node.zone
        {
            Ensure    = 'Present'
            Name      = $Node.Zone
            DependsOn = '[WindowsFeature]DNS'
        }

        foreach ($ARec in $Node.ARecords.keys) {
            xDnsRecord $ARec
            {
                Ensure    = 'Present'
                Name      = $ARec
                Zone      = $Node.Zone
                Type      = 'ARecord'
                Target    = $Node.ARecords[$ARec]
                DependsOn = '[WindowsFeature]DNS'
            }
        }

        foreach ($CName in $Node.CNameRecords.keys) {
            xDnsRecord $CName
            {
                Ensure    = 'Present'
                Name      = $CName
                Zone      = $Node.Zone
                Type      = 'CName'
                Target    = $Node.CNameRecords[$CName]
                DependsOn = '[WindowsFeature]DNS'
            }
        }
    }
}