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