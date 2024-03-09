[CmdLetBinding()]
param (
[PSParameter(Mandatory = $true)] $foo
)
$bar = $baz

function foo
{
[CmdLetBinding()]
param (
)
}
