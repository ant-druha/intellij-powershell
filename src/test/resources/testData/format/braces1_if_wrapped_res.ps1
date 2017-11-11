function test {
    foreach ($i in get-childitem |
            sort-object length)
    {
        $i  #highlight
        # comment here
        Write-Object (($j -gt 5) -and ($i -lt 15))
    }
}