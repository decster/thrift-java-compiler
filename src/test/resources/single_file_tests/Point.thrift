namespace java com.example.thrift

struct Point {
  1: required double xx,
  2: optional map<string, i32> yy
  3: optional list<i64> zz
  4: required set<binary> aa,
  5: optional bool bb,
  6: optional string cc,
}