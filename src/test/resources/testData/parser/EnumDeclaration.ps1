[Flags()] enum FileAttributes {
  Archive = 1
  Compressed = 2
  Device = 4
  Directory = 8
  Encrypted = 16
  Hidden = 32
}

[FileAttributes]$file1 = [FileAttributes]::Archive + [FileAttributes]::Compressed + [FileAttributes]::Device
"file1 attributes are: $file1"

[FileAttributes]$file2 = [FileAttributes]28 ## => 16 + 8 + 4
"file2 attributes are: $file2"

enum MediaTypes {
  unknown
  music = 10
  mp3
  aac
  ogg = 15
  oga = 15
  mogg = 15
  picture = 20
  jpg
  jpeg = 21
  png
  video = 40
  mpg
  mpeg = 41
  avi
  m4v
}
$dsd[1]=1
$dsd[$s]