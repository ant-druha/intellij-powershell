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

$v3 = &{						# get a scriptblock with $startValue of 200
         param ([int]$startValue = 1)
         $nextID = $startValue
         {
           ($script:nextID++)
         }.GetNewClosure()
       } 200
&$v3							# invoke script getting back 200
&$v3							# invoke script getting back 201