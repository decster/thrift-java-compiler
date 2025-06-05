namespace java com.example.thrift

struct Point {
  1: required double xx,
  2: optional map<string, i32> yy
}