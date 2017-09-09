& "Script1.ps1"
& $myVar
&{
  $i = 1
}
& {
  $i = 1
}

#dot notation
. "Script2.ps1"
. {
  $i = 1
}
. FunctionA

& mycmd.exe