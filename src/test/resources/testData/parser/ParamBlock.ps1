function Test-SympaMailingListMember
{

<#
.Synopsis
   This function returns True or False against a member of a list in relation to their function/role within that list (subscriber/owner/editor).
.EXAMPLE
   Test-SympaMailingListMember -Sympa $Sympa -MailingList queens-it -Member jim.bob@queens.ox.ac.uk -Function subscriber
#>

param(

    [Parameter(Mandatory=$true,HelpMessage="Pass in the result of the 'Get-SympaLogin' function")]
    $Sympa,

    [Parameter(Mandatory=$true,HelpMessage="Enter the name of the Mailing list(s) you want to query the member against")]
    [Array]$MailingList,

    [Parameter(Mandatory=$true,HelpMessage="Enter the address of the member(s) you want to test")]
    [Array]$Member,

    [Parameter()]
    [int]
    $Properties = 111+ 2,
    [Parameter()]
    [string]
    $Property = "Hello",

    [Parameter(Mandatory=$true,HelpMessage="Set the ")]
    [ValidateSet("subscriber", "owner", "editor")]
    [String]$Function

    )

    [bool]$Results = $Sympa.amI("$MailingList","$Function","$Member")

    #Return the result
    Return [bool]$Results
}