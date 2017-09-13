#labeled statement
:go_here while ($j -le 100)
         {
         # …
         }
:labelA
for ($i = 1; $i -le 5; ++$i)
{
  :labelB
  for ($j = 1; $j -le 3; ++$j)
  {
    :labelC
    for ($k = 1; $k -le 2; ++$k)
    {
    # …
    }
  }
}

: with_space while ($i -le 100) {}